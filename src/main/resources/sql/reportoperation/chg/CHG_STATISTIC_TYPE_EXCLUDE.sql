WITH _f AS (
  SELECT
    f.FormId,
    f.FormStatus,
    f.CreateTime,
    IIF(
      CHARINDEX('-', DivisionCreated) = 0,
      '',
      SUBSTRING(
        DivisionCreated,
        1,
        CHARINDEX('-', DivisionCreated) - 1
      )
    ) division,
    IIF(
      CHARINDEX('-', f.DivisionCreated) = 0,
      '',
      SUBSTRING(
        f.DivisionCreated,
        CHARINDEX('-', f.DivisionCreated) + 1,
        LEN(f.DivisionCreated) - CHARINDEX('-', f.DivisionCreated)
      )
    ) section
  FROM
    form f
  WHERE
    formClass = 'CHG'
    AND NOT EXISTS (
      SELECT
        1
      FROM
        form
      WHERE
        f.FormId = FormId
        AND FormStatus = 'PROPOSING'
    )
),
_f2 AS (
  SELECT
    *
  FROM
    _f f1
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        _f
      WHERE
        f1.FormId = FormId
        AND FormStatus = 'DEPRECATED'
    )
),
_form AS (
  SELECT
    fo.formid,
    fo.[section],
    icd.Standard,
    icd.IsUrgent
  FROM
    (
      SELECT
        f2.FormId,
        f2.section
      FROM
        _f2 f2
      WHERE
        f2.CreateTime BETWEEN :startDate
        AND :endDate
 AND  NOT EXISTS ( SELECT 1 FROM _f2 WHERE f2.formId = formId AND division = 'A01421' ) 
		${CONDITIONS}
    ) fo
    INNER JOIN (
      SELECT
        FormId,
        Standard,
        IsUrgent
      FROM
        FORM_INFO_CHG_DETAIL
    ) icd ON fo.FormId = icd.FormId
),
_general AS (
  SELECT
    *
  FROM
    _form
  WHERE
    Standard = ''
),
_standard AS (
  SELECT
    *
  FROM
    _form f
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        _general
      WHERE
        f.FormId = FormId
    )
),
_result AS (
  SELECT
    section,
    'generalYes' AS type,
    1 AS times
  FROM
    _general
  WHERE
    IsUrgent = 'Y'
  UNION ALL
  SELECT
    section,
    'generalNo' AS type,
    1 AS times
  FROM
    _general g
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        _general
      WHERE
        g.FormId = FormId
        AND IsUrgent = 'Y'
    )
  UNION ALL
  SELECT
    section,
    'standardYes' AS type,
    1 AS times
  FROM
    _standard
  WHERE
    IsUrgent = 'Y'
  UNION ALL
  SELECT
    section,
    'standardNo' AS type,
    1 AS times
  FROM
    _standard g
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        _standard
      WHERE
        g.FormId = FormId
        AND IsUrgent = 'Y'
    )
),
_resultTotal AS (
  SELECT
    'total' AS section,
    'generalYes' AS type,
    COUNT(*) AS times
  FROM
    _result
  WHERE
    type = 'generalYes'
  UNION ALL
  SELECT
    'total' AS section,
    'generalNo' AS type,
    COUNT(*) AS times
  FROM
    _result
  WHERE
    type = 'generalNo'
  UNION ALL
  SELECT
    'total' AS section,
    'standardYes' AS type,
    COUNT(*) AS times
  FROM
    _result
  WHERE
    type = 'standardYes'
  UNION ALL
  SELECT
    'total' AS section,
    'standardNo' AS type,
    COUNT(*) AS times
  FROM
    _result
  WHERE
    type = 'standardNo'
)
SELECT
  section,
  ISNULL([generalYes], 0) AS generalYes,
  ISNULL([generalNo], 0) AS generalNo,
  ISNULL([standardYes], 0) AS standardYes,
  ISNULL([standardNo], 0) AS standardNo
FROM
  _result AS p PIVOT (
    SUM(times) FOR type IN (
      [generalYes],
      [generalNo],
      [standardYes],
      [standardNo]
    )
  ) AS pt
UNION ALL
SELECT
  section,
  ISNULL([generalYes], 0) AS generalYes,
  ISNULL([generalNo], 0) AS generalNo,
  ISNULL([standardYes], 0) AS standardYes,
  ISNULL([standardNo], 0) AS standardNo
FROM
  _resultTotal AS p PIVOT (
    SUM(times) FOR type IN (
      [generalYes],
      [generalNo],
      [standardYes],
      [standardNo]
    )
  ) AS pt

WITH _f AS (
  SELECT
    f.formid,
    f.FormStatus,
    f.CreateTime,
    f.UpdatedAt,
    f.sourceId,
    f.UserSolving,
    IIF(
      CHARINDEX('-', DivisionSolving) = 0,
      '',
      SUBSTRING(
        DivisionCreated,
        1,
        CHARINDEX('-', DivisionSolving) - 1
      )
    ) division,
    IIF(
      CHARINDEX('-', f.DivisionSolving) = 0,
      '',
      SUBSTRING(
        f.DivisionCreated,
        CHARINDEX('-', f.DivisionSolving) + 1,
        LEN(f.DivisionSolving) - CHARINDEX('-', f.DivisionSolving)
      )
    ) section
  FROM
    form f
  WHERE
    f.Formclass = 'Q'
    AND NOT EXISTS (
      SELECT
        1
      FROM
        form
      WHERE
        FormId = f.FormId
        AND f.FormStatus = 'PROPOSING'
    )
),
_f2 AS (
  SELECT
    *
  FROM
    _f f
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        _f
      WHERE
        formid = f.formid
        AND f.FormStatus = 'DEPRECATED'
    )
),
_form AS (
  SELECT
    f.*,
    IIF(
      fid.ect IS NULL
      OR fid.ect = '',
      'N',
      IIF(
        CONVERT(varchar, fid.ect, 111) < CONVERT(varchar, fid.act, 111),
        'Y',
        'N'
      )
    ) AS delay,
    IIF(
      fid.ect IS NULL
      OR fid.ect = '',
      NULL,
      fid.ect
    ) AS ect,
    IIF(
      fid.act IS NULL
      OR fid.act = '',
      NULL,
      fid.act
    ) AS act,
    fid.IsSpecial,
    fid.SpecialEndCaseType
  FROM
    _f2 f
    INNER JOIN (
      SELECT
        FormId,
        ect,
        act,
        IsSpecial,
        SpecialEndCaseType
      FROM
        FORM_INFO_DATE
      WHERE
        formClass = 'Q'
        AND ect IS NOT NULL
    ) fid ON f.FormId = fid.FormId
  WHERE
    1 = 1
    AND CONVERT(varchar, fid.ect, 111) BETWEEN CONVERT(varchar, cast(:startDate as datetime), 111)
    AND CONVERT(varchar, cast(:endDate as datetime), 111)
	${CONDITIONS}
),
_closed AS (
  SELECT
    Formid,
    IsSpecial,
    SpecialEndCaseType
  FROM
    _form
  WHERE
    FormStatus = 'CLOSED'
),
_unfinished AS (
  SELECT
    *
  FROM
    _form f
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        _form
      WHERE
        f.Formid = FormId
        AND FormStatus = 'CLOSED'
    )
  UNION ALL
  SELECT
    *
  FROM
    _form
  WHERE
    FormStatus = 'CLOSED'
    AND delay = 'Y'
),
_result AS (
  SELECT
    '1' AS closeMethod,
    'total' AS num,
    count(*) AS times
  FROM
    _closed c
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        _closed
      WHERE
        c.Formid = Formid
        AND IsSpecial = 'Y'
    )
  UNION ALL
  SELECT
    '2' AS closeMethod,
    'total' AS num,
    count(*) AS times
  FROM
    _closed
  WHERE
    IsSpecial = 'Y'
    AND SpecialEndCaseType = 'S_END_2'
  UNION ALL
  SELECT
    '3' AS closeMethod,
    'total' AS num,
    count(*) AS times
  FROM
    _closed
  WHERE
    IsSpecial = 'Y'
    AND SpecialEndCaseType = 'S_END_1'
  UNION ALL
  SELECT
    '4' AS closeMethod,
    'total' AS num,
    COUNT(*) AS times
  FROM
    _unfinished
)
SELECT
  *
FROM
  _result AS p PIVOT (SUM(times) FOR num IN ([total])) AS pt

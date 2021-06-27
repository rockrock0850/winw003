WITH _f AS (
  SELECT
    f.formid,
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
    f.Formclass = 'CHG'
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
    *
  FROM
    _f2 f
  WHERE
    f.CreateTime BETWEEN :startDate
    AND :endDate 
 AND  NOT EXISTS ( SELECT 1 FROM _f2 WHERE f.FormId = FormId AND division = 'A01421' ) 
    ${CONDITIONS}
),
_FORM_IMPACT_ANALYSIS AS (
  SELECT
    ia.FormId,
    ia.TargetFraction
  FROM
    (
      SELECT
        FormId,
        IIF(
          TargetFraction IS NULL
          OR TargetFraction = '',
          '0',
          TargetFraction
        ) TargetFraction
      FROM
        FORM_IMPACT_ANALYSIS
    ) ia
  WHERE
    ia.TargetFraction NOT LIKE '%[^0-9]%'
),
defaultChange AS (
  SELECT
    *
  FROM
    (
      SELECT
        *
      FROM
        (
          SELECT
            *
          FROM
            _form f
          WHERE
            NOT EXISTS (
              SELECT
                1
              FROM
                FORM_INFO_CHG_DETAIL info
              WHERE
                info.IsNewSystem = 'Y'
                AND info.formId = f.formid
            )
        ) f1
      WHERE
        NOT EXISTS (
          SELECT
            1
          FROM
            FORM_INFO_CHG_DETAIL info
          WHERE
            info.IsNewService = 'Y'
            AND info.formId = f1.formid
        )
    ) f2
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        _FORM_IMPACT_ANALYSIS ia
      WHERE
        ia.FormId = f2.FormId
        AND ia.TargetFraction > 999
    )
),
importantChange AS (
  SELECT
    f.*,
    i.IsNewSystem
  FROM
    _form f
    LEFT JOIN FORM_INFO_CHG_DETAIL i ON f.formid = i.formid
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        defaultChange d
      WHERE
        f.FormId = d.FormId
    )
),
_result AS (
  SELECT
    SECTION,
    'secondary' AS change,
    1 AS times
  FROM
    defaultChange
  UNION ALL
  SELECT
    SECTION,
    'newSystem' AS change,
    1 AS times
  FROM
    importantChange i
  WHERE
    i.IsNewSystem = 'Y'
  UNION ALL
  SELECT
    SECTION,
    'notNewSystem' AS change,
    1 AS times
  FROM
    importantChange i
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        importantChange
      WHERE
        i.FormId = FormId
        AND IsNewSystem = 'Y'
    )
),
_resultTotal AS (
  SELECT
    'total' AS SECTION,
    'secondary' AS change,
    COUNT(*) AS times
  FROM
    _result
  WHERE
    change = 'secondary'
  UNION ALL
  SELECT
    'total' AS SECTION,
    'newSystem' AS change,
    COUNT(*) AS times
  FROM
    _result
  WHERE
    change = 'newSystem'
  UNION ALL
  SELECT
    'total' AS SECTION,
    'notNewSystem' AS change,
    COUNT(*) AS times
  FROM
    _result
  WHERE
    change = 'notNewSystem'
)
SELECT
  SECTION,
  ISNULL([secondary], 0) AS secondary,
  ISNULL([newSystem], 0) AS newSystem,
  ISNULL([notNewSystem], 0) AS notNewSystem
FROM
  _result AS p PIVOT (
    SUM(times) FOR change IN ([secondary], [newSystem], [notNewSystem])
  ) AS pt
UNION ALL
SELECT
  SECTION,
  ISNULL([secondary], 0) AS secondary,
  ISNULL([newSystem], 0) AS newSystem,
  ISNULL([notNewSystem], 0) AS notNewSystem
FROM
  _resultTotal AS p PIVOT (
    SUM(times) FOR change IN ([secondary], [newSystem], [notNewSystem])
  ) AS pt

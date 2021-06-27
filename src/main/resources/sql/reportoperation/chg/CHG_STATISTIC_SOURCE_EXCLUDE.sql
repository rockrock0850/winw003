WITH _f AS (
  SELECT
    f.FormId,
    f.SourceId,
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
    ) division
  FROM
    form f
  WHERE
    f.formClass = 'CHG'
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
_form AS (
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
        FormId = f.FormId
        AND FormStatus = 'DEPRECATED'
    )
),
_result AS (
  SELECT
    fc.FormClass,
    'times' AS t,
    1 AS counter
  FROM
    (
      SELECT
        SourceId
      FROM
        _form f1
      WHERE
        f1.CreateTime BETWEEN :startDate
        AND :endDate 
 AND  NOT EXISTS ( SELECT 1 FROM _form WHERE f1.formId = formId AND division = 'A01421' ) 
        ${CONDITIONS}
    ) f
    LEFT JOIN (
      SELECT
        formId,
        FormClass
      FROM
        form
    ) fc ON f.SourceId = fc.formId
)
SELECT
  *
FROM
  (
    SELECT
      FormClass,
      t,
      counter
    FROM
      _result
  ) AS p PIVOT (SUM(counter) FOR t IN ([times])) AS pt
UNION ALL
SELECT
  *
FROM
  (
    SELECT
      'total' AS FormClass,
      'times' AS t,
      COUNT(*) AS counter
    FROM
      _result
  ) AS p PIVOT (SUM(counter) FOR t IN ([times])) AS pt

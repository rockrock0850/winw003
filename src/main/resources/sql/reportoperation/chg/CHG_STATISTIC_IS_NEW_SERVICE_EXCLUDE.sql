WITH _FORM_INFO_CHG_DETAIL AS (
  SELECT
    f.[section],
    'newService' AS ns,
    1 AS times
  FROM
    (
      SELECT
        FormId
      FROM
        FORM_INFO_CHG_DETAIL
      WHERE
        IsNewService = 'Y'
    ) AS icd
    INNER JOIN (
      SELECT
        FormId,
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
          CHARINDEX('-', DivisionCreated) = 0,
          '',
          SUBSTRING(
            DivisionCreated,
            CHARINDEX('-', DivisionCreated) + 1,
            LEN(DivisionCreated) - CHARINDEX('-', DivisionCreated)
          )
        ) SECTION,
        CreatedAt
      FROM
        FORM
    ) AS f ON icd.FormId = f.FormId
  WHERE
    f.CreatedAt BETWEEN :startDate
    AND :endDate 
 AND  NOT EXISTS ( SELECT 1 FROM form nef WHERE nef.FormId = f.FormId AND f.division = 'A01421' ) 
    ${CONDITIONS}
)
SELECT
  *
FROM
  (
    SELECT
      [section],
      ns,
      times
    FROM
      _FORM_INFO_CHG_DETAIL
  ) AS p PIVOT (SUM(times) FOR ns IN ([newService])) AS pt
UNION ALL
SELECT
  *
FROM
  (
    SELECT
      'total' AS SECTION,
      'newService' AS ns,
      COUNT(*) AS times
    FROM
      _FORM_INFO_CHG_DETAIL
  ) AS p PIVOT (SUM(times) FOR ns IN ([newService])) AS pt

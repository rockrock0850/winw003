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
    f.formid,
    fid.ect,
    fid.act,
    CONVERT(varchar(6), fid.ect, 112) AS target
  FROM
    _f2 f
    INNER JOIN (
      SELECT
        infoDate.FormId,
        infoDate.ect,
        infoDate.act
      FROM
        FORM_INFO_DATE infoDate
      WHERE
        infoDate.formClass = 'Q'
        AND infoDate.ect IS NOT NULL
        AND NOT EXISTS (
          SELECT
            1
          FROM
            FORM_INFO_DATE 
          WHERE
            infoDate.formid = formid
            AND IsSpecial = 'Y'
        )
    ) fid ON f.FormId = fid.FormId
  WHERE
    1 = 1
  ${CONDITIONS}
),
_finished AS (
  SELECT
    *
  FROM
    _form f
  WHERE
    act IS NOT NULL
    AND NOT EXISTS (
      SELECT
        1
      FROM
        _form
      WHERE
        f.formid = formid
        AND CONVERT(varchar(10), ect, 120) < CONVERT(varchar(10), act, 120)
    )
),
_selectInterval AS (
  SELECT
    CONVERT(
      varchar(6),
      DATEADD(MONTH, number, :startDate ),
      112
    ) AS [Date]
  FROM
    master.dbo.spt_values
  WHERE
    type = 'p'
    AND DATEADD(MONTH, number, :startDate ) <= :endDate
),
_result AS (
  SELECT
    d.[Date] AS yearmonth,
    'all' AS type,
    iif(allItem.target IS NULL, 0, 1) AS times
  FROM
    _selectInterval d
    LEFT JOIN _form allItem ON d.date = allItem.target
  UNION ALL
  SELECT
    d.[Date] AS yearmonth,
    'finished' AS type,
    iif(allItem.target IS NULL, 0, 1) AS times
  FROM
    _selectInterval d
    LEFT JOIN _finished allItem ON d.date = allItem.target
),
_resultTotal AS (
  SELECT
    'total' AS yearmonth,
    'all' AS type,
    SUM(times) AS times
  FROM
    _result
  WHERE
    type = 'all'
  UNION ALL
  SELECT
    'total' AS yearmonth,
    'finished' AS type,
    SUM(times) AS times
  FROM
    _result
  WHERE
    type = 'finished'
)
SELECT
  yearmonth,
  [all],
  [finished],
  iif(
    [all] > 0,
    CONVERT(
      numeric(17, 2),
      CONVERT(
        float,
          (
            CONVERT(numeric(17, 2), [finished]) / CONVERT(numeric(17, 2), [all])
          ) * 100
      )
    ),
    0
  ) AS percentage
FROM
  _result AS p PIVOT (SUM(times) FOR type IN ([all], [finished])) AS pt
UNION ALL
SELECT
  yearmonth,
  [all],
  [finished],
  iif(
    [all] > 0,
    CONVERT(
      numeric(17, 2),
      CONVERT(
        float,
          (
            CONVERT(numeric(17, 2), [finished]) / CONVERT(numeric(17, 2), [all])
          ) * 100
      )
    ),
    0
  ) AS percentage
FROM
  _resultTotal AS p PIVOT (SUM(times) FOR type IN ([all], [finished])) AS pt

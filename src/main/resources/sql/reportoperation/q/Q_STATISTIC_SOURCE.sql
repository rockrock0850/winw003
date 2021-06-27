WITH _f AS (
  SELECT
    f.formid,
    f.FormStatus,
    fid.ReportTime,
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
  LEFT JOIN
  	FORM_INFO_DATE fid
  ON f.formid = fid.formid 
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
    *
  FROM
    _f2 f
  WHERE
    CONVERT(varchar,f.ReportTime,111) BETWEEN CONVERT(varchar,cast(:startDate as datetime),111)
    AND CONVERT(varchar,cast(:endDate as datetime),111)
	${CONDITIONS}
),
_SYS_OPTION AS (
  SELECT
    value,
    Display
  FROM
    SYS_OPTION
  WHERE
    optionId = '3'
),
_FORM_INFO_USER AS (
  SELECT
    fiu.QuestionId
  FROM
    _form f
    INNER JOIN (
      SELECT
        u.formid,
        u.QuestionId
      FROM
        FORM_INFO_USER u
    ) fiu ON f.FormId = fiu.Formid
),
_result AS (
  SELECT
    s.[Value],
    s.Display,
    'counter' AS count,
    IIF(f.QuestionId IS NULL, 0, 1) AS times
  FROM
    _SYS_OPTION s
    LEFT JOIN _FORM_INFO_USER f ON s.[Value] = f.QuestionId
  UNION ALL
  SELECT
  'total' AS [value],
  'total' AS display,
  'counter' AS count,
  count(*) AS times
  FROM
    _SYS_OPTION s
    INNER JOIN _FORM_INFO_USER f ON s.[Value] = f.QuestionId   
),
_resultTotal AS (
  SELECT
    'counter' AS count,
    1 AS t
  FROM
    _result
)
SELECT
  [value],
  display,
  ISNULL([counter], 0) AS counter
FROM
  _result AS p PIVOT (SUM(times) FOR count IN ([counter])) AS pt

WITH _f AS (
  SELECT
    f.formid,
    f.FormStatus,
    f.CreateTime,
    FID.ReportTime,
    FID.ACT,
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
  JOIN FORM_INFO_DATE FID 
  ON F.FormId=FID.FormId
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
    CONVERT(varchar,f.ReportTime, 111) BETWEEN CONVERT(varchar,cast(:startDate as datetime), 111)
    AND CONVERT(varchar,cast(:endDate as datetime), 111) 
    AND  NOT EXISTS ( SELECT 1 FROM _f2 WHERE f.formId = formId AND division = 'A01421' ) 
	${CONDITIONS}
),
_FORM_INFO_Q_DETAIL AS (
  SELECT
    f.FormId,
    f.FormStatus,
    f.ACT,
    iqd.QuestionPriority
  FROM
    _form f
    INNER JOIN (
      SELECT
        *
      FROM
        (
          SELECT
            FormId,
            QuestionPriority
          FROM
            FORM_INFO_Q_DETAIL
          WHERE
            QuestionPriority IS NOT NULL
        ) q
      WHERE
        NOT EXISTS (
          SELECT
            1
          FROM
            FORM_INFO_Q_DETAIL
          WHERE
            FormId = q.FormId
            AND QuestionPriority = ''
        )
    ) iqd ON f.FormId = iqd.FormId
),
_result AS (
  SELECT
    '1' AS grade,
    'all' AS num,
    COUNT(*) AS times
  FROM
    _FORM_INFO_Q_DETAIL
  WHERE
    QuestionPriority = '1'
  UNION ALL
  SELECT
    '1' AS grade,
    'unfinished' AS num,
    COUNT(*) AS times
  FROM
    _FORM_INFO_Q_DETAIL f
  WHERE
    QuestionPriority = '1'
    AND NOT EXISTS (
      SELECT
        1
      FROM
        _FORM_INFO_Q_DETAIL
      WHERE
        f.FormId = FormId
        AND FormStatus = 'CLOSED'
    )

	AND NOT EXISTS (	
      SELECT	
        1	
      FROM	
        _FORM_INFO_Q_DETAIL	
      WHERE	
        f.FormId = FormId	
        AND FormStatus = 'WATCHING'	
    )

	AND NOT EXISTS (	
      SELECT	
        1	
      FROM	
        FORM_VERIFY_LOG	
      WHERE	
        f.FormId = FormId
        AND FormStatus = 'APPROVING'
        AND VerifyType = 'REVIEW'
        AND f.ACT IS NOT NULL
    )
    
  UNION ALL
  SELECT
    '2' AS grade,
    'all' AS num,
    COUNT(*) AS times
  FROM
    _FORM_INFO_Q_DETAIL
  WHERE
    QuestionPriority = '2'
  UNION ALL
  SELECT
    '2' AS grade,
    'unfinished' AS num,
    COUNT(*) AS times
  FROM
    _FORM_INFO_Q_DETAIL f
  WHERE
    QuestionPriority = '2'
    AND NOT EXISTS (
      SELECT
        1
      FROM
        _FORM_INFO_Q_DETAIL
      WHERE
        f.FormId = FormId
        AND FormStatus = 'CLOSED'
    )

	AND NOT EXISTS (	
      SELECT	
        1	
      FROM	
        _FORM_INFO_Q_DETAIL	
      WHERE	
        f.FormId = FormId	
        AND FormStatus = 'WATCHING'	
    )

	AND NOT EXISTS (	
      SELECT	
        1	
      FROM	
        FORM_VERIFY_LOG	
      WHERE	
        f.FormId = FormId	
        AND FormStatus = 'APPROVING'
        AND VerifyType = 'REVIEW'
        AND f.ACT IS NOT NULL	
    )
	
  UNION ALL
  SELECT
    '3' AS grade,
    'all' AS num,
    COUNT(*) AS times
  FROM
    _FORM_INFO_Q_DETAIL
  WHERE
    QuestionPriority = '3'
  UNION ALL
  SELECT
    '3' AS grade,
    'unfinished' AS num,
    COUNT(*) AS times
  FROM
    _FORM_INFO_Q_DETAIL f
  WHERE
    QuestionPriority = '3'
    AND NOT EXISTS (
      SELECT
        1
      FROM
        _FORM_INFO_Q_DETAIL
      WHERE
        f.FormId = FormId
        AND FormStatus = 'CLOSED'
    )

	AND NOT EXISTS (
      SELECT
        1
      FROM
        _FORM_INFO_Q_DETAIL
      WHERE
        f.FormId = FormId
        AND FormStatus = 'WATCHING'
    )
 
    AND NOT EXISTS (	
      SELECT	
        1	
      FROM	
        FORM_VERIFY_LOG	
      WHERE	
        f.FormId = FormId	
        AND FormStatus = 'APPROVING'
        AND VerifyType = 'REVIEW'
        AND f.ACT IS NOT NULL	
    )
    
  UNION ALL
  SELECT
    'total' AS grade,
    'all' AS num,
    COUNT(*) AS times
  FROM
    _FORM_INFO_Q_DETAIL
  UNION ALL
  SELECT
    'total' AS grade,
    'unfinished' AS num,
    COUNT(*) AS times
  FROM
    _FORM_INFO_Q_DETAIL f
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        _FORM_INFO_Q_DETAIL
      WHERE
        f.FormId = FormId
        AND FormStatus = 'CLOSED'
    )

	AND NOT EXISTS (
      SELECT
        1
      FROM
        _FORM_INFO_Q_DETAIL
      WHERE
        f.FormId = FormId
        AND FormStatus = 'WATCHING'
    )
    
    AND NOT EXISTS (	
      SELECT	
        1	
      FROM	
        FORM_VERIFY_LOG	
      WHERE	
        f.FormId = FormId	
        AND FormStatus = 'APPROVING'
        AND VerifyType = 'REVIEW'
        AND f.ACT IS NOT NULL	
    )

)
SELECT
  grade,
  [all],
  [unfinished]
FROM
  _result AS p PIVOT (SUM(times) FOR num IN ([all], [unfinished])) AS pt

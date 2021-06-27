SELECT 
	RESULT.*,
	SG.GroupName,
	FORM_DATE.ACT,
	FORM_DATE.SCT,
	FORM_DATE.CCT
FROM (
	SELECT 
		INFO.*,
		CASE 
			INFO.VerifyType
			WHEN  
				'APPLY'
			THEN 
				CASE INFO.FormClass
					WHEN 'CHG' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_APPLY_CHG WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'INC' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_APPLY_INC WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'INC_C' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_APPLY_INC WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'JOB_AP' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_APPLY_JOB WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'JOB_AP_C' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_APPLY_JOB WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'JOB_SP' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_APPLY_JOB WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'JOB_SP_C' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_APPLY_JOB WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'Q' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_APPLY_Q WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'Q_C' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_APPLY_Q WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'SR' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_APPLY_SR WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'SR_C' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_APPLY_SR WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					ELSE ''
				END
			WHEN
				'REVIEW'
			THEN
				CASE INFO.FormClass
					WHEN 'CHG' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_REVIEW_CHG WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'INC' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_REVIEW_INC WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'INC_C' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_REVIEW_INC WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'JOB_AP' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_REVIEW_JOB WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'JOB_AP_C' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_REVIEW_JOB WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'JOB_SP' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_REVIEW_JOB WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'JOB_SP_C' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_REVIEW_JOB WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'Q' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_REVIEW_Q WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'Q_C' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_REVIEW_Q WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'SR' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_REVIEW_SR WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					WHEN 'SR_C' THEN (SELECT TOP 1 GroupId FROM FORM_PROCESS_DETAIL_REVIEW_SR WHERE DetailId = INFO.DetailId AND ProcessOrder = INFO.VerifyLevel)
					ELSE ''
			END
		END AS GroupId
	FROM
	( 
		SELECT 
				F.FormId,
				F.DetailId,
				F.SourceId,
				F.FormClass,
				F.FormStatus,
				F.GroupSolving,
				F.CreatedAt,
				F.DivisionSolving,
				F.UserSolving,
				FVL.VerifyLevel,
				FVL.VerifyType
		FROM
				FORM F
		LEFT JOIN
				FORM_VERIFY_LOG FVL
		ON
				(       F.FormId = FVL.FormId
				AND
						FVL.CompleteTime IS NULL
				)
		WHERE (1=1)
			${CONDITIONS}
	)INFO
) RESULT
LEFT JOIN
	SYS_GROUP SG
ON
	SG.GroupId = RESULT.GroupId
LEFT JOIN (
	select distinct
		FormId,
		ACT,
		CCT,
		SCT
	from 
		FORM_JOB_INFO_DATE
	UNION
	select distinct 
		FormId,
		ACT,
		CCT,
		'' AS SCT
	from 
		FORM_INFO_DATE
) FORM_DATE ON FORM_DATE.FormId = RESULT.FormId
ORDER BY 
	RESULT.CreatedAt
ASC
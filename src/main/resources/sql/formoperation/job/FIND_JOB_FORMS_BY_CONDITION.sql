SELECT 
	f.FormClass            AS formClass,
	f.formId               AS formId,
	f.UserSolving          AS UserSolving,
	f.CreateTime           AS createTime,
	f.DivisionCreated      AS divisionCreated,
	f.DivisionSolving      AS divisionSolving,
	f.FormStatus           AS FormStatus,
	FIDTL.Purpose          AS Purpose
FROM
 	FORM f
INNER JOIN (
	SELECT 
		Purpose,
		FormId
	FROM 
		FORM_JOB_INFO_AP_DETAIL
	UNION
	SELECT 
		Purpose,
		FormId
	FROM 
		FORM_JOB_INFO_SYS_DETAIL
) AS FIDTL
ON
 	f.FormId = FIDTL.FormId
WHERE
	(f.FormClass = 'JOB_AP' or f.FormClass = 'JOB_SP')
${CONDITIONS}
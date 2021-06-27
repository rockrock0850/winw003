SELECT 
	f.FormClass            AS formClass,
	f.formId               AS formId,
	f.UserSolving          AS UserSolving,
	f.CreateTime           AS createTime,
	f.DivisionCreated      AS divisionCreated,
	f.DivisionSolving      AS divisionSolving,
	f.FormStatus           AS FormStatus,
	f.groupSolving         AS GroupSolving,
	f.UserCreated          AS UserCreated
FROM
 	FORM f
INNER JOIN FORM_INFO_Q_DETAIL FIDTL ON f.FormId = FIDTL.FormId
LEFT JOIN FORM_INFO_DATE FID ON f.FormId = FID.FormId
WHERE 1=1
${CONDITIONS}
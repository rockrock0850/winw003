SELECT ori.formId, ori.eventPriority, ori.userCreated, ori.summary, ori.content, ori.ect, ori.createTime, ori.formClass,
ori.createName,ori.solvingName, ori.userSolving, ori.divisionSolving, ori.detailID, fvl_2.verifyLevel, fvl_2.verifyType FROM (
	SELECT
	F.formId,
	FIID.eventpriority,
	FIID.summary,
	FIID.content,
	FID.ect,
	F.createTime,
	F.formClass,
	LU1.name as createName,
	LU2.name as solvingName,
	F.userCreated,
	F.userSolving,
	F.divisionSolving,
	F.detailID,
	MAX(fvl_1.Id) AS LogId
FROM
	form F
JOIN 
	form_info_inc_detail FIID ON  F.formid = FIID.formId
LEFT JOIN 
	form_info_date FID ON F.FormId = FID.formId
LEFT JOIN
	ldap_user LU1 ON F.userCreated = LU1.UserId
LEFT JOIN
	ldap_user LU2 ON F.userSolving = LU2.UserId
JOIN FORM_VERIFY_LOG fvl_1 ON
	f.FormId = fvl_1.FormId
WHERE 
	(1=1)
${CONDITIONS}
GROUP BY
	F.formId,
	FIID.eventpriority,
	FIID.summary,
	FIID.content,
	FID.ect,
	F.createTime,
	F.formClass,
	LU1.name,
	LU2.name,
	F.userCreated,
	F.userSolving,
	F.divisionSolving,
	F.detailID
)ori LEFT JOIN FORM_VERIFY_LOG fvl_2 ON ori.LogId = fvl_2.Id
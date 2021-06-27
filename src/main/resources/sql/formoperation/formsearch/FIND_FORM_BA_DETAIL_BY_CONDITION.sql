SELECT DISTINCT
	f.FormClass          AS formClass,
	f.FormId             AS formId,
	f.FormStatus         AS formStatus,
	f.CreateTime         AS createTime,
	f.DivisionCreated    AS divisionCreated,
	lUser.Name           AS userCreated,
	f.DivisionSolving    AS divisionSolving,
	lUS.Name             AS userSolving,
	u.UnitId             AS unitId,
	u.UserName           AS userName,
	FIDTL.BatchName 		 AS batchName,
	FIDTL.Summary 			 AS summary,
	FIDTL.Division 		 AS division,
	FIDTL.ExecuteTime	   	 AS executeTime,
	FIDTL.DbInUse 			 AS dbInUse,
	FIDTL.EffectDate 		 AS effectDate,
	d.ECT                AS ect,
	d.ACT                AS act,
	d.EOT                AS eot
FROM
	FORM f WITH (NOLOCK)
LEFT JOIN
	FORM_INFO_USER u WITH (NOLOCK)
ON
	f.FormId = u.FormId
LEFT JOIN
	FORM_INFO_Date d WITH (NOLOCK)
ON
	f.FormId = d.FormId
LEFT JOIN
	FORM_INFO_BA_DETAIL FIDTL WITH (NOLOCK)
ON
	f.FormId = FIDTL.FormId
LEFT JOIN
	LDAP_USER lUser WITH (NOLOCK)
ON
	f.UserCreated = lUser.UserId
LEFT JOIN
	LDAP_USER lUS WITH (NOLOCK)
ON
	f.UserSolving = lUS.UserId
WHERE (1=1)
${CONDITIONS}
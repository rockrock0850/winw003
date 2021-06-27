SELECT 
	F.FormId       AS formId,
	INC.Summary    AS summary,
	D.CreateTime   AS createTime,
	F.UserCreated  AS userId,
	LU.Name  	   AS userCreated,
	F.FormStatus   AS formStatus
FROM
 	FORM F
JOIN
	FORM_INFO_INC_DETAIL INC
ON
 	F.FormId = INC.FormId
JOIN
	FORM_INFO_Date D
ON
	F.FormId = D.FormId 
JOIN 
	LDAP_USER LU
ON
	F.UserCreated = LU.UserId
WHERE
	F.FormClass = 'INC'
AND
	F.formStatus NOT IN ('PROPOSING', 'CLOSED', 'DEPRECATED')
AND
	INC.Summary LIKE '%' + :summary + '%'
AND
	INC.FormId LIKE '%' + :formId + '%'
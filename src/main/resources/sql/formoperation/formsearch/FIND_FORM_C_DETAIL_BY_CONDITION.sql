SELECT DISTINCT
	f.FormClass             AS formClass,
	f.FormId                AS formId,
	f.FormStatus            AS formStatus,
	f.SourceId             	AS sourceId,
	f.CreateTime            AS createTime,
	f.DivisionCreated       AS divisionCreated,
	f.DivisionSolving       AS divisionSolving,
	f.IsAlterDone	     	AS isAlterDone,
	lUser.Name              AS userCreated,
	lUS.Name                AS userSolving,
	FIDTL.Summary              AS summary,
	FIDTL.Content              AS content,
	FIDTL.HostHandle           AS hostHandle,
	FIDTL.CountersignedHandle  AS countersignedHandle,
	FIDTL.System            	AS system,
	FIDTL.AssetGroup           AS assetGroup,
	FIDTL.UserGroup           	AS userGroup,
	FIDTL.UserId           	AS userId,
	sClassOpt.Display       AS sClass,
	d.CreateTime            AS assignTime,
	d.AST					AS ast,
	d.ACT                   AS act,
	d.EOT                   AS eot,
	d.ECT                   AS ect,
	d.MECT					AS mect
--	,
--	FFV.FormFile AS formFile,
--  FUV.FormUserRecord AS formUserRecord,
--  FAV.AssociationForm AS associationForm
FROM
	FORM f WITH (NOLOCK)
--LEFT JOIN FORM_FILE_VIEW FFV WITH (NOLOCK) ON FFV.FormId = f.FormId
--LEFT JOIN FORM_USER_RECORD_VIEW FUV WITH (NOLOCK) ON FUV.FormId = f.FormId
--LEFT JOIN FORM_ASSOCIATION_VIEW FAV WITH (NOLOCK) ON FAV.FormId = f.FormId
LEFT JOIN
	FORM_USER_RECORD FUR WITH (NOLOCK)
ON
	f.FormId = FUR.FormId
LEFT JOIN
	FORM_FILE FF WITH (NOLOCK)
ON
	f.FormId = FF.FormId
LEFT JOIN
	FORM_INFO_DATE d WITH (NOLOCK)
ON
	f.FormId = d.FormId
LEFT JOIN
	FORM_INFO_C_DETAIL FIDTL WITH (NOLOCK)
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
LEFT JOIN
	SYS_OPTION sClassOpt WITH (NOLOCK)
ON
	sClassOpt.OptionId = '2'
	AND
	FIDTL.SClass = sClassOpt.Value
WHERE (1=1)
${CONDITIONS}
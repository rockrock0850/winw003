SELECT DISTINCT
	f.FormClass            AS formClass,
	f.FormId               AS formId,
	f.FormStatus           AS formStatus,
	f.CreateTime           AS createTime,
	f.DivisionCreated      AS divisionCreated,
	f.IsAlterDone	       AS isAlterDone,
	lUser.Name             AS userCreated,
	f.DivisionSolving      AS divisionSolving,
	lUS.Name               AS userSolving,
	UCOpt.Display          AS unitCategory,
	u.UnitId               AS unitId,
	u.UserName             AS userName,
	FIDTL.Summary          AS summary,
	FIDTL.Content          AS content,
	ECOpt.Display          AS eventClass,
	FIDTL.EventType        AS eventType,
	SEOpt.Display          AS eventSecurity,
	FIDTL.eventPriority    AS eventPriority,
	d.IsMainEvent          AS isMainEvent,
	d.IsInterrupt          AS isInterrupt,
	d.CreateTime           AS infoDateCreateTime,
	d.ACT                  AS act,
	d.ExcludeTime          AS excludeTime,
	d.ECT                  AS ect,
	sClassOpt.Display      AS sClass,
	sSubClass.Display      AS sSubClass,
	FIDTL.System           AS system,
	FIDTL.AssetGroup       AS assetGroup,
	FIDTL.CCategory        AS cCategory,
	FIDTL.CClass           AS cClass,
	FIDTL.CComponent       AS cComponent,
	ESOpt.Display          AS effectScope,
	INCULOpt.Display       AS urgentLevel,
	FIDTL.Countersigneds   AS countersigneds,
	d.IsIVR                AS isIVR,
	d.MainEvent            AS mainEvent,
	fp.ProcessProgram      AS processProgram,
	fp.Indication          AS indication,
	fp.Reason              AS reason,
	fp.IsSuggestCase 	   AS isSuggestCase,
	u.IsForward            AS isForward, 
	d.IsSameInc			   AS isSameInc,
	FIDTL.IsOnlineFail	   AS isOnlineFail,
	FIDTL.OnlineTime	   AS onlineTime,
	FIDTL.OnlineJobFormId  AS onlineJobFormId 
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
	FORM_INFO_USER u WITH (NOLOCK)
ON
	f.FormId = u.FormId
LEFT JOIN
	FORM_INFO_Date d WITH (NOLOCK)
ON
	f.FormId = d.FormId
LEFT JOIN
	FORM_INFO_INC_DETAIL FIDTL WITH (NOLOCK)
ON
	f.FormId = FIDTL.FormId
LEFT JOIN
	FORM_PROGRAM fp WITH (NOLOCK)
ON
	f.FormId = fp.FormId
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
LEFT JOIN
	SYS_OPTION UCOpt WITH (NOLOCK)
ON
	UCOpt.OptionId = '1'
	AND
	u.UnitCategory = UCOpt.Value
LEFT JOIN
	SYS_OPTION ESOpt WITH (NOLOCK)
ON
	ESOpt.OptionId = 'EFFECT_SCOPE'
	AND
	FIDTL.EffectScope = ESOpt.Value
LEFT JOIN
	SYS_OPTION INCULOpt WITH (NOLOCK)
ON
	INCULOpt.OptionId = 'INC_URGENT_LEVEL'
	AND
	FIDTL.UrgentLevel = INCULOpt.Value
LEFT JOIN
	SYS_OPTION ECOpt WITH (NOLOCK)
ON
	ECOpt.OptionId = '4'
	AND
	FIDTL.EventClass = ECOpt.Value
LEFT JOIN
	SYS_OPTION SEOpt WITH (NOLOCK)
ON
	SEOpt.OptionId = 'SecurityEvent'
	AND
	FIDTL.EventSecurity = SEOpt.Value
LEFT JOIN
	SYS_OPTION sSubClass WITH (NOLOCK)
ON
	FIDTL.sSubClass = sSubClass.Value
WHERE (1=1)
${CONDITIONS}
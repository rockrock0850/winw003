SELECT 
	f.FormClass            AS formClass,
	f.FormId               AS formId,
	f.FormStatus           AS formStatus,
	f.CreateTime           AS createTime,
	f.DivisionCreated      AS divisionCreated,
	f.IsAlterDone	       AS isAlterDone,
	f.DivisionSolving      AS divisionSolving,
	FIDTL.Summary          AS summary,
	FIDTL.Content          AS content,
	FIDTL.EventType        AS eventType,
	FIDTL.eventPriority    AS eventPriority,
	FIDTL.System           AS system,
	FIDTL.SystemId         AS systemId,
	FIDTL.AssetGroup       AS assetGroup,
	FIDTL.CCategory        AS cCategory,
	FIDTL.CClass           AS cClass,
	FIDTL.CComponent       AS cComponent,
	FIDTL.Countersigneds   AS countersigneds,
	FIDTL.UrgentLevel      AS UrgentLevel,
	FIDTL.EffectScope      AS EffectScope,
	d.IsIVR                AS isIVR,
	d.MainEvent            AS mainEvent,
	d.IsMainEvent          AS isMainEvent,
	d.IsInterrupt          AS isInterrupt,
	d.CreatedAt            AS infoDateCreateTime,
	d.ACT                  AS act,
	d.ExcludeTime          AS excludeTime,
	d.ECT                  AS ect,
	d.IsSameInc			   AS isSameInc,
	FIDTL.IsOnlineFail	   AS isOnlineFail,
	FIDTL.OnlineTime	   AS onlineTime,
	FIDTL.OnlineJobFormId  AS onlineJobFormId 
FROM
 	FORM f
JOIN
	FORM_INFO_INC_DETAIL FIDTL
ON
 	f.FormId = FIDTL.FormId
JOIN
	FORM_INFO_Date d
ON
	f.FormId = d.FormId
WHERE
	f.FormClass = 'INC'
${CONDITIONS}
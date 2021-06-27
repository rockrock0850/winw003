SELECT
	f.FormId                AS formId,
	f.FormClass             AS formClass,
	f.FormStatus            AS formStatus,
	f.CreateTime            AS createTime,
	FIDTL.Id 	        	AS Id,
	FIDTL.Summary           AS summary,       
	FIDTL.Indication        AS indication,    
	FIDTL.Reason            AS reason,
	FIDTL.ProcessProgram    AS processProgram,
	FIDTL.SystemId          AS systemId,
	FIDTL.System            AS system,
	FIDTL.AssetGroup        AS assetGroup,
	FIDTL.Knowledges        AS knowledges,
	FIDTL.Solutions         AS solutions,
	FIDTL.FlowName          AS flowName,
	FIDTL.UpdatedAt         AS updatedAt,
	sClassOpt.Display    	AS sClass,
	sSubClass.Display       AS sSubClass,
	FIDTL.IsEnabled         AS isEnabled
FROM
	FORM f WITH (NOLOCK)
LEFT JOIN
	FORM_INFO_KL_DETAIL FIDTL WITH (NOLOCK)
ON
	f.FormId = FIDTL.FormId
LEFT JOIN
	SYS_OPTION sClassOpt WITH (NOLOCK)
ON
	sClassOpt.OptionId = '2'
AND
	FIDTL.SClass = sClassOpt.Value
LEFT JOIN
	SYS_OPTION sSubClass WITH (NOLOCK)
ON
	sSubClass.OptionId = '0'
AND
	FIDTL.sSubClass = sSubClass.Value
WHERE (1=1)
${CONDITIONS}
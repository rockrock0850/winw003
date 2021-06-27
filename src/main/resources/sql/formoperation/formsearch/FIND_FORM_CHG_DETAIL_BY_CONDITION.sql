SELECT DISTINCT
	f.FormClass                 AS formClass,
	f.FormId                    AS formId,
	f.FormStatus                AS formStatus,
	f.SourceId                  AS sourceId,
	f.CreateTime                AS createTime,
	f.DivisionCreated           AS divisionCreated,
	lUser.Name                  AS userCreated,
	f.DivisionSolving           AS divisionSolving,
	lUS.Name                    AS userSolving,
	FIDTL.Summary               AS summary,
	FIDTL.Content               AS content,
	FIDTL.CCategory             AS cCategory,
	FIDTL.CClass                AS cClass,
	FIDTL.CComponent            AS cComponent,
	FIDTL.EffectSystem	   	    AS effectSystem,
	FIDTL.IsNewSystem		    AS isNewSystem,
	FIDTL.IsNewService	   	    AS isNewService,
	FIDTL.IsUrgent		   	    AS isUrgent,
	SDOpt.Display		   	    AS standard,
	FIDTL.ChangeType		    AS changeType,
	FIDTL.ChangeRank		    AS changeRank,
	FIDTL.IsEffectField	   	    AS isEffectField,
	FIDTL.IsEffectAccountant    AS isEffectAccountant,
	FIDTL.IsModifyProgram	    AS isModifyProgram,
	d.CAT				   	    AS cat,
	d.CCT				   	    AS cct,
	fiaView.Evaluation     	    AS evaluation,
	fiaView.Solution       	    AS solution,
	fiaView.Total          	    AS total
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
	FORM_INFO_CHG_DETAIL FIDTL WITH (NOLOCK)
ON
	f.FormId = FIDTL.FormId
LEFT JOIN
	FORM_IMPACT_ANALYSIS_VIEW fiaView WITH (NOLOCK)
ON
	f.FormId = fiaView.FormId
LEFT JOIN
	LDAP_USER lUser WITH (NOLOCK)
ON
	f.UserCreated = lUser.UserId
LEFT JOIN
	LDAP_USER lUS WITH (NOLOCK)
ON
	f.UserSolving = lUS.UserId
LEFT JOIN
	SYS_OPTION SDOpt WITH (NOLOCK)
ON
	SDOpt.OptionId = 'StandardChange'
	AND
	FIDTL.Standard = SDOpt.Value	
WHERE (1=1)
${CONDITIONS}
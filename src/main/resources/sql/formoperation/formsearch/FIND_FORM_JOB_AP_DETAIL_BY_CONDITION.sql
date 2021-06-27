SELECT DISTINCT
	f.FormClass            AS formClass,
	f.FormId               AS formId,
	f.FormStatus           AS formStatus,
	f.SourceId             AS sourceId,
	f.CreateTime           AS createTime,
	f.DivisionCreated      AS divisionCreated,
	lUser.Name             AS userCreated,
	f.DivisionSolving      AS divisionSolving,
	lUS.Name               AS userSolving,
	sClassOpt.Display      AS sClass,
	sSubClass.Display      AS sSubClass,
	ap.System              AS system,
	ap.ChangeType		   AS changeType,
	ap.ChangeRank		   AS changeRank,
	ap.IsHandleFirst	   AS isHandleFirst,
	ap.IsCorrect		   AS isCorrect,
	ap.IsAddFuntion        AS isAddFuntion,
	ap.IsAddReport		   AS isAddReport,
	ap.IsAddFile		   AS isAddFile,
	ap.IsProgramOnline	   AS isProgramOnline,
	ap.Purpose			   AS purpose,
	ap.Content			   AS content,
	ap.IsForward		   AS isForward,
	ap.IsWatching          AS isWatching,
	ap.Countersigneds      AS countersigneds,
	ap.IsModifyProgram	   AS isModifyProgram,
	d.IsPlaning			   AS isPlaning,
	d.IsUnPlaning		   AS isUnPlaning,
	d.OffLineTime		   AS offLineTime,
	d.IST				   AS ist,
	d.ICT				   AS ict,
	d.TCT				   AS tct,
	d.SCT				   AS sct,
	d.SIT				   AS sit,
	d.CAT				   AS cat,
	d.CCT				   AS cct,
	FJC.Division		   AS cDivision,
	FJC.DataType           AS cDataType,
	FJC.BookNumber         AS cBookNumber,
	FJC.OnlyNumber         AS cOnlyNumber,
	FJC.OnlyCode           AS cOnlyCode,
	FJC.LinkNumber         AS cLinkNumber,
	FJC.LinkCode           AS cLinkCode,
	FJC.LinkOnlyNumber     AS cLinkOnlyNumber,
	FJC.RollbackDesc       AS cRollbackDesc,
	FJC.IsTest             AS cIsTest,
	FJC.IsProduction       AS cIsProduction,
	FJC.Book               AS cBook,
	FJC.Only               AS cOnly,
	FJC.Link               AS cLink,
	FJC.LinkOnly           AS cLinkOnly,
	FJC.Description        AS cDescription,
	FJW.Type               AS cType,
	FJW.DBName             AS cDbName,
	FJW.PSBName            AS cPsbName,
	FJW.AlterWay           AS cAlterWay,
	FJC.UserId             AS cUserId,
	FJB.FormId             AS cbFormId,
	FJB.UserId             AS cbUserId,
	FJC.IsRollback         AS cIsRollback,
	FJB.IsRollback         AS cbIsRollback,
	FJC.Other              AS cOther,
	FJB.Other              AS cbOther,
	FJW.Remark             AS cRemark,
	FJB.Remark             AS cbRemark,
	FJB.PSB                AS cPsb,
	FJB.ProgramName        AS cProgramName,
	FJB.ProgramNumber      AS cProgramNumber,
	FJB.JCL                AS cJCL,
	FJB.CLJCL              AS cCLJCL,
	FJB.IsHelp             AS cIsHelp,
	FJB.IsCange            AS cIsChange,
	FJB.IsAllow            AS cIsAllow,
	FJB.IsOther            AS cIsOther,
	FJB.IsHelpCL           AS cIsHelpCL,
	FJB.IsOtherDesc        AS cIsOtherDesc,
	FJB.Reason             AS cReason,
	FJB.IT                 AS cIt,
	FJC.TCT                AS cTct,
	FJC.SCT                AS cSct,
	FJC.SIT                AS cSit,
	FJW.EIT                AS cEit,
	FJW.AST                AS cAst,
	FJB.Content            AS cContent
--	,
--  FAV.AssociationForm AS associationForm
FROM
	FORM f WITH (NOLOCK)
--LEFT JOIN FORM_ASSOCIATION_VIEW FAV WITH (NOLOCK) ON FAV.FormId = f.FormId
LEFT JOIN
	FORM_INFO_USER u WITH (NOLOCK)
ON
	f.FormId = u.FormId
LEFT JOIN
	FORM_JOB_INFO_Date d WITH (NOLOCK)
ON
	f.FormId = d.FormId
LEFT JOIN
	FORM_JOB_INFO_AP_DETAIL ap WITH (NOLOCK)
ON
	f.FormId = ap.FormId
LEFT JOIN 
	FORM_JOB_COUNTERSIGNED FJC WITH (NOLOCK)
ON
	f.FormId = FJC.FormId
LEFT JOIN 
	FORM_JOB_WORKING FJW WITH (NOLOCK)
ON
	f.FormId = FJW.FormId
LEFT JOIN 
	FORM_JOB_BATCH FJB WITH (NOLOCK)
ON
	f.FormId = FJB.FormId
LEFT JOIN
	FORM_JOB_LIBRARY FJL WITH (NOLOCK)
ON
	f.FormId = FJL.FormId
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
AND ap.SClass = sClassOpt.Value
LEFT JOIN
	SYS_OPTION sSubClass WITH (NOLOCK)
ON
	ap.sSubClass = sSubClass.Value
WHERE (1=1)
${CONDITIONS}
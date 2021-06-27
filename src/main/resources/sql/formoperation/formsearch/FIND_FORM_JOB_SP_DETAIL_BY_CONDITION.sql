SELECT DISTINCT
	F.FormClass            AS formClass,
	F.FormId               AS formId,
	F.FormStatus           AS formStatus,
	F.SourceId             AS sourceId,
	F.CreateTime           AS createTime,
	F.DivisionCreated      AS divisionCreated,
	lUser.Name             AS userCreated,
	F.DivisionSolving      AS divisionSolving,
	lUS.Name               AS userSolving,
	SO.Display      	   AS cCategory,
	ap.Working             AS working,      
	ap.Status              AS status,           
	ap.CClass              AS cClass,        
	ap.CComponent          AS cComponent,    
	ap.SystemId            AS systemId,      
	ap.System              AS system,        
	ap.UserId              AS userId,        
	ap.AStatus             AS aStatus,       
	ap.OnLineTime          AS onLineTime,    
	ap.IsReset             AS isReset,       
	ap.IsProduction        AS isProduction,  
	ap.IsTest              AS isTest,        
	ap.IsHandleFirst       AS isHandleFirst, 
	ap.IsForward           AS isForward,     
	ap.IsInterrupt         AS isInterrupt,   
	ap.Summary             AS summary,       
	ap.Purpose             AS purpose,       
	ap.Content             AS content,       
	ap.EffectScope         AS effectScope,   
	ap.Remark              AS remark,        
	ap.Reset               AS reset,         
	ap.Countersigneds      AS countersigneds,
	d.IsPlaning		       AS isPlaning,
	d.IsUnPlaning	       AS isUnPlaning,
	d.OffLineTime	       AS offLineTime,
	d.MECT			       AS mect,
	d.EOT			       AS eot,
	d.ECT			       AS ect,
	d.AST			       AS ast,
	d.ACT			       AS act,
	d.IST			       AS ist,
	d.ICT			       AS ict,
	d.TCT			       AS tct,
	d.SCT			       AS sct,
	d.SIT			       AS sit,
	d.CAT			       AS cat,
	d.CCT			       AS cct,
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
	FORM F WITH (NOLOCK)
--LEFT JOIN FORM_ASSOCIATION_VIEW FAV WITH (NOLOCK) ON FAV.FormId = f.FormId
LEFT JOIN 
	FORM_JOB_BATCH FJB WITH (NOLOCK)
ON
	f.FormId = FJB.FormId
LEFT JOIN 
	FORM_JOB_WORKING FJW WITH (NOLOCK)
ON
	f.FormId = FJW.FormId
LEFT JOIN 
	FORM_JOB_COUNTERSIGNED FJC WITH (NOLOCK)
ON
	f.FormId = FJC.FormId
LEFT JOIN
	FORM_INFO_USER FIU WITH (NOLOCK)
ON
	F.FormId = FIU.FormId
LEFT JOIN
	FORM_JOB_INFO_DATE d WITH (NOLOCK)
ON
	F.FormId = d.FormId
LEFT JOIN
	FORM_JOB_INFO_SYS_DETAIL ap WITH (NOLOCK)
ON
	F.FormId = ap.FormId
LEFT JOIN
	FORM_JOB_LIBRARY FJL WITH (NOLOCK)
ON
	F.FormId = FJL.FormId
LEFT JOIN
	LDAP_USER lUser WITH (NOLOCK)
ON
	F.UserCreated = lUser.UserId
LEFT JOIN
	LDAP_USER lUS WITH (NOLOCK)
ON
	F.UserSolving = lUS.UserId
LEFT JOIN
	SYS_OPTION SO WITH (NOLOCK)
ON
	SO.OptionId = 'cCategory'
	AND
	ap.CCategory = SO.Value
WHERE (1=1)
${CONDITIONS}
SELECT
    TOP 1 
    F.FormId,
    F.SourceId,
    F.DetailId,
    F.FormClass,
    F.ProcessName,
    F.FormStatus,
    F.ProcessStatus,
    F.DivisionCreated,
    F.UserCreated,
    F.DivisionSolving,
    F.UserSolving,
    F.GroupSolving,
    F.Parallel,
    SG.groupName,
    F.CreateTime,
    FID.CreateTime AS assignTime,
    FID.MECT,
	FID.ECT,
	FID.EOT,
	FID.ACT,
	FID.AST,
	FID.ReportTime,
	FID.ExcludeTime,
	FID.Observation,
	FID.CAT,
	FID.CCT,
	FID.MainEvent,
	FID.IsIVR,
	FID.IsSpecial,
	FID.IsSameInc,
	FID.SpecialEndCaseType,
	FID.IsMainEvent,
	FID.IsInterrupt,
    FIU.UnitId,
	FIU.UserName,
	FIU.QuestionId,
	FIU.Phone,
	FIU.Email,
	FIU.UnitCategory,
	FIU.IsForward,
    FVL.VerifyLevel,
    FVL.VerifyType,
    F.UpdatedBy,
    F.UpdatedAt,
    FID.OECT,
    F.IsExtended
FROM
    FORM F
LEFT JOIN
	FORM_INFO_USER FIU
ON
	F.FormId = FIU.FormId
JOIN
	FORM_INFO_DATE FID
ON
	F.FormId = FID.FormId
LEFT JOIN
    SYS_GROUP SG
ON
    SG.GroupId = F.GroupSolving
LEFT JOIN
    FORM_VERIFY_LOG FVL
ON
    FVL.FormId = F.FormId
WHERE (1=1)
${CONDITIONS}
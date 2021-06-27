SELECT TOP(1)
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
	FID.OffLineTime,
	FID.MECT,
	FID.EOT,
	FID.ECT,
	FID.AST,
	FID.ACT,
	FID.CAT,
	FID.CCT,
	FID.IST,
	FID.ICT,
	FID.TCT,
	FID.SCT,
	FID.SIT,
	FID.IsPlaning,
	FID.IsUnPlaning,
    FVL.VerifyLevel,
    FVL.VerifyType,
    F.UpdatedBy,
    F.UpdatedAt,
    F.CreatedBy,
    F.CreatedAt
FROM
    FORM F
JOIN
	FORM_JOB_INFO_DATE FID
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
ORDER BY FVL.ID DESC
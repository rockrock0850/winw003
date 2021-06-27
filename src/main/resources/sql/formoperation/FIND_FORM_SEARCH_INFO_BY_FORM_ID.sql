SELECT
	TOP 1 
    F.FormId AS formId,
    F.SourceId AS sourceId,
    F.DetailId AS detailId,
    F.FormClass AS formClass,
    F.ProcessName AS processName,
    F.FormStatus AS formStatus,
    F.ProcessStatus AS processStatus,
    F.DivisionCreated AS divisionCreated,
    F.UserCreated AS userCreated,
    F.DivisionSolving AS divisionSolving,
    F.UserSolving AS userSolving,
    F.GroupSolving AS groupSolving,
    SG.groupName AS groupName,
    F.CreateTime AS createTime,
    FVL.VerifyLevel AS verifyLevel,
    FVL.VerifyType AS verifyType,
    F.UpdatedBy AS updatedBy,
    F.UpdatedAt AS updatedAt
FROM
    FORM F
LEFT JOIN
    SYS_GROUP SG
ON
    SG.GroupId = F.GroupSolving
LEFT JOIN
    FORM_VERIFY_LOG FVL
ON
    FVL.FormId = F.FormId
WHERE
	F.FormId = :formId
ORDER BY
    FVL.Id DESC
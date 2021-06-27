SELECT
	TOP 1000
	FVL.Id,
    FVL.FormId,
    FVL.VerifyType,
    FVL.VerifyLevel,
    FVL.SubmitTime,
    FVL.CompleteTime,
    FVL.Parallel,
    LU.UserId,
    LU.Name	AS UserName,
    SG.DepartmentName + '-' + SG.Division AS GroupName,
    CASE
        WHEN ISNUMERIC (FVL.VerifyLevel) = 1
        THEN SG.GroupName
        ELSE FVL.VerifyLevel
    END AS GroupSolving,
    FVL.VerifyResult,
    FVL.VerifyComment,
    FVL.CreatedAt
FROM
    FORM_VERIFY_LOG FVL
LEFT JOIN
    LDAP_USER LU
ON
    FVL.UserId = LU.UserId
LEFT JOIN 
	SYS_GROUP SG
ON 
	FVL.GroupId = SG.GroupId
WHERE (1=1)
AND	FVL.FormId = :formId
AND FVL.VerifyResult <> 'JUMP'
AND FVL.CompleteTime IS NULL
${CONDITIONS}
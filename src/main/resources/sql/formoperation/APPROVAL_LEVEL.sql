UPDATE
    FORM_VERIFY_LOG
SET
    VerifyResult = :verifyResult,
    CompleteTime = :completeTime,
    UserId = :userId,
    VerifyComment = :verifyComment,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
FROM
    FORM_VERIFY_LOG FVL
JOIN
    FORM F
ON
    F.FormId = FVL.FormId
WHERE
    F.FormId = :formId
AND F.DetailId = :detailId
AND FVL.VerifyLevel = :verfiyLevel
AND FVL.VerifyType = :verfiyType
AND FVL.CompleteTime IS NULL
${CONDITIONS}
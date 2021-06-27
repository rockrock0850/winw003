SELECT
    REVIEW.GroupId,
    SG.GroupName,
    REVIEW.IsAddQuestionIssue,
    REVIEW.IsCloseForm,
    REVIEW.IsApprover,
    REVIEW.IsWaitForSubIssueFinish,
    REVIEW.IsModifyColumnData
FROM
    FORM F
JOIN
    FORM_PROCESS_DETAIL_REVIEW_INC REVIEW
ON
    F.DetailId = REVIEW.DetailId
JOIN 
	SYS_GROUP SG
ON
	REVIEW.GroupId = SG.GroupId
WHERE
    F.FormId = :formId
AND REVIEW.ProcessOrder = :verifyLevel
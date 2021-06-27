SELECT
    REVIEW.GroupId,
    SG.GroupName,
    REVIEW.IsCreateJobIssue,
	REVIEW.IsWaitForSubIssueFinish,
	REVIEW.IsModifyColumnData,
	REVIEW.IsApprover,
	REVIEW.IsCloseForm
FROM
    FORM F
JOIN
    FORM_PROCESS_DETAIL_REVIEW_CHG REVIEW
ON
    F.DetailId = REVIEW.DetailId
JOIN 
	SYS_GROUP SG
ON
	REVIEW.GroupId = SG.GroupId
WHERE
    F.FormId = :formId
AND REVIEW.ProcessOrder = :verifyLevel
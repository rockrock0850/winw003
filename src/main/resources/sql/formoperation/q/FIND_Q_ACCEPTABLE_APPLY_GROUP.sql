SELECT
    APPLY.GroupId,
    SG.GroupName,
    APPLY.IsWaitForSubIssueFinish,
    APPLY.IsModifyColumnData,
    APPLY.IsCreateChangeIssue,
    APPLY.IsApprover,
    APPLY.IsCreateCIssue,
    APPLY.IsParallel
FROM
    FORM F
JOIN
    FORM_PROCESS_DETAIL_APPLY_Q APPLY
ON
    F.DetailId = APPLY.DetailId
JOIN 
	SYS_GROUP SG
ON
	APPLY.GroupId = SG.GroupId
WHERE
    F.FormId = :formId
AND APPLY.ProcessOrder = :verifyLevel
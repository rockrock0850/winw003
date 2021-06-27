SELECT
    APPLY.GroupId,
    SG.GroupName,
	APPLY.IsSrCheckLevel,
	APPLY.IsSrCCheckLevel,
	APPLY.IsQuestionCheckLevel,
	APPLY.IsQuestionCCheckLevel,
	APPLY.IsBusinessImpactAnalysis,
	APPLY.IsApprover,
	APPLY.IsWaitForSubIssueFinish,
	APPLY.IsModifyColumnData
FROM
    FORM F
JOIN
    FORM_PROCESS_DETAIL_APPLY_CHG APPLY
ON
    F.DetailId = APPLY.DetailId
JOIN 
	SYS_GROUP SG
ON
	APPLY.GroupId = SG.GroupId
WHERE
    F.FormId = :formId
AND APPLY.ProcessOrder = :verifyLevel
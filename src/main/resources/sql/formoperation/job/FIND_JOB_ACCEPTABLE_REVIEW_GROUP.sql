SELECT
    REVIEW.GroupId,
    SG.GroupName,
    REVIEW.WorkProject,
	SO.Display AS WorkProjectName,
	REVIEW.IsWorkLevel,
	REVIEW.IsCreateJobCIssue,
	REVIEW.IsWaitForSubIssueFinish,
	REVIEW.NextLevel,
	REVIEW.BackLevel,
    REVIEW.IsCloseForm,
    REVIEW.IsApprover,
    REVIEW.IsModifyColumnData
FROM
    FORM F
JOIN
    FORM_PROCESS_DETAIL_REVIEW_JOB REVIEW
ON
    F.DetailId = REVIEW.DetailId
JOIN 
	SYS_GROUP SG
ON
	REVIEW.GroupId = SG.GroupId
LEFT JOIN 
	SYS_OPTION SO
ON 
	REVIEW.WorkProject = SO.[Value]
WHERE
    F.FormId = :formId
AND REVIEW.ProcessOrder = :verifyLevel
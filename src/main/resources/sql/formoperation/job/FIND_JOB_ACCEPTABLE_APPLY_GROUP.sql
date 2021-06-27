SELECT
    APPLY.GroupId,
    SG.GroupName,
	APPLY.IsWaitForSubIssueFinish,
	APPLY.WorkProject,
	SO.Display AS WorkProjectName,
	APPLY.IsWorkLevel,
	APPLY.IsCreateJobCIssue,
	APPLY.IsCreateSPJobIssue,
	APPLY.IsCreateCompareList,
	APPLY.IsApprover,
	APPLY.IsModifyColumnData,
    APPLY.IsParallel
FROM
    FORM F
JOIN
    FORM_PROCESS_DETAIL_APPLY_JOB APPLY
ON
    F.DetailId = APPLY.DetailId
JOIN 
	SYS_GROUP SG
ON
	APPLY.GroupId = SG.GroupId
LEFT JOIN 
	SYS_OPTION SO
ON 
	APPLY.WorkProject = SO.[Value]
WHERE
    F.FormId = :formId
AND APPLY.ProcessOrder = :verifyLevel
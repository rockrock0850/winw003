SELECT
	FPDJ.DetailId
	,FPDJ.ProcessId
	,FPDJ.ProcessOrder
	,FPDJ.GroupId
	,FPDJ.NextLevel
	,FPDJ.BackLevel
	,FPDJ.IsModifyColumnData
	,FPDJ.CreatedAt
	,FPDJ.CreatedBy
	,FPDJ.UpdatedAt
	,FPDJ.UpdatedBy
	,FPDJ.WorkProject
	,FPDJ.IsWorkLevel
	,FPDJ.IsCloseForm
	,FPDJ.IsCreateJobCIssue
	,FPDJ.IsWaitForSubIssueFinish
	,FPDJ.IsApprover
FROM
	FORM F
JOIN
	FORM_PROCESS_DETAIL_REVIEW_JOB FPDJ
ON
	F.DetailId = FPDJ.DetailId
WHERE
	F.FormId = :formId
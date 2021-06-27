SELECT
	 FPDJ.DetailId
	,FPDJ.ProcessId
	,FPDJ.ProcessOrder
	,FPDJ.GroupId
	,FPDJ.NextLevel
	,FPDJ.BackLevel
	,FPDJ.IsWaitForSubIssueFinish
	,FPDJ.WorkProject
	,FPDJ.IsWorkLevel
	,FPDJ.IsCreateJobCIssue
	,FPDJ.IsCreateCompareList
	,FPDJ.IsModifyColumnData
FROM
	FORM F
JOIN
	FORM_PROCESS_DETAIL_APPLY_JOB FPDJ
ON
	F.DetailId = FPDJ.DetailId
WHERE
	F.FormId = :formId

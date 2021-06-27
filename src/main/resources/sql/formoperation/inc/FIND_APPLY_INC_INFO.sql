SELECT
	GroupId,
    NextLevel,
    BackLevel,
    IsWaitForSubIssueFinish,
    ProcessOrder
FROM
    FORM_PROCESS_DETAIL_APPLY_INC
WHERE
    DetailId = :detailId
AND ProcessOrder = :processOrder
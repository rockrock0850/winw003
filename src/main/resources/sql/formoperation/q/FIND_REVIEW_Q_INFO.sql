SELECT
	GroupId,
    NextLevel,
    BackLevel,
    IsWaitForSubIssueFinish,
    ProcessOrder
FROM
    FORM_PROCESS_DETAIL_REVIEW_Q
WHERE
    DetailId = :detailId
AND ProcessOrder = :processOrder
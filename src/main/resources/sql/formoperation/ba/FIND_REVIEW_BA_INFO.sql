SELECT
    NextLevel,
    BackLevel,
    IsWaitForSubIssueFinish,
    GroupId,
    ProcessOrder
FROM
    FORM_PROCESS_DETAIL_REVIEW_BA
WHERE
    DetailId = :detailId
AND ProcessOrder = :processOrder
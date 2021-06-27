SELECT
    NextLevel,
    BackLevel,
    IsWaitForSubIssueFinish,
    GroupId,
    ProcessOrder
FROM
    FORM_PROCESS_DETAIL_REVIEW_SR
WHERE
    DetailId = :detailId
AND ProcessOrder = :processOrder
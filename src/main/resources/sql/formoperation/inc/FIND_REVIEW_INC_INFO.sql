SELECT
    NextLevel,
    BackLevel,
    IsWaitForSubIssueFinish,
    GroupId,
    ProcessOrder
FROM
    FORM_PROCESS_DETAIL_REVIEW_INC
WHERE
    DetailId = :detailId
AND ProcessOrder = :processOrder
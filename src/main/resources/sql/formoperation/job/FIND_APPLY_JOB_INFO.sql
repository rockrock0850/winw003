SELECT
	GroupId,
    NextLevel,
    BackLevel,
    ProcessOrder
FROM
    FORM_PROCESS_DETAIL_APPLY_JOB
WHERE
    DetailId = :detailId
AND ProcessOrder = :processOrder
SELECT
    PROCESS.DetailId,
    PROCESS.ProcessId,
    PROCESS.ProcessOrder,
    PROCESS.GroupId,
    PROCESS.NextLevel,
    PROCESS.BackLevel,
    SG.GroupName
FROM
    FORM_PROCESS_DETAIL_REVIEW_CHG PROCESS
JOIN
    SYS_GROUP SG
ON
    PROCESS.GroupId = SG.GroupId
WHERE (1=1)
${CONDITIONS}
ORDER BY
    PROCESS.processOrder ASC
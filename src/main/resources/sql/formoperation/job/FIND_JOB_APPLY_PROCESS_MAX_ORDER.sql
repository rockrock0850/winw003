SELECT
   MAX(PROCESS.ProcessOrder) AS ProcessOrder
FROM
    FORM F
JOIN
    FORM_PROCESS_DETAIL_APPLY_JOB PROCESS
ON
    F.DetailId = PROCESS.DetailId
WHERE
    F.FormId = :formId
AND F.DetailId = :detailId
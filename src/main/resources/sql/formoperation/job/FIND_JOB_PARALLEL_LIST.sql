SELECT
    FPDA.Parallels
FROM
    FORM F
JOIN
    FORM_PROCESS_DETAIL_APPLY_JOB FPDA
ON
    F.DetailId = FPDA.DetailId
WHERE
    F.FormId = :formId
AND FPDA.IsParallel = 'Y'
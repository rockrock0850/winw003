SELECT
    F.FormId AS Fn,
    F.FormStatus AS FnStatus,
    CASE
        WHEN (
                SELECT
                    TOP 1 FVL.VerifyLevel
                FROM
                    FORM_VERIFY_LOG FVL
                WHERE
                    FVL.FormId = F.FormId
                AND FVL.VerifyType = 'APPLY'
                ORDER BY
                    FVL.Id DESC) > 1
        THEN 0
        ELSE 1
    END AS FormStatus,
    CONVERT(VARCHAR, FJID.SIT, 23) AS PublishDate
FROM
    FORM F
JOIN
    FORM_JOB_INFO_DATE FJID
ON
    F.FormId = FJID.FormId
WHERE (1=1)
${CONDITIONS}
AND F.FormClass LIKE 'JOB%'
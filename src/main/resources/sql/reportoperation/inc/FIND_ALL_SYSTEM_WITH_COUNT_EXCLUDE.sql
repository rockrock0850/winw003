SELECT DISTINCT
    DETAIL.SystemBrand,
    S.SystemName,
    S.Limit,
    CNT.[Count]
FROM
    (
        SELECT
            FormId,
            FormStatus,
            CreateTime,
            IIF( CHARINDEX('-', DivisionCreated) = 0, '', SUBSTRING( DivisionCreated, 1, CHARINDEX('-', DivisionCreated) - 1 ) ) Division
        FROM
            FORM ) F
JOIN
    FORM_INFO_INC_DETAIL DETAIL
ON
    F.FormId = DETAIL.FormId
JOIN
    [SYSTEM] S
ON
    DETAIL.SystemBrand = S.SystemBrand
JOIN
    (
        SELECT
            SUB_DETAIL.SystemBrand,
            COUNT(SUB_DETAIL.SystemBrand) AS [Count]
        FROM
            (
                SELECT
                    FormId,
                    FormStatus,
                    CreateTime
                FROM
                    FORM ) SUB_F
        JOIN
            FORM_INFO_INC_DETAIL SUB_DETAIL
        ON
            SUB_F.FormId = SUB_DETAIL.FormId
        JOIN
            [SYSTEM] SUB_S
        ON
            SUB_DETAIL.SystemBrand = SUB_S.SystemBrand
        WHERE
    		SUB_S.MboName = 'PROBLEM'
        AND SUB_DETAIL.EventClass = :eventClass
        AND CAST(SUB_F.CreateTime AS DATE) <= CAST(:endDate AS DATE)
        AND CAST(SUB_F.CreateTime AS DATE) >= CAST(:startDate AS DATE)
		AND (SUB_F.FormStatus <> 'PROPOSING' AND SUB_F.FormStatus <> 'DEPRECATED')
        GROUP BY
            SUB_DETAIL.SystemBrand ) AS CNT
ON
    DETAIL.SystemBrand = CNT.SystemBrand
WHERE
    S.MboName = 'PROBLEM'
AND DETAIL.EventClass = :eventClass
AND CAST(F.CreateTime AS DATE) <= CAST(:endDate AS DATE)
AND CAST(F.CreateTime AS DATE) >= CAST(:startDate AS DATE)
AND (F.FormStatus <> 'PROPOSING' AND F.FormStatus <> 'DEPRECATED')
${CONDITIONS}
SELECT
   DETAIL.SystemBrand
FROM
    (  SELECT
            FormId,
            FormStatus,
            CreateTime,
            IIF( CHARINDEX('-', DivisionCreated) = 0, '', SUBSTRING( DivisionCreated, 1, CHARINDEX('-', DivisionCreated) - 1 ) ) Division
        FROM
            FORM
     ) F
JOIN
    FORM_INFO_INC_DETAIL DETAIL
ON
    F.FormId = DETAIL.FormId
JOIN
    [SYSTEM] S
ON
    DETAIL.SystemBrand = S.SystemBrand
WHERE
	DETAIL.EventClass = :eventClass
AND S.MboName = 'PROBLEM'
AND CAST(F.CreateTime AS DATE) <= CAST(:endDate AS DATE)
AND CAST(F.CreateTime AS DATE) >= CAST(:startDate AS DATE)
AND (F.FormStatus <> 'PROPOSING' AND F.FormStatus <> 'DEPRECATED')
${CONDITIONS}
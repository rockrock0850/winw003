SELECT
    Id,
    SystemBrand,
    SystemId,
    SystemName,
    Description,
    Department,
    MboName,
    Mark,
    Opinc,
    Apinc,
    Active,
    UpdatedBy,
    UpdatedAt,
    CreatedBy,
    CreatedAt,
    Limit
FROM
    [SYSTEM]
WHERE (1=1)
${CONDITIONS}
AND MboName = 'PROBLEM'
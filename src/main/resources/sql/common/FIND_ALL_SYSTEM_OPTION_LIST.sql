SELECT
    Id,
    Sort,
    OptionId,
    Name,
    Value,
    Display,
    ParentId,
    UpdatedBy,
    UpdatedAt,
    CreatedBy,
    CreatedAt,
    Active
FROM
    [SYS_OPTION]
WHERE (1=1)
${CONDITIONS} 
AND OptionId = 'StandardChange'
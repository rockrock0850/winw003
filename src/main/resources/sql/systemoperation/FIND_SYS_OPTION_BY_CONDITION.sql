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
    Active,
    IsKnowledge
FROM
    SYS_OPTION
WHERE (1=1) 
${CONDITIONS}
ORDER BY
    Id ASC
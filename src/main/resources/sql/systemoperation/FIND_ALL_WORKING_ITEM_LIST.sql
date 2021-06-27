SELECT
    Id,
    WorkingItemId,
    WorkingItemName,
    SpGroup,
    IsImpact,
    IsReview,
    UpdatedBy,
    UpdatedAt,
    CreatedBy,
    CreatedAt,
    Active
FROM
    [WORKING_ITEM]
WHERE (1=1)
${CONDITIONS} 

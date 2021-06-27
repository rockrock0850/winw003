UPDATE
	WORKING_ITEM	
SET
	SpGroup = :spGroup,
	IsImpact = :isImpact,
	IsReview = :isReview,
	UpdatedBy = :updatedBy,
	UpdatedAt = :updatedAt, 
	Active = :active	
WHERE 
	Id = :id
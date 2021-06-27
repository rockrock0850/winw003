UPDATE
	SYS_OPTION	
SET
	Display = :display,
	Active = :active,
	UpdatedBy = :updatedBy,
	UpdatedAt = :updatedAt 
WHERE 
	Id = :id
UPDATE
	SYSTEM	
SET
	SystemName = :systemName,
	Department = :department,
	Mark = :mark,
	Opinc = :opinc,
	Apinc = :apinc,
	Active = :active,
	Limit = :limit,
	UpdatedBy = :updatedBy,
	UpdatedAt = :updatedAt 
WHERE 
	Id = :id
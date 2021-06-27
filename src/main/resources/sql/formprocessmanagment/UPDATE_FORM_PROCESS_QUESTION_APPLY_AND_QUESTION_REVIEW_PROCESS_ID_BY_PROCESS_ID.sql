UPDATE 
	FORM_PROCESS_DETAIL_APPLY_Q
SET
	ProcessId = '0000000',
	UpdatedAt = :updatedAt ,
	UpdatedBy = :updatedBy
WHERE
	ProcessId = :processId

UPDATE 
	FORM_PROCESS_DETAIL_REVIEW_Q
SET
	ProcessId = '0000000',
	UpdatedAt = :updatedAt ,
	UpdatedBy = :updatedBy
WHERE
	ProcessId = :processId
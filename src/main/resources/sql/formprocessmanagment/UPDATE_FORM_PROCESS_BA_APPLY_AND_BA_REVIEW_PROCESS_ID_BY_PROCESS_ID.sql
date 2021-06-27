UPDATE 
	FORM_PROCESS_DETAIL_APPLY_BA
SET
	ProcessId = '0000000',
	UpdatedAt = :updatedAt ,
	UpdatedBy = :updatedBy
WHERE
	ProcessId = :processId

UPDATE 
	FORM_PROCESS_DETAIL_REVIEW_BA
SET
	ProcessId = '0000000',
	UpdatedAt = :updatedAt ,
	UpdatedBy = :updatedBy
WHERE
	ProcessId = :processId
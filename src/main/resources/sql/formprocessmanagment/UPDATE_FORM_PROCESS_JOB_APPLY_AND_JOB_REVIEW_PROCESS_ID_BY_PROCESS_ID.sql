UPDATE 
	FORM_PROCESS_DETAIL_APPLY_JOB
SET
	ProcessId = '0000000',
	UpdatedAt = :updatedAt ,
	UpdatedBy = :updatedBy
WHERE
	ProcessId = :processId

UPDATE 
	FORM_PROCESS_DETAIL_REVIEW_JOB
SET
	ProcessId = '0000000',
	UpdatedAt = :updatedAt ,
	UpdatedBy = :updatedBy
WHERE
	ProcessId = :processId
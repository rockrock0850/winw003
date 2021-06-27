UPDATE 
	fvl
SET 
	fvl.UserId = :userId,
	fvl.UpdatedAt = getdate(),
	fvl.UpdatedBy = 'system'
FROM 
	FORM_VERIFY_LOG fvl
WHERE 
	fvl.CompleteTime IS NULL
	and fvl.VerifyResult = 'PENDING'
	and fvl.Parallel IS NOT NULL
	and fvl.FormId = :formId
	and fvl.Parallel = :prallel
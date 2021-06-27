UPDATE 
	fvl
SET 
	fvl.UserId = :userSolving,
	fvl.UpdatedAt = getdate(),
	fvl.UpdatedBy = 'system'
FROM 
	FORM_VERIFY_LOG fvl
	INNER JOIN SYS_GROUP SG on SG.GroupId = fvl.GroupId
		and SG.AuthType = 0
WHERE 
	fvl.CompleteTime IS NULL
	and fvl.VerifyResult = 'PENDING'
	and ISNULL(fvl.Parallel, '') = ''
	and fvl.FormId = :formId
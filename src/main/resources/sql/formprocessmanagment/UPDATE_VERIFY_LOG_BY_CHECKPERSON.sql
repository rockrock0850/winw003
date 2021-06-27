UPDATE 
	fvl
SET 
	fvl.UserId = fjcpl.UserId,
	fvl.UpdatedAt = getdate(),
	fvl.UpdatedBy = 'system'
FROM 
	FORM_VERIFY_LOG fvl
INNER JOIN FORM_JOB_CHECK_PERSON_LIST fjcpl on fvl.FormId = fjcpl.FormId
	and fvl.VerifyLevel = fjcpl.Sort
INNER JOIN FORM f on f.FormId = fvl.FormId 
INNER JOIN FORM_PROCESS_DETAIL_APPLY_JOB fpdaj on f.DetailId = fpdaj.DetailId
	and fpdaj.IsWorkLevel = 'Y'
	and fpdaj.ProcessOrder = fvl.VerifyLevel
WHERE 
	CompleteTime IS NULL
	and VerifyType = 'APPLY'
	and VerifyResult = 'PENDING'
	and ISNULL(fvl.Parallel, '') = ''
	and fvl.FormId = :formId
	
	
UPDATE 
	fvl
SET 
	fvl.UserId = fjcpl.UserId,
	fvl.UpdatedAt = getdate(),
	fvl.UpdatedBy = 'system'
FROM 
	FORM_VERIFY_LOG fvl
INNER JOIN FORM_JOB_CHECK_PERSON_LIST fjcpl on fvl.FormId = fjcpl.FormId
	and fvl.VerifyLevel = fjcpl.Sort
INNER JOIN FORM f on f.FormId = fvl.FormId 
INNER JOIN FORM_PROCESS_DETAIL_REVIEW_JOB fpdrj on f.DetailId = fpdrj.DetailId
	and fpdrj.IsWorkLevel = 'Y'
	and fpdrj.ProcessOrder = fvl.VerifyLevel
WHERE 
	CompleteTime IS NULL
	and VerifyType = 'REVIEW'
	and VerifyResult = 'PENDING'
	and ISNULL(fvl.Parallel, '') = ''
	and fvl.FormId = :formId
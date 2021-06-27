SELECT
    f.DetailId,
    fvl.UserId,
    fvl.VerifyLevel,
    fvl.VerifyType
FROM
	FORM f
LEFT JOIN FORM_VERIFY_LOG fvl ON
	f.FormId = fvl.FormId
WHERE
	f.FormId = :formId
AND fvl.completeTime IS NULL
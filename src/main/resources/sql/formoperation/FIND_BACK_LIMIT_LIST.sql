SELECT DISTINCT
	F.FormId,
    F.DetailId,
    FVL.VerifyType,
    FVL.VerifyLevel AS VerifyLevel,
    FVL.VerifyLevel + 0
FROM
    (
    	SELECT * 
		FROM FORM_VERIFY_LOG 
		WHERE ${CONDITIONS}
	) FVL
JOIN
    FORM F
ON
    FVL.FormId = F.FormId
WHERE
FVL.FormId = :formId
AND FVL.VerifyType = :verifyType
AND FVL.VerifyLevel < :verifyLevel
ORDER BY
    FVL.VerifyLevel + 0 DESC
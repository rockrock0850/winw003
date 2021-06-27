WITH TEMP_FORM AS (
  SELECT
    FormId,
    DetailId,
    FormClass,
    FormStatus,
    IsAlterDone,
    IIF(
      CHARINDEX('-', DivisionCreated) = 0,
      '',
      SUBSTRING(
        DivisionCreated,
        1,
        CHARINDEX('-', DivisionCreated) - 1
      )
    ) Division
  FROM
    FORM
)

SELECT DISTINCT
    F.FormId,
    F.DetailId,
    F.FormClass,
    F.IsAlterDone
FROM
    TEMP_FORM F
JOIN 
    FORM_INFO_DATE FID
ON
    F.FormId=FID.FormId
JOIN
    FORM_VERIFY_LOG FVL
ON
    F.FormId=FVL.FormId
WHERE
CASE F.FormClass 
	WHEN :Q THEN (FID.ECT)
	WHEN :SR THEN (FID.ECT)
	WHEN :INC THEN (FID.ECT)
	WHEN :Q_C THEN (FID.MECT)
	WHEN :SR_C THEN (FID.MECT)
	WHEN :INC_C THEN (FID.MECT)
END BETWEEN :startDate AND :endDate
AND	F.IsAlterDone IS NOT NULL
AND ISNUMERIC (FVL.VerifyLevel) = 1
AND FVL.VerifyType = :REVIEW
AND F.FormStatus <> :DEPRECATED
AND (FVL.GroupId LIKE :sc OR FVL.GroupId LIKE :direct OR F.FormStatus = :status)
AND  NOT EXISTS ( SELECT 1 FROM form NEF WHERE NEF.FormId = F.FormId AND F.Division = 'A01421' ) 
${CONDITIONS}
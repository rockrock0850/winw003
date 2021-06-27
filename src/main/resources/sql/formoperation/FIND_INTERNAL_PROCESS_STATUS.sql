SELECT 
	fips.*, 
	fjdm.JobTabName 
FROM 
	FORM_INTERNAL_PROCESS_STATUS fips 
JOIN 
	FORM_JOB_DIVISION_MAPPING fjdm 
ON 
	fips.Division = fjdm.Division 
WHERE
	(1=1)
	${CONDITIONS}
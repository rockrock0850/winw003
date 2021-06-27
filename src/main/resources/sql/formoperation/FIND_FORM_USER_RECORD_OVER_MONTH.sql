SELECT
	DISTINCT(fid.FormId),
	f.UserCreated,
	f.DivisionSolving,
	f.FormClass,
	fid.ECT,
	fid.CreateTime
FROM
	FORM f
LEFT JOIN FORM_INFO_DATE fid ON
	f.FormId = fid.FormId
WHERE
    f.ProcessStatus NOT IN ('CLOSED', 'DEPRECATED', 'PROPOSING')
    AND f.FormClass = 'SR'
	AND fid.ECT IS NOT NULL
	AND (
		SELECT DATEADD(MONTH, :months, fid.CreateTime)) <= fid.ECT

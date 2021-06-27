SELECT fvl_2.FormId, fvl_2.VerifyLevel, fvl_2.UpdatedAt, ori.DivisionSolving, ori.UserCreated, ori.DetailId, ori.FormClass, ori.ECT FROM (
SELECT
	fvl_1.FormId,
	f.DivisionSolving,
	f.UserCreated,
	f.DetailId,
	f.FormClass,
	fid.ECT,
	MAX(fvl_1.Id) AS LogId
FROM
	FORM f
LEFT JOIN FORM_INFO_DATE fid ON
	f.FormId = fid.FormId
LEFT JOIN FORM_VERIFY_LOG fvl_1 ON
	f.FormId = fvl_1.FormId
WHERE
	f.ProcessStatus NOT IN ('CLOSED', 'DEPRECATED', 'PROPOSING')
	AND f.FormClass = 'SR'
	AND fid.ECT IS NOT NULL
	AND fid.ECT < :today
	AND fvl_1.VerifyType = 'REVIEW'
	AND fvl_1.VerifyResult = 'AGREED'
GROUP BY
	fvl_1.FormId,
	f.DetailId,
	f.DivisionSolving,
	f.FormClass,
	f.UserCreated,
	fid.ECT
)ori LEFT JOIN FORM_VERIFY_LOG fvl_2 ON ori.LogId=fvl_2.Id

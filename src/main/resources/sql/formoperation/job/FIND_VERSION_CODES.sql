SELECT
    FJLF.Id,
    FJL.FormId,
    FJL.Result,
    FJL.Time,
    FJL.Content,
    FJL.RowType,
    FJL.QyStatus
FROM
    FORM_JOB_LIBRARY FJL
LEFT JOIN
    FORM_JOB_LIBRARY_FILE FJLF
ON
    FJL.FormId = FJLF.FormId
AND FJL.Time = FJLF.Time
WHERE
	FJL.FormId = :formId 
AND FJL.RowType = :rowType
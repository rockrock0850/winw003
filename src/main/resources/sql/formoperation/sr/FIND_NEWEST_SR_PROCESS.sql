SELECT
    TOP 1 FP.ProcessId,
    FP.ProcessName,
    FPDA.DetailId,
    FP.UpdatedAt
FROM
    FORM_PROCESS FP
JOIN
    FORM_PROCESS_DETAIL_APPLY_SR FPDA
ON
    FP.ProcessId = FPDA.ProcessId
JOIN
    FORM_PROCESS_DETAIL_REVIEW_SR FPDR
ON
    FP.ProcessId = FPDR.ProcessId
AND FPDA.DetailId = FPDR.DetailId
WHERE 
	FP.FormType = :formType
AND FP.IsEnable = :isEnable
AND FP.DepartmentId = :departmentId
AND FP.Division = :division
ORDER BY
    FP.UpdatedAt DESC
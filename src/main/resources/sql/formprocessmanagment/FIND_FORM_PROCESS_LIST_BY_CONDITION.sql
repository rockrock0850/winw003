SELECT DISTINCT
	T1.Id,
	T1.FormType,
	T1.DepartmentId,
	T2.DepartmentName,
	T1.Division,
	T1.ProcessName,
	T1.IsEnable,
	T1.UpdatedAt,
	T1.UpdatedBy
FROM 
	FORM_PROCESS T1
JOIN
	SYS_GROUP T2
ON
	T1.DepartmentId = T2.DepartmentId
WHERE (1=1)

${CONDITIONS}
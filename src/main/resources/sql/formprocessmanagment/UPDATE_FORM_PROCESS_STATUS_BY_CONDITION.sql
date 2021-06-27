UPDATE
	FORM_PROCESS
SET
	IsEnable = :isEnable
WHERE
	Id = :id
AND
	FormType = :formType
AND
	DepartmentId = :departmentId
AND
	Division = :division


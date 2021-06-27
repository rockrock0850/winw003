SELECT
    LU.UserId AS userSolving,
    SG.DepartmentId + '-' + SG.Division AS divisionSolving,
    LU.UserId AS userCreated,
    SG.DepartmentId + '-' + SG.Division AS divisionCreated,
    SG.GroupId AS groupSolving, 
    SG.GroupName
FROM
    LDAP_USER LU
JOIN
    SYS_GROUP SG
ON
    LU.SysGroupId = SG.SysGroupId
WHERE 
	LU.UserId = :userId
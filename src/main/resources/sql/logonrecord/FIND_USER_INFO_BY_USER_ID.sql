SELECT
    LU.*,
    SG.GroupId,
    SG.GroupName,
    SG.DepartmentId,
    SG.Division
FROM
    LDAP_USER LU
JOIN
    SYS_GROUP SG
ON
    LU.SysGroupId = SG.SysGroupId
WHERE 
	LU.UserId = :userId
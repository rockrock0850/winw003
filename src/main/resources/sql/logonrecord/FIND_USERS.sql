SELECT
    LU.Name AS UserName,
    LU.UserId,
    LU.Email,
    SG.GroupId,
    SG.GroupName
FROM
    LDAP_USER LU
JOIN
    SYS_GROUP SG
ON
    LU.SysGroupId = SG.SysGroupId
WHERE (1=1)
${CONDITIONS}
ORDER BY
    LU.Id ASC
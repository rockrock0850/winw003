WITH 
isVsc AS (
	SELECT lu.Email FROM ldap_user lu
	JOIN sys_group sg ON lu.SysGroupId = sg.SysGroupId
	WHERE sg.DepartmentId = :departmentId
	AND sg.Division = :division
	AND sg.GroupId LIKE '%' + :vsc AND lu.IsEnabled = 'Y'
)
,
isSc AS (
	SELECT lu.Email from ldap_user lu
	JOIN sys_group sg ON lu.SysGroupId = sg.SysGroupId
	WHERE sg.DepartmentId = :departmentId
	AND sg.Division = :division
	AND sg.GroupId LIKE '%' + :sc 
	AND sg.GroupId NOT LIKE '%' + :vsc 
	AND lu.IsEnabled = 'Y'
)
,
isPic AS (
	SELECT lu.Email FROM ldap_user lu
	JOIN sys_group sg ON lu.SysGroupId = sg.SysGroupId
	WHERE sg.DepartmentId = :departmentId
	AND sg.Division = :division
	AND sg.GroupId = 'ISWP-' + :division 
	AND lu.IsEnabled = 'Y' 
)

SELECT 
	(STUFF(
    (
        SELECT
            ',' + Email 
        FROM
            isVsc
		FOR XML PATH ('') ), 1, 1, '' )) AS vscMails, 
	(STUFF(
	(
        SELECT
            ',' + Email 
        FROM
            isSc
		FOR XML PATH ('') ), 1, 1, '' )) AS scMails, 
	(STUFF(
    (
        SELECT
            ',' + Email 
        FROM
            isPic
		FOR XML PATH ('') ), 1, 1, '' )) AS picMails 
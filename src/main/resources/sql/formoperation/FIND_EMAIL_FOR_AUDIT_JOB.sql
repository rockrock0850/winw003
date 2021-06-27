WITH isPic AS (
	SELECT lu.Email FROM LDAP_USER lu
	WHERE lu.userid = :pic AND lu.IsEnabled = 'Y'
)
,
isVsc AS (
	SELECT lu.Email FROM ldap_user lu
	JOIN sys_group sg ON lu.SysGroupId = sg.SysGroupId
	WHERE sg.DepartmentId = :departmentId
	AND sg.Division = :division
	AND sg.GroupId LIKE '%VSC' AND lu.IsEnabled = 'Y'
)
,
isSc AS (
	SELECT lu.Email from ldap_user lu
	JOIN sys_group sg ON lu.SysGroupId = sg.SysGroupId
	WHERE sg.DepartmentId = :departmentId
	AND sg.Division = :division
	AND sg.GroupId LIKE '%' + :sc AND lu.IsEnabled = 'Y'
)
,
isD1 AS (
	SELECT lu.Email FROM ldap_user lu
	JOIN dashboard_direct_auth dda ON dda.UserId = lu.UserId
	WHERE division LIKE '%' + :division + '%' AND lu.IsEnabled = 'Y'
)
,
isD2 AS (
	SELECT lu.Email FROM ldap_user lu
	JOIN sys_group sg ON lu.SysGroupId = sg.SysGroupId
	WHERE sg.DepartmentId = :departmentId
	AND sg.GroupId LIKE '%Direct2' AND lu.IsEnabled = 'Y'
)

SELECT
    (STUFF(
    (
        SELECT
            ',' + Email
        FROM
            isPic
		FOR XML PATH ('') ), 1, 1, '' )) AS picMails,
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
            isD1
		FOR XML PATH ('') ), 1, 1, '' )) AS dc1Mails,
	(STUFF(
    (
        SELECT
            ',' + Email
        FROM
            isD2
		FOR XML PATH ('') ), 1, 1, '' )) AS dc2Mails
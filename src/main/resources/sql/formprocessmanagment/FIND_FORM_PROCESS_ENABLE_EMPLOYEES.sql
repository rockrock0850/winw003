SELECT
    u.userId,
	u.Name      AS name,
	u.Email     AS email
FROM
	FORM f
JOIN 
	FORM_PROCESS_DETAIL_APPLY_JOB j
ON 
	f.DetailId = j.DetailId
JOIN
	SYS_GROUP g
ON
	j.GroupId = g.GroupId
JOIN 
	LDAP_USER u
ON
	g.SysGroupId = u.SysGroupId
WHERE
	(1=1)
	${CONDITIONS}

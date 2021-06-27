SELECT
	SLOG.UserId  AS userId,
	LUSER.Name  AS userName,
	SLOG.Status AS status,
	SLOG.Time   AS time
FROM
	SYS_USER_LOG SLOG
LEFT JOIN
	LDAP_USER LUSER
ON
	SLOG.UserId = LUSER.UserId
WHERE
	LUSER.Name LIKE '%' + :userName + '%'
ORDER BY
	SLOG.Time DESC
SELECT 
	lu.UserId      AS userId,
	lu.Name        AS name,
	lu.IsEnabled   AS isEnabled
FROM 
    FORM_JOB_CHECK_PERSON_LIST jp
LEFT JOIN
    LDAP_USER lu
ON
    jp.UserId = lu.UserId
WHERE
    jp.FormId = :formId
AND (jp.UserId IS NOT NULL AND jp.UserId <> '')
SELECT DISTINCT
    SubGroup AS [Value],
    SubGroup AS Wording
FROM
    LDAP_USER
WHERE
    SubGroup IS NOT NULL
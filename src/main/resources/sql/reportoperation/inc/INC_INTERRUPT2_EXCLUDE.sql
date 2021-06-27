WITH
    _form AS
    (
        SELECT
            fm.FormId,
            fm.UserSolving,
            fm.CreateTime,
            fm.division
        FROM
            (
                SELECT
                    f.FormId,
                    f.UserSolving,
                    f.CreateTime,
                    IIF( CHARINDEX('-', f.DivisionSolving) = 0, '', SUBSTRING( f.DivisionSolving, 1
                    , CHARINDEX('-', f.DivisionSolving) - 1 ) ) division
                FROM
                    form f
                WHERE
                    f.formClass = 'JOB_SP'
                AND NOT EXISTS
                    (
                        SELECT
                            1
                        FROM
                            form
                        WHERE
                            f.FormId = FormId
                        AND FormStatus = 'PROPOSING')
                AND NOT EXISTS
                    (
                        SELECT
                            1
                        FROM
                            form
                        WHERE
                            f.FormId = FormId
                        AND FormStatus = 'DEPRECATED')
                AND CONVERT(VARCHAR,f.CreateTime,111) BETWEEN CONVERT(VARCHAR,CAST
                    ( :startDate AS DATETIME),111) AND CONVERT(VARCHAR,CAST
                    ( :endDate AS DATETIME),111) ) fm
    )
SELECT
    f.FormId,
    IIF(f.CreateTime IS NULL,'', CONVERT(VARCHAR(10), f.CreateTime, 111) +' '+ CONVERT(VARCHAR(8),
    f.CreateTime, 108)) AS CreateTime,
    u.Name,
    j.Working,
    IIF(j.OnLineTime IS NULL,'', CONVERT(VARCHAR(10), j.OnLineTime, 111) +' '+ CONVERT(VARCHAR(8),
    j.OnLineTime, 108)) AS OnLineTime,
    IIF(j.OffLineTime IS NULL,'', CONVERT(VARCHAR(10), j.OffLineTime, 111) +' '+ CONVERT(VARCHAR(8)
    , j.OffLineTime, 108)) AS OffLineTime,
    j.Summary
FROM
    (
        SELECT
            FormId,
            UserSolving,
            CreateTime
        FROM
            _form f
        WHERE
            1=1
 AND  NOT EXISTS ( SELECT 1 FROM _form WHERE f.FormId = FormId AND division = 'A01421' ) 
		${CONDITIONS}
) f
LEFT JOIN
    (
        SELECT
            UserId,
            Name
        FROM
            (
                SELECT
                    UserId,
                    Name,
                    ROW_NUMBER() over (partition BY userId ORDER BY updatedAt DESC,CreatedAt DESC)
                    AS num
                FROM
                    LDAP_USER ) l
        WHERE
            l.num = 1 ) u
ON
    u.UserId = f.UserSolving
LEFT JOIN
    (
        SELECT
            FormId,
            IIF(CHARINDEX('_', Working) = 0,Working,SUBSTRING(Working,CHARINDEX ('_', Working) + 1,
            LEN(Working) - CHARINDEX('_', Working) )) Working,
            OnLineTime,
            OffLineTime,
            IsProduction,
            Summary
        FROM
            FORM_JOB_INFO_SYS_DETAIL ) j
ON
    f.FormId = j.FormId
WHERE
    j.IsProduction = 'Y'
    
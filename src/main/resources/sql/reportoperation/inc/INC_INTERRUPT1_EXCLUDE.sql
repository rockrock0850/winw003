WITH
    _form AS
    (
        SELECT
            f.FormId,
            u.Name,
            f.FormStatus,
            f.Division,
            f.section
        FROM
            (
                SELECT
                    formid,
                    FormStatus,
                    formclass,
                    IIF( CHARINDEX('-', DivisionSolving) = 0, '', SUBSTRING( DivisionSolving, 1,
                    CHARINDEX('-', DivisionSolving) - 1 ) ) Division,
                    IIF(CHARINDEX('-', DivisionSolving) = 0,'',SUBSTRING(DivisionSolving,CHARINDEX
                    ('-', DivisionSolving) + 1, LEN(DivisionSolving) - CHARINDEX('-',
                    DivisionSolving) )) section,
                    UserSolving,
                    CreateTime
                FROM
                    form
                WHERE
                    FormClass = 'INC' ) f
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
                            ROW_NUMBER() over (partition BY userId ORDER BY updatedAt DESC,
                            CreatedAt DESC) AS num
                        FROM
                            LDAP_USER ) l
                WHERE
                    l.num = 1 ) u
        ON
            u.UserId = f.UserSolving
        WHERE
            NOT EXISTS
            (
                SELECT
                    1
                FROM
                    form
                WHERE
                    FormId = f.FormId
                AND f.FormStatus = 'PROPOSING' )
        AND NOT EXISTS
            (
                SELECT
                    1
                FROM
                    form
                WHERE
                    FormId = f.FormId
                AND f.FormStatus = 'DEPRECATED' )
        AND CONVERT(VARCHAR,f.CreateTime,111) BETWEEN CONVERT(VARCHAR,CAST
            ( :startDate AS DATETIME),111) AND CONVERT(VARCHAR,CAST
            ( :endDate AS DATETIME),111)
        AND NOT EXISTS
            (
                SELECT
                    1
                FROM
                    form
                WHERE
                    f.FormId = FormId
                AND division = '' )
    )
SELECT
    f.FormId,
    f.Name,
	f.FormStatus,
	f.Division,
	f.section AS Section,
	o.summary AS Summary,
    o.[System],
    IIF(i.CreateTime IS NULL,'', CONVERT(VARCHAR(10), i.CreateTime, 111) +' '+ CONVERT(VARCHAR(8),
    i.CreateTime, 108)) AS CreateTime,
    IIF(i.ExcludeTime IS NULL,'', CONVERT(VARCHAR(10), i.ExcludeTime, 111) +' '+ CONVERT(VARCHAR(8)
    , i.ExcludeTime, 108)) AS ExcludeTime
FROM
    (
        SELECT
            f.FormId,
            f.Name,
            f.FormStatus,
            f.section,
            f.Division 
        FROM
            _form f
        WHERE
            1=1
 AND  NOT EXISTS ( SELECT 1 FROM _form WHERE f.FormId = FormId AND division = 'A01421' ) 
        ${CONDITIONS}
 )f
LEFT JOIN
    (
        SELECT
            formId,
            summary,
            [System]
        FROM
            FORM_INFO_INC_DETAIL ) o
ON
    f.FormId = o.FormId
LEFT JOIN
    (
        SELECT
            FormId,
            CreateTime,
            ExcludeTime,
            IsInterrupt
        FROM
            FORM_INFO_DATE
        WHERE
            FormClass = 'INC' ) i
ON
    f.FormId = i.FormId
WHERE
    i.IsInterrupt = 'Y'
ORDER BY
    f.Division DESC,
    f.section ASC

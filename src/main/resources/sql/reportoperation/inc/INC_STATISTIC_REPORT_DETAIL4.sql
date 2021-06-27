WITH
    _form AS
    (
        SELECT
            FormId,
            SourceId,
            FormStatus,
            UserSolving,
            division,
            section,
            CONVERT(VARCHAR(20),CreateTime,111) AS CreateTime,
            DetailId
        FROM
            (
                SELECT
                    FormId,
                    SourceId,
                    FormStatus,
                    UserSolving,
                    IIF( CHARINDEX('-', f.DivisionSolving) = 0, '', SUBSTRING( f.DivisionSolving, 1
                    , CHARINDEX('-', f.DivisionSolving) - 1 ) ) division,
                    IIF( CHARINDEX('-', f.DivisionSolving) = 0, '', SUBSTRING( f.DivisionSolving,
                    CHARINDEX('-', f.DivisionSolving) + 1, LEN(f.DivisionSolving) - CHARINDEX('-',
                    f.DivisionSolving) ) ) section,
                    CreateTime,
                    DetailId
                FROM
                    form f
                WHERE
                    FormClass = 'INC'
                AND NOT EXISTS
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
                AND NOT EXISTS
                    (
                        SELECT
                            1
                        FROM
                            form
                        WHERE
                            FormId = f.FormId
                        AND f.FormStatus = 'CLOSED' )
                AND CONVERT(VARCHAR,f.CreateTime,111) BETWEEN CONVERT(VARCHAR,CAST
                    ( :startDate AS DATETIME),111) AND CONVERT(VARCHAR,CAST
                    ( :endDate AS DATETIME),111) )f
        WHERE
            1=1
        AND NOT EXISTS
            (
                SELECT
                    1
                FROM
                    form
                WHERE
                    f.FormId = FormId
                AND division = '' )
					${CONDITIONS}
    )
    ,
    _info AS
    (
        SELECT
            FormId,
            Summary
        FROM
            FORM_INFO_INC_DETAIL
    )
    ,
    _detail AS
    (
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
            'APPLY' AS processType
        FROM
            FORM_PROCESS_DETAIL_APPLY_INC
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
            'REVIEW' AS processType
        FROM
            FORM_PROCESS_DETAIL_REVIEW_INC
    )
    ,
    _iNCform AS
    (
        SELECT
            fu.SourceId,
            fu.formid + '/' + fu.section + '/' + name AS cform
        FROM
            (
                SELECT
                    _fo.formid,
                    _fo.SourceId,
                    _fo.UserSolving,
                    IIF( CHARINDEX('-', _fo.DivisionSolving) = 0, '', SUBSTRING
                    ( _fo.DivisionSolving, CHARINDEX('-', _fo.DivisionSolving) + 1, LEN
                    (_fo.DivisionSolving) - CHARINDEX('-', _fo.DivisionSolving) ) )    section,
                    IIF(_u.name IS NULL, '', _u.name)                               AS name
                FROM
                    (
                        SELECT
                            formid,
                            SourceId,
                            UserSolving,
                            DivisionSolving
                        FROM
                            form f
                        WHERE
                        	FormClass = 'INC_C'
                         AND  NOT EXISTS
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
                        AND NOT EXISTS
                            (
                                SELECT
                                    1
                                FROM
                                    form
                                WHERE
                                    FormId = f.FormId
                                AND f.FormStatus = 'CLOSED' ) )_fo
                LEFT JOIN
                    (
                        SELECT
                            lu.UserId,
                            lu.name
                        FROM
                            (
                                SELECT
                                    UserId,
                                    Name,
                                    ROW_NUMBER() OVER ( PARTITION BY UserId ORDER BY UpdatedAt DESC
                                    ) AS newest
                                FROM
                                    LDAP_USER ) lu
                        WHERE
                            newest = 1 ) _u
                ON
                    (
                        _fo.UserSolving = _u.UserId) ) fu
    )
    ,
    _merge AS
    (
        SELECT
            SourceId,
            ( STUFF (
            (
                SELECT
                    ',' + cform
                FROM
                    _iNCform c2
                WHERE
                    c2.SourceId = c1.SourceId FOR xml PATH ('') ), 1, 1, '' ) ) AS newField
        FROM
            _iNCform c1
        GROUP BY
            SourceId
    )
SELECT
    f.FormId,
    f.FormStatus,
    f.CreateTime,
    IIF(u.Name IS NULL,'',u.Name) AS Name,
    f.section,
    i.Summary,
    IIF(d.ECT IS NULL,'',CONVERT(VARCHAR(10),d.ECT,111)) AS ECT,
    IIF(g.GroupName IS NULL,'',g.GroupName)              AS GroupName,
    IIF(_m.newField IS NULL,'',_m.newField)              AS Countersigneds
FROM
    _form f
LEFT JOIN
    _info i
ON
    f.FormId = i.FormId
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
            ECT
        FROM
            FORM_INFO_DATE
        WHERE
            FormClass = 'INC') d
ON
    f.FormId = d.FormId
LEFT JOIN
    (
        SELECT
            l.*
        FROM
            (
                SELECT
                    FormId,
                    CompleteTime,
                    VerifyLevel,
                    VerifyType,
                    submitTime,
                    verifyResult,
                    ROW_NUMBER() OVER ( PARTITION BY FormId ORDER BY updatedAt DESC ) AS newest
                FROM
                    FORM_VERIFY_LOG) l
        WHERE
            l.newest = 1 )AS l
ON
    f.FormId = l.formid
LEFT JOIN
    _detail t
ON
    f.DetailId = t.DetailId
AND l.VerifyType = t.processType
AND l.VerifyLevel = t.ProcessOrder
LEFT JOIN
    (
        SELECT
            s.GroupId,
            s.GroupName
        FROM
            (
                SELECT
                    GroupId,
                    GroupName,
                    ROW_NUMBER() OVER ( PARTITION BY GroupId ORDER BY updatedAt DESC ) AS newest
                FROM
                    SYS_GROUP) s
        WHERE
            s.newest = 1 ) g
ON
    t.GroupId = g.GroupId
LEFT JOIN
    _merge _m
ON
    f.FormId = _m.SourceId
ORDER BY
    f.division,
    f.[section]

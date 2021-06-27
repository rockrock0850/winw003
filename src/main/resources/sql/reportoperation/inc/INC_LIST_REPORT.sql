WITH
    _f AS
    (
        SELECT
            f.formid,
            f.DetailId,
            f.formClass,
            f.FormStatus,
            f.CreateTime,
            f.usersolving,
            IIF( CHARINDEX('-', DivisionCreated) = 0, '', SUBSTRING( DivisionCreated, 1, CHARINDEX
            ('-', DivisionCreated) - 1 ) ) division,
            IIF( CHARINDEX('-', f.DivisionCreated) = 0, '', SUBSTRING( f.DivisionCreated, CHARINDEX
            ('-', f.DivisionCreated) + 1, LEN(f.DivisionCreated) - CHARINDEX('-', f.DivisionCreated
            ) ) ) section
        FROM
            form f
        WHERE
            f.Formclass = 'INC'
        AND NOT EXISTS
            (
                SELECT
                    1
                FROM
                    form
                WHERE
                    FormId = f.FormId
                AND f.FormStatus = 'PROPOSING' )
    )
    ,
    _f2 AS
    (
        SELECT
            *
        FROM
            _f f
        WHERE
            NOT EXISTS
            (
                SELECT
                    1
                FROM
                    _f
                WHERE
                    formid = f.formid
                AND f.FormStatus = 'DEPRECATED' )
    )
    ,
    _form AS
    (
        SELECT
            *
        FROM
            _f2 f
        WHERE
            1=1
		${CONDITIONS}
    )
    ,
    _PROCESS AS
    (
        SELECT
            DetailId,
            CONVERT(nvarchar(10),ProcessOrder) AS ProcessOrder,
            GroupId,
            'APPLY' AS type
        FROM
            FORM_PROCESS_DETAIL_APPLY_INC
        UNION ALL
        SELECT
            DetailId,
            CONVERT(nvarchar(10),ProcessOrder) AS ProcessOrder,
            GroupId,
            'REVIEW' AS type
        FROM
            FORM_PROCESS_DETAIL_REVIEW_INC
    )
    ,
    _INC_C AS
    (
        SELECT
            f.FormId,
            f.SourceId,
            f.UserSolving,
            f.FormStatus,
            IIF( CHARINDEX('-', f.DivisionSolving) = 0, '', SUBSTRING( f.DivisionCreated, CHARINDEX
            ('-', f.DivisionSolving) + 1, LEN(f.DivisionSolving) - CHARINDEX('-', f.DivisionSolving
            ) ) ) section
        FROM
            form f
        WHERE
            f.Formclass = 'INC_C'
        AND NOT EXISTS
            (
                SELECT
                    1
                FROM
                    form
                WHERE
                    FormId = f.FormId
                AND f.FormStatus = 'PROPOSING' )
    )
    ,
    _resultINCC AS
    (
        SELECT
            *
        FROM
            _INC_C f
        WHERE
            NOT EXISTS
            (
                SELECT
                    1
                FROM
                    _INC_C
                WHERE
                    formid = f.formid
                AND f.FormStatus = 'DEPRECATED' )
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
                    _fo.section,
                    IIF(_u.name IS NULL, '', _u.name) AS name
                FROM
                    _resultINCC _fo
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
    f.section,
    f.formid,
    f.FormStatus,
    f.usersolving,
    IIF(f.FormStatus='CLOSED',0,1) AS item,
    convert(varchar,info_date.ECT,120) AS ECT,
    convert(varchar,info_date.CreateTime,120) AS ACT, 
    info_detail.Summary,
    _p.GroupId,
    verify_log.verifylevel,
    IIF(_m.newField IS NULL,'',_m.newField) AS newField
FROM
    (
        SELECT
            fo.*
        FROM
            _form fo
        WHERE
            NOT EXISTS
            (
                SELECT
                    1
                FROM
                    _form
                WHERE
                    fo.FormId = FormId
                AND FormStatus = 'CLOSED'))f
LEFT JOIN
    (
        SELECT
            FormId,
            CreateTime,
            ECT
        FROM
            FORM_INFO_DATE ) info_date
ON
    f.FormId = info_date.FormId
LEFT JOIN
    (
        SELECT
            formid,
            Summary
        FROM
            FORM_INFO_INC_DETAIL ) info_detail
ON
    f.FormId = info_detail.formid
LEFT JOIN
    (
        SELECT
            fvl.formid,
            fvl.verifylevel,
            fvl.VerifyType
        FROM
            (
                SELECT
                    formid,
                    verifylevel,
                    VerifyType,
                    row_number() over(partition BY formid ORDER BY UpdatedAt DESC) AS latest
                FROM
                    FORM_VERIFY_LOG ) fvl
        WHERE
            fvl.latest = 1 ) verify_log
ON
    f.formid = verify_log.formid
LEFT JOIN
    _PROCESS _p
ON
    f.DetailId = _p.DetailId
AND verify_log.verifylevel = _p.ProcessOrder
AND verify_log.VerifyType = _p.type
LEFT JOIN
    _merge _m
ON
    f.FormId = _m.SourceId
    
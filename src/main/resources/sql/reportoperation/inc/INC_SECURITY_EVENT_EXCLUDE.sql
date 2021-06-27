WITH
    _f AS
    (
        SELECT
            f.formid,
            f.DetailId,
            f.formClass,
            f.FormStatus,
            f.CreateTime,
            f.UserCreated,
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
        AND CONVERT(VARCHAR(10),f.CreateTime,111) BETWEEN CONVERT(VARCHAR(10),CAST
            (:startDate AS DATETIME),111) AND CONVERT(VARCHAR(10),CAST
            (:endDate AS DATETIME),111)
 AND  NOT EXISTS ( SELECT 1 FROM _f2 WHERE f.formid = formid AND division = 'A01421' ) 
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
SELECT
	f.division,
    f.[section],
    f.FormId,
    f.FormStatus,
    f.UserCreated,
    IIF(f.FormStatus = 'CLOSED',0,1) as item,
    info_detail.Summary,
    SO.Display AS EventSecurity,
    info_detail.[System],
    verify_log.VerifyLevel,
    _p.GroupId
FROM
    _form f
LEFT JOIN
    (
        SELECT
            formid,
            Summary,
            EventSecurity,
            [System]
        FROM
            FORM_INFO_INC_DETAIL ) info_detail
ON
    f.formid = info_detail.FormId
LEFT JOIN SYS_OPTION SO ON SO.[Value] = info_detail.EventSecurity
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
                    row_number() over(partition BY formid ORDER BY UpdatedAt) AS latest
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
WHERE info_detail.EventSecurity IS NOT NULL AND info_detail.EventSecurity <> ''
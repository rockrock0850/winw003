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
            ('-', DivisionCreated) - 1 ) ) divisionC,           
            IIF( CHARINDEX('-', DivisionSolving) = 0, '', SUBSTRING( DivisionSolving, 1, CHARINDEX
            ('-', DivisionSolving) - 1 ) ) division,
            IIF( CHARINDEX('-', f.DivisionSolving) = 0, '', SUBSTRING( f.DivisionCreated, CHARINDEX
            ('-', f.DivisionSolving) + 1, LEN(f.DivisionSolving) - CHARINDEX('-', f.DivisionSolving
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
            f.FormId ,
            f.DetailId,
            f.FormStatus,
            f.CreateTime,
            f.usersolving,
            f.section,
            IIF(i.ect IS NOT NULL
        AND i.act IS NOT NULL,'Y','N') AS ectActNotNULL,
            i.ect ,
            i.act ,
            inc.EventPriority,
            inc.Summary,
            inc.eventType
        FROM
            _f2 f
        LEFT JOIN
            (
                SELECT
                    formid,
                    ect AS act,
                    ExcludeTime AS ect
                FROM
                    FORM_INFO_DATE
                WHERE
                    FormClass = 'INC') i
        ON
            f.formid = i.formid
        LEFT JOIN
            (
                SELECT
                    *
                FROM
                    FORM_INFO_INC_DETAIL iid
                WHERE
                    NOT EXISTS
                    (
                        SELECT
                            1
                        FROM
                            FORM_INFO_INC_DETAIL
                        WHERE
                            formid = iid.formid
                        AND EventPriority = '' 
                    ) 
            ) inc
        ON
            f.formId = inc.formid
        WHERE
            1=1
        AND f.CreateTime BETWEEN :startDate AND :endDate
			${CONDITIONS}
    )
    ,
    _process AS
    (
        SELECT
            DetailId,
            GroupId,
            CONVERT(VARCHAR(10),ProcessOrder) AS ProcessOrder,
            'APPLY'                           AS VerifyType
        FROM
            FORM_PROCESS_DETAIL_APPLY_INC
        UNION ALL
        SELECT
            DetailId,
            GroupId,
            CONVERT(VARCHAR(10),ProcessOrder) AS ProcessOrder,
            'REVIEW'                          AS VerifyType
        FROM
            FORM_PROCESS_DETAIL_REVIEW_INC
    )
SELECT
    IIF(LEN(result.CreateTime)>=7 ,substring(CONVERT(VARCHAR(10),result.CreateTime, 111),1,4)+substring(CONVERT(VARCHAR(10),result.CreateTime, 111),6,2),'')AS
    pickMonth,
    result.formId,
    result.FormStatus,
    convert(varchar,result.ect,120) AS ECT,
    convert(varchar,result.act,120) AS ACT,
    IIF(result.FormStatus='CLOSED',0,1) AS item,
    result.EventPriority,
    result.Summary,
    result.[section],
    result.usersolving,
    fv_log.VerifyLevel,
    fv_log.VerifyType,
    process_detial.GroupId,
    result.eventType
FROM
    (
        SELECT
            *
        FROM
            _form f
        WHERE
            NOT EXISTS
            (
                SELECT
                    1
                FROM
                    _form
                WHERE
                    f.formid = formid
                AND Ect > Act
                AND ectActNotNULL = 'Y'
                AND FormStatus = 'CLOSED' )) AS result
LEFT JOIN
    (
        SELECT
            *
        FROM
            (
                SELECT
                    formId,
                    VerifyLevel,
                    VerifyType,
                    ROW_NUMBER() OVER ( PARTITION BY FormId ORDER BY UpdatedAt DESC ) newest
                FROM
                    FORM_VERIFY_LOG ) _log
        WHERE
            _log.newest = 1 ) fv_log
ON
    result.formId = fv_log.formId
LEFT JOIN
    _process process_detial
ON
    result.DetailId = process_detial.DetailId
AND fv_log.VerifyLevel = process_detial.ProcessOrder
AND fv_log.VerifyType = process_detial.VerifyType
ORDER BY
    result.EventPriority
    
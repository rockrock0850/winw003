WITH
    _f AS
    (
        SELECT
            f.formid,
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
            f.FormStatus,
            f.CreateTime,
            IIF(i.ect IS NOT NULL
        AND i.act IS NOT NULL,'Y','N') AS ectActNotNULL,
            i.ect ,
            i.act ,
            i.ExcludeTime,
            i.CreateTime AS assignTime,
            inc.EventPriority,
            inc.eventType
        FROM
            _f2 f
        LEFT JOIN
            (
                SELECT
                    formid,
                    act,
                    ect,
                    ExcludeTime,
                    CreateTime
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
                        AND eventType = '' 
                    ) 
            ) inc
        ON
            f.formId = inc.formid
        WHERE
            1=1
        AND f.CreateTime BETWEEN :startDate AND :endDate
 		AND  NOT EXISTS ( SELECT 1 FROM _f2 WHERE f.formId = formId AND divisionC = 'A01421' ) 
		${CONDITIONS}
    )
    ,
    _avgTime AS
    (
        SELECT
            r.EventPriority,
            CAST(CAST(r.timeAvg / (3600*24) AS INT) AS VARCHAR) +':'+ RIGHT('00'+ CAST(CAST
            (r.timeAvg % (3600*24) /3600 AS INT) AS VARCHAR),2) + ':' + RIGHT('00'+ CAST(CAST
            (r.timeAvg % 3600 / 60 AS INT) AS VARCHAR),2) + ':' + RIGHT('00'+ CAST(CAST(r.timeAvg %
            60 AS INT) AS VARCHAR),2) AS hhmmdd,
            r.timeAvg
        FROM
            (
                SELECT
                    EventPriority,
                    CONVERT(DECIMAL(10,0),AVG(CONVERT(DECIMAL,totalSec))) AS timeAvg
                FROM
                    (
                        SELECT
                            EventPriority,
                            CAST(DATEDIFF(ss ,assignTime,ExcludeTime) AS INT) AS totalSec
                        FROM
                            _form
                        WHERE
                            FormStatus = 'CLOSED'
                        AND ectActNotNULL = 'Y') finishTime
                GROUP BY
                    EventPriority ) r
    )
    ,
    _result AS
    (
        SELECT
            '1'      AS ep,
            'all'    AS type,
            COUNT(1) AS num
        FROM
            _form
        WHERE
            EventPriority = '1'
        UNION ALL
        SELECT
            '2'      AS ep,
            'all'    AS type,
            COUNT(1) AS num
        FROM
            _form
        WHERE
            EventPriority = '2'
        UNION ALL
        SELECT
            '3'      AS ep,
            'all'    AS type,
            COUNT(1) AS num
        FROM
            _form
        WHERE
            EventPriority = '3'
        UNION ALL
        SELECT
            '4'      AS ep,
            'all'    AS type,
            COUNT(1) AS num
        FROM
            _form
        WHERE
            EventPriority = '4'
        UNION ALL
        SELECT
            '1'        AS ep,
            'finished' AS type,
            COUNT(1)   AS num
        FROM
            _form
        WHERE
            EventPriority = '1'
        AND Ect > Act
        AND ectActNotNULL = 'Y'
        AND formStatus = 'CLOSED'
        UNION ALL
        SELECT
            '2'        AS ep,
            'finished' AS type,
            COUNT(1)   AS num
        FROM
            _form
        WHERE
            EventPriority = '2'
        AND Ect > Act
        AND ectActNotNULL = 'Y'
        AND formStatus = 'CLOSED'
        UNION ALL
        SELECT
            '3'        AS ep,
            'finished' AS type,
            COUNT(1)   AS num
        FROM
            _form
        WHERE
            EventPriority = '3'
        AND Ect > Act
        AND ectActNotNULL = 'Y'
        AND formStatus = 'CLOSED'
        UNION ALL
        SELECT
            '4'        AS ep,
            'finished' AS type,
            COUNT(1)   AS num
        FROM
            _form
        WHERE
            EventPriority = '4'
        AND Ect > Act
        AND ectActNotNULL = 'Y'
        AND formStatus = 'CLOSED'
        UNION ALL
        SELECT
            '1'          AS ep,
            'unFinished' AS type,
            COUNT(1)     AS num
        FROM
            _form f
        WHERE
            EventPriority = '1'
        AND NOT EXISTS
            (
                SELECT
                    1
                FROM
                    _form
                WHERE
                    formid = f.formid
                AND Ect > Act
                AND ectActNotNULL = 'Y'
                AND formStatus = 'CLOSED')
        UNION ALL
        SELECT
            '2'          AS ep,
            'unFinished' AS type,
            COUNT(1)     AS num
        FROM
            _form f
        WHERE
            EventPriority = '2'
        AND NOT EXISTS
            (
                SELECT
                    1
                FROM
                    _form
                WHERE
                    formid = f.formid
                AND Ect > Act
                AND ectActNotNULL = 'Y'
                AND formStatus = 'CLOSED')
        UNION ALL
        SELECT
            '3'          AS ep,
            'unFinished' AS type,
            COUNT(1)     AS num
        FROM
            _form f
        WHERE
            EventPriority = '3'
        AND NOT EXISTS
            (
                SELECT
                    1
                FROM
                    _form
                WHERE
                    formid = f.formid
                AND Ect > Act
                AND ectActNotNULL = 'Y'
                AND formStatus = 'CLOSED')
        UNION ALL
        SELECT
            '4'          AS ep,
            'unFinished' AS type,
            COUNT(1)     AS num
        FROM
            _form f
        WHERE
            EventPriority = '4'
        AND NOT EXISTS
            (
                SELECT
                    1
                FROM
                    _form
                WHERE
                    formid = f.formid
                AND Ect > Act
                AND ectActNotNULL = 'Y'
                AND formStatus = 'CLOSED')
    )
    ,
    _showResult AS
    (
        SELECT
            IIF(LEN( :startDate )>=10 ,substring( :startDate ,1,10)+'~'+substring( :endDate ,1,10),'') AS pickMonth,
            rt.*,
            IIF(_avg.hhmmdd IS NULL,'0:00:00:00',_avg.hhmmdd) AS hmd,
            IIF(_avg.timeAvg IS NULL,0,_avg.timeAvg)          AS avg
        FROM
            (
                SELECT
                    *,
                    IIF( [ALL] > 0, CONVERT( NUMERIC(17, 2), CONVERT( FLOAT, ( CONVERT
                    (NUMERIC(17, 2), [finished]) / CONVERT(NUMERIC(17, 2), [ALL]) ) * 100 ) ),
                    0 ) AS percentage
                FROM
                    _result AS p PIVOT (SUM(num) FOR type IN ([ALL],
                                                              [finished],
                                                              [unFinished])) AS pt) AS rt
        LEFT JOIN
            _avgTime _avg
        ON
            _avg.EventPriority = rt.ep
    )
SELECT
    *
FROM
    _showResult
UNION ALL
SELECT
    'total'         AS pickMonth,
    '-'              AS ep,
    SUM([ALL])      AS [ALL],
    SUM(finished)   AS finished,
    SUM(unFinished) AS unFinished,
    IIF( SUM([ALL]) > 0, CONVERT( NUMERIC(17, 2), CONVERT( FLOAT, ( CONVERT
                    (NUMERIC(17, 2), SUM(finished)) / CONVERT(NUMERIC(17, 2), SUM([ALL])) ) * 100 ) ),
                    0 ) AS percentage,
    '-'              AS hmd,
    0               AS avg
FROM
    _showResult
GROUP BY
    pickMonth
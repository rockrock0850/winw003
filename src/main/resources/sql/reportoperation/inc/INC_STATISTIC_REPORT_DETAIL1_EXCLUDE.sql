WITH
    _form AS
    (
        SELECT
            FormId
        FROM
            (
                SELECT
                    FormId,
                    FormStatus,
                    IIF( CHARINDEX('-', f.DivisionSolving) = 0, '', SUBSTRING( f.DivisionSolving, 1
                    , CHARINDEX('-', f.DivisionSolving) - 1 ) ) division
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
 AND  NOT EXISTS ( SELECT 1 FROM form WHERE f.FormId = FormId AND division = 'A01421' ) 
				${CONDITIONS}
    )
    ,
    _result AS
    (
        SELECT
            EventClass
        FROM
            _form f
        INNER JOIN
            (
                SELECT
                    FormId,
                    EventClass
                FROM
                    FORM_INFO_INC_DETAIL ) i
        ON
            f.FormId = i.FormId
    )
    ,
    _merge AS
    (
        SELECT
            1      AS [no],
            'exception' AS [type],
            1      AS times
        FROM
            _result
        WHERE
            eventClass = '113'
        UNION ALL
        SELECT
            1      AS [no],
            'exception' AS [type],
            1      AS times
        FROM
            _result
        WHERE
            eventClass = '114'
        UNION ALL
        SELECT
            1      AS [no],
            'exception' AS [type],
            1      AS times
        FROM
            _result
        WHERE
            eventClass = '117'
        UNION ALL
        SELECT
            1      AS [no],
            'exception' AS [type],
            1      AS times
        FROM
            _result
        WHERE
            eventClass = '120'
        UNION ALL
        SELECT
            2      AS [no],
            'request' AS [type],
            1      AS times
        FROM
            _result
        WHERE
            eventClass = '115'
        UNION ALL
        SELECT
            2      AS [no],
            'request' AS [type],
            1      AS times
        FROM
            _result
        WHERE
            eventClass = '116'
        UNION ALL
        SELECT
            2      AS [no],
            'request' AS [type],
            1      AS times
        FROM
            _result
        WHERE
            eventClass = '119'
        UNION ALL
        SELECT
            3      AS [no],
            'counsel' AS [type],
            1      AS times
        FROM
            _result
        WHERE
            eventClass = '111'
        UNION ALL
        SELECT
            3      AS [no],
            'counsel' AS [type],
            1      AS times
        FROM
            _result
        WHERE
            eventClass = '112'
        UNION ALL
        SELECT
            3      AS [no],
            'counsel' AS [type],
            1      AS times
        FROM
            _result
        WHERE
            eventClass = '118'
    )
SELECT
    m.[type] as kind,
    SUM(m.times) AS times
FROM
    (
        SELECT
            [no  ],
            [type],
            times
        FROM
            _merge
        UNION ALL
        SELECT
            4    AS [no],
            'total' AS [type],
            times
        FROM
            _merge) m
GROUP BY
    m.[no],
    m.[type]
ORDER BY
    m.[no]
    
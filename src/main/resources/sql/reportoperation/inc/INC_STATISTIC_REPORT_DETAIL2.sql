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
				${CONDITIONS}
    )
    ,
    _result AS
    (
        SELECT
            IIF(LEN(CONVERT(VARCHAR(10), (id-110)))=1,'0'+CONVERT(VARCHAR(10), (id-110)), CONVERT
            (VARCHAR(10), (id-110))) +'.'+ s.Display AS Display,
            i.EventClass
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
        INNER JOIN
            (
                SELECT
                    id,
                    Value,
                    Display
                FROM
                    SYS_OPTION ) s
        ON
            i.EventClass = s.Value
    )
    ,
    _merge AS
    (
        SELECT
            1 AS [no],
            Display,
            1 AS times
        FROM
            _result
        WHERE
            EventClass = '111'
        UNION ALL
        SELECT
            2 AS [no],
            Display,
            1 AS times
        FROM
            _result
        WHERE
            EventClass = '112'
        UNION ALL
        SELECT
            3 AS [no],
            Display,
            1 AS times
        FROM
            _result
        WHERE
            EventClass = '113'
        UNION ALL
        SELECT
            4 AS [no],
            Display,
            1 AS times
        FROM
            _result
        WHERE
            EventClass = '114'
        UNION ALL
        SELECT
            5 AS [no],
            Display,
            1 AS times
        FROM
            _result
        WHERE
            EventClass = '115'
        UNION ALL
        SELECT
            6 AS [no],
            Display,
            1 AS times
        FROM
            _result
        WHERE
            EventClass = '116'
        UNION ALL
        SELECT
            7 AS [no],
            Display,
            1 AS times
        FROM
            _result
        WHERE
            EventClass = '117'
        UNION ALL
        SELECT
            8 AS [no],
            Display,
            1 AS times
        FROM
            _result
        WHERE
            EventClass = '118'
        UNION ALL
        SELECT
            9 AS [no],
            Display,
            1 AS times
        FROM
            _result
        WHERE
            EventClass = '119'
        UNION ALL
        SELECT
            10 AS [no],
            Display,
            1 AS times
        FROM
            _result
        WHERE
            EventClass = '120'
    )
SELECT
    m.Display AS display ,
    SUM(m.times) AS times
FROM
    (
        SELECT
            [no],
            Display,
            times
        FROM
            _merge
        UNION ALL
        SELECT
            11   AS [no],
            'total' AS Display,
            times
        FROM
            _merge) m
GROUP BY
    m.[no],
    m.[Display]
ORDER BY
    m.[no]
    
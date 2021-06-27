WITH
    _form AS
    (
        SELECT
            FormId,
            FormStatus
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
    _info AS
    (
        SELECT
            FormId,
            ECT,
            IIF(ACT = '',NULL,ACT) AS ACT
        FROM
            (
                SELECT
                    FormId,
                    ECT,
                    ACT
                FROM
                    FORM_INFO_DATE i
                WHERE
                    FormClass = 'INC'
                AND NOT EXISTS
                    (
                        SELECT
                            1
                        FROM
                            FORM_INFO_DATE
                        WHERE
                            i.FormId = FormId
                        AND ECT IS NULL )
                AND NOT EXISTS
                    (
                        SELECT
                            1
                        FROM
                            FORM_INFO_DATE
                        WHERE
                            i.FormId = FormId
                        AND ECT ='')) f
        WHERE
            CONVERT(VARCHAR(10),GETDATE(),111) > CONVERT(VARCHAR(10),f.ECT,111)
    )
    ,
    _detail AS
    (
        SELECT
            FormId,
            EventPriority
        FROM
            FORM_INFO_INC_DETAIL
    )
    ,
    _result AS
    (
        SELECT
            f.FormId,
            d.EventPriority
        FROM
            _form f
        INNER JOIN
            _detail d
        ON
            f.FormId = d.FormId
        INNER JOIN
            (
                SELECT
                    s.formId
                FROM
                    (
                        SELECT
                            FormId
                        FROM
                            _info
                        WHERE
                            ACT IS NULL
                        UNION ALL
                        SELECT
                            FormId
                        FROM
                            (
                                SELECT
                                    FormId,
                                    ACT,
                                    ECT
                                FROM
                                    _info
                                WHERE
                                    ACT IS NOT NULL) i
                        WHERE
                            CONVERT(VARCHAR(10),i.ACT,111) > CONVERT(VARCHAR(10),i.ECT,111)) s
                GROUP BY
                    s.FormId )i
        ON
            f.FormId = i.FormId
    )
    ,
    _merge AS
    (
        SELECT
            1 AS [no],
            EventPriority,
            1 AS times
        FROM
            _result
        WHERE
            EventPriority = '1'
        UNION ALL
        SELECT
            2 AS [no],
            EventPriority,
            1 AS times
        FROM
            _result
        WHERE
            EventPriority = '2'
        UNION ALL
        SELECT
            3 AS [no],
            EventPriority,
            1 AS times
        FROM
            _result
        WHERE
            EventPriority = '3'
        UNION ALL
        SELECT
            4 AS [no],
            EventPriority,
            1 AS times
        FROM
            _result
        WHERE
            EventPriority = '4'
    )
SELECT
    m.EventPriority,
    SUM(m.times) AS times
FROM
    (
        SELECT
            [no],
            EventPriority,
            times
        FROM
            _merge
        UNION ALL
        SELECT
            5    AS [no],
            'total' AS EventPriority,
            1    AS times
        FROM
            _merge) m
GROUP BY
    [no],
    [EventPriority]
ORDER BY
    [no]
    
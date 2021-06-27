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
    _info AS
    (
        SELECT
            i.FormId,
            i.EventClass,
            i.[System]
        FROM
            (
                SELECT
                    FormId,
                    EventClass,
                    [System],
                    IIF(EventClass IS NULL
                OR  EventClass ='',0,IIF(PATINDEX('%[^0-9]%',EventClass)=0,CONVERT(INT,EventClass)
                    ,0)) isNum
                FROM
                    FORM_INFO_INC_DETAIL i
                WHERE
                    NOT EXISTS
                    (
                        SELECT
                            1
                        FROM
                            FORM_INFO_INC_DETAIL
                        WHERE
                            i.FormId = FormId
                        AND [System] IS NULL )
                AND NOT EXISTS
                    (
                        SELECT
                            1
                        FROM
                            FORM_INFO_INC_DETAIL
                        WHERE
                            i.FormId = FormId
                        AND [System] = '' )
                AND NOT EXISTS
                    (
                        SELECT
                            1
                        FROM
                            FORM_INFO_INC_DETAIL
                        WHERE
                            i.FormId = FormId
                        AND EventClass IS NULL )
                AND NOT EXISTS
                    (
                        SELECT
                            1
                        FROM
                            FORM_INFO_INC_DETAIL
                        WHERE
                            i.FormId = FormId
                        AND EventClass = '' ) ) i
        WHERE
            i.isNum BETWEEN 111 AND 120
    )
    ,
    _merge AS
    (
        SELECT
            f.FormId,
            i.EventClass,
            i.[System]
        FROM
            _form f
        LEFT JOIN
            _info i
        ON
            f.FormId = i.FormId
    )
    ,
    _result AS
    (
        SELECT
            [System],
            'one'  AS kind,
            1      AS times
        FROM
            _merge
        WHERE
            EventClass = '111'
        UNION ALL
        SELECT
            [System],
            'two'  AS kind,
            1      AS times
        FROM
            _merge
        WHERE
            EventClass = '112'
        UNION ALL
        SELECT
            [System],
            'three' AS kind,
            1       AS times
        FROM
            _merge
        WHERE
            EventClass = '113'
        UNION ALL
        SELECT
            [System],
            'four' AS kind,
            1      AS times
        FROM
            _merge
        WHERE
            EventClass = '114'
        UNION ALL
        SELECT
            [System],
            'five' AS kind,
            1      AS times
        FROM
            _merge
        WHERE
            EventClass = '115'
        UNION ALL
        SELECT
            [System],
            'six'  AS kind,
            1      AS times
        FROM
            _merge
        WHERE
            EventClass = '116'
        UNION ALL
        SELECT
            [System],
            'seven' AS kind,
            1       AS times
        FROM
            _merge
        WHERE
            EventClass = '117'
        UNION ALL
        SELECT
            [System],
            'eight' AS kind,
            1       AS times
        FROM
            _merge
        WHERE
            EventClass = '118'
        UNION ALL
        SELECT
            [System],
            'nine' AS kind,
            1      AS times
        FROM
            _merge
        WHERE
            EventClass = '119'
        UNION ALL
        SELECT
            [System],
            'ten'  AS kind,
            1      AS times
        FROM
            _merge
        WHERE
            EventClass = '120'
    )
    ,
    _main AS
    (
        SELECT
            [System],
            one                                              AS a0,
            two                                              AS a1,
            three                                            AS a2,
            four                                             AS a3,
            five                                             AS a4,
            six                                              AS a5,
            seven                                            AS a6,
            eight                                            AS a7,
            nine                                             AS a8,
            ten                                              AS a9,
            one+two+three+four+five+six+seven+eight+nine+ten AS total
        FROM
            (
                SELECT
                    [System],
                    IIF([one] IS NULL,0,[one])     AS one,
                    IIF([two] IS NULL,0,[two])     AS two,
                    IIF([three] IS NULL,0,[three]) AS three,
                    IIF([four] IS NULL,0,[four])   AS four,
                    IIF([five] IS NULL,0,[five])   AS five,
                    IIF([six] IS NULL,0,[six])     AS six,
                    IIF([seven] IS NULL,0,[seven]) AS seven,
                    IIF([eight] IS NULL,0,[eight]) AS eight,
                    IIF([nine] IS NULL,0,[nine])   AS nine,
                    IIF([ten] IS NULL,0,[ten])     AS ten
                FROM
                    _result PIVOT (SUM(times) FOR kind IN ([one],
                                                           [two],
                                                           [three],
                                                           [four],
                                                           [five],
                                                           [six],
                                                           [seven],
                                                           [eight],
                                                           [nine],
                                                           [ten]) ) pvt) p
    )
    ,
    _total AS
    (
        SELECT
            [System],
            one                                              AS a0,
            two                                              AS a1,
            three                                            AS a2,
            four                                             AS a3,
            five                                             AS a4,
            six                                              AS a5,
            seven                                            AS a6,
            eight                                            AS a7,
            nine                                             AS a8,
            ten                                              AS a9,
            one+two+three+four+five+six+seven+eight+nine+ten AS total
        FROM
            (
                SELECT
                    [System],
                    IIF([one] IS NULL,0,[one])     AS one,
                    IIF([two] IS NULL,0,[two])     AS two,
                    IIF([three] IS NULL,0,[three]) AS three,
                    IIF([four] IS NULL,0,[four])   AS four,
                    IIF([five] IS NULL,0,[five])   AS five,
                    IIF([six] IS NULL,0,[six])     AS six,
                    IIF([seven] IS NULL,0,[seven]) AS seven,
                    IIF([eight] IS NULL,0,[eight]) AS eight,
                    IIF([nine] IS NULL,0,[nine])   AS nine,
                    IIF([ten] IS NULL,0,[ten])     AS ten
                FROM
                    (
                        SELECT
                            'total' AS [System],
                            kind,
                            times
                        FROM
                            _result) p PIVOT (SUM(p.times) FOR p.kind IN ([one],
                                                                          [two],
                                                                          [three],
                                                                          [four],
                                                                          [five],
                                                                          [six],
                                                                          [seven],
                                                                          [eight],
                                                                          [nine],
                                                                          [ten]) ) pvt) pt
    )
SELECT
    *
FROM
    _main
UNION ALL
SELECT
    *
FROM
    _total

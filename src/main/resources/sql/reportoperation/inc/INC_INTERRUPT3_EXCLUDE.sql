SELECT
    f.FormId,
    f.SourceId,
    i.[System],
    o.Display as display
FROM
    (
        SELECT
            formid,
            sourceid
        FROM
            FORM f
        WHERE
            NOT EXISTS
            (
                SELECT
                    *
                FROM
                    form
                WHERE
                    f.formId = FormId
                AND FormStatus = 'DEPRECATED')
        AND NOT EXISTS
            (
                SELECT
                    *
                FROM
                    form
                WHERE
                    f.formId = FormId
                AND FormStatus = 'PROPOSING') ) f
LEFT JOIN
    (
        SELECT
            FormId,
            SClass,
            [System]
        FROM
            FORM_INFO_INC_DETAIL
        UNION ALL
        SELECT
            FormId,
            SClass,
            [System]
        FROM
            FORM_INFO_Q_DETAIL
        UNION ALL
        SELECT
            FormId,
            SClass,
            [System]
        FROM
            FORM_INFO_SR_DETAIL ) i
ON
    f.FormId = i.FormId
LEFT JOIN
    (
        SELECT
            Display,
            [value]
        FROM
            SYS_OPTION ) o
ON
    i.SClass = o.Value
    
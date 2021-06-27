WITH
    _form AS
    (
        SELECT
            FormId,
            DetailId,
            FormStatus,
            userCreated,
            formclass,
            processStatus,
            isCForm
        FROM
            (
                SELECT
                    formid,
                    DetailId,
                    FormStatus,
                    userCreated,
                    formclass,
                    processStatus,
                    IIF( CHARINDEX('_', FormClass) = 0, '', SUBSTRING( FormClass, 1, CHARINDEX('_',
                    FormClass) - 1 ) ) FormType,
                    IIF(charindex('_',FormClass) = 0,'', SUBSTRING( FormClass, CHARINDEX('_',
                    FormClass) + 1, LEN(FormClass) - CHARINDEX('_', FormClass) )) AS isCForm
                FROM
                    form ) f
        WHERE
            NOT EXISTS
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
                AND f.FormStatus = 'CLOSED' )
        AND NOT EXISTS
            (
                SELECT
                    1
                FROM
                    form
                WHERE
                    FormId = f.FormId
                AND f.FormStatus = 'DEPRECATED' )
    )
    ,
    _kpiForm AS
    (
        SELECT
            *
        FROM
            _form
        WHERE
            FormClass = 'INC'
        UNION ALL
        SELECT
            *
        FROM
            _form
        WHERE
            FormClass = 'Q'
        UNION ALL
        SELECT
            *
        FROM
            _form
        WHERE
            FormClass = 'SR'
    )
    ,
    _luser AS
    (
        SELECT
            lu.UserId,
            lu.Name,
            lu.SysGroupId
        FROM
            (
                SELECT
                    l.UserId,
                    l.Name,
                    l.SysGroupId,
                    row_number() over(partition BY l.userId ORDER BY l.CreatedAt DESC) AS num
                FROM
                    LDAP_USER l) lu
        WHERE
            lu.num = 1
    )
    ,
    _sgroup AS
    (
        SELECT
            sg.SysGroupId,
            sg.GroupId,
            sg.GroupName
        FROM
            (
                SELECT
                    SysGroupId,
                    groupId,
                    GroupName,
                    row_number() over(partition BY groupId ORDER BY updatedAt) AS newset
                FROM
                    SYS_GROUP) sg
        WHERE
            sg.newset = 1
    )
    ,
    _process AS
    (
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
            'INC'   AS formType ,
            'APPLY' AS processType
        FROM
            FORM_PROCESS_DETAIL_APPLY_INC
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
            'Q'     AS formType ,
            'APPLY' AS processType
        FROM
            FORM_PROCESS_DETAIL_APPLY_Q
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
            'SR'    AS formType ,
            'APPLY' AS processType
        FROM
            FORM_PROCESS_DETAIL_APPLY_SR
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
            'INC'    AS formType ,
            'REVIEW' AS processType
        FROM
            FORM_PROCESS_DETAIL_REVIEW_INC
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
            'Q'      AS formType ,
            'REVIEW' AS processType
        FROM
            FORM_PROCESS_DETAIL_REVIEW_Q
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
            'SR'     AS formType ,
            'REVIEW' AS processType
        FROM
            FORM_PROCESS_DETAIL_REVIEW_SR
    )
    ,
    _formLog AS
    (
        SELECT
            fvlout.FormId,
            fvlout.VerifyLevel,
            fvlout.VerifyType,
            fvlout.VerifyResult,
            fvlout.CreatedAt,
            fvlout.SubmitTime,
            fvlout.completeTime
        FROM
            (
                SELECT
                    FormId,
                    VerifyLevel,
                    VerifyType,
                    VerifyResult,
                    CreatedAt,
                    completeTime,
                    submitTime,
                    ROW_NUMBER() over(partition BY fvl.FormId ORDER BY fvl.UpdatedAt DESC) AS
                    newest
                FROM
                    FORM_VERIFY_LOG fvl ) fvlout
        WHERE
            fvlout.newest = 1
    )
SELECT
    f.FormId,
    f.FormClass,
    f.FormStatus,
    f.ProcessStatus,
    u.Name,
    g.GroupName,
    fl.VerifyLevel,
    fl.SubmitTime,
    '' as WorkProject,
    p.GroupId
FROM
    _kpiForm f
LEFT JOIN
    _formLog fl
ON
    f.FormId = fl.FormId
LEFT JOIN
    _process p
ON
    f.DetailId = p.DetailId
AND f.FormClass = p.formType
AND fl.VerifyType = p.processType
AND fl.VerifyLevel = p.ProcessOrder
LEFT JOIN
    _luser u
ON
    f.UserCreated = u.UserId
LEFT JOIN
    _sgroup g
ON
    p.GroupId = g.GroupId
ORDER BY
    f.formClass

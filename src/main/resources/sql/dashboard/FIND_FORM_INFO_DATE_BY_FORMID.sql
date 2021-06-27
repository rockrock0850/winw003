WITH
    _form AS
    (
        SELECT
            formid,
            detailId,
            userCreated,
            FormType,
            isCForm,
            FinalType
        FROM
            (
                SELECT
                    formid,
                    FormStatus,
                    detailId,
                    userCreated,
                    IIF( CHARINDEX('_', FormClass) = 0, '', SUBSTRING( FormClass, 1, CHARINDEX('_',
                    FormClass) - 1 ) ) AS FormType,
                    IIF(charindex('_',FormClass) = 0,'', SUBSTRING( FormClass, CHARINDEX('_',
                    FormClass) + 1, LEN(FormClass) - CHARINDEX('_', FormClass) )) AS isCForm,
                    IIF( CHARINDEX('_', FormClass) = 0, '', SUBSTRING( REVERSE(FormClass), 1,
                    CHARINDEX('_', REVERSE(FormClass)) - 1 ) ) AS FinalType
                FROM
                    form ) f
        WHERE
            f.FormType = 'JOB'
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
    _process AS
    (
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
            'JOB'   AS formType ,
            'APPLY' AS processType
        FROM
            FORM_PROCESS_DETAIL_APPLY_JOB
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
            'JOB'    AS formType ,
            'REVIEW' AS processType
        FROM
            FORM_PROCESS_DETAIL_REVIEW_JOB
    )
    ,
    _formLog AS
    (
        SELECT
            fvlout.FormId,
            fvlout.SubmitTime,
            fvlout.VerifyLevel,
            fvlout.VerifyType,
            fvlout.VerifyResult,
            fvlout.CreatedAt,
            fvlout.completeTime
        FROM
            (
                SELECT
                    FormId,
                    SubmitTime,
                    VerifyLevel,
                    VerifyType,
                    VerifyResult,
                    CreatedAt,
                    completeTime,
                    ROW_NUMBER() over(partition BY fvl.FormId ORDER BY fvl.UpdatedAt DESC) AS
                    newest
                FROM
                    FORM_VERIFY_LOG fvl ) fvlout
        WHERE
            fvlout.newest = 1
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
    _result AS
    (
        SELECT
            f.FormId,
            f.isCForm,
            f.FinalType,
            g.GroupId,
            p.GroupId AS cGroupId
        FROM
            _form f
        LEFT JOIN
            _luser l
        ON
            f.UserCreated = l.UserId
        LEFT JOIN
            _sgroup g
        ON
            l.SysGroupId = g.SysGroupId
        LEFT JOIN
            _formLog lo
        ON
            f.FormId = lo.FormId
        LEFT JOIN
            _process p
        ON
            f.DetailId = p.DetailId
        AND f.FormType = p.formType
        AND lo.VerifyType = p.processType
        AND lo.VerifyLevel = p.ProcessOrder
    )
    ,
    _jobFormId AS
    (
        SELECT
            FormId,
            isCForm
        FROM
            _result
        WHERE
            FinalType = 'C'
        AND cGroupId = :cGroupid
        UNION ALL
        SELECT
            FormId,
            isCForm
        FROM
            _result r
        WHERE
            NOT EXISTS
            (
                SELECT
                    1
                FROM
                    _result
                WHERE
                    r.FormId = FormId
                AND FinalType = 'C' )
        AND GroupId = :groupId
    )
    ,
    _info AS
    (
        SELECT
            Formid,
            ECT,
            'SP' AS [type]
        FROM
            FORM_JOB_INFO_DATE
        WHERE
            ect IS NOT NULL
        UNION ALL
        SELECT
            Formid,
            SCT  AS ECT,
            'AP' AS [type]
        FROM
            FORM_JOB_INFO_DATE
        WHERE
            SCT IS NOT NULL
    )
SELECT
    CONVERT(VARCHAR,MIN(t.ECT),111) AS ECT
FROM
    (
        SELECT
            ECT
        FROM
            (
                SELECT
                    FormId,
                    IIF( CHARINDEX('_', isCForm) = 0, isCForm, SUBSTRING( isCForm, 1, CHARINDEX('_'
                    , isCForm) - 1 ) ) isCForm
                FROM
                    _jobFormId ) j
        INNER JOIN
            _info f
        ON
            j.FormId = f.FormId
        AND j.isCForm = f.[type]) t
WHERE
    CONVERT(VARCHAR,t.ECT, 111) >= CONVERT(VARCHAR,GETDATE(), 111)   
    
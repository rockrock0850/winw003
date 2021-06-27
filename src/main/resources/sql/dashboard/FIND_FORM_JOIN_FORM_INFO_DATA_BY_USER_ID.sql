WITH
    level1 AS
    (
        SELECT
            FormId,
            FormClass,
            FormStatus,
            UserCreated,
            GroupSolving,
            DetailId,
            CreatedAt,
			UserSolving
        FROM
            FORM fout
        WHERE
            NOT EXISTS
            (
                SELECT
                    1
                FROM
                    FORM fin
                WHERE
                    fin.FormStatus = 'PROPOSING'
                AND fin.FormId = fout.FormId )
        AND NOT EXISTS
            (
                SELECT
                    1
                FROM
                    FORM fin
                WHERE
                    fin.FormStatus = 'CLOSED'
                AND fin.FormId = fout.FormId )
        AND NOT EXISTS
            (
                SELECT
                    1
                FROM
                    FORM fin
                WHERE
                    fin.FormStatus = 'DEPRECATED'
                AND fin.FormId = fout.FormId )
		AND NOT EXISTS(
                SELECT
                    1
                FROM
                    FORM fin
                WHERE
                    fin.UserSolving = ''
                AND fin.FormId = fout.FormId			
		)
    )
    ,
    _jobForm AS
    (
        SELECT
            *,
            IIF(charindex('_',isCForm) = 0,isCForm, SUBSTRING( isCForm, CHARINDEX('_', isCForm) + 1
            , LEN(isCForm) - CHARINDEX('_', isCForm) )) AS finalType
        FROM
            (
                SELECT
                    *,
                    IIF(charindex('_',FormClass) = 0,FormClass,SUBSTRING( FormClass, 1, CHARINDEX
                    ('_', FormClass) - 1)) AS sourceForm,
                    IIF(charindex('_',FormClass) = 0,'', SUBSTRING( FormClass, CHARINDEX('_',
                    FormClass) + 1, LEN(FormClass) - CHARINDEX('_', FormClass) )) AS isCForm
                FROM
                    level1) l
        WHERE
            sourceForm = 'JOB'
    )
    ,
    _process AS
    (
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
			WorkProject,
            'JOB'   AS formType ,
            'APPLY' AS processType
        FROM
            FORM_PROCESS_DETAIL_APPLY_JOB
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
			WorkProject,
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
                    ROW_NUMBER() over(partition BY fvl.FormId ORDER BY fvl.UpdatedAt DESC,fvl.completeTime ) AS
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
            *
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
            jf.FormId,
            jf.FormClass,
            jf.FormStatus,
            flog.SubmitTime,
            flog.VerifyResult,
            g.GroupName,
            lu.Name,
            sg.GroupId,
            jf.finalType,
            p.GroupId AS cGroupId,
			p.WorkProject,
			jf.UserSolving
        FROM
            _jobForm jf
        LEFT JOIN
            _formLog flog
        ON
            jf.FormId = flog.FormId
        LEFT JOIN
            _process p
        ON
            jf.DetailId = p.DetailId
        AND flog.VerifyType = p.processType
        AND flog.VerifyLevel = p.ProcessOrder
        AND jf.sourceForm = p.formType
        LEFT JOIN
            _sgroup g
        ON
            p.GroupId = g.GroupId
        LEFT JOIN
            _luser lu
        ON
            jf.UserCreated = lu.UserId
        LEFT JOIN
            _sgroup sg
        ON
            lu.SysGroupId = sg.SysGroupId
    )
SELECT
    FormId,
    FormClass,
    FormStatus,
    SubmitTime,
    VerifyResult,
    GroupName,
    Name,
	WorkProject,
    GroupId
FROM
    _result r
WHERE    
UserSolving = :userSolving

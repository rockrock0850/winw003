WITH
    level1 AS
    (
        SELECT
            FormId,
            FormClass,
            FormStatus,
            UserCreated,
            GroupSolving,
            DetailId
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
    )
    ,
    level2 AS
    (
        SELECT
            *
        FROM
            level1 fout
        WHERE
            NOT EXISTS
            (
                SELECT
                    1
                FROM
                    level1 fin
                WHERE
                    fin.FormStatus = 'CLOSED'
                AND fin.FormId = fout.FormId )
    )
    ,
    level3 AS
    (
        SELECT
            *
        FROM
            level2 fout
        WHERE
            NOT EXISTS
            (
                SELECT
                    1
                FROM
                    level2 fin
                WHERE
                    fin.FormStatus = 'DEPRECATED'
                AND fin.FormId = fout.FormId )
    )
    ,
    _form AS
    (
        SELECT
            *,
            IIF(charindex('_',FormClass) = 0,FormClass,SUBSTRING( FormClass, 1, CHARINDEX('_',
            FormClass) - 1)) AS sourceForm,
            IIF(charindex('_',FormClass) = 0,'', SUBSTRING( FormClass, CHARINDEX('_', FormClass) +
            1, LEN(FormClass) - CHARINDEX('_', FormClass) )) AS isCForm
        FROM
            level3
    )
    ,
    _process_detail_c AS
    (
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
			'' as WorkProject,
            'CHG'   AS formType ,
            'APPLY' AS processType
        FROM
            FORM_PROCESS_DETAIL_APPLY_CHG
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
			'' as WorkProject,
            'INC'   AS formType ,
            'APPLY' AS processType
        FROM
            FORM_PROCESS_DETAIL_APPLY_INC
        UNION ALL
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
			'' as WorkProject,
            'Q'     AS formType ,
            'APPLY' AS processType
        FROM
            FORM_PROCESS_DETAIL_APPLY_Q
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
			'' as WorkProject,
            'SR'    AS formType ,
            'APPLY' AS processType
        FROM
            FORM_PROCESS_DETAIL_APPLY_SR
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
			'' as WorkProject,
            'CHG'    AS formType ,
            'REVIEW' AS processType
        FROM
            FORM_PROCESS_DETAIL_REVIEW_CHG
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
			'' as WorkProject,
            'INC'    AS formType ,
            'REVIEW' AS processType
        FROM
            FORM_PROCESS_DETAIL_REVIEW_INC
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
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
			'' as WorkProject,
            'Q'      AS formType ,
            'REVIEW' AS processType
        FROM
            FORM_PROCESS_DETAIL_REVIEW_Q
        UNION ALL
        SELECT
            detailId,
            CONVERT(nvarchar(30), ProcessOrder) AS ProcessOrder,
            GroupId,
			'' as WorkProject,
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
    _form_detail_summary AS
    (
    	SELECT
            formId,
            summary
		FROM
            FORM_INFO_CHG_DETAIL
		UNION ALL
		SELECT
    		formId,
    		summary
		FROM
		    FORM_INFO_INC_DETAIL
		UNION ALL
		SELECT
		    formId,
		    purpose AS summary
		FROM
		    FORM_JOB_INFO_AP_DETAIL
		UNION ALL    
		SELECT
		    formId,
		    purpose AS summary
		FROM
		    FORM_JOB_INFO_SYS_DETAIL
		UNION ALL
		SELECT
		    formId,
		    summary
		FROM
		    FORM_INFO_Q_DETAIL
		UNION ALL
		SELECT
		    formId,
		    summary
		FROM
		    FORM_INFO_SR_DETAIL
		UNION ALL
		SELECT
		    formId,
		    summary
		FROM
		    FORM_INFO_C_DETAIL
		UNION ALL
		SELECT
		    formId,
		    summary
		FROM
		    FORM_INFO_BA_DETAIL    
    )
    ,
    _result AS
    (
        SELECT
            f.FormId,
            f.FormClass,
            f.FormStatus,
            f.UserCreated,
            l.VerifyResult,
            l.CreatedAt,
			c.WorkProject,
            IIF(c.GroupId IS NOT NULL,c.GroupId,sg.groupId)       AS GroupId,
            IIF(n.GroupName IS NOT NULL,n.GroupName,sg.GroupName) AS GroupName,
            sg.groupId                                            AS GroupId2,
            lu.name,
            s.summary
        FROM
            _form f
        LEFT JOIN
            _formLog l
        ON
            f.FormId = l.FormId
        LEFT JOIN
            _process_detail_c c
        ON
            f.DetailId = c.DetailId
        AND f.sourceForm = c.formType
        AND l.VerifyType = c.processType
        AND l.VerifyLevel = c.ProcessOrder
        LEFT JOIN
            _sgroup n
        ON
            c.GroupId = n.GroupId
        LEFT JOIN
            _luser lu
        ON
            f.UserCreated = lu.UserId
        LEFT JOIN
            _sgroup sg
        ON
            lu.SysGroupId = sg.SysGroupId
        LEFT JOIN
        	_form_detail_summary s
        ON
        	f.FormId = s.formId
    )
SELECT
    r.FormId,
    r.FormClass,
    r.FormStatus,
    r.UserCreated,
    r.VerifyResult,
    r.CreatedAt,
    r.GroupId,
    r.GroupName,
    r.name,
    r.GroupId2,
	r.WorkProject,
    info.Observation,
    r.summary
FROM
    _result r
LEFT JOIN
    (
        SELECT
            FormId,
            CONVERT(VARCHAR,Observation,111) AS Observation
        FROM
            FORM_INFO_DATE ) info
ON
    r.FormId = info.FormId
WHERE
    1=1
${CONDITIONS}
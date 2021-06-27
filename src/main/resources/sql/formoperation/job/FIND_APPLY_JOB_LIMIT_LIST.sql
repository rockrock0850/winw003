SELECT DISTINCT
    *
FROM
    (
		-- 讓表單流程跟作業關卡勾機並過濾PERSON.UserId等於空的資料
		-- ==============================================================
        SELECT DISTINCT
            PROCESS.DetailId,
            PROCESS.ProcessId,
            PROCESS.ProcessOrder,
            PROCESS.GroupId,
            PROCESS.NextLevel,
            PROCESS.BackLevel,
    		PROCESS.IsParallel,
    		SO.Display AS SubGroup,
            SG.GroupName
        FROM
            FORM F
        JOIN
            FORM_PROCESS_DETAIL_APPLY_JOB PROCESS
        ON
            F.DetailId = PROCESS.DetailId
        JOIN
            SYS_GROUP SG
        ON
            PROCESS.GroupId = SG.GroupId
        JOIN
            FORM_JOB_CHECK_PERSON_LIST PERSON
        ON
            F.FormId = PERSON.FormId
        AND PROCESS.ProcessOrder = PERSON.Sort
        AND PROCESS.GroupId = PERSON.GroupId
        JOIN
            SYS_OPTION SO
        ON
            SO.Value = PERSON.Level
        AND PROCESS.WorkProject = SO.Value
        WHERE (1=1)
        ${CONDITIONS}
        AND (PERSON.UserId IS NOT NULL AND PERSON.UserId <> '')
		-- ==============================================================
		
        UNION ALL
		
        -- 工作單當前關卡之後但不是作業關卡(IsWorkLevel = 'N')的項目
		-- ==============================================================
        SELECT
            PROCESS.DetailId,
            PROCESS.ProcessId,
            PROCESS.ProcessOrder,
            PROCESS.GroupId,
            PROCESS.NextLevel,
            PROCESS.BackLevel,
    		PROCESS.IsParallel,
    		SO.Display AS SubGroup,
            SG.GroupName
        FROM
            FORM F
        JOIN
            FORM_PROCESS_DETAIL_APPLY_JOB PROCESS
        ON
            F.DetailId = PROCESS.DetailId
        JOIN
            SYS_GROUP SG
        ON
            PROCESS.GroupId = SG.GroupId
        LEFT JOIN
            FORM_JOB_CHECK_PERSON_LIST PERSON
        ON
            F.FormId = PERSON.FormId
        AND PROCESS.ProcessOrder = PERSON.Sort
        AND PROCESS.GroupId = PERSON.GroupId
        LEFT JOIN
            SYS_OPTION SO
        ON
            SO.Value = PERSON.Level
        AND PROCESS.WorkProject = SO.Value
        WHERE
            F.FormId = :formId
        AND PROCESS.detailId = :detailId
        AND (PROCESS.ProcessOrder > :verifyLevel AND PROCESS.ProcessOrder <= :processOrder)
        AND PROCESS.IsWorkLevel = 'N'
		-- ==============================================================
    ) RSULT
ORDER BY
    RSULT.ProcessOrder ASC
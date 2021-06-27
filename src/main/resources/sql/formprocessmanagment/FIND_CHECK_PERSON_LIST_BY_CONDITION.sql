SELECT DISTINCT
    SP.Value   AS workValue,
    SP.Display AS WorkProject,
    T4.FormId,
    T3.GroupName,
    T5.UserId,
    T5.Name,
    T2.DetailId,
    T2.ProcessId,
    T2.ProcessOrder,
    T2.GroupId,
    T2.NextLevel,
    T2.BackLevel,
    T2.IsWaitForSubIssueFinish,
    T2.IsWorkLevel,
    T2.IsCreateJobCIssue,
    T2.IsCreateCompareList,
    T2.IsModifyColumnData,
    T2.CreatedAt,
    T2.CreatedBy,
    T2.UpdatedAt,
    T2.UpdatedBy
FROM
    FORM T1
JOIN
    FORM_PROCESS_DETAIL_APPLY_JOB T2
ON
    T1.DetailId = T2.DetailId
LEFT JOIN
    SYS_OPTION SP
ON
    T2.WorkProject = SP.[Value]
JOIN
    SYS_GROUP T3
ON
    T3.GroupId = T2.GroupId
LEFT JOIN
    FORM_JOB_CHECK_PERSON_LIST T4
ON
    T1.FormId = T4.FormId
AND T4.Sort = T2.ProcessOrder
LEFT JOIN
    LDAP_USER T5
ON
    T4.UserId = T5.UserId
WHERE (1=1)
${CONDITIONS}
ORDER BY
    T2.ProcessOrder ASC
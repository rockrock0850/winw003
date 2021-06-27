SELECT
    SJOB.JobName,            
    SJOB.JobClass,           
    SJOB.JobGroup,           
    SJOB.JobDescription,     
    QTRIGGERS.NEXT_FIRE_TIME   AS nextFireTime,
    SJOB.RepeatInterval,       
    SJOB.TimeUnit,             
    SJOBLOG.StartTime		   AS lastFireTime,            
    SJOBLOG.Status             AS exeStatus,
    SJOB.Status                AS status
FROM
    SCHEDULE_JOB SJOB
LEFT JOIN
    QRTZ_TRIGGERS QTRIGGERS
ON
    QTRIGGERS.TRIGGER_NAME = SJOB.JobName
LEFT JOIN
    (
        SELECT
            SJOBLOG_1.id,
            SJOBLOG_1.JobName,
            SJOBLOG_1.Status,
            SJOBLOG_1.StartTime,
            SJOBLOG_1.EndTime
        FROM
            SCHEDULE_JOB_LOG SJOBLOG_1
        WHERE
            SJOBLOG_1.id =
            (
                SELECT
                    MAX(id)
                FROM
                    SCHEDULE_JOB_LOG MAXLOG
                WHERE
                    MAXLOG.JobName = SJOBLOG_1.JobName) ) SJOBLOG
ON
    SJOBLOG.JobName = SJOB.JobName
WHERE
	SJOB.JobDescription LIKE '%' + :jobDescription + '%'
ORDER BY
    SJOB.id DESC
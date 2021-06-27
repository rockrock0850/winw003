SELECT
    SJOB.JobName,            
    SJOB.JobDescription,     
    SJOB.JobGroup,           
    SJOB.TimeUnit,           
    SJOB.RepeatInterval,     
    SJOB.JobDescription,     
    SJOB.JobClass,           
    QTRIGGERS.NEXT_FIRE_TIME   AS nextFireTime,
    SJOB.TimeUnit,           
    SJOB.LastFireTime,          
    SJOB.UpdatedBy,          
    SJOB.UpdatedAt,            
    SJOB.Status          
FROM
    SCHEDULE_JOB SJOB
LEFT JOIN
    QRTZ_TRIGGERS QTRIGGERS
ON
    QTRIGGERS.TRIGGER_NAME = SJOB.JobName
WHERE
	SJOB.JobName = :jobName
DELETE
FROM
    SYS_MAIL_LOG
WHERE
    DATEDIFF(MONTH, CreatedAt, GETDATE()) >= :delLimit;
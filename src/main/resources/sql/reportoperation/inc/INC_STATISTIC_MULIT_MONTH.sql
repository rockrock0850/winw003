SELECT
    COALESCE(SUM(
        CASE
            WHEN CAST(F.CreateTime AS DATETIME) BETWEEN CAST(CONCAT(:startDate, '/1/1') AS DATETIME) AND CAST (CONCAT(:startDate, '/2/1') AS DATETIME)
            THEN 1
        END), 0) AS M1,
    COALESCE(SUM(
        CASE
            WHEN CAST(F.CreateTime AS DATETIME) BETWEEN CAST(CONCAT(:startDate, '/2/1') AS DATETIME) AND CAST (CONCAT(:startDate, '/3/1') AS DATETIME)
            THEN 1
        END), 0) AS M2,
    COALESCE(SUM(
        CASE
            WHEN CAST(F.CreateTime AS DATETIME) BETWEEN CAST(CONCAT(:startDate, '/3/1') AS DATETIME) AND CAST (CONCAT(:startDate, '/4/1') AS DATETIME)
            THEN 1
        END), 0) AS M3,
    COALESCE(SUM(
        CASE
            WHEN CAST(F.CreateTime AS DATETIME) BETWEEN CAST(CONCAT(:startDate, '/4/1') AS DATETIME) AND CAST (CONCAT(:startDate, '/5/1') AS DATETIME)
            THEN 1
        END), 0) AS M4,
    COALESCE(SUM(
        CASE
            WHEN CAST(F.CreateTime AS DATETIME) BETWEEN CAST(CONCAT(:startDate, '/5/1') AS DATETIME) AND CAST (CONCAT(:startDate, '/6/1') AS DATETIME)
            THEN 1
        END), 0) AS M5,
    COALESCE(SUM(
        CASE
            WHEN CAST(F.CreateTime AS DATETIME) BETWEEN CAST(CONCAT(:startDate, '/6/1') AS DATETIME) AND CAST (CONCAT(:startDate, '/7/1') AS DATETIME)
            THEN 1
        END), 0) AS M6,
    COALESCE(SUM(
        CASE
            WHEN CAST(F.CreateTime AS DATETIME) BETWEEN CAST(CONCAT(:startDate, '/7/1') AS DATETIME) AND CAST (CONCAT(:startDate, '/8/1') AS DATETIME)
            THEN 1
        END), 0) AS M7,
    COALESCE(SUM(
        CASE
            WHEN CAST(F.CreateTime AS DATETIME) BETWEEN CAST(CONCAT(:startDate, '/8/1') AS DATETIME) AND CAST (CONCAT(:startDate, '/9/1') AS DATETIME)
            THEN 1
        END), 0) AS M8,
    COALESCE(SUM(
        CASE
            WHEN CAST(F.CreateTime AS DATETIME) BETWEEN CAST(CONCAT(:startDate, '/9/1') AS DATETIME) AND CAST (CONCAT(:startDate, '/10/1') AS DATETIME)
            THEN 1
        END), 0) AS M9,
    COALESCE(SUM(
        CASE
            WHEN CAST(F.CreateTime AS DATETIME) BETWEEN CAST(CONCAT(:startDate, '/10/1') AS DATETIME) AND CAST (CONCAT(:startDate, '/11/1') AS DATETIME)
            THEN 1
        END), 0) AS M10,
    COALESCE(SUM(
        CASE
            WHEN CAST(F.CreateTime AS DATETIME) BETWEEN CAST(CONCAT(:startDate, '/11/1') AS DATETIME) AND CAST (CONCAT(:startDate, '/12/1') AS DATETIME)
            THEN 1
        END), 0) AS M11,
    COALESCE(SUM(
        CASE
            WHEN CAST(F.CreateTime AS DATETIME) BETWEEN CAST(CONCAT(:startDate, '/12/1') AS DATETIME) AND CAST (CONCAT(CONVERT(INT, :startDate)+1, '/1/1') AS DATETIME)
            THEN 1
        END), 0) AS M12
FROM
    (  SELECT
            *,
            IIF( CHARINDEX('-', DivisionCreated) = 0, '', SUBSTRING( DivisionCreated, 1, CHARINDEX('-', DivisionCreated) - 1 ) ) Division
        FROM
            FORM
     ) F
JOIN
    FORM_INFO_INC_DETAIL DETAIL
ON
    F.FormId = DETAIL.FormId
WHERE
    F.FormClass = 'INC'
AND F.FormStatus <> 'DEPRECATED'
${CONDITIONS}
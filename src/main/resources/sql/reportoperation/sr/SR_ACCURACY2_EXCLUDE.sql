DECLARE
    @spt_values_custom TABLE (id INT)
    DECLARE
        @i INT SET @i=0 WHILE @i<2048
    BEGIN
        INSERT
        INTO
            @spt_values_custom
        SELECT
            @i SET @i=@i+1
    END ;
WITH
    _f AS
    (
       SELECT
      F.FormId,
      IIF( CHARINDEX('_', FormClass) = 0, FormClass, SUBSTRING( FormClass, CHARINDEX('_',
      FormClass) + 1, LEN(FormClass) - CHARINDEX('_', FormClass) ) ) FormClass,
      IIF( CHARINDEX('-', DivisionCreated) = 0, '', SUBSTRING( DivisionCreated, 1, CHARINDEX
      ('-', DivisionCreated) - 1 ) ) division,
      IIF( CHARINDEX('-', DivisionCreated) = 0, DivisionCreated, SUBSTRING( DivisionCreated,
      CHARINDEX('-', DivisionCreated) + 1, LEN(DivisionCreated) - CHARINDEX('-',
      DivisionCreated) ) ) section,
      CONVERT( VARCHAR(6), fid.sct,112) CreateTime
  FROM
      form F
  JOIN FORM_JOB_INFO_DATE fid on F.FormId = fid.FormId 
  WHERE
      FormStatus = 'CLOSED'
  AND FormClass = 'JOB_AP'
  UNION ALL
  SELECT
      F.FormId,
      IIF( CHARINDEX('_', FormClass) = 0, FormClass, SUBSTRING( FormClass, CHARINDEX('_',
      FormClass) + 1, LEN(FormClass) - CHARINDEX('_ ', FormClass) ) ) FormClass,
      IIF( CHARINDEX('-', DivisionCreated) = 0, '', SUBSTRING( DivisionCreated, 1, CHARINDEX
      ('-', DivisionCreated) - 1 ) ) division,
      IIF( CHARINDEX('-', DivisionCreated) = 0, DivisionCreated, SUBSTRING( DivisionCreated,
      CHARINDEX('-', DivisionCreated) + 1, LEN(DivisionCreated) - CHARINDEX('-',
      DivisionCreated) ) )                 section,
      CONVERT( VARCHAR(6), fid.ACT,112) CreateTime
  FROM
      form F
  JOIN FORM_JOB_INFO_DATE fid on F.FormId = fid.FormId 
  WHERE
      FormStatus = 'CLOSED'
  AND FormClass = 'JOB_SP'
    )
    ,
    _form AS
    (
        SELECT
            *
        FROM
            _f f
        WHERE
            1=1
 AND  NOT EXISTS ( SELECT 1 FROM _f WHERE f.FormId = FormId AND division = 'A01421' ) 
		${CONDITIONS}
    )
    ,
    _spt_values_custom AS
    (
        SELECT
            CONVERT( VARCHAR(6), DATEADD(MONTH, s.id, :startDate ), 112 ) AS [DATE]
        FROM
            @spt_values_custom s
        WHERE
            DATEADD(MONTH, s.id, :startDate ) <= :endDate
    )
    ,
    _result AS
    (
        SELECT
            f.FormId,
            f.[section],
            [DATE],
            IIF(f.FormId IS NULL ,0,1) AS [COUNT]
        FROM
            _spt_values_custom s
        LEFT JOIN
            _form f
        ON
            f.CreateTime = s.[DATE]
    )
SELECT
    [DATE] AS yearmonth,
    IIF([section] IS NULL,'',[section]) AS [section],
    SUM([COUNT]) AS [count]
FROM
    _result
GROUP BY
    [DATE],
    [section]
ORDER BY
    yearmonth DESC
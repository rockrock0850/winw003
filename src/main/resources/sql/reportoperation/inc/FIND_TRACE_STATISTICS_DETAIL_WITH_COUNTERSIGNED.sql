SELECT
    F.Division,
    F.FormId,
    F.UserSolving AS UserId,
    LU.Name,
    F.FormStatus,
    FID.CreateTime,
    FID.ECT,
    DETAIL.Summary,
    Countersigned = STUFF(
	    (
	        SELECT
	            ',' + SUB_F.FormId + '/' + SUB_F.Division + '/' + SUB_LU.Name
	        FROM
	            (  SELECT
			            FormId,
			            SourceId,
			            FormStatus,
			            CreateTime,
			            UserSolving,
            			IIF(
            				CHARINDEX('-', DivisionSolving) = 0, '', SUBSTRING(DivisionSolving, CHARINDEX('-', DivisionSolving) + 1, LEN(DivisionSolving))
        				) Division
			        FROM
			            FORM
			     ) SUB_F
	        LEFT JOIN
	            LDAP_USER SUB_LU
	        ON
	            SUB_F.UserSolving = SUB_LU.UserId
	        WHERE
	            SUB_F.SourceId = F.FormId
			AND CAST(SUB_F.CreateTime AS DATE) <= CAST(:endDate AS DATE)
			AND CAST(SUB_F.CreateTime AS DATE) >= CAST(:startDate AS DATE)
			AND (SUB_F.FormStatus <> 'PROPOSING' AND SUB_F.FormStatus <> 'DEPRECATED') FOR XML PATH ('')
	    ), 1, 1, '' )
FROM
    (
        SELECT
            FormId,
            FormStatus,
            CreateTime,
            UserSolving,
            IIF(
            	CHARINDEX('-', DivisionCreated) = 0, '', SUBSTRING(DivisionCreated, CHARINDEX('-', DivisionCreated) + 1, LEN(DivisionCreated))
        	) Division
        FROM
            FORM ) F
JOIN
    FORM_INFO_INC_DETAIL DETAIL
ON
    F.FormId = DETAIL.FormId
JOIN
    FORM_INFO_DATE FID
ON
    F.FormId = FID.FormId
LEFT JOIN
    LDAP_USER LU
ON
    F.UserSolving = LU.UserId
JOIN
    [SYSTEM] S
ON
    DETAIL.SystemBrand = S.SystemBrand
WHERE
    S.MboName = 'PROBLEM'
AND DETAIL.EventClass = :eventClass
AND CAST(F.CreateTime AS DATE) <= CAST(:endDate AS DATE)
AND CAST(F.CreateTime AS DATE) >= CAST(:startDate AS DATE)
AND (F.FormStatus <> 'PROPOSING' AND F.FormStatus <> 'DEPRECATED')
${CONDITIONS}
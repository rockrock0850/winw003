WITH _formonlyc 
     AS (SELECT f.*
         FROM   (SELECT id, 
                        formid, 
                        sourceid, 
                        detailid, 
                        formclass, 
                        usercreated, 
                        createtime, 
                        formstatus, 
                        groupsolving,
                        divisionSolving,
						UserSolving,
                        Iif(Charindex('_', formclass) = 0, '', 
                        Substring(formclass, 1, 
                        Charindex('_', formclass) - 1))  AS sourceForm, 
                        Iif(Charindex('_', formclass) = 0, '', 
                        Substring(Reverse(formclass), 1, 
                        Charindex('_', Reverse(formclass)) - 1) 
                                ) AS 
                        isCForm 
                 FROM   form) f 
         WHERE  f.iscform = 'C'), 
     _cformfillter 
     AS (SELECT * 
         FROM   _formonlyc f 
         WHERE  NOT EXISTS (SELECT 1 
                            FROM   _formonlyc 
                            WHERE  f.formid = formid 
                                   AND formstatus = 'PROPOSING') 
                AND NOT EXISTS (SELECT 1 
                                FROM   _formonlyc 
                                WHERE  f.formid = formid 
                                       AND formstatus = 'DEPRECATED') 
                AND NOT EXISTS (SELECT 1 
                                FROM   _formonlyc 
                                WHERE  f.formid = formid 
                                       AND formstatus = 'CLOSED')), 
     _process_detail_c 
     AS (SELECT detailid, 
                CONVERT(NVARCHAR(30), processorder) AS ProcessOrder, 
                groupid, 
                ''                                  AS WorkProject, 
                'CHG'                               AS formType, 
                'APPLY'                             AS processType 
         FROM   form_process_detail_apply_chg 
         UNION ALL 
         SELECT detailid, 
                CONVERT(NVARCHAR(30), processorder) AS ProcessOrder, 
                groupid, 
                ''                                  AS WorkProject, 
                'INC'                               AS formType, 
                'APPLY'                             AS processType 
         FROM   form_process_detail_apply_inc 
         UNION ALL 
         SELECT detailid, 
                CONVERT(NVARCHAR(30), processorder) AS ProcessOrder, 
                groupid, 
                workproject, 
                'JOB'                               AS formType, 
                'APPLY'                             AS processType 
         FROM   form_process_detail_apply_job 
         UNION ALL 
         SELECT detailid, 
                CONVERT(NVARCHAR(30), processorder) AS ProcessOrder, 
                groupid, 
                ''                                  AS WorkProject, 
                'Q'                                 AS formType, 
                'APPLY'                             AS processType 
         FROM   form_process_detail_apply_q 
         UNION ALL 
         SELECT detailid, 
                CONVERT(NVARCHAR(30), processorder) AS ProcessOrder, 
                groupid, 
                ''                                  AS WorkProject, 
                'SR'                                AS formType, 
                'APPLY'                             AS processType 
         FROM   form_process_detail_apply_sr 
         UNION ALL 
         SELECT detailid, 
                CONVERT(NVARCHAR(30), processorder) AS ProcessOrder, 
                groupid, 
                ''                                  AS WorkProject, 
                'CHG'                               AS formType, 
                'REVIEW'                            AS processType 
         FROM   form_process_detail_review_chg 
         UNION ALL 
         SELECT detailid, 
                CONVERT(NVARCHAR(30), processorder) AS ProcessOrder, 
                groupid, 
                ''                                  AS WorkProject, 
                'INC'                               AS formType, 
                'REVIEW'                            AS processType 
         FROM   form_process_detail_review_inc 
         UNION ALL 
         SELECT detailid, 
                CONVERT(NVARCHAR(30), processorder) AS ProcessOrder, 
                groupid, 
                workproject, 
                'JOB'                               AS formType, 
                'REVIEW'                            AS processType 
         FROM   form_process_detail_review_job 
         UNION ALL 
         SELECT detailid, 
                CONVERT(NVARCHAR(30), processorder) AS ProcessOrder, 
                groupid, 
                ''                                  AS WorkProject, 
                'Q'                                 AS formType, 
                'REVIEW'                            AS processType 
         FROM   form_process_detail_review_q 
         UNION ALL 
         SELECT detailid, 
                CONVERT(NVARCHAR(30), processorder) AS ProcessOrder, 
                groupid, 
                ''                                  AS WorkProject, 
                'SR'                                AS formType, 
                'REVIEW'                            AS processType 
         FROM   form_process_detail_review_sr) 
SELECT 
       fvl.VerifyLevel, 
       fvl.VerifyType, 
       f.FormStatus AS VerifyResult, 
       fvl.SubmitTime, 
       f.Id, 
       f.FormId, 
       f.DetailId, 
       f.FormClass,
	   f.FormStatus, 
       f.UserCreated, 
       f.CreateTime, 
       f.DivisionSolving,
       getName.NAME, 
       PD.WorkProject, 
       sy.GroupName,
        detail.Summary,
	 formDate.ECT AS finishDate,
	 formDate.ACT AS actualCompDate,
	 formDate.Observation,
	 LU2.Name AS userSolving
FROM   _cformfillter f 
       LEFT JOIN (SELECT formid, 
                                UserId,
                                Parallel,
                                 completetime, 
                                 verifylevel, 
                                 verifytype, 
                                 submittime, 
                                 verifyresult
                          FROM   form_verify_log WHERE completetime IS NULL)AS fvl 
              ON f.formid = fvl.formid 
       LEFT JOIN _process_detail_c PD 
              ON f.detailid = PD.detailid 
                 AND f.sourceform = PD.formtype 
                 AND fvl.verifytype = PD.processtype 
                 AND fvl.verifylevel = PD.processorder 
       LEFT JOIN (SELECT lu.userid, 
                         lu.NAME 
                  FROM   (SELECT l.userid, 
                                 l.NAME, 
                                 Row_number() 
                                   OVER( 
                                     partition BY l.userid 
                                     ORDER BY l.createdat DESC) AS num 
                          FROM   ldap_user l) lu 
                  WHERE  lu.num = 1) getName 
              ON getName.userid = f.usercreated 
          
       LEFT JOIN (SELECT sg.groupid, 
                         sg.groupname 
                  FROM   (SELECT groupid, 
                                 groupname, 
                                 Row_number() 
                                   OVER( 
                                     partition BY groupid 
                                     ORDER BY updatedat DESC) AS num 
                          FROM   sys_group) sg 
                  WHERE  sg.num = 1) sy 
              ON PD.groupid = sy.groupid 
		JOIN
			(
			SELECT 
					F.FormId,
					AP.Purpose AS Summary
			FROM
					FORM F
			JOIN
					FORM_JOB_INFO_AP_DETAIL AP
			ON
					F.FormId = AP.FormId
			UNION ALL
					SELECT 
					F.FormId,
					SP.Summary
			FROM
					FORM F
			JOIN
					FORM_JOB_INFO_SYS_DETAIL SP
			ON
					F.FormId = SP.FormId
			UNION ALL
			SELECT 
					F.FormId,
					C.Summary
			FROM
					FORM F
			JOIN
					FORM_INFO_C_DETAIL C
			ON
					F.FormId = C.FormId
			)detail
		ON
			f.FormId = detail.FormId
			
			
JOIN
        (
		SELECT 
			F.FormId,
			C.ECT,
			C.ACT,
			NULL AS Observation
		FROM
			FORM F
		JOIN
			FORM_JOB_INFO_DATE C
		ON
			F.FormId = C.FormId
		UNION ALL
		SELECT 
			F.FormId,
			C.ECT,
			C.ACT,
			C.Observation
		FROM
			FORM F
		JOIN
			FORM_INFO_DATE C
		ON
			f.SourceId = C.FormId
		) formDate
ON
         f.FormId = formDate.FormId
LEFT JOIN 
		LDAP_USER LU2
ON
		f.UserSolving = LU2.UserId
WHERE  fvl.completetime IS NULL 
AND PD.groupid = :groupId
${CONDITIONS} 

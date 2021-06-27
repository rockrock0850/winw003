SELECT 
	LU2.Name AS userSolving,
	detail.Summary,
	temp.*, 
	temp.UserSolving AS UserSolvingId,
	getName.NAME, 
	gName.GroupName, 
	gName.WorkProject 
FROM   (SELECT 
				form_log.UserId,
				form_log.VerifyLevel, 
               	form_log.VerifyType, 
	            form_log.SubmitTime, 
               _form.FormStatus AS VerifyResult, 
               _form.Id, 
               _form.FormId, 
               _form.DetailId, 
               _form.FormClass, 
               _form.FormStatus, 
               _form.UserCreated, 
               _form.CreateTime,
               _form.UserSolving,
               _form.GroupSolving,
               _form.UpdatedAt,
               _form.DivisionSolving
        FROM  (SELECT * 
               FROM  (SELECT f.*, 
                             Row_number() 
                               OVER( 
                                 partition BY f.formid 
                                 ORDER BY f.updatedat DESC, f.completetime ) AS newest, 
                             IIF(Charindex('-', f.formid) = 0, '', 
                             Substring(f.formid, 1, 
                             Charindex('-', f.formid) - 1)) AS formtype 
                      FROM   form_verify_log f 
                      WHERE  f.verifytype = 'APPLY' 
                             AND f.completetime IS NULL) pvl 
               WHERE  pvl.newest = 1 
                      AND pvl.formtype = 'CHG') form_log 
              INNER JOIN(SELECT * 
                         FROM   (SELECT * 
                                 FROM   form fc 
                                 WHERE  NOT EXISTS(SELECT 1 
                                                   FROM   form f 
                                                   WHERE  f.formstatus = 
                                                          'CLOSED' 
                                                          AND fc.formid = 
                                                              f.formid)) 
                                tmpClosed 
                         WHERE  NOT EXISTS(SELECT 1 
                                           FROM   form f 
                                           WHERE  f.formstatus = 'DEPRECATED' 
                                                  AND tmpClosed.formid = 
                                                      f.formid)) 
                        _form 
                      ON form_log.formid = _form.formid) temp 
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
              ON getName.userid = temp.usercreated 
       LEFT JOIN(SELECT pro.*, 
                        sy.groupname, 
                        '' AS WorkProject 
                 FROM   (SELECT detailid, 
                                processorder, 
                                groupid 
                         FROM   form_process_detail_apply_chg) pro 
                        LEFT JOIN (SELECT sg.groupid, 
                                          sg.groupname 
                                   FROM   (SELECT groupid, 
                                                  groupname, 
                                                  Row_number() 
                                                    OVER( 
                                                      partition BY groupid 
                                                      ORDER BY updatedat DESC) 
                                                  AS num 
                                           FROM   sys_group) sg 
                                   WHERE  sg.num = 1) sy 
                               ON pro.groupid = sy.groupid)gName 
              ON ( temp.detailid = gName.detailid 
                   AND temp.verifylevel = 
                       CONVERT(NVARCHAR(10), gName.processorder) ) 
JOIN
        FORM_INFO_CHG_DETAIL detail
ON
        temp.FormId = detail.FormId
LEFT JOIN 
		LDAP_USER LU2
ON
		temp.UserSolving = LU2.UserId
WHERE  EXISTS(SELECT 1 
              FROM   form_process_detail_apply_chg PD 
              WHERE  PD.detailid = temp.detailid 
                     AND temp.verifylevel = 
                         CONVERT(NVARCHAR(10), PD.processorder) 
             	AND PD.GroupId = :groupId
             ) 
             ${CONDITIONS} 
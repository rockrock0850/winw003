WITH
-- 從FIND_FORM_PROCESS_DETAIL_REVIEW_XXX_BY_FORMID複製過來的語法
    ORI AS
    (
		SELECT
			LU2.Name AS userSolving,
			detail.Summary,
			formDate.SCT AS finishDate,
			formDate.ACT AS actualCompDate,
			temp.UserId,
			temp.VerifyLevel,
			temp.VerifyType,
			temp.VerifyResult,
			temp.SubmitTime,
			temp.FormStatus,
			temp.FormId,
			temp.DetailId,
			temp.FormClass,
			temp.UserCreated,
			temp.CreateTime,
            temp.DivisionCreated,
      		temp.GroupSolving,
			getName.NAME,
			gName.GroupName,
			gName.WorkProject
		FROM
			(
				SELECT
					form_log.UserId,
					form_log.VerifyLevel,
					form_log.VerifyType,
					form_log.VerifyResult,
					form_log.SubmitTime,
					_form.FormStatus,
					_form.Id,
					_form.FormId,
					_form.DetailId,
					_form.FormClass,
					_form.UserCreated,
					_form.CreateTime,
					_form.UserSolving,
                    _form.DivisionCreated,
               		_form.GroupSolving,
               		_form.DivisionSolving
				FROM
					(
						SELECT
							*
						FROM
							(
								SELECT
									f.*,
									Row_number() OVER( partition BY f.formid ORDER BY f.updatedat DESC,
									f.completetime) AS newest,
									Iif(Charindex('-', f.formid) = 0, '', Substring(f.formid, 1, Charindex
									('-', f.formid) - 1)) AS formtype
								FROM
									form_verify_log f
								WHERE
									f.verifytype = 'REVIEW'
								AND f.completetime IS NULL) pvl
						WHERE
							pvl.newest = 1
						AND pvl.formtype = 'JOB') form_log
				INNER JOIN
					(
						SELECT
							*
						FROM
							(
								SELECT
									*
								FROM
									form fc
								WHERE
									NOT EXISTS
									(
										SELECT
											1
										FROM
											form f
										WHERE
											f.formstatus = 'CLOSED'
										AND fc.formid = f.formid)) tmpClosed
						WHERE
							NOT EXISTS
							(
								SELECT
									1
								FROM
									form f
								WHERE
									f.formstatus = 'DEPRECATED'
								AND tmpClosed.formid = f.formid)) _form
				ON
					form_log.formid = _form.formid) temp
		LEFT JOIN
			(
				SELECT
					lu.userid,
					lu.NAME
				FROM
					(
						SELECT
							l.userid,
							l.NAME,
							Row_number() OVER( partition BY l.userid ORDER BY l.createdat DESC) AS num
						FROM
							ldap_user l) lu
				WHERE
					lu.num = 1) getName
		ON
			getName.userid = temp.usercreated
		LEFT JOIN
			(
				SELECT
					pro.*,
					sy.groupname
				FROM
					(
						SELECT
							detailid,
							processorder,
							workproject,
							groupid
						FROM
							form_process_detail_review_job) pro
				LEFT JOIN
					(
						SELECT
							sg.groupid,
							sg.groupname
						FROM
							(
								SELECT
									groupid,
									groupname,
									Row_number() OVER( partition BY groupid ORDER BY updatedat DESC) AS num
								FROM
									sys_group) sg
						WHERE
							sg.num = 1) sy
				ON
					pro.groupid = sy.groupid)gName
		ON
			(
				temp.detailid = gName.detailid
			AND temp.verifylevel = CONVERT(NVARCHAR(10), gName.processorder) )
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
					F.FormId = SP.FormId ) detail
		ON
			temp.FormId = detail.FormId
		JOIN
			FORM_JOB_INFO_DATE formDate
		ON
			temp.FormId = formDate.FormId
		LEFT JOIN
			LDAP_USER LU2
		ON
			temp.UserSolving = LU2.UserId
		WHERE
			EXISTS
			(
				SELECT
					1
				FROM
					form_process_detail_review_job PD
				WHERE
					PD.detailid = temp.detailid
				AND temp.verifylevel = CONVERT(NVARCHAR(10), PD.processorder) 
				${CONDITIONS} )
    )
    ,
-- 從DASHBOARD_DIRECT_AUTH分割副理能夠審核的科別
    AUTH AS
    (
        SELECT
            Split.a.value('.', 'NVARCHAR(20)') DATA
        FROM
            (
                SELECT
                    CAST('<X>'+REPLACE(
                    (
                        SELECT
                            Division
                        FROM
                            DASHBOARD_DIRECT_AUTH
                        WHERE
                            UserId = :userId ), ',', '</X><X>')+'</X>' AS XML) AS String ) AS A CROSS
            APPLY String.nodes('/X') AS Split(a)
    )

-- 過濾副理能夠審核的表單
SELECT
    *
FROM
    ORI
WHERE
    SUBSTRING(DivisionCreated, CHARINDEX('-', DivisionCreated)+1, LEN(DivisionCreated)) IN (SELECT * FROM AUTH);
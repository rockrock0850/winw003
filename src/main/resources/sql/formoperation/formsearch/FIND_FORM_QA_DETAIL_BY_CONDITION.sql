SELECT DISTINCT
	f.FormClass          AS formClass,
	f.FormId             AS formId,
	f.FormStatus         AS formStatus,
	f.CreateTime         AS createTime,
	f.DivisionCreated    AS divisionCreated,
	lUser.Name           AS userCreated,
	f.DivisionSolving    AS divisionSolving,
	lUS.Name             AS userSolving,
	UCOpt.Display        AS unitCategory,
	u.UnitId             AS unitId,
	u.UserName           AS userName,
	questionOpt.Display  AS questionId,
	q.Summary            AS summary,
	q.Content            AS content,
	q.questionPriority   AS questionPriority,
	d.IsSpecial          AS isSpecial,
	SECTOpt.Display      AS specialEndCaseType,
	d.ReportTime         AS reportTime,
	d.ECT                AS ect,
	d.AST                AS ast,
	d.ACT                AS act,
	d.Observation        AS observation,
	sClassOpt.Display    AS sClass,
	sSubClass.Display    AS sSubClass,
	q.System             AS system,
	q.AssetGroup         AS assetGroup,
	q.CCategory          AS cCategory,
	q.CClass             AS cClass,
	q.CComponent         AS cComponent,
	ESOpt.Display        AS effectScope,
	QULOpt.Display       AS urgentLevel,
	q.Countersigneds     AS countersigneds,
	fp.ProcessProgram    AS processProgram,
	fp.Indication        AS indication,
	fp.Reason            AS reason,
	fp.IsSuggestCase     AS isSuggestCase,
	knowledge1.Display   AS knowledge1,
	knowledge2.Display   AS Knowledge2,
	f.SourceId			 AS sourceId
FROM
	FORM f WITH (NOLOCK)
LEFT JOIN
	FORM_INFO_USER u WITH (NOLOCK)
ON
	f.FormId = u.FormId
LEFT JOIN
	FORM_INFO_Date d WITH (NOLOCK)
ON
	f.FormId = d.FormId
LEFT JOIN
	FORM_INFO_Q_DETAIL q WITH (NOLOCK)
ON
	f.FormId = q.FormId
LEFT JOIN
	FORM_PROGRAM fp WITH (NOLOCK)
ON
	f.FormId = fp.FormId
LEFT JOIN
	LDAP_USER lUser WITH (NOLOCK)
ON
	f.UserCreated = lUser.UserId
LEFT JOIN
	LDAP_USER lUS WITH (NOLOCK)
ON
	f.UserSolving = lUS.UserId
LEFT JOIN
	SYS_OPTION sClassOpt WITH (NOLOCK)
ON
	sClassOpt.OptionId = '2'
AND
	q.SClass = sClassOpt.Value
LEFT JOIN
	SYS_OPTION UCOpt WITH (NOLOCK)
ON
	UCOpt.OptionId = '1'
AND
	u.UnitCategory = UCOpt.Value
LEFT JOIN
	SYS_OPTION SECTOpt WITH (NOLOCK)
ON
	SECTOpt.OptionId = 'SPECIAL_END_CASE_TYPE'
AND
	d.SpecialEndCaseType = SECTOpt.Value
LEFT JOIN
	SYS_OPTION questionOpt WITH (NOLOCK)
ON
	questionOpt.OptionId = '3'
AND
	u.questionId = questionOpt.Value
LEFT JOIN
	SYS_OPTION ESOpt WITH (NOLOCK)
ON
	ESOpt.OptionId = 'EFFECT_SCOPE'
AND
	q.EffectScope = ESOpt.Value
LEFT JOIN
	SYS_OPTION QULOpt WITH (NOLOCK)
ON
	QULOpt.OptionId = 'Q_URGENT_LEVEL'
AND
	q.UrgentLevel = QULOpt.Value
LEFT JOIN
	SYS_OPTION sSubClass WITH (NOLOCK)
ON
	q.sSubClass = sSubClass.Value
LEFT JOIN
	SYS_OPTION knowledge1 WITH (NOLOCK)
ON
	fp.Knowledge1 = knowledge1.Value
LEFT JOIN
	SYS_OPTION knowledge2 WITH (NOLOCK)
ON
	fp.Knowledge2 = knowledge2.Value
WHERE (1=1)
AND f.FormClass = 'Q'
AND f.FormStatus = 'CLOSED'
AND fp.IsSuggestCase = 'Y'
AND fp.Knowledge1 IS NOT NULL
AND fp.Knowledge2 IS NOT NULL
${CONDITIONS}
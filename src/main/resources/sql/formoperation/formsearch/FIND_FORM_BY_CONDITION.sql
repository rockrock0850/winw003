SELECT
	f.FormClass  AS formClass,
	f.FormId     AS formId,
	f.FormStatus AS formStatus,
	f.CreateTime AS createTime
FROM
	FORM f
WHERE (1=1)
${CONDITIONS}
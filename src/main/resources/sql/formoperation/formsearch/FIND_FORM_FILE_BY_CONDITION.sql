SELECT
	f.FormId       AS formId,
	f.Name         AS name,
	f.Description  AS description
FROM
	FORM_FILE f
WHERE (1=1)
${CONDITIONS}
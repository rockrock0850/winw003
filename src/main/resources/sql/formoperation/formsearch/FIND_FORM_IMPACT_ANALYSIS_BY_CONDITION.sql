SELECT
	f.FormId          AS formId,
	f.TargetFraction  AS targetFraction,
	f.Description     AS description
FROM
	FORM_IMPACT_ANALYSIS f
WHERE (1=1)
${CONDITIONS}
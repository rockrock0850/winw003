UPDATE
    FORM_PROGRAM
SET
    Indication = :indication,
    Reason = :reason,
    ProcessProgram = :processProgram,
    DeprecatedReason = :deprecatedReason,
    IsSuggestCase = :isSuggestCase,
    Temporary = :temporary,
    Knowledges = :knowledges,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
	
UPDATE
	FIKD
SET
	FIKD.IsEnabled = :isSuggestCase,
    FIKD.UpdatedBy = :updatedBy,
    FIKD.UpdatedAt = :updatedAt
FROM
    FORM_INFO_KL_DETAIL FIKD
JOIN
	FORM F
ON 
	F.FormId = FIKD.FormId
WHERE
    F.sourceId = :formId;
UPDATE
    FORM
SET
    FormStatus = :formStatus,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
	
UPDATE
    FORM_INFO_DATE
SET
    Observation = :observation,
    IsSpecial = :isSpecial,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
	
UPDATE
    FORM_INFO_USER
SET
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
	
UPDATE
    FORM_INFO_Q_DETAIL
SET
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
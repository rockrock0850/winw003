SELECT
    FUR.Id,
    LU.Name,
    FUR.FormId,
    FUR.Summary,
    FUR.UpdatedBy,
    FUR.UpdatedAt,
    FUR.CreatedBy,
    FUR.CreatedAt
FROM
    FORM_USER_RECORD FUR
JOIN
    LDAP_USER LU
ON
    LU.UserId = FUR.UpdatedBy
WHERE
FUR.FormId = :formId
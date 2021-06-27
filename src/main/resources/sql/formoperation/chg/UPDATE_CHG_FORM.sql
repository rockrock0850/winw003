UPDATE
    FORM
SET
    DetailId = :detailId,
    FormClass = :formClass,
    FormStatus = :formStatus,
    ProcessStatus = :processStatus,
    DivisionCreated = :divisionCreated,
    UserCreated = :userCreated,
    DivisionSolving = :divisionSolving,
    UserSolving = :userSolving,
    GroupSolving = :groupSolving,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
	
UPDATE
    FORM_INFO_DATE
SET
    FormClass = :formClass,
    CAT = :cat,
    CCT = :cct,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
	
UPDATE
    FORM_INFO_USER
SET
    FormClass = :formClass,
    UnitId = :unitId,
    UserName = :userName,
    Phone = :phone,
    Email = :email,
    UnitCategory = :unitCategory,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
	
UPDATE
    FORM_INFO_CHG_DETAIL
SET
    Summary = :summary,
    Content = :content,
    EffectSystem = :effectSystem,
    CCategory = :cCategory,
    CClass = :cClass,
    CComponent = :cComponent,
    ChangeType = :changeType,
    ChangeRank = :changeRank,
	Standard = :standard,
	IsNewSystem = :isNewSystem,
	IsNewService = :isNewService,
	IsUrgent = :isUrgent,
	IsEffectField = :isEffectField,
	IsEffectAccountant = :isEffectAccountant,
	IsModifyProgram = :isModifyProgram,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
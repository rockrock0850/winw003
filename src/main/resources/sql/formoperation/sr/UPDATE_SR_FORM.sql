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
    ECT = :ect,
    EOT = :eot,
    ACT = :act,
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
    FORM_INFO_SR_DETAIL
SET
    Summary = :summary,
    Content = :content,
    Division = :division,
    CCategory = :cCategory,
    CClass = :cClass,
    CComponent = :cComponent,
    SClass = :sClass,
    SSubClass = :sSubClass,
    System = :system,
    SystemId = :systemId,
    AssetGroup = :assetGroup,
    EffectScope = :effectScope,
    UrgentLevel = :urgentLevel,
    RequireRank = :requireRank,
    EffectScopeWording = :effectScopeWording,
    UrgentLevelWording = :urgentLevelWording,
    Countersigneds = :countersigneds,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
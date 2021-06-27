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
    CreateTime = :assignTime,
    MainEvent = :mainEvent,
    ECT = :ect,
    ExcludeTime = :excludeTime,
    ACT = :act,
    IsIVR = :isIVR,
    IsMainEvent = :isMainEvent,
    IsSameInc = :isSameInc,
    IsInterrupt = :isInterrupt,
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
    IsForward = :isForward,
    UnitCategory = :unitCategory,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
	
UPDATE
    FORM_INFO_INC_DETAIL
SET
    Summary = :summary,
    Content = :content,
    CCategory = :cCategory,
    CClass = :cClass,
    CComponent = :cComponent,
    SClass = :sClass,
    SSubClass = :sSubClass,
    System = :system,
    systemBrand = :systemBrand,
    SystemId = :systemId,
    AssetGroup = :assetGroup,
    EffectScope = :effectScope,
    UrgentLevel = :urgentLevel,
    EventClass = :eventClass,
    EventType = :eventType,
    EventSecurity = :eventSecurity,
    EventPriority = :eventPriority,
    EffectScopeWording = :effectScopeWording,
    UrgentLevelWording = :urgentLevelWording,
    EventPriorityWording = :eventPriorityWording,
    Countersigneds = :countersigneds,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt,
    IsOnlineFail = :isOnlineFail,
	OnlineTime = :onlineTime,
	OnlineJobFormId = :onlineJobFormId
WHERE
    FormId = :formId;
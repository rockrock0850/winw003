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
    AST = :ast,
    ACT = :act,
    ReportTime = :reportTime,
    Observation = :observation,
    IsSpecial = :isSpecial,
    SpecialEndCaseType = :specialEndCaseType,
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
    QuestionId = :questionId,
    Phone = :phone,
    Email = :email,
    UnitCategory = :unitCategory,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
	
UPDATE
    FORM_INFO_Q_DETAIL
SET
    Summary = :summary,
    Content = :content,
    CCategory = :cCategory,
    CClass = :cClass,
    CComponent = :cComponent,
    SClass = :sClass,
    SSubClass = :sSubClass,
    SystemId = :systemId,
    System = :system,
    AssetGroup = :assetGroup,
    EffectScope = :effectScope,
    UrgentLevel = :urgentLevel,
    QuestionPriority = :questionPriority,
    EffectScopeWording = :effectScopeWording,
    UrgentLevelWording = :urgentLevelWording,
    QuestionPriorityWording = :questionPriorityWording,
    Countersigneds = :countersigneds,
    Solutions = :solutions,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
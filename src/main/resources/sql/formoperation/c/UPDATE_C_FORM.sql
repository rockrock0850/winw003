UPDATE
    FORM
SET
    DetailId = :detailId,
    Parallel = :parallel,
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
    MECT = :mect,
    ECT = :ect,
    EOT = :eot,
    AST = :ast,
    ACT = :act,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
	
UPDATE
    FORM_INFO_C_DETAIL
SET
    Summary = :summary,
    Content = :content,
    HostHandle = :hostHandle,
    CountersignedHandle = :countersignedHandle,
    SClass = :sClass,
    System = :system,
    SystemId = :systemId,
    AssetGroup = :assetGroup,
    Unit = :unitId, 
    UserGroup = :userGroup,
    UserId = :userId,
    SPCGroups = :spcGroups,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt,
    IsSuggestCase = :isSuggestCase
WHERE
    FormId = :formId;
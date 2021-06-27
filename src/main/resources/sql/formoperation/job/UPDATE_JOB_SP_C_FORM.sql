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
    FORM_JOB_INFO_SYS_DETAIL
SET
    Summary = :summary,
    Purpose = :purpose,
    Content = :content,
    Remark = :remark,
    Countersigneds = :countersigneds,
    IsProduction = :isProduction,
    IsTest = :isTest,
    IsHandleFirst = :isHandleFirst,
    System = :system,
    UserId = :userId,
    CUserId = :cuserId,
    AStatus = :astatus,
    Status = :status,
    SPCGroups = :spcGroups,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
	
    
UPDATE
    FORM_JOB_INFO_DATE
SET
    MECT = :mect,
    ECT = :ect,
    EOT = :eot,
    AST = :ast,
    ACT = :act,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
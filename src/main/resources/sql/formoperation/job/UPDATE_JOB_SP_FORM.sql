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
    FORM_JOB_INFO_SYS_DETAIL
SET
    Working = :working,
    Status = :status,
    CCategory = :cCategory,
    CClass = :cClass,
    CComponent = :cComponent,
    IsReset = :isReset,
    IsProduction = :isProduction,
    IsTest = :isTest,
    IsHandleFirst = :isHandleFirst,
    IsForward = :isForward,
    IsInterrupt = :isInterrupt,
    Summary = :summary,
    Content = :content,
    EffectScope = :effectScope,
    Remark = :remark,
    Reset = :reset,
    Countersigneds = :countersigneds,
    ECT = :ect,
    OnLineTime = :onLineTime,
    OffLineTime = :offLineTime,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
	
UPDATE
    FORM_JOB_INFO_DATE
SET
    MECT = :mect,
    EOT = :eot,
    AST = :ast,
    ACT = :act,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
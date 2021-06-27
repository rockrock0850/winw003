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
    FORM_JOB_INFO_AP_DETAIL
SET
	SClass = :sClass,
    SSubClass = :sSubClass,
    ChangeType = :changeType,
    ChangeRank = :changeRank,
    SystemId = :systemId,
    System = :system,
    IsForward = :isForward,
    IsHandleFirst = :isHandleFirst,
    IsCorrect = :isCorrect,
    IsAddFuntion = :isAddFuntion,
    IsWatching = :isWatching,
    IsAddReport = :isAddReport,
    IsAddFile = :isAddFile,
    IsProgramOnline = :isProgramOnline,
    IsModifyProgram = :isModifyProgram,
    Purpose = :purpose,
    Content = :content,
    Countersigneds = :countersigneds,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
	
UPDATE
    FORM_JOB_INFO_DATE
SET
	CAT = :cat,
    CCT = :cct,
    TCT = :tct,
    SCT = :sct,
    SIT = :sit,
    IST = :ist,
    ICT = :ict,
    AST = :ast,
    OffLineTime = :offLineTime,
    IsPlaning = :isPlaning,
    IsUnPlaning = :isUnPlaning,
    UpdatedBy = :updatedBy,
    UpdatedAt = :updatedAt
WHERE
    FormId = :formId;
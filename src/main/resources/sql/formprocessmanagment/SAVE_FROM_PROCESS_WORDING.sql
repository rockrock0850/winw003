IF EXISTS (SELECT Id FROM FORM_PROCESS_LEVEL_WORDING WHERE DetailId = :detailId and ProcessOrder = :processOrder
    and [Type] = :type
    and WordingLevel = :wordingLevel
)
    UPDATE 
        FORM_PROCESS_LEVEL_WORDING
    SET 
        Wording = :wording,
        UpdatedAt = getdate(),
 		UpdatedBy = :loginUserId
    WHERE
        DetailId = :detailId 
        and ProcessOrder = :processOrder
        and [Type] = :type
        and WordingLevel = :wordingLevel
ELSE
    INSERT INTO FORM_PROCESS_LEVEL_WORDING
        (CreatedAt, CreatedBy, UpdatedAt, UpdatedBy, DetailId, ProcessOrder, [Type], Wording,WordingLevel)
    VALUES
        (getdate(),:loginUserId,getdate(),:loginUserId, :detailId, :processOrder,:type,:wording,:wordingLevel)

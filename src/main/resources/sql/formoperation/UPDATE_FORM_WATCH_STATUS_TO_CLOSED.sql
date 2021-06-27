UPDATE 
        FORM 
SET 
        FormStatus = :closed ,
        ProcessStatus = :closed ,
        UpdatedAt = :currentDate ,
        UpdatedBy = :updatedBy
WHERE
         FormId 
 IN(
        
SELECT 
        F.FormId
FROM 
        FORM F
JOIN
        FORM_INFO_DATE FD
ON
        F.FormId = FD.FormId
WHERE
       FD.Observation  < :currentDate 
AND
        F.FormStatus = :watchingStatus
AND
        F.ProcessStatus = :watchingStatus
)
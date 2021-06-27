-- 過濾退回清單內重複的資料
-- 注意 : 如果遇到平行會辦清單異動換人, 會導致退回清單撈到上一組平行會辦人員, 此問題在Java端解決
WITH CTE AS(
   SELECT FormId, VerifyLevel, VerifyType, SubmitTime, CompleteTime, UserId, VerifyResult, VerifyComment, Parallel,
       RN = ROW_NUMBER()OVER(PARTITION BY FormId, VerifyLevel, VerifyType, UserId ORDER BY UpdatedAt DESC)
   FROM FORM_VERIFY_LOG
   WHERE 
   FormId=:formId AND VerifyLevel=:verifyLevel AND VerifyType=:verifyType AND CompleteTime IS NOT NULL
)
SELECT * FROM CTE WHERE RN = 1 ORDER BY CompleteTime DESC
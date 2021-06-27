--遞迴計數指定表單底下所有未作廢和結案的延伸單

WITH CTE AS (
SELECT Id, FormId, SourceId, FormClass, FormStatus FROM FORM WHERE FormId = :formId
UNION ALL
SELECT F.Id, F.FormId, F.SourceId, F.FormClass, F.FormStatus FROM FORM F JOIN CTE ON F.SourceId = CTE.FormId
)

SELECT
    COUNT(*)-1 AS [COUNT]--減去自己
FROM
    CTE
WHERE
 	FormStatus <> 'CLOSED'
AND FormStatus <> 'DEPRECATED';
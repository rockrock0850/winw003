WITH CTE AS (
SELECT Id, FormId, SourceId, FormClass, FormStatus FROM FORM WHERE FormId = :formId
UNION ALL
SELECT F.Id, F.FormId, F.SourceId, F.FormClass, F.FormStatus FROM FORM F JOIN CTE ON F.SourceId = CTE.FormId
)
SELECT * FROM CTE ORDER BY Id;
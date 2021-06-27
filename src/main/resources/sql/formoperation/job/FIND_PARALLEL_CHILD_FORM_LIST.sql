WITH CTE AS (
SELECT Id, FormId, SourceId, FormClass, Parallel FROM FORM WHERE FormId = :formId
UNION ALL
SELECT F.Id, F.FormId, F.SourceId, F.FormClass, F.Parallel FROM FORM F JOIN CTE ON F.SourceId = CTE.FormId
)
SELECT * FROM CTE WHERE Parallel IS NOT NULL AND Parallel != '' AND FormId != :formId ORDER BY Id;
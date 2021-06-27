-- 0006869: 【PRO】F_表單查詢：選需求單等，查詢/匯出等待時間異常(約30秒)
DROP VIEW FORM_FILE_VIEW;
DROP VIEW FORM_USER_RECORD_VIEW;
DROP VIEW FORM_ASSOCIATION_VIEW;

CREATE VIEW
    FORM_ASSOCIATION_VIEW
    (
        FormId,
        AssociationForm
    ) AS
-- Select the stretch form id from formId and bind it single column.
SELECT 
	F.FormId,
	(STUFF((
	SELECT
		'[-]' + FormId 
	FROM
	   (SELECT FormId FROM findAssociation(F.FormId) WHERE FormId IS NOT NULL AND ISNULL(FormId, '') <> f.FormId) AS A
	FOR XML PATH ('') ), 1, 3, '' )
	) AS AssociationForm
FROM FORM F;

CREATE VIEW
    FORM_FILE_VIEW
    (
        FormId,
        FormFile
    ) AS
-- Select the form attachments from formId and bind it in single column.
SELECT 
	F.FormId,
	(STUFF((
        SELECT
            '[-]' + Name 
        FROM
            FORM_FILE 
        WHERE f.FormId = FormId
		FOR XML PATH ('') ), 1, 3, '' )
	) AS FormFile
FROM FORM F;

CREATE VIEW
    FORM_USER_RECORD_VIEW
    (
        FormId,
        FormUserRecord
    ) AS
-- Select the form user log data from formId and bind it in single column.
SELECT 
	F.FormId,
	(STUFF((
        SELECT
            '[-]' + Summary 
        FROM
            FORM_USER_RECORD 
        WHERE f.FormId = FormId
		FOR XML PATH ('') ), 1, 3, '' )
	) AS FormUserRecord
FROM FORM F;

-- 
CREATE FUNCTION findAssociation (@formId NVARCHAR(50))  
RETURNS @formIds TABLE   
(  
    FormId NVARCHAR(255) NOT NULL
)  AS   
BEGIN  
WITH 
CTE AS (
SELECT FormId, SourceId FROM FORM WHERE FormId = @formId
UNION ALL
SELECT F.FormId, F.SourceId FROM FORM F JOIN CTE ON F.SourceId = CTE.FormId
), 
CTE_REVERSE AS (
SELECT FormId, SourceId FROM FORM WHERE FormId = @formId
UNION ALL
SELECT F.FormId, F.SourceId FROM FORM F JOIN CTE_REVERSE ON F.FormId = CTE_REVERSE.SourceId
)

INSERT @formIds 
SELECT FormId FROM CTE UNION ALL SELECT FormId FROM CTE_REVERSE
RETURN  
END;
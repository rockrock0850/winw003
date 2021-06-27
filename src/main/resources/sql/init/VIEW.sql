--衝擊分析欄位表: 表單編號,影響評估,因應措施,分數
CREATE VIEW 
	FORM_IMPACT_ANALYSIS_VIEW
AS 
	SELECT 
        p.FormId  AS FormId,
        p.E       AS Evaluation,
        p.S       AS Solution,
        p.T       AS Total
	FROM (
		SELECT
				FormId, QuestionType, Description AS Content
		FROM
				FORM_IMPACT_ANALYSIS
		WHERE 
				QuestionType IN ('E', 'S')
	UNION ALL
		SELECT
				FormId, QuestionType, TargetFraction AS Content
		FROM
				FORM_IMPACT_ANALYSIS
		WHERE 
				QuestionType IN ('T')      
	) t  
	PIVOT (
		MAX(Content)
		FOR QuestionType IN ([E], [S], [T])
	) p;
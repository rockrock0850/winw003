-- Value欄位自訂:取OptionId + (OptionId_COUNT)+1 --
SELECT
	CONVERT(nvarchar(10), COUNT(*) +1) AS V 
FROM [SYS_OPTION] 
WHERE OptionId = 'StandardChange'

DECLARE
    @page INT,@pageSize INT
    SET @page=${PAGE} 			--現在頁數
    SET @pageSize=${PAGE_SIZE}	--每頁顯示筆數
    
BEGIN
	WITH QUERY_RESULT AS (
    	${QUERY_STRING}
	)

    SELECT
        *,
        (SELECT COUNT(*) FROM QUERY_RESULT) AS recordsTotal,
        (SELECT COUNT(*) FROM QUERY_RESULT) AS recordsFiltered
    FROM
        QUERY_RESULT
    ORDER BY
        ${ORDER_BY} ${SORTING} OFFSET (@page-1)*@pageSize ROWS --第一筆 rownum
    FETCH NEXT
        @pageSize ROWS ONLY --從第一筆 rownum 往下取得總筆數
END
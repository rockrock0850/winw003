WITH menuPermission AS (
	SELECT 
		sgp.MenuId, sm.ParentId
	FROM
		SYS_GROUP sg
	JOIN 
		SYS_GROUP_PERMISSION sgp ON sg.SysGroupId = sgp.SysGroupId
	JOIN
		SYS_MENU sm ON sgp.MenuId = sm.MenuId
	WHERE
		sg.GroupId = :groupId
)

SELECT 
	menu.Id        		AS id,
	menu.MenuId    		AS menuId,
	menu.MenuName  		AS menuName,
	menu.Path      		AS path,
	menu.Comment   		AS comment,
	menu.OrderId   		AS orderId,
	menu.Enabled   		AS enabled,
	menu.ParentId  		AS parentId,
	menu.OpenWindow  	AS openWindow
FROM 
	SYS_MENU menu
WHERE 
	menu.MenuId IN(
		SELECT DISTINCT rootMp.ParentId AS MenuId FROM menuPermission rootMp
	)
	
UNION ALL

SELECT
	menu.Id        		AS id,
	menu.MenuId    		AS menuId,
	menu.MenuName  		AS menuName,
	menu.Path      		AS path,
	menu.Comment   		AS comment,
	menu.OrderId   		AS orderId,
	menu.Enabled   		AS enabled,
	menu.ParentId  		AS parentId,
	menu.OpenWindow  	AS openWindow
FROM
	SYS_MENU menu
WHERE
	menu.MenuId IN(
		SELECT subMp.MenuId AS MenuId FROM menuPermission subMp
	)
ORDER BY menu.ParentId ASC, menu.OrderId ASC
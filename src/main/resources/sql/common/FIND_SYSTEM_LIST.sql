SELECT
    Id,
    SystemBrand, 
    SystemId, 
    SystemName,	
    Description, 
    Department,	
    MboName, 
    Mark, 
    Limit,  
    Opinc, 
    Apinc, 
    Active  
FROM
    [SYSTEM]
WHERE (1=1)
${CONDITIONS}
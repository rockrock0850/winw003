-- 0006575: 【PRO】F_簽核紀錄_關卡群組，應紀錄當時簽核軌跡之人員身分角色
ALTER TABLE FORM_VERIFY_LOG ADD GroupId NVARCHAR(100) NULL;

-- 0005892: 【PRO】SP工作會辦單：Summary欄位，長度調整為:1000
ALTER TABLE FORM_JOB_INFO_SYS_DETAIL ALTER COLUMN Summary nvarchar(1000) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL

-- FORM_JOB_INFO_SYS_DETAIL.SPCGroups欄位型態不夠長, 下次上版要記得更正。
ALTER TABLE FORM_JOB_INFO_SYS_DETAIL ALTER COLUMN SPCGroups text COLLATE Chinese_Taiwan_Stroke_CI_AS NULL

-- 2020/08/21: 【PRO】DB：[SYS_OPTION]資料表，新增 Active欄位，預設值為'Y'
ALTER TABLE [SYS_OPTION] ADD Active NVARCHAR(2) DEFAULT 'Y' NOT NULL;

-- 2020/08/26: 【PRO】DB：[WORKING_ITEM]資料表，新增 Active欄位，預設值為'Y'
ALTER TABLE [WORKING_ITEM] ADD Active NVARCHAR(2) DEFAULT 'Y' NOT NULL;

-- 2020/08/27: 【PRO】DB： [WORKING_ITEM]、[SYS_OPTION]資料表，新增 Active欄位中文說明
EXEC sys.sp_updateextendedproperty 'MS_Description', '狀態', 'user', 'dbo', 'table', 'WORKING_ITEM', 'column', 'Active' GO 
EXEC sys.sp_updateextendedproperty 'MS_Description', '狀態', 'user', 'dbo', 'table', 'SYS_OPTION', 'column', 'Active' GO 
-- 0007320: 【008】CR-65_事件單報表：AP工作單停機公告報表
INSERT INTO SYS_MENU (MenuId, MenuName, Path, Comment, OrderId, Enabled, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, OpenWindow) VALUES ('F050310', 'AP工作單停機公告報表', 'reportOperation/apReport/init', '', 10, true, 'F0503', null, null, null, null, false);

-- 0007299: 【008】CR-42_AP工作單：批次頁籤 新增「會辦機房內容」
ALTER TABLE FORM_JOB_BATCH ADD [CountersignedIDC] nvarchar(1000) GO

-- 0007221: 【008】CR-43-1_AP工作單_DC科內會流程(串會)
-- 更新
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active, IsKnowledge) VALUES (NULL, 'C_LIST', '表單資訊頁簽-會辦科區塊的清單', 'A01419-DC-DC1', '資料管制科-ONLINE', NULL, 'SYSTEM', GETDATE(), 'SYSTEM', GETDATE(), 'Y', 'Y') GO
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active, IsKnowledge) VALUES (NULL, 'C_LIST', '表單資訊頁簽-會辦科區塊的清單', 'A01419-DC-DC2', '資料管制科-BATCH', NULL, 'SYSTEM', GETDATE(), 'SYSTEM', GETDATE(), 'Y', 'Y') GO
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active, IsKnowledge) VALUES (NULL, 'C_LIST', '表單資訊頁簽-會辦科區塊的清單', 'A01419-DC-DC3', '資料管制科-OPEN', NULL, 'SYSTEM', GETDATE(), 'SYSTEM', GETDATE(), 'Y', 'Y') GO
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active, IsKnowledge) VALUES (NULL, 'C_LIST', '表單資訊頁簽-會辦科區塊的清單', 'A01419-DC-BATCH', '資料管制科-批次', NULL, 'SYSTEM', GETDATE(), 'SYSTEM', GETDATE(), 'Y', 'Y') GO
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active, IsKnowledge) VALUES (NULL, 'C_LIST', '表單資訊頁簽-會辦科區塊的清單', 'A01419-DC-DB', '資料管制科-DB變更', NULL, 'SYSTEM', GETDATE(), 'SYSTEM', GETDATE(), 'Y', 'Y') GO
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('DC-DC1', '資料管制科', 'ONLINE') GO
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('DC-DC2', '資料管制科', 'BATCH') GO
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('DC-DC3', '資料管制科', 'OPEN') GO
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('DC-BATCH', '資料管制科', '批次') GO
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('DC-DB', '資料管制科', 'DB變更') GO
-- 還原
--DELETE FROM SYS_OPTION WHERE Value = 'A01419-DC-DC1' GO
--DELETE FROM SYS_OPTION WHERE Value = 'A01419-DC-DC2' GO
--DELETE FROM SYS_OPTION WHERE Value = 'A01419-DC-DC3' GO
--DELETE FROM SYS_OPTION WHERE Value = 'A01419-DC-BATCH' GO
--DELETE FROM SYS_OPTION WHERE Value = 'A01419-DC-DB' GO
--DELETE FROM FORM_JOB_DIVISION_MAPPING WHERE Division = 'DC-DC1' GO
--DELETE FROM FORM_JOB_DIVISION_MAPPING WHERE Division = 'DC-DC2' GO
--DELETE FROM FORM_JOB_DIVISION_MAPPING WHERE Division = 'DC-DC3' GO
--DELETE FROM FORM_JOB_DIVISION_MAPPING WHERE Division = 'DC-BATCH' GO
--DELETE FROM FORM_JOB_DIVISION_MAPPING WHERE Division = 'DC-DB' GO

--0007098: 【008】ISO_16_知識庫：引用「處理方案」內欄位
--新增
INSERT INTO SYS_MENU (MenuId, MenuName, Path, Comment, OrderId, Enabled, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, OpenWindow) VALUES ('F0505', '知識庫報表查詢作業', '', '', 1, 1, 'root', null, null, null, null, 0);
INSERT INTO SYS_MENU (MenuId, MenuName, Path, Comment, OrderId, Enabled, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, OpenWindow) VALUES ('F050501', '知識庫分析表', 'reportOperation/knowledgeAnalysis/init', '', 1, 1, 'F0505', null, null, null, null, 0);
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('KL', '1,801,802,803,29,49,50,39,40,41,42,43,44', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO SYS_MENU (MenuId, MenuName, Path, Comment, OrderId, Enabled, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, OpenWindow) VALUES ('F0406', '知識庫', 'formSearch/knowledge', '', 1, 1, 'F04', null, null, null, null, 0);
UPDATE SYS_MENU SET OrderId = 2 WHERE MenuId = 'F0401';
UPDATE SYS_MENU SET OrderId = 3 WHERE MenuId = 'F0402';
UPDATE SYS_MENU SET OrderId = 4 WHERE MenuId = 'F0403';
UPDATE SYS_MENU SET OrderId = 5 WHERE MenuId = 'F0404';
UPDATE SYS_MENU SET OrderId = 6 WHERE MenuId = 'F0405';
UPDATE SYS_MENU SET OrderId = 1 WHERE MenuId = 'F03';
UPDATE SYS_MENU SET OrderId = 2 WHERE MenuId = 'F04';
UPDATE SYS_MENU SET OrderId = 3 WHERE MenuId = 'F0501';
UPDATE SYS_MENU SET OrderId = 4 WHERE MenuId = 'F0502';
UPDATE SYS_MENU SET OrderId = 5 WHERE MenuId = 'F0503';
UPDATE SYS_MENU SET OrderId = 6 WHERE MenuId = 'F0504';
UPDATE SYS_MENU SET OrderId = 7 WHERE MenuId = 'F0505';

--還原
--DELETE FORM_FIELDS WHERE FormClass = 'KL';
--DELETE SYS_MENU WHERE MenuId = 'F0505';
--DELETE SYS_MENU WHERE MenuId = 'F050501';
--DELETE SYS_MENU WHERE MenuId = 'F0406';
--UPDATE SYS_MENU SET OrderId = 1 WHERE MenuId = 'F0401';
--UPDATE SYS_MENU SET OrderId = 2 WHERE MenuId = 'F0402';
--UPDATE SYS_MENU SET OrderId = 3 WHERE MenuId = 'F0403';
--UPDATE SYS_MENU SET OrderId = 4 WHERE MenuId = 'F0404';
--UPDATE SYS_MENU SET OrderId = 5 WHERE MenuId = 'F0405';

--0007081: 【008】CR-67_問題單觀察期，新增到期日發MAIL排程，並排除例假日(行事曆)
INSERT INTO SCHEDULE_JOB (JobName, JobGroup, JobDescription, JobClass, Status, TimeUnit, RepeatInterval, ExecuteTimes, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt)
VALUES('NotificationObsExpiredQJob', 'job', '問題單觀察期過期通知', 'com.ebizprise.winw.project.job.NotificationObsExpiredQJob', 1, 0, 0, 1, NULL, NULL, 'SYSTEM', getDate());

-- 0007290: 【008】SIT_問題單：來源表單欄位，更新舊單欄位之SCRIPT
-- 更新
UPDATE FORM SET SourceId = '' WHERE FormClass = 'Q' AND SourceId IS NULL GO 

-- 0007257: 【008】SIT_表單查詢：問題單_加入條件「來源表單」，調整來源表單頁面位置
-- 更新	
UPDATE FORM_FIELDS SET FormFields = '1,1005,29,34,2,35,6,7,5,3,4,8,9,10,28,1000,30,31,32,33,36,37,38,39,40,41,42,43,44,45,46,47,48,801,802,803,804' WHERE FormClass = 'Q' GO 
-- 還原
--UPDATE FORM_FIELDS SET FormFields = '1,29,34,2,35,6,7,5,3,4,8,9,10,28,1000,30,31,32,33,36,37,38,39,40,41,42,43,44,45,46,47,48,801,802,803,804,1005' WHERE FormClass = 'Q' GO 

-- 0007238: 【008】SIT_表單查詢：事件單_加入條件「同一事件兩日內復發」、「上線失敗」、「上線時間」、「工作單單號」
-- 更新
UPDATE FORM_FIELDS SET FormFields = '1,49,62,2,6,7,5,3,4,8,9,10,50,51,1000,52,53,54,55,56,57,58,59,60,61,63,64,65,66,67,68,69,70,71,72,73,801,802,803,805,1001,1002,1003,1004' WHERE FormClass = 'INC' GO 
-- 還原
UPDATE FORM_FIELDS SET FormFields = '1,49,62,2,6,7,5,3,4,8,9,10,50,51,1000,52,53,54,55,56,57,58,59,60,61,63,64,65,66,67,68,69,70,71,72,73,801,802,803,805' WHERE FormClass = 'INC' GO  

-- 0007257: 【008】SIT_表單查詢：問題單_加入條件「來源表單」
-- 更新
UPDATE FORM_FIELDS SET FormFields = '1,29,34,2,35,6,7,5,3,4,8,9,10,28,1000,30,31,32,33,36,37,38,39,40,41,42,43,44,45,46,47,48,801,802,803,804,1005' WHERE FormClass = 'Q' GO 
-- 還原
UPDATE FORM_FIELDS SET FormFields = '1,29,34,2,35,6,7,5,3,4,8,9,10,28,1000,30,31,32,33,36,37,38,39,40,41,42,43,44,45,46,47,48,801,802,803,804' WHERE FormClass = 'Q' GO  

-- 0007089: 【008】ISO_7_變更單擴充附件功能
INSERT INTO SYS_PARAMETER (ParamId, ParamKey, ParamValue, IsPassword, Description, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES (18, 'CHANGE_FORM_ATTACHMENT_NAME_SYSTEM_FEASIBIILITY_ANALYSIS_REPORT', '可行性分析報告', 'N', '變更單,新系統=「Y」時，變更單送出(送審)應檢核附件檔名「可行性分析報告」', 'SYSTEM',GETDATE(), 'SYSTEM', GETDATE());
INSERT INTO SYS_PARAMETER (ParamId, ParamKey, ParamValue, IsPassword, Description, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES (19, 'CHANGE_FORM_ATTACHMENT_NAME_SYSTEM_PUBLISH_ACCEPT_RULE', '上線驗收準則', 'N', '變更單,新系統=「Y」時，變更單送出(送審)應檢核附件檔名「上線驗收準則」', 'SYSTEM',GETDATE(), 'SYSTEM', GETDATE());
INSERT INTO SYS_PARAMETER (ParamId, ParamKey, ParamValue, IsPassword, Description, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES (20, 'CHANGE_FORM_ATTACHMENT_NAME_SERVICE_FEASIBIILITY_ANALYSIS_REPORT', '可行性分析報告', 'N', '變更單,新服務暨重大服務=「Y」時，變更單送出(送審)應檢核附件檔名「可行性分析報告」', 'SYSTEM',GETDATE(), 'SYSTEM', GETDATE());
INSERT INTO SYS_PARAMETER (ParamId, ParamKey, ParamValue, IsPassword, Description, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES (21, 'CHANGE_FORM_ATTACHMENT_NAME_SERVICE_PLANNING_REPORT', '新服務暨服務異動評估規劃報告', 'N', '變更單,新服務暨重大服務=「Y」時，變更單送出(送審)應檢核附件檔名「新服務暨服務異動評估規劃報告」', 'SYSTEM',GETDATE(), 'SYSTEM', GETDATE());
INSERT INTO SYS_PARAMETER (ParamId, ParamKey, ParamValue, IsPassword, Description, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES (22, 'CHANGE_FORM_ATTACHMENT_NAME_OVER_IMPACT_THRESHOLD', '上線驗收準則', 'N', '變更單,新系統=「N」、新服務暨重大服務=「N」且衝擊分析>=1000之重大變更時，變更單送出(送審)應檢核附件檔名「上線驗收準則」', 'SYSTEM',GETDATE(), 'SYSTEM', GETDATE());
INSERT INTO SYS_PARAMETER (ParamId, ParamKey, ParamValue, IsPassword, Description, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES (23, 'QUESTION_FORM_ATTACHMENT_NAME_NEW_SYSTEM', '新系統上線檢核表', 'N', '問題單的變更單有勾選新系統時，問題單送出(送審)應檢核附件檔名「新系統上線檢核表」', 'SYSTEM',GETDATE(), 'SYSTEM', GETDATE());
INSERT INTO SYS_PARAMETER (ParamId, ParamKey, ParamValue, IsPassword, Description, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES (24, 'REQUIREMENT_FORM_ATTACHMENT_NAME_NEW_SYSTEM', '新系統上線檢核表', 'N', '需求單的變更單有勾選新系統時，需求單送出(送審)應檢核附件檔名「新系統上線檢核表」', 'SYSTEM',GETDATE(), 'SYSTEM', GETDATE());

-- 0007098: 【008】ISO_16_知識庫：引用「處理方案」內欄位
DELETE FORM_FIELDS WHERE Formclass = 'QA' GO

-- 更新
INSERT INTO SYS_MENU (MenuId, MenuName, Path, Comment, OrderId, Enabled, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, OpenWindow) VALUES ('F0314', '知識庫原因維護', 'knowledgeBase/init', '', 14, 1, 'F03', null, null, null, null, 0) GO
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('QA', '1,49,50,39,41,5,30,802,803,801,35,4', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00') GO
-- 還原
DELETE FROM SYS_MENU WHERE MenuId = 'F0314' GO
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('QA', '1,39,41,5,30,802,803,801,35,4', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00') GO

-- 0006904: 【PRO】F_表單查詢：異常欄位盤點 (持續更新)
DELETE FORM_FIELDS WHERE FormClass = 'Q_C' GO
DELETE FORM_FIELDS WHERE FormClass = 'SR_C' GO
DELETE FORM_FIELDS WHERE FormClass = 'INC_C' GO
DELETE FORM_FIELDS WHERE FormClass = 'JOB_AP_C' GO
-- 更新
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('SR_C', '1,66,,875,6,7,2,907,3,4,5,876,877,878,1000,879,880,881,882,883,884,885,886,887,888', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00') GO
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('Q_C', '1,66,,875,6,7,2,907,3,4,5,876,877,878,1000,879,880,881,882,883,884,885,886,887,888', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00') GO
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('INC_C', '1,66,,875,6,7,2,907,3,4,5,876,877,878,1000,879,880,881,882,883,884,885,886,887,888', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00') GO
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('JOB_AP_C', '1,201,212,219,3,2,7,4,5,228,204,213,201,202,203,205,206,222,223,224,225,226,220,221,207,208,209,210,211,236,215,217,218,216', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00') GO
-- 還原
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('SR_C', '1,875,6,7,2,907,3,4,5,876,877,878,1000,879,880,881,882,883,884,885,886,887,888', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00') GO
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('Q_C', '1,875,6,7,2,907,3,4,5,876,877,878,1000,879,880,881,882,883,884,885,886,887,888', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00') GO
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('INC_C', '1,875,6,7,2,907,3,4,5,876,877,878,1000,879,880,881,882,883,884,885,886,887,888', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00') GO
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('JOB_AP_C', '1,212,219,3,2,7,4,5,228,204,213,202,203,205,206,222,223,224,225,226,220,221,207,208,209,210,211,236,215,217,218,216', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00') GO

--0007062: 【008】ISO_8_AP工作單：表單資訊「程式上線」勾選時，送出表單時，須檢核有測試文件
--更新
DELETE FROM FORM_FIELDS WHERE FormClass = 'JOB_AP'
INSERT INTO FORM_FIELDS (FormClass,FormFields,UpdatedBy,UpdatedAt,CreatedBy,CreatedAt) VALUES 
('JOB_AP','1,227,5,7,212,223,2,3,213,204,219,6,201,202,203,205,206,207,208,209,210,211,236,215,217,218,220,221,222,224,225,226,216,228','System','2019-08-27 00:00:00.000','System','2019-08-27 00:00:00.000')
 GO
-- 還原
INSERT INTO FORM_FIELDS (FormClass,FormFields,UpdatedBy,UpdatedAt,CreatedBy,CreatedAt) VALUES 
('JOB_AP','1,227,5,7,212,223,2,3,213,204,219,6,201,202,203,205,206,207,208,209,210,211,236,215,217,218,220,221,222,224,225,226,216','System','2019-08-27 00:00:00.000','System','2019-08-27 00:00:00.000')
 GO
 
-- 0007276: 【008】SIT_AP工作會辦單：新增「程式上線」欄位
-- 更新
UPDATE FORM_FIELDS SET FormFields = '1,201,212,219,3,2,7,4,5,228,204,213,202,203,205,206,222,223,224,225,226,220,221,207,208,209,210,211,236,215,217,218,216,247' WHERE FormClass = 'JOB_AP_C' GO 
-- 還原
UPDATE FORM_FIELDS SET FormFields = '1,201,212,219,3,2,7,4,5,228,204,213,202,203,205,206,222,223,224,225,226,220,221,207,208,209,210,211,236,215,217,218,216' WHERE FormClass = 'JOB_AP_C' GO 

-- 0007267: 【008】CR_變更單：新增「未有修改程式」欄位
-- 更新
UPDATE FORM_FIELDS SET FormFields = '1,67,2,7,6,72,68,3,4,5,66,3000,3001,3002,3003,74,75,76,77,78,79,80,81,82,904,905,906,89' WHERE FormClass = 'CHG' GO 
-- 還原
UPDATE FORM_FIELDS SET FormFields = '1,67,2,7,6,72,68,3,4,5,66,3000,3001,3002,3003,74,75,76,77,78,79,80,81,82,904,905,906' WHERE FormClass = 'CHG' GO 

-- 0007267: 【008】CR_AP工作單：新增「未有修改程式」欄位
-- 更新
UPDATE FORM_FIELDS SET FormFields = '1,227,5,7,212,223,2,3,213,204,219,6,201,202,203,205,206,207,208,209,210,211,228,236,215,217,218,220,221,222,224,225,226,216,255' WHERE FormClass = 'JOB_AP' GO 
-- 還原
UPDATE FORM_FIELDS SET FormFields = '1,227,5,7,212,223,2,3,213,204,219,6,201,202,203,205,206,207,208,209,210,211,228,236,215,217,218,220,221,222,224,225,226,216' WHERE FormClass = 'JOB_AP' GO 

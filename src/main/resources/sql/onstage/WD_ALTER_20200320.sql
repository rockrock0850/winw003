-- 0005456: 【PRO】報表_問題單：問題單來源數量統計表，查詢條件與對應表單資訊調整，FORM_INFO_DATE中每張問題單補上reportTime
UPDATE FORM_INFO_DATE SET FORM_INFO_DATE.ReportTime = (SELECT FORM.CreateTime FROM FORM WHERE FORM.FormClass = 'Q' AND FORM.FormId = FORM_INFO_DATE.FormId)

-- 0005339: 【PRO】 事件會辦單：新增「暫時性解決方案，且無法於事件目標解決時間內根本解決者」欄位
ALTER TABLE FORM_INFO_C_DETAIL ADD IsSuggestCase CHAR(1) NULL DEFAULT('N');
ALTER TABLE FORM_INFO_C_DETAIL  WITH CHECK ADD CHECK  (IsSuggestCase='N' OR IsSuggestCase='Y');

-- M_0005260: 【UAT】Issue_AP工作單：頁籤_DB變更，加入欄位規則
ALTER TABLE FORM_JOB_WORKING ADD AST DATETIME

-- 0005261: 【UAT】Issue_AP工作單：作業關卡項目名稱，新增「檢查人」
DELETE FROM SYS_OPTION WHERE OptionId='WORK_LEVEL' AND Value='CHECKER';
UPDATE SYS_OPTION SET Display = '檢查人' WHERE OptionId='WORK_LEVEL' AND Value='TPERSON';

-- 0005346:【PRO】DB：會辦單「主辦科處理情形」HostHandle長度增至2000，AP工作單「修改詳細說明(FOR SP)」長度調整至2000，SP工作會辦單「修改詳細說明」長度增至2000
ALTER TABLE [dbo].[FORM_JOB_COUNTERSIGNED] ALTER COLUMN [Description] nvarchar(2000);
ALTER TABLE [dbo].[FORM_JOB_INFO_SYS_DETAIL] ALTER COLUMN [Remark] nvarchar(2000);
ALTER TABLE [dbo].[FORM_INFO_C_DETAIL] ALTER COLUMN [HostHandle] nvarchar(2000);

-- 0005260: 【UAT】Issue_AP工作單：頁籤_DB變更，加入欄位規則
ALTER TABLE FORM_JOB_WORKING ADD Remark nvarchar(3000);
ALTER TABLE FORM_FILE ALTER COLUMN AlterContent nvarchar(1000);
ALTER TABLE FORM_FILE ALTER COLUMN LayoutDataset nvarchar(1000);
EXEC sp_rename 'FORM_JOB_WORKING.View', 'Veew', 'COLUMN';
EXEC sp_rename 'FORM_FILE.AlterCotent', 'AlterContent', 'COLUMN';
ALTER TABLE FORM_FILE ADD Type nvarchar(10);-- 要先執行新增Type欄位
UPDATE FORM_FILE SET Type = 'FILE';-- 再執行更新Type欄位

-- 0005264: 【UAT】Issue_AP工作單：將現行「CL JCL」「執行JCL」「程式名稱」欄位的 (1)長度調整到2000
ALTER TABLE [dbo].[FORM_JOB_BATCH] ALTER COLUMN ProgramName nvarchar(2000);
ALTER TABLE [dbo].[FORM_JOB_BATCH] ALTER COLUMN JCL nvarchar(2000);
ALTER TABLE [dbo].[FORM_JOB_BATCH] ALTER COLUMN CLJCL nvarchar(2000);

-- 0005261: 【UAT】Issue_AP工作單：作業關卡項目名稱，新增「檢查人」
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('16', 'WORK_LEVEL', '工作單作業關卡的工作項目', 'CHECKER', '檢查人', null, 'System', '2019-08-02 18:01:07', 'System', '2019-08-02 18:01:07');
INSERT INTO SYS_OPTION_ROLE (RoleId, OptionId, Value, Description, Condition, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('30', 'WORK_LEVEL', 'CHECKER', '工作關卡-檢查人-Condition為Null或空值代表該角色的人員可撈取所有系統科人員', '', 'SYSTEM', '2020-03-05 15:30:00', 'SYSTEM', '2020-03-05 15:30:00');

-- 0005257: 【UAT】Issue_問題單：頁籤_處理方案，新增「暫時性解決方案」欄位
ALTER TABLE FORM_PROGRAM ADD [Temporary] nvarchar(2000)

-- 0005205: 【UAT】表單_系統名稱：新增PLAN+MGMT兩個部門的資料
DELETE FROM SYSTEM WHERE Department = 'SECURITY';

INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-WEB-SENSE_PLANPROBLEM337', 'SEC-WEB-SENSE', 'DLP防個資外洩系統', 'DLP防個資外洩系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-HSM-CN_PLANPROBLEM337', 'SEC-HSM-CN', 'EDI/EATM安控系統', 'EDI/EATM安控系統', 'PLAN', 'PROBLEM', '系統軟體─與交易業務間接相關(支援交易)', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-IPS_PLANPROBLEM337', 'SEC-IPS', 'IPS入侵偵測系統', 'IPS入侵偵測系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-OTP_PLANPROBLEM337', 'SEC-OTP', 'OTP雙因素認證系統', 'OTP雙因素認證系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-HSM-POS_PLANPROBLEM337', 'SEC-HSM-POS', 'POS安控系統', 'POS安控系統', 'PLAN', 'PROBLEM', '系統軟體─與交易業務間接相關(支援交易)', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-PROXY-ISA_PLANPROBLEM337', 'SEC-PROXY-ISA', 'Proxy代理伺服器系統', 'Proxy代理伺服器系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-VPN_PLANPROBLEM337', 'SEC-VPN', 'VPN系統', 'VPN系統', 'PLAN', 'PROBLEM', '系統軟體─與公司日常營運管理相關', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-PRIVACY-XF_PLANPROBLEM337', 'SEC-PRIVACY-XF', 'Xfort資安防護系統', 'Xfort資安防護系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-PROXY-XGATE_PLANPROBLEM337', 'SEC-PROXY-XGATE', 'Xgate上網代理系統', 'Xgate上網代理系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-SAWMILL_PLANPROBLEM337', 'SEC-SAWMILL', '日誌分析報表系統', '日誌分析報表系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-NPS_PLANPROBLEM333', 'SEC-NPS', '合法設備連線管理系統', '合法設備連線管理系統', 'PLAN', 'PROBLEM', '應用軟體─與公司日常營運管理作業相關', 3, 3, 'Y', null, null, null, null, '3');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-MDM_PLANPROBLEM337', 'SEC-MDM', '行動裝置管理系統', '行動裝置管理系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-Anchor_PLANPROBLEM337', 'SEC-Anchor', '伺服器連線管理系統', '伺服器連線管理系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('FS001_PLANPROBLEM337', 'FS001', '系統弱點掃描系統', '系統弱點掃描系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-Tufin_PLANPROBLEM337', 'SEC-Tufin', '防火牆安全政策異動管理系統', '防火牆安全政策異動管理系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-CheckPoint_PLANPROBLEM337', 'SEC-CheckPoint', '防火牆系統', '防火牆系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('AV001_PLANPROBLEM333', 'AV001', '防毒系統', '防毒系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '3');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('AV002_PLANPROBLEM337', 'AV002', '防毒牆系統', '防毒牆系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-MAIL-SPAM_PLANPROBLEM337', 'SEC-MAIL-SPAM', '垃圾郵件過濾系統', '垃圾郵件過濾系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-MAIL-APT_PLANPROBLEM333', 'SEC-MAIL-APT', '社交工程電子郵件防禦系統', '社交工程電子郵件防禦系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '3');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-HSM-HKNB_PLANPROBLEM337', 'SEC-HSM-HKNB', '香港網銀動態密碼驗證系統', '香港網銀動態密碼驗證系統', 'PLAN', 'PROBLEM', '系統軟體─與交易業務間接相關(支援交易)', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('Airmagnet_PLANPROBLEM337', 'Airmagnet', '無線基地台管理', '無線基地台管理', 'PLAN', 'PROBLEM', '系統軟體─與公司日常營運管理相關', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('Fortify_PLANPROBLEM337', 'Fortify', '程式碼弱點掃描系統', '程式碼弱點掃描系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-MAIL_BK_PLANPROBLEM337', 'SEC-MAIL_BK', '郵件內容偵測系統', '郵件內容偵測系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC_PLAN337', 'SEC', '資安服務系統', '資安服務系統', 'PLAN', '', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC_PLANPROBLEM337', 'SEC', '資安服務系統', '資安服務系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'N', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-Monitor_PLANPROBLEM333', 'SEC-Monitor', '資安設備監控系統', '資安設備監控系統', 'PLAN', 'PROBLEM', '系統軟體─與公司日常營運管理相關', 3, 3, 'Y', null, null, null, null, '3');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-NetIQ_PLANPROBLEM333', 'SEC-NetIQ', '資安設備監控系統', '資安設備監控系統', 'PLAN', 'PROBLEM', '系統軟體─與公司日常營運管理相關', 3, 3, 'N', null, null, null, null, '3');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-MSE_PLANPROBLEM337', 'SEC-MSE', '電子郵件社交工程演練系統', '電子郵件社交工程演練系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-HSM-PSDB_PLANPROBLEM337', 'SEC-HSM-PSDB', '實券保管安控系統', '實券保管安控系統', 'PLAN', 'PROBLEM', '系統軟體─與交易業務間接相關(支援交易)', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('WebInspect_PLANPROBLEM337', 'WebInspect', '網頁滲透測試弱點掃描系統', '網頁滲透測試弱點掃描系統', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-WAF_PLANPROBLEM337', 'SEC-WAF', '網頁應用程式防火牆', '網頁應用程式防火牆', 'PLAN', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-WEB-SENSE_MGMTPROBLEM337', 'SEC-WEB-SENSE', 'DLP防個資外洩系統', 'DLP防個資外洩系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-HSM-CN_MGMTPROBLEM337', 'SEC-HSM-CN', 'EDI/EATM安控系統', 'EDI/EATM安控系統', 'MGMT', 'PROBLEM', '系統軟體─與交易業務間接相關(支援交易)', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-IPS_MGMTPROBLEM337', 'SEC-IPS', 'IPS入侵偵測系統', 'IPS入侵偵測系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-OTP_MGMTPROBLEM337', 'SEC-OTP', 'OTP雙因素認證系統', 'OTP雙因素認證系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-HSM-POS_MGMTPROBLEM337', 'SEC-HSM-POS', 'POS安控系統', 'POS安控系統', 'MGMT', 'PROBLEM', '系統軟體─與交易業務間接相關(支援交易)', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-PROXY-ISA_MGMTPROBLEM337', 'SEC-PROXY-ISA', 'Proxy代理伺服器系統', 'Proxy代理伺服器系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-VPN_MGMTPROBLEM337', 'SEC-VPN', 'VPN系統', 'VPN系統', 'MGMT', 'PROBLEM', '系統軟體─與公司日常營運管理相關', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-PRIVACY-XF_MGMTPROBLEM337', 'SEC-PRIVACY-XF', 'Xfort資安防護系統', 'Xfort資安防護系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-PROXY-XGATE_MGMTPROBLEM337', 'SEC-PROXY-XGATE', 'Xgate上網代理系統', 'Xgate上網代理系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-SAWMILL_MGMTPROBLEM337', 'SEC-SAWMILL', '日誌分析報表系統', '日誌分析報表系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-NPS_MGMTPROBLEM333', 'SEC-NPS', '合法設備連線管理系統', '合法設備連線管理系統', 'MGMT', 'PROBLEM', '應用軟體─與公司日常營運管理作業相關', 3, 3, 'Y', null, null, null, null, '3');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-MDM_MGMTPROBLEM337', 'SEC-MDM', '行動裝置管理系統', '行動裝置管理系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-Anchor_MGMTPROBLEM337', 'SEC-Anchor', '伺服器連線管理系統', '伺服器連線管理系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('FS001_MGMTPROBLEM337', 'FS001', '系統弱點掃描系統', '系統弱點掃描系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-Tufin_MGMTPROBLEM337', 'SEC-Tufin', '防火牆安全政策異動管理系統', '防火牆安全政策異動管理系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-CheckPoint_MGMTPROBLEM337', 'SEC-CheckPoint', '防火牆系統', '防火牆系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('AV001_MGMTPROBLEM333', 'AV001', '防毒系統', '防毒系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '3');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('AV002_MGMTPROBLEM337', 'AV002', '防毒牆系統', '防毒牆系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-MAIL-SPAM_MGMTPROBLEM337', 'SEC-MAIL-SPAM', '垃圾郵件過濾系統', '垃圾郵件過濾系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-MAIL-APT_MGMTPROBLEM333', 'SEC-MAIL-APT', '社交工程電子郵件防禦系統', '社交工程電子郵件防禦系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '3');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-HSM-HKNB_MGMTPROBLEM337', 'SEC-HSM-HKNB', '香港網銀動態密碼驗證系統', '香港網銀動態密碼驗證系統', 'MGMT', 'PROBLEM', '系統軟體─與交易業務間接相關(支援交易)', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('Airmagnet_MGMTPROBLEM337', 'Airmagnet', '無線基地台管理', '無線基地台管理', 'MGMT', 'PROBLEM', '系統軟體─與公司日常營運管理相關', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('Fortify_MGMTPROBLEM337', 'Fortify', '程式碼弱點掃描系統', '程式碼弱點掃描系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-MAIL_BK_MGMTPROBLEM337', 'SEC-MAIL_BK', '郵件內容偵測系統', '郵件內容偵測系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC_MGMT337', 'SEC', '資安服務系統', '資安服務系統', 'MGMT', '', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC_MGMTPROBLEM337', 'SEC', '資安服務系統', '資安服務系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'N', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-Monitor_MGMTPROBLEM333', 'SEC-Monitor', '資安設備監控系統', '資安設備監控系統', 'MGMT', 'PROBLEM', '系統軟體─與公司日常營運管理相關', 3, 3, 'Y', null, null, null, null, '3');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-NetIQ_MGMTPROBLEM333', 'SEC-NetIQ', '資安設備監控系統', '資安設備監控系統', 'MGMT', 'PROBLEM', '系統軟體─與公司日常營運管理相關', 3, 3, 'N', null, null, null, null, '3');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-MSE_MGMTPROBLEM337', 'SEC-MSE', '電子郵件社交工程演練系統', '電子郵件社交工程演練系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-HSM-PSDB_MGMTPROBLEM337', 'SEC-HSM-PSDB', '實券保管安控系統', '實券保管安控系統', 'MGMT', 'PROBLEM', '系統軟體─與交易業務間接相關(支援交易)', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('WebInspect_MGMTPROBLEM337', 'WebInspect', '網頁滲透測試弱點掃描系統', '網頁滲透測試弱點掃描系統', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');
INSERT INTO SYSTEM (SystemBrand, SystemId, SystemName, Description, Department, MboName, Mark, Opinc, Apinc, Active, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Limit) VALUES ('SEC-WAF_MGMTPROBLEM337', 'SEC-WAF', '網頁應用程式防火牆', '網頁應用程式防火牆', 'MGMT', 'PROBLEM', 'IT基礎相關軟體', 3, 3, 'Y', null, null, null, null, '7');

-- 0005216: 【UAT】表單_內容(Content)欄位長度增加至2000
ALTER TABLE FORM_INFO_SR_DETAIL ALTER COLUMN Content nvarchar(2000) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL
ALTER TABLE FORM_INFO_Q_DETAIL ALTER COLUMN Content nvarchar(2000) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL
ALTER TABLE FORM_INFO_INC_DETAIL ALTER COLUMN Content nvarchar(2000) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL
ALTER TABLE FORM_INFO_CHG_DETAIL ALTER COLUMN Content nvarchar(2000) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL
ALTER TABLE FORM_INFO_C_DETAIL ALTER COLUMN Content nvarchar(2000) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL
ALTER TABLE FORM_JOB_INFO_AP_DETAIL ALTER COLUMN Content nvarchar(2000) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL
ALTER TABLE FORM_JOB_INFO_SYS_DETAIL ALTER COLUMN Content nvarchar(2000) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL
ALTER TABLE FORM_JOB_INFO_SYS_DETAIL ALTER COLUMN Countersigneds nvarchar(2000) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL

-- 0005204: 【UAT】AP工作會辦單：會辦頁籤未帶出
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('AP1', '設計一科', '設一');
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('AP2', '設計二科', '設二');
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('AP3', '設計三科', '設三');
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('AP4', '設計四科', '設四');
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('DC', '資料管制科', 'ONLINE');
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('DC', '資料管制科', 'BATCH');
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('DC', '資料管制科', 'OPEN');
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('DC', '資料管制科', '批次');
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('EA', '電子商務科', '電商');
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('OA', '辦公室自動化科', 'OA');
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('PT', '連管科', '連管');
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('PLAN', '資安規劃科', '資安規劃');
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('MGMT', '資安管理科', '資安管理');
INSERT INTO FORM_JOB_DIVISION_MAPPING (Division, DivisionName, JobTabName) VALUES ('SP', '系統科', '系統');

-- 更新作業關卡群組資料
UPDATE SYS_OPTION_ROLE SET Value = 'IMS' WHERE Value = 'IMS/CICS';
UPDATE SYS_OPTION_ROLE SET Value = 'AS400MVS' WHERE Value = 'MVS/AS400';
INSERT INTO SYS_OPTION_ROLE (RoleId, OptionId, Value, Description, Condition, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('29', 'WORK_LEVEL', 'CICS', '工作關卡-IMS/CICS組-Condition為Null或空值代表該角色的人員可撈取所有系統科人員', 'SP_CICS', 'SYSTEM', '2020-03-05 15:30:00', 'SYSTEM', '2020-03-05 15:30:00');
UPDATE SYS_OPTION SET Value = 'AS400MVS', Display = 'AS400MVS組' WHERE Value = 'MVS/AS400';
UPDATE SYS_OPTION SET Value = 'IMS', Display = 'IMS組' WHERE Value = 'IMS/CICS';
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('15', 'WORK_LEVEL', '工作單作業關卡的工作項目', 'CICS', 'CICS組', null, 'System', '2019-08-02 18:01:07', 'System', '2019-08-02 18:01:07');

-- 0005019: 【UAT】CR_21 首頁依登入者AD權限顯示表單資訊與UI調整
UPDATE SYS_OPTION_ROLE SET Condition = 'SP_AS400MVS' WHERE Condition = 'SP_MVS';

-- 0005167: 【SIT】需求會辦單：給系統科，副科指派平行會辦後送出，錯誤訊息
ALTER TABLE FORM ALTER COLUMN Parallel nvarchar(30) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL
ALTER TABLE FORM_VERIFY_LOG ALTER COLUMN Parallel nvarchar(30) COLLATE Chinese_Taiwan_Stroke_CI_AS NULL

--0005025: 【UAT】CR_38 平行會辦功能(系統科工作單_組別會簽設定)
ALTER TABLE FORM ADD Parallel nvarchar(10)
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'平行會辦群組' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM', @level2type=N'COLUMN',@level2name=N'Parallel'
ALTER TABLE FORM_VERIFY_LOG ADD Parallel nvarchar(10)
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'平行會辦群組' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_VERIFY_LOG', @level2type=N'COLUMN',@level2name=N'Parallel'
ALTER TABLE FORM_JOB_INFO_SYS_DETAIL ADD SPCGroups nvarchar(2000)
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'會辦系統科群組清單' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_JOB_INFO_SYS_DETAIL', @level2type=N'COLUMN',@level2name=N'SPCGroups'

ALTER TABLE FORM_PROCESS_DETAIL_APPLY_Q ADD [Parallels] [nvarchar](200) NULL;
ALTER TABLE FORM_PROCESS_DETAIL_APPLY_Q ADD [IsParallel] [char](1) NULL DEFAULT ('N');
ALTER TABLE [dbo].[FORM_PROCESS_DETAIL_APPLY_Q]  WITH CHECK ADD CHECK  (([IsParallel]='N' OR [IsParallel]='Y'));
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'平行會辦群組' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_DETAIL_APPLY_Q', @level2type=N'COLUMN',@level2name=N'Parallels';
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否平行會辦關卡' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_DETAIL_APPLY_Q', @level2type=N'COLUMN',@level2name=N'IsParallel';

ALTER TABLE FORM_PROCESS_DETAIL_APPLY_SR ADD [Parallels] [nvarchar](200) NULL;
ALTER TABLE FORM_PROCESS_DETAIL_APPLY_SR ADD [IsParallel] [char](1) NULL DEFAULT ('N');
ALTER TABLE [dbo].[FORM_PROCESS_DETAIL_APPLY_SR]  WITH CHECK ADD CHECK  (([IsParallel]='N' OR [IsParallel]='Y'));
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'平行會辦群組' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_DETAIL_APPLY_SR', @level2type=N'COLUMN',@level2name=N'Parallels';
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否平行會辦關卡' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_DETAIL_APPLY_SR', @level2type=N'COLUMN',@level2name=N'IsParallel';

ALTER TABLE FORM_PROCESS_DETAIL_APPLY_JOB ADD [Parallels] [nvarchar](200) NULL;
ALTER TABLE FORM_PROCESS_DETAIL_APPLY_JOB ADD [IsParallel] [char](1) NULL DEFAULT ('N');
ALTER TABLE [dbo].[FORM_PROCESS_DETAIL_APPLY_JOB]  WITH CHECK ADD CHECK  (([IsParallel]='N' OR [IsParallel]='Y'));
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'平行會辦群組' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_DETAIL_APPLY_JOB', @level2type=N'COLUMN',@level2name=N'Parallels';
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否平行會辦關卡' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_DETAIL_APPLY_JOB', @level2type=N'COLUMN',@level2name=N'IsParallel';

DELETE FROM SYS_OPTION WHERE OptionId='SPGROUP';
DELETE FROM SYS_OPTION WHERE OptionId='WORK_LEVEL';

----------------------------------SYS_MENU-------------------
ALTER TABLE SYS_MENU ADD OpenWindow bit;
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否另開視窗' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_MENU', @level2type=N'COLUMN',@level2name=N'OpenWindow'
GO

UPDATE SYS_MENU SET OpenWindow = 0
UPDATE SYS_MENU SET OpenWindow = 1 WHERE MenuId IN ('F0402','F0403','F0404')
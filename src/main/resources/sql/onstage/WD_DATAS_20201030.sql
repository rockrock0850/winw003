-- 0005027: 【UAT】CR_41 新增「批次作業中斷對策表管理」功能
INSERT INTO SYS_MENU (MenuId, MenuName, Path, Comment, OrderId, Enabled, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, OpenWindow) VALUES ('F0405', '新增批次作業中斷對策表管理', 'formSearch/addPage/BA', '', 5, 1, 'F04', null, null, null, null, 1);
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('13', 'FORM_CLASS', '表單類別清單', 'BA', '批次作業中斷對策表管理', null, 'System', '2020-10-22 11:23:59', 'System', '2020-10-22 11:23:59');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('BA', '1,2,3,5,6,7,81,82,83,84,85,86', 'System', '2020-08-27 00:00:00', 'System', '2020-08-27 00:00:00');


-- 0006575: 【PRO】F_簽核紀錄_關卡群組，應紀錄當時簽核軌跡之人員身分角色
UPDATE FORM_VERIFY_LOG SET GroupId = '' WHERE Id = ;-- 針對異常的審核紀錄更新該筆紀錄的審核者群組
UPDATE FVL SET FVL.GroupId = SG.GroupId FROM FORM_VERIFY_LOG FVL JOIN LDAP_USER LU ON LU.UserId=FVL.UserId JOIN SYS_GROUP SG ON SG.SysGroupId=LU.SysGroupId WHERE FVL.GroupId IS NULL;

-- 0005033: 【UAT】CR_36 表單查詢UI調整與新增進階查詢項目
DELETE FJC FROM FORM_JOB_COUNTERSIGNED FJC JOIN FORM F ON F.FormId=FJC.FormId WHERE FJC.Division NOT LIKE SUBSTRING(F.DivisionSolving, 8, 9)+'%'

DELETE FROM FORM_FIELDS;
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('SR', '1,18,11,3,15,13,6,2,7,4,5,8,9,10,12,14,16,17,19,20,21,22,23,24,25,26,27,801,901,902,903', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('Q', '1,29,34,2,35,6,7,5,3,4,8,9,10,28,30,31,32,33,36,37,38,39,40,41,42,43,44,45,46,47,48,801,802,803,804,901,902,903', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('INC', '1,49,62,2,6,7,5,3,4,8,9,10,50,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,67,68,69,70,71,72,73,801,802,803,805,901,902,903', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('CHG', '1,67,2,7,6,72,68,3,4,5,66,69,70,71,73,74,75,76,77,78,79,80,81,82,901,902,903,904,905,906', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('JOB_AP', '1,227,5,7,212,223,2,3,213,204,219,6,201,202,203,205,206,207,208,209,210,211,236,215,217,218,220,221,222,224,225,226,216', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('SR_C', '1,875,6,7,2,907,3,876,877,878,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('Q_C', '1,875,6,7,2,907,3,876,877,878,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('INC_C', '1,875,6,7,2,907,3,876,877,878,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('JOB_SP', '1,243,227,2,238,3,5,6,7,201,229,213,230,231,232,233,234,235,214,237,239,240,241,242,244,245', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('JOB_AP_C', '1,212,219,3,2,7,228,204,213,202,203,205,206,222,223,224,225,226,220,221,207,208,209,210,211,236,215,217,218,216', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('JOB_SP_C', '1,2,2001,227,246,3,5,6,7,201,212,213,230,233,234,235,18,20,243,242,244,245', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('QA', '1,39,41,5,30,802,803,801,35', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('AP1', '2001,2002,2003,2004,2005', 'System', '2020-09-17 00:00:00', 'System', '2020-09-17 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('AP2', '2001,2002,2003,2004,2005', 'System', '2020-09-17 00:00:00', 'System', '2020-09-17 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('AP3', '2001,2002,2003,2004,2005', 'System', '2020-09-17 00:00:00', 'System', '2020-09-17 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('AP4', '2001,2002,2003,2004,2005', 'System', '2020-09-17 00:00:00', 'System', '2020-09-17 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('SP', '2006,2007,2008,2009', 'System', '2020-09-17 00:00:00', 'System', '2020-09-17 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('PT', '2001,2002,2003,2004,2005', 'System', '2020-09-17 00:00:00', 'System', '2020-09-17 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('EA', '2001,2002,2003,2004,2005', 'System', '2020-09-17 00:00:00', 'System', '2020-09-17 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('OA', '2001,2002,2003,2004,2005', 'System', '2020-09-17 00:00:00', 'System', '2020-09-17 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('PLAN', '2001,2002,2003,2004,2005,2010,2011', 'System', '2020-09-17 00:00:00', 'System', '2020-09-17 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('MGMT', '2001,2002,2003,2004,2005,2010,2011', 'System', '2020-09-17 00:00:00', 'System', '2020-09-17 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('DC', '2011', 'System', '2020-09-17 00:00:00', 'System', '2020-09-17 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('DC1', '2012,2013,2014,2015,2016,2017,2018,2019,2020,2021,2022,2023,2024,2025,2026,2027,2028', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('DC2', '2012,2013,2014,2015,2016,2017,2018,2019,2020,2021,2022,2023,2024,2025,2026,2027,2028', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('DC3', '2012,2013,2014,2015,2027,2028,2029', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('DB', '2044,2045,2046,2047,2048,2036', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('BATCH', '2049,2050,2051,2037,2030,2031,2032,2033,2034,2035,2036,2038,2039,2040,2041,2042,2043', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');

-- 0005033: 【UAT】CR_36 表單查詢UI調整與新增進階查詢項目
DELETE FROM SYS_OPTION WHERE OptionId='FORM_CLASS';
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active) VALUES (7, 'FORM_CLASS', '表單類別清單', 'CHG', '變更單', null, 'System', '2019-08-29 11:23:59', 'System', '2019-08-29 11:23:59', 'Y');
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active) VALUES (5, 'FORM_CLASS', '表單類別清單', 'INC', '事件單', null, 'System', '2019-08-29 11:23:59', 'System', '2019-08-29 11:23:59', 'Y');
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active) VALUES (6, 'FORM_CLASS', '表單類別清單', 'INC_C', '事件會辦單', null, 'System', '2019-08-29 11:23:59', 'System', '2019-08-29 11:23:59', 'Y');
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active) VALUES (8, 'FORM_CLASS', '表單類別清單', 'JOB_AP', '工作單', null, 'System', '2019-08-29 11:23:59', 'System', '2019-08-29 11:23:59', 'Y');
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active) VALUES (10, 'FORM_CLASS', '表單類別清單', 'JOB_AP_C', '工作會辦單', null, 'System', '2019-08-29 11:23:59', 'System', '2019-08-29 11:23:59', 'Y');
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active) VALUES (9, 'FORM_CLASS', '表單類別清單', 'JOB_SP', '系統科工作單', null, 'System', '2019-08-29 11:23:59', 'System', '2019-08-29 11:23:59', 'Y');
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active) VALUES (11, 'FORM_CLASS', '表單類別清單', 'JOB_SP_C', '系統科工作會辦單', null, 'System', '2019-08-29 11:23:59', 'System', '2019-08-29 11:23:59', 'Y');
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active) VALUES (3, 'FORM_CLASS', '表單類別清單', 'Q', '問題單', null, 'System', '2019-08-29 11:23:59', 'System', '2019-08-29 11:23:59', 'Y');
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active) VALUES (4, 'FORM_CLASS', '表單類別清單', 'Q_C', '問題會辦單', null, 'System', '2019-08-29 11:23:59', 'System', '2019-08-29 11:23:59', 'Y');
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active) VALUES (1, 'FORM_CLASS', '表單類別清單', 'SR', '需求單', null, 'System', '2019-08-29 11:23:59', 'System', '2019-08-29 11:23:59', 'Y');
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active) VALUES (2, 'FORM_CLASS', '表單類別清單', 'SR_C', '需求會辦單', null, 'System', '2019-08-29 11:23:59', 'System', '2019-08-29 11:23:59', 'Y');
INSERT INTO SYS_OPTION (Sort, OptionId, Name, Value, Display, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, Active) VALUES (12, 'FORM_CLASS', '表單類別清單', 'QA', '問題知識庫', null, 'System', '2019-08-29 11:23:59', 'System', '2019-08-29 11:23:59', 'Y');

-- 0006050: 【PRO】CR_4 稽催通知(郵件功能、系統調整與DB異動)
INSERT INTO SYS_MENU (MenuId, MenuName, Path, Comment, OrderId, Enabled, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, OpenWindow) VALUES ('F0312', '稽催通知參數設定', 'auditNotifyParams', '', 13, 1, 'F03', null, null, null, null, 0);

-- 2020/08/13: 【DEV】系統管理-->功能選單_新增項目
DELETE FROM SYS_MENU WHERE MenuId='F0308';
DELETE FROM SYS_MENU WHERE MenuId='F0309';
DELETE FROM SYS_MENU WHERE MenuId='F0310';
DELETE FROM SYS_MENU WHERE MenuId='F0312';
INSERT INTO SYS_MENU (MenuId, MenuName, [Path], Comment, OrderId, Enabled, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, OpenWindow) VALUES ('F0308', '系統名稱管理', 'systemNameManagement/init', '', 8, 1, 'F03', NULL, NULL, NULL, NULL, 0); 
INSERT INTO SYS_MENU (MenuId, MenuName, [Path], Comment, OrderId, Enabled, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, OpenWindow) VALUES('F0309', '標準變更作業管理', 'standradChgManagement/init', '', 9, 1, 'F03', NULL, NULL, NULL, NULL, 0);
INSERT INTO SYS_MENU (MenuId, MenuName, [Path], Comment, OrderId, Enabled, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, OpenWindow) VALUES('F0310', '工作組別管理', 'jobGroupManagement/init', '', 10, 1, 'F03', NULL, NULL, NULL, NULL, 0);
INSERT INTO SYS_MENU (MenuId, MenuName, [Path], Comment, OrderId, Enabled, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, OpenWindow) VALUES('F0311', '工作要項管理', 'jobItemManagement/init', '', 11, 1, 'F03', NULL, NULL, NULL, NULL, 0);
INSERT INTO SYS_MENU (MenuId, MenuName, [Path], Comment, OrderId, Enabled, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, OpenWindow) VALUES('F0312', '服務類別管理', 'serviceTypeManagement/init', '', 12, 1, 'F03', NULL, NULL, NULL, NULL, 0);
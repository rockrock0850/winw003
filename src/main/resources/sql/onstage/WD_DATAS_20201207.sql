-- 0006833: 【PRO】F_表單查詢：「摘要」欄位查詢異常與新增「開單科別」「開單人員」欄位
DELETE FROM FORM_FIELDS WHERE FormClass = 'QA';
DELETE FROM FORM_FIELDS WHERE FormClass = 'BA';
DELETE FROM FORM_FIELDS WHERE FormClass = 'Q_C';
DELETE FROM FORM_FIELDS WHERE FormClass = 'SR_C';
DELETE FROM FORM_FIELDS WHERE FormClass = 'INC_C';
DELETE FROM FORM_FIELDS WHERE FormClass = 'JOB_AP_C';
DELETE FROM FORM_FIELDS WHERE FormClass = 'JOB_SP_C';

-- 更新
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('BA', '1,2,3,5,6,7,81,82,83,84,85,86,4', 'System', '2020-08-27 00:00:00', 'System', '2020-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('QA', '1,39,41,5,30,802,803,801,35,4', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('JOB_AP_C', '1,212,219,3,2,7,4,5,228,204,213,202,203,205,206,222,223,224,225,226,220,221,207,208,209,210,211,236,215,217,218,216', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('JOB_SP_C', '1,2,2001,227,246,3,4,5,6,7,201,212,213,230,233,234,235,18,20,243,242,244,245', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('SR_C', '1,875,6,7,2,907,3,4,5,876,877,878,1000,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('Q_C', '1,875,6,7,2,907,3,4,5,876,877,878,1000,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('INC_C', '1,875,6,7,2,907,3,4,5,876,877,878,1000,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');

--  復原
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('BA', '1,2,3,5,6,7,81,82,83,84,85,86', 'System', '2020-08-27 00:00:00', 'System', '2020-08-27 00:00:00');
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('QA', '1,39,41,5,30,802,803,801,35', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('JOB_AP_C', '1,212,219,3,2,7,228,204,213,202,203,205,206,222,223,224,225,226,220,221,207,208,209,210,211,236,215,217,218,216', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('JOB_SP_C', '1,2,2001,227,246,3,5,6,7,201,212,213,230,233,234,235,18,20,243,242,244,245', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('SR_C', '1,875,6,7,2,907,3,876,877,878,1000,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('Q_C', '1,875,6,7,2,907,3,876,877,878,1000,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('INC_C', '1,875,6,7,2,907,3,876,877,878,1000,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');

-- 0006818: 【PRO】F_表單查詢：事件單「影響範圍」「緊急程度」未帶出值
DELETE FROM FORM_FIELDS WHERE FormClass = 'CHG';
-- 更新資料
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('CHG', '1,67,2,7,6,72,68,3,4,5,66,3000,3001,3002,3003,74,75,76,77,78,79,80,81,82,901,902,903,904,905,906', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
-- 回復資料
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('CHG', '1,67,2,7,6,72,68,3,4,5,66,69,70,71,73,74,75,76,77,78,79,80,81,82,901,902,903,904,905,906', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');

-- 0006646: 【UAT】ISO_CR_8 變更成功失敗統計表
-- 補上舊的事件會辦單的主單預計完成時間欄位
WITH P AS (
	SELECT F.SourceId, F.FormId FROM FORM_INFO_DATE FID JOIN FORM F ON F.FormId = FID.FormId WHERE FID.FormClass='INC_C' AND FID.MECT IS NULL
),
T AS (
	SELECT DISTINCT P.SourceId FormId, FID.ECT FROM FORM_INFO_DATE FID JOIN P ON P.SourceId = FID.FormId
)
UPDATE FID SET MECT = T.ECT, UpdatedAt = '2020/11/23 23:59:59' FROM FORM_INFO_DATE FID JOIN P P ON P.FormId = FID.FormId JOIN T T ON T.FormId = P.SourceId WHERE FID.FormClass='INC_C' AND FID.MECT IS NULL;

-- 資料復原
--UPDATE FID SET MECT = NULL FROM FORM_INFO_DATE FID WHERE FID.UpdatedAt = '2020/11/23 23:59:59';

-- 0006690: 【PRO】表單查詢：SP工作單與AP工作會辦單_欄位調整
-- 新增會辦科欄位
DELETE FROM FORM_FIELDS WHERE FormClass = 'JOB_SP';
-- 更新欄位
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('JOB_SP', '1,243,227,2,238,3,5,6,7,201,229,213,230,231,232,233,234,235,214,237,239,240,241,242,244,245,216', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
-- 復原欄位
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('JOB_SP', '1,243,227,2,238,3,5,6,7,201,229,213,230,231,232,233,234,235,214,237,239,240,241,242,244,245', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');

-- 0006646: 【UAT】ISO_CR_8 變更成功失敗統計表
-- 刪除欄位資訊
DELETE FROM FORM_FIELDS WHERE FormClass = 'Q';
DELETE FROM FORM_FIELDS WHERE FormClass = 'SR';
DELETE FROM FORM_FIELDS WHERE FormClass = 'INC';
DELETE FROM FORM_FIELDS WHERE FormClass = 'Q_C';
DELETE FROM FORM_FIELDS WHERE FormClass = 'SR_C';
DELETE FROM FORM_FIELDS WHERE FormClass = 'INC_C';
-- 更新欄位
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('SR', '1,18,11,3,15,13,6,2,7,4,5,8,9,10,12,1000,14,16,17,19,20,21,22,23,24,25,26,27,801,901,902,903', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('Q', '1,29,34,2,35,6,7,5,3,4,8,9,10,28,1000,30,31,32,33,36,37,38,39,40,41,42,43,44,45,46,47,48,801,802,803,804,901,902,903', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('INC', '1,49,62,2,6,7,5,3,4,8,9,10,50,51,1000,52,53,54,55,56,57,58,59,60,61,63,64,65,66,67,68,69,70,71,72,73,801,802,803,805,901,902,903', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('SR_C', '1,875,6,7,2,907,3,876,877,878,1000,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('Q_C', '1,875,6,7,2,907,3,876,877,878,1000,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');
INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('INC_C', '1,875,6,7,2,907,3,876,877,878,1000,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');
-- 復原欄位
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('SR', '1,18,11,3,15,13,6,2,7,4,5,8,9,10,12,14,16,17,19,20,21,22,23,24,25,26,27,801,901,902,903', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('Q', '1,29,34,2,35,6,7,5,3,4,8,9,10,28,30,31,32,33,36,37,38,39,40,41,42,43,44,45,46,47,48,801,802,803,804,901,902,903', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('INC', '1,49,62,2,6,7,5,3,4,8,9,10,50,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,67,68,69,70,71,72,73,801,802,803,805,901,902,903', 'System', '2019-08-27 00:00:00', 'System', '2019-08-27 00:00:00');
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('SR_C', '1,875,6,7,2,907,3,876,877,878,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('Q_C', '1,875,6,7,2,907,3,876,877,878,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');
--INSERT INTO FORM_FIELDS (FormClass, FormFields, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt) VALUES ('INC_C', '1,875,6,7,2,907,3,876,877,878,879,880,881,882,883,884,885,886,887,888,901,902,903', 'System', '2020-09-09 00:00:00', 'System', '2020-09-09 00:00:00');
-- 新增功能選單
INSERT INTO SYS_MENU (MenuId, MenuName, Path, Comment, OrderId, Enabled, ParentId, UpdatedBy, UpdatedAt, CreatedBy, CreatedAt, OpenWindow) VALUES ('F050405', '變更成功失敗統計表', 'reportOperation/cHGAlterResult/init', '', 5, 1, 'F0504', null, null, null, null, 0);
-- 補上舊表單的FORM.IsAlterDone欄位的資料
WITH
    RST AS
    (
        SELECT DISTINCT
            F.SourceId AS FormId
        FROM
            FORM F
        WHERE
            F.FormClass = 'CHG'
        AND F.FormStatus = 'CLOSED'
    )
UPDATE
    F
SET
    IsAlterDone =
    CASE
        WHEN (FID.ACT <= FID.ECT OR  FID.ACT <= FID.MECT)
        THEN 'Y'
        WHEN (FID.ACT IS NULL OR  FID.ACT = '')
        THEN NULL
        ELSE 'N'
    END
FROM
    RST R
JOIN
    FORM F
ON
    F.FormId = R.FormId
JOIN
    FORM_INFO_DATE FID
ON
    FID.FormId = F.FormId
JOIN
    FORM_VERIFY_LOG FVL
ON
    F.FormId = FVL.FormId
WHERE
    FVL.VerifyType = 'REVIEW' ;
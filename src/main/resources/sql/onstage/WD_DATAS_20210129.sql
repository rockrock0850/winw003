-- 0006869: 【PRO】F_表單查詢：選需求單等，查詢/匯出等待時間異常(約30秒)
-- 移除901,902,903
UPDATE FORM_FIELDS SET FormFields = '1,29,34,2,35,6,7,5,3,4,8,9,10,28,1000,30,31,32,33,36,37,38,39,40,41,42,43,44,45,46,47,48,801,802,803,804' WHERE FormClass = 'Q'
UPDATE FORM_FIELDS SET FormFields = '1,49,62,2,6,7,5,3,4,8,9,10,50,51,1000,52,53,54,55,56,57,58,59,60,61,63,64,65,66,67,68,69,70,71,72,73,801,802,803,805' WHERE FormClass = 'INC'
UPDATE FORM_FIELDS SET FormFields = '1,67,2,7,6,72,68,3,4,5,66,3000,3001,3002,3003,74,75,76,77,78,79,80,81,82,904,905,906' WHERE FormClass = 'CHG'
UPDATE FORM_FIELDS SET FormFields = '1,875,6,7,2,907,3,4,5,876,877,878,1000,879,880,881,882,883,884,885,886,887,888' WHERE FormClass = 'Q_C'
UPDATE FORM_FIELDS SET FormFields = '1,875,6,7,2,907,3,4,5,876,877,878,1000,879,880,881,882,883,884,885,886,887,888' WHERE FormClass = 'SR_C'
UPDATE FORM_FIELDS SET FormFields = '1,875,6,7,2,907,3,4,5,876,877,878,1000,879,880,881,882,883,884,885,886,887,888' WHERE FormClass = 'INC_C'
UPDATE FORM_FIELDS SET FormFields = '1,227,5,7,212,223,2,3,213,204,219,6,201,202,203,205,206,207,208,209,210,211,236,215,217,218,220,221,222,224,225,226,216' WHERE FormClass = 'JOB_AP'
UPDATE FORM_FIELDS SET FormFields = '1,243,227,2,238,3,5,6,7,201,229,213,230,231,232,233,234,235,214,237,239,240,241,242,244,245,216' WHERE FormClass = 'JOB_SP'
UPDATE FORM_FIELDS SET FormFields = '1,212,219,3,2,7,4,5,228,204,213,202,203,205,206,222,223,224,225,226,220,221,207,208,209,210,211,236,215,217,218,216' WHERE FormClass = 'JOB_AP_C'
UPDATE FORM_FIELDS SET FormFields = '1,2,2001,227,246,3,4,5,6,7,201,212,213,230,233,234,235,18,20,243,242,244,245' WHERE FormClass = 'JOB_SP_C'
-- 0006869: 【PRO】F_表單查詢：選需求單等，查詢/匯出等待時間異常(約30秒)
-- 移除901,902,903
UPDATE FORM_FIELDS SET FormFields = '1,18,11,3,15,13,6,2,7,4,5,8,9,10,12,1000,14,16,17,19,20,21,22,23,24,25,26,27,801' WHERE FormClass = 'SR'
-- 0006876: 【PRO】F_事件單：事件主類別等欄位選項調整
-- 更新
UPDATE SYS_OPTION SET Display = '分行設備諮詢' WHERE OptionId = '4' AND [Value] = '118';
-- 還原
--UPDATE SYS_OPTION SET Display = '端末硬體故障-連線問題' WHERE OptionId = '4' AND [Value] = '118';
-- 更新
UPDATE SYS_OPTION SET Display = '影響個別分行、單位、或少數同仁；所有事件類型為服務請求或服務諮詢之事件。' WHERE OptionId = 'EFFECT_SCOPE' AND [Value] = '3';
-- 還原
--UPDATE SYS_OPTION SET Display = '影響個別分行、單位、或少數同仁；所有事件類型為服務請求之事件' WHERE OptionId = 'EFFECT_SCOPE' AND [Value] = '3';
-- 更新
UPDATE SYS_OPTION SET Display = '服務異常：本行一般營運項目部分功能無法使用(與交易業務間接或與公司日常營運管理相關系統)。<br><br>服務異常：凡造成業務短暫延遲或停頓，但可立即修復之資訊異常狀況。<br><br>服務請求：與業務作業間接相關系統。<br><br>服務諮詢：諮詢內容與業務作業直接或間接相關。' WHERE OptionId = 'INC_URGENT_LEVEL' AND [Value] = '3';
-- 還原
--UPDATE SYS_OPTION SET Display = '服務異常：本行一般營運項目部分功能無法使用(與交易業務間接或與公司日常營運管理相關系統)。<br><br>服務異常：凡造成業務短暫延遲或停頓，但可立即修復之資訊異常狀況。<br><br>服務請求：與業務作業間接相關系統' WHERE OptionId = 'INC_URGENT_LEVEL' AND [Value] = '3';
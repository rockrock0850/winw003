-- 0006647: 【UAT】ISO_CR_7 變更成功、失敗的選項及標註機制
-- 新增欄位
ALTER TABLE FORM ADD IsAlterDone [char](1)
-- 復原欄位
--ALTER TABLE FORM DROP COLUMN IsAlterDone;
-- 新增欄位備註
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否變更成功(NULL=沒開變更單或是沒有填預計完成時間或實際完成時間)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM', @level2type=N'COLUMN',@level2name=N'IsAlterDone'
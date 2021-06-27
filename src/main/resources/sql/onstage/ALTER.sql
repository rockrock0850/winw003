-- 0007098: 【008】ISO_16_知識庫：引用「處理方案」內欄位
ALTER TABLE FORM_PROGRAM DROP COLUMN Knowledge1 GO
ALTER TABLE FORM_PROGRAM DROP COLUMN Knowledge2 GO
ALTER TABLE FORM_PROGRAM ADD Knowledges nvarchar(2000) GO
DECLARE @v sql_variant SET @v = N'知識庫原因與根因' EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_PROGRAM', N'COLUMN', N'Knowledges' GO

UPDATE SYS_OPTION SET Active = 'N' WHERE OptionId = 'FORM_CLASS' AND [Value] = 'QA' GO
ALTER TABLE SYS_OPTION ADD IsKnowledge nvarchar(2) DEFAULT 'Y' NOT NULL GO
DECLARE @v sql_variant SET @v = N'是否加入知識庫(只適用於知識庫)' EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'SYS_OPTION', N'COLUMN', N'isDisplayKnowledge'
GO
ALTER TABLE FORM_INFO_Q_DETAIL ADD Solutions nvarchar(20) GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'參照知識庫' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_Q_DETAIL', @level2type=N'COLUMN',@level2name=N'Solutions'
GO

/****** Object:  Table [dbo].[FORM_INFO_KL_DETAIL]    Script Date: 2020/4/13 下午 05:21:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[FORM_INFO_KL_DETAIL](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[UpdatedBy] [nvarchar](50) NULL,
	[UpdatedAt] [datetime] NULL,
	[CreatedBy] [nvarchar](50) NULL,
	[CreatedAt] [datetime] NULL,
	[FormId] [nvarchar](20) NOT NULL,
	[Summary] [nvarchar](500) NULL,
	[Indication] [nvarchar](2000) NULL,
	[Reason] [nvarchar](2000) NULL,
	[ProcessProgram] [nvarchar](2000) NULL,
	[SystemId] [nvarchar](30) NULL,
	[System] [nvarchar](300) NULL,
	[AssetGroup] [nvarchar](30) NULL,
	[SClass] [nvarchar](30) NULL,
	[SSubClass] [nvarchar](30) NULL,
	[Knowledges] [nvarchar](2000) NULL,
	[Solutions] [nvarchar](2000) NULL,
	[FlowName] [nvarchar](10) NULL,
	[IsEnabled] [char](1) NULL DEFAULT ('Y'),
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [FORM_INFO_KL_DETAIL_FormId] UNIQUE NONCLUSTERED 
(
	[FormId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'知識庫明細' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'Id'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'變更者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'UpdatedBy'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'變更時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'UpdatedAt'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'CreatedBy'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'CreatedAt'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'表單編號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'FormId'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'摘要' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'Summary'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'征兆' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'Indication'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'原因' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'Reason'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'處理方案' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'ProcessProgram'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'系統編號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'SystemId'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'系統名稱' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'System'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'資訊資產群組' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'AssetGroup'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'服務類別' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'SClass'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'服務子類別' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'SSubClass'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'知識庫根因' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'Knowledges'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'相關解決方案' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'Solutions'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流程別(與表單類別一致)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'FlowName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否啟用' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_INFO_KL_DETAIL', @level2type=N'COLUMN',@level2name=N'IsEnabled'
GO

-- FORM 新增欄位
ALTER TABLE FORM ADD IsExtended char(1) DEFAULT 'N' NULL GO

DECLARE @v sql_variant 
SET @v = N'判斷該表單是否已延期'
EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM', N'COLUMN', N'IsExtended'
GO

-- 復原
ALTER TABLE FORM DROP COLUMN IsExtended GO

-- FORM_INFO_DATE 新增欄位
ALTER TABLE FORM_INFO_DATE ADD OECT DATETIME GO

DECLARE @v sql_variant 
SET @v = N'紀錄原始預計完成時間(給表單是否延期最為基準判斷)'
EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_INFO_DATE', N'COLUMN', N'OECT'
GO

-- 復原
ALTER TABLE FORM_INFO_DATE DROP COLUMN OECT GO


-- 變更單detail新增欄位
ALTER TABLE FORM_INFO_CHG_DETAIL ADD IsScopeChanged char(1) DEFAULT 'N' NULL GO

DECLARE @v sql_variant 
SET @v = N'變更範圍是否不同'
EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_INFO_CHG_DETAIL', N'COLUMN', N'IsScopeChanged'
GO

-- 復原
ALTER TABLE FORM_INFO_CHG_DETAIL DROP COLUMN IsScopeChanged GO

--擴充衝擊分析功能: 新增欄位「是否加總」、「是否啟用」--
ALTER TABLE QUESTION_MAINTAIN ADD IsAddUp char(1) DEFAULT 'Y' NULL
GO

DECLARE @v sql_variant 
SET @v = N'是否加總'
EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'QUESTION_MAINTAIN', N'COLUMN', N'IsAddUp'
GO

ALTER TABLE QUESTION_MAINTAIN ADD IsEnable char(1) DEFAULT 'Y' NULL
GO

DECLARE @v sql_variant 
SET @v = N'是否啟用'
EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'QUESTION_MAINTAIN', N'COLUMN', N'IsEnable'
GO

ALTER TABLE FORM_IMPACT_ANALYSIS ADD IsAddUp char(1) DEFAULT 'Y' NULL
GO

DECLARE @v sql_variant 
SET @v = N'是否加總'
EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_IMPACT_ANALYSIS', N'COLUMN', N'IsAddUp'
GO

-- 0007098: 【008】ISO_16_知識庫：引用「處理方案」內欄位
-- 更新
ALTER TABLE FORM_PROGRAM ADD Knowledge1 nvarchar(1000) GO
ALTER TABLE FORM_PROGRAM ADD Knowledge2 nvarchar(1000) GO
DECLARE @v sql_variant SET @v = N'知識庫原因類別' EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_PROGRAM', N'COLUMN', N'Knowledge1'  GO
DECLARE @v sql_variant SET @v = N'知識庫子原因類別' EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_PROGRAM', N'COLUMN', N'Knowledge2'  GO
DECLARE @v sql_variant SET @v = N'暫時性解決方案' EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_PROGRAM', N'COLUMN', N'Temporary'  GO

-- 復原
ALTER TABLE FORM_PROGRAM DROP COLUMN Knowledge1 GO
ALTER TABLE FORM_PROGRAM DROP COLUMN Knowledge2 GO

--AP工作單：表單資訊「程式上線」
-- 更新
ALTER TABLE FORM_JOB_INFO_AP_DETAIL ADD IsProgramOnline char(1) DEFAULT 'N' NULL GO
ALTER TABLE FORM_JOB_INFO_AP_DETAIL  WITH CHECK ADD CHECK  (IsProgramOnline='N' OR IsProgramOnline='Y') GO
DECLARE @v sql_variant 
SET @v = N'程式上線'
EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_JOB_INFO_AP_DETAIL', N'COLUMN', N'IsProgramOnline'
GO

-- 復原
ALTER TABLE FORM_JOB_INFO_AP_DETAIL DROP CONSTRAINT DF__FORM_JOB___IsPro__62BA8D0A GO
ALTER TABLE FORM_JOB_INFO_AP_DETAIL DROP COLUMN IsProgramOnline GO

--同一事件兩日內復發
ALTER TABLE dbo.FORM_INFO_DATE ADD
	IsSameInc char(1) NULL
GO

DECLARE @v sql_variant 
SET @v = N'同一事件兩日內復發'
EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_INFO_DATE', N'COLUMN', N'IsSameInc'
GO

ALTER TABLE dbo.FORM_INFO_DATE ADD CONSTRAINT
	DF_FORM_INFO_DATE_IsSameInc DEFAULT 'N' FOR IsSameInc
GO

--事件單：擴充「上線失敗」欄位
ALTER TABLE FORM_INFO_INC_DETAIL ADD
	IsOnlineFail char(1) NULL,
	OnlineTime datetime NULL,
	OnlineJobFormId nvarchar(20) NULL
GO
DECLARE @v sql_variant 
SET @v = N'上線失敗'
EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_INFO_INC_DETAIL', N'COLUMN', N'IsOnlineFail'
GO
DECLARE @v sql_variant 
SET @v = N'上線時間'
EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_INFO_INC_DETAIL', N'COLUMN', N'OnlineTime'
GO
DECLARE @v sql_variant 
SET @v = N'上線失敗的工作單單號'
EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_INFO_INC_DETAIL', N'COLUMN', N'OnlineJobFormId'
GO

-- 0007267:【008】CR_變更單、工作單：加入「未有修改程式」選項
-- 新增	變更單 「未有修改程式」欄位
ALTER TABLE FORM_INFO_CHG_DETAIL ADD IsModifyProgram char(1) NULL DEFAULT ('N') GO 
DECLARE @v sql_variant 
SET @v = N'未有修改程式'
EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_INFO_CHG_DETAIL', N'COLUMN', N'IsModifyProgram' GO 
-- 還原
ALTER TABLE FORM_INFO_CHG_DETAIL DROP COLUMN IsModifyProgram GO 

-- 新增	AP工作單 「未有修改程式」欄位
ALTER TABLE FORM_JOB_INFO_AP_DETAIL ADD IsModifyProgram char(1) NULL DEFAULT ('N') GO 
DECLARE @v sql_variant 
SET @v = N'未有修改程式'
EXECUTE sp_addextendedproperty N'MS_Description', @v, N'SCHEMA', N'dbo', N'TABLE', N'FORM_JOB_INFO_AP_DETAIL', N'COLUMN', N'IsModifyProgram' GO

-- 還原
ALTER TABLE FORM_JOB_INFO_AP_DETAIL DROP COLUMN IsModifyProgram GO
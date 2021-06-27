--系統記錄(LOG)：附件新增、附件刪除
DROP TABLE [FORM_FILE_LOG]
GO

CREATE TABLE [FORM_FILE_LOG](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[UpdatedBy] [nvarchar](50) NULL,
	[UpdatedAt] [datetime] NULL,
	[CreatedBy] [nvarchar](50) NULL,
	[CreatedAt] [datetime] NULL,
	[FormId] [varchar](20) NOT NULL,
	[ActionType] [varchar](10) NULL,
 CONSTRAINT [PK__FORM_FILE_LOG__3214EC07BE2956ED] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_FILE_LOG', @level2type=N'COLUMN',@level2name=N'Id'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'變更者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_FILE_LOG', @level2type=N'COLUMN',@level2name=N'UpdatedBy'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'變更時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_FILE_LOG', @level2type=N'COLUMN',@level2name=N'UpdatedAt'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_FILE_LOG', @level2type=N'COLUMN',@level2name=N'CreatedBy'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_FILE_LOG', @level2type=N'COLUMN',@level2name=N'CreatedAt'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'表單編號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_FILE_LOG', @level2type=N'COLUMN',@level2name=N'FormId'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'執行動作(ADD或DELETE)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_FILE_LOG', @level2type=N'COLUMN',@level2name=N'ActionType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'附件上傳LOG紀錄' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_FILE_LOG'
GO

--0007105: 【008】CR-21_簽核視窗_功能擴充
CREATE TABLE [FORM_PROCESS_LEVEL_WORDING](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[CreatedAt] [datetime] NULL,
	[CreatedBy] [nvarchar](50) NULL,
	[UpdatedAt] [datetime] NULL,
	[UpdatedBy] [nvarchar](50) NULL,
	[DetailId] [nvarchar](20) NOT NULL,
	[ProcessOrder] [int] NOT NULL,
	[Type] [nvarchar](20) NOT NULL,
	[Wording] [nvarchar](100) NULL,
	[WordingLevel] [int] NOT NULL
) ON [PRIMARY]

GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_LEVEL_WORDING', @level2type=N'COLUMN',@level2name=N'CreatedAt'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_LEVEL_WORDING', @level2type=N'COLUMN',@level2name=N'CreatedBy'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'變更時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_LEVEL_WORDING', @level2type=N'COLUMN',@level2name=N'UpdatedAt'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'變更者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_LEVEL_WORDING', @level2type=N'COLUMN',@level2name=N'UpdatedBy'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流程ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_LEVEL_WORDING', @level2type=N'COLUMN',@level2name=N'DetailId'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流程順序' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_LEVEL_WORDING', @level2type=N'COLUMN',@level2name=N'ProcessOrder'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'APPLY/REVIEW' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_LEVEL_WORDING', @level2type=N'COLUMN',@level2name=N'Type'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'關卡顯示文字' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_LEVEL_WORDING', @level2type=N'COLUMN',@level2name=N'Wording'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文字對應關卡編號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_PROCESS_LEVEL_WORDING', @level2type=N'COLUMN',@level2name=N'WordingLevel'
GO



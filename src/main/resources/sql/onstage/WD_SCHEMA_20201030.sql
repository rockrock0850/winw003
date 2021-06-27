-- 0005027: 【UAT】CR_41 新增「批次作業中斷對策表管理」功能
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[FORM_INFO_BA_DETAIL](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[FormId] [nvarchar](20) NOT NULL,
	[BatchName] [nvarchar](200) NULL,
	[Summary] [nvarchar](200) NULL,
	[Division] [nvarchar](200) NULL,
	[ExecuteTime] [nvarchar](50) NULL,
	[DbInUse] [nvarchar](200) NULL,
	[EffectDate] [datetime] NULL,
	[UpdatedBy] [nvarchar](50) NULL,
	[UpdatedAt] [datetime] NULL,
	[CreatedBy] [nvarchar](50) NULL,
	[CreatedAt] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [FORM_INFO_BA_DETAIL_FormId] UNIQUE NONCLUSTERED 
(
	[FormId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
EXEC ISWP_WINW.sys.sp_addextendedproperty 'MS_Description', '流水號', 'user', 'dbo', 'table', 'FORM_INFO_BA_DETAIL', 'column', 'Id' 
GO
EXEC ISWP_WINW.sys.sp_addextendedproperty 'MS_Description', '表單編號', 'user', 'dbo', 'table', 'FORM_INFO_BA_DETAIL', 'column', 'FormId' 
GO
EXEC ISWP_WINW.sys.sp_addextendedproperty 'MS_Description', '建立時間', 'user', 'dbo', 'table', 'FORM_INFO_BA_DETAIL', 'column', 'CreatedAt' 
GO
EXEC ISWP_WINW.sys.sp_addextendedproperty 'MS_Description', '建立者', 'user', 'dbo', 'table', 'FORM_INFO_BA_DETAIL', 'column', 'CreatedBy' 
GO
EXEC ISWP_WINW.sys.sp_addextendedproperty 'MS_Description', '變更時間', 'user', 'dbo', 'table', 'FORM_INFO_BA_DETAIL', 'column', 'UpdatedAt' 
GO
EXEC ISWP_WINW.sys.sp_addextendedproperty 'MS_Description', '變更者', 'user', 'dbo', 'table', 'FORM_INFO_BA_DETAIL', 'column', 'UpdatedBy' 
GO
EXEC ISWP_WINW.sys.sp_addextendedproperty 'MS_Description', '負責科', 'user', 'dbo', 'table', 'FORM_INFO_BA_DETAIL', 'column', 'Division' 
GO
EXEC ISWP_WINW.sys.sp_addextendedproperty 'MS_Description', '批次工作名稱', 'user', 'dbo', 'table', 'FORM_INFO_BA_DETAIL', 'column', 'BatchName' 
GO
EXEC ISWP_WINW.sys.sp_addextendedproperty 'MS_Description', '作業名稱描述', 'user', 'dbo', 'table', 'FORM_INFO_BA_DETAIL', 'column', 'Summary' 
GO
EXEC ISWP_WINW.sys.sp_addextendedproperty 'MS_Description', '執行時間', 'user', 'dbo', 'table', 'FORM_INFO_BA_DETAIL', 'column', 'ExecuteTime' 
GO
EXEC ISWP_WINW.sys.sp_addextendedproperty 'MS_Description', '使用資料庫', 'user', 'dbo', 'table', 'FORM_INFO_BA_DETAIL', 'column', 'DbInUse' 
GO
EXEC ISWP_WINW.sys.sp_addextendedproperty 'MS_Description', '生效日期', 'user', 'dbo', 'table', 'FORM_INFO_BA_DETAIL', 'column', 'EffectDate' 
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING OFF
GO
CREATE TABLE [dbo].[FORM_PROCESS_DETAIL_APPLY_BA](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[DetailId] [nvarchar](20) NOT NULL,
	[ProcessId] [nvarchar](20) NOT NULL,
	[ProcessOrder] [int] NOT NULL,
	[GroupId] [nvarchar](100) NULL,
	[NextLevel] [int] NULL,
	[BackLevel] [int] NULL,
	[IsCreateChangeIssue] [char](1) NULL,
	[IsCreateCIssue] [char](1) NULL,
	[IsWaitForSubIssueFinish] [char](1) NULL,
	[IsCheckLevel] [char](1) NULL,
	[IsModifyColumnData] [char](1) NULL,
	[CreatedAt] [datetime] NULL,
	[CreatedBy] [nvarchar](50) NULL,
	[UpdatedAt] [datetime] NULL,
	[UpdatedBy] [nvarchar](50) NULL
) ON [PRIMARY]
SET ANSI_PADDING ON
ALTER TABLE [dbo].[FORM_PROCESS_DETAIL_APPLY_BA] ADD [IsApprover] [char](1) NULL
ALTER TABLE [dbo].[FORM_PROCESS_DETAIL_APPLY_BA] ADD [Parallels] [nvarchar](200) NULL
ALTER TABLE [dbo].[FORM_PROCESS_DETAIL_APPLY_BA] ADD [IsParallel] [char](1) NULL DEFAULT ('N')
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[FORM_PROCESS_DETAIL_REVIEW_BA](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[DetailId] [nvarchar](20) NOT NULL,
	[ProcessId] [nvarchar](30) NOT NULL,
	[ProcessOrder] [int] NOT NULL,
	[GroupId] [nvarchar](100) NULL,
	[NextLevel] [int] NULL,
	[BackLevel] [int] NULL,
	[IsModifyColumnData] [char](1) NULL,
	[CreatedAt] [datetime] NULL,
	[CreatedBy] [nvarchar](50) NULL,
	[UpdatedAt] [datetime] NULL,
	[UpdatedBy] [nvarchar](50) NULL,
	[IsCloseForm] [char](1) NULL,
	[IsApprover] [char](1) NULL,
	[IsWaitForSubIssueFinish] [char](1) NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO

-- 0006050: 【PRO】CR_4 稽催通知(郵件功能、系統調整與DB異動)
/****** Object:  Table [dbo].[AUDIT_NOTIFY_PARAMS]    Script Date: 2020/9/1 下午 04:27:44 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[AUDIT_NOTIFY_PARAMS](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[UpdatedBy] [nvarchar](50) NULL,
	[UpdatedAt] [datetime] NULL,
	[CreatedBy] [nvarchar](50) NULL,
	[CreatedAt] [datetime] NULL,
	[FormType] [nvarchar](10) NOT NULL,
	[NotifyType] [nvarchar](20) NOT NULL,
	[Time] [nvarchar](2) NULL,
	[IsSc] [char](1) NOT NULL,
	[IsD1] [char](1) NOT NULL,
	[IsD2] [char](1) NOT NULL,
	[IsPic] [char](1) NOT NULL,
	[IsVsc] [char](1) NOT NULL,
	[NotifyMails] [nvarchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [AUDIT_NOTIFY_PARAMS_FormType] UNIQUE NONCLUSTERED 
(
	[FormType],[NotifyType] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[AUDIT_NOTIFY_PARAMS] ADD  DEFAULT ('Y') FOR [IsSc]
GO

ALTER TABLE [dbo].[AUDIT_NOTIFY_PARAMS] ADD  DEFAULT ('Y') FOR [IsD1]
GO

ALTER TABLE [dbo].[AUDIT_NOTIFY_PARAMS] ADD  DEFAULT ('Y') FOR [IsD2]
GO

ALTER TABLE [dbo].[AUDIT_NOTIFY_PARAMS] ADD  DEFAULT ('Y') FOR [IsPic]
GO

ALTER TABLE [dbo].[AUDIT_NOTIFY_PARAMS] ADD  DEFAULT ('Y') FOR [IsVsc]
GO

ALTER TABLE [dbo].[AUDIT_NOTIFY_PARAMS]  WITH CHECK ADD CHECK  (([IsPic]='N' OR [IsPic]='Y'))
GO

ALTER TABLE [dbo].[AUDIT_NOTIFY_PARAMS]  WITH CHECK ADD CHECK  (([IsVsc]='N' OR [IsVsc]='Y'))
GO

ALTER TABLE [dbo].[AUDIT_NOTIFY_PARAMS]  WITH CHECK ADD CHECK  (([IsD1]='N' OR [IsD1]='Y'))
GO

ALTER TABLE [dbo].[AUDIT_NOTIFY_PARAMS]  WITH CHECK ADD CHECK  (([IsD2]='N' OR [IsD2]='Y'))
GO

ALTER TABLE [dbo].[AUDIT_NOTIFY_PARAMS]  WITH CHECK ADD CHECK  (([IsSc]='N' OR [IsSc]='Y'))
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'Id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'變更者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'UpdatedBy'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'變更時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'UpdatedAt'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'CreatedBy'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'CreatedAt'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'表單類型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'FormType'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'通知類型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'NotifyType'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'即將逾期極限值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'Time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否寄信給科長' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'IsSc'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否寄信給副理' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'IsD1'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否寄信給協理' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'IsD2'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否寄信給經辦' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'IsPic'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否寄信給副科' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'IsVsc'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'負責人信件清單' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS', @level2type=N'COLUMN',@level2name=N'NotifyMails'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'稽催通知參數設定' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'AUDIT_NOTIFY_PARAMS'
GO
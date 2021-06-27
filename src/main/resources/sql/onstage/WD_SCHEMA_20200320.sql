-- 0005028: 【UAT】CR_42 行事曆設定介面開發 + 事件單「目標解決時間」規則
/****** Object:  Table [dbo].[SYS_HOLIDAY]    Script Date: 2020/6/23 下午 02:40:35 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[SYS_HOLIDAY](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[UpdatedBy] [nvarchar](50) NULL,
	[UpdatedAt] [datetime] NULL,
	[CreatedBy] [nvarchar](50) NULL,
	[CreatedAt] [datetime] NULL,
	[Year] [nvarchar](10) NOT NULL,
	[Date] [datetime] NULL,
	[Name] [nvarchar](1000) NULL,
	[IsHoliday] [char](1) NULL DEFAULT ('Y'),
	[HolidayCategory] [nvarchar](2000) NULL,
	[Description] [nvarchar](max) NULL,
	[FileName] [nvarchar](500) NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[SYS_HOLIDAY]  WITH CHECK ADD CHECK  (([IsHoliday]='N' OR [IsHoliday]='Y'))
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'系統假日/補班日紀錄表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_HOLIDAY'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_HOLIDAY', @level2type=N'COLUMN',@level2name=N'Id'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'年份' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_HOLIDAY', @level2type=N'COLUMN',@level2name=N'Year'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_HOLIDAY', @level2type=N'COLUMN',@level2name=N'Date'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'節日名稱' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_HOLIDAY', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否為放假日或補上班日' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_HOLIDAY', @level2type=N'COLUMN',@level2name=N'IsHoliday'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'放假日種類' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_HOLIDAY', @level2type=N'COLUMN',@level2name=N'HolidayCategory'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'放假日備註' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_HOLIDAY', @level2type=N'COLUMN',@level2name=N'Description'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'檔案名稱' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_HOLIDAY', @level2type=N'COLUMN',@level2name=N'FileName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'更新人員' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_HOLIDAY', @level2type=N'COLUMN',@level2name=N'UpdatedBy'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'更新時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_HOLIDAY', @level2type=N'COLUMN',@level2name=N'UpdatedAt'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_HOLIDAY', @level2type=N'COLUMN',@level2name=N'CreatedBy'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_HOLIDAY', @level2type=N'COLUMN',@level2name=N'CreatedAt'
GO

-- 0005301: 【PRO】mail：程式邏輯調整+新增log紀錄
/****** Object:  Table [dbo].[SYS_MAIL_LOG]    Script Date: 2020/6/16 下午 04:10:43 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[SYS_MAIL_LOG](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[Recognize] [nvarchar](100) NOT NULL,
	[Addresses] [nvarchar](max) NULL,
	[UpdatedBy] [nvarchar](50) NULL,
	[UpdatedAt] [datetime] NULL,
	[CreatedBy] [nvarchar](50) NULL,
	[CreatedAt] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_MAIL_LOG', @level2type=N'COLUMN',@level2name=N'Id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'辨識符號(可以是任意文字, 主要用於辨識這是哪個功能或是哪個表單發出來的信)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_MAIL_LOG', @level2type=N'COLUMN',@level2name=N'Recognize'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'寄出去的郵件地址清單' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_MAIL_LOG', @level2type=N'COLUMN',@level2name=N'Addresses'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'更新人員' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_MAIL_LOG', @level2type=N'COLUMN',@level2name=N'UpdatedBy'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'更新時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_MAIL_LOG', @level2type=N'COLUMN',@level2name=N'UpdatedAt'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_MAIL_LOG', @level2type=N'COLUMN',@level2name=N'CreatedBy'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_MAIL_LOG', @level2type=N'COLUMN',@level2name=N'CreatedAt'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'系統信件發送紀錄' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_MAIL_LOG'
GO

-- 0005260: 【UAT】Issue_AP工作單：頁籤_DB變更，加入欄位規則
/****** Object:  Table [dbo].[FORM_JOB_SEGMENT_LIST]    Script Date: 2020/6/5 下午 01:16:20 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[FORM_JOB_SEGMENT_LIST](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[FormId] [nvarchar](20) NOT NULL,
	[Type] [nvarchar](10) NOT NULL,
	[Segment] [nvarchar](500) NULL,
	[KeyValue] [nvarchar](1000) NULL,
	[AlterColumns] [nvarchar](1000) NULL,
	[AlterContent] [nvarchar](1000) NULL,
	[LayoutDataset] [nvarchar](1000) NULL,
	[UpdatedBy] [nvarchar](50) NULL,
	[UpdatedAt] [datetime] NULL,
	[CreatedBy] [nvarchar](50) NULL,
	[CreatedAt] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'頁簽-DB變更、OPEN清單、程式清單裡面的資料庫變更清單(若少欄位再往下新增)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_JOB_SEGMENT_LIST'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_JOB_SEGMENT_LIST', @level2type=N'COLUMN',@level2name=N'Id'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'表單編號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_JOB_SEGMENT_LIST', @level2type=N'COLUMN',@level2name=N'FormId'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Segment名稱' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_JOB_SEGMENT_LIST', @level2type=N'COLUMN',@level2name=N'Segment'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Key Value' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_JOB_SEGMENT_LIST', @level2type=N'COLUMN',@level2name=N'KeyValue'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'變更的欄位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_JOB_SEGMENT_LIST', @level2type=N'COLUMN',@level2name=N'AlterColumns'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'變更內容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_JOB_SEGMENT_LIST', @level2type=N'COLUMN',@level2name=N'AlterContent'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Layout Dataset' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_JOB_SEGMENT_LIST', @level2type=N'COLUMN',@level2name=N'LayoutDataset'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'更新人員' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_JOB_SEGMENT_LIST', @level2type=N'COLUMN',@level2name=N'UpdatedBy'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'更新時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_JOB_SEGMENT_LIST', @level2type=N'COLUMN',@level2name=N'UpdatedAt'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_JOB_SEGMENT_LIST', @level2type=N'COLUMN',@level2name=N'CreatedBy'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'FORM_JOB_SEGMENT_LIST', @level2type=N'COLUMN',@level2name=N'CreatedAt'
GO

-- 0005020: 【UAT】CR_27 副理審核科別設定
/****** Object:  Table [dbo].[DASHBOARD_DIRECT_AUTH]    Script Date: 2020/4/9 上午 11:16:26 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[DASHBOARD_DIRECT_AUTH](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[UserId] [nvarchar](50) NOT NULL,
	[Division] [nvarchar](250) NOT NULL,
	[UpdatedBy] [nvarchar](50) NULL,
	[UpdatedAt] [datetime] NULL,
	[CreatedBy] [nvarchar](50) NULL,
	[CreatedAt] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [DASHBOARD_DIRECT_AUTH_UserId] UNIQUE NONCLUSTERED 
(
	[UserId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DASHBOARD_DIRECT_AUTH', @level2type=N'COLUMN',@level2name=N'Id'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'使用者編號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DASHBOARD_DIRECT_AUTH', @level2type=N'COLUMN',@level2name=N'UserId'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'科別' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DASHBOARD_DIRECT_AUTH', @level2type=N'COLUMN',@level2name=N'Division'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'變更者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DASHBOARD_DIRECT_AUTH', @level2type=N'COLUMN',@level2name=N'UpdatedBy'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'變更時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DASHBOARD_DIRECT_AUTH', @level2type=N'COLUMN',@level2name=N'UpdatedAt'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DASHBOARD_DIRECT_AUTH', @level2type=N'COLUMN',@level2name=N'CreatedBy'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建立時間' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DASHBOARD_DIRECT_AUTH', @level2type=N'COLUMN',@level2name=N'CreatedAt'
GO

/****** Object:  Table [dbo].[SYS_OPTION_ROLE]    Script Date: 2020/3/5 下午 03:45:22 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[SYS_OPTION_ROLE](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[RoleId] [nvarchar](100) NOT NULL UNIQUE,
	[OptionId] [nvarchar](100) NOT NULL,
	[Value] [nvarchar](100) NOT NULL,
	[Description] [text] NOT NULL,
	[Condition] [nvarchar](100) NOT NULL,
	[UpdatedBy] [nvarchar](50) NULL,
	[UpdatedAt] [datetime] NULL,
	[CreatedBy] [nvarchar](50) NULL,
	[CreatedAt] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'資料編號' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_OPTION_ROLE', @level2type=N'COLUMN',@level2name=N'RoleId'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'SYS_OPTION對應OptionId' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_OPTION_ROLE', @level2type=N'COLUMN',@level2name=N'OptionId'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'SYS_OPTION對應Value' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_OPTION_ROLE', @level2type=N'COLUMN',@level2name=N'Value'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Role描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_OPTION_ROLE', @level2type=N'COLUMN',@level2name=N'Description'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Role條件,附加參數等等資料' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_OPTION_ROLE', @level2type=N'COLUMN',@level2name=N'Condition'
GO



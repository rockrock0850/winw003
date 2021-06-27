with _form as(
	select 
		FormId,
		FormClass,
		FormStatus,
		UserCreated,
		CreatedAt,
		IIF(charindex('_',FormClass) = 0,FormClass,SUBSTRING( FormClass, 1, CHARINDEX('_',
		FormClass) - 1)) AS sourceForm,
		IIF(charindex('_',FormClass) = 0,'', SUBSTRING( FormClass, CHARINDEX('_',
		FormClass) + 1, LEN(FormClass) - CHARINDEX('_', FormClass) )) AS isCForm		 
	from 
		FORM 
	where
		FormStatus='PROPOSING'
)
select
	result.FormId,
	result.FormClass,
	result.FormStatus,
	result.CreatedAt,
	result.UserCreated,
	REPLACE(REPLACE(CAST(result.Summary as VARCHAR(8000)),CHAR(13),''), CHAR(10), '') Summary	 
from(
	select
		sr.FormId,
		sr.FormClass,
		sr.FormStatus,
		sr.CreatedAt,
		sr.UserCreated,
		srd.Summary 
	from
		(select
			FormId,
			FormClass,
			FormStatus,
			UserCreated,
			CreatedAt 
		from 
			_form
		where
			FormClass='SR') sr
		left join
		(select
			FormId,
			Summary
		from
			FORM_INFO_SR_DETAIL) srd
		on sr.FormId = srd.FormId
	union all
	select
		q.FormId,
		q.FormClass,
		q.FormStatus,
		q.CreatedAt,
		q.UserCreated,
		qd.Summary 
	from
		(select
			FormId,
			FormClass,
			FormStatus,
			UserCreated,
			CreatedAt 
		from 
			_form
		where
			FormClass='Q') q
		left join(
		select
			FormId,
			Summary
		from
			FORM_INFO_Q_DETAIL) qd
		on q.FormId = qd.FormId
	union all
	select
		inc.FormId,
		inc.FormClass,
		inc.FormStatus,
		inc.CreatedAt,
		inc.UserCreated,
		incd.Summary 
	from
		(select
			FormId,
			FormClass,
			FormStatus,
			UserCreated,
			CreatedAt 
		from 
			_form
		where
			FormClass='INC') inc
		left join(
		select
			FormId,
			Summary
		from
			FORM_INFO_INC_DETAIL) incd
		on inc.FormId = incd.FormId
	union all
	select
		chg.FormId,
		chg.FormClass,
		chg.FormStatus,
		chg.CreatedAt,
		chg.UserCreated,
		chgd.Summary 
	from
		(select
			FormId,
			FormClass,
			FormStatus,
			UserCreated,
			CreatedAt 
		from 
			_form
		where
			FormClass='CHG') chg
		left join(
		select
			FormId,
			Summary
		from
			FORM_INFO_CHG_DETAIL) chgd
		on chg.FormId = chgd.FormId
	union all
	select
		c.FormId,
		c.FormClass,
		c.FormStatus,
		c.CreatedAt,
		c.UserCreated,
		cd.Summary 
	from
		(select
			f.FormId,
			f.FormClass,
			f.FormStatus,
			f.UserCreated,
			f.CreatedAt 
		from
		_form f
		where
			f.isCForm='C'
			and
			not exists(
				select
				1
				from 
				_form
				where
				f.FormId = FormId
				and
				sourceForm = 'JOB'
			)) c
		left join(
		select
			FormId,
			Summary
		from
			FORM_INFO_C_DETAIL) cd
		on c.FormId = cd.FormId
	union all
	select
		chg.FormId,
		chg.FormClass,
		chg.FormStatus,
		chg.CreatedAt,
		chg.UserCreated,
		chgd.Summary 
	from
		(select
			FormId,
			FormClass,
			FormStatus,
			UserCreated,
			CreatedAt 
		from 
			_form
		where
			FormClass='JOB_AP') chg
		left join(
		select
			FormId,
			Purpose as Summary
		from
			FORM_JOB_INFO_AP_DETAIL) chgd
		on chg.FormId = chgd.FormId
	union all
	select
		chg.FormId,
		chg.FormClass,
		chg.FormStatus,
		chg.CreatedAt,
		chg.UserCreated,
		chgd.Summary 
	from
		(select
			FormId,
			FormClass,
			FormStatus,
			UserCreated,
			CreatedAt 
		from 
			_form
		where
			FormClass='JOB_SP') chg
		left join(
		select
			FormId,
			Summary
		from
			FORM_JOB_INFO_SYS_DETAIL) chgd
		on chg.FormId = chgd.FormId
	union all
	select
		chg.FormId,
		chg.FormClass,
		chg.FormStatus,
		chg.CreatedAt,
		chg.UserCreated,
		chgd.Summary 
	from
		(select
			FormId,
			FormClass,
			FormStatus,
			UserCreated,
			CreatedAt 
		from 
			_form
		where
			FormClass='JOB_AP_C') chg
		left join(
		select
			FormId,
			[Description] as Summary
		from
			FORM_JOB_COUNTERSIGNED) chgd
		on chg.FormId = chgd.FormId
	union all
	select
		chg.FormId,
		chg.FormClass,
		chg.FormStatus,
		chg.CreatedAt,
		chg.UserCreated,
		chgd.Summary 
	from
		(select
			FormId,
			FormClass,
			FormStatus,
			UserCreated,
			CreatedAt 
		from 
			_form
		where
			FormClass='JOB_SP_C') chg
		left join(
		select
			FormId,
			[Description] as Summary
		from
			FORM_JOB_COUNTERSIGNED) chgd
		on chg.FormId = chgd.FormId
	union all
	select
		chg.FormId,
		chg.FormClass,
		chg.FormStatus,
		chg.CreatedAt,
		chg.UserCreated,
		chgd.Summary 
	from
		(select
			FormId,
			FormClass,
			FormStatus,
			UserCreated,
			CreatedAt 
		from 
			_form
		where
			FormClass='BA') chg
		left join(
		select
			FormId,
			[Summary] as Summary
		from
			FORM_INFO_BA_DETAIL) chgd
		on chg.FormId = chgd.FormId
) result 
where
	1 = 1
	${CONDITIONS}

With _f AS(
SELECT
  f.FormId,
  f.UserCreated,
  f.FormStatus,
  IIF(
      CHARINDEX('-', f.DivisionCreated) = 0,
      '',
      SUBSTRING(
        f.DivisionCreated,
        1,
        CHARINDEX('-', f.DivisionCreated) - 1
      )
  ) division,	
  IIF(CHARINDEX('-', f.DivisionCreated) = 0,
  '', SUBSTRING(f.DivisionCreated, CHARINDEX('-', f.DivisionCreated) + 1, LEN(f.DivisionCreated) - CHARINDEX('-', f.DivisionCreated))) section
	FROM FORM f
	WHERE
	f.formClass = 'Q'  
     AND NOT EXISTS (
      SELECT
        1
      FROM
        form
      WHERE
        FormId = f.FormId
        AND 
		FormStatus = 'PROPOSING'
    )
),
_f2 AS(
  SELECT
    *
  FROM
    _f f
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        _f
      WHERE
        formid = f.formid
        AND 
		f.FormStatus = 'DEPRECATED'
    )
),
_form AS(
  SELECT
    *
  FROM
    _f2 f
  WHERE
	1=1
 AND  NOT EXISTS ( SELECT 1 FROM _f2 WHERE f.FormId = FormId AND division = 'A01421' ) 
  ${CONDITIONS}

)
SELECT
  fid.FormId,
  convert(varchar, fid.CreateTime, 111) CreateTime,
  convert(varchar, fid.ect, 111) ECT,
  convert(varchar, fid.act, 111) ACT,
  fid.SpecialEndCaseType,
  fm.UserCreated,
  fm.section,
  REPLACE(REPLACE(CAST(fiqd.Summary as VARCHAR(8000)),CHAR(13),''), CHAR(10), '') Summary,
  REPLACE(REPLACE(CAST(fiqd.Content as VARCHAR(8000)),CHAR(13),''), CHAR(10), '') Content,
  fiqd.EffectScope,
  fiqd.UrgentLevel,
  fiqd.QuestionPriority,
  fiqd.Countersigneds,
  fp.Indication,
  fp.Reason,
  fp.ProcessProgram,
  fp.IsSuggestCase,
  u.Name
FROM (SELECT
  FormId,
  CreateTime,
  ect,
  act,
  SpecialEndCaseType
FROM FORM_INFO_DATE
WHERE formClass = 'Q'
AND IsSpecial = 'Y'
AND SpecialEndCaseType = :universal
) fid
INNER JOIN _form fm
  ON (fid.FormId = fm.FormId)
LEFT JOIN (SELECT
  FormId,
  Summary,
  Content,
  Countersigneds,
  IIF(CHARINDEX('_', f.EffectScope) = 0,
  '', SUBSTRING(f.EffectScope, CHARINDEX('_', f.EffectScope) + 1, LEN(f.EffectScope) - CHARINDEX('_', f.EffectScope))) EffectScope,
  IIF(CHARINDEX('_', f.UrgentLevel) = 0,
  '', SUBSTRING(REVERSE(f.UrgentLevel), 1, CHARINDEX('_', REVERSE(f.UrgentLevel)) - 1)) UrgentLevel,
  QuestionPriority
FROM FORM_INFO_Q_DETAIL f) fiqd
  ON (fid.formId = fiqd.formId)
LEFT JOIN (SELECT
  FormId,
  Indication,
  Reason,
  ProcessProgram,
  IIF(IsSuggestCase is null,'N',IsSuggestCase) IsSuggestCase
FROM FORM_PROGRAM) fp
  ON (fid.FormId = fp.FormId)
LEFT JOIN (SELECT
  UserId,
  Name,
  ROW_NUMBER() OVER (PARTITION BY UserId ORDER BY UpdatedAt DESC) AS newest
FROM LDAP_USER) u
  ON (fm.UserCreated = u.UserId)
  
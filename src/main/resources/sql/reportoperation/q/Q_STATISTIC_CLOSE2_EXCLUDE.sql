WITH _f AS (
  SELECT
    f.formid,
    f.FormStatus,
    CONVERT(varchar,f.CreateTime, 111)as CreateTime,
    f.UpdatedAt,
    f.sourceId,
    f.UserSolving,
    f.DetailId,
    IIF(
      CHARINDEX('-', DivisionSolving) = 0,
      '',
      SUBSTRING(
        DivisionCreated,
        1,
        CHARINDEX('-', DivisionSolving) - 1
      )
    ) division,
    IIF(
      CHARINDEX('-', f.DivisionSolving) = 0,
      '',
      SUBSTRING(
        f.DivisionCreated,
        CHARINDEX('-', f.DivisionSolving) + 1,
        LEN(f.DivisionSolving) - CHARINDEX('-', f.DivisionSolving)
      )
    ) section
  FROM
    form f
  WHERE
    f.Formclass = 'Q'
    AND NOT EXISTS (
      SELECT
        1
      FROM
        form
      WHERE
        FormId = f.FormId
        AND f.FormStatus = 'PROPOSING'
    )
),
_f2 AS (
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
        AND f.FormStatus = 'DEPRECATED'
    )
),
_form AS (
  SELECT
    f.*,
    IIF(
      fid.ect IS NULL
      OR fid.ect = '',
      'N',
      IIF(
        CONVERT(varchar, fid.ect, 111) < CONVERT(varchar, fid.act, 111),
        'Y',
        'N'
      )
    ) AS delay,
    IIF(
      fid.ect IS NULL
      OR fid.ect = '',
      NULL,
      CONVERT(varchar, fid.ect, 111)
    ) AS ect,
    IIF(
      fid.act IS NULL
      OR fid.act = '',
      NULL,
      CONVERT(varchar, fid.act, 111)
    ) AS act,
    fid.IsSpecial,
    fid.SpecialEndCaseType
  FROM
    _f2 f
    INNER JOIN (
      SELECT
        FormId,
        ect,
        act,
        IsSpecial,
        SpecialEndCaseType
      FROM
        FORM_INFO_DATE
      WHERE
        formClass = 'Q'
        AND ect IS NOT NULL
    ) fid ON f.FormId = fid.FormId
  WHERE
    1 = 1
    AND CONVERT(varchar, fid.ect, 111) BETWEEN CONVERT(varchar, cast(:startDate as datetime), 111)
    AND CONVERT(varchar, cast(:endDate as datetime), 111)
 AND  NOT EXISTS ( SELECT 1 FROM _f2 WHERE f.FormId = FormId AND division = 'A01421' ) 
	${CONDITIONS}
),
_unfinished AS (
  SELECT
    *
  FROM
    _form f
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        _form
      WHERE
        f.Formid = FormId
        AND FormStatus = 'CLOSED'
    )
  UNION ALL
  SELECT
    *
  FROM
    _form
  WHERE
    FormStatus = 'CLOSED'
    AND delay = 'Y'
),
_Q_C AS (
  SELECT
    f.FormId,
    f.SourceId,
    f.UserSolving,
    f.FormStatus,
    IIF(
      CHARINDEX('-', f.DivisionSolving) = 0,
      '',
      SUBSTRING(
        f.DivisionCreated,
        CHARINDEX('-', f.DivisionSolving) + 1,
        LEN(f.DivisionSolving) - CHARINDEX('-', f.DivisionSolving)
      )
    ) section
  FROM
    form f
  WHERE
    f.Formclass = 'Q_C'
    AND NOT EXISTS (
      SELECT
        1
      FROM
        form
      WHERE
        FormId = f.FormId
        AND f.FormStatus = 'PROPOSING'
    )
),
_resultQC AS (
  SELECT
    *
  FROM
    _Q_C f
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        _Q_C
      WHERE
        formid = f.formid
        AND f.FormStatus = 'DEPRECATED'
    )
),
_cform AS (
  SELECT
    fu.SourceId,
    fu.formid + '/' + fu.section + '/' + name AS cform
  FROM
    (
      SELECT
        _form.formid,
        _form.SourceId,
        _form.UserSolving,
        _form.section,
        IIF(_u.name IS NULL, '', _u.name) AS name
      FROM
        _resultQC _form
        LEFT JOIN (
          SELECT
            lu.UserId,
            lu.name
          FROM
            (
              SELECT
                UserId,
                Name,
                ROW_NUMBER() OVER (
                  PARTITION BY UserId
                  ORDER BY
                    UpdatedAt DESC
                ) AS newest
              FROM
                LDAP_USER
            ) lu
          WHERE
            newest = 1
        ) _u ON (_form.UserSolving = _u.UserId)
    ) fu
),
_merge AS (
  SELECT
    SourceId,
    (
      STUFF (
        (
          SELECT
            ',' + cform
          FROM
            _cform c2
          WHERE
            c2.SourceId = c1.SourceId FOR xml PATH ('')
        ),
        1,
        1,
        ''
      )
    ) AS newField
  FROM
    _cform c1
  GROUP BY
    SourceId
)
SELECT
  _un.FormId,
  _un.section,
  _cname.name,
  _un.IsSpecial,
  _sys.GroupName,
  _un.FormStatus,
  _un.CreateTime,
  _un.ECT,
  _un.ACT,
  IIF(_c.newField IS NULL,'',_c.newField) AS newField,
  _q.Summary
FROM
  _unfinished _un
  LEFT JOIN (
    SELECT
      *
    FROM
      (
        SELECT
          formId,
          VerifyLevel,
          VerifyType,
          ROW_NUMBER() OVER (
            PARTITION BY formId
            ORDER BY
              UpdatedAt DESC
          ) AS newest
        FROM
          FORM_VERIFY_LOG
      ) _l
    WHERE
      newest = 1
  ) _log ON _un.formId = _log.formId
  LEFT JOIN (
    SELECT
      'APPLY' AS type,
      DetailId,
      CONVERT(varchar(10), ProcessOrder) AS ProcessOrder,
      GroupId
    FROM
      FORM_PROCESS_DETAIL_APPLY_Q
    UNION ALL
    SELECT
      'REVIEW' AS type,
      DetailId,
      CONVERT(varchar(10), ProcessOrder) AS ProcessOrder,
      GroupId
    FROM
      FORM_PROCESS_DETAIL_REVIEW_Q
  ) _detail ON (
    _un.DetailId = _detail.DetailId
    AND _detail.type = _log.VerifyType
    AND _detail.ProcessOrder = _log.VerifyLevel
  )
  LEFT JOIN (
    SELECT
      lu.UserId,
      lu.name
    FROM
      (
        SELECT
          UserId,
          Name,
          ROW_NUMBER() OVER (
            PARTITION BY UserId
            ORDER BY
              UpdatedAt DESC
          ) AS newest
        FROM
          LDAP_USER
      ) lu
    WHERE
      newest = 1
  ) _cname ON _cname.UserId = _un.UserSolving
  LEFT JOIN (
    SELECT
      g.GroupId,
      g.GroupName
    FROM
      (
        SELECT
          GroupId,
          GroupName,
          ROW_NUMBER() OVER (
            PARTITION BY GroupId
            ORDER BY
              UpdatedAt DESC
          ) AS newest
        FROM
          SYS_GROUP
      ) g
    WHERE
      newest = 1
  ) _sys ON _sys.GroupId = _detail.GroupId
  LEFT JOIN _merge _c ON _c.SourceId = _un.FormId
  LEFT JOIN (
    SELECT
      FormId,
      REPLACE(
        REPLACE(CAST(Summary AS varchar(8000)), CHAR(13), ''),
        CHAR(10),
        ''
      ) Summary
    FROM
      FORM_INFO_Q_DETAIL
  ) _q ON _q.FormId = _un.FormId

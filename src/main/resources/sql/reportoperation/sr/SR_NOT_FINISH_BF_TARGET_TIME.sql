WITH GET_FORM_SR_FILTER_FORMSTATUS_DEPRECATED AS (
  SELECT
    FormId,
    DetailId,
    FormStatus,
    UserCreated
  FROM
    form f
  WHERE
    FormClass = 'SR'
    AND NOT EXISTS (
      SELECT
        1
      FROM
        form dep
      WHERE
        dep.FormId = f.FormId
        AND dep.FormStatus = 'DEPRECATED'
    )
),
_FORM AS (
  SELECT
    *
  FROM
    GET_FORM_SR_FILTER_FORMSTATUS_DEPRECATED f
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        GET_FORM_SR_FILTER_FORMSTATUS_DEPRECATED dep
      WHERE
        dep.FormId = f.FormId
        AND dep.FormStatus = 'PROPOSING'
    )
),
GET_INFO_DATA_SR_FILTER_ECT_NULL AS (
  SELECT
    FormId,
    ECT,
    ACT
  FROM
    FORM_INFO_DATE
  WHERE
    FormClass = 'SR'
    AND ECT IS NOT NULL
),
_FORM_INFO_DATE AS (
  SELECT
    *
  FROM
    GET_INFO_DATA_SR_FILTER_ECT_NULL g
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        GET_INFO_DATA_SR_FILTER_ECT_NULL gi
      WHERE
        gi.FormId = g.FormId
        AND gi.ECT = ''
    )
),
GET_INFO_DATA_ACT_NULL_BLANK AS (
  SELECT
    *
  FROM
    _FORM_INFO_DATE f
  WHERE
    f.ACT IS NULL
  UNION ALL
  SELECT
    *
  FROM
    _FORM_INFO_DATE f
  WHERE
    f.ACT = ''
),
GET_INFO_DATA_ACT_NOT_NULL AS (
  SELECT
    *
  FROM
    _FORM_INFO_DATE f
  WHERE
    NOT EXISTS (
      SELECT
        1
      FROM
        GET_INFO_DATA_ACT_NULL_BLANK g
      WHERE
        f.FormId = g.FormId
    )
),
GET_FORM_PROCESS_DETAIL AS (
  SELECT
    DetailId,
    CONVERT(nvarchar(10), ProcessOrder) AS ProcessOrder,
    GroupId,
    'APPLY' AS VerifyType
  FROM
    FORM_PROCESS_DETAIL_APPLY_SR
  UNION ALL
  SELECT
    DetailId,
    CONVERT(nvarchar(10), ProcessOrder) AS ProcessOrder,
    GroupId,
    'REVIEW' AS VerifyType
  FROM
    FORM_PROCESS_DETAIL_REVIEW_SR
),
_SYS_GROUP AS (
  SELECT
    SysGroupId,
    DepartmentId,
    DepartmentName,
    division,
    GroupId,
    GroupName
  FROM
    SYS_GROUP
)
SELECT
  result.FormId,
  result.DetailId,
  result.FormStatus,
  result.UserCreated,
  result.Summary,
  result.ECT,
  result.ACT,
  result.GroupId,
  CASE result.FormStatus WHEN 'CLOSED' THEN '' ELSE result.GroupName END GroupName,
  result.Name,
  result.DepartmentId,
  result.DepartmentName,
  result.division
FROM
  (
    SELECT
      _f.FormId,
      _f.DetailId,
      _f.FormStatus,
      _f.UserCreated,
      info_date.ECT,
      info_date.ACT,
      _group.GroupId,
      _group.GroupName,
      l_user.Name,
      _FORM_INFO_SR_DETAIL.Summary,
      s_group.DepartmentId,
      s_group.DepartmentName,
      s_group.division,
      s_group.SysGroupId
    FROM
      _FORM _f
      INNER JOIN (
        SELECT
          *
        FROM
          GET_INFO_DATA_ACT_NULL_BLANK
        WHERE
          CONVERT(varchar(10), ect, 120) < CONVERT(varchar(10), GETDATE(), 120)
        UNION ALL
        SELECT
          *
        FROM
          GET_INFO_DATA_ACT_NOT_NULL
        WHERE
          CONVERT(varchar(10), ect, 120) < CONVERT(varchar(10), act, 120)
      ) info_date ON _f.FormId = info_date.FormId
      LEFT JOIN (
        SELECT
          *
        FROM
          (
            SELECT
              FormId,
              VerifyLevel,
              VerifyType,
              ROW_NUMBER() OVER (
                PARTITION BY FormId
                ORDER BY
                  UpdatedAt DESC
              ) newest
            FROM
              FORM_VERIFY_LOG
          ) _log
        WHERE
          _log.newest = 1
      ) fv_log ON _f.FormId = fv_log.FormId
      LEFT JOIN GET_FORM_PROCESS_DETAIL process_detial ON _f.DetailId = process_detial.DetailId
      AND fv_log.VerifyLevel = process_detial.ProcessOrder
      AND fv_log.VerifyType = process_detial.VerifyType
      LEFT JOIN (
        SELECT
          GroupId,
          GroupName
        FROM
          _SYS_GROUP
      ) _group ON process_detial.GroupId = _group.GroupId
      LEFT JOIN (
        SELECT
          u.UserId,
          u.Name,
          u.SysGroupId
        FROM
          (
            SELECT
              UserId,
              Name,
              SysGroupId,
              ROW_NUMBER() OVER (
                PARTITION BY UserId
                ORDER BY
                  UpdatedAt DESC
              ) AS newest
            FROM
              LDAP_USER
          ) u
        WHERE
          u.newest = 1
      ) l_user ON _f.UserCreated = l_user.UserId
      INNER JOIN (
        SELECT
          FORMId,
          Summary
        FROM
          FORM_INFO_SR_DETAIL
      ) _FORM_INFO_SR_DETAIL ON _f.FormId = _FORM_INFO_SR_DETAIL.FormId
      INNER JOIN (
        SELECT
          SysGroupId,
          DepartmentId,
          DepartmentName,
          division
        FROM
          _SYS_GROUP
        WHERE
          division IS NOT NULL
      ) s_group ON l_user.SysGroupId = s_group.SysGroupId
  ) result
WHERE
  CONVERT(varchar,result.ECT, 111) BETWEEN CONVERT(varchar,:startDate, 111)
  AND CONVERT(varchar,:endDate, 111) 
  ${CONDITIONS}

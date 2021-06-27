SELECT
        F.FormId,
        F.SourceId,
        FD.MECT,
        FD.ECT,
        FD.EOT,
        FD.ACT,
        FD.AST,
        FD.ReportTime,
        FD.ExcludeTime,
        FD.Observation,
        FD.CAT,
        FD.CCT
FROM
        FORM F
JOIN
        FORM_INFO_DATE FD
ON
        F.SourceId = FD.FormId
WHERE
    (1=1)
${CONDITIONS} 
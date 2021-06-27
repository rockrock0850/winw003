/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.service.impl.report.chg;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ebizprise.project.utility.str.CommonStringUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseReportOperationService;
import com.ebizprise.winw.project.enums.ReportOperationEnum;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.excel.model.ExcelModel;
import com.ebizprise.winw.project.vo.ReportOperationVO;

/**
 * 變更成功失敗統計表
 * @author adam.yeh
 */
@Service
public class CHGAlterResultServiceImpl extends BaseReportOperationService {

    private static final Logger logger = LoggerFactory.getLogger(CHGAlterResultServiceImpl.class);

    private final String COLUMN_CLASS = "0";
    private final String COLUMN_SUCCESS = "1";
    private final String COLUMN_FAILURE = "2";
    private final String COLUMN_TOTAL = "3";
    private final String COLUMN_RATE = "4";

    @Override
    public String getUrl () {
        setUrl(ReportOperationEnum.CHG_ALTER_RESULT_SERVICE.getPath());
        return pageUrl;
    }

    @Override
    public void setReportOperationVO (ReportOperationVO vo) {
        setNotExists(ReportOperationEnum.CHG_ALTER_RESULT_SERVICE.getValue());
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_CHG.getResource(ReportOperationEnum.CHG_ALTER_RESULT_SERVICE.getSqlFileName()));
        Map<Integer, Map<String ,Object>> dataSet = new HashMap<Integer, Map<String, Object>>();
        List<Map<String, Object>> result = getDataList(vo, getResourceEnum(), getNotExists(), getSQLParams());

        if (result != null) {
            dataSet = getReportDataSet(result);
        }

        vo.setStatisticsResult(dataSet);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setDataSet (Collection<Object> dataSet, ReportOperationVO vo) {
        // 抬頭
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_TITLEM,
                getMessage(ReportOperationEnum.CHG_ALTER_RESULT_SERVICE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_10));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));

        // 查詢條件
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_TITLE,
                getMessage(ReportOperationEnum.SEARCH_CONDITION.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1));
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_CONTENT + StringConstant.PAPAM_1,
                getMessage(ReportOperationEnum.SEARCH_DATE.getI18n()),
                vo.getStartInterval() + StringConstant.TILDE + vo.getEndInterval(),
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_2,
                getMessage(ReportOperationEnum.SEARCH_EXCLUDE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1, 
                vo.getIsExclude(),
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_4));
        dataSet.add(new ExcelModel(StringConstant.PARAM_SKIP_A_LINE));

        // 查詢結果
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_TITLE,
                getMessage(ReportOperationEnum.SEARCH_RESULT.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS + StringConstant.PAPAM_1));
        dataSet.add(new ExcelModel(
                StringConstant.PARAM_HEADERM,
                getMessage(ReportOperationEnum.FORM_TYPE_CODE.getI18n()),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                getMessage("success"),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                getMessage("failure"),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                getMessage("total"),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                getMessage("successRate"),
                StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2));

        setNotExists(ReportOperationEnum.CHG_ALTER_RESULT_SERVICE.getValue());
        setResourceEnum(ResourceEnum.SQL_REPORT_OPERATION_CHG.getResource(ReportOperationEnum.CHG_ALTER_RESULT_SERVICE.getSqlFileName()));
        List<Map<String, Object>> result = getDataList(vo, getResourceEnum(), getNotExists(), getSQLParams());

        if (result != null) {
            Map<Integer, Map<String, Object>> reports = getReportDataSet(result);
            
            for (Integer key : reports.keySet()) {
                Map<String, Object> row = reports.get(key);
                dataSet.add(new ExcelModel(
                        StringConstant.PARAM_BODYM,
                        MapUtils.getString(row, COLUMN_CLASS),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_4,
                        MapUtils.getString(row, COLUMN_SUCCESS),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                        MapUtils.getString(row, COLUMN_FAILURE),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                        MapUtils.getString(row, COLUMN_TOTAL),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2,
                        MapUtils.getString(row, COLUMN_RATE),
                        StringConstant.PARAM_MERGE_COLUMNS+StringConstant.PAPAM_2));
            }
        }
    }

    private Map<Integer, Map<String, Object>> getReportDataSet (List<Map<String, Object>> result) {
        int count = 0;
        boolean isAlterDone = false;
        double successQ = 0, failureQ = 0;
        double successQc = 0, failureQc = 0;
        double successSr = 0, failureSr = 0;
        double successInc = 0, failureInc = 0;
        double successSrc = 0, failureSrc = 0;
        double successIncc = 0, failureIncc = 0;
        
        for (Map<String, Object> map : result) {
            isAlterDone = StringConstant.SHORT_YES.equals(MapUtils.getString(map, "IsAlterDone"));
            
            if (FormEnum.Q.name().equals(MapUtils.getString(map, "FormClass"))) {
                if (isAlterDone) successQ++; else failureQ++; 
            } else if (FormEnum.SR.name().equals(MapUtils.getString(map, "FormClass"))) {
                if (isAlterDone) successSr++; else failureSr++; 
            } else if (FormEnum.INC.name().equals(MapUtils.getString(map, "FormClass"))) {
                if (isAlterDone) successInc++; else failureInc++; 
            } else if (FormEnum.Q_C.name().equals(MapUtils.getString(map, "FormClass"))) {
                if (isAlterDone) successQc++; else failureQc++; 
            } else if (FormEnum.SR_C.name().equals(MapUtils.getString(map, "FormClass"))) {
                if (isAlterDone) successSrc++; else failureSrc++; 
            } else if (FormEnum.INC_C.name().equals(MapUtils.getString(map, "FormClass"))) {
                if (isAlterDone) successIncc++; else failureIncc++; 
            } else {
                logger.error("變更失敗成功報表發生異常, 沒有找到對應的表單類別(FromClass) : " + map);
            }
        }

        Map<Integer, Map<String, Object>> dataSet = new HashMap<Integer, Map<String, Object>>();
        dataSet.put(count++, genRowMap(successQ, failureQ, FormEnum.Q.wording()));
        dataSet.put(count++, genRowMap(successSr, failureSr, FormEnum.SR.wording()));
        dataSet.put(count++, genRowMap(successInc, failureInc, FormEnum.INC.wording()));
        dataSet.put(count++, genRowMap(successQc, failureQc, FormEnum.Q_C.wording()));
        dataSet.put(count++, genRowMap(successSrc, failureSrc, FormEnum.SR_C.wording()));
        dataSet.put(count++, genRowMap(successIncc, failureIncc, FormEnum.INC_C.wording()));
        dataSet.put(count++, getSubTotal(dataSet));
        
        return dataSet;
    }

    private Map<String, Object> getSQLParams () {
        Map<String, Object> params = new HashMap<>();
        params.put("Q", FormEnum.Q.name());
        params.put("SR", FormEnum.SR.name());
        params.put("INC", FormEnum.INC.name());
        params.put("Q_C", FormEnum.Q_C.name());
        params.put("SR_C", FormEnum.SR_C.name());
        params.put("sc", "%" + UserEnum.SC.name());
        params.put("INC_C", FormEnum.INC_C.name());
        params.put("status", FormEnum.CLOSED.name());
        params.put("REVIEW", FormEnum.REVIEW.name());
        params.put("DEPRECATED", FormEnum.DEPRECATED.name());
        params.put("direct", "%" + UserEnum.DIRECT.name() + "%");
        
        return params;
    }

    /**
     * 小計
     * @param dataSet
     * @return
     * @author adam.yeh
     */
    private Map<String, Object> getSubTotal (Map<Integer, Map<String, Object>> dataSet) {
        int total = 0;
        int success = 0;
        int failure = 0;
        
        for (Integer key : dataSet.keySet()) {
            Map<String, Object> row = dataSet.get(key);
            total += MapUtils.getInteger(row, COLUMN_TOTAL, 0);
            success += MapUtils.getInteger(row, COLUMN_SUCCESS, 0);
            failure += MapUtils.getInteger(row, COLUMN_FAILURE, 0);
        }

        Map<String, Object> row = new HashMap<>();
        row.put(COLUMN_CLASS, getMessage("report.operation.chg.count"));
        row.put(COLUMN_SUCCESS, success);
        row.put(COLUMN_FAILURE, failure);
        row.put(COLUMN_TOTAL, total);
        row.put(COLUMN_RATE, getSuccessRate(success, failure));
        
        return row;
    }

    private Map<String, Object> genRowMap (double success, double failure, String clazz) {
        Map<String, Object> row = new HashMap<>();
        row.put(COLUMN_CLASS, getMessage(clazz));
        row.put(COLUMN_SUCCESS, (int) success);
        row.put(COLUMN_FAILURE, (int) failure);
        row.put(COLUMN_TOTAL, (int) (success + failure));
        row.put(COLUMN_RATE, getSuccessRate(success, failure));
        
        return row;
    }

    /**
     * 取得成功率
     * @param success
     * @param failure
     * @return
     * @author adam.yeh
     */
    private String getSuccessRate (double success, double failure) {
        if (success == 0 && failure == 0) {
            return "0.00%";
        }
        
        double rate = success / (success+failure);
        String formated = CommonStringUtil.numberFormat(rate, "#.####");
        double percentage = Double.valueOf(formated) * 100;
        formated = CommonStringUtil.numberFormat(percentage, "0.00");
        
        return formated + "%";
    }

}

package com.ebizprise.winw.project.controller.form.operation.common;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;

/**
 * 引導表單內容 控制類別
 * 
 * @author adam.yeh
 */
@RestController
@RequestMapping("/formDispatcher")
public class FormDispatcherController extends BaseController {
    

    /**
     * 表頭
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/header")
    public ModelAndView header (@RequestBody Map<String, Object> vo) {
        String uri = "";
        String formClass = StringUtils.upperCase(MapUtils.getString(vo, "formClass"));

        switch (FormEnum.valueOf(formClass)) {
            case SR:
                uri = DispatcherEnum.REQUIREMENT.header();
                break;

            case Q:
                uri = DispatcherEnum.QUESTION.header();
                break;

            case INC:
                uri = DispatcherEnum.EVENT.header();
                break;

            case CHG:
                uri = DispatcherEnum.CHANGE.header();
                break;

            case Q_C:
            case SR_C:
            case INC_C:
                uri = DispatcherEnum.COUNTERSIGNED.header();
                break;

            case JOB_SP:
                uri = DispatcherEnum.JOB_SP.header();
                break;
                
            case JOB_AP:
                uri = DispatcherEnum.JOB_AP.header();
                break;
            
            case JOB_SP_C:
                uri = DispatcherEnum.JOB_SP_C.header();
                break;
            
            case JOB_AP_C:
                uri = DispatcherEnum.JOB_AP_C.header();
                break;

            case BA:
                uri = DispatcherEnum.BATCHINTERRUPT.header();
                break;

            case KL:
                uri = DispatcherEnum.KNOWLEDGE.header();
                break;
            
            default:
                break;
        }
        
        ModelAndView modelView = new ModelAndView(uri);
        modelView.addObject("formClass", formClass);
        modelView.addObject("info", BeanUtil.toJson(vo));

        return modelView;
    }

    /**
     * 表單資訊
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/formInfo")
    public ModelAndView formInfo (@RequestBody Map<String, Object> vo) {
        String uri = "";
        String formClass = StringUtils.upperCase(MapUtils.getString(vo, "formClass"));

        switch (FormEnum.valueOf(formClass)) {
            case SR:
                uri = DispatcherEnum.REQUIREMENT.formInfo();
                break;

            case Q:
                uri = DispatcherEnum.QUESTION.formInfo();
                break;

            case INC:
                uri = DispatcherEnum.EVENT.formInfo();
                break;

            case CHG:
                uri = DispatcherEnum.CHANGE.formInfo();
                break;

            case Q_C:
            case SR_C:
            case INC_C:
                uri = DispatcherEnum.COUNTERSIGNED.formInfo();
                break;

            case JOB_SP:
                uri = DispatcherEnum.JOB_SP.formInfo();
                break;

            case JOB_AP:
                uri = DispatcherEnum.JOB_AP.formInfo();
                break;
            
            case JOB_SP_C:
                uri = DispatcherEnum.JOB_SP_C.formInfo();
                break;
            
            case JOB_AP_C:
                uri = DispatcherEnum.JOB_AP_C.formInfo();
                break;
            
            case BA:
                uri = DispatcherEnum.BATCHINTERRUPT.formInfo();
                break;

            case KL:
                uri = DispatcherEnum.KNOWLEDGE.formInfo();
                break;
            
            default:
                break;
        }

        return new ModelAndView(uri, "formClass", formClass);
    }

    /**
     * 作業關卡
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/checkPerson")
    public ModelAndView checkPerson (@RequestBody Map<String, Object> vo) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(DispatcherEnum.JOB.checkPerson());
        modelAndView.addObject("tabType", StringUtils.upperCase(MapUtils.getString(vo, "tabType")));
        return modelAndView;

    }

    /**
     * 程式庫
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/library")
    public ModelAndView library (@RequestBody Map<String, Object> vo) {
        return new ModelAndView(DispatcherEnum.JOB.library(), "tabType",
                StringUtils.upperCase(MapUtils.getString(vo, "tabType")));
    }

    /**
     * 衝擊分析
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/impactAnalysis")
    public ModelAndView impactAnalysis (@RequestBody Map<String, Object> vo) {
        return new ModelAndView(
                DispatcherEnum.FORM.impactAnalysis(),
                "formClass", StringUtils.upperCase(MapUtils.getString(vo, "formClass")));
    }

    /**
     * 處理方案
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/program")
    public ModelAndView program (@RequestBody Map<String, Object> vo) {
        String uri = "";
        String formClass = StringUtils.upperCase(MapUtils.getString(vo, "formClass"));

        switch (FormEnum.valueOf(formClass)) {
            case SR:
                uri = DispatcherEnum.REQUIREMENT.program();
                break;

            case Q:
                uri = DispatcherEnum.QUESTION.program();
                break;

            case INC:
                uri = DispatcherEnum.EVENT.program();
                break;

            default:
                break;
        }

        return new ModelAndView(uri, "formClass", formClass);
    }

    /**
     * 日誌
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/log")
    public ModelAndView log (@RequestBody Map<String, Object> vo) {
        return new ModelAndView(DispatcherEnum.FORM.log(), "formClass",
                StringUtils.upperCase(MapUtils.getString(vo, "formClass")));
    }

    /**
     * 審核
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/checkLog")
    public ModelAndView checkLog (@RequestBody Map<String, Object> vo) {
        String formClass = StringUtils.upperCase(MapUtils.getString(vo, "formClass"));
        return new ModelAndView(DispatcherEnum.FORM.checkLog(), "formClass", formClass);
    }

    /**
     * 附件
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/fileList")
    public ModelAndView fileList (@RequestBody Map<String, Object> vo) {
        return new ModelAndView(DispatcherEnum.FORM.fileList(), "formClass",
                StringUtils.upperCase(MapUtils.getString(vo, "formClass")));
    }

    /**
     * 關連表單
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/linkList")
    public ModelAndView linkList (@RequestBody Map<String, Object> vo) {
        return new ModelAndView(DispatcherEnum.FORM.linkList(), "formClass",
                StringUtils.upperCase(MapUtils.getString(vo, "formClass")));
    }

    /**
     * 流程資訊
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/processInfo")
    public ModelAndView processInfo (@RequestBody Map<String, Object> vo) {
        return new ModelAndView(DispatcherEnum.FORM.processInfo(), "formClass",
                StringUtils.upperCase(MapUtils.getString(vo, "formClass")));
    }

    /**
     * 工作單頁簽 設計1234科
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/ap")
    public ModelAndView ap (@RequestBody Map<String, Object> vo) {
        return new ModelAndView(DispatcherEnum.JOB.ap(), "tabType",
                StringUtils.upperCase(MapUtils.getString(vo, "tabType")));
    }

    /**
     * 工作單頁簽
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/dc")
    public ModelAndView dc (@RequestBody Map<String, Object> vo) {
        return new ModelAndView(DispatcherEnum.JOB.dc(), "tabType",
                StringUtils.upperCase(MapUtils.getString(vo, "tabType")));
    }

    /**
     * 工作單頁簽 資安規劃/資安管理
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/planmgmt")
    public ModelAndView planmgmt (@RequestBody Map<String, Object> vo) {
        return new ModelAndView(DispatcherEnum.JOB.planmgmt(), "tabType",
                StringUtils.upperCase(MapUtils.getString(vo, "tabType")));
    }

    /**
     * 工作單頁簽
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/working")
    public ModelAndView working (@RequestBody Map<String, Object> vo) {
        String uri = "";
        String tabType = StringUtils.upperCase(MapUtils.getString(vo, "tabType"));

        switch (tabType) {
            case "OPEN":
                uri = DispatcherEnum.JOB.open();
                break;

            case "DB":
                uri = DispatcherEnum.JOB.db();
                break;

            case "PROGRAMS":
                uri = DispatcherEnum.JOB.programs();
                break;

            default:
                break;
        }

        return new ModelAndView(uri, "tabType", tabType);
    }

    /**
     * 工作單頁簽
     * 
     * @param vo
     * @return
     */
    @PostMapping(path = "/other")
    public ModelAndView other (@RequestBody Map<String, Object> vo) {
        String uri = "";
        String tabType = StringUtils.upperCase(MapUtils.getString(vo, "tabType"));

        switch (tabType) {
            case "OA":
                uri = DispatcherEnum.JOB.oa();
                break;

            case "EA":
                uri = DispatcherEnum.JOB.ea();
                break;

            case "PT":
                uri = DispatcherEnum.JOB.pt();
                break;

            case "BATCH":
                uri = DispatcherEnum.JOB.batch();
                break;

            case "SP":
                uri = DispatcherEnum.JOB.sp();
                break;

            default:
                break;
        }

        return new ModelAndView(uri, "tabType", tabType);
    }

    @Override
    @Deprecated
    public ModelAndView initPage () {
        return null;
    }

}

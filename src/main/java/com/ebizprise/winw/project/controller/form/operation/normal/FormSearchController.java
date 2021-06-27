package com.ebizprise.winw.project.controller.form.operation.normal;

import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.ICommonFormService;
import com.ebizprise.winw.project.service.IFormSearchService;
import com.ebizprise.winw.project.vo.FormSearchVO;

/**
 * 表單查詢
 * 
 * @author adam.yeh
 */
@RestController
@RequestMapping("/formSearch")
public class FormSearchController extends BaseController {
    
    @Autowired
    private IFormSearchService service;
    @Autowired
    private ICommonFormService commonFormService;

    @Override
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView initPage () {
        return new ModelAndView(DispatcherEnum.FORM_SEARCH.initPage());
    }
    
    @RequestMapping(path = "/knowledge",method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView knowledge (FormSearchVO vo) {
        return new ModelAndView(DispatcherEnum.KNOWLEDGE_FIND.initPage());
    }
    
    /**
     * 新增表單
     * 
     * @param vo
     */
    @RequestMapping(path = "/addPage/{formClass}",method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView addPage (FormSearchVO vo) {
        //只有經辦才可以進入新增表單頁面
        if(commonFormService.isPic()) {
            return new ModelAndView(DispatcherEnum.FORM_SEARCH.dispatch(), "info", vo);
        } else {
            return new ModelAndView(DispatcherEnum.DASHBOARD.dispatch(), "info", vo);
        }
    }
    
    /**
     * 查詢表單的導頁功能
     * 
     * @param vo
     */
    @GetMapping(path = "/search/{formId}")
    public ModelAndView search(FormSearchVO vo) {
        service.getFormInfo(vo);
        vo.setIsSearch(true);
        return new ModelAndView(DispatcherEnum.FORM_SEARCH.dispatch(), "info", vo);
    }

    @PostMapping(path = "/query", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<FormSearchVO>> query (@RequestBody FormSearchVO vo) {
        String formClazz = vo.getFormClass();

        // 非系統科工作單會動態渲染出會辦頁簽內的資料,
        // 所以直接換成查詢會辦頁籤內容。
        if (StringUtils.isNotBlank(formClazz)) {
            swapCountersigned(vo, FormEnum.valueOf(formClazz));
        }
        
        return ResponseEntity.ok(service.findBySearch(vo));
    }

    /**
     * 取得表單類型的欄位資訊
     * 
     * @param formSearchVO
     */
    @GetMapping(path = "/getFormFieldsInfo/{formClass}")
    public List<FormSearchVO> getFormFieldsInfo(FormSearchVO vo) {
        return service.findFormFieldsByFormClass(vo.getFormClass());
    }

    /**
     * 表單作業查詢結果匯出Excel
     * @throws IOException 
     * 
     */
    @PostMapping(path = "/export")
    public void export (@RequestBody FormSearchVO vo) throws IOException {
        // 非系統科工作單會動態渲染出會辦頁簽內的資料,
        // 所以直接換成查詢會辦頁籤內容。
        if (StringUtils.isNotBlank(vo.getFormClass())) {
            swapCountersigned(vo, FormEnum.valueOf(vo.getFormClass()));
        }
        
        List<FormSearchVO> formRecords = service.findByExport(vo);
        service.exportExcel(vo, formRecords, response);
    }

    /**
     * 非系統科工作單需要把科別由countersigneds轉換到cDivision<br>
     * 這樣才查詢的到會辦頁簽的資料。
     * @param vo
     * @param clazz
     * @author adam.yeh
     */
    private void swapCountersigned (FormSearchVO vo, FormEnum clazz) {
        String countersigneds = "";

        if (FormEnum.JOB_AP == clazz && 
                StringUtils.isNotBlank(vo.getCountersigneds())) {
            countersigneds = vo.getCountersigneds();
        } else if (FormEnum.JOB_AP_C == clazz && 
                StringUtils.isNotBlank(vo.getApcCountersigneds())) {
            countersigneds = vo.getApcCountersigneds();
        }
        
        if (StringUtils.isNotBlank(countersigneds) && 
                !FormJobEnum.DC.name().equals(countersigneds)) {
            vo.setcDivision(countersigneds.split("-")[1]);
        }
    }
    
}

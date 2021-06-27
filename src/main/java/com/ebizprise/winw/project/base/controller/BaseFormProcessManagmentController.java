package com.ebizprise.winw.project.base.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.winw.project.service.IFormProcessBaseService;
import com.ebizprise.winw.project.vo.HtmlVO;

/**
 * 表單流程管理 共用 Base Controller
 * 
 * The <code>BaseFormProcessController</code>
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年7月4日
 */
public abstract class BaseFormProcessManagmentController extends BaseController {
    
    @Autowired
    private IFormProcessBaseService formProcessBaseService;
    
    /**
     * 取得不能更新資料的訊息
     * 
     * @return Map
     */
    protected Map<String, Object> getCannotUpdateMessage() {
        Map<String, Object> resultMp = new HashMap<>();
        resultMp.put("result", this.getMessage("global.update.result"));// 資料更新成功
        
        return resultMp;
    }

    /**
     * 根據狀態,回傳message
     * 
     * @param result
     * @return Map
     */
    protected Map<String, Object> getResultMessage(boolean result) {
        Map<String, Object> resultMp = new HashMap<>();

        if (result) {
            resultMp.put("result", this.getMessage("global.update.result"));// 資料更新成功
        } else {
            resultMp.put("result", this.getMessage("global.save.fail"));// 儲存資料失敗,請檢查通知相關人員並檢查伺服器Log
        }
        return resultMp;
    }
    
    /**
     * 取得頁面所需要的Selector
     * 
     * @param modelAndView
     * @return ModelAndView
     */
    protected ModelAndView getFormProcessManagmentSelector(ModelAndView modelAndView) {
        //取得表單類別(目前為HardCode)
        List<HtmlVO> formTypeLs =  formProcessBaseService.getFormTypeSelector();
        //取得科別
        List<HtmlVO> divisionLs =  formProcessBaseService.getSysGroupSelector();
        modelAndView.addObject("formTypeLs", formTypeLs);
        modelAndView.addObject("divisionLs", divisionLs);
        
        return modelAndView;
    }
    
}

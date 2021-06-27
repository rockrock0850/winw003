package com.ebizprise.winw.project.controller.sysmanagment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.IQuestionMaintainService;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.QuestionVO;

/**
 * 衝擊分析題庫維護
 * 
 * @author joyce.hsu
 * @version 1.0, Created at 2019年6月4日
 */
@RestController
@RequestMapping("/system/questionMaintain")
public class QuestionMaintainController extends BaseController {

    @Autowired
    private IQuestionMaintainService questionMaintainService;

    @Override
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView initPage () {
        return new ModelAndView(DispatcherEnum.QUESTION_MAINTAIN.initPage());
    }
    
    @PostMapping(path = "/getParameterList")
    public List<QuestionVO> getParameterList (@RequestBody QuestionVO vo) {
        return questionMaintainService.getQuestionVOs(vo);
    }

    /**
     * 儲存功能
     */
    @PostMapping(value = "/updateParams", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateParameter (@RequestBody List<QuestionVO> vos) {
        questionMaintainService.updateQuestions(vos);
    }

    /**
     * 刪除功能
     */
    @PostMapping(value = "/deleteParams", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteParameter (@RequestBody List<QuestionVO> voList) {
        questionMaintainService.deleteQuestions(voList);
    }

}

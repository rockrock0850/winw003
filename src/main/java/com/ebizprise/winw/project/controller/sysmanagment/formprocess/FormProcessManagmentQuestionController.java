package com.ebizprise.winw.project.controller.sysmanagment.formprocess;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.controller.BaseFormProcessManagmentController;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.IFormProcessManagmentService;
import com.ebizprise.winw.project.vo.FormProcessManagmentQuestionFormVO;

/**
 * 表單流程管理 問題單 控制器
 * 
 * The <code>FormProcessManagmentController</code>	
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月24日
 */
@Controller
@RequestMapping("/formProcessManagmentQuestion")
public class FormProcessManagmentQuestionController extends BaseFormProcessManagmentController {
    
    //private static final Logger logger = LoggerFactory.getLogger(FormProcessManagmentQuestionController.class);
    
    @Autowired
    private IFormProcessManagmentService<FormProcessManagmentQuestionFormVO> formProcessQuestionService;
    
    /**
     * 表單流程管理 首頁
     * 
     */
    @Override
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView initPage() {
        ModelAndView modelAndView = new ModelAndView(DispatcherEnum.FORM_PROCESS_MANAGMENT_Q.addPage());
        
        return getFormProcessManagmentSelector(modelAndView);
    }
    
    /**
     * 新增表單申請流程
     * 
     * @return String
     */
    @PostMapping(value = "/insertFormProcess", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String,Object>> insertFormProcess(@RequestBody FormProcessManagmentQuestionFormVO formVo) {
        return ResponseEntity.ok(getResultMessage(formProcessQuestionService.insertFormProcess(formVo)));
    }
    
    /**
     * 更新表單申請流程
     * 
     * @return String
     */
    @PostMapping(value = "/updateFormProcess", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String,Object>> updateFormProcess(@RequestBody FormProcessManagmentQuestionFormVO formVo) {
        //若為啟用狀態,則無法被更新
        if(StringConstant.SHORT_YES.equalsIgnoreCase(formVo.getIsEnable())) {
            return ResponseEntity.ok(getCannotUpdateMessage());
        }
        
        return ResponseEntity.ok(getResultMessage(formProcessQuestionService.updateFormProcess(formVo)));
    }
    
}

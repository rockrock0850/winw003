package com.ebizprise.winw.project.controller.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;

/**
 * 登入
 *  
 * @author adam.yeh
 */
@RestController
@RequestMapping("/login")
public class LoginController extends BaseController {
    
    @Value("${version}")
    private String version;
    @Value("${sit.test.form}")
    private boolean isTestForm;
    @Value("${uat.test.report}")
    private boolean isTestReport;

	@Override
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView initPage() {
	    Map<String, Object> params = new HashMap<>();
	    params.put("version", version);
	    params.put("isTestForm", isTestForm);
        params.put("isTestReport", isTestReport);
	    
		return new ModelAndView(DispatcherEnum.LOGIN.initPage(), "params", params);
	}

}
package com.ebizprise.winw.project.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.winw.project.base.controller.BaseController;

/**
 * 首頁導向 dashboard
 * @author gary.tsai 2019/12/6
 */
@Controller
public class IndexController extends BaseController {

    @Override
    @RequestMapping("/")
    public ModelAndView initPage() {
        return new ModelAndView("redirect:/dashboard");
    }
    
}

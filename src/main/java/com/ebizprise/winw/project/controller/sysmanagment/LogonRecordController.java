package com.ebizprise.winw.project.controller.sysmanagment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.ISysUserService;
import com.ebizprise.winw.project.vo.LogonRecordVO;

/**
 * 
 * 登入登出查詢作業
 * 
 * @author willy.peng
 * @version 1.0, Created at 2019年5月29日
 */
@RestController
@RequestMapping("/logonRecord")
public class LogonRecordController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;

    @Override
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView initPage() {
        return new ModelAndView(DispatcherEnum.LOGON_RECORD.initPage());
    }
    
    @PostMapping(path = "/search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<LogonRecordVO>> search(@RequestBody LogonRecordVO queryVo) {
        List<LogonRecordVO> logonRecords = new ArrayList<>();
        logonRecords = sysUserService.findUserLogsByCondition(queryVo);

        return ResponseEntity.ok(logonRecords);
    }
}

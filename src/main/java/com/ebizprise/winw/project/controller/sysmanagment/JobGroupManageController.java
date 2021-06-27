package com.ebizprise.winw.project.controller.sysmanagment;

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
import com.ebizprise.winw.project.service.IJobGroupManagementService;
import com.ebizprise.winw.project.vo.SystemOptionVO;

/**
 * 工作組別管理
 * 
 * @author momo.liu 2020/08/25
 */
@RestController
@RequestMapping("/jobGroupManagement/init")
public class JobGroupManageController extends BaseController {

    @Autowired
    private IJobGroupManagementService jobGroupManagementService;

    // 工作組別管理 首頁
    @Override
    @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView initPage() {
        return new ModelAndView(DispatcherEnum.JOB_GROUP_MANAGE.initPage());
    }

    // 工作組別管理 查詢
    @PostMapping(value = "/getJopGroupManagementByCondition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<SystemOptionVO>> getJopGroupManagementByCondition(@RequestBody SystemOptionVO vo) {
        return ResponseEntity.ok(jobGroupManagementService.getJopGroupManagementByCondition(vo));
    }

    // 新增工作組別管理
    @PostMapping(value = "/createData")
    public SystemOptionVO createData(@RequestBody SystemOptionVO vo) {
        SystemOptionVO condition = new SystemOptionVO();
        condition.setDisplay(vo.getDisplay());
        List<SystemOptionVO> exist = jobGroupManagementService.getJopGroupManagementByCondition(condition);

        if (exist.size() == 0) {
            return jobGroupManagementService.createData(vo);
        } else {
            vo.setValidateLogicError(vo.getDisplay() + " 已存在，請重新輸入工作組別名稱");
        }
        
        return vo;
    }

    // 編輯工作組別管理
    @PostMapping(value = "/updateData")
    public SystemOptionVO updateData(@RequestBody SystemOptionVO vo) {
        return jobGroupManagementService.update(vo);
    }
    
}

package com.ebizprise.winw.project.controller.sysmanagment;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.winw.project.annotation.CtlLog;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.IJobItemManagementService;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.JobManagementVO;

/**
 * 工作要項管理
 * 
 * @author momo.liu 2020/08/26
 */
@RestController
@RequestMapping("/jobItemManagement/init")
public class JobItemManageController extends BaseController {

    @Autowired
    private IJobItemManagementService jobItemManagementService;

    // 工作要項管理 首頁
    @Override
    @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView initPage() {
        return new ModelAndView(DispatcherEnum.JOB_ITEM_MANAGE.initPage());
    }

    // 工作要項管理 查詢
    @PostMapping(value = "/getJobItemManagmentByCondition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<JobManagementVO>> getSystemManagmentByCondition(@RequestBody JobManagementVO vo) {
        return ResponseEntity.ok(jobItemManagementService.getJobItemManagementByCondition(vo));
    }

    // 工作要項管理 新增
    @PostMapping(value = "/createData")
    public JobManagementVO createData(@RequestBody JobManagementVO vo) {
        JobManagementVO condition = new JobManagementVO();
        condition.setWorkingItemName(vo.getWorkingItemName());
        List<JobManagementVO> exist = jobItemManagementService.getJobItemManagementByCondition(condition);

        if (exist.size() == 0) {
            return jobItemManagementService.createData(vo);
        } else {
            vo.setValidateLogicError(vo.getWorkingItemName() + " 已存在，請重新輸入工作要項管理名稱");
        }

        return vo;
        
    }

    // 工作要項管理 編輯
    @PostMapping(value = "/updateData")
    public JobManagementVO updateData(@RequestBody JobManagementVO vo) {
        return jobItemManagementService.update(vo);
    }

    @CtlLog
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData (@RequestBody JobManagementVO vo) {
        DataVerifyUtil verifyUtil = new DataVerifyUtil()
                .string(vo.getIsReview(), getMessage("workingItem.isReview"))                   // 變更覆核
                .string(vo.getSpGroup(), getMessage("job.item.system.group.search"))            // 系統科組別
                .string(vo.getWorkingItemName(), getMessage("workingItem.model.title"))         // 工作要項
                .string(vo.getIsImpact(), getMessage("job.item.change.is.impact.search"));      // 變更衝擊分析

        return verifyUtil.build();
    }
    
}

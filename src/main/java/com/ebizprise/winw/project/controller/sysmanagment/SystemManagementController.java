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
import com.ebizprise.winw.project.service.ISystemManagementService;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.SystemManagementVO;

/**
 * 系統名稱管理
 * 
 * @author momo.liu 2020/08/13
 */
@RestController
@RequestMapping("/systemNameManagement/init")
public class SystemManagementController extends BaseController {

    @Autowired
    private ISystemManagementService systemManagementService;

    /*
     * 系統名稱管理 首頁
     */
    @Override
    @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView initPage() {
        return new ModelAndView(DispatcherEnum.SYSTEM_NAME_MANAGE.initPage());
    }

    /*
     * 系統名稱管理 查詢
     */
    @PostMapping(value = "/getSystemManagmentByCondition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<SystemManagementVO>> getSystemManagmentByCondition(@RequestBody SystemManagementVO vo) {
        return ResponseEntity.ok(systemManagementService.getSystemManagmentByCondition(vo));
    }

    /*
     * 新增 系統名稱
     */
    @PostMapping(value = "/createData")
    public SystemManagementVO createData(@RequestBody SystemManagementVO vo) {
        SystemManagementVO condition = new SystemManagementVO();
        condition.setSystemId(vo.getSystemId());
        
        List<SystemManagementVO> exist = systemManagementService.getSystemManagmentByCondition(condition);
        if (exist.size() == 0) {
            vo = systemManagementService.createData(vo);
        } else {
            vo.setValidateLogicError(vo.getSystemId() + " 已存在，請重新輸入系統名稱");
        }
        
        return vo;
    }

    /*
     * 編輯 系統名稱
     */
    @PostMapping(value = "/updateData")
    public SystemManagementVO updateData(@RequestBody SystemManagementVO vo) {
        return systemManagementService.update(vo);
    }

    @CtlLog
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData(@RequestBody SystemManagementVO vo) {
        DataVerifyUtil verify = new DataVerifyUtil()
                .string(vo.getMark(), getMessage("form.column.check.mark"))                        // 資訊資產群組
                .string(vo.getSystemId(), getMessage("form.column.check.system.name"))             // 系統名稱(代碼)
                .string(vo.getDepartment(), getMessage("form.column.check.department"))            // 科別
                .number(vo.getOpinc(), getMessage("form.column.check.opinc"))                      // OPINC
                .number(vo.getApinc(), getMessage("form.column.check.apinc"))                      // APINC
                .number(vo.getLimit(), getMessage("form.column.check.limit"))                      // 極限值
                .string(vo.getSystemName(), getMessage("form.column.check.system.chinese.name"));  // 系統中文說明

        return verify.build();
    }
}
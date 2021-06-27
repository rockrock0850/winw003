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
import com.ebizprise.winw.project.service.IStandardChangeService;
import com.ebizprise.winw.project.vo.SystemOptionVO;

/**
 * 標準變更作業管理
 * 
 * @author momo.liu 2020/08/21
 */
@RestController
@RequestMapping("/standardChangeOperation/init")
public class StandardChangeController extends BaseController {

    @Autowired
    private IStandardChangeService standardChangeService;

    // 標準變更作業管理 首頁
    @Override
    @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView initPage() {
        return new ModelAndView(DispatcherEnum.STANDARD_CHANGE_MANAGE.initPage());
    }

    // 標準變更作業管理 查詢
    @PostMapping(value = "/getSystemOptionByCondition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<SystemOptionVO>> getSystemOptionByCondition(@RequestBody SystemOptionVO vo) {
        return ResponseEntity.ok(standardChangeService.getSystemOptionByCondition(vo));
    }

    // 新增標準變更作業
    @PostMapping(value = "/createData")
    public SystemOptionVO createData(@RequestBody SystemOptionVO vo) {
        SystemOptionVO condition = new SystemOptionVO();
        condition.setDisplay(vo.getDisplay()); // 標準變更作業名稱
        
        List<SystemOptionVO> exist = standardChangeService.getSystemOptionByCondition(condition);
        if (exist.size() == 0) {
            return standardChangeService.createData(vo);
        } else {
            vo.setValidateLogicError(vo.getDisplay() + " 已存在，請重新輸入標準變更管理作業名稱");
        }
        
        return vo;
    }

    // 編輯標準變更作業
    @PostMapping(value = "/updateData")
    public SystemOptionVO updateData(@RequestBody SystemOptionVO vo) {
        return standardChangeService.update(vo);
    }
}

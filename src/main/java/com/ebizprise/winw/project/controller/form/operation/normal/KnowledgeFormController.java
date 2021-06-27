package com.ebizprise.winw.project.controller.form.operation.normal;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.annotation.CtlLog;
import com.ebizprise.winw.project.annotation.ModifyRecord;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.service.IBaseForm;
import com.ebizprise.winw.project.service.impl.KnowledgeFormServiceImpl;
import com.ebizprise.winw.project.service.impl.SysUserServiceImpl;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.KnowledgeFormVO;

/**
 * 問題知識庫 靜態表單
 * @author adam.yeh
 */
@RestController
@RequestMapping("/knowledgeForm")
public class KnowledgeFormController extends BaseController implements IBaseForm<KnowledgeFormVO> {
    
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private KnowledgeFormServiceImpl service;
    
    /**
     * 產生知識庫表單
     */
    @PostMapping(path = "/build")
    public String build (@RequestBody KnowledgeFormVO vo) {
        if (service.isEligible(vo)) { 
            service.create(vo);
            return BeanUtil.jEscape(BeanUtil.toJson(vo));
        } else {
            return null;
        }
    }

    /**
     * 根據前端紀錄的表單編號回查表單內容
     * @param vo
     * @return
     * @author adam.yeh
     */
    @CtlLog
    @PostMapping(path = "/list")
    public List<KnowledgeFormVO> list (@RequestBody KnowledgeFormVO vo) {
        return service.list(vo);
    }

    @CtlLog
    @Override
    @PostMapping(path = "/info")
    public KnowledgeFormVO info (@RequestBody KnowledgeFormVO vo) {
        service.getFormInfo(vo);
        return vo;
    }

    @Override
    @ModifyRecord
    @PostMapping(path = "/save")
    public KnowledgeFormVO save (@RequestBody KnowledgeFormVO vo) {
        service.mergeFormInfo(vo);
        return vo;
    }
    
    @CtlLog
    @Override
    @PostMapping(path = "/validateColumnData")
    public Map<String, Object> validateColumnData (@RequestBody KnowledgeFormVO vo) {
        boolean isAdmin = sysUserService.isAdmin();
        
        if (isAdmin) {
            return new DataVerifyUtil().build();
        }
        
        DataVerifyUtil verifyUtil = new DataVerifyUtil()
                .string(vo.getSummary(), this.getMessage("form.report.search.summary"))                             // 摘要
                .string(vo.getReason(), this.getMessage("q.report.operation.specialcase.reason"))                   // 原因
                .string(vo.getIndication(), this.getMessage("q.report.operation.specialcase.sign"))                 // 徵兆
                .string(vo.getProcessProgram(), this.getMessage("q.report.operation.specialcase.treatmentPlan"));   // 處理方案
    
        return verifyUtil.build();
    }
    
    @Override
    @Deprecated
    public KnowledgeFormVO modifyColsByVice(@RequestBody KnowledgeFormVO vo) {
        return vo;
    }

    @Override
    @Deprecated
    public KnowledgeFormVO send (@RequestBody KnowledgeFormVO vo) throws Exception {
        return vo;
    }

    @Override
    @Deprecated
    public Map<String, Object> validateColumnData (@RequestBody KnowledgeFormVO vo, boolean isJustInfo) {
        return null;
    }
    
    @Override
    @Deprecated
    public KnowledgeFormVO approval (@RequestBody KnowledgeFormVO vo) {
        return vo;
    }

    @Override
    @Deprecated
    public KnowledgeFormVO reject (@RequestBody KnowledgeFormVO vo) {
        return vo;
    }

    @Override
    @Deprecated
    public KnowledgeFormVO deprecated (@RequestBody KnowledgeFormVO vo) {
        return vo;
    }
    
    @Override
    @Deprecated
    public ModelAndView initPage () {
        return null;
    }

    @Override
    @Deprecated
    public void delete (KnowledgeFormVO vo) {
    }

}

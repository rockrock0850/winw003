package com.ebizprise.winw.project.controller.common;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.exception.FormBatchApprovalException;
import com.ebizprise.winw.project.service.IBaseFormService;
import com.ebizprise.winw.project.service.IDashBoardService;
import com.ebizprise.winw.project.service.ISysUserService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.*;
import com.ebizprise.winw.project.xml.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 公布欄
 * @author gary.tsai, adam.yeh 2019/5/2
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IDashBoardService dashBoardService;
    @Autowired
    private IBaseFormService<EventFormVO> incFormService;
    @Autowired
    private IBaseFormService<QuestionFormVO> qFormService;
    @Autowired
    private IBaseFormService<ChangeFormVO> chgFormService;
    @Autowired
    private IBaseFormService<SpJobFormVO> spJobFormService;
    @Autowired
    private IBaseFormService<ApJobFormVO> apJobFormService;
    @Autowired
    private IBaseFormService<RequirementFormVO> srFormService;
    @Autowired
    private IBaseFormService<BatchInterruptFormVO> baFormService;
    
	@Override
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView initPage() {
	    SysUserVO vo = sysUserService.getLoginUserInfo(UserInfoUtil.loginUserId());
	    List<DashBoardVO> dashBoardVOs = dashBoardService.getDashBoardDataList(vo);
		return new ModelAndView(DispatcherEnum.DASHBOARD.initPage(),"dashBoardVOs", dashBoardVOs);
	}

	/**
	 * 整批審核
	 * @param voList
	 * @author adam.yeh
	 */
    @PostMapping(path = "/approval")
    public DashBoardVO approval (@RequestBody List<DashBoardVO> voList) {
        FormEnum clazz;
        DashBoardVO response = new DashBoardVO();
        StringBuilder builder = new StringBuilder();
        String message = "表單 : %s 審核錯誤。原因 : %s" + System.lineSeparator();
        String errorMessage = this.getMessage("form.common.approval.warning.2");

        /*
         * 開發時已想過使用介面的方式取出每個service,
         * 但每個介面要對應不同VO又無法使用BaseFormVO統一之,
         * 在不考慮實作新的介面且有時間壓力的情況下,
         * 遂先行使用switch case的方式實作。
         */
        for (DashBoardVO vo : voList) {
            clazz = FormEnum.valueOf(vo.getFormClass());
            try {
                switch (clazz) {
                    case Q:
                        QuestionFormVO q = new QuestionFormVO();
                        BeanUtil.copyProperties(vo, q);
                        if (qFormService.verifyStretchForm(q, FormVerifyType.STRETCH_FINISHED)) {
                            throw new FormBatchApprovalException(errorMessage);
                        }
                        q.setUserSolving(vo.getUserId());
                        qFormService.verifying(q);
                        qFormService.notifyProcessMail(q);
                        break;

                    case SR:
                        RequirementFormVO sr = new RequirementFormVO();
                        BeanUtil.copyProperties(vo, sr);
                        if (srFormService.verifyStretchForm(sr, FormVerifyType.STRETCH_FINISHED)) {
                            throw new FormBatchApprovalException(errorMessage);
                        }
                        sr.setUserSolving(vo.getUserId());
                        srFormService.verifying(sr);
                        srFormService.notifyProcessMail(sr);
                        break;

                    case INC:
                        EventFormVO inc = new EventFormVO();
                        BeanUtil.copyProperties(vo, inc);
                        if (incFormService.verifyStretchForm(inc, FormVerifyType.STRETCH_FINISHED)) {
                            throw new FormBatchApprovalException(errorMessage);
                        }
                        inc.setUserSolving(vo.getUserId());
                        incFormService.verifying(inc);
                        incFormService.notifyProcessMail(inc);
                        break;

                    case JOB_AP:
                        ApJobFormVO ap = new ApJobFormVO();
                        BeanUtil.copyProperties(vo, ap);
                        if (apJobFormService.verifyStretchForm(ap, FormVerifyType.STRETCH_FINISHED)) {
                            throw new FormBatchApprovalException(errorMessage);
                        }
                        ap.setUserSolving(vo.getUserId());
                        apJobFormService.verifying(ap);
                        apJobFormService.notifyProcessMail(ap);
                        break;

                    case JOB_SP:
                        SpJobFormVO sp = new SpJobFormVO();
                        BeanUtil.copyProperties(vo, sp);
                        if (spJobFormService.verifyStretchForm(sp, FormVerifyType.STRETCH_FINISHED)) {
                            throw new FormBatchApprovalException(errorMessage);
                        }
                        sp.setUserSolving(vo.getUserId());
                        spJobFormService.verifying(sp);
                        spJobFormService.notifyProcessMail(sp);
                        break;

                    case CHG:
                        ChangeFormVO chg = new ChangeFormVO();
                        BeanUtil.copyProperties(vo, chg);
                        if (chgFormService.verifyStretchForm(chg, FormVerifyType.STRETCH_FINISHED)) {
                            throw new FormBatchApprovalException(errorMessage);
                        }
                        chg.setUserSolving(vo.getUserId());
                        chgFormService.verifying(chg);
                        chgFormService.notifyProcessMail(chg);
                        break;
                        
                    case BA:
                        BatchInterruptFormVO ba = new BatchInterruptFormVO();
                        BeanUtil.copyProperties(vo, ba);
                        if (baFormService.verifyStretchForm(ba, FormVerifyType.STRETCH_FINISHED)) {
                            throw new FormBatchApprovalException(errorMessage);
                        }
                        ba.setUserSolving(vo.getUserId());
                        baFormService.verifying(ba);
                        baFormService.notifyProcessMail(ba);
                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                builder.append(String.format(message, vo.getFormId(), e.getMessage()));
            }
            response.setValidateLogicError(builder.toString());
        }

        return response;
    }

}

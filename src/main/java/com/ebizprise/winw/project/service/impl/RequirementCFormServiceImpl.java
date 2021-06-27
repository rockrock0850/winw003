package com.ebizprise.winw.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.entity.FormProcessDetailApplySrEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailReviewSrEntity;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplySrRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewSrRepository;
import com.ebizprise.winw.project.service.IBaseCountersignedFormService;
import com.ebizprise.winw.project.vo.CountersignedFormVO;

/**
 * @author gary.tsai 2019/8/22
 */
@Service("requirementCFormService")
public class RequirementCFormServiceImpl extends BaseFormService<CountersignedFormVO> implements IBaseCountersignedFormService<CountersignedFormVO> {
    @Autowired
    private IFormProcessDetailApplySrRepository formProcessDetailApplySrRepository;
    @Autowired
    private IFormProcessDetailReviewSrRepository formProcessDetailReviewSrRepository;

    @Override
    public void sendMail(CountersignedFormVO vo) {
        asyncMailLauncher(vo);
    }

    @Override
    public String getGroupIdFromFormProcess(String detailId, int processOrder, String verifyType) {
        switch (FormEnum.valueOf(verifyType)) {
            case APPLY:
                FormProcessDetailApplySrEntity formProcessDetailApplySrEntity = formProcessDetailApplySrRepository.findByDetailIdAndProcessOrder(detailId, processOrder);
                return formProcessDetailApplySrEntity.getGroupId();
            case REVIEW:
                FormProcessDetailReviewSrEntity formProcessDetailReviewSrEntity = formProcessDetailReviewSrRepository.findByDetailIdAndProcessOrder(detailId, processOrder);
                return formProcessDetailReviewSrEntity.getGroupId();
            default:
                return null;
        }
    }


    @Override
    protected String getFormApplyGroupInfo(CountersignedFormVO vo) {
        FormProcessDetailApplySrEntity applySrPojo =
                formProcessDetailApplySrRepository.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getVerifyLevel()));
        return applySrPojo.getGroupId();
    }

    @Override
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        FormProcessDetailApplySrEntity applySrPojo =
                formProcessDetailApplySrRepository.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));
        return applySrPojo.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(CountersignedFormVO vo) {
        FormProcessDetailReviewSrEntity reviewSrPojo =
                formProcessDetailReviewSrRepository.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getVerifyLevel()));
        return reviewSrPojo.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        FormProcessDetailReviewSrEntity reviewSrPojo =
                formProcessDetailReviewSrRepository.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));
        return reviewSrPojo.getGroupId();
    }

    @Override
    public CountersignedFormVO getFormDetailInfo(String formId) {
        return this.getVariousCFormDetailInfo(formId);
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String detailId, String verifyType, String verifyLevel) {
        return null;
    }

    @Override
    @Deprecated
    protected String isApplyLastLevel (String formClazz, String detailId, String verifyType, String verifyLevel) {
        return null;
    }

    @Override
    @Deprecated
    protected String getReviewLastLevel (String detailId) {
        return null;
    }

}

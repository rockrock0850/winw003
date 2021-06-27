package com.ebizprise.winw.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.entity.FormProcessDetailApplyQEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailReviewQEntity;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyQRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewQRepository;
import com.ebizprise.winw.project.service.IBaseCountersignedFormService;
import com.ebizprise.winw.project.vo.CountersignedFormVO;

/**
 * @author gary.tsai 2019/8/22
 */
@Service("questionCFormService")
public class QuestionCFormServiceImpl extends BaseFormService<CountersignedFormVO> implements IBaseCountersignedFormService<CountersignedFormVO> {
    @Autowired
    private IFormProcessDetailApplyQRepository formProcessDetailApplyQRepository;
    @Autowired
    private IFormProcessDetailReviewQRepository formProcessDetailReviewQRepository;

    @Override
    public void sendMail(CountersignedFormVO vo) {
        asyncMailLauncher(vo);
    }

    @Override
    public String getGroupIdFromFormProcess(String detailId, int processOrder, String verifyType) {
        switch (FormEnum.valueOf(verifyType)) {
            case APPLY:
                FormProcessDetailApplyQEntity formProcessDetailApplyQEntity = formProcessDetailApplyQRepository.findByDetailIdAndProcessOrder(detailId, processOrder);
                return formProcessDetailApplyQEntity.getGroupId();
            case REVIEW:
                FormProcessDetailReviewQEntity formProcessDetailReviewQEntity = formProcessDetailReviewQRepository.findByDetailIdAndProcessOrder(detailId, processOrder);
                return formProcessDetailReviewQEntity.getGroupId();
            default:
                return null;
        }
    }

    @Override
    protected String getFormApplyGroupInfo(CountersignedFormVO vo) {
        FormProcessDetailApplyQEntity applyQPojo =
                formProcessDetailApplyQRepository.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getVerifyLevel()));
        return applyQPojo.getGroupId();
    }

    @Override
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        FormProcessDetailApplyQEntity applyQPojo =
                formProcessDetailApplyQRepository.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));
        return applyQPojo.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(CountersignedFormVO vo) {
        FormProcessDetailReviewQEntity reviewQPojo =
                formProcessDetailReviewQRepository.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getVerifyLevel()));
        return reviewQPojo.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        FormProcessDetailReviewQEntity reviewQPojo =
                formProcessDetailReviewQRepository.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));
        return reviewQPojo.getGroupId();
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

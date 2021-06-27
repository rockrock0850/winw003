package com.ebizprise.winw.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.entity.FormProcessDetailApplyJobEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailReviewJobEntity;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyJobRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewJobRepository;
import com.ebizprise.winw.project.service.IBaseCountersignedFormService;
import com.ebizprise.winw.project.vo.CountersignedFormVO;

/**
 * @author gary.tsai 2019/10/30
 */
@Service("jobAPCFormService")
public class JobAPCFormServiceImpl extends BaseFormService<CountersignedFormVO> implements IBaseCountersignedFormService<CountersignedFormVO> {
    @Autowired
    private IFormProcessDetailApplyJobRepository formProcessDetailApplyJobRepository;
    @Autowired
    private IFormProcessDetailReviewJobRepository formProcessDetailReviewJobRepository;

    @Override
    public void sendMail(CountersignedFormVO vo) {
        asyncMailLauncher(vo);
    }

    @Override
    public String getGroupIdFromFormProcess(String detailId, int processOrder, String verifyType) {
        switch (FormEnum.valueOf(verifyType)) {
            case APPLY:
                FormProcessDetailApplyJobEntity formProcessDetailApplyJobEntity = formProcessDetailApplyJobRepository.findByDetailIdAndProcessOrder(detailId, processOrder);
                return formProcessDetailApplyJobEntity.getGroupId();
            case REVIEW:
                FormProcessDetailReviewJobEntity formProcessDetailReviewJobEntity = formProcessDetailReviewJobRepository.findByDetailIdAndProcessOrder(detailId, processOrder);
                return formProcessDetailReviewJobEntity.getGroupId();
            default:
                return null;
        }
    }

    @Override
    protected String getFormApplyGroupInfo(CountersignedFormVO vo) {
        FormProcessDetailApplyJobEntity applyJobPojo =
                formProcessDetailApplyJobRepository.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getVerifyLevel()));
        return applyJobPojo.getGroupId();
    }

    @Override
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        FormProcessDetailApplyJobEntity applyJobPojo =
                formProcessDetailApplyJobRepository.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));
        return applyJobPojo.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(CountersignedFormVO vo) {
        FormProcessDetailReviewJobEntity reviewJobPojo =
                formProcessDetailReviewJobRepository.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getVerifyLevel()));
        return reviewJobPojo.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        FormProcessDetailReviewJobEntity reviewJobPojo =
                formProcessDetailReviewJobRepository.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));
        return reviewJobPojo.getGroupId();
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

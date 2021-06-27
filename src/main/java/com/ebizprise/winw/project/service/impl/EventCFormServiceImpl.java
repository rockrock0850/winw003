package com.ebizprise.winw.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebizprise.winw.project.base.service.BaseFormService;
import com.ebizprise.winw.project.entity.FormProcessDetailApplyIncEntity;
import com.ebizprise.winw.project.entity.FormProcessDetailReviewIncEntity;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.repository.IFormProcessDetailApplyIncRepository;
import com.ebizprise.winw.project.repository.IFormProcessDetailReviewIncRepository;
import com.ebizprise.winw.project.service.IBaseCountersignedFormService;
import com.ebizprise.winw.project.vo.CountersignedFormVO;

/**
 * @author gary.tsai 2019/8/22
 */
@Service("eventCFormService")
public class EventCFormServiceImpl extends BaseFormService<CountersignedFormVO> implements IBaseCountersignedFormService<CountersignedFormVO> {
    @Autowired
    private IFormProcessDetailApplyIncRepository formProcessDetailApplyIncRepository;
    @Autowired
    private IFormProcessDetailReviewIncRepository formProcessDetailReviewIncRepository;

    @Override
    public void sendMail(CountersignedFormVO vo) {
        asyncMailLauncher(vo);
    }

    @Override
    public String getGroupIdFromFormProcess(String detailId, int processOrder, String verifyType) {
        switch (FormEnum.valueOf(verifyType)) {
            case APPLY:
                FormProcessDetailApplyIncEntity formProcessDetailApplyIncEntity = formProcessDetailApplyIncRepository.findByDetailIdAndProcessOrder(detailId, processOrder);
                return formProcessDetailApplyIncEntity.getGroupId();
            case REVIEW:
                FormProcessDetailReviewIncEntity formProcessDetailReviewIncEntity = formProcessDetailReviewIncRepository.findByDetailIdAndProcessOrder(detailId, processOrder);
                return formProcessDetailReviewIncEntity.getGroupId();
            default:
                return null;
        }
    }

    @Override
    protected String getFormApplyGroupInfo(CountersignedFormVO vo) {
        FormProcessDetailApplyIncEntity applyIncPojo =
                formProcessDetailApplyIncRepository.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getVerifyLevel()));
        return applyIncPojo.getGroupId();
    }

    @Override
    protected String getFormApplyGroupInfo(String detailId, String processOrder) {
        FormProcessDetailApplyIncEntity applyIncPojo =
                formProcessDetailApplyIncRepository.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));
        return applyIncPojo.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(CountersignedFormVO vo) {
        FormProcessDetailReviewIncEntity reviewIncPojo =
                formProcessDetailReviewIncRepository.findByDetailIdAndProcessOrder(vo.getDetailId(), Integer.valueOf(vo.getVerifyLevel()));
        return reviewIncPojo.getGroupId();
    }

    @Override
    protected String getFormReviewGroupInfo(String detailId, String processOrder) {
        FormProcessDetailReviewIncEntity reviewIncPojo =
                formProcessDetailReviewIncRepository.findByDetailIdAndProcessOrder(detailId, Integer.valueOf(processOrder));
        return reviewIncPojo.getGroupId();
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
        // TODO Auto-generated method stub
        return null;
    }

}

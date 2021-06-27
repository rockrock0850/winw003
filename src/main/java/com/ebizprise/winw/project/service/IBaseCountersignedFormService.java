package com.ebizprise.winw.project.service;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * @author gary.tsai 2019/8/22
 */
public interface IBaseCountersignedFormService<formVO extends BaseFormVO> {

    /**
     * 會辦單每一階段流程的寄信流程
     * @param vo
     */
    void sendMail(formVO vo);

    /**
     * 取得該階段流程的群組ID
     * @param detailId
     * @param processOrder
     * @param verifyType
     */
    String getGroupIdFromFormProcess(String detailId, int processOrder, String verifyType);
}

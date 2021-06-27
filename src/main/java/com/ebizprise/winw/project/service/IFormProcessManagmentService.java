package com.ebizprise.winw.project.service;

import com.ebizprise.winw.project.vo.BaseFormProcessManagmentFormVo;
import com.ebizprise.winw.project.vo.FormProcessManagmentResultVO;

public interface IFormProcessManagmentService<formProcessVo extends BaseFormProcessManagmentFormVo> {

    /**
     * 新增表單流程
     * 
     * @param vo
     * @return boolean
     */
    boolean insertFormProcess(formProcessVo vo);
    
    /**
     * 更新表單流程
     * 
     * @param vo
     * @return boolean
     */
    boolean updateFormProcess(formProcessVo vo);

    /**
     * 透過ID,查詢完整的表單流程資訊
     * 
     * @param id
     * @return FormProcessManagmentFormVO
     */
    FormProcessManagmentResultVO getFormProcessManagmentFormById(Long id);

    /**
     * 透過 Detail Id 和 Group Id 取得流程編號
     * @param detailId
     * @param groupId
     * @return
     */
    int getFormProcessOrder(String detailId, String groupId, String verifyType);
    
    /**
     * 透過表單ID,取得對應的Process資訊
     * 
     * @param formId
     * @return
     */
    default FormProcessManagmentResultVO getByFormId(String formId) {return null;}

}

package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.vo.BaseFormProcessManagmentDetailVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentBaseVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentResultVO;
import com.ebizprise.winw.project.vo.HtmlVO;

/**
 * 表單流程管理 共用服務
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年6月27日
 */
public interface IFormProcessBaseService {
    
    /**
     * 透過條件 查詢表單流程資訊
     * 
     * @return List
     */
    List<FormProcessManagmentBaseVO> getFormProcessManagmentByCondition(FormProcessManagmentResultVO vo);
    
    /**
     * 透過ID 更新表單流程狀態
     * 
     * @param voLs
     * @return boolean
     */
    boolean updateFormProcessStatusById(List<FormProcessManagmentBaseVO> voLs);
    
    /**
     * 取得系統群組下拉選單資訊
     * 
     * @return
     */
    List<HtmlVO> getSysGroupSelector();
    
    /**
     * 取得系統科組別下拉選單
     * 
     * @return
     */
    List<HtmlVO> getSpGroupSelector();

    /**
     * 透過部門ID以及科別,取得群組資訊
     * 
     * @return List
     */
    List<HtmlVO> getSysGroupIdByDeptIdAndDivision(FormProcessManagmentBaseVO baseVo);
    
    /**
     * 透過部門ID以及科別,取得群組資訊(含經理)
     * 
     * @return List
     */
    List<HtmlVO> getSysGroupIdByDeptIdAndDivisionWithManagment(FormProcessManagmentBaseVO baseVo);
    
    /**
     * 透過ID 查詢該表單流程的主表
     * 
     * @param Long
     * @return FormProcessManagmentBaseVO
     */
    FormProcessManagmentBaseVO getFormProcessById(Long id);
    
    /**
     * 表單類型下拉選單暫時使用HardCode方式
     * 
     * @return List
     */
    List<HtmlVO> getFormTypeSelector();

    /**
     * 取得申請簽核清單
     * 
     * @param vo
     * @return
     * @author adam.yeh
     */
    public List<BaseFormProcessManagmentDetailVO> getApplySigningList (BaseFormProcessManagmentDetailVO vo);

    /**
     * 取得審核簽核清單
     * 
     * @param vo
     * @return
     * @author adam.yeh
     */
    public List<BaseFormProcessManagmentDetailVO> getReviewSigningList (BaseFormProcessManagmentDetailVO vo);

    /**
     * 透過條件 查詢單筆FormProcess流程
     * 
     * @param vo
     * @return FormProcessManagmentBaseVO
     */
    FormProcessManagmentBaseVO getFormProcessByCondition(FormProcessManagmentBaseVO vo);
    
    /**
     * 取得當前關卡資訊
     * 
     * @param vo
     * @return List
     */
    public List<BaseFormProcessManagmentDetailVO> getCurrentSigningList (BaseFormProcessManagmentDetailVO vo);
    
    /**
     * 若為申請第一關,或審核最後一關,當根據情景不同,撈出對應關卡資訊
     * 
     * @param vo
     * @return List
     */
    public List<BaseFormProcessManagmentDetailVO> getLastLevelSigningList (String userTitleCode,BaseFormProcessManagmentDetailVO vo);
    
    /**
     * 複寫送簽關卡名稱
     * 
     * @param signingList 簽核關卡
     * @param usertitleCode 客戶職位代碼
     * @param detailVO 關卡資訊
     * @return List
     */
    public List<BaseFormProcessManagmentDetailVO> overwriteSigningListGroupName(List<BaseFormProcessManagmentDetailVO> signingList,String usertitleCode,BaseFormProcessManagmentDetailVO detailVO);
    
    /**
     * 取得職位代號
     * @param groupId
     * @return String
     */
    public String getUserTitileCode (String groupId);

    /**
     * 取得流程關卡文字
     * @param signingList
     * @param apply
     * @param vo
     * @author jacky.fu
     */
    void getProcessWording (List<BaseFormProcessManagmentDetailVO> signingList, FormEnum apply,BaseFormProcessManagmentDetailVO vo);
    
}

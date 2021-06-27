package com.ebizprise.winw.project.service;

import java.util.List;
import java.util.Map;

import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.util.DataVerifyUtil;
import com.ebizprise.winw.project.vo.CommonCheckPersonVO;
import com.ebizprise.winw.project.vo.CommonJobFormVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentJobApplyVO;

/**
 * 工作單 共用邏輯服務
 * 
 * @author andrew.lee
 * @version 1.0, Created at 2019年9月11日
 */
public interface ICommonJobFormService {

    /**
     * 透過條件,取得設定的流程工作關卡所屬人員資訊
     * @param vo 傳入表單ID、表單類型、流程ID
     * @return List
     */
    public List<FormProcessManagmentJobApplyVO> getJobWorkItems(BaseFormVO vo, boolean isVerify);
    
    /**
     * 取得會辦單明細
     * 
     * @param formId
     * @param division
     * @return
     */
    public CommonJobFormVO getCountersignedDetail(String formId, String division);
    
    /**
     * 儲存會辦單明細
     * 
     * @param vo
     */
    public void saveCountersignedDetail(CommonJobFormVO vo);

    /**
     * 取得工作單 批次 頁簽的明細
     * 
     * @return
     * @author adam.yeh
     */
    public CommonJobFormVO getJobBatchDetail (CommonJobFormVO vo);

    /**
     * 儲存工作單 批次 頁簽的明細
     * 
     * @param vo
     * @author adam.yeh
     */
    public void saveJobBatchDetail (CommonJobFormVO vo);

    /**
     * 儲存作業關卡
     * 
     * @param vo
     * @author adam.yeh
     */
    public void saveCheckPerson (CommonJobFormVO vo);
    
    /**
     * 傳入科別資訊,取得可編輯的工作單對應的科別專屬頁簽權限
     * 
     * @return List
     */
    List<String> getJobDivisionTabList(String division);
    
    /**
     * 用工作表單編號，取得作業關卡人員是否存在
     * 
     * @param formId
     */
    public Map<String, Object> checkJobPeasonExistByFormId(String formId);

    /**
     * 檢核作業關卡已選人員清單是否符合規則
     * 
     * @param vo
     * @param persons
     * @param verifyUtil
     */
    public void validatePersonList (CommonJobFormVO vo, List<CommonCheckPersonVO> persons, DataVerifyUtil verifyUtil);
    
    /**
     * 檢核已選擇之作業關卡人員是否還存在
     * @param formId
     * @param verifyUtil
     * @author adam.yeh
     */
    public void validatePersonIsExist (String formId, DataVerifyUtil verifyUtil);

    /**
     * 判斷該流程階段是否有工作關卡
     * @param detailId
     * @param verifyType 
     * @return
     * @author adam.yeh
     */
    public boolean hasWorkLevel (String detailId, String verifyType);

    /**
     * 取得DB變更、OPEN清單、程式清單頁簽的詳細資料
     * @param vo
     * @return
     * @author adam.yeh
     */
    public CommonJobFormVO getJobWorkingDetail (CommonJobFormVO vo);

    /**
     * 新增/更新DB變更、OPEN清單、程式清單頁簽的詳細資料
     * @param vo
     * @author adam.yeh
     */
    public void mergeJobWorkingDetail (CommonJobFormVO vo);

    /**
     * 刪除DB變更、OPEN清單、程式清單頁簽的詳細資料
     * @param ids
     * @author adam.yeh
     */
    public void mergeJobWorkingDetail (List<Long> ids);

    /**
     * 判斷Ap工作單要顯示資訊科或資安科頁簽
     * @param departmentId
     * @return
     * @author adam.yeh
     */
    public CommonJobFormVO isSecurityDeptTabs (String departmentId);

    /**
     * 傳入表單ID,取得資料管制科內會可編輯的工作單對應的科別專屬頁簽權限
     * 
     * @return List
     */
    public List<String> getInternalProcessJobDivisionTabList(String formId);
}

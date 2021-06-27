package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormVerifyType;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 表單 新增/修改/審核/退回等作業 標準介面
 * 
 * @author adam.yeh
 */
public interface IBaseFormService<formVO extends BaseFormVO> {

    /**
     * 超級副科修改作業館卡人員，平行會辦人員，處理人員後更新對應的審核處理人員
     * @param vo
     * @author jacky.fu
     */
    public default void updateVerifyLog (formVO vo) {};
    
    /**
     * 取得表頭與表單資訊頁簽
     * 
     * @param vo
     */
    public void getFormInfo (formVO vo);

    /**
     * 取得表頭與表單資訊頁簽
     * @param vo
     * @param isBaseOnDatabase 是否強制將資料庫的資料覆蓋頁面資料
     * @author adam.yeh
     */
//    public void getFormInfo (formVO vo, boolean isBaseOnDatabase);

    /**
     * 新增/儲存 表頭與表單資訊
     * 
     * @param vo
     * @throws Exception 
     */
    public void mergeFormInfo (formVO vo) throws Exception;
    
    /**
     * 新增/儲存 表頭與表單資訊 (會拋出例外)
     * @param vo
     * @author adam.yeh
     */
    public default void mergeFormInfoThrows (formVO vo) throws Exception {};
    
    /**
     * 送出申請
     * 
     * @param vo
     * @throws Exception 
     */
    public void sendToVerification (formVO vo) throws Exception;

    /**
     * 審核同意/退回
     * 
     * @param vo
     * @throws Exception 
     */
    public void verifying (formVO vo) throws Exception;
    
    /**
     * 表單送簽時，押上狀態的前置準備
     * 
     * @param vo
     */
    public void prepareVerifying (formVO vo);
    
    /**
     * 副科於非自身關卡修改並儲存表單後，新增一筆簽核紀錄
     * 
     * @param vo
     */
    public void createVerifyCommentByVice (formVO vo);

    /**
     * 依照檢核型態FormVerifyType檢核延伸單
     * @param vo
     * @return
     */
    public boolean verifyStretchForm (formVO vo, FormVerifyType type);

    /**
     * 刪除表單
     * 
     * @param vo
     */
    public void deleteForm (formVO vo);
    
    /**
     * 判斷當前登入者是否有審核的權限
     * 
     * @param vo
     * @return
     */
    public boolean isVerifyAcceptable (formVO vo, SysUserVO userInfo);

    /**
     * 檢查表單是否已棄用或結案
     * 
     * @param vo
     * @return
     */
    public boolean isFormClosed (formVO vo);

    /**
     * 檢查流程是否開啟
     * 
     * @return
     */
    public boolean isNewerDetailExist (formVO vo);
    
    /**
     * 取得最新流程編號
     * @return
     */
    public formVO newerProcessDetail (String division, FormEnum e);

    /**
     * 寄送通知信給下一關或上一關
     * @param vo
     */
    public void notifyProcessMail (formVO vo);

    /**
     * 檢查延伸單是否全部已作廢或結案
     * @param vo
     * @return
     * @author adam.yeh
     */
    public boolean isAllStretchDied (formVO vo);
    
    /**
     * 儲存處理方案的資訊
     * 
     * @param vo
     */
    public default void saveProgram (formVO vo) {}
    
    /**
     * 取得處理方案資訊
     * 
     * @param formId
     * @return
     */
    public default formVO getProgram (String formId) {return null;}
    
    /**
     * 產生延伸單
     * 
     * @param vo
     * @author adam.yeh
     */
    public default void create (formVO vo) {}
    
    /**
     * 鎖定表單檔案狀態
     * 
     * @param formId 表單ID
     */
    public void lockFormFileStatus(String formId);
    
    /**
     * 解開鎖定表單檔案狀態
     * 
     * @param formId
     *            表單ID
     */
    public void unlockFormFileStatus(String formId);

    /**
     * 副科/副理 表單直接結案
     * 
     * @param vo
     */
    public default void immediateClose(formVO vo) {}
    
    /**
     * 透過FormId取得該表單會辦科部門資訊
     * 
     * @param formId
     * @return List
     */
    public default List<String> getFormCountsignList(String formId) {return null;}
    
    /**
     * 檢查該表單是否有上傳對應的附件
     * 若無上傳,則回傳錯誤資訊
     * @return String
     */
    public default String checkAttachmentExists(formVO vo) {return "";}

    /**
     * 若三大主單與三大主單的會辦單的預計完成時間往後延且選擇了 <br>
     * 1. 此單之變更範圍，與原來不同 -> 將該單所開之所有變更單的表單內容增加警告訊息且隱藏[新增工作單]按鈕<br>
     * 2. 此單之變更範圍，與原來相同 -> 將該單所開之所有變更單的預計完成時間同步成與本單同
     * @param vo
     * @author adam.yeh
						 
     */
    public default void ectExtended(formVO vo) throws Exception {};
    
    public default void doInternalProcess(formVO vo) { }
    
    public default void finishedInternalProcess(formVO vo) { }
    
    public default void sendSplitProcess(formVO vo) { }
}

package com.ebizprise.winw.project.service;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.vo.CommonFormVO;
import com.ebizprise.winw.project.vo.FormImpactAnalysisVO;
import com.ebizprise.winw.project.vo.LdapUserVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 表單的 日誌/檔案 共用服務介面
 * 
 * @author adam.yeh
 */
public interface ICommonFormService {
    
    /**
     * 取得日誌清單
     * 
     * @param formId
     * @return
     */
    public List<CommonFormVO> getLogs (String formId);

    /**
     * 註銷多筆日誌
     * 
     * @param voList
     */
    public void deleteLogs (List<CommonFormVO> voList);
    
    /**
     * 多筆儲存日誌
     * 
     * @param voList
     */
    public void saveLogs (List<CommonFormVO> voList);
    
    /**
     * 取得附件清單<br>
     * P.S. 可配合getFormFiles()
     * 
     * @param vo
     * @return
     * @author adam.yeh
     */
    public List<CommonFormVO> getFiles (CommonFormVO vo);
    
    /**
     * 儲存附件清單<br>
     * P.S. 可配合saveFormFile()
     * 
     * @param formId
     * @param description
     * @param alterContent
     * @param layoutDataset
     * @param file
     * @author adam.yeh
     */
    public void saveFile (
            String type, 
            String formId, 
            String description, 
            String alterContent,
            String layoutDataset,
            MultipartFile file) throws Exception;
    
    /**
     * 刪除附件清單<br>
     * P.S. 可配合delFormFile()
     * 
     * @param voList
     * @author adam.yeh
     */
    public void deleteFiles (List<CommonFormVO> voList);
    
    /**
     * 下載檔案<br>
     * P.S. 可配合downloadFile()
     * 
     * @param vo
     * @return
     * @author adam.yeh
     */
    public File download (CommonFormVO vo) throws Exception;
    
    /**
     * 透過姓名,取得用戶資訊
     * 
     * @param userName
     * @return List
     */
    public List<LdapUserVO> getLdapUserByUserName(String userName);
    
    /**
     * 取得全部衝擊分析題目
     * @param formId
     * @return List
     */
    public List<FormImpactAnalysisVO> getFormImpactAnalysis();
    
    /**
     * 透過表單Id, 取得衝擊分析資訊(沒給formId取得全部)
     * @param formId
     * @return List
     */
    public List<FormImpactAnalysisVO> getFormImpactAnalysis(String formId);
    
    /**
     * 保存衝擊分析資訊
     * 
     * @param vo
     */
    void saveFormImpactAnalysis(FormImpactAnalysisVO vo);
    
    /**
     * 取得表單關聯資訊
     * 
     * @param formId
     * @return List
     */
    List<BaseFormVO> getFormRelationship(String formId);
    
    /**
     * 根據表單ID,檢查該表單是否為會辦單
     * 
     * @param formId
     * @return boolean
     */
    boolean isCounterSign(String formId);

    /**
     * 取得需要被啟用的HTML欄位
     * 
     * @param vo
     * @return Array
     */
    public List<String> getEnabledCols (CommonFormVO vo, SysUserVO userInfo);
    
    /**
     * 取得需要被關閉的HTML欄位
     * 
     * @param vo
     * @return Array
     */
    public List<String> getDisabledCols (CommonFormVO vo);

    /**
     * 取得需要被啟用的HTML功能按鈕
     * 
     * @param vo
     * @return
     */
    public List<String> getEnabledButtons (CommonFormVO vo, SysUserVO userInfo);
    
    /**
     * 當前登入人員是否為經辦
     * @return boolean
     */
    public boolean isPic();

    /**
     * 取得審核歷程記錄
     * @param vo
     */
    public List<BaseFormVO> getFormLogs (BaseFormVO vo, SysUserVO userInfo);
    
    /**
     * 儲存Countersigneds
     * @param formId
     * @param formClass
     * @param countersigneds
     * @author bernard.yu
     */
    public void updateCountersigneds (String formId, String formClass, String countersigneds);
    
    /**
     * 做廢表單
     * @param idList
     * @author bernard.yu
     */
    public void deprecatedForms (List<String> idList);
    
    /**
     * 更新會辦科處理情況
     * @author bernard.yu
     */
    public void updateContent (List<String> idList);
    
    /**
     * 副科於非自身關卡修改並儲存表單後，新增一筆簽核紀錄
     * 
     * @param vo
     */
    public void createVerifyCommentByVice (CommonFormVO vo);
    
    /**
     * 鎖定表單檔案狀態
     * 
     * @param formId
     */
    public void lockFormFileStatus (String formId);
    
    /**
     * 寄送通知信給下一關或上一關
     * 
     * @param formId
     */
    public void notifyProcessMail (CommonFormVO vo);
    
}

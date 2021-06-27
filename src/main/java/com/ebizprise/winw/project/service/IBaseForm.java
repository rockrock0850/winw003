package com.ebizprise.winw.project.service;

import java.util.Map;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

/**
 * 表單控制類別 標準介面
 * 
 * @author adam.yeh
 */
public interface IBaseForm<formVO extends BaseFormVO> {
    
    /**
     * 取得表頭與表單資訊頁簽的資料
     * 
     * @param vo
     * @return
     */
    public formVO info (formVO vo);
    
    /**
     * 審核同意
     * 
     * @param vo
     * @throws Exception 
     */
    public formVO approval (formVO vo) throws Exception;
    
    /**
     * 審核退回
     * 
     * @param vo
     * @throws Exception 
     */
    public formVO reject (formVO vo) throws Exception;
    
    /**
     * 表單作廢
     * 
     * @param vo
     * @throws Exception 
     */
    public formVO deprecated (formVO vo) throws Exception;
    
    /**
     * 儲存/暫存表單
     * 
     * @param vo
     * @throws Exception
     */
    public formVO save (formVO vo) throws Exception;
    
    /**
     * 送出表單
     * 
     * @param vo
     */
    public formVO send (formVO vo) throws Exception;
    
    /**
     * 刪除表單
     * 
     * @param vo
     */
    public void delete (formVO vo);
    
    /**
     * 副科於非自身關卡修改欄位並儲存表單
     * (不影響流程)
     * 
     * @param vo
     * @return 
     */
    public formVO modifyColsByVice (formVO vo) throws Exception;

    /**
     * 檢核欄位資料
     * 
     * @param vo
     * @return Map
     * @throws Exception
     */
    public Map<String, Object> validateColumnData(formVO vo) throws Exception;
    
    /**
     * 檢核欄位資料
     * 
     * @param vo
     * @param isJustInfo 是否只驗證表單資訊頁簽
     * @param isSave 按下儲存按鈕的action
     * @return Map
     * @throws Exception
     */
    public default Map<String, Object> validateColumnData(formVO vo, boolean isJustInfo) throws Exception {
        return null;
    }
    
}

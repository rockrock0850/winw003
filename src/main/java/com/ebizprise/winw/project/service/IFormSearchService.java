package com.ebizprise.winw.project.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ebizprise.winw.project.base.vo.BaseFormVO;
import com.ebizprise.winw.project.vo.FormSearchVO;
import com.ebizprise.winw.project.vo.VersionFormVO;

public interface IFormSearchService {

    /**
     * 取得表頭/表單資訊
     * 
     * @param vo
     */
    public void getFormInfo (FormSearchVO vo);

    /**
     * 取得表單類型欄位資訊
     * 
     * @param formClass
     * @return
     */
    public List<FormSearchVO> findFormFieldsByFormClass(String formClass);

    /**
     * 表單查詢
     * 
     * @param formSearchVO
     * @return
     */
    public List<FormSearchVO> findBySearch (FormSearchVO formSearchVO);

    /**
     * 匯出查詢
     * 
     * @param formSearchVO
     * @return
     */
    public List<FormSearchVO> findByExport (FormSearchVO formSearchVO);
    
    /**
     * 表單資料查詢
     * 
     * @param formSearchVO
     * @param isExport
     * @return
     */
    public List<FormSearchVO> findFormByCondition(FormSearchVO formSearchVO, boolean isExport);

    /**
     * 匯出Excel
     * 
     * @param formSearchVo
     * @param records
     * @param response
     * @throws IOException 
     */
    public void exportExcel(FormSearchVO formSearchVo, List<FormSearchVO> records,
            HttpServletResponse response) throws IOException;

    /**
     * 取得非擬案、非結案、非作廢之事件單
     * 
     * @param summary
     */
    public List<BaseFormVO> getIncForms(FormSearchVO vo);

    /**
     * 查詢User開立工作單資訊<br>
     * 查詢工作單狀態
     * 
     * @param search
     * @return
     * @author adam.yeh
     */
    public List<VersionFormVO> getJobInfoList (FormSearchVO search);

    /**
     * 事件單資料查詢(沒分頁)
     * @param condition
     * @return
     * @author jacky.fu
     */
    List<FormSearchVO> getIncFormsByCondition (FormSearchVO condition);
    
}

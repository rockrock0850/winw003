package com.ebizprise.winw.project.validation.form;

import java.util.List;

import com.ebizprise.winw.project.vo.CommonFormVO;
import com.ebizprise.winw.project.vo.GroupFunctionVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

public interface FormColumnValidate {
    
    public List<String> adminButtons();
    
    public List<String> adminColumns();
    
    public List<String> viceButtons(CommonFormVO vo, SysUserVO userInfo);
    
    public List<String> viceColumns(CommonFormVO vo, SysUserVO userInfo);

    public List<String> initButtons (CommonFormVO vo);

    public List<String> proposeButtons (CommonFormVO vo, SysUserVO userInfo);

    public List<String> approveButtons (CommonFormVO vo, SysUserVO userInfo,GroupFunctionVO groupVO);

    public List<String> initColumns (CommonFormVO vo);

    public List<String> applyColumns (CommonFormVO vo, SysUserVO userInfo);

    public List<String> reviewColumns (CommonFormVO vo, SysUserVO userInfo);

    public List<String> proposeColumns (CommonFormVO vo, SysUserVO userInfo);

    public List<String> disableAllColumns (CommonFormVO vo);

    public default List<String> assignButtons (CommonFormVO vo, SysUserVO userInfo) {
        return null;
    };

    public default List<String> assignButtons (CommonFormVO vo, SysUserVO userInfo, GroupFunctionVO groupFunctionVO) {
        return null;
    };
    
    /**
     * 表單狀態為申請中的時候可使用的欄位
     * @return
     * @author adam.yeh
     */
    public default List<String> applyColumns (CommonFormVO vo, SysUserVO userInfo, GroupFunctionVO groupFunctionVO) {
		return null;
	}
}

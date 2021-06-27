package com.ebizprise.winw.project.validation.form.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ebizprise.winw.project.base.validation.BaseFormValidateHelper;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.enums.form.editable.ButtonsEnum;
import com.ebizprise.winw.project.enums.form.editable.UnEditableEnum;
import com.ebizprise.winw.project.validation.form.FormColumnValidate;
import com.ebizprise.winw.project.vo.CommonFormVO;
import com.ebizprise.winw.project.vo.GroupFunctionVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 知識庫欄位/按鈕權限處理類別<br>
 * P.S. 此類別內禁止對資料庫進行操作<br>
 * 參考文件 : WINW003_表單作業_可修改欄位一覽表
 * 
 * @author adam.yeh
 */
@Component
public class KLValidateColHelper extends BaseFormValidateHelper implements FormColumnValidate {

    @Override
    public List<String> adminButtons () {
        return getCommonAdminBtns();
    }

    @Override
    public List<String> adminColumns () {
        return getCommonAdminCols();
    }
    
    @Override
    public List<String> viceColumns (CommonFormVO vo, SysUserVO userInfo) {
        List<String> columns = new ArrayList<String>();

        if (isChief(userInfo) && isFormGroup(vo, userInfo, UserEnum.DIVISION_CHIEF) || 
                isVice(userInfo) && isFormGroup(vo, userInfo, UserEnum.VICE_DIVISION_CHIEF)) {
            columns.addAll(allColumns());
        }
        
        return columns;
    }

    @Override
    public List<String> approveButtons (CommonFormVO vo, SysUserVO userInfo, GroupFunctionVO groupFunctionVO) {
        List<String> buttons = new ArrayList<String>();

        if (isChief(userInfo) && isFormGroup(vo, userInfo, UserEnum.DIVISION_CHIEF) || 
                isVice(userInfo) && isFormGroup(vo, userInfo, UserEnum.VICE_DIVISION_CHIEF)) {
            fileButtons(buttons);
            buttons.add(ButtonsEnum.SAVE.id());
        }
        
        return buttons;
    }

    @Override
    public List<String> initColumns (CommonFormVO vo) {
        return Arrays.asList(UnEditableEnum.KL.columns());
    }

    @Override
    public List<String> proposeColumns (CommonFormVO vo, SysUserVO userInfo) {
        List<String> columns = new ArrayList<String>();
        
        if (isCreator(vo, userInfo)) {
            columns.addAll(
                    Arrays.asList(UnEditableEnum.KL.columns()));
        } else {
            columns = disableAllColumns(vo);
        }
        
        return columns;
    }

    @Override
    public List<String> disableAllColumns (CommonFormVO vo) {
        return allColumns();
    }
    
    @Override
    public List<String> applyColumns (CommonFormVO vo, SysUserVO userInfo) {
        return viceColumns(vo, userInfo);
    }

    @Override
    public List<String> reviewColumns (CommonFormVO vo, SysUserVO userInfo) {
        return viceColumns(vo, userInfo);
    }

    @Override
    @Deprecated
    protected void modifyColumnButtons (List<String> buttons) {
    }

    @Override
    @Deprecated
    protected void applyLevelOneButtons (List<String> buttons) {
    }

    @Override
    @Deprecated
    protected void applyLastLevelColumns (List<String> columns) {
    }
    
    @Override
    @Deprecated
    public List<String> initButtons (CommonFormVO vo) {
        return null;
    }
    
    @Override
    @Deprecated
    public List<String> viceButtons (CommonFormVO vo, SysUserVO userInfo) {
        return null;
    }

    @Override
    @Deprecated
    public List<String> proposeButtons (CommonFormVO vo, SysUserVO userInfo) {
        return null;
    }

}

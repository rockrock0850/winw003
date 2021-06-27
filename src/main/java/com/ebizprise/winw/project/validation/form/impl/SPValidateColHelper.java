package com.ebizprise.winw.project.validation.form.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ebizprise.winw.project.base.validation.BaseFormValidateHelper;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.enums.form.editable.BaseColEnum;
import com.ebizprise.winw.project.enums.form.editable.ButtonsEnum;
import com.ebizprise.winw.project.enums.form.editable.EditableEnum;
import com.ebizprise.winw.project.enums.form.editable.UnEditableEnum;
import com.ebizprise.winw.project.validation.form.FormColumnValidate;
import com.ebizprise.winw.project.vo.CommonFormVO;
import com.ebizprise.winw.project.vo.GroupFunctionVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 系統科工作單單欄位/按鈕權限處理類別<br>
 * P.S. 此類別內禁止對資料庫進行操作<br>
 * 參考文件 : WINW003_表單作業_可修改欄位一覽表
 * 
 * @author adam.yeh
 */
@Component
public class SPValidateColHelper extends BaseFormValidateHelper implements FormColumnValidate {

    @Override
    public List<String> adminButtons () {
        return getCommonAdminBtns();
    }

    @Override
    public List<String> adminColumns () {
        return getCommonAdminCols();
    }
    
    @Override
    public List<String> viceButtons (CommonFormVO vo, SysUserVO userInfo) {
        return getCommonViceBtns(vo, userInfo);
    }
    
    @Override
    public List<String> viceColumns (CommonFormVO vo, SysUserVO userInfo) {
        return getCommonViceCols(vo, userInfo);
    }
    
    @Override
    public List<String> initButtons (CommonFormVO vo) {
        List<String> buttons = new ArrayList<String>();
        bothFileLogButtons(buttons);
        buttons.add(ButtonsEnum.SEND.id());
        buttons.add(ButtonsEnum.SAVE.id());
        buttons.add(ButtonsEnum.PERSON.id());
        
        return buttons;
    }

    @Override
    public List<String> proposeButtons (CommonFormVO vo, SysUserVO userInfo) {
        List<String> buttons = new ArrayList<String>();
        
        if (isCreator(vo, userInfo)) {
            bothFileLogButtons(buttons);
            buttons.add(ButtonsEnum.SEND.id());
            buttons.add(ButtonsEnum.SAVE.id());
            buttons.add(ButtonsEnum.DELETE.id());
            buttons.add(ButtonsEnum.PERSON.id());
        }
        
        return buttons;
    }

    @Override
    public List<String> approveButtons (CommonFormVO vo, SysUserVO userInfo, GroupFunctionVO groupVO)  {
        List<String> buttons = new ArrayList<String>();
        boolean isOwer = Boolean.valueOf(vo.getIsOwner());
        boolean isPic = "1".equals(userInfo.getAuthorLevel());
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        boolean isWorkLevel = Boolean.valueOf(vo.getIsWorkLevel());
        boolean isApplyLastLevel = Boolean.valueOf(vo.getIsApplyLastLevel());
        boolean isCreateJobCIssue = Boolean.valueOf(vo.getIsCreateJobCIssue());
        boolean isModifyColumnData = Boolean.valueOf(vo.getIsModifyColumnData());
        boolean isTester = FormJobEnum.TPERSON.name().equals(vo.getUserName());

        if (isApplyLevelOne(vo, userInfo)) {
            applyLevelOneButtons(buttons);
        } else if (isWorkLevel && !isApprover(vo)) {
            if (isOwer) {
                if (isApplyLastLevel) {
                    buttons.add(ButtonsEnum.CLOSE.id());
                }
                
                buttons.add(ButtonsEnum.SIGNING.id());
            }
            
            if (!isTester) {
                bothFileLogButtons(buttons);
                buttons.add(ButtonsEnum.SAVE.id());
            }
        } else if (isVerifyAcceptable && !isApprover(vo)) {
            bothFileLogButtons(buttons);
            buttons.add(ButtonsEnum.SAVE.id());
            buttons.add(ButtonsEnum.SIGNING.id());
            
            if (!isPic) {
                buttons.add(ButtonsEnum.DEPRECATED.id());
            }

            if (isCreateJobCIssue) {
                buttons.add(ButtonsEnum.JOB.id());
            }
            
            if (isEnableCloseButton(vo)) {
                buttons.add(ButtonsEnum.CLOSE.id());
            }

            if (isModifyColumnData) {
                modifyColumnButtons(buttons);
            }
        }
        
        if(isUserSolving(vo, userInfo) && isPic(groupVO)) {
            logButtons(buttons);
        }     
        
        if(isUserSolving(vo, userInfo)) {
            fileButtons(buttons);
        }
        
        return buttons;
    }

    @Override
    public List<String> initColumns (CommonFormVO vo) {
        return Arrays.asList(UnEditableEnum.JOB_SP.columns());
    }

    @Override
    public List<String> proposeColumns (CommonFormVO vo, SysUserVO userInfo) {
        List<String> columns = new ArrayList<String>();
        
        if (isCreator(vo, userInfo)) {
            columns.addAll(
                    Arrays.asList(UnEditableEnum.JOB_SP.columns()));
        } else {
            columns = disableAllColumns(vo);
        }
        
        return columns;
    }

    @Override
    public List<String> disableAllColumns (CommonFormVO vo) {
        List<String> columns = allColumns();
        columns.add(BaseColEnum.PERSON);
        return columns;
    }
    
    @Override
    public List<String> applyColumns (CommonFormVO vo, SysUserVO userInfo) {
        boolean isOwer = Boolean.valueOf(vo.getIsOwner());
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        boolean isWorkLevel = Boolean.valueOf(vo.getIsWorkLevel());
        boolean isApplyLastLevel = Boolean.valueOf(vo.getIsApplyLastLevel());
        boolean isModifyColumnData = Boolean.valueOf(vo.getIsModifyColumnData());
        boolean isTester = FormJobEnum.TPERSON.name().equals(vo.getUserName());
        
        List<String> columns = new ArrayList<String>();
        
        if (isWorkLevel) {
            columns.add(BaseColEnum.VERIFY_COMMENT);
            
            if (isOwer && !isTester) {
                columns.add(BaseColEnum.EOT);
                columns.add(BaseColEnum.MECT);
                columns.add(BaseColEnum.ONLINE_TIME);
                columns.add(BaseColEnum.OFFLINE_TIME);
                columns.add(BaseColEnum.CLIST);
                columns.add(BaseColEnum.RESET);
                columns.add(BaseColEnum.IS_RESET);
                columns.add(BaseColEnum.WORK_ITEM);
                columns.add(BaseColEnum.REMARK);
                columns.add(BaseColEnum.EFFECT_SCOPE);
                columns.add(BaseColEnum.CONTENT);
                columns.add(BaseColEnum.SUMMARY);
                columns.add(BaseColEnum.WORKING);
                columns.add(BaseColEnum.IS_INTERRUPT);
                columns.add(BaseColEnum.IS_FORWARD);
                columns.add(BaseColEnum.IS_HANDLEFIRST);
                columns.add(BaseColEnum.IS_TEST);
                columns.add(BaseColEnum.IS_PRODUCTION);
                columns.add(BaseColEnum.CCATEGORY);
                columns.add(BaseColEnum.CCLASS);
                columns.add(BaseColEnum.CCOMPONENT_DIALOG);
            }
        } else if (isVerifyAcceptable) {
            columns.add(BaseColEnum.VERIFY_COMMENT);
            
            if (isApplyLevelOne(vo, userInfo)) {
                columns.addAll(Arrays.asList(EditableEnum.JOB_SP.columns()));
            }
            
            if (!isApprover(vo)) {
                if (isApplyLastLevel) {
                    applyLastLevelColumns(columns);
                }
                
                if (isModifyColumnData) {
                    columns.addAll(Arrays.asList(EditableEnum.JOB_SP.columns()));
                }
            }
        }
        
        return columns;
    }

    @Override
    public List<String> reviewColumns (CommonFormVO vo, SysUserVO userInfo) {
        List<String> columns = new ArrayList<String>();
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        boolean isModifyColumnData = Boolean.valueOf(vo.getIsModifyColumnData());
        
        if (isVerifyAcceptable && !isApprover(vo)) {
            columns.add(BaseColEnum.VERIFY_COMMENT);
            if (isModifyColumnData) {
                columns.addAll(Arrays.asList(EditableEnum.JOB_SP.columns()));
            }
        }
        
        return columns;
    }

    @Override
    protected void modifyColumnButtons (List<String> buttons) {
        bothFileLogButtons(buttons);
        buttons.add(ButtonsEnum.SAVE.id());
    }

    @Override
    protected void applyLevelOneButtons (List<String> buttons) {
        bothFileLogButtons(buttons);
        buttons.add(ButtonsEnum.SAVE.id());
        buttons.add(ButtonsEnum.SIGNING.id());
    }

    @Override
    protected void applyLastLevelColumns (List<String> columns) {
        columns.add(BaseColEnum.AST);
        columns.add(BaseColEnum.ACT);
    }

}

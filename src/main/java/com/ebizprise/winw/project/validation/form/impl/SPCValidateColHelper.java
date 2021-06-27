package com.ebizprise.winw.project.validation.form.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.ebizprise.winw.project.base.validation.BaseFormValidateHelper;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.enums.form.editable.BaseColEnum;
import com.ebizprise.winw.project.enums.form.editable.ButtonsEnum;
import com.ebizprise.winw.project.enums.form.editable.EditOneEnum;
import com.ebizprise.winw.project.enums.form.editable.EditableEnum;
import com.ebizprise.winw.project.enums.form.editable.UnEditableEnum;
import com.ebizprise.winw.project.validation.form.FormColumnValidate;
import com.ebizprise.winw.project.vo.CommonFormVO;
import com.ebizprise.winw.project.vo.GroupFunctionVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 系統科工作會辦單欄位/按鈕權限處理類別<br>
 * P.S. 此類別內禁止對資料庫進行操作<br>
 * 參考文件 : WINW003_表單作業_可修改欄位一覽表
 * 
 * @author adam.yeh
 */
@Component
public class SPCValidateColHelper extends BaseFormValidateHelper implements FormColumnValidate {

    @Override
    public List<String> adminButtons () {
        return getCommonAdminBtns();
    }

    @Override
    public List<String> adminColumns () {
        return allColumns();
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
        }
        
        return buttons;
    }
    
    @Override
    public List<String> assignButtons (CommonFormVO vo, SysUserVO userInfo) {
        List<String> buttons = new ArrayList<String>();
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        
        if (isVerifyAcceptable && !isApprover(vo)) {
            bothFileLogButtons(buttons);
            buttons.add(ButtonsEnum.SAVE.id());
            buttons.add(ButtonsEnum.SIGNING.id());
            buttons.add(ButtonsEnum.SOLVE.id());
            buttons.add(ButtonsEnum.PERSON.id());
            buttons.add(ButtonsEnum.DEPRECATED.id());
            
            if (super.isVice(userInfo) && StringUtils.equals(userInfo.getDivision(), FormJobEnum.SP.name())) {
            	buttons.add(ButtonsEnum.SPLIT_PROCESS.id());
            }
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
        boolean isCreateSPJobIussue = Boolean.valueOf(vo.getIsCreateSPJobIssue());
        boolean isModifyColumnData = Boolean.valueOf(vo.getIsModifyColumnData());

        if (isApplyLevelOne(vo, userInfo)) {
            applyLevelOneButtons(buttons);
        } else if (isWorkLevel && !isApprover(vo)) {
            if (isOwer) {
                bothFileLogButtons(buttons);
                buttons.add(ButtonsEnum.SAVE.id());
                buttons.add(ButtonsEnum.SIGNING.id());
            }
        } else if (isVerifyAcceptable && !isApprover(vo)) {
            bothFileLogButtons(buttons);
            buttons.add(ButtonsEnum.SAVE.id());
            buttons.add(ButtonsEnum.SIGNING.id());
            
            if (!isPic) {
                buttons.add(ButtonsEnum.DEPRECATED.id());
            }
            
            if (isCreateSPJobIussue) {
                buttons.add(ButtonsEnum.WORKING.id());
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
        return Arrays.asList(UnEditableEnum.JOB_SP_C.columns());
    }

    @Override
    public List<String> proposeColumns (CommonFormVO vo, SysUserVO userInfo) {
        List<String> columns = new ArrayList<String>();
        
        if (isCreator(vo, userInfo)) {
            columns.addAll(
                    Arrays.asList(UnEditableEnum.JOB_SP_C.columns()));
        } else {
            columns = disableAllColumns(vo);
        }
        
        return columns;
    }

    @Override
    public List<String> disableAllColumns (CommonFormVO vo) {
        List<String> columns = allColumns();
        columns.add(BaseColEnum.PERSON);
        columns.add(BaseColEnum.SPC_GROUPS);
        
        return columns;
    }
    
    @Override
    public List<String> applyColumns (CommonFormVO vo, SysUserVO userInfo) {
        boolean isOwer = Boolean.valueOf(vo.getIsOwner());
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        boolean isWorkLevel = Boolean.valueOf(vo.getIsWorkLevel());
        boolean isSp = vo.getDivisionSolving().indexOf("SP") != -1;
        boolean isApplyLastLevel = Boolean.valueOf(vo.getIsApplyLastLevel());
        boolean isModifyColumnData = Boolean.valueOf(vo.getIsModifyColumnData());
        boolean isTester = (
                StringUtils.isNotBlank(vo.getUserName()) && 
                FormJobEnum.TPERSON.name().equals(vo.getUserName()));
        
        List<String> columns = new ArrayList<String>();
        
        if (isWorkLevel) {
            if (isOwer) {
                columns.add(BaseColEnum.VERIFY_COMMENT);
                columns.add(BaseColEnum.COUNTERSIGNEDS);
            }
        } else if (isVerifyAcceptable) {
            columns.add(BaseColEnum.VERIFY_COMMENT);
            
            if (!isApprover(vo)) {
                columns.add(BaseColEnum.COUNTERSIGNEDS);
                
                if (isApplyLevelOne(vo, userInfo)) {
                    columns.addAll(Arrays.asList(EditOneEnum.JOB_SP_C.columns()));
                }
                
                if (FormEnum.ASSIGNING.name().equals(vo.getFormStatus())) {
                    columns.add(BaseColEnum.PERSON);
                    columns.add(BaseColEnum.SPC_GROUPS);
                    columns.add(isSp ? 
                            BaseColEnum.SPC_GROUPS : BaseColEnum.USER_SOLVING);
                    columns.addAll(allColumns());
                }
                
                if (isApplyLastLevel || !isTester) {
                    applyLastLevelColumns(columns);
                }
                
                if (isModifyColumnData) {
                    columns.addAll(Arrays.asList(EditableEnum.JOB_SP_C.columns()));
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
                columns.addAll(Arrays.asList(EditableEnum.JOB_SP_C.columns()));
            }
        }
        
        return columns;
    }
    
    @Override
    protected boolean isApplyLevelOne (CommonFormVO vo, SysUserVO userInfo) {
        FormEnum verifyType = FormEnum.valueOf(vo.getVerifyType());
        boolean isParallel = StringUtils.isNotBlank(vo.getParallel());
        return ((isResolver(vo, userInfo) || isParallel) && 
                FormEnum.APPLY == verifyType && "2".equals(vo.getVerifyLevel()));
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
        buttons.add(ButtonsEnum.WORKING.id());
    }

    @Override
    protected void applyLastLevelColumns (List<String> columns) {
        columns.add(BaseColEnum.EOT);
        columns.add(BaseColEnum.ECT);
        columns.add(BaseColEnum.AST);
        columns.add(BaseColEnum.ACT);
        columns.add(BaseColEnum.SYSTEM_2);
    }

}

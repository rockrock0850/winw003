package com.ebizprise.winw.project.validation.form.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebizprise.winw.project.base.validation.BaseFormValidateHelper;
import com.ebizprise.winw.project.entity.FormVerifyLogEntity;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.editable.BaseColEnum;
import com.ebizprise.winw.project.enums.form.editable.ButtonsEnum;
import com.ebizprise.winw.project.enums.form.editable.EditOneEnum;
import com.ebizprise.winw.project.enums.form.editable.EditableEnum;
import com.ebizprise.winw.project.enums.form.editable.UnEditableEnum;
import com.ebizprise.winw.project.repository.IFormVerifyLogRepository;
import com.ebizprise.winw.project.validation.form.FormColumnValidate;
import com.ebizprise.winw.project.vo.CommonFormVO;
import com.ebizprise.winw.project.vo.GroupFunctionVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 變更單欄位/按鈕權限處理類別<br>
 * P.S. 此類別內禁止對資料庫進行操作<br>
 * 參考文件 : WINW003_表單作業_可修改欄位一覽表
 * 
 * @author adam.yeh
 */
@Component
public class CHGValidateColHelper extends BaseFormValidateHelper implements FormColumnValidate {
    
    @Autowired
    private IFormVerifyLogRepository verifyRepo;

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
        buttons.add(ButtonsEnum.IMPACT.id());
        buttons.add(ButtonsEnum.PROGRAM.id());
        
        return buttons;
    }

    @Override
    public List<String> proposeButtons (CommonFormVO vo, SysUserVO userInfo) {
        List<String> buttons = new ArrayList<String>();
        
        if (isCreator(vo, userInfo)) {
            bothFileLogButtons(buttons);
            buttons.add(ButtonsEnum.SEND.id());
            buttons.add(ButtonsEnum.SAVE.id());
            buttons.add(ButtonsEnum.IMPACT.id());
            buttons.add(ButtonsEnum.DELETE.id());
            buttons.add(ButtonsEnum.PROGRAM.id());
        }
        
        return buttons;
    }

    @Override
    public List<String> approveButtons (CommonFormVO vo, SysUserVO userInfo, GroupFunctionVO groupVO)  {
        List<String> buttons = new ArrayList<String>();
        FormVerifyLogEntity verifyLog = verifyRepo.findTop1ByFormIdAndVerifyLevelOrderByUpdatedAtDesc(vo.getFormId(), vo.getVerifyLevel());
        boolean isPic = "1".equals(userInfo.getAuthorLevel());
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        boolean isApply = FormEnum.APPLY.name().equals(vo.getVerifyType());
        boolean isCreateJobIussue = Boolean.valueOf(vo.getIsCreateJobIssue());
        boolean isModifyColumnData = Boolean.valueOf(vo.getIsModifyColumnData());
        boolean isPending = FormEnum.PENDING.name().equals(verifyLog.getVerifyResult());
        boolean isViceLevel = verifyLog.getGroupId().endsWith(UserEnum.VSC.name()) && isPending;
        boolean isChiefLevel = verifyLog.getGroupId().equals(vo.getGroupSolving() + UserEnum.SC.name()) && isPending;
        
        if (isApplyLevelOne(vo, userInfo)) {
            applyLevelOneButtons(buttons);
            bothFileLogButtons(buttons);
        } else if (isVerifyAcceptable && !isApprover(vo)) {
            bothFileLogButtons(buttons);
            buttons.add(ButtonsEnum.SAVE.id());
            buttons.add(ButtonsEnum.SIGNING.id());
            
            if (!isPic) {
                buttons.add(ButtonsEnum.DEPRECATED.id());
            }
            
            if (isEnableCloseButton(vo)) {
                buttons.add(ButtonsEnum.CLOSE.id());
            }

            if (isModifyColumnData) {
                modifyColumnButtons(buttons);
            }
        }

        if (isCreateJobIussue) {
            buttons.add(ButtonsEnum.WORKING.id());
        }
        
        if (isUserSolving(vo, userInfo) && isPic(groupVO)) {
            logButtons(buttons);
        }
        
        //變更單權限調整:當經辦送出變更單，進入申請流程之副科、科長關卡，「同科經辦」可直接新增工作單，但不顯示送出按鈕
        if (isPic && isApply && (isViceLevel || isChiefLevel)) {
            buttons.add(ButtonsEnum.WORKING.id());
        }
        
        return buttons;
    }
    
    @Override
    public List<String> initColumns (CommonFormVO vo) {
        return Arrays.asList(UnEditableEnum.CHG.columns());
    }

    @Override
    public List<String> proposeColumns (CommonFormVO vo, SysUserVO userInfo) {
        List<String> columns = new ArrayList<String>();
        
        if (isCreator(vo, userInfo)) {
            columns.addAll(
                    Arrays.asList(UnEditableEnum.CHG.columns()));
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
        List<String> columns = new ArrayList<String>();
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        boolean isModifyColumnData = Boolean.valueOf(vo.getIsModifyColumnData());

        if (isVerifyAcceptable) {
            columns.add(BaseColEnum.VERIFY_COMMENT);

            if (isApplyLevelOne(vo, userInfo)) {
                columns.addAll(Arrays.asList(EditOneEnum.CHG.columns()));
            }

            if (isModifyColumnData && !isApprover(vo)) {
                columns.addAll(Arrays.asList(EditableEnum.CHG.columns()));
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
                columns.addAll(Arrays.asList(EditableEnum.CHG.columns()));
            }
        }
        
        return columns;
    }

    @Override
    protected void modifyColumnButtons (List<String> buttons) {
        logButtons(buttons);
        buttons.add(ButtonsEnum.SAVE.id());
        buttons.add(ButtonsEnum.PROGRAM.id());
    }

    @Override
    protected void applyLevelOneButtons (List<String> buttons) {
        logButtons(buttons);
        buttons.add(ButtonsEnum.SAVE.id());
        buttons.add(ButtonsEnum.PROGRAM.id());
        buttons.add(ButtonsEnum.SIGNING.id());
    }

    @Override
    @Deprecated
    protected void applyLastLevelColumns (List<String> columns) {
    }

}

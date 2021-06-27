package com.ebizprise.winw.project.base.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.enums.form.editable.BaseColEnum;
import com.ebizprise.winw.project.enums.form.editable.ButtonsEnum;
import com.ebizprise.winw.project.vo.CommonFormVO;
import com.ebizprise.winw.project.vo.GroupFunctionVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;

/**
 * 表單欄位/按鈕驗證基礎類別
 * 
 * @author adam.yeh
 */
public abstract class BaseFormValidateHelper {
    
    protected abstract void modifyColumnButtons(List<String> buttons);
    protected abstract void applyLevelOneButtons(List<String> buttons);
    protected abstract void applyLastLevelColumns(List<String> columns);
    
    protected List<String> getCommonAdminBtns () {
        List<String> buttons = new ArrayList<>();
        buttons.add(ButtonsEnum.SAVE.id());
        bothFileLogButtons(buttons);
        
        return buttons;
    }
    
    protected List<String> getCommonAdminCols () {
        List<String> cols = allColumns();
        cols.add(BaseColEnum.USER_SOLVING);
        cols.add(BaseColEnum.USER_CREATED);
        return cols;
    }
    
    /**
     * 不受流程關卡限制，被會辦的副科長可修改之欄位。
     * @param vo
     * @param userInfo
     * @return
     */
    protected List<String> getCommonViceCols (CommonFormVO vo, SysUserVO userInfo) {
        List<String> columns = allColumns();

        if (!FormEnum.DEPRECATED.name().equals(vo.getFormStatus()) && isGroupSolvingByVice(vo, userInfo)) {
            columns.add(BaseColEnum.USER_SOLVING);
        }

        return columns;
    }
    
    /**
     * (1)不受流程關卡限制，被會辦的副科長可取得的按鈕。<br>
     * (2)排除表單狀態為「擬訂中」
     * 
     * @param vo
     * @param userInfo
     * @return
     */
    protected List<String> getCommonViceBtns (CommonFormVO vo, SysUserVO userInfo) {
        boolean isProposing = FormEnum.PROPOSING.name().equals(vo.getFormStatus());
        List<String> buttons = new ArrayList<String>();
        
        if (isGroupSolvingByVice(vo, userInfo) && !isProposing) {
            bothFileLogButtons(buttons);
            buttons.add(ButtonsEnum.SAVE.id());
            
            if (vo.getIsVerifyAcceptable()) {
                buttons.add(ButtonsEnum.SIGNING.id());
                buttons.add(ButtonsEnum.DEPRECATED.id());
            }
        }

        return buttons;
    }
    
    protected void bothFileLogButtons (List<String> buttons) {
        buttons.add(ButtonsEnum.LOGNEW.id());
        buttons.add(ButtonsEnum.LOGDEL.id());
        buttons.add(ButtonsEnum.LOGSAVE.id());
        buttons.add(ButtonsEnum.FILENEW.id());
        buttons.add(ButtonsEnum.FILEDEL.id());
    }
    
    protected void logButtons (List<String> buttons) {
        buttons.add(ButtonsEnum.LOGNEW.id());
        buttons.add(ButtonsEnum.LOGDEL.id());
        buttons.add(ButtonsEnum.LOGSAVE.id());
    }
    
    protected void fileButtons (List<String> buttons) {
        buttons.add(ButtonsEnum.FILENEW.id());
        buttons.add(ButtonsEnum.FILEDEL.id());
    }
    
    protected void enableProgram(List<String> columns) {
        columns.add(BaseColEnum.PROCESS_PROGRAM);
        columns.add(BaseColEnum.TEMPORARY);
    }
    
    /**
     * 是否為科長
     * @param userInfo
     * @return
     * @author adam.yeh
     */
    protected boolean isChief (SysUserVO userInfo) {
        String groupId = userInfo.getGroupId();
        boolean isVice = isVice(userInfo);
        boolean isChief = groupId.indexOf("SC") != -1;
        
        return !isVice && isChief;
    }
    
    /**
     * 是否為副科長
     * @param userInfo
     * @return
     * @author adam.yeh
     */
    protected boolean isVice (SysUserVO userInfo) {
        String groupId = userInfo.getGroupId();
        return groupId.indexOf("VSC") != -1;
    }
    
    /**
     * 是否為副理
     * @param userInfo
     * @return
     * @author adam.yeh
     */
    protected boolean isDirect1 (SysUserVO userInfo) {
        String groupId = userInfo.getGroupId();
        return groupId.indexOf("Direct1") != -1;
    }
    
    /**
     * 是否為協理
     * @param userInfo
     * @return
     * @author adam.yeh
     */
    protected boolean isDirect2 (SysUserVO userInfo) {
        String groupId = userInfo.getGroupId();
        return groupId.indexOf("Direct2") != -1;
    }

    protected List<String> allColumns () {
        List<String> columns = new ArrayList<String>();
        columns.add(BaseColEnum.ALL_INFO_COLS);
        columns.add(BaseColEnum.ALL_IMPACT_COLS);
        columns.add(BaseColEnum.ALL_VERIFY_COLS);
        columns.add(BaseColEnum.ALL_PROGRAM_COLS);
        
        return columns;
    }
    
    protected boolean isCreator (CommonFormVO vo, SysUserVO userInfo) {
        return userInfo.getUserId().equals(vo.getUserCreated()) &&
                StringUtils.contains(vo.getDivisionCreated(), userInfo.getDivision());
    }

    protected boolean isResolver (CommonFormVO vo, SysUserVO userInfo) {
        return userInfo.getUserId().equals(vo.getUserSolving());
    }
    
    protected boolean isEnableCButton (CommonFormVO vo) {
        String verifyType = vo.getVerifyType();
        String isCreateC = vo.getIsCreateCIssue();
        
        return Boolean.valueOf(isCreateC) &&
                FormEnum.APPLY.name().equals(verifyType);
    }

    protected boolean isApplyLevelOne (CommonFormVO vo, SysUserVO userInfo) {
        FormEnum verifyType = FormEnum.valueOf(vo.getVerifyType());
        return (FormEnum.APPLY == verifyType &&
                "1".equals(vo.getVerifyLevel()) &&
                isResolver(vo, userInfo));
    }

    protected boolean isEnableChangeButton (CommonFormVO vo) {
        String verifyType = vo.getVerifyType();
        String isCreateChange = vo.getIsCreateChangeIssue();
        
        return Boolean.valueOf(isCreateChange) &&
                FormEnum.APPLY.name().equals(verifyType);
    }

    /**
     * 若具備該關卡之審核權但又為表單處理人員, 則不予以自行審核權力
     * 
     * @param vo
     * @return
     * @author adam.yeh
     */
    protected boolean isApprover (CommonFormVO vo) {
        return Boolean.valueOf(vo.getIsApprover());
    }

    protected boolean isEnableCloseButton (CommonFormVO vo) {
        return Boolean.valueOf(vo.getIsCloseForm());
    }
    
    /**
     * 是否為表單處理人員
     * 
     * @param vo
     * @return boolean
     */
    protected boolean isUserSolving(CommonFormVO vo,SysUserVO userInfo) {
        String userSolving = vo.getUserSolving();
        String userId = userInfo.getUserId();

        return userSolving.equals(userId);
    }
    
    /**
     * 是否為該科內的群組
     * @param vo            表單資訊
     * @param userInfo      登入者資訊
     * @param targetGroup   需要進行判斷的群組
     * @return
     * @author adam.yeh
     */
    protected boolean isFormGroup (CommonFormVO vo, SysUserVO userInfo, UserEnum targetGroup) {
        String groupSolving = "";
        String loginGroupId = userInfo.getGroupId().split(StringConstant.DASH)[1];
        String[] splited = StringUtils.split(vo.getGroupSolving(), StringConstant.DASH);
        
        if (ArrayUtils.isArrayIndexValid(splited, 1)) {
            groupSolving = splited[1] + targetGroup.symbol();
        }
        
        return loginGroupId.equals(groupSolving);
    }
    
    /**
     * 是否為被會辦的副科長
     * @param vo
     * @return boolean
     */
    protected boolean isGroupSolvingByVice (CommonFormVO vo, SysUserVO userInfo) {
        String loginUserGroupId = userInfo.getGroupId().split(StringConstant.DASH)[1];
        String groupSolving = "";
        String[] groupSolvingArr = StringUtils.split(vo.getGroupSolving(), StringConstant.DASH);
        if (ArrayUtils.isArrayIndexValid(groupSolvingArr, 1)) {
            groupSolving = groupSolvingArr[1] + UserEnum.VICE_DIVISION_CHIEF.symbol();
        }
        
        return loginUserGroupId.equals(groupSolving);
    }
    
    /**
     * 檢查登入人員是否為經辦
     * 
     * @param groupFunctionVO
     * @return boolean
     */
    protected boolean isPic(GroupFunctionVO groupVO) {
        return "0".equals(groupVO.getAuthType());
    }
    
}

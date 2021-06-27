package com.ebizprise.winw.project.validation.form.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.validation.BaseFormValidateHelper;
import com.ebizprise.winw.project.enums.form.FormEnum;
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
 * 問題單欄位/按鈕權限處理類別<br>
 * P.S. 此類別內禁止對資料庫進行操作<br>
 * 參考文件 : WINW003_表單作業_可修改欄位一覽表
 * 
 * @author adam.yeh
 */
@Component
public class QValidateColHelper extends BaseFormValidateHelper implements FormColumnValidate {

    @Override
    public List<String> adminButtons () {
        return getCommonAdminBtns();
    }

    @Override
    public List<String> adminColumns () {
    	List<String> columns = getCommonAdminCols();
        columns.add(BaseColEnum.OBSERVATION);
        return columns;
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
            buttons.add(ButtonsEnum.DELETE.id());
            buttons.add(ButtonsEnum.PROGRAM.id());
        }
        
        return buttons;
    }

    @Override
    public List<String> approveButtons (CommonFormVO vo, SysUserVO userInfo, GroupFunctionVO groupVO)  {
        List<String> buttons = new ArrayList<String>();
        boolean isPic = "1".equals(userInfo.getAuthorLevel());
        boolean isVice = isVice(userInfo);
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        boolean isModifyColumnData = Boolean.valueOf(vo.getIsModifyColumnData());
        boolean isWatching = FormEnum.WATCHING.name().equals(vo.getFormStatus());

        if (isApplyLevelOne(vo, userInfo)) {
            applyLevelOneButtons(buttons);
        } else if (isVerifyAcceptable && !isApprover(vo)) {
            bothFileLogButtons(buttons);
            buttons.add(ButtonsEnum.SAVE.id());
            buttons.add(ButtonsEnum.SIGNING.id());
            
            if (!isPic) {
                buttons.add(ButtonsEnum.DEPRECATED.id());
            }
            
            if (isEnableChangeButton(vo)) {
                buttons.add(ButtonsEnum.ALTER.id());
            }
            
            if (isEnableCButton(vo)) {
                buttons.add(ButtonsEnum.C.id());
            }
            
            if (isEnableCloseButton(vo)) {
                buttons.add(ButtonsEnum.CLOSE.id());
            }

            if (isModifyColumnData) {
                modifyColumnButtons(buttons);
            }
        }
        
        if (isWatching && isVice) {
            buttons.add(ButtonsEnum.SAVE.id());
            buttons.add(ButtonsEnum.SIGNING.id());
            buttons.add(ButtonsEnum.DEPRECATED.id());
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
        return Arrays.asList(UnEditableEnum.Q.columns());
    }

    @Override
    public List<String> proposeColumns (CommonFormVO vo, SysUserVO userInfo) {
        List<String> columns = new ArrayList<String>();
        
        if (isCreator(vo, userInfo)) {
            columns.addAll(
                    Arrays.asList(UnEditableEnum.Q.columns()));
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
    	return null;
    }
    
    @Override
    public List<String> applyColumns (CommonFormVO vo, SysUserVO userInfo, GroupFunctionVO groupFunctionVO) {
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        boolean isApplyLastLevel = Boolean.valueOf(vo.getIsApplyLastLevel());
        boolean isModifyColumnData = Boolean.valueOf(vo.getIsModifyColumnData());
        
        List<String> columns = new ArrayList<String>();

        if (isVerifyAcceptable) {
        	boolean isApplyLevelOne = isApplyLevelOne(vo, userInfo);

        	columns.add(BaseColEnum.VERIFY_COMMENT);
            
            if ((isPic(groupFunctionVO) && (isApplyLevelOne || isApplyLastLevel || vo.getIsBackToApply()))) {
            	columns.add(BaseColEnum.KNOWLEDGE_DELETE);
            }
            
            if (isApplyLevelOne) {
                columns.addAll(Arrays.asList(EditOneEnum.Q.columns()));
                if(isUserSolving(vo, userInfo)) {
                    enableProgram(columns);
                }
            }
            
            if (!isApprover(vo)) {
                if (isVice(userInfo)) {
                    columns.add(BaseColEnum.OBSERVATION);
                }
                
                if (isApplyLastLevel) {
                    applyLastLevelColumns(columns);
                }
                
                if (isModifyColumnData) {
                    columns.addAll(Arrays.asList(EditableEnum.Q.columns()));
                    if (isUserSolving(vo, userInfo)) {
                        enableProgram(columns);
                    }
                }
            }
        }
        
        return columns;
    }

    @Override
    public List<String> reviewColumns (CommonFormVO vo, SysUserVO userInfo) {
        List<String> columns = new ArrayList<String>();
        String isSpecial = vo.getIsSpecial();
        boolean isVerifyAcceptable = vo.getIsVerifyAcceptable();
        boolean isModifyColumnData = Boolean.valueOf(vo.getIsModifyColumnData());
        
        if (isVerifyAcceptable && !isApprover(vo)) {
            columns.add(BaseColEnum.VERIFY_COMMENT);
            
            if (isVice(userInfo)) {
                if (StringConstant.SHORT_NO.equals(isSpecial)) {
                    columns.add(BaseColEnum.IS_SPECIAL);
                } else {
                    columns.add(BaseColEnum.SPECIAL_ENDCASETYPE);
                }
                columns.add(BaseColEnum.OBSERVATION);
            }
            
            if (isModifyColumnData) {
                columns.addAll(Arrays.asList(EditableEnum.Q.columns()));
                //只有處理人員才可編輯處理方案頁簽的資訊
                if(isUserSolving(vo, userInfo)) {
                    enableProgram(columns);
                }
            }
        }
        
        return columns;
    }

    @Override
    protected void modifyColumnButtons (List<String> buttons) {
        bothFileLogButtons(buttons);
        buttons.add(ButtonsEnum.SAVE.id());
        buttons.add(ButtonsEnum.PROGRAM.id());
    }

    @Override
    protected void applyLevelOneButtons (List<String> buttons) {
        bothFileLogButtons(buttons);
        buttons.add(ButtonsEnum.SAVE.id());
        buttons.add(ButtonsEnum.PROGRAM.id());
        buttons.add(ButtonsEnum.SIGNING.id());
    }

    @Override
    protected void applyLastLevelColumns (List<String> columns) {
        columns.add(BaseColEnum.AST);
        columns.add(BaseColEnum.ACT);
        columns.add(BaseColEnum.REASON);
        columns.add(BaseColEnum.INDICATION);
        columns.add(BaseColEnum.IS_SUGGESTCASE);
        columns.add(BaseColEnum.PROCESS_PROGRAM);
        columns.add(BaseColEnum.TEMPORARY);
    }

}

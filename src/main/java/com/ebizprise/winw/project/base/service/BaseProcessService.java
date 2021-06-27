package com.ebizprise.winw.project.base.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.BaseFormProcessManagmentDetailVO;
import com.ebizprise.winw.project.vo.FormProcessManagmentWordingVO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

/**
 * 表單流程 服務層 基礎類別
 * 
 * @author adam.yeh
 */
public abstract class BaseProcessService extends BaseService {

    protected abstract int fetchApplyProcessDetailProcessOrder(String detailId, String groupId);
    protected abstract int fetchReviewProcessDetailProcessOrder(String detailId, String groupId);
    
    /**
     * 計算審核同意/退回時查詢能跳幾關的區間</br>
     * 1. 第0個=起始位置</br>
     * 2. 第1個=結束位置
     * 
     * @param vo processOrder 向下跳關/向上跳關值
     * @param nextLevel
     * @return
     * @author adam.yeh
     */
    protected List<Integer> calculateLimitNumber (BaseFormProcessManagmentDetailVO vo, int nextLevel) {
        int order = vo.getProcessOrder();
        nextLevel = order != 0 ? nextLevel : 1;
        List<Integer> limits = new ArrayList<>();

        limits.add(order);
        limits.add(order + nextLevel);
        
        return limits;
    }

    protected int getFormProcessDetailProcessOrder(String detailId, String groupId, String verifyType){
        switch (FormEnum.valueOf(verifyType)) {
            case APPLY:
                return fetchApplyProcessDetailProcessOrder(detailId, groupId);
            case REVIEW:
                return fetchReviewProcessDetailProcessOrder(detailId, groupId);
            default:
                return 0;
        }

    }
    
    /**
     * 流程關卡文字儲存或更新
     * @param levelWordings
     * @param detailId
     * @param processOrder
     * @param type
     * @author jacky.fu
     */
    protected void saveWording(String levelWordings, String detailId,int processOrder,FormEnum type){
        if(StringUtils.isBlank(levelWordings)) {
            return;
        }
        List<FormProcessManagmentWordingVO> wordingList = new ArrayList<FormProcessManagmentWordingVO>();
        JSONArray jsonArray = new JSONArray(levelWordings);
        jsonArray.forEach(new Consumer<Object>() {
            @Override
            public void accept (Object t) {
                if (t instanceof JSONObject) {
                    FormProcessManagmentWordingVO vo = new FormProcessManagmentWordingVO();
                    JSONObject obj = (JSONObject) t;
                    vo.setProcessorder(processOrder);
                    vo.setWording(obj.getString("wording"));
                    vo.setWordingLevel(obj.getInt("wordingLevel"));
                    vo.setDetailid(detailId);
                    vo.setType(type.toString());
                    wordingList.add(vo);
                }
            }
        });
        
        ResourceEnum resource = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("SAVE_FROM_PROCESS_WORDING");
        for(FormProcessManagmentWordingVO vo : wordingList) {
            Map<String,Object> para = new HashMap<String,Object>();
            para.put("detailId", vo.getDetailid());
            para.put("processOrder", vo.getProcessorder());  
            para.put("wording", vo.getWording());
            para.put("type", vo.getType());  
            para.put("wordingLevel", vo.getWordingLevel());
            para.put("loginUserId", UserInfoUtil.loginUserId());
            jdbcRepository.update(resource,para);
        }
    }
    
    /**
     * 搜尋流程關卡文字
     * @param detailId
     * @param type
     * @param ProcessOrder 當前關卡
     * @return
     * @author jacky.fu
     */
    protected JsonArray getWordingByCondition(String detailId,FormEnum type,String ProcessOrder){
        ResourceEnum resource = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("FIND_FROM_PROCESS_WORDING");
        Conditions condi = new Conditions();
        condi.and().equal("wording.DetailId",detailId);
        condi.and().equal("wording.[Type]",type.toString());
        condi.and().equal("wording.ProcessOrder",ProcessOrder);
        List<FormProcessManagmentWordingVO> list = jdbcRepository.queryForList(resource, condi, FormProcessManagmentWordingVO.class);
        
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<FormProcessManagmentWordingVO>>() {}.getType());
        JsonArray arr = element.getAsJsonArray();
        
        return arr;
    }
    
}

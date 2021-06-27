package com.ebizprise.winw.project.base.vo;

import com.ebizprise.project.utility.bean.BeanUtil;

/**
 * WebService/RestFul API的共用回傳基礎類別<br>
 * Notice : <br>
 * 為了保持回傳的資料格式與文件內容一致,<br>
 * 因此不可加上共用屬性, 以免傳出去的資料格式參雜不必要的欄位<br>
 * 所以這個類別只會複寫toString()。
 * @author adam.yeh
 */
public abstract class BaseResponseVO {

    @Override
    public String toString () {
        return BeanUtil.toJson(this);
    }
    
}

package com.ebizprise.winw.project.test.performance;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

public class NewOperation {

    public static void main (String[] args) {
        BaseFormVO v1 = new BaseFormVO();
        BaseFormVO v2 = new BaseFormVO();
        System.out.println("v1 = v2 ? " + (v1 == v2));
        
        BaseFormVO v3 = new BaseFormVO();
        BaseFormVO v4 = v3;
        System.out.println("v3 = v4 ? " + (v3 == v4));
    }
    
}

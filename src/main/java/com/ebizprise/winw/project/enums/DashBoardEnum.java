/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.enums;

/**
 * 首頁列舉
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年9月13日
 */
public enum DashBoardEnum {
   TYPE_PROPOSING("PROPOSING","report.operation.form.status.proposing.%d"),
   
   TYPE_APPROVING("APPROVING","report.operation.form.status.approving.%d"),
   
   TYPE_CHARGING("CHARGING","report.operation.form.status.charging.%d"),
   
   TYPE_CLOSED("CLOSED","report.operation.form.status.closed.%d"),
   
   TYPE_DEPRECATED("DEPRECATED","report.operation.form.status.deprecated.%d"),
   
   TYPE_ASSIGNING("ASSIGNING","report.operation.form.status.assigning.%d"),
   
   TYPE_SELFSOLVE("SELFSOLVE","report.operation.form.status.selfsolve.%d"),
   
   TYPE_WATCHING("WATCHING","report.operation.form.status.watching.%d");
   
   private String name;
   private String i18nKey;
   DashBoardEnum(String... param) {
       init(param);
   }
   
   private void init(String... param) {
       if(param!=null) {
           for(int i=0;i<param.length;i++) {
               if(i==0) this.name=param[i];
               if(i==1) this.i18nKey=param[i];
           }
       }
   }
   public String getName() {
       return this.name;
   }
   
   public String getI18nKey() {
       return this.i18nKey;
   }
}

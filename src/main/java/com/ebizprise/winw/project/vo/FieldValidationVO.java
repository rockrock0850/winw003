package com.ebizprise.winw.project.vo;

/**
 * @author gary.tsai 2019/6/13
 */
public class FieldValidationVO {
public String fieldName;
public String errorMessage;

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}

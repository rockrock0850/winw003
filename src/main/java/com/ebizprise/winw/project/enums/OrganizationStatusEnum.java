package com.ebizprise.winw.project.enums;

/**
 * @author gary.tsai 2019/5/22
 */
public enum OrganizationStatusEnum {
  DISABLED("0", "DISABLED"),
  ENABLED("1", "ENABLED");

	public String status;
	public String desc;

	public String getStatus() {
		return status;
	}

	public String getDesc() {
		return desc;
	}

	OrganizationStatusEnum(String status, String desc) {
		this.status = status;
		this.desc = desc;
  }
}

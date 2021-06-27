package com.ebizprise.winw.project.xml.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "EIPAD")
public class EIPADVO {

	@XmlElement(name = "accounts")
	public AccountVO accountVO;

	@XmlTransient
	public AccountVO getAccountVO() {
		return accountVO;
	}

	public void setAccountVO(AccountVO accountVO) {
		this.accountVO = accountVO;
	}
}

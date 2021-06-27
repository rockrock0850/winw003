package com.ebizprise.winw.project.xml.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author gary.tsai 2019/5/17
 */
public class AccountVO {
  @XmlElement(name = "userid")
  List<UserIdVO> userIdVOList;

  @XmlTransient
  public List<UserIdVO> getUserIdVOList() {
    return userIdVOList;
  }

  public void setUserIdVOList(List<UserIdVO> userIdVOList) {
    this.userIdVOList = userIdVOList;
  }
}

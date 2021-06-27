package com.ebizprise.winw.project.xml.vo;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author gary.tsai 2019/5/17
 */
public class GroupVO {

  String name;
  String description;

  @XmlElement(name = "Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlElement(name = "description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
  
}

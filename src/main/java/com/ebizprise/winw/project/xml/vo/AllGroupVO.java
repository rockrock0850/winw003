package com.ebizprise.winw.project.xml.vo;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "AllGroup")
public class AllGroupVO {

	@XmlElement(name = "Group")
	public List<GroupVO> groupVOList;

	@XmlTransient
	public List<GroupVO> getGroupVOList() {
		return groupVOList;
	}

	public void setGroupVOList(List<GroupVO> groupVOList) {
		this.groupVOList = groupVOList;
	}
	
}

package com.ebizprise.winw.project.entity;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ebizprise.winw.project.base.entity.BaseEntity;

/**
 * @author gary.tsai 2019/6/27
 */
@Entity
@Table(name = "FORM_INFO_C_DETAIL")
public class FormInfoCDetailEntity extends BaseEntity {

    private Long id;
    private String formId;
    private String summary;
    private String content;
    private String userId;
    private String sClass;
    private String hostHandle;
    private String countersignedHandle;
    private String systemId;
    private String system;
    private String assetGroup;
    private String unit;
    private String userGroup;
    private String spcGroups;
    private String isSuggestCase;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "FormId")
    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    @Basic
    @Column(name = "Summary")
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Basic
    @Column(name = "Content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "UserId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "SClass")
    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }

    @Basic
    @Column(name = "HostHandle")
    public String getHostHandle() {
        return hostHandle;
    }

    public void setHostHandle(String hostHandle) {
        this.hostHandle = hostHandle;
    }

    @Basic
    @Column(name = "CountersignedHandle")
    public String getCountersignedHandle() {
        return countersignedHandle;
    }

    public void setCountersignedHandle(String countersignedHandle) {
        this.countersignedHandle = countersignedHandle;
    }

    @Basic
    @Column(name = "SystemId")
    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    @Basic
    @Column(name = "System")
    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @Basic
    @Column(name = "AssetGroup")
    public String getAssetGroup() {
        return assetGroup;
    }

    public void setAssetGroup(String assetGroup) {
        this.assetGroup = assetGroup;
    }

    @Basic
    @Column(name = "Unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Basic
    @Column(name = "UserGroup")
    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    @Basic
    @Column(name = "SpcGroups")
    public String getSpcGroups () {
        return spcGroups;
    }

    public void setSpcGroups (String spcGroups) {
        this.spcGroups = spcGroups;
    }
    
    @Basic
    @Column(name = "IsSuggestCase")
    public String getIsSuggestCase() {
        return isSuggestCase;
    }

    public void setIsSuggestCase(String isSuggestCase) {
        this.isSuggestCase = isSuggestCase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormInfoCDetailEntity that = (FormInfoCDetailEntity) o;
        return id == that.id && Objects.equals(formId, that.formId) && Objects.equals(summary, that.summary)
                && Objects.equals(content, that.content) && Objects.equals(userId, that.userId)
                && Objects.equals(sClass, that.sClass) && Objects.equals(hostHandle, that.hostHandle)
                && Objects.equals(countersignedHandle, that.countersignedHandle) && Objects.equals(system, that.system)
                && Objects.equals(assetGroup, that.assetGroup) && Objects.equals(unit, that.unit)
                && Objects.equals(userGroup, that.userGroup)
                && Objects.equals(systemId, that.systemId)
                && Objects.equals(spcGroups, that.spcGroups)
                && Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
                && Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
                && Objects.equals(this.getCreatedBy(), this.getCreatedBy())
                && Objects.equals(this.getCreatedAt(), this.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, formId, summary, content, userId, sClass, hostHandle, countersignedHandle, system,
                assetGroup, unit, userGroup,systemId, spcGroups, this.getUpdatedBy(), this.getUpdatedAt(), this.getCreatedBy(),
                this.getCreatedAt());
    }
    
}

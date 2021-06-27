package com.ebizprise.winw.project.entity;

import java.util.Date;
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
@Table(name = "FORM_INFO_INC_DETAIL")
public class FormInfoIncDetailEntity extends BaseEntity {

    private Long id;
    private String formId;
    private String summary;
    private String content;
    private String cCategory;
    private String cClass;
    private String cComponent;
    private String sClass;
    private String sSubClass;
    private String eventClass;
    private String eventType;
    private String eventSecurity;
    private String eventPriority;
    private String systemBrand;
    private String systemId;
    private String system;
    private String assetGroup;
    private String effectScope;
    private String urgentLevel;
    private String countersigneds;
    private String effectScopeWording;
    private String urgentLevelWording;
    private String eventPriorityWording;
    private String isOnlineFail;
    private String onlineJobFormId;
    private Date onlineTime;

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
    @Column(name = "CCategory")
    public String getcCategory() {
        return cCategory;
    }

    public void setcCategory(String cCategory) {
        this.cCategory = cCategory;
    }

    @Basic
    @Column(name = "CClass")
    public String getcClass() {
        return cClass;
    }

    public void setcClass(String cClass) {
        this.cClass = cClass;
    }

    @Basic
    @Column(name = "CComponent")
    public String getcComponent() {
        return cComponent;
    }

    public void setcComponent(String cComponent) {
        this.cComponent = cComponent;
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
    @Column(name = "SSubClass")
    public String getsSubClass() {
        return sSubClass;
    }

    public void setsSubClass(String sSubClass) {
        this.sSubClass = sSubClass;
    }

    @Basic
    @Column(name = "EventClass")
    public String getEventClass() {
        return eventClass;
    }

    public void setEventClass(String eventClass) {
        this.eventClass = eventClass;
    }

    @Basic
    @Column(name = "EventType")
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Basic
    @Column(name = "EventSecurity")
    public String getEventSecurity() {
        return eventSecurity;
    }

    public void setEventSecurity(String eventSecurity) {
        this.eventSecurity = eventSecurity;
    }

    @Basic
    @Column(name = "EventPriority")
    public String getEventPriority() {
        return eventPriority;
    }

    public void setEventPriority(String eventPriority) {
        this.eventPriority = eventPriority;
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
    @Column(name = "EffectScope")
    public String getEffectScope() {
        return effectScope;
    }

    public void setEffectScope(String effectScope) {
        this.effectScope = effectScope;
    }

    @Basic
    @Column(name = "UrgentLevel")
    public String getUrgentLevel() {
        return urgentLevel;
    }

    public void setUrgentLevel(String urgentLevel) {
        this.urgentLevel = urgentLevel;
    }

    @Basic
    @Column(name = "Countersigneds")
    public String getCountersigneds() {
        return countersigneds;
    }

    public void setCountersigneds(String countersigneds) {
        this.countersigneds = countersigneds;
    }

    @Basic
    @Column(name = "SystemBrand")
    public String getSystemBrand () {
        return systemBrand;
    }

    public void setSystemBrand (String systemBrand) {
        this.systemBrand = systemBrand;
    }

    @Basic
    @Column(name = "EffectScopeWording")
    public String getEffectScopeWording () {
        return effectScopeWording;
    }

    public void setEffectScopeWording (String effectScopeWording) {
        this.effectScopeWording = effectScopeWording;
    }

    @Basic
    @Column(name = "UrgentLevelWording")
    public String getUrgentLevelWording () {
        return urgentLevelWording;
    }

    public void setUrgentLevelWording (String urgentLevelWording) {
        this.urgentLevelWording = urgentLevelWording;
    }

    @Basic
    @Column(name = "EventPriorityWording")
    public String getEventPriorityWording () {
        return eventPriorityWording;
    }

    public void setEventPriorityWording (String eventPriorityWording) {
        this.eventPriorityWording = eventPriorityWording;
    }

    @Basic
    @Column(name = "OnlineJobFormId")
    public String getOnlineJobFormId () {
        return onlineJobFormId;
    }

    public void setOnlineJobFormId (String onlineJobFormId) {
        this.onlineJobFormId = onlineJobFormId;
    }

    @Basic
    @Column(name = "OnlineTime")
    public Date getOnlineTime () {
        return onlineTime;
    }

    public void setOnlineTime (Date onlineTime) {
        this.onlineTime = onlineTime;
    }
    
    @Basic
    @Column(name = "IsOnlineFail")
    public String getIsOnlineFail() {
        return isOnlineFail;
    }

    public void setIsOnlineFail(String isOnlineFail) {
        this.isOnlineFail = isOnlineFail;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormInfoIncDetailEntity that = (FormInfoIncDetailEntity) o;
        return id == that.id && Objects.equals(formId, that.formId) && Objects.equals(summary, that.summary)
                && Objects.equals(content, that.content) && Objects.equals(cCategory, that.cCategory)
                && Objects.equals(cClass, that.cClass) && Objects.equals(cComponent, that.cComponent)
                && Objects.equals(sClass, that.sClass) && Objects.equals(sSubClass, that.sSubClass)
                && Objects.equals(eventClass, that.eventClass) && Objects.equals(eventType, that.eventType)
                && Objects.equals(eventSecurity, that.eventSecurity)
                && Objects.equals(systemBrand, that.systemBrand)
                && Objects.equals(systemId, that.systemId)
                && Objects.equals(eventPriority, that.eventPriority) && Objects.equals(system, that.system)
                && Objects.equals(assetGroup, that.assetGroup) && Objects.equals(effectScope, that.effectScope)
                && Objects.equals(urgentLevel, that.urgentLevel)
                && Objects.equals(countersigneds, that.countersigneds)
                && Objects.equals(effectScopeWording, that.effectScopeWording)
                && Objects.equals(urgentLevelWording, that.urgentLevelWording)
                && Objects.equals(eventPriorityWording, that.eventPriorityWording)
                && Objects.equals(this.getUpdatedBy(), this.getUpdatedBy())
                && Objects.equals(this.getUpdatedAt(), this.getUpdatedAt())
                && Objects.equals(this.getCreatedBy(), this.getCreatedBy())
                && Objects.equals(this.getCreatedAt(), this.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, formId, summary, content, cCategory, cClass, cComponent, sClass, sSubClass, eventClass,
                eventType, eventSecurity, eventPriority, system, assetGroup, effectScope, urgentLevel, countersigneds, systemBrand, systemId,
                this.getUpdatedBy(), this.getUpdatedAt(), this.getCreatedBy(), this.getCreatedAt(), effectScopeWording, urgentLevelWording, eventPriorityWording);
    }
    
}

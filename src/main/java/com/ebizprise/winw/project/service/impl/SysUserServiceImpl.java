package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.LdapUserEntity;
import com.ebizprise.winw.project.entity.SysGroupEntity;
import com.ebizprise.winw.project.entity.SysUserLogEntity;
import com.ebizprise.winw.project.enums.LDAPGroupEnum;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.enums.form.FormJobEnum;
import com.ebizprise.winw.project.jdbc.LdapUserJDBC;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.jdbc.criteria.SQL;
import com.ebizprise.winw.project.repository.ILdapUserRepository;
import com.ebizprise.winw.project.repository.ISysGroupRepository;
import com.ebizprise.winw.project.repository.ISysUserLogRepository;
import com.ebizprise.winw.project.service.ISysUserService;
import com.ebizprise.winw.project.service.startup.FormProcessHelper;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.BaseFormProcessManagmentDetailVO;
import com.ebizprise.winw.project.vo.GroupFunctionVO;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.LdapUserVO;
import com.ebizprise.winw.project.vo.LogonRecordVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;
import com.ebizprise.winw.project.xml.vo.UserIdVO;

@Transactional
@Service("sysUserService")
public class SysUserServiceImpl extends BaseService implements ISysUserService {
    
    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private LdapUserJDBC ldapUserJDBC;

    @Autowired
    private ILdapUserRepository ldapUserRepository;

    @Autowired
    private ISysGroupRepository sysGroupRepository;

    @Autowired
    private ISysUserLogRepository sysUserLogRepository;

    @Autowired
    private FormProcessHelper processHelper;
    
    /**
     * 系統管理者
     * @param groupId
     * @return
     * @author adam.yeh
     */
    public boolean isAdmin () {
        return UserEnum.ADMIN.wording().equalsIgnoreCase(fetchLoginUser().getUserId());
    }
    
    /**
     * 經辦
     * @param groupId
     * @return
     * @author adam.yeh
     */
    public boolean isPic (String groupId) {
        if (StringUtils.isBlank(groupId)) {
            groupId = fetchLoginUser().getGroupId();
        }
        
        return UserEnum.PIC.name().equals(
                UserInfoUtil.getUserTitleCode(getTitleCode(groupId)));
    }

    /**
     * 副科長
     * @param groupId
     * @return
     * @author adam.yeh
     */
    public boolean isVice (String groupId) {
        if (StringUtils.isBlank(groupId)) {
            groupId = fetchLoginUser().getGroupId();
        }
        
        return UserEnum.VSC.name().equals(
                UserInfoUtil.getUserTitleCode(getTitleCode(groupId)));
    }

    /**
     * 科長
     * @param groupId
     * @return
     * @author adam.yeh
     */
    public boolean isChief (String groupId) {
        if (StringUtils.isBlank(groupId)) {
            groupId = fetchLoginUser().getGroupId();
        }
        
        return UserEnum.SC.name().equals(
                UserInfoUtil.getUserTitleCode(getTitleCode(groupId)));
    }

    /**
     * 副理
     * @param groupId
     * @return
     * @author adam.yeh
     */
    public boolean isDirect1 (String groupId) {
        if (StringUtils.isBlank(groupId)) {
            groupId = fetchLoginUser().getGroupId();
        }
        
        return UserEnum.DIRECT1.name().equals(
                UserInfoUtil.getUserTitleCode(getTitleCode(groupId)));
    }

    /**
     * 協理
     * @param groupId
     * @return
     * @author adam.yeh
     */
    public boolean isDirect2 (String groupId) {
        if (StringUtils.isBlank(groupId)) {
            groupId = fetchLoginUser().getGroupId();
        }
        
        return UserEnum.DIRECT2.name().equals(
                UserInfoUtil.getUserTitleCode(getTitleCode(groupId)));
    }

    @Override
    public void saveUser(LdapUserEntity ldapUserEntity) {
        ldapUserRepository.save(ldapUserEntity);
    }

    @Override
    public void saveUserLog(SysUserLogEntity sysUserLogEntity) {
        sysUserLogRepository.save(sysUserLogEntity);
    }

    @Override
    public Map<String,String> getLdapUser(){
        Map<String,String> map = new HashMap<String,String>();
        List<LdapUserEntity> ldapUser=ldapUserRepository.findAll();
        for(LdapUserEntity l:ldapUser) {
            if(map.containsKey(l.getUserId())) {
                continue;
            }else {
                map.put(l.getUserId(), l.getName());
            }
        }
        return map;
    }

    /**
     * 主要處理從 WebService 取得的 xml 解析後將該 VO 轉存為資料庫的物件
     *
     * @param userIdVOList
     */
    @Override
    public void saveUsersFromWebService(List<UserIdVO> userIdVOList) {
        // 在更新使用者資訊前先將 enabled 狀態設為 0 關閉狀態，如 LDAP 同步欄設定為N則忽略
        // 如果LDAP中還存在該帳號才會再開啟
        ldapUserRepository.updateAllUsersEnableValue(StringConstant.SHORT_NO, StringConstant.SHORT_YES);

        // 當資料庫查無該使用者時會新增該使用者資料否則為更新使用者資料
        if (CollectionUtils.isNotEmpty(userIdVOList)) {
            for (UserIdVO userIdVO : userIdVOList) {
                LdapUserEntity ldapUserEntity = ldapUserRepository.findByUserId(userIdVO.getuId());
                if (Objects.isNull(ldapUserEntity)) {
                    ldapUserEntity = new LdapUserEntity();
                    ldapUserEntity.setUserId(userIdVO.getuId());
                }
                ldapUserEntity.setName(userIdVO.getDescription());
                ldapUserEntity.setTitle(userIdVO.getTitle());
                ldapUserEntity.setEmail(userIdVO.getMail());
                ldapUserEntity.setLdapOu(userIdVO.getOu());
                ldapUserEntity.setIsEnabled(StringConstant.SHORT_YES);
                ldapUserEntity.setGroups(userIdVO.getMemberOf());
                ldapUserRepository.save(ldapUserEntity);
            }
        }
    }

    /**
     * 處理從 LDAP 同步的使用者資料
     *
     * @param tempUserList
     */
    @Override
    public void saveUsersFromLDAP (
            List<LdapUserEntity> tempUserList, 
            List<Map<String, Object>> modifyUserList) throws Exception {
        // 在更新使用者資訊前先將 IsEnabled 狀態設為 N 關閉狀態，如 LDAP 同步欄設定為N則忽略
        // 如果LDAP中還存在該帳號才會再開啟
        ldapUserRepository.updateAllUsersEnableValue(StringConstant.SHORT_NO, StringConstant.SHORT_YES);
        // 將資料庫所有使用者取出，並再跟LDAP上的使用者作比對
        List<LdapUserEntity> existUserList = ldapUserRepository.findAll();
        Map<String, LdapUserEntity> existUserMap = new HashMap<>();
        for (LdapUserEntity existUser : existUserList) {
            existUserMap.put(existUser.getUserId(), existUser);
        }
        existUserList.clear();

        // 當資料庫查無該使用者時會新增該使用者資料否則為更新使用者資料
        if (CollectionUtils.isNotEmpty(tempUserList)) {
            List<LdapUserEntity> newUserData = new ArrayList<>();
            List<SysGroupEntity> sysGroupList = sysGroupRepository.findAll();
            LdapUserEntity updateUser;
            Date today = new Date();
            String memberOf, groupId;
            String sysGroupId = null;

            for (LdapUserEntity tempUser : tempUserList) {
                for (SysGroupEntity sysGroup : sysGroupList) {
                    try {
                        // 先以使用者 OU 查詢 SYS_GROUP 對應資料
                        // 以使用者的 title 為判斷依據如"辦公室自動化科副科長"、"一等專案襄理兼辦公室自動化科科長"、"一等專員"
                        // 再依照判斷放入正確的系統群組ID，專員一律放進"經辦"群組中
                        // 確認使用者的部門代碼是否符合系統群組部門代碼
                        // 當使用者為副理或協理時，忽略比對，因他們沒有系統群組科別
                        if (StringUtils.isBlank(sysGroup.getDepartmentId()) || 
                                !tempUser.getLdapDn().contains(sysGroup.getDepartmentId())) {
                            continue;
                        } else if (StringUtils.isBlank(tempUser.getTitle())) { // 無頭銜
                            continue;
                        } else if (!tempUser.getLdapOu().equals(sysGroup.getDivision())) {// 比對科別
                            continue;
                        }

                        groupId = sysGroup.getGroupId();
                        memberOf = tempUser.getIsSynced();// 完整群組資料

                        // 分配角色
                        if (StringUtils.isNotBlank(memberOf)) {
                            if (isDirect2(memberOf, groupId)) {
                                sysGroupId = String.valueOf(sysGroup.getSysGroupId());
                            } else if (isDirect1(memberOf, groupId)) {
                                sysGroupId = String.valueOf(sysGroup.getSysGroupId());
                            } else if (isChief(memberOf, groupId)) {
                                sysGroupId = String.valueOf(sysGroup.getSysGroupId());
                            } else if (isVice(memberOf, groupId)) {
                                sysGroupId = String.valueOf(sysGroup.getSysGroupId());
                            } else if (isPic(memberOf, groupId)) {
                                sysGroupId = String.valueOf(sysGroup.getSysGroupId());
                            } else {
                                sysGroupId = null;
                            }
                        } else {
                            logger.warn("群組叢集為空 : " + BeanUtil.toJson(tempUser));
                        }
                        
                        if (StringUtils.isBlank(sysGroupId)) {
                            logger.warn("找不到系統群組編號 : " + BeanUtil.toJson(tempUser));
                        } else {
                            if (!existUserMap.containsKey(tempUser.getUserId())) {
                                updateUser = new LdapUserEntity();
                                newUserData.add(updateUser);
                                updateUser.setCreatedAt(today);
                                updateUser.setUserId(tempUser.getUserId());
                                updateUser.setCreatedBy(UserEnum.SYSTEM.wording());
                            } else if (existUserMap.get(tempUser.getUserId())
                                    .getIsSynced().equals(StringConstant.SHORT_NO)) { // 如使用者 ldap 同步狀態為 N 則不更新
                                continue;
                            } else {
                                updateUser = existUserMap.get(tempUser.getUserId());
                                updateUser.setUpdatedAt(today);
                                updateUser.setUpdatedBy(UserEnum.SYSTEM.wording());
                            }
                            
                            updateUser.setSysGroupId(sysGroupId);
                            updateUser.setLdapOu(tempUser.getLdapOu());
                            updateUser.setLdapDn(tempUser.getLdapDn());
                            updateUser.setGroups(tempUser.getGroups());
                            updateUser.setTitle(tempUser.getTitle());
                            updateUser.setEmail(tempUser.getEmail());
                            updateUser.setName(tempUser.getName());
                            updateUser.setAuthorLevel(tempUser.getAuthorLevel());
                            updateUser.setIsSynced(StringConstant.SHORT_YES);
                            updateUser.setIsEnabled(StringConstant.SHORT_YES);

                            // 目前 SP 科會需要以使用者的群組判斷並加入至 SubGroup
                            if (tempUser.getGroups().contains(LDAPGroupEnum.SP_IMS.key)) {
                                updateUser.setSubGroup(LDAPGroupEnum.SP_IMS.key);
                            } else if (tempUser.getGroups().contains(LDAPGroupEnum.SP_DC.key)) {
                                updateUser.setSubGroup(LDAPGroupEnum.SP_DC.key);
                            } else if (tempUser.getGroups().contains(LDAPGroupEnum.SP_MQ.key)) {
                                updateUser.setSubGroup(LDAPGroupEnum.SP_MQ.key);
                            } else if (tempUser.getGroups().contains(LDAPGroupEnum.SP_MVS.key)) {
                                updateUser.setSubGroup(LDAPGroupEnum.SP_MVS.key);
                            } else if (tempUser.getGroups().contains(LDAPGroupEnum.SP_NT.key)) {
                                updateUser.setSubGroup(LDAPGroupEnum.SP_NT.key);
                            } else if (tempUser.getGroups().contains(LDAPGroupEnum.SP_OP.key)) {
                                updateUser.setSubGroup(LDAPGroupEnum.SP_OP.key);
                            } else if (tempUser.getGroups().contains(LDAPGroupEnum.SP_R6.key)) {
                                updateUser.setSubGroup(LDAPGroupEnum.SP_R6.key);
                            } else if (tempUser.getGroups().contains(LDAPGroupEnum.SP_AS400MVS.key)) {
                                updateUser.setSubGroup(LDAPGroupEnum.SP_AS400MVS.key);
                            }
                            
                            // 記錄在SCHEDULE_JOB_LOG.Message
                            Map<String, Object> modifyUserMap = new HashMap<>();
                            modifyUserMap.put("memberOf", memberOf);
                            modifyUserMap.put("name", updateUser.getName());
                            modifyUserMap.put("title", updateUser.getTitle());
                            modifyUserMap.put("email", updateUser.getEmail());
                            modifyUserMap.put("userId", updateUser.getUserId());
                            modifyUserMap.put("subGroup", updateUser.getSubGroup());
                            modifyUserList.add(modifyUserMap);
                        }
                    } catch (Exception e) {
                        logger.error(this.getMessage("ldap.users.error", new String[]{tempUser.getUserId()}));
                        logger.error(tempUser.toString());
                        throw e;
                    }
                }
            }
            
            ldapUserRepository.saveAll(newUserData);
            sysGroupList.clear();
        }
    }

    public GroupFunctionVO getLoginUserGroupFunction() {
    	String groupId = fetchLoginUser().getGroupId();
        return getGroupFunction(groupId);
    }
    
    public GroupFunctionVO getGroupFunction(String groupId) {
    	GroupFunctionVO groupFunc = new GroupFunctionVO();
    	SysGroupEntity sysGroup = sysGroupRepository.findByGroupId(groupId);
    	BeanUtil.copyProperties(sysGroup, groupFunc);
    	return groupFunc;
    }
    
    @Override
    public List<LogonRecordVO> getInternalProcessEmployee() {
        ResourceEnum resource = ResourceEnum.SQL_LOGON_RECORD.getResource("FIND_USERS");
        Conditions condition = new Conditions();
        condition.and().equal("LU.IsEnabled", StringConstant.SHORT_YES);
        condition.and().leftPT().equal("SG.Division", FormJobEnum.DC.name());
        condition.or().equal("LU.SubGroup", "SP_OP").RightPT();
        List<LogonRecordVO> empList = jdbcRepository.queryForList(resource, condition, LogonRecordVO.class);
        return empList;
    }    
    
    @Override
    public LdapUserEntity findUserByUserId(String userId) {
        return ldapUserRepository.findByUserId(userId);
    }

    @Override
    public SysUserLogEntity findUserLogLastRecord(String userId, String status) {
        return sysUserLogRepository.findTop1ByUserIdAndStatusOrderByTimeDesc(userId, status);
    }

    /**
     * 搜尋所有登出/登入的紀錄
     *
     * @return
     * @author willy.peng
     */
    @Override
    public List<LogonRecordVO> findAllUserLogs() {
        ResourceEnum resource = ResourceEnum.SQL_LOGON_RECORD.getResource("FIND_ALL_RECORDS");
        List<LogonRecordVO> detailLists = ldapUserJDBC.queryForList(resource, LogonRecordVO.class);

        return detailLists;
    }

    /**
     * 模糊搜尋登出/登入的紀錄
     *
     * @return
     * @author willy.peng
     */
    @Override
    public List<LogonRecordVO> findUserLogsByCondition(LogonRecordVO queryVo) {
        ResourceEnum resource = ResourceEnum.SQL_LOGON_RECORD.getResource("FIND_LOGON_RECORDS_BY_CONDITION");
        Conditions conditions = new Conditions();

        if (StringUtils.isNotBlank(queryVo.getUserName())) {
            conditions.and().like("LUSER.Name", queryVo.getUserName());
        }
        if (StringUtils.isNotBlank(queryVo.getStatus())) {
            conditions.and().equal("SLOG.Status", queryVo.getStatus());
        }
        if (StringUtils.isNotBlank(queryVo.getQueryStartDate())) {
            conditions.and().gtEqual("CONVERT(date, SLOG.Time, 111)", queryVo.getQueryStartDate());
        }
        if (StringUtils.isNotBlank(queryVo.getQueryEndDate())) {
            conditions.and().ltEqual("CONVERT(date, SLOG.Time, 111)", queryVo.getQueryEndDate());
        }
        
        conditions.and().unEqual("SLOG.Status", "TIMEOUT");
        conditions.orderBy("SLOG.Time", SQL.DESC);

        List<LogonRecordVO> logonRecords = ldapUserJDBC.queryForList(resource, conditions, LogonRecordVO.class);

        return logonRecords;
    }

    /**
     * 用自訂群組ID查詢群組成員
     *
     * @param sysGroupId
     * @author willy.peng
     */
    @Override
    public List<LdapUserVO> getGroupUsers (Long sysGroupId) {
        return BeanUtil.copyList(
                ldapUserRepository.findBySysGroupId(Long.toString(sysGroupId)), LdapUserVO.class);
    }

    @Override
    public List<LdapUserVO> getEmployeeOfProcess (BaseFormProcessManagmentDetailVO vo, String workProject) {
        ResourceEnum resource = ResourceEnum.SQL_FORM_PROCESS_MANAGMENT.getResource("FIND_FORM_PROCESS_ENABLE_EMPLOYEES");
        Conditions condition = genEmployeeProcessConditions(vo, workProject, false);
        List<LdapUserVO> empList = jdbcRepository.queryForList(resource, condition, LdapUserVO.class);
        
        if (CollectionUtils.isEmpty(empList)) {
            condition = genEmployeeProcessConditions(vo, workProject, true);
            empList = jdbcRepository.queryForList(resource, condition, LdapUserVO.class);
        }

        return empList;
    }

    @Override
    public LogonRecordVO getActiviatedUser (String userId) {
        LdapUserEntity pojo = ldapUserRepository.findByUserIdAndIsEnabled(userId, StringConstant.SHORT_YES);
        LogonRecordVO vo = new LogonRecordVO();
        BeanUtil.copyProperties(pojo, vo);

        return vo;
    }

    @Override
    public List<LogonRecordVO> findUsers (HtmlVO vo) {
        Conditions conditions = new Conditions();
        ResourceEnum resource = ResourceEnum.SQL_LOGON_RECORD.getResource("FIND_USERS");

        if (vo.getIsAll()) {
            conditions.and().equal("LU.isEnabled", StringConstant.SHORT_YES);
        } 
        
        if (StringUtils.isNotEmpty(vo.getGroupId())) {
            conditions.and().equal("SG.GroupId", vo.getGroupId());
        } else if (CollectionUtils.isNotEmpty(vo.getGroupIdList())) {
            conditions.and().in("SG.GroupId", vo.getGroupIdList());
        } else if (StringUtils.isNotBlank(vo.getSubGroup())) {
            conditions.and().equal("LU.SubGroup", vo.getSubGroup());
        }

        return jdbcRepository.queryForList(resource, conditions, LogonRecordVO.class);
    }

    @Override
    public SysUserVO getLoginUserInfo (String userId) {
        return fetchLoginUser();
    }
    
    @Override
    @Deprecated
    public void saveUsersFromLDAP (List<LdapUserEntity> ldapUserEntityList) throws Exception {
    }

    // 經辦
    private boolean isPic (String memberOf, String groupId) {
        return StringUtils.contains(memberOf, UserEnum.USR.symbol()) &&
                !StringUtils.endsWith(groupId, UserEnum.MGR4.wording()) &&
                !StringUtils.endsWith(groupId, UserEnum.MGR3.wording()) &&
                !StringUtils.endsWith(groupId, UserEnum.MGR2.wording()) &&
                !StringUtils.endsWith(groupId, UserEnum.MGR1.wording());
    }

    // 副科
    private boolean isVice (String memberOf, String groupId) {
        return StringUtils.contains(memberOf, UserEnum.MGR1.symbol()) && 
                StringUtils.endsWith(groupId, UserEnum.MGR1.wording());
    }

    // 科長
    private boolean isChief (String memberOf, String groupId) {
        return StringUtils.contains(memberOf, UserEnum.MGR2.symbol()) && 
                StringUtils.endsWith(groupId, UserEnum.MGR2.wording()) && 
                !StringUtils.endsWith(groupId, UserEnum.MGR1.wording());
    }

    // 副理
    private boolean isDirect1 (String memberOf, String groupId) {
        return StringUtils.contains(memberOf, UserEnum.MGR3.symbol()) && 
                StringUtils.endsWith(groupId, UserEnum.MGR3.wording());
    }

    // 協理
    private boolean isDirect2 (String memberOf, String groupId) {
        return StringUtils.contains(memberOf, UserEnum.MGR4.symbol()) && 
                StringUtils.endsWith(groupId, UserEnum.MGR4.wording());
    }
    
    private SysUserVO getTitleCode (String groupId) {
        SysUserVO vo = new SysUserVO();
        vo.setGroupId(groupId);
        vo.setDivision(MapUtils.getString(
                processHelper.getGIdmDivision(), groupId, ""));
        
        return vo;
    }

    private Conditions genEmployeeProcessConditions (
            BaseFormProcessManagmentDetailVO vo, String workProject, boolean isAll) {
        Conditions condition = new Conditions();
        
        condition.and().equal("f.FormId", vo.getFormId())
                 .and().equal("u.IsEnabled", StringConstant.SHORT_YES)
                 .and().equal("j.ProcessOrder", String.valueOf(vo.getProcessOrder()));
        
        if(StringUtils.isNotBlank(workProject) && !isAll) {
            condition.and().likeEnded("u.SubGroup", workProject);
        }
        
        return condition;
    }

}

package com.ebizprise.winw.project.service;

import java.util.List;
import java.util.Map;

import com.ebizprise.winw.project.entity.LdapUserEntity;
import com.ebizprise.winw.project.entity.SysUserLogEntity;
import com.ebizprise.winw.project.vo.BaseFormProcessManagmentDetailVO;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.LdapUserVO;
import com.ebizprise.winw.project.vo.LogonRecordVO;
import com.ebizprise.winw.project.xml.vo.SysUserVO;
import com.ebizprise.winw.project.xml.vo.UserIdVO;

public interface ISysUserService {

	public void saveUser(LdapUserEntity ldapUserEntity);

	public void saveUserLog(SysUserLogEntity sysUserLogEntity);

	public void saveUsersFromWebService(List<UserIdVO> userIdVOList);

	public void saveUsersFromLDAP(List<LdapUserEntity> ldapUserEntityList) throws Exception;

    public void saveUsersFromLDAP(List<LdapUserEntity> ldapUserEntityList, List<Map<String, Object>> modifyUserList) throws Exception;

	public LdapUserEntity findUserByUserId(String userId);

	public SysUserLogEntity findUserLogLastRecord(String userId, String status);

	public List<LogonRecordVO> findAllUserLogs();

    public List<LogonRecordVO> findUserLogsByCondition(LogonRecordVO detail);

    /**
     * 用群組代號取得群組人員
     * 
     * @param sysGroupId
     */
    public List<LdapUserVO> getGroupUsers(Long sysGroupId);
    
    /**
     * 取得流程群組關卡啟用中的人員
     */
    public List<LdapUserVO> getEmployeeOfProcess(BaseFormProcessManagmentDetailVO vo,String workProject);
    
    public Map<String,String> getLdapUser();

    /**
     * 找已啟用的使用者
     * 
     * @param userId
     * @return
     * @author adam.yeh
     */
    public LogonRecordVO getActiviatedUser(String userId);

    /**
     * 找使用者清單
     * @return
     * @author adam.yeh
     * @param htmlVO
     */
	public List<LogonRecordVO> findUsers (HtmlVO htmlVO);

	/**
	 * 找當前登入者資訊
	 * 
	 * @param userId
	 * @return
	 * @author adam.yeh
	 */
    public SysUserVO getLoginUserInfo (String userId);
    
    /**
     * 取得內會流程關卡啟用中的人員
     * @return
     */
    public List<LogonRecordVO> getInternalProcessEmployee();
    
}

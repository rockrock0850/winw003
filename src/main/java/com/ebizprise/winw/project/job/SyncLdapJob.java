package com.ebizprise.winw.project.job;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.naming.directory.Attributes;

import com.ebizprise.project.utility.net.LdapUtil;

/**
 * @author gary.tsai 2019/5/29
 */
public interface SyncLdapJob {

	public void saveDataFromWebService() throws Exception;

	public void saveDataFromLDAP() throws Exception;

    public void saveDataFromLDAP (List<Map<String, Object>> modifyUserList) throws Exception;

	/**
	 * 連線到 LDAP 並取得屬性
	 * 
	 * @param userName
	 * @param password
	 * @param ldapDomain
	 * @param ldapPort
	 * @param ldapDn
	 * @param objetClassType
	 * @return
	 * @throws Exception
	 */
	default List<Attributes> getAttrsFromLdap(String userName, String password, String ldapDomain, String ldapPort,
			String ldapDn, String objetClassType) throws Exception {
		LdapUtil ldapUtil = null;
		List<Attributes> attributesList;
		try {
			ldapUtil = new LdapUtil(userName, password, ldapDomain, ldapPort, ldapDn);
			ldapUtil.getLdapContext();
			ldapUtil.loginLdap();
			attributesList = ldapUtil.getAllInfoByObjectClass(objetClassType);
		} catch (Exception e) {
			throw e;
		} finally {
			ldapUtil.closeConnection();
		}
		return attributesList;
	}

	/**
	 * 依LDAP的欄位屬性名稱取出，如: distinguishedName: CN=PWA User
	 * PWA使用者,OU=System,OU=eBizprise,DC=ebizprise,DC=corp 並分割字串並只取得內容值
	 *
	 * @param attrs
	 * @param attrName
	 * @return
	 */
	default String getAttrValue(Attributes attrs, String attrName) {
		return !Objects.isNull(attrs.get(attrName)) ? attrs.get(attrName).toString().split(":", 2)[1].trim() : null;
	}

}

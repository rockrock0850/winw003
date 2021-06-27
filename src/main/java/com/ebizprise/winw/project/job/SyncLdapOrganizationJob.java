package com.ebizprise.winw.project.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.directory.Attributes;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.project.utility.code.Base64Util;
import com.ebizprise.project.utility.code.CryptoUtil;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.entity.LdapOrganizationEntity;
import com.ebizprise.winw.project.enums.LDAPAttributeEnum;
import com.ebizprise.winw.project.service.ISysOrganizationService;

/**
 * 同步LDAP組織_排程
 */
public class SyncLdapOrganizationJob extends QuartzJobFactory implements SyncLdapJob {
	private static final Logger logger = LoggerFactory.getLogger(SyncLdapOrganizationJob.class);

	@Autowired
	private ISysOrganizationService sysOrganizationService;

	@Override
	public String executeJob(JobExecutionContext jobCtx) throws Exception {
		saveDataFromLDAP();
		return "";
	}

	@Override
	public void saveDataFromWebService() throws Exception {
		//目前組織取得沒有 web service 的方式
	}

	@Override
	public void saveDataFromLDAP() throws Exception {
		List<Attributes> attributesList;
		String userName = env.getProperty("ldap.default.user");
		String ldapPassword = env.getProperty("ldap.default.pa55word");
		// 從LDAP取得組織所有資訊
		try {
			// 如設定檔將編碼加密開啟，需解碼
			String encrypt = env.getProperty("encrypt.enabled");
			if(StringUtils.isNotBlank(encrypt) && encrypt.equalsIgnoreCase(StringConstant.TRUE)){
				CryptoUtil cryptoUtil = new CryptoUtil(new Base64Util());
				ldapPassword = cryptoUtil.decode(ldapPassword);
			}

			attributesList = getAttrsFromLdap(userName, ldapPassword,
					env.getProperty("ldap.domain"), env.getProperty("ldap.port"), env.getProperty("ldap.dn"), "organizationalUnit");
		} catch (Exception e) {
			logger.warn(getMessage("login.ldap.error.messages", new String[] { userName }));
			throw e;
		}
		List<LdapOrganizationEntity> ldapOrganizationEntityList = new ArrayList<>();
		String name;
		for (Attributes attrs : attributesList) {

			name = getAttrValue(attrs, LDAPAttributeEnum.name.name());
			if (StringUtils.isBlank(name)) {
				continue;
			}
			LdapOrganizationEntity ldapOrganizationEntity = new LdapOrganizationEntity();
			ldapOrganizationEntity.setOrgId(name);
			ldapOrganizationEntity.setName(getAttrValue(attrs, LDAPAttributeEnum.description.name()));
			ldapOrganizationEntity.setLdapDn(getAttrValue(attrs, LDAPAttributeEnum.distinguishedName.name()));
			ldapOrganizationEntity.setEnabled(StringConstant.SHORT_YES);
			ldapOrganizationEntityList.add(ldapOrganizationEntity);
		}
		sysOrganizationService.saveOrganizationFromLDAP(ldapOrganizationEntityList);
	}

    @Override
    @Deprecated
    public void saveDataFromLDAP (List<Map<String, Object>> modifyUserList) throws Exception {
    }
}

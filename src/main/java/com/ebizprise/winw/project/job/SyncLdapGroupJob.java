package com.ebizprise.winw.project.job;

import java.io.IOException;
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
import com.ebizprise.project.utility.doc.xml.JaxbUtil;
import com.ebizprise.project.utility.net.HttpUtility;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.entity.LdapGroupEntity;
import com.ebizprise.winw.project.enums.LDAPAttributeEnum;
import com.ebizprise.winw.project.service.ISysGroupService;
import com.ebizprise.winw.project.xml.vo.EIPADVO;

/**
 * 同步LDAP群組_排程
 */
public class SyncLdapGroupJob extends QuartzJobFactory implements SyncLdapJob {
    
	private static final Logger logger = LoggerFactory.getLogger(SyncLdapGroupJob.class);

	@Autowired
	private ISysGroupService sysGroupService;

	@Override
	public String executeJob(JobExecutionContext jobCtx) throws Exception {
		//　預設是以 LDAP 的方式取得群組
		if (env.getProperty("ldap.enabled").equalsIgnoreCase("true")) {
			saveDataFromLDAP();
		} else {
			saveDataFromWebService();
		}
		
		return "";
	}

	/**
	 * 由 web service 取得所有群組資訊 但合庫在2019年時預計淘汰
	 *
	 * @throws IOException
	 */
	@Override
    @Deprecated
	public void saveDataFromWebService() throws IOException {
		// 模擬透過 web service 取得 xml 內容
		String requestURL = "http://localhost:8080/ISWP/ldap/getAllGroup";
		StringBuilder xml = new StringBuilder();

		try {
			HttpUtility.sendGetRequest(requestURL);
			String[] response = HttpUtility.readMultipleLinesRespone();
			for (String line : response) {
				xml.append(line);
			}
		} catch (IOException ex) {
			throw ex;
		}
		try {
            JaxbUtil util = new JaxbUtil(EIPADVO.class);
            EIPADVO eipadVO = util.fromXml(xml.toString());
			logger.info(getMessage("ldap.groups.counting",
					new String[] {String.valueOf(eipadVO.getAccountVO().getUserIdVOList().size())}));
			sysGroupService.saveGroupsFromWebService(eipadVO.getAccountVO().getUserIdVOList());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void saveDataFromLDAP() throws Exception {
		List<Attributes> attributesList;
		String ldapDn = env.getProperty("ldap.dn");
		String ldapPort = env.getProperty("ldap.port");
		String ldapDomain = env.getProperty("ldap.domain");
		String encrypt = env.getProperty("encrypt.enabled");
		String ldapUser = env.getProperty("ldap.default.user");
		String ldapPassword = env.getProperty("ldap.default.pa55word");
		boolean isEncrypt = 
		        StringUtils.isNotBlank(encrypt) &&
		        encrypt.equalsIgnoreCase(StringConstant.TRUE);
		
		try {
            if (isEncrypt) {
				CryptoUtil cryptoUtil = new CryptoUtil(new Base64Util());
				ldapPassword = cryptoUtil.decode(ldapPassword);
			}

			attributesList = getAttrsFromLdap(ldapUser, ldapPassword, ldapDomain, ldapPort, ldapDn, "user");
		} catch (Exception e) {
			logger.warn(getMessage("login.ldap.error.messages", new String[] { ldapUser }));
			throw e;
		}
		
		List<LdapGroupEntity> ldapGroupEntitiesList = new ArrayList<>();
		
		for (Attributes attrs : attributesList) {
			LdapGroupEntity ldapGroupEntity = new LdapGroupEntity();
			ldapGroupEntity.setName(getAttrValue(attrs, LDAPAttributeEnum.description.name()));
			ldapGroupEntity.setLdapDn(getAttrValue(attrs, LDAPAttributeEnum.distinguishedName.name()));
			ldapGroupEntity.setEnabled(StringConstant.SHORT_YES);
			ldapGroupEntitiesList.add(ldapGroupEntity);
		}
		
		sysGroupService.saveGroupsFromLDAP(ldapGroupEntitiesList);
	}

    @Override
    @Deprecated
    public void saveDataFromLDAP (List<Map<String, Object>> modifyUserList) throws Exception {
    }

}
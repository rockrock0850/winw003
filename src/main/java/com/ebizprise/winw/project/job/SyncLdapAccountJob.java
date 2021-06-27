package com.ebizprise.winw.project.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.directory.Attributes;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.project.utility.code.Base64Util;
import com.ebizprise.project.utility.code.CryptoUtil;
import com.ebizprise.project.utility.doc.xml.JaxbUtil;
import com.ebizprise.project.utility.net.HttpUtility;
import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.entity.LdapUserEntity;
import com.ebizprise.winw.project.enums.LDAPAttributeEnum;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.service.ISysUserService;
import com.ebizprise.winw.project.xml.vo.EIPADVO;

/**
 * 同步LDAP帳號_排程
 */
public class SyncLdapAccountJob extends QuartzJobFactory implements SyncLdapJob {
    private static final Logger logger = LoggerFactory.getLogger(SyncLdapAccountJob.class);

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public String executeJob(JobExecutionContext jobCtx) throws Exception {
        List<Map<String, Object>> modifyUserList = new ArrayList<>();
        
        //　預設是以 LDAP 的方式取得帳號
        if (env.getProperty("ldap.enabled").equals("true")) {
            saveDataFromLDAP(modifyUserList);
        } else {
            saveDataFromWebService();
        }
        
        return BeanUtil.toJson(modifyUserList);
    }

    /**
     * 由 web service 取得所有帳號資訊 但合庫在2019年時預計淘汰
     *
     * @throws IOException
     */
    @Override
    @Deprecated
    public void saveDataFromWebService() throws IOException {
        // 模擬透過 web service 取得 xml 內容
        String requestURL = "http://localhost:8080/ISWP/ldap/getAllUser";
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
            logger.info(getMessage("ldap.users.counting",
                    new String[] { String.valueOf(eipadVO.getAccountVO().getUserIdVOList().size()) }));
            sysUserService.saveUsersFromWebService(eipadVO.getAccountVO().getUserIdVOList());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Deprecated
    public void saveDataFromLDAP () throws Exception {
    }

    @Override
    public void saveDataFromLDAP (List<Map<String, Object>> modifyUserList) throws Exception {
        long time1, time2, time3;
        time1 = System.currentTimeMillis();
        List<Attributes> attributesList;
        String userName = env.getProperty("ldap.default.user");
        String ldapPassword = env.getProperty("ldap.default.pa55word");
        
        // 從LDAP取得使用者所有資訊
        try {
            // 如設定檔將編碼加密開啟，需解碼
            String encrypt = env.getProperty("encrypt.enabled");
            if(StringUtils.isNotBlank(encrypt) && encrypt.equalsIgnoreCase(StringConstant.TRUE)){
                CryptoUtil cryptoUtil = new CryptoUtil(new Base64Util());
                ldapPassword = cryptoUtil.decode(ldapPassword);
            }

            attributesList = getAttrsFromLdap(userName, ldapPassword,
                    env.getProperty("ldap.domain"), env.getProperty("ldap.port"), env.getProperty("ldap.dn"), "user");
            logger.info("LDAP 使用者 共: " + attributesList.size() + " 人");
        } catch (Exception e) {
            logger.warn(getMessage("login.ldap.error.messages", new String[] { userName }));
            throw e;
        }
        
        String userId, groupName, memberOf;
        String[] dnSplit, ouSplit, memberOfSplit;
        StringBuilder groups = new StringBuilder();
        String ldapOu, authorization, distinguishedName;
        List<LdapUserEntity> tempUserList = new ArrayList<>();
        
        for (Attributes attrs : attributesList) {
            groups.setLength(0);
            userId = getAttrValue(attrs, LDAPAttributeEnum.sAMAccountName.name());
            if (StringUtils.isBlank(userId)) {
                continue;
            }
            
            LdapUserEntity tempUser = new LdapUserEntity();
            tempUser.setUserId(userId); // 使用者帳號名稱
            tempUser.setName(getAttrValue(attrs, LDAPAttributeEnum.displayName.name())); // 使用者中文名稱
            tempUser.setTitle(getAttrValue(attrs, LDAPAttributeEnum.title.name())); // 使用者職稱
            tempUser.setRocId(getAttrValue(attrs, LDAPAttributeEnum.ROCID.name())); // 使用者身份證字號
            tempUser.setEmail(getAttrValue(attrs, LDAPAttributeEnum.mail.name())); // 使用者電子郵件帳號
            distinguishedName = getAttrValue(attrs, LDAPAttributeEnum.distinguishedName.name()); // 使用者組織DN
            tempUser.setLdapDn(distinguishedName);
            logger.info("LDAP distinguishedName : " + distinguishedName);
            memberOf = getAttrValue(attrs, LDAPAttributeEnum.memberOf.name());
            logger.info("LDAP memberOf : " + memberOf);

            // 使用者組織代號
            dnSplit = distinguishedName.split(StringConstant.COMMA, 2);
            ouSplit = dnSplit[1].split(StringConstant.COMMA);
            ldapOu = ouSplit[0].replaceAll("OU=", "");
            
            // 資管科判斷科別的方式跟其他科不一樣
            if (isDc(ldapOu)) {
                if (isDcDirect(memberOf)) {
                    ldapOu = "Manager";
                } else if (isDcDivision(memberOf)) {
                    ldapOu = memberOf.substring(memberOf.indexOf(UserEnum.DC.symbol()));
                    ldapOu = ldapOu.split(",")[0];
                    ldapOu = ldapOu.split("-")[1];
                }
            }
            
            tempUser.setLdapOu(ldapOu);
            
            if (StringUtils.isNotBlank(memberOf)) {
                if (StringUtils.contains(memberOf, UserEnum.USR.symbol())) {
                    authorization = "1";
                } else {
                    authorization = "2";
                }

                tempUser.setAuthorLevel(authorization);
                
                memberOfSplit = memberOf.split(StringConstant.COMMA);
                for (String str : memberOfSplit) {
                    if (str.trim().startsWith("CN=")) {
                        groupName = str.replace("CN=", "").trim();
                        groups.append(groupName).append(StringConstant.COMMA);
                    }
                }
            }

            tempUser.setGroups(groups.toString().replaceAll(",$", "")); // 使用者隸屬群組
            tempUser.setIsEnabled(StringConstant.SHORT_YES); // 開啟使用者
            tempUser.setIsSynced(memberOf);// 暫時存放完整群組資料
            tempUserList.add(tempUser);
        }
        time2 = System.currentTimeMillis();
        logger.info("LDAP抓取資料並比對花了：" + (time2-time1)/1000 + "秒");
        sysUserService.saveUsersFromLDAP(tempUserList, modifyUserList);
        time3 = System.currentTimeMillis();
        logger.info("新增並更新資料並比對花了：" + (time3-time2)/1000 + "秒");
    }

    private boolean isDcDirect (String memberOf) {
        return StringUtils.contains(memberOf, UserEnum.MGR3.symbol()) ||
                StringUtils.contains(memberOf, UserEnum.MGR4.symbol());
    }

    private boolean isDcDivision (String memberOf) {
        return StringUtils.contains(memberOf, UserEnum.DC.symbol());
    }

    private boolean isDc (String ldapOu) {
        return UserEnum.DC.wording().equalsIgnoreCase(ldapOu);
    }

}

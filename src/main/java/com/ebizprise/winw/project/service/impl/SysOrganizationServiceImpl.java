package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.project.utility.str.StringConstant;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.LdapOrganizationEntity;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.repository.ILdapOrganizationRepository;
import com.ebizprise.winw.project.service.ISysOrganizationService;

@Transactional
@Service("sysOrganizationService")
public class SysOrganizationServiceImpl extends BaseService implements ISysOrganizationService {
	private static final Logger logger = LoggerFactory.getLogger(SysOrganizationServiceImpl.class);

	@Autowired
	private ILdapOrganizationRepository ldapOrganizationRepository;

	@Override
	public void saveOrganizationFromLDAP(List<LdapOrganizationEntity> ldapOrganizationEntityList) {
		// 在更新組織資訊前先將 enabled 狀態設為 N 關閉狀態
		// 如果LDAP中還存在該組織才會再開啟
		ldapOrganizationRepository.updateAllOrganizationEnableValue(StringConstant.SHORT_NO);
		LdapOrganizationEntity errorRecord = new LdapOrganizationEntity();
		try {
			// 當資料庫查無該組織時會新增該組織資料否則為更新組織資料
			if (CollectionUtils.isNotEmpty(ldapOrganizationEntityList)) {
				LdapOrganizationEntity dbLdapOrganizationEntity;
				List<LdapOrganizationEntity> toDbLdapOrganazationEntityList = new ArrayList<>();
				String[] dnSplit;
				for (LdapOrganizationEntity ldapOrganizationEntity : ldapOrganizationEntityList) {
					errorRecord = ldapOrganizationEntity;
					dbLdapOrganizationEntity = ldapOrganizationRepository.findByOrgId(ldapOrganizationEntity.getOrgId());
					if (Objects.isNull(dbLdapOrganizationEntity)) {
						dbLdapOrganizationEntity = new LdapOrganizationEntity();
						dbLdapOrganizationEntity.setOrgId(ldapOrganizationEntity.getOrgId());
						dbLdapOrganizationEntity.setCreatedBy(UserEnum.SYSTEM.wording());
						dbLdapOrganizationEntity.setCreatedAt(new Date());
						toDbLdapOrganazationEntityList.add(dbLdapOrganizationEntity);
					} else {
						dbLdapOrganizationEntity.setUpdatedBy(UserEnum.SYSTEM.wording());
						dbLdapOrganizationEntity.setUpdatedAt(new Date());
					}

					// 將組織DN拆解
					// 如:CN=OA,OU=A01419,OU=eBizprise,DC=ebizprise,DC=corp
					dnSplit = ldapOrganizationEntity.getLdapDn().split(",");

					// 該組織所在的單位
					if (dnSplit[0].startsWith("OU=")) {
						dbLdapOrganizationEntity.setLdapOu(dnSplit[0]);
					}

					// 該組織所在的上一層單位
					if (dnSplit[1].startsWith("OU=")) {
						dbLdapOrganizationEntity.setUpperOu(dnSplit[1]);
					}
					dbLdapOrganizationEntity.setLdapDn(ldapOrganizationEntity.getLdapDn());
					dbLdapOrganizationEntity.setName(ldapOrganizationEntity.getName());
					dbLdapOrganizationEntity.setEnabled(StringConstant.SHORT_YES);
				}
				ldapOrganizationRepository.saveAll(toDbLdapOrganazationEntityList);
			}
		} catch (Exception e) {
			logger.error("錯誤的組織資料內容:" + errorRecord.toString());
		}

	}

	@Override
	public LdapOrganizationEntity findByOrgId(String orgId) {
		return ldapOrganizationRepository.findByOrgId(orgId);
	}
}

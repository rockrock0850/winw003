package com.ebizprise.winw.project.test.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.winw.project.entity.FormUserRecordEntity;
import com.ebizprise.winw.project.enums.UserEnum;
import com.ebizprise.winw.project.repository.IFormUserRecordRepository;
import com.ebizprise.winw.project.test.base.TestBase;

public class FromUserRecordRepositoryTest extends TestBase {

    @Autowired
    private IFormUserRecordRepository formUserRecordRepository;

    private String formId = "SR-000000003";

    @Ignore
    @Before
    public void testInsertData() throws InterruptedException {
        List<FormUserRecordEntity> formUserRecordEntityList = new ArrayList<>();
        FormUserRecordEntity formUserRecordEntity1 = new FormUserRecordEntity();
        formUserRecordEntity1.setFormId(formId);
        formUserRecordEntity1.setSummary("SR-000000003測試1");
        formUserRecordEntity1.setCreatedAt(new Date());
        formUserRecordEntity1.setCreatedBy(UserEnum.SYSTEM.wording());
        formUserRecordEntity1.setUpdatedAt(new Date());
        formUserRecordEntity1.setUpdatedBy(UserEnum.SYSTEM.wording());
        formUserRecordEntityList.add(formUserRecordEntity1);
        TimeUnit.SECONDS.sleep(5);
        FormUserRecordEntity formUserRecordEntity2 = new FormUserRecordEntity();
        formUserRecordEntity2.setFormId(formId);
        formUserRecordEntity2.setSummary("SR-000000003測試2");
        formUserRecordEntity2.setCreatedAt(new Date());
        formUserRecordEntity2.setCreatedBy(UserEnum.SYSTEM.wording());
        formUserRecordEntity2.setUpdatedAt(new Date());
        formUserRecordEntity2.setUpdatedBy(UserEnum.SYSTEM.wording());
        formUserRecordEntityList.add(formUserRecordEntity2);
        formUserRecordRepository.saveAll(formUserRecordEntityList);
    }

    @Ignore
    @Test
    public void testDeleteData() {
        formUserRecordRepository.deleteById(2L);
    }

    @Ignore
    @Test
    public void testFindAllByFormId() {
        List<FormUserRecordEntity> formUserRecordEntityList = formUserRecordRepository
                .findAllByFormIdOrderByUpdatedAtDesc("SR-000000003");
        Assert.assertNotNull(formUserRecordEntityList);
    }

    @Ignore
    @Test
    public void testFindById() {
        FormUserRecordEntity formUserRecordEntity = new FormUserRecordEntity();
        formUserRecordEntity.setId(1L);
        FormUserRecordEntity formUserRecordEntityList = formUserRecordRepository.findById(formUserRecordEntity.getId())
                .orElse(null);
        Assert.assertNotNull(formUserRecordEntityList);
    }

    @Test
    public void testCountByFormId() {
        Long count = formUserRecordRepository.countByFormId("SR-00000003");
        Assert.assertTrue(count > 0);
    }

}
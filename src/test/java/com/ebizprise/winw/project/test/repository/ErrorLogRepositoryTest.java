package com.ebizprise.winw.project.test.repository;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.winw.project.entity.ErrorLogEntity;
import com.ebizprise.winw.project.repository.IErrorLogRepository;
import com.ebizprise.winw.project.test.base.TestBase;

public class ErrorLogRepositoryTest extends TestBase {

    @Autowired
    private IErrorLogRepository errorLogRepository;

    @Test
    public void testFindAllByUpdatedAtBefore() {

        Date startDate = DateUtils.getDayStart(new Date());
        List<ErrorLogEntity> errorLogEntityList = errorLogRepository.findAllByTimeBefore(startDate);
        Assert.assertNotNull(errorLogEntityList);
    }

}
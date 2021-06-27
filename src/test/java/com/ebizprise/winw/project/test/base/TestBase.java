package com.ebizprise.winw.project.test.base;

import com.ebizprise.winw.project.config.QuartzConfig;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.winw.project.config.AppConfig;
import com.ebizprise.winw.project.config.DataStoreConfig;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { AppConfig.class, DataStoreConfig.class, QuartzConfig.class })
@WebAppConfiguration
@EnableTransactionManagement
@Rollback(false)
@Transactional
public class TestBase {

}

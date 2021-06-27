package com.ebizprise.winw.project.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement // 開啟事務支持
@Import(DataStoreConfig.class) // 導入數據源的配置
public class QuartzConfig {

	@Autowired
	DataSource dataSource;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Bean
	public SchedulerFactoryBean factoryBean() throws IOException {
		final SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setDataSource(dataSource);
		scheduler.setTransactionManager(transactionManager);
		scheduler.setOverwriteExistingJobs(true);
		scheduler.setQuartzProperties(quartzProperties());
		return scheduler;
	}

	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}
}
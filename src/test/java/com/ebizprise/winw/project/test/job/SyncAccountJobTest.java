package com.ebizprise.winw.project.test.job;

import java.io.IOException;

import org.junit.Test;

import com.ebizprise.project.utility.net.HttpUtility;
import com.ebizprise.winw.project.test.base.TestBase;

public class SyncAccountJobTest extends TestBase {

	@Test
	public void test() throws Exception {

		// test sending GET request
		String requestURL = "http://localhost:8080/ISWP/ldap/getAllUser";
		try {
			HttpUtility.sendGetRequest(requestURL);
			String[] response = HttpUtility.readMultipleLinesRespone();
			for (String line : response) {
				System.out.println(line);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		HttpUtility.disconnect();

		System.out.println("=====================================");

//		JobDetail jobDetail = JobBuilder.newJob(SyncLdapAccountJob.class).storeDurably(true).build();
//
//		Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).startNow().build();
//
//		scheduler.scheduleJob(jobDetail, trigger);
//
//		Thread.sleep(5000);
	}
	
}

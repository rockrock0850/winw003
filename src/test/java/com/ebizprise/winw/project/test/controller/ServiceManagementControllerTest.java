package com.ebizprise.winw.project.test.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.winw.project.config.AppConfig;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
@EnableTransactionManagement
@Rollback(false)
@Transactional
public class ServiceManagementControllerTest {
    private MockMvc mockMvc;
    
    @Test
    public void test_initPage() throws Exception {
        mockMvc.perform(get("/serviceTypeManagement/init"))
        .andExpect(status().isOk());
    }
}

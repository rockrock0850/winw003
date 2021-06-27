package com.ebizprise.winw.project.test.service;

import com.ebizprise.winw.project.service.IClearDataAndFilesService;
import com.ebizprise.winw.project.test.base.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author gary.tsai 2019/9/10
 */
public class ClearDataAndFilesServiceTest extends TestBase {

    @Autowired
    public IClearDataAndFilesService clearDataAndFilesService;

    @Test
    public void testClearProcess() throws Exception {
        clearDataAndFilesService.clearProcess();
    }
}

package com.ebizprise.winw.project.test.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.winw.project.entity.FormVerifyLogEntity;
import com.ebizprise.winw.project.enums.form.FormEnum;
import com.ebizprise.winw.project.repository.IFormVerifyLogRepository;
import com.ebizprise.winw.project.test.base.TestBase;

public class FormVerifyLogRepositoryTest extends TestBase {

    @Autowired
    private IFormVerifyLogRepository formVerifyLogRepository;

    @Ignore
    @Test
    public void testFindTop1ByFormIdAndVerifyResultOrderByUpdatedAtDesc() throws IOException {
        formVerifyLogRepository.findTop1ByFormIdAndVerifyResultOrderByUpdatedAtDesc("SR-00000015", "AGREED");
    }

    //	@Ignore
    @Test
    public void testFindTop1ByFormIdAndVerifyResultInOrderByUpdatedAtDesc() throws IOException {
        List<String> verifyResultList = new ArrayList<>();
        verifyResultList.add(FormEnum.AGREED.name());
        verifyResultList.add(FormEnum.SENT.name());
        FormVerifyLogEntity formVerifyLogEntity = formVerifyLogRepository.findTop1ByFormIdAndVerifyResultInOrderByUpdatedAtDesc("SR-00000015", verifyResultList);
		Assert.assertNotNull(formVerifyLogEntity);
    }

}
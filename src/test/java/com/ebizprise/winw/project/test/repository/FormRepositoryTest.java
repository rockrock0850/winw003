package com.ebizprise.winw.project.test.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebizprise.winw.project.entity.FormEntity;
import com.ebizprise.winw.project.repository.IFormRepository;
import com.ebizprise.winw.project.test.base.TestBase;

public class FormRepositoryTest extends TestBase {

    @Autowired
    private IFormRepository formRepository;

    @Test
    public void testFindTop1ByFormIdOrderByIdAsc(){
        FormEntity formEntity = formRepository.findTop1ByFormIdOrderByIdAsc("00000017");
        Assert.assertNotNull(formEntity);
    }

}
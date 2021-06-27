package com.ebizprise.winw.project.test.repository;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.ebizprise.project.utility.date.DateUtils;
import com.ebizprise.project.utility.trans.FileUtil;
import com.ebizprise.winw.project.entity.FormFileEntity;
import com.ebizprise.winw.project.repository.IFormFileRepository;
import com.ebizprise.winw.project.test.base.TestBase;

public class FromFileRepositoryTest extends TestBase {

	@Autowired
	private IFormFileRepository formFileRepository;

	@Autowired
	private Environment env;

	@Ignore
	@Test
	public void testSave() throws IOException {
		byte[] buffer = FileUtil.getByteFromFile("C:/temp/ITOA_ISMR_SCA_20181029.pdf");

		FormFileEntity formFileEntity = new FormFileEntity();
		formFileEntity.setFormId("test_form_id");
		formFileEntity.setName("ITOA_ISMR_SCA_20181029.pdf");
		formFileEntity.setDescription("測試上傳");
		formFileEntity.setData(buffer);
		formFileRepository.save(formFileEntity);
	}

//	@Ignore
	@Test
	public void testFindFormFileByName() throws IOException {
		FormFileEntity formFileEntity = formFileRepository.findByName("ITOA_ISMR_SCA_20181029.pdf");
//		File file = FileUtil.getFileFromByte(formFileEntity.getData(), "C:/temp/test.pdf");
//		Assert.assertTrue(file.exists());
		File file = new File(env.getProperty("form.file.download.dir") + File.separatorChar
				+ DateUtils.getCurrentDate(DateUtils._PATTERN_YYYYMMDD) + File.separatorChar + formFileEntity.getName());

		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		file = FileUtil.getFileFromByte(formFileEntity.getData(), file.getAbsolutePath());
		Assert.assertTrue(file.exists());
	}

	@Ignore
	@Test
	public void testDeleteFormFileByFormIdAndName() throws IOException {
		String formId = "test_form_id";
		String fileName = "CTBC103-模組對應清單.xlsx";
		formFileRepository.deleteByFormIdAndName(formId, fileName);
	}
}
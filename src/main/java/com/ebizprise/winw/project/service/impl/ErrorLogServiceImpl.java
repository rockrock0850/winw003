package com.ebizprise.winw.project.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebizprise.winw.project.entity.ErrorLogEntity;
import com.ebizprise.winw.project.repository.IErrorLogRepository;
import com.ebizprise.winw.project.service.IErrorLogService;

@Transactional
@Service("errorLogService")
public class ErrorLogServiceImpl implements IErrorLogService {

	@Autowired
	private IErrorLogRepository errorLogRepository;

	@Override
	public List<ErrorLogEntity> findAllJobs() {
		return errorLogRepository.findAll();
	}

	@Override
	public ErrorLogEntity findByServerIp(String serverIp) {
		return errorLogRepository.findByServerIp(serverIp);
	}

	@Override
	public void saveOrUpdate(ErrorLogEntity errorLogEntity) {
		errorLogRepository.save(errorLogEntity);
	}

	@Override
	public void delete(ErrorLogEntity errorLogEntity) {
		errorLogRepository.delete(errorLogEntity);
	}
}

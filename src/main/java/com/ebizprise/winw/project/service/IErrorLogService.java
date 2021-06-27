package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.entity.ErrorLogEntity;

public interface IErrorLogService {

	public List<ErrorLogEntity> findAllJobs();

	public ErrorLogEntity findByServerIp(String serverIp);

	public void saveOrUpdate(ErrorLogEntity errorLogEntity);

	public void delete(ErrorLogEntity errorLogEntity);

}

package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.vo.ScheduleJobVO;

/**
 * 系統排程 服務類別
 * 
 * @author gary.tsai, adam.yeh
 *
 */
public interface IScheduleJobService {

	public List<ScheduleJobVO> findAllJobs();

	public ScheduleJobVO findByJobName(ScheduleJobVO vo);

	public void saveOrUpdate(ScheduleJobVO jobVO);

	public void delete(ScheduleJobVO jobVO);

	/**
	 * 模糊搜尋排程
	 * 
	 * @param jobVO
	 * @return 
	 * @author adam.yeh
	 */
    public List<ScheduleJobVO> findJobs (ScheduleJobVO jobVO);

    /**
     * 搜尋排程執行歷程
     *  
     * @param jobVO
     * @return
     * @author adam.yeh
     */
    public List<ScheduleJobVO> findJobHistory (ScheduleJobVO jobVO);

    /**
     * 儲存/更新 排程設定
     * 
     * @param jobVO
     * @author adam.yeh
     */
    public void saveJob (ScheduleJobVO jobVO);

}

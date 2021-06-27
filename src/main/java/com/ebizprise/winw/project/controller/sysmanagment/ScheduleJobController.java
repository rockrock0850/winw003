package com.ebizprise.winw.project.controller.sysmanagment;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.enums.JobStatusEnum;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.job.BaseJob;
import com.ebizprise.winw.project.service.IScheduleJobService;
import com.ebizprise.winw.project.vo.ScheduleJobHistoryVO;
import com.ebizprise.winw.project.vo.ScheduleJobVO;

/**
 * 系統批次設定
 * 
 * @author gary.tsai, adam.yeh
 *
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleJobController extends BaseJob {

	@Autowired
	private IScheduleJobService scheduleJobService;

    @Override
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView initPage () {
        return new ModelAndView(DispatcherEnum.SCHEDULE_JOB.initPage());
    }

    /**
     * 編輯頁
     * 
     * @return
     * @author adam.yeh
     */
    @GetMapping(path = "/editPage/{jobName}")
    public ModelAndView editPage (ScheduleJobVO vo) {
        vo = scheduleJobService.findByJobName(vo);
        vo.setNextFireTime(scheduleJobService.findJobs(vo).get(0).getNextFireTime());
        
        return new ModelAndView(DispatcherEnum.SCHEDULE_JOB.editPage(), "scheduleVO", BeanUtil.toJson(vo));
    }

    /**
     * 搜尋
     * 
     * @param jobVO
     * @return
     * @author adam.yeh
     */
    @PostMapping(path = "/search")
    public List<ScheduleJobVO> search (@RequestBody ScheduleJobVO jobVO) {
        List<ScheduleJobVO> detailList = new ArrayList<>();
        
        if (StringUtils.isBlank(jobVO.getJobDescription())) {
            detailList = scheduleJobService.findAllJobs();
        } else {
            detailList = scheduleJobService.findJobs(jobVO);
        }
        
        return detailList;
    }

    /**
     * 歷史紀錄
     * 
     * @param jobVO
     * @return
     * @author adam.yeh
     */
    @PostMapping(path = "/history", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ScheduleJobHistoryVO> history (@RequestBody ScheduleJobVO jobVO) {
        List<ScheduleJobVO> historyList = scheduleJobService.findJobHistory(jobVO);
        ScheduleJobHistoryVO vo = new ScheduleJobHistoryVO();
        vo.setJobName(jobVO.getJobName());
        vo.setJobDescription(jobVO.getJobDescription());
        vo.setScheduleJobVOList(historyList);
        
        return ResponseEntity.ok(vo);
    }

    /**
     * 儲存
     * 
     * @param jobVO
     * @return
     * @author adam.yeh
     */
    @PostMapping(path = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ScheduleJobVO> save (@RequestBody ScheduleJobVO jobVO) {
        scheduleJobService.saveJob(jobVO);
        
        int status = jobVO.getStatus();
        if (JobStatusEnum.SUSPEND.status() == status) {
            this.suspendJobProcess(jobVO);
        } else {
            this.mergeJobProcess(jobVO);
        }

        return ResponseEntity.ok(jobVO);
    }

    /**
     * 註冊排程
     * 
     * @param jobVO
     * @return
     */
    @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ScheduleJobVO> register (@Valid @RequestBody ScheduleJobVO jobVO) {
		saveOrUpdateScheduleJobStatus(jobVO, JobStatusEnum.START.status());
		this.mergeJobProcess(jobVO);
        
		return ResponseEntity.ok(jobVO);
	}

    /**
     * 刪除排程
     * 
     * @param jobVO
     * @return
     */
	@PostMapping("/delete")
	public ResponseEntity<ScheduleJobVO> delete (@Valid @RequestBody ScheduleJobVO jobVO) {
		saveOrUpdateScheduleJobStatus(jobVO, JobStatusEnum.DELETE.status());
		this.deleteJobProcess(jobVO);
        
		return ResponseEntity.ok(jobVO);
	}

	/**
	 * 停用排程
	 * 
	 * @param jobVO
	 * @return
	 */
	@PostMapping("/stop")
	public ResponseEntity<ScheduleJobVO> stop(@Valid @RequestBody ScheduleJobVO jobVO) {
		saveOrUpdateScheduleJobStatus(jobVO, JobStatusEnum.SUSPEND.status());
		this.suspendJobProcess(jobVO);
        
		return ResponseEntity.ok(jobVO);
	}

    /**
     * 啟用排程一次
     * 
     * @param jobVO
     * @return
     */
    @PostMapping("/once")
    public ResponseEntity<ScheduleJobVO> once (@RequestBody ScheduleJobVO jobVO) {
        this.executeJobOnce(jobVO);
        return ResponseEntity.ok(jobVO);
    }

	/**
	 * 啟用排程
	 * 
	 * @param jobVO
	 * @return
	 */
	@PostMapping("/start")
	public ResponseEntity<ScheduleJobVO> start(@Valid @RequestBody ScheduleJobVO jobVO) {
		saveOrUpdateScheduleJobStatus(jobVO, JobStatusEnum.START.status());
		this.resumeJobProcess(jobVO);

		return ResponseEntity.ok(jobVO);
	}

	private ScheduleJobVO saveOrUpdateScheduleJobStatus (ScheduleJobVO jobVO, int status) {
		jobVO.setStatus(status);
		ScheduleJobVO oriJobVO = scheduleJobService.findByJobName(jobVO);
		
		if (null != oriJobVO) {
			scheduleJobService.delete(oriJobVO);
		}
		
		scheduleJobService.saveOrUpdate(jobVO);

		return jobVO;
	}

}

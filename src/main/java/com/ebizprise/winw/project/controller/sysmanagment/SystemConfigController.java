package com.ebizprise.winw.project.controller.sysmanagment;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.ISystemConfigService;
import com.ebizprise.winw.project.vo.SysParameterVO;

/**
 * 系統參數
 * 
 * @author joyce.hsu
 */
@Controller
@RequestMapping("/system/systemConfig")
public class SystemConfigController extends BaseController {

	@Autowired
	private ISystemConfigService systemConfigService;


	@Override
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView initPage() {
		List<SysParameterVO> list = systemConfigService.selectAllParameters();
		return new ModelAndView(DispatcherEnum.SYSTEM_CONFIG.initPage(), "list", BeanUtil.toJson(list));
	}
	/**
	 * 查詢功能
	 */
	@PostMapping(path = "/search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<SysParameterVO>> selectAllParameters(@RequestBody SysParameterVO vo) {
		List<SysParameterVO> list = systemConfigService.selectParametersByKey(vo.getParamKey());
		return ResponseEntity.ok(list);
	}

	/**
	 * 同步功能
	 */
	@RequestMapping(value = "/syncParameterList", method = RequestMethod.POST)
	public ResponseEntity<List<SysParameterVO>> syncParameterList() {
		systemConfigService.syncParameterList();
		List<SysParameterVO> list = systemConfigService.selectAllParameters();
		return ResponseEntity.ok(list);
	}

	/**
	 * 儲存功能
	 */
	@PostMapping(value = "/updateParameter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<SysParameterVO>> updateParameter(@Valid @RequestBody SysParameterVO vo, BindingResult bindingResult) {
		systemConfigService.updateParameter(vo);
		List<SysParameterVO> list = systemConfigService.selectAllParameters();
		return ResponseEntity.ok(list);
	}
}

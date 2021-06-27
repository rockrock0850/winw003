package com.ebizprise.winw.project.controller.sysmanagment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.ISystemConfigService;
import com.ebizprise.winw.project.service.impl.HolidayImportService;
import com.ebizprise.winw.project.util.FileVerifyUtil;
import com.ebizprise.winw.project.vo.HolidayVO;

/**
 * 匯入假日資料
 * @author adam.yeh
 */
@RestController
@RequestMapping("/holidayImport")
public class HolidayImportController extends BaseController {

    @Autowired
    private HolidayImportService service;
    @Autowired
    private ISystemConfigService systemConfigService;

    @Override
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView initPage () {
        return new ModelAndView(DispatcherEnum.HOLIDAY_IMPORT.initPage());
    }

    /**
     * 搜尋
     * @param vo
     * @return
     * @author adam.yeh
     */
    @PostMapping(path = "/search")
    public List<HolidayVO> search (@RequestBody HolidayVO vo) {
        return service.getYearHolidays(vo);
    }

    /**
     * 匯入工作日/假日資料
     * @param file
     * @throws Exception
     */
    @PostMapping(path = "/importHoliday")
    public ResponseEntity<Map<String, Object>> importHoliday (
            @RequestParam("year") String year,
            @RequestParam("file") MultipartFile file) throws Exception {
        String returnMsg = "";
        Map<String, Object> resMap = new HashMap<>();
        FileVerifyUtil verifyUtil = new FileVerifyUtil();
        String size = systemConfigService.getFileSize().getParamValue();
        
        returnMsg = verifyUtil
            .exists(file)
            .stringWithErrorMsg(size, getMessage("form.common.file.config.fileSize"))
            .buildErrorMsg();
        
        if (StringUtils.isBlank(returnMsg)) {
            String allowExtension = "txt".toUpperCase();
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase();
            
            returnMsg = verifyUtil
                .fileSize(file, new Long(size))
                .extension(fileExtension, allowExtension)
                .buildErrorMsg();
            
            if (StringUtils.isBlank(returnMsg)) {
                service.importFile(year, file);
            }
        }

        resMap.put("returnMsg", returnMsg);
        resMap.put("dataList", service.getYearHolidays(new HolidayVO()));

        return ResponseEntity.ok(resMap);
    }

}

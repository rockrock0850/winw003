package com.ebizprise.winw.project.controller.alterlist;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.vo.AlterFormStatisticTypeVO;

/**
 * 資訊部門各變更類型統計表
 * 
 * @author adam.yeh
 *
 */
@RestController
@RequestMapping("/alterFormStatisticType")
public class AlterFormStatisticTypeController extends BaseController {

    @Override
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView initPage () {
        return new ModelAndView(DispatcherEnum.ALTER_STATISTIC_TYPE.initPage(), "alterList", BeanUtil.toJson(fakeInfoList()));
    }
    
    private List<AlterFormStatisticTypeVO> fakeInfoList () {
        List<AlterFormStatisticTypeVO> voList = new ArrayList<>();
        
        for (int i = 1; i < 100; i++) {
            AlterFormStatisticTypeVO vo = new AlterFormStatisticTypeVO();
            vo.setDept("AP" + i);
            
            if (i % 2 == 0) {
                vo.setNormalUrgent("11");
                vo.setNormalNonUrgent("11");   
            } else {
                vo.setStandardUrgent("22");
                vo.setStandardNonUrgent("22");   
            }
            
            voList.add(vo);
        }
        
        return voList;
    }

}

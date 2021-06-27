package com.ebizprise.winw.project.controller.sysmanagment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.base.controller.BaseController;
import com.ebizprise.winw.project.enums.SysParamEnum;
import com.ebizprise.winw.project.enums.page.DispatcherEnum;
import com.ebizprise.winw.project.service.impl.ServiceTypeManagementServiceImpl;
import com.ebizprise.winw.project.vo.SystemOptionVO;

/**
 * 服務類別管理
 * @author momo.liu
 */
@RestController
@RequestMapping("/serviceTypeManagement/init")
public class ServiceManagementController extends BaseController {

    @Autowired
    private ServiceTypeManagementServiceImpl service;

    @Override
    @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView initPage () {
        SystemOptionVO subVo = new SystemOptionVO();
        SystemOptionVO queryVo = new SystemOptionVO();
        List<SystemOptionVO> menus = service.getHierachicalList(queryVo);

        for (SystemOptionVO serviceType : menus) {
            subVo.setParentId(serviceType.getValue());
            serviceType.setSubServiceType(service.getHierachicalList(subVo));
        }

        queryVo.setServiceType(menus);

        return new ModelAndView(DispatcherEnum.SERVICE_TYPE_MANAGE.initPage(), "groupData", queryVo);
    }

    @PostMapping(value = "/query")
    public ModelAndView query () {
        SystemOptionVO subVO = new SystemOptionVO();
        String jsonStr = request.getParameter("formPostData");
        SystemOptionVO vo = BeanUtil.fromJson(jsonStr, SystemOptionVO.class);
        vo.setDisplay(vo.getSearchCol1());
        List<SystemOptionVO> menus = service.getHierachicalList(vo);

        for (SystemOptionVO serviceType : menus) {
            subVO.setDisplay(vo.getSearchCol2());
            subVO.setParentId(serviceType.getValue());
            serviceType.setSubServiceType(service.getHierachicalList(subVO));
        }

        vo.setServiceType(menus);

        if (StringUtils.isNoneBlank(vo.getName())) {
            List<SystemOptionVO> cleanEmptyList = new ArrayList<SystemOptionVO>();
            for (SystemOptionVO menu : menus) { // 只查詢服務子類別時，若父類別有該項子類別則顯示，反之則不顯示
                if (CollectionUtils.isNotEmpty(menu.getSubServiceType())) {
                    cleanEmptyList.add(menu);
                }
            }

            vo.setServiceType(cleanEmptyList);
        }

        return new ModelAndView(DispatcherEnum.SERVICE_TYPE_MANAGE.initPage(), "groupData", vo);
    }

    @PostMapping(value = "/createLevel1")
    public SystemOptionVO createLevel1 (@RequestBody SystemOptionVO vo) {
        List<SystemOptionVO> exist = service.getHierachicalList(vo);

        if (exist.size() == 0) {
            vo.setOptionId(SysParamEnum.SERVICE.action());
            vo = service.create(vo);
        } else {
            vo.setValidateLogicError("[" + vo.getDisplay() + "] 已存在。");
        }

        return vo;
    }

    @PostMapping(value = "/createLevel2")
    public SystemOptionVO createLevel2 (@RequestBody SystemOptionVO vo) {
        List<SystemOptionVO> exist = service.getHierachicalList(vo);

        if (exist.size() == 0) {
            vo.setOptionId(SysParamEnum.SERVICE_2.action());
            vo = service.create(vo);
        } else {
            vo.setValidateLogicError("[" + vo.getDisplay() + "] 已存在。");
        }

        return vo;
    }

    @PostMapping(value = "/updateLevel")
    public SystemOptionVO updateLevel (@RequestBody SystemOptionVO vo) {
        return service.update(vo);
    }

}

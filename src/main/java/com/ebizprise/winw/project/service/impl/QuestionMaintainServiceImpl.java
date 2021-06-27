package com.ebizprise.winw.project.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.ebizprise.project.utility.bean.BeanUtil;
import com.ebizprise.winw.project.base.service.BaseService;
import com.ebizprise.winw.project.entity.QuestionMaintainEntity;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.repository.IQuestionMaintainRepository;
import com.ebizprise.winw.project.service.IQuestionMaintainService;
import com.ebizprise.winw.project.util.UserInfoUtil;
import com.ebizprise.winw.project.vo.HtmlVO;
import com.ebizprise.winw.project.vo.QuestionVO;

@Transactional
@Service("questionMaintainService")
public class QuestionMaintainServiceImpl extends BaseService implements IQuestionMaintainService {

    @Autowired
    private IQuestionMaintainRepository questionMaintainRepository;

    @Override
    public List<QuestionVO> getQuestionVOs() {
        List<QuestionMaintainEntity> datas = questionMaintainRepository.findAll();
        return BeanUtil.copyList(datas, QuestionVO.class);
    }

    @Override
    public List<QuestionVO> getQuestionVOs (QuestionVO vo) {
        ResourceEnum resource = ResourceEnum.SQL_SYSTEM_MANAGEMENT.getResource("FIND_ALL_QUESTIONS_BY_CONDITION");
        Conditions conditions = new Conditions();
        return jdbcRepository.queryForList(resource, conditions, vo, QuestionVO.class);
    }
    
    @Override
    public void updateQuestions(List<QuestionVO> voList) {
        Date now = new Date();
        List<QuestionMaintainEntity> pojoList = new ArrayList<>();

        for (QuestionVO vo : voList) {
            QuestionMaintainEntity pojo = new QuestionMaintainEntity();
            BeanUtil.copyProperties(vo, pojo);
            pojo.setUpdatedBy(UserInfoUtil.loginUserId());
            pojo.setUpdatedAt(now);

            if (pojo.getId() == null) {
                pojo.setCreatedBy(UserInfoUtil.loginUserId());
                pojo.setCreatedAt(now);
            }

            pojoList.add(pojo);
        }

        questionMaintainRepository.saveAll(pojoList);
    }

    @Override
    public void deleteQuestions(List<QuestionVO> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }

        for (QuestionVO questionVO : voList) {
            Long id = questionVO.getId();
            questionMaintainRepository.deleteById(id);
        }
    }

}

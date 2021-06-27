package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.vo.QuestionVO;

/**
 * @author joyce.hsu
 * @version 1.0, Created at 2019年6月4日
 */
public interface IQuestionMaintainService {
    
    public List<QuestionVO> getQuestionVOs();

    public void updateQuestions(List<QuestionVO> vos);

    public void deleteQuestions(List<QuestionVO> voList);

    public List<QuestionVO> getQuestionVOs (QuestionVO vo);
    
}

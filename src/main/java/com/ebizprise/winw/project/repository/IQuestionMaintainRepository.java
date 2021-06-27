package com.ebizprise.winw.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.QuestionMaintainEntity;

@Repository("questionMaintainRepository")
public interface IQuestionMaintainRepository extends JpaRepository<QuestionMaintainEntity, Long> {

}
package com.ebizprise.winw.project.repository;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormFieldsEntity;

@Repository("formFieldsRepository")
public interface IFormFieldsRepository extends JpaRepository<FormFieldsEntity, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public FormFieldsEntity findByFormClass(String formClass);
}

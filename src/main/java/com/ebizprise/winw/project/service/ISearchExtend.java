package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.jdbc.JdbcRepositoy;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;

/**
 * 查詢interface
 * 
 * @author jacky.fu
 *
 */
public interface ISearchExtend {

    /**
     * 自定義擴充條件
     * 
     * @param con
     * @return
     * @author jacky.fu
     */
    default Conditions cusCondition (Conditions con) {
        return con;
    }

    /**
     * 自定義查詢
     * 
     * @param jdbcRepository
     * @param resource
     * @param conditions
     * @return
     * @author jacky.fu
     * @param <T>
     */
    <T> List<T> cusQuery (JdbcRepositoy jdbcRepository, ResourceEnum resource, Conditions conditions);

}

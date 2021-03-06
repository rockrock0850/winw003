package com.ebizprise.winw.project.jdbc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ebizprise.project.utility.trans.FileUtil;
import com.ebizprise.winw.project.enums.ResourceEnum;
import com.ebizprise.winw.project.jdbc.criteria.Conditions;
import com.ebizprise.winw.project.jdbc.criteria.Paging;

/**
 * 處理JDBC操作共用工具 基礎類別
 * 
 * @author adam.yeh
 */
@Repository("BaseJDBC")
public abstract class BaseJDBC {

    private static final Logger logger = LoggerFactory.getLogger(BaseJDBC.class);

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected NamedParameterJdbcDaoSupport support;

    /**
     *
     * @param resource
     * @return
     */
    public int update (ResourceEnum resource) {
        return support.getJdbcTemplate().update(getSqlText(resource));
    }
    
    /**
     *
     * @param resource
     * @param sqlParams
     * @return
     */
    public int update (ResourceEnum resource, Map<String, Object> sqlParams) {
        return support.getNamedParameterJdbcTemplate().update(getSqlText(resource), sqlParams);
    }
    
    /**
     *
     * @param resource
     * @param sqlParams
     * @return
     */
    public int update (ResourceEnum resource, Conditions conditions, Map<String, Object> sqlParams) {
        return support.getNamedParameterJdbcTemplate().update(getSqlText(resource, conditions), sqlParams);
    }

    /**
     *
     * @param resource
     * @param sqlParam
     * @return
     */
    public int[] updateBatch (ResourceEnum resource, List<Map<String, Object>> sqlParam) {
        MapSqlParameterSource[] mapParamArr = new MapSqlParameterSource[sqlParam.size()]; 
        
        for(int i = 0 ; i < sqlParam.size() ; i++) {
            Map<String, ?> m = sqlParam.get(i);
            mapParamArr[i] = new MapSqlParameterSource(m);
        }
        
        int[] effCount = 
                support.getNamedParameterJdbcTemplate().batchUpdate(getSqlText(resource), mapParamArr);
        
        return effCount;
    }

    /**
     *
     * @param resource
     * @param clazz
     * @return
     */
    public <T> T queryForBean (ResourceEnum resource, Class<T> clazz) {
        return queryForBean(resource, new HashMap<String, Object>(), clazz);
    }

    /**
     *
     * @param resource
     * @param clazz
     * @return
     */
    public <T> T queryForBean (ResourceEnum resource, Conditions conditions, Class<T> clazz) {
        List<T> dataLs = 
                support.getNamedParameterJdbcTemplate().query(getSqlText(resource, conditions), new HashMap<String, Object>(), new BeanPropertyRowMapper<T>(clazz));
        
        return CollectionUtils.isEmpty(dataLs) ? null : dataLs.get(0);
    }

    /**
     *
     * @param resource
     * @param params
     * @param clazz
     * @return
     */
    public <T> T queryForBean (ResourceEnum resource, Map<String, Object> params, Class<T> clazz) {
        List<T> dataLs = 
                support.getNamedParameterJdbcTemplate().query(getSqlText(resource), params, new BeanPropertyRowMapper<T>(clazz));
        
        return CollectionUtils.isEmpty(dataLs) ? null : dataLs.get(0);
    }

    /**
     *
     * @param resource
     * @param clazz
     * @return
     */
    public <T> T queryForBean (ResourceEnum resource, Conditions conditions, Map<String, Object> params, Class<T> clazz) {
        List<T> dataLs = 
                support.getNamedParameterJdbcTemplate().query(getSqlText(resource, conditions), params, new BeanPropertyRowMapper<T>(clazz));
        
        return CollectionUtils.isEmpty(dataLs) ? null : dataLs.get(0);
    }
    
    /**
     * 
     * @param resource
     * @return
     */
    public Map<String, Object> queryForMap (ResourceEnum resource) {
        return queryForMap(resource, new HashMap<String, Object>());
    }
    
    /**
     *
     * @param resource  
     * @param conditions
     * @return
     */
    public Map<String, Object> queryForMap (ResourceEnum resource, Conditions conditions) {
        List<Map<String, Object>> dataLs = support.getNamedParameterJdbcTemplate().queryForList(getSqlText(resource, conditions), new HashMap<String, Object>());
        
        int size = dataLs != null ? dataLs.size() : -1;
        
        if(dataLs != null && dataLs.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, size);
        }
        
        if(size == 1) {
            return dataLs.get(0);
        } 
        
        return null;
    }
    
    /**
     *
     * @param resource  
     * @param params    查詢條件對應參數
     * @return
     */
    public Map<String, Object> queryForMap (ResourceEnum resource, Map<String, Object> params) {
        List<Map<String, Object>> dataLs = support.getNamedParameterJdbcTemplate().queryForList(getSqlText(resource), params);
        
        int size = dataLs != null ? dataLs.size() : -1;
        
        if(dataLs != null && dataLs.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, size);
        }
        
        if(size == 1) {
            return dataLs.get(0);
        } 
        
        return null;
    }
    
    /**
     *
     * @param resource  
     * @param params    查詢條件對應參數
     * @return
     */
    public Map<String, Object> queryForMap (ResourceEnum resource, Conditions conditions, Map<String, Object> params) {
        List<Map<String, Object>> dataLs = support.getNamedParameterJdbcTemplate().queryForList(getSqlText(resource, conditions), params);
        
        int size = dataLs != null ? dataLs.size() : -1;
        
        if(dataLs != null && dataLs.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, size);
        }
        
        if(size == 1) {
            return dataLs.get(0);
        } 
        
        return null;
    }
    
    /**
     *
     * @param resource 
     * @return
     */
    public <T> List<Map<String, Object>> queryForList (ResourceEnum resource) {
        List<Map<String, Object>> rtnLs = 
                support.getNamedParameterJdbcTemplate().queryForList(getSqlText(resource), new HashMap<String, Object>());
        
        if(CollectionUtils.isEmpty(rtnLs)) {
            return Collections.emptyList();
        }
        
        return rtnLs;
    }
    
    /**
     *
     * @param resource 
     * @param conditions 
     * @return
     */
    public <T> List<Map<String, Object>> queryForList (ResourceEnum resource, Conditions conditions) {
        List<Map<String, Object>> rtnLs = 
                support.getNamedParameterJdbcTemplate().queryForList(getSqlText(resource, conditions), new HashMap<String, Object>());
        
        if(CollectionUtils.isEmpty(rtnLs)) {
            return Collections.emptyList();
        }
        
        return rtnLs;
    }     
    
    /**
     *
     * @param resource   
     * @param params    查詢條件參數
     * @return
     */
    public <T> List<Map<String, Object>> queryForList (ResourceEnum resource, Map<String, Object> params) {
        List<Map<String, Object>> rtnLs = 
                support.getNamedParameterJdbcTemplate().queryForList(getSqlText(resource), params);
        
        if(CollectionUtils.isEmpty(rtnLs)) {
            return Collections.emptyList();
        }
        
        return rtnLs;
    }    
 
    /**
     * @param resource  
     * @param conditions 
     * @param params    查詢條件參數 
     * @return
     */
    public <T> List<Map<String, Object>> queryForList (ResourceEnum resource, Conditions conditions, Map<String, Object> params) {
        List<Map<String, Object>> rtnLs = 
                support.getNamedParameterJdbcTemplate().queryForList(getSqlText(resource, conditions), params);
        if(CollectionUtils.isEmpty(rtnLs)) {
            return Collections.emptyList();
        }
        
        return rtnLs;
    }
    
    /**
     * @param resource  
     * @param clazz     須轉型之類別
     * @return
     */
    public <T> List<T> queryForList (ResourceEnum resource, Class<T> clazz) {
        List<T> rtnLs = 
                support.getNamedParameterJdbcTemplate().query(getSqlText(resource), new HashMap<String, Object>(), new BeanPropertyRowMapper<T>(clazz));
        
        if(CollectionUtils.isEmpty(rtnLs)) {
            return Collections.emptyList();
        }
        
        return rtnLs;
    }
    
    /**
     * @param resource  
     * @param clazz     須轉型之類別
     * @return
     */
    public <T> List<T> queryForList (ResourceEnum resource, Paging paging, Class<T> clazz) {
        List<T> rtnLs = 
                support.getNamedParameterJdbcTemplate().query(getSqlText(resource, null, paging), new HashMap<String, Object>(), new BeanPropertyRowMapper<T>(clazz));
        
        if(CollectionUtils.isEmpty(rtnLs)) {
            return Collections.emptyList();
        }
        
        return rtnLs;
    }
    
    /**
     * @param resource  
     * @param conditions  
     * @param clazz     須轉型之類別
     * @return
     */
    public <T> List<T> queryForList (ResourceEnum resource, Conditions conditions, Class<T> clazz) {
        List<T> rtnLs = 
                support.getNamedParameterJdbcTemplate().query(getSqlText(resource, conditions), new HashMap<String, Object>(), new BeanPropertyRowMapper<T>(clazz));
        
        if(CollectionUtils.isEmpty(rtnLs)) {
            return Collections.emptyList();
        }
        
        return rtnLs;
    }
    
    /**
     * @param resource  
     * @param conditions  
     * @param clazz     須轉型之類別
     * @return
     */
    public <T> List<T> queryForList (ResourceEnum resource, Conditions conditions, Paging paging, Class<T> clazz) {
        List<T> rtnLs = 
                support.getNamedParameterJdbcTemplate().query(getSqlText(resource, conditions, paging), new HashMap<String, Object>(), new BeanPropertyRowMapper<T>(clazz));
        
        if(CollectionUtils.isEmpty(rtnLs)) {
            return Collections.emptyList();
        }
        
        return rtnLs;
    }

    /**
     * @param resource  
     * @param conditions 
     * @param params    查詢條件參數 
     * @param clazz     須轉型之類別
     * @return
     */
    public <T> List<T> queryForList (ResourceEnum resource, Conditions conditions, Map<String, Object> params, Class<T> clazz) {
        List<T> rtnLs = 
                support.getNamedParameterJdbcTemplate().query(getSqlText(resource, conditions), params, new BeanPropertyRowMapper<T>(clazz));
        
        if(CollectionUtils.isEmpty(rtnLs)) {
            return Collections.emptyList();
        }
        
        return rtnLs;
    }

    /**
     * @param resource  
     * @param conditions 
     * @param params    查詢條件參數 
     * @param clazz     須轉型之類別
     * @return
     */
    public <T> List<T> queryForList (ResourceEnum resource, Conditions conditions, Map<String, Object> params, Paging paging, Class<T> clazz) {
        List<T> rtnLs = 
                support.getNamedParameterJdbcTemplate().query(getSqlText(resource, conditions, paging), params, new BeanPropertyRowMapper<T>(clazz));
        
        if(CollectionUtils.isEmpty(rtnLs)) {
            return Collections.emptyList();
        }
        
        return rtnLs;
    }
    
    /**
     *
     * @param resource  
     * @param params    查詢條件參數
     * @param clazz     須轉型之類別
     * @return
     */
    public <T> List<T> queryForList (ResourceEnum resource, Map<String, Object> params, Class<T> clazz) {
        List<T> rtnLs = 
                support.getNamedParameterJdbcTemplate().query(getSqlText(resource), params, new BeanPropertyRowMapper<T>(clazz));
        
        if(CollectionUtils.isEmpty(rtnLs)) {
            return Collections.emptyList();
        }
        
        return rtnLs;
    }
    
    /**
     *
     * @param resource  
     * @param params    查詢條件參數
     * @param clazz     須轉型之類別
     * @return
     */
    public <T> List<T> queryForList (ResourceEnum resource, Map<String, Object> params, Paging paging, Class<T> clazz) {
        List<T> rtnLs = 
                support.getNamedParameterJdbcTemplate().query(getSqlText(resource, null, paging), params, new BeanPropertyRowMapper<T>(clazz));
        
        if(CollectionUtils.isEmpty(rtnLs)) {
            return Collections.emptyList();
        }
        
        return rtnLs;
    }

    private static String getSqlText (ResourceEnum resource) {
        return getSqlText(resource, null);
    }
    
    private synchronized static String getSqlText (ResourceEnum resource, Conditions conditions) {
        StringBuilder path = new StringBuilder();
        path.append(resource.dir());
        path.append(resource.file());
        path.append(resource.extension());
        
        String sqlText = FileUtil.readFile(path.toString());

        if (conditions != null) {
            sqlText = conditions.done(sqlText);
        }
        
        logger.info("Use SQL : " + resource.file());

        return sqlText;
    }
    
    private static String getSqlText (ResourceEnum resource, Conditions conditions, Paging paging) {
        String sorting = paging.getSorting();
        String template = paging.getPagingSQL();
        String sqlText = getSqlText(resource, conditions);
        String orderBy = StringUtils.join(paging.getOrderBy(), ",");
        
        if (StringUtils.isBlank(orderBy)) {
            try {
                template = "";
                throw new NullPointerException("The parameter from Paging.orderBy is null.");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        
        template = StringUtils.replace(template, "${SORTING}", sorting);
        template = StringUtils.replace(template, "${ORDER_BY}", orderBy);
        template = StringUtils.replace(template, "${QUERY_STRING}", sqlText);
        template = StringUtils.replace(template, "${PAGE}", String.valueOf(paging.getPage()));
        template = StringUtils.replace(template, "${PAGE_SIZE}", String.valueOf(paging.getPagesize()));
        
        return template;
    }
    
}
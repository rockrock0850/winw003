package com.ebizprise.winw.project.jdbc.criteria;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * JDBC DAO的查詢條件物件化類別
 * 
 * @author adam.yeh
 * @create date: NOV 19, 2017
 */
public class Conditions {

    private StringBuilder condition;

    public Conditions () {
        condition = new StringBuilder();
    }
    
    public Conditions equal (String column, String value) {
        appendPairTypes(new Pair(column, value, SQL.EQUAL));
        return this;
    }
    
    public Conditions like (String column, String value) {
        appendPairTypes(new Pair(column, value, SQL.LIKE));
        return this;
    }
    
    public Conditions likeBegin (String column, String value) {
        appendPairTypes(new Pair(column, value, SQL.LIKEBEGIN));
        return this;
    }
    
    public Conditions likeEnded (String column, String value) {
        appendPairTypes(new Pair(column, value, SQL.LIKEENDED));
        return this;
    }
    
    public Conditions unEqual (String column, String value) {
        appendPairTypes(new Pair(column, value, SQL.UNEQUAL));
        return this;
    }
    
    public Conditions in (String column, List<String> values) {
        appendPairTypes(new Pair(column, values, SQL.IN));
        return this;
    }
    
    public Conditions gt (String column, String value) {
        appendPairTypes(new Pair(column, value, SQL.GT));
        return this;
    }
    
    public Conditions lt (String column, String value) {
        appendPairTypes(new Pair(column, value, SQL.LT));
        return this;
    }
    
    public Conditions gtEqual (String column, String value) {
        appendPairTypes(new Pair(column, value, SQL.GTEQUAL));
        return this;
    }
    
    public Conditions ltEqual (String column, String value) {
        appendPairTypes(new Pair(column, value, SQL.LTEQUAL));
        return this;
    }

    public Conditions notNull (String column) {
        appendPairTypes(new Pair(column, SQL.NOTNULL));
        return this;
    }

    public Conditions isNull (String column) {
        appendPairTypes(new Pair(column, SQL.ISNULL));
        return this;
    }
    
    public Conditions notIn (String column, List<String> values) {
        appendPairTypes(new Pair(column, values, SQL.NOTIN));
        return this;
    }
    
    /**
     * 左括弧
     * 
     * @return
     */
    public Conditions leftPT () {
        condition.append(" (");
        return this;
    }
    
    /**
     * 左括弧
     * 
     * @param e 連結符號 ( AND、OR... )
     * @return
     */
    
    public Conditions leftPT (SQL e) {
        condition.append(" " + e.operator() + " (");
        return this;
    }
    
    /**
     * 右括弧
     * 
     * @return
     */
    public Conditions RightPT () {
        condition.append(") ");
        return this;
    }
    
    /**
     * 右括弧
     * 
     * @param e 連結符號 ( AND、OR... )
     * @return
     */
    public Conditions RightPT (SQL e) {
        condition.append(") " + e.operator() + " ");
        return this;
    }
    
    /**
     * 將組裝好的條件句參數AND起來
     * 
     * @return
     */
    public Conditions and () {
        condition.append(" " + SQL.AND.operator() + " ");
        return this;
    }
    
    /**
     * 將組裝好的條件句參數OR起來
     * 
     * @return
     */
    public Conditions or () {
        condition.append(" " + SQL.OR.operator() + " ");
        return this;
    }
    
    /**
     * 排序
     * 
     * @return
     */
    public Conditions orderBy (String column) {
        return orderBy(column, SQL.ASC);
    }
    
    /**
     * 排序
     * 
     * @param order 升/降 冪
     * @return
     */
    public Conditions orderBy (String column, SQL order) {
        condition.append(" ORDER BY " + column + " " + order.operator() + " ");
        return this;
    }
    
    /**
     * 宣告條件句結束
     * 
     * @return
     */
    public String done (String sqlText) {
        String done = StringUtils.replace(sqlText, "${CONDITIONS}", condition.toString());
        condition = null;
        
        return done;
    }

    private void appendPairTypes (Pair pair) {
        SQL type = pair.getMatchType();
        String column = pair.getColumn();
        String operator = type.operator();
        List<String> values = pair.getValues();
        String value = StringUtils.replace(pair.getValue(), "'", "''");
        
        switch (type) {
            case NOTIN:
            case IN:
                StringBuilder sqlText = new StringBuilder();
                sqlText.append("( '");
                sqlText.append(StringUtils.join(values, "', '"));
                sqlText.append("' )");
                condition.append(column + " " + operator + " " + sqlText.toString());
                sqlText = null;
                
                break;
                
            case LIKE:
                condition.append(column + " " + operator + " '%" + value + "%'");
                break;
                
            case LIKEBEGIN:
                condition.append(column + " " + operator + " '" + value + "%'");
                break;
                
            case LIKEENDED:
                condition.append(column + " " + operator + " '%" + value + "'");
                break;

            case NOTEXISTS:
                condition.append(" " + operator + " ( " + value + " ) ");
                break;
                
            case ISNULL:
            case NOTNULL:
                condition.append(column + " " + operator);
                break;

            default:
                condition.append(column + " " + operator + " '" + value + "'");
                break;
        }
        
        pair = null;
    }
    
}

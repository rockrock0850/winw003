<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!--  
輸入參數說明 : 
	- id1 : 日期輸入欄識別碼
	- name1 : 輸入欄名稱識別碼
	- warning : 警告文字
	- removeTdTag : 是否需要移除td標籤
-->

<c:set var='id1' scope="application" value='${param.id1}'/>

<c:choose>
	<c:when test='${param.removeTdTag}'></c:when>
	<c:otherwise><td></c:otherwise>
</c:choose>
<input id="<c:out value='${id1}'/>" type="text" class='initDateTimePicker' name="<c:out value='${param.name1}'/>" style="width: 10rem;" readonly /><c:out value='${param.warning}'/>
<c:choose>
	<c:when test='${param.removeTdTag}'></c:when>
	<c:otherwise></td></c:otherwise>
</c:choose>
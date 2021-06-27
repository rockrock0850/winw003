<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!--  
輸入參數說明 :
	- id1 : 日期輸入欄識別碼
	- name1 : 輸入欄名稱識別碼
	- id2 : 日期輸入欄識別碼
	- name2 : 輸入欄名稱識別碼
-->
<c:import url="/WEB-INF/jsp/common/models/dateModel.jsp">
    <c:param name="id1" value="${param.id1}" />
    <c:param name="name1" value="${param.name1}" />
    <c:param name="removeTdTag" value="true" />
</c:import>
<span style='margin-right: 15px'>~</span>
<c:import url="/WEB-INF/jsp/common/models/dateModel.jsp">
  	<c:param name="id1" value="${param.id2}" />
    <c:param name="name1" value="${param.name2}" />
    <c:param name="removeTdTag" value="true" />
</c:import>		

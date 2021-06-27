<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<td id='systemModel'>
	<input id='systemBrand' name='systemBrand' type="hidden" /> 
	<input id='systemId' name='systemId' type="text" size="4" style="width: 5rem;" readonly />
	<jsp:include page='/WEB-INF/jsp/common/models/textareaModel.jsp'>
		<jsp:param name="id" value="system" />
		<jsp:param name="name" value="system" />
		<jsp:param name="readonly" value="true" />
	</jsp:include> 
	<button id='systemDialog' type='button' onclick='SystemDialog.show()'><s:message code="system.select.name.dialog" text='選取系統名稱' /></button>
</td>

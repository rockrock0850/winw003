<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<td id='cComponentModel'>
	<jsp:include page='/WEB-INF/jsp/common/models/textareaModel.jsp'>
		<jsp:param name="id" value="cComponent" />
		<jsp:param name="name" value="cComponent" />
		<jsp:param name="readonly" value="true" />
	</jsp:include> 
	<textarea id='cComponentDisplay' class="textareaLikeInput hidden" readonly />
	<button id='cComponentDialog' type='button' onclick='CComponentDialog.show()'>選擇組態元件</button>
</td>

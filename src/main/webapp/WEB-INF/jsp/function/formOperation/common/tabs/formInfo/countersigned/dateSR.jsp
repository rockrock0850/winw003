<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${formClass == 'SR_C'}">
	<table class="grid_query">
		<tr>
			<th>表單分派時間</th>
			<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
				<jsp:param name="id1" value="assignTime" />
				<jsp:param name="name1" value="assignTime" />
			</jsp:include>
			<th>主單預計完成時間</th>
			<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
				<jsp:param name="id1" value="mect" />
				<jsp:param name="name1" value="mect" />
			</jsp:include>
			<th>實際完成時間</th>
			<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
				<jsp:param name="id1" value="act" />
				<jsp:param name="name1" value="act" />
			</jsp:include>
		</tr>
	</table>
</c:if>
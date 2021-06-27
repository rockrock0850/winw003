<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script>
$(function () {
	$('textarea#<c:out value="${param.id}"/>').resizable({handles: "se"});
});
</script>

<div style='display:inline-block'>
	<textarea id='<c:out value="${param.id}"/>' class="textareaLikeInput" name='<c:out value="${param.name}"/>' type="text" <c:if test='${param.readonly}'>readonly</c:if> />
</div>
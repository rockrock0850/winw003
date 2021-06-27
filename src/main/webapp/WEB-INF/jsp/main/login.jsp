<!doctype html>
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<head>
<meta charset="utf-8">
<title><s:message code="system.name" text="資訊服務作業流程平台" /></title>
<script>
$(function () {
	if ('${error}') {
		var error = ObjectUtil.parse('${error}');
		alert(error.message);
	}

    $('#language').on('change', function () {
        var url = $(this).val();

        if (url) {
        	url = PurifyUtil.dom(url);
        	url = PurityUtil.uri(url);
            window.location = url;
        }

        return false;
    });

	// SessionUtil.set('idleTime', 0);// 重置系統閒置計時器
});
</script>
</head>
<body>
<section class="loginBlock">
    <img src="${contextPath}/static/images/tcb_logo.jpg" class="logo">
    <div class="sysName">
    	<font size="4"><br /><s:message code="system.name" text="資訊服務作業流程平台" /></font>
    </div>
	<form id='loginForm' action="${contextPath}/login" method="post" autocomplete="off">
	    <div class="inputPanel">
	        <table>
				<c:choose>
					<c:when test='${params.isTestForm}'>
			            <tr>
				            <th width="60%" align="right"><b><font size="3"><s:message code="login.account.field" text="帳  號" /></b></strong></th>
				            <td><input type="text" style="width:250%;" id="username" name="username" value="ANTONYLIU"/></td>
			            </tr>
			            <tr>
				            <th width="60%" align="right"><font size="3"><s:message code="login.password.field" text="密  碼" /></font></th>
				            <td><input type="password" style="width:250%;" id="password" name="password" value="1qaz@WSX" /></td>
			            </tr>
					</c:when>
					<c:otherwise>
			            <tr>
				            <th width="60%" align="right"><b><font size="3"><s:message code="login.account.field" text="帳  號" /></b></strong></th>
				            <td><input type="text" style="width:250%;" id="username" name="username" value=""/></td>
			            </tr>
			            <tr>
				            <th width="60%" align="right"><font size="3"><s:message code="login.password.field" text="密  碼" /></font></th>
				            <td><input type="password" style="width:250%;" id="password" name="password" value="" /></td>
			            </tr>
					</c:otherwise>
				</c:choose>
	        </table>
	        <div class="btnBR"><button><i class="iconx-login"></i><s:message code="login.enter.button" text="登入" /></button></div>
			<c:if test='${params.isTestReport}'>
		        <div class="btnBR">
		        	<a href='http://eipmis.tcb.com/sites/wfmg/Shared%20Documents/[%20ISWP%20資訊服務作業流程平台]/ISWP問題單追蹤表.xlsx'>
		        		<font size='4' style="color: yellow;">ISWP問題單追蹤表</font>
		        	</a>
	        	</div>
			</c:if>
			<input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
	    </div>
		<input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
	</form>
	<div style="margin-left:20%;color:black;position:relative;top:105%;">
       	<a href='http://eipmis.tcb.com/sites/wfmg/Shared%20Documents/[%20ISWP%20資訊服務作業流程平台]/資訊服務作業流程平台_教育訓練簡報.pptx' target='_blank'>
       		<font size="3" style="color:black;">ISWP系統操作手冊</font>
       	</a>
       	<font size="3" style="color:white;">、</font>
        <font size="3" style="color:white;"><fmt:message key="web.browser.recommend"></fmt:message></font>
       	<font size="3" style="color:white;">；</font>
       	<a href='${pageContext.request.contextPath}/static/attachment/WINW003_ISWP 資訊服務作業流程平台 Release Notes.pdf' target='_blank'>
       		<font size="3" style="color:white;">Release Notes資訊</font>
       	</a>
		&nbsp;
		&nbsp;
		<font size="2" style="color:white;">Ver.1-${params.version}</font>
    </div>
</section>
</body>
</html>

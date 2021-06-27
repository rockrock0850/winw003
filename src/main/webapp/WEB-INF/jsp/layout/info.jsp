﻿<%@ page contentType="text/html; charset=UTF-8"%>

<script language="javascript">
	var loginUserInfo = ObjectUtil.parse('${sysUserVO}');
	
	$(function() {
		$('button').click(function () {
			if ($(this).find('i').hasClass('iconx-logout')) {
				parent.location.href = '${contextPath}/logout';
			}
		});
	})
</script>

<title><s:message code="system.name" text='資訊服務作業流程平台' /></title>
<div class="frame_header">
	<header>
		<a href='${contextPath}/dashboard'><img src="${contextPath}/static/images/cologo01.gif" class="logo" /></a>
		<button>
			<i class="iconx-logout"></i> <s:message code="system.logout" text='登出' />
		</button>
		<section class="info">
			<span><s:message code="header.login.time" arguments="${sysUserVO.loginTime}" text='登入時間 : ' /></span> <span><s:message code="header.login.user" arguments="${sysUserVO.userId};${sysUserVO.name}" argumentSeparator=";" text='登入者 : ' /></span> <span><s:message code="header.group.name" arguments="${sysUserVO.groupName}" text='群組名稱 : ' />
	</span> &nbsp;
		</section>
	</header>
</div>

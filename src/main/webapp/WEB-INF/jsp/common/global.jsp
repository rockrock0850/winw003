<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page import="java.util.Date"%>

<c:set var='now' value='<%=new Date()%>' />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%@ include file="/WEB-INF/jsp/common/library.jsp"%>

<link rel="shortcut icon" href="${pageContext.request.contextPath}/static/attachment/favicon.ico" type="image/x-icon">
<link rel="icon" href="${pageContext.request.contextPath}/static/attachment/favicon." type="image/x-icon">

<script>
	var contextPath = "${pageContext.request.contextPath}";
	
	/*
	var systemTimeout = 30;// 15分鐘
	
	function timerIncrement () {
	    if (!$('input[type="password"]').exists()) {
	    	let idleTime = SessionUtil.get('idleTime');
	    	
	    	if (!$.isNumeric(idleTime)) {
	    		SessionUtil.set('idleTime', 0);
	    	} else if (idleTime == systemTimeout) {
				alert("<s:message code='global.login.timeout' text='登入逾時。' />");
		        location.href = contextPath + '/logout';
	    	} else {
	    		idleTime = parseInt(idleTime) + 1;
	    		SessionUtil.set('idleTime', idleTime);
	    	}
	    }
	}
	 
	$.fn.exists = function () {
		return this.length > 0;
	}
	*/
	
	$(function(){
		/*
		系統的登入逾時功能 : 
		本來想直接複製其他專案(103或FITAS)的機制過來用, 
		但是發現合庫這邊可以開多個頁簽作業的情況下, 導致A頁簽放著, 使用B頁簽
		雖然B頁簽的計數器一直刷新, 但A頁簽如果計數到timeout, 系統還是會照樣判斷登入逾時。
		所以要改成從伺服器端去計數登入逾時, 讓所有頁簽共享登入時間。
		P.S. login.jsp:27 也有一行相關程式
		
		setInterval(timerIncrement, 1 * 1000);// 每分鐘檢查一次
		
		$(this).mousemove(function (e) {
		    idleTime = 0;
		});
		
		$(this).keypress(function (e) {
		    idleTime = 0;
		});
		*/
		
		// 綁定搜尋面板的顯示/隱藏事件
		$("button.searchPanel").click(function () {
			var $header = $(this);
			$header.next().slideToggle(500, function () {
				var $icon = $header.find('i');
				$icon.toggleClass(function () {
					if ($icon.hasClass('iconx-collapse')) {
						return 'iconx-expand';
					}
				});
			});
		});
		
		// 防止HTML表單在使用者按下Enter的時候刷新整個頁面
		$("form").not('form#loginForm').submit(function() { return false; });
	});
	
	function back () {
		var breadcrumb = HtmlUtil.popBreadcrumb();
		
		if (breadcrumb.href) {
			SendUtil.href('/' + breadcrumb.href);
		}
	}
	
	function toForm (formId) {
		if (formId) {
			SendUtil.hrefOpenWindow('/formSearch/search', formId);
		} else {
			var headForm = form2object('headForm');
			HtmlUtil.putBreadcrumb('formSearch/search/' + headForm.formId);
			SendUtil.hrefOpenWindow('/formSearch/search', headForm.sourceId);
		}
	}

	function gotoFunction (uri,isOpenWindow) {
		DialogUtil.showLoading();
		SessionUtil.clear();
		
		var postForm = 'form#postForm';		
		if($(postForm).length == 0) {
			$('div#breadcrumb').append("<form id='postForm' class='hidden' action='' method='post'></form>");
		}
		
		if(isOpenWindow) {
			$(postForm).attr("target","_blank");
			DialogUtil.hideLoading();
			
			let menuUrls = $("div#menu").find("a");
			$.each(menuUrls,function() {
				$(this).attr("class","");
			});
		} else {
			$(postForm).attr("target","");
		}
		
		$(postForm).attr('action', contextPath + uri);
		$(postForm).submit();
	}
</script>
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<script>
var ajaxSetting = {async:false};
var baseFormInfo = ObjectUtil.parse('${info}');

$(function () {
	init();
	tabClickListener();
	HtmlUtil.initTabs();
});

function init () {
	if (baseFormInfo.isSearch) {
		baseFormInfo = baseFormInfo.formInfo;
	}
	
	SendUtil.post('/formDispatcher/header', baseFormInfo, function (response) {
		$('div#formHeader').html(response);
	}, ajaxSetting);
	
	SendUtil.post('/formDispatcher/formInfo', baseFormInfo, function (response) {
		$('div#tabContent').html(response);
	}, null, true);
}

function tabClickListener () {
	$('li#tabFormInfo').unbind('click').click(function () {
		HtmlUtil.temporary();
		SendUtil.post('/formDispatcher/formInfo', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabProgram').unbind('click').click(function () {
		HtmlUtil.temporary();
		SendUtil.post('/formDispatcher/program', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabLog').unbind('click').click(function () {
		HtmlUtil.temporary();
		SendUtil.post('/formDispatcher/log', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabCheckLog').unbind('click').click(function () {
		HtmlUtil.temporary();
		SendUtil.post('/formDispatcher/checkLog', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabFileList').unbind('click').click(function () {
		HtmlUtil.temporary();
		SendUtil.post('/formDispatcher/fileList', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabLinkList').unbind('click').click(function () {
		HtmlUtil.temporary();
		SendUtil.post('/formDispatcher/linkList', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabCheckPerson').unbind('click').click(function () {
		HtmlUtil.temporary();
		SendUtil.post('/formDispatcher/checkPerson', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabLibrary').unbind('click').click(function () {
		HtmlUtil.temporary();
		SendUtil.post('/formDispatcher/library', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabFormImpactAnalysis').unbind('click').click(function () {
		HtmlUtil.temporary();
		SendUtil.post('/formDispatcher/impactAnalysis', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabAp1').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'AP1';
		SendUtil.post('/formDispatcher/ap', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabAp2').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'AP2';
		SendUtil.post('/formDispatcher/ap', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabAp3').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'AP3';
		SendUtil.post('/formDispatcher/ap', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabAp4').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'AP4';
		SendUtil.post('/formDispatcher/ap', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabSp').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'SP';
		SendUtil.post('/formDispatcher/other', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabDc1').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'DC1';
		SendUtil.post('/formDispatcher/dc', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabDc2').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'DC2';
		SendUtil.post('/formDispatcher/dc', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabDc3').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'DC3';
		SendUtil.post('/formDispatcher/dc', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabBatch').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'BATCH';
		SendUtil.post('/formDispatcher/other', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabPt').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'PT';
		SendUtil.post('/formDispatcher/other', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabPlan').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'PLAN';
		SendUtil.post('/formDispatcher/planmgmt', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabMgmt').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'MGMT';
		SendUtil.post('/formDispatcher/planmgmt', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabEa').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'EA';
		SendUtil.post('/formDispatcher/other', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabOa').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'OA';
		SendUtil.post('/formDispatcher/other', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabPrograms').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'PROGRAMS';
		SendUtil.post('/formDispatcher/working', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabDb').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'DB';
		SendUtil.post('/formDispatcher/working', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabOpen').unbind('click').click(function () {
		HtmlUtil.temporary();
		baseFormInfo.tabType = 'OPEN';
		SendUtil.post('/formDispatcher/working', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
	
	$('li#tabProcessInfo').unbind('click').click(function () {
		HtmlUtil.temporary();
		SendUtil.post('/formDispatcher/processInfo', baseFormInfo, function (response) {
			$('div#tabContent').html(response);
		}, null, true);
	});
}
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<div id='formHeader'></div>
	<ul id='tabBar' class="nav nav-tabs">
		<li id='tabFormInfo' class="active"><a href="#formInfo"><s:message code='form.title.tabFormInfo' text='表單資訊'/></a></li>
		<li id='tabCheckPerson' style="display: none"><a href="#checkPerson"><s:message code='form.title.tabCheckPerson' text='作業關卡'/></a></li>
		<li id="tabLibrary" jobTabName="程式庫" style="display: none"><a href="#library"><s:message code='form.title.tabLibrary' text='程式庫'/></a></li>
		<li id="tabFormImpactAnalysis" style="display: none"><a href="#impactAnalysis"><s:message code='form.title.tabFormImpactAnalysis' text='衝擊分析'/></a></li>
		<li id='tabProgram' style="display: none"><a href="#program"><s:message code='form.title.tabProgram' text='處理方案'/></a></li>
		<li id='tabCheckLog' style="display: none"><a href="#checkLog"><s:message code='form.title.tabCheckLog' text='審核'/></a></li>
		<li id='tabLog' style="display: none"><a href="#log"><s:message code='form.title.tabLog' text='日誌'/></a></li>
		<li id='tabFileList' style="display: none"><a href="#fileList"><s:message code='form.title.tabFileList' text='附件'/></a></li>
		<li id='tabLinkList' style="display: none"><a href="#linkList"><s:message code='form.title.tabLinkList' text='關聯表單'/></a></li>
		<li id='tabAp1' jobTabName="設一" style="display: none"><a href="#ap1"><s:message code='form.title.tabAp1' text='設一'/></a></li>
		<li id='tabAp2' jobTabName="設二" style="display: none"><a href="#ap2"><s:message code='form.title.tabAp2' text='設二'/></a></li>
		<li id='tabAp3' jobTabName="設三" style="display: none"><a href="#ap3"><s:message code='form.title.tabAp3' text='設三'/></a></li>
		<li id='tabAp4' jobTabName="設四" style="display: none"><a href="#ap4"><s:message code='form.title.tabAp4' text='設四'/></a></li>
		<li id='tabSp' jobTabName="系統" style="display: none"><a href="#sp"><s:message code='form.title.tabSp' text='系統'/></a></li>
		<li id='tabDc1' jobTabName="ONLINE" style="display: none"><a href="#dc1"><s:message code='form.title.tabDc1' text='ONLINE'/></a></li>
		<li id='tabDc2' jobTabName="BATCH" style="display: none"><a href="#dc2"><s:message code='form.title.tabDc2' text='BATCH'/></a></li>
		<li id='tabDc3' jobTabName="OPEN" style="display: none"><a href="#dc3"><s:message code='form.title.tabDc3' text='OPEN'/></a></li>
		<li id='tabPt' jobTabName="連管" style="display: none"><a href="#pt"><s:message code='form.title.tabPt' text='連管'/></a></li>
		<li id='tabPlan' jobTabName="資安規劃" style="display: none"><a href="#plan"><s:message code='form.title.tabPlan' text='資安規劃'/></a></li>
		<li id='tabMgmt' jobTabName="資安管理" style="display: none"><a href="#mgmt"><s:message code='form.title.tabMgmt' text='資安管理'/></a></li>
		<li id='tabEa' jobTabName="電商" style="display: none"><a href="#ea"><s:message code='form.title.tabEa' text='電商'/></a></li>
		<li id='tabOa' jobTabName="OA" style="display: none"><a href="#oa"><s:message code='form.title.tabOa' text='OA'/></a></li>
		<li id='tabBatch' jobTabName="批次" style="display: none"><a href="#batch"><s:message code='form.title.tabBatch' text='批次'/></a></li>
		<li id='tabDb' jobTabName="DB變更" style="display: none"><a href="#db"><s:message code='form.title.tabDb' text='DB變更'/></a></li>
		<li id='tabOpen' jobTabName="Open清單" style="display: none"><a href="#open"><s:message code='form.title.tabOpen' text='Open清單'/></a></li>
		<li id='tabPrograms' jobTabName="程式清單" style="display: none"><a href="#programs"><s:message code='form.title.tabPrograms' text='程式清單'/></a></li>
		<li id='tabProcessInfo' style="display: none"><a href="#processInfo"><s:message code='form.title.tabProcessInfo' text='流程資訊'/></a></li>
	</ul>
	<div id='tabContent'></div>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
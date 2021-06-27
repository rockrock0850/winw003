<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<script>//# sourceURL=incStatisticReportAPIssue.js
var timeIdList = [];
var request, abnormalList, detailList;
var ajaxSetting = {async:false};

$(function () {
	initEvent();
});

function initEvent () {
	$('#searchBtn').click(function () {
		request = form2object('reportForm');
		
		if(!request.startDate || !request.endDate){
			alert('<s:message code="form.report.search.dateNotBlank" text="" />');
			return;
		}
		
		if(isSameSearchCondition(request) && 
				!confirm('<s:message code="form.report.search.noChange" text="" />')){
			return;
		}

		var dates = DateUtil.getMonths(
				request.startDate, request.endDate);
		timeIdList = [];
		$.each(dates, function (i, data) {
			year = data.getFullYear();
			month = data.getMonth() + 1;
			month = month.toString().length < 2 ? '0' + month : month; 
			timeId = year + "/" + month;
			timeIdList.push(timeId);
		});

		initTable(request);
		
		$('.year').html(request.startDate);
		request.months = timeIdList;
		request.endInterval = request.endDate;
		request.startInterval = request.startDate;
		request.operation = 'iNCStatisticReportAPIssue';
		request.isExclude = request.excludeCbx ? 'Y' : 'N';
		SendUtil.post("/reportOperation/search", request, function (response) {
			$('input#isSearched').prop('checked', true);
			$('input#recordIsExclude').val(request.isExclude);
			$('input#recordStartDate').val(request.startInterval);
			$('input#recordEndDate').val(request.endInterval);
			$('fieldset#detail').show();
			$('fieldset#abnormal').show();
			TableUtil.reDraw(detailList, response.resultMap.detailList);
			TableUtil.reDraw(abnormalList, response.resultMap.abnormalList);
		}, null, true);
	});

	$('#exportBtn').click(function () {
		var records = form2object('recordForm');
		request = form2object('reportForm');
		
		if(!request.startDate || !request.endDate){
			alert('<s:message code="form.report.search.dateNotBlank" text="" />');
			return;
		}
		
		if (!records.isSearched) {
			alert('<s:message code="form.report.search.noData" text="" />');
			return;
		}
		
		request.months = timeIdList;
		request.endInterval = request.endDate;
		request.startInterval = request.startDate;
		request.operation = 'iNCStatisticReportAPIssue';
		request.isExclude = request.excludeCbx ? 'Y' : 'N';
		SendUtil.formPost('/reportOperation/export', request);
	});
}

function initTable (request) {
	var row = '<th>{0}</th>';
	var options = {
		columnDefs: [
			{targets: '_all', className: 'dt-center'},
			{targets: [0], width: '20%'}
		]
	};
	
	// 應用系統異常事件
	var year, month;
	var $tr = $('<tr></tr>');
	$tr.append(StringUtil.format(row, "系統名稱"));
	$tr.append(StringUtil.format(row, "臨界值"));
	$.each(timeIdList, function (i, timeId) {
		$tr.append(StringUtil.format(row, timeId));
	});
	$tr.append(StringUtil.format(row, '總和'));

	TableUtil.deleteTable('#abnormalList');	
	$('#abnormalList thead').append($tr.clone());
	abnormalList = TableUtil.init('#abnormalList', options);
	
	// 各科未結案事件單追蹤統計表 (應用系統異常事件明細)
	options = {
		columnDefs: [
			{orderable: false, targets: '_all'}, 
			{targets: [0], title: '主辦科', data: 'Division', className: 'dt-center'}, 
			{targets: [1], title: '表單編號', data: 'FormId', className: 'dt-center'}, 
			{targets: [2], title: '處理人員', data: 'Name', className: 'dt-center'}, 
			{targets: [3], title: '表單狀態', data: 'FormStatus', className: 'dt-center'}, 
			{targets: [4], title: '事件發生時間', data: 'CreateTime', className: 'dt-center', 
					render: function (data) {
						return DateUtil.toDate(data);
					}}, 
			{targets: [5], title: '預計完成時間', data: 'ECT', className: 'dt-center', 
					render: function (data) {
						return DateUtil.toDate(data);
					}},
			{targets: [6], title: '摘要', data: 'Summary', className: 'dt-center'},
			{targets: [7], title: '未結案會辦單', data: 'Countersigned', className: 'dt-center'},
			{targets: [8], title: '', data: 'UserId', className: 'dt-center hidden'}
		]
	};
	TableUtil.deleteTable('#detailList');	
	detailList = TableUtil.init('#detailList', options);
}

function isSameSearchCondition (request) {
	var isSecurity = request.excludeCbx ? 'Y' : 'N';
	return $('input#recordIsExclude').val() == isSecurity && 
			$('input#recordStartDate').val() == request.startDate && 
			$('input#recordEndDate').val() == request.EndDate;
}
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="report.operation.title.inc.statisticReportAPIssue" text='事件單應用系統異常統計表' /></h1>
	<form id='recordForm'>
		<input id="recordStartDate" name='recordStartDate' type="hidden" />
		<input id="recordEndDate" name='recordEndDate' type="hidden" />
		<input id="recordIsExclude" name='recordIsExclude' type="hidden" />
		<input id="isSearched" name='isSearched' class='hidden' type="checkbox" />
	</form>
	
	<c:import url="/WEB-INF/jsp/common/models/formReportSearchModel.jsp">
		<c:param name="month" value="report.operation.3" />
    	<c:param name="excludeCbId" value="excludeCbx" />
    	<c:param name="searchBtId" value="searchBtn" />
    	<c:param name="exportBtId" value="exportBtn" />
    	<c:param name="layout" value="1" />
    	<c:param name="action" value="6" />
		<c:param name="showConditionTitle" value="Y" />
	</c:import>	

	<fieldset id='abnormal' class='hidden'>
		<legend>應用系統異常事件</legend>
		<table id="abnormalList" class="grid_list">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
	
	<fieldset id='detail' class='hidden'>
		<legend>各科未結案事件單追蹤統計表 (應用系統異常事件明細)</legend>
		<table id="detailList" class="grid_list nowrap">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
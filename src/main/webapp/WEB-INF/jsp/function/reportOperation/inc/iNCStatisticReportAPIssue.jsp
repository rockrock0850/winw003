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
	
	// ????????????????????????
	var year, month;
	var $tr = $('<tr></tr>');
	$tr.append(StringUtil.format(row, "????????????"));
	$tr.append(StringUtil.format(row, "?????????"));
	$.each(timeIdList, function (i, timeId) {
		$tr.append(StringUtil.format(row, timeId));
	});
	$tr.append(StringUtil.format(row, '??????'));

	TableUtil.deleteTable('#abnormalList');	
	$('#abnormalList thead').append($tr.clone());
	abnormalList = TableUtil.init('#abnormalList', options);
	
	// ??????????????????????????????????????? (??????????????????????????????)
	options = {
		columnDefs: [
			{orderable: false, targets: '_all'}, 
			{targets: [0], title: '?????????', data: 'Division', className: 'dt-center'}, 
			{targets: [1], title: '????????????', data: 'FormId', className: 'dt-center'}, 
			{targets: [2], title: '????????????', data: 'Name', className: 'dt-center'}, 
			{targets: [3], title: '????????????', data: 'FormStatus', className: 'dt-center'}, 
			{targets: [4], title: '??????????????????', data: 'CreateTime', className: 'dt-center', 
					render: function (data) {
						return DateUtil.toDate(data);
					}}, 
			{targets: [5], title: '??????????????????', data: 'ECT', className: 'dt-center', 
					render: function (data) {
						return DateUtil.toDate(data);
					}},
			{targets: [6], title: '??????', data: 'Summary', className: 'dt-center'},
			{targets: [7], title: '??????????????????', data: 'Countersigned', className: 'dt-center'},
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
	<h1><s:message code="report.operation.title.inc.statisticReportAPIssue" text='????????????????????????????????????' /></h1>
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
		<legend>????????????????????????</legend>
		<table id="abnormalList" class="grid_list">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
	
	<fieldset id='detail' class='hidden'>
		<legend>??????????????????????????????????????? (??????????????????????????????)</legend>
		<table id="detailList" class="grid_list nowrap">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
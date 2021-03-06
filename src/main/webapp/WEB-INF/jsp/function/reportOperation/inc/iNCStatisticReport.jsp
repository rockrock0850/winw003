<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<script>//# sourceURL=incStatisticReport.js
var timeIdList = [];
var ajaxSetting = {async:false};
var request, rankList, abnormalList, machineList;

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
		request.operation = 'iNCStatisticReport';
		request.isExclude = request.excludeCbx ? 'Y' : 'N';
		SendUtil.post("/reportOperation/search", request, function (response) {
			$('input#isSearched').prop('checked', true);
			$('input#recordIsExclude').val(request.isExclude);
			$('input#recordStartDate').val(request.startInterval);
			$('input#recordEndDate').val(request.endInterval);
			$('fieldset#rank').show();
			$('fieldset#abnormal').show();
			$('fieldset#machine').show();
			TableUtil.reDraw(rankList, response.resultMap.rankList);
			TableUtil.reDraw(abnormalList, response.resultMap.abnormalList);
			TableUtil.reDraw(machineList, response.resultMap.machineList);
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
		request.operation = 'iNCStatisticReport';
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
	
	// ???????????????????????????
	TableUtil.deleteTable('#rankList');	
	SendUtil.get('/html/getDropdownList', 4, function (response) {
		var $tr = $('<tr></tr>');
		
		$tr.append(StringUtil.format(row, '????????????'));
		$.each(response, function (i, data) {
			$tr.append(StringUtil.format(row, data.wording));
		});
		$tr.append(StringUtil.format(row, '??????'));
		
		$('#rankList thead').append($tr);
	}, ajaxSetting);
	rankList = TableUtil.init('#rankList', options);
	
	// ???????????????????????????????????????
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

	TableUtil.deleteTable('#machineList');
	$('#machineList thead').append($tr.clone());
	machineList = TableUtil.init('#machineList', options);
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
	<h1><s:message code="report.operation.title.inc.statisticReport" text='???????????????????????????' /></h1>
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
	
	<fieldset id='rank' class='hidden'>
		<legend>???????????????????????????</legend>
		<table id="rankList" class="grid_list">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>

	<fieldset id='abnormal' class='hidden'>
		<legend>????????????????????????</legend>
		<table id="abnormalList" class="grid_list">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>

	<fieldset id='machine' class='hidden'>
		<legend>????????????</legend>
		<table id="machineList" class="grid_list">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
		
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
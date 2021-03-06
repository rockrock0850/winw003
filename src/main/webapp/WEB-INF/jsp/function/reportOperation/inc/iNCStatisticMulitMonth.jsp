<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<style>
  .ui-datepicker-month,
  .ui-datepicker-prev,
  .ui-datepicker-next,
  .ui-datepicker-calendar {
   		display: none;
   }
</style>
<script>//# sourceURL=incStatisticMulitMonth.js
var request, tableClass, tableReason, tablePriority;

$(function () {
	initTable();
	initEvent();
});

function initTable () {
	var options = {
		info: false,
		paging: false,
		ordering: false,
		pageLength: -1,
		columnDefs: [
			{targets: '_all', className: 'dt-center'}
		]
	};
	tableClass = TableUtil.init('#tableClass', options);
	tableReason = TableUtil.init('#tableReason', options);
	tablePriority = TableUtil.init('#tablePriority', options);
}

function initEvent () {
	$('#searchBtn').click(function () {
		request = form2object('reportForm');
		
		if(!request.startDate){
			alert('<s:message code="form.report.search.dateNotBlank" text="" />');
			return;
		}
		
		if(isSameSearchCondition(request) && 
				!confirm('<s:message code="form.report.search.noChange" text="" />')){
			return;
		}
		
		$('.year').html(request.startDate);
		request.startInterval = request.startDate;
		request.operation = 'iNCStatisticMulitMonth';
		request.isExclude = request.excludeCbx ? 'Y' : 'N';
		SendUtil.post("/reportOperation/search", request, function (response) {
			$('input#isSearched').prop('checked', true);
			$('input#recordIsExclude').val(request.isExclude);
			$('input#recordStartDate').val(request.startInterval);
			$('fieldset#class').show();
			$('fieldset#reason').show();
			$('fieldset#priority').show();
			TableUtil.reDraw(tableClass, TableUtil.fromObjList(response.resultDataList.tableClass));
			TableUtil.reDraw(tableReason, TableUtil.fromObjList(response.resultDataList.tableReason));
			TableUtil.reDraw(tablePriority, TableUtil.fromObjList(response.resultDataList.tablePriority));
		}, null, true);
	});

	$('#exportBtn').click(function () {
		var records = form2object('recordForm');
		request = form2object('reportForm');
		
		if(!request.startDate){
			alert('<s:message code="form.report.search.dateNotBlank" text="" />');
			return;
		}
		
		if (!records.isSearched) {
			alert('<s:message code="form.report.search.noData" text="" />');
			return;
		}

		request.startInterval = request.startDate;
		request.operation = 'iNCStatisticMulitMonth';
		request.isExclude = request.excludeCbx ? 'Y' : 'N';
		SendUtil.formPost('/reportOperation/export', request);	       		
	});
}

function isSameSearchCondition (request) {
	var isSecurity = request.excludeCbx ? 'Y' : 'N';
	return $('input#recordIsExclude').val() == isSecurity && 
			$('input#recordStartDate').val() == request.startDate;
}
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="report.operation.title.inc.statisticMulitMonth" text='??????????????????????????????' /></h1>
	<form id='recordForm'>
		<input id="recordStartDate" name='recordStartDate' type="hidden" />
		<input id="recordIsExclude" name='recordIsExclude' type="hidden" />
		<input id="isSearched" name='isSearched' class='hidden' type="checkbox" />
	</form>
	<c:import url="/WEB-INF/jsp/common/models/formReportSearchModel.jsp">
		<c:param name="month" value="report.operation.tltle.year" />
    	<c:param name="excludeCbId" value="excludeCbx" />
    	<c:param name="searchBtId" value="searchBtn" />
    	<c:param name="exportBtId" value="exportBtn" />
    	<c:param name="layout" value="3" />
    	<c:param name="action" value="5" />
		<c:param name="showConditionTitle" value="Y" />
	</c:import>	
	
	<fieldset id='class' class='hidden'>
		<legend>????????????????????????</legend>
		<table id="tableClass" class="grid_list subToltal">
			<thead>
				<tr>
					<th rowspan="3">???????????????</th>
					<th class='year' colspan="12">2019???</th>
					<th rowspan="3">??????</th>
				</tr>
				<tr>
					<th colspan="3">1???</th>
					<th colspan="3">2???</th>
					<th colspan="3">3???</th>
					<th colspan="3">4???</th>
				</tr>
				<tr>
					<th>1???</th>
					<th>2???</th>
					<th>3???</th>
					<th>4???</th>
					<th>5???</th>
					<th>6???</th>
					<th>7???</th>
					<th>8???</th>
					<th>9???</th>
					<th>10???</th>
					<th>11???</th>
					<th>12???</th>
				</tr>
	        </thead>
	        <tbody></tbody>
		</table>
	</fieldset>
	
	<fieldset id='reason' class='hidden'>
		<legend>???????????????????????????</legend>
		<table id="tableReason" class="grid_list subToltal">
			<thead>
				<tr>
					<th rowspan="3">??????????????????</th>
					<th class='year' colspan="12">2019???</th>
					<th rowspan="3">??????</th>
				</tr>
				<tr>
					<th colspan="3">1???</th>
					<th colspan="3">2???</th>
					<th colspan="3">3???</th>
					<th colspan="3">4???</th>
				</tr>
				<tr>
					<th>1???</th>
					<th>2???</th>
					<th>3???</th>
					<th>4???</th>
					<th>5???</th>
					<th>6???</th>
					<th>7???</th>
					<th>8???</th>
					<th>9???</th>
					<th>10???</th>
					<th>11???</th>
					<th>12???</th>
				</tr>
	        </thead>
	        <tbody></tbody>
		</table>
	</fieldset>
	
	<fieldset id='priority' class='hidden'>
		<legend>?????????????????????????????????????????????</legend>
		<table id="tablePriority" class="grid_list subToltal">
			<thead>
				<tr>
					<th rowspan="3">????????????????????????</th>
					<th class='year' colspan="12">2019???</th>
					<th rowspan="3">??????</th>
				</tr>
				<tr>
					<th colspan="3">1???</th>
					<th colspan="3">2???</th>
					<th colspan="3">3???</th>
					<th colspan="3">4???</th>
				</tr>
				<tr>
					<th>1???</th>
					<th>2???</th>
					<th>3???</th>
					<th>4???</th>
					<th>5???</th>
					<th>6???</th>
					<th>7???</th>
					<th>8???</th>
					<th>9???</th>
					<th>10???</th>
					<th>11???</th>
					<th>12???</th>
				</tr>
	        </thead>
	        <tbody></tbody>
		</table>
	</fieldset>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
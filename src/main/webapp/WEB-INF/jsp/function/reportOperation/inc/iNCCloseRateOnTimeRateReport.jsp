<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<script>//# sourceURL=incCloseRate.js
var saveRemoveId = [];

$(function () {
	$('#searchBtn').click(function(){
		if ($("#startDate").val() == '') {
			alert('<s:message code="form.report.search.monthNotBlank" text="" />');
			return;
		}
		var check = 'N';
		if ($("#excludeCbx").prop("checked")) {
			check = 'Y';
		}		
		if ($('#sendStartDate').val() == ($("#startDate").val()) && 
				$('#sendEndDate').val() == ($("#endDate").val()) &&
				$('#sendEventType').val() == ($("#eventType").val()) &&
				$('#sendIsExclude').val() == check) {
			if (!confirm('<s:message code="form.report.search.noChange" text="" />')) {
				return;
			}
		}
		
		TableUtil.deleteTable('#tableDataList');
		$.each(saveRemoveId,function(index,value){
			$(value).remove();
		});
		
		saveRemoveId = [];		
		var vos ={}
		
		if ($("#excludeCbx").prop("checked")) {
			vos.isExclude = 'Y';
		} else {
			vos.isExclude = 'N';
		}
		vos.startInterval = $("#startDate").val();
		vos.endInterval = $("#endDate").val();
		vos.eventType = $("#eventType").val();
		vos.operation = 'iNCCloseRateOnTimeRateReport';
		SendUtil.post("/reportOperation/search", vos, function(data){
			$("#resultField1").css('display','block');
			$.each(data,function(keys,values){
				if (keys == 'startInterval') {
					$('#sendStartDate').val(values);
				} else if (keys == 'endInterval') {
					$('#sendEndDate').val(values);
				} else if (keys == 'eventType') { //事件類型
					$('#sendEventType').val(values);
				} else if (keys == 'isExclude') {
					$('#sendIsExclude').val(values);
				} else if (keys == 'statisticsResult') {
					$('#cloneTableResult').clone(true).attr('id','tableResult').insertAfter('#resultLegend1:last');
					saveRemoveId.push('#tableResult');
					var html = '';
					
					var count = 0;
					var i;
					for (i in values) {
					    if (values.hasOwnProperty(i)) {
					        count++;
					    }
					}
					if (count > 1) {
						var j = 1;
						$.each(values,function(k,v){
							if (count == j) {
								html += '<tr align="center" style="font-weight:bold;" bgcolor="#DDDDDD">';
							} else {
								html += '<tr align="center">';
							}
							
							$.each(v,function(k1,v1){
								if (v1 == 'total') {
									html += '<td><s:message code="report.operation.inc.form.total" text="" /></td>';
								} else if (v1 == null) {
									html += '<td>-</td>';
								} else {
									if (k1 == 5) {
										html += '<td>' + v1 + '%</td>';									
									} else {
										html += '<td>' + v1 + '</td>';
									}
								}
							});

							html += '</tr>';
							j++;
							
						});
						$('#noData').val("N");
					} else {
						html +='<tr align="center"><td>-</td><td><s:message code="form.report.search.noData" text="" /></td></tr>';
						$('#noData').val("");
					}
					$('#tableResult tbody').empty().append(html);
				} else if (keys == 'resultDataList') {
					var total = 0;
					$.each(values,function(k,v){
						$('#cloneTableTotal').clone(true).attr('id','tableTotal').insertAfter('#resultLegend2:last');					
						$('#cloneTableUnfinishedOnScheduled').clone(true).attr('id','tableDataList').insertAfter('#resultLegend2:last');
						total = total + v.length;
						if (total > 0) {
							$("#resultField2").css('display','block');
						} else {
							$("#resultField2").css('display','none');
						}
						saveRemoveId.push('#tableTotal');							
						saveRemoveId.push('#tableDataList');
						var options = {
								data: v,
								columnDefs: [
								{orderable: false, targets: []},
								{targets: [0], data: 'pickMonth', className: 'dt-center'}, 
								{targets: [1], data: 'formid', className: 'dt-center'}, 
								{targets: [2], data: 'ECT', className: 'dt-center'}, 
								{targets: [3], data: 'ACT', className: 'dt-center'}, 
								{targets: [4], data: 'FormStatus', className: ''},
								{targets: [5], data: 'EventPriority', className: 'dt-center'},
								{targets: [6], data: 'Summary', className: ''},
								{targets: [7], data: 'usersolving', className: 'dt-center'}, 
								{targets: [8], data: 'section', className: 'dt-center'},
								{targets: [9], data: 'eventType', className: 'dt-center'}
								]
						};
						TableUtil.init('#tableDataList', options);						
					});
					$('#tableTotal tr').children().eq(1).text(total);	
				}
			});
		},null,true);		
	});	

	$('#exportBtn').click(function(){
		if (saveRemoveId.length == 0 || $('#noData').val() == '') {
			alert('<s:message code="form.report.search.noData" text="" />');
			return;
		}
		if ($('#sendStartDate').val() == '' || $('#sendEndDate').val() == '' || $('#sendIsExclude').val() == '') {
			alert('<s:message code="form.report.search.reSearch" text="" />');
			return;
		}		
		var postData = {};
		postData["operation"] = 'iNCCloseRateOnTimeRateReport';
		postData["isExclude"] = $("#sendIsExclude").val();
		postData["startInterval"] = $("#sendStartDate").val();
		postData["endInterval"] = $("#sendEndDate").val();
		postData["eventType"] = $("#sendEventType").val();
		SendUtil.formPost('/reportOperation/export', postData);				
	});
});
</script>

</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="report.operation.title.inc.closeRateOnTimeRateReport" text='事件如期結案比率報告' /></h1>
	
	<c:import url="/WEB-INF/jsp/common/models/formReportSearchModel.jsp">
		<c:param name="month" value="report.operation.year.month" />
    	<c:param name="excludeCbId" value="excludeCbx" />
		<c:param name="eventType" value="form.search.column.inc.eventType" />
    	<c:param name="searchBtId" value="searchBtn" />
    	<c:param name="exportBtId" value="exportBtn" />
    	<c:param name="layout" value="1" />
    	<c:param name="eventLayout" value="1" />
    	<c:param name="action" value="1" />
		<c:param name="showConditionTitle" value="Y" />
	</c:import>

	<fieldset id='resultField1' style='display:none'>
		<legend id="resultLegend1"><s:message code="global.query.result" text='查詢結果' /></legend>
	</fieldset>
	
	<fieldset id='resultField2' style='display:none'>
		<legend id="resultLegend2"><s:message code="report.operation.unsolved.as.scheduled" text='未如期完成明細表' /></legend>
	</fieldset>
	
	<div style="display:none">
		<table id="cloneTableResult" class="grid_list" style="width: 100%">
			<thead>
				<tr align="center">
					<th width="13%"><s:message code="report.operation.start.end" text='報告年月(起、訖)' /></th>
					<th width="13%"><s:message code="form.question.form.info.urgent.level" text='緊急程度' /></th>
					<th width="13%"><s:message code="report.operation.accept.event" text='受理事件數' /><br>(C)</th>
					<th width="13%"><s:message code="report.operation.solved.as.scheduled" text='如期解決事件數' /><br>(B)</th>
					<th width="12%"><s:message code="report.operation.unsolved.as.scheduled" text='未如期完成明細表' /></th>
					<th width="12%"><s:message code="report.operation.closing.rate" text='如期結案率' /></th>
					<th width="12%"><s:message code="report.operation.average.solved.time" text='事件平均解決時間' /><br><s:message code="report.operation.dd.hh.MM.ss" text='(日:時:分:秒)' /></th>
					<th width="12%"><s:message code="report.operation.average.solved.time" text='事件平均解決時間' /><br><s:message code="report.operation.second.total" text='(總秒)' /></th>
				</tr>
			</thead>	
		 	<tbody></tbody>
		</table>
		<table id="cloneTableUnfinishedOnScheduled" class="grid_list" style="width: 100%;word-break:break-all; word-wrap:break-all;">
			<thead>
				<tr align="center">
					<th width="6%" align="center"><s:message code="report.operation.year.month" text='報告年月' /></th>
					<th width="11%" align="center"><s:message code="form.report.search.serialnumber" text='表單編號' /></th>
					<th width="14%" align="center"><s:message code="report.operation.event.exclude.time" text='事件排除時間' /></th>
					<th width="14%" align="center"><s:message code="report.operation.target.solved.time" text='目標解決時間' /></th>
					<th width="14%" align="center"><s:message code="form.report.search.formStatus" text='表單狀態' /></th>
					<th width="5%" align="center"><s:message code="report.operation.event.grade" text='事件等級' /></th>
					<th width="17%" align="center"><s:message code="report.operation.event.summary" text='事件摘要' /></th>
					<th width="5%" align="center"><s:message code="form.question.process.user.name" text='處理人員' /></th>
					<th width="5%" align="center"><s:message code="form.question.process.division" text='處理科別' /></th>	
					<th width="8%" align="center"><s:message code="form.search.column.inc.eventType" text='事件類型' /></th>					
				</tr>
			</thead>	
		 	<tbody></tbody>
		</table>
		<table id="cloneTableTotal" class="grid_query">
			<tr>
				<th><s:message code="q.report.operation.specialcase.total" text='總數' /></th>
				<td></td>
			</tr>
		</table>			
	</div>		
	
	<input type="hidden" id="sendEventType" value=""/>
	<input type="hidden" id="sendStartDate" value=""/>
	<input type="hidden" id="sendEndDate" value=""/>
	<input type="hidden" id="sendIsExclude" value=""/>
	<input type="hidden" id="noData" value=""/>		
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	

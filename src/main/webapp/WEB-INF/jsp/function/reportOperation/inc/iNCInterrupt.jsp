<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<script>
	var saveRemoveId = [];
	var saveRemoveId2 = [];
	$(function () {
		$('#searchBtn').click(function(){
			if($("#startDate").val()=='' || $("#endDate").val()==''){
				alert('<fmt:message key="form.report.search.dateNotBlank"/>');
				return;
			}
			var check = 'N';
			if($("#excludeCbx").prop("checked")){
				check = 'Y';
			}		
			if($('#sendStartDate').val() == $("#startDate").val()
				 && $('#sendEndDate').val()==$("#endDate").val() 
					&& $('#sendIsExclude').val()==check){
				if(!confirm('<fmt:message key="form.report.search.noChange"/>')){
					return;
				}
			}
			
			$.each(saveRemoveId,function(index,value){
				TableUtil.deleteTable('#body'+value);
				$('#body'+value).remove();
				$('#br1'+value).remove();
				$('#br2'+value).remove();
			});
			saveRemoveId = [];
			
			$.each(saveRemoveId2,function(index,value){
				TableUtil.deleteTable('#body2'+value);
				$('#body2'+value).remove();
			});
			saveRemoveId2 = [];
			
			var vos ={}
				if($("#excludeCbx").prop("checked")){
					vos.isExclude = 'Y';
				}else{
					vos.isExclude = 'N';
				}
			vos.startInterval=$("#startDate").val();
			vos.endInterval=$("#endDate").val();
			vos.operation = 'iNCInterrupt';
			SendUtil.post("/reportOperation/search",vos,function(data){	
				$('#resultField').css('display','block');
				$.each(data,function(keys,values){
					if(keys=='startInterval'){
						$('#sendStartDate').val(values);
					}
					if(keys=='endInterval'){
						$('#sendEndDate').val(values);
					}
					if(keys=='isExclude'){
						$('#sendIsExclude').val(values);
					}	
					if(keys=='resultDataList'){
						var count = 0;
						var i;
						for (i in values) {
						    if (values.hasOwnProperty(i)) {
						        count++;
						    }
						}
						if(count >=1){
							$('#resultField1').css('display','block');
						}
						var total = 0;
						var keyValue = "resultLegend1";
						var num = 1;
						$.each(values,function(k,v){
							$('#cloneBody1').clone(true).attr('id','body'+k).insertAfter('#'+ keyValue +':last');							
							keyValue = 'body'+k;
							if(num < count){
								$('#cloneBr').clone(true).attr('id','br1'+k).insertAfter('#'+ keyValue +':last');
								keyValue = 'br1'+k;
								$('#cloneBr').clone(true).attr('id','br2'+k).insertAfter('#'+ keyValue +':last');
								keyValue = 'br2'+k;
							}
							num++;
							saveRemoveId.push(k);
							total = total + v.length;
							var options = {
									data: v,
									columnDefs: [
									{orderable: false, targets: []},
									{targets: [0], data: 'FormId', className: 'dt-center'}, 
									{targets: [1], data: 'Summary', className: ''}, 
									{targets: [2], data: 'Section', className: 'dt-center'}, 
									{targets: [3], data: 'Name', className: 'dt-center'}, 
									{targets: [4], data: 'FormStatus', className: 'dt-center'},
									{targets: [5], data: 'System', className: ''},
									{targets: [6], data: 'CreateTime', className: 'dt-center'},
									{targets: [7], data: 'ExcludeTime', className: 'dt-center'} 
								]
							};
							TableUtil.init('#body'+k, options);	
						});
						$('#tableTotal1 tr').children().eq(1).text(total);
					}
					if(keys=='resultList'){
						var count = 0;
						var i;
						for (i in values) {
						    if (values.hasOwnProperty(i)) {
						        count++;
						    }
						}
						if(count >=1){
							$('#resultField2').css('display','block');
						}
						var total = values.length;
						var keyValue = "resultLegend2";
						var num = 1;						
						$('#cloneBody2').clone(true).attr('id','body2'+keys).insertAfter('#'+ keyValue +':last');
						saveRemoveId2.push(keys);
						var options = {
								data: values,
								columnDefs: [
								{orderable: false, targets: []},
								{targets: [0], data: 'FormId', className: 'dt-center'}, 
								{targets: [1], data: 'Summary', className: ''}, 
								{targets: [2], data: 'Name', className: 'dt-center'}, 
								{targets: [3], data: 'Working', className: 'dt-center'}, 
								{targets: [4], data: 'display', className: ''},
								{targets: [5], data: 'System', className: ''},
								{targets: [6], data: 'CreateTime', className: 'dt-center'},
								{targets: [7], data: 'OffLineTime', className: 'dt-center'},
								{targets: [8], data: 'OnLineTime', className: 'dt-center'} 
							]
						};
						TableUtil.init('#body2'+keys, options);	
						$('#tableTotal2 tr').children().eq(1).text(total);					
					}
				});
			},null,true);
		});
		
		$('#exportBtn').click(function(){
			if(saveRemoveId.length == 0 && saveRemoveId2.length == 0){
				alert('<fmt:message key="form.report.search.noData"/>');
				return;
			}
			if($('#sendStartDate').val() =='' || $('#sendEndDate').val()=='' || $('#sendIsExclude').val()==''){
				alert('<fmt:message key="form.report.search.reSearch"/>');
				return;
			}
			var postData = {};
			postData["operation"] = 'iNCInterrupt';
			postData["isExclude"] = $("#sendIsExclude").val();
			postData["startInterval"] = $("#sendStartDate").val();
			postData["endInterval"] = $("#sendEndDate").val();
			SendUtil.formPost('/reportOperation/export', postData);		
		});
});
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="report.operation.title.inc.interrupt" text='各服務資訊服務之服務水準達成率' /></h1>
	
	<c:import url="/WEB-INF/jsp/common/models/formReportSearchModel.jsp">
		<c:param name="month" value="report.operation.1" />
    	<c:param name="excludeCbId" value="excludeCbx" />
    	<c:param name="searchBtId" value="searchBtn" />
    	<c:param name="exportBtId" value="exportBtn" />
    	<c:param name="layout" value="1" />
    	<c:param name="action" value="1" />
		<c:param name="showConditionTitle" value="Y" />
	</c:import>	
	
	<fieldset id="resultField"  style="display:none">
		<legend id="resultLegend"><s:message code="schedule.find.legend.result" text='' /></legend>
		<table id="tableTotal1" class="grid_query">
			<tr>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="report.operation.inc.form.interrupt" text='' /></th>
				<td></td>
			</tr>
		</table>
		<br/>
		<table id="tableTotal2" class="grid_query">
			<tr>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="report.operation.inc.form.production" text='' /></th>
				<td></td>
			</tr>			
		</table>		
	</fieldset>	
	<fieldset id="resultField1" style="display:none">
		<legend id="resultLegend1"><s:message code="report.operation.inc.form.interrupt" text='' /></legend>		
	</fieldset>	
	<fieldset id="resultField2" style="display:none">
		<legend id="resultLegend2"><s:message code="report.operation.inc.form.production" text='' /></legend>		
	</fieldset>		
	<div style="display:none">		
		<table id="cloneBody1" class="grid_list" style="width: 100%">
			<thead>
				<tr>
					<th width="13%"><s:message code="form.report.search.serialnumber" text='表單編號' /></th>
					<th width="25%"><s:message code="form.report.search.summary" text='摘要' /></th>
					<th width="8%"><s:message code="form.report.search.host" text='主辦科' /></th>
					<th width="9%"><s:message code="q.report.operation.specialcase.handling" text='經辦' /></th>
					<th width="10%"><s:message code="dashboard.form.formStatus" text='表單狀態' /></th>
					<th width="15%"><s:message code="form.search.column.system" text='系統名稱' /></th>
					<th width="10%"><s:message code="form.search.column.inc.infoDateCreateTime" text='事件發生時間' /></th>
					<th width="10%"><s:message code="report.operation.event.exclude.time" text='事件排除時間' /></th>							
				</tr>
			</thead>
		</table>
		<table id="cloneBody2" class="grid_list" style="width: 100%">
			<thead>
				<tr>
					<th width="13%"><s:message code="form.report.search.serialnumber" text='表單編號' /></th>
					<th width="20%"><s:message code="form.report.search.summary" text='摘要' /></th>
					<th width="8%"><s:message code="q.report.operation.specialcase.handling" text='經辦' /></th>
					<th width="8%"><s:message code="report.operation.inc.work.group" text='SP工作群組' /></th>					
					<th width="10%"><s:message code="form.event.form.info.service.type" text='服務類別' /></th>
					<th width="11%"><s:message code="form.search.column.system" text='系統名稱' /></th>
					<th width="10%"><s:message code="form.question.form.info.report.date" text='報告日期' /></th>
					<th width="10%"><s:message code="form.job.form.offLineTime" text='公告停機時間' /></th>
					<th width="10%"><s:message code="form.job.form.onLineTime" text='公告恢復時間' /></th>							
				</tr>
			</thead>
		</table>		
		<br id='cloneBr' />	
	</div>
	<input type="hidden" id="sendStartDate" value=""/>
	<input type="hidden" id="sendEndDate" value=""/>
	<input type="hidden" id="sendIsExclude" value=""/>			
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
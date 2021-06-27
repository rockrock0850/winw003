<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<script>
	var saveRemoveId = [];
	$(function () {
		$('#searchBtn').click(function(){
			var check = 'N';
			if($("#excludeCbx").prop("checked")){
				check = 'Y';
			}		
			if($('#sendIsExclude').val()==check){
				if(!confirm('<fmt:message key="form.report.search.noChange"/>')){
					return;
				}
			}
			
			$.each(saveRemoveId,function(index,value){
				TableUtil.deleteTable('#body'+value);
				$('#br'+value).remove();
				$('#total'+value).remove();
				$('#body'+value).remove();
			});
			saveRemoveId = [];
			var vos ={}
				if($("#excludeCbx").prop("checked")){
					vos.isExclude = 'Y';
				}else{
					vos.isExclude = 'N';
				}
			vos.operation = 'qSpecialClose';
			SendUtil.post("/reportOperation/search",vos,function(data){		
				var count = 0;
				var i;
				for (i in data) {
				    if (data.hasOwnProperty(i)) {
				        count++;
				    }
				}
				if(count>1){
					$('#resultField1').css('display','block');
					$('#resultField2').css('display','block');
				}else{
					$('#resultField1').css('display','none');
					$('#resultField2').css('display','none');
					var check = 'N';
				}
				if($("#excludeCbx").prop("checked")){
					check = 'Y';
				}				
				$('#sendIsExclude').val(check);	
				var total = 0;
				$.each(data,function(keys,values){
					if(keys=='resultDataList'){					
						$.each(values,function(k,v){
							if(k=='S_END_1'){
								$('#cloneBr').clone(true).attr('id','br'+k).insertAfter('#resultLegend1:last');
								$('#cloneTotal').clone(true).attr('id','total'+k).insertAfter('#resultLegend1:last');
								$('#cloneBody1').clone(true).attr('id','body'+k).insertAfter('#resultLegend1:last');
								saveRemoveId.unshift(k);
								var options = {
										data: v,
										columnDefs: [
											{orderable: false, targets: []},
											{targets: [0], data: 'FormId', className: 'dt-center'}, 
											{targets: [1], data: 'section', className: '',"render": function ( data, type, row, meta ) {
												
												return row.section+'</br>'+row.Countersigneds;
											}}, 
											{targets: [2], data: 'Name', className: 'dt-center',"render": function ( data, type, row, meta ) {
												
												return row.Name+'</br>'+'<s:message code="q.report.operation.specialcase.trace" text="特殊結案待追蹤" />';
											}}, 
											{targets: [3], data: 'CreateTime', className: '',"render": function ( data, type, row, meta ) {
												
												return row.CreateTime+'</br>'+row.ECT+'</br>'+row.ACT;
											}}, 
											{targets: [4], data: 'EffectScope', className: 'dt-center',"render": function ( data, type, row, meta ) {
												
												return row.EffectScope+'</br>'+row.UrgentLevel+'</br>'+row.QuestionPriority;
											}},
											{targets: [5], data: 'Summary', className: 'dt-left'},
											{targets: [6], data: 'Content', className: 'dt-left'},
											{targets: [7], data: 'Indication', className: 'dt-center'},
											{targets: [8], data: 'Reason', className: 'dt-center'},
											{targets: [9], data: 'ProcessProgram', className: 'dt-center'},
											{targets: [10], data: 'IsSuggestCase', className: 'dt-center'}	 
										]
								};
								TableUtil.init('#body'+k, options);	
								$('#total'+ k +' tr').children().eq(1).text(v.length);								
							}
							if(k=='S_END_2'){
								$('#cloneBr').clone(true).attr('id','br'+k).insertAfter('#resultLegend2:last');
								$('#cloneTotal').clone(true).attr('id','total'+k).insertAfter('#resultLegend2:last');
								$('#cloneBody2').clone(true).attr('id','body'+k).insertAfter('#resultLegend2:last');
								saveRemoveId.unshift(k);
								var options = {
										data: v,
										columnDefs: [
											{orderable: false, targets: []},
											{targets: [0], data: 'FormId', className: 'dt-center'}, 
											{targets: [1], data: 'section', className: '',"render": function ( data, type, row, meta ) {
												
												return row.section+'</br>'+row.Countersigneds;
											}}, 
											{targets: [2], data: 'Name', className: 'dt-center',"render": function ( data, type, row, meta ) {
												
												return row.Name+'</br>'+'<s:message code="q.report.operation.specialcase.close" text="特殊結案已結案" />';
											}}, 
											{targets: [3], data: 'CreateTime', className: '',"render": function ( data, type, row, meta ) {
												
												return row.CreateTime+'</br>'+row.ECT+'</br>'+row.ACT;
											}}, 
											{targets: [4], data: 'EffectScope', className: 'dt-center',"render": function ( data, type, row, meta ) {
												
												return row.EffectScope+'</br>'+row.UrgentLevel+'</br>'+row.QuestionPriority;
											}},
											{targets: [5], data: 'Summary', className: 'dt-left'},
											{targets: [6], data: 'Content', className: 'dt-left'}
											 
										]
									};
									TableUtil.init('#body'+k, options);	
									$('#total'+ k +' tr').children().eq(1).text(v.length);
							}					
						});
					}
				});			
			},null,true);
		});
		
		$('#exportBtn').click(function(){
			if(saveRemoveId.length == 0){
				alert('<s:message code="form.report.search.noData" text="" />');
				return;
			}
			if($('#sendIsExclude').val()==''){
				alert('<s:message code="form.report.search.reSearch" text="" />');
				return;
			}
			var postData = {};
			postData["operation"] = 'qSpecialClose';
			postData["isExclude"] = $("#sendIsExclude").val();
			SendUtil.formPost('/reportOperation/export', postData);		       			
		});
	});
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="report.operation.title.q.specialClose" text='特殊結案問題單彙總報' /></h1>
	
	<c:import url="/WEB-INF/jsp/common/models/formReportSearchModel.jsp">
		<c:param name="month" value="" />
    	<c:param name="excludeCbId" value="excludeCbx" />
    	<c:param name="searchBtId" value="searchBtn" />
    	<c:param name="exportBtId" value="exportBtn" />
    	<c:param name="layout" value="6" />
    	<c:param name="action" value="6" />
		<c:param name="showConditionTitle" value="Y" />
	</c:import>
	
	<fieldset class="search">
		<legend><s:message code="q.report.operation.description.title" text='' /></legend>
		<button class="small fieldControl">
				<i class="iconx-collapse"></i>|||
		</button>
     <p><s:message code="q.report.operation.special.close.description" text='「特殊結案待追蹤」者，經由特殊問題會議，若真正可結案者，中文狀態將變成「特殊結案已結案」' /></p>
	</fieldset>	
	
	<fieldset id='resultField1' style='display:none'>
		<legend id="resultLegend1"><s:message code="q.report.operation.specialcase.trace" text='特殊結案待追蹤' /></legend>
	</fieldset>
	
	<fieldset id='resultField2' style='display:none'>
		<legend id="resultLegend2"><s:message code="q.report.operation.specialcase.close" text='特殊結案已結案' /></legend>
	</fieldset>	
			
	<div style="display:none">
		<table id="cloneBody1" class="grid_list" style="width: 100%">
			<thead>
				<tr align="center">
					<th width="10%"><s:message code="form.report.search.serialnumber" text='表單編號' /></th>
					<th width="5%"><s:message code="q.report.operation.specialcase.section" text='科別' />/</br><s:message code="q.report.operation.specialcase.processingUnit" text='會辦科' /></th>
					<th width="10%"><s:message code="q.report.operation.specialcase.handling" text='經辦' />/</br><s:message code="form.report.search.formStatus" text='表單狀態' /></th>
					<th width="10%"><s:message code="dashboard.form.billingTime" text='開單時間' />/</br><s:message code="form.question.form.info.ect" text='預計完成時間' />/</br><s:message code="form.question.form.info.act" text='實際完成時間' /></th>
					<th width="5%"><s:message code="q.report.operation.specialcase.influences" text='影響' />/</br><s:message code="q.report.operation.specialcase.urgent" text='緊急' />/</br><s:message code="q.report.operation.specialcase.order" text='順序' /></th>
					<th width="10%"><s:message code="q.report.operation.specialcase.summary" text='摘要' /></th>
					<th width="15%"><s:message code="q.report.operation.specialcase.content" text='內容' /></th>
					<th width="15%"><s:message code="q.report.operation.specialcase.sign" text='徵兆' /></th>
					<th width="10%"><s:message code="q.report.operation.specialcase.reason" text='原因' /></th>
					<th width="5%"><s:message code="q.report.operation.specialcase.solve" text='解決' />/</br><s:message code="q.report.operation.specialcase.treatmentPlan" text='處理方案' /></th>
					<th width="5%"><s:message code="q.report.operation.specialcase.temporarySolution" text='暫解方案' /></th>						
				</tr>
			</thead>
		</table>
		<table id="cloneBody2" class="grid_list" style="width: 100%">
			<thead>
				<tr align="center">
					<th width="10%"><s:message code="form.report.search.serialnumber" text='表單編號' /></th>
					<th width="5%"><s:message code="q.report.operation.specialcase.section" text='科別' />/</br><s:message code="q.report.operation.specialcase.processingUnit" text='會辦科' /></th>
					<th width="10%"><s:message code="q.report.operation.specialcase.handling" text='經辦' />/</br><s:message code="form.report.search.formStatus" text='表單狀態' /></th>
					<th width="10%"><s:message code="dashboard.form.billingTime" text='開單時間' />/</br><s:message code="form.question.form.info.ect" text='預計完成時間' />/</br><s:message code="form.question.form.info.act" text='實際完成時間' /></th>
					<th width="5%"><s:message code="q.report.operation.specialcase.influences" text='影響' />/</br><s:message code="q.report.operation.specialcase.urgent" text='緊急' />/</br><s:message code="q.report.operation.specialcase.order" text='順序' /></th>
					<th width="25%"><s:message code="q.report.operation.specialcase.summary" text='摘要' /></th>
					<th width="35%"><s:message code="q.report.operation.specialcase.content" text='內容' /></th>								
				</tr>
			</thead>
		</table>
		<table id="cloneTotal" class="grid_query">
			<tr>
				<th><s:message code="q.report.operation.specialcase.totalNumber" text='總件數' /></th>
				<td></td>
			</tr>
		</table>
		<br id='cloneBr' />				
	</div>
	<input type="hidden" id="sendIsExclude" value=""/>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	

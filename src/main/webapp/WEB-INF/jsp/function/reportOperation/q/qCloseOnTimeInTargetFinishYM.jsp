<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<script>
var saveRemoveId = [];
	$(function () {
		$('#searchBtn').click(function(){
			if($("#startDate").val()=='' || $("#endDate").val()==''){
				alert('<s:message code="form.report.search.monthNotBlank" text="" />');
				return;
			}
			var check = 'N';
			if($("#excludeCbx").prop("checked")){
				check = 'Y';
			}		
			if($('#sendStartDate').val() == ($("#startDate").val()+'/01')
				 && $('#sendEndDate').val()==($("#endDate").val()+'/01') 
					&& $('#sendIsExclude').val()==check){
				if(!confirm('<s:message code="form.report.search.noChange" text="" />')){
					return;
				}
			}
			$.each(saveRemoveId,function(index,value){
				$(value).remove();
			});
			saveRemoveId = [];		
			var vos ={}
			if($("#excludeCbx").prop("checked")){
				vos.isExclude = 'Y';
			}else{
				vos.isExclude = 'N';
			}
			vos.startInterval=$("#startDate").val()+'/01';
			vos.endInterval=$("#endDate").val()+'/01';
			vos.operation = 'qCloseOnTimeInTargetFinishYM';
			SendUtil.post("/reportOperation/search",vos,function(data){
				$("#resultField").css('display','block');
				$.each(data,function(keys,values){
					if(keys=='startInterval'){
						$('#sendStartDate').val(values);
					}else if(keys=='endInterval'){
						$('#sendEndDate').val(values);
					}else if(keys=='isExclude'){
						$('#sendIsExclude').val(values);
					}else if(keys=='statisticsResult'){
						$('#cloneTableTitle').clone(true).attr('id','tableTitle').insertAfter('#resultLegend:last');
						saveRemoveId.unshift('#tableTitle');
						var html = '';
						
						var count = 0;
						var i;
						for (i in values) {
						    if (values.hasOwnProperty(i)) {
						        count++;
						    }
						}
						if(count > 1){
							var j = 1;
							$.each(values,function(k,v){
								if(count == j){
									html += '<tr align="center" style="font-weight:bold;" bgcolor="#DDDDDD">';
								}else{
									html += '<tr align="center">';
								}
								
								$.each(v,function(k1,v1){
									if(v1=='total'){
										html +='<td><s:message code="report.operation.q.form.grade.total" text="" /></td>';
									}else{
										if(k1==3){
											if(count == j){
												html +='<td></td>';
											}else{
												html +='<td>'+v1+'%</td>';
											}										
										}else{
											html +='<td>'+v1+'</td>';
										}										
									}
								});
								html += '</tr>';
								j++;
							});
							$('#noData').val("N");
						}else{
							html +='<tr align="center"><td>-</td><td><s:message code="form.report.search.noData" text="" /></td></tr>';
							$('#noData').val("");
						}
						$('#tableTitle tbody').empty().append(html);
					}
				});
			},null,true);		
		});
		
		$('#exportBtn').click(function(){
			if(saveRemoveId.length == 0 || $('#noData').val()==''){
				alert('<s:message code="form.report.search.noData" text="" />');
				return;
			}
			if($('#sendStartDate').val() =='' || $('#sendEndDate').val()=='' || $('#sendIsExclude').val()==''){
				alert('<s:message code="form.report.search.reSearch" text="" />');
				return;
			}				
			var postData = {};
			postData["operation"] = 'qCloseOnTimeInTargetFinishYM';
			postData["isExclude"] = $("#sendIsExclude").val();
			postData["startInterval"] = $("#sendStartDate").val()+'/01';
			postData["endInterval"] = $("#sendEndDate").val()+'/01';
			SendUtil.formPost('/reportOperation/export', postData);					
		});					
	});
</script>
   <style>
   	.ui-datepicker-calendar {
    display: none;
    }
   </style>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="report.operation.title.q.closeOnTimeInTargetFinishYM" text='問題處理完成率' /></h1>

	<c:import url="/WEB-INF/jsp/common/models/formReportSearchModel.jsp">
		<c:param name="month" value="q.report.operation.yearMonth.target" />
    	<c:param name="excludeCbId" value="excludeCbx" />
    	<c:param name="searchBtId" value="searchBtn" />
    	<c:param name="exportBtId" value="exportBtn" />
    	<c:param name="layout" value="1" />
    	<c:param name="action" value="2" />
		<c:param name="showConditionTitle" value="Y" />
	</c:import>
	
	<fieldset class="search">
		<legend><s:message code="q.report.operation.description.title" text='' /></legend>
			<button class="small fieldControl">
				<i class="iconx-collapse"></i>|||
		</button>
		<table class="grid_query">
			<tr>
				<th><s:message code="q.report.operation.description.content.1" text='問題結案率期望值' /></th>
				<td><s:message code="q.report.operation.description.content.2" text='85%' /></td>
				<th><s:message code="q.report.operation.description.content.3" text='計算公式' /></th>
				<td><s:message code="q.report.operation.description.content.4" text='(B/A)*100%' /></td>
			</tr>
		</table>
		<p><s:message code="q.report.operation.description.content.5" text='問題單數量(A)：預計完成日期的「年月」為群組的問題單數量' /></p><br>
		<p><s:message code="q.report.operation.description.content.6" text='已解決的問題單數量(B)：預計完成年月日>=實際完成年月日，' />
           <s:message code="q.report.operation.description.content.8" text='不含「已作廢' />」</p><br>	
	</fieldset>	
	<fieldset id='resultField' style='display:none'>
		<legend id="resultLegend"><s:message code="q.report.operation.search.result" text='查詢結果' /></legend>
	</fieldset>
	<div style="display:none">	
		<table id="cloneTableTitle" class="grid_list" style="width: 100%">
			<thead>
				<tr align="center">
					<th width="20%"><s:message code="q.report.operation.yearMonth.target" text='目標解決年月' /></th>
					<th width="20%"><s:message code="q.report.operation.form.quantity.a" text='問題單數量(A)' /></th>
					<th width="20%"><s:message code="q.report.operation.form.finished.b" text='已解決問題單數量(B)' /></th>
					<th width="20%"><s:message code="q.report.operation.form.percent" text='問題處理完成率' /></th>
				</tr>
			</thead>	
		 	<tbody></tbody>
		</table>		
	</div>	
	<input type="hidden" id="sendStartDate" value=""/>
	<input type="hidden" id="sendEndDate" value=""/>
	<input type="hidden" id="sendIsExclude" value=""/>
	<input type="hidden" id="noData" value=""/>		
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	

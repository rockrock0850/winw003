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
			alert('<s:message code="form.report.search.dateNotBlank" text="" />');
			return;
		}
		var check = 'N';
		if($("#excludeCbx").prop("checked")){
			check = 'Y';
		}		
		if($('#sendStartDate').val() == $("#startDate").val()
			 && $('#sendEndDate').val()==$("#endDate").val() 
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
		vos.startInterval=$("#startDate").val();
		vos.endInterval=$("#endDate").val();
		vos.operation = 'cHGAlterResult';
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
									html +='<td><s:message code="report.operation.chg.total" text="" /></td>';
								}else{
									html +='<td>'+v1+'</td>';
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
		postData["operation"] = 'cHGAlterResult';
		postData["isExclude"] = $("#sendIsExclude").val();
		postData["startInterval"] = $("#sendStartDate").val();
		postData["endInterval"] = $("#sendEndDate").val();
		SendUtil.formPost('/reportOperation/export', postData);					
	});
});
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="report.operation.title.chg.alterResult" text='變更成功失敗統計表' /></h1>
	<c:import url="/WEB-INF/jsp/common/models/formReportSearchModel.jsp">
		<c:param name="month" value="report.operation.5" />
    	<c:param name="excludeCbId" value="excludeCbx" />
    	<c:param name="searchBtId" value="searchBtn" />
    	<c:param name="exportBtId" value="exportBtn" />
    	<c:param name="layout" value="1" />
    	<c:param name="action" value="1" />
		<c:param name="showConditionTitle" value="Y" />
	</c:import>	
	<fieldset id="resultField"  style="display:none">
		<legend id="resultLegend"><s:message code="schedule.find.legend.result" text='' /></legend>
	</fieldset>
	<div style="display:none">
		<table id="cloneTableTitle" class="grid_list" style="width: 100%">
			<thead>
				<tr>
					<th><s:message code="form.process.managment.find.page.form.type" text='表單類別' /></th>
					<th><s:message code="success" text='成功' /></th>
					<th><s:message code="failure" text='失敗' /></th>						
					<th><s:message code="total" text='合計' /></th>
					<th><s:message code="successRate" text='成功率' /></th>						
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

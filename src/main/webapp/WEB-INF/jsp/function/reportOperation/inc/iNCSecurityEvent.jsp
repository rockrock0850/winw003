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
			$('#br'+value).remove();
			$('#body'+value).remove();
			$('#title'+value).remove();
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
		vos.operation = 'iNCSecurityEvent';
		SendUtil.post("/reportOperation/search",vos,function(data){	
			$('#resultField1').css('display','block');
			var count = 0;
			var i;
			for (i in data) {
			    if (data.hasOwnProperty(i)) {
			        count++;
			    }
			}
			if(count>1){
				$('#resultField2').css('display','block');
			}else{
				$('#resultField2').css('display','none');
				var check = 'N';
				if($("#excludeCbx").prop("checked")){
					check = 'Y';
				}				
				$('#sendStartDate').val($("#startDate").val());
				$('#sendEndDate').val($("#endDate").val());
				$('#sendIsExclude').val(check);					
			}
			var total = 0;
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
					$.each(values,function(k,v){
						$('#cloneBr').clone(true).attr('id','br'+k).insertAfter('#resultLegend2:last');
						$('#cloneBody').clone(true).attr('id','body'+k).insertAfter('#resultLegend2:last');
						$('#cloneTitle').clone(true).attr('id','title'+k).insertAfter("#resultLegend2:last");
						$('#title'+k+' tr').children().eq(1).text(v[0].section);
						$('#title'+k+' tr').children().eq(3).text(v.length);						
						total = total + v.length;
						saveRemoveId.unshift(k);
						var options = {
								data: v,
								columnDefs: [
								{orderable: false, targets: []},
								{targets: [0], data: 'FormId', className: 'dt-center'}, 
								{targets: [1], data: 'Summary', className: ''}, 
								{targets: [2], data: 'UserCreated', className: 'dt-center'}, 
								{targets: [3], data: 'FormStatus', className: ''}, 
								{targets: [4], data: 'System', className: 'dt-center'},
								{targets: [5], data: 'EventSecurity', className: 'dt-center'}
							]
						};
						TableUtil.init('#body'+k, options);						
					});
				}

			});
			$('#eventTotal tr').children().eq(1).text(total);			
		},null,true);
	});
		
	$('#exportBtn').click(function(){
		if(saveRemoveId.length == 0){
			alert('<fmt:message key="form.report.search.noData"/>');
			return;
		}
		if($('#sendStartDate').val() =='' || $('#sendEndDate').val()=='' || $('#sendIsExclude').val()==''){
			alert('<fmt:message key="form.report.search.reSearch"/>');
			return;
		}
		var postData = {};
		postData["operation"] = 'iNCSecurityEvent';
		postData["isExclude"] = $("#sendIsExclude").val();
		postData["startInterval"] = $("#sendStartDate").val();
		postData["endInterval"] = $("#sendEndDate").val();
		SendUtil.formPost('/reportOperation/export', postData);	       		
	});
	
});
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="report.operation.title.inc.securityEvent" text='資安事件統計表' /></h1>
	
	<c:import url="/WEB-INF/jsp/common/models/formReportSearchModel.jsp">
		<c:param name="month" value="report.operation.2" />
    	<c:param name="excludeCbId" value="excludeCbx" />
    	<c:param name="searchBtId" value="searchBtn" />
    	<c:param name="exportBtId" value="exportBtn" />
    	<c:param name="layout" value="1" />
    	<c:param name="action" value="1" />
		<c:param name="showConditionTitle" value="Y" />
	</c:import>	
	
	<fieldset id='resultField1' style='display:none'>
		<legend id="resultLegend1"><s:message code="global.query.result" text='查詢結果' /></legend>
		<table id="eventTotal" class="grid_query">
			<tr>
				<th><s:message code="report.operation.information.security.event.total" text='' /></th>
				<td></td>
			</tr>
		</table>
	</fieldset>

	<fieldset id='resultField2' style='display:none'>
		<legend id="resultLegend2"></legend>
	</fieldset>
	
	<div style="display:none">
		<table id="cloneTitle" class="grid_query">
			<tr>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="form.report.search.host" text='' /></th>
				<td></td>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="report.operation.information.security.event.num" text='' /></th>
				<td></td>
			</tr>
		</table>
		<table id="cloneBody" class="grid_list" style="width: 100%">
			<thead>
				<tr>
					<th width="10%"><s:message code="form.report.search.serialnumber" text='' /></th>
					<th width="35%"><s:message code="form.report.search.summary" text='' /></th>
					<th width="10%"><s:message code="q.report.operation.specialcase.handling" text='' /></th>
					<th width="15%"><s:message code="form.report.search.formStatus" text='' /></th>
					<th width="10%"><s:message code="form.search.column.system" text='' /></th>
					<th width="10%"><s:message code="report.operation.information.security.event.type" text='' /></th>							
				</tr>
			</thead>
		</table>
		<br id='cloneBr' />	
	</div>
	<input type="hidden" id="sendStartDate" value=""/>
	<input type="hidden" id="sendEndDate" value=""/>
	<input type="hidden" id="sendIsExclude" value=""/>		
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	    
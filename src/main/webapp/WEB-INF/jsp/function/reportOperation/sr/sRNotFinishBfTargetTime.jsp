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
		vos.operation = 'sRNotFinishBfTargetTime';
		SendUtil.post("/reportOperation/search",vos,function(data){	
			$('#totalFieldset').css('display','block');
			var count = 0;
			var i;
			for (i in data) {
			    if (data.hasOwnProperty(i)) {
			        count++;
			    }
			}
			if(count>1){
				$('#resultField').css('display','block');
			}else{
				$('#resultField').css('display','none');
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
						$('#cloneBr').clone(true).attr('id','br'+k).insertAfter('#resultLegend:last');
						$('#cloneBody').clone(true).attr('id','body'+k).insertAfter('#resultLegend:last');
						$('#cloneTitle').clone(true).attr('id','title'+k).insertAfter("#resultLegend:last");
						$('#title'+k+' tr').children().eq(1).text(v[0].DepartmentName+"-"+v[0].division);
						$('#title'+k+' tr').children().eq(3).text(v.length);
						total = total + v.length;
						saveRemoveId.unshift(k);
						var options = {
								data: v,
								columnDefs: [
								{orderable: false, targets: []},
								{targets: [0], data: 'FormId', className: 'dt-center'}, 
								{targets: [1], data: 'Summary', className: ''}, 
								{targets: [2], data: 'Name', className: 'dt-center'}, 
								{targets: [3], data: 'FormStatus', className: ''}, 
								{targets: [4], data: 'division', className: 'dt-center'},
								{targets: [5], data: 'ECT', className: 'dt-center'},
								{targets: [6], data: 'ACT', className: 'dt-center'} 
							],
							order: [[ 5, "desc" ]]
						};
						TableUtil.init('#body'+k, options);						
					});
				}

			});
			$('#tableTotal tr').children().eq(1).text(total);			
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
		postData["operation"] = 'sRNotFinishBfTargetTime';
		postData["isExclude"] = $("#sendIsExclude").val();
		postData["startInterval"] = $("#sendStartDate").val();
		postData["endInterval"] = $("#sendEndDate").val();
		SendUtil.formPost('/reportOperation/export', postData);		
	});
	
});
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="report.operation.title.sr.notFinishBfTargetTime" text='應用系統之需求未於預計完成日修改完畢的個數' /></h1>
	
	<c:import url="/WEB-INF/jsp/common/models/formReportSearchModel.jsp">
		<c:param name="month" value="report.operation.sr.form.ect" />
    	<c:param name="excludeCbId" value="excludeCbx" />
    	<c:param name="searchBtId" value="searchBtn" />
    	<c:param name="exportBtId" value="exportBtn" />
    	<c:param name="layout" value="1" />
    	<c:param name="action" value="1" />
		<c:param name="showConditionTitle" value="Y" />
	</c:import>
	
	<fieldset id='totalFieldset' style='display: none'>
		<table id="tableTotal" class="grid_query">
			<tr>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="form.report.search.total" text='' /></th>
				<td></td>
			</tr>
		</table>			
	</fieldset>		
	<fieldset id="resultField"  style="display:none">
		<legend id="resultLegend"><s:message code="schedule.find.legend.result" text='' /></legend>
	</fieldset>
	<div style="display:none">
		<table id="cloneTitle" class="grid_query">
			<tr>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="form.report.search.host" text='' /></th>
				<td></td>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="form.report.search.unfinish" text='' /></th>
				<td></td>
			</tr>
		</table>
		<table id="cloneBody" class="grid_list" style="width: 100%">
			<thead>
				<tr>
					<th width="10%"><s:message code="form.report.search.serialnumber" text='' /></th>
					<th width="35%"><s:message code="form.report.search.summary" text='' /></th>
					<th width="10%"><s:message code="form.report.search.principal" text='' /></th>
					<th width="15%"><s:message code="form.report.search.formStatus" text='' /></th>
					<th width="10%"><s:message code="form.report.search.beResponsibleFor" text='' /></th>
					<th width="10%"><s:message code="form.report.search.aimsCompletionDate" text='' /></th>
					<th width="10%"><s:message code="form.report.search.actualCompletionDate" text='' /></th>							
				</tr>
			</thead>
		</table>
		<br id='cloneBr' />
	</div>
	<input type="hidden" id="sendStartDate" value=""/>
	<input type="hidden" id="sendEndDate" value=""/>
	<input type="hidden" id="sendIsExclude" value=""/>	
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	

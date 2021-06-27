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
			if($('#sendSection').val()==$("#unitId").val() && $('#sendIsExclude').val()==check){
				if(!confirm('<s:message code="form.report.search.noChange" text="" />')){
					return;
				}
			}
			TableUtil.deleteTable('#tableDataList');
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
			vos.section = $("#unitId").val();
			vos.operation = 'iNCListReport';
			SendUtil.post("/reportOperation/search",vos,function(data){
				$("#resultField").css('display','block');
				var total = 0;
				$.each(data,function(keys,values){
					if(keys=='section'){
						$('#sendSection').val(values);
					}else if(keys=='isExclude'){
						$('#sendIsExclude').val(values);
					}else if(keys=='resultDataList'){
						$.each(values,function(k,v){					
							$('#cloneBody').clone(true).attr('id','tableDataList').insertAfter('#resultLegend:last');
							total = total + v.length;
							if(total > 0){
								$('#noData').val('1');
								$("#resultLegend").css('display','block');
							}else{
								$('#noData').val('');
								$("#resultLegend").css('display','none');
							}							
							saveRemoveId.push('#tableDataList');
							//saveRemoveId.unshift('#tableDataList');
							var options = {
									data: v,
									columnDefs: [
									{orderable: false, targets: []},
									{targets: [0], data: 'section', className: 'dt-center'}, 
									{targets: [1], data: 'formid', className: 'dt-center'}, 
									{targets: [2], data: 'Summary', className: ''}, 
									{targets: [3], data: 'usersolving', className: 'dt-center'}, 
									{targets: [4], data: 'FormStatus', className: ''},
									{targets: [5], data: 'newField', className: ''},
									{targets: [6], data: 'ACT', className: 'dt-center'},
									{targets: [7], data: 'ECT', className: 'dt-center'}
									]
							};
							TableUtil.init('#tableDataList', options);						
						});	
					}
				});
			},null,true);		
		});
		
		$('#exportBtn').click(function(){
			if(saveRemoveId.length == 0 || $('#noData').val()==''){
				alert('<s:message code="form.report.search.noData" text="" />');
				return;
			}
			if($('#sendIsExclude').val()==''){
				alert('<s:message code="form.report.search.reSearch" text="" />');
				return;
			}		
			var postData = {};
			postData["operation"] = 'iNCListReport';
			postData["isExclude"] = $("#sendIsExclude").val();
			postData["section"] = $("#sendSection").val();
			SendUtil.formPost('/reportOperation/export', postData);					
		});
						
	});
  </script>

</head>
<div id="scriptBlock"></div>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>
<h1><s:message code="report.operation.title.inc.listReport" text='各科未結案事件單追蹤統計表' /></h1>	

	<c:import url="/WEB-INF/jsp/common/models/formReportSearchModel.jsp">
		<c:param name="month" value="report.operation.department" />
    	<c:param name="excludeCbId" value="excludeCbx" />
    	<c:param name="searchBtId" value="searchBtn" />
    	<c:param name="exportBtId" value="exportBtn" />
    	<c:param name="layout" value="2" />
    	<c:param name="action" value="3" />
		<c:param name="showConditionTitle" value="Y" />
	</c:import>	
		
	<fieldset id="resultField"  style="display:none">
		<legend id="resultLegend"><s:message code="schedule.find.legend.result" text='' /></legend>
	</fieldset>
	<div style="display:none">
		<table id="cloneBody" class="grid_list" style="width: 100%">
			<thead>
				<tr>
					<th width="5%"><s:message code="form.report.search.host" text='' /></th>
					<th width="10%"><s:message code="form.report.search.serialnumber" text='' /></th>
					<th width="25%"><s:message code="form.report.search.summary" text='' /></th>
					<th width="5%"><s:message code="form.change.process.user.name" text='' /></th>
					<th width="20%"><s:message code="form.report.search.formStatus" text='' /></th>
					<th width="15%"><s:message code="report.operation.unfinished.c.form" text='' /><br>
					<s:message code="form.report.search.serialnumber" text='' />/
					<s:message code="form.question.process.division" text='' />/
					<s:message code="form.question.process.user.name" text='' />
					</th>
					<th width="10%"><s:message code="report.operation.event.time" text='' /></th>
					<th width="10%"><s:message code="report.operation.expected.completion.date" text='' /></th>							
				</tr>
			</thead>
		</table>
		<br id='cloneBr' />
	</div>
	<input type="hidden" id="sendSection" value=""/>
	<input type="hidden" id="sendIsExclude" value=""/>
	<input type="hidden" id="noData" value=""/>	
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<script>
var saveRemoveId = [];
var saveRemoveId2 = [];
var saveRemoveId3 = [];
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
			//TableUtil.deleteTable('#body'+value);
			$('#body'+value).remove();
			//$('#br1'+value).remove();
			//$('#br2'+value).remove();
		});
		saveRemoveId = [];
		
		$.each(saveRemoveId2,function(index,value){
			TableUtil.deleteTable('#body2'+value);
			$('#body2'+value).remove();
		});
		saveRemoveId2 = [];

		$.each(saveRemoveId3,function(index,value){
			//TableUtil.deleteTable('#body2'+value);
			$('#body3'+value).remove();
		});
		saveRemoveId3 = [];		
		
		var vos ={}
			if($("#excludeCbx").prop("checked")){
				vos.isExclude = 'Y';
			}else{
				vos.isExclude = 'N';
			}
		vos.startInterval=$("#startDate").val();
		vos.endInterval=$("#endDate").val();
		vos.operation = 'iNCStatisticReportDetail';
		SendUtil.post("/reportOperation/search",vos,function(data){	
			console.log(data);
			$('#resultDiv').css('display','block');
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
						if(k=='typeStatistic'){
							$('#cloneBody').clone(true).attr('id','body'+k).insertAfter('#resultLegend1:last');
							$('#body' + k + ' tr').children().eq(0).text('<fmt:message key="report.operation.inc.type"/>');
							$('#body' + k + ' tr').children().eq(1).text('<fmt:message key="report.operation.number.accepted"/>');
							saveRemoveId.push(k);
							var html = '';
							
							var count = 0;
							var i;
							for (i in v) {
							    if (v.hasOwnProperty(i)) {
							        count++;
							    }
							}
							if(count >= 1){
								var j = 1;
								$.each(v,function(k1,v1){
									if(count == j){
										html += '<tr align="center" style="font-weight:bold;" bgcolor="#DDDDDD">';
									}else{
										html += '<tr align="center">';
									}
									$.each(v1,function(k2,v2){
										html +='<td>'+v2+'</td>';										
									});

									html += '</tr>';
									j++;
								});
								$('#noData').val("N");
							}else{
								html +='<tr align="center"><td colspan="2"><s:message code="form.report.search.noData" text="" /></td></tr>';
								$('#noData').val("");
							}
							$('#body'+ k +' tbody').empty().append(html);
						}
						if(k=='reasonStatistic'){
							$('#cloneBody').clone(true).attr('id','body'+k).insertAfter('#resultLegend2:last');
							$('#body' + k + ' tr').children().eq(0).text('<fmt:message key="report.operation.inc.main"/>');
							$('#body' + k + ' tr').children().eq(1).text('<fmt:message key="report.operation.number.accepted"/>');
							saveRemoveId.push(k);
							var html = '';
							
							var count = 0;
							var i;
							for (i in v) {
							    if (v.hasOwnProperty(i)) {
							        count++;
							    }
							}
							if(count >= 1){
								var j = 1;
								$.each(v,function(k1,v1){
									if(count == j){
										html += '<tr align="center" style="font-weight:bold;" bgcolor="#DDDDDD">';
									}else{
										html += '<tr align="center">';
									}
									$.each(v1,function(k2,v2){
										html +='<td>'+v2+'</td>';										
									});

									html += '</tr>';
									j++;
								});
								$('#noData').val("N");
							}else{
								html +='<tr align="center"><td colspan="2"><s:message code="form.report.search.noData" text="" /></td></tr>';
								$('#noData').val("");
							}
							$('#body'+ k +' tbody').empty().append(html);							
						}
						if(k=='levelStatistic'){
							$('#cloneBody').clone(true).attr('id','body'+k).insertAfter('#resultLegend3:last');
							$('#body' + k + ' tr').children().eq(0).text('<fmt:message key="report.operation.inc.sequence"/>');
							$('#body' + k + ' tr').children().eq(1).text('<fmt:message key="report.operation.number.overdue"/>');
							saveRemoveId.push(k);
							var html = '';							
							var count = 0;
							var i;
							for (i in v) {
							    if (v.hasOwnProperty(i)) {
							        count++;
							    }
							}
							if(count >= 1){
								var j = 1;
								$.each(v,function(k1,v1){
									if(count == j){
										html += '<tr align="center" style="font-weight:bold;" bgcolor="#DDDDDD">';
									}else{
										html += '<tr align="center">';
									}
									$.each(v1,function(k2,v2){
										html +='<td>'+v2+'</td>';										
									});

									html += '</tr>';
									j++;
								});
								$('#noData').val("N");
							}else{
								html +='<tr align="center"><td colspan="2"><s:message code="form.report.search.noData" text="" /></td></tr>';
								//$('#noData').val("");
							}
							$('#body'+ k +' tbody').empty().append(html);							
						}
					});
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
						//$('#resultField2').css('display','block');
					}
					var total = values.length;
					//var keyValue = "resultLegend2";
					var num = 1;						
					$('#cloneDetail').clone(true).attr('id','body2'+keys).insertAfter('#resultLegend4:last');
					saveRemoveId2.push(keys);
					var options = {
							data: values,
							columnDefs: [
							{orderable: false, targets: []},
							{targets: [0], data: 'section', className: 'dt-center'}, 
							{targets: [1], data: 'FormId', className: 'dt-center'}, 
							{targets: [2], data: 'Summary', className: ''}, 
							{targets: [3], data: 'Name', className: 'dt-center'}, 
							{targets: [4], data: 'FormStatus', className: ''},
							{targets: [5], data: 'Countersigneds', className: ''},
							{targets: [6], data: 'CreateTime', className: 'dt-center'},
							{targets: [7], data: 'ECT', className: 'dt-center'}
						]
					};
					TableUtil.init('#body2'+keys, options);	
					//$('#tableTotal2 tr').children().eq(1).text(total);	
				}
				if(keys=='dynamicData'){
					$('#cloneBody').clone(true).attr('id','body3'+keys).insertAfter('#resultLegend5:last');
					saveRemoveId3.push(keys);
					$.each(values,function(k,v){
						if(k=='header'){
							var html = '';
							$.each(v,function(k1,v1){
								html += '<tr align="center">';
								$.each(v1,function(k2,v2){
									html += '<th>'+v2+'</th>';
								});
								html += '</tr>';
							});
							$('#body3'+ keys +' thead').empty().append(html);
						}
						if(k=='body'){
							var html = '';
							var count = 0;
							var i;
							for (i in v) {
							    if (v.hasOwnProperty(i)) {
							        count++;
							    }
							}
							if(count >= 1){
								var j = 1;							
								$.each(v,function(k1,v1){
									if(count == j){
										html += '<tr align="center" style="font-weight:bold;" bgcolor="#DDDDDD">';
									}else{
										html += '<tr align="center">';
									}
									var k=1;									
									$.each(v1,function(k2,v2){
										if(k==1){
											html += '<td align="left">'+v2+'</td>';
										}else{
											html += '<td>'+v2+'</td>';
										}									
										k=2;
									});
									html += '</tr>';
									j++;
								});
							}else{
								html +='<tr align="center"><td colspan="2"><s:message code="form.report.search.noData" text="" /></td></tr>';
								//$('#noData').val("");
							}
							$('#body3'+ keys +' tbody').empty().append(html);
						}
					});
				}
			});

			
		},null,true);
	});
	
	$('#exportBtn').click(function(){
		if(saveRemoveId.length == 0 && saveRemoveId2.length == 0 && saveRemoveId3.length == 0){
			alert('<fmt:message key="form.report.search.noData"/>');
			return;
		}
		if($('#sendStartDate').val() =='' || $('#sendEndDate').val()=='' || $('#sendIsExclude').val()==''){
			alert('<fmt:message key="form.report.search.reSearch"/>');
			return;
		}
		var postData = {};
		postData["operation"] = 'iNCStatisticReportDetail';
		postData["isExclude"] = $("#sendIsExclude").val();
		postData["startInterval"] = $("#sendStartDate").val();
		postData["endInterval"] = $("#sendEndDate").val();
		SendUtil.formPost('/reportOperation/export', postData);		
	});
});
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="report.operation.title.inc.statisticReportDetail" text='??????????????????????????????(?????????)' /></h1>
	
	<c:import url="/WEB-INF/jsp/common/models/formReportSearchModel.jsp">
		<c:param name="month" value="report.operation.3" />
    	<c:param name="excludeCbId" value="excludeCbx" />
    	<c:param name="searchBtId" value="searchBtn" />
    	<c:param name="exportBtId" value="exportBtn" />
    	<c:param name="layout" value="1" />
    	<c:param name="action" value="1" />
		<c:param name="showConditionTitle" value="Y" />
	</c:import>	
	<div id='resultDiv' style="width: 100%; height: auto; overflow: auto; display:none;">
		<fieldset style="width: 50%; float: left;">
			<legend id='resultLegend1'><s:message code="report.operation.inc.type.statistics" text='????????????????????????' /></legend>
		</fieldset>
		<fieldset style="width: 50%; float: right;">
			<legend id='resultLegend2'><s:message code="report.operation.inc.reason.statistics" text='???????????????????????????' /></legend>
		</fieldset>
		<fieldset style="width: 50%;">
			<legend id='resultLegend3'><s:message code="report.operation.inc.level.statistics" text='?????????????????????????????????????????????' /></legend>
		</fieldset>
		<fieldset style="width: 100%;">
			<legend id='resultLegend4'><s:message code="report.operation.inc.unfinished" text='??????????????????????????????????????? (??????)' /></legend>
		</fieldset>
		<fieldset style="width: 100%;">
			<legend id='resultLegend5'><s:message code="report.operation.inc.correspond.statistics" text='?????????????????????????????????????????????' /></legend>
		</fieldset>									
	</div>

	<div style="display:none">
		<table id="cloneBody" class="grid_list">
			<thead>
				<tr align="center">
					<th width="50%"><s:message code="report.operation.q.form.closed.method" text='?????????????????????' /></th>
					<th width="50%"><s:message code="report.operation.q.form.num" text='??????' /></th>
				</tr>
			</thead>	
		 	<tbody></tbody>
		</table>
		<table id="cloneDetail" class="grid_list">
			<thead>
				<tr align="center">
					<th width="5%"><s:message code="form.report.search.host" text='?????????' /></th>
					<th width="14%"><s:message code="form.report.search.serialnumber" text='????????????' /></th>
					<th width="25%"><s:message code="form.report.search.summary" text='??????' /></th>
					<th width="8%"><s:message code="q.report.operation.specialcase.handling" text='??????' /></th>
					<th width="10%"><s:message code="form.search.column.formStatus" text='????????????' /></th>
					<th width="18%"><s:message code="report.operation.unfinished.c.form" text='??????????????????' /></th>
					<th width="10%"><s:message code="form.link.column.4" text='????????????' /></th>
					<th width="10%"><s:message code="report.operation.expected.completion.date" text='???????????????' /></th>
				</tr>
			</thead>	
		 	<tbody></tbody>
		</table>					
	</div>
	<input type="hidden" id="sendStartDate" value=""/>
	<input type="hidden" id="sendEndDate" value=""/>
	<input type="hidden" id="sendIsExclude" value=""/>			
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
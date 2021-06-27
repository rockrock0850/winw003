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
		if($('#sendStartDate').val() == $("#startDate").val()
			 && $('#sendEndDate').val()==$("#endDate").val() 
				&& $('#sendIsExclude').val()==check){
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
		vos.startInterval=$("#startDate").val();
		vos.endInterval=$("#endDate").val();
		vos.operation = 'qStatisticClose';
		SendUtil.post("/reportOperation/search",vos,function(data){
			$("#resultField1").css('display','block');
			$.each(data,function(keys,values){
				var total = 0;
				if(keys=='startInterval'){
					$('#sendStartDate').val(values);
				}else if(keys=='endInterval'){
					$('#sendEndDate').val(values);
				}else if(keys=='isExclude'){
					$('#sendIsExclude').val(values);
				}else if(keys=='statisticsResult'){
					$('#cloneTableTotal').clone(true).attr('id','tableTitleTotal').insertAfter('#resultLegend1:last');
					saveRemoveId.push('#tableTitleTotal');
					var html = '';
					var count = 0;
					var i;
					for (i in values) {
					    if (values.hasOwnProperty(i)) {
					        count++;
					    }
					}
					total = count;
					if(count > 1){
						var j = 1;
						$.each(values,function(k,v){
							html += '<tr align="center">';
							$.each(v,function(k1,v1){
								if(k1==0){
									html +='<td align="left">'+v1+'</td>';
								}
								else{
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
					$('#tableTitleTotal tbody').empty().append(html);
				}else if(keys=='resultDataList'){
						$.each(values,function(k,v){					
							$('#cloneTableUnfinishedList').clone(true).attr('id','tableDataList').insertAfter('#resultLegend2:last');
							total = total + v.length;
							if(total > 0){
								$("#resultField2").css('display','block');
							}else{
								$("#resultField2").css('display','none');
							}							
							saveRemoveId.push('#tableDataList');
							//saveRemoveId.unshift('#tableDataList');
							var options = {
									data: v,
									columnDefs: [
									{orderable: false, targets: []},
									{targets: [0], data: 'FormId', className: 'dt-center'}, 
									{targets: [1], data: 'section', className: 'dt-center'}, 
									{targets: [2], data: 'name', className: 'dt-center'}, 
									{targets: [3], data: 'IsSpecial', className: 'dt-center'}, 
									{targets: [4], data: 'FormStatus', className: 'dt-left'},
									{targets: [5], data: 'CreateTime', className: 'dt-center'},
									{targets: [6], data: 'ECT', className: 'dt-center'},
									{targets: [7], data: 'ACT', className: 'dt-center'}, 
									{targets: [8], data: 'newField', className: 'dt-left'},
									{targets: [9], data: 'Summary', className: 'dt-left'}
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
		if($('#sendStartDate').val() =='' || $('#sendEndDate').val()=='' || $('#sendIsExclude').val()==''){
			alert('<s:message code="form.report.search.reSearch" text="" />');
			return;
		}			
		var postData = {};
		postData["operation"] = 'qStatisticClose';
		postData["isExclude"] = $("#sendIsExclude").val();
		postData["startInterval"] = $("#sendStartDate").val();
		postData["endInterval"] = $("#sendEndDate").val();
		SendUtil.formPost('/reportOperation/export', postData);		
	});
	
});
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="report.operation.title.q.statisticClose" text='問題單結案數量統計表' /></h1>
	
	<c:import url="/WEB-INF/jsp/common/models/formReportSearchModel.jsp">
		<c:param name="month" value="report.operation.q.form.2" />
    	<c:param name="excludeCbId" value="excludeCbx" />
    	<c:param name="searchBtId" value="searchBtn" />
    	<c:param name="exportBtId" value="exportBtn" />
    	<c:param name="layout" value="1" />
    	<c:param name="action" value="1" />
		<c:param name="showConditionTitle" value="Y" />
	</c:import>
	
	<fieldset class="search">
		<button class="small fieldControl">
			<i class="iconx-collapse"></i>|||
		</button>
		<legend><s:message code="q.report.operation.description.title" text='報表備註' /></legend>
		<p><s:message code="report.operation.q.description.content.7" text='審核完成=已結案' /></p><br>
		<p><s:message code="report.operation.q.description.content.8" text='(1)(2)(3)為「預計完成日」在查詢區間內，且表單「已結案」，不含「已作廢」' /></p><br>
		<p><s:message code="report.operation.q.description.content.9" text='(4)未於預計完成日完成的問題單定義：類別為問題單 且「表單狀態」為(經辦處理中、待副科核可、待科長核可、待副理核可、待協理核可) 或「預計完成日」小於「實際完成日」' /></p>
	</fieldset>	

	<fieldset id='resultField1' style='display:none'>
		<legend id="resultLegend1"><s:message code="report.operation.q.form.closed.method" text='問題單結案方式' /></legend>
	</fieldset>
	
	<fieldset id='resultField2' style='display:none'>
		<legend id="resultLegend2"><s:message code="report.operation.q.form.incl.unfinished" text='未結案含逾期結案' /></legend>
	</fieldset>
	
	<div style="display:none">
		<table id="cloneTableTotal" class="grid_list" style="width: 45%">
			<thead>
				<tr align="center">
					<th width="70%"><s:message code="report.operation.q.form.closed.method" text='問題單結案方式' /></th>
					<th width="30%"><s:message code="report.operation.q.form.num" text='筆數' /></th>
				</tr>
			</thead>	
		 	<tbody></tbody>
		</table>
		<table id="cloneTableUnfinishedList" class="grid_list" style="width: 100%;word-break:break-all; word-wrap:break-all;">
			<thead>
				<tr align="center">
					<th width="9%"><s:message code="form.report.search.serialnumber" text='表單編號' /></th>
					<th width="4%"><s:message code="group.function.division" text='科別' /></th>
					<th width="7%"><s:message code="q.report.operation.specialcase.handling" text='經辦' /></th>
					<th width="5%"><s:message code="form.question.form.info.is.special" text='特殊結案' /></th>
					<th width="18%"><s:message code="form.report.search.formStatus" text='表單狀態' /></th>
					<th width="8%"><s:message code="form.search.column.createTime" text='開單時間' /></th>
					<th width="8%"><s:message code="form.question.form.info.ect" text='預計完成時間' /></th>
					<th width="8%"><s:message code="form.question.form.info.act" text='實際完成時間' /></th>
					<th width="10%"><s:message code="form.change.form.info.change.countersigneds" text='會辦科' /></th>
					<th width="18%"><s:message code="form.report.search.summary" text='摘要' /></th>					
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

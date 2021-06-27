<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<script>
var saveRemoveId = [];
var hideDetail = '';
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
		vos.operation = 'sRSuccessRate';
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
					if(count >= 1){
						$.each(values,function(k,v){
							html += '<tr align="center">';
							$.each(v,function(k1,v1){
								if(k1==0){
									html +='<td><a href="javascript: void(0)" onclick="showDetail(\'#f' + v1 + '\');">'+v1+'</a></td>';
								}
								else if(k1==4){
									html +='<td>'+v1+'%</td>';										
								}else{
									html +='<td>'+v1+'</td>';
								}
								
							});

							html += '</tr>';
						});
						$('#noData').val("N");
					}else{
						html +='<tr align="center"><td>-</td><td colspan="4"><s:message code="form.report.search.noData" text="" /></td></tr>';
						$('#noData').val("");
					}
					$('#tableTitle tbody').empty().append(html);					
				}else if(keys=='resultDataList'){
					var count = 0;
					var i;
					for (i in values) {
					    if (values.hasOwnProperty(i)) {
					        count++;
					    }
					}
					if(count >=1){
						$.each(values,function(k,v){
							var html = '';
							$('#cloneField').clone(true).attr('id','f'+k).css('display','none').insertAfter('#detail:last');
							$('#cloneLegend').attr('id','l'+k);
							$('#l'+k).text(k);
							$('#cloneTableDetail').clone(true).attr('id','t'+k).insertAfter('#l'+k+':last');
							saveRemoveId.unshift('#f'+k);
							$.each(v,function(k1,v1){
								html += '<tr align="center">';
									var sign = '';
									$.each(v1,function(k2,v2){
										if(k2 == 1 && v2== ''){
											sign = '0';
											html +='<td>'+sign+'</td>';
										}else if(k2==2 && sign=='-'){
											html +='<td>'+sign+'</td>';
										}else{
											html +='<td>'+v2+'</td>';
										}										
									});
								html += '</tr>';
							});

							$('#t'+k+' tbody').empty().append(html);
						});
					}
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
		postData["operation"] = 'sRSuccessRate';
		postData["isExclude"] = $("#sendIsExclude").val();
		postData["startInterval"] = $("#sendStartDate").val()+'/01';
		postData["endInterval"] = $("#sendEndDate").val()+'/01';
		SendUtil.formPost('/reportOperation/export', postData);			
	});					
});

function showDetail(objectId){
	if(hideDetail != ''){
		$(hideDetail).css('display','none');
	}
	$(objectId).css('display','block');
	hideDetail = objectId;
}

</script>
<style>
 .ui-datepicker-calendar {
    display: none;
  }
</style>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
<h1><s:message code="report.operation.title.sr.successRate" text='上線成功率' /></h1>

	<c:import url="/WEB-INF/jsp/common/models/formReportSearchModel.jsp">
		<c:param name="month" value="report.operation.sr.form.searchMonth" />
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
					<th><s:message code="report.operation.sr.description.content.1" text='上線成功率期望值' /></th>
					<td><s:message code="report.operation.sr.description.content.2" text='97%' /></td>
					<th><s:message code="report.operation.sr.description.content.3" text='計算公式' /></th>
					<td><s:message code="report.operation.sr.description.content.4" text='(A+B-C)/(A+B) * 100%' /></td>
				</tr>
			</table>
			<p><s:message code="report.operation.sr.description.content.5" text='失敗的問題單取以「開單時間」挑選' /></p>
	</fieldset>			
	<fieldset id='resultField' style='display:none'>
		<legend id="resultLegend"><s:message code="q.report.operation.search.result" text='查詢結果' /></legend>
	</fieldset>
	<div id="detail"></div>
	<div style="display:none">	
		<table id="cloneTableTitle" class="grid_list" style="width: 100%">
			<thead>
				<tr align="center">
					<th width="20%"><s:message code="report.operation.sr.form.searchMonth" text='查詢月份' /></th>
					<th width="20%"><s:message code="report.operation.sr.form.spNumber" text='執行完的SP工作單數量' /><br/>(A)</th>
					<th width="20%"><s:message code="report.operation.sr.form.apNumber" text='執行完的AP工作單數量' /><br/>(B)</th>
					<th width="20%"><s:message code="report.operation.sr.form.onlineNumber" text='來源為上線修正的工作單數量' /><br/>(C)</th>
					<th width="20%"><s:message code="report.operation.sr.form.successRate" text='上線成功率' /></th>
				</tr>
			</thead>	
		 	<tbody></tbody>
		</table>
		<table id="cloneTableDetail" class="grid_list" style="width: 100%">
			<thead>
				<tr align="center">
					<th width="33%"><s:message code="report.operation.sr.form.searchMonth" text='查詢月份' /></th>
					<th width="33%"><s:message code="form.search.ap.department" text='AP部門' /></th>
					<th width="34%"><s:message code="form.search.ap.bill.num" text='工作單數量' /></th>
				</tr>
			</thead>	
		 	<tbody></tbody>
		</table>
		<fieldset id='cloneField'>	
			<legend id="cloneLegend"></legend>
		</fieldset>					
	</div>				
	<input type="hidden" id="sendStartDate" value=""/>
	<input type="hidden" id="sendEndDate" value=""/>
	<input type="hidden" id="sendIsExclude" value=""/>
	<input type="hidden" id="noData" value=""/>	
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	

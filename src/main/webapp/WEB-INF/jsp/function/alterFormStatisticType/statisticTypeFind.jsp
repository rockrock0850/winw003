<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>

<script>
var dataTable;
var alterList = ObjectUtil.parse('${alterList}');

$(function () {
	var options = {
		minDate: ''
	};
	DateUtil.datePicker(options);
	
	options = {
		data: TableUtil.fromObjList(alterList),
		columnDefs: [
			{targets: '_all', className: 'dt-center'}
		],
		drew: function (table) {
	 		var summary1 = 0;
	 		var summary2 = 0;
	 		var summary3 = 0;
	 		var summary4 = 0;
	 		var dataList = TableUtil.getRows(table);
	 		
	 		$.each(dataList, function () {
	 			summary1 += !this[1] ? 0 : parseInt(this[1]);
	 			summary2 += !this[2] ? 0 : parseInt(this[2]);
	 			summary3 += !this[3] ? 0 : parseInt(this[3]);
	 			summary4 += !this[4] ? 0 : parseInt(this[4]);
	 		});
	 		
	 		$('th#normalUrgent').html(summary1);
	 		$('th#normalNonUrgent').html(summary2);
	 		$('th#standardUrgent').html(summary3);
	 		$('th#standardNonUrgent').html(summary4);
		}
	};
	dataTable = TableUtil.init('#dataTable', options);
});
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1>各變更類型統計表(一般、標準、緊急)</h1>

	<fieldset class="search">
		<legend>查詢條件</legend>

		<button class="small fieldControl searchPanel">
			<i class="iconx-collapse"></i>|||
		</button>
		
		<form id='search'>
			<table class="grid_query">
				<tr>
					<jsp:include page="/WEB-INF/jsp/common/models/dateModel.jsp">
						<jsp:param name="header" value="變更報告月" />
						<jsp:param name="id1" value="" />
						<jsp:param name="id2" value="" />
						<jsp:param name="name1" value="" />
						<jsp:param name="name2" value="" />
						<jsp:param name="isPeriod" value="true" />
					</jsp:include>
					<td>
						<button><i class="iconx-search"></i> 查詢</button>
						<button><i class="iconx-export"></i> 匯出</button>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>

	<fieldset>
		<legend>查詢結果</legend>
		<table id="dataTable" class="display collapse cell-border">
			<thead>
				<tr>
					<th rowspan="2">部門</th>
					<th colspan="2">一般變更</th>
					<th colspan="2">標準變更</th>
				</tr>
				<tr>
					<th>緊急變更</th>
					<th>非緊急變更</th>
					<th>緊急變更</th>
					<th>非緊急變更</th>
				</tr>
	        </thead>
	        <tbody></tbody>
	        <tfoot>
				<th>小計</th>
				<th id='normalUrgent'></th>
				<th id='normalNonUrgent'></th>
				<th id='standardUrgent'></th>
				<th id='standardNonUrgent'></th>
	        </tfoot>
		</table>
	</fieldset>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
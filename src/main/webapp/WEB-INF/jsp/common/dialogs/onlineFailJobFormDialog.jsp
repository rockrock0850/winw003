<%@ page contentType="text/html; charset=UTF-8" %>
<script>
var OnlineJobFormIdDialog = function () {

	var tOptions = {
		orderByDef: [0,'ASC'],
		orderBy: [
			'formId'
		],
		queryPath: '/html/getJobFormList',
		columnDefs: [
			{orderable: false, targets: '_all'}, 
			{targets: [0], title: '表單編號', data: 'formId', className: 'dt-center'}, 
			{targets: [1], title: '作業目的', data: 'purpose', className: 'dt-center'},
			{targets: [2], title: '表單狀態', data: 'formStatusWording', className: 'dt-center'},
			{targets: [3], title: '處理科別', data: 'divisionSolving', className: 'dt-center'},
			{targets: [4], title: '處理人員', data: 'userSolving', className: 'dt-center'},
			{targets: [5], title: '開單時間', data: 'createTime', className: 'dt-center',
				render: function (data) {
					return DateUtil.toDateTime(data);
			}},
		]
	};
	
	var show = function () {
		var dialog = 'div#onlineJobFormIdDialog';
		var table = dialog + ' table#onlineJobFormIdList';
		
		var options = {
			open: function (event, ui) {
				tOptions.request = loginUserInfo;
				TableUtil.paging('#onlineJobFormIdList', tOptions);
			},
			close: function () {
				TableUtil.deleteTable(table);
			}
		};
		
		DialogUtil.show(dialog, options);
		DialogUtil.clickRow(table, function (row) {
			$('#onlineJobFormId').val(row.formId);
			TableUtil.deleteTable(table);
		}, true);
	}
	
	var search = function () {
		var options = tOptions;
		var headForm = form2object('headForm');
		var request = form2object('onlineJobFormIdForm');
		if(request.createTime){
			request.createTime = DateUtil.formatDate(request.createTime);
		}
		request.mboName = headForm.formClass;
		options.request = request;
		TableUtil.reDraw('#onlineJobFormIdList', options, true);
	}
	
	return {
		show : show,
		search : search,
		tOptions : tOptions
	}
	
}();
</script>
<div id="onlineJobFormIdDialog" style="display: none;">
	<fieldset>
		<legend>選取系統名稱</legend>
		<form id='onlineJobFormIdForm'>
			<table class="grid_query">
				<tr>
					<th>表單編號</th>
					<td>
						<input id='FormId' type="text" name='formId' />
					</td>
					<th>作業目的</th>
					<td>
						<input id='Purpose' type="text" name='purpose' />
					</td>
					<th>處理科別</th>
					<td>
						<input id='DivisionSolving' type="text" name='divisionSolving' />
					</td>
				</tr>
				<tr>
					<th>開單時間</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="onlineFailCreateTime" />
						<jsp:param name="name1" value="createTime" />
					</jsp:include>
					<th>處理人員</th>
					<td>
						<input id='UserSolving' type="text" name='userSolving' />
					</td>
					<td>
						<button type='button' onclick='OnlineJobFormIdDialog.search()'>
							<i class="iconx-search"></i>查詢
						</button>
					</td>
				</tr>
			</table>
		</form>
		<table id="onlineJobFormIdList" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
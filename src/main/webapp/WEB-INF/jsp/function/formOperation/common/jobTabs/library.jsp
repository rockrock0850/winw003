<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<script>
var ajaxSetting = {async:false};
var libraryTable, baseLineTable, request;

$(function () {
	request = form2object('headForm');
	initLibraryTable();
	initBaseLineTable();
    authViewControl();
});

function initLibraryTable () {
	request.rowType = 'LIBRARY';
	SendUtil.post('/apJobForm/getVersionCodeList', request, function (response) {
		var payload = '';
		var options = {
			data: response,
			columnDefs: [
				{orderable: false, targets: [0]}, 
				{targets: [0], title: '查詢時間', data: 'time', className: 'dt-center',
					render: function (data, type, row) {
						return DateUtil.toDateTime(data);
					}},
				{targets: [1], title: '查詢結果', data: 'msg', className: 'dt-center'},
				{targets: [2], title: '資料內容', data: 'fileName', className: 'dt-center',
					render: function (data, type, row) {
						if ('1' == row.qyStatus) {
							payload = '/{0}/{1}';
							payload = StringUtil.format(payload, row.id, row.fn); 
							data = '<a href="' + contextPath + '/apJobForm/download' + payload + '">' + data + '</a>';
						}
						
						return data;
					}},
				{targets: [3], data: 'fn', className: 'hidden'},
				{targets: [4], data: 'id', className: 'hidden'},
				{targets: [5], data: 'qyStatus', className: 'hidden'}
			]
		};
		libraryTable = TableUtil.init('#libraryList', options);
		TableUtil.page(libraryTable, 'last');
	}, ajaxSetting);
}

function initBaseLineTable () {
	request.rowType = 'BASELINE';
	SendUtil.post('/apJobForm/getVersionCodeList', request, function (response) {
		var options = {
			data: response,
			columnDefs: [
				{orderable: false, targets: [0]}, 
				{targets: [0], title: '查詢時間', data: 'time', className: 'dt-center',
					render: function (data, type, row) {
						return DateUtil.toDateTime(data);
					}},
				{targets: [1], title: '查詢結果', data: 'msg', className: 'dt-center'},
				{targets: [2], title: '資料內容', data: 'baseLine', className: 'dt-center'},
				{targets: [3], data: 'qyStatus', className: 'hidden'}
			]
		};
		baseLineTable = TableUtil.init('#baseLineList', options);
		TableUtil.page(baseLineTable, 'last');
	}, ajaxSetting);
}

function clickVersionDiffButton () {
	SendUtil.post('/apJobForm/versionCodeDiffProc', form2object('headForm'), function (response) {
		TableUtil.addRow(libraryTable, response);
		TableUtil.page(libraryTable, 'last');
	}, null, true);
}

function clickBaseLineButton () {
	SendUtil.post('/apJobForm/versionCodeBaseLine', form2object('headForm'), function (response) {
		TableUtil.addRow(baseLineTable, response);
		TableUtil.page(baseLineTable, 'last');
	}, null, true);
}
</script>

<table id='libraryTables' style='width:100%'>
	<tr>
		<td valign="top" style="width: 50%; padding: 10px;">
			<table style="width: 100%;">
				<tr>
					<td>
						<div class="grid_BtnBar"">
							<button onclick='clickVersionDiffButton()'>
								<i class="iconx-query"></i> 查詢是否有進館
							</button>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<table id="libraryList" class="display collapse cell-border">
							<thead></thead>
							<tbody></tbody>
						</table>
					</td>
				</tr>
			</table>
		</td>
		<td valign="top" style="width: 50%; padding: 10px;">
			<table style="width: 100%;">
				<tr>
					<td>
						<div class="grid_BtnBar"">
							<button onclick='clickBaseLineButton()'>
								<i class="iconx-query"></i> 查詢BaseLine編號
							</button>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<table id="baseLineList" class="display collapse cell-border">
							<thead></thead>
							<tbody></tbody>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
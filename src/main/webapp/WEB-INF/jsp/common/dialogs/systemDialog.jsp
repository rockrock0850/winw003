﻿<%@ page contentType="text/html; charset=UTF-8" %>
<script>
// 系統名稱清單對話框
var SystemDialog = function () {

	var tOptions = {
		orderByDef: [3,'ASC'],
		orderBy: [
			'Department',
			'SystemId',
			'SystemName',
			'Id'
		],
		queryPath: '/html/getSystemInfoList',
		columnDefs: [
			{orderable: false, targets: '_all'}, 
			{width: '15%', targets: [0], title: '部門代號',    data: 'department', className: 'dt-center'}, 
			{width: '15%', targets: [1], title: '系統編號',    data: 'systemId', 	className: 'dt-center'}, 
			{width: '65%', targets: [2], title: '系統中文說明', data: 'systemName', className: ''},
			{width: '',    targets: [3], title: '資訊資產群組', data: 'mark', 		className: 'dt-center hidden'},
			{width: '',    targets: [4], title: 'Opinc', 	data: 'opinc',      className: 'dt-center hidden'},
			{width: '',    targets: [5], title: 'Apinc', 	data: 'apinc',      className: 'dt-center hidden'},
			{width: '',    targets: [6], title: '極限值', 	data: 'limit', 	 	className: 'dt-center hidden'},
			{width: '',    targets: [7], title: '狀態', 		data: 'active', 	className: 'dt-center hidden',
				render : function(data) {
					if (data == 'Y') {
						return '啟用';
					} else if (data == 'N') {
						return '<font color="red">停用</font>';
					} else {
						return "";
					}
				}
			}
		]
	};
	
	var show = function () {
		var dialog = 'div#systemDialog';
		var table = dialog + ' table#systemList';
		var options = {
			open: function (event, ui) {
				var formInfo = form2object('headForm');
				loginUserInfo.mboName = formInfo.formClass;
				tOptions.request = loginUserInfo;
				TableUtil.paging('#systemList', tOptions);
			},
			close: function () {
				TableUtil.deleteTable(table);
				$('input#department').val('');
			}
		};
		
		DialogUtil.show(dialog, options);
		DialogUtil.clickRow(table, function (row) {
			$('td#systemModel input#systemBrand').val(row.systemBrand);
			$('td#systemModel input#systemId').val(row.systemId);
			$('td#systemModel textarea#system').val(row.systemName);
			$('textarea#assetGroup').val(row.mark);
			TableUtil.deleteTable(table);
		}, true);
	}
	
	return {
		show : show,
		tOptions : tOptions
	}
}();

function searchSystem () {
	var options = SystemDialog.tOptions;
	var request = form2object('systemForm');
	var headForm = form2object('headForm');
	
	request.mboName = headForm.formClass;
	options.request = request;
	TableUtil.reDraw('#systemList', options, true);
}
</script>
<div id="systemDialog" style="display: none;">
	<fieldset>
		<legend><s:message code="system.select.name.dialog" text='選取系統名稱' /></legend>
		<form id='systemForm'>
			<table class="grid_query">
				<tr>
					<th><s:message code="system.department.code.name" text='部門代號' /></th>
					<td>
						<input id='department' type="text" name='department' />
					</td>
					<th><s:message code="system.id" text='系統編號' /></th>
					<td>
						<input id='systemId' type="text" name='systemId' />
					</td>
					<th><s:message code="system.chinese.name.description" text='系統中文說明' /></th>
					<td>
						<input id='systemName' type="text" name='systemName' />
					</td>
					<td>
						<button type='button' onclick='searchSystem()'>
							<i class="iconx-search"></i><s:message code="button.search" text='查詢' />
						</button>
					</td>
				</tr>
			</table>
		</form>
		<table id="systemList" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
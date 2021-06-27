<%@ page contentType="text/html; charset=UTF-8" %>

<style>
#cComponentList_wrapper thead,
#cComponentList_wrapper th {text-align: center !important;}
</style>

<script>
// 組態元件清單
var CComponentDialog = function () {
	
	var ajaxSetting = {async:false};
	var tOptions = {
// 		orderBy: ['Sort'],
		orderByDef: [2,'ASC'],
		orderBy: [
			'Value',
			'Wording',
			'Sort'
		],
		queryPath: '/html/getCComponentList',
		columnDefs: [
			{orderable: false, targets: '_all'}, 
			{targets: [0], title: '編號', data: 'value', className: 'dt-left', width: '50%'}, 
			{targets: [1], title: '名稱', data: 'wording', className: 'dt-left', width: '50%', 
				render: function (data, type, full) {
					return full.wording ? full.wording : full.display;
				}}
		]
	};
	
	var show = function () {
		var dialog = 'div#cComponentDialog';
		var cClass = form2object('infoForm').cClass;
		
		if (!cClass) {
			alert('<s:message code="form.question.verify.c.class.error" text="請選擇組態元件類別" />');
			return;
		}
		
		var table = dialog + ' table#cComponentList';
		var options = {
			open: function (event, ui) {
				tOptions.request = {
					optionId: cClass	
				}
				TableUtil.paging('#cComponentList', tOptions);
			},
			close: function () {
				TableUtil.deleteTable(table);
				$('div#cComponentDialog input#wording').val('');
			}
		};
		
		DialogUtil.show(dialog, options);
		DialogUtil.clickRow(table, function (row) {
			$('td#cComponentModel textarea#cComponent').val(row.value);
			$('td#cComponentModel textarea#cComponentDisplay').val(row.wording ? row.wording : row.display);
			$('div#cComponentDialog input#wording').val('');
			TableUtil.deleteTable(table);
		}, true);
	}
	
	var search = function () {
		var cClass = form2object('infoForm').cClass;
		var request = form2object('cComponentForm');
		
		request.optionId = cClass;
		tOptions.request = request;
		TableUtil.reDraw('#cComponentList', tOptions, true);
	}
	
	return {
		show : show,
		search : search
	}
}();
</script>
<div id="cComponentDialog" style="display: none;">
	<fieldset>
		<legend>選取組態元件</legend>
		<form id='cComponentForm'>
			<table class="grid_query">
				<tr>
					<th>名稱或代號</th>
					<td><input id='wording' type="text" name='wording' /></td>
					<td>
						<button type='button' onclick='CComponentDialog.search()'>
							<i class="iconx-search"></i>查詢
						</button>
					</td>
				</tr>
			</table>
		</form>
		<table id="cComponentList" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
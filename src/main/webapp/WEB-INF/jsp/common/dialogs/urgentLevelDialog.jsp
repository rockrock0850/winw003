<%@ page contentType="text/html; charset=UTF-8" %>

<style>
#urgentLevelList_wrapper thead,
#urgentLevelList_wrapper th {text-align: center !important;}
</style>

<script>
var ULDialog = function () {

	var show = function (urgentLevel) {
		var dialog = 'div#urgentLevelDialog';
		var table = dialog + ' table#urgentLevelList';
		var options = {
			width: '600px',
			maxWidth: '600px',
			open: function (event, ui) {
				SendUtil.get("/html/getDropdownList", urgentLevel, function (response) {
					if ($.fn.DataTable.isDataTable('#urgentLevelList')) {
						TableUtil.reDraw($(table).DataTable(), response);
						return;
					}

					var options = {
						data: response,
						columnDefs: [
							{orderable: false, targets: '_all'}, 
							{targets: [0], title: '等級', data: 'value', className: 'dt-center', width: '10%'}, 
							{targets: [1], title: '說明', data: 'wording', className: 'dt-left', width: '90%'}
						]
					};
					TableUtil.init('#urgentLevelList', options);
				}, ajaxSetting, true);
			},
			close: function () {
				TableUtil.deleteTable(table);
			}
		};
		
		DialogUtil.show(dialog, options);
		DialogUtil.clickRow(table, function (row) {
			$('textarea#urgentLevelWording').val(row.wording.replace(/\<br>/g, ''));
			$('input#urgentLevel').val(row.value).change();
		}, true);
	}
	
	return {
		show : show
	}
}();
</script>

<div id="urgentLevelDialog" style="display: none;">
	<fieldset>
		<legend>選取緊急程度</legend>
		<table id="urgentLevelList" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
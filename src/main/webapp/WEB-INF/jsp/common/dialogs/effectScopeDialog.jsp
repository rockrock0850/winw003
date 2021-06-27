<%@ page contentType="text/html; charset=UTF-8" %>

<style>
#effectScopeList_wrapper thead,
#effectScopeList_wrapper th {text-align: center !important;}
</style>

<script>
var ESDialog = function () {

	var show = function () {
		var dialog = 'div#effectScopeDialog';
		var table = dialog + ' table#effectScopeList';
		var options = {
			width: '600px',
			maxWidth: '600px',
			open: function (event, ui) {
				SendUtil.get("/html/getDropdownList", 'EFFECT_SCOPE', function (response) {
					if ($.fn.DataTable.isDataTable('#effectScopeList')) {
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
					TableUtil.init('#effectScopeList', options);
				}, ajaxSetting, true);
			},
			close: function () {
				TableUtil.deleteTable(table);
			}
		};
		
		DialogUtil.show(dialog, options);
		DialogUtil.clickRow(table, function (row) {
			$('textarea#effectScopeWording').val(row.wording.replace(/\<br>/g, ''));
			$('input#effectScope').val(row.value).change();
		}, true);
	}
	
	return {
		show : show
	}
}();
</script>

<div id="effectScopeDialog" style="display: none;">
	<fieldset>
		<legend>選取影響範圍</legend>
		<table id="effectScopeList" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
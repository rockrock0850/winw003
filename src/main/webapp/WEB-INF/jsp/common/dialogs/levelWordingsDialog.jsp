<%@ page contentType="text/html; charset=UTF-8" %>
<script>//# sourceURL=LevelWordingsDialog.js
/**
 * 關卡文字設定視窗
 */
var LevelWordingsDialog = function () {

	var dialog = 'div#levelWordingsDialog';
	var table = dialog + ' table#levelWordingsList';
	
	var show = function (stages, confirmButton, cancelButton) {
		let isEnabled;
		let tableOpts = {
			info: false,
			paging: false,
			pageLength: -1,
			ordering: false,
			data: stages.levels,
			columnDefs: [
				{orderable: false, targets: []}, 
				{targets: [0], title: '關卡', data: 'level', className: 'dt-center',
					render : function (data, type, row, meta) {
						return '<input type="text" value="' + data + '" disabled/>';
				}},
				{targets: [1], title: '自訂文字', data: 'wording', className: 'dt-center', 
					render : function (data, type, row, meta) {
						isEnabled = row.isCurrentLevel ? 'disabled' : '';
						return '<input class="wording" type="text" value="' + data + '" ' + isEnabled + ' />';
				}}
			]
		};
		
		setup(stages.levels, tableOpts, confirmButton, cancelButton);
	}
	
	function setup (res, tableOpts, confirmButton, cancelButton) {
		let dialogOpts = {
			width: '500px',
			maxWidth: '1000px',
			resizable: false,
			open: function (event, ui) {
				if (!TableUtil.isDataTable('#levelWordingsList')) {
					TableUtil.init('#levelWordingsList', tableOpts);
				} else {
					TableUtil.reDraw($(table).DataTable(), res);
				}
				
				DialogUtil.hideLoading();
			},
			close: function () {
				TableUtil.deleteTable(table);
			},
			buttons: {
				"確定": function () {
					HtmlUtil.lockSubmitKey(true);
					
					if (confirmButton) {
						var myRows = [];
						$('#levelWordingsDialog tbody tr ').each(function() {
						  var obj = {}
						  obj["wordingLevel"] = $(this).find("td:eq(0) input").val();
						  obj["wording"] = $(this).find("td:eq(1) input").val();
						  myRows.push(obj)
						});
						confirmButton(myRows);
					}
					
					DialogUtil.close();
					TableUtil.deleteTable(table);
				},
				"取消": function () {
					if (cancelButton) {
						cancelButton(table);	
					}
					
					DialogUtil.close();
					TableUtil.deleteTable(table);
				}
			}
		};
		
		DialogUtil.show(dialog, dialogOpts);
	}
	
	return {
		show : show
	};
}();
</script>

<div id="levelWordingsDialog" style="display: none;">
	<fieldset>
		<legend>送簽關卡選擇</legend>
		<table id="levelWordingsList" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
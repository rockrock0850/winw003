<%@ page contentType="text/html; charset=UTF-8" %>

<script>
/**
 * 變更單副科長專用
 * 按【同意】btn
 * 選項：
 *  - 呈核給科長 (進下一關_科長)
 *  - 代科長核准 (程式邏輯：達標->送副理；未達標->送審核最後一關)
 */
var ChgViceDialog = function () {
	
	var show = function (request, confirmButton, cancelButton) {
		var dialog = 'div#chgViceDialog';
		var table = dialog + ' table#chgViceList';
		var radio = '<input type="radio" name="detailId" value="{0}" {1} />';
		
		var options = {
			width: '700px',
			maxWidth: '1000px',
			open: function (event, ui) {
				SendUtil.post('/html/getSigningList', request, function (response) {
					if (TableUtil.isDataTable('#chgViceList')) {
						return;
					}
					
					var options = {
						data: findViceData(response),
						columnDefs: [
							{orderable: false, targets: []}, 
							{
								targets: [0], 
								data: 'detailId', 
								title: "功能", 
								className: 'dt-center', 
								width: '5%',
								render: function (data, type, row, meta) {
									return StringUtil.format(radio, data, meta.row == 0 ? 'checked' : '');
								}
							},
							{targets: [1], title: '流程關卡', data: 'groupName', className: 'dt-center', width: '20%'}
						]
					};
					
					TableUtil.init('#chgViceList', options);
				}, null, true);
			},
			close: function () {
				TableUtil.deleteTable(table);
			},
			buttons: {
				"確定": function () {
					HtmlUtil.lockSubmitKey(true);
					if (confirmButton) {
						var checked = DialogUtil.findRadioChecked(table);
						var rowData = TableUtil.getRow($(table).DataTable(), checked);
						
						confirmButton(rowData);
					}
				},
				"取消": function () {
					if (cancelButton) {
						cancelButton();
					}
					
					DialogUtil.close();
					TableUtil.deleteTable(table);
				}
			}
		};

		DialogUtil.show(dialog, options);
		HtmlUtil.clickRowRadioEvent(table + ' tbody');
	}

	function findViceData (response) {
		var viceData = [];
		
		if ($.isArray(response)) {
			$.each(response, function (i, stage) {
				if (stage.groupId.indexOf('SC') != -1) {
					let vice = stage;
					vice.groupName = '呈核給科長';
					viceData.push(vice);
					viceData.push({
						groupName:'代科長核准',
						detailId: vice.detailId
					});
					
					return false;// break;
				}
			});
		}
		
		return viceData;
	}
	
	return {
		show : show
	}
}();
</script>

<div id="chgViceDialog" style="display: none;">
	<fieldset>
		<legend>送簽關卡選擇</legend>
		<table id="chgViceList" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
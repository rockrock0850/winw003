<%@ page contentType="text/html; charset=UTF-8" %>

<script>
// 系統行事曆
var HolidayDialog = function () {
	
	var show = function (tOptions) {
		var dialog = 'div#holidayDialog';
		var table = dialog + ' table#holidayTable';
		
		var options = {
			close: function () {
				TableUtil.deleteTable(table);
			}
		};
		DialogUtil.show(dialog, options);
		
		// 若table已經存在就重畫
		if (TableUtil.isDataTable(table)) {
			TableUtil.reDraw(
					$(table).DataTable(), tOptions.data);
		} else {
			TableUtil.init(table, tOptions);
		}
	}
	
	return {
		show : show
	}
}();
</script>

<div id='holidayDialog' style='display: none'>
	<fieldset>
		<legend>年假日表</legend>
		<table id="holidayTable" class="display collapse nowrap cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
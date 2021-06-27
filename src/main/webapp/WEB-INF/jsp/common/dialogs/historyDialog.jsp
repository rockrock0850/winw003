<%@ page contentType="text/html; charset=UTF-8" %>

<script>
// 歷史紀錄對話框
var HistoryDialog = function () {
	
	var show = function (tOptions) {
		var dialog = 'div#historyDialog';
		var title = dialog + ' th#title';
		var name = dialog + ' td#name';
		var table = dialog + ' table#table';

		$(title).html(tOptions.title);
		$(name).html(tOptions.name);
		
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

<div id='historyDialog' style='display: none'>
	<fieldset>
		<legend>&nbsp;</legend>
		<table class="grid_query">
			<tr>
				<th id='title'></th>
				<td id='name'></td>
			</tr>
		</table>
	</fieldset>
	
	<fieldset>
		<legend>歷程資料</legend>
		<table id="table" class="display collapse nowrap cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
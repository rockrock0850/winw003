<%@ page contentType="text/html; charset=UTF-8" %>

<style>
#impactList_wrapper thead,
#impactList_wrapper th {text-align: center !important;}
</style>

<script>
var ImpactDialog = function () {

	var show = function (row) {
		var dialog = 'div#impactDialog';
		var table = dialog + ' table#impactList';
		var options = {
			width: '600px',
			maxWidth: '600px',
			open: function (event, ui) {
				SendUtil.post("/commonForm/getFormImpactAnalysis", form2object('headForm'), function (response) {
					var fractionList = getTargetFractionList(row, response);
					
					if ($.fn.DataTable.isDataTable('#impactList')) {
						TableUtil.reDraw(
								$(table).DataTable(), fractionList);
						return;
					}

					var options = {
						data: fractionList,
						columnDefs: [
							{orderable: false, targets: '_all'}, 
							{targets: [0], title: '等級', data: 'value', className: 'dt-center', width: '10%'}, 
							{targets: [1], title: '說明', data: 'wording', className: 'dt-left', width: '90%'}
						]
					};
					TableUtil.init('#impactList', options);
				}, ajaxSetting, true);
			},
			close: function () {
				TableUtil.deleteTable(table);
			}
		};
		
		DialogUtil.show(dialog, options);
		DialogUtil.clickRow(table, function (clicked) {
	        var rowData = $(row).closest('tr');
	        var targetFraction = rowData.find('input#targetFraction');
	        var descriptionValue = rowData.find('input#descriptionValue');
	        
			$(descriptionValue).val(clicked.wording);
			$(targetFraction).val(clicked.value).change();
		}, true);
	}
	
	function getTargetFractionList (row, response) {
		var fractionList = [];
		
		if (hasRowData(response)) {
			var order = $(row).siblings('input#order').val()-1;
			var fractions = response[order].fraction.split(';');
			var descriptions = response[order].description.split(';');
			var size = fractions.length;
			
			for (var i = 0; i < size; i++) {
				if (!parseInt(fractions[i]) && !descriptions[i]) {
					continue;
				}
				
				fractionList.push(
					{
						value:fractions[i], 
						wording:descriptions[i]
					}
				);
			}
		}
		
		return fractionList;
	}

	function hasRowData (response) {
		return (response && response.length > 0);
	}
	
	return {
		show : show
	}
}();
</script>

<div id="impactDialog" style="display: none;">
	<fieldset>
		<legend>選取衝擊分析</legend>
		<table id="impactList" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
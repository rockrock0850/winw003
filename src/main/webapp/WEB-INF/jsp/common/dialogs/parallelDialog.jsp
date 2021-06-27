<%@ page contentType="text/html; charset=UTF-8" %>
<script>
/**
 * 開啟平行會辦編輯視窗
 */
var ParallelDialog = function () {

	var dialog = 'div#parallelDialog';
	var table = dialog + ' table#parallelList';
	var checkbox = '<input type="checkbox" name="workLevel" value="{0}" onchange="ParallelDialog.checkallBoxChangeEvent()" {1} />';
	
	var show = function (confirmButton, cancelButton) {
		let isCheck, parallels;
		SendUtil.post('/html/getFormCParallels', {isAll:true}, function (options) {
			let tableOpts = {
				info: false,
				paging: false,
				pageLength: -1,
				ordering: false,
				data: options,
				columnDefs: [
					{orderable: false, targets: []}, 
					{
						width: '1%',
						targets: [0], 
						data: 'value', 
						className: 'dt-center clickable', 
						title: '<input id="checkall" type="checkbox" onchange="ParallelDialog.checkall(this)" />', 
						render: function (data, type, row, meta) {
							isCheck = '';
							parallels = $('input#parallels').val();
							
							if (parallels) {
								parallels = parallels.split(',');
								isCheck = $.inArray(data, parallels) > -1 ? 'checked' : '';
							}
							
							return StringUtil.format(checkbox, data, isCheck);
						}
					},
					{targets: [1], title: '組別', data: 'wording', className: 'dt-center clickable', width: '5%'}
				]
			};
			
			setup(options, tableOpts, confirmButton, cancelButton);
		});
	}

	// 監聽全選按鈕是否依然開啟/關閉
	var checkallBoxChangeEvent = function () {
		let checkallBox = 'input#checkall';
		let checkboxs = '#parallelList input[name="workLevel"]';
		TableUtil.checkallChangeEvent(checkallBox, checkboxs);
	}

	// 全選
	var checkall = function (checkallBox) {
		let checkboxs = '#parallelList input[name="workLevel"]';
		TableUtil.checkall(checkallBox, checkboxs);
	}
	
	function setup (options, tableOpts, confirmButton, cancelButton) {
		let dialogOpts = {
			width: '500px',
			maxWidth: '1000px',
			resizable: false,
			open: function (event, ui) {
				if (!TableUtil.isDataTable('#parallelList')) {
					TableUtil.init('#parallelList', tableOpts);
				} else {
					TableUtil.reDraw($(table).DataTable(), options);
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
						confirmButton(HtmlUtil.findCheckedboxs(table));
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
		HtmlUtil.clickRowCheckboxEvent(table + ' tbody');
	}
	
	return {
		show : show,
		checkall : checkall,
		checkallBoxChangeEvent : checkallBoxChangeEvent
	};
}();
</script>

<div id="parallelDialog" style="display: none;">
	<fieldset>
		<legend>送簽關卡選擇</legend>
		<table id="parallelList" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
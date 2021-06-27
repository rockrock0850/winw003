<%@ page contentType="text/html; charset=UTF-8" %>
<div id='incFormDialog' style='display: none'>
	<fieldset id="incFormSearch">
		<legend><s:message code='form.event.form.info.main.event' text='併入主要事件單' /></legend>
		<table class="grid_query">
			<tbody>
				<tr>
					<th><s:message code='form.report.search.serialnumber' text='表單編號' /></th>
					<td>
						<input id="formId" name="formId" type="text"/>
					</td>
					<th><s:message code='report.operation.event.summary' text='事件摘要' /></th>
					<td>
						<input id="summary" name="summary" type="text"/>
					</td>
					<td>
						<button onclick='getIncFormList()'>
							<i class="iconx-search"></i> <s:message code="button.search" text='查詢' />
						</button>
					</td>
				</tr>
			</tbody>
		</table>
	</fieldset>
	<fieldset>
		<table id="incTable" class="display collapse nowrap cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
<script>
/**
 * 取得非擬案、非結案、非作廢之事件單
 */
var IncFormDialog = function () {

	var dialog = 'div#incFormDialog';
	var table = dialog + ' table#incTable';
	var options = {
		width: '100%',
		data: null,
		columnDefs: [
			{ 
	            targets: 0,
	            searchable: false,
	            orderable: false,
	            className: 'dt-center',
	            render: function(data, type, full, meta){
	                  data = '<input type="radio" name="incFormId" value="'+full.formId+'">';
	               return data;
	            }
	        },
			{targets: [1], title: '表單編號', data: 'formId', className: 'dt-center'}, 
			{targets: [2], title: '摘要', data: 'summary', className: 'dt-center'},
			{targets: [3], title: '事件發生時間', data: 'createTime', className: 'dt-center', 
				render: function (data) {
					return DateUtil.toDateTime(data);
				}},
			{targets: [4], title: '開單人員', data: 'userCreated', className: 'dt-center'},
			{targets: [5], title: '表單狀態', data: 'formStatus', className: 'dt-center'},
			{targets: [6], title: '', data: 'userId', className: 'dt-center hidden'}
		]
	};
	
	var show = function (callback) {
		var dialogOpts = {
			width:'900px',
			maxHeight: '500px',
			maxWidth: '100px',
			resizable: true,
			open: function (event, ui) {
				if (TableUtil.isDataTable(table)) {
					TableUtil.reDraw($(table).DataTable(), options);
				}else{
					TableUtil.init(table, options);
				}
				
				DialogUtil.hideLoading();
			},
			close: function () {
				DialogUtil.close(dialog);
				TableUtil.deleteTable(table);
			},
			buttons: {
				"確定": function (e) {
					HtmlUtil.lockSubmitKey(true);
					if (callback) {
						var radio = $(this).find("input[name=incFormId]:checked");
						var checkValue = radio.val();
						var result = {
							"checkValue":checkValue
						}
						
						callback(result);
						DialogUtil.close(dialog);
						TableUtil.deleteTable(table);
					}
				}
			}
		};

		DialogUtil.show(dialog, dialogOpts);
		HtmlUtil.clickRowRadioEvent(table + ' tbody');
	}
	
	return {
		show : show
	}
}();

function getIncFormList() {
	SendUtil.post('/html/getIncFormList', form2object('incFormSearch'), function (resData) {
		TableUtil.reDraw($("#incTable").DataTable(), resData);
	}, null, true);
}
</script>
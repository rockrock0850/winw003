<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script>
var dataTable;

$(function () {
	initDataTable();
	authViewControl();
});

function initDataTable () {
	var headForm = form2object('headForm');
	var summary = '<textarea class="form-daily-log resizable" name="{0}" rows="1" cols="1">{1}</textarea>';
	var payload = '';
	var extension = [];
	var options = {
		checkbox: true,
		columnDefs: [
			{orderable: false, targets: [0]}, 
			{
				targets: [0], 
				data: 'id', 
				className: 'dt-center',
				title: '<input id="checkall" type="checkbox" onchange="checkall(this)" />', 
				width: '10%',
				render: function (data, type, row) {
					let checked = row.isChecked ? 'checked' : '';
					return '<input name="check" value="' + data + '" type="checkbox" onchange="checkallBoxChangeEvent()" checked />'
				}
			}, 
			{targets: [1], title: '<s:message code="log.tab.recoder" text="紀錄人員"/>', data: 'name', className: 'dt-center', width: '10%'},
			{targets: [2], title: '<s:message code="log.tab.recode.date" text="記錄日期"/>', data: 'createdAt', className: 'dt-center', width: '15%',
				render: function (data) {
					return DateUtil.toDateTime(data);
				}}, 
			{targets: [3], title: '<s:message code="log.tab.summary" text="摘要"/>', data: 'summary', className: 'dt-center', width: '50%',
				render: function (data) {
					return StringUtil.format(summary, 'summary', data ? data : '');
			}},
			{targets: [4], title: '<s:message code="system.updated.at.column.title" text="更新時間"/>', data: 'updatedAt', className: 'dt-center', width: '15%',
				render: function (data) {
					return DateUtil.toDateTime(data);
			}}
		],
		drew: function (dataTable) {
			$(".resizable").resizable({handles: "se"});
		}
	};
	
	if (HtmlUtil.tempData && 
		HtmlUtil.tempData.logList &&
		HtmlUtil.tempData.logList.length > 0) {
		options.data = HtmlUtil.tempData.logList;
	} else if (headForm.formId) {
		SendUtil.post('/commonForm/getLogList', headForm, function (resData) {
			options.data = resData.logList;
		}, ajaxSetting);
	}
		
	dataTable = TableUtil.init('#logList', options);
}

function clickLogSaveButton () {
	var headForm = form2object('headForm');
	var checkedList = $('#logList input[name="check"]').is(':checked');

	if (!headForm.formId) {
		alert('<s:message code="log.tab.alert.error.1" text="尚未暫存或發送申請單, 請起單之後再更新日誌。"/>');
		return;
	}
	
	if (!checkedList) {
		alert('<s:message code="log.tab.alert.message.4" text="請選擇要更新或新增的項目。"/>');
		return;
	}
	
	if (confirm('<s:message code="log.tab.alert.message.5" text="確定新增?"/>')) {
		var dataList = [];
		var options = {summary : 'textarea'};
		
		$.each($('#logList input[name="check"]'), function () {
			if ($(this).is(':checked')) {
				var item = TableUtil.getRow(dataTable, this, options);
				item.formId = $('input#formId').val();
				dataList.push(item);
			}
		});

		SendUtil.post('/commonForm/saveLogList', dataList, function () {
			alert('<s:message code="log.tab.alert.message.6" text="更新成功"/>');
			SendUtil.post('/commonForm/getLogList', form2object('headForm'), function (resData) {
				HtmlUtil.tempData.logList = resData.logList;
				TableUtil.reDraw(dataTable, resData.logList);
				$('input#checkall').prop('checked', false);
			});
		});
	}
}

function clickLogNewButton () {
	var item = {
		name : loginUserInfo.name,
		createdBy : loginUserInfo.userId,
		updatedBy : loginUserInfo.userId
	};
	
	TableUtil.addRow(dataTable, item);
}

function clickLogDeleteButton () {
	var checkedList = $('#logList input[name="check"]').is(':checked');
	
	if (!checkedList) {
		alert('<s:message code="log.tab.alert.message.1" text="請選擇要刪除的項目。"/>');
		return;
	}
	
	if (confirm('<s:message code="log.tab.alert.message.2" text="確認刪除?"/>')) {
		var deleteList = [];
		
		$.each($('#logList input[name="check"]'), function () {
			if ($(this).is(':checked')) {
				var item = TableUtil.getRow(dataTable, this);
				
				if (item.id) {
					item.formId = $('input#formId').val();
					deleteList.push(item);
				}
				
				TableUtil.deleteRow(dataTable, this);
			}
		});
		
		SendUtil.post('/commonForm/deleteLogList', deleteList, function () {
			HtmlUtil.temporary();
			alert('<s:message code="log.tab.alert.message.3" text="刪除成功"/>');
			$('input#checkall').prop('checked', false);
		});
	}
}

// 全選
function checkall (checkallBox) {
	var checkboxs = '#logList input[name="check"]';
	TableUtil.checkall(checkallBox, checkboxs);
}

// 監聽全選按鈕是否依然開啟/關閉
function checkallBoxChangeEvent () {
	var checkallBox = 'input#checkall';
	var checkboxs = '#logList input[name="check"]';
	TableUtil.checkallChangeEvent(checkallBox, checkboxs);
}
</script>

<fieldset class='search' style="margin-bottom: 0 !important;">
	<table class='grid_query'>
		<tbody>
			<tr><td><span style="color: red; ">為確保日誌修改紀錄正確且精準，因此必須「勾選」想要更新的日誌才能「儲存」或「刪除」。</span></td></tr>
		</tbody>
	</table>
</fieldset>

<div id='logButtons' class="grid_BtnBar">
	<button id='logNewButton' onclick='clickLogNewButton();' style='display:none'>
		<i class="iconx-add"></i><s:message code="button.add" text="新增"/>
	</button>
	<button id='logDeleteButton' onclick='clickLogDeleteButton();' style='display:none'>
		<i class="iconx-delete"></i><s:message code="button.delete" text="刪除"/>
	</button>
	<button id='logSaveButton' onclick='clickLogSaveButton();' style='display:none'>
		<i class="iconx-save"></i><s:message code="button.save" text="儲存"/>
	</button>
</div>

<table id="logList" class="display collapse cell-border">
	<thead></thead>
	<tbody></tbody>
</table>
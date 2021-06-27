<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<script>
var count = 0;
var delSegmentIds = [];
var segmentTable, otherTable;

$(function () {
	DateUtil.dateTimePicker();
	initView();
	fillCForm('tabDbAlterForm', 'DB');
    authViewControl();
});

function initView () {
	let formId = $('input#formId').val();
	delSegmentIds = [];
	setupFiles();
	setupSegments();
	$('form#tabDbAlterForm .resizable-textarea').resizable({handles: "se"});
}

function setupFiles () {
	let reqData = form2object('headForm');
	reqData.type = 'DB';
	SendUtil.post('/commonForm/getFileList', reqData, function (resData) {
		let otherOpt = {
			checkbox: true,
			data : resData,
			columnDefs: [
				{orderable: false, targets: [0]},  
				{
					targets: [0], 
					data: 'id', 
					className: 'dt-center clickable',
					title: '<input id="checkallOther" type="checkbox" onchange="checkallFunc(\'#otherTable\', \'#checkallOther\')" />', 
					render: function (data) {
						return '<input name="check" value="' + data + '" type="checkbox" onchange="checkallChangeEvent(\'#otherTable\', this, \'#checkallOther\')" />'
					}
				}, 
				{targets: [1], title: "檔名", data: 'name', className: 'dt-center fileNameText',
					render: function (data, type, row) {
						payload = '/{0}/{1}';
						extension = row.name.split('.');
						payload = StringUtil.format(payload, row.id, row.formId, extension[0], extension[1]);
						
						return'<a href="' + contextPath + '/commonForm/download' + payload + '">' + data + '</a>';
					}}, 
				{targets: [2], title: '檔案大小(MB)', data: 'data', className: 'dt-center clickable'}, 
				{targets: [3], title: "說明", data: 'description', className: 'dt-center clickable'}, 
				{targets: [4], title: "變更內容", data: 'alterContent', className: 'dt-center clickable', 
			    	render: function (data) {
			    		return subStr(data);
			    	}}, 
				{targets: [5], title: "Layout Dataset", data: 'layoutDataset', className: 'dt-center clickable',
			    	render: function (data) {
			    		return subStr(data);
			    	}}
			]
		};
		
		if (TableUtil.isDataTable('#otherTable')) {
			TableUtil.reDraw(otherTable, resData);
		} else {
			otherTable = TableUtil.init('#otherTable', otherOpt);
		}
		
		HtmlUtil.clickRowCheckboxEvent('#otherTable tbody');
	}, null, true);
}

function setupSegments () {
	let tabInfo = HtmlUtil.tempData.tabDbAlterForm;
	let dataList = tabInfo ? tabInfo.segments : [];
	let segmentOpt = {
		checkbox: true,
		data : dataList,
		init: initSegments,
		drew: drawSegments,
		pageLength: 3,
		columnDefs: [
			{orderable: false, targets: [0]},  
			{
				targets: [0], 
				data: 'id', 
				className: 'dt-center clickable',
				title: '<input id="checkallSegment" type="checkbox" onchange="checkallFunc(\'#segmentTable\', \'#checkallSegment\')" />', 
				render: function (data, type, full, meta) {
					return '<input name="check" value="' + data + '" type="checkbox" onchange="checkallChangeEvent(\'#segmentTable\', this, \'#checkallSegment\')" />'
				}
			}, 
			{targets: [1], title: "Segment名稱", data: 'segment', className: 'dt-center clickable', width: '10%', 
		    	render: function (data) {
		    		return subStr(data);
		    	}}, 
			{targets: [2], title: "Key Value", data: 'keyValue', className: 'dt-center clickable', 
		    	render: function (data) {
		    		return subStr(data);
		    	}}, 
			{targets: [3], title: "變更的欄位", data: 'alterColumns', className: 'dt-center clickable', 
		    	render: function (data) {
		    		return subStr(data);
		    	}}, 
			{targets: [4], title: "變更內容", data: 'alterContent', className: 'dt-center clickable', 
		    	render: function (data) {
		    		return subStr(data);
		    	}}, 
			{targets: [5], title: "Layout Dataset", data: 'layoutDataset', className: 'dt-center clickable', 
			    	render: function (data) {
			    		return subStr(data);
			    	}}, 
		    {targets: [6], title: '功能', data: '', className: 'dt-center', 
		    	render: function (data, type, full, meta) {
		    		data = '<input class="hidden" value="' + meta.row + '" />';
		    		data += '<button type="button" onclick="editSegment(this)">編輯</button>';
		    		return data;
		    	}, width: '5%'},
			{targets: [7], title: "", data: 'type', className: 'dt-center hidden'},
			{targets: [8], title: "", data: 'formId', className: 'dt-center hidden'}
		]
	};
	
	if (TableUtil.isDataTable('#segmentTable')) {
		TableUtil.reDraw(segmentTable, dataList);
	} else {
		segmentTable = TableUtil.init('#segmentTable', segmentOpt);	
	}
	
	HtmlUtil.clickRowCheckboxEvent('#segmentTable tbody');
}

function newSegment () {
	SegmentDialog.show(function (row) {
		row.id='new' + count++;
	    TableUtil.addRow(segmentTable, row);
	});
}

function editSegment (btn) {
	let number = $(btn).siblings('input').val();
	let row = TableUtil.getRow(segmentTable, btn);
	let page = TableUtil.getInfo(segmentTable).page;
	
	row.page = page;
	row.number = number;
	SegmentDialog.edit(row, function (row) {
		TableUtil.editRow(segmentTable, btn, row);
	});
}

function pickSegments (checkbox) {
	if (segmentTable) {
		let id = $(checkbox).val();
		
		if ($(checkbox).is(':checked')) {
			delSegmentIds.push(id);
		} else {
			delSegmentIds = ObjectUtil.arrayRemove(delSegmentIds, id);
		}
	}
}

function drawSegments (dataTable) {
	let temp = {};
	let isInArray = false;
	let $checkboxs = $('#segmentTable input[type="checkbox"]');
	
	$('#checkallSegment').prop('checked', false);
	$.each($checkboxs, function () {
		temp = TableUtil.getRow(dataTable, $(this));
		
		if (temp) {
			isInArray = $.inArray(temp.id.toString(), delSegmentIds) != -1;
			$(this).prop('checked', isInArray).change();
		}
	});
	
	$checkboxs.change(function () {
	    pickSegments(this);
	});
}

function delSegment() {
	var headForm = form2object('headForm');
	
	if (!headForm.formId) {
		alert('<s:message code="file.tab.alert.error.2" text="尚未暫存或發送申請單, 請起單之後再刪除項目。"/>');
		return;
	}
	
	if (delSegmentIds.length == 0) {
		alert('請選擇要刪除的項目。');
		return;
	}
	
	if (confirm('確定刪除?')) {
		HtmlUtil.temporary();
		let isInArray = false;
		let clear = [], delIds = [];
		let tabId = 'tabDbAlterForm';
		let rows = TableUtil.getRows(segmentTable, {page:'all'});
		
		$.each(rows, function (i, item) {
			isInArray = $.inArray(item.id.toString(), delSegmentIds) != -1;
			if (!isInArray) {
				clear.push(item);
			}
		});
		
		$.each(delSegmentIds, function (i, id) {
			if (id.indexOf('new') == -1) {
				delIds.push(id);
			}
		});
		
	    SendUtil.post("/commonJobForm/deleteSegments", delIds, function () {
	    	let formId = form2object('headForm').formId;
	    	delSegmentIds = [];
	    	HtmlUtil.tempData.tabDbAlterForm.segments = clear;
	 	 	setupSegments();
		}, null, true);
	}
}

function newOtherFile () {
	var headForm = form2object('headForm');
	
	if (!headForm.formId) {
		alert('<s:message code="file.tab.alert.error.1" text="尚未暫存或發送申請單, 請起單之後再上傳檔案。"/>');
		return;
	}
	
	UploadDialog.show('tabDb');
}

function delOtherFile () {
	var checkedList = $('#otherTable input[name="check"]').is(':checked');
	
	if (!checkedList) {
		alert('<s:message code="file.tab.alert.message.1" text="請選擇要刪除的項目。"/>');
		return;
	}
	
	if (!confirm('<s:message code="file.tab.alert.message.2" text="確認刪除?"/>')) {
		return;
	}
	
	var deleteList = [];
	var deleteDenyMsg = "";
	var info = form2object('headForm');
	var isChg = ValidateUtil.formInfo().isChg(info);
	var isAdmin = ValidateUtil.loginRole().isAdmin();
	var isUserSolving = info.userSolving == loginUserInfo.userId;
	
	$.each($('#otherTable input[name="check"]'), function () {	
		if (!$(this).is(':checked')) {
			return true;
		}

		let islocked = $(this).closest("tr").find(".islocked").text();
		let fileName = $(this).closest("tr").find(".fileNameText").text();
		let notUserSolving = "<s:message code='file.tab.button.delete.file.not.user' text='檔案: {0} 非處理人員,無法進行刪除'/>";
		let fileLocked = "<s:message code='file.tab.button.delete.file.is.locked' text='檔案: {0} 非本流程關卡所上傳的檔案,無法進行刪除'/>" 
		
		if (isAdmin) {
			addDelItem(deleteList, otherTable, this);
		} else if (isChg) {
			if (islocked == "N") {
				addDelItem(deleteList, otherTable, this);
			} else {
				deleteDenyMsg += StringUtil.format(fileLocked, fileName) + "\r\n";
			}
		} else if (isUserSolving) {
			addDelItem(deleteList, otherTable, this);
		} else {
			deleteDenyMsg += StringUtil.format(notUserSolving, fileName) + "\r\n";
		}
	});
	
	if(deleteDenyMsg) {
		alert(deleteDenyMsg);
		$('input#checkall').prop('checked', false);
	} else {
		SendUtil.post('/commonForm/deleteFileList', deleteList, function (resData) {
			alert('<s:message code="file.tab.alert.message.3" text="刪除成功"/>');
			$('input#checkall').prop('checked', false);
			
			TableUtil.reDraw(otherTable, resData);
		});
	}
}

function initSegments (dataTable) {
	let $checkboxs = $('#segmentTable input[type="checkbox"]');
	
	// Data Table每畫一筆資料就會觸發change()1次, 
	// 所以在Table初始化完成之後先把change()事件移除, 
	// 等Table畫完再重新綁定。
	$checkboxs.unbind('change');
	$('#segmentTable').unbind('page.dt').on('page.dt', function () {
		$checkboxs.unbind('change');
	});
}

// 全選
function checkallFunc (tableId, checkallId) {
    let checkallBox = checkallId;
    let checkboxs = tableId + ' input[name="check"]';
    TableUtil.checkall(checkallBox, checkboxs);
    
    $.each($(checkboxs), function () {
	    pickSegments(this);
    });
}

// 監聽全選按鈕是否依然開啟/關閉
function checkallChangeEvent (tableId, checkbox, checkallId) {
    let checkallBox = checkallId;
    let checkboxs = tableId + ' input[name="check"]';
    TableUtil.checkallChangeEvent(checkallBox, checkboxs);
}

function subStr (str, limit) {
	limit = limit ? limit : 60;
	return str.length > limit ? str.substring(0, limit) + '...' : str;
}

function addDelItem (deleteList, dataTable, row) {
	let item = TableUtil.getRow(dataTable, row);
	item.formId = $('input#formId').val();
	deleteList.push(item);
}

function isSavedForm () {
	let headForm = form2object('headForm');
	let formId = headForm.formId;
	let clazz = headForm.formClass;
	let isSubForm = clazz.indexOf('_C') != -1;
	
	if (!formId && isSubForm) {
		return '請先儲存表單!';
	}
	
	return '';
}
</script>
<fieldset>
	<legend>資料庫變更申請單</legend>
	<form id='tabDbAlterForm'>
        <input id="id" class="hidden" name="id" />
        <input id="type" class="hidden" name="type" value='DB' />
		<table class="grid_query">
			<tr>
				<th>資料庫名稱</th>
				<td><input id='dbname' name='dbname' type="text" /></td>
				<th>PSB名稱</th>
				<td><input id='psbname' name='psbname' type="text" /></td>
				<th>變更方式</th>
				<td><input id='alterWay' name='alterWay' type="text" /></td>
				<th>預計實施日期</th>
				<td>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="eit" />
						<jsp:param name="name1" value="eit" />
					</jsp:include>
				</td>
				<th>實際實施日期</th>
				<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
					<jsp:param name="id1" value="ast" />
					<jsp:param name="name1" value="ast" />
				</jsp:include>
			</tr>
		</table>
		<fieldset>
			<div class="grid_BtnBar">
				<button id='newSegmentBtn' type='button' onclick='newSegment()'><i class="iconx-add"></i> 新增</button>
				<button id='delSegmentBtn' type='button' onclick='delSegment()'><i class="iconx-delete"></i> 移除</button>
			</div>
			<table id="segmentTable" class="display collapse cell-border">
				<thead></thead>
				<tbody></tbody>
			</table>
		</fieldset>
		<fieldset>
			<legend>其他(請直接附加DB變更文件)</legend>
			<div class="grid_BtnBar">
				<button id='newOtherBtn' type='button' onclick='newOtherFile();'>
					<i class="iconx-upload-file"></i> <s:message code="file.tab.button.add.file" text="新增附件"/>
				</button>
				<button id='delOtherBtn' type='button' onclick='delOtherFile();'>
					<i class="iconx-delete"></i> <s:message code="button.delete" text="刪除"/>
				</button>
			</div>
			<table id="otherTable" class="display collapse cell-border">
				<thead></thead>
				<tbody></tbody>
			</table>
		</fieldset>
		<fieldset>
			<legend>備註</legend>
			<textarea id='remark' name='remark' class='resizable-textarea' cols="50" rows="4" maxlength="3000" ></textarea>
		</fieldset>
	</form>
</fieldset>
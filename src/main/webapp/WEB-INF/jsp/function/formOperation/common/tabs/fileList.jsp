<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<script>//# sourceURL=fileList.js
var reqData, dataTable;

$(function () {
	initView();
	initDataTable();
	authViewControl();
});

function initView () {
	SendUtil.post('/commonForm/getFileLimitInfo', null, function (response) {
		$('p#fileSize').html(response.fileSize);
		$('p#fileExtension').html(response.fileExtension);
	});
}

function initDataTable () {
	reqData = form2object('headForm');
	reqData.type = 'FILE';
	SendUtil.post('/commonForm/getFileList', reqData, function (resData) {
		fileData = resData;
		
		var payload = '';
		var extension = [];
		var options = {
			checkbox: true,
			data: resData,
			columnDefs: [
				{orderable: false, targets: [0]}, 
				{
					targets: [0], 
					data: 'id', 
					className: 'dt-center clickable',
					title: '<input id="checkall" type="checkbox" onchange="checkall(this)" />', 
					render: function (data) {
						return '<input name="check" value="' + data + '" type="checkbox" onchange="checkallBoxChangeEvent()" />'
					}
				}, 
				{targets: [1], title: '<s:message code="file.tab.name" text="檔案名稱"/>', data: 'name', className: 'dt-center fileNameText clickable',
					render: function (data, type, row) {
						payload = '/{0}/{1}';
						extension = row.name.split('.');
						payload = StringUtil.format(payload, row.id, row.formId, extension[0], extension[1]); 
						
						return'<a href="' + contextPath + '/commonForm/download' + payload + '">' + data + '</a>';
					}}, 
				{targets: [2], title: '<s:message code="file.tab.comment" text="檔案說明"/>', data: 'description', className: 'dt-center clickable'},
				{targets: [3], title: '<s:message code="file.tab.size" text="檔案大小(MB)"/>', data: 'data', className: 'dt-center clickable'},
				{targets: [4], title: '<s:message code="file.tab.upload.user" text="上傳人員"/>', data: 'createdBy', className: 'hidden'},
				{targets: [5], title: '<s:message code="file.tab.upload.time" text="上傳時間"/>', data: 'createdAt', className: 'hidden',
					render: function (data) {
						return DateUtil.toDateTime(data);
					}},
				{targets: [6], data: 'islocked', className: 'hidden islocked'}
			]
		};
		dataTable = TableUtil.init('#reportList', options);
	});
}

function clickFileListNewButton () {
	let headForm = form2object('headForm');
	let isVice = ValidateUtil.loginRole().isVice();
	
	if (!headForm.formId) {
		alert('<s:message code="file.tab.alert.error.1" text="尚未暫存或發送申請單, 請起單之後再上傳檔案。"/>');
		return;
	}
	
	if (isVice && !headForm.isVerifyAcceptable) {
		let buttonEvents = {};
		buttonEvents["cancel"] = function () {};
		buttonEvents["confirm"] = function () {
			let data = $.extend(form2object('headForm'), form2object('modifyForm'), HtmlUtil.tempData.infoForm);
			ObjectUtil.dataToBackend(data, allDateArgs);
			
			if (!data.modifyComment) {
				alert("<s:message code='form.modify.reason.not.empty' text='請填寫修改原因!'/>");
				return;
			}
			
			if (confirm('<s:message code="file.tab.alert.message.confirm" text="確認送出當前修改原因?"/>')) {
				SendUtil.post(getFormDomain() + '/modifyColsByVice', data, function (response) {
					fetchInfo(response);
					DialogUtil.close();
					UploadDialog.show('tabFileList');
				}, null, true);
			}
		}
		ModifyingDialog.show(formInfo, buttonEvents, type);
	} else {
		UploadDialog.show('tabFileList');
	}
}

function clickFileListDeleteButton () {
	var checkedList = $('#reportList input[name="check"]').is(':checked');
	
	let headForm = form2object('headForm');
	let isVice = ValidateUtil.loginRole().isVice();
	
	if (!checkedList) {
		alert('<s:message code="file.tab.alert.message.1" text="請選擇要刪除的項目。"/>');
		return;
	}
	
	if (isVice && !headForm.isVerifyAcceptable) {
		let buttonEvents = {};
		buttonEvents["cancel"] = function () {};
		buttonEvents["confirm"] = function () {
			let data = $.extend(form2object('headForm'), form2object('modifyForm'), HtmlUtil.tempData.infoForm);
			ObjectUtil.dataToBackend(data, allDateArgs);	

			if (!data.modifyComment) {
				alert("<s:message code='form.modify.reason.not.empty' text='請填寫修改原因!'/>");
				return;
			}
			
			if (confirm("<s:message code='file.tab.alert.message.2' text='確認刪除?'/>")) {
				SendUtil.post(getFormDomain() + '/modifyColsByVice', data, function (response) {
					deleteItemByUser();
					fetchInfo(response);
					DialogUtil.close();
				}, null, true);
			}
		}
		
		ModifyingDialog.show(formInfo, buttonEvents, type);
	} else if (confirm('<s:message code="file.tab.alert.message.2" text="確認刪除?"/>')) {
		deleteItemByUser();
	}
}
	
function deleteItemByUser () {
	var deleteList = [];
	var deleteDenyMsg = "";
	var info = form2object('headForm');
	var isChg = ValidateUtil.formInfo().isChg(info);
	var isAdmin = ValidateUtil.loginRole().isAdmin();
	var isVice = ValidateUtil.loginRole().isVice();
	var isUserSolving = info.userSolving == loginUserInfo.userId;
	
	$.each($('#reportList input[name="check"]'), function () {	
		if (!$(this).is(':checked')) {
			return true;
		}

		let islocked = $(this).closest("tr").find(".islocked").text();
		let fileName = $(this).closest("tr").find(".fileNameText").text();
		let notUserSolving = "<s:message code='file.tab.button.delete.file.not.user' text='檔案: {0} 非處理人員,無法進行刪除'/>";
		let fileLocked = "<s:message code='file.tab.button.delete.file.is.locked' text='檔案: {0} 非本流程關卡所上傳的檔案,無法進行刪除'/>";
		
	   /**
		* 1.副科在自己關卡可刪附件，不存簽核紀錄
		* 2.副科於非審核關卡和已結案表單可刪附件，存簽核紀錄
		* 3.變更單邏輯
		* 4.新增需求:變更單，副科可刪除他人上傳之附件
		*/
		if (isAdmin) { 
			addDelItem(deleteList, dataTable, this);
		} else if (isChg) {
			if (isVice || islocked == "N") {
				addDelItem(deleteList, dataTable, this);
			} else {
				deleteDenyMsg += StringUtil.format(fileLocked, fileName) + "\r\n";
			}
		} else if (isVice || isUserSolving) {
			addDelItem(deleteList, dataTable, this);
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
			
			TableUtil.reDraw(dataTable, resData);
		});
	}
}

function addDelItem (deleteList, dataTable, row) {
	let item = TableUtil.getRow(dataTable, row);
	item.formId = $('input#formId').val();
	deleteList.push(item);
}

// 全選
function checkall (checkallBox) {
	var checkboxs = '#reportList input[name="check"]';
	TableUtil.checkall(checkallBox, checkboxs);
}

// 監聽全選按鈕是否依然開啟/關閉
function checkallBoxChangeEvent () {
	var checkallBox = 'input#checkall';
	var checkboxs = '#reportList input[name="check"]';
	TableUtil.checkallChangeEvent(checkallBox, checkboxs);
}
</script>

<fieldset class='search' style="margin-bottom: 0 !important;">
	<table class='grid_query'>
		<tbody>
			<tr><td><span style="color: red; ">1.可上傳的檔案大小：<p id='fileSize' style='display:inline-block'></p>Mb內</span></td></tr>
			<tr><td><span style="color: red; ">2.可上傳的文件副檔名：<p id='fileExtension' style='display:inline-block'></p></span></td></tr>
		</tbody>
	</table>
</fieldset>

<div id='fileButtons' class="grid_BtnBar">
	<button id='fileListNewButton' onclick='clickFileListNewButton();' style='display:none'>
		<i class="iconx-upload-file"></i> <s:message code="file.tab.button.add.file" text="新增附件"/>
	</button>
	<button id='fileListDeleteButton' onclick='clickFileListDeleteButton();' style='display:none'>
		<i class="iconx-delete"></i> <s:message code="button.delete" text="刪除"/>
	</button>
</div>

<table id="reportList" class="display collapse cell-border">
	<thead></thead>
	<tbody></tbody>
</table>
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<script>//# sourceURL=questionMaintian.js
var dataTable;
var ajaxSetting = {async:false};

var QuestionFind = function () {
	var options = {
		orderByDef: [0,'DESC'],
		orderBy: ['id'],
		queryPath: '/system/questionMaintain/getParameterList',
		columnDefs: [
			{targets: [0,1], orderable: false},
			{targets: [0], title: '<s:message code="question.table.field.analysis" text="分析" />', className: 'dt-center', data: 'id', width: '5%'},
			{targets: [1], title: '<input type="checkbox" id="checkall" onchange="checkall(this)"/>', data: 'id', className: 'dt-center hidden', width: '5%',
				render: function (data) {
					data = !data ? "" : data;
					return "<input type='checkbox' name='id' value='" + data + "' onchange='checkallBoxChangeEvent()'/>";
				}},
			{targets: [2], title: '<s:message code="question.table.field.content" text="題目內容" />', data: 'content', className: 'dt-center',
				render: function (data) {
					data = !data ? "" : data;
					return "<textarea class='question-maintain resizable' type='text' name='content' rows='1' cols='1' maxlength='200' sytle='width:90%' >" + data + "</textarea>";
				}}, 
			{targets: [3], title: '<s:message code="question.table.field.score.option" text="分數選項" />', data: 'fraction', className: 'dt-center', width: '15%',
				render: function (data) {
					data = !data ? "" : data;
					return "<textarea class='question-maintain resizable' type='text' name='fraction' rows='1' cols='1' maxlength='200' sytle='width:90%' >" + data + "</textarea>";
				}}, 
			{targets: [4], title: '<s:message code="question.table.field.score.statement" text="分數說明" />', data: 'description', className: 'dt-center', width: '25%',
				render: function (data) {
					data = !data ? "" : data;
					return "<textarea class='question-maintain resizable' type='text' name='description' rows='1' cols='1' maxlength='200' sytle='width:90%' >" + data + "</textarea>";
				}},
			{targets: [5], title: '<s:message code="question.table.field.check.score" text="是否需要檢核分數" />', data: 'isValidateFraction', className: 'dt-center', width: '10%',
				render: function (data) {
					let selectStr = "<select id='isValidateFraction' name='isValidateFraction'><option value='N'><s:message code='global.select.option.n' text='否' /></option>{0}</select>";
					if("Y" == data) {
						selectStr = StringUtil.format(selectStr,"<option value='Y' selected='selected'><s:message code='global.select.option.y' text='是' /></option>");
					} else {
						selectStr = StringUtil.format(selectStr,"<option value='Y'><s:message code='global.select.option.y' text='是' /></option>");
					}
					
					return selectStr;
				}},
			{targets: [6], title: '<s:message code="question.table.field.creator" text="建立人員" />', data: 'createdBy', className: 'hidden',
				render: function (data) {
					data = !data ? "" : data;
					return "<textarea class='question-maintain resizable' type='text' name='createdBy' rows='1' cols='1'>" + data + "</textarea>";
				}},
			{targets: [7], title: '<s:message code="question.table.field.create.time" text="建立時間" />', data: 'createdAt', className: 'hidden',
				render: function (data) {
					data = !data ? "" : data;
					return "<textarea class='question-maintain resizable' type='text' name='createdAt' rows='1' cols='1'>" + data + "</textarea>";
				}},
			{targets: [8], title: '<s:message code="question.table.field.check.addup" text="是否加總" />', data: 'isAddUp', className: 'dt-center', width: '8%',
				render: function (data) {
					let selectStr = "<select id='isAddUp' name='isAddUp'><option value='N'><s:message code='global.select.option.n' text='否' /></option>{0}</select>";
					if("N" == data) {
						selectStr = StringUtil.format(selectStr,"<option value='Y'><s:message code='global.select.option.y' text='是' /></option>");
					} else {
						selectStr = StringUtil.format(selectStr,"<option value='Y' selected='selected'><s:message code='global.select.option.y' text='是' /></option>");
					}
					
					return selectStr;
				}},
			{targets: [9], title: '<s:message code="question.table.field.check.enable" text="是否啟用" />', data: 'isEnable', className: 'dt-center', width: '8%',
				render: function (data) {
					let selectStr = "<select id='isEnable' name='isEnable'><option value='N'><s:message code='global.select.option.n' text='否' /></option>{0}</select>";
					if("N" == data) {
						selectStr = StringUtil.format(selectStr,"<option value='Y'><s:message code='global.select.option.y' text='是' /></option>");
					} else {
						selectStr = StringUtil.format(selectStr,"<option value='Y' selected='selected'><s:message code='global.select.option.y' text='是' /></option>");
					}
					
					return selectStr;
				}},			
		],
		drew: function (dataTable) {
			$(".resizable").resizable({handles: "se"});
		}
	};

	var search = function () {
		options.request = {};
		dataTable = TableUtil.paging('#dataTable', options);
	}
	
	return {
		options : options,
		search : search
	}
}();

$(function(){
	QuestionFind.search();
});

function saveItems () {
	var message = validation();
	
	if (message) {
		alert(message);
		return;
	}
	
	if(!verifyFractionAndDescriptionMatch()) {
		return;
	}
	
	if (confirm("是否確定儲存?")) {
		SendUtil.post('/system/questionMaintain/updateParams', recordSaveDataList(), function (data) {
        	alert('更新成功!');
    		TableUtil.reDraw('#dataTable', QuestionFind.options, true);
		});
	} else {
		$('#dataTable tr:last').remove();
	}
	
}

function deleteItems () {
	if (!$('input[type="checkbox"]').is(':checked')) {
		alert('請至少勾選一項要刪除的資料。');
		return;
	}
	
	if(confirm("是否確定刪除?")){
		SendUtil.post('/system/questionMaintain/deleteParams', recordDeleteIds(), function (data) {
			$("#checkall").prop('checked', false);
		});
	}
}

function addItems () {
	$("input#checkall").prop('checked', false);
	TableUtil.addRow(dataTable);
}

// 全選
function checkall (checkallBox) {
	var checkboxs = '#dataTable input[name="id"]';
	TableUtil.checkall(checkallBox, checkboxs);
}

// 監聽全選按鈕是否依然開啟/關閉
function checkallBoxChangeEvent () {
	var checkallBox = 'input#checkall';
	var checkboxs = '#dataTable input[name="id"]';
	TableUtil.checkallChangeEvent(checkallBox, checkboxs);
}

function recordSaveDataList () {
	var name, value;
	var saveList = [];
	var saveData = {};
	var inputs = '#dataTable input[name="id"],textarea,select';

	$.each($(inputs), function () {
		name = $(this).get(0).name;
		value = $(this).get(0).value;
		saveData[name] = value;

		// 若表格往後有新增欄位，須更新換下一行的判斷
		if (name == 'isEnable') {
			saveList.push(saveData);
			saveData = {};
		}
	});

	return saveList;
}

function recordDeleteIds () {
	var itemId;
	var deleteList = [];
	var itemIds = '#dataTable input[name="id"]';
	
	$.each($(itemIds), function () {
		itemId = $(this).get(0).value;
		
		if(!$(this).is(":checked")){
			return true;
		}

		TableUtil.deleteRow(dataTable, this);
		
		if (!itemId) {
			return true;
		}

		deleteList.push({id: itemId});
	});
	
	return deleteList;
}

// 分數選項欄位只能填寫數字
function isFraction (value) {
	if(!/[^0-9]/.test(value)){
		return value > 0;
	} else {
		return false;
	}
}

function validation () {
	var message = '';
	var key, value, question, fractions, isMatch;
	var inputs = '#dataTable textarea[type="text"]';
	
	$.each($(inputs), function () {
		key = $(this).get(0).name;
		value = $(this).get(0).value;
		question = $(this).closest('tr').find('td:first').text();
		question = StringUtil.format('第{0}題：', question);
		question = !message ? question : '\n' + question;
		isMatch = !message.match($.trim(question));
		
		// 內容
		if(key === 'content' && !value){
			value = '「題目 」內容不可空白';
			message += isMatch ? 
					(question + value)  : '、' + value;
		}
		
		// 分數
		if (key === 'fraction'){
			if(!value){
				value = '「分數」內容不可空白';
				message += isMatch ? 
						(question + value)  : '、' + value;
			}
		}
		
		// 分數說明
		if (key === 'description'){
			if(!value){
				value = '「分數說明」內容不可空白';
				message += isMatch ? 
						(question + value)  : '、' + value;
			}
		}
	});
	
	return message;
}

/**
 * 檢查衝擊分析的分數設定以及分數描述數量是否一致 
 *
 */
function verifyFractionAndDescriptionMatch() {
	let trObject = $("#dataTable").find("tr");
	let alertMessage = "";
	
	$.each(trObject,function(index,element) {
		let fraction = $(this).find("[name='fraction']").val();
		let description = $(this).find("[name='description']").val();

		if(fraction && description) {//排除掉第一欄的header
			//避免使用者沒有輸入最末尾的 ; 號,這邊檢查最後一個字串,若沒有輸入的話則幫其拼湊上去
			if(fraction.substring(fraction.length -1) != ";") {
				fraction = fraction + ";";
			}
			
			if(description.substring(description.length -1) != ";") {
				description = description + ";";
			}
			
			if(fraction.indexOf(";") == -1 || description.indexOf(";") == -1 ) {
				let message = "問題{0},輸入的內容必須帶有 ; 號  \r\n";
				alertMessage = alertMessage + StringUtil.format(message,index);
			} else {
				fraction = fraction.split(";");
				description = description.split(";");
				
				if(fraction.length != description.length) {
					let message = "問題{0},分數選項與分數說明數量不一致,請檢查是否有遺漏  ; 號  \r\n";
					alertMessage = alertMessage + StringUtil.format(message,index);
				}
			}		
			
		}
	});
	if(alertMessage != "") {
		alert(alertMessage);
		return false;
	} else {
		return true;
	}
}

// 勾選項目設定 啟用/停用
function enableItems(isEnable) {
	var itemId;
	var itemIds = '#dataTable input[name="id"]';
	
	$.each($(itemIds), function () {
		itemId = $(this).get(0).value;
		
		if(!$(this).is(":checked")){
			return true;
		}
		
		$(this).closest('tr').find("#isEnable").val(isEnable);
	});
}
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="question.title" text='衝擊分析題庫維護' /></h1>
	<fieldset>
		<legend><s:message code="question.list" text='題庫列表' /></legend>
		<div class="grid_BtnBar">
			<button type="button" onclick="saveItems()"><i class="iconx-save"></i><s:message code="button.save" text='儲存' /></button>
			<button type="button" onclick="QuestionMaintainDialog.show()"><i class="iconx-add"></i><s:message code="button.add" text='新增' /></button>
			<button type="button" onclick="enableItems('Y')" class="hidden" ><i class="iconx-enable"></i><s:message code="button.enable" text='啟用' /></button>
			<button type="button" onclick="enableItems('N')" class="hidden" ><i class="iconx-disable"></i><s:message code="button.disable" text='停用' /></button>
		</div>
		<table id="dataTable" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
	<%-- include dialog from questionMaintainDialog for add --%>
	<jsp:include page='/WEB-INF/jsp/common/dialogs/questionMaintainDialog.jsp'>
		<jsp:param name="function" value="questionMaintain" />
	</jsp:include>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
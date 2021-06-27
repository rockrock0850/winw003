<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>
<h1><s:message code="system.name.manage.title" text='系統名稱管理' /></h1>
<fieldset class="search">
	<legend><s:message code="global.query.condition" text='查詢條件' /></legend>
	<button class="small fieldControl searchPanel">
		<i class="iconx-collapse"></i>|||
	</button>	
	<form id="queryForm">
		<table class="grid_query">
			<tr>
				<th>&nbsp;<s:message code="system.name.id.search" text='系統名稱(代碼)' /></th>
				<td><input type="text" id="systemId" name="systemId" maxlength="100" ></td>
				<th>&nbsp;<s:message code="system.chinese.name.description" text='系統中文說明' /></th>
				<td><input type="text" id="systemName" name="systemName" maxlength="500" ></td>
				<th>&nbsp;<s:message code="system.source.group" text='資訊資產群組' /></th>
				<td><input type="text" id="mark" name="mark" maxlength="500" ></td>
				<th>&nbsp;<s:message code="system.management.department" text='科別' /></th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="divisionCreated" />
						<jsp:param name="name" value="department" />
						<jsp:param name="isDisabled" value="false" />
					</jsp:include>
				</td>
				<td>
					<button id="searchBtn" type="button" style="width:40px;">
						<i class="iconx-search"></i> <s:message code="button.search" text='查詢' />
					</button>
				</td>
				<td>
					<button id="addBtn" type="button" onclick="SystemNameDialogAdd.show();" style="width:40px;">
					    <i class="iconx-add"></i> <s:message code="button.add" text='新增' />
					</button>
				</td>
			</tr>
		</table>
	</form>
</fieldset>

<fieldset>
	<legend><s:message code="global.query.result" text='查詢結果' /></legend>
	<div class="grid_BtnBar" style="display:none">
		<button id="saveBtn">
			<i class="iconx-save"></i><s:message code="button.save" text='儲存' />
		</button>
	</div>
	<table id="dataTable" class="display collapse cell-border">
		<thead></thead>
		<tbody></tbody>
	</table>
</fieldset>

<%-- include dialog from systemNameDialog for add and edit --%>
<jsp:include page='/WEB-INF/jsp/common/dialogs/systemNameDialog.jsp'>
	<jsp:param name="isAdding" value="true" />
</jsp:include>

<jsp:include page='/WEB-INF/jsp/common/dialogs/systemNameDialog.jsp'>
	<jsp:param name="isEditing" value="true" />
</jsp:include>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>
<script>//# sourceURL=systemNameManagementFind.js
var dataTable;
var ajaxSetting = {async:false};

$(function() {
	dataTableInit();
	initSelectView();

<%-- 查詢按鈕--%>
$('#searchBtn').click(function() {
	let params = form2object("queryForm");		
	SendUtil.post('/systemNameManagement/init/getSystemManagmentByCondition', params, function (data) {			
		TableUtil.reDraw(dataTable, data);
		var editTable = TableUtil.getRows(dataTable);
		editTable.forEach(function(element) {
			if(element.id == dataTable[element.id]) {
				dataTable.department = editTable.department;
				return false;
			}
		});
	},null,true);
  });
});

<%-- 動態下拉選單 --%>
function initSelectView() {
	SendUtil.get("/html/getDivisionSelectors", true, function (option) {
		HtmlUtil.singleSelect('select#divisionCreated', option);
	}, ajaxSetting);
}

<%-- 檢查欄位  --%>
function validate(param) { 
	if(!param.systemId || !param.systemName || !param.mark || !param.department){
		alert("請輸入 系統名稱(代碼)\n請輸入 系統中文說明\n請輸入 資訊資產群組\n請選擇 科別\n" + 
			  "請輸入 Opinc\n請輸入 Apinc\n請輸入 極限值\nOpinc、Apinc、極限值必須為數字");
		return false; 
	}
	
	if(!param.opinc || parseFloat(param.opinc).toString() == "NaN" ||  
	   !param.apinc || parseFloat(param.apinc).toString() == "NaN" ||  
	   !param.limit || parseFloat(param.limit).toString() == "NaN" ){
		alert("請輸入 Opinc\n請輸入 Apinc\n請輸入 極限值\nOpinc、Apinc、極限值必須為數字");
		return false; 
	}
	return true; 
}

<%-- DataTable初始化  --%>
function dataTableInit() {
	var formList = {};//若有需要進行初始查詢的話,再修改其來源	
	var options = {
			data : formList,
			pageLength: 20,
			order: [10,'DESC'],
			columnDefs : [
				{orderable: false, targets: []}, 
				{width: '0%', targets: [0], title: '', data: 'id', className: 'hidden id' }, 
				{width: '10%', targets: [1], title: '<s:message code="system.name.id" text="系統名稱(代碼)" />', data: 'systemId', className: 'dt-center'},
				{width: '16%', targets: [2], title: '<s:message code="system.chinese.name.description" text="系統中文說明" />', data: 'systemName', className: 'dt-center'}, 
				{width: '6%', targets: [3], title: '<s:message code="system.department.column.title" text="科別" />', data: 'department', className: 'dt-center'}, 
				{width: '22%', targets: [4], title: '<s:message code="system.mark.column.title" text="資訊資產群組" />', data: 'mark', className: 'dt-center'}, 
				{width: '5%', targets: [5], title: '<s:message code="system.opinc.column.title" text="Opinc" />', data: 'opinc', className: 'dt-center'}, 
				{width: '5%', targets: [6], title: '<s:message code="system.apinc.column.title" text="Apinc" />', data: 'apinc', className: 'dt-center'}, 
				{width: '5%', targets: [7], title: '<s:message code="system.limit.column.title" text="極限值" />', data: 'limit', className: 'dt-center'}, 
				{width: '5%', targets: [8], title: '<s:message code="system.active.column.title" text="狀態" />', data: 'active', className: 'dt-center',
					render : function(data) {
						if (data == 'Y') {
							return '啟用';
						} else if (data == 'N') {
							return '<font color="red">停用</font>';
						} else {
							return "";
						}
					}
				}, 
				{width: '10%', targets: [9], title: '<s:message code="system.updated.by.column.title" text="更新人" />', data: 'updatedBy', className: 'dt-center'}, 
				{width: '10%', targets: [10], title: '<s:message code="system.updated.at.column.title" text="更新時間" />', data: 'updatedAt', className: 'dt-center',
					render : function(data) {
						return DateUtil.toDateTime(data);
					}	
				}, 
				{
					width: '6%',
					targets: [11], 
					data: 'id', 
					title: '<s:message code="system.function.column.title" text="功能" />', 
					className: 'dt-center', 
					render: function (data) {
						return '<button onclick="SystemNameDialogEdit.show('+ data +')"><i class="iconx-edit"></i><s:message code="button.edit" text="編輯" /></button>'
					}
				}, 
			]
	}
	dataTable = TableUtil.init('#dataTable', options);
}
</script>
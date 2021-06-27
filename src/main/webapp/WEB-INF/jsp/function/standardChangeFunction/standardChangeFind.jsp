<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	

<h1><s:message code="standard.change.manage.title" text='標準變更作業管理' /></h1>
<fieldset class="search">
	<legend><s:message code="global.query.condition" text='查詢條件' /></legend>

	<button class="small fieldControl searchPanel">
		<i class="iconx-collapse"></i>|||
	</button>
	
	<form id="queryForm">
		<table class="grid_query">
			<tr>
				<th>&nbsp;<s:message code="standard.change.operation.name.search" text='標準變更作業名稱' /></th>
				<td><input type="text" id="display" name="display" maxlength="500" ></td>
				<td>
					<button id="searchBtn" type="button" style="width:40px;">
						<i class="iconx-search"></i> <s:message code="button.search" text='查詢' />
					</button>
				</td>
				<td>
					<button id="addBtn" type="button" onclick="StandardChangeDialogAdd.show();" style="width:40px;">
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

<%-- include dialog from standardChangeDialog for add and edit --%>
<jsp:include page='/WEB-INF/jsp/common/dialogs/standardChangeDialog.jsp'>
	<jsp:param name="isAdding" value="true" />
</jsp:include>

<jsp:include page='/WEB-INF/jsp/common/dialogs/standardChangeDialog.jsp'>
	<jsp:param name="isEditing" value="true" />
</jsp:include>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>
<script>//# sourceURL=standardChandFind.js
var dataTable;
var ajaxSetting = {async:false};

$(function() {
	dataTableInit();

<%-- 查詢按鈕--%>
$('#searchBtn').click(function() {
	let params = form2object("queryForm");		
	SendUtil.post('/standardChangeOperation/init/getSystemOptionByCondition', params, function (data) {			
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

<%-- 檢查欄位 --%>
function validate(param) { 
	if(!param.display){
		alert("請輸入 標準變更作業名稱");
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
			order: [4,'DESC'],
			columnDefs : [
				{orderable: false, targets: []}, 
				{width: '0%', targets: [0], title: '', data: 'id', className: 'hidden id' }, 
				{width: '48%', targets: [1], title: '<s:message code="standard.change.operation.column.name" text="標準變更作業" />', data: 'display', className: 'dt-center'},
				{width: '9%', targets: [2], title: '<s:message code="standard.change.active.column.title" text="狀態" />', data: 'active', className: 'dt-center',
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
				{width: '13%', targets: [3], title: '<s:message code="standard.change.updated.by.column.title" text="更新人" />', data: 'updatedBy', className: 'dt-center'}, 
				{width: '20%', targets: [4], title: '<s:message code="standard.change.updated.at.column.title" text="更新時間" />', data: 'updatedAt', className: 'dt-center',
					render : function(data) {
						return DateUtil.toDateTime(data);
					}
				}, 
				{
					width: '10%',
					targets: [5], 
					data: 'id', 
					title: '<s:message code="standard.change.function.column.title" text="功能" />', 
					className: 'dt-center', 
					render: function (data) {
						return '<button onclick="StandardChangeDialogEdit.show('+ data +')"><i class="iconx-edit"></i><s:message code="button.edit" text="編輯" /></button>'
					}
				}, 
			]
	}
	dataTable = TableUtil.init('#dataTable', options);
}
</script>
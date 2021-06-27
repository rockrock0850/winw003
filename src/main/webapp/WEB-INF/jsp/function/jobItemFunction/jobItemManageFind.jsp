<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>

<h1><s:message code="job.item.management.title" text='工作要項管理' /></h1>
<fieldset class="search">
	<legend><s:message code="global.query.condition" text='查詢條件' /></legend>

	<button class="small fieldControl searchPanel">
		<i class="iconx-collapse"></i>|||
	</button>
	
	<form id="queryForm">
		<table class="grid_query">
			<tr>
				<th>&nbsp;<s:message code="job.item.manage.name.search" text='工作要項名稱' /></th>
				<td><input type="text" id="workingItemName" name="workingItemName" maxlength="100" ></td>
				<th>&nbsp;<s:message code="job.item.system.group.search" text='系統科組別' /></th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="divisionCreated" />
						<jsp:param name="name" value="spGroup" />
						<jsp:param name="isDisabled" value="false" />
					</jsp:include>
				</td>
				<td></td>
				<th>&nbsp;<s:message code="job.item.change.is.impact.search" text='變更衝擊分析' /></th>
				<td>
					<select id="isImpact" name="isImpact">
						<option value=""><s:message code="global.select.please.choose" text='請選擇' /></option>
						<option value="Y">Y</option>
						<option value="N">N</option>
					</select>
				</td>
				<td></td>
				<th>&nbsp;<s:message code="job.item.change.is.review.search" text='變更覆核' /></th>
				<td>
					<select id="isReview" name="isReview">
						<option value=""><s:message code="global.select.please.choose" text='請選擇' /></option>
						<option value="Y">Y</option>
						<option value="N">N</option>
					</select>
				</td>
				<td></td>
				<td>&nbsp;
					<button id="searchBtn" type="button" style="width:40px;">
						<i class="iconx-search"></i> <s:message code="button.search" text='查詢' />
					</button>
				</td>
				<td>
					<button id="addBtn" type="button" onclick="JobItemDialogAdd.show();" style="width:40px;">
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

<%-- include dialog from jobItemDialog for add and edit --%>
<jsp:include page='/WEB-INF/jsp/common/dialogs/jobItemDialog.jsp'>
	<jsp:param name="isAdding" value="true" />
</jsp:include>

<jsp:include page='/WEB-INF/jsp/common/dialogs/jobItemDialog.jsp'>
	<jsp:param name="isEditing" value="true" />
</jsp:include>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>
<script>//# sourceURL=jobItemManageFind.js
var dataTable;
var ajaxSetting = {async:false};

$(function() {
	dataTableInit();
	initSelectView();				  

<%-- 查詢按鈕--%>
$('#searchBtn').click(function() {
	let params = form2object("queryForm");		
	SendUtil.post('/jobItemManagement/init/getJobItemManagmentByCondition', params, function (data) {			
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
	SendUtil.get("/html/getSpGroupSelectors", true, function (option) {
		HtmlUtil.singleSelect('select#divisionCreated', option);
	}, ajaxSetting);
}

<%-- 檢查欄位  --%>
function validate(param) { 
	if(!param.workingItemName || !param.isImpact || !param.spGroup || !param.isReview) {
		alert("請輸入 工作要項名稱\n請輸入 變更衝擊分析\n請輸入 系統科組別\n請輸入 變更覆核");
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
			order: [7,'DESC'],
			columnDefs : [
				{orderable: false, targets: []}, 
				{width: '0%', targets: [0], title: '', data: 'id', className: 'hidden id' }, 
				{width: '36%', targets: [1], title: '<s:message code="job.item.name.column.title" text="工作要項名稱" />', data: 'workingItemName', className: 'dt-center'},
				{width: '10%', targets: [2], title: '<s:message code="job.item.system.group.column.title" text="系統科組別" />', data: 'spGroup', className: 'dt-center'}, 
				{width: '8%', targets: [3], title: '<s:message code="job.item.change.is.impact.column.title" text="變更衝擊分析" />', data: 'isImpact', className: 'dt-center', 
					render : function(data) {
						if (data == 'Y') {
							return 'Y';
						} else if (data == 'N') {
							return '<font color="red">N</font>';
						} else {
							return "";
						}
					}
				}, 
				{width: '8%', targets: [4], title: '<s:message code="job.item.change.is.review.column.title" text="變更覆核" />', data: 'isReview', className: 'dt-center', 
					render : function(data) {
						if (data == 'Y') {
							return 'Y';
						} else if (data == 'N') {
							return '<font color="red">N</font>';
						} else {
							return "";
						}
					}
				}, 
				{width: '8%', targets: [5], title: '<s:message code="job.item.active.column.title" text="狀態" />', data: 'active', className: 'dt-center',
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
				{width: '11%', targets: [6], title: '<s:message code="system.updated.by.column.title" text="更新人" />', data: 'updatedBy', className: 'dt-center'}, 
				{width: '11%', targets: [7], title: '<s:message code="system.updated.at.column.title" text="更新時間" />', data: 'updatedAt', className: 'dt-center',
					render : function(data) {
						return DateUtil.toDateTime(data);
					}	
				}, 
				{
					width: '8%',
					targets: [8], 
					data: 'id', 
					title: '<s:message code="system.function.column.title" text="功能" />', 
					className: 'dt-center', 
					render: function (data) {
						return '<button onclick="JobItemDialogEdit.show('+ data +')"><i class="iconx-edit"></i><s:message code="button.edit" text="編輯" /></button>'
					}
				}, 
			]
	}
	dataTable = TableUtil.init('#dataTable', options);
}
</script>
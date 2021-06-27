<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
</head>

<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	

<h1><s:message code="form.process.managment.find.page.title" text='表單流程管理' /></h1>

<fieldset class="search">
	<legend><s:message code="global.query.condition" text='查詢條件' /></legend>

	<button class="small fieldControl searchPanel">
		<i class="iconx-collapse"></i>|||
	</button>
	
	<form id="queryForm">
		<table class="grid_query">
			<tr>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="form.process.managment.find.page.form.type" text='表單類別' /></th><!-- 表單類別  -->
				<td>
					<select id="formType" name="formType">
						<option value=""><s:message code="global.select.please.all" text='全部' /></option>
						<c:forEach var="formTypeLs" items="${formTypeLs}">
							<option value="${formTypeLs.value}">${formTypeLs.wording}</option>
						</c:forEach>
					</select>
				</td>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="form.process.managment.find.page.division.type" text='部門科別' /></th><!-- 部門科別 -->
				<td>
					<select id="division" name="division">
						<option value=""><s:message code="global.select.please.all" text='全部' /></option>
						<c:forEach var="divisionLs" items="${divisionLs}">
							<option value="${divisionLs.value}">${divisionLs.wording}</option>
						</c:forEach>
					</select>
				</td>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="form.process.managment.find.page.process.name" text='流程名稱' /></th><!-- 流程名稱 -->
				<td><input type="text" id="processName" name="processName"></td>
				<td>
					<button id="searchBtn" type="button">
						<i class="iconx-search"></i> <s:message code="button.search" text='查詢' />
					</button>
					<button id="addBtn" type="button">
						<i class="iconx-add"></i> <s:message code="form.process.managment.find.page.add.new.process" text='新增流程' />
					</button>
				</td>
			</tr>
		</table>
	</form>
</fieldset>

<fieldset>
	<legend><s:message code="global.query.result" text='查詢結果' /></legend>
	<div class="grid_BtnBar" _style="display:none">
		<button id="saveBtn">
			<i class="iconx-save"></i><s:message code="button.save" text='儲存' />
		</button>
	</div>
	<table id="dataTable" class="display collapse cell-border">
		<thead></thead>
		<tbody></tbody>
	</table>
</fieldset>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	

<script>//# sourceURL=FormProcessManagmentFind.js
$(function() {
	dataTableInit();
	dataOnload();
	
	<%-- 查詢按鈕--%>
	$('#searchBtn').click(function() {
		let params = form2object("queryForm");
		
		SendUtil.post('/formProcessManagment/getFormProcessDetailByConditon', params, function (data) {
			TableUtil.reDraw(dataTable, data);
		},null,true);
	});
	
	<%-- 新增頁面按鈕--%>
	$("#addBtn").click(function() {
		SessionUtil.set('formProcess', form2object("queryForm"));
		SendUtil.href("/formProcessManagment/addPage");
	});
	
	<%-- 新增頁面按鈕--%>
	$("#saveBtn").click(function() {
		let verifyResult = true;
		let enableStatus = $(".isEnable");
		
		if(enableStatus.length > 0) {
			if(confirm("<s:message code='form.process.managment.find.page.alert.1' text='是否要進行保存' />")) {//是否要進行保存
				let params = {};
				let changeStatusArray = [];
				let verifyDuplicate = []; 
				let processName = "";
				
				$.each(enableStatus,function(index,element) {
					let data = {};
					let id = $(this).closest("tr").find(".id").text();//DataTable設定ID麻煩,故改為用class接
					let departmentId = $(this).closest("tr").find(".departmentId").text();
					let division = $(this).closest("tr").find(".division").text();
					let formType = $(this).closest("tr").find(".formType").text();
					let isEnable = element.value
					processName = $(this).closest("tr").find(".processName").text();
				
					data.id = id;
					data.formType = formType;
					data.departmentId = departmentId;
					data.division = division;
					data.isEnable = isEnable;
					data.processName = processName;
					
					changeStatusArray.push(data);
				});
				
				if(!verifyResult) {
					let alertStr = "<s:message code='form.process.managment.update.status.duplicate.warning' text='流程名稱 : {0} 同科別下已經有相同的表單類型正在啟用中,請先停用並且儲存' />";
					alertStr = StringUtil.format(alertStr,processName);
					alert(alertStr);
				
					return;
				}
			
				params.changeStatusList = changeStatusArray;
				
				SendUtil.post('/formProcessManagment/updateFormProcessStatusById', params, function (data) {
					if(data != undefined || data != null) {
						alert(data.result);
					}
				},null,true);
			}
			
		}
	})
});

<%-- 編輯頁面 --%>
function editPage(id) {
	let params = form2object("queryForm");
	params.id = id;
	SendUtil.post("/formProcessManagment/verifyProcessStatus",params,function(data) {
		SessionUtil.set('formProcess', params);
		SendUtil.href("/formProcessManagment/editPage", id);
	});
	
}


<%-- DataTable初始化  --%>
function dataTableInit() {

	var formList = {};//若有需要進行初始查詢的話,再修改其來源
	
	var options = {
			data : formList,
			columnDefs : [
				{orderable: false, targets: []}, 
				{targets: [0], title: '', data: 'id', className: 'hidden id' }, 
				{
					targets: [1], 
					data: 'id', 
					title: "", 
					className: 'dt-center', 
					render: function (data) {
						return '<button onclick="editPage('+ data +')"><i class="iconx-edit"></i><s:message code="button.edit" text="編輯" /></button>'
					}
				},
				{targets: [2], title: '<s:message code="form.process.managment.find.page.column.title.form.type" text="表單類別" />', data: 'formName', className: 'dt-center'}, 
				{targets: [3], title: '<s:message code="form.process.managment.find.page.column.title.dept.and.division" text="部門科別" />', data: 'divisionText', className: 'dt-center'}, 
				{targets: [4], title: '<s:message code="form.process.managment.find.page.column.title.process.name" text="流程名稱" />', data: 'processName', className: 'dt-center'}, 
				{targets: [5], title: '<s:message code="form.process.managment.find.page.column.title.update.by" text="修改人員" />', data: 'updatedBy', className: 'dt-center'}, 
				{targets: [6], title: '<s:message code="form.process.managment.find.page.column.title.update.at" text="修改時間" />', data: 'updatedAtText', className: 'dt-center'}, 
				{
					targets: [7], 
					data: 'isEnable', 
					title: '<s:message code="form.process.managment.find.page.column.title.is.enable" text="狀態" />', 
					className: 'dt-center', 
					render: function (data) {
						if("Y" == data) {
							return '<select class="isEnable">'+'<option value="Y" selected="selected"><s:message code="form.process.managment.find.page.column.title.is.enable.status.enable" text="啟用" /></option>'+'<option value="N"><s:message code="form.process.managment.find.page.column.title.is.enable.status.disenable" text="停用" /></option>'+'</select>'
						} else {
							return '<select class="isEnable">'+'<option value="Y"><s:message code="form.process.managment.find.page.column.title.is.enable.status.enable" text="啟用" /></option>'+'<option value="N" selected="selected"><s:message code="form.process.managment.find.page.column.title.is.enable.status.disenable" text="停用" /></option>'+'</select>'
						}
					}	
				},
				{targets: [8], title: '', data: 'departmentId', className: 'hidden departmentId'}, 
				{targets: [9], title: '', data: 'division', className: 'hidden division'}, 
				{targets: [10], title: '', data: 'formType', className: 'hidden formType'}, 
				{targets: [11], title: '', data: 'processName', className: 'hidden processName'}, 
			]
	}
	dataTable = TableUtil.init('#dataTable', options);
}

//若從編輯頁面點選回上一頁,則可透過queryCondition的資料重新查詢先前查詢過的內容
function dataOnload() {
	let jsonStr = SessionUtil.get('formProcess');
	if(jsonStr) {
		let jsonObject = ObjectUtil.parse(jsonStr);
		ObjectUtil.autoSetColumnValue(jsonObject);
		
		let params = form2object("queryForm");
		
		$.each(params,function(index,element) {
			SendUtil.post('/formProcessManagment/getFormProcessDetailByConditon', params, function (data) {
				TableUtil.reDraw(dataTable, data);
			},null,true);
			return;
		});
	}
}
</script>



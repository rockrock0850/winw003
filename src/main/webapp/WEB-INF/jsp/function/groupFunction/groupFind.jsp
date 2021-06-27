<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>

<script>//# sourceURL=groupFind.js
var dataTable;
var uriSearch = '/groupFunction/search';
var uriEditPage = '/groupFunction/edit';

$(function() {
	DateUtil.datePicker();
    var options = {
        data: null,
        pageLength: 20,
        columnDefs: [
        	{orderable: false, targets: []},
        	{targets: [0], title: '', data: 'sysGroupId', className: 'hidden'},
        	{targets: [1], title: '<s:message code="group.function.department.id" text='部門代碼' />', data: 'departmentId', className: 'dt-center'},
        	{targets: [2], title: '<s:message code="group.function.department.name" text='部門名稱' />', data: 'departmentName', className: 'dt-center'},
        	{targets: [3], title: '<s:message code="group.function.division" text='科別' />', data: 'division', className: 'dt-center'},
        	{targets: [4], title: '<s:message code="group.function.group.id" text='群組代碼' />', data: 'groupId', className: 'dt-center'},
        	{targets: [5], title: '<s:message code="group.function.group.name" text='群組代碼' />', data: 'groupName', className: 'dt-center'},
        	{targets: [6], title: '', data: null, className: 'dt-center',
        		defaultContent: '<button onclick="editPage(this);"><i class="iconx-edit"></i><s:message code="button.edit" text='編輯' /></button>'
        	}
        ]
    };
    dataTable = TableUtil.init('#dataTable', options);
	
});

function search () {
	SendUtil.post(uriSearch, form2object('search'), function (resData) {
		TableUtil.reDraw(dataTable, resData);
	}, null, true);
}

<%-- 編輯群組設定 --%>
function editPage(button){
	var data = TableUtil.getRow(dataTable, button);
	SendUtil.href(uriEditPage, data.sysGroupId.toString());
}

</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>
	<h1><s:message code="group.function.title" text='群組功能管理' /></h1>
	
	<fieldset class="search">
		<legend><s:message code="global.query.condition" text='查詢條件' /></legend>

		<button class="small fieldControl searchPanel">
			<i class="iconx-collapse"></i>|||
		</button>

		<form id='search'>
			<table class="grid_query">
				<tr>
					<th>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<s:message code="group.function.group.name" text='群組名稱' />
					</th>
					<td>
						<input type="text" name='groupName' style="width: 20rem;" />
					</td>
					<td>
						<button type='button' onclick='search()'>
							<i class="iconx-search"></i>
							<s:message code="button.search" text='查詢' />
						</button>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
	
	<fieldset>
		<legend><s:message code="global.query.result" text='查詢結果' /></legend>
		<table id="dataTable" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
	
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
</html>
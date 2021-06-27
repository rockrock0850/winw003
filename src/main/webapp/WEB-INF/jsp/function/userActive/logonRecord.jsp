<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>

<script>
var dataTable;
var uriSearch = '/logonRecord/search';

$(function(){
	initTable();
	initSelect();	
	initDatePicker();
});

function initDatePicker () {
	var dateOption = {
		minDate: ''
	};					
	DateUtil.datePickerById('#queryStartDate' ,dateOption);
	DateUtil.datePickerById('#queryEndDate' ,dateOption);
}

function search () {
	if($("#queryStartDate").val()!='' && $("#queryEndDate").val()!=''){
		if(new Date($("#queryStartDate").val()) > new Date($("#queryEndDate").val())){
			alert('<s:message code="logon.record.query.date.errmsg" text="開始時間不可大於結束時間!" />');
			return false;
		}
	}
	
	SendUtil.post(uriSearch, form2object('search'), function (resData) {
		TableUtil.reDraw(dataTable, resData);
	}, null, true);
}

function initSelect () {
	var options = [];
	
	options.push({
		value : 'LOGIN',
		wording : '<s:message code="logon.record.login" text="登入" />'
	});
	options.push({
		value : 'LOGOUT',
		wording : '<s:message code="logon.record.logout" text="登出" />'
	});
	
	HtmlUtil.singleSelect('select#status', options);
}

function initTable () {
	var options = {
		data: null,
		pageLength: 20,
		columnDefs: [
			{orderable: false, targets: []}, 
			{targets: [0], title: '<s:message code="logon.record.account" text='帳號' />', data: 'userId', className: 'dt-center'}, 
			{targets: [1], title: '<s:message code="logon.record.user.name" text='姓名' />', data: 'userName', className: 'dt-center'}, 
			{targets: [2], title: '<s:message code="logon.record.status" text='狀態' />', data: 'status', className: 'dt-center',
				render : function (data) {
					var message = "";
					
					switch(data){
						case 'LOGIN':
						case 'FAIL':
							message = '<s:message code="logon.record.login" text='登入' />';
							break;
						case 'LOGOUT':
							message = '<s:message code="logon.record.logout" text='登出' />';
							break;
						case 'TIMEOUT':
							message = '逾時';
							break;
						default:
							message = data;
					}
					
					return message;
			}}, 
			{targets: [3], title: '<s:message code="logon.record.time" text='時間' />', data: 'time', className: 'dt-center',
				render : function (data) {
					return DateUtil.toDateTime(data);
			}},
			{targets: [4], title: '<s:message code="logon.record.ip" text='IP' />', data: 'ip', className: 'dt-center'},
			{targets: [5], title: '<s:message code="logon.record.status.result" text='結果' />', data: 'status', className: 'dt-center',
				render : function (data) {
					var message = "";
					
					switch(data){
						case 'LOGIN':
						case 'LOGOUT':
							message = '<s:message code="logon.record.status.result.success" text='成功' />';
							break;
						case 'FAIL':
							message = '<s:message code="logon.record.status.result.fail" text='失敗' />';
							break;
						default:
							message = data;
					}
					
					return message;
			}}
		]
	};
	dataTable = TableUtil.init('#dataTable', options);
}
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>
	<h1><s:message code="logon.record.h1.title" text='登入/登出紀錄' /></h1>
	
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
						<s:message code="logon.record.user.name" text='姓名' />
					</th>
					<td>
						<input type="text" name='userName' style="width: 10rem;" />
					</td>
					<th><s:message code="logon.record.time" text='時間' /></th>
					<td>
						<jsp:include page="/WEB-INF/jsp/common/models/dateRangeModel.jsp" >
							<jsp:param name="id1" value="queryStartDate" />
							<jsp:param name="name1" value="queryStartDate" />
							<jsp:param name="id2" value="queryEndDate" />
							<jsp:param name="name2" value="queryEndDate" />
						</jsp:include>
					</td>
					<th><s:message code="logon.record.status" text='狀態' /></th>
					<td>
						<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
							<jsp:param name="id" value="status" />
							<jsp:param name="name" value="status" />
						</jsp:include>
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
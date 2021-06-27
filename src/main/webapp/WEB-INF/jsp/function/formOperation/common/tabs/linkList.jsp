<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<table id="linkedListTable" class="display collapse cell-border">
	<thead></thead>
	<tbody></tbody>
</table>

<script>//# sourceURL=link.js
$(function() {
	initDataTable();
});

function initDataTable () {
	reqData = form2object('headForm');
	SendUtil.post('/commonForm/getFormLinkList', reqData, function (resData) {
		var options = {
			checkbox: true,
			data: resData,
			columnDefs: [
				{orderable: false, targets: [0]}, 
				{targets: [0], title: '<s:message code="form.header.form.source.id" text="來源表單"/>', data: 'sourceId', className: 'dt-center',render: function(data) {
					return '<a href="javascript:dispatcherForm(\'' + data + '\')"> ' + data + '</a>';
				}}, 
				{targets: [1], title: '<s:message code="form.link.column.1" text="表單類別"/>', data: 'formClass', className: 'dt-center'}, 
				{targets: [2], title: '<s:message code="form.link.column.2" text="表單單號"/>', data: 'formId', className: 'dt-center',render: function(data) {
					return '<a href="javascript:dispatcherForm(\'' + data + '\')"> ' + data + '</a>';
				}}, 
				{targets: [3], title: '<s:message code="form.link.column.3" text="表單狀態"/>', data: 'formStatus', className: 'dt-center'},
				{targets: [4], title: '<s:message code="form.link.column.4" text="開單日期"/>', data: 'createdAt', className: 'dt-center', 
					render: function (data) {
						return DateUtil.toDateTime(data);
				}},
				{targets: [5], title: '<s:message code="form.link.column.5" text="處理科別"/>', data: 'divisionSolving', className: 'dt-center'}, 
				{targets: [6], title: '<s:message code="form.link.column.6" text="處理人員"/>', data: 'userSolving', className: 'dt-center'},
				{targets: [7], title: '<s:message code="form.link.column.7" text="實際完成時間"/>', data: 'act', className: 'dt-center', 
					render: function (data) {
						return DateUtil.toDateTime(data);
				}},
				{targets: [8], title: '<s:message code="form.countersigned.form.sct" text="連線系統完成日期"/>', data: 'sct', className: 'dt-center', 
					render: function (data) {
						return DateUtil.toDateTime(data);
				}},
				{targets: [9], title: '<s:message code="form.countersigned.form.cct" text="預計變更結束時間"/>', data: 'cct', className: 'hidden', 
					render: function (data) {
						return DateUtil.toDateTime(data);
				}}
			]
		};
		dataTable = TableUtil.init('#linkedListTable', options);
	});
}

//傳入FormId,導到指定表單編輯頁面
function dispatcherForm (formId) {
	HtmlUtil.putBreadcrumb('formSearch/search/' + $('input#formId').val());
	SendUtil.hrefOpenWindow("/formSearch/search/" + formId);
}
</script> 
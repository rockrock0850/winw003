<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<script>//# sourceURL=srVerifyLog.js
var dataTable;

$(function () {
	initView();
	initDataTable();
	authViewControl();
});

function initView () {
	if (HtmlUtil.tempData.verifyForm) {
		ObjectUtil.autoSetFormValue(HtmlUtil.tempData.verifyForm, 'verifyForm');
	}
}

function initDataTable () {
	SendUtil.post('/commonForm/getVerificationLog', form2object('headForm'), function (response) {
		var options = {
			data: response,
			columnDefs: [
				{orderable: false, targets: '_all'}, 
				{targets: [0], title: "<s:message code='form.verify.log.column.1' text='審核關卡'/>", data: 'verifyLevel', className: 'dt-center hidden'}, 
				{targets: [1], title: "<s:message code='form.verify.log.column.7' text='關卡群組'/>", data: 'groupSolving', className: 'dt-center'}, 
				{targets: [2], title: "<s:message code='form.verify.log.column.3' text='完成時間'/>", data: 'completeTime', className: 'dt-center', 
						render: function (data) {
							return DateUtil.toDateTime(data);
						}}, 
				{targets: [3], title: "<s:message code='form.verify.log.column.4' text='人員'/>", data: 'userName', className: 'dt-center'}, 
				{targets: [4], title: "<s:message code='form.verify.log.column.5' text='審核結果'/>", data: 'verifyResultWording', className: 'dt-center'},
				{targets: [5], title: "<s:message code='form.verify.verifyComment' text='簽核意見'/>", data: 'verifyComment', className: 'dt-center'},
				{targets: [6], title: '', data: 'userId', className: 'dt-center hidden'},
				{targets: [7], title: '', data: 'formId', className: 'dt-center hidden'},
				{targets: [8], title: '', data: 'verifyResult', className: 'dt-center hidden'}
			]
		};
		dataTable = TableUtil.init('#verifyLogList', options);
		TableUtil.page(dataTable, 'last');
	});
}
</script>
<fieldset>
	<legend><s:message code='form.verify.verifyLogList' text='審核歷程'/></legend>
	<table id="verifyLogList" class="display collapse cell-border">
		<thead></thead>
		<tbody></tbody>
	</table>
</fieldset>
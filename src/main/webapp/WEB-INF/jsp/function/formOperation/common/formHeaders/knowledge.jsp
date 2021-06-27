<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<script>//# sourceURL=kHead.js
var formInfo = ObjectUtil.parse('${info}');

$(function () {
	fillForm(formInfo);
});

function fetchInfo (request) {
	SendUtil.post('/knowledgeForm/info', request, function (response) {
		SendUtil.get("/html/getDivisionSelectors", true, function (option) {
			HtmlUtil.singleSelect('select#divisionCreated', option);
			$('select#divisionCreated').val(response.divisionCreated);
		},ajaxSetting);
		
		SendUtil.post("/html/getUserSelectors", request, function (option) {
			HtmlUtil.singleSelect('select#userCreated', option);
		}, ajaxSetting);
		
		SendUtil.get("/html/getDivisionSelectors", true, function (option) {
			HtmlUtil.singleSelect('select#divisionSolving', option);
			$('select#divisionSolving').val(response.divisionSolving);
		}, ajaxSetting);
		
		SendUtil.get("/html/getSysGroupSelectors", null, function (option) {
			HtmlUtil.singleSelect('select#groupSolving', option);
			$('select#groupSolving').val(response.groupSolving);
		}, ajaxSetting);

		SendUtil.post("/html/getUserSelectors", request, function (option) {
			HtmlUtil.singleSelect('select#userSolving', option);
		}, ajaxSetting);
		
		fillForm(response);
	}, ajaxSetting);
}

function fillForm (info) {
	$('input#formId').val(info.formId);
	
	if(info.sourceId) {
		$('a#sourceId').html(info.sourceId);
	}

	ObjectUtil.autoSetFormValue(info, 'headForm');
	
	if (HtmlUtil.tempData.infoForm) {
		ObjectUtil.autoSetFormValue(HtmlUtil.tempData.infoForm, 'infoForm');
	} else {
		ObjectUtil.autoSetFormValue(info, 'infoForm');
	}
	
	$('td#flowName').html(info.flowNameDisplay);
	$('td#formStatus').html(info.formStatusWording);
	$('p#processName').html(info.processName).show();
	$('td#knowledger').html($('select#userSolving').val());
	$('legend#formId').html(info.formId ? info.formId : '【NEW】');
	$('input#updatedAt').val(DateUtil.toDateTime(info.updatedAt));
	$('td#createTime').html(DateUtil.toDateTime(info.createTime));
}

// 統一根據表單狀態處理畫面要顯示或隱藏的元件
function authViewControl () {
	var info = form2object('headForm');
	ObjectUtil.dataToBackend(info);
	
	$('form#headForm').find('select').attr('disabled', true);
	SendUtil.post('/commonForm/getEditableCols', info, function (response) {
		HtmlUtil.enableFunctionButtons(response.buttons, true);
		HtmlUtil.disableElements(response.disabledColumns, true);
		HtmlUtil.disableElements(response.enabledColumns, false);
		ValidateUtil.adminCListViewCtrl();

		// 檢查當前頁簽的按鈕欄上沒有按鈕的話就把整欄隱藏
		$('div[id$="Buttons"]').toggle(
				$('div[id$="Buttons"] button').is(":visible"));
	}, ajaxSetting);
}

/**
 * 表頭「儲存」按鈕
 */
function clickFormInfoSaveButton () {
	HtmlUtil.temporary();
	
	let verifyResult = verifyInfoForm();

	if (verifyResult) {
		alert(verifyResult);
		return;
	}

	SendUtil.post('/knowledgeForm/info', form2object('headForm'), function (res) {
		let isVice = ValidateUtil.loginRole().isVice();
		let viewEct = DateUtil.fromDate(HtmlUtil.tempData.infoForm.ect);
		let isAdmin = ValidateUtil.loginRole().isAdmin();

		if (confirm("<s:message code='form.question.form.info.configm.save' text='確定儲存?'/>")) {
			saveBasicInfo(function () {
				save();
				alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
			});
		} 
	});
}

function verifyInfoForm () {
	let verifyResult = '';
	let headForm = form2object('headForm');
	let reqData = $.extend(headForm, HtmlUtil.tempData.infoForm, HtmlUtil.tempData.programForm);
	let tempArgs = allDateArgs;
	
	tempArgs.push('updatedAt');
	tempArgs.push('createTime');
	ObjectUtil.dataToBackend(reqData, tempArgs);
	
	verifyResult = 
		ValidateUtil.formFields('/knowledgeForm/validateColumnData', reqData);
	
	return verifyResult;
}

function save () {
	authViewControl();
	fetchInfo(form2object('headForm'));
}

function getFormDomain () {
	return '/knowledgeForm';
}
</script>

<h1>
	<s:message code='form.question.process.knowledge.1' text='知識庫'/>
</h1>
<fieldset>
	<legend id='formId'>【NEW】</legend>
	<form id='headForm'>
		<input id='sourceId' class='hidden' name='sourceId' />
		<input id='formId' class='hidden' name='formId' />
		<input id='formClass' class='hidden' name='formClass' />
		<input id='formType' class='hidden' name='formType' />
		<input id='verifyLevel' class='hidden' name='verifyLevel' />
		<input id='verifyType' class='hidden' name='verifyType' />
		<input id='detailId' class='hidden' name='detailId' />
		<input id='formStatus' class='hidden' name='formStatus' />
		<input id='processStatus' class='hidden' name='processStatus' />
		<input id='processProgram' class='hidden' name='processProgram' />
		<input id='reason' class='hidden' name='reason' />
		<input id='indication' class='hidden' name='indication' />
		<input id='isVerifyAcceptable' class='hidden' type='checkbox' name='isVerifyAcceptable' />
		<input id='isModifyColumnData' class='hidden' type='checkbox' name='isModifyColumnData' />
		<input id='isCreateChangeIssue' class='hidden' type='checkbox' name='isCreateChangeIssue' />
		<input id='isApplyLastLevel' class='hidden' type='checkbox' name='isApplyLastLevel' />
		<input id='isCreateCIssue' class='hidden' type='checkbox' name='isCreateCIssue' />
		<input id='isWaitForSubIssueFinish' class='hidden' type='checkbox' name='isWaitForSubIssueFinish' />
		<input id='isCloseForm' class='hidden' type='checkbox' name='isCloseForm' />
		<input id='isApprover' class='hidden' type='checkbox' name='isApprover' />
		<input id='isNextLevel' class='hidden' type='checkbox' name='isNextLevel' />
		<input id='isBackToApply' class='hidden' type='checkbox' name='isBackToApply' />
		<input id='isEctExtended' class='hidden' type='checkbox' name='isEctExtended' />
		<input id='isScopeChanged' class='hidden' type='checkbox' name='isScopeChanged' />
		<input id='isSave' class='hidden' type='checkbox' name='isSave' />
		<input id='oect' class='hidden' name='oect' />
		<input id='isDifferentScope' class='hidden' type='checkbox' name='isDifferentScope' />
		
		<table class="grid_query">
			<tr>
				<th><s:message code='form.question.process.knowledge.9' text='流程類別'/></th>
				<td id='flowName'></td>
			</tr>
			<tr>
				<th><s:message code='form.header.form.source.id' text='來源表單'/></th>
				<td>
					<font color="red"><a id="sourceId" class='href-no-color' href="javascript:toForm();"></a></font>
				</td>
				<th>
					<s:message code='form.question.create.form.division' text='開單科別'/>
				</th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="divisionCreated" />
						<jsp:param name="name" value="divisionCreated" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
				<th>
					<s:message code='form.question.create.user.name' text='開單人員'/>
				</th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="userCreated" />
						<jsp:param name="name" value="userCreated" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
				<th>
					<s:message code='form.question.create.time' text='開單時間'/>
				</th>
				<td id='createTime'></td>
			</tr>
			<tr>
				<th>
					<s:message code='form.question.process.division' text='處理科別'/>
				</th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="divisionSolving" />
						<jsp:param name="name" value="divisionSolving" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
				<th>
					<s:message code='form.question.process.group.id' text='處理群組'/>
				</th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="groupSolving" />
						<jsp:param name="name" value="groupSolving" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
				<th>
					<s:message code='form.question.process.user.name' text='處理人員'/>
				</th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="userSolving" />
						<jsp:param name="name" value="userSolving" />
						<jsp:param name="isDisabled" value="true" />
						<jsp:param name="defaultName" value='global.select.please.choose' />
					</jsp:include>
				</td>
				<th>
					<s:message code='form.question.process.knowledge' text='知識庫建立者'/>
				</th>
				<td id='knowledger'></td>
				<td colspan="2">
					<%@ include file="/WEB-INF/jsp/function/formOperation/common/featureButtons/commonButtons.jsp"%>
				</td>
			</tr>
		</table>
	</form>
</fieldset>
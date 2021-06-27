<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<h1 id='formWording'>工作單</h1>
<fieldset>
	<legend id='formId'>【NEW】</legend>
	<form id="headForm">
		<input id='formId' class='hidden' name='formId' />
		<input id='sourceId' class='hidden' name='sourceId' />
		<input id='formClass' class='hidden' name='formClass' />
		<input id='formType' class='hidden' name='formType' />
		<input id='verifyLevel' class='hidden' name='verifyLevel' />
		<input id='verifyType' class='hidden' name='verifyType' />
		<input id='detailId' class='hidden' name='detailId' />
		<input id='formStatus' class='hidden' name='formStatus' />
		<input id='createTime' class='hidden' name='createTime' />
		<input id='updatedAt' class='hidden' name='updatedAt' />
		<input id='workProject' class='hidden' name='workProject' />
		<input id='processStatus' class='hidden' name='processStatus' />
		<input id='isVerifyAcceptable' class='hidden' type='checkbox' name='isVerifyAcceptable' /> 
		<input id='isWorkLevel' class='hidden' type='checkbox' name='isWorkLevel' />
		<input id='isModifyColumnData' class='hidden' type='checkbox' name='isModifyColumnData' />
		<input id='isApplyLastLevel' class='hidden' type='checkbox' name='isApplyLastLevel' />
		<input id='isCreateJobCIssue' class='hidden' type='checkbox' name='isCreateJobCIssue' />
		<input id='isCreateCompareList' class='hidden' type='checkbox' name='isCreateCompareList' />
		<input id='isWaitForSubIssueFinish' class='hidden' type='checkbox' name='isWaitForSubIssueFinish' />
		<input id='isOwner' class='hidden' type='checkbox' name='isOwner' />
        <input id="isCloseForm" class="hidden" type="checkbox" name="isCloseForm" />
		<input id='isApprover' class='hidden' type='checkbox' name='isApprover' />
		<input id='isNextLevel' class='hidden' type='checkbox' name='isNextLevel' />
		<input id='isBackToApply' class='hidden' type='checkbox' name='isBackToApply' />
		<input id='isWithoutWatching' class='hidden' type='checkbox' name='isWithoutWatching' />
		<input id='isApTabs' class='hidden' type='checkbox' name='isApTabs' />
		<input id='isServerSideUpdated' class='hidden' type='checkbox' name='isServerSideUpdated' />
		<input id='isSave' class='hidden' type='checkbox' name='isSave' />
		<input id='isSuperVice' class='hidden' type='checkbox' name='isSuperVice' />
		
		<table class="grid_query">
			<tr>
				<th><s:message code='form.header.form.source.id' text='來源表單'/></th>
				<td>
					<font color="red"><a id="sourceId" class='href-no-color' href="javascript:toForm();"></a></font>
				</td>
				<th>表單狀態</th>
				<td id='formStatus'></td>
				<th>開單科別</th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="divisionCreated" />
						<jsp:param name="name" value="divisionCreated" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
				<th>開單人員</th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="userCreated" />
						<jsp:param name="name" value="userCreated" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
			</tr>
			<tr>
				<th>處理科別</th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="divisionSolving" />
						<jsp:param name="name" value="divisionSolving" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
				<th>處理群組</th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="groupSolving" />
						<jsp:param name="name" value="groupSolving" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
				<th>處理人員</th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="userSolving" />
						<jsp:param name="name" value="userSolving" />
						<jsp:param name="isDisabled" value="true" />
						<jsp:param name="defaultName" value='global.select.please.choose' />
					</jsp:include>
				</td>
				<th><s:message code="form.header.create.time" text="開單時間" /></th>
				<td id='createTime'></td>
				<td colspan="2">
					<%@ include file="/WEB-INF/jsp/function/formOperation/common/featureButtons/commonButtons.jsp"%>
					<%@ include file="/WEB-INF/jsp/function/formOperation/common/featureButtons/checkLogButtons.jsp"%>
				</td>
			</tr>
		</table>
	</form>
</fieldset>

<script>//# sourceURL=apJobHead.js
var formInfo = baseFormInfo;

$(function () {
	fillForm(formInfo);
});

function fetchInfo (request) {
	SendUtil.post('/apJobForm/info', request, function (response) {
		SendUtil.get("/html/getDivisionSelectors", true, function (option) {
			HtmlUtil.singleSelect('select#divisionCreated', option);
		}, ajaxSetting);
		
		SendUtil.post("/html/getUserSelectors", request, function (option) {
			HtmlUtil.singleSelect('select#userCreated', option);
		}, ajaxSetting);
		
		SendUtil.get("/html/getDivisionSelectors", true, function (option) {
			HtmlUtil.singleSelect('select#divisionSolving', option);
		}, ajaxSetting);
		
		SendUtil.get("/html/getSysGroupSelectors", null, function (option) {
			HtmlUtil.singleSelect('select#groupSolving', option);
		}, ajaxSetting);

		SendUtil.post("/html/getUserSelectors", request, function (option) {
			HtmlUtil.singleSelect('select#userSolving', option);
		}, ajaxSetting);
		
		if (response.formId) {
			let updatedAt = response.updatedAt;// It was avoided making updateAt oldest that will invoked form lock mechanism.
			HtmlUtil.tempData.infoForm = 
				$.extend(response, HtmlUtil.tempData.infoForm);
			response.updatedAt = updatedAt;
		}
		
		fillForm(response.formId ? response : formInfo);
	}, ajaxSetting);
 	
 	let formId = request.formId;
 	
 	if (formId) {
 	 	fetchCInfo('/getOaDetail', 'tabOaForm', [formId, 'OA']);
 	 	fetchCInfo('/getEaDetail', 'tabEaForm', [formId, 'EA']);
 	 	fetchCInfo('/getPtDetail', 'tabPtForm', [formId, 'PT']);
 	 	fetchCInfo('/getSpDetail', 'tabSpForm', [formId, 'SP']);
 	 	fetchCInfo('/getApDetail', 'tabAp1Form', [formId, 'AP1']);
 	 	fetchCInfo('/getApDetail', 'tabAp2Form', [formId, 'AP2']);
 	 	fetchCInfo('/getApDetail', 'tabAp3Form', [formId, 'AP3']);
 	 	fetchCInfo('/getApDetail', 'tabAp4Form', [formId, 'AP4']);
 	 	fetchCInfo('/getDcDetail', 'tabDc1Form', [formId, 'DC1']);
 	 	fetchCInfo('/getDcDetail', 'tabDc2Form', [formId, 'DC2']);
 	 	fetchCInfo('/getDcDetail', 'tabDc3Form', [formId, 'DC3']);
 	 	fetchCInfo('/getBatchDetail', 'tabBtForm', [formId, 'BATCH']);
 	 	fetchCInfo('/getPlanmgmtDetail', 'tabPm1Form', [formId, 'PLAN']);
 	 	fetchCInfo('/getPlanmgmtDetail', 'tabPm2Form', [formId, 'MGMT']);
 	 	fetchCInfo('/getDbAlterDetail', 'tabDbAlterForm', [formId, 'DB']);
 	}
}

function fetchCInfo (action, tabId, reqData) {
    SendUtil.get("/commonJobForm" + action, reqData, function (resData) {
    	if (resData.id) {
	    	HtmlUtil.tempData[tabId] = 
	    		HtmlUtil.tempData[tabId] ? HtmlUtil.tempData[tabId] : {};
	    	HtmlUtil.tempData[tabId] = $.extend(resData, HtmlUtil.tempData[tabId]);
	        ObjectUtil.autoSetFormValue(resData, tabId, ["dataType", "division"]);
    	}
	});
}

function fillCForm (tabId, tabType) {
	if (HtmlUtil.tempData[tabId]) {
		ObjectUtil.autoSetFormValue(HtmlUtil.tempData[tabId], tabId);
	}
}

function fillForm (info) {
	$('input#formId').val(info.formId);
	
	ObjectUtil.autoSetFormValue(info, 'headForm');
	
	if (!HtmlUtil.tempData.infoForm) {
		ObjectUtil.autoSetFormValue(info, 'infoForm');
	} else {
		ObjectUtil.autoSetFormValue(HtmlUtil.tempData.infoForm, 'infoForm');
	}

	$('a#sourceId').text(info.sourceId);
	$("#infoForm").find("#changeType").val(info.changeType);
	$("#infoForm").find("#changeRank").val(info.changeRank);
	$('p#processName').html(info.processName).show();
	$('legend#formId').html(info.formId ? info.formId : '【NEW】');
	$('td#formStatus').html(info.formStatusWording);
	$('td#createTime').html(DateUtil.toDateTime(info.createTime));
	tooltips(info);
}

// 統一根據表單狀態處理畫面要顯示或隱藏的元件
function authViewControl () {
	var info = form2object('headForm');
	ObjectUtil.dataToBackend(info);
	
	$('form#headForm').find('select').attr('disabled', true);
	SendUtil.post('/commonForm/getEditableCols', info, function (response) {
		$('li#tabLog').show();
		$('li#tabCheckLog').show();
		$('li#tabFileList').show();
		$('li#tabLinkList').show();
		$('li#tabCheckPerson').show();
		
		HtmlUtil.enableFunctionButtons(response.buttons, true);
		HtmlUtil.disableElements(response.disabledColumns, true);
		HtmlUtil.disableElements(response.enabledColumns, false);
		ValidateUtil.adminCListViewCtrl();

		// 檢查當前頁簽的按鈕欄上沒有按鈕的話就把整欄隱藏
		$('div[id$="Buttons"]').toggle(
				$('div[id$="Buttons"] button').is(":visible"));

		showAllJobTab();
	}, ajaxSetting);
}

function tooltips (info) {
	var formTip = '表單狀態：{0}\n流程狀態：{1}';
	var processTip = '表單類別：{0}\n設定科別：{1}';
	
	formTip = StringUtil.format(formTip, info.formStatusWording, info.processStatusWording);
	processTip = StringUtil.format(processTip, info.formWording, $('select#divisionSolving').find(':selected').text());
	processTip = !$('p#processName').html() ? '' : processTip;

	$('td#formStatus').attr('title', formTip);
	$('td#processStatus').attr('title', processTip);
}

function clickFormInfoSaveButton () {
	HtmlUtil.temporary();
	
	if (confirm("<s:message code='form.question.form.info.configm.save' text='確定儲存?'/>")) {
		savePersonList();
		saveBasicInfo(function () {
			save();
			alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
		});
	}
}

/**
 * 超級副科使用的「儲存」按鈕
 */
function clickViceSaveButton (type) {
	HtmlUtil.temporary();
	
	let verifyResult = verifyInfoForm(false, true, true);

	if (verifyResult) {
 		alert(verifyResult);
 		return;
 	}
	
	// 副科於非自身關卡，編輯欄位後，點擊儲存-->新增對話框+系統提示視窗
	let buttonEvents = {};
	buttonEvents["cancel"] = function () {};
	buttonEvents["confirm"] = function () {
		HtmlUtil.temporary();
		let reqData = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm, form2object('modifyForm'));
		
		if (!reqData.modifyComment) {
			alert("<s:message code='form.modify.reason.not.empty' text='請填寫修改原因!'/>");
			return;
		}
		
		if (confirm("<s:message code='form.question.form.info.configm.save' text='確定儲存?'/>")) {
			reqData = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm,form2object('modifyForm'));
			reqData.isNextLevel = false;
			ObjectUtil.dataToBackend(reqData, allDateArgs);
			savePersonList();
			SendUtil.post('/apJobForm/modifyColsByVice', reqData, function (response) {
				alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
				saveJobTabs();
				fetchInfo(response);
				DialogUtil.close();
			}, null, true);
		}
	};
	
	SendUtil.post(getFormDomain() + '/info', form2object('headForm'), function (formInfo) {
		formInfo.processOrder = formInfo.verifyLevel;
		ModifyingDialog.show(formInfo, buttonEvents, type);
	}, null, true);
}

function clickFormInfoSendButton () {
	HtmlUtil.temporary();

	saveBasicInfo(function () {
		save();
	});
	
	let verifyResult = verifyInfoForm(true, true, false);
	
	if (verifyResult) {
		alert(verifyResult);
		return;
	}

	if (confirm("<s:message code='form.question.form.info.confirm.send' text='確定送出?'/>")) {
		let reqData = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm);
		reqData.processOrder = 1;
		reqData.isNextLevel = true;
		reqData.verifyType = 'APPLY';
		reqData.changeVerifyType = false;
		SendUtil.post('/html/getSigningList', reqData, function (nextLevels) {
			reqData.verifyLevel = 1;
			reqData.jumpLevel = nextLevels.length > 0 ? nextLevels[0].processOrder : 0;
			ObjectUtil.dataToBackend(reqData, allDateArgs);
			SendUtil.post('/apJobForm/send', reqData, function (response) {
				alert("<s:message code='form.question.form.info.success.send' text='送出成功'/>");
				SendUtil.href('/dashboard');
			});
		}, null, true);
	}
}

function clickFormInfoDeleteButton () {
	if (confirm("<s:message code='form.question.form.info.configm.delete' text='確定刪除?'/>")) {
		SendUtil.post('/apJobForm/delete', form2object('headForm'), function () {
			alert("<s:message code='form.question.form.info.success.delete' text='刪除成功!'/>");
			SendUtil.href('/dashboard');
		});
	}
}

function clickCheckLogAgreeButton (processOrder) {
	let headForm = form2object('headForm');
	SendUtil.post('/apJobForm/info', headForm, function (formInfo) {
		let request = $.extend(headForm, formInfo);
		let isWithoutWatching = formInfo.isWithoutWatching;
		
	 	if(isWithoutWatching) {
	 		clickCheckLogCloseFormButton();
	 	} else {
			if (confirm("<s:message code='form.question.form.info.confirm.send' text='確定送出?'/>")) {
				request = $.extend(request, form2object('verifyForm'));
				request.isNextLevel = true;
				request.jumpLevel = processOrder;
				request.processOrder = request.verifyLevel;
				SendUtil.post('/apJobForm/approval', request, function (response) {
					alert("<s:message code='form.verify.success' text='審核成功!'/>");
					DialogUtil.close();
					SendUtil.href('/dashboard');
				}, null, true);
			}
	 	}
	});
}

function clickCheckLogRejectButton (processOrder) {
	HtmlUtil.temporary();
	
	let request = $.extend(form2object('headForm'), form2object('verifyForm'));
	request.processOrder = request.verifyLevel;
	
	if (!request.verifyComment) {
		alert("<s:message code='form.verify.reject.reason.not.empty' text='請填寫退回原因!'/>");
		return;
	}

	if (confirm("<s:message code='form.verify.reject.confirm' text='確定退回?'/>")) {
		request.isNextLevel = false;
		request.jumpLevel = processOrder;
		SendUtil.post('/apJobForm/reject', request, function (response) {
			alert("<s:message code='form.verify.reject.success' text='退回成功!'/>");
			DialogUtil.close();
			SendUtil.href('/dashboard');
		}, null, true);
	}
}

function clickCheckLogDeleteButton () {
	HtmlUtil.temporary();

	let reqData = $.extend(form2object('headForm'), form2object('verifyForm'));
	if (!reqData.verifyComment) {
		alert("<s:message code='form.verify.reject.reason.not.empty' text='請填寫退回原因!'/>");
		return;
	}
	
	if (confirm("<s:message code='form.verify.deprecated.confirm' text='確定作廢?'/>")) {
		SendUtil.post('/apJobForm/deprecated', reqData, function (resData) {
			alert("<s:message code='form.verify.deprecated.success' text='作廢成功'/>");
			SendUtil.href('/dashboard');
		}, null, true);
	}
}

// 直接結案按鈕事件
function clickCheckLogCloseFormButton () {
	HtmlUtil.temporary();
	
	let words = "<s:message code='form.common.is.close.form' text='是否直接結案?'/>";
	if (ValidateUtil.loginRole().isVice()) {
		words = "<s:message code='form.common.is.close.form.is.vice' text='是否代科長審核?'/>";
	} else if (ValidateUtil.loginRole().isDirect1()) {
		words = "<s:message code='form.common.is.close.form.is.direct1' text='是否代協理審核?'/>";
	}
	
	if (confirm(words)) {
		let reqData = $.extend(form2object('headForm'), form2object('verifyForm'));
		SendUtil.post('/apJobForm/closeForm', reqData, function (resData) {
			alert("<s:message code='global.update.result' text='更新資料成功!'/>");
			DialogUtil.close();
			SendUtil.href('/dashboard');
		}, null, true);
	}
}

function verifyInfoForm (isForceVerify, isSave, isSuperVice) {
	let verifyResult = '';
	let headForm = form2object('headForm');
	let isApproving = headForm.formStatus == 'APPROVING';
	let isApplyLevelOne = headForm.formStatus == 'PROPOSING' || (!headForm.formStatus);
	
	if (isApproving || isForceVerify || isApplyLevelOne) {
		let infoForm = $.extend(headForm, HtmlUtil.tempData.infoForm);
		let personForm = $.extend(headForm, HtmlUtil.tempData.personForm);
		
		infoForm.isSave = !!isSave;
		infoForm.isSuperVice = !!isSuperVice;
		ObjectUtil.dataToBackend(infoForm, allDateArgs);
		verifyResult = 
			ValidateUtil.formFields('/apJobForm/validateColumnData', infoForm);
	}
	
	verifyResult += verifyCTabs(headForm);
	
	return verifyResult;
}

function saveJobTabs () {
	saveJobCTab('/saveOaDetail', 'tabOaForm');
	saveJobCTab('/saveEaDetail', 'tabEaForm');
	saveJobCTab('/saveSpDetail', 'tabSpForm');
	saveJobCTab('/savePtDetail', 'tabPtForm');
	saveJobCTab('/saveApDetail', 'tabAp1Form');
	saveJobCTab('/saveApDetail', 'tabAp2Form');
	saveJobCTab('/saveApDetail', 'tabAp3Form');
	saveJobCTab('/saveApDetail', 'tabAp4Form');
	saveJobCTab('/saveDcDetail', 'tabDc1Form');
	saveJobCTab('/saveDcDetail', 'tabDc2Form');
	saveJobCTab('/saveDcDetail', 'tabDc3Form');
	saveJobCTab('/saveBatchDetail', 'tabBtForm');
	saveJobCTab('/savePlanmgmtDetail', 'tabPm1Form');
	saveJobCTab('/savePlanmgmtDetail', 'tabPm2Form');
	saveDbAlter();
}

function savePersonList () {
	let personList = HtmlUtil.tempData.checkPersonForm;
	if (!$.isEmptyObject(personList)) {
		let reqData = HtmlUtil.tempData.checkPersonForm;
		reqData = $.extend(form2object('headForm'), reqData);
		reqData = ObjectUtil.dataToBackend(reqData, allDateArgs);
		SendUtil.post('/commonJobForm/saveCheckPerson', reqData);
	}
}

function save () {
	saveJobTabs();
	savePersonList();
	authViewControl();
}

function showAllJobTab () {
	let reqData = {};
	let isJobTab, securityTabs;
	let isAdmin = ValidateUtil.loginRole().isAdmin();

	reqData.isApTabs = true;
	reqData.departmentId = 
		loginUserInfo.departmentId;
	
	SendUtil.post("/commonJobForm/getJobTabs", reqData, function (resData) {
		$.each($("ul#tabBar li[jobTabName]"), function () {
			isJobTab = $(this).attr("id");
			securityTabs = resData.division;
			
			// 資訊部/資安部頁簽
			if (isAdmin) {
				isJobTab = true;
			} else if (resData.isApTabs) {
				isJobTab = securityTabs.indexOf(isJobTab) == -1;// 資訊部
			} else {
				isJobTab = securityTabs.indexOf(isJobTab) != -1;// 資安部
			}
			
			if (isJobTab) {
				$(this).show();	
			}	
		});
	}, ajaxSetting);
}

function verifyCTabs (headForm) {
	let verifyResult = '';
	
	ObjectUtil.clear(HtmlUtil.tempData.tabOaForm);
	ObjectUtil.clear(HtmlUtil.tempData.tabEaForm);
	ObjectUtil.clear(HtmlUtil.tempData.tabSpForm);
	ObjectUtil.clear(HtmlUtil.tempData.tabBtForm);
	ObjectUtil.clear(HtmlUtil.tempData.tabPtForm);
	ObjectUtil.clear(HtmlUtil.tempData.tabPm1Form);
	ObjectUtil.clear(HtmlUtil.tempData.tabPm2Form);
	ObjectUtil.clear(HtmlUtil.tempData.tabDc1Form);
	ObjectUtil.clear(HtmlUtil.tempData.tabDc2Form);
	ObjectUtil.clear(HtmlUtil.tempData.tabDc3Form);
	ObjectUtil.clear(HtmlUtil.tempData.tabAp1Form);
	ObjectUtil.clear(HtmlUtil.tempData.tabAp2Form);
	ObjectUtil.clear(HtmlUtil.tempData.tabAp3Form);
	ObjectUtil.clear(HtmlUtil.tempData.tabAp4Form);
	
	let tabForm = $.extend(
			headForm, 
			HtmlUtil.tempData.tabOaForm,
			HtmlUtil.tempData.tabEaForm,
			HtmlUtil.tempData.tabSpForm,
			HtmlUtil.tempData.tabBtForm,
			HtmlUtil.tempData.tabPtForm,
			HtmlUtil.tempData.tabPm1Form,
			HtmlUtil.tempData.tabPm2Form,
			HtmlUtil.tempData.tabDc1Form,
			HtmlUtil.tempData.tabDc2Form,
			HtmlUtil.tempData.tabDc3Form,
			HtmlUtil.tempData.tabAp1Form,
			HtmlUtil.tempData.tabAp2Form,
			HtmlUtil.tempData.tabAp3Form,
			HtmlUtil.tempData.tabAp4Form);
	ObjectUtil.dataToBackend(tabForm, allDateArgs);

	verifyResult += verifyResult ? "\n" : '';
	verifyResult += 
		ValidateUtil.formFields('/commonJobForm/validateCountersigned', tabForm);
	
	return verifyResult;
}

function getFormDomain () {
	return '/apJobForm';
}
</script>
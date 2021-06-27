<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%--
    SP工作會辦單
--%>

<script>//# sourceURL=spcHead.js
var formInfo = baseFormInfo;

$(function () {
	fillForm(formInfo);
});

function fetchInfo(request) {
	SendUtil.post("/spJobCForm/info", request, function (response) {
		var isAssigning = response.formStatus == 'ASSIGNING';
		
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
		
        if (isAssigning) {
        	request.isAll = true;
            request.groupId = response.groupSolving;
        }
		
		SendUtil.post("/html/getUserSelectors", request, function (option) {
			HtmlUtil.singleSelect('select#userSolving', option);
		}, ajaxSetting);
		
        fillForm(response.formId ? response : formInfo);
	}, ajaxSetting);
 	
 	let formId = request.formId ? 
 			request.formId : request.sourceId;
 	fetchCInfo('/getSpDetail', 'tabSpForm', [formId, 'SP']);
}

function fetchCInfo (action, tabId, reqData) {
    SendUtil.get("/commonJobForm" + action, reqData, function (resData) {
    	if (resData.id) {
	    	HtmlUtil.tempData[tabId] = 
	    		HtmlUtil.tempData[tabId] ? HtmlUtil.tempData[tabId] : {};
	    	HtmlUtil.tempData[tabId] = $.extend(resData, HtmlUtil.tempData[tabId]);
    		resData.formId = $('input#formId').val();
	        ObjectUtil.autoSetFormValue(resData, 'infoForm', ["dataType", "division"]);
    	}
	});
}

function fillForm (info) {
	$('input#formId').val(info.formId);
	
	ObjectUtil.autoSetFormValue(info, "headForm");
	
	if (!HtmlUtil.tempData.infoForm) {
		ObjectUtil.autoSetFormValue(info, 'infoForm');
	} else {
		ObjectUtil.autoSetFormValue(HtmlUtil.tempData.infoForm, 'infoForm');
	}

	$('a#sourceId').text(info.sourceId);
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
	}, ajaxSetting);
}

function tooltips (info) {
	var formTip = '<s:message code="form.header.status.tip" text="表單狀態：{0}\\n流程狀態：{1}" />';
	var processTip = '<s:message code="form.header.process.tip" text="表單類別：{0}\\n設定科別：{1}" />';
	
	formTip = StringUtil.format(formTip, info.formStatusWording, info.processStatusWording);
	processTip = StringUtil.format(processTip, info.formWording, $('select#divisionSolving').find(':selected').text());
	processTip = !$('p#processName').html() ? '' : processTip;

	$('td#formStatus').attr('title', formTip);
	$('td#processStatus').attr('title', processTip);
}

function clickFormInfoSaveButton () {
	HtmlUtil.temporary();
	
	if (confirm("<s:message code='form.question.form.info.configm.save' text='確定儲存?'/>")) {
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
			SendUtil.post('/spJobCForm/modifyColsByVice', reqData, function (response) {
				alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
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
	
	let verifyResult = verifyInfoForm(false, true, false);

	if (verifyResult) {
		alert(verifyResult);
		return;
	}

	let request = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm);
	request.isAssigning = true;
	request.groupId = StringUtil.spLoginTitle(loginUserInfo);
	ObjectUtil.dataToBackend(request, allDateArgs);
	SendUtil.post('/spJobCForm/send', request, function (response) {
	    alert('<s:message code="global.send.success" text="送出成功!" />');
	    SendUtil.href('/dashboard');
	}, ajaxSetting, true);
}

function clickFormInfoDeleteButton () {
	if (confirm("<s:message code='form.question.form.info.configm.delete' text='確定刪除?'/>")) {
		SendUtil.post('/spJobCForm/delete', form2object('headForm'), function () {
			alert("<s:message code='form.question.form.info.success.delete' text='刪除成功!'/>");
			SendUtil.href('/dashboard');
		});
	}
}

function clickCheckLogAgreeButton (processOrder) {
	let headForm = form2object('headForm');
	headForm.processOrder = headForm.verifyLevel;

	SendUtil.post('/spJobCForm/info', headForm, function (formInfo) {
		let spcGroups = ObjectUtil.parse(formInfo.spcGroups);
		let verifyResult = ValidateUtil.formFields('/spJobCForm/validateColumnData', formInfo);

		if (isNeedUserSolving(formInfo)) {
			alert('請選擇處理人員。');
			return;
		} 

		if (isNeedSpcGroups(formInfo, spcGroups)) {
			alert('請至少選擇一位會辦系統科群組的處理人員。');
			return;
		}

		if (verifyResult) {
			alert(verifyResult);
			return;
		}
		
		if (confirm("<s:message code='form.question.form.info.confirm.send' text='確定送出?'/>")) {
			DialogUtil.close();
			let request = $.extend(headForm, form2object('verifyForm'));
			request.isNextLevel = true;
			request.jumpLevel = processOrder;
			SendUtil.post('/spJobCForm/approval', request, function (response) {
				alert("<s:message code='form.verify.success' text='審核成功!'/>");
				SendUtil.href('/dashboard');
			}, null, true);
		}
	});
}

function clickCheckLogRejectButton (processOrder) {
	HtmlUtil.temporary();
	
	let request = $.extend(form2object('headForm'), form2object('verifyForm'));
	
	if (!request.verifyComment) {
		alert("<s:message code='form.verify.reject.reason.not.empty' text='請填寫退回原因!'/>");
		return;
	}
	
	if (confirm("<s:message code='form.verify.reject.confirm' text='確定退回?'/>")) {
		SendUtil.post('/spJobCForm/info', request, function (formInfo) {
			request = $.extend(request, formInfo);
			request.isParallel = false;
			request.isNextLevel = false;
			request.jumpLevel = processOrder;
			request.processOrder = request.verifyLevel;
			SendUtil.post('/spJobCForm/reject', request, function (response) {
				alert("<s:message code='form.verify.reject.success' text='退回成功!'/>");
				DialogUtil.close();
				SendUtil.href('/dashboard');
			}, null, true);
		});
	}
}

function clickCheckLogDeleteButton () {
	HtmlUtil.temporary();

	let request = $.extend(form2object('headForm'), form2object('verifyForm'));
	if (!request.verifyComment) {
		alert("<s:message code='form.verify.reject.reason.not.empty' text='請填寫退回原因!'/>");
		return;
	}
	
	if (confirm("<s:message code='form.verify.deprecated.confirm' text='確定作廢?'/>")) {
		SendUtil.post('/spJobCForm/deprecated', request, function (resData) {
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
		SendUtil.post('/spJobCForm/closeForm', reqData, function (resData) {
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
	let isProposing = headForm.formStatus == 'PROPOSING';
	
	if (isApproving || isForceVerify || isProposing) {
		let infoForm = $.extend(headForm, HtmlUtil.tempData.infoForm);
		let personForm = $.extend(headForm, HtmlUtil.tempData.personForm);
		ObjectUtil.dataToBackend(infoForm, allDateArgs);
		infoForm.isSave = !!isSave;
		infoForm.isSuperVice = !!isSuperVice;
		verifyResult = 
			ValidateUtil.formFields('/spJobCForm/validateColumnData', infoForm);
		verifyResult += verifyResult ? "\n" : '';
		
		if (!isProposing) {
			verifyResult += 
				ValidateUtil.formFields('/commonJobForm/validatePerson', personForm);
		}
	}
	
	return verifyResult;
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
	SpcModel.init();
	savePersonList();
	authViewControl();
}

/**
 * 副科「串會副科長」按鈕
 */
function clickSplitProcessViceButton() {
	HtmlUtil.temporary();
	
	let verifyResult = verifyInfoForm(false, true);

	if (verifyResult) {
		alert(verifyResult);
		return;
	}
	
	let info = form2object('headForm');
	SplitProcessViceDialog.show(info);
}

function getFormDomain () {
	return '/spJobCForm';
}
</script>

<h1 id="formWording"><s:message code="work.countersigned.form" text="工作會辦單" /></h1>
<fieldset>
	<legend id="formId">【NEW】</legend>
	<form id="headForm">
		<input id="formId" class="hidden" name="formId" />
		<input id="sourceId" class="hidden" name="sourceId" />
		<input id="formClass" class="hidden" name="formClass" />
		<input id="formType" class="hidden" name="formType" />
		<input id="formStatus" class="hidden" name="formStatus" />
		<input id="detailId" class="hidden" name="detailId" />
		<input id="processStatus" class="hidden" name="processStatus" />
		<input id="verifyLevel" class="hidden" name="verifyLevel" />
		<input id="verifyType" class="hidden" name="verifyType" />
		<input id="createTime" class="hidden" name="createTime" />
		<input id="updatedAt" class="hidden" name="updatedAt" />
        <input id="isCloseForm" class="hidden" type="checkbox" name="isCloseForm" />
		<input id="isVerifyAcceptable" class="hidden" type="checkbox" name="isVerifyAcceptable" />
		<input id="isModifyColumnData" class="hidden" type="checkbox" name="isModifyColumnData" />
		<input id='isApplyLastLevel' class='hidden' type='checkbox' name='isApplyLastLevel' />
		<input id='isCreateSPJobIssue' class='hidden' type='checkbox' name='isCreateSPJobIssue' />
		<input id="isCreateChangeIssue" class="hidden" type="checkbox" name="isCreateChangeIssue" />
		<input id='isApprover' class='hidden' type='checkbox' name='isApprover' />
		<input id='isNextLevel' class='hidden' type='checkbox' name='isNextLevel' />
		<input id='isBackToApply' class='hidden' type='checkbox' name='isBackToApply' />
		<input id='isAssigning' class='hidden' type='checkbox' name='isAssigning' />
		<input id='isParallel' class='hidden' type='checkbox' name='isParallel' />
		<input id='parallel' class='hidden' name='parallel' />
		<input id='isServerSideUpdated' class='hidden' type='checkbox' name='isServerSideUpdated' />
		<input id='isSave' class='hidden' type='checkbox' name='isSave' />
		<input id='isSuperVice' class='hidden' type='checkbox' name='isSuperVice' />
		
		<table class="grid_query">
			<tr>
				<th><s:message code="form.header.form.source.id" text="來源表單" /></th>
				<td>
					<font color="red"><a id="sourceId" class="href-no-color" href="javascript:toForm();"></a></font>
				</td>
				<th><s:message code="form.header.form.status" text="表單狀態" /></th>
				<td id="formStatus"></td>
				<th><s:message code="form.header.create.form.division" text="開單科別" /></th>
				<td>
					<jsp:include page="/WEB-INF/jsp/common/models/selectorModel.jsp">
						<jsp:param name="id" value="divisionCreated" />
						<jsp:param name="name" value="divisionCreated" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
				<th><s:message code="form.header.create.user.name" text="開單人員" /></th>
				<td>
					<jsp:include page="/WEB-INF/jsp/common/models/selectorModel.jsp">
						<jsp:param name="id" value="userCreated" />
						<jsp:param name="name" value="userCreated" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
			</tr>
			<tr>
				<th><s:message code="form.header.process.division" text="處理科別" /></th>
				<td>
					<jsp:include page="/WEB-INF/jsp/common/models/selectorModel.jsp">
						<jsp:param name="id" value="divisionSolving" />
						<jsp:param name="name" value="divisionSolving" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
				<th><s:message code="form.header.process.group.id" text="處理群組" /></th>
				<td>
					<jsp:include page="/WEB-INF/jsp/common/models/selectorModel.jsp">
						<jsp:param name="id" value="groupSolving" />
						<jsp:param name="name" value="groupSolving" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
				<th><s:message code="form.header.process.user.name" text="處理人員" /></th>
				<td>
					<jsp:include page="/WEB-INF/jsp/common/models/selectorModel.jsp">
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
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<script>//# sourceURL=incHead.js
var formInfo = ObjectUtil.parse('${info}');
var showBackToPic;

$(function () {
	fillForm(formInfo);
});

function fetchInfo (request) {
	if (request.isServerSideUpdated) {// 資料從伺服器運算並自動更新
		HtmlUtil.tempData.infoForm.ect = request.ect;
	}
	
	SendUtil.post('/eventForm/info', request, function (response) {
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

		fillForm(response);
	}, ajaxSetting);
}

function fillForm (info) {	
	$('input#formId').val(info.formId);

	ObjectUtil.autoSetFormValue(info, 'headForm');
	
	if (!HtmlUtil.tempData.infoForm) {
		ObjectUtil.autoSetFormValue(info, 'infoForm');
	} else {
		ObjectUtil.autoSetFormValue(HtmlUtil.tempData.infoForm, 'infoForm');
	}
	
	$('p#processName').html(info.processName).show();
	$('input#ect').val(DateUtil.toDateTime(info.ect));
	$('legend#formId').html(info.formId ? info.formId : '【NEW】');
	$('td#formStatus').html(info.formStatusWording);
	$('td#createTime').html(DateUtil.toDateTime(info.createTime));
	showBackToPic = info.showBackToPic;//這個值取決於是否會顯示退回經辦的條件
	tooltips(info);
}

// 統一根據表單狀態處理畫面要顯示或隱藏的元件
function authViewControl () {
	var info = form2object('headForm');
	ObjectUtil.dataToBackend(info);
	ValidateUtil.authView(info);
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

	SendUtil.post('/eventForm/info', form2object('headForm'), function (res) {
		let isVice = ValidateUtil.loginRole().isVice();
		let isAdmin = ValidateUtil.loginRole().isAdmin();
		$('input#isEctExtended[type="checkbox"]').prop('checked', (isVice || isAdmin));

		if (confirm("<s:message code='form.question.form.info.configm.save' text='確定儲存?'/>")) {
			saveBasicInfo(function () {
				save();
				alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
			});
		} 
	});
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
		
		DialogUtil.close();
		
		SendUtil.post(getFormDomain() + '/info', form2object('headForm'), function (res) {
			let oEct = DateUtil.fromDate(res.oect);
			let viewEct = DateUtil.fromDate(HtmlUtil.tempData.infoForm.ect);
			let isApplyLastLevel = form2object('headForm').isApplyLastLevel;

			if (isApplyLastLevel && (viewEct > oEct)) {
				ScopeChangedDialog.show(function () {
					if (confirm("<s:message code='form.question.form.info.configm.save' text='確定儲存?'/>")) {
						reqData = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm,form2object('modifyForm'));
						reqData.isNextLevel = false;
						
						if (reqData.isScopeChanged) {
							reqData.isDifferentScope = "Y";
						}
						
						ObjectUtil.dataToBackend(reqData, allDateArgs);
						SendUtil.post('/eventForm/modifyColsByVice', reqData, function (response) {
							alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
							fetchInfo(response);
							DialogUtil.close();
						}, null, true);
					}
				});
			} else if (confirm("<s:message code='form.question.form.info.configm.save' text='確定儲存?'/>")) {
				reqData = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm,form2object('modifyForm'));
				reqData.isNextLevel = false;
				reqData.isEctExtended = true;
				ObjectUtil.dataToBackend(reqData, allDateArgs);
				SendUtil.post('/eventForm/modifyColsByVice', reqData, function (response) {
					alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
					fetchInfo(response);
					DialogUtil.close();
				}, null, true);
			}
		});
	};
	
	SendUtil.post(getFormDomain() + '/info', form2object('headForm'), function (formInfo) {
		formInfo.processOrder = formInfo.verifyLevel;
		ModifyingDialog.show(formInfo, buttonEvents, type);
	}, null, true);
}

function clickFormInfoSendButton() {
	HtmlUtil.temporary();
	
	let reqData = HtmlUtil.tempData.infoForm;
	reqData = $.extend(form2object('headForm'), reqData);
	allDateArgs = ObjectUtil.arrayRemove(allDateArgs, 'mect');
	reqData = ObjectUtil.dataToBackend(reqData, allDateArgs);
	
	saveBasicInfo(function () {
		save();
	});
	
	let verifyResult = verifyInfoForm(true, true, false);

	if (verifyResult) {
		alert(verifyResult);
		return;
	}

	var confirmButton = function (selected) {
		if (!selected.division) {
			alert("<s:message code='form.common.choose.division' text='請選擇科別!'/>");
			return;
		}

		if (confirm(StringUtil.format('<s:message code="form.common.send.confirm" text="確定送出「{0}」?" />', selected.wording))) {
	 		let request = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm,form2object('verifyForm'));
	 		ObjectUtil.dataToBackend(request, allDateArgs);

	 		if (request.divisionCreated != selected.division) {
	 			assigning(request, selected);
	 			return;
	 		}

	 		if (confirm("<s:message code='form.common.handle.by.self' text='按【確定】：自行處理；按【取消】： 送副科長指派處理人員。'/>")) {
	 			selfSolve(request);
	 		} else {
	 			assigning(request, selected);
	 		}
		}
	}
	
	SolvingDialog.show(confirmButton, function () {});
}

// 指派人員
function assigning (request, selected) {
	HtmlUtil.tempData.infoForm.divisionSolving = selected.division;

	saveBasicInfo(function () {
		save();
	});
	
	request = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm);
	request.isAssigning = true;
	ObjectUtil.dataToBackend(request, allDateArgs);
	
	SendUtil.post('/eventForm/send', request, function (response) {
		alert("<s:message code='form.common.send.success' text='送出成功!'/>");
		SendUtil.href('/dashboard');
	}, ajaxSetting, true);
}

// 自行處理
function selfSolve (request) {
	HtmlUtil.tempData.infoForm.isSelfSolve = true;

	saveBasicInfo(function () {
		save();
	});

	request = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm);
	request.jumpLevel = 2;
	request.verifyLevel = 1;
	request.isNextLevel = true;
	request.verifyType = 'APPLY';
	ObjectUtil.dataToBackend(request, allDateArgs);

	SendUtil.post('/eventForm/send', request, function (response) {
		fetchInfo(response);
		authViewControl();
		DialogUtil.close();
		alert("<s:message code='form.common.send.success' text='送出成功!'/>");
	}, ajaxSetting, true);
}

function clickFormInfoDeleteButton () {
	if (confirm("<s:message code='form.question.form.info.configm.delete' text='確定刪除?'/>")) {
		SendUtil.post('/eventForm/delete', form2object('headForm'), function () {
			alert("<s:message code='form.question.form.info.success.delete' text='刪除成功!'/>");
			SendUtil.href('/dashboard');
		});
	}
}

function clickCheckLogAgreeButton (processOrder) {
	HtmlUtil.temporary();
	
	let headForm = form2object('headForm');
	let isAssigning = headForm.formStatus == 'ASSIGNING';
	let isSelfSolve = headForm.formStatus == 'SELFSOLVE';
	
	headForm.isNextLevel = true;
	headForm.processOrder = isAssigning ? 1 : headForm.verifyLevel;
	SendUtil.post('/eventForm/info', headForm, function (formInfo) {
		let request = $.extend(headForm, form2object('verifyForm'));
		request.infoForm = formInfo;
		
		if (!formInfo.userSolving) {
			alert('請選擇處理人員。');
			return;
		}
		
		if (isAssigning || isSelfSolve) {
			setUpFormProcess(request, isAssigning, isSelfSolve);
			return;
		}

		// 副科送結案(REVIEW)，加入檢核「實際完成時間」小於「預計完成時間」
		if (ValidateUtil.loginRole().isVice() && !validate(request.infoForm)) {
			return;
		}
		
		if (confirm("<s:message code='global.send.confirm' text='確定送出?'/>")) {
			request.jumpLevel = processOrder;
			approval(request);
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
		SendUtil.post('/eventForm/info', request, function (formInfo) {
			request = $.extend(request, formInfo);
			request.processOrder = request.verifyLevel;
			request.isNextLevel = false;
			request.jumpLevel = processOrder;
			SendUtil.post('/eventForm/reject', request, function (response) {
				alert("<s:message code='form.verify.reject.success' text='退回成功!'/>");
				DialogUtil.close();
				SendUtil.href('/dashboard');
			});
		}, null, true);
	}
}

function clickCheckLogDeleteButton () {
	HtmlUtil.temporary();
	let reqData = $.extend(form2object('headForm'), form2object('verifyForm'));
	
	if (confirm("<s:message code='form.verify.deprecated.confirm' text='確定作廢?'/>")) {
		if (!reqData.verifyComment) {
			alert("<s:message code='form.verify.deprecated.reason.not.empty' text='請填寫作廢原因!'/>");
			return;
		}
		SendUtil.post('/eventForm/deprecated', reqData, function (resData) {
			alert("<s:message code='form.verify.deprecated.success' text='作廢成功'/>");
			SendUtil.href('/dashboard');
		}, null, true);
	}
}

function setUpFormProcess (request, isAssigning, isSelfSolve) {
	if (isAssigning) {
		request.jumpLevel = 2;
		request.verifyLevel = 1;
	} else if (isSelfSolve) {
		request.jumpLevel = 1;
		request.verifyLevel = 1;
	}
	
	if (isAssigning || isSelfSolve) {
		request.isNextLevel = true;
		request.isAssigning = isAssigning;
		request.isSelfSolve = isSelfSolve;
		approval(request);
	}
}

function approval (request) {
	SendUtil.post('/eventForm/approval', request, function (response) {
		SendUtil.get('/eventForm/program', request.formId, function (response2) {
			if((response2.isSuggestCase == 'Y' || request.infoForm.isOnlineFail == 'Y' || request.infoForm.isSameInc == 'Y') 
					&& request.formClass == 'INC'){
				var groupId = $("input[name='detailId']:checked").closest("tr").find(".groupIdClass").text().toUpperCase();
				if (groupId.indexOf('DIRECT1') !== -1 || groupId.indexOf('DIRECT2') !== -1) {
					SendUtil.post('/questionForm/openAndSaveVice',formInfo, function (response2) {
					}, null, true);
				}
				SendUtil.post('/questionForm/openAndSaveChief',formInfo, function (response2) {
				}, null, true);
			}
		});
		
		alert("<s:message code='form.verify.success' text='審核成功!'/>");
		DialogUtil.close();
		SendUtil.href('/dashboard');
	}, null, true);
}

function verifyInfoForm (isForceVerify, isSave, isSuperVice) {
	ObjectUtil.clear(HtmlUtil.tempData.programForm);
	let verifyResult = '';
	let headForm = form2object('headForm');
	let isApproving = headForm.formStatus == 'APPROVING';
	let isAssigning = headForm.formStatus == 'ASSIGNING'; //待分派
	let reqData = $.extend(headForm, HtmlUtil.tempData.infoForm, HtmlUtil.tempData.programForm);
	
	if (isApproving || isForceVerify || isAssigning) {
		ObjectUtil.dataToBackend(reqData, allDateArgs);
		reqData.isSave = !!isSave;
		reqData.isSuperVice = !!isSuperVice;
		verifyResult = 
			ValidateUtil.formFields('/eventForm/validateColumnData', reqData);
	}
	
	return verifyResult;
}

function save () {
	saveProgramForm();
	authViewControl();
}

function getFormDomain () {
	return '/eventForm';
}

//直接結案按鈕事件
function clickCheckLogCloseFormButton () {
	HtmlUtil.temporary();
	let infoForm = HtmlUtil.tempData.infoForm;
	
	let words = "<s:message code='form.common.is.close.form' text='是否直接結案?'/>";
	if (ValidateUtil.loginRole().isVice()) {
		words = "<s:message code='form.common.is.close.form.is.vice' text='是否代科長審核?'/>";
	} else if (ValidateUtil.loginRole().isDirect1()) {
		words = "<s:message code='form.common.is.close.form.is.direct1' text='是否代協理審核?'/>";
	}
	
	if (confirm(words)) {
		let reqData = $.extend(form2object('headForm'), form2object('verifyForm'));
		SendUtil.post('/eventForm/closeForm', reqData, function (resData) {
			SendUtil.get('/eventForm/program', reqData.formId, function (response) {
				if((response.isSuggestCase == 'Y' || infoForm.isOnlineFail == true || infoForm.isSameInc == true) 
						&& reqData.formClass == 'INC'){
					SendUtil.post('/questionForm/openAndSaveVice',formInfo, function (response2) {
					}, null, true);
				}
			});
			
			alert("<s:message code='global.update.result' text='更新資料成功!'/>");
			DialogUtil.close();
			SendUtil.href('/dashboard');
		}, null, true);
	}
}

// 跳至審核指定關卡
function toReviewTargetLevel (request, jumpToReviewLevel) {
	request.jumpLevel = jumpToReviewLevel;
	SendUtil.post("/eventForm/jumpToReviewTargetLevel", request, function (response) {
		alert("<s:message code='global.send.success' text='送出成功'/>");
		DialogUtil.close();
		SendUtil.href('/dashboard');
	}, null, true);
}
</script>

<h1>事件單</h1>
<fieldset>
	<legend id='formId'>【NEW】</legend>
	<form id="headForm">
		<input id='formId' class='hidden' name='formId' />
		<input id='formClass' class='hidden' name='formClass' />
		<input id='formType' class='hidden' name='formType' />
		<input id='verifyLevel' class='hidden', name='verifyLevel' />
		<input id='verifyType' class='hidden', name='verifyType' />
		<input id='detailId' class='hidden' name='detailId' />
		<input id='formStatus' class='hidden' name='formStatus' />
		<input id='createTime' class='hidden' name='createTime' />
		<input id='updatedAt' class='hidden' name='updatedAt' />
		<input id='processStatus' class='hidden' name='processStatus' />
		<input id='processProgram' class='hidden' name='processProgram' />
		<input id='reason' class='hidden' name='reason' />
		<input id='indication' class='hidden' name='indication' />
		<input id='isVerifyAcceptable' class='hidden' type='checkbox' name='isVerifyAcceptable' />
		<input id='isModifyColumnData' class='hidden' type='checkbox' name='isModifyColumnData' />
		<input id='isApplyLastLevel' class='hidden' type='checkbox' name='isApplyLastLevel' />
		<input id='isCreateChangeIssue' class='hidden' type='checkbox' name='isCreateChangeIssue' />
		<input id='isCreateCIssue' class='hidden' type='checkbox' name='isCreateCIssue' />
		<input id='isCloseForm' class='hidden' type='checkbox' name='isCloseForm' />
		<input id='isAddQuestionIssue' class='hidden' type='checkbox' name='isAddQuestionIssue' />
		<input id='isApprover' class='hidden' type='checkbox' name='isApprover' />
		<input id='isNextLevel' class='hidden' type='checkbox' name='isNextLevel' />
		<input id='isAssigning' class='hidden' type='checkbox' name='isAssigning' />
		<input id='isSelfSolve' class='hidden' type='checkbox' name='isSelfSolve' />
		<input id='isBackToApply' class='hidden' type='checkbox' name='isBackToApply' />
		<input id='isShowBackToPic' class='hidden' type='checkbox' name='isShowBackToPic' />
		<input id='isEctExtended' class='hidden' type='checkbox' name='isEctExtended' />
		<input id='isScopeChanged' class='hidden' type='checkbox' name='isScopeChanged' />
		<input id='isSave' class='hidden' type='checkbox' name='isSave' />
		<input id='isServerSideUpdated' class='hidden' type='checkbox' name='isServerSideUpdated' />
		<input id='oect' class='hidden' name='oect' />
		<input id='isSuperVice' class='hidden' type='checkbox' name='isSuperVice' />
		<input id='isDifferentScope' class='hidden' type='checkbox' name='isDifferentScope' />
		
		<table class="grid_query">
			<tr>
				<th>表單狀態</th>
				<td id='formStatus' title=""></td>
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
				<th>開單時間</th>
				<td id='createTime'></td>
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
				<td></td>
				<td></td>
				<td colspan="2">
					<%@ include file="/WEB-INF/jsp/function/formOperation/common/featureButtons/commonButtons.jsp"%>
					<%@ include file="/WEB-INF/jsp/function/formOperation/common/featureButtons/checkLogButtons.jsp"%>
				</td>
			</tr>
		</table>
	</form>
</fieldset>
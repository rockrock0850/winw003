<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<script>//# sourceURL=cHead.js
var formInfo = baseFormInfo;

$(function () {
	fillForm(formInfo);
});

function fetchInfo (request) {
	SendUtil.post('/countersignedForm/info', request, function (response) {
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
	$('h1#formWording').html(info.formWording);
	$('td#createTime').html(DateUtil.toDateTime(info.createTime));
	$('p#processName').html(info.processName).show();
	$('legend#formId').html(info.formId ? info.formId : '【NEW】');
	$('td#formStatus').html(info.formStatusWording);
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
		
		HtmlUtil.enableFunctionButtons(response.buttons, true);
		HtmlUtil.disableElements(response.disabledColumns, true);
		HtmlUtil.disableElements(response.enabledColumns, false);
		ValidateUtil.adminCListViewCtrl();

		// 檢查當前頁簽的按鈕欄上沒有按鈕的話就把整欄隱藏
		$('div[id$="Buttons"]').toggle(
				$('div[id$="Buttons"] button').is(":visible"));

		if(info.formClass == "INC_C"){
		    $('#hostHandle').closest("tr").hide(); 
		}
		
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
	
	SendUtil.post('/countersignedForm/info', form2object('headForm'), function (res) {
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
	
	let verifyResult = verifyInfoForm(true, true, true);

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
			let viewEct = DateUtil.fromDate(HtmlUtil.tempData.infoForm.mect);
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
						SendUtil.post('/countersignedForm/modifyColsByVice', reqData, function (response) {
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
				SendUtil.post('/countersignedForm/modifyColsByVice', reqData, function (response) {
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

	if (confirm('確定送出?')) {
		let request = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm);
		request.isAssigning = true;
		ObjectUtil.dataToBackend(request, allDateArgs);
		SendUtil.post('/countersignedForm/send', request, function (response) {
			alert("<s:message code='form.question.form.info.success.send' text='送出成功'/>");
			SendUtil.href('/dashboard');
		}, null, true);
	}
}

function clickFormInfoDeleteButton () {
	if (confirm("<s:message code='form.question.form.info.configm.delete' text='確定刪除?'/>")) {
		SendUtil.post('/countersignedForm/delete', form2object('headForm'), function () {
			alert("<s:message code='form.question.form.info.success.delete' text='刪除成功!'/>");
			SendUtil.href('/dashboard');
		});
	}
}

function clickCheckLogAgreeButton (processOrder) {
	HtmlUtil.temporary();
	
	let headForm = form2object('headForm');
	let isAssigning = headForm.formStatus == 'ASSIGNING';
	headForm.processOrder = isAssigning ? 1 : headForm.verifyLevel;
	
	SendUtil.post('/countersignedForm/info', headForm, function (formInfo) {
		let spcGroups = ObjectUtil.parse(formInfo.spcGroups);
		let request = $.extend(headForm, form2object('verifyForm'));
		request.infoForm = formInfo;
		
		if (isNeedUserSolving(formInfo)) {
			alert('請選擇處理人員。');
			return;
		} 

		if (isNeedSpcGroups(formInfo, spcGroups)) {
			alert('請至少選擇一位會辦系統科群組的處理人員。');
			return;
		}

		// 副科送結案(REVIEW)，加入檢核「實際完成時間」小於「預計完成時間」
		if (ValidateUtil.loginRole().isVice() && !validate(request.infoForm)) {
			return;
		}
		
		if (confirm("<s:message code='global.send.confirm' text='確定送出?'/>")) {
			if (isAssigning) {
				request.jumpLevel = 2;
				request.verifyLevel = 1;
				request.isNextLevel = true;
				request.isAssigning = isAssigning;
			} else {
				request.isNextLevel = true;
				request.jumpLevel = processOrder;
			}

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
		SendUtil.post('/countersignedForm/info', request, function (formInfo) {
			request = $.extend(request, formInfo);
			request.isParallel = false;
			request.isNextLevel = false;
			request.jumpLevel = processOrder;
			request.processOrder = request.verifyLevel;
			SendUtil.post('/countersignedForm/reject', request, function (response) {
				alert("<s:message code='form.verify.reject.success' text='退回成功!'/>");
				DialogUtil.close();
				SendUtil.href('/dashboard');
			}, null, true);
		});
	}
}

function clickCheckLogDeleteButton () {
	HtmlUtil.temporary();

	let reqData = $.extend(form2object('headForm'), form2object('verifyForm'));
	if (!reqData.verifyComment) {
		alert("<s:message code='form.verify.deprecated.reason.not.empty' text='請填寫作廢原因!'/>");
		return;
	}
	
	if (confirm("<s:message code='form.verify.deprecated.confirm' text='確定作廢?'/>")) {
		SendUtil.post('/countersignedForm/deprecated', reqData, function (resData) {
			alert("<s:message code='form.verify.deprecated.success' text='作廢成功'/>");
			SendUtil.href('/dashboard');
		}, null, true);
	}
}

function approval (request) {
	DialogUtil.close();
	SendUtil.post('/countersignedForm/approval', request, function (response) {
		
		SendUtil.get('/eventForm/program', request.formId, function (response2) {
			if(request.infoForm.isSuggestCase == 'Y' && request.formClass == 'INC_C'){
				SendUtil.post('/questionForm/openAndSaveChief',formInfo, function (response2) {
				}, null, true);
			}
		});
		
		alert('<s:message code="global.send.success" text="送出成功!" />');
		SendUtil.href('/dashboard');
	}, null, true);
}

function verifyInfoForm (isForceVerify, isSave, isSuperVice) {
	let verifyResult = '';
	let headForm = form2object('headForm');
	let isApproving = headForm.formStatus == 'APPROVING';
	let isAssigning = headForm.formStatus == 'ASSIGNING'; //待分派
	let reqData = $.extend(headForm, HtmlUtil.tempData.infoForm);
	
	if (isApproving || isForceVerify || isAssigning) {
		ObjectUtil.dataToBackend(reqData, allDateArgs);
		reqData.isSave = !!isSave;
		reqData.isSuperVice = !!isSuperVice;
		verifyResult = 
			ValidateUtil.formFields('/countersignedForm/validateColumnData', reqData);
	}
	
	return verifyResult;
}

function save () {
	SpcModel.init();
	authViewControl();
}

function getFormDomain () {
	return '/countersignedForm';
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
		let reqData = $.extend(form2object('headForm'), form2object('verifyForm'));//TODO
		SendUtil.post('/countersignedForm/closeForm', reqData, function (resData) {
			
			SendUtil.get('/eventForm/program', reqData.formId, function (response) {
				if(infoForm.isSuggestCase == true && reqData.formClass == 'INC_C'){
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

function validate(param) { 
	let isReview = headForm.verifyType.value == 'REVIEW';

	if (isReview) {
		// 「實際完成時間」小於「主單預計完成時間」 => pass
		if (!(param.act < param.mect)) {
			alert("<s:message code='from.common.act.must.less.than.mect' text='「實際完成時間」必須小於「主單預計完成時間」'/>");
			DialogUtil.close();
			return false; 
		}
	}

	return true;
}
</script>

<h1 id='formWording'>會辦單</h1>
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
		<input id='processStatus' class='hidden' name='processStatus' />
		<input id='parallel' class='hidden' name='parallel' />
		<input id='isVerifyAcceptable' class='hidden' type='checkbox' name='isVerifyAcceptable' />
		<input id='isModifyColumnData' class='hidden' type='checkbox' name='isModifyColumnData' />
		<input id='isApplyLastLevel' class='hidden' type='checkbox' name='isApplyLastLevel' />
		<input id='isCreateChangeIssue' class='hidden' type='checkbox' name='isCreateChangeIssue' />
		<input id='isApprover' class='hidden' type='checkbox' name='isApprover' />
		<input id='isAssigning' class='hidden' type='checkbox' name='isAssigning' />
		<input id='isNextLevel' class='hidden' type='checkbox' name='isNextLevel' />
		<input id='isBackToApply' class='hidden' type='checkbox' name='isBackToApply' />
		<input id='isParallel' class='hidden' type='checkbox' name='isParallel' />
		<input id='isCloseForm' class='hidden' type='checkbox' name='isCloseForm' />
		<input id='isEctExtended' class='hidden' type='checkbox' name='isEctExtended' />
		<input id='isScopeChanged' class='hidden' type='checkbox' name='isScopeChanged' />
		<input id='isSave' class='hidden' type='checkbox' name='isSave' />
		<input id='isServerSideUpdated' class='hidden' type='checkbox' name='isServerSideUpdated' />
		<input id='oect' class='hidden' name='oect' />
		<input id='isSuperVice' class='hidden' type='checkbox' name='isSuperVice' />
		<input id='isDifferentScope' class='hidden' type='checkbox' name='isDifferentScope' />
		
		<table class="grid_query">
			<tr>
				<th>來源表單</th>
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
				<th>開單時間</th>
				<td id='createTime'></td>
				<td colspan="2">
					<%@ include file="/WEB-INF/jsp/function/formOperation/common/featureButtons/commonButtons.jsp"%>
					<%@ include file="/WEB-INF/jsp/function/formOperation/common/featureButtons/checkLogButtons.jsp"%>
				</td>
			</tr>
		</table>
	</form>
</fieldset>
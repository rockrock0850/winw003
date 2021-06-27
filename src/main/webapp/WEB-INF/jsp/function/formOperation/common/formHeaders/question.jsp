<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<h1>
	<s:message code='question.form' text='問題單'/>
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
		<input id='createTime' class='hidden' name='createTime' />
		<input id='updatedAt' class='hidden' name='updatedAt' />
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
		<input id='isServerSideUpdated' class='hidden' type='checkbox' name='isServerSideUpdated' />
		<input id='oect' class='hidden' name='oect' />
		<input id='isSuperVice' class='hidden' type='checkbox' name='isSuperVice' />
		<input id='isDifferentScope' class='hidden' type='checkbox' name='isDifferentScope' />
		
		<table class="grid_query">
			<tr>
				<th class='sourceId' style="display: none;"><s:message code='form.header.form.source.id' text='來源表單'/></th>
				<td class='sourceId' style="display: none;">
					<font color="red"><a id="sourceId" class='href-no-color' href="javascript:toForm();"></a></font>
				</td>
				<th>
					<s:message code='form.question.form.status' text='表單狀態'/>
				</th>
				<td id='formStatus'></td>
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

<script>//# sourceURL=qHead.js
var formInfo = ObjectUtil.parse('${info}');

$(function () {
	fillForm(formInfo);
});

function fetchInfo (request) {
	SendUtil.post('/questionForm/info', request, function (response) {
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
	
	if (!HtmlUtil.tempData.infoForm) {
		ObjectUtil.autoSetFormValue(info, 'infoForm');
	} else {
		if (ValidateUtil.formInfo().isCreated(info)) {
			HtmlUtil.tempData.infoForm.reportTime = info.reportTime;
		}
		ObjectUtil.autoSetFormValue(HtmlUtil.tempData.infoForm, 'infoForm');
	}
	
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
	ValidateUtil.authView(info);
	$(".sourceId").toggle(info.sourceId ? true : false);
}

function tooltips (info) {
	var formTip = "<s:message code='form.question.form.tip.1.1' text='表單狀態：{0}'/>" + "\r\n" + "<s:message code='form.question.form.tip.1.2' text='流程狀態：{1}'/>"
	var processTip = "<s:message code='form.question.form.tip.2.1' text='表單類別：{0}'/>" + "\r\n" + "<s:message code='form.question.form.tip.2.2' text='設定科別：{1}'/>";
	
	formTip = StringUtil.format(formTip, info.formStatusWording, info.processStatusWording);
	processTip = StringUtil.format(processTip, info.formWording, $('select#divisionSolving').find(':selected').text());
	processTip = !$('p#processName').html() ? '' : processTip;
	
	$('td#formStatus').attr('title', formTip);
	$('td#processStatus').attr('title', processTip);
}

/**
 * 表頭「儲存」按鈕
 *
 * 問題單觀察期:
 * 副科已填寫問題單觀察期日期，點擊【儲存】:
 * 若觀察期未達到7、30日之規則，需阻擋且彈出警告視窗(警告文字內容依照舊邏輯)；
 * 否則可儲存成功且表單狀態進入【觀察中】。
 */
function clickFormInfoSaveButton () {
	HtmlUtil.temporary();
	
	let verifyResult = verifyInfoForm(false, true, false);
	
	if (verifyResult) {
		alert(verifyResult);
		return;
	}

	SendUtil.post('/questionForm/info', form2object('headForm'), function (res) {
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
					
						SendUtil.post('/questionForm/modifyColsByVice', reqData, function (response) {
							alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
							saveProgramForm();
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
				SendUtil.post('/questionForm/modifyColsByVice', reqData, function (response) {
					alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
					saveProgramForm();
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
	
	let verifyResult = verifyInfoForm(true, false, false);	  

	if (verifyResult) {
		alert(verifyResult);
		return;
	}


	if (confirm("<s:message code='form.question.form.info.confirm.send' text='確定送出?'/>")) {
		reqData = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm,form2object('verifyForm'));
		reqData.verifyLevel = 1;
		reqData.isNextLevel = true;
		reqData.verifyType = 'APPLY';
		reqData.jumpLevel = 2;
		ObjectUtil.dataToBackend(reqData, allDateArgs);

		SendUtil.post('/questionForm/send', reqData, function (response) {
			alert("<s:message code='form.question.form.info.success.send' text='送出成功'/>");
			SendUtil.href('/dashboard');
		}, null, true);
	}
}

function clickFormInfoDeleteButton () {
	if (confirm("<s:message code='form.question.form.info.configm.delete' text='確定刪除?'/>")) {
		SendUtil.post('/questionForm/delete', form2object('headForm'), function () {
			alert("<s:message code='form.question.form.info.success.delete' text='刪除成功!'/>");
			SendUtil.href('/dashboard');
		});
	}
}

function clickCheckLogAgreeButton (processOrder, signingInfo) {
	HtmlUtil.temporary();
	let headForm = form2object('headForm');
	
	SendUtil.post('/questionForm/info', headForm, function (formInfo) {
		let request = $.extend(headForm, formInfo);
		
		if (ValidateUtil.loginRole().isVice() && !validate(request)) {
			return;
		}
		
		let verifyResult = verifyInfoForm(true, false, false);
		if (ValidateUtil.loginRole().isVice()) {
			let reqData = $.extend(headForm, HtmlUtil.tempData.infoForm, HtmlUtil.tempData.programForm);
			ObjectUtil.dataToBackend(reqData, allDateArgs);
			verifyResult = ValidateUtil.formFields('/questionForm/validateColumnData/forSigningAction', reqData);
			
			if (verifyResult) {
				alert(verifyResult);
				DialogUtil.close();
				return;
			}
		}
		
		if (confirm("<s:message code='form.question.form.info.confirm.send' text='確定送出?'/>")) {
			request = $.extend(request, form2object('verifyForm'));
			request.isNextLevel = true;
			request.jumpLevel = processOrder;
			request.processOrder = request.verifyLevel;
			
			let isReview = headForm.verifyType == 'REVIEW';
			let isKnowledgeChecked = $('#isKnowledgeable').prop('checked');
			if (isKnowledgeChecked) {
				request.isKnowledgeable = 'Y';
			}

			SendUtil.post('/questionForm/approval', request, function (response) {				
				alert("<s:message code='form.verify.success' text='審核成功!'/>");
				DialogUtil.close();
					//加入知識庫規則:問題單進入結案流程，「副科代科長核可」或「科長核可」送籤到副理關卡，且勾選「建議加入處理方案?」
					if (isReview && (ValidateUtil.loginRole().isVice() || ValidateUtil.loginRole().isChief())) {
						if (isKnowledgeChecked || (request.isSuggestCase == "Y" && isKnowledgeChecked)) {
							SendUtil.post('/knowledgeForm/build', request, null, null, true);
						}
					}
					SendUtil.href('/dashboard');
			}, null, true);
		}
	});
}

function clickCheckLogRejectButton (processOrder) {
	HtmlUtil.temporary();
	
	let request = $.extend(form2object('headForm'), form2object('verifyForm'));
	let params = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm, form2object('verifyForm'));
	
	if (!request.verifyComment) {
		alert("<s:message code='form.verify.reject.reason.not.empty' text='請填寫退回原因!'/>");
		return;
	}
	
	if (confirm("<s:message code='form.verify.reject.confirm' text='確定退回?'/>")) {
		request.processOrder = request.verifyLevel;
		request.isNextLevel = false;
		request.jumpLevel = processOrder;
		SendUtil.post('/questionForm/reject', request, function (response) {
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
		alert("<s:message code='form.verify.deprecated.reason.not.empty' text='請填寫作廢原因!'/>");
		return;
	}
	
	if (confirm("<s:message code='form.verify.deprecated.confirm' text='確定作廢?'/>")) {
		SendUtil.post('/questionForm/deprecated', reqData, function (resData) {
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
		SendUtil.post('/questionForm/closeForm', reqData, function (resData) {
			alert("<s:message code='global.update.result' text='更新資料成功!'/>");
			DialogUtil.close();
			SendUtil.href('/dashboard');
		}, null, true);
	}
}

function verifyInfoForm (isForceVerify, isSave, isSuperVice) {
	ObjectUtil.clear(HtmlUtil.tempData.programForm);
	let verifyResult = '';
	let headForm = form2object('headForm');
	let isApproving = headForm.formStatus == 'APPROVING';
	let isWatching = headForm.formStatus == 'WATCHING';
	let reqData = $.extend(headForm, HtmlUtil.tempData.infoForm, HtmlUtil.tempData.programForm);
	
	if (isApproving || isWatching || isForceVerify) {
		ObjectUtil.dataToBackend(reqData, allDateArgs);
		reqData.isSave = !!isSave;
		reqData.isSuperVice = !!isSuperVice;
		let url = isSave ? '/questionForm/validateColumnData/forSaveAction' : '/questionForm/validateColumnData';
		verifyResult = 
			ValidateUtil.formFields(url, reqData);
	}
	
	return verifyResult;
}

function save () {
	saveProgramForm();
	authViewControl();
}

function getFormDomain () {
	return '/questionForm';
}
</script>
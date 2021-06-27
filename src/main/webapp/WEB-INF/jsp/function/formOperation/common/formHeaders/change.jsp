<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<h1><s:message code='change.form' text='變更單'/></h1>
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
		<input id='processStatus' class='hidden' name='processStatus' />
		<input id='createTime' class='hidden' name='createTime' />
		<input id='updatedAt' class='hidden' name='updatedAt' />
		<input id='parallel' class='hidden' name='parallel' />
		<input id='unitCategory' class='hidden' name='unitCategory' />
		<input id='totalFraction' class='hidden' name='totalFraction' />
		<input id='isVerifyAcceptable' class='hidden' type='checkbox' name='isVerifyAcceptable' />
		<input id='isModifyColumnData' class='hidden' type='checkbox' name='isModifyColumnData' />
		<input id='isCloseForm' class='hidden' type='checkbox' name='isCloseForm' />
		<input id='isApplyLastLevel' class='hidden' type='checkbox' name='isApplyLastLevel' />
		<input id='isCreateJobIssue' class='hidden' type='checkbox' name='isCreateJobIssue' />
		<input id='isBusinessImpactAnalysis' class='hidden' type='checkbox' name='isBusinessImpactAnalysis' />
		<input id='impactThreshold' class='hidden' name='impactThreshold' />
		<input id='isFromCountersign' class='hidden' type='checkbox' name='isFromCountersign' />
		<input id='isApprover' class='hidden' type='checkbox' name='isApprover' />
		<input id='isNextLevel' class='hidden' type='checkbox' name='isNextLevel' />
		<input id='isBackToApply' class='hidden' type='checkbox' name='isBackToApply' />
		<input id='isServerSideUpdated' class='hidden' type='checkbox' name='isServerSideUpdated' />
		<input id='isSave' class='hidden' type='checkbox' name='isSave' />
		<input id='isSuperVice' class='hidden' type='checkbox' name='isSuperVice' />
		<input id='isDifferentScope' class='hidden' type='checkbox' name='isDifferentScope' />
		
		<table class="grid_query">
			<tr>
				<th><s:message code='form.change.form.source.id' text='來源表單'/> </th>
				<td>
					<a id="returnPage" href="javascript:toForm();"><font id="sourceIdText" color="red"></font></a>
				</td>
				<th><s:message code='form.change.form.status' text='表單狀態'/> </th>
				<td id='formStatus'></td>
				<th><s:message code='form.change.create.form.division' text='開單科別'/></th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="divisionCreated" />
						<jsp:param name="name" value="divisionCreated" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
				<th><s:message code='form.change.create.user.name' text='開單人員'/></th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="userCreated" />
						<jsp:param name="name" value="userCreated" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
			</tr>
			<tr>
				<th><s:message code='form.change.process.division' text='處理科別'/></th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="divisionSolving" />
						<jsp:param name="name" value="divisionSolving" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
				<th><s:message code='form.change.process.group.id' text='處理群組'/></th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="groupSolving" />
						<jsp:param name="name" value="groupSolving" />
						<jsp:param name="isDisabled" value="true" />
					</jsp:include>
				</td>
				<th><s:message code='form.change.process.user.name' text='處理人員'/></th>
				<td>
					<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
						<jsp:param name="id" value="userSolving" />
						<jsp:param name="name" value="userSolving" />
						<jsp:param name="isDisabled" value="true" />
						<jsp:param name="defaultName" value='global.select.please.choose' />
					</jsp:include>
				</td>
				<th><s:message code='form.change.create.time' text='開單時間'/></th>
				<td id='createTime'></td>
				<td colspan="2">
					<%@ include file="/WEB-INF/jsp/function/formOperation/common/featureButtons/commonButtons.jsp"%>
					<%@ include file="/WEB-INF/jsp/function/formOperation/common/featureButtons/checkLogButtons.jsp"%>
				</td>
			</tr>
		</table>
	</form>
</fieldset>

<script>//# sourceURL=chgHead.js
var formInfo = baseFormInfo;

$(function () {
	fillForm(formInfo);
});

function fetchInfo (reqData) {
	if (reqData.isServerSideUpdated) {// 資料從伺服器運算並自動更新
		HtmlUtil.tempData.infoForm.cat = reqData.cat;
	}
	
	SendUtil.post('/changeForm/info', reqData, function (resData) {
		SendUtil.get("/html/getDivisionSelectors", true, function (option) {
			HtmlUtil.singleSelect('select#divisionCreated', option);
			$('select#divisionCreated').val(resData.divisionCreated);
		}, ajaxSetting);
		
		SendUtil.post("/html/getUserSelectors", reqData, function (option) {
			HtmlUtil.singleSelect('select#userCreated', option);
			$('select#userCreated').val(resData.userCreated);
		}, ajaxSetting);
		
		SendUtil.get("/html/getDivisionSelectors", true, function (option) {
			HtmlUtil.singleSelect('select#divisionSolving', option);
			$('select#divisionSolving').val(resData.divisionSolving);
		}, ajaxSetting);
		
		SendUtil.get("/html/getSysGroupSelectors", null, function (option) {
			HtmlUtil.singleSelect('select#groupSolving', option);
			$('select#groupSolving').val(resData.groupSolving);
		}, ajaxSetting);

		SendUtil.post("/html/getUserSelectors", reqData, function (option) {
			HtmlUtil.singleSelect('select#userSolving', option);
		}, ajaxSetting);
		
		fillForm(resData.formId ? resData : formInfo);
	}, ajaxSetting);

	if (!HtmlUtil.tempData.impactForm) {
		HtmlUtil.tempData.impactForm = {};
		
		SendUtil.post('/commonForm/getFormImpactAnalysis', reqData, function (impactList) {
			$.each(impactList, function (i, rowData) {
				if (rowData.questionType == "S") {
					HtmlUtil.tempData.impactForm.solution = rowData.description;
				} else if (rowData.questionType == "E") {
					HtmlUtil.tempData.impactForm.evaluation = rowData.description;
				} else if(rowData.questionType == "T") {
					HtmlUtil.tempData.impactForm.totalFraction = rowData.targetFraction;
				}
			});
			
			HtmlUtil.tempData.impactForm.impactList = impactList;
		});
	}
}

function fillForm (info) {
	$('input#formId').val(info.formId);
	
	ObjectUtil.autoSetFormValue(info, 'headForm');
	if (!HtmlUtil.tempData.infoForm) {
		ObjectUtil.autoSetFormValue(info, 'infoForm');
	} else {
		HtmlUtil.tempData.infoForm.changeRank = info.changeRank;
		ObjectUtil.autoSetFormValue(HtmlUtil.tempData.infoForm, 'infoForm');
	}
	
	$('#sourceIdText').text(info.sourceId);
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
	
	SendUtil.post('/commonForm/getEditableCols', info, function (response) {
		$('li#tabLog').show();
		$('li#tabCheckLog').show();
		$('li#tabFileList').show();
		$('li#tabLinkList').show();
		$('li#tabFormImpactAnalysis').show();
		
		HtmlUtil.enableFunctionButtons(response.buttons, true);
		HtmlUtil.disableElements(response.disabledColumns, true);
		HtmlUtil.disableElements(response.enabledColumns, false);
		ValidateUtil.adminCListViewCtrl();

		// 檢查當前頁簽的按鈕欄上沒有按鈕的話就把整欄隱藏
		$('div[id$="Buttons"]').toggle(
				$('div[id$="Buttons"] button').is(":visible"));
		
		/**
		* 1.舊變更單因母單延期，且已重新開立變更單時
		* (1)舊變更單：未結案前，隱藏【新增工作單】操作按鈕。
		* (2)新變更單：正確顯示【新增工作單】操作按鈕。
		* 2.新開立的變更單(尚未產生FormId、擬案中):隱藏【新增工作單】按鈕，經辦送出表單後再進入表單，才會顯示【新增工作單】。
		*/
		SendUtil.post('/changeForm/info', info, function (resData) {
			if (resData.isScopeChanged == 'Y' || resData.formId == "" || resData.formStatus == "PROPOSING") {
				$('#clickWorkingButton').hide();
			} else if (!ValidateUtil.loginRole().isPic()) {
				$('#clickWorkingButton').hide();
			} else {
				$('#clickWorkingButton').show();
			}
		}, ajaxSetting);
		
	}, ajaxSetting);
	
}

//檢查變更等級
function verifyChangeLevel () {
	let standard = $("select#standard").val();
	let impactForm = HtmlUtil.tempData.impactForm;
	let isNewSystem = $("input#isNewSystem").is(":checked");
	let isNewService = $("input#isNewService").is(":checked");
	let totalFraction = parseInt(impactForm ? impactForm.totalFraction : 0);
	let impactThreshold = parseInt(form2object('headForm').impactThreshold);
	
	$('span#isNewServiceWarning').toggle(isNewService);
	
	if(totalFraction >= impactThreshold || isNewSystem || isNewService) {
		$("input#changeRank").val('<s:message code="form.change.changeRank.2" text="重大變更"/>');
	} else {
		$("input#changeRank").val('<s:message code="form.change.changeRank.1" text="次要變更"/>');
	}
	
	if(standard) {
		$("input#changeType").val('<s:message code="form.change.changeType.2" text="標準變更"/>');
	} else {
		$("input#changeType").val('<s:message code="form.change.changeType.1" text="一般變更"/>');
	}
}

function tooltips (info) {
	var formTip = "<s:message code='form.change.form.tip.1.1' text='表單狀態：{0}'/>" + "\r\n" + "<s:message code='form.change.form.tip.1.2' text='流程狀態：{1}'/>"
	var processTip = "<s:message code='form.change.form.tip.2.1' text='表單類別：{0}'/>" + "\r\n" + "<s:message code='form.change.form.tip.2.2' text='設定科別：{1}'/>";
	
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
			
			if (reqData.isScopeChanged) {
				reqData.isDifferentScope = "Y";
			}
			
			ObjectUtil.dataToBackend(reqData, allDateArgs);
			saveImpactList();
			SendUtil.post('/changeForm/modifyColsByVice', reqData, function (response) {
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
	
	let verifyResult = verifyInfoForm(true, true, false);

	if (verifyResult) {
		alert(verifyResult);
		return;
	}

	if (confirm("<s:message code='form.question.form.info.confirm.send' text='確定送出?'/>")) {
		let reqData = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm,form2object('verifyForm'));
		reqData.processOrder = 1;
		reqData.isNextLevel = true;
		reqData.verifyType = 'APPLY';
		reqData.verifyLevel = 1;
		reqData.jumpLevel = 2;
		ObjectUtil.dataToBackend(reqData, allDateArgs);

		SendUtil.post('/changeForm/send', reqData, function (response) {
			alert("<s:message code='form.question.form.info.success.send' text='送出成功'/>");
			SendUtil.href('/dashboard');
		}, null, true);
	}
}

function clickFormInfoDeleteButton () {
	if (confirm("<s:message code='form.question.form.info.configm.delete' text='確定刪除?'/>")) {
		SendUtil.post('/changeForm/delete', form2object('headForm'), function () {
			alert("<s:message code='form.question.form.info.success.delete' text='刪除成功!'/>");
			SendUtil.href('/dashboard');
		});
	}
}

function clickCheckLogAgreeButton (processOrder) {
	HtmlUtil.temporary();
		
	if (confirm("<s:message code='form.question.form.info.confirm.send' text='確定送出?'/>")) {
		let request = $.extend(form2object('headForm'), form2object('verifyForm'));
		request.isNextLevel = true;
		
		if (processOrder) {
			request.jumpLevel = processOrder;
			SendUtil.post('/changeForm/approval', request, function (response) {
				alert("<s:message code='form.verify.success' text='審核成功!'/>");
				DialogUtil.close();
				SendUtil.href('/dashboard');
			}, null, true);
		} else {
			toReviewTargetLevel(request, isJumpReviewLast(request) ? '' : 1);
		}
	}
}

function clickCheckLogRejectButton (processOrder) {
	HtmlUtil.temporary();
	
	let request = $.extend(form2object('headForm'), form2object('verifyForm'));
	
	if (!request.verifyComment) {
		alert("<s:message code='form.verify.reject.reason.not.empty' text='請填寫退回原因!'/>");
		return;
	}

	if (confirm("<s:message code='form.verify.reject.confirm' text='確定退回?'/>")) {
		request.isNextLevel = false;
		request.processOrder = request.verifyLevel;
		request.jumpLevel = processOrder;
		SendUtil.post('/changeForm/reject', request, function (response) {
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
		SendUtil.post('/changeForm/deprecated', reqData, function (resData) {
			alert("<s:message code='form.verify.deprecated.success' text='作廢成功'/>");
			SendUtil.href('/dashboard');
		}, null, true);
	}
}

/*
 *  變更單的特殊邏輯
 *  變更單申請第2關固定為副科長；第3關固定為科長。
 *  當科長進行審核的時候系統需要判斷是否達到風險標準(以下簡稱達標/未達標)
 *  達標 : 系統需須直接進入審核第1關(固定為副理)
 *  未達標 : 系統需自動將關卡推進到審核最後1關(固定為經辦)
 */
function isJumpReviewLast (request) {
	let jump = false;
	
	SendUtil.post('/changeForm/info', request, function (response) {
		let warningWording;
		let isNewSystem = response.isNewSystem == 'Y';
		let isNewService = response.isNewService == 'Y';
		let isFromCountersign = request.isFromCountersign;
		let totalFraction = parseInt(response.totalFraction);
		let isReviewProcess = "REVIEW" == request.verifyType;
		let isImportantCChange = isNewSystem || isNewService;
		let impactThreshold = parseInt(response.impactThreshold);
		let isBusinessImpactAnalysis = response.isBusinessImpactAnalysis == 'Y'; // 是否代理審核衝擊分析最後一關的資訊
		let isImportantChange = isNewSystem || isNewService || (totalFraction >= impactThreshold);
						
		// 判斷該關卡是否有勾選衝擊分析判斷選項
		if (!isFromCountersign && !isReviewProcess && 
				isBusinessImpactAnalysis && !totalFraction) {
			alert("請輸入或選擇<s:message code='form.title.tabFormImpactAnalysis' text='衝擊分析'/>");
			return;
		}
		
		if (isFromCountersign && !isImportantCChange) {
			jump = true;
		}

		if (!isFromCountersign && !isImportantChange) {
			jump = true;
		}
	}, ajaxSetting);
	
	return jump;
}

// 跳至審核指定關卡
function toReviewTargetLevel (request, jumpToReviewLevel) {
	request.jumpLevel = jumpToReviewLevel;
	SendUtil.post("/changeForm/jumpToReviewTargetLevel", request, function (response) {
		alert("<s:message code='global.send.success' text='送出成功'/>");
		DialogUtil.close();
		SendUtil.href('/dashboard');
	}, null, true);
}

// 檢核並更新暫存資料
function verifyInfoForm (isForceVerify, isSave, isSuperVice) {
	let verifyResult = '';
	let headForm = form2object('headForm');
	let infoForm = HtmlUtil.tempData.infoForm;
	let impactForm = HtmlUtil.tempData.impactForm;
	let isApproving = headForm.formStatus == 'APPROVING';
	
	if (isApproving || isForceVerify) {
		let reqData = $.extend(headForm, infoForm);
		reqData.impactForm = impactForm;
		ObjectUtil.dataToBackend(reqData, allDateArgs);
		reqData.isSave = !!isSave;
		reqData.isSuperVice = !!isSuperVice;
		verifyResult += 
			ValidateUtil.formFields('/changeForm/validateColumnData', reqData);
	}
	
	return verifyResult;
}

function saveImpactList () {
	if (HtmlUtil.tempData.impactForm &&
			HtmlUtil.tempData.impactForm.impactList && 
			HtmlUtil.tempData.impactForm.impactList.length > 0) {
		let reqData = {};
		reqData.formId = form2object('headForm').formId;
		reqData.impactList = HtmlUtil.tempData.impactForm.impactList;
		SendUtil.post('/commonForm/saveFormImpactAnalysisList', reqData, function () {
			HtmlUtil.tempData.impactForm.impactList = impactList;
		}, ajaxSetting);
	}
}

function save () {
	saveImpactList();
	authViewControl();
	verifyChangeLevel();
}

function impactToJsObject (row) {
	let params = {};
	let target = $(row).next('tr');
	let content = target.find("#content").val();
	let fraction = target.find("#fraction").val();
	let fractionLs = target.find('#fractionLs').val();
	let description = target.find("#description").val();
	let questionType = target.find("#questionType").val();
	let targetFraction = target.find("#targetFraction").val();
	let isValidateFraction = target.find("#isValidateFraction").val();
	let isAddUp = target.find("#isAddUp").val();
	
	params.content = content;
	params.fraction = fraction;
	params.fractionLs = ObjectUtil.parse(fractionLs);
	params.description = description;
	params.questionType = questionType;
	params.targetFraction = targetFraction;
	params.isValidateFraction = isValidateFraction;
	params.isAddUp = isAddUp;
	
	return params;
}

function isQuestionData (element) {
	return (element.questionType != "E" && element.questionType != "S" && element.questionType != "T");
}

function getFormDomain () {
	return '/changeForm';
}

//直接結案按鈕事件
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
		SendUtil.post('/requirementForm/closeForm', reqData, function (resData) {
			alert("<s:message code='global.update.result' text='更新資料成功!'/>");
			DialogUtil.close();
			SendUtil.href('/dashboard');
		}, null, true);
	}
}
</script>
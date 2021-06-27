<%@ page contentType="text/html; charset=UTF-8"%>

<script>//# sourceURL=checkLogButtons.js
$(function () {
	$('custom#idWording').text(ValidateUtil.loginRole().isPic() ? '送出' : '簽核');
	
	if (ValidateUtil.loginRole().isVice()) {
		$('custom#logCloseWording').text('代科長審核');
	} else if (ValidateUtil.loginRole().isDirect1()) {
		$('custom#logCloseWording').text('代協理審核');
	} else {
		$('custom#logCloseWording').text('直接結案');
	}
});

/**
 * 開啟簽核dialog
 */
function prepareSigning (type) {
	let isSuggestCase;
	let headForm = form2object('headForm');
	
	SendUtil.post(getFormDomain() + '/info', headForm, function (formInfo) {
		let buttonEvents = {};
		buttonEvents["confirm"] = handleSigningEvent;
		buttonEvents["cancel"] = function () {};
		buttonEvents["deprecated"] = clickCheckLogDeleteButton;
		buttonEvents["closeForm"] = clickCheckLogCloseFormButton;
		buttonEvents["backToPic"] = backToPic;
		
		if (headForm.formClass == 'Q' &&
				 headForm.formClass == 'SR' &&
				 headForm.formClass == 'INC') {
			SendUtil.get(getFormDomain() + '/program', formInfo.formId, function (program) {
				if (HtmlUtil.tempData.programForm) {
					isSuggestCase = HtmlUtil.tempData.programForm.isSuggestCase;
				} else {
					isSuggestCase = program.isSuggestCase;
				}
	
				formInfo.isSuggestCase = isSuggestCase;
				formInfo.processOrder = formInfo.verifyLevel;
				SigningDialog.show(formInfo, buttonEvents, type);
			});
		} else {
			formInfo.processOrder = formInfo.verifyLevel;
			SigningDialog.show(formInfo, buttonEvents, type);
		}
	}, null, true);
}

/**
 * 處理簽核操作事件
 */
function handleSigningEvent() {
	let header = form2object('headForm');
	let verifyType = header.verifyType;
	let targetRadio = $("input[name='detailId']:checked");
	let isKnowledgeable = $("input[name='isKnowledgeable']:checked");
	let isNextLevel = (targetRadio.attr("isNextLevel") == 'true');
	let groupId = targetRadio.closest("tr").find(".groupIdClass").text();
	let processOrder = parseInt(targetRadio.closest("tr").find(".processOrderClass").text());
	
	if (isApplyFirstLevel(isNextLevel, verifyType, groupId)) {
		alert("流程第一關無法再執行退件!");
	} else if (isNextLevel) {
		HtmlUtil.temporary();
		let verifyResult = verifyInfoForm(false);

		if (verifyResult) {
			alert(verifyResult);
			return;
		}

		SendUtil.post(getFormDomain() + '/info', form2object('headForm'), function (formInfo) {	
			let isVice = ValidateUtil.loginRole().isVice();
			let isReview = ValidateUtil.formInfo().isReview(formInfo);
			let viewEct = DateUtil.fromDate(HtmlUtil.tempData.infoForm.ect);
			
			formInfo.isKnowledgeable = isKnowledgeable;
   
			if (isVice && isReview && viewEct > formInfo.ect) {
				DialogUtil.close();
				ScopeChangedDialog.show(function () {
					saveBasicInfo(function () {
						save();
						clickCheckLogAgreeButton(processOrder, formInfo);
					});
				});
			} else {
				saveBasicInfo(function () {
					save();
					clickCheckLogAgreeButton(processOrder, formInfo);
				});
			} 
		});
	} else {
		clickCheckLogRejectButton(processOrder);
	}
}

function isApplyFirstLevel(isNextLevel, verifyType, groupId) {
	return (!isNextLevel && "APPLY" == verifyType && "EMPTY" == groupId);
}

function isNeedSpcGroups (formInfo, spcGroups) {
	let clazz = formInfo.formClass;
	let isSP = formInfo.divisionSolving.indexOf('SP') != -1;
	return !('INC_C' == clazz) && (!spcGroups || spcGroups.length < 1) && isSP;
}

function isNeedUserSolving (formInfo) {
	let isSP = formInfo.divisionSolving.indexOf('SP') != -1;
	return !isSP && !formInfo.userSolving;
}

function backToPic () {
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
			SendUtil.post('/eventForm/backToPic', request, function (response) {
				alert("<s:message code='form.verify.reject.success' text='退回成功!'/>");
				DialogUtil.close();
				SendUtil.href('/dashboard');
			});
		}, null, true);
	}
}
</script>

<button id='signingButton' type="button" onclick='prepareSigning("SIGNING");' style="display: none;">
	<i class="iconx-apply"></i> <custom id='idWording'>簽核</custom>
</button>
<button id='checkLogDeleteButton' type="button" onclick='prepareSigning("DELETE");' style="display: none;">
	<i class="iconx-delete"></i> 作廢
</button>
<button id='checkLogCloseForm' type="button" onclick='prepareSigning("CLOSE_FORM");' style="display: none;">
	<i class="iconx-apply"></i> <custom id='logCloseWording'>直接結案</custom>
</button>

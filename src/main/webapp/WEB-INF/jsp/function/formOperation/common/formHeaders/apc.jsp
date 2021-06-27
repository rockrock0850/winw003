<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<h1 id="formWording"><s:message code="work.countersigned.form" text="工作會辦單" /></h1>
<fieldset>
	<legend id="formId">【NEW】</legend>
	<form id="headForm">
		<input id="formId" class="hidden" name="formId" />
		<input id="sourceId" class="hidden" name="sourceId" />
		<input id="formClass" class="hidden" name="formClass" />
		<input id="formType" class="hidden" name="formType" />
		<input id="verifyLevel" class="hidden" name="verifyLevel" />
		<input id="verifyType" class="hidden" name="verifyType" />
		<input id="detailId" class="hidden" name="detailId" />
		<input id="formStatus" class="hidden" name="formStatus" />
		<input id="createTime" class="hidden" name="createTime" />
		<input id="updatedAt" class="hidden" name="updatedAt" />
		<input id="processStatus" class="hidden" name="processStatus" />
		<input id="isVerifyAcceptable" class="hidden" type="checkbox" name="isVerifyAcceptable" /> 
		<input id="isWorkLevel" class="hidden" type="checkbox" name="isWorkLevel" />
		<input id="isModifyColumnData" class="hidden" type="checkbox" name="isModifyColumnData" />
		<input id="isCreateJobCIssue" class="hidden" type="checkbox" name="isCreateJobCIssue" />
		<input id='isApplyLastLevel' class='hidden' type='checkbox' name='isApplyLastLevel' />
		<input id="isCreateCompareList" class="hidden" type="checkbox" name="isCreateCompareList" />
		<input id="isWaitForSubIssueFinish" class="hidden" type="checkbox" name="isWaitForSubIssueFinish" />
		<input id="isOwner" class="hidden" type="checkbox" name="isOwner" />
        <input id="isCloseForm" class="hidden" type="checkbox" name="isCloseForm" />
		<input id='isApprover' class='hidden' type='checkbox' name='isApprover' />
		<input id='isNextLevel' class='hidden' type='checkbox' name='isNextLevel' />
		<input id='isBackToApply' class='hidden' type='checkbox' name='isBackToApply' />
		<input id='isAssigning' class='hidden' type='checkbox' name='isAssigning' />
		<input id='isApTabs' class='hidden' type='checkbox' name='isApTabs' />
		<input id='isServerSideUpdated' class='hidden' type='checkbox' name='isServerSideUpdated' />
		<input id='isSave' class='hidden' type='checkbox' name='isSave' />
		<input id='isSuperVice' class='hidden' type='checkbox' name='isSuperVice' />
		<input id='currentProcess' class='hidden' name='currentProcess' />
		<input id='internalProcessItems' class='hidden' name='internalProcessItems' />
		
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

<script>//# sourceURL=apcHead.js
var formInfo = baseFormInfo;

$(function () {
    fillForm(formInfo);
});

function fetchInfo (request) {
    SendUtil.post("/apJobCForm/info", request, function (response) {
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
		
        request.countersigneds = getInternalProcessQueryFlag(response);
        if (ValidateUtil.loginRole().isPic() && request.countersigneds === "Y") {
        	$("button#signingButton").attr("onclick", "signingWithInternalProcess();");
        }
        
		SendUtil.post("/html/getUserSelectors", request, function (option) {
			HtmlUtil.singleSelect('select#userSolving', option);
		}, ajaxSetting);
        
        fillForm(response.formId ? response : formInfo);
	}, ajaxSetting);
 	
 	let formId = request.formId ? 
 			request.formId : request.sourceId;
 	
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

function getInternalProcessQueryFlag (response) {
	let result = "N";
	let divisionSolving = response.divisionSolving;
	let groupSolving = response.groupSolving;
	if (divisionSolving.endsWith("DC") || (divisionSolving.endsWith("SP") && groupSolving.endsWith("OP"))) {
		result = "Y";
	}
	return result;
}

function fetchCInfo (action, tabId, reqData) {
	let isDb = reqData[1] == 'DB';
	
    SendUtil.get("/commonJobForm" + action, reqData, function (resData) {
    	if (resData.id) {
    		let segments = [];
    		let formId = $('input#formId').val();
    		
	    	HtmlUtil.tempData[tabId] = 
	    		HtmlUtil.tempData[tabId] ? HtmlUtil.tempData[tabId] : {};
	    	HtmlUtil.tempData[tabId] = $.extend(resData, HtmlUtil.tempData[tabId]);
    		resData.formId = formId;

			segments = HtmlUtil.tempData[tabId].segments;
    		if (isDb && segments.length > 0) {
    			$.each(segments, function (i, item) {
    				item.formId = formId;
    			});
    		}
    		
	        ObjectUtil.autoSetFormValue(resData, tabId, ["dataType", "division"]);
    	}
	});
    
    // 生成主單的暫存附件檔
    if (isDb) {
    	reqData = form2object('headForm');
    	reqData.type = 'DB';
    	SendUtil.post('/commonForm/getFileList', reqData, function () {
    		if (TableUtil.isDataTable('#otherTable')) {
        		setupFiles();
    		}
    	});
    }
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
		
		HtmlUtil.enableFunctionButtons(response.buttons, true);
		HtmlUtil.disableElements(response.disabledColumns, true);
		HtmlUtil.disableElements(response.enabledColumns, false);
		ValidateUtil.adminCListViewCtrl();

		// 檢查當前頁簽的按鈕欄上沒有按鈕的話就把整欄隱藏
		$('div[id$="Buttons"]').toggle(
				$('div[id$="Buttons"] button').is(":visible"));

        displayTabs(info);
	}, ajaxSetting);
}

function tooltips(info) {
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
		setInternalProcess(false);
		saveBasicInfo(function () {
			save();
			setInternalProcess(true);
			alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
		});
	}
}

function clickInternalProcessButton () {
	HtmlUtil.temporary();
	
	let verifyResult = verifyInfoForm(false, true);

	if (verifyResult) {
		alert(verifyResult);
		return;
	}
	
	InternalProcessDialog.show(formInfo);
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
			setInternalProcess(false);
			reqData = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm,form2object('modifyForm'));
			reqData.isNextLevel = false;
			ObjectUtil.dataToBackend(reqData, allDateArgs);
			SendUtil.post('/apJobCForm/modifyColsByVice', reqData, function (response) {
				alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
				fetchInfo(response);
				setInternalProcess(true);
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

	let reqData = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm);
	reqData.isAssigning = true;
	reqData.groupId = StringUtil.spLoginTitle(loginUserInfo);
	ObjectUtil.dataToBackend(reqData, allDateArgs);
	SendUtil.post('/apJobCForm/send', reqData, function (response) {
	    alert('<s:message code="global.send.success" text="送出成功!" />');
	    SendUtil.href('/dashboard');
	}, null, true);
}

function clickFormInfoDeleteButton () {
	if (confirm("<s:message code='form.question.form.info.configm.delete' text='確定刪除?'/>")) {
		SendUtil.post('/apJobCForm/delete', form2object('headForm'), function () {
			alert("<s:message code='form.question.form.info.success.delete' text='刪除成功!'/>");
			SendUtil.href('/dashboard');
		});
	}
}

function clickCheckLogAgreeButton (processOrder) {
	let headForm = form2object('headForm');
	headForm.processOrder = headForm.verifyLevel;
	
	SendUtil.post('/apJobCForm/info', headForm, function (formInfo) {
		if (!formInfo.userSolving) {
			alert('請選擇處理人員。');
			return ;
		}
		
		if (confirm("<s:message code='form.question.form.info.confirm.send' text='確定送出?'/>")) {
			let request = $.extend(headForm, form2object('verifyForm'));
			request.isNextLevel = true;
			request.jumpLevel = processOrder;
			SendUtil.post('/apJobCForm/approval', request, function (response) {
				alert("<s:message code='form.verify.success' text='審核成功!'/>");
				DialogUtil.close();
				SendUtil.href('/dashboard');
			}, null, true);
		}
	});
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

function clickCheckLogRejectButton (processOrder) {
	HtmlUtil.temporary();
	
	let request = $.extend(form2object('headForm'), form2object('verifyForm'));
	request.processOrder = request.verifyLevel;
	
	if (!request.verifyComment) {
		alert("<s:message code='form.verify.reject.reason.not.empty' text='請填寫退回原因!'/>");
		return;
	}
	
	if (confirm("<s:message code='form.verify.reject.confirm' text='確定退回?'/>")) {
		SendUtil.post('/apJobCForm/info', request, function (formInfo) {
			request = $.extend(request, formInfo);
			request.isNextLevel = false;
			request.jumpLevel = processOrder;
			SendUtil.post('/apJobCForm/reject', request, function (response) {
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
		alert("<s:message code='form.verify.reject.reason.not.empty' text='請填寫退回原因!'/>");
		return;
	}
	
	if (confirm("<s:message code='form.verify.deprecated.confirm' text='確定作廢?'/>")) {
		SendUtil.post('/apJobCForm/deprecated', reqData, function (resData) {
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
		SendUtil.post('/apJobCForm/closeForm', reqData, function (resData) {
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
	let isAssigning = headForm.formStatus == 'ASSIGNING'; //待分派
	
	if (isApproving || isForceVerify || isAssigning) {
		let infoForm = $.extend(headForm, HtmlUtil.tempData.infoForm);
		ObjectUtil.dataToBackend(infoForm, allDateArgs);
		infoForm.isSave = !!isSave;
		infoForm.isSuperVice = !!isSuperVice;  
		verifyResult = 
			ValidateUtil.formFields('/apJobCForm/validateColumnData', infoForm);
		verifyResult += verifyResult ? "\n" : '';
	}
	
	verifyResult += verifyCTabs(headForm);
	
	return verifyResult;
}

function saveJobTabs () {
	let division = form2object('headForm').divisionSolving;
	
	if (division) {
		division = division.substring(7, 9);
		
		if (division == 'OA') {
			saveJobCTab('/saveOaDetail', 'tabOaForm');
		} else if (division == 'EA') {
			saveJobCTab('/saveEaDetail', 'tabEaForm');
		} else if (division == 'SP') {
			saveJobCTab('/saveSpDetail', 'tabSpForm');
		} else if (division == 'PT') {
			saveJobCTab('/savePtDetail', 'tabPtForm');
		} else if (division == 'AP1') {
			saveJobCTab('/saveApDetail', 'tabAp1Form');
		} else if (division == 'AP2') {
			saveJobCTab('/saveApDetail', 'tabAp2Form');
		} else if (division == 'AP3') {
			saveJobCTab('/saveApDetail', 'tabAp3Form');
		} else if (division == 'AP4') {
			saveJobCTab('/saveApDetail', 'tabAp4Form');
		} else if (division == 'DC') {
			saveJobCTab('/saveDcDetail', 'tabDc1Form');
			saveJobCTab('/saveDcDetail', 'tabDc2Form');
			saveJobCTab('/saveDcDetail', 'tabDc3Form');
			saveJobCTab('/saveBatchDetail', 'tabBtForm');
			saveDbAlter();
		} else if (division == 'PLAN') {
			saveJobCTab('/savePlanmgmtDetail', 'tabPm1Form');
		} else if (division == 'MGMT') {
			saveJobCTab('/savePlanmgmtDetail', 'tabPm2Form');
		}
	}
}

function save () {
	saveJobTabs();
	authViewControl();
}

// 根據條件顯示工作單科別相關頁簽
function displayTabs (request) {
	SendUtil.post("/commonJobForm/getJobTabs", request, function (data) {
		let replacement;
		$.each(data.jobTabName, function(index, element) {
			replacement = StringUtil.format("[jobTabName = '{0}']", element);
			$("li" + replacement).show();
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

function signingWithInternalProcess() {
	let info = form2object('headForm');
	SendUtil.get('/html/getUnfinishedInternalProcess', info.formId, function (response) {
		if (response && response.length > 0) {
			let process = response.join(", ");
			alert("<s:message code='form.question.form.info.internalprocess.send.error' arguments='"+process+"'/>");
		} else {
			prepareSigning("SIGNING");
			$("form#verifyForm textarea#verifyComment").val("<s:message code='form.question.form.info.internalprocess.finished.signing.comment' text='內會結束，送副科長審核'/>");
		}
	}, null, true);
}

function setInternalProcess(isInit) {
	if (isInit) {
		let internalProcessItems = $("input#internalProcessItems").val();
		if (internalProcessItems && internalProcessItems.split(",").length > 0) {
			let items = internalProcessItems.split(",");
			$("table#internalProcessList input[name='internalProcessItem']").prop("checked", false);
			for (let index in items) {
				$("table#internalProcessList input[name='internalProcessItem'][value='"+items[index]+"']").prop("checked", true);
			}
		}
	} else {
		let arr = new Array();
		$.map($("table#internalProcessList input[name='internalProcessItem']:checked"), function(e){
		    arr.push(e.value);
		});
		$("input#internalProcessItems").val(arr.join(","));
	}
}

function getFormDomain () {
	return '/apJobCForm';
}
</script>

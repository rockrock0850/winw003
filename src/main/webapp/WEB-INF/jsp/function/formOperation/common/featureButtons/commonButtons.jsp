<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<button id="formInfoSaveButton" type="button" onclick='clickFormInfoSaveButton();' style="display: none;">
	<i class="iconx-save"></i> 儲存
</button>
<button id="formInfoSaveButtonForVice" type="button" onclick='clickViceSaveButton();' style="display: none;">
	<i class="iconx-save"></i> 儲存 
</button>
<button id='internalProcessButton' type='button' onclick='clickInternalProcessButton();' style="display: none;">
	<i class="icon-apply"></i> 內會
</button>
<button id='splitProcessViceButton' type='button' onclick='clickSplitProcessViceButton();' style="display: none;">
	<i class="iconx-save"></i> 串會副科長
</button>
<button id='formInfoDraftButton' type='button' onclick='clickFormInfoSaveButton();' style="display: none;">
	<i class="iconx-save"></i> 暫存
</button>
<button id='formInfoSendButton' type='button' onclick='clickFormInfoSendButton();' style="display: none;">
	<i class="iconx-upload"></i> 送出
</button>
<button id="formInfoDeleteButton" type='button' onclick='clickFormInfoDeleteButton();' style="display: none;">
	<i class="iconx-delete"></i> 刪除
</button>
<c:choose>
	<c:when test='${formClass == "SR" || formClass == "Q"}'>
		<!-- 需求單\問題單 -->
		
		<button id='clickAlterButton' type='button' onclick='clickAlterButton1();' style="display: none;">
			<i class="iconx-add"></i> 新增變更單
		</button>
		<button id='clickCountersignedButton' type='button' onclick='clickCountersignedButton1();' style="display: none;">
			<i class="iconx-add"></i> 新增會辦單
		</button>
	</c:when>
	
	<c:when test='${formClass == "INC"}'>
		<!-- 事件單 -->
		
		<button id='clickAlterButton' type='button' onclick='clickAlterButton1();' style="display: none;">
			<i class="iconx-add"></i> 新增變更單
		</button>
		<button id='clickCountersignedButton' type='button' onclick='clickCountersignedButton1();' style="display: none;">
			<i class="iconx-add"></i> 新增會辦單
		</button>
		<button id='clickQButton' type='button' onclick='clickQButton1();' style="display: none;">
			<i class="iconx-add"></i> 新增問題單
		</button>
	</c:when>
	
	<c:when test='${formClass == "CHG"}'>
		<!-- 變更單 -->
		
		<button id='clickWorkingButton' type='button' onclick='clickWorkingButton1();' style="display: none;">
			<i class="iconx-add"></i> 新增工作單
		</button>
	</c:when>
	
	<c:when test='${formClass == "JOB_AP" or formClass == "JOB_SP"}'>
		<!-- 工作單 -->
		
		<button id="clickJobCFormButton" type='button' onclick="clickJobCFormButton1();" style="display: none;">
			<i class="iconx-add"></i> 新增會辦單
		</button>
	</c:when>
	
	<c:otherwise>
		<!-- 會辦單 -->
		
		<button id='clickWorkingButton' type='button' onclick='clickWorkingButton1();' style="display: none;">
			<i class="iconx-add"></i> 新增工作單
		</button>
		<button id='clickAlterButton' type='button' onclick='clickAlterButton1();' style="display: none;">
			<i class="iconx-add"></i> 新增變更單
		</button>
	</c:otherwise>
</c:choose>

<script>//# sourceURL=commonButtons.js
var allDateArgs = [
	'it',
	'ect', 
	'eot', 
	'act', 
	'ast', 
	'cat', 
	'cct',
	'tct',
	'sct',
	'sit',
	'ist',
	'ict',
	'eit',
	'mect',
	'assignTime',
	'reportTime', 
	'onLineTime',
	'offLineTime',
	'observation', 
	'excludeTime',
	'effectDate',
	'oect'
];

$(function() {
	fillForm(formInfo);
	fetchInfo(form2object('headForm'));
	
	// 副科儲存按鈕的顯示條件
	if (isViceSave()) {
		$("#formInfoSaveButtonForVice").attr("style", "display: inline-block;");
		$("#formInfoSaveButton").attr("style", "visibility: hidden;");
	}
});

function saveBasicInfo (callback) {
	let reqData = HtmlUtil.tempData.infoForm;
	reqData = $.extend(form2object('headForm'), reqData);
	
	reqData.isSave = true;
	reqData = ObjectUtil.dataToBackend(reqData, allDateArgs);
	
	SendUtil.post(getFormDomain() + '/save', reqData, function (resData) {
		fetchInfo(resData);
		callback(resData);
	}, ajaxSetting);
}

function saveProgramForm () {
	if (HtmlUtil.tempData.programForm) {
		let reqData = HtmlUtil.tempData.programForm;
		reqData = $.extend(form2object('headForm'), reqData);
		reqData = ObjectUtil.dataToBackend(reqData, allDateArgs);

		SendUtil.post(getFormDomain() + '/program', reqData, function (resData) {
			let tempData = HtmlUtil.tempData.programForm;
			ObjectUtil.autoSetFormValue(tempData, 'programForm');
			HtmlUtil.tempData.programForm = $.extend(tempData, resData);
		}, ajaxSetting);
	}
}

function saveJobCTab (action, tabId) {
	let reqData = HtmlUtil.tempData[tabId];
	
	if (reqData) {
		reqData = $.extend(reqData, form2object('headForm'));
		reqData = ObjectUtil.dataToBackend(reqData, allDateArgs);
		SendUtil.post("/commonJobForm" + action, reqData, function (resData) {
			HtmlUtil.tempData[tabId].id = resData.id;
			ObjectUtil.autoSetFormValue(HtmlUtil.tempData[tabId], tabId);
			
			if (resData.segments && 
					TableUtil.isDataTable('#segmentTable')) {
				let tempData = HtmlUtil.tempData[tabId];
				let segmentTable = $('#segmentTable').DataTable();
				tempData.segments = resData.segments;
				TableUtil.reDraw(segmentTable, resData.segments);
			}
		}, ajaxSetting);
	}
}

function saveDbAlter () {
	if (HtmlUtil.tempData.tabDbAlterForm) {
		let segments = HtmlUtil.tempData.tabDbAlterForm.segments;
		
		if (segments && segments.length > 0) {
			$.each(segments, function (i, item) {
				if (item.id.toString().
						indexOf('new') != -1) {
					item.id = '';
				};
			});
		}
	}
	
	saveJobCTab('/saveDbAlterDetail', 'tabDbAlterForm');
}

function clickCountersignedButton1 () {
	var confirmButton = function (selected) {
		if (!selected.division) {
			alert('請選擇科別!');
			return;
		}
		
		if (!confirm('確定會辦「' + selected.wording + '」?')) {
			return;
		}
		
		DialogUtil.showLoading();

		SendUtil.post(getFormDomain() + '/info', form2object('headForm'), function (response) {
			let request = $.extend(form2object('headForm'), response);
			request.userId = $('select#userSolving').find(':selected').text();
			request.unitId = $('select#divisionCreated').find(':selected').text();
			request.userGroup = $('select#groupSolving').find(':selected').text();
			request.divisionSolving = selected.division;
			ObjectUtil.dataToBackend(request, allDateArgs);
			HtmlUtil.putBreadcrumb('formSearch/search/' + request.formId);
			
			DialogUtil.close();
			SendUtil.post('/countersignedForm/build', request, function (response) {
				SendUtil.formPost('/countersignedForm', response, true);
			}, null, true);
		});
	}
	
	SolvingDialog.showLimit(getFormDomain(), confirmButton, function () {});
}

function clickJobCFormButton1 () {
    var confirmButton = function (selected) {
        if (!selected.division) {
            alert('<s:message code="form.common.choose.division" text="請選擇科別!" />');
            return;
        }
        
        if (confirm(StringUtil.format('<s:message code="form.common.countersigned.confirm" text="確定會辦「{0}」?" />', selected.wording))) {
        	DialogUtil.showLoading();
        	
    		SendUtil.post(getFormDomain() + '/info', form2object('headForm'), function (response) {
    	        let request = $.extend(form2object('headForm'), response);
    	        request.divisionSolving = selected.division;
    	        ObjectUtil.dataToBackend(request, allDateArgs);
    			HtmlUtil.putBreadcrumb('formSearch/search/' + request.formId);
    	        
    	        let isSP = request.divisionSolving.indexOf("SP") >= 0;
    	        let domain = isSP ? '/spJobCForm' : '/apJobCForm';
    	        
    	        //送出之後就要關閉Dialog,否則會因為畫面卡頓而變得很突兀
   	        	DialogUtil.close();
    	        SendUtil.post(domain + '/build', request, function (response) {
    	            SendUtil.formPost(domain, response,true);
    	        }, null, true);
    		});
        }
    }
    
    SolvingDialog.showLimit(getFormDomain(), confirmButton, function () {});
}

// 新增變更單
function clickAlterButton1 () {
	if(confirm("確定新增?")) {
		DialogUtil.showLoading();
		let headForm = form2object('headForm');
		SendUtil.post(getFormDomain() + '/info', headForm, function (formInfo) {
			let request = $.extend(headForm, formInfo);
			ObjectUtil.dataToBackend(request, allDateArgs);
			HtmlUtil.putBreadcrumb('formSearch/search/' + request.formId);
			SendUtil.post('/changeForm/build', request, function (response) {
				DialogUtil.close();
				SendUtil.formPost('/changeForm', response, true);
			}, null, true);
		});
	} 
}

// 新增問題單
function clickQButton1 () {
	if(confirm("確定新增?")) {
		let sourceId = $("input#formId").val();
		HtmlUtil.putBreadcrumb('formSearch/search/' + sourceId);
		SendUtil.hrefOpenWindow('/questionForm/init', sourceId);
	} 
}

/**
 * 1. 新增工作單
 * 2.【新增工作單】按鈕，系統檢核:「預計變更結束時間」>= 當前系統時間
 *    若通過，允許新增；反之ALERT
 */
function clickWorkingButton1 () {
	 var sysTime = new Date();
	 var cct = new Date($('#cct').val());
	 
	 if (cct != "" && cct < sysTime) {
			alert("<s:message code='form.common.cct.must.more.than.sys.time' text='超過「預計變更結束時間」，無法新增工作單 '/>");
			return;
		}
	 
	 if (confirm("<s:message code='form.change.form.tip.5' text='是否需要新開工作單?'/>")) {
			DialogUtil.showLoading();
			
			SendUtil.post(getFormDomain() + '/info', form2object('headForm'), function (response) {
				let request = $.extend(form2object('headForm'), response);
				let isSP = request.divisionSolving.indexOf("SP") != -1;
				let path = isSP ? '/spJobForm' : '/apJobForm';

				ObjectUtil.dataToBackend(request, allDateArgs);
				HtmlUtil.putBreadcrumb('formSearch/search/' + request.formId);
				SendUtil.post(path + '/build', request, function (response) {
					DialogUtil.close();
					SendUtil.formPost(path, response,true);
				}, null, true);
			});
		}
}

function isViceSave () {
	let headForm = form2object('headForm');
	let isKLForm = headForm.formClass == 'KL';
	let isVice = ValidateUtil.loginRole().isVice();
	let isProposing = headForm.formStatus == 'PROPOSING';
	let isVerifyAcceptable = headForm.isVerifyAcceptable;
	let divisionByUser = loginUserInfo.groupId.split("-")[1];
	let divisionSolving = headForm.divisionSolving.split("-")[1];
	
	return isVice && 
			!isKLForm && 
			!isProposing && 
			!isVerifyAcceptable && 
			headForm.formStatus !== 'DEPRECATED' && 
			divisionSolving == divisionByUser.split("VSC")[0];
}
</script>
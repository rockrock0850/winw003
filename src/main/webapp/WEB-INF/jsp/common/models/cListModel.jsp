<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<!--  
輸入參數說明 : 
	- formClass : 表單類型
-->

<script>
var CListModel = function () {
	var nextRow = 3;
	var countersigneds = [];
	var ajaxSetting = {async:false};
	var formClass = PurifyUtil.dom("<c:out value='${param.formClass}'/>");
	var headForm = form2object('headForm');
	var isVerifyAcceptable = headForm.isVerifyAcceptable;
	
	var init = function () {
		var tr = $('<tr></tr>');
		var isDisabled = false;
		var td = '<td><label><input id="{0}" type="checkbox" value="{1}" onclick="CListModel.check(this)" />{2}</label></td>';	
		var formInfo = form2object('headForm');
		
		fetchCountersigneds(formInfo);
		
		let data = "C_LIST" + "/" + formClass;
		SendUtil.get('/html/getDropdownList', data, function (response) {
			isDisabled = $('input#countersigneds').prop('disabled');
			
			genCountersigneds(response, td, tr);
			
			$.each(countersigneds, function (i, item) {
				$('table#cList input#' + item).prop('checked',true);
				$('input#countersigneds').val(countersigneds.join(','));
			});
			
			$('table#cList input[type="checkbox"]').attr('disabled', isDisabled);
		}, ajaxSetting);
		
	}
	
	function check (checked) {
		if (formInfo.formStatus != "PROPOSING" && !isVerifyAcceptable &&
				ValidateUtil.loginRole().isVice()) {
			save (checked);
			return;
		} else {
			modifyCountersigneds (checked);
		}
	}
	
	function save (checked) {
		var checkedDivision = $(checked).val();
		var counterSignStatus = "";
		var type = "";
		
		var buttonEvents = {};
		buttonEvents["cancel"] = function () {
			restoreCheck(checked);
			DialogUtil.close();
		};
		buttonEvents["confirm"] = function () {
			
			let reqData = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm, form2object('modifyForm'));
			if (!reqData.modifyComment) {
				alert("<s:message code='form.modify.reason.not.empty' text='請填寫修改原因!'/>");
				return;
			}
			
			if (confirm("<s:message code='global.save.confirm' text='確定儲存?'/>")) {
				modifyCountersigneds(checked);
				if (type =="addAndDec") {
					SendUtil.get('/commonForm/saveCountersignds', [formInfo.formId, formInfo.formClass, $('#countersigneds').val()],
						function (data) {
					},ajaxSetting,true);
				} else if (type == "delProposing") {
					SendUtil.get('/commonForm/deprecatedForms', [formInfo.formId, checkedDivision, formInfo.formClass, $('#countersigneds').val()],
						function (data) {
					},ajaxSetting,true);
				}
				
				reqData = $.extend(form2object('headForm'), HtmlUtil.tempData.infoForm,form2object('modifyForm'));
				reqData.isNextLevel = false;
				ObjectUtil.dataToBackend(reqData, allDateArgs);
			
				SendUtil.post('/commonForm/modifyCountersigndByVice', reqData, function (response) {
					alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
// 					fetchInfo(response);
					DialogUtil.close();
				}, null, true);
			}
		};
		
		//加會
		if($(checked).is(':checked')){
			//修改原因
			if (!isVerifyAcceptable) {
				//彈出視窗
				type = "addAndDec";
				ModifyingDialog.show(formInfo,buttonEvents,"cList");
				return;
			}
		}
		
		//減會判斷
		SendUtil.get('/commonForm/getCountersignedForm', [formInfo.formId, checkedDivision], function (data) {
			counterSignStatus = data;
		},ajaxSetting,false);
		
		if (counterSignStatus == "inProcess") {
			alert('<s:message code="form.common.is.countersignds.worning.1" text="會辦單已開出，無法取消會辦" />');
			restoreCheck(checked);
		} else if (counterSignStatus == "proposing") {
			if (confirm('<s:message code="form.common.is.countersignds.deprecated" text="若取消會辦項目，將作廢：擬案中的會辦單，是否確認？" />')){
				//修改原因
				if (!isVerifyAcceptable) {
					//彈出視窗
					type = "delProposing";
					ModifyingDialog.show(formInfo,buttonEvents,"cList");
					return;
				}
			} else {
				restoreCheck(checked);
			}
		} else {
			//修改原因
			if (!isVerifyAcceptable) {
				//彈出視窗
				type = "addAndDec";
				ModifyingDialog.show(formInfo,buttonEvents,"cList");
				return;
			}
		}
	}
	
	function modifyCountersigneds (checked) {
		countersigneds = ObjectUtil.arrayRemove(countersigneds, $(checked).val());
		if ($(checked).is(':checked')) {
			countersigneds.push($(checked).val());
		}
		$('input#countersigneds').val(countersigneds.join(','));
	}

	function genCountersigneds (response, td, tr) {
		$.each(response, function (i, item) {				
			if (i != 0 && i % nextRow == 0) {
				$('table#cList').append(tr);
				tr = $('<tr></tr>');
			}
			
			if ('JOB' == formClass && 
					'SP' == item.value) {
				return true;
			}
			
			tr.append(StringUtil.format(td, item.value, item.value, item.wording));
			
			if (i == response.length-1) {
				$('table#cList').append(tr);
			}
		});
		
		$('table#cList').append(tr);
	}
	
	function fetchCountersigneds (formInfo) {
		if (HtmlUtil.tempData.infoForm) {
			countersigneds = HtmlUtil.tempData.infoForm.countersigneds;
			countersigneds = countersigneds ? countersigneds.split(',') : [];
		} else if (formInfo.formStatus) {
			SendUtil.get('/commonForm/getCListFromFormSelected', 
					[formInfo.formId, formInfo.formClass], function (response) {
				if (response.countersigneds) {
					countersigneds = response.countersigneds.split(',');	
				}
			}, ajaxSetting);
		}
	}
	
	function restoreCheck (checked) {
		if($(checked).is(':checked')){
			$(checked).prop('checked',false);
		} else {
			$(checked).prop('checked',true);
		}
	}
	
	return {
		init : init,
		check : check
	};
}();

$(function () {
	CListModel.init();
});
</script>

<table id='cList' class="grid_query"></table>
<input id='countersigneds' class='hidden' type='text' name='countersigneds' />
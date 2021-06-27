<%@ page contentType="text/html; charset=UTF-8" %>
<%-- 表單流程 共用JS --%>
<script>//# sourceURL=FormProcessCommonSetting.js
const isEnable = '${isEnable}';//流程狀態是否啟用(只要有值則為編輯狀態,若無則是新增)
const countersign = '${isCountersign}';//是否為會辦單
var applyGroupSelectorDatas = {};//申請流程群組
var reviewGroupSelectorDatas = {};//審核流程群組

$(document).ready(function() {
	let formType = "${formType}";
	if("" != formType) {
		$("#formType").val(formType);//初始化下拉選單的值
	} 
	
	<%-- 按鈕事件 --%>
	$('button').click(function() {
		var add = $(this).is("#addApplyBtn");//刪除申請流程
		var add1 = $(this).is("#addReviewBtn");//刪除申請流程
		var del = $(this).is("#deleteApplyBtn");//刪除申請流程
		var del2 = $(this).is("#deleteReviewBtn");//刪除申請流程
		var save = $(this).find('i').hasClass('iconx-save');//保存
		var back = $(this).find('i').hasClass('iconx-back');//回首頁
	
		if (back == true) {
			goBackSearchPage();
		}
		if (save == true) {
			if(confirm("<s:message code='form.process.managment.add.page.function.confirm.1' text='是否確定儲存?' />")) {
				saveProcess();
			}
		}
		
		//刪除申請流程
		if (del == true) {
			if (confirm("<s:message code='form.process.managment.add.page.function.confirm.2' text='是否確定刪除?' />")) {
				let allApplyProcessCheckBox = $('#applyProcessTable').find('input[name="delCheck"]:checked');
				
				allApplyProcessCheckBox.each(function() {
					$(this).parent().parent().remove();
				});
				
				resetSelectorAndOrderText('applyProcessOrderText','applyProcessTable');
			}
		}
		
		//刪除審核流程
		if (del2 == true) {
			if (confirm("<s:message code='form.process.managment.add.page.function.confirm.2' text='是否確定刪除?' />")) {
				let reviewProcessCheckBox = $('#reviewProcessTable').find('input[name="delCheck"]:checked');
				
				reviewProcessCheckBox.each(function() {
					$(this).parent().parent().remove();
				});
			
				resetSelectorAndOrderText('reviewProcessText','reviewProcessTable')
			}
		}
		
		//新增申請流程
		if (add == true) {
			let division = $("#division").val();
			
			if(division != "") {
				$('#reportList > tbody:last-child').append(createApplyFormDetail);
				resetSelectorAndOrderText('applyProcessOrderText','applyProcessTable');
				addRowGroupEvent("applySysGroup");
			} else {
				alert("<s:message code='form.process.managment.add.page.function.alert.1' text='請選擇部門科別' />");//請選擇部門科別
			}
			
			addWorkProject("applyProcessTable");
		}
		
		//新增審核流程
		if (add1 == true) {
			let division = $("#division").val();
			if(division != "") {
				$('#reportList1 > tbody:last-child').append(createReviewFormDetail);
				resetSelectorAndOrderText('reviewProcessText','reviewProcessTable');
				addRowGroupEvent("reviewSysGroup");
			} else {
				alert("<s:message code='form.process.managment.add.page.function.alert.1' text='請選擇部門科別' />");//請選擇部門科別
			}
			addWorkProject("reviewProcessTable");
		}
	});
	setupColumn();
});

<%-- 表單類型動態切換對應新增頁面 --%>
$("#formType").change(function() {
	let formType = $(this).val();
	SendUtil.href("/formProcessManagment/dispatcherPageByFormType",formType);
});

<%-- 科別下拉選單事件綁定 --%>
$("#division").change(function() {
	changeDivisionEvent();
	resetAllRowData();
	addWorkProject();
});

// 平行會辦的單選按鈕事件
$(document).on("change", 'input[id="isParallel"]' , function() {
	let thisParallel = $(this);
	let otherParallels = $('input[id="isParallel"]').not(this);
	let thisButton = thisParallel.parent('td').next().find('button');
	let otherButtons = otherParallels.parent('td').next().find('button');
	
	thisButton.show();
	otherButtons.hide();
	thisParallel.prop('chekced', true);
	otherParallels.prop('checked', false);
});

function levelWordingsButton (btn) {
	let $btn = $(btn);
	let isCurrentLevel, levels = [];
	let rows = $btn.closest('tbody').find('tr');
	let currentWordings = $btn.siblings('input');
	let currentLevel = $btn.closest('tr').find('td .currentLevel').text();
	
	if (currentWordings.val()) {// 已存在的資料
		
		var count = 0;
		
		$.each(ObjectUtil.parse(currentWordings.val()), function () {
			
			if(rows.length == count){//減關卡
				return; 
			}
			
			if(this.wordingLevel == currentLevel){
				this.wording = '當前關卡';
			}
			this.isCurrentLevel = currentLevel == this.wordingLevel;
			this.level = this.wordingLevel;
			levels.push(this);
			count++;
		});
		
		
		$.each(rows, function () {//補關卡
			if(count > 0){
				count--;
				return;
			}
	        isCurrentLevel = currentLevel == $(this).find('td .currentLevel').text();
			
			let o = {
				isCurrentLevel : isCurrentLevel,
				level : $(this).find('td .currentLevel').text(),
				wording : isCurrentLevel ? '當前關卡' : ''
			};
			
			levels.push(o);
		});
		
	} else {
		$.each(rows, function () {// 全新的資料
	        isCurrentLevel = currentLevel == $(this).find('td .currentLevel').text();
			
			let o = {
				isCurrentLevel : isCurrentLevel,
				level : $(this).find('td .currentLevel').text(),
				wording : isCurrentLevel ? '當前關卡' : ''
			};
			
			levels.push(o);
		});
	}
	
	let stages = {
		detailId : '',
		levels : levels,
		currentLevel : currentLevel
	};
	
	LevelWordingsDialog.show(stages, function (stages) {
		currentWordings.val(ObjectUtil.stringify(stages));
	});
}

function parallelButton () {
	ParallelDialog.show(function (checkeds) {
		let itemList = [];
		$.each(checkeds, function (i, checkedItem) {
			itemList.push(checkedItem.value);
		});
		$('input#parallels').val(itemList.join(','));
	});
}

<%-- 復原所有資料 --%>
function resetAllRowData() {
	let pageType = $('input#pageType').val();
	
	if (pageType == 'add') {
		$("#reviewProcessTable").find(".sysGroup").val("");
		$("#applyProcessTable").find(".sysGroup").val("");
		$(".nextLevel").val("1");
		$(".backLevel").val("1");
		$("input[type=checkbox]").prop("checked",false);
	}
}

<%-- 若為IsEnable狀態,則無法進行流程修改 --%>
function setupColumn(){
	if("${isEnable}" == "Y"){
		$('input[type="text"]').prop("readonly", true);
		$('input[type="checkbox"]').prop("disabled", "disabled");
		$('input[type="radio"]').prop("disabled", "disabled");
		$('select').attr("disabled", "disabled");
		$(":button").hide();
		$('#goBackBtn').show();
		$(".iconx-save").hide();
		alert('<s:message code="form.process.managment.edit.page.enabled.process.can.not.modify" text="啟用狀態的表單流程無法被編輯,需先將其設定為停用狀態" />');
	} 
}


<%-- 根據流程的新增或刪除,重設所有流程順序的數字以及下拉選單裡面動態產生的值 --%>
function resetSelectorAndOrderText(textClass,targetTableId) {
	resetOrderText(textClass);
	resetSelectorCountByRows(targetTableId,"nextLevel",true);
	resetSelectorCountByRows(targetTableId,"backLevel",false);
	
	//若為工作單或工作會辦單的話,執行下列funciton(只有工作單/工作會辦單才有)
	let formType = "${formType}";
	if(formType == 8 || formType == 9) {
		lockFirstIsWorkLevelCheckbox();
	}
	
}

<%-- 根據ClassName 重設流程順序的值 --%>
function resetOrderText(className) {
	let count = 1;
	let target = $("." + className + "");
	target.each(function(index,element) {
		$(this).text(count++);
	});
}

<%-- 根據申請/審核流程的數量 自動調整可選擇的關卡數量--%>
function resetSelectorCountByRows(tableClassName,selectorClassName,isDesc) {
	let target = $("#" + tableClassName + "").find("." + selectorClassName + "");
	
	$.each(target,function(index,element) {
		//先清空底下的所有option
		$(this).empty();
		//TODO 視情況再打開不跳關的選項
		//$(this).append("<option value=''></option>");
		
		//有多少個selector就代表有多少個流程
		let selectorCount = target.length - index;
		
		//如果是Desc的話,代表option的產生邏輯是從row的總量來計算,扣掉當前forEach的index,得出該Selector可產生的option數量
		if(isDesc == true ) {
			//這邊要注意,forloop 從1開始,且結束條件只能比selectorCount小
			for(var i=1; i < selectorCount; i++) {
				let nextLevelStr = "<s:message code='form.process.managment.add.page.function.next.level' text='下{0}關' />";
				nextLevelStr = nextLevelStr.replace("{0}",i);
				$(this).append("<option value='" + i + "'>" + nextLevelStr + "</option>");
			}
		} else {
			//若不是的話,直接按照以index的數量為基準,產生option
			if(index > 0) {//忽略掉第一筆,因為第一筆無法往上退件
				let row = 1;
				for(var i= target.length; i > selectorCount; i--) {
					let backLevelStr = "<s:message code='form.process.managment.add.page.function.back.level' text='退{0}關' />";
					backLevelStr = backLevelStr.replace("{0}",row)
					$(this).append("<option value='" + row + "'>" + backLevelStr + "</option>");
					row++;
				}
			}
		}
	});
}


<%-- 取得群組下拉選單資訊 --%>
function getSelectorGroupData() {
	let value = $("#division").val();

	if(value != "") {
		value = value.split("-");
		
		let params = {};
		params.departmentId = value[0];
		params.division = value[1];
		params.formType = $("#formType").val();
	
		SendUtil.post('/formProcessManagment/getGroupInfoByDepartmentIdAndDivision', params, function (data) {
			applyGroupSelectorDatas = data;//作為全域變數,用於單獨新增ROW的時候
		});
	
		SendUtil.post('/formProcessManagment/getGroupInfoByDepartmentIdAndDivisionWithManagment', params, function (data) {
			reviewGroupSelectorDatas = data;//作為全域變數,用於單獨新增ROW的時候
		});
	}
}

<%-- 根據科別下拉選單的選擇,同步異動所有群組下拉選單 --%>
function changeDivisionEvent() {
	let value = $("#division").val();
	let pageType = $('input#pageType').val();

	if (pageType == 'add') {
		$(".applySysGroup").empty().append("<option value=''><s:message code='form.process.managment.add.page.review.process.choose.process.group' text='請選擇流程群組' /></option>");
		$(".reviewSysGroup").empty().append("<option value=''><s:message code='form.process.managment.add.page.review.process.choose.process.group' text='請選擇流程群組' /></option>");

		if(value != "") {
			value = value.split("-");
			
			let params = {};
			params.departmentId = value[0];
			params.division = value[1];
			params.formType = $("#formType").val();
			
			SendUtil.post('/formProcessManagment/getGroupInfoByDepartmentIdAndDivision', params, function (data) {
				applyGroupSelectorDatas = data;//作為全域變數,用於單獨新增ROW的時候
				
				$.each(applyGroupSelectorDatas,function(index,element) {
					let optionStr = "<option value='{VALUE}'>{WORDING}</option>";
						optionStr = optionStr.replace("{VALUE}",element.value).replace("{WORDING}",element.wording);
						$(".applySysGroup").append(optionStr);
				});
			},null,false);
			
			SendUtil.post('/formProcessManagment/getGroupInfoByDepartmentIdAndDivisionWithManagment', params, function (data) {
				reviewGroupSelectorDatas = data;//作為全域變數,用於單獨新增ROW的時候
				
				$.each(reviewGroupSelectorDatas,function(index,element) {
					let optionStr = "<option value='{VALUE}'>{WORDING}</option>";
						optionStr = optionStr.replace("{VALUE}",element.value).replace("{WORDING}",element.wording);
						$(".reviewSysGroup").append(optionStr);
				});
			},null,false);
		}
	}
	
	$('[custom="parallelCols"]').toggle(isSpDivision());
}

<%-- 編輯頁面專屬 - 表單流程內容初始化 --%>
function formProcessDataInit() {
	let jsonStr = '${formProcessManagmentFormVoJsonStr}';
	jsonStr = jsonStr.replaceAll('\"\[', '\[');
	jsonStr = jsonStr.replaceAll('\]\"', '\]');

	if("" != jsonStr) {
		let formProcessManagmentFormVO = ObjectUtil.parse(jsonStr);
		processId = formProcessManagmentFormVO.processId;//取得ProcessId,並且作為全域變數
	
		//建立跳關或退關下拉選單
		resetSelectorCountByRows("applyProcessTable","nextLevel",true);
		resetSelectorCountByRows("applyProcessTable","backLevel",false);
		resetSelectorCountByRows("reviewProcessTable","nextLevel",true);
		resetSelectorCountByRows("reviewProcessTable","backLevel",false);
		
		$("#formType").val(formProcessManagmentFormVO.formType);
		$("#division").val(formProcessManagmentFormVO.division).change();
		$("#processName").val(formProcessManagmentFormVO.processName);
		
		//然後還要取得所有群組 跳關 退關的selector,把各自的value塞進去
		let applyNextLevelSelector = $("#applyProcessTable").find(".nextLevel");//申請流程 跳X關selector
		let applyBackLevelSelector = $("#applyProcessTable").find(".backLevel");//申請流程 退X關selector
		let applyWordingsSelector = $("#applyProcessTable").find(".levelWordings");//關卡文字
		
		let reviewNextLevelSelector = $("#reviewProcessTable").find(".nextLevel");//審核流程 跳X關selector
		let reviewBackLevelSelector = $("#reviewProcessTable").find(".backLevel");//審核流程 退X關selector
		let reviewWordingsSelector = $("#reviewProcessTable").find(".levelWordings");//關卡文字

		$.each(reviewWordingsSelector,function(index) {
			let levelWordings = formProcessManagmentFormVO.reviewProcessList[index].levelWordings;
			$(this).val(ObjectUtil.stringify(levelWordings));	
		});
		
		$.each(applyWordingsSelector,function(index) {
			let levelWordings = formProcessManagmentFormVO.applyProcessList[index].levelWordings;
			$(this).val(ObjectUtil.stringify(levelWordings));	
		});
		
		$.each(applyNextLevelSelector,function(index) {
			let nextLevel = formProcessManagmentFormVO.applyProcessList[index].nextLevel;
			$(this).val(nextLevel);	
		});
	
		$.each(reviewNextLevelSelector,function(index) {
			let nextLevel = formProcessManagmentFormVO.reviewProcessList[index].nextLevel;
			$(this).val(nextLevel);
		});
		
		$.each(applyBackLevelSelector,function(index) {
			let backLevel = formProcessManagmentFormVO.applyProcessList[index].backLevel;
			$(this).val(backLevel);
		});
	
		$.each(reviewBackLevelSelector,function(index) {
			let backLevel = formProcessManagmentFormVO.reviewProcessList[index].backLevel;
			$(this).val(backLevel);
		});
	
		getSelectorGroupData();
	}
}

<%-- 新增流程,單獨植入群組Selector option資訊 --%>
function addRowGroupEvent(className) {
	let sysGroupSelectors =  $("." + className);
	let targetSelectorDatas = {};
	
	if("applySysGroup" == className) {
		targetSelectorDatas = applyGroupSelectorDatas
	} else if("reviewSysGroup" == className) {
		targetSelectorDatas = reviewGroupSelectorDatas
	} else {
		return;
	}
	
	$.each(sysGroupSelectors,function() {
		let target = $(this);
		
		if(target.has('option').length == 0) {//如果該option的長度為0,代表是新增的欄位,若沒有這個判斷,下拉選單的資料會無限增加
			//先插入請選擇群組的初始選項
			target.append("<option value=''><s:message code='form.process.managment.add.page.review.process.choose.process.group' text='請選擇流程群組' /></option>");
					
			$.each(targetSelectorDatas,function(index,element) {
				let optionStr = "<option value='{VALUE}'>{WORDING}</option>";
					optionStr = optionStr.replace("{VALUE}",element.value).replace("{WORDING}",element.wording);
					target.append(optionStr);
			});
		}
	});
}

function addWorkProject (targetTableId) {
	let targetWorkProjectRow = $("#" + targetTableId).find("select.workProject");

	SendUtil.get('/html/getDropdownList', 'WORK_LEVEL', function (options) {
		$.each(targetWorkProjectRow,function(index, element) {
			let target = $(this);
			if($(this).find("option").length == 0) {
				target.append("<option value=''><s:message code='global.select.please.choose' text='請選擇'/></option>");
				
				$.each(options, function(index,element) {
					let optionStr = "<option value='{VALUE}'>{WORDING}</option>";
					optionStr = optionStr.replace("{VALUE}",element.value).replace("{WORDING}",element.wording);
					target.append(optionStr);
				})
			}
		});
	});
}

/**
 * 回查詢頁面
 */
function goBackSearchPage() {
	SendUtil.href("/formProcessManagment/");
}

/**
 * 鎖定申請流程第一關的作業關卡checbox(工作單/工作會辦單專用)
 */
function lockFirstIsWorkLevelCheckbox() {
	let applyProcess = $("#applyProcessTable").find("tr");
	$.each(applyProcess,function(index,element) {
		if(0 == index) {
			$(this).find("input#isWorkLevel").attr("disabled",true).prop("checked",false);
		} else {
			$(this).find("input#isWorkLevel").attr("disabled",false);
		}
	});
}

/**
 * 判斷是否為會辦單流程
 */
function isCountersign() {
	if("Y" == countersign) {
		return true;
	} else {
		return false;
	}
}

function isSpDivision () {
	let division = $('select#division').val();
	return division.indexOf("SP") != -1;
}
</script>
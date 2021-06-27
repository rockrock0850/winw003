﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script>//# sourceURL=signingDialog.js
/**
 * 開啟送簽關卡對話視窗
 */
var SigningDialog = function () {
	var dialog = 'div#signingDialog';
	var nextLevelTable = dialog + ' table#nextLevelSigningList';
	var backLevelTable = dialog + ' table#backLevelSigningList';
	
	var show = function (reqData, buttonEvents, type) {
		let levels = {};
		levels.nextLevelItem = createDataTable(reqData, true); //沿用
		levels.backLevelItem = createDataTable(reqData, false);
		alertLevelUI(levels, buttonEvents, type);
		$('input#isKnowledgeable').prop('checked', reqData.isSuggestCase == 'Y');
	}
	
	function alertLevelUI (levels, buttonEvents, type) {
		let isReview = headForm.verifyType.value == 'REVIEW';
		let dialogOpts = {
			width: '700px',
			maxWidth: '1000px',
			resizable: false,
			open: function (event, ui) {
				//進關
				if (!TableUtil.isDataTable(nextLevelTable)) {
					TableUtil.init(nextLevelTable, levels.nextLevelItem.options);
				} else {
					TableUtil.reDraw($(nextLevelTable).DataTable(), levels.nextLevelItem.levels);
				}
				//退關
				if (!TableUtil.isDataTable(backLevelTable)) {
					TableUtil.init(backLevelTable, levels.backLevelItem.options);
				} else {
					TableUtil.reDraw($(backLevelTable).DataTable(), levels.backLevelItem.levels);
				}
				
				if (isReview) { //表單進到審核流程再顯示checkbox
					$('fieldset#knowledgeable').toggle(isKnowledgeableDisplay());
				}
			},
			close: function () {
				TableUtil.deleteTable(nextLevelTable);
				TableUtil.deleteTable(backLevelTable);
			},
			buttons : createButton(buttonEvents, type)
		};
		
		DialogUtil.show(dialog, dialogOpts);
		HtmlUtil.clickRowRadioEvent(nextLevelTable + ' tbody');
		HtmlUtil.clickRowRadioEvent(backLevelTable + ' tbody');
	}
	
   /**
	* 建立Datatable初始化資料
	*/
	function createDataTable (reqData, isNextLevel) {
	    let tempData = $.extend({}, reqData);
	    tempData.changeVerifyType = false;
	    tempData.isNextLevel = isNextLevel;
	   	
		let levels = getLevelList(tempData);
		let isChangeVerifyType = isEmptyLevel(levels);
		let isWithoutWatching = tempData.isWithoutWatching;
		let isInc = ValidateUtil.formInfo().isInc(tempData);
		let isChg = ValidateUtil.formInfo().isChg(tempData);
		let isApJob = ValidateUtil.formInfo().isApJob(tempData);
		let isApply = ValidateUtil.formInfo().isApply(tempData);
		let isReview = ValidateUtil.formInfo().isReview(tempData);
		let isParallel = ValidateUtil.formInfo().isParallel(tempData);
		
		if (isNextLevel) {// 進關
			if (isChangeVerifyType) {
				if (isApply) {
					tempData.verifyLevel = 0;
					tempData.processOrder = 0;
					tempData.verifyType = "REVIEW";
					tempData.changeVerifyType = false;
				} else {
					tempData.lastLevel = true;
				}
				
				levels = getLevelList(tempData);
			} 
		
			if (isChg && isApply) {
				levels = chgSpacialProcess(levels, tempData);
			} else if (isInc && isReview) {
				levels = incSpacialProcess(levels, tempData);
			} else if (isApJob && isReview && isWithoutWatching) {
				levels = apJobSpacialProcess(levels, tempData);
			} else if (isParallel) {
				levels = parallelProcess(tempData);
			}
		} else {// 退關
			if (isChangeVerifyType) {
				if (isReview) {// 是否為審核流程
					tempData.verifyType = "APPLY";
					tempData.changeVerifyType = true;
					tempData.verifyLevel= 99;// TODO 釐清為什麼這邊是99
				} else {// 加上第一關的註記(與最後一關共用相同變數名稱)
					tempData.lastLevel = true;
				}
				
				levels = getLevelList(tempData);
			}
		}
		
		tempData = {};
		tempData.levels = levels;
		tempData.options = 
			createDatatableOption(levels, isNextLevel);
		
		return tempData;
	}
	
   /**
   	* 取得DataTable Option
   	*/
	function createDatatableOption(levels, isNextLevel) {
		let radio = '<input type="radio" name="detailId" value="{0}" isNextLevel={2} {1} />';
		let tableOpts = {
			data: levels,
			columnDefs: [
				{orderable: false, targets: []}, 
				{
					targets: [0], 
					data: 'detailId', 
					title: "功能", 
					className: 'dt-center', 
					width: '5%',
					render: function (data, type, row, meta) {
						if (isNextLevel) {
							return StringUtil.format(radio, data, meta.row == 0 ? 'checked' : '', isNextLevel);
						} else {
							return StringUtil.format(radio, data, '', isNextLevel);
						}
					}
				},
				{targets: [1], title: '順序', data: 'processOrder', className: 'dt-center hidden processOrderClass', width: '5%'}, 
				{targets: [2], title: '流程關卡', data: 'groupName', className: 'dt-center', width: '20%', render: function (data, type, row, meta) {
					return row.wording ? row.wording : row.groupName;
				}}, 
				{targets: [3], title: '', data: 'groupId', className: 'dt-center hidden groupIdClass'}
			]
		};
		
		return tableOpts;
	}
   
   /**
   	* 取得關卡資訊
   	*/
   function getLevelList(reqData) {
	   let levels = [];
	   
	   SendUtil.post('/html/getSigningList', reqData, function (response) {
		   levels = response;
	   }, ajaxSetting);
	   
	   return levels;
   }
   
   /**
   	* 按鈕顯示事件(判斷為簽核,作廢,還是直接結案)
   	* 同樣,也需要根據按鈕的顯示邏輯,隱藏對應的HTML欄位
   	*/
   function createButton (buttonEvents, type) {
		let buttons = {}
	   	let isShowBackToPic = $("input#isShowBackToPic").is(":checked");// 只有事件單才有isShowBackToPic
	   	
		if("SIGNING" == type) {
			buttons["確定"] = function () {
				HtmlUtil.lockSubmitKey(true);
				if (buttonEvents.confirm) {
					buttonEvents.confirm();
				}
			};
			
			buttons["取消"] = function () {
				if (buttonEvents.cancel) {
					TableUtil.deleteTable(nextLevelTable);
					TableUtil.deleteTable(backLevelTable);
					DialogUtil.close();
					buttonEvents.cancel();	
				}
			};
			
			if(isShowBackToPic) {
				buttons["退回經辦"] = function () {
					HtmlUtil.lockSubmitKey(true);
					if (buttonEvents.backToPic) {
						buttonEvents.backToPic();
					}
				}
			} 
		} else if("DELETE" == type) {
			buttons["作廢"] = function () {
				if (buttonEvents.deprecated) {
					buttonEvents.deprecated();
				}
			};
		} else if ("CLOSE_FORM" == type) {
			var words = "直接結案";
			if (ValidateUtil.loginRole().isVice()) {
				words = "代科長審核";
			} else if (ValidateUtil.loginRole().isDirect1()) {
				words = "代協理審核";
			}
			buttons[words] = function () {
				if (buttonEvents.closeForm) {
					buttonEvents.closeForm();
				}
			};
		}
	   	$("div#signingDiv").toggle("SIGNING" == type);
		
		return buttons;
   }

	/* 
	 *  變更單的特殊邏輯
	 * 	變更單申請倒數第二關固定為副科長, 若需代科長審核, 需要進行跳關形式的跨流程進關。
	 * 	但系統未設計上述邏輯, 因此暫時在這邊加上跨流程的固定邏輯。
	 * 	實際設計方針 : 
	 * 		1. 表單流程設定需要調整為能夠跨流程(申請->審核)跳關下拉選項
	 *		2. 表單進/退關的時候要判斷是否在該流程已撈不到關卡卻還有跳關關數
	 * 		3. 若還有關數則須再往對應的流程(申請/審核)撈取關卡資訊
	 *		4. 若無則實施一般進/退關流程即可
	 *	因為牽扯範圍廣泛, 若客戶新增CR才會另行開發
	 */
	function chgSpacialProcess (levels, tempData) {
		let tempLevels = [];
		let isVice = ValidateUtil.loginRole().isVice();
		let isChief = ValidateUtil.loginRole().isChief();
		let isChg = ValidateUtil.formInfo().isChg(tempData);
		
		if (isChief) {
			if (isJumpReviewLast(tempData)) {
				tempLevels.push(getCrossLevel('送交經辦', tempData));
			} else {
				tempLevels.push(getCrossLevel('呈核給副理', tempData));
			}
		} else if (isVice && $.isArray(levels)) {
			let stage = levels[0];
			stage.groupName = '呈核科長';
			tempLevels.push(stage);
			tempLevels.push(getCrossLevel('代科長核准', tempData));
		}
		
		return tempLevels.length > 0 ? tempLevels : levels;
	}
	
	/*
	 * 事件單的特殊邏輯
	 * 審核流程第1關固定為副科長；第2關固定為科長；第3關固定為副理, 
	 * 且表單流程設定裡面的審核流程必定要將跳關關數開到最大, 因為這樣程式才抓的到關卡資料。
	 * 1. 副科長 : 固定將進關清單寫成"呈核給科長"跟"代科長呈核副理"
	 * 2. 科長 : 固定將進關清單寫成"呈核副理"
	 */
	function incSpacialProcess (levels, tempData) {
		let chief, direct1;
		let tempLevels = [];
		let isVice = ValidateUtil.loginRole().isVice();
		let isChief = ValidateUtil.loginRole().isChief();
		
		if (isVice) {
			chief = levels[0];
			direct1 = levels[1];
			
			chief.groupName = '呈核科長';
			tempLevels.push(chief);

			direct1.groupName = '代科長呈核副理';
			tempLevels.push(direct1);
		} else if (isChief) {
			direct1 = levels[0];
			direct1.groupName = '呈核副理';
			tempLevels.push(direct1);
		}
		
		return tempLevels.length > 0 ? tempLevels : levels;
	}
	
	// 如果AP工作單有勾選「送交監督人員」時，REIVEW_經辦按送出，才會顯示關卡視窗；反之，直接結案。
	function apJobSpacialProcess (levels, tempData) {
		return [getCrossLevel('直接結案', tempData)];
	}
	
	function parallelProcess (tempData) {
		return [getCrossLevel('平行會辦通過', tempData)];
	}
	
	function getCrossLevel (groupName, tempData) {
		return {
			groupId: '',
			processOrder: 0,
			groupName: groupName,
			detailId: tempData.detailId
		}
	}
	
	function isKnowledgeableDisplay () {
		let info = form2object('headForm');
		return (ValidateUtil.loginRole().isVice() || ValidateUtil.loginRole().isChief()) && ValidateUtil.formInfo().isQ(info)
	}
	
	function isEmptyLevel (levels) {
		return (levels && levels.length == 0);
	}
   
	return {
		show : show
	};
}();
</script>


<div id="signingDialog" style="display: none;">
	<div id="signingDiv">
		<fieldset>
			<legend>送簽關卡選擇</legend>
			<table id="nextLevelSigningList" class="display collapse cell-border">
				<thead></thead>
				<tbody></tbody>
			</table>
		</fieldset>
		<fieldset>
			<legend>退件關卡選擇</legend>
			<table id="backLevelSigningList" class="display collapse cell-border">
				<thead></thead>
				<tbody></tbody>
			</table>
		</fieldset>
	</div>
	<fieldset id='knowledgeable' style='display:none'>
		<%-- 建議加入處理方案 等同 是否加入知識庫--%>
		<label><input id='isKnowledgeable' name='isKnowledgeable' type='checkbox' /><font color="red">建議加入「處理方案」?</font></label>
	</fieldset>
	<fieldset>
		<legend><s:message code='form.verify.verifyComment' text='簽核意見'/></legend>
		<form id='verifyForm'>
			<table class="grid_list">
				<tr>
					<td><textarea id='verifyComment' class='signing-remark' name='verifyComment' rows="3"></textarea></td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
//# sourceURL=ValidateUtil.js

/**
 * 資料驗證共用工具
 * 
 * @author AndrewLee
 */
var ValidateUtil = function () {
	
	/**
	 * 前端畫面欄位/按鈕權限控制
	 */
	var authView = function (info) {
		$('form#headForm').find('select').attr('disabled', true);
		SendUtil.post('/commonForm/getEditableCols', info, function (response) {
			$('li#tabLog').show();
			$('li#tabProgram').show();
			$('li#tabCheckLog').show();
			$('li#tabFileList').show();
			$('li#tabLinkList').show();

			HtmlUtil.enableFunctionButtons(null);
			HtmlUtil.enableFunctionButtons(response.buttons, true);
			HtmlUtil.disableElements(response.disabledColumns, true);
			HtmlUtil.disableElements(response.enabledColumns, false);
			adminCListViewCtrl();

			// 檢查當前頁簽的按鈕欄上沒有按鈕的話就把整欄隱藏
			$('div[id$="Buttons"]').toggle(
					$('div[id$="Buttons"] button').is(":visible"));
		}, ajaxSetting);
	}
	
	var adminCListViewCtrl = function () {
		let isAdmin = loginRole().isAdmin();
		let cList = $('table#cList input[type="checkbox"]');
		
		if (isAdmin) {
			$.each(cList, function (i, c) {
				$(c).attr('disabled', $(c).is(':checked'));
			});
		}
	}
	
	/**
	 * 判斷登入者隸屬於哪個群組
	 */
	var loginRole = function () {
		
		var userId = loginUserInfo.userId;
		var groupId = loginUserInfo.groupId;
		var level = loginUserInfo.authorLevel;
		
		/**
		 * 系統管理者
		 */
		var isAdmin = function () {
			return 'admin' == userId;
		}
		
		/**
		 *  經辦
		 */
		var isPic = function () {
			return level <= 1;
		}

		/**
		 *  科長
		 */
		var isChief = function () {
			return (groupId.indexOf('VSC') == -1 && groupId.indexOf('SC') != -1);
		}

		/**
		 *  副科長
		 */
		var isVice = function () {
			return groupId.indexOf('VSC') != -1;
		}
		
	    /**
	     * 副理
	     */
		var isDirect1 = function () {
			return groupId.indexOf('Direct1') != -1;
		}
		
		/**
	     * 協理
	     */
		var isDirect2 = function () {
			return groupId.indexOf('Direct2') != -1;
		}
		
		return {
			isPic : isPic,
			isVice : isVice,
			isAdmin : isAdmin,
			isChief : isChief,
			isDirect1 : isDirect1,
			isDirect2 : isDirect2
		}
	};
	
	/**
	 * 傳入表頭資訊, 判斷是哪種狀態、種類或流程
	 */
	var formInfo = function () {
		
		/**
		 * 已成單落入資料庫
		 */
		var isCreated = function (formInfo) {
			return formInfo.formId ? true : false;
		}
		
		/**
		 * AP工作單
		 */
		var isApJob = function (formInfo) {
			return formInfo.formClass == 'JOB_AP';
		}
		
		/**
		 * 變更單
		 */
		var isChg = function (formInfo) {
			return formInfo.formClass == 'CHG';
		}
		
		/**
		 * 問題單
		 */
		var isQ = function (formInfo) {
			return formInfo.formClass == 'Q';
		}
		
		/**
		 * 需求單
		 */
		var isSr = function (formInfo) {
			return formInfo.formClass == 'SR';
		}
		
		/**
		 * 事件單
		 */
		var isInc = function (formInfo) {
			return formInfo.formClass == 'INC';
		}
		
		/**
		 * 申請流程
		 */
		var isApply = function (formInfo) {
			return formInfo.verifyType == 'APPLY';
		}

		/**
		 * 審核流程
		 */
		var isReview = function (formInfo) {
			return formInfo.verifyType == 'REVIEW';
		}
		
		/**
		 * 平行會辦狀態
		 */
		var isParallel = function (formInfo) {
			return formInfo.isParallel == 'Y';
		}
		
		return {
			isQ : isQ,
			isSr : isSr,
			isChg : isChg,
			isInc : isInc,
			isApJob : isApJob,
			isApply : isApply,
			isReview : isReview,
			isCreated : isCreated,
			isParallel : isParallel
		}
	}
	
	/**
	 * 傳入URL以及FormVo物件,驗證欄位資料
	 */
	var formFields = function(url,info) {
		let verifyResult = '';
		
		SendUtil.post(url, info, function (resData) {
			if(!resData.isSuccess) {
				verifyResult = resData.errorMsg;
			}
		}, {async: false});

		return verifyResult;
	}
	
	return {
		authView : authView,
		formInfo : formInfo, 
		loginRole : loginRole,
		formFields : formFields,
		adminCListViewCtrl : adminCListViewCtrl
	}
}();
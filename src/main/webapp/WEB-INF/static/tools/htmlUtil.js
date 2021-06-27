//# sourceURL=HtmlUtil.js

/**
 * 統一紀錄前端共用操作邏輯
 * @author adam.yeh
 */
var HtmlUtil = function () {

	var tempData = {};
	var oriWidth, newWidth, breadcrumbs, latest, isPop;

	/**
	 * 暫存表單當前頁簽內容的資料
	 * P.S. 預設抓取第一張基數表單內容
	 */
	var temporary = function () {
		let id = '';
		let formId = $('form:odd').first().attr('id');
		let viewData = form2object(formId);
		
		if(tempData[formId] &&
				tempData[formId].id){
		    id = tempData[formId].id;
		}
		
		viewData.id = id ? id : '';
		tempData[formId] = viewData;
		
		// 暫存日誌
		if (TableUtil.isDataTable('#logList')) {
			temporaryLogList();
		}
		
		// 暫存衝擊分析
		if ($('#impactForm').is(":visible")) {
			temporaryImpactList();
		}
		
		// 暫存DB變更頁簽內的Segment清單
		if (TableUtil.isDataTable('#segmentTable')) {
			temporarySegmentTable();
		}
		
		console.log(tempData);
	}
	
	/**
	 * 依據傳入陣列啟用/取消元素編輯狀態
	 */
	var disableElements = function (ary, isDisabled) {
		$.each(ary, function (i, name) {
			$(name).prop('disabled', isDisabled);
			if ($(name).next('button').length > 0) {
				$($(name).next('button')).prop('disabled', isDisabled);
			}
	        $(name).multiselect('reload' );
	        $(name).multiselect('disable', isDisabled);
		});
	}
	
	/**
	 * 依據傳入陣列啟用/取消功能按鈕編輯狀態
	 * 如果不設定陣列參數的話預設會隱藏所有按鈕ID以Button結尾的元件
	 */
	var enableFunctionButtons = function (ary, isEnabled) {
		if (!ary || ary.length == 0) {
			$('button[id$="Button"]').toggle(false);
			return;
		}
		
		$.each(ary, function (i, name) {
			$(name).toggle(isEnabled);
		});
	}
	
	/**
	 * 存入麵包屑
	 */
	var putBreadcrumb = function (href, name) {
		breadcrumbs = SessionUtil.get('breadcrumb') ? 
				ObjectUtil.parse(SessionUtil.get('breadcrumb')) : [];
		
		var breadcrumb = {
			href: href,
			name: name
		};
		
		breadcrumbs.push(breadcrumb);
		SessionUtil.set('breadcrumb', breadcrumbs);
	}
	
	/**
	 * 取出最後一員麵包屑
	 */
	var popBreadcrumb = function () {
		breadcrumbs = SessionUtil.get('breadcrumb') ? 
				ObjectUtil.parse(SessionUtil.get('breadcrumb')) : [];
		latest = breadcrumbs[breadcrumbs.length-1];
		isPop = isPopBreadcrumb(latest);
		
		if (isPop) {
			latst = breadcrumbs.pop();
			SessionUtil.set('breadcrumb', breadcrumbs);
		}
		
		return latest;
	}
	
	/**
	 * 沖刷出所有麵包屑
	 */
	var flushBreadcrumbs = function () {
		breadcrumbs = SessionUtil.get('breadcrumb') ? 
				ObjectUtil.parse(SessionUtil.get('breadcrumb')) : [];
		SessionUtil.set('breadcrumb', []);
		
		return breadcrumbs;
	}
	
	/**
	 * 取得所有麵包屑清單
	 */
	var getBreadcrumbs = function () {
		breadcrumbs = SessionUtil.get('breadcrumb') ? 
				ObjectUtil.parse(SessionUtil.get('breadcrumb')) : [];
		
		return breadcrumbs;
	}
	
	/**
	 * 初始化多選下拉
	 */
	var initMultiSelect = function (e, opt) {
		let el = e ? e : 'select[multiple]';
		let def = {
            columns: 1,
            minHeight: 200, 
            selectAll: false,
            texts: {
				selectAll:'全選',
				unselectAll:'取消',
				placeholder:'請選擇'
			}
        };
		
        $(el).multiselect($.extend(def, opt));
        $(el).multiselect('disable', true);
	}
	
	/**
	 * 動態加載多選下拉選單內容
	 * selector JQuery selector statement
	 * contents Data List
	 */
	var multiSelect = function (selector, contents) {
		$.each($(selector).find('option'), function () {
			if ($(this).val()) {
				$(this).remove();
			}
		});
		
		$.each(contents, function () {
			var option = '<option value=' + this.value + '>' + this.wording + '</options>';
			$(selector).append(option);
		});

        $(selector).multiselect('reload' );
        $(selector).multiselect('disable', false);
	}
	
	/**
	 * 動態加載下拉選單內容
	 * selector JQuery selector statement
	 * contents Data List
	 */
	var singleSelect = function (selector, contents) {
		$.each($(selector).find('option'), function () {
			if ($(this).val()) {
				$(this).remove();
			}
		});
		
		$.each(contents, function () {
			var option = '<option value=' + this.value + '>' + this.wording + '</options>';
			$(selector).append(option);
		});
	}

	/**
	 * 鎖定Enter 空格以及ESC鍵
	 */
	var lockSubmitKey = function (lock) {
		if (lock) {
			$(document).keydown(function (e) {
				if (e.keyCode == 32 || e.keyCode == 13 || e.keyCode == 27) {
					return false;
				}
			});
		} else {
			$(document).unbind("keydown");
		}
	}
	
	/**
	 * 初始化tab頁簽
	 */
	var initTabs = function () {
		$('.nav-tabs > li > a').unbind('click').click(function (event) {
			//stop browser to take action for clicked anchor
			event.preventDefault();
						
			//get displaying tab content jQuery selector
			var activeTabSelector = $('.nav-tabs > li.active > a').attr('href');					
						
			//find actived navigation and remove 'active' css
			var activedNav = $('.nav-tabs > li.active');
			activedNav.removeClass('active');
						
			//add 'active' css into clicked navigation
			$(this).parents('li').addClass('active');
						
			//hide displaying tab content
			$(activeTabSelector).removeClass('active');
			$(activeTabSelector).addClass('hide');
						
			//show target tab content
			var targetTabSelector = $(this).attr('href');
			$(targetTabSelector).removeClass('hide');
			$(targetTabSelector).addClass('active');
		});
	}

	var saveOriWidth = function (width) {
		if (!$('main').hasClass('wide')) {
			oriWidth = width;
		}
	}
	
	var saveNewWidth = function (width) {
		if (!newWidth && $('main').hasClass('wide')) {
			newWidth = width;
		}
	}
	
	var adjustTableWidth = function () {
		var width = !$('main').hasClass('wide') ? oriWidth : newWidth;
		$('.dataTable').attr('style', width);
		$('.dataTables_wrapper').attr('style', width);
	}
	
	var emptySelect = function(id, isHard) {
		let defaultOpt = $(id + ' option').first();
		
		if (isHard) {
			$(id).empty();
		} else {
			$(id).empty().append(defaultOpt);
		}
	}
	
	// 針對指定表格的每行註冊點擊事件並將radio勾起來
	var clickRowRadioEvent = function (clickBody) {
		var isChecked;
		
		$(clickBody).unbind('click').on('click', 'tr', function () {
			isChecked = $(this).find('input[type="radio"]').is(':checked');
			
			if (isChecked) {
				return;
			}
			
			$(this).find('input[type="radio"]').prop('checked', !isChecked);
		});
	}

	var clickRowCheckboxEvent = function (clickBody) {
		let isChecked, $checkbox;
		
		$(clickBody).unbind('click').on('click', 'td.clickable', function () {
			$checkbox = $(this).closest('tr').find('input[type="checkbox"]');
			isChecked = $checkbox.is(':checked');
			$checkbox.prop('checked', !isChecked).change();
		});
	}
	
	// 蒐集當前畫面中所有以勾選的項目
	var findCheckedboxs = function (jsExpression) {
		let checkeds = [];
		let dataTable = $(jsExpression).DataTable();
		let checkboxList = $(jsExpression).find('input[type="checkbox"]');
		
		$.each(checkboxList, function () {
			if ($(this).is(':checked')) {
				checkeds.push(TableUtil.getRow(dataTable, $(this)));
			}
		});
		
		return checkeds;
	}
	
	function temporaryLogList () {
		var dataList = [];
		var options = {summary : 'textarea'};
		var logList = $('#logList input[name="check"]');
		
		if (logList.length == 0) {
			tempData['logList'] = [];
			return;
		}
		
		$.each(logList, function () {
 			var item = {};
 			item = $.extend(item, TableUtil.getRow(dataTable, this, options));
 			item.checked = $(this).is(':checked');
 			dataList.push(item);
		});
		
		tempData['logList'] = dataList;
	}
	
	function temporarySegmentTable () {
		let dataTable = $('#segmentTable').DataTable();
		let segments = TableUtil.getRows(dataTable, {page:'all'});
		
		// 如果新增延伸單就必須刷新表單編號, 否則會更新到主單的資料。
		if (segments) {
			$.each(segments, function (i, item) {
				item.formId = $('input#formId').val();
			});
		}
		
		tempData.tabDbAlterForm.segments = segments;
	}
	
	function temporaryImpactList () {
		let impactList = [];
	 	let totalFraction = 0;
	 	let solution = $("#solution").val();// 因應措施
	 	let evaluation = $("#evaluation").val();// 影響評估
		
		$.each($(".impactRowValues"), function(index, element) {
			let impactObj = impactToJsObject(this);
			
			if(impactObj.isAddUp=="Y") {// 衝擊分析加總:isAddUp=Y才須加總
				totalFraction += parseInt(impactObj.targetFraction);
			}
			
			impactList.push(impactObj);
		});
		
	 	if(evaluation) {
	 		let params = {}
	 		params.questionType = "E";
	 		params.description = evaluation;
	 		params.isValidateFraction = "N";
	 		impactList.push(params);
	 	}
		
	 	if(solution) {
	 		let params = {}
	 		params.questionType = "S";
	 		params.description = solution;
	 		params.isValidateFraction = "N";
	 		impactList.push(params);
	 	}

	 	if(totalFraction) {
	 		let params = {}
	 		params.questionType = "T";
	 		params.targetFraction = totalFraction;
	 		params.isValidateFraction = "N";
	 		impactList.push(params);
	 	}
		
		tempData.impactForm.impactList = impactList;
	}
	
	function isFuntionButtons (element) {
		return $('div#formInfoButtons').has(element).length > 0;
	}
	
	function isPopBreadcrumb (breadcrumb) {
		var isPop = true;
		
		// variable functions is define at menu.jsp
		$.each(functions, function () {
			if (breadcrumb.name == $(this).text()) {
				isPop = false;
				return false;// break
			}
		});
		
		return isPop;
	}
	
	return {
		initMultiSelect : initMultiSelect,
		tempData : tempData,
		temporary : temporary,
		multiSelect : multiSelect,
		singleSelect : singleSelect,
		initTabs : initTabs,
		adjustTableWidth : adjustTableWidth,
		adjustTableWidth : adjustTableWidth,
		saveOriWidth : saveOriWidth,
		saveNewWidth : saveNewWidth,
		emptySelect : emptySelect,
		putBreadcrumb : putBreadcrumb,
		popBreadcrumb : popBreadcrumb,
		getBreadcrumbs : getBreadcrumbs,
		flushBreadcrumbs : flushBreadcrumbs,
		lockSubmitKey : lockSubmitKey,
		disableElements : disableElements,
		findCheckedboxs : findCheckedboxs,
		enableFunctionButtons : enableFunctionButtons,
		clickRowRadioEvent : clickRowRadioEvent,
		clickRowCheckboxEvent : clickRowCheckboxEvent
	}
}();
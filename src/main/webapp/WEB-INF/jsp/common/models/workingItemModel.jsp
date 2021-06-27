<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<script>
var WorkingItemModel = function () {

	var dataTable;
	var workingItemListTable = 'td#workingItemModel table#workingItemList';

	var init = function () {
	    var options = {
	            columns: [
	                { 
	                    title: '<input id="workingItemCheckall" type="checkbox" onchange="WorkingItemModel.wiCheckall()" />', 
	                    data: 'workingItemId', className: 'dt-center',
	                    render: function (data, type, obj, meta) {
	                        return '<input name="workingItemCbx" type="checkbox" onchange="WorkingItemModel.wiCheckallChangeEvent()" value="' + data + '">';
	                    }
	                },
	                { title: '<s:message code="workingItem.workingItemName" text="工作要項名稱" />', data: 'workingItemName', className: 'dt-center' },
	                { title: '<s:message code="workingItem.spGroup" text="系統科組別" />', data: 'spGroup', className: 'dt-center' }, 
	                { title: '<s:message code="workingItem.isImpact" text="變更衝擊分析" />', data: 'isImpact', className: 'dt-center' }, 
	                { title: '<s:message code="workingItem.isReview" text="變更覆核" />', data: 'isReview', className: 'dt-center' },
	                { 
	                    title: '', data: 'workingItemJson', className: 'hidden',
	                    render: function (data, type, obj, meta) {
	                        return '<input name="workingItemJson" type="hidden" value=\'' + data + '\'>';
	                    }
	                }
	            ],
	            paging: false,
	            ordering: false,
	            info: false,
				pageLength: -1
	    };
	    dataTable = TableUtil.init(workingItemListTable, options);
	    initWorkingItem();
	}

	// 全選
	var wiCheckall = function () {
	    let checkallBox = '#workingItemCheckall';
	    let checkboxs = workingItemListTable + ' input[name="workingItemCbx"]';
	    TableUtil.checkall(checkallBox, checkboxs);
	}

	// 監聽全選按鈕是否依然開啟/關閉
	var wiCheckallChangeEvent = function  () {
	    let checkallBox = '#workingItemCheckall';
	    let checkboxs = workingItemListTable + ' input[name="workingItemCbx"]';
	    TableUtil.checkallChangeEvent(checkallBox, checkboxs);
	}

	var openWorkingItemDialog = function () {
	    let dialogSelector = 'div#workingItemDialog';
	    let tableSelector = dialogSelector + ' table#workingItemResultList';
	    WorkingItemDialog.show(dialogSelector, tableSelector, function (rowData) {
	    	return addWorkingItemRow(rowData);
	    });
	}

	var deleteWorkingItemRow = function () {
	    let $checkboxs = $(workingItemListTable).find('input[name="workingItemCbx"]:checked');
	    
	    $checkboxs.each(function (i, o) {
	        TableUtil.deleteRow(dataTable, o);
	    });
	    
	    wiCheckallChangeEvent();
	    // 重塞hidden json
	    $('#workingItem').val(collectWorkingItemJson());
	}
	
	function initWorkingItem () {
	    let workingItem = [];
	    let formInfo = form2object('headForm');
	    
	    if (HtmlUtil.tempData.infoForm &&
	    		HtmlUtil.tempData.infoForm.workingItem) {
	    	workingItem = ObjectUtil.parse(
	    			HtmlUtil.tempData.infoForm.workingItem);
	    	
	    	let tempItems = [];
	    	$.each(workingItem, function (i, rowData) {
	    		rowData = $.extend(
	    				rowData, ObjectUtil.parse(rowData.item));
	    		tempItems.push(rowData);
	    	});
	    	workingItem = tempItems;
		} else if (formInfo.formStatus) {
			SendUtil.get('/commonJobForm/getWorkingItemsFromJob', 
					formInfo.formId, function (response) {
				workingItem = response ? response : [];
			}, ajaxSetting);
		}
	    
		$.each(workingItem, function () {
			addWorkingItemRow(this);
		});
	}

	/**
	 * 傳給workingItemDialog作為點選後的callback method
	 * 回傳{ result: '', message: ''}
	 */
	function addWorkingItemRow (rowData) {
	    let result = {};
	    let isDuplicated = isDuplicatedWorkingItem(rowData.workingItemId);
	    if (isDuplicated) {
	        result['result'] = false;
	        result['message'] = StringUtil.format('<s:message code="workingItem.error.isDuplicated" text="{0}已存在工作要項清單" />', rowData.workingItemName);
	        return result;
	    }
	    
	    var item = {
	   		workingItemId: rowData.workingItemId,
	   		workingItemName: rowData.workingItemName,
	   		spGroup: rowData.spGroup
	    };
	    
	    var rowDataObj = {
	        "item": ObjectUtil.stringify(item),
	        "isImpact": rowData.isImpact,
	        "isReview": rowData.isReview
	    };
	    
	    rowData['workingItemJson'] = ObjectUtil.stringify(rowDataObj);
	    TableUtil.addRow(dataTable, rowData);
	    
	    wiCheckallChangeEvent();
	    // 塞hidden json
	    $('#workingItem').val(collectWorkingItemJson());
	    
	    result['result'] = true;
	    return result;
	}

	function collectWorkingItemJson() {
	    let itemArray = [];
	    $(workingItemListTable).find('input[name="workingItemJson"]').each(function (i, o) {
	        itemArray.push(ObjectUtil.parse($(o).val()));
	    });
	    
	    return ObjectUtil.stringify(itemArray);
	}

	function isDuplicatedWorkingItem(workingItemId) {
	    let isDuplicated = false;
	    let $checkboxs = $(workingItemListTable).find('input[name="workingItemCbx"]');

	    $checkboxs.each(function (i, o) {
	        if ($(o).val() && $(o).val() == workingItemId) {
	            isDuplicated = true;
	            return false; // break;
	        }
	    });
	    
	    return isDuplicated;
	}
	
	return {
		init : init,
		wiCheckall : wiCheckall,
		openWorkingItemDialog : openWorkingItemDialog,
		wiCheckallChangeEvent : wiCheckallChangeEvent,
		deleteWorkingItemRow : deleteWorkingItemRow
	}
}();

$(function () {
	WorkingItemModel.init();
});
</script>

<td id="workingItemModel" colspan="2">
    <input id="workingItem" name="workingItem" type="hidden" />
    <fieldset>
        <legend><s:message code="workingItem.model.title" text="工作要項" /></legend>
        <div class="grid_BtnBar">
            <button type="button" onclick="WorkingItemModel.openWorkingItemDialog()">
                <i class="iconx-add"></i><s:message code="button.add" text="新增" />
            </button>
            <button type="button" onclick="WorkingItemModel.deleteWorkingItemRow()">
                <i class="iconx-delete"></i><s:message code="button.delete" text="刪除" />
            </button>
        </div>
        <div style="width: 550px; height: 200px; overflow-y: auto;">
	        <table id="workingItemList" class="display collapse cell-border">
	            <thead></thead>
	            <tbody></tbody>
	        </table>
        </div>
    </fieldset>
</td>
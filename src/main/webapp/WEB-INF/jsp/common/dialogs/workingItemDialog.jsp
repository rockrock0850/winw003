<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<script>
/**
 * 新增工作要項對話框
 */
var WorkingItemDialog = function () {

	var dialog = 'div#workingItemDialog';
	var table = dialog + ' table#workingItemResultList';
	
	var show = function (dialogSelector, tableSelector, callbackFunc) {
        if (!callbackFunc) { return false; }
        
        let dialogOpts = {
            width: '1000px',
            maxWidth: '1000px',
            resizable: false,
            open: function (event, ui) {
            	initTable();
            },
            close: function () {
				TableUtil.deleteTable(table);
            }
        };
        
		DialogUtil.show(dialog, dialogOpts);
		DialogUtil.clickRow(table, function (row) {
            let result = callbackFunc(row);
            if (result.result) {
                DialogUtil.close();
				TableUtil.deleteTable(table);
            } else {
                alert(result.message);
            }
		}, true);
	}
	
	function initTable () {
        let formInfo = form2object('headForm');
        loginUserInfo.mboName = formInfo.formClass;
        SendUtil.post('/html/getWorkingItemList', loginUserInfo, function (resData) {
            if (TableUtil.isDataTable(table)) {
                TableUtil.reDraw($(table).DataTable(), resData);
            } else {
                let options = {
                    data: resData,
                    columnDefs: [
                        {orderable: false, targets: '_all'}, 
                        {targets: [0], title: '工作要項名稱', data: 'workingItemName', className: 'dt-center'}, 
                        {targets: [1], title: '系統科組別', data: 'spGroup', className: 'dt-center'}, 
                        {targets: [2], title: '變更衝擊分析', data: 'isImpact', className: 'dt-center'}, 
                        {targets: [3], title: '變更覆核', data: 'isReview', className: 'dt-center'},
                        {targets: [4], title: '', data: 'workingItemId', className: 'hidden'}
                    ]
                };
                TableUtil.init(table, options);
            }
        }, null, true);
	}
	
	return {
		show : show
	}
}();

function searchWorkingItem () {
	let table = 'div#workingItemDialog table#workingItemResultList';
	var reqData = form2object('workingItemForm');
	reqData.mboName = form2object('headForm').formClass;
	
	SendUtil.post('/html/getWorkingItemList', reqData, function (resData) {
		TableUtil.reDraw($(table).DataTable(), resData);
	});
}
</script>
<div id="workingItemDialog" style="display: none;">
	<fieldset>
		<legend><s:message code="workingItem.dialog.title" text="選取工作要項" /></legend>
		<form id="workingItemForm">
			<table class="grid_query">
				<tr>
					<th><s:message code="workingItem.dialog.keyword" text="關鍵字" /></th>
					<td>
						<input type="text" name="keyword" />
					</td>
					<td>
						<button type="button" onclick="searchWorkingItem()">
							<i class="iconx-search"></i><s:message code="button.search" text="查詢" />
						</button>
					</td>
				</tr>
			</table>
		</form>
		<table id="workingItemResultList" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
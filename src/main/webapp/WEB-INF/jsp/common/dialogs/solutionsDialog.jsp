<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<script>//# sourceURL=solutionsDialog.js
/**
 * 新增相關解決方案對話框
 */
var SolutionsDialog = function () {

	var dataTable;
	var dialog = 'div#solutionsDialog';
	var table = dialog + ' table#solutionsResultList';
	
	var show = function (callback) {
        let dialogOpts = {
            width: '1200px',
            maxWidth: '1000px',
            resizable: true,
            open: function (event, ui) {
            	search();
            },
            close: function () {
				TableUtil.deleteTable(table);
            	$(dialog).find('input').val('');
            }
        };
        
		DialogUtil.show(dialog, dialogOpts);
		DialogUtil.clickRow(table, function (row) {
			if (callback) {
				let solutions = $('input#solutions').val();
				
				if (solutions) {
					if (solutions.indexOf(row.formId) >= 0) {
						alert(row.formId + '重複新增。');
						return;
					} else {
						solutions += ',' + row.formId;
					}
				} else {
					solutions = row.formId;
				}
				
				callback(solutions);
	            DialogUtil.close();
				TableUtil.deleteTable(table);
			}
		}, false);
	}

	var search = function () {
		let reqData = form2object('solutionsForm');
		reqData.isEnabled = 'Y';
		reqData.formClass = 'KL';
		reqData.isDetails = false;
		
		SendUtil.post('/knowledgeForm/list', reqData, function (resData) {
			let formId = form2object('headForm').formId;
			
			resData = $.grep(resData, function (item) {
				return item.formId != formId;
			});
			
			if (TableUtil.isDataTable(table)) {
	            TableUtil.reDraw(dataTable, resData);
	        } else {
	            let options = {
	                data: resData,
	                columnDefs: [
	                    {orderable: false, targets: '_all'}, 
	                    {targets: [0], title: '<s:message code="form.report.search.serialnumber" text="表單編號" />', data: 'formId', className: 'dt-center clickable'},
	                    {targets: [1], title: '<s:message code="form.report.search.summary" text="摘要" />', data: 'summary', className: 'dt-center clickable'},
	                    {targets: [2], title: '<s:message code="q.report.operation.specialcase.sign" text="徵兆" />', data: 'indication', className: 'dt-center clickable'},
	                    {targets: [3], title: '<s:message code="q.report.operation.specialcase.reason" text="原因" />', data: 'reason', className: 'dt-center clickable'},
	                    {targets: [4], title: '<s:message code="q.report.operation.specialcase.treatmentPlan" text="處理方案" />', data: 'processProgram', className: 'dt-center clickable'}
	                ]
	            };
	            dataTable = TableUtil.init(table, options);
	        }
		});
	}
	
	return {
		show : show,
		search : search
	}
}();
</script>
<div id="solutionsDialog" style="display: none;">
	<fieldset>
		<legend><s:message code="solutions.dialog.title" text="選取工作要項" /></legend>
		<form id="solutionsForm">
			<table class="grid_query">
				<tr>
					<th><s:message code="form.report.search.summary" text="摘要" /></th>
					<td>
						<input type="text" name="summary" />
					</td>
					<th><s:message code="q.report.operation.specialcase.sign" text="徵兆" /></th>
					<td>
						<input type="text" name="indication" />
					</td>
					<th><s:message code="q.report.operation.specialcase.reason" text="原因" /></th>
					<td>
						<input type="text" name="reason" />
					</td>
					<th><s:message code="q.report.operation.specialcase.treatmentPlan" text="處理方案" /></th>
					<td>
						<input type="text" name="processProgram" />
					</td>
					<td>
						<button type="button" onclick="SolutionsDialog.search()">
							<i class="iconx-search"></i><s:message code="button.search" text="查詢" />
						</button>
					</td>
				</tr>
			</table>
		</form>
		<table id="solutionsResultList" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</div>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<script>//# sourceURL=solutionsModel.js
/**
 * 新增相關解決方案模組
 */
var SolutionsModel = function () {

	var dataTable;
	var solutionsListTable = 'fieldset#solutionsModel table#solutionsList';

	var init = function () {
		tempSelection();
		HtmlUtil.clickRowCheckboxEvent(solutionsListTable + ' tbody');
	}

	// 全選
	var checkall = function () {
	    let checkallBox = '#solutionsCheckall';
	    let checkboxs = solutionsListTable + ' input[name="solutionsCbx"]';
	    TableUtil.checkall(checkallBox, checkboxs);
	}

	// 監聽全選按鈕是否依然開啟/關閉
	var checkallChangeEvent = function  () {
	    let checkallBox = '#solutionsCheckall';
	    let checkboxs = solutionsListTable + ' input[name="solutionsCbx"]';
	    TableUtil.checkallChangeEvent(checkallBox, checkboxs);
	}

	var openDialog = function () {
		HtmlUtil.temporary();
		
	    SolutionsDialog.show(function (data) {
	    	$('input#solutions').val(data);
	    	HtmlUtil.tempData.infoForm.solutions = data;
	    	tempSelection();
	    });
	}

	var deleteRow = function () {
	    let $checkboxs = $(solutionsListTable).find('input[name="solutionsCbx"]:checked');
	    
	    $checkboxs.each(function (i, o) {
	        TableUtil.deleteRow(dataTable, o);
	    });
	    
	    checkallChangeEvent();
	    
	    let formIds = [];
	    let rows = TableUtil.getRows(dataTable, {page:'all'});
	    
	    if (rows) {
		    $.each(rows, function () {
		    	formIds.push(this.formId);
		    });
	    }
	    
	    $('#solutions').val(formIds.toString());
	}
	
	// 有暫存就拿暫存否則用資料庫的
	function tempSelection () {
	    let solutions;
	    let form = form2object('headForm');
	    
	    if (HtmlUtil.tempData.infoForm &&
	    		HtmlUtil.tempData.infoForm.solutions) {
	    	solutions = HtmlUtil.tempData.infoForm.solutions;
		} else {
	    	solutions = $('input#solutions').val();
		}
	    
	    form.isDetails = true;
	    form.solutions = solutions;
	    
		SendUtil.post('/knowledgeForm/list', form, function (resData) {
			if (TableUtil.isDataTable(solutionsListTable)) {
	            TableUtil.reDraw(dataTable, resData);
	        } else {
	            let options = {
	                data: resData,
	                columnDefs: [
		                {
		                	targets: [0],
		                    title: '<input id="solutionsCheckall" type="checkbox" onchange="SolutionsModel.checkall()" />', 
		                    data: 'solutionsId', className: 'dt-center',
		                    render: function (data, type, obj, meta) {
		                        return '<input name="solutionsCbx" type="checkbox" onchange="SolutionsModel.checkallChangeEvent()" value="' + data + '">';
		                    }
		                },
	                    {targets: [1], title: '<s:message code="form.report.search.serialnumber" text="表單編號" />', data: 'formId', className: 'dt-center clickable'},
	                    {targets: [2], title: '<s:message code="form.report.search.summary" text="摘要" />', data: 'summary', className: 'dt-center clickable'},
	                    {targets: [3], title: '<s:message code="q.report.operation.specialcase.sign" text="徵兆" />', data: 'indication', className: 'dt-center clickable'},
	                    {targets: [4], title: '<s:message code="q.report.operation.specialcase.reason" text="原因" />', data: 'reason', className: 'dt-center clickable'},
	                    {targets: [5], title: '<s:message code="q.report.operation.specialcase.treatmentPlan" text="處理方案" />', data: 'processProgram', className: 'dt-center clickable'}
	                ]
	            };
	            dataTable = TableUtil.init(solutionsListTable, options);
	        }
		});
	    
	    return solutions;
	}
	
	return {
		init : init,
		checkall : checkall,
		deleteRow : deleteRow,
		openDialog : openDialog,
		checkallChangeEvent : checkallChangeEvent
	}
}();

$(function () {
	SolutionsModel.init();
});
</script>

<input id="solutions" name="solutions" type="hidden" />
<fieldset id="solutionsModel">
    <legend><s:message code="form.question.process.knowledge.7" text="相關解決方案" /></legend>
    <div class="grid_BtnBar">
        <button type="button" onclick="SolutionsModel.openDialog()">
            <i class="iconx-add"></i><s:message code="button.add" text="新增" />
        </button>
        <button type="button" onclick="SolutionsModel.deleteRow()">
            <i class="iconx-delete"></i><s:message code="button.delete" text="刪除" />
        </button>
    </div>
	<table id="solutionsList" class="display collapse cell-border">
	    <thead></thead>
	    <tbody></tbody>
	</table>
</fieldset>
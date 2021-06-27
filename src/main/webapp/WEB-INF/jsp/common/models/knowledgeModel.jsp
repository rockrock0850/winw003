<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<script>//# sourceURL=knowledgeModel.js
/**
 * 新增知識庫根因模組
 */
var KnowledgeModel = function () {

	var dataTable;
	var knowledgeListTable = 'fieldset#knowledgeModel table#knowledgeList';

	var init = function () {
		let knowledges = tempSelection();
		
	    var options = {
			data: ObjectUtil.parse(knowledges),
            columns: [
                { 
                    title: '<input id="knowledgeCheckall" type="checkbox" onchange="KnowledgeModel.checkall()" />', 
                    data: 'knowledgeId', 
                    className: 'dt-center',
                    render: function (data, type, obj, meta) {
                        return '<input name="knowledgeCbx" type="checkbox" onchange="KnowledgeModel.checkallChangeEvent()" value="' + data + '">';
                    }
                },
                { title: '<s:message code="form.question.form.info.knowledge1" text="原因類別" />', data: 'knowledge1Display', className: 'dt-center clickable' },
                { title: '<s:message code="form.question.form.info.knowledge2" text="問題根因" />', data: 'knowledge2Display', className: 'dt-center clickable' }
            ],
            paging: false,
            ordering: false,
            info: false,
			pageLength: -1
	    };
	    
	    dataTable = TableUtil.init(knowledgeListTable, options);
		HtmlUtil.clickRowCheckboxEvent(knowledgeListTable + ' tbody');
	}

	// 全選
	var checkall = function () {
	    let checkallBox = '#knowledgeCheckall';
	    let checkboxs = knowledgeListTable + ' input[name="knowledgeCbx"]';
	    TableUtil.checkall(checkallBox, checkboxs);
	}

	// 監聽全選按鈕是否依然開啟/關閉
	var checkallChangeEvent = function  () {
	    let checkallBox = '#knowledgeCheckall';
	    let checkboxs = knowledgeListTable + ' input[name="knowledgeCbx"]';
	    TableUtil.checkallChangeEvent(checkallBox, checkboxs);
	}

	var openDialog = function () {
	    KnowledgeDialog.show(function (reasons) {
	    	TableUtil.reDraw(dataTable, reasons);
	    	findIsKnowledgeBox(reasons);
	    	$('input#knowledges').val(ObjectUtil.stringify(reasons));
	    });
	}

	var deleteRow = function () {
	    let $checkboxs = $(knowledgeListTable).find('input[name="knowledgeCbx"]:checked');
	    
	    $checkboxs.each(function (i, o) {
	        TableUtil.deleteRow(dataTable, o);
	    });
	    
	    checkallChangeEvent();
	    
	    let reasons = TableUtil.getRows(dataTable, {page:'all'});
	    findIsKnowledgeBox(reasons);
	    $('input#knowledges').val(ObjectUtil.stringify(reasons));
	}

	function findIsKnowledgeBox (reasons) {
    	let isKnowledge = false;
    	
    	$.each(reasons, function (i, item) {
    		SendUtil.get('/html/getSubDropdownList', item.knowledge1, function (subReasons) {
    			isKnowledge = isKnowledgeFunc(item.knowledge2, subReasons);
    		}, ajaxSetting);

			if (isKnowledge) return false;// break;
    	});
    	
    	if (typeof isSuggestCaseDisabled != 'undefined') {
			isSuggestCaseDisabled(isKnowledge);
    	}
	}
	
	function isKnowledgeFunc (knowledge2, subReasons) {
		let isKnowledge = false;

		if (subReasons) {
			$.each(subReasons, function (i, sub) {
				if (!knowledge2) return false;// break;

				if (sub.value == knowledge2 && 
						sub.isKnowledge == 'Y') {
					isKnowledge = true;
					return false;// break;
				}
		
				if (isKnowledge) return false;// break;
			});
		}
		
		return isKnowledge;
	}
	
	// 有暫存就拿暫存否則用資料庫的
	function tempSelection () {
	    let knowledges = [];
	    let formInfo = form2object('headForm');
	    
	    if (HtmlUtil.tempData.infoForm &&
	    		HtmlUtil.tempData.infoForm.knowledges) {
	    	knowledges = HtmlUtil.tempData.infoForm.knowledges;
		} else {
	    	knowledges = $('input#knowledges').val();
		}
	    
	    return knowledges;
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
	KnowledgeModel.init();
});
</script>

<input id="knowledges" name="knowledges" type="hidden" />
<fieldset id="knowledgeModel"> 
    <legend><s:message code="form.question.process.knowledge.6" text="知識庫根因" /></legend>
    <div class="grid_BtnBar">
        <button type="button" onclick="KnowledgeModel.openDialog()">
            <i class="iconx-add"></i><s:message code="button.add" text="新增" />
        </button>
        <button type="button" onclick="KnowledgeModel.deleteRow()">
            <i class="iconx-delete"></i><s:message code="button.delete" text="刪除" />
        </button>
    </div>
    <div style="width: 550px; height: 200px; overflow-y: auto;">
     <table id="knowledgeList" class="display collapse cell-border">
         <thead></thead>
         <tbody></tbody>
     </table>
    </div>
</fieldset>
﻿<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<style>
.grid_query table th {
    text-align: left;
}
</style>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="form.question.process.knowledge.1" text='知識庫' /></h1>
	<%-- 表單類型欄位查詢 --%>
	<fieldset id="search" style="width: 25%; float: left; margin-left: 20px; margin-right: 20px;">
		<input id='formClass' name='formClass' type='hidden' value='KL' />
	
		<legend>
			<label><input id="checkAllSwitch" type="checkbox" onchange="checkAll()">全選</label>
			<button onclick='search()'>
				<i class="iconx-search"></i> <s:message code="button.search" text='查詢' />
			</button>
		</legend>

		<%-- 查詢欄位 --%>
		<div style="height: 28rem; width: 28rem; overflow: auto;">
			<table id="fieldsTable" class="grid_query"></table>
		</div>
	</fieldset>
	
	<%-- 表單查詢結果 --%>
	<fieldset id="search2">
		<legend>
			<s:message code="global.query.result" text='查詢結果' />
			<button onclick='exportExcel()'>
				<i class="iconx-export"></i> <s:message code="button.export" text='匯出' />
			</button>
		</legend>
		<div id="tableMax">
			<table id="dataTable" class="display collapse cell-border nowrap">
				<thead></thead>
				<tbody></tbody>
			</table>
		</div>
	</fieldset>
	
	<%-- 預先讀取欄位下拉選單資料 --%>
	<fieldset style="display: none">
		<%-- DC科的會辦項目 --%>
		<select id="DCSelect" name="DCSelect">
			<option value=""><s:message code="global.select.please.all" text='全部' /></option>
			<option value="DC1"><s:message code="form.title.tabDc1" text='ONLINE' /></option>
			<option value="DC2"><s:message code="form.title.tabDc2" text='BATCH' /></option>
			<option value="DC3"><s:message code="form.title.tabDc3" text='OPEN' /></option>
			<option value="DB"><s:message code="form.title.tabDb" text='DB變更' /></option>
			<option value="BATCH"><s:message code="form.title.tabBatch" text='批次' /></option>
		</select>
		<%-- 工作組別 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="workingSelect" />
			<jsp:param name="name" value="workingSelect" />
		</jsp:include>
		<%-- 特殊結案,轉開問題單,設定為主要事件單,全部功能服務中斷 --%>
		<select id="YNSelect" name="YNSelect">
			<option value=""><s:message code="global.select.please.all" text='全部' /></option>
			<option value="Y">Y</option>
			<option value="N">N</option>
		</select>
		<%-- 變更類型 --%>
		<select id="CTSelect" name="CTSelect">
			<option value=""><s:message code="global.select.please.all" text='全部' /></option>
			<option value="一般變更"><s:message code="form.search.html.change.type.normal" text='一般變更' /></option>
			<option value="標準變更"><s:message code="form.search.html.change.type.standard" text='標準變更' /></option>
		</select>
		<%-- 變更等級 --%>
		<select id="CLSelect" name="CLSelect">
			<option value=""><s:message code="global.select.please.all" text='全部' /></option>
			<option value="次要變更"><s:message code="form.search.html.change.level.middle" text='次要變更' /></option>
			<option value="重大變更"><s:message code="form.search.html.change.level.high" text='重大變更' /></option>
		</select>
		<%-- 表單狀態 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="formStatusSelect" />
			<jsp:param name="name" value="formStatusSelect" />
		</jsp:include>
		<%-- 單位分類 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="unitCategorySelect" />
			<jsp:param name="name" value="unitCategorySelect" />
		</jsp:include>
		<%-- 服務類別 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="sClassSelect" />
			<jsp:param name="name" value="sClassSelect" />
		</jsp:include>
		<%-- 會辦科 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="countersignedsSelect" />
			<jsp:param name="name" value="countersignedsSelect" />
		</jsp:include>
		<%-- 知識庫原因類別 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="knowledge1Select" />
			<jsp:param name="name" value="knowledge1Select" />
		</jsp:include>
		<%-- 會辦科 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="apcCountersignedsSelect" />
			<jsp:param name="name" value="countersignedsSelect" />
		</jsp:include>
		<%-- 問題來源 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="questionIdSelect" />
			<jsp:param name="name" value="questionIdSelect" />
		</jsp:include>
		<%-- 影響範圍 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="effectScopeSelect" />
			<jsp:param name="name" value="effectScopeSelect" />
		</jsp:include>
		<%-- 需求單緊急程度 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="srUrgentLevelSelect" />
			<jsp:param name="name" value="srUrgentLevelSelect" />
		</jsp:include>
		<%-- 問題單緊急程度 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="qUrgentLevelSelect" />
			<jsp:param name="name" value="qUrgentLevelSelect" />
		</jsp:include>
		<%-- 事件單緊急程度 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="incUrgentLevelSelect" />
			<jsp:param name="name" value="incUrgentLevelSelect" />
		</jsp:include>
		<%-- 事件主類別 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="eventClassSelect" />
			<jsp:param name="name" value="eventClassSelect" />
		</jsp:include>
		<%-- 資安事件 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="eventSecuritySelect" />
			<jsp:param name="name" value="eventSecuritySelect" />
		</jsp:include>
		<%-- 標準變更作業 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="standardSelect" />
			<jsp:param name="name" value="standardSelect" />
		</jsp:include>
		<%-- 組態分類 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="cCategorySelect" />
			<jsp:param name="name" value="cCategorySelect" />
		</jsp:include>
		<%-- 特殊結案狀態 --%>
		<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
			<jsp:param name="id" value="SpecialEndCaseTypeSelect" />
			<jsp:param name="name" value="SpecialEndCaseTypeSelect" />
		</jsp:include>
	</fieldset>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	

<style type="text/css">
.date-model-split {
	margin-right: 15px;
}
</style>

<script>//# sourceURL=knowledgeFind.js
var dataTable;
var dateOption = {minDate: ''};
var ajaxSetting = {async:false};
var uriSearch = '/formSearch/query';
var toPageColumn = ["formId", "sourceId"];
var timePattern = new RegExp(/Start$/);// 正規表示式:找結尾為Start的字串
var i18nSort = '<s:message code="form.search.html.sort" text="排序" />';
var subSelect = ["sSubClassSelect", "cClassSelect", "cComponentSelect", 'knowledge2Select'];
var i18nformId = '<s:message code="form.search.column.formId" text="表單編號" />';
var i18nColumnView = '<s:message code="form.search.html.column.view" text="欄位顯示" />';
var subDCClazz = ['DC1', 'DC2', 'DC3', 'DB', 'BATCH'];
var allowFormClazzes = ['KL'];

$(function () {
	DateUtil.datePicker();
	initView();
	SessionUtil.fetch('formSearch', function (record) {
		search(record);
	});
});

function initView () {
	<%-- 取得工作組別清單 --%>
	SendUtil.get('/html/getDropdownList', 'WORK_LEVEL', function (options) {
		var optionsRemoved = [];
		$(options).each(function(){
			if(this.wording.indexOf('組') != -1) {
				optionsRemoved.push(this);
			}
		});
		HtmlUtil.singleSelect('select#workingSelect', optionsRemoved);
	}, ajaxSetting);
	<%-- 取得表單狀態下拉選單 --%>
	SendUtil.get('/html/getFormStatusSelectors', null, function (options) {
		HtmlUtil.singleSelect('select#formStatusSelect', options);
	}, ajaxSetting);
	<%-- 取得提出單位分類下拉選單 --%>
	SendUtil.get("/html/getDropdownList", 1, function (options) {
		HtmlUtil.singleSelect('select#unitCategorySelect', options);
	}, ajaxSetting);
	<%-- 取得會辦科下拉選單 --%>
	SendUtil.get("/html/getDropdownList/release", 'C_LIST', function (options) {
		HtmlUtil.singleSelect('select#countersignedsSelect', options);
		
		let expectedsp = [];
		$.each(options, function () {
			if (this.value.indexOf('SP') == -1) {
				expectedsp.push(this);
			}
		});
		
		HtmlUtil.singleSelect('select#apcCountersignedsSelect', expectedsp);
	}, ajaxSetting);
	<%-- 取得知識庫原因類別下拉選單 --%>
	SendUtil.get('/html/getDropdownList', 'knowledge', function (options) {
		HtmlUtil.singleSelect('select#knowledge1Select', options);
	}, ajaxSetting);
	<%-- 取得服務類別下拉選單 --%>
	SendUtil.get('/html/getDropdownList', 2, function (options) {
		HtmlUtil.singleSelect('select#sClassSelect', options);
	}, ajaxSetting);
	<%-- 取得問題來源下拉選單 --%>
	SendUtil.get("/html/getDropdownList", 3, function (options) {
		HtmlUtil.singleSelect('select#questionIdSelect', options);
	}, ajaxSetting);
	<%-- 取得事件主類別下拉選單 --%>
	SendUtil.get("/html/getDropdownList", 4, function (options) {
		HtmlUtil.singleSelect('select#eventClassSelect', options);
	}, ajaxSetting);
	<%-- 取得資安事件下拉選單 --%>
	SendUtil.get("/html/getDropdownList", 'SecurityEvent', function (options) {
		HtmlUtil.singleSelect('select#eventSecuritySelect', options);
	}, ajaxSetting);
	<%-- 取得影響範圍下拉選單 --%>
	SendUtil.get("/html/getDropdownList", 'EFFECT_SCOPE', function (options) {
		HtmlUtil.singleSelect('select#effectScopeSelect', options);
	}, ajaxSetting);
	<%-- 取得需求單緊急程度下拉選單 --%>
	SendUtil.get("/html/getDropdownList", 'SR_URGENT_LEVEL', function (options) {
		HtmlUtil.singleSelect('select#srUrgentLevelSelect', options);
	}, ajaxSetting);
	<%-- 取得問題單緊急程度下拉選單 --%>
	SendUtil.get("/html/getDropdownList", 'Q_URGENT_LEVEL', function (options) {
		HtmlUtil.singleSelect('select#qUrgentLevelSelect', options);
	}, ajaxSetting);
	<%-- 取得事件單緊急程度下拉選單 --%>
	SendUtil.get("/html/getDropdownList", 'INC_URGENT_LEVEL', function (options) {
		HtmlUtil.singleSelect('select#incUrgentLevelSelect', options);
	}, ajaxSetting);
	<%-- 取得標準變更作業下拉選單 --%>
	SendUtil.get('/html/getDropdownList', 'StandardChange', function (options) {
		HtmlUtil.singleSelect('select#standardSelect', options);
	}, ajaxSetting);
	<%-- 取得組態分類下拉選單 --%>
	SendUtil.get('/html/getDropdownList', 'cCategory', function (options) {
		HtmlUtil.singleSelect('select#cCategorySelect', options);
	}, ajaxSetting);
	<%-- 取得特殊結案下拉選單 --%>
	SendUtil.get('/html/getDropdownList', 'SPECIAL_END_CASE_TYPE', function (options) {
		HtmlUtil.singleSelect('select#SpecialEndCaseTypeSelect', options);
	}, ajaxSetting);
	
	SendUtil.get('/formSearch/getFormFieldsInfo', 'KL', function (fields) {
		$.each(fields, function () {
			setColumn(this, 'KL');
		})
		DateUtil.datePicker(dateOption);
		genApColumns('KL');
	}, null, true);
	
	// 依據瀏覽器大小動態設定查詢區塊的寬度
	$('fieldset#search2').css({'width':($( window ).width() / 2 * 0.08) + 'rem'});
    dataTable = TableUtil.init('#dataTable', defaultOpt(null));
	$('div#tableMax').css({'width':($('fieldSet#search2').width()/13.8) + 'rem','overflow':'auto'});
}

function genApColumns (clazz) {
	if ('JOB_AP' == clazz || 'JOB_AP_C' == clazz) {
		$('select#countersigneds,select#apcCountersigneds').change(function () {
			clazz = $(this).val().split('-')[1];
			$("#countersignedsCols tr").remove(); 
			$("#apcCountersignedsCols tr").remove(); 
			
			if (clazz) {
				SendUtil.get('/formSearch/getFormFieldsInfo', clazz, function (fields) {
					$.each(fields, function () {
						setColumn(this, clazz);
					})
					DateUtil.datePicker(dateOption);
					genDcColumns(clazz);
				}, null, true);
			}
		});
	}
}

function genDcColumns (clazz) {
	if ('DC' == clazz) {
		$('select#cType').change(function () {
			clazz = $(this).val();
			$("#cTypeCols tr").remove(); 
			
			if (clazz) {
				SendUtil.get('/formSearch/getFormFieldsInfo', clazz, function (fields) {
					$.each(fields, function () {
						setColumn(this, clazz);
					})
					DateUtil.datePicker(dateOption);
				}, null, true);
			}
		});
	}
}

function search (record) {
	<%-- 檢查顯示欄位是否有勾選 --%>
	var message = noViewCheck();
	if (message) {
		alert(message);
		return false;
	}
	
	<%-- 檢查顯示欄位的排序輸入是否正確 --%>
	message = isErrorOrder();
	if (message) {
		alert(message);
		return false;
	}
	
	cleanTable();
	
	var searchForm = record ? record : form2object('search');
	dataTable = TableUtil.paging('#dataTable', dynamicOpt(searchForm));
	
	SessionUtil.record('formSearch', searchForm);
}

function exportExcel() {
	var result;
	
	<%-- 檢查顯示欄位是否有勾選 --%>
	result = noViewCheck();
	
	if (result) {
		alert(result);
		return false;
	}
	
	<%-- 檢查顯示欄位的排序輸入是否正確 --%>
	result = isErrorOrder();
	
	if (result) {
		alert(result);
		return false;
	}

	var columnOrder = [];
	var postData = form2object('search');
	
	if($("input#formClass").val() != ''){
		var noOrderCount = 0;
		var orderMap = new Map();   // 紀錄有排序的欄位
		var noOrderMap = new Map(); // 紀錄沒有排序的欄位
		
		$('#search').find('input[type="checkbox"].columnView:checked').each(function(){
			id = $(this).closest('tr').next('tr').find('td').find(':first').attr("id");

			// 去除start/end關鍵字
			if (id.match(timePattern)) {
				id = id.replace(timePattern, "");
			}

			let orderDom = $(this).closest('tr').find('th').find('input[class="order"]');
			if ($(orderDom).is(":visible")) order = $(orderDom).val();

			var formMap = new Map();
			formMap.set("id", id);
			
			if (order) {
				orderMap.set(order, formMap);
			} else {
				noOrderMap.set(noOrderCount, formMap);
				noOrderCount++;
			}
		})
		
		var orderMapAsc = sortMap(orderMap);

		// 先排有輸入排序的欄位
		orderMapAsc.forEach(function(item, index, array){
			id = item.get("id");
			columnOrder.push(id);
		})
		
		// 再排未輸入排序的欄位
		noOrderMap.forEach(function(item, index, array){
			id = item.get("id");
			columnOrder.push(id);
		})

		postData["columnOrder"] = columnOrder;
	}

	FileUtil.download('/formSearch/export', postData);
}

function defaultOpt (req){
	return {
		request: req,
        pageLength: 20,
		queryPath: uriSearch,
		orderBy: ['CreateTime'],
		orderByDef: [3, 'DESC'],
		orderBy: [
			'FormClass',
			'FormId',
			'FormStatus',
			'CreateTime',
		],
        columnDefs: [
        	{orderable: false, targets: []},
        	{targets: [0], title: '<s:message code="form.search.column.formClass" text="表單類別" />', data: 'formClass', className: 'dt-center'},
        	{targets: [1], title: '<s:message code="form.search.column.formId" text="表單編號" />', data: 'formId', className: 'dt-center',
        		render : function (data) {
					return '<a href="javascript: toForm(\''+data+'\')">' + data + '</a>';
			}},
        	{targets: [2], title: '<s:message code="form.search.column.formStatus" text="表單狀態" />', data: 'formStatus', className: 'dt-center'},
        	{targets: [3], title: '<s:message code="form.search.column.createTime" text="開單時間" />', data: 'createTime', className: 'dt-center',
        		render : function (data) {
					return DateUtil.toDateTime(data);
			}}
        ]
    };
}

function dynamicOpt (req){
	let colDefs = getTableColDef();
	let orderByDef = getOrderByDef(colDefs);
	let orderByCols = getOrderByCols(colDefs);
	
	return {
		request: req,
        pageLength: 20,
        columnDefs: colDefs,
		orderBy: orderByCols,
		queryPath: uriSearch,
		orderByDef: orderByDef
    };
}

function getOrderByDef (colDefs) {
	let orderByDef = [];
	let date = '<s:message code="logon.record.date" text="日期" />';
	let time = '<s:message code="logon.record.time" text="時間" />';
	let createTime = '<s:message code="form.search.column.createTime" text="開單時間" />';
	
	$.each(colDefs, function (i, item) {
		if (createTime == item.title) {
			orderByDef.push(i-1);// 減去第0個預設排序用的欄位定義項目
		}
	});

	if (orderByDef.length == 0) {
		$.each(colDefs, function (i, item) {
			if (item.title) {
				if (item.title.indexOf(time) != -1) {
					orderByDef.push(i-1);// 減去第0個預設排序用的欄位定義項目
					return false;// break;
				} else if (item.title.indexOf(date) != -1) {
					orderByDef.push(i-1);// 減去第0個預設排序用的欄位定義項目
					return false;// break;
				}
			}
		});
	}

	if (orderByDef.length == 0) {
		orderByDef.push(0);
	}

	orderByDef.push('DESC');
	
	return orderByDef;
}

function getOrderByCols (colDefs) {
	let temp;
	let orderByCols = [];
	
	$.each(colDefs, function () {
		if (this.data) {
			temp = this.data;
			temp = temp.replace(/\b[a-z]/g, function (letter) {
			    return letter.toUpperCase();
			});
			orderByCols.push(temp);
		}
	});
	
	return orderByCols;
}

function checkAll () {
	$('.columnView:not(:hidden)').each(function () {
		if (this.id == 'formIdBox') {
			return true;// continue
		}
		
		$(this).prop("checked", $('#checkAllSwitch').prop("checked"));
	})
}

function setColumn (field, clazz) {
	var necessaryField = field.isDefaultCol ? 'checked' : '';
	var isDisabled = field.voName == 'formId' ? 'disabled' : '';
	
	var column = '<tr>';
	column += '<th width="10%"><label><input id=' + field.voName + 'Box type="checkbox" class="columnView" ' + necessaryField + ' ' + isDisabled + '></label>&nbsp;&nbsp;';
	column += '<input class="order" type="text" style="width: 10px;" />&nbsp;&nbsp;<nobr><label for=' + field.voName + 'Box>' + field.name + '</label></nobr>';
	column += '</th>';
	column += '</tr>';
	column += '<tr>';
	column += columnType(field, clazz);
	column += '</tr>';

	// 新增子動態欄位
	if (('DC' == clazz && 'cType' == field.voName) || 
			('JOB_AP' == clazz && 'countersigneds' == field.voName) ||
			('JOB_AP_C' == clazz && 'apcCountersigneds' == field.voName)) {
		column += '<tr>';
		column += '<td><table id="' + field.voName + 'Cols" class="grid_query"></table></td>';
		column += '</tr>';
		
		if ('JOB_AP_C' == clazz) {
			column = column.replace('class="order"', 'class="order hidden"');
			column = column.replace('class="columnView"', 'class="columnView hidden"');
		}
	}

	if (allowFormClazzes.indexOf(clazz) >= 0) {
		$("#fieldsTable").append(column);
	} else if (subDCClazz.indexOf(clazz) >= 0) {
		$("#cTypeCols").append(column);
	} else {
		$("#countersignedsCols,#apcCountersignedsCols").append(column);
	}
}

function columnType(field, clazz){
	var column = '';
	
	switch (field.type) {
		case 'INPUT':
			if (field.index == '902' || field.index == '66' || field.index == '201' || field.index == '1005') { // associationForm or sourceId
				column = '<td width="100%"><input class="hidden" id=' + field.voName + ' name=' + field.voName + ' type="text" style="width: 20rem;" /></td>';
			} else {
				column = '<td width="100%"><input id=' + field.voName + ' name=' + field.voName + ' type="text" style="width: 20rem;" /></td>';
			}
			
			break;
		case 'RANGE':
			column = '<td width="100%"><input id="' + field.voName + 'Start" name="' + field.voName + 'Start" type="text" class="initDatePicker" style="width: 6rem;" readonly />';
			column += '<span class="date-model-split">~</span>';
			column += '<input id="' + field.voName + 'End" name="' + field.voName + 'End" type="text" class="initDatePicker" style="width: 6rem;" readonly /></td>';
			break;
		case 'SELECT':
			var column = '';
			var id = formIndexIdMap.get(field.index);

			if (subSelect.indexOf(id) < 0) {
				var subOptMeth = '';
				
				if (field.voName == 'sClass') {
					subOptMeth = 'onchange="getSSubClass(this.value)"';
				} else if (field.voName == 'cCategory') {
					subOptMeth = 'onchange="getCClass(this.value)"';
				} else if (field.voName == 'knowledge1') {
					subOptMeth = 'onchange="getKnowledge2(this.value)"';
				}

				column += '<td width="100%"><select id=' + field.voName + ' name=' + field.voName + ' '+ subOptMeth  +'>';
				
				if (clazz == 'JOB_AP_C' && id == 'countersignedsSelect') {
					id = 'apcCountersignedsSelect';
				}
				
				$("#" + id + " option").each(function () {
					column += '<option value=' + this.value + '>' + this.text + '</option>'
				});
				
				column += '</select></td>';
			} else {
				var subOptMeth = '';

				column += '<td width="100%">';
				
				if (field.voName == "cClass") {
					subOptMeth = 'onchange="getCComponent(this.value)"';
				}
				
				column += '<select id=' + field.voName + ' name=' + field.voName + ' ' + subOptMeth + '>';
				column += '<option value="">全部</option>';
				column += '</select></td>';
			}
			
			break;
	}

	return column;
}

function getTableColDef () {
	var noOrderCount = 0;
	var orderMap = new Map();   // 紀錄有排序的欄位
	var noOrderMap = new Map(); // 紀錄沒有排序的欄位
	
	$('#search').find('input[type="checkbox"].columnView:checked').each(function () {
		name = $(this).closest('tr').find('nobr').text();
		id = $(this).closest('tr').next('tr').find('td').find(':first').attr("id");

		// 將xxxxStart調整成xxxx
		if(id.match(timePattern)){
			id = id.replace(timePattern, "");
		}

		var formMap = new Map();
		formMap.set("id", id);
		formMap.set("name", name);
		
		let orderDom = $(this).closest('tr').find('th').find('input[class="order"]');
		if ($(orderDom).is(":visible")) order = $(orderDom).val();
		
		if (order) {
			orderMap.set(order, formMap);
		}else{
			noOrderMap.set(noOrderCount, formMap);
			noOrderCount++;
		}
	})
	
	// 排序:小->大
	var columns = [];
	var columnIdx = 0;
	var orderMapAsc = sortMap(orderMap);
	
	columns.push({orderable: false, targets: []});

	// 先排有輸入排序的欄位
	orderMapAsc.forEach(function (item, index, array) {
		genTableColDefs(columnIdx, columns, item);
		columnIdx++;
	})
	
	// 再排未輸入排序的欄位
	noOrderMap.forEach(function (item, index, array) {
		genTableColDefs(columnIdx, columns, item);
		columnIdx++;
	})
	
	return columns;
}

function genTableColDefs (columnIdx, columns, item) {
	let colLimit = 20;
	let id = item.get("id");
	let name = item.get("name");
	let isDateColumn = $("#" + id + "Start").attr("class") && 
			$("#" + id + "Start").attr("class").indexOf("initDatePicker") >= 0;
	
	let info = {};
	info["data"] = id;
	info["targets"] = columnIdx;
	info["className"] = 'dt-center';
	info["title"] = '<s:message code="xxxxxx" text="' + name + '" />';

	info["render"] = function (data, type, row) {
		if (toPageColumn.indexOf(id) >= 0) {// 表單編號加入連結
			data = '<a href="javascript: toForm(\'' + data + '\')">' + data + '</a>';
		} else if (isDateColumn) {// 調整時間格式
			data = DateUtil.toDateTime(data);
		} else if (data && id === 'knowledges') {
			let kJson = ObjectUtil.parse(data);
			data = '';
			$.each(kJson, function (index, item) {
				data += item.knowledge1Display + ' : ' + item.knowledge2Display
				if (index < kJson.length) data += '<br>';
			});
		} else if (data && id === 'solutions') {
			let formIds = data.split(',');
			data = '';
			$.each(formIds, function (index, formId) {
				data += '<a href="javascript: toForm(\'' + formId + '\')">' + formId + '</a><br>';
			});
		} else if (data && data.length > colLimit) {
			data = data.substring(0, colLimit) + '...';
		}
		
		return data;
	}
	
	columns.push(info);
}

<%-- 取得服務子類別  --%>
function getSSubClass(id){
	if (id) {
		SendUtil.get('/html/getSubDropdownList', id, function (options) {
			HtmlUtil.singleSelect('select#sSubClass', options);
		}, ajaxSetting);
	} else {
		HtmlUtil.emptySelect("#sSubClass");
	}
}

<%-- 檢查欄位顯示是否有勾選  --%>
function noViewCheck(){
	var viewMsg;
	
	if ($('input[type="checkbox"].columnView:checked').length == 0){
		viewMsg = '<s:message code="form.search.message.view.checkbox.not.click" text="未勾選顯示欄位!!" />';
	}

	return viewMsg;
}

<%-- 檢查排序是否有誤 --%>
function isErrorOrder(){
	var orderList = [];
	var errorMsg;

	$('#search').find('input[type="checkbox"].columnView:checked').each(function(){
		order = $(this).closest('tr').find('th').find('input[class="order"]').val();

		if (order){
			if(isNaN(order)) {
				errorMsg = '<s:message code="form.search.message.view.sort.not.correct" text="顯示欄位排序請輸入正確數字!!" />';
				return false;
			}
					
			if (orderList.indexOf(order)>=0) {
				errorMsg = '<s:message code="form.search.message.view.sort.the.same.value" text="顯示欄位排序不可輸入重複數字!!" />';
				return false;
			} else {
				orderList.push(order);
			}
		}
	})

	return errorMsg;
}

function cleanTable () {
	// 先清除datatable結構
	dataTable.destroy();
	// 再清除table資料
	$('#dataTable').empty();
}

function sortMap(dataMap){
	var keys = [];
	// Initialize your sorted maps object
	var sortedMap = new Map();

	// Put keys in Array
	dataMap.forEach(function callback(value, key, map) {
	    keys.push(key);
	});

	// Sort keys array and go through them to put in and put them in sorted map
	keys.sort().map(function(key) {
	    sortedMap.set(key, dataMap.get(key));
	});

	return sortedMap;
}

<%-- 下拉選單map, index請參考FormFieldsEnum, SR.class, Q.class, ...  --%>
var formIndexIdMap = new Map();
// 表單共通
formIndexIdMap.set(2, 'formStatusSelect');          //表單狀態
formIndexIdMap.set(8, 'unitCategorySelect');        //提出單位分類
												    
// 需求單 SR                                        
formIndexIdMap.set(16, 'sClassSelect');             //服務類別
formIndexIdMap.set(17, 'sSubClassSelect');          //服務子類別
formIndexIdMap.set(20, 'cCategorySelect');          //組態分類
formIndexIdMap.set(21, 'cClassSelect');             //組態元件類別
formIndexIdMap.set(22, 'cComponentSelect');         //組態元件
formIndexIdMap.set(23, 'effectScopeSelect');        //影響範圍
formIndexIdMap.set(24, 'srUrgentLevelSelect');      //緊急程度
formIndexIdMap.set(27, 'countersignedsSelect');     //會辦科
												    
// 問題單 Q                                         
formIndexIdMap.set(28, 'questionIdSelect');         //問題來源
formIndexIdMap.set(32, 'YNSelect');                 //特殊結案
formIndexIdMap.set(33, 'SpecialEndCaseTypeSelect'); //特殊結案狀態
formIndexIdMap.set(39, 'sClassSelect');             //服務類別
formIndexIdMap.set(40, 'sSubClassSelect');          //服務子類別
formIndexIdMap.set(43, 'cCategorySelect');          //組態分類
formIndexIdMap.set(44, 'cClassSelect');             //組態元件類別
formIndexIdMap.set(45, 'cComponentSelect');         //組態元件
formIndexIdMap.set(46, 'effectScopeSelect');        //影響範圍
formIndexIdMap.set(47, 'qUrgentLevelSelect');       //緊急程度
formIndexIdMap.set(48, 'countersignedsSelect');     //會辦科
formIndexIdMap.set(49, 'knowledge1Select');  	    //知識庫原因類別
formIndexIdMap.set(50, 'knowledge2Select');  	    //知識庫原因子類別
formIndexIdMap.set(804,'YNSelect');                 //是否建議加入處理方案
												    
// 事件單 INC                                       
formIndexIdMap.set(51, 'eventClassSelect');         //事件主類別
formIndexIdMap.set(53, 'eventSecuritySelect');      //資安事件
formIndexIdMap.set(55, 'YNSelect');                 //設定為主要事件單
formIndexIdMap.set(57, 'YNSelect');                 //全部功能服務中斷
formIndexIdMap.set(63, 'sClassSelect');             //服務類別
formIndexIdMap.set(64, 'sSubClassSelect');          //服務子類別
formIndexIdMap.set(67, 'cCategorySelect');          //組態分類
formIndexIdMap.set(68, 'cClassSelect');             //組態元件類別
formIndexIdMap.set(69, 'cComponentSelect');         //組態元件
formIndexIdMap.set(70, 'effectScopeSelect');        //影響範圍
formIndexIdMap.set(71, 'incUrgentLevelSelect');     //緊急程度
formIndexIdMap.set(72, 'countersignedsSelect');     //會辦科
formIndexIdMap.set(73, 'YNSelect');                 //事件來源為IVR
formIndexIdMap.set(805, 'YNSelect');                //暫時性解決方案，且無法於事件目標解決時間內根本解決者?
formIndexIdMap.set(1000, 'YNSelect');    	        //變更成功失敗
formIndexIdMap.set(1001, 'YNSelect');    	        //同一事件兩日內復發
formIndexIdMap.set(1002, 'YNSelect');    	        //上線失敗
formIndexIdMap.set(1003, 'onlineTime');    	        //上線時間
formIndexIdMap.set(1004, 'onlineJobFormId');        //(上線失敗的)工作單單號
												    
// 變更單 CHG                                       
formIndexIdMap.set(3000, 'cCategorySelect');        //組態分類
formIndexIdMap.set(3001, 'cClassSelect');           //組態元件類別
formIndexIdMap.set(3002, 'cComponentSelect');       //組態元件
formIndexIdMap.set(3003, 'YNSelect');               //是新系統
formIndexIdMap.set(74, 'YNSelect');                 //是新服務暨重大服務
formIndexIdMap.set(75, 'YNSelect');                 //緊急變更
formIndexIdMap.set(76, 'standardSelect');           //標準變更作業
formIndexIdMap.set(77, 'CTSelect');                 //變更類型
formIndexIdMap.set(78, 'CLSelect');                 //變更等級
formIndexIdMap.set(79, 'YNSelect');                 //有新增異動欄位影響到資料倉儲系統產出資料
formIndexIdMap.set(80, 'YNSelect');                 //有新增異動會計科目影響到資料倉儲系統產出資料
formIndexIdMap.set(89, 'YNSelect');                 //未有修改程式
												    
// 批次作業中斷對策表管理                           
formIndexIdMap.set(81, 'batchName');			    //批次工作名稱
formIndexIdMap.set(82, 'summary');				    //作業名稱描述
formIndexIdMap.set(83, 'executeTime');			    //執行時間
formIndexIdMap.set(84, 'dbInUse');				    //使用資料庫
formIndexIdMap.set(85, 'effectDate');			    //生效日期
formIndexIdMap.set(86, 'division');				    //負責科
												    
// 工作單 JOB                                       
formIndexIdMap.set(202, 'sClassSelect');            //服務類別
formIndexIdMap.set(203, 'sSubClassSelect');         //服務子類別
formIndexIdMap.set(205, 'CTSelect');                //變更類型
formIndexIdMap.set(206, 'CLSelect');                //變更等級
formIndexIdMap.set(207, 'YNSelect');                //先處理後呈閱
formIndexIdMap.set(208, 'YNSelect');                //上線修正
formIndexIdMap.set(209, 'YNSelect');                //新增系統功能
formIndexIdMap.set(210, 'YNSelect');                //新增報表
formIndexIdMap.set(211, 'YNSelect');                //新增檔案
formIndexIdMap.set(228, 'YNSelect');    	        //程式上線
formIndexIdMap.set(215, 'YNSelect');                //送交監督人員
formIndexIdMap.set(216, 'countersignedsSelect');    //會辦科
formIndexIdMap.set(217, 'YNSelect');                //會造成服務中斷或需要停機，屬於計畫性系統維護
formIndexIdMap.set(218, 'YNSelect');                //會造成服務中斷或需要停機，屬於非計畫性系統維護
formIndexIdMap.set(232, 'YNSelect');                //回復原廠設定
formIndexIdMap.set(233, 'YNSelect');                //TEST
formIndexIdMap.set(234, 'YNSelect');                //PRODUCTION
formIndexIdMap.set(235, 'YNSelect');                //先處理後呈閱
formIndexIdMap.set(236, 'YNSelect');                //是否需送交組態維護人員
formIndexIdMap.set(237, 'YNSelect');                //是否造成服務中斷或需要停機
formIndexIdMap.set(238, 'workingSelect');           //工作組別
formIndexIdMap.set(239, 'cCategorySelect');         //組態分類
formIndexIdMap.set(240, 'cClassSelect');            //組態元件類別
formIndexIdMap.set(241, 'cComponentSelect');        //組態元件
formIndexIdMap.set(255, 'YNSelect');                //未有修改程式
												    
// 一般會辦單                                       
formIndexIdMap.set(883, 'sClassSelect');            //服務類別
												    
// 工作會辦單                                       
formIndexIdMap.set(2006, 'YNSelect');          	    //TEST
formIndexIdMap.set(2007, 'YNSelect');          	    //PRODUCTION
formIndexIdMap.set(2010, 'YNSelect');          	    //回復到前一版
formIndexIdMap.set(2027, 'YNSelect');          	    //
formIndexIdMap.set(2038, 'YNSelect');          	    //
formIndexIdMap.set(2039, 'YNSelect');          	    //
formIndexIdMap.set(2040, 'YNSelect');          	    //執行HELP程式
formIndexIdMap.set(2041, 'YNSelect');          	    //允許JOB在TWS執行時，Return Code <=4視為正常
formIndexIdMap.set(2042, 'YNSelect');          	    //其他
formIndexIdMap.set(2043, 'YNSelect');          	    //HELP程式CL
formIndexIdMap.set(2051, 'YNSelect');          	    //
formIndexIdMap.set(2011, 'DCSelect');          	    //資料管制科的會辦項目下拉選單
formIndexIdMap.set(247,  'YNSelect');				//程式上線
</script>
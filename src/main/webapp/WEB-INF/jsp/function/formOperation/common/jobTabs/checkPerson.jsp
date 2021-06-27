<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script>//# sourceURL=checkPerson.js
var dataTable;

$(function () {
	initView();
	initEvent();
	authViewControl();
});

function initView(){
	var table = '#checkPersonForm table#table';
	var options = {
		columnDefs: [
			{targets: '_all', orderable: false},
			{targets: [0], title: '流程順序', data: 'processOrder', className: 'dt-center', width:'30%',
	            render: function (data, type, full, meta) {
	            	return full.processOrder ? data : full.order;
            	}
            },
			{targets: [1], title: '流程群組', data: 'groupName', className: 'dt-center', width:'30%'},
			{ targets: [2], title: '作業關卡', data: 'workProject', className: 'dt-center', width:'30%'},
			{
	        	targets: [3],
				title: '作業人員',
				width:'10%',
				className: 'dt-left',
	            render: function (data, type, full, meta) {
					full.name = full.name ? full.name : '';
					full.userId = full.userId ? full.userId : '';
					full.workValue = full.workValue ? full.workValue : full.level;
					full.processOrder = full.processOrder ? full.processOrder : full.order;
					
					data = '<input type="text" style="width: 10rem;" class="displayValue" name="checkPersonList['+meta.row+'].name" value="'+full.name+'" readonly="readonly" >';
					data += '<input type="hidden"  name="checkPersonList['+meta.row+'].level" value="'+full.workValue+'" >';
					data += '<input type="hidden"  name="checkPersonList['+meta.row+'].groupId" value="'+full.groupId+'" >';
					data += '<input type="hidden"  name="checkPersonList['+meta.row+'].order" value="'+full.processOrder+'" >';
					data += '<input type="hidden"  name="checkPersonList['+meta.row+'].groupName" value="'+full.groupName+'" >';
					data += '<input type="hidden"  name="checkPersonList['+meta.row+'].workProject" value="'+full.workProject+'" >';
					data += '<input type="hidden" class="hideValue" name="checkPersonList['+meta.row+'].userId" value="'+full.userId+'" >';
					data += '<button type="button" class="selectUser" style="margin-left:10px"><i class="iconx-search"></i><s:message code="global.select.user" /></button>';
					data += '<button type="button" class="checkPersonClearButton" onclick="clearBtn(this);"><i class="iconx-refresh"></i><s:message code="button.clear" /></button>';
					return data;
	            }
	        }
		],
		paging: false,
		ordering: false,
		info: false,
		pageLength: -1
	};

	if (HtmlUtil.tempData.checkPersonForm && 
			HtmlUtil.tempData.checkPersonForm.checkPersonList && 
			HtmlUtil.tempData.checkPersonForm.checkPersonList.length > 0) {
		options.data = HtmlUtil.tempData.checkPersonForm.checkPersonList;
	} else {
		SendUtil.post('/commonJobForm/getCheckPerson', form2object('headForm'), function (resData) {
			options.data = resData.checkPersionList;
		}, ajaxSetting);
	}

	dataTable = TableUtil.init(table, options);
}

function initEvent () {
	$('body').on('click', '.selectUser', function() {
		var postData = {};
		postData["formId"] = $("#formId").text();
		postData["groupName"] = $(this).closest("tr").find('td:nth-child(2)').text();
		postData["processOrder"] = $(this).closest("tr").find('td:nth-child(1)').text();
		postData = $.extend(postData,form2object('headForm'));
		UserDialog.show($(this), postData, callback);
    });
}

function clearBtn (target) {
	let parent = $(target).parent();
	parent.find(".hideValue").val("");
	parent.find(".displayValue").val("");
}

function callback (row, selected) {
	row.find(".hideValue").val(selected.value);
	row.find(".displayValue").val(selected.display);
	HtmlUtil.tempData.infoForm.cuserId = selected.display; 
}
</script>
<form id='checkPersonForm'>
	<fieldset>
		<legend><s:message code="form.process.managment.gate.user.setting" /></legend>
		<table id="table" class="display collapse nowrap cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
</form>
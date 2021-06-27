<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<script>//# sourceURL=spcModel.js
/**
 * 會辦系統科群組
 */
var SpcModel = function () {
	
	var spcGroupsAry = [];
	var formInfo, spcGroupTable;
	
	var init = function () {
		SendUtil.post('/html/getFormCParallels', form2object('headForm'), function (response) {
			let options = {
				checkbox: true,
				data: response,
				columnDefs: [
					{orderable: false, targets: [0]},  
					{targets: [0], data: 'value', className: 'dt-center hidden'},
				    {targets: [1], title: '群組名稱', data: 'wording', width: '30%', className: 'dt-center',
						render: function (data, type, full, meta) {
							return '<label for="' + full.value + 'Id">' + data + '</label>';
						}},
				    {targets: [2], title: '會辦科處理人員編號', data: 'userId', className: 'dt-center', 
				    	render: function (data) {
				    		return '<input id="userId" name="userId" type="text" style="width: 10rem;" readonly="readonly" value="' + (data ? data : '') + '" >';
				    	}}, 
				    {targets: [3], title: '會辦科處理人員姓名', data: 'userName', className: 'dt-center', 
				    	render: function (data) {
				    		return '<input id="userName" name="userName" type="text" style="width: 10rem;" readonly="readonly" value="' + (data ? data : '') + '" >';
				    	}}, 
				    {targets: [4], title: '功能', data: '', className: 'dt-center', 
				    	render: function (data) {
				    		data = '<button type="button" onclick="SpcModel.selectUser(this)">清單</button>';
				    		data += '<button type="button" onclick="SpcModel.deleteUser(this)">刪除</button>';
				    		return data;
				    	}}
				],
				paging: false,
				ordering: false,
				info: false,
				pageLength: -1
		    };
			TableUtil.deleteTable('#spcGroupTable');
			spcGroupTable = TableUtil.init('#spcGroupTable', options);
			
			formInfo = form2object('headForm');
			if (formInfo.formStatus) {
				SendUtil.post(getFormDomain() + '/info', formInfo, function (response) {
					let tempData = fetchTempData(response.spcGroups);

					if (tempData && tempData.length > 0) {
						$('input#spcGroups').val(tempData);
						spcGroupsAry = ObjectUtil.parse(tempData);
						fillGroups();
					}
				}, ajaxSetting);
			}
		}, ajaxSetting);
	}

	var selectUser = function (element) {
		let request = form2object('headForm');
		let rowData = TableUtil.getRow(spcGroupTable, element);
		
		request.groupName = '系統科群組';
		request.subGroup = rowData.value;
		UserDialog.show($(element), request, function (row, selected) {
			row.find("input[name='userId']").val(selected.value);
			row.find("input[name='userName']").val(selected.display);
			updateGroups(row, true);
		});
	}

	var deleteUser = function (element) {
		updateGroups($(element), false);
		$(element).closest('tr').find('input#userId').val('');
		$(element).closest('tr').find('input#userName').val('');
	}
	
	function fetchTempData (realData) {
		let tempData;
		
		if (HtmlUtil.tempData.infoForm) {
			tempData = HtmlUtil.tempData.infoForm.spcGroups;
		}
		
		return tempData ? tempData : realData;
	}

	function fillGroups () {
		SendUtil.post('/html/getFormCParallels', form2object('headForm'), function (response) {
			$.each(response, function (i, toView) {
				$.each(spcGroupsAry, function (j, fromDB) {
					if (toView.value == fromDB.value) {
						response[i] = $.extend(toView, fromDB);
					}
				});
			});
			TableUtil.reDraw(spcGroupTable, response);	
		}, ajaxSetting);
	}

	function updateGroups (element, isAdd) {
		let row = getRow(element);

		ObjectUtil.clear(row);
		spcGroupsAry = arrayRemove(spcGroupsAry, row);
		
		if (isAdd) {
			spcGroupsAry.push(row);
		}
		
		$('input#spcGroups').val(ObjectUtil.stringify(spcGroupsAry));
	}

	function arrayRemove (arr, target) {
		return $.grep(arr, function(element, i) {
		    return element.value != target.value;
		});
	}

	function getRow (element) {
		let options = {
			userId : 'input',
			userName : 'input',
		};
		
		if (!spcGroupTable) {
			init();
		}
		
		return TableUtil.getRow(spcGroupTable, $(element), options);
	}

	return {
		init : init,
		selectUser : selectUser, 
		deleteUser : deleteUser
	};
}();

$(function () {
	SpcModel.init();
	authViewControl();
});
</script>

<table id='spcGroupTable' class="display collapse cell-border nowrap">
	<thead></thead>
	<tbody></tbody>
</table>
<input id='spcGroups' class='hidden' type='text' name='spcGroups' />
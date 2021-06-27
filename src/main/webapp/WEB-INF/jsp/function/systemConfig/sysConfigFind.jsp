﻿<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/global.jsp"%>

	<script>

		var list = ObjectUtil.parse('${list}');
		var dataTable;
		var rowData;
		var ajaxOption = {async : false};

		$(function(){
			var options = {
				data: list,
				columnDefs: [
					{targets: [0], title: '<s:message code="sysconfig.header.paramKey" text='參數' />', data: 'paramKey', className: 'text-left'},
					{targets: [1], title: '<s:message code="sysconfig.header.paramValue" text='設定值' />', data: 'paramValue', className: 'dt-center',
						render: function (data, type, row, meta) {
							data = $.isEmptyObject(data) ? "" : data;
							if(row.isPassword === 'Y'){
								return "<input type='password' name='paramValue' value='" + data +"' />";
							} else {
								return "<input type='text' name='paramValue' value='" + data +"' />";
							}
						}},
					{targets: [2], title: '<s:message code="sysconfig.header.isPasword" text='密碼欄位' />', data: 'isPassword', className: 'dt-center',
						render: function (data) {
							if(data === 'Y'){
								return "<input type='checkbox' name='isPsw' checked='checked' onClick='transferPa55word(this);'/>";
							} else {
								return "<input type='checkbox' name='isPsw' onClick='transferPa55word(this);'/>";
							}
						}},
					{targets: [3], title: '<s:message code="sysconfig.header.description" text='描述' />', data: 'description', className: 'dt-center',
						render: function (data) {
							data = $.isEmptyObject(data) ? "" : data;
							return "<input type='text' name='description' value='" + data +"' />";
						}},
					{targets: [4], title: '<s:message code="sysconfig.header.updateAt" text='修改時間' />', data: 'updateAt', className: 'dt-center'},
					{targets: [5], title: '<s:message code="sysconfig.header.updateBy" text='修改人員' />', data: 'updateBy', className: 'dt-center'},
					{targets: [6], title: '', data: 'paramId', className: 'dt-center',
						render: function (data, type, row, meta ) {
							return "<button type='button' value='" + data + "' onClick='save(this);'><i class='iconx-save'></i> <s:message code="button.save" text='儲存' /></button>";
						}}
				]
			};
			dataTable = TableUtil.init('#dataTable', options);

			<%-- 查詢按鈕--%>
			$('#searchBtn').click(function(){
				SendUtil.post('/system/systemConfig/search', form2object('search'), function (data) {
					TableUtil.reDraw(dataTable, data);
				},ajaxOption,true);
			});

			<%-- 同步按鈕--%>
			$('#syncBtn').click(function(){
				SendUtil.post('/system/systemConfig/syncParameterList', {}, function (data) {
					if(!!data){
						TableUtil.reDraw(dataTable, data);
						alert('<s:message code="sysconfig.sync.success" text='同步成功' />');
					}
				},ajaxOption,true);
			});

		});

		<%-- 每列儲存按鈕 --%>
		function save(obj){
			if(confirm("<s:message code='form.process.managment.add.page.function.confirm.1' text='是否確定儲存?' />")) {
				var param = getRowData(obj);
				SendUtil.post('/system/systemConfig/updateParameter', param, function (data) {
					if(!!data){
						TableUtil.reDraw(dataTable, data);
						alert('<s:message code="global.save.success" text='儲存成功' />');
					}
				},ajaxOption,true);
			}
		}

		<%-- 密碼改變 --%>
		function transferPa55word(obj){
			var $input = $(obj).closest('tr').find('input[name="paramValue"]');
			$input.val('');
			if($(obj).is(':checked')){
				$input.attr('type', 'password');
			}else{
				$input.attr('type', 'text');
			}
		}

		<%-- 取得每列參數 --%>
		function getRowData (obj) {
			// 宣告
			var $tr = $(obj).closest('tr');
			var param = {};
			// 塞入每列參數
			param['paramValue'] = $tr.find('input[name="paramValue"]').val();
			param['description'] = $tr.find('input[name="description"]').val();
			param['paramId'] = $(obj).val();
			if($tr.find('input[name="isPsw"]').is(':checked')){
				param['isPassword'] = '1';
			}else{
				param['isPassword'] = '0';
			}
			return param;
		}

	</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>
<h1><s:message code="sysconfig.title" text='系統參數設定' /></h1>

<fieldset class="search">
	<legend><s:message code="sysconfig.query.condition" text='查詢條件' /></legend>

	<button class="small fieldControl searchPanel">
		<i class="iconx-collapse"></i>|||
	</button>

	<form id="search">
		<table class="grid_query">
			<tr>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="sysconfig.param" text='參數' /></th>
				<td>
					<input type="text" name="paramKey" style="width: 20rem;" />
				</td>
				<td>
					<button type="button" id="searchBtn">
						<i class="iconx-search"></i>
						<s:message code="sysconfig.button.query" text='查詢' />
					</button>
					<button type="button" id="syncBtn">
						<i class="iconx-add"></i>
						<s:message code="sysconfig.button.sync" text='同步' />
					</button>
				</td>
			</tr>
		</table>
	</form>
</fieldset>

<fieldset>
	<legend><s:message code="sysconfig.query.result" text='查詢結果' /></legend>
	<table id="dataTable" class="display collapse cell-border">
		<thead></thead>
		<tbody></tbody>
	</table>
</fieldset>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>
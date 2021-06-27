<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script>
//開啟新增視窗
var StandardChangeDialogAdd = function () {
	
	var show = function () {
		var dialog = 'div#divDialogStandardAdd';
		var systemOpt = {
			modal : true,
			width : 600,
			height : 210
		};		
		DialogUtil.show(dialog, systemOpt);
		$(dialog + ' input').val(""); //將視窗前一次輸入的值清空
	}	
	
	return {
		show : show,
	}
}();

//開啟編輯視窗
var StandardChangeDialogEdit = function () {
	
	var show = function (id) {
		var dialog = 'div#divDialogStandardEdit';
		var systemOpt = {
			modal : true,
			width : 600,
			height : 210
		};		
		DialogUtil.show(dialog, systemOpt);

		var editInfo = {};
		var listInfo = TableUtil.getRows(dataTable, {page:'all'});
		listInfo.forEach(function(element) {
			if(element.id == id) {
				var index = listInfo.indexOf(element);
				editInfo = listInfo[index];
				return false;
			}
		});
		fillForm(editInfo);
	}
	
	function fillForm(data) {
		$("#divDialogStandardEdit input[name='id']").val(data.id);
		$("#divDialogStandardEdit input[name='display']").val(data.display);
		$("#divDialogStandardEdit input[name='active']").attr('checked', false);
		$("#divDialogStandardEdit input[name='active'][value='" + data.active + "']").click();
	}
	
	return {
		show : show,
	}
}();
</script>

<c:if test="${param.isAdding}">
<script>
//標準變更作業管理_新增對話框
<%-- 新增視窗_儲存按鈕 --%>
function createData() {
	let params = form2object("divDialogStandardAdd");
	
	if(!validate(params)) {
		return;
	}
	
	SendUtil.post('/standardChangeOperation/init/createData', params, function(data) {
		PurifyUtil.obj(divDialogStandardAdd);
		alert("儲存成功");
		$("#divDialogStandardAdd").dialog("close");
		$("#divDialogStandardAdd input").val("");
		$("#searchBtn").click();
	}, null, true);
}
</script>
</c:if>

<c:if test="${param.isEditing}">
<script>
//標準變更作業管理_編輯對話框
<%-- 編輯視窗 _儲存按鈕 --%>
function updateData() {
	let params = form2object("divDialogStandardEdit");
	params.active = $("#divDialogStandardEdit input[name='active']:checked").val();
	
	if(!validate(params)) {
		return;
	}
	
	SendUtil.post('/standardChangeOperation/init/updateData', params, function(data) {					
		var listInfo = TableUtil.getRows(dataTable, {page:'all'});
		listInfo.forEach(function(element) {
			if(element.id == params.id) {
				index = listInfo.indexOf(element);
				params.id = parseInt(params.id);
				listInfo[index] = params;
				return false;
			}
		});
		alert('編輯成功');			
		$("#divDialogStandardEdit").dialog('close');
		$('#searchBtn').click();
	}, null, true);
}
</script>
</c:if>

<c:if test="${param.isAdding}">
	<div style="display: none;" id="divDialogStandardAdd">
		<form id='addStandardForm'>
</c:if>
<c:if test="${param.isEditing}">
	<div style="display: none;" id="divDialogStandardEdit">
		<form id='editStandardForm'>
		   	<input type="hidden" name="id"/>
</c:if>
		<fieldset>
			<legend>
				<c:if test="${param.isAdding}">新增標準變更作業</c:if>
				<c:if test="${param.isEditing}">編輯標準變更作業</c:if>
			</legend>
			<div class="grid_BtnBar">
				<button type="button" 
					<c:if test="${param.isAdding}">onclick="createData();"</c:if>
					<c:if test="${param.isEditing}">onclick="updateData();"</c:if>
				>
					<i class="icon-save"></i>儲存
				</button>
			</div>
			<table id="standardChangeList" class="grid_query">
				<tr>
					<td><font color="red">標準變更作業名稱:</font></td>
					<td><input type="text" name="display"  
						style="width: 25rem" value="" maxlength="500"/></td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td><font color="red">狀態:</font></td>
					<td>
						<label>
							<input type="radio" name='active' value='Y' checked />
							<s:message code="standard.change.edit.label.enable" text='啟用' />
						</label>
						<label>
							<input type="radio" name='active' value='N'/>
							<s:message code="standard.change.edit.label.disable" text='停用' />
						</label>
					</td>
				</tr>
			</table>
		</fieldset>
		<input type="hidden" name="standardChangeOperation" value="">
	</form>
</div>
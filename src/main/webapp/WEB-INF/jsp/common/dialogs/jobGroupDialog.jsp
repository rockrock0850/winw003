<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script>
//開啟新增視窗
var JobGroupDialogAdd = function () {
	
	var show = function () {
		var dialog = 'div#divDialogJobGroupAdd';
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
var JobGroupDialogEdit = function () {
	
	var show = function (id) {
		var dialog = 'div#divDialogJobGroupEdit';
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
		$("#divDialogJobGroupEdit input[name='id']").val(data.id);
		$("#divDialogJobGroupEdit input[name='display']").val(data.display);
		$("#divDialogJobGroupEdit input[name='active']").attr('checked', false);
		$("#divDialogJobGroupEdit input[name='active'][value='" + data.active + "']").click();
	}
	
	return {
		show : show,
	}
}();
</script>

<c:if test="${param.isAdding}">
<script>
//工作組別管理_新增對話框
<%-- 新增視窗_儲存按鈕 --%>
function createData() {
	let params = form2object("divDialogJobGroupAdd");
	
	if(!validate(params)) {
		return;
	}
	
	SendUtil.post('/jobGroupManagement/init/createData', params, function(data) {
		PurifyUtil.obj(divDialogJobGroupAdd);
		alert("儲存成功");			
		$("#divDialogJobGroupAdd").dialog("close");
		$('#divDialogJobGroupAdd input').val("");
		$('#searchBtn').click();
	}, null, true);
}
</script>
</c:if>

<c:if test="${param.isEditing}">
<script>
//工作組別管理_編輯對話框
<%-- 編輯視窗 _儲存按鈕 --%>
function updateData() {
	let params = form2object("divDialogJobGroupEdit");
	params.active = $("#divDialogJobGroupEdit input[name='active']:checked").val();
	
	if(!validate(params)) {
		return;
	}
	
	SendUtil.post('/jobGroupManagement/init/updateData', params, function(data) {					
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
		$("#divDialogJobGroupEdit").dialog('close');
		$('#searchBtn').click();
	}, null, true);
}
</script>
</c:if>

<c:if test="${param.isAdding}">
	<div style="display: none;" id="divDialogJobGroupAdd">
		<form id='addJobGroupForm'>
</c:if>
<c:if test="${param.isEditing}">
	<div style="display: none;" id="divDialogJobGroupEdit">
		<form id='editJobGroupForm'>
		   	<input type="hidden" name="id"/>
</c:if>
		<fieldset>
			<legend>
				<c:if test="${param.isAdding}">新增工作組別</c:if>
				<c:if test="${param.isEditing}">編輯工作組別</c:if>
			</legend>
			<div class="grid_BtnBar">
				<button type="button" 
					<c:if test="${param.isAdding}">onclick="createData();"</c:if>
					<c:if test="${param.isEditing}">onclick="updateData();"</c:if>
				>
					<i class="icon-save"></i>儲存
				</button>
			</div>
			<table id="jobGroupList" class="grid_query">
				<tr>
					<td><font color="red">工作組別名稱:</font></td>
					<td>
					  <c:if test="${param.isAdding}">
						<input type="text" name="display" style="width: 25rem" value="" maxlength="500"/>
					  </c:if>
					  <c:if test="${param.isEditing}">
						<input type="text" name="display" style="width: 25rem" value="" maxlength="500"/>
					  </c:if>
					</td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td><font color="red">狀態:</font></td>
					<td>
						<label>
							<input type="radio" name='active' value='Y' checked />
							<s:message code="job.gruop.edit.label.enable" text='啟用' />
						</label>
						<label>
							<input type="radio" name='active' value='N'/>
							<s:message code="job.gruop.edit.label.disable" text='停用' />
						</label>
					</td>
				</tr>
			</table>
		</fieldset>
		<input type="hidden" name="jobGroupManagement" value="">
	</form>
</div>
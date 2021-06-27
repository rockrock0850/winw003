<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script>
//開啟新增視窗
var JobItemDialogAdd = function () {
	
	var show = function () {
		var dialog = 'div#divDialogJobItemAdd';
		var systemOpt = {
			modal : true,
			width : 750,
			height : 240
		};		
		DialogUtil.show(dialog, systemOpt);
		$(dialog + ' input').val(""); //將視窗前一次輸入的值清空
	}	
	
	return {
		show : show
	}
}();

//開啟編輯視窗
var JobItemDialogEdit = function () {
	
	var show = function (id) {
		var dialog = 'div#divDialogJobItemEdit';
		var systemOpt = {
			modal : true,
			width : 750,
			height : 240
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
		$("#divDialogJobItemEdit input[name='id']").val(data.id);
		$("#divDialogJobItemEdit input[name='workingItemName']").val(data.workingItemName);
		$("#divDialogJobItemEdit select[name='spGroup']").val(data.spGroup); 
		$("#divDialogJobItemEdit select[name='isImpact']").val(data.isImpact);
		$("#divDialogJobItemEdit select[name='isReview']").val(data.isReview);
		$("#divDialogJobItemEdit input[name='active']").attr('checked', false);
		$("#divDialogJobItemEdit input[name='active'][value='" + data.active + "']").click();
	}
	
	return {
		show : show
	}
}();
</script>

<c:if test="${param.isAdding}">
<script>
//工作要項管理_新增對話框
<%-- 新增視窗_儲存按鈕 --%>
function createData() {
	let params = form2object("divDialogJobItemAdd");
	let verifyResult = ValidateUtil.formFields('/jobItemManagement/init/validateColumnData', params);

	if (verifyResult) {
		alert(verifyResult);
		return;
	}
	
	SendUtil.post('/jobItemManagement/init/createData', params, function(data) {
		PurifyUtil.obj(divDialogJobItemAdd);
		alert("儲存成功");			
		$("#divDialogJobItemAdd").dialog("close");
		$('#divDialogJobItemAdd input').val("");
		$('#divDialogJobItemAdd select').val("");
		$('#searchBtn').click();
	}, null, true);
}
</script>
</c:if>

<c:if test="${param.isEditing}">
<script>
//工作要項管理_編輯對話框
<%-- 編輯視窗 _儲存按鈕 --%>
function updateData() {
	let params = form2object("divDialogJobItemEdit");
	params.active = $("#divDialogJobItemEdit input[name='active']:checked").val();
	let verifyResult = ValidateUtil.formFields('/jobItemManagement/init/validateColumnData', params);

	if (verifyResult) {
		alert(verifyResult);
		return;
	}
	
	SendUtil.post('/jobItemManagement/init/updateData', params, function(data) {					
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
		$("#divDialogJobItemEdit").dialog('close');
		$('#searchBtn').click();
	}, null, true);
}
</script>
</c:if>

<c:if test="${param.isAdding}">
	<div style="display: none;" id="divDialogJobItemAdd">
		<form id='addJobItemForm'>
</c:if>
<c:if test="${param.isEditing}">
	<div style="display: none;" id="divDialogJobItemEdit">
		<form id='editJobItemForm'>
		   	<input type="hidden" name="id"/>
</c:if>
		<fieldset>
			<legend>
				<c:if test="${param.isAdding}">新增工作要項</c:if>
				<c:if test="${param.isEditing}">編輯工作要項</c:if>
			</legend>
			<div class="grid_BtnBar">
				<button type="button" 
					<c:if test="${param.isAdding}">onclick="createData();"</c:if>
					<c:if test="${param.isEditing}">onclick="updateData();"</c:if>
				>
					<i class="icon-save"></i>儲存
				</button>
			</div>
			<table id="jobItemList" class="grid_query">
				<tr>
					<td><font color="red">工作要項名稱:</font></td>
					<td><input type="text" name="workingItemName"  
						style="width: 20rem" value="" maxlength="100"
						<c:if test="${param.isEditing}">disabled</c:if>
						/>
					</td>
					<td><font color="red">系統科組別:</font></td> 
					<td>
						<select name="spGroup">
							<option value=""><s:message code="global.select.please.choose" text='請選擇' /></option>
							<option value="DC">DC</option>
							<option value="IMS/CICS">IMS/CICS</option>
							<option value="MQ">MQ</option>
							<option value="MVS/AS400">MVS/AS400</option>
							<option value="NT">NT</option>
							<option value="R6">R6</option>
						</select>
					</td>
				</tr>
				<tr>
					<td><font color="red">變更衝擊分析:</font></td>
					<td>
						<select name="isImpact">
							<option value=""><s:message code="global.select.please.choose" text='請選擇' /></option>
							<option value="Y">Y</option>
							<option value="N">N</option>
						</select>
					</td>
					<td><font color="red">變更覆核:</font></td>
					<td>
						<select name="isReview">
							<option value=""><s:message code="global.select.please.choose" text='請選擇' /></option>
							<option value="Y">Y</option>
							<option value="N">N</option>
						</select>
					</td>
				</tr>
				<tr>
					<td><font color="red">狀態:</font></td>
					<td>
						<label>
							<input type="radio" name='active' value='Y' checked />
							<s:message code="job.item.edit.label.enable" text='啟用' />
						</label>
						<label>
							<input type="radio" name='active' value='N'/>
							<s:message code="job.item.edit.label.disable" text='停用' />
						</label>
					</td>
				</tr>
			</table>
		</fieldset>
		<input type="hidden" name="jobItemManagement" value="">
	</form>
</div>
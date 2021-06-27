<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script>
var SystemNameDialogAdd = function () {
	
	var show = function () {
		var dialog = 'div#divDialogSystemAdd';
		var systemOpt = {
			modal : true,
			width : 750,
			height : 280
		};		
		DialogUtil.show(dialog, systemOpt);
		$(dialog + ' input').val(""); //將視窗前一次輸入的值清空
		$(dialog + ' select').val("");
	}	
	
	return {
		show : show,
	}
}();

var SystemNameDialogEdit = function () {
	
	var show = function (id) {
		var dialog = 'div#divDialogSystemEdit';
		var systemOpt = {
			modal : true,
			width : 750,
			height : 280
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
		$("#divDialogSystemEdit input[name='id']").val(data.id);
		$("#divDialogSystemEdit input[name='systemId']").val(data.systemId);
		$("#divDialogSystemEdit input[name='systemName']").val(data.systemName);
		$("#divDialogSystemEdit select[name='department']").val(findDepData(data));
		$("#divDialogSystemEdit input[name='mark']").val(data.mark);
		$("#divDialogSystemEdit input[name='opinc']").val(data.opinc);
		$("#divDialogSystemEdit input[name='apinc']").val(data.apinc);
		$("#divDialogSystemEdit input[name='limit']").val(data.limit);
		$("#divDialogSystemEdit input[name='active']").attr('checked', false);
		$("#divDialogSystemEdit input[name='active'][value='" + data.active + "']").click();
	}

	//將第一次點擊編輯視窗時，未帶入的科別取回
	function findDepData(data) {
		var selected = '';
		var options = $("#divisionCreated option");	
		
		if (data) {
			$.each(options, function (i, stage) {
				if ($(stage).attr('value').indexOf(data.department) != -1) { //比對dash後的部門代號
					selected = $(stage).attr('value');
				}
			});
		}	
		return selected;
	}
	
	return {
		show : show,
	}
}();
</script>

<c:if test="${param.isAdding}">
<script>
// 系統名稱管理_新增對話框
<%-- 新增視窗:儲存按鈕 --%>
function createData () {
	let params = form2object("divDialogSystemAdd");
	let verifyResult = ValidateUtil.formFields('/systemNameManagement/init/validateColumnData', params);

	if (verifyResult) {
		alert(verifyResult);
		return;
	}
	
	SendUtil.post('/systemNameManagement/init/createData', params, function(data) {
		PurifyUtil.obj(divDialogSystemAdd); //防止js injection
		alert("儲存成功");			
		$("#divDialogSystemAdd").dialog("close");
		$('#divDialogSystemAdd input').val("");
		$('#searchBtn').click();
	}, null, true);
}
</script>
</c:if>

<c:if test="${param.isEditing}">
<script>
//系統名稱管理_編輯對話框
<%-- 編輯視窗:儲存按鈕 --%>
function updateData() {
	let params = form2object("divDialogSystemEdit");
	params.active = $("#divDialogSystemEdit input[name='active']:checked").val();
	let verifyResult = ValidateUtil.formFields('/systemNameManagement/init/validateColumnData', params);

	if (verifyResult) {
		alert(verifyResult);
		return;
	}
	
	SendUtil.post('/systemNameManagement/init/updateData', params, function(data) {	
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
		$("#divDialogSystemEdit").dialog('close');
		$('#searchBtn').click();
	}, null, true);
}
</script>
</c:if>

<c:if test="${param.isAdding}">
	<div style="display: none;" id="divDialogSystemAdd">
		<form id='addForm'>
</c:if>
<c:if test="${param.isEditing}">
	<div style="display: none;" id="divDialogSystemEdit">
		<form id='editForm'>
		   	<input type="hidden" name="id"/>
</c:if>
		<fieldset>
			<legend>
				<c:if test="${param.isAdding}">新增系統名稱</c:if>
				<c:if test="${param.isEditing}">編輯系統名稱</c:if>
			</legend>
			<div class="grid_BtnBar">
				<button type="button" 
					<c:if test="${param.isAdding}">onclick="createData();"</c:if>
					<c:if test="${param.isEditing}">onclick="updateData();"</c:if>
				>
					<i class="icon-save"></i>儲存
				</button>
			</div>
			<table id="systemNameList" class="grid_query">
				<tr>
					<td><font color="red">系統名稱(代碼):</font></td>
					<td><input type="text" name="systemId"
						style="width: 15rem" value="" maxlength="100" 
						<c:if test="${param.isEditing}">disabled</c:if>
						/></td>
					<td><font color="red">科別:</font></td> 
					<td>
						<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
							<jsp:param name="id" value="divisionCreated" />
							<jsp:param name="name" value="department" />
							<jsp:param name="isDisabled" value="false" />
						</jsp:include>
					</td>
				</tr>
				<tr>
					<td><font color="red">系統中文說明:</font></td>
					<td><input type="text" name="systemName"  
						style="width: 15rem" value="" maxlength="500"/></td>
					<td><font color="red">Opinc:</font></td>
					<td><input type="text" name="opinc"  
						style="width: 15rem" value="" maxlength="4" />
					</td>
				</tr>
				<tr>
					<td><font color="red">資訊資產群組:</font></td>
					<td><input type="text" name="mark"  
						style="width: 15rem" value="" maxlength="500"/></td>
					<td><font color="red">Apinc:</font></td>
					<td><input type="text" name="apinc"  
						style="width: 15rem" value="" maxlength="4"/></td>
				</tr>
				<tr>
					<td><font color="red">狀態:</font></td>
					<td>
						<label>
							<input type="radio" name='active' value='Y' checked />
							<s:message code="systemNameManagement.edit.label.enable" text='啟用' />
						</label>
						<label>
							<input type="radio" name='active' value='N'/>
							<s:message code="systemNameManagement.edit.label.disable" text='停用' />
						</label>
					</td>
					<td><font color="red">極限值:</font></td>
					<td><input type="text" name="limit"  
						style="width: 15rem" value="" maxlength="5"/></td>
				</tr>
			</table>
		</fieldset>
		<input type="hidden" name="systemNameManagement" value="">
	</form>
</div>
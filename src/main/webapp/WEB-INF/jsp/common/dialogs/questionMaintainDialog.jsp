<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<script>
function validate (param) {
	if (!param.setContent) {
		alert("題目內容不可空白。");
		return false; 
	}
	if (!param.setFraction) {
		alert("分數選項不可空白。");
		return false; 
	}
	if (!param.setDescription) {
		alert("分數說明不可空白。");
		return false; 
	}
	
	return true; 
}

function create () {
	let dialog = 'div#questionMaintainDialog';
	let params = form2object('paramForm');
	
	if (!validate(params)) {
		return;
	}
	
	addItems();
	setLastRowDate(params);
	saveItems();
	
	// 清空彈出視窗資料
	$('#paramForm').trigger("reset");
	DialogUtil.close(dialog);
}

function setLastRowDate (params) {
	let lastRow = $('#dataTable tr:last');
	lastRow.find('textarea[name = "content"]').text(params.setContent);
	lastRow.find('textarea[name = "fraction"]').text(params.setFraction);
	lastRow.find('textarea[name = "description"]').text(params.setDescription);
	lastRow.find("#isValidateFraction").val(params.setIsValidateFraction);
	lastRow.find("#isAddUp").val(params.setIsAddUp);
	lastRow.find("#isEnable").val(params.setIsEnable);
}

var QuestionMaintainDialog = function () {

	let dialog = 'div#questionMaintainDialog';
	
	var show = function () {
		$(dialog + ' #actionBtn').unbind('click').click(function () {
			create();
		});

		DialogUtil.show(dialog, {
			modal : true,
			width : 660,
			height : 380
		});
	}
	
	return {
		show : show
	}
}();
</script>

<div id='questionMaintainDialog' style="display: none;">
	<form id="paramForm">
		<fieldset>
			<div class="grid_BtnBar">
				<button id='actionBtn' type="button" >
					<i class="icon-save"></i>儲存
				</button>
			</div>
			<table id="dataTable" class="grid_query">
				<tr>
					<td><font><s:message code="question.table.field.content" text="題目內容" />:</font></td>
					<td>
						<textarea name="setContent" cols="60" rows="2" maxlength="500" ></textarea>
					</td>
				</tr>
				<tr>
					<td><font><s:message code="question.table.field.score.option" text="分數選項" />:</font></td>
					<td><input type="text" name="setFraction" style="width: 27rem" value="" maxlength="500"/></td>
				</tr>
				<tr>
					<td><font><s:message code="question.table.field.score.statement" text="分數說明" />:</font></td>
					<td>
						<textarea name="setDescription" cols="60" rows="2" maxlength="500" ></textarea>
					</td>
				</tr>
				<tr>
					<td><font><s:message code="question.table.field.check.score" text="是否需要檢核分數" />:</font></td>
					<td>
						<select id='setIsValidateFraction' name='setIsValidateFraction'>
							<option value='N' selected='selected'><s:message code='global.select.option.n' text='否' />
							<option value='Y'><s:message code='global.select.option.y' text='是' />
						</select>
					</td>
				</tr>
				<tr>
					<td><font><s:message code="question.table.field.check.addup" text="是否加總" />:</font></td>
					<td>	
						<select id='setIsAddUp' name='setIsAddUp'>
							<option value='Y' selected='selected'><s:message code='global.select.option.y' text='是' />
							<option value='N'><s:message code='global.select.option.n' text='否' />
						</select>	
					</td>
				</tr>
				<tr>
					<td><font><s:message code="question.table.field.check.enable" text="是否啟用" />:</font></td>
					<td>
						<select id='setIsEnable' name='setIsEnable'>
							<option value='Y' selected='selected'><s:message code='global.select.option.y' text='是' />
							<option value='N'><s:message code='global.select.option.n' text='否' />
						</select>	
					</td>
				</tr>
			</table>
		</fieldset>
		<div><font color="red">分數選項：超過1個分數時，以;區隔，結尾不用;</font></div>
		<div><font color="red">是否需要檢核分數：當本項評估項目達75分(含)以上時，須影響評估欄位中說明該項對現有作業衝擊以及其因應措施。</font></div>
		<div><font color="red">是否加總：系統列入變更衝擊總分計算。</font></div>
	</form>
</div>
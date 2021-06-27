<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script>
$(function () {
	initView();
	authViewControl();
});

function initView () {
	let headForm = form2object('headForm');
	
	if (HtmlUtil.tempData.programForm) {
		ObjectUtil.autoSetFormValue(HtmlUtil.tempData.programForm, 'programForm');
	} else if (headForm.formId) {
		SendUtil.get('/eventForm/program', headForm.formId, function (response) {
			ObjectUtil.clear(response);
			HtmlUtil.tempData.programForm = response;
			ObjectUtil.autoSetFormValue(response, 'programForm');
		});
	}

	$('form#programForm .resizable-textarea').resizable({handles: "se"});
}
</script>
<form id='programForm'>
	<input id='id' class='hidden' name='id' />
	<input id='createdBy' class='hidden', name='createdBy' />
	<input id='createdAt' class='hidden', name='createdAt' />
	<table class="grid_list">
		<tr>
			<th width="20%"></th>
			<td align="left">
				<label><input id="isSuggestCase" name="isSuggestCase" type="checkbox" /><font color="red">暫時性解決方案，且無法於事件目標解決時間內根本解決者?</font></label>
			</td>
		</tr>
		<tr>
			<th width="20%"><s:message code='form.question.program.indication' text='徵兆'/></th>
			<td><textarea id="indication" class='resizable-textarea' name="indication"  rows="3" cols="4" maxlength="2000"></textarea></td>
		</tr>
		<tr>
			<th width="20%"><s:message code='form.question.program.reason' text='原因'/></th>
			<td><textarea id="reason" class='resizable-textarea' name="reason" rows="3" cols="4" maxlength="2000"></textarea></td>
		</tr>
		<tr>
			<th width="20%"><s:message code='form.question.program.reprocess.programason' text='處理方案'/></th>
			<td><textarea id="processProgram" class='resizable-textarea' name="processProgram" rows="3" cols="4" maxlength="2000"></textarea></td>
		</tr>
	</table>
</form>
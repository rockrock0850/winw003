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
		SendUtil.get('/requirementForm/program', headForm.formId, function (response) {
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
			<th width="20%"><s:message code='form.question.program.reprocess.programason' text='處理方案'/></th>
			<td><textarea id="processProgram" class='resizable-textarea' name="processProgram"  rows="3" cols="4" maxlength="2000"></textarea></td>
		</tr>
	</table>
</form>
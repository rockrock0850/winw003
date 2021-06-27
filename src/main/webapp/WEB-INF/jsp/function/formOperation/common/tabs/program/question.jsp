<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script>
var subReasons;
var ajaxSetting = {async:false};

$(function () {
	initView();
	authViewControl();
});

function initView () {
	let headForm = form2object('headForm');

	if (HtmlUtil.tempData.programForm) {
		ObjectUtil.autoSetFormValue(HtmlUtil.tempData.programForm, 'programForm');
	} else if (headForm.formId) {
		SendUtil.get('/questionForm/program', headForm.formId, function (res) {
			ObjectUtil.clear(res);

			if (res.knowledge2) {
				res.knowledge2 = res.knowledge2.split(',');
			}

			HtmlUtil.tempData.programForm = res;
			ObjectUtil.autoSetFormValue(res, 'programForm');
		}, ajaxSetting);
	}

	$('form#programForm .resizable-textarea').resizable({handles: "se"});
}

function isSuggestCaseDisabled (isKnowledge) {
	$('input#isSuggestCase').prop('checked', isKnowledge);
	$('input#isSuggestCase').prop('disabled', isKnowledge);
}

function clickProgramSaveButton () {
	if (!confirm("<s:message code='form.question.program.configm.save' text='確定儲存?'/>")) {
		return;
	}

	request = $.extend(form2object('headForm'), form2object('programForm'));
	ObjectUtil.dataToBackend(request);
	SendUtil.post('/questionForm/program', request, function (response) {
		alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
		ObjectUtil.autoSetFormValue(response, 'programForm');
	});
}
</script>

<form id="programForm">
	<input id='id' class='hidden' name='id' />
	<input id='createdBy' class='hidden' name='createdBy' />
	<input id='createdAt' class='hidden' name='createdAt' />
	<table class="grid_list">
		<tr>
			<th width="20%"></th>
			<td align="left">
				<label>
					<input id="isSuggestCase" name="isSuggestCase" type="checkbox" /><s:message code='form.question.program.note.1' text='建議加入「處理方案」?'/>
				</label>
			</td>
			<td>
				<table class="grid_query">
					<tr><td><jsp:include page="/WEB-INF/jsp/common/models/knowledgeModel.jsp" /></td></tr>
				</table>
			</td>
		</tr>
		<tr>
			<th width="20%"><s:message code='form.question.program.indication' text='徵兆'/></th>
			<td align="left" colspan='5'><textarea id="indication" name="indication" class="resizable-textarea" rows="3" cols="4" maxlength="2000"></textarea></td>
		</tr>
		<tr>
			<th width="20%"><s:message code='form.question.program.reason' text='原因'/></th>
			<td align="left" colspan='5'><textarea id="reason" name="reason" class="resizable-textarea" rows="3" cols="4" maxlength="2000"></textarea></td>
		</tr>
		<tr>
			<th width="20%"><s:message code='form.question.program.reprocess.programason' text='處理方案'/></th>
			<td align="left" colspan='5'><textarea id="processProgram" name="processProgram" class="resizable-textarea" rows="3" cols="4" maxlength="2000"></textarea></td>
		</tr>
		<tr>
			<th width="20%"><s:message code='form.question.program.reprocess.temporary' text='暫時性解決方案'/></th>
			<td align="left" colspan='5'><textarea id="temporary" name="temporary" class="resizable-textarea" rows="3" cols="4" maxlength="2000"></textarea></td>
		</tr>
	</table>
</form>
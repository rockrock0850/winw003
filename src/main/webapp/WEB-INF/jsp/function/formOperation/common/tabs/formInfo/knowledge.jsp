<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<style>
	.resizable-textarea {
		width: 500px !important
	}
</style>

<script>//# sourceURL=kInfo.js
$(function () {
	DateUtil.dateTimePicker();
	initView();
	initEvent();
	fetchInfo(form2object('headForm'));
	authViewControl();
});

function initView () {
	SendUtil.get("/html/getDivisionSelectors", false, function (option) {
		HtmlUtil.singleSelect('select#division', option);
	},ajaxSetting);
	
	SendUtil.get('/html/getDropdownList', 2, function (options) {
		HtmlUtil.singleSelect('select#sClass', options);
	}, ajaxSetting);
	
	SendUtil.get('/html/getDropdownList', 'cCategory', function (options) {
		HtmlUtil.singleSelect('select#cCategory', options);
	}, ajaxSetting);
	
	$('form#infoForm .resizable-textarea').resizable({handles: "se"});
}

function initEvent () {
	$('select#sClass').change(function () {
		if ($(this).val()) {
			SendUtil.get('/html/getSubDropdownList', $(this).val(), function (options) {
				HtmlUtil.singleSelect('select#sSubClass', options);
			}, ajaxSetting);
		} else {
			HtmlUtil.emptySelect("#sSubClass");
		}
	});
	
	$('select#cCategory').change(function () {
		if ($(this).val()) {
			SendUtil.get('/html/getSubDropdownList', $(this).val(), function (options) {
				HtmlUtil.singleSelect('select#cClass', options);
			}, ajaxSetting);
		} else {
			HtmlUtil.emptySelect("#cClass");
			$('textarea#cComponent').val('');
		}
	});

	$('select#cClass').change(function () {
		if (!$(this).val()) {
			$('textarea#cComponent').val('');
		}
	});
}
</script>

<div>
	<form id='infoForm'>
		<fieldset>
			<legend><s:message code='form.question.process.knowledge.2' text='知識庫明細'/></legend>
			<table class="grid_query">
				<tr>
					<td valign="top">
						<table>
							<tr>
								<th><span style="color: red; "><s:message code='form.report.search.summary' text='摘要'/></span></th>
								<td><input type="text" id="summary" name="summary" value="" maxlength="500"/></td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.question.program.indication' text='徵兆'/></span></th>
								<td align="left" colspan='5'><textarea id="indication" name="indication" class="resizable-textarea" rows="3" cols="4" maxlength="2000"></textarea></td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.question.program.reason' text='原因'/></span></th>
								<td align="left" colspan='5'><textarea id="reason" name="reason" class="resizable-textarea" rows="3" cols="4" maxlength="2000"></textarea></td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.question.program.reprocess.programason' text='處理方案'/></span></th>
								<td align="left" colspan='5'><textarea id="processProgram" name="processProgram" class="resizable-textarea" rows="3" cols="4" maxlength="2000"></textarea></td>
							</tr>
						</table>
					</td>
					<td valign="top">
						<table class="grid_query">
							<tr>
								<th><s:message code='form.question.form.info.system.name' text='系統名稱'/></th>
								<jsp:include page='/WEB-INF/jsp/common/models/systemModel.jsp' />
							</tr>
							<tr>
								<th><s:message code='form.question.form.info.asset.group' text='資訊資產群組'/></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/textareaModel.jsp'>
										<jsp:param name="id" value="assetGroup" />
										<jsp:param name="name" value="assetGroup" />
										<jsp:param name="readonly" value="true" />
									</jsp:include> 
								</td>
							</tr>
							<tr>
								<th><s:message code='form.question.form.info.service.type' text='服務類別'/></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="sClass" />
										<jsp:param name="name" value="sClass" />
									</jsp:include>
								</td>
							</tr>
							<tr>
								<th><s:message code='form.question.form.info.service.sub.type' text='服務子類別'/></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="sSubClass" />
										<jsp:param name="name" value="sSubClass" />
									</jsp:include>
								</td>
							</tr>
							<tr><td colspan="2"><jsp:include page="/WEB-INF/jsp/common/models/knowledgeModel.jsp" /></td></tr>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
				<legend><s:message code='form.question.form.info.date' text='日期'/></legend>
				<table class="grid_query">
					<tr>
						<th><s:message code='form.question.process.knowledge.4' text='知識庫建立日期'/></th>
						<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
							<jsp:param name="id1" value="createTime" />
							<jsp:param name="name1" value="createTime" />
						</jsp:include>
						<th><s:message code='form.question.process.knowledge.5' text='前次變更時間'/></th>
						<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
							<jsp:param name="id1" value="updatedAt" />
							<jsp:param name="name1" value="updatedAt" />
						</jsp:include>
					</tr>
				</table>
		</fieldset>
		<jsp:include page="/WEB-INF/jsp/common/models/solutionsModel.jsp" />
	</form>
</div>
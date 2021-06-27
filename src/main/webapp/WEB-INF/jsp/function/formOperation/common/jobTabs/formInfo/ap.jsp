﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<div>
	<form id="infoForm">
		<fieldset>
			<legend><s:message code='form.job.legend.item' text='工作要項'/></legend>
			<table class="grid_query">
				<tr>
					<td><label><input id='isHandleFirst' name='isHandleFirst' type="checkbox" /><s:message code='form.job.form.isHandleFirst' text='先處理後呈閱'/></label></td>
					<td><label><input id='isCorrect' name='isCorrect' type="checkbox" /><s:message code='form.job.form.isCorrect' text='上線修正'/></label></td>
					<td><label><input id='isAddFuntion' name='isAddFuntion' type="checkbox" /><s:message code='form.job.form.isAddFuntion' text='新增系統功能'/></label></td>
					<td><label><input id='isForward' name='isForward' type="checkbox" /><s:message code='form.job.form.isForward' text='送交組態人員'/></label></td>
					<td><label><input id='isWatching' name='isWatching' type="checkbox" /><s:message code='form.job.form.isWatching' text='送交監督人員'/></label></td>
				</tr>
				<tr>
					<td><label><input id='isAddReport' name='isAddReport' type="checkbox" /><s:message code='form.job.form.isAddReport' text='新增報表'/>(<font color="red"><s:message code='form.job.form.need.attached' text='需檢附清單'/></font>)</label></td>
					<td><label><input id='isAddFile' name='isAddFile' type="checkbox" /><s:message code='form.job.form.isAddFile' text='新增檔案'/>(<font color="red"><s:message code='form.job.form.need.attached' text='需檢附清單'/></font>)</label></td>
					<td><label><input id='isProgramOnline' name='isProgramOnline' type="checkbox" onclick="handleClick(this);" /><s:message code='form.job.form.isProgramOnline' text='程式上線'/>(<font color="red"><s:message code='form.job.form.need.test.file' text='需檢附測試文件'/></font>)</label></td>
					<td><label><input id="isModifyProgram" name="isModifyProgram" type="checkbox" onclick="handleClick(this);" /><s:message code='form.job.form.isModifyProgram' text='未有修改程式'/></label></td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend><s:message code='form.job.legend.detail' text='工作明細'/></legend>
			<table class="grid_query">
				<tr>
					<td valign="top">
						<table>
							<tr>
								<th><font color="red"><s:message code='form.job.form.purpose' text='作業目的'/></font></th>
								<td><textarea id='purpose' name='purpose' class='resizable-textarea' cols="50" rows="8" maxlength="500" ></textarea></td>
							</tr>
							<tr>
								<td colspan="2">
									<fieldset>
										<legend><s:message code='form.job.form.class' text='會辦科'/></legend>
										<jsp:include page='/WEB-INF/jsp/common/models/cListModel.jsp' >
											<jsp:param name="formClass" value="JOB" />
										</jsp:include>
									</fieldset>
								</td>
							</tr>
						</table>
					</td>
					<td valign="top">
						<table class="grid_query">
							<tr>
								<th><font color="red"><s:message code='form.job.form.content.2' text='執行內容'/></font></th>
								<td><textarea id='content' name='content' class='resizable-textarea' cols="50" rows="8" maxlength="2000" ></textarea></td>
							</tr>
							<tr>
								<th><font color="red"><s:message code='form.job.form.system' text='系統名稱'/></font></th>
								<jsp:include page='/WEB-INF/jsp/common/models/systemModel.jsp' />
							</tr>
							<tr>
								<th><font color="red"><s:message code='form.job.form.sClass' text='服務類別'/></font></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="sClass" />
										<jsp:param name="name" value="sClass" />
									</jsp:include>
								</td>
							</tr>
							<tr>
								<th><s:message code='form.job.form.sSubClass' text='子類別'/></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="sSubClass" />
										<jsp:param name="name" value="sSubClass" />
									</jsp:include>
								</td>
							</tr>
							<tr>
								<th><s:message code='form.job.form.changeType' text='變更類型'/></th>
								<td><input id='changeType' name='changeType' type="text" disabled="disabled"/></td>
							</tr>
							<tr>
								<th><s:message code='form.job.form.changeRank' text='變更等級'/></th>
								<td><input id='changeRank' name='changeRank' type="text" disabled="disabled"/></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend><s:message code='form.job.legend.date' text='日期'/></legend>
			<label><input id='isPlaning' name='isPlaning' type="checkbox" /><s:message code='form.job.form.isPlaning' text='會造成服務中斷或需要停機，屬於計劃性系統維護'/>&nbsp;&nbsp;&nbsp;&nbsp;</label>
			<label><input id='isUnPlaning' name='isUnPlaning' type="checkbox" /><s:message code='form.job.form.isUnPlaning' text='會造成服務中斷或需要停機，屬於非計劃性系統維護'/>&nbsp;&nbsp;&nbsp;&nbsp;</label>
			<table class="grid_query">
				<tr>
					<th><s:message code='form.job.form.tct' text='測試系統完成時間'/></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="tct" />
						<jsp:param name="name1" value="tct" />
					</jsp:include>
					<th><s:message code='form.job.form.sct' text='連線系統完成日期'/></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="sct" />
						<jsp:param name="name1" value="sct" />
					</jsp:include>
					<th><s:message code='form.job.form.sit' text='連線系統實施日期'/></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="sit" />
						<jsp:param name="name1" value="sit" />
					</jsp:include>
				</tr>
				<tr>
					<th><s:message code='form.job.form.offLineTime' text='公告停機時間'/></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="offLineTime" />
						<jsp:param name="name1" value="offLineTime" />
						<jsp:param name="warning" value="(至少於五天前公告)" />
					</jsp:include>
				</tr>
				<tr>
					<th><s:message code='form.job.form.ist' text='中斷起始時間'/></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="ist" />
						<jsp:param name="name1" value="ist" />
					</jsp:include>
					<th><s:message code='form.job.form.ict' text='中斷結束時間'/></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="ict" />
						<jsp:param name="name1" value="ict" />
					</jsp:include>
				</tr>
				<tr>
					<th><s:message code='form.job.form.cat' text='變更申請時間'/></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="cat" />
						<jsp:param name="name1" value="cat" />
					</jsp:include>
					<th><s:message code='form.job.form.cct' text='變更結束時間'/></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="cct" />
						<jsp:param name="name1" value="cct" />
					</jsp:include>
				</tr>
			</table>
		</fieldset>
	</form>
</div>

<script>//# sourceURL=apJobInfo.js
$(function () {
	DateUtil.dateTimePicker();
	initView();
	initEvent();
	fetchInfo(form2object('headForm'));
	authViewControl();
});

function initView () {
	SendUtil.get("/html/getDropdownList", 1, function (option) {
		HtmlUtil.singleSelect('select#unitCategory', option);
	}, ajaxSetting);
	
	SendUtil.get("/html/getDropdownList", 'EFFECT_SCOPE', function (option) {
		HtmlUtil.singleSelect('select#effectScope', option);
	}, ajaxSetting);
	
	SendUtil.get("/html/getDropdownList", 'SR_URGENT_LEVEL', function (option) {
		HtmlUtil.singleSelect('select#urgentLevel', option);
	}, ajaxSetting);
	
	SendUtil.get("/html/getDivisionSelectors", false, function (option) {
		HtmlUtil.singleSelect('select#division', option);
	}, ajaxSetting);

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
				
				//變更單有勾選「未有修改程式」，所開立之AP工作單，預設勾選「未有修改程式」且不可更改
				if ($('#isModifyProgram').is(':checked')) {
					$('#isModifyProgram').prop('disabled', true);
				}
				
				//「程式上線」、「未有修改程式」:若其中一項有勾選，則另一項DISABLE 
				if ($('#isProgramOnline').is(':checked')) {
					$('#isModifyProgram').prop('disabled', true);
				} else if ($('#isModifyProgram').is(':checked')) {
					$('#isProgramOnline').prop('disabled', true);
				}
				
			}, ajaxSetting);
		} else {
			HtmlUtil.emptySelect("#sSubClass");
		}
	});
}

function handleClick(checkbox) {
	if ($('#isProgramOnline').is(':checked')) {
		$('#isModifyProgram').prop('disabled', true);
	} else {
		$('#isModifyProgram').prop('disabled', false);
	}
	
	if ($('#isModifyProgram').is(':checked')) {
		$('#isProgramOnline').prop('disabled', true);
	} else {
		$('#isProgramOnline').prop('disabled', false);
	}
}

</script>

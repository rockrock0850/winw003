<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script>//# sourceURL=cInfo.js
$(function () {
	DateUtil.dateTimePicker();
	initView();
	initEvent();
	fetchInfo(form2object('headForm'));
	showSpcBlock();
	authViewControl();
});

function initView () {
	SendUtil.get("/html/getDivisionSelectors", false, function (option) {
		HtmlUtil.singleSelect('select#unitId', option);
	}, ajaxSetting);

	SendUtil.get('/html/getDropdownList', 2, function (options) {
		HtmlUtil.singleSelect('select#sClass', options);
	}, ajaxSetting);
	$('form#infoForm .resizable-textarea').resizable({handles: "se"});
}

function initEvent () {
	$('#userSolving').on('change', function() {
		var text = $(this).find("option:selected").text();
		$('#userId').val(text);
	});
	
	$('select#unitId').change(function () {
		if (!$(this).val().trim()) {
			$('input#unitIdWording').val('');
		}
		
		$('input#unitIdWording').val($(this).val());
	});
}

function showSpcBlock () {
	var sourceId = $('input#sourceId').val();
	var division = $('select#divisionSolving').val();
	var isSP = (division && division.indexOf('SP') != -1);
	var isSRQ = 
		(sourceId && 
		(sourceId.indexOf('SR') != -1 || sourceId.indexOf('Q') != -1));
	
	if (isSRQ && isSP) {
		$('tr#spcGroupBlock').show();
	}
	
	var isIC = $('input#formId').val().indexOf('IC') != -1;
	if (isIC){
		$('tr#inccGroupBlock').show();
	}
	
}
</script>
<div>
	<form id="infoForm">
		<fieldset>
			<legend>明細</legend>
			<table class="grid_query">
				<tr>
					<td valign="top">
						<table class="grid_query">
							<tr>
								<th><font color="red">摘要</font></th>
								<td><input id='summary' name='summary' type="text" value="" maxlength="500" /></td>
							</tr>
							<tr>
								<th><font color="red">內容</font></th>
								<td><textarea id='content' name='content' class='resizable-textarea' cols="50" rows="12" maxlength="2000"></textarea></td>
							</tr>
							<tr>
								<th>主辦科處理情形</th>
								<td><textarea id='hostHandle' name='hostHandle' class='resizable-textarea' cols="50" rows="4" maxlength="2000"></textarea></td>
							</tr>
							<tr>
								<th>會辦科處理情形</th>
								<td><textarea id='countersignedHandle' name='countersignedHandle' class='resizable-textarea' cols="50" rows="4" maxlength="450" ></textarea></td>
							</tr>
						</table>
					</td>
					<td valign="top">
						<table class="grid_query">
							<tr>
								<th><font color="red">系統名稱</font></th>
								<jsp:include page='/WEB-INF/jsp/common/models/systemModel.jsp' />
							</tr>
							<tr>
								<th>資訊資產群組</th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/textareaModel.jsp'>
										<jsp:param name="id" value="assetGroup" />
										<jsp:param name="name" value="assetGroup" />
										<jsp:param name="readonly" value="true" />
									</jsp:include> 
								</td>
							</tr>
							<tr class='hidden'>
								<th>負責單位</th>
								<td nowrap="nowrap">
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="unitId" />
										<jsp:param name="name" value="unitId" />
									</jsp:include>
									<input id='unitIdWording' type="text" value="" readonly />
								</td>
							</tr>
							<tr>
								<th>處理人員群組</th>
								<td><input id='userGroup' name='userGroup' type="text" readonly="readonly"/></td>
							</tr>
							<tr>
								<th>處理人員</th>
								<td><input id='userId' name='userId' type="text" readonly="readonly"/></td>
							</tr>
							<tr>
								<th><font color="red">服務類別</font></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="sClass" />
										<jsp:param name="name" value="sClass" />
									</jsp:include>
								</td>
							</tr>
							<tr id='spcGroupBlock' class='hidden'>
								<td colspan="2">
									<fieldset>
										<legend>會辦系統科群組</legend>
										<jsp:include page='/WEB-INF/jsp/common/models/spcModel.jsp' />
									</fieldset>
								</td>
							</tr>
							<tr id='inccGroupBlock' class='hidden'>
								<th></th>
								<td>
									<label><input type="checkbox" id="isSuggestCase" name="isSuggestCase"/><span style="color: red; "><s:message code='form.search.column.form.program.isForward' text='暫時性解決方案，且無法於事件目標解決時間內根本解決者?'/></span></label>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend>日期</legend>
			<%@ include file="/WEB-INF/jsp/function/formOperation/common/tabs/formInfo/countersigned/dateQ.jsp"%>
			<%@ include file="/WEB-INF/jsp/function/formOperation/common/tabs/formInfo/countersigned/dateSR.jsp"%>
			<%@ include file="/WEB-INF/jsp/function/formOperation/common/tabs/formInfo/countersigned/dateINC.jsp"%>
		</fieldset>
	</form>
</div>
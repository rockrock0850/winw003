<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<script>//# sourceURL=audiParamsFind.js
var auditNotifyParams = ObjectUtil.parse('${auditNotifyParams}');

$(function () {
	initView();
	HtmlUtil.initTabs();
});

function initView () {
	initTextAreas();
	cloneWarningMessage();
	fillAuditNotifyForms();
}

function saveButton () {
	let verifyResult;
	let reqData = {};
	let formList = [];
	
	formList.push(ObjectUtil.dataToBackend(form2object('qeForm')));
	formList.push(ObjectUtil.dataToBackend(form2object('qesForm')));
	formList.push(ObjectUtil.dataToBackend(form2object('sreForm')));
	formList.push(ObjectUtil.dataToBackend(form2object('sresForm')));
	formList.push(ObjectUtil.dataToBackend(form2object('inceForm')));
	formList.push(ObjectUtil.dataToBackend(form2object('incesForm')));
	formList.push(ObjectUtil.dataToBackend(form2object('incheForm')));
	formList.push(ObjectUtil.dataToBackend(form2object('inchesForm')));
	reqData.params = formList;
	
	verifyResult = ValidateUtil.formFields(
			'/auditNotifyParams/validateColumnData', reqData);

	if (verifyResult) {
		alert(verifyResult);
		return;
	}
	
	if (confirm("<s:message code='form.question.form.info.configm.save' text='確定儲存?'/>")) {
		SendUtil.post('/auditNotifyParams/save', reqData, function (resData) {
			alert("<s:message code='form.question.form.info.success.save' text='儲存成功!'/>");
		}, null, true);
	}
}

function fillAuditNotifyForms () {
	$.each(auditNotifyParams, function (i, item) {
		if (item.formType == 'Q') {
			fillForm(item, 'q');
		} else if (item.formType == 'SR') {
			fillForm(item, 'sr');
		} else if (item.formType == 'INC') {
			fillForm(item, 'inc');
		} else if (item.formType == 'INCH') {
			fillForm(item, 'inch');
		}
	});	
}

function fillForm (item, formTag) {
	if (item.notifyType == 'EXPIRED') {
		ObjectUtil.autoSetFormValue(item, formTag + 'eForm');
	} else if (item.notifyType == 'EXPIRE_SOON') {
		ObjectUtil.autoSetFormValue(item, formTag + 'esForm');
	}
}

function initTextAreas () {
	// 如果textarea在隱藏的狀態被初始化, 會導致初始化後的寬度被限制在0
	$('div#srTabCtn').removeClass('hide');
	$('div#incTabCtn').removeClass('hide');
	$('div#incHourTabCtn').removeClass('hide');
	$('textarea#qeUserId').resizable({handles: "se"});
	$('textarea#qesUserId').resizable({handles: "se"});
	$('textarea#sreUserId').resizable({handles: "se"});
	$('textarea#sresUserId').resizable({handles: "se"});
	$('textarea#inceUserId').resizable({handles: "se"});
	$('textarea#incesUserId').resizable({handles: "se"});
	$('textarea#incheUserId').resizable({handles: "se"});
	$('textarea#inchesUserId').resizable({handles: "se"});
	$('div#srTabCtn').addClass('hide');
	$('div#incTabCtn').addClass('hide');
	$('div#incHourTabCtn').addClass('hide');
}

function cloneWarningMessage () {
	$('fieldset#auditWarningLoad1').html($('fieldset#auditWarning').html());
	$('fieldset#auditWarningLoad2').html($('fieldset#auditWarning').html());
	$('fieldset#auditWarningLoad3').html($('fieldset#auditWarning').html());
	$('fieldset#auditWarningLoad4').html($('fieldset#auditWarning').html());
}
</script>

</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1>稽催通知參數設定</h1>

	<fieldset class="search">
		<button class="small fieldControl searchPanel">
			<i class="iconx-collapse"></i>|||
		</button>
		<form>
			<table class="grid_query" style='float:right'>
				<tr>
					<td>
						<button id="saveBtn" type="button" onclick='saveButton();'>
							<i class="iconx-save"></i> 儲存
						</button>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
	
	<ul id='tabBar' class="nav nav-tabs">
		<li id='qTab' class="active"><a href="#qTabCtn"><s:message code='question.form' text='問題單'/></a></li>
		<li id='srTab'><a href="#srTabCtn"><s:message code='requirement.form' text='需求單'/></a></li>
		<li id="incTab"><a href="#incTabCtn"><s:message code='event.form' text='事件單'/></a></li>
		<li id="incHourTab"><a href="#incHourTabCtn"><s:message code='event.form' text='事件單'/>(時)</a></li>
	</ul>
	
	<div id='qTabCtn'>
		<fieldset id='auditWarningLoad1' class='search' style="margin-bottom: 0 !important;"></fieldset>
		<table class="grid_query">
			<tr>
				<td valign="top">
					<form id='qeForm'>
						<input id='qeId' class='hidden' name='id' />
						<input id='qeFormType' class='hidden' name='formType' value='Q' />
						<input id='qeNotifyType' class='hidden' name='notifyType' value='EXPIRED' />
						<fieldset>
							<legend>已逾期</legend>
							<jsp:include page='/WEB-INF/jsp/common/models/auditParamsModel.jsp'>
								<jsp:param name="sc" value="qeSc" />
								<jsp:param name="d1" value="qeD1" />
								<jsp:param name="d2" value="qeD2" />
								<jsp:param name="pic" value="qePic" />
								<jsp:param name="vsc" value="qeVsc" />
								<jsp:param name="timeUnit" value="天" />
								<jsp:param name="time" value="qeTime" />
								<jsp:param name="notifyMails" value="qeMails" />
								<jsp:param name="isNeedExpireTime" value="false" />
							</jsp:include>
						</fieldset>
					</form>
				</td>
				<td valign="top">
					<form id='qesForm'>
						<input id='qesId' class='hidden' name='id' />
						<input id='qesFormType' class='hidden' name='formType' value='Q' />
						<input id='qesNotifyType' class='hidden' name='notifyType' value='EXPIRE_SOON' />
						<fieldset>
							<legend>即將逾期</legend>
							<jsp:include page='/WEB-INF/jsp/common/models/auditParamsModel.jsp'>
								<jsp:param name="sc" value="qesSc" />
								<jsp:param name="d1" value="qesD1" />
								<jsp:param name="d2" value="qesD2" />
								<jsp:param name="pic" value="qesPic" />
								<jsp:param name="vsc" value="qesVsc" />
								<jsp:param name="timeUnit" value="天" />
								<jsp:param name="time" value="qesTime" />
								<jsp:param name="notifyMails" value="qesMails" />
								<jsp:param name="isNeedExpireTime" value="true" />
							</jsp:include>
						</fieldset>
					</form>
				</td>
			</tr>
		</table>
	</div>
	
	<div id='srTabCtn' class='hide'>
		<fieldset id='auditWarningLoad2' class='search' style="margin-bottom: 0 !important;"></fieldset>
		<table class="grid_query">
			<tr>
				<td valign="top">
					<form id='sreForm'>
						<input id='sreId' class='hidden' name='id' />
						<input id='sreFormType' class='hidden' name='formType' value='SR' />
						<input id='sreNotifyType' class='hidden' name='notifyType' value='EXPIRED' />
						<fieldset>
							<legend>已逾期</legend>
							<jsp:include page='/WEB-INF/jsp/common/models/auditParamsModel.jsp'>
								<jsp:param name="sc" value="sreSc" />
								<jsp:param name="d1" value="sreD1" />
								<jsp:param name="d2" value="sreD2" />
								<jsp:param name="pic" value="srePic" />
								<jsp:param name="vsc" value="sreVsc" />
								<jsp:param name="timeUnit" value="天" />
								<jsp:param name="time" value="sreTime" />
								<jsp:param name="notifyMails" value="sreMails" />
								<jsp:param name="isNeedExpireTime" value="false" />
							</jsp:include>
						</fieldset>
					</form>
				</td>
				<td valign="top">
					<form id='sresForm'>
						<input id='sresId' class='hidden' name='id' />
						<input id='sresFormType' class='hidden' name='formType' value='SR' />
						<input id='sresNotifyType' class='hidden' name='notifyType' value='EXPIRE_SOON' />
						<fieldset>
							<legend>即將逾期</legend>
							<jsp:include page='/WEB-INF/jsp/common/models/auditParamsModel.jsp'>
								<jsp:param name="sc" value="sresSc" />
								<jsp:param name="d1" value="sresD1" />
								<jsp:param name="d2" value="sresD2" />
								<jsp:param name="pic" value="sresPic" />
								<jsp:param name="vsc" value="sresVsc" />
								<jsp:param name="timeUnit" value="天" />
								<jsp:param name="time" value="sresTime" />
								<jsp:param name="notifyMails" value="sresMails" />
								<jsp:param name="isNeedExpireTime" value="true" />
							</jsp:include>
						</fieldset>
					</form>
				</td>
			</tr>
		</table>
	</div>
	
	<div id='incTabCtn' class='hide'>
		<fieldset id='auditWarningLoad3' class='search' style="margin-bottom: 0 !important;"></fieldset>
		<table class="grid_query">
			<tr>
				<td valign="top">
					<form id='inceForm'>
						<input id='inceId' class='hidden' name='id' />
						<input id='inceFormType' class='hidden' name='formType' value='INC' />
						<input id='inceNotifyType' class='hidden' name='notifyType' value='EXPIRED' />
						<fieldset>
							<legend>已逾期</legend>
							<jsp:include page='/WEB-INF/jsp/common/models/auditParamsModel.jsp'>
								<jsp:param name="sc" value="inceSc" />
								<jsp:param name="d1" value="inceD1" />
								<jsp:param name="d2" value="inceD2" />
								<jsp:param name="pic" value="incePic" />
								<jsp:param name="vsc" value="inceVsc" />
								<jsp:param name="timeUnit" value="天" />
								<jsp:param name="time" value="inceTime" />
								<jsp:param name="notifyMails" value="inceMails" />
								<jsp:param name="isNeedExpireTime" value="false" />
							</jsp:include>
						</fieldset>
					</form>
				</td>
				<td valign="top">
					<form id='incesForm'>
						<input id='incesId' class='hidden' name='id' />
						<input id='incesFormType' class='hidden' name='formType' value='INC' />
						<input id='incesNotifyType' class='hidden' name='notifyType' value='EXPIRE_SOON' />
						<fieldset>
							<legend>即將逾期</legend>
							<jsp:include page='/WEB-INF/jsp/common/models/auditParamsModel.jsp'>
								<jsp:param name="sc" value="incesSc" />
								<jsp:param name="d1" value="incesD1" />
								<jsp:param name="d2" value="incesD2" />
								<jsp:param name="pic" value="incesPic" />
								<jsp:param name="vsc" value="incesVsc" />
								<jsp:param name="timeUnit" value="天" />
								<jsp:param name="time" value="incesTime" />
								<jsp:param name="notifyMails" value="incesMails" />
								<jsp:param name="isNeedExpireTime" value="true" />
							</jsp:include>
						</fieldset>
					</form>
				</td>
			</tr>
		</table>
	</div>
	
	<div id='incHourTabCtn' class='hide'>
		<fieldset id='auditWarningLoad4' class='search' style="margin-bottom: 0 !important;"></fieldset>
		<table class="grid_query">
			<tr>
				<td valign="top">
					<form id='incheForm'>
						<input id='incheId' class='hidden' name='id' />
						<input id='incheFormType' class='hidden' name='formType' value='INCH' />
						<input id='incheNotifyType' class='hidden' name='notifyType' value='EXPIRED' />
						<fieldset>
							<legend>已逾期</legend>
							<jsp:include page='/WEB-INF/jsp/common/models/auditParamsModel.jsp'>
								<jsp:param name="sc" value="incheSc" />
								<jsp:param name="d1" value="incheD1" />
								<jsp:param name="d2" value="incheD2" />
								<jsp:param name="pic" value="inchePic" />
								<jsp:param name="vsc" value="incheVsc" />
								<jsp:param name="timeUnit" value="時" />
								<jsp:param name="time" value="incheTime" />
								<jsp:param name="notifyMails" value="incheMails" />
								<jsp:param name="isNeedExpireTime" value="false" />
							</jsp:include>
						</fieldset>
					</form>
				</td>
				<td valign="top">
					<form id='inchesForm'>
						<input id='inchesId' class='hidden' name='id' />
						<input id='inchesFormType' class='hidden' name='formType' value='INCH' />
						<input id='inchesNotifyType' class='hidden' name='notifyType' value='EXPIRE_SOON' />
						<fieldset>
							<legend>即將逾期</legend>
							<jsp:include page='/WEB-INF/jsp/common/models/auditParamsModel.jsp'>
								<jsp:param name="sc" value="inchesSc" />
								<jsp:param name="d1" value="inchesD1" />
								<jsp:param name="d2" value="inchesD2" />
								<jsp:param name="pic" value="inchesPic" />
								<jsp:param name="vsc" value="inchesVsc" />
								<jsp:param name="timeUnit" value="時" />
								<jsp:param name="time" value="inchesTime" />
								<jsp:param name="notifyMails" value="inchesMails" />
								<jsp:param name="isNeedExpireTime" value="true" />
							</jsp:include>
						</fieldset>
					</form>
				</td>
			</tr>
		</table>
	</div>
	
	<fieldset id='auditWarning' class='hidden'>
		<table class='grid_query'>
			<tbody>
				<tr><td><span style="color: red; ">1. 負責人郵件地址清單結尾請務必加上「;」符號。</span></td></tr>
				<tr><td><span style="color: red; ">2. 負責人郵件地址清單若有多個請用「;」符號區隔。</span></td></tr>
			</tbody>
		</table>
	</fieldset>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
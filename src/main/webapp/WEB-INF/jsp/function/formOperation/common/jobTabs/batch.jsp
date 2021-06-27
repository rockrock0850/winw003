<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<script>
var tabType = '<c:out value="${tabType}"/>';
var getUrl = "/commonJobForm/getBatchDetail";
var saveUrl = "/commonJobForm/saveBatchDetail";

$(function () {
    $('input#division').val(tabType);
    DateUtil.dateTimePicker();
	fillCForm('tabBtForm', 'BATCH');
    authViewControl();
	$('form#tabBtForm .resizable-textarea').resizable({handles: "se"});
	
	$("textarea#countersignedIDC").prop("disabled", 
			(!ValidateUtil.loginRole().isPic() || loginUserInfo.subGroup !== "SP_OP"));
});
</script>
<fieldset>
	<legend>批次作業申請單</legend>
    <form id="tabBtForm">
        <input id="id" class="hidden" name="id" />
        <input id="division" class="hidden" name="division" />
		<table class="grid_query">
			<tr>
				<th>程式CL及工作執行日期</th>
                <jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
                    <jsp:param name="id1" value="it" />
                    <jsp:param name="name1" value="it" />
                </jsp:include>
                <th><s:message code="form.countersigned.form.userId" text="系統負責人" /></th>
                <td><input id="userId" name="userId" maxlength="10" type="text" style="width: 8rem;" /></td>
				<td colspan="2"></td>
			</tr>
			<tr>
				<th>PSB名稱</th>
				<td colspan="5">
					<input id='psb' name='psb' type="text" />
				</td>
			</tr>
			<tr>
				<td colspan="6">
					<fieldset>
						<legend>作業項目</legend>
						<table class="grid_query">
							<tr>
								<td colspan="4">
									<label><input id='isCange' name='isCange' type="checkbox" />批次作業流程變更</label>
									<label><input id='isOtherDesc' name='isOtherDesc' type="checkbox" />其他(綁工作內容或附件說明)</label>
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<label><input id='isHelp' name='isHelp' type="checkbox" />執行HELP程式</label>
									<label><input id='isAllow' name='isAllow' type="checkbox" checked />允許JOB在TWS執行時，Return Code <=4視為正常</label>
								</td>
							</tr>
							<tr>
								<th>CL JCL</th>
								<td><textarea id='cljcl' name='cljcl' class='resizable-textarea' cols="35" rows="4" maxlength="2000" ></textarea></td>
								<td><label><input id='isOther' name='isOther' type="checkbox" />其他</label></td>
							</tr>
							<tr>
								<th>執行JCL</th>
								<td><textarea id='jcl' name='jcl' class='resizable-textarea' cols="35" rows="4" maxlength="2000" ></textarea></td>
								<td><label><input id='isHelpCl' name='isHelpCl' type="checkbox" />HELP程式CL</label></td>
							</tr>
							<tr>
								<th>程式名稱</th>
								<td><textarea id='programName' name='programName' class='resizable-textarea' cols="35" rows="4" maxlength="2000" ></textarea></td>
								<th>程式支數</th>
								<td><input id='programNumber' name='programNumber' type="text" maxlength="6" /></td>
							</tr>
						</table>
					</fieldset>
					<table class="grid_query">
						<tr>
							<th>申請原因</th>
							<td><textarea id='reason' name='reason' class='resizable-textarea' cols="50" rows="4"></textarea></td>
						</tr>
						<tr>
							<th>資管科工作內容</th>
							<td><textarea id='content' name='content' class='resizable-textarea' cols="50" rows="4"></textarea></td>
						</tr>
						<tr>
							<th>批次單的備註</th>
							<td><textarea id='remark' name='remark' class='resizable-textarea' cols="50" rows="4"></textarea></td>
						</tr>
						<tr>
							<td colspan="6">
								<fieldset>
									<legend>回復作業</legend>
									<table class="grid_query">
										<tr>
										    <th><input id='isRollback' name='isRollback' type="checkbox" checked /></th>
											<th><label for='isRollback'>回復到前一版</label></th>
											<th>&emsp;其他</th>
											<td><textarea id='other' name='other' class='resizable-textarea' cols="50" rows="5"></textarea></td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
						<tr>
							<th>會辦機房內容</th>
							<td><textarea id='countersignedIDC' name='countersignedIDC' class='resizable-textarea' cols="50" rows="4"></textarea></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
    </form>
</fieldset>
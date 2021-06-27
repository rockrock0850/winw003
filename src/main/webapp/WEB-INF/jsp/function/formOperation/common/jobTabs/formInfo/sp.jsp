<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<script>//# sourceURL=spInfo.js
$(function () {
	DateUtil.dateTimePicker();
	initView();
	initEvent();
	fetchInfo(form2object('headForm'));
	authViewControl();
});

function initEvent(){
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

	$('input[type=radio]').change(function () {
		$(this).prop('chekced', true);
		$('input[type=radio]').not(this).prop('checked', false);
	});
}

function initView () {
	SendUtil.get('/html/getDropdownList', 'cCategory', function (options) {
		HtmlUtil.singleSelect('select#cCategory', options);
	}, ajaxSetting);
	
	SendUtil.get('/html/getDropdownList', 'WORK_LEVEL', function (options) {
		var optionsRemoved = [];
		$(options).each(function(){
			if(this.wording.indexOf('組') != -1) {
				optionsRemoved.push(this);
			}
		});
		HtmlUtil.singleSelect('select#working', optionsRemoved);
	}, ajaxSetting);
	$('form#infoForm .resizable-textarea').resizable({handles: "se"});
}
</script>

<div>
	<form id="infoForm">
		<fieldset>
			<legend>工作明細</legend>
			<table class="grid_query">
				<tr>
					<td valign="top">
						<table>
							<tr>
								<th><font color="red"><s:message code="form.job.form.summary" text='工作摘要' /></font></th>
								<td><textarea id='summary' name="summary" class='resizable-textarea' cols="40" rows="1" maxlength="500" ></textarea></td>
							</tr>
							<tr>
								<th><font color="red"><s:message code="form.job.form.content" text='工作內容' /></font></th>
								<td><textarea id='content' name="content" class='resizable-textarea' cols="40" rows="6" maxlength="2000" ></textarea></td>
							</tr>
							<tr>
								<th><font color="red"><s:message code="form.job.form.effectScope" text='影響範圍' /></font></th>
								<td><textarea id='effectScope' name="effectScope" class='resizable-textarea' cols="40" rows="6" maxlength="500" ></textarea></td>
							</tr>
							<tr>
								<th><s:message code="form.job.form.remark" text='備註' /></th>
								<td><textarea id='remark' name="remark" class='resizable-textarea' cols="50" rows="6" maxlength="1000" ></textarea></td>
							</tr>
							<tr>
								<td colspan="2">
									<fieldset>
										<legend><s:message code="form.job.form.work.fail.recover" text='作業失敗回復程序' /></legend>
										<table class="grid_query">
											<tr>
												<th><label for='isReset'><s:message code="form.job.form.isReset" text='回復原設定' /></label></th>
												<td><input id="isReset" name="isReset" value="N" type="checkbox" checked /></td>
											</tr>
											<tr>
												<th><s:message code="form.job.form.reset" text='其他回復作業' /></th>
												<td><textarea id='reset' name="reset" class='resizable-textarea' cols="40" rows="6" maxlength="500" ></textarea></td>
											</tr>
										</table>
									</fieldset>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<fieldset>
										<legend><s:message code="form.job.form.class" text='會辦科' /></legend>
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
								<td align="right"><label for='isTest'><s:message code="form.job.form.isTest" text='TEST' /></label></td>
								<td><input id='isTest' type="radio" name='isTest' checked></td>
							</tr>
							<tr>
								<td align="right"><label for='isProduction'><s:message code="form.job.form.isProduction" text='PRODUCTION' /></label></td>
								<td><input id='isProduction' type="radio" name='isProduction'></td>
							</tr>
							<tr>
								<td align="right"><label for='isHandleFirst'><s:message code="form.job.form.isHandleFirst" text='先處理後呈閱' /></label></td>
								<td><input id="isHandleFirst" name="isHandleFirst" type="checkbox" /></td>
							</tr>	
							<tr>			
								<th><font color="red"><s:message code='form.job.form.ect' text='預計完成時間'/></font></th>
								<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
									<jsp:param name="id1" value="ect" />
									<jsp:param name="name1" value="ect" />
								</jsp:include>
							</tr>
							<tr>
								<td align="right" nowrap="nowrap"><label for='isForward'><s:message code="form.job.form.isForward" text='是否需送交組態維護人員' /></label></td>
								<td><input id="isForward" name="isForward" type="checkbox" /></td>
							</tr>
							<tr>
								<td align="right" nowrap="nowrap"><label for='isInterrupt'><s:message code="form.job.form.isInterrupt" text='是否造成服務中斷或需要停機' /></label></td>
								<td><input id="isInterrupt" name="isInterrupt" type="checkbox" /></td>
							</tr>
							<tr>
								<td align="right"><s:message code="form.job.form.offLineTime" text='公告停機時間' /></td>
								<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
									<jsp:param name="id1" value="offLineTime" />
									<jsp:param name="name1" value="offLineTime" />
								</jsp:include>
							</tr>
							<tr>
								<td align="right"><s:message code="form.job.form.onLineTime" text='公告恢復時間' /></td>
								<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
									<jsp:param name="id1" value="onLineTime" />
									<jsp:param name="name1" value="onLineTime" />
								</jsp:include>
							</tr>
							<tr>
								<td align="right"><font color="red"><s:message code="form.job.form.working" text='工作組別' /></font></td>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="working" />
										<jsp:param name="name" value="working" />
									</jsp:include>
								</td>
							</tr>
							<tr>
								<th><font color="red"><s:message code='form.question.form.info.question.c.category' text='組態分類'/></font></th>
								<td>
								<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
									<jsp:param name="id" value="cCategory" />
									<jsp:param name="name" value="cCategory" />
								</jsp:include>
								</td>
							</tr>
							<tr>
								<th><font color="red"><s:message code='form.question.form.info.question.c.class' text='組態元件類別'/></font></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="cClass" />
										<jsp:param name="name" value="cClass" />
									</jsp:include>
								</td>
							</tr>
							<tr>
								<th><font color="red"><s:message code='form.question.form.info.question.c.component' text='組態元件'/></font></th>
								<jsp:include page='/WEB-INF/jsp/common/models/cComponentModel.jsp' />	
							</tr>
							<tr><jsp:include page="/WEB-INF/jsp/common/models/workingItemModel.jsp" /></tr>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend>日期</legend>
			<table class="grid_query">
				<tr>
					<th><font color="red"><s:message code='form.job.form.eot' text='預計開始時間'/></font></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="eot" />
						<jsp:param name="name1" value="eot" />
					</jsp:include>					
					<th><font color="red"><s:message code='form.job.form.ect' text='預計完成時間'/></font></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="mect" />
						<jsp:param name="name1" value="mect" />
					</jsp:include>
				</tr>
				<tr>
					<th><s:message code='form.job.form.ast' text='實際開始時間'/></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="ast" />
						<jsp:param name="name1" value="ast" />
					</jsp:include>
					<th><s:message code='form.job.form.act' text='實際完成時間'/></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="act" />
						<jsp:param name="name1" value="act" />
					</jsp:include>
				</tr>
			</table>
		</fieldset>
	</form>
</div>
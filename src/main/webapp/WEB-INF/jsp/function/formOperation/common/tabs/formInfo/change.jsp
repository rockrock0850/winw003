<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<div>
	<form id="infoForm">
		<fieldset>
			<legend><s:message code='form.question.user.detail' text='使用者資訊'/></legend>
			<table class="grid_query">
				<tr>
					<th><span style="color: red; "><s:message code='form.question.question.source.department.type' text='提出單位分類'/></span></th>
					<td>
						<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
							<jsp:param name="id" value="unitCategory" />
							<jsp:param name="name" value="unitCategory" />
						</jsp:include>
					</td>
					<th><span style="color: red; "><s:message code='form.question.question.source.user.department' text='提出人員單位'/></span></th>
					<td>
						<input id='unitId' name='unitId' type="text" maxlength="30" />
					</td>
					<th><span style="color: red; "><s:message code='form.question.question.source.user.name' text='提出人員姓名'/></span></th>
					<td>
						<input id='userName' name='userName' type="text" maxlength="10"/>
						<input id='userId' class='hidden' name='userId' type="text" />
					</td>
				</tr>
				<tr>
					<th><s:message code='form.question.form.info.phone' text='電話'/></th>
					<td><input id='phone' name='phone' type="text" value="" maxlength="30"/></td>
					<th><s:message code='form.question.form.info.email' text='電子郵件' /></th>
					<td colspan="3"><input id='email' name='email' type="text" value="" size="60" maxlength="100"/></td>
				</tr>
			</table>
		</fieldset>
	
		<fieldset>
			<legend><s:message code='form.change.form.info.change.detail' text='變更明細'/></legend>
			<table class="grid_query">
				<tr>
					<td valign="top">
						<table>
							<tr>
								<th><span style="color: red; "><s:message code='form.change.form.info.change.summary' text='變更摘要'/></span></th>
								<td><input type="text" id="summary" name="summary" value="" maxlength="500" /></td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.change.form.info.change.content' text='變更內容'/></span></th>
								<td><textarea id="content" name="content" class='resizable-textarea' cols="50" rows="12" maxlength="2000" ></textarea></td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.change.form.info.change.effect.system' text='變更影響系統'/></span></th>
								<td><textarea id="effectSystem" name="effectSystem" class='resizable-textarea' cols="50" rows="12" maxlength="500"></textarea></td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.question.form.info.question.c.category' text='組態分類'/></span></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="cCategory" />
										<jsp:param name="name" value="cCategory" />
									</jsp:include>
								</td>
								</tr>
								<tr>
									<th><span style="color: red; "><s:message code='form.question.form.info.question.c.class' text='組態元件類別'/></span></th>
									<td>
										<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
											<jsp:param name="id" value="cClass" />
											<jsp:param name="name" value="cClass" />
										</jsp:include>
									</td>
								</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.question.form.info.question.c.component' text='組態元件'/></span></th>
								<jsp:include page='/WEB-INF/jsp/common/models/cComponentModel.jsp' />
							</tr>
						</table>
					</td>
					<td valign="top">
						<table class="grid_query">
							<tr>
								<td><label><input type="checkbox" id="isNewSystem" name="isNewSystem"/><s:message code='form.change.form.info.is.new.system' text='是新系統'/></label></td>
								<td><a href="${pageContext.request.contextPath}/static/attachment/TCB_上線驗收準則(範本).doc" target='_blank'><s:message code='form.change.form.info.tcb' text='連結至TCB上線驗收準則'/></a></td>
							</tr>
							<tr>
								<td><label><input type="checkbox" id="isNewService" name="isNewService"/><s:message code='form.change.form.info.new.big.service' text='新服務暨重大服務'/></label></td>
								<td><span id='isNewServiceWarning' class='hidden' style="color: red; ">請檢附新服務暨服務異動評估規劃報告</span></td>
							</tr>
							<tr>
								<td><label><input type="checkbox" id="isUrgent" name="isUrgent"/><s:message code='form.change.form.info.isUrgent' text='緊急變更'/></label></td>
								<td></td>
							</tr>
							<tr>
								<td><s:message code='form.change.form.info.starnd' text='標準變更作業'/></td>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="standard" />
										<jsp:param name="name" value="standard" />
									</jsp:include>
								</td>				
							</tr>
							<tr>
								<td><span style="color: red; "><s:message code='form.change.form.info.change.type' text='變更類型'/></span></td>
								<td><input type="text" id="changeType" name="changeType" value="<s:message code ='form.change.changeType.1' text='一般變更'/>" readonly="readonly"/></td>
							</tr>
							<tr>
								<td><span style="color: red; "><s:message code='form.change.form.info.change.rank' text='變更等級'/></span></td>
								<td><input type="text" id="changeRank" name="changeRank" value="<s:message code ='form.change.changeRank.1' text='次要變更'/>" readonly="readonly"/></td>
							</tr>
							<tr>
								<td colspan="2"><label><input type="checkbox" id="isEffectField" name='isEffectField' /><s:message code='form.change.form.info.isEffectField' text='有新增異動欄位影響到資料倉儲系統產出資料'/></label></td>
							</tr>
							<tr>
								<td colspan="2"><label><input type="checkbox" id="isEffectAccountant" name='isEffectAccountant' /><s:message code='form.change.form.info.isEffectAccountant' text='有新增異動會計科目影響到資料倉儲系統產出資料'/></label></td>
							</tr>
							<tr>
								<td colspan="2"><label><input type="checkbox" id="isModifyProgram" name='isModifyProgram' /><s:message code='form.change.form.info.is.modify.program' text='未有修改程式'/></label></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend><s:message code='form.question.form.info.date' text='日期'/></legend>
			<table class="grid_query">
				<tr>
					<th>
						<s:message code='form.change.form.info.cat' text='變更申請時間'/>
					</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="cat" />
						<jsp:param name="name1" value="cat" />
					</jsp:include>
					<th>
						<span style="color: red; "><s:message code='form.change.form.info.cct' text='預計變更結束時間'/></span>
					</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="cct" />
						<jsp:param name="name1" value="cct" />
					</jsp:include>
				</tr>
			</table>
		</fieldset>
	</form>
</div>

<script>//# sourceURL=chgInfo.js
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
	
	SendUtil.get("/html/getDropdownList", 3, function (option) {
		HtmlUtil.singleSelect('select#changeId', option);
	}, ajaxSetting);
	
	SendUtil.get("/html/getDivisionSelectors", false, function (option) {
		HtmlUtil.singleSelect('select#division', option);
	}, ajaxSetting);
	
	SendUtil.get('/html/getDropdownList', 'cCategory', function (options) {
		HtmlUtil.singleSelect('select#cCategory', options);
	}, ajaxSetting);
	
	SendUtil.get('/html/getDropdownList', 'StandardChange', function (options) {
		HtmlUtil.singleSelect('select#standard', options);
	}, ajaxSetting);
	$('form#infoForm .resizable-textarea').resizable({handles: "se"});
	//避免因為option資料過長導致跑版
	$("select#standard").css("width","150px");
}

function initEvent () {
	$('select#division').change(function () {
		if (!$(this).val().trim()) {
			$('input#divisionWording').val('');
		}
		
		$('input#divisionWording').val($(this).val());
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
	
	// 根據衝擊分析分數以及是否勾選新系統或重大變更的選項,調整變更類型跟變更等級的文字
	$("input#isNewSystem,input#isNewService,select#standard").change(verifyChangeLevel);
}
</script>
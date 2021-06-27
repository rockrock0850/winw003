<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<div>
	<form id="infoForm">
		<fieldset>
			<legend><s:message code='form.event.user.detail' text='使用者資訊'/></legend>
			<table width="100%" class="grid_query">
				<tr>
					<th><span style="color: red; "><s:message code="form.event.question.source.department.type" text="提出單位分類" /></span></th>
					<td>
						<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
							<jsp:param name="id" value="unitCategory" />
							<jsp:param name="name" value="unitCategory" />
						</jsp:include>
					</td>
					<th><span style="color: red; "><s:message code="form.event.question.source.user.department" text="提出人員單位" /></span></th>
					<td>
						<input id='unitId' name='unitId' type="text" maxlength="30" />
					</td>
					<th><span style="color: red; "><s:message code='form.event.question.source.user.name' text='提出人員姓名'/></span></th>
					<td>
						<input id='userName' name='userName' type="text" maxlength="10" />
						<input id='userId' class='hidden' name='userId' type="text" />
					</td>
					
				</tr>
				<tr>
					<th><s:message code='form.event.form.info.phone' text='電話'/></th>
					<td><input id='phone' name='phone' type="text" maxlength="30" /></td>
					<th><s:message code='form.event.form.info.email' text='電子郵件' /></th>
					<td><input id='email' name='email' type="text" value="" maxlength="100" /></td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend><s:message code='form.event.form.info.event.detail' text='事件明細'/></legend>
			<table class="grid_query">
				<tr>
					<td valign="top">
						<table class="grid_query">
							<tr>
								<th><span style="color: red; "><s:message code='form.event.form.info.event.summary' text='事件摘要'/></span></th>
								<td><input id='summary' name='summary' type="text" value="" maxlength="500" /></td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.event.form.info.event.content' text='事件內容'/></span></th>
								<td><textarea id='content' name='content' class='resizable-textarea' cols="50" rows="12" maxlength="2000"></textarea></td>
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
							<tr>
								<td colspan="2">
									<fieldset>
										<legend><s:message code='form.question.form.info.question.countersigneds' text='會辦科'/></legend>
										<jsp:include page='/WEB-INF/jsp/common/models/cListModel.jsp' >
											<jsp:param name="formClass" value="INC" />
										</jsp:include>
									</fieldset>
								</td>
							</tr>
						</table>
					</td>
					<td valign="top">
						<table class="grid_query">
							<tr>
								<td colspan="2">
									<fieldset>
										<jsp:include page='/WEB-INF/jsp/common/models/onlineFailModel.jsp'/>
									</fieldset>
								</td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.event.form.info.eventClass' text='事件主類別'/></span></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="eventClass" />
										<jsp:param name="name" value="eventClass" />
									</jsp:include>
								</td>
							</tr>
							<tr>
								<th><s:message code='form.event.form.info.eventType' text='事件類型'/></th>
								<td><input id='eventType' type="text" name='eventType' value="" readonly="readonly"/></td>
							</tr>
							<tr>
								<th><s:message code='form.event.form.info.eventSecurity' text='資安事件'/></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="eventSecurity" />
										<jsp:param name="name" value="eventSecurity" />
										<jsp:param name="defaultName" value="global.select.please.choose" />
									</jsp:include>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="height: 20px;">&nbsp;</td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.event.form.info.service.type' text='服務類別'/></span></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="sClass" />
										<jsp:param name="name" value="sClass" />
									</jsp:include>
								</td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.event.form.info.service.sub.type' text='服務子類別'/></span></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="sSubClass" />
										<jsp:param name="name" value="sSubClass" />
									</jsp:include>
								</td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.event.form.info.system.name' text='系統名稱'/></span></th>
								<jsp:include page='/WEB-INF/jsp/common/models/systemModel.jsp' />
							</tr>
							<tr>
								<th><s:message code='form.event.form.info.asset.group' text='資訊資產群組'/></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/textareaModel.jsp'>
										<jsp:param name="id" value="assetGroup" />
										<jsp:param name="name" value="assetGroup" />
										<jsp:param name="readonly" value="true" />
									</jsp:include> 
								</td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.event.form.info.effect.scope' text='影響範圍'/></span></th>
								<td>
									<input id='effectScope' name='effectScope' type="text" size="4" style="width: 5rem;" readonly />
									<jsp:include page='/WEB-INF/jsp/common/models/textareaModel.jsp'>
										<jsp:param name="id" value="effectScopeWording" />
										<jsp:param name="name" value="effectScopeWording" />
										<jsp:param name="readonly" value="true" />
									</jsp:include> 
									<button id='effectScopeBtn' type='button' class="iconx-search" onclick="ESDialog.show()">列表</button>
								</td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.event.form.info.urgent.level' text='緊急程度'/></span></th>
								<td>
									<input id='urgentLevel' name='urgentLevel' type="text" size="4" style="width: 5rem;" readonly />
									<jsp:include page='/WEB-INF/jsp/common/models/textareaModel.jsp'>
										<jsp:param name="id" value="urgentLevelWording" />
										<jsp:param name="name" value="urgentLevelWording" />
										<jsp:param name="readonly" value="true" />
									</jsp:include> 
									<button id='urgentLevelBtn' type='button' class="iconx-search" onclick="ULDialog.show('INC_URGENT_LEVEL');">列表</button>
								</td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.event.form.info.eventPriority' text='事件優先順序'/></span></th>
								<td>
									<input id='eventPriority' name='eventPriority' type="text" value="" size="1" readonly style="width: 2rem;" />
									<jsp:include page='/WEB-INF/jsp/common/models/textareaModel.jsp'>
										<jsp:param name="id" value="eventPriorityWording" />
										<jsp:param name="name" value="eventPriorityWording" />
										<jsp:param name="readonly" value="true" />
									</jsp:include> 
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend><s:message code='form.event.form.info.date' text='日期'/></legend>
			<table width="100%" class="grid_query">
				<tr>
					<th><label for='isIVR'><s:message code='form.event.form.info.isIVR' text='事件來源為IVR'/></label></th>
					<td><input id='isIVR' type="checkbox" name='isIVR' /></td>
					<th><label for='isMainEvent'><s:message code='form.event.form.info.isMainEvent' text='設定為主要事件單'/></label></th>
					<td><input id='isMainEvent' type="checkbox" name='isMainEvent' /></td>
					<th><label for='isInterrupt'><s:message code='form.event.form.info.isInterrupt' text='全部功能服務中斷'/></label></th>
					<td><input id='isInterrupt' type="checkbox" name='isInterrupt' /></td>
				</tr>
				<tr>
					<th><span style="color: red; "><s:message code='form.event.form.info.infoDateCreateTime' text='事件發生時間'/></span></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="assignTime" />
						<jsp:param name="name1" value="assignTime" />
					</jsp:include>
					<th><s:message code='form.event.form.info.main.event' text='併入主要事件單'/></th>
					<td>
						<input id='mainEvent' style="width:auto !important" name='mainEvent' type="text" readonly />&nbsp;&nbsp;
						<button id='incFormDailog' type='button' class="iconx-search" onclick="alertIncDialog();">列表</button>
					</td>
					<th><label for='isSameInc'><span style="color: red; ">同一事件兩日內復發</span></label></th>
                    <td><input id='isSameInc' type="checkbox" name='isSameInc' /></td>
				</tr>
				<tr>
					<th><s:message code='form.event.form.info.ect' text='目標解決時間'/></th>
					<td><input id="ect" type="text" name="ect" style="width: 10rem;" readonly /></td>
					<th>事件(當下)排除時間</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="excludeTime" />
						<jsp:param name="name1" value="excludeTime" />
					</jsp:include>
					<th>事件完成時間</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="act" />
						<jsp:param name="name1" value="act" />
					</jsp:include>
				</tr>
			</table>
		</fieldset>
	</form>
</div>

<script>//# sourceURL=incInfo.js
$(function () {
	DateUtil.dateTimePicker();
	DateUtil.dateTimePickerById('input#assignTime', {minDate:''});
	DateUtil.dateTimePickerById('input#excludeTime', {minDate:''});

	initView();
	initEvent();
	fetchInfo(form2object('headForm'));
	authViewControl();
});

function initView () {
	SendUtil.get("/html/getDropdownList", 1, function (option) {
		HtmlUtil.singleSelect('select#unitCategory', option);
	}, ajaxSetting);

	SendUtil.get("/html/getDropdownList", 4, function (option) {
		HtmlUtil.singleSelect('select#eventClass', option);
	}, ajaxSetting);

	SendUtil.get('/html/getDropdownList', 2, function (options) {
		HtmlUtil.singleSelect('select#sClass', options);
	}, ajaxSetting);

	SendUtil.get('/html/getDropdownList', 2, function (options) {
		HtmlUtil.singleSelect('select#sClass', options);
	}, ajaxSetting);

	SendUtil.get('/html/getDropdownList', 'cCategory', function (options) {
		HtmlUtil.singleSelect('select#cCategory', options);
	}, ajaxSetting);

	SendUtil.get("/html/getDropdownList", 'SecurityEvent', function (options) {
		HtmlUtil.singleSelect('select#eventSecurity', options);
	}, ajaxSetting);
	$('form#infoForm .resizable-textarea').resizable({handles: "se"});
}

function initEvent () {
	$('input#effectScope').change(function () {
		fetchDemandLevel('input#eventPriority');
	});

	$('input#urgentLevel').change(function () {
		fetchDemandLevel('input#eventPriority');
	});

	$('input#eventPriority').change(function () {
		fetchPriorityDesc('textarea#eventPriorityWording', $(this).val());
	});
	
	$('select#eventClass').change(function() {
		let value = $(this).find("option:selected").val();
		if(value) {
			let params = {};
			params.value = value
			params.wording = $(this).find("option:selected").text();
			SendUtil.post('/html/getSysOptionRole', params, function (info) {
				$("input#eventType").val(info.condition);
			}, null, true);
		} else {
			$("input#eventType").val('');
		}
	});

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

	$("select#effectScope").change(function() {
		if(!$(this).val()) {
			$("select#urgentLevel").val("");
			$("#urgentLevelWording").val("");
			$("#eventPriorityWording").val("");
		}
	});
	
	$('input#isMainEvent').change(function () {
		$('button#incFormDailog').toggle(!$(this).is(':checked'));
	});
}

function fetchDemandLevel (demandName) {
	var effect = $('input#effectScope').val();
	var urgent = $('input#urgentLevel').val();

	if (!effect.trim() || !urgent.trim()) {
		$(demandName).val('');
		return;
	}

	var formClass = form2object('headForm').formClass;
	SendUtil.get('/html/getDemandLevel', [formClass, effect, urgent], function (response) {
		$(demandName).val(response.wording).change();
	});
}

function fetchPriorityDesc (priorityName, priority) {
	if (!priority.trim()) {
		$(priorityName).val('');
		return;
	}

	SendUtil.get('/html/getDropdownList', 'INC_PRIORITY', function (response) {
		$.each(response, function () {
			if (this.value == priority) {
				$(priorityName).val(this.wording);
				return false;// break;
			}
		});
	}, null, true);
}

function alertIncDialog () {
	var isDisabled = $('input#isMainEvent').prop('disabled');
	
	if (!isDisabled) {
		var callback = function (result) {
			$('#mainEvent').val(result.checkValue);
		}
		
		IncFormDialog.show(callback);
	}
}

function validate(param) { 
	let isReview = headForm.verifyType.value == 'REVIEW';

	if (isReview) {
		// 「實際完成時間」小於「預計完成時間」 => pass
		if (!(param.act < param.ect)) {
			alert("<s:message code='from.common.act.must.less.than.ect' text='「實際完成時間」必須小於「預計完成時間」'/>");
			DialogUtil.close();
			return false; 
		}
	}

	return true;
}
</script>
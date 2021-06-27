<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<div>
	<form id='infoForm'>
		<fieldset>
			<legend>使用者資訊</legend>
			<table width="100%" class="grid_query">
				<tr>
					<th><span style="color: red; ">提出單位分類</span></th>
					<td>
						<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
							<jsp:param name="id" value="unitCategory" />
							<jsp:param name="name" value="unitCategory" />
						</jsp:include>
					</td>
					<th><span style="color: red; ">提出人員單位</span></th>
					<td>
						<input id='unitId' name='unitId' type="text" maxlength="30" />
					</td>
					<th><span style="color: red; ">提出人員姓名</span></th>
					<td>
						<input id='userName' name='userName' type="text" maxlength="10" />
						<input id='userId' class='hidden' name='userId' type="text"  />
					</td>
				</tr>
				<tr>
					<th>電話</th>
					<td><input id='phone' name='phone' type="text" maxlength="30" /></td>
					<th>電子郵件</th>
					<td><input id='email' name='email' type="text" value="" maxlength="100" /></td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend>需求明細</legend>
			<table class="grid_query">
				<tr>
					<td valign="top">
						<table class="grid_query">
							<tr>
								<th><span style="color: red; ">需求摘要</span></th>
								<td><input id='summary' name='summary' type="text" value="" maxlength="500" /></td>
							</tr>
							<tr>
								<th><span style="color: red; ">需求內容</span></th>
								<td><textarea id="content" name="content" class='resizable-textarea' cols="50" rows="12" maxlength="2000" ></textarea></td>
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
										<legend>會辦科</legend>
										<jsp:include page='/WEB-INF/jsp/common/models/cListModel.jsp' >
											<jsp:param name="formClass" value="SR" />
										</jsp:include>
									</fieldset>
								</td>
							</tr>
						</table>
					</td>
					<td valign="top">
						<table class="grid_query">
							<tr class='hidden'>
								<th>負責科</th>
								<td nowrap="nowrap">
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="division" />
										<jsp:param name="name" value="division" />
									</jsp:include>
									<input id='divisionWording' type="text" value="" readonly style="width: 10rem;" />
								</td>
							</tr>
							<tr>
								<td style="height: 50px;">&nbsp;</td>
							</tr>
							<tr>
								<th><span style="color: red; ">服務類別</span></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="sClass" />
										<jsp:param name="name" value="sClass" />
									</jsp:include>
								</td>
							</tr>
							<tr>
								<th><span>服務子類別</span></th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="sSubClass" />
										<jsp:param name="name" value="sSubClass" />
									</jsp:include>
								</td>
							</tr>
							<tr>
								<th><span style="color: red; ">系統名稱</span></th>
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
							<tr>
								<th><span style="color: red;">影響範圍</span></th>
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
								<th><span style="color: red; ">緊急程度</span></th>
								<td>
									<input id='urgentLevel' name='urgentLevel' type="text" size="4" style="width: 5rem;" readonly />
									<jsp:include page='/WEB-INF/jsp/common/models/textareaModel.jsp'>
										<jsp:param name="id" value="urgentLevelWording" />
										<jsp:param name="name" value="urgentLevelWording" />
										<jsp:param name="readonly" value="true" />
									</jsp:include>
									<button id='urgentLevelBtn' type='button' class="iconx-search" onclick="ULDialog.show('SR_URGENT_LEVEL');">列表</button>
								</td>
							</tr>
							<tr>
								<th><span style="color: red; ">需求等級</span></th>
								<td><input id='requireRank' name='requireRank' type="text" value="" size="1" readonly="readonly" style="width: 2rem;" /></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend>日期</legend>
			<table class="grid_query">
				<tr>
					<th><span style="color: red; ">預計完成時間</span></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="ect" />
						<jsp:param name="name1" value="ect" />
					</jsp:include>
				</tr>
				<tr>
					<th><span style="color: red; ">與業務單位確認預計上線時間</span></th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="eot" />
						<jsp:param name="name1" value="eot" />
					</jsp:include>
					<th>實際完成時間</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="act" />
						<jsp:param name="name1" value="act" />
					</jsp:include>
				</tr>
			</table>
		</fieldset>
	</form>
</div>

<script>//# sourceURL=srInfo.js
$(function () {
	DateUtil.dateTimePicker();
	initView();
	initEvent();
	fetchInfo(form2object('headForm'));
	authViewControl();
	initFieldValue();
});

function initFieldValue () {
	var formInfo = form2object('headForm');
	
	if (!formInfo.formStatus) {
		var userCreatedText = $('select#divisionCreated option:selected').text();
		$('select#division').val(userCreatedText).change();
	}
}

function initView () {
	SendUtil.get("/html/getDropdownList", 1, function (option) {
		HtmlUtil.singleSelect('select#unitCategory', option);
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
	$('input#effectScope').change(function () {
		fetchDemandLevel('input#requireRank');
	});

	$('input#urgentLevel').change(function () {
		fetchDemandLevel('input#requireRank');
	});

	$('select#division').change(function () {
		if ($(this).val()) {
			$('input#divisionWording').val($(this).val());
		} else {
			$('input#divisionWording').val('');
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
		}
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
		$(demandName).val(response.wording);
	});
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
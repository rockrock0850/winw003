<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<div>
	<form id='infoForm'>
		<input type="hidden" id="questionIdWording" name="questionIdWording">
		<fieldset>
			<legend><s:message code='form.question.user.detail' text='使用者資訊'/></legend>
			<table class="grid_query">
				<tr>
					<th><span style="color: red; "><s:message code='form.question.question.source' text='問題來源'/></span></th>
					<td>
						<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
							<jsp:param name="id" value="questionId" />
							<jsp:param name="name" value="questionId" />
						</jsp:include>
					</td>
				</tr>
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
						<input id='userId' class='hidden' name='userId' type="text"  />
					</td>
				</tr>
				<tr>
					<th><s:message code='form.question.form.info.phone' text='電話'/></th>
					<td><input id='phone' name='phone' type="text" value=""  maxlength="30"/></td>
					<th><s:message code='form.question.form.info.email' text='電子郵件'/></th>
					<td colspan="3"><input id='email' name='email' type="text" value="" size="60" /></td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend><s:message code='form.question.form.info.question.detail' text='問題明細'/></legend>
			<table class="grid_query">
				<tr>
					<td valign="top">
						<table>
							<tr>
								<th><span style="color: red; "><s:message code='form.question.form.info.question.summary' text='問題摘要'/></span></th>
								<td><input type="text" id="summary" name="summary" value="" maxlength="500"/></td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.question.form.info.question.content' text='問題內容'/></span></th>
								<td><textarea id="content" name="content" class='resizable-textarea' cols="50" rows="12" maxlength="2000"></textarea></td>
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
									<th><font color="red"><s:message code='form.question.form.info.question.c.class' text='組態元件類別'/></font></th>
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
											<jsp:param name="formClass" value="Q" />
										</jsp:include>
									</fieldset>
								</td>
						</tr>
						</table>
					</td>
					<td valign="top">
						<table class="grid_query">
							<tr>
								<th><s:message code='form.question.process.knowledge.10' text='參照知識庫'/></th>
								<td>
									<input id='solutions' style="width:auto !important" name='solutions' type="text" readonly onclick='hyperLink();' />&nbsp;&nbsp;
									<button id='knowledgeDialog' type='button' class="iconx-search" onclick="alertKnowledgeDialog();">列表</button>
									<button id='knowledgeDelete' type='button' class="iconx-search" onclick="javascript: $('input#solutions').val('')">清除</button>
								</td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.question.form.info.service.type' text='服務類別'/></span></th>
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
							<tr>
								<th><span style="color: red; "><s:message code='form.question.form.info.system.name' text='系統名稱'/></span></th>
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
								<th><span style="color: red; "><s:message code='form.question.form.info.effect.scope' text='影響範圍'/></span></th>
								<td>
									<input id='effectScope' name='effectScope' type="text" size="4" style="width: 5rem;" readonly />
									<jsp:include page='/WEB-INF/jsp/common/models/textareaModel.jsp'>
										<jsp:param name="id" value="effectScopeWording" />
										<jsp:param name="name" value="effectScopeWording" />
										<jsp:param name="readonly" value="true" />
									</jsp:include> 
									<button id='effectScopeBtn' type='button' class="iconx-search" onclick="ESDialog.show();">列表</button>
								</td>
							</tr>
							<tr>
								<th><span style="color: red; "><s:message code='form.question.form.info.urgent.level' text='緊急程度'/></span></th>
								<td>
									<input id='urgentLevel' name='urgentLevel' type="text" size="4" style="width: 5rem;" readonly />
									<jsp:include page='/WEB-INF/jsp/common/models/textareaModel.jsp'>
										<jsp:param name="id" value="urgentLevelWording" />
										<jsp:param name="name" value="urgentLevelWording" />
										<jsp:param name="readonly" value="true" />
									</jsp:include> 
									<button id='urgentLevelBtn' type='button' class="iconx-search" onclick="ULDialog.show('Q_URGENT_LEVEL');">列表</button>
								</td>
							</tr>
							<tr>
								<td><span style="color: red; "><s:message code='form.question.form.info.rank.level.desc' text='問題優先順序'/></span></td>
								<td>
									<input type="text" id="questionPriority" name="questionPriority" value="" size="4"style="width: 4rem;" readonly="readonly"/>
									<jsp:include page='/WEB-INF/jsp/common/models/textareaModel.jsp'>
										<jsp:param name="id" value="questionPriorityWording" />
										<jsp:param name="name" value="questionPriorityWording" />
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
				<legend><s:message code='form.question.form.info.date' text='日期'/></legend>
				<table class="grid_query">
					<tr>
						<th><s:message code='form.question.form.info.report.date' text='報告日期'/></th>
						<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
							<jsp:param name="id1" value="reportTime" />
							<jsp:param name="name1" value="reportTime" />
						</jsp:include>
						<th><s:message code='form.question.form.info.ast' text='實際開始時間'/></th>
						<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
							<jsp:param name="id1" value="ast" />
							<jsp:param name="name1" value="ast" />
						</jsp:include>
						<th><label for='isSpecial'><s:message code='form.question.form.info.is.special' text='特殊結案'/></label></th>
						<td>
							<input type="checkbox" id="isSpecial" name="isSpecial"/>
						</td>
						<th><s:message code='form.question.form.info.special.end.case.type' text='特殊結案狀態'/></th>
						<td>
							<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
								<jsp:param name="id" value="specialEndCaseType" />
								<jsp:param name="name" value="specialEndCaseType" />
							</jsp:include>
						</td>
					</tr>
					<tr>
						<th><span style="color: red; "><s:message code='form.question.form.info.ect' text='預計完成時間'/></span></th>
						<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
							<jsp:param name="id1" value="ect" />
							<jsp:param name="name1" value="ect" />
						</jsp:include>
						<th><s:message code='form.question.form.info.act' text='實際完成時間'/></th>
						<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
							<jsp:param name="id1" value="act" />
							<jsp:param name="name1" value="act" />
						</jsp:include>
						<th><s:message code='form.question.form.info.observation.type' text='問題單觀察期'/></th>
						<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
							<jsp:param name="id1" value="observation" />
							<jsp:param name="name1" value="observation" />
						</jsp:include>
					</tr>
				</table>
		</fieldset>
	</form>
</div>

<script>//# sourceURL=qInfo.js
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
	},ajaxSetting);
	
	SendUtil.get("/html/getDropdownList", 3, function (option) {
		HtmlUtil.singleSelect('select#questionId', option);
	},ajaxSetting);
	
	SendUtil.get("/html/getDivisionSelectors", false, function (option) {
		HtmlUtil.singleSelect('select#division', option);
	},ajaxSetting);
	
	SendUtil.get("/html/getDropdownList", 'SPECIAL_END_CASE_TYPE', function (option) {
		HtmlUtil.singleSelect('select#specialEndCaseType', option);
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
	// 是否特殊結案切換事件
	$("input#isSpecial").click(function() {
		if($(this).is(":checked")) {
			$("select#specialEndCaseType").attr("disabled",false);
		} else {
			$("select#specialEndCaseType").val("");
			$("select#specialEndCaseType").attr("disabled",true);
		}
	});
	
	$('input#effectScope').change(function () {
		fetchDemandLevel('input#questionPriority');
	});
	
	$('input#urgentLevel').change(function () {
		fetchDemandLevel('input#questionPriority');
	});

	$('input#questionPriority').change(function () {
		fetchPriorityDesc('textarea#questionPriorityWording', $(this).val());
	});
	
	$('select#division').change(function () {
		if (!$(this).val().trim()) {
			$('input#divisionWording').val('');
		}
		
		$('input#divisionWording').val($(this).val());
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
	
	$('select#questionId').change(function() {
		let value = $(this).find("option:selected").val();
		if(value) {
			$("input#questionIdWording").val($(this).find("option:selected").text());
		} else {
			$("input#questionIdWording").val("");
		}
	})			
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

	SendUtil.get('/html/getDropdownList', 'Q_PRIORITY', function (response) {
		$.each(response, function () {
			if (this.value == priority) {
				$(priorityName).val(this.wording);
				return false;// break;
			}
		});
	}, null, true);
}

function validate(param) { 
	let isReview = headForm.verifyType.value == 'REVIEW';

	if (isReview) {
		if (!param.observation) {
			alert("<s:message code='form.question.form.info.observation' text='請輸入 「問題單觀察期」'/>");
			DialogUtil.close();
			return false; 
		}

		// 副科送結案(REVIEW)，加入檢核「實際完成時間」小於「預計完成時間」
		if (!(param.act < param.ect)) {
			alert("<s:message code='from.common.act.must.less.than.ect' text='「實際完成時間」必須小於「預計完成時間」'/>");
			DialogUtil.close();
			return false; 
		}
	}

	return true;
}

function hyperLink () {
	let solutions = $('input#solutions').val();
	if (solutions) {
		toForm(solutions);
	}
}

function alertKnowledgeDialog () {
	HtmlUtil.temporary();
	$('input#solutions').val('');
    SolutionsDialog.show(function (data) {
    	$('input#solutions').val(data);
    	HtmlUtil.tempData.infoForm.solutions = data;
    });
}
</script>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<div>
	<form id='infoForm'>
		<fieldset>
			<legend>明細</legend>
			<table class="grid_query">
				<tr>
					<td valign="top">
						<table class="grid_query">
							<tr>
								<th><span style="color: red; ">批次工作名稱</span></th>
								<td><textarea id="batchName" name="batchName" class='resizable-textarea' cols="50" rows="12" maxlength="2000" ></textarea></td>
							</tr>
							<tr>
								<th>執行時間</th>
								<td><textarea id="executeTime" name="executeTime" class='resizable-textarea' cols="50" rows="12" maxlength="2000" ></textarea></td>
							</tr>
							<tr>
								<th><span style="color: red; ">生效日期</span></th>
								<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
									<jsp:param name="id1" value="effectDate" />
									<jsp:param name="name1" value="effectDate" />
								</jsp:include>
							</tr>
						</table>
					</td>
					<td valign="top">
						<table class="grid_query">
							<tr>
								<th><span style="color: red; ">作業名稱描述</span></th>
								<td><textarea id="summary" name="summary" class='resizable-textarea' cols="50" rows="12" maxlength="2000" ></textarea></td>
							</tr>
							<tr>
								<th>使用資料庫</th>
								<td><textarea id="dbInUse" name="dbInUse" class='resizable-textarea' cols="50" rows="12" maxlength="2000" ></textarea></td>
							</tr>
							<tr>
								<th>負責科</th>
								<td>
									<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
										<jsp:param name="id" value="division" />
										<jsp:param name="name" value="division" />
									</jsp:include>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
	</form>
</div>

<script>//# sourceURL=baInfo.js
$(function () {
	DateUtil.dateTimePicker();
	initView();
	fetchInfo(form2object('headForm'));
	authViewControl();
	initFieldValue();
});

function initFieldValue () {
	var formInfo = form2object('headForm');
	if (!formInfo.formStatus) {
		var userCreatedValue = $('select#divisionCreated option:selected').val();
		$('select#division').val(userCreatedValue).change();
	}
}

function initView () {
	SendUtil.get("/html/getDivisionSelectors", true, function (option) {
		HtmlUtil.singleSelect('select#division', option);
	}, ajaxSetting);
	$('form#infoForm .resizable-textarea').resizable({handles: "se"});
}
</script>
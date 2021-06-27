﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<div>
	<form id="infoForm">
		<fieldset>
			<legend>工作要項</legend>
			<table class="grid_query">
				<tr>
					<td><label><input id="isHandleFirst" name="isHandleFirst" type="checkbox" /><s:message code='form.job.form.isHandleFirst' text='先處理後呈閱'/></label></td>
					<td><label><input id="isCorrect" name="isCorrect" type="checkbox" /><s:message code='form.job.form.isCorrect' text='上線修正'/></label></td>
					<td><label><input id="isAddFuntion" name="isAddFuntion" type="checkbox" /><s:message code='form.job.form.isAddFuntion' text='新增系統功能'/></label></td>
					<td><label><input id="isForward" name="isForward" type="checkbox" /><s:message code='form.job.form.isForward' text='送交組態人員'/></label></td>
					<td><label><input id="isWatching" name="isWatching" type="checkbox" /><s:message code='form.job.form.isWatching' text='送交監督人員'/></label></td>
				</tr>
				<tr>
					<td><label><input id="isAddReport" name="isAddReport" type="checkbox" /><s:message code='form.job.form.isAddReport' text='新增報表'/>(<font color="red">需檢附清單</font>)</label></td>
					<td><label><input id="isAddFile" name="isAddFile" type="checkbox" /><s:message code='form.job.form.isAddFile' text='新增檔案'/>(<font color="red">需檢附清單</font>)</label></td>
					<td><label><input id="isProgramOnline" name="isProgramOnline" type="checkbox" /><s:message code='form.job.form.isProgramOnline' text='程式上線'/></label></td>
					<td><label><input id="isModifyProgram" name="isModifyProgram" type="checkbox" /><s:message code='form.job.form.isModifyProgram' text='未有修改程式'/></label></td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend>工作明細</legend>
			<table class="grid_query">
				<tr>
					<td valign="top">
						<table>
							<tr>
								<th><font color="red">作業目的</font></th>
								<td><textarea id="purpose" name="purpose" class='resizable-textarea' cols="50" rows="8" maxlength="500" ></textarea></td>
							</tr>
							<tr>
								<th><font color="red">執行內容</font></th>
								<td><textarea id="content" name="content" class='resizable-textarea' cols="50" rows="8" maxlength="1000" ></textarea></td>
							</tr>
                            <tr>
                                <th>
                                    <span id="countersignedsSpan">
                                        <s:message code="form.job.form.countersigneds" text="工作會辦處理情形" />
                                    </span>
                                </th>
                                <td><textarea id="countersigneds" name="countersigneds" class='resizable-textarea' cols="50" rows="8" maxlength="2000" ></textarea></td>
                            </tr>
                            <jsp:include page="/WEB-INF/jsp/common/models/internalProcessModel.jsp">
								<jsp:param name="id" value="internalProcess" />
								<jsp:param name="legend" value="會辦項目" />
								<jsp:param name="isDisabled" value="true" />
							</jsp:include>
						</table>
					</td>
					<td valign="top">
						<table class="grid_query">
							<tr>
								<th><font color="red">系統名稱</font></th>
								<jsp:include page="/WEB-INF/jsp/common/models/systemModel.jsp" />
							</tr>
							<tr>
								<th><font color="red">服務類別</font></th>
								<td>
									<jsp:include page="/WEB-INF/jsp/common/models/selectorModel.jsp">
										<jsp:param name="id" value="sClass" />
										<jsp:param name="name" value="sClass" />
									</jsp:include>
								</td>
							</tr>
							<tr>
								<th>子類別</th>
								<td>
									<jsp:include page="/WEB-INF/jsp/common/models/selectorModel.jsp">
										<jsp:param name="id" value="sSubClass" />
										<jsp:param name="name" value="sSubClass" />
									</jsp:include>
								</td>
							</tr>
							<tr>
								<th>變更類型</th>
								<td><input id="changeType" name="changeType" type="text" readonly /></td>
							</tr>
							<tr>
								<th>變更等級</th>
								<td><input id="changeRank" name="changeRank" type="text" readonly /></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend>日期</legend>
			<label><input id="isPlaning" name="isPlaning" type="checkbox" />會造成服務中斷或需要停機，屬於計劃性系統維護&nbsp;&nbsp;&nbsp;&nbsp;</label>
			<label><input id="isUnPlaning" name="isUnPlaning" type="checkbox" />會造成服務中斷或需要停機，屬於非計劃性系統維護&nbsp;&nbsp;&nbsp;&nbsp;</label>
			<table class="grid_query">
				<tr>
					<th>測試系統完成時間</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="tct" />
						<jsp:param name="name1" value="tct" />
					</jsp:include>
					<th>連線系統完成日期</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="sct" />
						<jsp:param name="name1" value="sct" />
					</jsp:include>
					<th>連線系統實施日期</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="sit" />
						<jsp:param name="name1" value="sit" />
					</jsp:include>
				</tr>
				<tr>
					<th>公告停機時間</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="offLineTime" />
						<jsp:param name="name1" value="offLineTime" />
						<jsp:param name="warning" value="(至少於五天前公告)" />
					</jsp:include>
				</tr>
				<tr>
					<th>中斷起始時間</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="ist" />
						<jsp:param name="name1" value="ist" />
					</jsp:include>
					<th>中斷結束時間</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="ict" />
						<jsp:param name="name1" value="ict" />
					</jsp:include>
				</tr>
				<tr>
					<th>變更申請時間</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="cat" />
						<jsp:param name="name1" value="cat" />
					</jsp:include>
					<th>變更結束時間</th>
					<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
						<jsp:param name="id1" value="cct" />
						<jsp:param name="name1" value="cct" />
					</jsp:include>
				</tr>
			</table>
		</fieldset>
	</form>
</div>

<script>//# sourceURL=apcInfo.js
$(function () {
    DateUtil.dateTimePicker();
    initView();
    initEvent();
    fetchInfo(form2object('headForm'));
    authViewControl();
});

function initView() {
    SendUtil.get("/html/getDropdownList", 2, function (options) {
        HtmlUtil.singleSelect('select#sClass', options);
    }, ajaxSetting);
	$('form#infoForm .resizable-textarea').resizable({handles: "se"});
}

function initEvent() {
    $('select#sClass').change(function () {
        if ($(this).val()) {
            SendUtil.get("/html/getSubDropdownList", $(this).val(), function (options) {
                HtmlUtil.singleSelect('select#sSubClass', options);
            }, ajaxSetting);
        } else {
            HtmlUtil.emptySelect('#sSubClass');
        }
    });
}
</script>

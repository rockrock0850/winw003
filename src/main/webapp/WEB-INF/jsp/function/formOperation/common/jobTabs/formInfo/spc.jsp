﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<script>//# sourceURL=spcInfo.js
$(function () {
    DateUtil.dateTimePicker();
    initView();
    initEvent();
    fetchInfo(form2object('headForm'));
    authViewControl();
});

function initView () {
	$('form#infoForm .resizable-textarea').resizable({handles: "se"});
}

function initEvent () {
	$('input[type=radio]').change(function () {
		$(this).prop('chekced', true);
		$('input[type=radio]').not(this).prop('checked', false);
	});
}
</script>

<div>
    <form id="infoForm">
        <fieldset>
            <legend><s:message code="form.job.legend.detail" text="工作明細" /></legend>
            <table class="grid_query">
                <tr>
                    <td valign="top">
                        <table>
                            <tr>
                                <th><font color="red"><s:message code="form.job.form.summary" text="工作摘要" /></font></th>
                                <td><textarea id="summary" name="summary" class='resizable-textarea' cols="50" rows="1" maxlength="500" ></textarea></td>
                            </tr>
                            <tr>
                                <th>
                                    <span id="purposeSpan">
                                        <font color="red"><s:message code="form.job.form.purpose" text="作業目的" /></font>
                                    </span>
                                </th>
                                <td><textarea id="purpose" name="purpose" class='resizable-textarea' cols="50" rows="4" maxlength="500" ></textarea></td>
                            </tr>
                            <tr>
                                <th>
                                    <span id="contentSpan">
                                        <font color="red"><s:message code="form.job.form.content.2" text="執行內容" /></font>
                                    </span>
                                </th>
                                <td><textarea id="content" name="content" class='resizable-textarea' cols="50" rows="4" maxlength="1000" ></textarea></td>
                            </tr>
                            <tr>
                                <th>
                                    <span id="remarkSpan">
                                        <s:message code="form.job.form.remark.2" text="修改詳細說明" />
                                    </span>
                                </th>
                                <td><textarea id="remark" name="remark" class='resizable-textarea' cols="50" rows="4" maxlength="2000" ></textarea></td>
                            </tr>
                            <tr>
                                <th>
                                    <span id="countersignedsSpan">
                                        <s:message code="form.job.form.countersigneds" text="工作會辦處理情形" />
                                    </span>
                                </th>
                                <td><textarea id="countersigneds" name="countersigneds" class='resizable-textarea' cols="50" rows="4" maxlength="2000" ></textarea></td>
                            </tr>
                            <tr>
								<td colspan="2">
									<fieldset>
										<legend>會辦系統科群組</legend>
										<jsp:include page='/WEB-INF/jsp/common/models/spcModel.jsp' />
									</fieldset>
								</td>
                            </tr>
                        </table>
                    </td>
                    <td valign="top">
                        <table class="grid_query">
                            <tr>
                                <td align="right"><label for='isTest'><s:message code="form.job.form.isTest" text='TEST' /></label></td>
                                <td><input id="isTest" type="radio" name="isTest" disabled="disabled"></td>
                            </tr>
                            <tr>
                                <td align="right"><label for='isProduction'><s:message code="form.job.form.isProduction" text='PRODUCTION' /></label></td>
                                <td><input id="isProduction" type="radio" name="isProduction" disabled="disabled"></td>
                            </tr>
                            <tr>
                                <td align="right"><label for='isHandleFirst'><s:message code="form.job.form.isHandleFirst" text="先處理後呈閱" /></label></td>
                                <td><input id="isHandleFirst" name="isHandleFirst" type="checkbox" /></td>
                            </tr>
                            <tr>
                                <td align="right"><s:message code="form.job.form.system" text="系統名稱" /></td>
                                <td><input id="system" name="system" type="text" /></td>
                            </tr>
                            <tr>
                                <td align="right"><s:message code="form.job.form.userId" text="系統負責人員" /></td>
                                <td>
                                    <input id="userName" type="text" /><!-- 姓名 -->
                                    <input id="userId" name="userId" type="hidden" /><!-- ID -->
                                </td>
                            </tr>
                            <tr>
                                <td align="right"><s:message code="form.job.form.cuserId" text="會辦處理人員" /></td>
                                <td><input id="cuserId" name="cuserId" type="text" /></td>
                            </tr>
                            <tr>
                                <td align="right"><s:message code="form.job.form.astatus" text="核准狀態" /></td>
                                <td><input id="astatus" name="astatus" type="text" /></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </fieldset>
        <fieldset>
            <legend><s:message code="form.job.legend.date" text="日期" /></legend>
            <table class="grid_query" id="dateTable">
                <tr>
                    <th><s:message code="form.job.form.mect" text="主單預計完成時間" /></th>
                    <jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
                        <jsp:param name="id1" value="mect" />
                        <jsp:param name="name1" value="mect" />
                    </jsp:include>
                </tr>
                <tr>
                    <th>
                        <span id="eotSpan">
                            <s:message code="form.job.form.eot" text="預計開始時間" />
                        </span>
                    </th>
                    <jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
                        <jsp:param name="id1" value="eot" />
                        <jsp:param name="name1" value="eot" />
                    </jsp:include>
                    <th>
                        <span id="ectSpan">
                            <s:message code="form.job.form.ect" text="預計完成時間"/>
                        </span>
                    </th>
                    <jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
                        <jsp:param name="id1" value="ect" />
                        <jsp:param name="name1" value="ect" />
                    </jsp:include>
                </tr>
                <tr>
                    <th>
                        <span id="astSpan">
                            <s:message code="form.job.form.ast" text="實際開始時間"/>
                        </span>
                    </th>
                    <jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
                        <jsp:param name="id1" value="ast" />
                        <jsp:param name="name1" value="ast" />
                    </jsp:include>
                    <th>
                        <span id="actSpan">
                            <s:message code="form.job.form.act" text="實際完成時間"/>
                        </span>
                    </th>
                    <jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
                        <jsp:param name="id1" value="act" />
                        <jsp:param name="name1" value="act" />
                    </jsp:include>
                </tr>
            </table>
        </fieldset>
    </form>
</div>
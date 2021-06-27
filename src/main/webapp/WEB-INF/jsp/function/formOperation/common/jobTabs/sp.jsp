<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<script>
var getUrl = "/commonJobForm/getSpDetail";
var saveUrl = "/commonJobForm/saveSpDetail";
var tabType = '<c:out value="${tabType}"/>';

$(function () {
    $('input#division').val(tabType);
    DateUtil.dateTimePicker();
    initEvent();
	fillCForm('tabSpForm', 'SP');
    authViewControl();
});

function initEvent () {
	$('input[type=radio]').change(function () {
		$(this).prop('chekced', true);
		$('input[type=radio]').not(this).prop('checked', false);
	});
}
</script>

<fieldset>
	<legend>系統科工作單(二)</legend>
    <form id="tabSpForm">
        <input id="id" class="hidden" name="id" />
        <input id="division" class="hidden" name="division" />
		<table class="grid_query">
			<tr>
                <th><s:message code="form.countersigned.form.sct" text="連線系統完成日期" /></th>
                <jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
                    <jsp:param name="id1" value="sct" />
                    <jsp:param name="name1" value="sct" />
                </jsp:include>
			</tr>
			<tr>
				<th>修正系統</th>
				<td colspan="3">
					<label>
						<input id='isTest' type="radio" name='isTest' checked>
						<span>TEST</span>
					</label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label>
						<input id='isProduction' type="radio" name='isProduction'>
						<span>PRODUCTION</span>
					</label>
				</td>
			</tr>
            <tr>
                <th>修改詳細說明</th>
                <td colspan="4"><textarea id="description" name="description" cols="50" rows="6" maxlength="2000" style="width: 100%;" ></textarea></td>
            </tr>
		</table>
    </form>
</fieldset>
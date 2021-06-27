<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<script>
var getUrl = "/commonJobForm/getPtDetail";
var saveUrl = "/commonJobForm/savePtDetail";
var tabType = '<c:out value="${tabType}"/>';

$(function () {
    $('input#division').val(tabType);
    DateUtil.dateTimePicker();
	fillCForm('tabPtForm', 'PT');
    authViewControl();
});
</script>

<fieldset>
    <legend><s:message code="form.countersigned.form.PT" text="連管科工作單(二)" /></legend>
    <form id="tabPtForm">
        <input id="id" class="hidden" name="id" />
        <input id="division" class="hidden" name="division" />
        <table class="grid_query">
            <tr>
                <th><s:message code="form.countersigned.form.userId" text="系統負責人" /></th>
                <td><input id="userId" name="userId" maxlength="10" type="text" style="width: 8rem;" /></td>
                <td colspan="4"></td>
            </tr>
            <tr>
                <th><s:message code="form.countersigned.form.tct" text="測試系統完成日期" /></th>
                <jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
                    <jsp:param name="id1" value="tct" />
                    <jsp:param name="name1" value="tct" />
                </jsp:include>
                <th><s:message code="form.countersigned.form.sct" text="連線系統完成日期" /></th>
                <jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
                    <jsp:param name="id1" value="sct" />
                    <jsp:param name="name1" value="sct" />
                </jsp:include>
                <th><s:message code="form.countersigned.form.sit" text="連線系統實施日期" /></th>
                <jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
                    <jsp:param name="id1" value="sit" />
                    <jsp:param name="name1" value="sit" />
                </jsp:include>
            </tr>
            <tr>
                <th><s:message code="form.countersigned.form.description" text="工作說明" /></th>
                <td colspan="4"><textarea id="description" name="description" cols="50" rows="6" style="width: 100%;"></textarea></td>
            </tr>
        </table>
    </form>
</fieldset>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<script>
var getUrl = "/commonJobForm/getApDetail";
var saveUrl = "/commonJobForm/saveApDetail";
var tabType = '<c:out value="${tabType}"/>';

$(function () {
	let tabTitle, tabForm;
	
	switch(tabType) {
	    case "AP1":
	    	tabForm = 'tabAp1Form';
	        tabTitle = '<s:message code="form.countersigned.form.AP1" text="設計一科工作單(二)" />';
	        break;
	    case "AP2":
	    	tabForm = 'tabAp2Form';
	        tabTitle = '<s:message code="form.countersigned.form.AP2" text="設計二科工作單(二)" />';
	        break;
	    case "AP3":
	    	tabForm = 'tabAp3Form';
	        tabTitle = '<s:message code="form.countersigned.form.AP3" text="設計三科工作單(二)" />';
	        break;
	    case "AP4":
	    	tabForm = 'tabAp4Form';
	        tabTitle = '<s:message code="form.countersigned.form.AP4" text="設計四科工作單(二)" />';
	        break;
	}

	$('input#division').val(tabType);
	$('legend#tabTitle').html(tabTitle);
	$('legend#tabTitle').siblings('form').attr('id', tabForm);

	DateUtil.dateTimePicker();
    fillCForm(tabForm, tabType);
    authViewControl();
});
</script>

<fieldset>
	<legend id="tabTitle"></legend>
    <form id=''>
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
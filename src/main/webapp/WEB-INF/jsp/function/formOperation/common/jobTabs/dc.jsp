<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<script>
var getUrl = "/commonJobForm/getDcDetail";
var saveUrl = "/commonJobForm/saveDcDetail";
var tabType = '<c:out value="${tabType}" />';

$(function () {
	let dataType, tabForm;
	
    switch (tabType) {
        case "DC1":
            dataType = 'ONLINE';
	    	tabForm = 'tabDc1Form';
            break;
        case "DC2":
            dataType = 'BATCH';
	    	tabForm = 'tabDc2Form';
            break;
        case "DC3":
            dataType = 'OPEN';
	    	tabForm = 'tabDc3Form';
            break;
    }
    
    $('input#division').val(tabType);
    $('input#dataType').val(dataType);
    $('span#dataTypeSpan').html(dataType);
	$('legend#tabTitle').siblings('form').attr('id', tabForm);
    $('legend#tabTitle').html('<s:message code="form.countersigned.form.DC" text="資管科工作單(二)" />');
    
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
        <input id="dataType" class="hidden" name="dataType" />
    	<table class="grid_query">
    		<tr>
    			<th><s:message code="form.countersigned.form.dataType" text="資料型態" /></th>
    			<td><span id="dataTypeSpan"></span></td>
    			<th><s:message code="form.countersigned.form.userId" text="系統負責人" /></th>
    			<td><input id="userId" name="userId" maxlength="10" type="text" style="width: 8rem;" /></td>
    			<td colspan="2"></td>
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
    		
    		<!-- 如果點擊BOOK或ONLINE要顯示 -->
    		<c:if test="${tabType == 'DC1' || tabType == 'DC2'}">
        		<tr>
        			<th colspan="6"><s:message code="form.countersigned.form.book" text="1.Book" /></th>
        		</tr>
        		<tr>
        			<td colspan="4"><textarea id="book" name="book" cols="50" rows="4" style="width: 100%;"></textarea></td>
        			<th><s:message code="form.countersigned.form.bookNumber" text="Book支數" /></th>
        			<td><input id="bookNumber" name="bookNumber" maxlength="6" type="text" /></td>
        		</tr>
        		<tr>
        			<th><s:message code="form.countersigned.form.only" text="2.Compile Only" /></th>
        			<td colspan="5">
                        <s:message code="form.countersigned.form.returnCode" text="Return_Code=" />
                        <input id="onlyCode" name="onlyCode" type="text" maxlength="20" style="width: 10rem;" />&nbsp;&nbsp;
                        <s:message code="form.countersigned.form.returnCode.desc" text="視為正常" />
        			</td>
        		</tr>
        		<tr>
        			<td colspan="4"><textarea id="only" name="only" cols="50" rows="4" style="width: 100%;"></textarea></td>
        			<th><s:message code="form.countersigned.form.programNumber" text="程式支數" /></th>
        			<td><input id="onlyNumber" name="onlyNumber" maxlength="6" type="text" /></td>
        		</tr>
        
        
        		<tr>
        			<th><s:message code="form.countersigned.form.link" text="3.Compile Link" /></th>
        			<td colspan="5">
                        <s:message code="form.countersigned.form.returnCode" text="Return_Code=" />
                        <input id="linkCode" name="linkCode" type="text" maxlength="20" style="width: 10rem;" />&nbsp;&nbsp;
                        <s:message code="form.countersigned.form.returnCode.desc" text="視為正常" />
        			</td>
        		</tr>
        		<tr>
        			<td colspan="4"><textarea id="link" name="link" cols="50" rows="4" style="width: 100%;"></textarea></td>
        			<th><s:message code="form.countersigned.form.programNumber" text="程式支數" /></th>
        			<td><input id="linkNumber" name="linkNumber" maxlength="6" type="text" /></td>
        		</tr>
        
        		<tr>
        			<th colspan="6"><s:message code="form.countersigned.form.linkOnly" text="4.Link Only" /></th>
        		</tr>
        		<tr>
        			<td colspan="4"><textarea id="linkOnly" name="linkOnly" cols="50" rows="4" style="width: 100%;"></textarea></td>
        			<th><s:message code="form.countersigned.form.programNumber" text="程式支數" /></th>
        			<td><input id="linkOnlyNumber" name="linkOnlyNumber" maxlength="6" type="text" /></td>
        		</tr>
        		<tr>
        			<th colspan="6"><s:message code="form.countersigned.form.other" text="5.其他事項" /></th>
        		</tr>
        		<tr>
        			<td colspan="6"><textarea id="other" name="other" cols="50" rows="4" style="width: 100%;"></textarea></td>
        		</tr>
    		</c:if>
    		<!-- 如果點擊BOOK或ONLINE要顯示 END -->
    		
    		<!-- 如果點擊OPEN要顯示 -->
    		<c:if test="${tabType == 'DC3'}">
        		<tr>
        			<th colspan="6"><s:message code="form.countersigned.form.description" text="說明" /></th>
        		</tr>
        		<tr>
        			<td colspan="6"><textarea id="description" name="description" cols="50" rows="4" style="width: 100%;"></textarea></td>
        		</tr>
            </c:if>
    		<!-- 如果點擊OPEN要顯示 END -->
    		
    		<tr>
    			<td colspan="6">
    				<fieldset>
    					<legend><s:message code="form.countersigned.form.rollback.title" text="回復作業" /></legend>
    					<table class="grid_query">
    						<tr>
    							<th><label for='isRollback'><s:message code="form.countersigned.form.isRollback" text="回復到前一版" /></label></th>
    							<td><input id="isRollback" name="isRollback" type="checkbox" checked /></td>
    							<th><s:message code="form.countersigned.form.rollbackDesc" text="其他" /></th>
    							<td><textarea id="rollbackDesc" name="rollbackDesc" maxlength="50" cols="50" rows="2" style="width: 100%;"></textarea></td>
    						</tr>
    					</table>
    				</fieldset>
    			</td>
    		</tr>
	   </table>
   </form>
</fieldset>
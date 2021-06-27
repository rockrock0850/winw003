﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!--  
輸入參數說明 : 
	- id : 識別碼
	- legend : fieldset標題
-->
<tr id="<c:out value='${param.id}'/>">
	<td colspan="2">
		<fieldset>
			<legend><c:out value='${param.legend}'/></legend>
			<table id="internalProcessList" class="grid_query"></table>
		</fieldset>
	</td>
</tr>

<script>// #sourceURL=InternalProcessModel.js
var InternalProcessModel = function() {
	
	var init = function() {
		let formId = formInfo.formId || formInfo.sourceId;
		
	    SendUtil.get("/html/getInternalProcessList", formId, function (response) {
	    	if (response && response.length > 0) {
	    		$("tr#<c:out value='${param.id}'/> table#internalProcessList").empty();
	    		
				let htmlBuilder = new Array();
				htmlBuilder.push("<tr>");
				let count = 0;
				let disabled = ${param.isDisabled} ? ' disabled="disabled"' : '';
				$.each(response, function () {
					let checked = this.display === "Y" ? " checked" : "";
					let checkbox = '<td><label><input type="checkbox" name="internalProcessItem" value="'+this.value+'"'+checked+disabled+'/>'+this.wording+'</label></td>';
					htmlBuilder.push(checkbox);
					if (++count % 3 === 0) {
						htmlBuilder.push("</tr><tr>");
					}
				});
				htmlBuilder.push("</tr>");
				$("tr#<c:out value='${param.id}'/> table#internalProcessList").append(htmlBuilder.join(""));
	    	} else {
	    		$("tr#<c:out value='${param.id}'/>").hide();
	    	}
	    }, ajaxSetting);
	}
	
	return {
		init : init
	}
}();

$(function () {
	InternalProcessModel.init();
});
</script>

<%--
<fieldset>
	<legend>會辦項目</legend>
	<table>
		<tr>
			<td><input type="checkbox"/>DC-ONLINE</td>
			<td><input type="checkbox"/>DC-OPEN</td>
			<td><input type="checkbox"/>DC-BATCH</td>
		</tr>
		<tr>
			<td><input type="checkbox"/>DC-批次</td>
			<td><input type="checkbox"/>DC-DB變更</td>
		</tr>
	</table>
</fieldset>
--%>
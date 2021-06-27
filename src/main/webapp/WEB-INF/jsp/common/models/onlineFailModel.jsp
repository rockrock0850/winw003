<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>
var OnlineFailModel = function () {
	
	var onlineFailEvent = function (cb) {
		$('#onlineFailTbale p').toggle((cb && cb.checked));
		
		if (cb && cb.checked) {
			$('#onlineFailTbale input').prop('disabled', false);
			$('#onlineFailTbale button').prop('disabled', false);
	    } else {
	    	$('#onlineFailTbale input').prop('disabled', true);
			$('#onlineFailTbale button').prop('disabled', true);
			$('#onlineTime').val("");
			$('#onlineJobFormId').val("");
			$('#isOnlineFail').prop('disabled', false);
	    }
		
		if (typeof onlineFailChangeEvent == 'function'){
			onlineFailChangeEvent(cb);
		}
	}
	
	return {
		onlineFailEvent : onlineFailEvent
	}
}();

$(function () {
	OnlineFailModel.onlineFailEvent($('#isOnlineFail'));
});
</script>
<table class="grid_query" id="onlineFailTbale">
	<tr>
		<th><label for="isOnlineFail">&nbsp;上線失敗</label></th>
		<td><input id='isOnlineFail' onchange="OnlineFailModel.onlineFailEvent(this)" type="checkbox" name='isOnlineFail' value=""/></td>
	</tr>
	<tr>
		<th>上線時間</th>
		<jsp:include page="/WEB-INF/jsp/common/models/dateTimeModel.jsp">
			<jsp:param name="id1" value="onlineTime" />
			<jsp:param name="name1" value="onlineTime" />
		</jsp:include>
	</tr>
	<tr>
		<th>工作單單號</th>
		<td>
			<input id='onlineJobFormId' type="input" name='onlineJobFormId' value="" readonly="readonly"/>
			<button id='onlineJobFormIdBtn' type='button' onclick='OnlineJobFormIdDialog.show()'>選擇表單</button>
		</td>							
	</tr>
	<tr>
		<th></th>
		<td><p id='onlineFailWording' style="color:red">事件單的上線失敗相關欄位，如未填寫完整，則事件單無法成立</p></td>
	</tr>
</table>
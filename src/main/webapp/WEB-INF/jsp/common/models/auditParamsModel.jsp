<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!--  
輸入參數說明 : 
	- time : 即將逾期臨界值
	- timeUnit : 即將逾期臨界值單位
	- notifyMails : 管理人員郵件地址
	- pic : 哪一種單的經辦
	- vsc : 哪一種單的副科
	- sc : 哪一種單的科長
	- d1 : 哪一種單的副理
	- d2 : 哪一種單的協理
	- isNeedExpireTime : 是否要通知時間
-->

<input class='hidden' name='createdBy' />
<input class='hidden' name='updatedBy' />
<input class='hidden' name='createdAt' />
<input class='hidden' name='updatedAt' />
<table class="grid_query">
	<c:choose> 
		<c:when test='${param.isNeedExpireTime}'>
			<tr>
				<th>臨界值</th>
				<td><input id="${param.time}" type="number" name="time">&nbsp;${param.timeUnit}</td>
			</tr>
		</c:when> 
		<c:otherwise> </c:otherwise>
	</c:choose>
	<tr>
		<th>負責人</th>
		<td><textarea id='${param.notifyMails}' name='notifyMails' class='resizable-textarea'></textarea></td>
	</tr>
	<tr>
		<td colspan="2">
			<fieldset>
				<legend>通知群組</legend>
				<table class="grid_query" style="width: 105%;">
					<tr>
						<td><label for='${param.pic}'><input id='${param.pic}' type="checkbox" name='isPic' />經辦</label></td>
						<td><label for='${param.vsc}'><input id='${param.vsc}' type="checkbox" name='isVsc' />副科</label></td>
						<td><label for='${param.sc}'><input id='${param.sc}' type="checkbox" name='isSc' />科長</label></td>
					</tr>
					<tr>
						<td><label for='${param.d1}'><input id='${param.d1}' type="checkbox" name='isD1' />副理</label></td>
						<td><label for='${param.d2}'><input id='${param.d2}' type="checkbox" name='isD2' />協理</label></td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
</table>
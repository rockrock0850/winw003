<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<%--  
輸入參數說明 : 
	- id : 下拉選單識別碼
	- name : 下拉選單名稱識別碼
	- isDisabled : 可否編輯
	- defaultValue : 設定預設參數值 (選擇)
	- defaultName : 設定預設參數外顯值 (選擇)
P.S. 
	若將EL表達式排版, 元件內容值會出現換行符號且產生大量空白文字, 導致資料傳輸到後端之後發生錯誤, 
	所以將排版調整為一行不進行排版, 就能將多餘的換行以及空白剃除。
--%>

<select 
	id="<c:out value='${param.id}'/>"
	<c:if test='${param.isDisabled}'>
		disabled="disabled"
	</c:if>
	
	name="<c:out value='${param.name}'/>">
	
	<option value='<c:choose><c:when test='${not empty param.defaultValue}'><c:out value="${param.defaultValue}"/></c:when><c:otherwise></c:otherwise></c:choose>'><c:choose><c:when test='${not empty param.defaultName}'><s:message code='${param.defaultName}' text='請選擇' /></c:when><c:otherwise><s:message code="global.select.please.all" text='全部' /></c:otherwise></c:choose></option>
</select>

<%-- 
	<option 
		value='
 			<c:choose> 
				<c:when test='${not empty param.defaultValue}'>
					${param.defaultValue}
			</c:when> 
				<c:otherwise> </c:otherwise>
			</c:choose>'>
			
			<c:choose>
				<c:when test='${not empty param.defaultName}'>
					${param.defaultName}
				</c:when>
				<c:otherwise>
					全部
				</c:otherwise>
			</c:choose>
	</option>
--%>
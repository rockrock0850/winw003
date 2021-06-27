<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>
<h1><s:message code="service.type.management.title" text='服務類別管理' /></h1>
<fieldset class="search">
	<legend><s:message code="global.query.condition" text='查詢條件' /></legend>

	<button class="small fieldControl searchPanel">
		<i class="iconx-collapse"></i>|||
	</button>
	
	<form id="queryForm">
		<table class="grid_query">
			<tr>
				<th>&nbsp;<s:message code="service.type.manage.name.search" text='服務類別' /></th>
				<td><input type="text" name="searchCol1" maxlength="500" value='${groupData.searchCol1}' ></td>
				<th>&nbsp;<s:message code="service.type.subcategory.name.search" text='服務子類別' /></th>
				<td><input type="text" name="searchCol2" maxlength="500"  value='${groupData.searchCol2}' ></td>
				<th>&nbsp;<s:message code="service.type.active.search" text='服務類別狀態' /></th>
				<td>
					<select id="active" name="active">
						<c:choose>
							<c:when test="${groupData.active == 'Y'}">
								<option value=""><s:message code="global.select.please.choose" text='請選擇' /></option>
								<option value="Y" selected>啟用</option>
								<option value="N">停用</option>
							</c:when>
							<c:when test="${groupData.active == 'N'}">
								<option value=""><s:message code="global.select.please.choose" text='請選擇' /></option>
								<option value="Y">啟用</option>
								<option value="N" selected>停用</option>
							</c:when>
							<c:otherwise>
								<option value=""><s:message code="global.select.please.choose" text='請選擇' /></option>
								<option value="Y">啟用</option>
								<option value="N">停用</option>
							</c:otherwise>
						</c:choose>
					</select>
				</td>
				<td></td>
				<td></td>
				<td>
					<button id="searchBtn" type="button" style="width:43px;">
						<i class="iconx-search"></i> <s:message code="button.search" text='查詢' />
					</button>
				</td>
				<td>
					<button id="addBtn" type="button" onclick="SysOptionDialog.show('', '新增服務類別', false, false);" style="width:96px;">
					    <i class="iconx-add"></i> <s:message code="button.add.level.one" text='新增服務類別' />
					</button>
				</td>
			</tr>
		</table>
	</form>
</fieldset>

<fieldset>
	<legend><s:message code="global.query.result" text='查詢結果' /></legend>
	<div class="grid_BtnBar" style="display:none">
		<button id="saveBtn">
			<i class="iconx-save"></i><s:message code="button.save" text='儲存' />
		</button>
	</div>
		<section id="func" class="tab-content active">
			<table class="grid_query">
			</table>
			<table class="grid_list" id="dataTable">
			<tr>
				<th width="3%"></th> <!--checkbox 欄位，暫時隱藏 -->
				<th width="16%"><s:message code="service.type.manage.name.search" text='服務類別' /></th>
				<th width="30%"><s:message code="service.type.subcategory.name.search" text='服務子類別' /></th>
				<th width="8%"><s:message code="service.type.active.search" text='服務類別狀態' /></th>
				<th width="8%"><s:message code="service.type.active.search.sub" text='服務子類別狀態' /></th>
				<th width="11%"><s:message code="service.type.updated.by.column.title" text='更新人' /></th>
				<th width="17%"><s:message code="service.type.updated.at.column.title" text='更新時間' /></th>
				<th width="7%"><s:message code="service.type.function.column.title" text='功能' /></th>
			</tr>		
			<c:forEach var="mainMenu" items="${groupData.serviceType}">
				<tbody>
					<tr>
						<%-- 服務類別選單 --%>
						<td><input type="checkbox" class="hidden"/></td>
						<td><i class="fa fa-caret-right"></i><a href="#"
							class="moduleName">${mainMenu.display}</a></td>
						<td></td>
						<td align="center">${mainMenu.active=='Y' ? '啟用' : '停用'}</td>
						<td align="center"></td>
						<td align="center">${mainMenu.updatedBy}</td>
						<td align="center">${mainMenu.updatedAt}</td>
						<td class="hidden">${mainMenu.value}</td>
						<td align="center"><button onclick='SysOptionDialog.show(${mainMenu}, "編輯服務類別", true, false)'>
							<i class="iconx-edit"></i>
								<s:message code="button.edit" text="編輯" />
							</button>
						</td>						
						<tr class="funcItem" style="display: table-row;">
							<td></td>
							<td></td>
							<td><button id="addSubBtn" type="button" onclick='SysOptionDialog.show(${mainMenu}, "新增服務子類別", false, true)' style="width:113px;">
								    <s:message code="button.add.level.two" text='新增服務子類別' /></button>
							</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						
						<%-- 服務子類別選單 --%>
						<c:forEach var="subMenu" items="${mainMenu.subServiceType}">
							<tr class="funcItem">
								<td></td>
								<td align="right"><input class="hidden" type="checkbox" id="${subMenu.optionId}" value="${subMenu.optionId}" name="submu" /></td>
								<td>${subMenu.display}</td>
								<td align="center"></td>
								<td align="center">${subMenu.active=='Y' ? '啟用' : '停用'}</td>
								<td align="center">${subMenu.updatedBy}</td>
								<td align="center">${subMenu.updatedAt}</td>
								<td align="center">
									<button onclick='SysOptionDialog.show(${subMenu}, "編輯服務子類別", true, true)'>
										<i class="iconx-edit"></i><s:message code="button.edit" text="編輯"/>
									</button>
								</td>
							</tr>
						</c:forEach>
					</tr>
				</tbody>
			</c:forEach>
		</table>
	</section>
</fieldset>

<%-- include dialog from serviceTypeDialog for add and edit --%>
<jsp:include page='/WEB-INF/jsp/common/dialogs/sysOptionDialog.jsp'>
	<jsp:param name="function" value="serviceTypeManagement" />
	<jsp:param name="isDisplayKnowledge" value="false" />
</jsp:include>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>
<script> //# sourceURL=serviceTypeManageFind.js  
var groupData = ObjectUtil.parse('${groupData}');
var dataTable;
var ajaxSetting = {async:false};

$(function() {
	hideSubMenu();
});

<%-- 查詢按鈕--%>
$('#searchBtn').click(function() {
	let params = form2object("queryForm");		
	SendUtil.formPost('/serviceTypeManagement/init/query', params, false);
});

<%-- 設定隱藏/顯示服務子類別選單  --%>
function hideSubMenu() {
	$('.funcItem').hide();
	$('.moduleName').click(function() {
		var isClosed = $(this).siblings().hasClass('fa-caret-right');
		$('.fa-caret-down').toggleClass('fa-caret-down').toggleClass('fa-caret-right');
		$('.funcItem').hide();
		if (isClosed) {
			$(this).parent().parent().parent().find('.funcItem').show();
			$(this).siblings().toggleClass('fa-caret-right').toggleClass('fa-caret-down');
		}
	});
	
	serviceFunctionCheckAll();
}

<%-- checkbox勾選功能 --%>
function serviceFunctionCheckAll() {
	$(document).ready(function() {
            $('.checkAll').click(
                function() {
                    $(this).parent().parent().siblings('.funcItem').find('input:checkbox').prop('checked', this.checked);
                });

            $('.funcItem>td').find('input:checkbox').click(function() {
            	var checkallBox = $(this).closest("tbody").find('.checkAll');
            	var itemBoxTotal = $(this).closest("tbody").find("input[name='submu']").length;
            	var itemBoxChecked = $(this).closest("tbody").find("input[name='submu']:checked").length;

                if (itemBoxTotal > itemBoxChecked) {
                    checkallBox.prop("indeterminate", true);
                }

                if (itemBoxTotal == itemBoxChecked) {
                	checkallBox.prop('indeterminate', false);
                    checkallBox.prop('checked', this.checked);
                }

                if (itemBoxChecked == 0) {
                    checkallBox.prop('checked', false);
                    checkallBox.prop('indeterminate', false);
                }
          	});
        });
}
</script>
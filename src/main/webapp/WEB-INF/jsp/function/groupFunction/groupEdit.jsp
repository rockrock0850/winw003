<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>

<script>//# sourceURL=groupEdit.js 
var groupData = ObjectUtil.parse('${groupData}');
var uriSave = '/groupFunction/save';
var dataTable;

$(function() {	
	HtmlUtil.initTabs();
	DateUtil.datePicker();
	
	initGroupInfo();
	hideSubMenu();
	initLdapUserInfo();
	
});

<%-- 讀取群組資料 --%>
function initGroupInfo () {
	$.each(groupData, function(index, value) {
		switch(index){
			case "isAllowBatchReview":
				if(value==='Y'){
					$('#'+index).prop('checked', true);
				}
				break;
			case "isDisplayKpi":
				if(value==='Y'){
					$('#'+index).prop('checked', true);
				}
				break;
			case "authType":
				$('#'+index).val(value);
				break;
			default:
				$('#'+index).html(value);
		}
	}); 
}

<%-- 隱藏設定功能子表 --%>
function hideSubMenu(){
	$('.funcItem').hide();
	$('.moduleName').click(function() {
		$(this).parent().parent().parent().find('.funcItem').toggle();
		$(this).siblings().toggleClass('fa-caret-right');
		$(this).siblings().toggleClass('fa-caret-down');
	});
	
	groupFunctionCheckAll();
	setGroupMenuPermission();
}

<%-- 顯示該群組Menu權限 --%>
function setGroupMenuPermission(){
	$.each(groupData.groupPermissions, function(index, value) {
		$("#"+value.menuId).prop("checked","true");
	}); 

	$('.checkAll').each(function() {
		var checkallBox = $(this).closest("tbody").find('.checkAll');
    	var itemBoxTotal = $(this).closest("tbody").find("input[name='submu']").length;
    	var itemBoxChecked = $(this).closest("tbody").find("input[name='submu']:checked").length;
    	
		if ((itemBoxTotal == itemBoxChecked) && itemBoxTotal != 0) {
            checkallBox.prop('checked', true);
        } else if((itemBoxTotal > itemBoxChecked) && itemBoxChecked != 0){
        	checkallBox.prop("indeterminate", true);
        }
	})
}

<%-- 設定功能-勾選功能 --%>
function groupFunctionCheckAll(){
	$(document).ready(function() {
            $('.checkAll').click(
                function() {
                    $(this).parent().parent().siblings(
                            '.funcItem').find('input:checkbox')
                        .prop('checked', this.checked);
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

<%-- 讀取人員清單資料 --%>
function initLdapUserInfo(){
	var options = {
	        data: groupData.ldapUsers,
	        <%-- 因為欄位無法對齊，先暫時用scrollbar --%>
	        //sScrollXInner: '105%',
	        columnDefs: [
	        	{orderable: false, targets: []},
	        	{targets: [0], title: '<s:message code="group.function.edit.ldapUser.enabled" text='狀態' />', data: 'isEnabled', className: 'dt-center',
	        		render: function (data) {
						if(data==='Y'){
							return '<s:message code="group.function.edit.enable" text='啟用' />';
						}else{
							return '<font color="red">'+'<s:message code="group.function.edit.disable" text='停用' />'+'</font>';
						}
				}},
	        	{targets: [1], title: '<s:message code="group.function.edit.ldapUser.name" text='姓名' />', data: 'name', className: 'dt-center'},
	        	{targets: [2], title: '<s:message code="group.function.edit.ldapUser.email" text='Email' />', data: 'email', className: 'dt-center'},
	        	{targets: [3], title: '<s:message code="global.created.at" text='建立時間' />', data: 'createdAt', className: 'dt-center',
	        		render : function (data) {
						return DateUtil.toDateTime(data);
				}},
	        	{targets: [4], title: '<s:message code="global.updated.at" text='修改時間' />', data: 'updatedAt', className: 'dt-center',
	        		render : function (data) {
						return DateUtil.toDateTime(data);
				}}
	        ]
	    };
	dataTable = TableUtil.init('#dataTable', options);
	<%-- 隱藏使用者清單 --%>
	$('#user').addClass('hide');
}

function save(){
	var data = {};
	data.sysGroupId=groupData.sysGroupId;
	data.allowBatchReview = $('input#isAllowBatchReview').is(':checked') ? "Y" : "N";
	data.displayKpi = $('input#isDisplayKpi').is(':checked') ? "Y" : "N";
	data.authType = $('#authType').val();

	var permissionsList = [];
	
	$('.funcItem>td').find('input:checkbox').each(function(){
		if($(this).is(':checked')){
			var dataArray = {};
			dataArray.sysGroupId = groupData.sysGroupId;
			dataArray.menuId = $(this).val();

			permissionsList.push(dataArray);			
		}
	})
	
	data.groupPermissions = permissionsList;
	SendUtil.post(uriSave, data, function (resData) {
		if(resData){
			alert("更新成功");
		}
	}, null, true);
}
</script>
</head>

<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>
	<h1><s:message code="group.function.edit.title" text='編輯群組權限' /></h1>
	
	<!--群組資料-->
	<fieldset>
		<legend>
			<s:message code="group.function.edit.group.info" text='群組資料' />
		</legend>
		<table id="groupInfo" class="grid_query">
			<tr>
				<th>
					<s:message code="group.function.department.id" text='部門代碼' />
				</th>
				<td id="departmentId"></td>
				<th>
					<s:message code="group.function.department.name" text='部門名稱' />
				</th>
				<td id="departmentName"></td>
				<th>
					<s:message code="group.function.division" text='科別' />
				</th>
				<td id="division"></td>
				<th>
					<s:message code="group.function.group.id" text='群組代碼' />
				</th>
				<td id="groupId"></td>
				<th>
					<s:message code="group.function.group.name" text='群組名稱' />
				</th>
				<td id="groupName"></td>
			</tr>
			<tr>
				<th>
					<font color="red">&nbsp;*&nbsp;</font>
					<s:message code="group.function.auth.type" text='身份' />
				</th>
				<td>
					<select id="authType">
						<option value="0"><s:message code="group.function.auth.type.applicant" text='經辦人員' /></option>
						<option value="1"><s:message code="group.function.auth.type.inspector" text='審核者' /></option>
					</select></td>
				<th>
					<s:message code="group.function.edit.allow.batch.review" text='允許批次審核' />
				</th>
				<td>
					<input id="isAllowBatchReview" type="checkbox" />
				</td>
				<th>
					<s:message code="group.function.edit.display.kpi" text='顯示首頁KPI' />
				</th>
				<td>
					<input id="isDisplayKpi" type="checkbox" />
				</td>
				<td colspan="6" align="right">
					<button onclick='save()'>
						<i class="iconx-save"></i> 
						<s:message code="button.save" text='儲存' />
					</button>
					&nbsp;&nbsp;
					<button onclick='back()'>
						<i class="iconx-back"></i>
						<s:message code="group.function.edit.button.back" text='回前頁' />
					</button>
				</td>
			</tr>
		</table>
	</fieldset>
	
	<!-- tabs-->
	<ul class="nav nav-tabs">
		<li class="active">
			<a href="#func">設定功能</a>
		</li>
		<li>
			<a href="#user">人員清單</a>
		</li>
	</ul>
	
	<!-- tab1 功能權限 -->
	<section id="func" class="tab-content active">
		<table class="grid_query">
		</table>
		<table class="grid_list" id="reportList">
			<tr>
				<th width="3%"></th>
				<th width="15%"><s:message code="group.function.edit.modle.desc" text='模組說明' /></th>
				<th width="15%"><s:message code="group.function.edit.function.id" text='功能代碼' /></th>
				<th width="30%"><s:message code="group.function.edit.function.name" text='功能名稱' /></th>
				<!--<th>功能權限</th>-->
			</tr>
			<c:forEach var="mainMenu" items="${groupData.menus}">
				<tbody>
					<tr>
						<%-- main menu --%>
						<td><input type="checkbox" class="checkAll" /></td>
						<td><i class="fa fa-caret-right"></i><a href="#"
							class="moduleName">${mainMenu.menuName}</a></td>
						<td></td>
						<td></td>
						
						<%-- sub menu --%>
						<c:forEach var="subMenu" items="${mainMenu.subMenus}">
							<tr class="funcItem">
								<td></td>
								<td align="right"><input type="checkbox" id="${subMenu.menuId}" value="${subMenu.menuId}" name="submu" /></td>
								<td>${subMenu.menuId}</td>
								<td>${subMenu.menuName}</td>
							</tr>
						</c:forEach>
					</tr>
				</tbody>
			</c:forEach>
		</table>
	</section>

	<!-- tab2 設定人員 -->
	<section id="user" class="tab-content">
		<table id="dataTable" class="display collapse cell-border">
		</table>
	</section>
	
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
</html>
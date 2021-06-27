<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<!--button link-->
</head>

<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	

<body>
	<h1>${title}</h1>
	<fieldset>
		<legend><s:message code="form.process.managment.add.page.review.process.definition" text="流程定義" /></legend>
		<table class="grid_query">
			<tr>
				<th><font color="red">*</font><s:message code="form.process.managment.find.page.form.type" text='表單類別' /></th>
				<td>
					<select id="formType">
						<c:forEach var="formTypeLs" items="${formTypeLs}">
							<option value="${formTypeLs.value}">${formTypeLs.wording}</option>
						</c:forEach>
					</select>
				</td>
				<th><font color="red">*</font><s:message code="form.process.managment.find.page.division.type" text='部門科別' /></th>
				<td>
					<select id="division">
						<option value=""><s:message code="form.process.managment.find.page.please.choose" text='請選擇' /></option>
						<c:forEach var="divisionLs" items="${divisionLs}">
							<option value="${divisionLs.value}">${divisionLs.wording}</option>
						</c:forEach>
					</select>
				</td>
				<th><font color="red">*</font><s:message code="form.process.managment.find.page.process.name" text='流程名稱' /></th>
				<td><input id="processName" type="text" value="" style="width: 10rem;" maxlength="30" /></td>

				<td>
					<button>
						<i class="iconx-save"></i> <s:message code="button.save" text="儲存" />
					</button>
					<button>
						<i class="iconx-back"></i> <s:message code="group.function.edit.button.back" text="回前頁" />
					</button>
					<input id='pageType' class='hidden' value='add' />
				</td>
			</tr>
		</table>
	</fieldset>

	<fieldset>
		<legend><s:message code="form.process.managment.add.page.apply.process" text="申請流程" /></legend>
		<div class="grid_BtnBar" _style="display:none">
			<button id="addApplyBtn">
				<i class="iconx-add"></i><s:message code="form.process.managment.add.page.apply.process.add.new.level" text="新增關卡" />
			</button>
			<button id="deleteApplyBtn">
				<i class="iconx-delete"></i><s:message code="form.process.managment.add.page.apply.process.remove.new.level" text="刪除關卡" />
			</button>
		</div>
		<div>
			<table class="grid_list" id="reportList">
				<tr align="center">
					<th>
						<s:message code="form.process.managment.add.page.apply.process.delete" text="刪除" />
					</th>
					<th>
						<s:message code="form.process.managment.add.page.apply.process.process.order" text="流程順序" />
					</th>
					<th>
						<s:message code="form.process.managment.add.page.apply.process.process.group" text="流程群組" />
					</th>
					<th>
                        <s:message code="form.process.managment.levelwordings" text="關卡文字" />
                    </th>
					<c:if test="${isCountersign == 'Y'}">
						<th custom='parallelCols' style='display:none'>平行會辦</th>
						<th custom='parallelCols' style='display:none'>會辦群組</th>
						<th style='display:none'><input id='parallels' name='parallels' /></th>
					</c:if>
					<th>
						<s:message code="form.process.managment.add.page.apply.process.most" text="最多" /><br/><s:message code="form.process.managment.add.page.apply.process.next.level.counts" text="跳件關數" />
					</th>
					<th>
						<s:message code="form.process.managment.add.page.apply.process.most" text="最多" /><br/><s:message code="form.process.managment.add.page.apply.process.back.level.counts" text="退件關數" />
					</th>
					<th>
						<s:message code="form.process.managment.add.page.apply.process.is.create" text="可否產生" /><br/><s:message code="form.process.managment.add.page.apply.process.change.issue" text="變更單" />
					</th>
					<c:if test="${isCountersign != 'Y'}">
						<th>
							<s:message code="form.process.managment.add.page.apply.process.is.create" text="可否產生" /><br/><s:message code="form.process.managment.add.page.apply.process.change.require.issue" text="需求會辦單" />
						</th>
					</c:if>
					<th>
						<s:message code="form.process.managment.add.page.apply.process.wait" text="等待" /><br/><s:message code="form.process.managment.add.page.apply.process.sub.issue.finish" text="衍生單完成" />
					</th>
					<c:if test="${isCountersign != 'Y'}">
						<th>
							<s:message code="form.process.managment.add.page.apply.process.change.issue" text="變更單" /><br/><s:message code="form.process.managment.add.page.apply.process.check.level" text="檢核關卡" />
						</th>
					</c:if>
					<th>
						<s:message code="form.process.managment.add.page.review.process.is.modify.column.data.1" text="可修改欄位資料" />
					</th>
					<th>
						<s:message code="form.process.managment.column.is.approver" text="是否為審核者" />
					</th>
				</tr>
				<tbody id="applyProcessTable">
					<tr align="center">
						<td><input name="delCheck" type="checkbox" /></td>
						<td><font class="applyProcessOrderText currentLevel">1</font></td>
						<td>
							<select class="applySysGroup" id="sysGroup">
								<option value=''><s:message code="form.process.managment.add.page.apply.process.choose.process.group" text="請選擇流程群組" /></option>
							</select>
						</td>
						<td>
                            <button type='button' onclick='levelWordingsButton(this);'>
                                <i class="iconx-add"></i>設定
                            </button>
                            <input class='levelWordings hidden' name='levelWordings' />
                        </td>
						<c:if test="${isCountersign == 'Y'}">
							<td custom='parallelCols' style='display:none'><input id='isParallel' type='radio'/></td>
							<td custom='parallelCols' style='display:none'>
								<button id="parallelButton" type='button' onclick='parallelButton();' style="display:none">
									<i class="iconx-add"></i>編輯
								</button>
							</td>
						</c:if>
						<td>
							<select class="nextLevel"></select>
						</td>
						<td>
							<select class="backLevel"></select>
						</td>
						<td><input id="isCreateChangeIssue" type="checkbox" /></td>
						<c:if test="${isCountersign != 'Y'}">
							<td><input id="isCreateCIssue" type="checkbox" /></td>
						</c:if>
						<td><input id="isWaitForSubIssueFinish" type="checkbox" /></td>
						<c:if test="${isCountersign != 'Y'}">
							<td><input id="isCheckLevel" type="radio" name="isCheckLevel"></td>
						</c:if>
						<td><input id="isModifyColumnData" type="checkbox" /></td>
						<td><input id="isApprover" type="checkbox" /></td><!-- 是否為審核者 -->
					</tr>
				</tbody>
			</table>
		</div>
	</fieldset>

	<fieldset>
		<legend><s:message code="form.process.managment.add.page.review.process" text="審核流程" /></legend>
		<div class="grid_BtnBar" _style="display:none">
			<button id="addReviewBtn">
				<i class="iconx-add"></i><s:message code="form.process.managment.add.page.review.process.add.new.level" text="新增關卡" />
			</button>
			<button id="deleteReviewBtn">
				<i class="iconx-delete"></i><s:message code="form.process.managment.add.page.review.process.remove.new.level" text="刪除關卡" />
			</button>
		</div>
		<div>
			<table class="grid_list" id="reportList1">
				<thead>
					<tr align="center">
						<th>
							<s:message code="form.process.managment.add.page.review.process.delete" text="刪除" />
						</th>
						<th>
							<s:message code="form.process.managment.add.page.review.process.process.order" text="流程順序" />
						</th>
						<th>
							<s:message code="form.process.managment.add.page.review.process.process.group" text="流程群組" />
						</th>
						<th>
                            <s:message code="form.process.managment.levelwordings" text="關卡文字" />
                        </th>
						<th>
							<s:message code="form.process.managment.add.page.review.process.process.most" text="最多" /><br/><s:message code="form.process.managment.add.page.review.process.next.level.counts" text="跳件關數" />
						</th>
						<th>
							<s:message code="form.process.managment.add.page.review.process.process.most" text="最多" /><br/><s:message code="form.process.managment.add.page.review.process.back.level.counts" text="退件關數" />
						</th>
						<th>
							<s:message code="form.process.managment.add.page.review.process.is.modify.column.data.1" text="可修改欄位資料" />
						</th>
						<th>
							<s:message code="form.process.managment.common.is.close.form" text="可否直接結案" />
						</th>
						<th>
							<s:message code="form.process.managment.column.is.approver" text="是否為審核者" />
						</th>
						<th>
							<s:message code="form.process.managment.add.page.apply.process.wait" text="等待" /><br/><s:message code="form.process.managment.add.page.apply.process.sub.issue.finish" text="衍生單完成" />
						</th>
					</tr>
				</thead>
				<tbody id="reviewProcessTable">
					<tr align="center">
						<td><input type="checkbox" name="delCheck"/></td>
						<td><font class="reviewProcessText currentLevel">1</font></td>
						<td>
							<select class="reviewSysGroup" id="sysGroup">
								<option value=''><s:message code="form.process.managment.add.page.review.process.choose.process.group" text="請選擇流程群組" /></option>
							</select>
						</td>
						<td>
                            <button type='button' onclick='levelWordingsButton(this);'>
                                <i class="iconx-add"></i>設定
                            </button>
                            <input class='levelWordings hidden' name='levelWordings' />
                        </td>
						<td>
							<select class="nextLevel">
							</select>
						</td>
						<td>
							<select class="backLevel">
							</select>
						</td>
						<td><input id="isModifyColumnData" type="checkbox" /></td>
						<td><input id="isCloseForm" type="checkbox" /></td><!-- 可否直接結案-->
						<td><input id="isApprover" type="checkbox" /></td><!-- 是否為審核者 -->
						<td><input id="isWaitForSubIssueFinish" type="checkbox" /></td>
					</tr>
				</tbody>
			</table>
		</div>
	</fieldset>
	
<%@ include file="/WEB-INF/jsp/function/formProcessManagment/Common/FormProcessCommonSetting.jsp"%>	
<script>
<%-- 建立申請流程步驟內容 --%>
function createApplyFormDetail() {
	let isDisplay = 'styel="display:none"';
	let applyObjStr = '<tr align="center">';
		applyObjStr = applyObjStr + '<td><input name="delCheck" type="checkbox" /></td>';
		applyObjStr = applyObjStr + '<td><font class="applyProcessOrderText currentLevel"></font></td>';
		applyObjStr = applyObjStr + '<td><select class="applySysGroup" id="sysGroup"></select></td>';
        applyObjStr = applyObjStr +  '<td><button id="levelWordingsBtn" type="button" onclick="levelWordingsButton(this);"><i class="iconx-add"></i>設定</button><input class="hidden levelWordings" name="levelWordings" /></td>';
		if(isCountersign()) {
			isDisplay = isSpDivision() ? '' : 'style="display:none"';
			applyObjStr = applyObjStr + '<td custom="parallelCols" ' + isDisplay + '><input id="isParallel" type="radio"/></td>'
		}
	
		if(isCountersign()) {
			isDisplay = isSpDivision() ? '' : 'style="display:none"';
			applyObjStr = applyObjStr + 
				'<td custom="parallelCols" ' + isDisplay + '><button id="parallelButton" type="button" onclick="parallelButton();" style="display:none"><i class="iconx-add"></i>編輯</button></td>'
		}
		
		applyObjStr = applyObjStr + '<td><select class="nextLevel"></select></td>';
		applyObjStr = applyObjStr + '<td><select class="backLevel"></select></td>';
		applyObjStr = applyObjStr + '<td><input id="isCreateChangeIssue" type="checkbox"/></td>' ;
	
		if(!isCountersign()) {
			applyObjStr = applyObjStr + '<td><input id="isCreateCIssue" type="checkbox"/></td>'
		}

		applyObjStr = applyObjStr + '<td><input id="isWaitForSubIssueFinish" type="checkbox"/></td>'
		
		if(!isCountersign()) {
			applyObjStr = applyObjStr + '<td><input id="isCheckLevel" type="radio" name="isCheckLevel" /></td>'
		}
	
		applyObjStr = applyObjStr + '<td><input id="isModifyColumnData" type="checkbox"/></td>'
		applyObjStr = applyObjStr +  '<td><input id="isApprover" type="checkbox"/></td>'
		applyObjStr = applyObjStr + '</tr>'
	return applyObjStr;
}

<%-- 建立申請流程步驟內容 --%>
function createReviewFormDetail() {
	let reviewObjStr = '<tr align="center">'  
		reviewObjStr = reviewObjStr + '<td><input name="delCheck" type="checkbox" /></td>'
		reviewObjStr = reviewObjStr +  '<td><font class="reviewProcessText currentLevel"></font></td>'
		reviewObjStr = reviewObjStr +  '<td><select class="reviewSysGroup" id="sysGroup">'
		reviewObjStr = reviewObjStr +  '</select></td>'
        reviewObjStr = reviewObjStr +  '<td><button id="levelWordingsBtn" type="button" onclick="levelWordingsButton(this);"><i class="iconx-add"></i>設定</button><input class="hidden levelWordings" name="levelWordings" /></td>';
		reviewObjStr = reviewObjStr +  '<td><select class="nextLevel"></select></td>'
		reviewObjStr = reviewObjStr +  '<td><select class="backLevel"></select></td>'
		reviewObjStr = reviewObjStr +  '<td><input id="isModifyColumnData" type="checkbox"/></td>'
		reviewObjStr = reviewObjStr +  '<td><input id="isCloseForm" type="checkbox"/></td>'
		reviewObjStr = reviewObjStr +  '<td><input id="isApprover" type="checkbox"/></td>'
		reviewObjStr = reviewObjStr + '<td><input id="isWaitForSubIssueFinish" type="checkbox"/></td>'
		reviewObjStr = reviewObjStr +  '</tr>'
	
	return reviewObjStr;
}

<%-- 保存 --%>
function saveProcess() {
	alert("process");
	let verifyResult = true;
	let mainParams = {};//傳入後端的物件
	let applyProcessDataArray = [];//存放申請流程資訊陣列
	let reviewProcessDataArray = [];//存放審核流程資訊陣列
	let applyProcess = $("#applyProcessTable").find("tr");
	let reviewProcess = $("#reviewProcessTable").find("tr");
	let parallels = $('input#parallels').val();
	let formType = $("#formType").val();
	let division = $("#division").val();
	let processName = $("#processName").val();
	let radio = $('input:radio[name="isCheckLevel"]');
	
	if("" == formType) {
		alert("<s:message code='form.process.managment.add.page.function.alert.2' text='請選擇表單類別' />");//請選擇表單類別
		return;
	}
	
	if("" == division) {
		alert("<s:message code='form.process.managment.add.page.function.alert.3' text='請選擇科別' />");//請選擇科別
		return;
	}
	
	if("" == processName) {
		alert("<s:message code='form.process.managment.add.page.function.alert.4' text='請輸入流程名稱' />");//請輸入流程名稱
		return;
	}
	
	//會辦單不需要檢核變更檢核關卡是否勾選
	if(!isCountersign()) {
		if(!radio.is(":checked")) {
			alert("<s:message code='form.process.managment.add.page.function.alert.5' text='請選擇變更檢核關卡' />");//請選擇變更單檢核關卡
			return;
		}
	} else if (isSpDivision()) {
		if (!parallels) {
			alert('平行會辦組別至少選擇一組!');
			return;
		}
	}
	
	$.each(applyProcess,function() {
		let processOrder = 							$(this).find(".applyProcessOrderText").text();//流程順序
		let groupId = 								$(this).find("#sysGroup").val();//流程群組
		let nextLevel = 							$(this).find(".nextLevel").val();//下幾關(最多跳件關數)
		let backLevel = 							$(this).find(".backLevel").val();//退幾關(最多退件關數)
		let isCreateChangeIssue = 					$(this).find("#isCreateChangeIssue").prop("checked");//可否產生變更單
		let isCreateCIssue = 						$(this).find("#isCreateCIssue").prop("checked");//可否產生需求會辦單
		let isWaitForSubIssueFinish = 				$(this).find("#isWaitForSubIssueFinish").prop("checked");//等待衍生單完成
		let isCheckLevel = 							$(this).find("#isCheckLevel").is(':checked');//變更單檢核關卡
		let isModifyColumnData = 					$(this).find("#isModifyColumnData").prop("checked");//可修改欄位資料
		let isApprover = 							$(this).find("#isApprover").prop("checked");//是否為審核者
		let isParallel = 							$(this).find("#isParallel").prop("checked");//是否為平行會辦關卡
        let levelWordings =                         $(this).find(".levelWordings").val();//關卡文字
		//processOrder 一定會有值,若沒值的話代表JQuery selector撈到一些奇怪的tr,特別用此法進行排除
		if("" != processOrder) {
			if("" == groupId) {
				let alertStr = "<s:message code='form.process.managment.add.page.function.alert.6' text='申請流程順序 {0} 請選擇流程群組' />"
					alertStr = alertStr.replace("{0}",processOrder);
				alert(alertStr);//請選擇流程群組
				verifyResult = false;
				return false;
			}
		
			let applyProcessParams = {};
			applyProcessParams.processOrder = processOrder;
			applyProcessParams.groupId = groupId;
			applyProcessParams.nextLevel = nextLevel == null? "0" : nextLevel;
			applyProcessParams.backLevel = backLevel == null? "0" : backLevel;
			applyProcessParams.isCreateChangeIssue = isCreateChangeIssue == true? 'Y' : 'N';
			applyProcessParams.isCreateCIssue = isCreateCIssue == true? 'Y' : 'N';
			applyProcessParams.isWaitForSubIssueFinish = isWaitForSubIssueFinish == true? 'Y' : 'N';
			applyProcessParams.isModifyColumnData = isModifyColumnData == true? 'Y' : 'N';
			applyProcessParams.isCheckLevel = isCheckLevel == true? 'Y' : 'N';
			applyProcessParams.isApprover = isApprover == true? 'Y' : 'N';
			applyProcessParams.isParallel = isParallel ? 'Y' : 'N';
            applyProcessParams.levelWordings = levelWordings;
			if (isParallel) {
				applyProcessParams.parallels = parallels;
			}
			
			applyProcessDataArray.push(applyProcessParams)
		}
	});
	
	//沒驗過就直接停止
	if(!verifyResult) {
		return;
	}
	
	$.each(reviewProcess,function() {
		let processOrder = 		 		$(this).find(".reviewProcessText").text();//流程順序
		let groupId = 			 		$(this).find("#sysGroup").val();//流程群組
		let nextLevel =			 		$(this).find(".nextLevel").val();//下幾關(最多跳件關數)
		let backLevel = 		 		$(this).find(".backLevel").val();//退幾關(最多退件關數)
		let isModifyColumnData = 		$(this).find("#isModifyColumnData").prop("checked");//可修改欄位資料
		let isCloseForm = 		 		$(this).find("#isCloseForm").prop("checked");//可否直接結案
		let isApprover = 		 		$(this).find("#isApprover").prop("checked");//是否為審核者
		let isWaitForSubIssueFinish = 	$(this).find("#isWaitForSubIssueFinish").prop("checked");//等待衍生單完成
        let levelWordings =             $(this).find(".levelWordings").val();//關卡文字
		//processOrder 一定會有值,若沒值的話代表JQuery selector撈到一些奇怪的tr,特別用此法進行排除
		if("" != processOrder) {
			if("" == groupId) {
				let alertStr = "<s:message code='form.process.managment.add.page.function.alert.7' text='審核流程順序 {0} 請選擇流程群組' />"
					alertStr = alertStr.replace("{0}",processOrder);
				
				alert(alertStr);//請選擇流程群組
				verifyResult = false;
				
				return false;
			}
	
			let reviewProcessParams = {};
			reviewProcessParams.processOrder = processOrder;
			reviewProcessParams.groupId = groupId;
			reviewProcessParams.nextLevel = nextLevel == null ? "0" : nextLevel;
			reviewProcessParams.backLevel = backLevel == null? "0" : backLevel;
			reviewProcessParams.isModifyColumnData = isModifyColumnData == true? 'Y' : 'N';
			reviewProcessParams.isCloseForm = isCloseForm == true? 'Y' : 'N';
			reviewProcessParams.isApprover = isApprover == true? 'Y' : 'N';
			reviewProcessParams.isWaitForSubIssueFinish = isWaitForSubIssueFinish == true? 'Y' : 'N';
            reviewProcessParams.levelWordings = levelWordings;
			reviewProcessDataArray.push(reviewProcessParams)
		}
	});
	
	//沒驗過就直接停止
	if(!verifyResult) {
		return;
	}
	
	//把上述兩個流程的資訊,放入mainParams中
	mainParams.applyProcessList = applyProcessDataArray;
	mainParams.reviewProcessList = reviewProcessDataArray;
	mainParams.formType = formType;
	mainParams.division = division;
	mainParams.processName = processName;
	
	//Insert
	SendUtil.post('/formProcessManagmentBa/insertFormProcess', mainParams, function (data) {
		if(data != undefined || data != null) {
			alert(data.result);
			goBackSearchPage();
		}
	},null,true);
}

</script>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
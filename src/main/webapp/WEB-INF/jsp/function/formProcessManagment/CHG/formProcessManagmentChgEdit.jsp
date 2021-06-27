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
					<select id="formType" disabled="disabled">
						<c:forEach var="formTypeLs" items="${formTypeLs}">
							<option value="${formTypeLs.value}">${formTypeLs.wording}</option>
						</c:forEach>
					</select>
				</td>
				<th><font color="red">*</font><s:message code="form.process.managment.find.page.division.type" text='部門科別' /></th>
				<td>
					<select id="division" disabled="disabled">
						<c:forEach var="divisionLs" items="${divisionLs}">
							<option value="${divisionLs.value}">${divisionLs.wording}</option>
						</c:forEach>
					</select>
				</td>
				<th><font color="red">*</font><s:message code="form.process.managment.find.page.process.name" text='流程名稱' /></th>
				<td><input id="processName" type="text" value="${formProcessManagmentFormVo.processName}" style="width: 10rem;" maxlength="30" /></td>
				<th><s:message code='form.process.managment.edit.page.update.by' text='修改人員' /></th>
				<td nowrap="nowrap">${formProcessManagmentFormVo.updatedBy}</td>
				<th><s:message code='form.process.managment.edit.page.update.at' text='修改時間' /></th>
				<td nowrap="nowrap">${updateDateText}</td>
				<td nowrap="nowrap">
					<button>
						<i class="iconx-save"></i> <s:message code="button.save" text="儲存" />
					</button>
					<button id="goBackBtn">
						<i class="iconx-back"></i> <s:message code="group.function.edit.button.back" text="回前頁" />
					</button>
					<input id='pageType' class='hidden' value='edit' />
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
						<s:message code="form.process.managment.chg.column.level" text="關卡" />
					</th>
					<th>
						<s:message code="form.process.managment.chg.column.this.level" text="本關卡" />
					</th>
					<th>
                        <s:message code="form.process.managment.levelwordings" text="關卡文字" />
                    </th>
					<th>
						<s:message code="form.process.managment.add.page.apply.process.most" text="最多" /><br/><s:message code="form.process.managment.add.page.apply.process.next.level.counts" text="跳件關數" />
					</th>
					<th>
						<s:message code="form.process.managment.add.page.apply.process.most" text="最多" /><br/><s:message code="form.process.managment.add.page.apply.process.back.level.counts" text="退件關數" />
					</th>
					<th>
						<s:message code="form.process.managment.chg.column.sr.check.level" text="需求單檢核關卡" />
					</th>
					<th>
						<s:message code="form.process.managment.chg.column.sr.c.check.level" text="需求單會辦檢核關卡" />
					</th>
					<th>
						<s:message code="form.process.managment.chg.column.quest.check.level" text="問題單檢核關卡" />
					</th>
					<th>
						<s:message code="form.process.managment.chg.column.quest.c.check.level" text="問題單會辦單檢核關卡" />
					</th>
					<th>
						<s:message code="form.process.managment.chg.column.wait.for.sub.issue.finish" text="等待衍生單完成" />
					</th>
					<th>
						<s:message code="form.process.managment.chg.column.business.impact.analysi" text="衝擊分析判斷" />
					</th>
					<th>
						<s:message code="form.process.managment.add.page.review.process.is.modify.column.data.1" text="可修改欄位資料" />
					</th>
					<th>
						<s:message code="form.process.managment.column.is.approver" text="是否為審核者" />
					</th>
				</tr>
				<tbody id="applyProcessTable">
					<c:forEach var="applyProcessList" items="${formProcessManagmentFormVo.applyProcessList}">
						<tr align="center">
							<td><input name="delCheck" type="checkbox" /></td>
							<td><font class="applyProcessOrderText currentLevel">${applyProcessList.processOrder}</font></td>
							<td>
								<select class="applySysGroup" id="sysGroup">
									<c:forEach var="sysGroupLs" items="${applySysGroupLs}">
										<c:choose>
											<c:when test="${applyProcessList.groupId eq sysGroupLs.value}">
												<option value="${sysGroupLs.value}" selected="selected">${sysGroupLs.wording}</option>
											</c:when>
											<c:otherwise>
												<option value="${sysGroupLs.value}">${sysGroupLs.wording}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</td>
							<td>
                                <button type='button' onclick="levelWordingsButton(this)">
                                    <i class="iconx-add"></i>設定
                                </button>
                                <input class="hidden levelWordings" name="levelWordings" />
                            </td>
							<td>
								<select class="nextLevel">
								</select>
							</td>
							<td>
								<select class="backLevel">
								</select>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq applyProcessList.isSrCheckLevel}">
										<input id="isSrCheckLevel" type="radio" name="isSrCheckLevel" checked="checked"><!-- 需求單檢核關卡 -->
									</c:when>
									<c:otherwise>
										<input id="isSrCheckLevel" type="radio" name="isSrCheckLevel"><!-- 需求單檢核關卡 -->
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq applyProcessList.isSrCCheckLevel}">
										<input id="isSrCCheckLevel" type="radio" name="isSrCCheckLevel" checked="checked"><!-- 需求會辦單檢核關卡 -->
									</c:when>
									<c:otherwise>
										<input id="isSrCCheckLevel" type="radio" name="isSrCCheckLevel"><!-- 需求會辦單檢核關卡 -->
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq applyProcessList.isQuestionCheckLevel}">
										<input id="isQuestionCheckLevel" type="radio" name="isQuestionCheckLevel" checked="checked"><!-- 問題單檢核關卡 -->
									</c:when>
									<c:otherwise>
										<input id="isQuestionCheckLevel" type="radio" name="isQuestionCheckLevel"><!-- 問題單檢核關卡 -->
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq applyProcessList.isQuestionCCheckLevel}">
										<input id="isQuestionCCheckLevel" type="radio" name="isQuestionCCheckLevel" checked="checked"><!-- 問題會辦單檢核關卡 -->
									</c:when>
									<c:otherwise>
										<input id="isQuestionCCheckLevel" type="radio" name="isQuestionCCheckLevel"><!-- 問題會辦單檢核關卡 -->
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq applyProcessList.isWaitForSubIssueFinish}">
										<input id="isWaitForSubIssueFinish" type="checkbox" checked="checked"/><!-- 等待衍生單完成 -->
									</c:when>
									<c:otherwise>
										<input id="isWaitForSubIssueFinish" type="checkbox" /><!-- 等待衍生單完成 -->
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq applyProcessList.isBusinessImpactAnalysis}">
										<input id="isBusinessImpactAnalysis" type="checkbox" checked="checked"/><!-- 衝擊分析判斷 -->
									</c:when>
									<c:otherwise>
										<input id="isBusinessImpactAnalysis" type="checkbox" /><!-- 衝擊分析判斷 -->
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq applyProcessList.isModifyColumnData}">
										<input id="isModifyColumnData" type="checkbox" checked="checked"/>
									</c:when>
									<c:otherwise>
										<input id="isModifyColumnData" type="checkbox" />
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq applyProcessList.isApprover}">
										<input id="isApprover" type="checkbox" checked="checked"/>
									</c:when>
									<c:otherwise>
										<input id="isApprover" type="checkbox" /><!-- 是否為審核者 -->
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
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
							<s:message code="form.process.managment.chg.column.level" text="關卡" />
						</th>
						<th>
							<s:message code="form.process.managment.chg.column.this.level" text="本關卡" />
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
							<s:message code="form.process.managment.chg.column.is.create.job.issue" text="是否產生工作單" />
						</th>
						<th>
							<s:message code="form.process.managment.chg.column.wait.for.sub.issue.finish" text="等待衍生單完成" />
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
					</tr>
				</thead>
				<tbody id="reviewProcessTable">
					<c:forEach var="reviewProcessList" items="${formProcessManagmentFormVo.reviewProcessList}">
						<tr align="center">
							<td><input type="checkbox" name="delCheck"/></td>
							<td><font class="reviewProcessText currentLevel">${reviewProcessList.processOrder}</font></td>
							<td>
								<select class="reviewSysGroup" id="sysGroup">
									<c:forEach var="sysGroupLs" items="${reviewSysGroupLs}">
										<c:choose>
											<c:when test="${reviewProcessList.groupId eq sysGroupLs.value}">
												<option value="${sysGroupLs.value}" selected="selected">${sysGroupLs.wording}</option>
											</c:when>
											<c:otherwise>
												<option value="${sysGroupLs.value}">${sysGroupLs.wording}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</td>
							<td>
                                <button type='button' onclick="levelWordingsButton(this)">
                                    <i class="iconx-add"></i>設定
                                </button>
                                <input class="hidden levelWordings" name="levelWordings" />
                            </td>
							<td>
								<select class="nextLevel">
								</select>
							</td>
							<td>
								<select class="backLevel">
							</select>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq reviewProcessList.isCreateJobIssue}">
										<input id="isCreateJobIssue" type="checkbox" checked="checked"/>
									</c:when>
									<c:otherwise>
										<input id="isCreateJobIssue" type="checkbox" />
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq reviewProcessList.isWaitForSubIssueFinish}">
										<input id="isWaitForSubIssueFinish" type="checkbox" checked="checked"/><!-- 等待衍生單完成 -->
									</c:when>
									<c:otherwise>
										<input id="isWaitForSubIssueFinish" type="checkbox" /><!-- 等待衍生單完成 -->
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq reviewProcessList.isModifyColumnData}">
										<input id="isModifyColumnData" type="checkbox" checked="checked"/>
									</c:when>
									<c:otherwise>
										<input id="isModifyColumnData" type="checkbox" />
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq reviewProcessList.isCloseForm}">
										<input id="isCloseForm" type="checkbox" checked="checked"/>
									</c:when>
									<c:otherwise>
										<input id="isCloseForm" type="checkbox" />
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' eq reviewProcessList.isApprover}">
										<input id="isApprover" type="checkbox" checked="checked"/>
									</c:when>
									<c:otherwise>
										<input id="isApprover" type="checkbox" /><!-- 是否為審核者 -->
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</fieldset>

<%@ include file="/WEB-INF/jsp/function/formProcessManagment/Common/FormProcessCommonSetting.jsp"%>	

<script>//# sourceURL=edit.js

var processId = "";//formProcessDataInit處取得

$(function() {
	formProcessDataInit();//初始化
});

<%-- 建立申請流程步驟內容 --%>
function createApplyFormDetail() {
	let applyObjStr = '<tr align="center">'  
		applyObjStr = applyObjStr + '<td><input name="delCheck" type="checkbox" /></td>'
		applyObjStr = applyObjStr +  '<td><font class="applyProcessOrderText currentLevel"></font></td>'
		applyObjStr = applyObjStr +  '<td><select class="applySysGroup" id="sysGroup">'
		applyObjStr = applyObjStr +  '</select></td>'
        applyObjStr = applyObjStr +  '<td><button id="levelWordingsBtn" type="button" onclick="levelWordingsButton(this);"><i class="iconx-add"></i>設定</button><input class="hidden levelWordings" name="levelWordings" /></td>';
		applyObjStr = applyObjStr +  '<td><select class="nextLevel"></select></td>'
		applyObjStr = applyObjStr +  '<td><select class="backLevel"></select></td>'
		applyObjStr = applyObjStr +  '<td><input id="isSrCheckLevel" type="radio" name="isSrCheckLevel"></td>' 
		applyObjStr = applyObjStr +  '<td><input id="isSrCCheckLevel" type="radio" name="isSrCCheckLevel"></td>'
		applyObjStr = applyObjStr +  '<td><input id="isQuestionCheckLevel" type="radio" name="isQuestionCheckLevel"></td>'
		applyObjStr = applyObjStr +  '<td><input id="isQuestionCCheckLevel" type="radio" name="isQuestionCCheckLevel"></td>'
		applyObjStr = applyObjStr +  '<td><input id="isWaitForSubIssueFinish" type="checkbox" /></td>'
		applyObjStr = applyObjStr +  '<td><input id="isBusinessImpactAnalysis" type="checkbox" /></td>'
		applyObjStr = applyObjStr +  '<td><input id="isModifyColumnData" type="checkbox"/></td>'
		applyObjStr = applyObjStr +  '<td><input id="isApprover" type="checkbox"/></td>'
		applyObjStr = applyObjStr +  '</tr>'
	
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
		reviewObjStr = reviewObjStr +  '<td><input id="isCreateJobIssue" type="checkbox" /></td>'
		reviewObjStr = reviewObjStr +  '<td><input id="isWaitForSubIssueFinish" type="checkbox" /></td>'
		reviewObjStr = reviewObjStr +  '<td><input id="isModifyColumnData" type="checkbox"/></td>'
		reviewObjStr = reviewObjStr +  '<td><input id="isCloseForm" type="checkbox"/></td>'
		reviewObjStr = reviewObjStr +  '<td><input id="isApprover" type="checkbox"/></td>'
		reviewObjStr = reviewObjStr +  '</tr>'
	
	return reviewObjStr;
}

<%-- 保存 --%>
function saveProcess() {
	let verifyResult = true;
	let mainParams = {};//傳入後端的物件
	let applyProcessDataArray = [];//存放申請流程資訊陣列
	let reviewProcessDataArray = [];//存放審核流程資訊陣列
	let applyProcess = $("#applyProcessTable").find("tr");
	let reviewProcess = $("#reviewProcessTable").find("tr");
	
	let formType = $("#formType").val();
	let division = $("#division").val();
	let processName = $("#processName").val();
	let isSrCheckLevelRadio = $('input:radio[name="isSrCheckLevel"]');
	let isSrCCheckLevelRadio = $('input:radio[name="isSrCCheckLevel"]');
	let isQuestionCheckLevelRadio = $('input:radio[name="isQuestionCheckLevel"]');
	let isQuestionCCheckLevelRadio = $('input:radio[name="isQuestionCCheckLevel"]');
	
	if("" == processName) {
		alert("<s:message code='form.process.managment.add.page.function.alert.4' text='請輸入流程名稱' />");//請輸入流程名稱
		
		return;
	}
	
	if(!isSrCheckLevelRadio.is(":checked")) {
		alert("<s:message code='form.process.managment.chg.verify.warning.1' text='請選擇需求單檢核關卡' />");//請選擇需求單檢核關卡
		
		return;
	}
	
	if(!isSrCCheckLevelRadio.is(":checked")) {
		alert("<s:message code='form.process.managment.chg.verify.warning.2' text='請選擇需求會辦單檢核關卡' />");//請選擇需求會辦單檢核關卡
		
		return;
	}
	
	if(!isQuestionCheckLevelRadio.is(":checked")) {
		alert("<s:message code='form.process.managment.chg.verify.warning.3' text='請選擇問題單檢核關卡' />");//請選擇問題單檢核關卡
		
		return;
	}
	
	if(!isQuestionCCheckLevelRadio.is(":checked")) {
		alert("<s:message code='form.process.managment.chg.verify.warning.4' text='請選擇問題會辦單檢核關卡' />");//請選擇問題會辦單檢核關卡
		
		return;
	}
	
	$.each(applyProcess,function() {
		let processOrder = 				$(this).find(".applyProcessOrderText").text();//流程順序
		let groupId = 					$(this).find("#sysGroup").val();//流程群組
		let nextLevel = 				$(this).find(".nextLevel").val();//下幾關(最多跳件關數)
		let backLevel = 				$(this).find(".backLevel").val();//退幾關(最多退件關數)
		let isSrCheckLevel = 			$(this).find("#isSrCheckLevel").is(":checked");//需求單檢核關卡
		let isSrCCheckLevel = 			$(this).find("#isSrCCheckLevel").is(":checked");//需求會辦單檢核關卡
		let isQuestionCheckLevel = 		$(this).find("#isQuestionCheckLevel").is(":checked");//問題單檢核關卡
		let isQuestionCCheckLevel = 	$(this).find("#isQuestionCCheckLevel").is(':checked');//問題會辦單檢核關卡
		let isBusinessImpactAnalysis = 	$(this).find("#isBusinessImpactAnalysis").prop("checked");//衝擊分析判斷
		let isModifyColumnData = 		$(this).find("#isModifyColumnData").prop("checked");//可修改欄位資料
		let isApprover = 				$(this).find("#isApprover").prop("checked");//是否為審核者
		let isWaitForSubIssueFinish = 	$(this).find("#isWaitForSubIssueFinish").prop("checked");//等待衍生單完成
        let levelWordings =             $(this).find(".levelWordings").val();//關卡文字
		//processOrder 一定會有值,若沒值的話代表JQuery selector撈到一些奇怪的tr,特別用此法進行排除
		if("" != processOrder) {
			if("" == groupId) {
				let alertStr = "<s:message code='form.process.managment.add.page.function.alert.6' text='申請流程順序 {0} 請選擇流程群組' />"
					alertStr = alertStr.replace("{0}",processOrder);
				alert(alertStr);
				verifyResult = false;
				
				return false;
			}
		
			let applyProcessParams = {};
			applyProcessParams.processOrder = processOrder;
			applyProcessParams.groupId = groupId;
			applyProcessParams.nextLevel = nextLevel == null? "0" : nextLevel;
			applyProcessParams.backLevel = backLevel == null? "0" : backLevel;
			applyProcessParams.isSrCheckLevel = isSrCheckLevel == true? 'Y' : 'N';
			applyProcessParams.isSrCCheckLevel = isSrCCheckLevel == true? 'Y' : 'N';
			applyProcessParams.isQuestionCheckLevel = isQuestionCheckLevel == true? 'Y' : 'N';
			applyProcessParams.isQuestionCCheckLevel = isQuestionCCheckLevel == true? 'Y' : 'N';
			applyProcessParams.isBusinessImpactAnalysis = isBusinessImpactAnalysis == true? 'Y' : 'N';
			applyProcessParams.isModifyColumnData = isModifyColumnData == true? 'Y' : 'N';
			applyProcessParams.isApprover = isApprover == true? 'Y' : 'N';
			applyProcessParams.isWaitForSubIssueFinish = isWaitForSubIssueFinish == true? 'Y' : 'N';
            applyProcessParams.levelWordings = levelWordings;
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
		let isCreateJobIssue = 	 		$(this).find("#isCreateJobIssue").prop("checked");//可否產生工作單
		let isWaitForSubIssueFinish = 	$(this).find("#isWaitForSubIssueFinish").prop("checked");//等待衍生單完成
		let isModifyColumnData = 		$(this).find("#isModifyColumnData").prop("checked");//可修改欄位資料
		let isCloseForm = 				$(this).find("#isCloseForm").prop("checked");//可否直接結案
		let isApprover = 				$(this).find("#isApprover").prop("checked");//是否為審核者
        let levelWordings =             $(this).find(".levelWordings").val();//關卡文字
		//processOrder 一定會有值,若沒值的話代表JQuery selector撈到一些奇怪的tr,特別用此法進行排除
		if("" != processOrder) {
			if("" == groupId) {
				let alertStr = "<s:message code='form.process.managment.add.page.function.alert.7' text='審核流程順序 {0} 請選擇流程群組' />"
					alertStr = alertStr.replace("{0}",processOrder);
				alert(alertStr);
				verifyResult = false;
				
				return false;
			}
	
			let reviewProcessParams = {};
			reviewProcessParams.processOrder = processOrder;
			reviewProcessParams.groupId = groupId;
			reviewProcessParams.nextLevel = nextLevel == null ? "0" : nextLevel;
			reviewProcessParams.backLevel = backLevel == null? "0" : backLevel;
			reviewProcessParams.isCreateJobIssue = isCreateJobIssue == true? 'Y' : 'N';
			reviewProcessParams.isWaitForSubIssueFinish = isWaitForSubIssueFinish == true? 'Y' : 'N';
			reviewProcessParams.isModifyColumnData = isModifyColumnData == true? 'Y' : 'N';
			reviewProcessParams.isCloseForm = isCloseForm == true? 'Y' : 'N';
			reviewProcessParams.isApprover = isApprover == true? 'Y' : 'N';
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
	mainParams.processId = processId;
	mainParams.isEnable = '${isEnable}';
	
	//Update
	SendUtil.post('/formProcessManagmentChg/updateFormProcess', mainParams, function (data) {
		if(data != undefined || data != null) {
			alert(data.result);
			goBackSearchPage();
		}
	},null,true);
}

</script>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
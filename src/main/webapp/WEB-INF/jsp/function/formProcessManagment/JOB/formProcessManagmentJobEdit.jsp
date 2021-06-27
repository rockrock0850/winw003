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
				<th><span style="color: red; ">*</span><s:message code="form.process.managment.find.page.form.type" text='表單類別' /></th>
				<td>
					<select id="formType" disabled="disabled">
						<c:forEach var="formTypeLs" items="${formTypeLs}">
							<option value="${formTypeLs.value}">${formTypeLs.wording}</option>
						</c:forEach>
					</select>
				</td>
				<th><span style="color: red; ">*</span><s:message code="form.process.managment.find.page.division.type" text='部門科別' /></th>
				<td>
					<select id="division" disabled="disabled">
						<c:forEach var="divisionLs" items="${divisionLs}">
							<option value="${divisionLs.value}">${divisionLs.wording}</option>
						</c:forEach>
					</select>
				</td>
				<th><span style="color: red; ">*</span><s:message code="form.process.managment.find.page.process.name" text='流程名稱' /></th>
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
				</td>
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
						<s:message code="form.process.managment.job.column.level" text="關數"/>
					</th>
					<th>
						<s:message code="form.process.managment.job.column.this.level" text="本關卡"/>
					</th>
					<th>
                        <s:message code="form.process.managment.levelwordings" text="關卡文字" />
                    </th>
					<c:if test="${isCountersign == 'Y'}">
						<th>平行會辦</th>
						<th>會辦群組</th>
						<th style='display:none'>
							<c:choose>
						        <c:when test="${isParallel == 'Y'}">
						           <input id='parallels' name='parallels' value='${parallels}' />
						        </c:when>
						        <c:otherwise>
						           <input id='parallels' name='parallels' />     
						        </c:otherwise>
						    </c:choose>
						</th>
					</c:if>
					<th>
						<s:message code="form.process.managment.job.column.work.project" text="工作項目"/>
					</th>
					<th>
						<s:message code="form.process.managment.add.page.apply.process.most" text="最多" /><br/><s:message code="form.process.managment.add.page.apply.process.next.level.counts" text="跳件關數" />
					</th>
					<th>
						<s:message code="form.process.managment.add.page.apply.process.most" text="最多" /><br/><s:message code="form.process.managment.add.page.apply.process.back.level.counts" text="退件關數" />
					</th>
					<th>
						<s:message code="form.process.managment.job.column.work.level" text="作業關卡"/>
					</th>
					<c:if test="${isCountersign != 'Y'}">
						<th>
							<s:message code="form.process.managment.job.column.is.create.job.c.issue" text="可否產生工作會辦單"/>
						</th>
					</c:if>
					<c:if test="${isCountersign == 'Y'}">
						<th>
							<s:message code="form.process.managment.job.column.is.create.sp.job.issue" text="可否新增系統科工作單"/>
						</th>
					</c:if>
					<th>
						<s:message code="form.process.managment.chg.column.wait.for.sub.issue.finish" text="等待衍生單完成" />
					</th>
					<th>
						<s:message code="form.process.managment.job.column.is.create.compare.code.list" text="可否產生程式差異清單"/>
					</th>
					<th>
						<s:message code="form.process.managment.add.page.review.process.is.modify.column.data.1" text="可修改欄位資料" />
					</th>
					<th><s:message code="form.process.managment.column.is.approver" text="是否為審核者" /></th>
				</tr>
				<tbody id="applyProcessTable">
					<c:forEach var="applyProcessList" items="${formProcessManagmentFormVo.applyProcessList}" varStatus="loop">
						<tr align="center">
							<td><input name="delCheck" type="checkbox" /></td>
							<td><span class="applyProcessOrderText currentLevel">${applyProcessList.processOrder}</span></td>
							<td>
								<select class="applySysGroup" id="sysGroup">
									<option value="">請選擇</option>
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
							<c:if test="${isCountersign == 'Y'}">
								<td>
									<c:choose>
										<c:when test="${'Y' eq applyProcessList.isParallel}">
											<input id='isParallel' type='radio' checked />
										</c:when>
										<c:otherwise>
											<input id='isParallel' type='radio' />
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:choose>
										<c:when test="${'Y' eq applyProcessList.isParallel}">
											<button id="parallelButton" type='button' onclick='parallelButton();'>
												<i class="iconx-add"></i>編輯
											</button>
										</c:when>
										<c:otherwise>
											<button id="parallelButton" type='button' onclick='parallelButton();' style="display:none">
												<i class="iconx-add"></i>編輯
											</button>
										</c:otherwise>
									</c:choose>
								</td>
							</c:if>
							<td>
								<select class="workProject" id="workProject">
									<option value="">請選擇</option>
									<c:forEach var="workProject" items="${workProjectLs}">
										<c:choose>
											<c:when test="${applyProcessList.workProject eq workProject.value}">
												<option value="${workProject.value}" selected="selected">${workProject.wording}</option>
											</c:when>
											<c:otherwise>
												<option value="${workProject.value}">${workProject.wording}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
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
									<c:when test="${'Y' == applyProcessList.isWorkLevel}">
										<!-- 作業關卡 -->
										<input id="isWorkLevel" type="checkbox" checked="checked"/>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${0 == loop.index}">
												<input id="isWorkLevel" type="checkbox" disabled />
											</c:when>
											<c:otherwise>
												<input id="isWorkLevel" type="checkbox" />
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</td>
							<c:if test="${isCountersign != 'Y'}">
								<td>
									<c:choose>
										<c:when test="${'Y' == applyProcessList.isCreateJobCIssue}">
											<!-- 可否產生工作會辦單 -->
											<input id="isCreateJobCIssue" type="checkbox" checked="checked"/>
										</c:when>
										<c:otherwise>
											<input id="isCreateJobCIssue" type="checkbox" />
										</c:otherwise>
									</c:choose>
								</td>
							</c:if>
							<c:if test="${isCountersign == 'Y'}">
								<td>
									<c:choose>
										<c:when test="${'Y' == applyProcessList.isCreateSpJobIssue}">
											<!-- 可否產生工作會辦單 -->
											<input id="isCreateSpJobIssue" type="checkbox" checked="checked"/>
										</c:when>
										<c:otherwise>
											<input id="isCreateSpJobIssue" type="checkbox" />
										</c:otherwise>
									</c:choose>
								</td>
							</c:if>
							<td>
								<c:choose>
									<c:when test="${'Y' == applyProcessList.isWaitForSubIssueFinish}">
										<!-- 等待衍生單完成 -->
										<input id="isWaitForSubIssueFinish" name="isWaitForSubIssueFinishApply" type="radio"  checked="checked"/>
									</c:when>
									<c:otherwise>
										<input id="isWaitForSubIssueFinish" name="isWaitForSubIssueFinishApply" type="radio"  />
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' == applyProcessList.isCreateCompareList}">
										<!-- 是否產生程式差異清單 -->
										<input id="isCreateCompareList" type="checkbox" checked="checked"/>
									</c:when>
									<c:otherwise>
										<input id="isCreateCompareList" type="checkbox" />
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${'Y' == applyProcessList.isModifyColumnData}">
										<!-- 可異動欄位資料 -->
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
							<s:message code="form.process.managment.add.page.review.process.process.order" text="流程順序" />
						</th>
						<th>
							<s:message code="form.process.managment.add.page.review.process.process.group" text="流程群組" />
						</th>
						<th>
                            <s:message code="form.process.managment.levelwordings" text="關卡文字" />
                        </th>
						<th>
							<s:message code="form.process.managment.job.column.work.project" text="工作項目"/>
						</th>
						<th>
							<s:message code="form.process.managment.add.page.review.process.process.most" text="最多" /><br/><s:message code="form.process.managment.add.page.review.process.next.level.counts" text="跳件關數" />
						</th>
						<th>
							<s:message code="form.process.managment.add.page.review.process.process.most" text="最多" /><br/><s:message code="form.process.managment.add.page.review.process.back.level.counts" text="退件關數" />
						</th>
						<th>
							<s:message code="form.process.managment.job.column.work.level" text="作業關卡"/>
						</th>
						
						<c:if test="${isCountersign != 'Y'}">
							<th>
								<s:message code="form.process.managment.job.column.is.create.job.c.issue" text="可否產生工作會辦單"/>
							</th>
							<th>
								<s:message code="form.process.managment.chg.column.wait.for.sub.issue.finish" text="等待衍生單完成" />
							</th>
						</c:if>
						
						<th>
							<s:message code="form.process.managment.add.page.review.process.is.modify.column.data.1" text="可修改欄位資料" />
						</th>
						<th>
							<s:message code="form.process.managment.common.is.close.form" text="可否直接結案" />
						</th>
						<th><s:message code="form.process.managment.column.is.approver" text="是否為審核者" /></th>
					</tr>
				</thead>
				<tbody id="reviewProcessTable">
					<c:forEach var="reviewProcessList" items="${formProcessManagmentFormVo.reviewProcessList}">
						<tr align="center">
							<td><input type="checkbox" name="delCheck"/></td>
							<td><span class="reviewProcessText currentLevel">${reviewProcessList.processOrder}</span></td>
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
								<select class="workProject" id="workProject">
									<option value="">請選擇</option>
									<c:forEach var="workProject" items="${workProjectLs}">
										<c:choose>
											<c:when test="${reviewProcessList.workProject eq workProject.value}">
												<option value="${workProject.value}" selected="selected">${workProject.wording}</option>
											</c:when>
											<c:otherwise>
												<option value="${workProject.value}">${workProject.wording}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
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
									<c:when test="${'Y' == reviewProcessList.isWorkLevel}">
										<!-- 作業關卡 -->
										<input id="isWorkLevel" type="checkbox" checked="checked"/>
									</c:when>
									<c:otherwise>
										<input id="isWorkLevel" type="checkbox" />
									</c:otherwise>
								</c:choose>
							</td>
							<c:if test="${isCountersign != 'Y'}">
								<td>
									<c:choose>
										<c:when test="${'Y' == reviewProcessList.isCreateJobCIssue}">
											<!-- 可否產生工作會辦單 -->
											<input id="isCreateJobCIssue" type="checkbox" checked="checked"/>
										</c:when>
										<c:otherwise>
											<input id="isCreateJobCIssue" type="checkbox" />
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:choose>
										<c:when test="${'Y' == reviewProcessList.isWaitForSubIssueFinish}">
											<!-- 可否產生工作會辦單 -->
											<input id="isWaitForSubIssueFinish" type="radio" checked="checked" name="isWaitForSubIssueFinishReview"/>
										</c:when>
										<c:otherwise>
											<input id="isWaitForSubIssueFinish" type="radio" name="isWaitForSubIssueFinishReview"/>
										</c:otherwise>
									</c:choose>
								</td>
							</c:if>
							<td>
								<c:choose>
									<c:when test="${'Y' == reviewProcessList.isModifyColumnData}">
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

<script>//# sourceURL=ProcessJobEdit.js
var processId = "";//formProcessDataInit處取得

$(function() {
	formProcessDataInit();//初始化
});

<%-- 建立申請流程步驟內容 --%>
function createApplyFormDetail() {
	let applyObjStr = '<tr align="center">'  
		applyObjStr = applyObjStr + '<td><input name="delCheck" type="checkbox" /></td>'
		applyObjStr = applyObjStr +  '<td><font class="applyProcessOrderText currentLevel"></font></td>'
		applyObjStr = applyObjStr +  '<td><select class="applySysGroup" id="sysGroup"></select></td>'
        applyObjStr = applyObjStr +  '<td><button id="levelWordingsBtn" type="button" onclick="levelWordingsButton(this);"><i class="iconx-add"></i>設定</button><input class="hidden levelWordings" name="levelWordings" /></td>';
		if(isCountersign()) {
			applyObjStr = applyObjStr + '<td><input id="isParallel" type="radio"/></td>'
		}
	
		if(isCountersign()) {
			applyObjStr = applyObjStr + 
				'<td><button id="parallelButton" type="button" onclick="parallelButton();" style="display:none"><i class="iconx-add"></i>編輯</button></td>'
		}
		
		applyObjStr = applyObjStr +  '<td><select class="workProject" id="workProject"></select></td>'
		applyObjStr = applyObjStr +  '<td><select class="nextLevel"></select></td>'
		applyObjStr = applyObjStr +  '<td><select class="backLevel"></select></td>'
		applyObjStr = applyObjStr +  '<td><input id="isWorkLevel" type="checkbox" /></td>' 
	
		if(!isCountersign()) {
			applyObjStr = applyObjStr + '<td><input id="isCreateJobCIssue" type="checkbox" /></td>'
		}
	
		if(isCountersign()) {
			applyObjStr = applyObjStr + '<td><input id="isCreateSpJobIssue" type="checkbox" /></td>'
		}
		
		applyObjStr = applyObjStr + '<td><input id="isWaitForSubIssueFinish" name="isWaitForSubIssueFinishApply" type="radio"  /></td>'
		applyObjStr = applyObjStr +  '<td><input id="isCreateCompareList" type="checkbox" /></td>'
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
		reviewObjStr = reviewObjStr +  '<td><select class="reviewSysGroup" id="sysGroup"></select></td>'
        reviewObjStr = reviewObjStr +  '<td><button id="levelWordingsBtn" type="button" onclick="levelWordingsButton(this);"><i class="iconx-add"></i>設定</button><input class="hidden levelWordings" name="levelWordings" /></td>';
		reviewObjStr = reviewObjStr +  '<td><select class="workProject" id="workProject"></select></td>'
		reviewObjStr = reviewObjStr +  '<td><select class="nextLevel"></select></td>'
		reviewObjStr = reviewObjStr +  '<td><select class="backLevel"></select></td>'
		reviewObjStr = reviewObjStr +  '<td><input id="isWorkLevel" type="checkbox" /></td>' 
		
		if(!isCountersign()) {
			reviewObjStr = reviewObjStr + '<td><input id="isCreateJobCIssue" type="checkbox" /></td>'
			reviewObjStr = reviewObjStr + '<td><input id="isWaitForSubIssueFinish" name="isWaitForSubIssueFinishReview" type="radio" /></td>'
		}
		
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
	let parallels = $('input#parallels').val();
	let formType = $("#formType").val();
	let division = $("#division").val();
	let processName = $("#processName").val();
	let applyIsWaitForSubIssueFinishRadio = $("#applyProcessTable").find('[name="isWaitForSubIssueFinishApply"]');
	let reviewIsWaitForSubIssueFinishRadio = $("#reviewProcessTable").find('[name="isWaitForSubIssueFinishReview"]');
	
	if("" == processName) {
		alert("<s:message code='form.process.managment.add.page.function.alert.4' text='請輸入流程名稱' />");//請輸入流程名稱
		return;
	}
	
	if(!applyIsWaitForSubIssueFinishRadio.is(":checked")) {
		alert("<s:message code='form.process.managment.job.page.function.alert.1' text='請選擇等待衍生單關卡關卡' />");//請選擇等待衍生單關卡關卡
		return;
	} 
	
	if(!isCountersign()) {
		if (!reviewIsWaitForSubIssueFinishRadio.is(":checked")) {
			alert("<s:message code='form.process.managment.job.page.function.alert.1' text='請選擇等待衍生單關卡關卡' />");//請選擇等待衍生單關卡關卡
			return;
		}
	} else {
		if (!parallels) {
			alert('平行會辦組別至少選擇一組!');
			return;
		}
	}
	
	$.each(applyProcess,function() {
		let processOrder = 							$(this).find(".applyProcessOrderText").text();//流程順序
		let groupId = 								$(this).find("#sysGroup").val();//流程群組
		let workProject = 							$(this).find("#workProject").val();//工作項目
		let nextLevel = 							$(this).find(".nextLevel").val();//下幾關(最多跳件關數)
		let backLevel = 							$(this).find(".backLevel").val();//退幾關(最多退件關數)
		let isWorkLevel = 							$(this).find("#isWorkLevel").prop("checked");//作業關卡
		let isCreateJobCIssue = 					$(this).find("#isCreateJobCIssue").prop("checked");//可否產生工作會辦單 
		let isWaitForSubIssueFinish = 				$(this).find("#isWaitForSubIssueFinish").is(':checked');//等待衍生單完成
		let isCreateCompareList = 					$(this).find("#isCreateCompareList").prop("checked");//是否產生程式差異清單
		let isModifyColumnData = 					$(this).find("#isModifyColumnData").prop("checked");//可修改欄位資料
		let isCreateSpJobIssue = 					$(this).find("#isCreateSpJobIssue").prop("checked");//可否新增SP工作單 
		let isApprover = 							$(this).find("#isApprover").prop("checked");//是否為審核者
		let isParallel = 							$(this).find("#isParallel").prop("checked");//是否為平行會辦關卡
        let levelWordings =                         $(this).find(".levelWordings").val();//關卡文字
		//processOrder 一定會有值,若沒值的話代表JQuery selector撈到一些奇怪的tr,特別用此法進行排除
		if("" != processOrder) {
			if("" == groupId) {
				let alertStr = "<s:message code='form.process.managment.job.page.function.alert.6' text='申請流程順序 {0} 請選擇關卡' />"
					alertStr = alertStr.replace("{0}",processOrder);
				alert(alertStr);//請選擇流程群組
				verifyResult = false;
				return false;
			}
			
			if("" == workProject && isWorkLevel) {
				let alertStr = "<s:message code='form.process.managment.job.page.function.alert.2' text='申請關卡 {0} 請輸入工作項目' />"
					alertStr = alertStr.replace("{0}",processOrder);
				alert(alertStr);//申請關卡  X 請輸入工作項目
				verifyResult = false;
				return false;
			}
		
			let applyProcessParams = {};
			applyProcessParams.processOrder = processOrder;
			applyProcessParams.groupId = groupId;
			applyProcessParams.workProject = workProject;
			applyProcessParams.nextLevel = nextLevel == null? "0" : nextLevel;
			applyProcessParams.backLevel = backLevel == null? "0" : backLevel;
			applyProcessParams.isWorkLevel = isWorkLevel == true? 'Y' : 'N';
			applyProcessParams.isCreateJobCIssue = isCreateJobCIssue == true? 'Y' : 'N';
			applyProcessParams.isWaitForSubIssueFinish = isWaitForSubIssueFinish == true? 'Y' : 'N';
			applyProcessParams.isCreateCompareList = isCreateCompareList == true? 'Y' : 'N';
			applyProcessParams.isModifyColumnData = isModifyColumnData == true? 'Y' : 'N';
			applyProcessParams.isCreateSpJobIssue = isCreateSpJobIssue == true? 'Y' : 'N';
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
		let workProject = 		 		$(this).find("#workProject").val();//工作項目
		let nextLevel =			 		$(this).find(".nextLevel").val();//下幾關(最多跳件關數)
		let backLevel = 		 		$(this).find(".backLevel").val();//退幾關(最多退件關數)
		let isWorkLevel = 		 		$(this).find("#isWorkLevel").prop("checked");//作業關卡
		let isCreateJobCIssue =  		$(this).find("#isCreateJobCIssue").prop("checked");//可否產生工作會辦單 
		let isModifyColumnData = 		$(this).find("#isModifyColumnData").prop("checked");//可修改欄位資料
		let isCloseForm = 		 		$(this).find("#isCloseForm").prop("checked");//可否直接結案
		let isWaitForSubIssueFinish = 	$(this).find("#isWaitForSubIssueFinish").is(':checked');//等待衍生單完成
		let isApprover = 				$(this).find("#isApprover").prop("checked");//是否為審核者
        let levelWordings =             $(this).find(".levelWordings").val();//關卡文字
		//processOrder 一定會有值,若沒值的話代表JQuery selector撈到一些奇怪的tr,特別用此法進行排除
		if("" != processOrder) {
			if("" == groupId) {
				let alertStr = "<s:message code='form.process.managment.job.page.function.alert.7' text='審核流程順序 {0} 請選擇關卡' />"
					alertStr = alertStr.replace("{0}",processOrder);
				
				alert(alertStr);//請選擇流程群組
				verifyResult = false;
				
				return false;
			}
	
			let reviewProcessParams = {};
			reviewProcessParams.processOrder = processOrder;
			reviewProcessParams.groupId = groupId;
			reviewProcessParams.workProject = workProject;
			reviewProcessParams.nextLevel = nextLevel == null ? "0" : nextLevel;
			reviewProcessParams.backLevel = backLevel == null? "0" : backLevel;
			reviewProcessParams.isWorkLevel = isWorkLevel == true? 'Y' : 'N';
			reviewProcessParams.isCreateJobCIssue = isCreateJobCIssue == true? 'Y' : 'N';
			reviewProcessParams.isModifyColumnData = isModifyColumnData == true? 'Y' : 'N';
			reviewProcessParams.isCloseForm = isCloseForm == true? 'Y' : 'N';
			reviewProcessParams.isWaitForSubIssueFinish = isWaitForSubIssueFinish == true? 'Y' : 'N';
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
	SendUtil.post('/formProcessManagmentJob/updateFormProcess', mainParams, function (data) {
		if(data != undefined || data != null) {
			alert(data.result);
			goBackSearchPage();
		}
	},null,true);
}
</script>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
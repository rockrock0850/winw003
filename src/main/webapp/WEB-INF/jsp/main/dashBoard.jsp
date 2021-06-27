﻿<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
</head>

<script>//# sourceURL=dashBoard.js
var dataTable;
var options = {};
var fecthFakeData = [];

$(function () {
	let isDirect2 = ValidateUtil.loginRole().isDirect2();
	
	if (isDirect2) {
		$("#myButton").text('<s:message code="dashboard.form.checkAll.2" text="整批放行" />');
	} else {
		$("#myButton").text('<s:message code="dashboard.form.checkAll" text="整批審核" />');
	}
	
	HtmlUtil.flushBreadcrumbs();
	HtmlUtil.putBreadcrumb('dashboard', '首頁');
	<c:forEach var='dashboardvo' items='${dashBoardVOs}' varStatus='status'>
		<c:forEach var='vos' items='${dashboardvo.formList}' varStatus='s'>
			fecthFakeData.push(ObjectUtil.parse('${vos}'));
		</c:forEach>
		
		<c:if test='${dashboardvo.htmlId == "kpi"}'>
			var kpiOptions = {};			
			var kpiFecthFakeData = [];
			
			<c:forEach var='kpiGetMap' items='${dashboardvo.kpiList}'>
				<c:forEach var='mapGetLists' items='${kpiGetMap.value}'>
					var kipDatasheet = {}
					
					<c:forEach var='mapGetList' items='${mapGetLists}'>
						kipDatasheet["${mapGetList.key}"] = "${mapGetList.value}";
					</c:forEach>
					
					kpiFecthFakeData.push(kipDatasheet);
				</c:forEach>
				
				kpiOptions = {
					data: kpiFecthFakeData,
					columnDefs: [
						{orderable: false, targets: []},
						{targets: [0], title: '<s:message code="dashboard.form.no" text='表單號碼' />', data: 'orderNumber', className: '', "render": function ( data, type, row, meta ) {
							return '<a href="javascript:toForm(' + '\'' + row.orderNumber + '\'' + ')">'+row.orderNumber+'</a>';
							},"visible": ${status.first}}, 
						{targets: [1], title: '<s:message code="dashboard.form.type" text='表單種類' />', data: 'orderType', className: 'clickable'}, 
						{targets: [2], title: '<s:message code="dashboard.form.sender" text='開單人員' />', data: 'sender', className: 'dt-center clickable'},
						{targets: [3], title: '<s:message code="dashboard.form.deliveryTime" text='送達時間' />', data: 'billingTime', className: 'dt-center clickable'},											
						{targets: [4], title: '<s:message code="dashboard.form.formStatus" text='表單狀態' />', data: 'orderStatus', className: 'clickable'},  
						{targets: [5], title: '<s:message code="dashboard.form.processStatus" text='流程狀態' />', data: 'processStatus', className: 'dt-center clickable'},  
					],
					order: [[ 3, "desc" ]]
				};
				
				TableUtil.init('#a${kpiGetMap.key}dataTable', kpiOptions);
				kpiFecthFakeData = [];								
			</c:forEach>
			
			$('#i2issue').show();
			$('#i3event').show();
			$('#i1demand').show();
			$('#a2issue').hide();
			$('#a3event').hide();
			$('#a1demand').hide();
			$('#kpifieldset').hide();
		</c:if>
		
		<c:if test='${dashboardvo.htmlId == "assignmentWork"}'>
			options = {
				data: fecthFakeData,
				columnDefs: [
					{orderable: false, targets: []},
					{targets: [0], title: '<s:message code="dashboard.form.no" text='表單號碼' />', data: 'orderNumber', className: '', 
						render: function ( data, type, row, meta ) {
							return '<a href="javascript:toForm(' + '\'' + row.orderNumber + '\'' + ')">'+row.orderNumber+'</a>';
						}, visible: true}, 
					{targets: [1], title: '<s:message code="dashboard.form.type" text='表單種類' />', data: 'orderType', className: 'clickable'}, 
					{targets: [2], title: '<s:message code="dashboard.form.formStatus" text='表單狀態' />', data: 'orderStatus', className: 'clickable'}, 
					{targets: [3], title: '<s:message code="dashboard.form.sender" text='開單人員' />', data: 'sender', className: 'dt-center clickable'}, 
					{targets: [4], title: '<s:message code="dashboard.form.deliveryTime" text='送達時間' />', data: 'arrivedTime', className: 'dt-center clickable'}, 
				],
				order: [[ 4, "desc" ]]
			};
		</c:if>
		
		<c:if test='${dashboardvo.htmlId == "checkPending"}'>
			options = {
				data: fecthFakeData,
				columnDefs: [
					{orderable: false, targets: []},
					{targets: [0], "sTitle": '<input type="checkbox" id="checkall" onchange="checkall(this)"/>', data: 'id', className: 'dt-center clickable',"render": function ( data, type, row, meta ) {
						data = !data ? "" : data;
						return "<input type='checkbox' name='id' value='" + data + "' onchange='checkallBoxChangeEvent()'/>";
					},"visible":${dashboardvo.isAllowBatchReview}},
					{targets: [1], title: '<s:message code="dashboard.form.no" text='表單號碼' />', data: 'orderNumber', className: '', "render": function ( data, type, row, meta ) {
						return '<a href="javascript:toForm(' + '\'' + row.orderNumber + '\'' + ')">'+row.orderNumber+'</a>';
					}}, 
					{targets: [2], title: '<s:message code="dashboard.form.type" text='表單種類' />', data: 'orderType', className: 'clickable'}, 
					{targets: [3], title: '<s:message code="dashboard.form.summary" text='表單摘要' />', data: 'summary', className: 'dt-center clickable'}, 
					{targets: [4], title: '<s:message code="dashboard.form.formStatus" text='表單狀態' />', data: 'orderStatus', className: 'clickable'}, 
					{targets: [5], title: '<s:message code="dashboard.form.finishDate" text='預計處理時間' />', data: 'finishDate', className: 'dt-center clickable'},
					{targets: [6], title: '<s:message code="dashboard.form.divisionSolving" text='處理科別' />', data: 'divisionSolving', className: 'dt-center clickable'},
					{targets: [7], title: '<s:message code="dashboard.form.userSolving" text='處理人員' />', data: 'userSolving', className: 'dt-center clickable'},
					{targets: [8], title: '<s:message code="dashboard.form.actualCompDate" text='實際完成時間' />', data: 'actualCompDate', className: 'dt-center clickable'},
					{targets: [9], title: '<s:message code="form.countersigned.form.sct" text='連線系統完成日期'/>', data: 'tct', className: 'dt-center clickable'}
				],
				 order: [[ 5, "desc" ]]
			};
		</c:if>
		
		<c:if test='${dashboardvo.htmlId == "checkPendingApprover"}'>
			options = {
				data: fecthFakeData,
				columnDefs: [
					{orderable: false, targets: []},
					{targets: [0], "sTitle": '<input type="checkbox" id="checkall" onchange="checkall(this)"/>', data: 'id', className: 'dt-center clickable',"render": function ( data, type, row, meta ) {
						data = !data ? "" : data;
						return "<input type='checkbox' name='id' value='" + data + "' onchange='checkallBoxChangeEvent()'/>";
					},"visible":${dashboardvo.isAllowBatchReview}},
					{targets: [1], title: '<s:message code="dashboard.form.no" text='表單號碼' />', data: 'orderNumber', className: '', "render": function ( data, type, row, meta ) {
						return '<a href="javascript:toForm(' + '\'' + row.orderNumber + '\'' + ')">'+row.orderNumber+'</a>';
					}}, 
					{targets: [2], title: '<s:message code="dashboard.form.type" text='表單種類' />', data: 'orderType', className: 'clickable'}, 
					{targets: [3], title: '<s:message code="dashboard.form.summary" text='表單摘要' />', data: 'summary', className: 'dt-center clickable'}, 
					{targets: [4], title: '<s:message code="dashboard.form.formStatus" text='表單狀態' />', data: 'orderStatus', className: 'clickable'}, 
					{targets: [5], title: '<s:message code="dashboard.form.userSolving" text='處理人員' />', data: 'userSolving', className: 'dt-center clickable'}, 
					{targets: [6], title: '<s:message code="dashboard.form.finishDate" text='預計處理時間' />', data: 'finishDate', className: 'dt-center clickable'},
					{targets: [7], title: '<s:message code="dashboard.form.actualCompDate" text='實際完成時間' />', data: 'actualCompDate', className: 'dt-center clickable'},
					{targets: [8], title: '<s:message code="dashboard.form.observation" text='問題單觀察期' />', data: 'observation', className: 'dt-center clickable'},
                    {targets: [9], title: '<s:message code="form.countersigned.form.sct" text='連線系統完成日期'/>', data: 'tct', className: 'dt-center clickable'}
				],
				 order: [[ 5, "desc" ]]
			};
		</c:if>
		
		<c:if test='${dashboardvo.htmlId == "notSent"}'>
			options = {
				data: fecthFakeData,
				columnDefs: [
					{orderable: false, targets: []},
					{targets: [0], title: '<s:message code="dashboard.form.no" text='表單號碼' />', data: 'orderNumber', className: '', "width": "15%", "render": function ( data, type, row, meta ) {
						return '<a href="javascript:toForm(' + '\'' + row.orderNumber + '\'' + ')">'+row.orderNumber+'</a>';
					}}, 
					{targets: [1], title: '<s:message code="dashboard.form.type" text='表單種類' />', data: 'orderType', className: 'clickable', "width": "10%"}, 
					{targets: [2], title: '<s:message code="dashboard.form.formStatus" text='表單狀態' />', data: 'orderStatus', className: 'clickable', "width": "10%"}, 
					{targets: [3], title: '<s:message code="dashboard.form.summary" text='表單摘要' />', data: 'summary', className: 'clickable', "width": "50%"}, 
					{targets: [4], title: '<s:message code="dashboard.form.billingTime" text='開單時間' />', data: 'billingTime', className: 'dt-center clickable', "width": "15%"}, 
				],
				order: [[ 4, "desc" ]]
			};
		</c:if>
		
		<c:if test='${dashboardvo.htmlId == "unfinished"}'>
			options = {
				data: fecthFakeData,
				columnDefs: [
					{orderable: false, targets: []},
					{targets: [0], title: '<s:message code="dashboard.form.no" text='表單號碼' />', data: 'orderNumber', className: '', "render": function ( data, type, row, meta ) {
						return '<a href="javascript:toForm(' + '\'' + row.orderNumber + '\'' + ')">'+row.orderNumber+'</a>';
					}}, 
					{targets: [1], title: '<s:message code="dashboard.form.type" text='表單種類' />', data: 'orderType', className: 'clickable'}, 
					{targets: [2], title: '<s:message code="dashboard.form.formStatus" text='表單狀態' />', data: 'orderStatus', className: 'clickable'},
					{targets: [3], title: '<s:message code="dashboard.form.summary" text='表單摘要' />', data: 'summary', className: 'clickable'}, 
					{targets: [4], title: '<s:message code="dashboard.form.sender" text='開單人員' />', data: 'sender', className: 'dt-center clickable'}, 
					{targets: [5], title: '<s:message code="dashboard.form.deliveryTime" text='送達時間' />', data: 'arrivedTime', className: 'dt-center clickable',"visible": true} 
				],
				order: [[ 5, "desc" ]]
			};
		</c:if>		
		
		initTable('${dashboardvo.htmlId}', options);
		fecthFakeData = [];
	</c:forEach>
	
	visbleTableCtrl();
});

function initTable (htmlId, options) {
	let table = '#' + htmlId + 'dataTable';
	
	if (htmlId == 'checkPendingApprover') {
		dataTable = TableUtil.init(table, options);
	} else {
		TableUtil.init(table, options);	
	}
	
	HtmlUtil.clickRowCheckboxEvent(table + ' tbody');
}

function checkall (checkallBox) {
	var checkboxs = '#checkPendingApprover input[name="id"]';
	TableUtil.checkall(checkallBox, checkboxs);
}

function checkallBoxChangeEvent () {
	var checkallBox = 'input#checkall';
	var checkboxs = '#checkPendingApprover input[name="id"]';
	TableUtil.checkallChangeEvent(checkallBox, checkboxs);
}

function approvalBatch () {
	let formData, isChg;
	var message = validation();
	let isChief = ValidateUtil.loginRole().isChief();
	let isDirect2 = ValidateUtil.loginRole().isDirect2();
	
	if (message) {
		alert(message);
		return;
	}
	
	var userConfirm = false;
	var message = "";
	
	if (isDirect2) {
		message = "整批放行成功。"
		userConfirm = confirm('<s:message code="dashboard.form.checkOnlyAgree.2" text="整批放行只可同意,是否確定?" />');
	} else {
		message = "整批審核成功。";
		userConfirm = confirm('<s:message code="dashboard.form.checkOnlyAgree" text="整批審核只可同意,是否確定?" />')
	}
	
	if (userConfirm) {
		var approvalList = [];
		
		$.each($('#checkPendingApprover input[name="id"]'), function () {
			if (!$(this).is(':checked')) {
				return true;// continue;
			}

			formData = TableUtil.getRow(dataTable, this);
			isChg = ValidateUtil.formInfo().isChg(formData);

			if (isChg && isChief && 
					isJumpReviewLast(formData)) {
				toReviewTargetLevel(formData, '');
			} else {
				approvalList.push(formData);
			}
		});
		
		SendUtil.post('/dashboard/approval', approvalList, function (data) {
			if (data.isLogicError) {
				return;
			}
			
	    	alert(message);
			SendUtil.reload();
		});
	}	
}

/*
 *  變更單的特殊邏輯
 *  變更單申請第2關固定為副科長；第3關固定為科長。
 *  當科長進行審核的時候系統需要判斷是否達到風險標準(以下簡稱達標/未達標)
 *  達標 : 系統需須直接進入審核第1關(固定為副理)
 *  未達標 : 系統需自動將關卡推進到審核最後1關(固定為經辦)
 */
function isJumpReviewLast (request) {
	let jump = false;
	
	SendUtil.post('/changeForm/info', request, function (response) {
		let warningWording;
		let isNewSystem = response.isNewSystem == 'Y';
		let isNewService = response.isNewService == 'Y';
		let isFromCountersign = response.isFromCountersign;
		let totalFraction = parseInt(response.totalFraction);
		let isReviewProcess = "REVIEW" == request.verifyType;
		let isImportantCChange = isNewSystem || isNewService;
		let impactThreshold = parseInt(response.impactThreshold);
		let isBusinessImpactAnalysis = response.isBusinessImpactAnalysis == 'Y'; // 是否代理審核衝擊分析最後一關的資訊
		let isImportantChange = isNewSystem || isNewService || (totalFraction >= impactThreshold);
						
		// 判斷該關卡是否有勾選衝擊分析判斷選項
		if (!isFromCountersign && !isReviewProcess && 
				isBusinessImpactAnalysis && !totalFraction) {
			return jump;
		}
		
		if (isFromCountersign && !isImportantCChange) {
			jump = true;
		}

		if (!isFromCountersign && !isImportantChange) {
			jump = true;
		}
	}, {async:false});
	
	return jump;
}

// 跳至審核指定關卡
function toReviewTargetLevel (request, jumpToReviewLevel) {
	request.userSolving = request.userId;
	request.jumpLevel = jumpToReviewLevel;
	SendUtil.post("/changeForm/jumpToReviewTargetLevel", request);
}

function validation() {
	var message = '';
	if (!$('input[type="checkbox"]').is(':checked')) {
		message = '<s:message code="dashboard.form.send" text="請至少勾選一項要覆核的項目。" />';
	}
	return message;
}
	
function showHideKPI(object1,object2,object3,object4,object5,callback) {
	object4.show();
	object5.show();
	var enter='';
	if(object1[0].style.display == 'block'){
		enter = 'block';
	}else{
		enter = 'none';
	}

	object1.toggle();

	window.setTimeout(function(){
		// 如果 callback 是個函式就呼叫它
		if( typeof callback === 'function' ){
		   callback(object1,object2,object3,object4,object5);
		}
	}, 1000);
}

function showHideFieldset(object1,object2,object3,object4,object5){
	if((object1[0].style.display == '' || object1[0].style.display == 'block') || 
			(object2[0].style.display == '' || object2[0].style.display == 'block') || 
				(object3[0].style.display == '' || object3[0].style.display == 'block') ){
			object4.show();
			object5.show();
		}else{
			object4.hide();
			object5.hide();
	}		
}

function showHide(object) {
	object.toggle();
}

function visbleTableCtrl() {
	let buttons = $("fieldset#buttonArea").find("button");
	$.each(buttons,function() {	
		$(this).click();
	});
}

</script>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1>首頁</h1>
	<c:forEach var="dashboardvo" items="${dashBoardVOs}" varStatus="status">
		<c:if test="${status.first}">
			<c:if test="${dashboardvo.htmlId == 'kpi'}">
				<fieldset id="kpiFieldsetMain">
					<legend style="font-size:26px;"><s:message code="dashboard.kpi.statistics" text='KPI統計狀況' /></legend>
						<table class="grid_list" style="width: 50%;">
							<tr>
								<th width="25%"></th>
								<th width="25%" style="font-size:26px;"><s:message code="requirement.form" text='需求單' /></th>
								<th width="25%" style="font-size:26px;"><s:message code="question.form" text='問題單' /></th>
								<th width="25%" style="font-size:26px;"><s:message code="event.form" text='事件單' /></th>
							</tr>
							<tr>
								<td align="center" style="font-size:26px;"><s:message code="dashboard.order.unfinished" text='未結案' /></td>
								<td align="center" style="font-size:26px;">
									<c:if test="${dashboardvo.actualDemand > dashboardvo.kpiDemand}">
										<a href="###"
											onclick="showHideKPI($('#a1demand'),$('#a2issue'),$('#a3event'),$('#kpifieldset'),$('#kpilegend'),showHideFieldset);" style="color:red;">${dashboardvo.actualDemand}</a>
									</c:if>
									<c:if test="${dashboardvo.actualDemand <= dashboardvo.kpiDemand}">
										<a href="###"
										onclick="showHideKPI($('#a1demand'),$('#a2issue'),$('#a3event'),$('#kpifieldset'),$('#kpilegend'),showHideFieldset);">${dashboardvo.actualDemand}</a>
									</c:if>							
								</td>				
								<td align="center" style="font-size:26px;">
									<c:if test="${dashboardvo.actualIssue > dashboardvo.kpiIssue}">
										<a href="###" 
										onclick="showHideKPI($('#a2issue'),$('#a1demand'),$('#a3event'),$('#kpifieldset'),$('#kpilegend'),showHideFieldset);" style="color:red;">${dashboardvo.actualIssue}</a>
									</c:if>
									<c:if test="${dashboardvo.actualIssue <= dashboardvo.kpiIssue}">
										<a href="###" 
										onclick="showHideKPI($('#a2issue'),$('#a1demand'),$('#a3event'),$('#kpifieldset'),$('#kpilegend'),showHideFieldset);">${dashboardvo.actualIssue}</a>
									</c:if>							
								</td>							
								<td align="center" style="font-size:26px;">
									<c:if test="${dashboardvo.actualEvent > dashboardvo.kpiEvent}">
										<a href="###" 
										onclick="showHideKPI($('#a3event'),$('#a1demand'),$('#a2issue'),$('#kpifieldset'),$('#kpilegend'),showHideFieldset);" style="color:red;">${dashboardvo.actualEvent}</a>
									</c:if>
									<c:if test="${dashboardvo.actualEvent <= dashboardvo.kpiEvent}">
										<a href="###" 
										onclick="showHideKPI($('#a3event'),$('#a1demand'),$('#a2issue'),$('#kpifieldset'),$('#kpilegend'),showHideFieldset);" >${dashboardvo.actualEvent}</a>
									</c:if>							
								</td>
							</tr>
							<tr>
								<td align="center" style="font-size:26px;"><s:message code="dashboard.order.KPItarget" text='KPI目標' /></td>
								<td align="center" style="font-size:26px;">${dashboardvo.kpiDemand}</td>
								<td align="center" style="font-size:26px;">${dashboardvo.kpiIssue}</td>
								<td align="center" style="font-size:26px;">${dashboardvo.kpiEvent}</td>
							</tr>
					</table>
				</fieldset>	
				
				<fieldset id="kpifieldset" style="display: block;width: 100%;">
					<legend id="kpilegend" style="display: none;"><s:message code="dashboard.kpi.unfinishedList" text='KPI統計-未結案清單' /></legend>
					<c:forEach var='kpiGetMap' items='${dashboardvo.kpiList}' varStatus='vs'>
					  <div id="a${kpiGetMap.key}" style="display:block">
					  <legend id="i${kpiGetMap.key}" style="display: none;margin-bottom：30px;"><s:message code="dashboard.kpi.unfinishedList.${kpiGetMap.key}" text='KPI統計-未結案清單' /></legend><br/>
						<table id="a${kpiGetMap.key}dataTable" class="display collapse cell-border">
							<thead></thead>
							<tbody></tbody>
						</table>
					 </div>					
					</c:forEach>
				</fieldset>		
			</c:if>
		</c:if>
		
		<c:if test="${status.first}">
			<fieldset id="buttonArea">
				<legend style="font-size: 20px"><s:message code="dashboard.case.overview" text='案件數總覽' /></legend>
		</c:if>
		
		<c:if test="${dashboardvo.htmlId != 'kpi'}">
			<c:if test="${dashboardvo.count > 0 || 
							dashboardvo.authType =='0' || 
							dashboardvo.htmlId == 'checkPending' || 
							dashboardvo.htmlId == 'checkPendingApprover'}">		
				<button style="font-size: 30px" onclick="showHide($('#${dashboardvo.htmlId}div'));" title='<s:message code="dashboard.message.${dashboardvo.htmlId}" text='提示訊息' />'>
					<s:message code="dashboard.button.${dashboardvo.htmlId}" text='功能名稱' /><br />${dashboardvo.count}
				</button>
				<c:if test="${!status.last}">&nbsp;&nbsp;</c:if>
			</c:if>
		</c:if>
		
		<c:if test="${status.last}">
			</fieldset>	
		</c:if>
	</c:forEach>
	
	<c:forEach var="dashboardvo" items="${dashBoardVOs}" varStatus="status">
		<c:if test="${dashboardvo.htmlId != 'kpi'}">
			<c:if test="${dashboardvo.count > 0 || 
							dashboardvo.authType =='0' || 
							dashboardvo.htmlId == 'checkPending' || 
							dashboardvo.htmlId == 'checkPendingApprover'}">
				<div id="${dashboardvo.htmlId}div">
					<fieldset id="${dashboardvo.htmlId}">
						<legend><s:message code="dashboard.button.${dashboardvo.htmlId}" text='功能名稱' /></legend>			
						<c:if test="${dashboardvo.htmlId =='assignmentWork'}">
							<div style='margin:3px'><s:message code="dashboard.message.nextTime" text='下一個指定到期日' />:${dashboardvo.nextExpiration}</div>
						</c:if>
						<c:if test="${dashboardvo.htmlId =='checkPendingApprover' && dashboardvo.isAllowBatchReview}">
							<div class="grid_BtnBar">
								<button onclick="approvalBatch()" id="myButton">
									<i class="iconx-save"></i><s:message code="dashboard.form.checkAll" text='整批審核' />           
								</button>
							</div>
						</c:if>
						<table id="${dashboardvo.htmlId}dataTable" class="display collapse cell-border">
							<thead></thead>
							<tbody></tbody>
						</table>
					</fieldset>
				</div>
			</c:if>
		</c:if>
	</c:forEach>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
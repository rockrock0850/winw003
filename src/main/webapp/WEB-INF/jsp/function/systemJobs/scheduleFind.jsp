<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>

<script>
var dataTable;
var ajaxSetting = {async:false};

$(function(){
	initTable();
	SessionUtil.fetch('scheduleJob', function (record) {
		search(record);
	});
});

function initTable () {
	var options = {
		data: [],
		columnDefs: [
			{orderable: false, targets: []}, 
			{targets: [0], title: '', data: 'jobName', className: 'hidden'}, 
			{targets: [1], title: '', data: 'jobClass', className: 'hidden'}, 
			{targets: [2], title: '', data: 'jobGroup', className: 'hidden'}, 
			{
				targets: [3], 
				data: null, 
				title: "功能", 
				className: '', 
				defaultContent: 
					'<button onclick="gotoEditPage(this);"><i class="iconx-edit"></i>編輯</button>' + 
					'<button onclick="executeJob(this);"><i class="iconx-edit"></i>執行</button>' + 
					'<button onclick="alertHistory(this);"><i class="iconx-edit"></i>歷程</button>'
			},
			{targets: [4], title: '排程名稱', data: 'jobDescription', className: 'dt-center'}, 
			{targets: [5], title: '上次執行時間', data: 'lastFireTime', className: 'dt-center', 
				render: function (data) {
					return DateUtil.toDateTime(data);
				}},  
			{targets: [6], title: '下次執行時間', data: 'nextFireTime', className: 'dt-center',
				render : function (data) {
					return DateUtil.toDateTime(data);
			}}, 
			{targets: [7], title: '排程執行頻率(間隔/單位)', data: 'timeUnit', className: 'dt-center', 
				render: function (data, type, row, mata) {
					var interval = '';
					if (parseInt(row.repeatInterval) <= 0) {
						interval = fromTimeUnit(row.timeUnit);
					} else {
						interval = row.repeatInterval + '/' + fromTimeUnit(row.timeUnit);
					}
					
					return interval;
			}},
			{targets: [8], title: '執行狀態', data: 'exeStatus', className: 'dt-center', 
				render: function (data) {
					return fromStatus(data);
				}}, 
			{targets: [9], title: '使用狀態', data: 'status', className: 'dt-center', 
				render: function (data) {
					return fromStatus(data);
			}}
		]
	};
	dataTable = TableUtil.init('#dataTable', options);
}

function search (record) {
	var conditions = record ? record : form2object('search');
	SendUtil.post('/schedule/search', conditions, function (resData) {
		TableUtil.reDraw(dataTable, resData);
		SessionUtil.record('scheduleJob', conditions);
	}, ajaxSetting);
}

function gotoEditPage (button) {
	var data = TableUtil.getRow(dataTable, button);
	SendUtil.href('/schedule/editPage', data.jobName);
}

function executeJob (button) {
	if (!confirm('是否確定執行?')) {
		return;
	}
	
	var data = TableUtil.getRow(dataTable, button);
	data.timeUnit = 0;
	data.executeTimes = 1;
	data.repeatInterval = 0;
	data.startTime = DateUtil.addSeconds($.now(), 30);
	data.startTime = DateUtil.fromDate(data.startTime);
	
	SendUtil.post('/schedule/once', data, function (resData) {
		if (2 == resData.status) {
			return;
		}
		
		alert('排程「' + resData.jobDescription + '」正在執行...');
	});
}

function alertHistory (button) {
	var data = TableUtil.getRow(dataTable, button);
	SendUtil.post('/schedule/history', data, function (resData) {
		var options = {
			title: '<s:message code="schedule.find.legend.name" text="排程名稱" />',
			name: resData.jobDescription,
			data: resData.scheduleJobVOList,
			columnDefs: [
				{orderable: false, targets: []}, 
				{targets: [0], title: '<s:message code="schedule.history.label.start.time" text="開始時間" />', data: 'startTime', className: 'dt-center', width: '20%',
					render: function (data) {
						return DateUtil.toDateTime(data);
					}}, 
				{targets: [1], title: '<s:message code="schedule.history.label.end.time" text="結束時間" />', data: 'endTime', className: 'dt-center',  width: '20%',
					render: function (data) {
							return DateUtil.toDateTime(data);
						}}, 
				{targets: [2], title: '<s:message code="schedule.history.label.result" text="結果" />', data: 'status', className: 'dt-center',  width: '20%',
					render: function (data) {
						return fromStatus(data);
				}}, 
				{targets: [3], title: '<s:message code="schedule.history.label.remark" text="備註" />', data: 'message', className: 'dt-left', width: '40%',
					render: function (data) {
					if(data && data.length > 100){
						data = data.substring(0, 100) + "...";
					}
					
					return data;
				}}
			]
		};
		HistoryDialog.show(options);
	}, null, true);
}

// 排程執行狀態碼轉排程執行狀態文字
var fromStatus = function (status) {
	if (!status) {
		return '';
	}
	
	var wording;
	
	if (typeof status === 'string') {
		status = parseInt(status);
	}
	
	switch (status) {
		case 0 : 
			wording = '已刪除';
			break;
		case 1 : 
			wording = '已啟用';
			break;
		case 2 : 
			wording = '已停止';
			break;
		case 3 : 
			wording = '重新開始';
			break;
		case 4 : 
			wording = '產生中';
			break;
		case 5 : 
			wording = '執行中';
			break;
		case 6 : 
			wording = '發生錯誤';
			break;
		case 7 : 
			wording = '已完成';
			break;
	}
	
	return wording;
}

// 排程時間識別碼轉排程識別碼文字
var fromTimeUnit = function (unit) {
	var wording;
	
	if (typeof unit === 'string') {
		unit = parseInt(unit);
	}
	
	switch (unit) {
		case 0 : 
			wording = '一次';
			break;
		case 1 : 
			wording = '秒';
			break;
		case 2 : 
			wording = '分';
			break;
		case 3 : 
			wording = '時';
			break;
		case 4 : 
			wording = '天';
			break;
		case 5 : 
			wording = '週';
			break;
		case 6 : 
			wording = '月';
			break;
		case 7 : 
			wording = '年';
			break;
		default: 
			wording = '一次';
			break;
	}
	
	return wording;
}
</script>
</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="schedule.find.h1.title" text='系統批次設定' /></h1>

	<fieldset class="search">
		<legend><s:message code="schedule.find.legend.name" text='排程名稱' /></legend>

		<button class="small fieldControl searchPanel">
			<i class="iconx-collapse"></i>|||
		</button>

		<form id='search'>
			<table class="grid_query">
				<tr>
					<th>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<s:message code="schedule.find.th.search" text='查詢條件' />
					</th>
					<td>
						<input type="text" name='jobDescription' style="width: 20rem;" />
					</td>
					<td>
						<button type='button' onclick='search()'>
							<i class="iconx-search"></i>
							<s:message code="schedule.find.button.search" text='查詢' />
						</button>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
   
	<fieldset>
		<legend><s:message code="schedule.find.legend.result" text='查詢結果' /></legend>
		<table id="dataTable" class="display collapse cell-border">
			<thead></thead>
			<tbody></tbody>
		</table>
	</fieldset>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>

<script>
var scheduleVO = ObjectUtil.parse('${scheduleVO}');
var uriSchedule = '/schedule';
var uriSave = '/schedule/save';

$(function(){
	DateUtil.datePicker();
	initForm();
	registerListeners();
});

function initForm () {
	$('input#jobName').val(scheduleVO.jobName);
	$('input#jobDescription').val(scheduleVO.jobDescription);
	$('input#jobClass').val(scheduleVO.jobClass);
	$('input#jobGroup').val(scheduleVO.jobGroup);
	
	if (scheduleVO.timeUnit <= 0) {
		$('input#once').prop('checked', true);
		$('input#repeatInterval').prop('disabled', true);
		$('select#timeUnit').prop('disabled', true);
	} else {
		$('input#every').prop('checked', true);
		$('input#repeatInterval').val(scheduleVO.repeatInterval);
		$('select#timeUnit').val(!scheduleVO.timeUnit ? 0 : scheduleVO.timeUnit);
	}

	$('input#startTime').val(DateUtil.toDateTime(scheduleVO.startTime));
	$('input#nextFireTime').val(scheduleVO.nextFireTime);
	
	var dateTime = DateUtil.toDateTime(scheduleVO.nextFireTime);
	if (dateTime) {
		dateTime = dateTime.split(' ');
		$('input#date').val(dateTime[0]);
		
		dateTime = dateTime[1].split(':');
		$('input#hours').val(dateTime[0]);
		$('input#minutes').val(dateTime[1]);
		$('input#seconds').val(dateTime[2]);
	}
	
	if (scheduleVO.status == 1) {
		$('input#enabled').prop('checked', true);
	} else {
		$('input#disabled').prop('checked', true);
	}
	
	$('input#modifier').val(scheduleVO.updatedBy);
	$('input#modifyTime').val(DateUtil.toDateTime(scheduleVO.updatedAt));
}

function save () {
	if ($('input#enabled').is(':checked')) {
		if (!isIntervalSelected()) {
			return;
		}
		
		if (!isNextExeTimeSelected()) {
			return;
		}
	}
	
	if (!confirm('是否確定儲存？')) {
		return;
	}
	
	var isOnce = $('input#once').is(':checked');
	var data = form2object('dataForm');
	data.status = $('input#enabled').is(':checked') ? 1 : 2;
	data.timeUnit = isOnce ? 0 : parseInt(data.timeUnit);
	data.executeTimes =  isOnce ? 1 : -1;
	data.repeatInterval =  isOnce ? 0 : data.repeatInterval;
	data.nextFireTime = 
		data.date + ' ' + data.hours + ':' + data.minutes + ':' + data.seconds;
	data.nextFireTime = DateUtil.fromDate(data.nextFireTime);
	
	SendUtil.post(uriSave, data, function (resData) {
		alert('更新資料成功。');
		SendUtil.href(uriSchedule);
	});
}

function isIntervalSelected () {
	var isEverySelected = $('input#every').is(':checked');
	
	if (!isEverySelected) {
		return true;
	}
	
	isEverySelected = $('input#repeatInterval').val();
	
	if (!isEverySelected) {
		alert('<s:message code="schedule.edit.field.valid.1" text="請輸入「每次執行時間」。" />');
		return false;
	}
	
	if (isEverySelected <= 0) {
		alert('<s:message code="schedule.edit.field.valid.2" text="「每次執行時間」不可小於或等於零。" />');
		return false;
	}
	
	isEverySelected = $('select#timeUnit').val();
	
	if (isEverySelected == 0) {
		alert('<s:message code="schedule.edit.field.valid.3" text="請選擇每次執行時間「單位」。" />');
		return false;
	}
	
	return true;
}

function isNextExeTimeSelected () {
	var date = $('input#date').val();
	var hours = $('input#hours').val();
	var minutes = $('input#minutes').val();
	var seconds = $('input#seconds').val();

	if (!date || !hours || !minutes || !seconds) {
		alert('<s:message code="schedule.edit.field.valid.4" text="請輸入「排程下次執行時間」。" />');
		return false;
	}

	if (hours < 0 || minutes < 0 || seconds < 0) {
		alert('<s:message code="schedule.edit.field.valid.5" text="「排程下次執行時間」不可小於零。" />');
		return false;
	}
	
	return true;
}

function registerListeners () {
	$('input[name="freqency"]').change(function () {
		var id = $(this).attr('id');
		var isDisabled = (id == 'once');
		
		$('input#repeatInterval').prop('disabled', isDisabled);
		$('select#timeUnit').prop('disabled', isDisabled);
	});
}
</script>
</head>
<body>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1><s:message code="schedule.edit.h1.title" text='編輯系統批次' /></h1>
	
	<fieldset>
		<legend>
			<s:message code="schedule.edit.legend.name" text='系統批次' />
		</legend>
		
		<form id='dataForm'>
			<input id='jobName' type='hidden' name='jobName' />
			<input id='jobGroup' type='hidden' name='jobGroup' />
			<input id='jobClass' type='hidden' name='jobClass' />
			<input id='nextFireTime' type='hidden' name='nextFireTime' />
			
			<table class="grid_query">
				<tr>
					<th width="15%">
						<font color="red">&nbsp;&nbsp;&nbsp;&nbsp;</font>
						<s:message code="schedule.edit.th.name" text='排程名稱' />
					</th>
					<td width="45%">
						<input id='jobDescription' type="text" class='custom-label' name='jobDescription' readonly />
					</td>
				</tr>
				<tr>
					<th width="12%">
						<font color="red">&nbsp;*&nbsp;</font>
						<s:message code="schedule.edit.th.interval" text='排程執行頻率' />
					</th>
					<td>
						<label>
							<input id='once' type="radio" name='freqency' />
							<s:message code="schedule.edit.label.once" text='一次' />
						</label>
						<label>
							<input id='every' type="radio" name='freqency' />
							<s:message code="schedule.edit.label.every" text='每' />&nbsp;
						</label>
						<input id='repeatInterval' type="text" name='repeatInterval' style="width: 1rem;" /> 
						<select id='timeUnit' name='timeUnit'>
							<option value='0'>&nbsp;請選擇</option>
							<option value='3'>&nbsp;時</option>
							<option value='4'>&nbsp;天</option>
							<option value='5'>&nbsp;週</option>
							<option value='6'>&nbsp;月</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>
						<font color="red">&nbsp;&nbsp;&nbsp;&nbsp;</font>
						<s:message code="schedule.edit.th.first" text='排程首次執行時間' />
					</th>
					<td><input id='startTime' type="text" class='custom-label' readonly /></td>
				</tr>
				<tr>
					<th>
						<font color="red">&nbsp;*&nbsp;</font>
						<s:message code="schedule.edit.th.next" text='排程下次執行時間' />
					</th>
					<td class="button">
						<input id='date' type="text" class='initDatePicker' name='date' style="width: 8rem;" readonly />
						<input id='hours' type="number" name='hours' style="width: 2rem;" /> 時 
						<input id='minutes' type="number" name='minutes' style="width: 2rem;" /> 分 
						<input id='seconds' type="number" name='seconds' style="width: 2rem;" /> 秒
					</td>
				</tr>
				<tr>
					<th>
						<font color="red">&nbsp;*&nbsp;</font>
						<s:message code="schedule.edit.th.exestatus" text='使用狀態' />
					</th>
					<td>
						<label>
							<input id='enabled' type="radio" name='status' checked />
							<s:message code="schedule.edit.label.enable" text='啟用' />
						</label>
						<label>
							<input id='disabled' type="radio" name='status' />
							<s:message code="schedule.edit.label.disable" text='停用' />
						</label>
					</td>
				</tr>
				<tr>
					<th>
						<font color="red">&nbsp;&nbsp;&nbsp;&nbsp;</font>
						<s:message code="schedule.edit.th.modifier" text='修改人員' />
					</th>
					<td><input id='modifier' type="text" class='custom-label' readonly /></td>
				</tr>
				<tr>
					<th>
						<font color="red">&nbsp;&nbsp;&nbsp;&nbsp;</font>
						<s:message code="schedule.edit.th.modifyTime" text='修改時間' />
					</th>
					<td><input id='modifyTime' type="text" class='custom-label' readonly /></td>
				</tr>
			</table>
		</form>
	</fieldset>

	<div class="btnBR">
        <button onclick='save()'>
        	<i class="iconx-save"></i>
        	<s:message code="schedule.edit.button.save" text='儲存' />
       	</button>
        <button onclick='back()'>
        	<i class="iconx-back"></i>
        	<s:message code="schedule.edit.button.back" text='回前頁' />
       	</button>
    </div>
<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>	
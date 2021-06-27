<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/global.jsp"%>
<script>
var dataTable;

$(function () {
	initView();
	initEvent();
	$('input#startDate').val(new Date().getFullYear());
	SendUtil.post('/holidayImport/search', {}, function (resData) {
		TableUtil.reDraw(dataTable, resData);
	}, null, true);
});

function initTable () {
	var options = {
		data: [],
		columnDefs: [
			{orderable: false, targets: []}, 
			{targets: [0], title: '年份', data: 'year', className: 'dt-center', width: '5%'},
			{targets: [1], title: '檔案名稱', data: 'fileName', className: 'dt-center', width: '5%'}, 
			{
				targets: [2], 
				data: null, 
				width: '5%',
				title: "功能", 
				className: 'dt-center', 
				defaultContent: '<button onclick="viewList(this);"><i class="iconx-edit"></i>檢視</button>'
			}
		]
	};
	dataTable = TableUtil.init('#dataTable', options);
}

function viewList (btn) {
	let row = TableUtil.getRow(dataTable, btn);
	let reqData = {
		isView : true,
		year : row.year
	};
	SendUtil.post('/holidayImport/search', reqData, function (resData) {
		let options = {
			data: resData,
			columnDefs: [
				{orderable: false, targets: []}, 
				{targets: [0], title: '節日名稱', data: 'name', className: 'dt-center'}, 
				{targets: [1], title: '日期', data: 'date', className: 'dt-center'}
			]
		};
		HolidayDialog.show(options);
	}, null, true);
}

function initView () {
	var today = new Date();
	var startOptions = {
       	dateFormat: 'yy',
       	onClose: function(dateText, inst) { 
           	var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
           	var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
           	startOptions.defaultDate = new Date(year, month, 1);
			DateUtil.datePickerById('#startDate', startOptions);
           	$(this).datepicker('setDate', startOptions.defaultDate);
		},
		minDate: today,
		maxDate: DateUtil.addYears(today, 1)
	};	
	DateUtil.datePickerById('#startDate', startOptions);

	initTable();
}

function initEvent () {
	$('input[type="file"]').change(function () {
		read(this);
	});
}

function uploadHoliday () {
	let formData = FileUtil.getFormFile();
	let startDate = $('input#startDate').val();
	
	if (!startDate) {
		alert('請選擇年分。');
		return;
	}
	
	if ($.isEmptyObject(formData)) {
		alert('<s:message code="form.common.file.empty.file" text="請選擇檔案" />');
		return;
	}
	
	if (confirm('<s:message code="form.common.file.upload.confirm" text="確認上傳?" />')) {
		formData.append('year', startDate);
		FileUtil.upload('/holidayImport/importHoliday', formData, function (res) {
			if (res.returnMsg) {
				alert(res.returnMsg);
			} else { 
				if (res.dataList) {
					TableUtil.reDraw(dataTable, res.dataList);
				}
				alert("<s:message code='file.tab.alert.message.4' text='上傳成功' />");
			}
			
			$('input[type="file"]').val('');
		});
	}
}

function read (input) {
	if (!input.files[0]) {
		FileUtil.reset();
		return;
	}
	
	try {
		FileUtil.readAsFile('file', input.files[0]);
	} catch (e) {
		$(input).val('');
	}
}
</script>

<style>
  .ui-datepicker-prev,
  .ui-datepicker-next,
  .ui-datepicker-month,
  .ui-datepicker-calendar {
   		display: none;
   }
</style>

</head>
<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>	
	<h1>匯入假日資料</h1>

	<fieldset class="search">
		<button class="small fieldControl searchPanel">
			<i class="iconx-collapse"></i>|||
		</button>
		<form>
			<table class="grid_query">
				<tr>
					<th>&nbsp;&nbsp;&nbsp;&nbsp;年份</th>
					<td>
						<c:import url="/WEB-INF/jsp/common/models/dateModel.jsp">
		    				<c:param name="id1" value="startDate" />
							<c:param name="name1" value="startDate" />
		    				<c:param name="removeTdTag" value="true" />
						</c:import>
					</td>
					<th><font color="red">*&nbsp;</font>檔案(.txt)</th>
					<td>
						<input id='fileHoliday' type="file">
						<button id="importHoliday" type='button' onclick='uploadHoliday();' >
							<i class="iconx-export"></i><fmt:message key="button.import"/>
						</button>
					</td>
				</tr>
				<tr>
					<th>&nbsp;&nbsp;&nbsp;&nbsp;範例</th>
					<td>
				       	<a href='${pageContext.request.contextPath}/static/attachment/holidayExample.txt' target='_blank'>
				       		<font style="color:blue;">檔案範本</font>
				       	</a>
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
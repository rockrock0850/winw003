<%@ page contentType="text/html; charset=UTF-8" %>

<div id='internalProcessDialog' style='display: none'>
	<fieldset>
		<legend>會辦項目處理進度</legend>
		<div class="grid_BtnBar">
			<button type="button" id="finished" onclick="InternalProcessDialog.finished();"><i class="icon-ok"></i>結束內會</button>
			<button type="button" id="submit" onclick="InternalProcessDialog.submit();"><i class="icon-ok"></i>送出內會</button>
		</div>
		<table id="solvingList" class="grid_list"></table>
	</fieldset>
</div>

<script>//# sourceURL=InternalProcessDialog.js
/**
 * 開啟「選擇內會單位」對話框
 */
var InternalProcessDialog = function () {

	const dialog = "div#internalProcessDialog";
	const table = dialog + " " + "table#solvingList";
	const options = {
			width: '300px',
			maxWidth: '300px',
	}
	var info;
	var internalProcessItems;
	
	var show = function (info_) {
		internalProcessItems = {};
		info = info_;
		let formId = info.formId;
		SendUtil.get('/html/getInternalProcessStatus', formId, function (response) {
			$(table).empty();
			
			let htmlBuilder = new Array();
			htmlBuilder.push("<thead>");
			htmlBuilder.push(	"<tr>");
			htmlBuilder.push(		"<th></th>");
			htmlBuilder.push(		"<th>會辦項目</th>");
			htmlBuilder.push(		"<th>已處理</th>");
			htmlBuilder.push(	"</tr>");
			htmlBuilder.push("</thead>");
			
			htmlBuilder.push("<tbody>");
			let checked = " checked";
			let index = 0;
			let obj;
			$.each(response, function () {
				let clazz = ++index % 2 == 0 ? "odd" : "even";
				
				htmlBuilder.push("<tr class='"+clazz+"' onclick='InternalProcessDialog.radioEvent("+index+")'>");
				
				htmlBuilder.push("<td style='text-align:center;'>");
				let item = '<input type="radio" id="internalProcessDialogItem'+index+'" name="internalProcessDialogItem" value="'+this.value+'"'+checked+'/>';
				htmlBuilder.push(item);
				htmlBuilder.push("</td>");
				
				htmlBuilder.push("<td>");
				htmlBuilder.push(this.wording);
				htmlBuilder.push("</td>");
				
				htmlBuilder.push("<td style='text-align:center;'>");
				htmlBuilder.push(this.display);
				htmlBuilder.push("</td>");
				
				htmlBuilder.push("</tr>");
				
				checked = "";
				
				obj = {
					name: this.wording,
					value: this.value, 
					index: index
				};
				internalProcessItems[obj.value] = obj;
			});
			htmlBuilder.push("</tbody>");
			$(table).append(htmlBuilder.join(""));
			
			
		}, null, true);

		DialogUtil.show(dialog, options);
	}
	
	var radioEvent = function(index) {
		$("input#internalProcessDialogItem"+index).prop("checked", true);
	}
	
	// 送出內會
	var submit = function() {
		let currentProcess = $("#internalProcessDialog input[name='internalProcessDialogItem']:checked").val();
		let nextUser = {
				name : $("select#userSolving option:selected").text(),
				id : $("select#userSolving option:selected").val(),
		}
		
		if (nextUser.id === info.userSolving) {
			alert("<s:message code='form.question.form.info.internalprocess.error'/>");
			return;
		}
		
		if (confirm("<s:message code='form.question.form.info.internalprocess.submit.confirm' arguments='"+nextUser.name+"'/>")) {
			info.userSolving = nextUser.id;
			info.currentProcess = currentProcess;
			HtmlUtil.lockSubmitKey(true);
			saveBasicInfo(function () {
				save();
				SendUtil.post('/apJobCForm/sendInternalProcess', info, null, ajaxSetting);
				alert("<s:message code='form.question.form.info.internalprocess.submit.success'/>");
			});
			SendUtil.href("/dashboard");
		}
	}
	
	// 結束內會
	var finished = function() {
		let currentProcess = $("#internalProcessDialog input[name='internalProcessDialogItem']:checked").val();
		let unfinishedInternalProcesses = getUnfinishedInternalProcess();
		let isUnfinishedInternalProcessOverTwo = (unfinishedInternalProcesses && unfinishedInternalProcesses.length >= 2);
		
		if (isUnfinishedInternalProcessOverTwo) {
			let unfinishedInternalProcessesArrayStr = unfinishedInternalProcesses.join(", ");
			alert("<s:message code='form.question.form.info.internalprocess.finished.check.error.over' arguments='"+unfinishedInternalProcessesArrayStr+"'/>");
			return;
		} else {
			if (unfinishedInternalProcesses && unfinishedInternalProcesses.length == 1) {
				let unfinishedInternalProcess = unfinishedInternalProcesses[0];
				let currentProcessName = internalProcessItems[currentProcess].name;
				if (currentProcessName != unfinishedInternalProcess) {
					alert("<s:message code='form.question.form.info.internalprocess.finished.check.error.nomatch' arguments='"+unfinishedInternalProcess+"'/>");
					return;
				}
			}
		}
		
		if (confirm("<s:message code='form.question.form.info.internalprocess.finished.confirm'/>")) {
			info.currentProcess = currentProcess;
			HtmlUtil.lockSubmitKey(true);
			saveBasicInfo(function () {
				save();
				SendUtil.post('/apJobCForm/finishedInternalProcess', info, null, ajaxSetting);
				DialogUtil.close();
				signingWithInternalProcess();
			});
		}
	}
	
	function getUnfinishedInternalProcess() {
		let result = null;
		let options = {
			async: false,
		};
		SendUtil.get('/html/getUnfinishedInternalProcess', info.formId, function (response) {
			result = response;
		}, options, true);	
		return result;
	}
	
	return {
		show : show,
		radioEvent : radioEvent,
		finished : finished,
		submit : submit,
	}
}();
</script>
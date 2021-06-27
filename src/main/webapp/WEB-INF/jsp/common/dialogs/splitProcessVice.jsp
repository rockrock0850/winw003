<%@ page contentType="text/html; charset=UTF-8" %>

<div id='splitProcessViceDialog' style='display: none'>
	<fieldset>
		<legend>選擇副科長</legend>
		<div class="grid_BtnBar">
			<button type="button" id="submit" onclick="SplitProcessViceDialog.submit();"><i class="icon-ok"></i>確定</button>
		</div>
		<table id="viceList" class="grid_list"></table>
	</fieldset>
</div>

<script>//# sourceURL=SplitProcessViceDialog.js
var SplitProcessViceDialog = function () {

	const dialog = "div#splitProcessViceDialog";
	const table = dialog + " " + "table#viceList";
	const options = {
			width: '300px',
			maxWidth: '300px',
	}
	
	var info;
	var vices = {};
	
	var show = function (info_) {
		info = info_;
		SendUtil.get('/html/getSplitProcessViceList', {}, function (response) {
			$(table).empty();
			
			let htmlBuilder = new Array();
			htmlBuilder.push("<thead>");
			htmlBuilder.push(	"<tr>");
			htmlBuilder.push(		"<th></th>");
			htmlBuilder.push(		"<th>姓名</th>");
			htmlBuilder.push(	"</tr>");
			htmlBuilder.push("</thead>");
			
			htmlBuilder.push("<tbody>");
			let checked = " checked";
			let index = 0;
			$.each(response, function () {
				let clazz = ++index % 2 == 0 ? "odd" : "even";
				
				htmlBuilder.push("<tr class='"+clazz+"' onclick='SplitProcessViceDialog.radioEvent("+index+")'>");
				
				htmlBuilder.push("<td style='text-align:center;'>");
				let item = '<input type="radio" id="splitProcessViceDialogItem'+index+'" name="splitProcessViceDialogItem" value="'+this.value+'"'+checked+'/>';
				htmlBuilder.push(item);
				htmlBuilder.push("</td>");
				
				htmlBuilder.push("<td>");
				htmlBuilder.push(this.wording);
				htmlBuilder.push("</td>");
				
				htmlBuilder.push("</tr>");
				
				checked = "";
				vices[this.value] = this;
			});
			htmlBuilder.push("</tbody>");
			$(table).append(htmlBuilder.join(""));
			
			
		}, null, true);

		DialogUtil.show(dialog, options);
	}
	
	var radioEvent = function(index) {
		$("input#splitProcessViceDialogItem"+index).prop("checked", true);
	}
	
	var submit = function() {
		let vice = $("#splitProcessViceDialog input[name='splitProcessViceDialogItem']:checked").val();
		
		if (confirm("<s:message code='form.question.form.info.splitprocess.confirm' arguments='"+vices[vice].wording+"'/>")) {
			info.currentProcess = vices[vice].value;
			HtmlUtil.lockSubmitKey(true);
			saveBasicInfo(function () {
				save();
				SendUtil.post('/apJobCForm/sendSplitProcess', info, null, ajaxSetting);
				alert("<s:message code='form.question.form.info.splitprocess.success'/>");
			});
			
			DialogUtil.close();
		}
	}
	
	return {
		show : show,
		radioEvent : radioEvent,
		submit : submit,
	}
}();
</script>
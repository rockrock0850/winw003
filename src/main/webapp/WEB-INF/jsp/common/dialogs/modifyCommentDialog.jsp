﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script>//# sourceURL=modifyCommentDialog.js
/**
 * 開啟 修改原因對話框
 */
 var ModifyingDialog = function () {
		var dialog = 'div#modifyingDialog';
		
		var show = function (reqData, buttonEvents, type) {
			alertLevelUI(buttonEvents, type);
		}
		
		return {
			show : show,
		}
		
		function alertLevelUI (buttonEvents, type) {
			let dialogOpts = {
				width: '700px',
				maxWidth: '1000px',
				resizable: false,
				close: function(event, ui) {
	                close(buttonEvents, type);
	            },
				buttons : createButton(buttonEvents, type)
			};
			
			DialogUtil.show(dialog, dialogOpts);
			document.getElementById("modifyComment").value = ""; //將前一次輸入的值清空 
		}
		
		function createButton (buttonEvents, type) {
			let buttons = {}
			buttons["確定"] = function () {
				if (buttonEvents.confirm) {
					buttonEvents.confirm();
				}
			};
			buttons["取消"] = function () {
				if (buttonEvents.cancel) {
					DialogUtil.close();
					buttonEvents.cancel();
				}
			};
			
			return buttons;
		}
	   
		function close (buttonEvents, type) {
			if (type == "cList") {
				buttonEvents.cancel();
			}
		}
	}();
</script>

<div id="modifyingDialog" style="display: none;">
	<div id="modifyingDiv">
		<fieldset>
			<legend><s:message code='form.modify.verifyComment' text='修改原因'/></legend>
			<form id='modifyForm'>
				<table class="grid_list">
					<tr>
						<td><textarea maxlength='2000' id='modifyComment' class='signing-remark' name='modifyComment' rows="3"></textarea></td>
					</tr>
				</table>
			</form>
		</fieldset>
	</div>
</div>
﻿<%@ page contentType="text/html; charset=UTF-8" %>
<script>//# sourceURL=expectdEndDialog.js
/*
 * 限制在REVIEW階段, 
 * 若母單「預計完成時間」延期, 
 * 副科在自己關卡按下【儲存】時,  
 * 系統以彈跳視窗提示
 */
var ScopeChangedDialog = function () {

	var dialog = 'div#scopeChangedDialog';
	var table = dialog + ' table#scopeChangedTable';
	
	var show = function (callback) {
		let dialogOpts = {
			width:'900px',
			maxHeight: '500px',
			maxWidth: '100px',
			resizable: true,
			open: function (event, ui) {
				if (!TableUtil.isDataTable(table)) {
					TableUtil.init(table, {});
				}
			},
			close: function () {
				closed();
			},
			buttons: {
				"確定": function (e) {
					HtmlUtil.lockSubmitKey(true);
					
					if (callback) {
						if (document.getElementById("isScopeChanged").checked) {
							$('input#isDifferentScope[type="checkbox"]').prop('checked', true);
						}
						
						let isScopeChanged = $(table + ' input#isScopeChanged').is(':checked');
						$('input#isEctExtended[type="checkbox"]').prop('checked', true);
						$('input#isScopeChanged[type="checkbox"]').prop('checked', isScopeChanged);
						callback();
					}
					
					closed();
				},
				"取消": function () {
					closed();
				}
			}
		};

		DialogUtil.show(dialog, dialogOpts);
		HtmlUtil.clickRowRadioEvent(table + ' tbody');
	}
	
	function closed () {
		DialogUtil.close();
	}
	
	return {
		show : show
	}
}();
</script>

<div id='scopeChangedDialog' style='display: none'>
	<fieldset>
		<table id="scopeChangedTable" class="display collapse nowrap cell-border">
			<thead>
				<tr>
					<th></th>
					<th><s:message code="form.common.approval.warning.5" text="確認事項" /></th>
					<th><s:message code="form.common.approval.warning.6" text="待執行事項" /></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td style="text-align:center;"><input type="radio" name="isScopeChanged" checked></td>
					<td><s:message code="form.common.approval.warning.9" text="此單之變更範圍，與原來相同。" /></td>
					<td><s:message code="form.common.approval.warning.101" text="系統將此單所有變更單「預計變更結束時間」</br>連動更新為此單「預計完成時間」。" /></td>
				</tr>
				<tr>
					<td style="text-align:center;"><input id='isScopeChanged' type="radio" name="isScopeChanged"></td>
					<td><s:message code="form.common.approval.warning.7" text="此單之變更範圍，與原來不同，或增加會辦科。" /></td>
					<td><s:message code="form.common.approval.warning.81" text="(1) 請手動將此單所有變更單結案。</br>(2) 重新手動開立變更單重新評估。" /></td>
				</tr>
			</tbody>
		</table>
	</fieldset>
</div>
<%@ page contentType="text/html; charset=UTF-8" %>
<script>
/**
 * 開啟「新增Segment」對話框
 */
var SegmentDialog = function () {

	var dialog = 'div#segmentDialog';
	
	var show = function (confirmButton, cancelButton) {
		let dialogOpts = {
			width: '1000px',
			maxWidth: '2000px',
			resizable: true,
			open: function (event, ui) {
				$(dialog + ' .resizable-textarea').resizable({handles: "se"});
				DialogUtil.hideLoading();
			},
			close: function () {
				$(dialog + ' input,textarea').val('');
			},
			buttons: {
				"確定": function () {
					HtmlUtil.lockSubmitKey(true);
					if (confirmButton) {
						let headForm = form2object('headForm');
						let segmentRow = form2object('segmentRow');
						
						segmentRow.type = 'DB';
						segmentRow.formId = headForm.formId;
						PurifyUtil.obj(segmentRow);
						confirmButton(segmentRow);
					}
					
					DialogUtil.close();
					$(dialog + ' input,textarea').val('');
				},
				"取消": function () {
					if (cancelButton) {
						cancelButton(table);	
					}
					
					DialogUtil.close();
					$(dialog + ' input,textarea').val('');
				}
			}
		};
		
		DialogUtil.show(dialog, dialogOpts);
	}
	
	var edit = function (row, confirmButton, cancelButton) {
		ObjectUtil.autoSetFormValue(row, 'segmentRow');
		SegmentDialog.show(confirmButton, cancelButton);
	}
	
	return {
		show : show,
		edit : edit
	}
}();
</script>

<div id='segmentDialog' style='display: none'>
	<form id='segmentRow' class="search">
		<input id="id" class="hidden" name="id" />
		<input id="type" class="hidden" name="type" />
		<input id="page" class="hidden" name="page" />
		<input id="formId" class="hidden" name="formId" />
		<input id="number" class="hidden" name="number" />
		<input id="createdAt" class="hidden" name="createdAt" />
		<input id="updatedAt" class="hidden" name="updatedAt" />
		<input id="createdBy" class="hidden" name="createdBy" />
		<input id="updatedBy" class="hidden" name="updatedBy" />
		<table class="grid_list">
		    <tr>
		        <th width="15%">Segment名稱</th>
		        <td nowrap="nowrap"><input id='segment' name='segment' type="text" style="width: 20rem;" maxlength="500" >
		    </tr>
		    <tr>
		        <th width="15%">Key Value</th>
		        <td nowrap="nowrap"><input id='keyValue' name='keyValue' type="text" style="width: 20rem;" maxlength="1000" >
		    </tr>
		    <tr>
		        <th width="15%">變更的欄位</th>
		        <td nowrap="nowrap"><input id='alterColumns' name='alterColumns' type="text" style="width: 20rem;" maxlength="1000" >
		    </tr>
		    <tr>
		        <th width="15%">變更內容</th>
		        <td><textarea id='alterContent' name='alterContent' class='resizable-textarea' cols="50" rows="8" maxlength="1000" ></textarea>
		    </tr>
		    <tr>
		        <th width="15%">Layout Dataset</th>
		        <td nowrap="nowrap"><input id='layoutDataset' name='layoutDataset' type="text" style="width: 20rem;" maxlength="1000" >
		    </tr>
		</table>
   </form>
</div>
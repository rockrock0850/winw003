﻿﻿<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<script>
// 檔案上傳對話視窗
var UploadDialog = function () {
	
	var tabId = '';
	
	var show = function (tab) {
		tabId = tab;
		initView();
		
		let dialog = 'div#uploadDialog';
		let options = {
			close: function () {
				DialogUtil.hideLoading();
			}
		};
		
		DialogUtil.show(dialog, options);
		
		$('input[type="file"]').change(function () {
			read(this);
		});
	}

	var upload = function () {
		let formData = FileUtil.getFormFile();
		
		if ($.isEmptyObject(formData)) {
			alert('<s:message code="form.common.file.empty.file" text="請選擇檔案" />');
			return;
		}
		
		if (!confirm('<s:message code="form.common.file.upload.confirm" text="確認上傳?" />')) {
			return;
		}

		formData.append('type', $('input#fileDialogCol0').val());
		formData.append('formId', $('input#formId').val());
		formData.append('description', PurifyUtil.dom($('input#fileDialogCol1').val()));
		formData.append('layoutDataset', PurifyUtil.dom($('input#fileDialogCol2').val()));
		formData.append('alterContent', PurifyUtil.dom($('textarea#fileDialogCol3').val()));

		FileUtil.upload('/commonForm/upload', formData, function (result) {
			if (result.returnMsg) {
				alert(result.returnMsg);
			} else {
				DialogUtil.close();
			}
			
			$('#fileDialogCol1').val('');	
			$('#fileDialogCol3').val('');	
			$('#fileDialogCol2').val('');
			$('input[type="file"]').val('');
			$('li#' + tabId).trigger('click');
		});
	}

	function initView () {
		let isFileList = tabId == 'tabFileList';
		$('tr#trAlterContent').toggle(!isFileList);
		$('tr#trLayoutDataset').toggle(!isFileList);
		$('input#fileDialogCol0').val(isFileList ? 'FILE' : 'DB');
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
	
	return {
		show : show,
		upload : upload
	}
}();
</script>
<div id='uploadDialog' style='display: none'>
	<fieldset>
        <input id="fileDialogCol0" class="hidden" name="type" />
		<legend><s:message code="form.common.file.upload" text='上傳' /></legend>
		<table class="grid_query">
			<tr>
				<th><font color="red">&nbsp;*&nbsp;</font><s:message code="form.common.file.path" text='檔案路徑' /></th>
				<td><input type="file"></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="form.common.file.description" text='檔案說明' /></th>
				<td><input id='fileDialogCol1' type="text" size="60" maxlength="200" /></td>
				<td nowrap="nowrap">
					<button onclick='UploadDialog.upload();'><i class="iconx-upload-file"></i> <s:message code="form.common.file.upload" text='上傳' /></button>
				</td>
			</tr>
		    <tr id='trLayoutDataset'>
		        <th width="15%">Layout Dataset</th>
		        <td nowrap="nowrap"><input id='fileDialogCol2' name='layoutDataset' type="text" style="width: 20rem;" maxlength="1000" >
		    </tr>
		    <tr id='trAlterContent'>
		        <th width="15%">變更內容</th>
		        <td><textarea id='fileDialogCol3' name='alterContent' class='resizable-textarea' cols="50" rows="8" maxlength="1000" ></textarea>
		    </tr>
		</table>
	</fieldset>
</div>
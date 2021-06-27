<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<script>//# sourceURL=sysOptionDialog.js
function validate (param) {
	if (!param.display) {
		alert("名稱不可空白。");
		return false; 
	}
	
	return true; 
}

function create (actionType) {
	let dialog = 'div#sysOptionDialog';
	let params = form2object('paramForm');
	params.active = $(dialog + ' input#enabled').is(':checked') ? 'Y' : 'N';
	
	if (!validate(params)) {
		return;
	}
	
	SendUtil.post('/${param.function}/init/' + actionType, params, function (data) {
		alert("儲存成功");		
		$('#searchBtn').click();		
		DialogUtil.close(dialog);
	}, null, true);
}

function update () {
	let dialog = 'div#sysOptionDialog';
	let params = form2object('paramForm');
	params.active = $(dialog + ' input#enabled').is(':checked') ? 'Y' : 'N';
	params.isKnowledge = $(dialog + ' input#isKnowledge').is(':checked') ? 'Y' : 'N';
	
	if(!validate(params)) {
		return;
	}
	
	SendUtil.post('/${param.function}/init/updateLevel', params, function(data) {	
		alert('編輯成功');	
		$('#searchBtn').click();		
		DialogUtil.close(dialog);
	}, null, true);
}

var SysOptionDialog = function () {
	let dialog = 'div#sysOptionDialog';
	let isDisplayKnowledge = ${param.isDisplayKnowledge};
	
	var show = function (data, actionLabel, isEdit, isSub) {
		$(dialog + ' input').val("");
		$(dialog + ' #actionLabel').html(actionLabel);
		$(dialog + ' #actionBtn').unbind('click').click(function () {
			if (isEdit) {
				update();
			} else {
				create(isSub ? 'createLevel2' : 'createLevel1');
			}
		});

		if (!isDisplayKnowledge) {
			$('tr#isDisplayKnowledge').hide();
		}
		
		fillForm(data, isEdit, isSub);
		DialogUtil.show(dialog, {
			modal : true,
			width : 640,
			height : 200
		});
	}
	
	function fillForm (data, isEdit, isSub) {
		if (data) {
			if (isSub && !isEdit) {
				$(dialog + ' input[name="parentId"]').val(data.value);
			}
			
			if (isEdit) {
				$(dialog + " input[name='id']").val(data.id);
				$(dialog + " input[name='value']").val(data.value);
				$(dialog + " input[name='name']").val(data.name);
				$(dialog + " input[name='display']").val(data.display);
				$(dialog + ' input[name="parentId"]').val(data.parentId);
				$(dialog + ' input[name="optionId"]').val(data.optionId);
				$(dialog + ' input[name="createdBy"]').val(data.createdBy);
				$(dialog + ' input[name="createdAt"]').val(data.createdAt);
				$(dialog + " input#isKnowledge").attr('checked', 'Y' == data.isKnowledge);	
				if ('Y' == data.active) {
					$(dialog + " input#enabled").attr('checked', true);	
				} else {
					$(dialog + " input#disabled").attr('checked', true);
				}
			}
		}
	}
	
	return {
		show : show
	}
}();
</script>

<div id='sysOptionDialog' style="display: none;">
	<form id="paramForm">
	   	<input class="hidden" name="id" />
	   	<input class="hidden" name="name" />
	   	<input class="hidden" name="value" />
	   	<input class="hidden" name="optionId" />
		<input class="hidden" name="parentId" />
	   	<input class="hidden" name="createdBy" />
		<input class="hidden" name="createdAt" />
		<fieldset>
			<legend id='actionLabel'></legend>
			<div class="grid_BtnBar">
				<button id='actionBtn' type="button" >
					<i class="icon-save"></i>儲存
				</button>
			</div>
			<table id="dataTable" class="grid_query">
				<tr>
					<td><font color="red">名稱:</font></td>
					<td><input type="text" name="display" style="width: 27rem" value="" maxlength="500"/></td>
				</tr>
				<tr>
					<td><font color="red">狀態:</font></td>
					<td>
						<label>
							<input id='enabled' type="radio" name='active' checked />
							<s:message code="service.type.edit.label.enable" text='啟用' />
						</label>
						<label>
							<input id='disabled' type="radio" name='active' />
							<s:message code="service.type.edit.label.disable" text='停用' />
						</label>
					</td>
				</tr>
				<tr id='isDisplayKnowledge'>
					<td><label for='isKnowledge'>建議加入問題知識庫?</label></td>
					<td>
						<input id='isKnowledge' type="checkbox" name='isKnowledge' checked />
					</td>
				</tr>
			</table>
		</fieldset>
	</form>
</div>
<%@ page contentType="text/html; charset=UTF-8" %>
<script>
/**
 * 開啟「選擇使用者」對話框
 */
var UserDialog = function () {

	var dialog = 'div#userDialog';
	var table = dialog + ' table#userList';
	
	var show = function (element, request, callback) {
		SendUtil.post('/html/getGroupUserList', request, function (resData) {
			let options = {
				title: '選擇人員',
				name: '選擇人員',
				width: '100%',
				data: resData.userList,
				columnDefs: [
					{ 
			            targets: 0,
			            searchable: false,
			            orderable: false,
			            className: 'dt-center',
			            render: function(data, type, full, meta){
			                  data = '<input type="radio" name="id" value="' + full.userId + '">';
			                  data += '<input type="hidden" name="name" value="' + (full.name ? full.name : full.userName) + '">';
			               return data;
			            }
			        },
					{targets: [1], title: '姓名', data: '', className: 'dt-center',autoWidth: true, 
			        	render: function (data, type, full, meta) {
			        		return full.name ? full.name : full.userName;
			        	}}, 
					{targets: [2], title: 'Email', data: 'email', className: 'dt-center'},
				]
			};
			$("#groupName").text(resData.groupName);
			alertListView(element, options, callback);
		}, null, true);
	}
	
	function alertListView (element, options, callback) {
		let dialogOpts = {
			width:'900px',
			maxHeight: '500px',
			maxWidth: '100px',
			resizable: true,
			open: function (event, ui) {
				if (!TableUtil.isDataTable(table)) {
					TableUtil.init(table, options);
				} else {
					TableUtil.reDraw($(table).DataTable(), options.data);
				}
			},
			close: function () {
				DialogUtil.close(dialog);
				TableUtil.deleteTable(table);
			},
			buttons: {
				"確定": function (e) {
					HtmlUtil.lockSubmitKey(true);
					if (callback) {
						let radio = $(this).find("input[name=id]:checked");
						let checkValue = radio.val();
						let checkValueDisPlay = radio.siblings("input[name=name]").val();
						
						if (checkValue && checkValueDisPlay) {
							let selected = {
								value: checkValue,
								display: checkValueDisPlay
							}
							callback($(element).closest('tr'), selected);
						}
						
						TableUtil.deleteTable(table);
						DialogUtil.close(dialog);
					}
				}
			}
		};

		DialogUtil.show(dialog, dialogOpts);
		HtmlUtil.clickRowRadioEvent(table + ' tbody');
	}
	
	return {
		show : show
	}
}();
</script>

<div id='userDialog' style='display: none'>
	<fieldset>
		<legend><s:message code='global.select.user'/></legend>
		<table class="grid_query">
			<tbody><tr>
				<th><s:message code='group.function.group.name'/></th>
				<td id="groupName"></td>
			</tr>
		</tbody></table>
	</fieldset>
	<fieldset>
		<table id="userList" class="display collapse nowrap cell-border">
			<thead>
			</thead>
			<tbody>
			</tbody>
		</table>
	</fieldset>
</div>
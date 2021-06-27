<%@ page contentType="text/html; charset=UTF-8" %>
<script>
/**
 * 開啟「選擇處理科別」對話框
 */
var SolvingDialog = function () {

	var dialog = 'div#solvingDialog';
	var table = dialog + ' table#solvingList';

	var show = function (confirmButton, cancelButton) {
		var options = {
			width: '380px',
			maxWidth: '300px',
			open: function (event, ui) {
				initTable();
				SendUtil.get('/html/getDeptDevisionSelectors', null, function (options) {
					HtmlUtil.singleSelect('select#divisions', options);
				}, null, true);
			},
			close: function () {
				$('select#divisions').val('').change();
			},
			buttons: {
				"確定": function () {
					HtmlUtil.lockSubmitKey(true);
					
					if (confirmButton) {
						var data = {
							division : $('select#divisions').val(),
							wording : $('select#divisions').find(':selected').text()
						};
						confirmButton(data);
					}
				},
				"取消": function () {
					if (cancelButton) {
						DialogUtil.close();
						cancelButton();	
						$('select#divisions').val('').change();
					}
				}
			}
		};
		
		DialogUtil.show(dialog, options);
	}

	/**
	 * 隨著會辦科勾選數量限制顯示清單
	 */
	var showLimit = function (domain, confirmButton, cancelButton) {
		var options = {
			width: '380px',
			maxWidth: '300px',
			open: function (event, ui) {
				initTable();
				cutCountersigneds(domain);
			},
			close: function () {
				$('select#divisions').val('').change();
			},
			buttons: {
				"確定": function () {
					HtmlUtil.lockSubmitKey(true);
					
					if (confirmButton) {
						var data = {
							division : $('select#divisions').val(),
							wording : $('select#divisions').find(':selected').text()
						};
						confirmButton(data);
					}
				},
				"取消": function () {
					if (cancelButton) {
						DialogUtil.close();
						cancelButton();	
						$('select#divisions').val('').change();
					}
				}
			}
		};
		
		DialogUtil.show(dialog, options);
	}
	
	function cutCountersigneds (domain) {
		let reqData = form2object('headForm');
		let formId = reqData.formId;
		let clazz = reqData.formClass;
		
		SendUtil.get('/html/getCountersignedDivisionList', [formId, clazz], function (options) {
			if (options && options.length > 0) {
				SendUtil.post(domain + '/info', form2object('headForm'), function (response) {
					let countersigneds = (response.countersigneds).split(",");
					if(countersigneds.length == 1) {
						let option = $("select#divisions").find('option').get(0);
						document.getElementById('divisions').removeChild(option);
					} 
					clearDivisions(response.countersigneds, options);
				}, null, true);
			}
		});
	}
	
	// 清除主單位未勾選的科別
	function clearDivisions (divisions, options) {
		var newDivisions = [];
		
		if (divisions) {
			$.each(divisions.split(','), function (i, c) {
				$.each(options, function (j, o) {
					if (o.value.indexOf(c) != -1) {
						newDivisions.push(o);
					}
				});
			});
		}
		
		HtmlUtil.singleSelect('select#divisions', options);
	}
	
	function initTable () {
		if (!TableUtil.isDataTable('#solvingList')) {
			var options = {
				info: false,
				paging: false,
				ordering: false,
				columnDefs: [
					{orderable: false, targets: []}, 
					{targets: [0], className: 'dt-center'}
				]
			};
			TableUtil.init('#solvingList', options);
		}
	}
	
	return {
		show : show,
		showLimit : showLimit
	}
}();
</script>
<div id='solvingDialog' style='display: none'>
	<fieldset>
		<legend>選擇處理科別</legend>
		<table id="solvingList" class="display collapse cell-border dataTable no-footer" style="margin-left:0px; width:347px;">
			<thead>
				<th>科別名稱</th>
			</thead>
			<tbody>
				<tr class="odd">
					<td class=" dt-center">
						<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
							<jsp:param name="id" value="divisions" />
							<jsp:param name="name" value="divisions" />
						</jsp:include>
					</td>
				</tr>
			</tbody>
		</table>
	</fieldset>
</div>
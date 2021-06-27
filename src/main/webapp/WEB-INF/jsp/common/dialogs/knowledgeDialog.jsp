<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<script>//# sourceURL=knoledgeDialog.js
/**
 * 新增知識庫根因對話框
 */
var KnowledgeDialog = function () {

	var dialog = 'div#knowledgeDialog';

	var show = function (confirmButton) {
        let dialogOpts = {
        	height: 300,
            width: '500px',
            maxWidth: '1000px',
            resizable: true,
            open: function (event, ui) {
            	initView();
            },
            close: function () {
				DialogUtil.close();
            },
			buttons: {
				"確定": function () {
					HtmlUtil.lockSubmitKey(true);
					
					if (confirmButton) {
						let exists = mapValueFromKnowledges();
						let val = $('input#knowledges').val();
						let knowledges = ObjectUtil.parse(val ? val : '[]');
						let knowledge1 = $('select#knowledge1').val();
						let knowledge2 = $('select#knowledge2').val();
						let knowledgeMap1 = makeOptionsToMap($('select#knowledge1').find('option'));
						let knowledgeMap2 = makeOptionsToMap($('select#knowledge2').find('option'));
						
						if (knowledge2) {
							$.each(knowledge2, function () {
								if ($.inArray(this.toString(), exists) < 0) {// Not in the array
									let obj = {
										'knowledge1': knowledge1,
										'knowledge2': this.toString(),
										'knowledge1Display': knowledgeMap1[knowledge1],
										'knowledge2Display': knowledgeMap2[this]
									};
									knowledges.push(obj);
								}
							});
						}
						
						confirmButton(knowledges);
						DialogUtil.close();
					}
				},
				"取消": function () {
					DialogUtil.close();
				}
			}
        };
        
		DialogUtil.show(dialog, dialogOpts);
	}
	
	function initView () {
		SendUtil.get('/html/getDropdownList', 'knowledge', function (options) {
			HtmlUtil.emptySelect("select#knowledge2", true);
			HtmlUtil.singleSelect('select#knowledge1', options);

			$('select#knowledge1').change(function () {
				if ($(this).val()) {
					SendUtil.get('/html/getSubDropdownList', $(this).val(), function (options) {
						HtmlUtil.multiSelect('select#knowledge2', options);
					});
				} else {
					HtmlUtil.emptySelect("select#knowledge2", true);
				}
				
				HtmlUtil.initMultiSelect('select#knowledge2', {});
			});
			
			HtmlUtil.initMultiSelect('select#knowledge2', {});
		});
	}
	
	return {
		show : show
	}
	
	function mapValueFromKnowledges () {
		let ary = [];
		let knowledges = $('input#knowledges').val();
		
		if (knowledges) {
			knowledges = ObjectUtil.parse(knowledges);
			ary = $.map(knowledges, function (item, i) {
				return item.knowledge2;
			});
		}
		
		return ary;
	}
	
	function makeOptionsToMap (options) {
		let map = {};
		
		if (options) {
			$.each(options, function () {
				map[$(this).val()] = $(this).html();
			});
		}
		
		return map;
	}
}();
</script>
<div id="knowledgeDialog" style="display: none;">
	<fieldset>
		<legend><s:message code="knowledge.dialog.title" text="選取工作要項" /></legend>
		<form id="knowledgeForm">
			<table class="grid_list">
				<tr>
					<td><span style="color: red; ">原因類別</span>:</td>
					<td>
						<jsp:include page='/WEB-INF/jsp/common/models/selectorModel.jsp'>
							<jsp:param name="id" value="knowledge1" />
							<jsp:param name="name" value="knowledge1" />
							<jsp:param name="defaultName" value='global.select.please.choose' />
						</jsp:include>
					</td>
					<td><span style="color: red; ">問題根因</span>:</td>
					<td>
						<jsp:include page='/WEB-INF/jsp/common/models/multiSelectorModel.jsp'>
							<jsp:param name="id" value="knowledge2" />
							<jsp:param name="name" value="knowledge2" />
							<jsp:param name="defaultName" value='global.select.please.choose' />
						</jsp:include>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>
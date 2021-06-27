<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!--  
輸入參數說明 :
	- month       : '月份' 
	- excludeCbId : 是否排除資安部checkbox識別碼
	- searchBtId  : 查詢button識別碼
	- exportBtId  : 匯出button識別碼
	- layout	  : HTML顯示參數  1.區間選擇器、2.下拉選單、3.日期選擇器
	- action 	  : 初始化選擇參數  
		1.區間選擇器[年月日]、2.區間選擇器[年月]、3.下拉選單、
		4.日期選擇器[年月]、5.日期選擇器[年]、6.區間選擇器[年月日]限制結束日期為起始日期之往後1年
		7.日期選擇器[年月日]
-->
<script>
	var ajaxSetting = {async:false};
	$(function () {
		<c:choose>
			<c:when test='${param.action == "1"}'>
				var startOptions = {
						maxDate: '',
						minDate: '',
			        	onSelect: function (selected,inst) {
			            	endOptions.minDate = selected;
							DateUtil.datePickerById('#endDate', endOptions);
			        	},
				    	onClose: function(dateText, inst) {
				    		if($("#endDate").val()){
				    			endOptions.minDate = dateText;
								DateUtil.datePickerById('#endDate', endOptions);
					    	}
				    	}
					};
				var endOptions = {
						minDate: '',
						maxDate: '',
			       		onSelect: function (selected,inst) {
			            	startOptions.maxDate = selected;
							DateUtil.datePickerById('#startDate', startOptions);
			        	},
				    	onClose: function(dateText, inst) {
				    		if($("#startDate").val()){
				    			startOptions.maxDate = dateText;
								DateUtil.datePickerById('#startDate', startOptions);
					    	}
				    	}
					};					
				DateUtil.datePickerById('#startDate' ,startOptions);
				DateUtil.datePickerById('#endDate' ,endOptions);		
			</c:when>
			<c:when test='${param.action == "2"}'>
				var startOptions={
			        	changeMonth: true,
			        	changeYear: true,
			        	showButtonPanel: false,
			        	monthNames:["01","02","03","04","05","06","07","08","09","10","11","12"],
			        	dateFormat: 'yy/MM',
			        	onClose: function(dateText, inst) { 
			            	var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
			            	var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
			            	startOptions.defaultDate = new Date(year, month, 1);
							DateUtil.datePickerById('#startDate', startOptions);
			            	$(this).datepicker('setDate', startOptions.defaultDate);
			            	if($("#endDate").val() && ($(this).val()).valueOf() > ($("#endDate").val()).valueOf()){
		            			alert('<s:message code="report.operation.start.dont.gt.end" text="" />');
		            			$(this).val("");
				        	}
			        	},
						minDate: ''	        
				};
				
				var endOptions = {
			        	changeMonth: true,
			        	changeYear: true,
			        	showButtonPanel: false,
			        	dateFormat: 'yy/MM',
			        	monthNames:["01","02","03","04","05","06","07","08","09","10","11","12"],
			       		onClose: function(dateText, inst) { 
			            	var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
			            	var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
			            	endOptions.defaultDate = new Date(year, month, 1);
							DateUtil.datePickerById('#endDate', endOptions);
			            	$(this).datepicker('setDate', endOptions.defaultDate);
			            	if($("#startDate").val() && ($(this).val()).valueOf() < ($("#startDate").val()).valueOf()){
		            			alert('<s:message code="report.operation.end.dont.lt.start" text="" />');
		            			$(this).val("");
				        	}		            
			        	},
			        	minDate: ''
				};
					
				DateUtil.datePickerById('#startDate' ,startOptions);
				DateUtil.datePickerById('#endDate' ,endOptions);		
			</c:when>
			<c:when test='${param.action == "3"}'>
				SendUtil.get("/html/getDivisionSelectorsOnlyUseInc", 'Y', function (option) {
					HtmlUtil.singleSelect('select#unitId', option);
				}, ajaxSetting);
	
				$('#excludeCbx').change(function() {
					var isCheck = this.checked ? 'Y' : 'N';
					SendUtil.get("/html/getDivisionSelectorsOnlyUseInc", isCheck, function (option) {
						HtmlUtil.singleSelect('select#unitId', option);
					}, ajaxSetting);			
				});		
			</c:when>
			<c:when test='${param.action == "4"}'>
				var startOptions={
			        	monthNames:["01","02","03","04","05","06","07","08","09","10","11","12"],
			        	dateFormat: 'yy/MM',
			        	onClose: function(dateText, inst) { 
			            	var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
			            	var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
			            	startOptions.defaultDate = new Date(year, month, 1);
							DateUtil.datePickerById('#startDate', startOptions);
			            	$(this).datepicker('setDate', startOptions.defaultDate);
			        },
					minDate: ''	        
				};	
				DateUtil.datePickerById('#startDate' ,startOptions);		
			</c:when>
			<c:when test='${param.action == "5"}'>
				var startOptions = {
		        	dateFormat: 'yy',
					minDate: '',
		        	showButtonPanel: false,
		       		onSelect: function (selected,inst) {
		            	var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
		            	var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
		            	startOptions.defaultDate = new Date(year, month, 1);
						DateUtil.datePickerById('#startDate', startOptions);
		            	$(this).datepicker('setDate', startOptions.defaultDate);
		        	},
		        	onClose: function (dateText, inst) { 
		            	var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
		            	var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
		            	startOptions.defaultDate = new Date(year, month, 1);
						DateUtil.datePickerById('#startDate', startOptions);
		            	$(this).datepicker('setDate', startOptions.defaultDate);
			        },
				};	
				DateUtil.datePickerById('#startDate' ,startOptions);
			</c:when>	
			<c:when test='${param.action == "6"}'>
				var startOptions = {
						maxDate: '',
						minDate: '',
			        	onSelect: function (selected,inst) {
			            	endOptions.minDate = selected;
			            	endOptions.maxDate = DateUtil.toDate(DateUtil.addYears(selected, 1));
							DateUtil.datePickerById('#endDate', endOptions);
			        	},
				    	onClose: function(dateText, inst) {
				    		if($("#endDate").val()){
				    			endOptions.minDate = dateText;
				            	endOptions.maxDate = DateUtil.toDate(DateUtil.addYears(dateText, 1));
								DateUtil.datePickerById('#endDate', endOptions);
					    	}
				    	}
					};
				var endOptions = {
						minDate: '',
						maxDate: '',
			       		onSelect: function (selected,inst) {
			            	startOptions.maxDate = selected;
							DateUtil.datePickerById('#startDate', startOptions);
			        	},
				    	onClose: function(dateText, inst) {
				    		if($("#startDate").val()){
				    			startOptions.maxDate = dateText;
								DateUtil.datePickerById('#startDate', startOptions);
					    	}
				    	}
					};					
				DateUtil.datePickerById('#startDate' ,startOptions);
				DateUtil.datePickerById('#endDate' ,endOptions);		
			</c:when>	
			<c:when test='${param.action == "7"}'>
				var startOptions = {
					maxDate: '',
					minDate: '',
		        	onSelect: function (selected,inst) {
		            	endOptions.minDate = selected;
						DateUtil.datePickerById('#endDate', endOptions);
		        	},
			    	onClose: function(dateText, inst) {
			    		if($("#endDate").val()){
			    			endOptions.minDate = dateText;
							DateUtil.datePickerById('#endDate', endOptions);
				    	}
			    	}
				};	
				DateUtil.datePickerById('#startDate' ,startOptions);		
			</c:when>
		</c:choose>
	});

</script>

<fieldset class="search">
		<c:if test="${param.showConditionTitle == 'Y'}">
			<legend><s:message code="schedule.find.th.search" text='' /></legend>
		</c:if>
		<button class="small fieldControl searchPanel">
			<i class="iconx-collapse"></i>|||
		</button>
		<form id='reportForm'>
			<table class="grid_query">
				<tr>
					<c:choose>
						<c:when test="${param.layout == '1'}">
							<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="${param.month}" text='' /></th>
							<td>
								<jsp:include page="/WEB-INF/jsp/common/models/dateRangeModel.jsp" >
									<jsp:param name="id1" value="startDate" />
									<jsp:param name="name1" value="startDate" />
									<jsp:param name="id2" value="endDate" />
									<jsp:param name="name2" value="endDate" />
								</jsp:include>
							</td>			
						</c:when>
						<c:when test="${param.layout == '2'}">
							<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="${param.month}" text='' /></th>
							<td width="70px">
								<c:import url='/WEB-INF/jsp/common/models/selectorModel.jsp'>
									<c:param name="id" value="unitId" />
									<c:param name="name" value="unitId" />
									<c:param name="removeTdTag" value="true" />
									<c:param name="defaultValue" value="allSection" />
									<c:param name="defaultName" value='<fmt:message key="report.operation.select.division.all"/>' />
								</c:import>
							</td>	
						</c:when>
						<c:when test="${param.layout == '3'}">
							<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="${param.month}" text='' /></th>
							<td>
								<c:import url="/WEB-INF/jsp/common/models/dateModel.jsp">
				    				<c:param name="id1" value="startDate" />
									<c:param name="name1" value="startDate" />
				    				<c:param name="removeTdTag" value="true" />
								</c:import>
							</td>		
						</c:when>
					</c:choose>
					
					<c:if test="${param.eventLayout == '1'}">
						<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="${param.eventType}" text='事件類型' /></th>
						<td>
							<select id="eventType" name="eventType">
								<option value=""><s:message code="global.select.please.all" text='全部' /></option>
								<option value="服務異常"><s:message code="report.operation.service.exception" text='服務異常' /></option>
								<option value="服務請求"><s:message code="report.operation.service.request" text='服務請求' /></option>
								<option value="服務諮詢"><s:message code="report.operation.service.counsel" text='服務諮詢' /></option>
							</select>
						</td>		
					</c:if>
					
					<c:if test="${param.eventLayout == '2'}">
						<th>&nbsp;&nbsp;&nbsp;&nbsp;<s:message code="form.link.column.3" text='表單狀態' /></th>
						<td>
							<select id="formStatus" name="formStatus">
								<option value=""><s:message code="global.select.please.all" text='全部' /></option>
								<option value="unfinished"><s:message code="dashboard.order.unfinished" text='未結案' /></option>
								<option value="finished"><s:message code="report.operation.form.status.closed.0" text='已結案' /></option>
							</select>
						</td>		
					</c:if>
					
					<th><label for='excludeCbx'><fmt:message key="form.report.search.exclude"/></label></th>
					<td>
						<input id="<c:out value='${param.excludeCbId}'/>" name="<c:out value='${param.excludeCbId}'/>" type="checkbox" checked="checked">&nbsp;&nbsp;
						<button type='button' id="<c:out value='${param.searchBtId}'/>" ><i class="iconx-search"></i><fmt:message key="button.search"/></button>
						<button type='button' id="<c:out value='${param.exportBtId}'/>" ><i class="iconx-export"></i><fmt:message key="button.export"/></button>
					</td>
				</tr>
			</table>
		</form>
</fieldset>

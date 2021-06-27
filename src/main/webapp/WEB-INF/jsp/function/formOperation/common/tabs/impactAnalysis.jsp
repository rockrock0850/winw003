<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<div class="grid_BtnBar">
	<div align="left">
		<table class='grid_query'>
			<tbody>
				<tr><td><font><s:message code='form.impactAnalysis.note.1' text='變更衝擊可以不用填寫的情況：一、來源是問題會辦單或需求會辦單(應為主單填寫)。二、新系統(應填寫可行性分析報告)。三、變更類型為標準變更。'/></font></td></tr>
				<tr><td><span style="color: red; "><font id="note"></font></span></td></tr>
			</tbody>
		</table>
	</div>
</div>
<div>
	<form id="impactForm">
		<table id="formImpactAnalysisTable" class="grid_list"></table>
		<table class="grid_list">
			<tr>
				<td>
					<span><s:message code='form.change.form.info.change.rank' text='變更等級'/> : </span>
					<span id="changeRank" style="color: red; "></span>
				</td>
			</tr>
			<tr>
				<td>
					<s:message code='form.impactAnalysis.totalFraction.name' text='總分'/>:
					<span id="totalFractionDisplay" style="color: red;" ></span>
					<input type="hidden" id="totalFraction" name="totalFraction">
				</td>
			</tr>
		</table>
		<br><br>
		<table class="grid_list">
			<tr>
				<td><s:message code='form.impactAnalysis.evaluation' text='影響評估'/></td>
				<td><textarea cols="50" rows="4" id="evaluation" name='evaluation' maxlength="250"  ></textarea></td>
			</tr>
			<tr>
				<td><s:message code='form.impactAnalysis.solution' text='因應措施'/></td>
				<td><textarea cols="50" rows="4" id="solution" name='solution' maxlength="250" ></textarea></td>
			</tr>
		</table>
	</form>
</div>

<script>//# sourceURL=impactAnalysis.js
$(function() {
	initView();
	initEvent();
	authViewControl();
});

function initView () {
	if (HtmlUtil.tempData.impactForm && 
			HtmlUtil.tempData.impactForm.impactList && 
			HtmlUtil.tempData.impactForm.impactList.length > 0) {
		createImpactList(HtmlUtil.tempData.impactForm.impactList);
	} else {
		SendUtil.post('/commonForm/getFormImpactAnalysis', 
				form2object('headForm'), function (resData) {
			createImpactList(resData);
		}, ajaxSetting);
	}
}

function initEvent () {
	$(".targetFractionSelector").change(function () {
		let totalFraction = 0;
		
		$.each($(".impactRowValues"), function(index, element) {
			let target = $(this).next('tr').find("#targetFraction");
			let isAddUp = $(this).next('tr').find("#isAddUp").val();
			//衝擊分析加總:isAddUp=Y才須加總
			if(isAddUp=="Y") {
				totalFraction += parseInt(target.val());
			}
		});
		
		setTotalFractionText(totalFraction);
	});
}
	
function createImpactList (resData) {
	let count = 1;
	let totalFraction = 0;
	let note = "";
	let template = "<tr class='impactRowValues'><td colspan='4'>{0}.{1}</td></tr>";
		template += "<tr>";
		template += "<td width='1%'>";
		template += "<input type='hidden' id='order' value='{2}'>";
		template += '<button type="button" class="impactDialogBtn iconx-search" onclick="ImpactDialog.show(this)">列表</button>';
		template += "</td>";
		template += "<td width='1%'>";
		template += '<button type="button" class="clearTargetFractionBtn" onclick="clearTargetFraction(this)">清除</button>';
		template += "</td>";
		template += "<td width='5%'>";
		template += "<input id='targetFraction' name='targetFraction' class='targetFractionSelector' type='text' value='{3}' readonly />";
		template += "</td>";
		template += "<td width='93%'>";
		template += "<input type='text' id='descriptionValue' name='descriptionValue' value='{4}' readonly='readonly'/>";
		template += "<input type='hidden' id='content' name='content' value='{5}'>";
		template += "<input type='hidden' id='description' name='description' value='{6}'>";
		template += "<input type='hidden' id='questionType' name='questionType' value='{7}'>";
		template += "<input type='hidden' id='isValidateFraction' name='isValidateFraction' value='{8}'>";
		template += "<input type='hidden' id='fraction' name='fraction' value='{9}'>";
		template += "<input type='hidden' id='isAddUp' name='isAddUp' value='{10}'>";
		template += "<input type='hidden' id='fractionLs' name='fractionLs' value='{11}'>";
		template += "</td>";
		template += "</tr>";
	
	$.each(resData, function (index, rowData) {
		if(isQuestionData(rowData)) {
			let targetDesc;
			let fractionLs = rowData.fractionLs;
			let fraction = clearFraction(rowData.fraction);
			let description = clearFraction(rowData.description);
			let targetFraction = rowData.targetFraction;
			let indexStr = count < 9 ? "0" + count : count;
			let isAddUp = rowData.isAddUp == null ? "Y" : rowData.isAddUp;

			// 備註文字
			if(isAddUp=="N") {
				if(note.length==0) {
					note += "※";
				}
				note += "第" + (index + 1) + "項、";
			}

			$.each(fractionLs ,function(index2, fraction) {
				if (fraction != 0 && fraction == targetFraction) {
					targetDesc = description.split(';');
					targetDesc = targetDesc[index2] ? targetDesc[index2] : "";
					//衝擊分析加總:isAddUp=Y才須加總
					if(isAddUp=="Y") {
						totalFraction += parseInt(targetFraction);
					}
				}
			});
		
			let resultStr = StringUtil.format(
					template, 
					indexStr, 
					rowData.content, 
					count,
					targetFraction ? targetFraction : '0', 
					targetDesc ? targetDesc : '', 
					rowData.content,
					description, 
					rowData.questionType, 
					rowData.isValidateFraction,
					fraction,
					isAddUp,
					ObjectUtil.stringify(fractionLs));
			
			count++;
			$("#formImpactAnalysisTable").append(resultStr);
		} else if(rowData.questionType == "E" ) {
			$("#evaluation").val(rowData.description);
		} else if(rowData.questionType == "S" ) {
			$("#solution").val(rowData.description);
		}
	});

	setTotalFractionText(totalFraction);
	setNote(note);
}

function clearFraction (fractions) {
	var swap = '';
	var split = fractions.split(';');
	
	$.each(split, function (i, v) {
		if (v && v != '0') {
			swap += (v + ';');
		}
	});
	
	return swap;
}

//如果questionType為 E 或 S 或 T,則代表是影響評估跟因應措施的資料,直接塞值即可
function isQuestionData (element) {
	return (element.questionType != "E" && element.questionType != "S" && element.questionType != "T");
}

function setTotalFractionText (totalFraction) {
	$("span#totalFractionDisplay").text(totalFraction);
	let isNewSystem =  HtmlUtil.tempData.infoForm.isNewSystem;
	let isNewService = HtmlUtil.tempData.infoForm.isNewService;
	
	//衝擊分析大於1000分則將變更等級的文字改為重大變更
	if(totalFraction >= 1000) {
		$("span#changeRank").text("<s:message code='form.change.changeRank.2' text='重大變更'/>");
	} else {
		if((isNewSystem && 'Y' == isNewSystem) || (isNewService && 'Y' == isNewService)) {
			$("span#changeRank").text("<s:message code='form.change.changeRank.2' text='重大變更'/>");
		} else {
			$("span#changeRank").text("<s:message code='form.change.changeRank.1' text='次要變更'/>");
		}
	}
	
	$("input#totalFraction").val(totalFraction);
	HtmlUtil.tempData.infoForm.changeRank = $("span#changeRank").text();
}

// 目標衝擊分析歸0且清空說明文字
function clearTargetFraction(rowData) {
	$(rowData).closest('tr').find('#descriptionValue').val("");
	$(rowData).closest('tr').find('#targetFraction').val(0).change();
}

// 備註文字: 第{}項、第{}項，不列入變更衝擊分析總分計算。
function setNote(note) {
	if(note.length > 0) {
		note = note.substring(0, note.length-1) + "不列入變更衝擊分數計算。";
		$("#note").text(note);
	}
}
</script>

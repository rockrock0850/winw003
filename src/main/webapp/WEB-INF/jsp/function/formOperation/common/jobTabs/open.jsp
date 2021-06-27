<%@ page contentType="text/html; charset=UTF-8" %>
<fieldset>
	<legend>OPEN清單</legend>
	<table class="grid_query">
		<tr>
			<th>UCM Project</th>
			<td><input type="text" /></td>
			<th>Stream</th>
			<td><input type="text" /></td>
			<th>view</th>
			<td><input type="text" /></td>
		</tr>
		<tr>
			<td colspan="6">
				<fieldset>
					<legend>Change Set</legend>
					<div class="grid_BtnBar" _style="display:none">
						<button>
							<i class="icon-save"></i> 檢視變更集
						</button>
					</div>
					<table class="grid_list">
						<tr>
							<th>Segment名稱</th>
							<th>Key Value</th>
							<th>變更的欄位</th>
							<th>變更內容</th>
							<th>Layout Dataset</th>
						</tr>
						<tbody>

						</tbody>
					</table>
					<section id="pagerObj" class="pager">
						<button>
							<i class="icon-pager-first"></i>
						</button>
						<button>
							<i class="icon-pager-prev"></i>
						</button>
						<span>目前 <input type="text" value="1">
							頁,共1頁&nbsp;總共3筆
						</span>
						<button>
							<i class="icon-pager-next"></i>
						</button>
						<button>
							<i class="icon-pager-last"></i>
						</button>
					</section>
				</fieldset>
			</td>
		</tr>
	</table>
</fieldset>
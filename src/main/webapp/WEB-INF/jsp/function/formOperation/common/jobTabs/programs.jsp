<%@ page contentType="text/html; charset=UTF-8" %>
<fieldset>
	<legend>維護工作單(一)工作清單</legend>
	<table class="grid_query">
		<tr>
			<th>測試系統完成日期</th>
			<td><input type="text" /><a href="#"><i
					class="icon-calculator"></i></a></td>
			<th>WO</th>
			<td><input type="text" /></td>
			<th>單號</th>
			<td><input type="text" /></td>
		</tr>
		<tr>
			<th>連線/批次系統完成日期</th>
			<td><input type="text" /><a href="#"><i
					class="icon-calculator"></i></a></td>
			<th>連線/批次系統實施日期</th>
			<td><input type="text" /><a href="#"><i
					class="icon-calculator"></i></a></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td colspan="6">
				<div class="grid_BtnBar" _style="display:none">
					<button>
						<i class="icon-add"></i> 新增
					</button>
					<button>
						<i class="icon-add"></i> 移除
					</button>
					<button>
						<i class="icon-add"></i> 產生資管工單Online Batch
					</button>
				</div>
				<table class="grid_list">
					<tr>
						<th>名稱</th>
						<th>狀態</th>
						<th>DataSet名稱</th>
						<th>撰寫人</th>
						<th>測試人</th>
						<th>O/B</th>
						<th>種類</th>
						<th>備註</th>
						<th>Source</th>
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
			</td>
		</tr>

	</table>
</fieldset>
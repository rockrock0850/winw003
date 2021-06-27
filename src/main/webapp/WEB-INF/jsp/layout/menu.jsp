<script language="javascript">
var width, oriWidth, newWidth, functions;
var title = '<div>{0}</div>';
var ul = '<ul id="ul{0}"></ul>';
var item = '<li><a realPath="{2}" href="javascript:gotoFunction(\'\/{0}\',{3});" target="_parent">{1}</a></li>';
var dropdown = '<div id="{0}" class="uldropdown"></div>';
var dropdownIds = [];// 儲存dropdown元素的id

$(function () {
	// 選單箭頭左右的縮合
	$('.iframeToggle').click(function () {
		$('main').toggleClass('wide');
		$('.frame_menu').toggleClass('iframeCollapse');
		
		$(this).toggleClass('collapse');
		$(this).find('i').toggleClass(function() {
			if ($(this).hasClass('iconx-pager-prev')) {
				$(this).removeClass();
				return 'iconx-pager-next';
			}
			if ($(this).hasClass('iconx-pager-next')) {
				$(this).removeClass();
				return 'iconx-pager-prev';
			}
		});
		
		$($.fn.dataTable.tables(true)).DataTable().columns.adjust();
        $('.dataTables_scrollBody thead tr').css({'visibility':'collapse'});
        $('.dataTables_scrollBody tfoot tr').css({'visibility':'collapse'});
        $('.dataTables_scrollBody').css({'overflow-y':'hidden'});

		HtmlUtil.saveNewWidth($('.dataTable').attr('style'));
		HtmlUtil.adjustTableWidth();
	});
	
	// 生成選單
	SendUtil.post('/menu/getMenuTree', '', function (resData) {
		// 生成選單DOM
		$.each(resData, function (i, root) {
			// 生成第一層選單
			var $dropdown = $(StringUtil.format(dropdown, root.menuId));

			dropdownIds.push($dropdown.attr('id'));
			$dropdown.append(StringUtil.format(title, root.menuName));

			if (root.subMenus.length <= 0) {
				return;
			}

			// 遞回生成子選單
			var $ul = $(StringUtil.format(ul, root.menuId));
			
			$.each(root.subMenus, function (i, child) {
				let isOpenWindow = child.openWindow;
				findSubMenus(child, $ul,isOpenWindow);// 遞回開始
			});

			$dropdown.append($ul);
			$('div#menu').append($dropdown);
		});
	}, {async: false});

	// 初始化選單
	$.each(dropdownIds, function (i, id) {
		new uldropdown({dropid: id});
	});
	
	functions = $('div#menu').find('a');
});

function findSubMenus (parent, $ulParent,isOpenWindow) {
	if (parent.subMenus.length <= 0) {
		$ulParent.append(StringUtil.format(item, parent.path, parent.menuName, parent.path,isOpenWindow));
		return;
	}

	var $ul = $(StringUtil.format(ul, parent.menuId));
	var $dropdown = $(StringUtil.format(dropdown, parent.menuId));

	dropdownIds.push($dropdown.attr('id'));
	$dropdown.append(StringUtil.format(title, parent.menuName));

	$.each(parent.subMenus, function (i, child) {
		findSubMenus(child, $ul);
	});

	$dropdown.append($ul);
	$ulParent.append($dropdown);
}
</script>

<div class="frame_menu">
	<button class="iframeToggle">
		<i class="iconx-pager-prev"></i>≡
	</button>
	<div id='menu' style="overflow: -moz-scrollbars-vertical; overflow-y: auto; height: 100%;">
		<div class="uldropdown"><div><a href="javascript:gotoFunction('/dashboard');"  target="_parent" style="color:white">首頁</a></div></div>
	</div>
</div>

<script>
$(function () {
	collectBreadcrumbs();
	createBreadcrumbs();
	$("#breadcrumb").jBreadCrumb();
});

function collectBreadcrumbs () {
	$('.uldropdown a').unbind('click').click(function () {
		if (HtmlUtil.popBreadcrumb()) {
			HtmlUtil.flushBreadcrumbs();
		}
		
		var $uls = $(this).closest('ul[id^="ul"]');
		$.each($uls, function () {
			HtmlUtil.putBreadcrumb('', $(this).siblings('div').html());
		});
		HtmlUtil.putBreadcrumb($(this).attr('realPath'), $(this).html());
	});
}

function createBreadcrumbs () {
	var wording, latest;
	var root = 'div#breadcrumb ul';
	var li = '<li><a>{0}</a></li>';
	var href = '<li><a href="${contextPath}/{0}">{1}</a></li>';
	var breadcrumbs = HtmlUtil.getBreadcrumbs();
	
	if (!breadcrumbs) {
		return;
	}
	
	$.each(breadcrumbs, function () {
		if (this.href && this.name) {
			wording = StringUtil.format(href, this.href, this.name);
			$(root).append(wording);
		} else if (this.name) {
			wording = StringUtil.format(li, this.name);
			$(root).append(wording);
		}
	});
	
	wording = $('h1').html();
	latest = breadcrumbs[breadcrumbs.length-1];
	if (latest && wording != latest.name) {
		$(root).append(StringUtil.format(li, wording));
	}
}
</script>

<div class="breadCrumbHolder module">
	<div id="breadcrumb" class="breadCrumb module">
		<ul>
			<li><a href="${contextPath}/dashboard">Home</a></li>
			<c:forEach var='breadcrumb' items='${breadcrumbs}'>
				<li><a href="${contextPath}/${breadcrumb.href}">${breadcrumb.name}</a></li>
			</c:forEach>
		</ul>
	</div>
</div>
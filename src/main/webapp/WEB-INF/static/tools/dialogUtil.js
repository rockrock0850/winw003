//# sourceURL=DialogUtil.js

/**
 * 對話框管理工具
 * @author adam.yeh
 */
var DialogUtil = function () {
	
	var dialog;
	var customLoad;
	var dialogTitleColor = '#007A55';
	var dialogTitleBar = '.ui-dialog-titlebar';
	
	var clickRow = function (table, callback, isAutoClose) {
		if (!callback) {
			return;
		}
		
		$(table + ' tbody').unbind('click').on('click', 'tr', function () {
			callback(TableUtil.getRow($(table).DataTable(), this));
			
			if (isAutoClose) {
				close();
			}
		});
	}
	
	var show = function (d, o) {
		dialog = d;
		
		var option = {
			width: '1000px',
			maxWidth: '1000px',
			resizable: false
		};
		
		open($.extend(option, o));
	}
	
	var showLoading = function (options) {
		if (!customLoad) {
			var o = {
				zIndex: 10,
				overlay: $('#custom-overlay')
	        }
			
			customLoad = $('body').loading($.extend(o, options));
		}

		customLoad.loading('start');
	}
	
	var hideLoading = function () {
		if (customLoad) {
			customLoad.loading('stop');
		}
	}
	
	var close = function () {
		if (dialog) {
			$(dialog).dialog().dialog('destroy');
		}
		HtmlUtil.lockSubmitKey(false);
	}
	
	// 在指定的表格內找到已選取的radio button
	var findRadioChecked = function (table) {
		var checked;
		var radioList = $(table).find('input[type="radio"]');
		
		$.each(radioList, function () {
			if ($(this).is(':checked')) {
				checked = this;
				return false;
			}
		});
		
		return checked;
	}
	
	function open (options) {
		var def = {
			modal : true,
		    position: {
		    	my: 'center' + ' ' + 'center',
		    	at: 'center' + ' ' + 'center'
		    }
		};
		$(dialog)
			.dialog($.extend(def, options))
			.prev(dialogTitleBar)
			.css('background-color', dialogTitleColor);// 客製化對話框標題的背景顏色
		$('.ui-dialog').css('z-index', 3);
		$('.ui-widget-overlay').css('z-index', 2);
	}
	
	return {
		show : show,
		close : close,
		clickRow : clickRow,
		showLoading : showLoading,
		hideLoading : hideLoading,
		findRadioChecked : findRadioChecked
	}
}();
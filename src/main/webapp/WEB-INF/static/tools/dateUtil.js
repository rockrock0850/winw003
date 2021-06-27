//# sourceURL=DateUtil.js

/**
 * 時間管理工具類別
 * 
 * @author adam.yeh
 */
var DateUtil = function () {
	
	var DATE_FORMAT = 'yy/mm/dd';
	var today = new Date();
	var error = 'DateUtil has an error parameters 「{0}」 in the {1}().';
	
	/**
	 * 目前只能轉YYYY-MM-DD,可再自行擴充
	 * date 日期 {TimeStamp}
	 */
	var formatDate = function (date) {
	    var d = new Date(date),
	        month = '' + (d.getMonth() + 1),
	        day = '' + d.getDate(),
	        year = d.getFullYear();

	    if (month.length < 2) 
	        month = '0' + month;
	    if (day.length < 2) 
	        day = '0' + day;

	    return [year, month, day].join('-');
	}
	
	/**
	 * 取得指定日期區間的陣列
	 */
	var getMonths = function(startDate, endDate) {
		var dates = [];
		var currentDate = build(startDate); 
		
		while (currentDate <= build(endDate)) {
			dates.push(currentDate);
			currentDate = addMonths(currentDate, 1);
		}
		
		return dates;
	};
	
	/**
	 * 取得指定日期區間的陣列
	 */
	var getDates = function(startDate, endDate) {
		var dates = [];
		var currentDate = build(startDate); 
		
		while (currentDate <= build(endDate)) {
			dates.push(currentDate);
			currentDate = addDays(currentDate, 1);
		}
		
		return dates;
	};

	/**
	 * 將當前時間增加為"指定年"數
	 * 
	 * date 日期 {TimeStamp or date string}
	 */
	var addYears = function (date, years) {
		if (!date || !years) {
			alert(StringUtil.format(error, '!date || !years', 'addYears'));
			return;
		}
		
		date = build(date);
		date.setFullYear(date.getFullYear() + years);
		
		return date;
	};

	/**
	 * 將當前時間增加為"指定月"數
	 * 
	 * date 日期 {TimeStamp or date string}
	 */
	var addMonths = function (date, months) {
		if (!date || !months) {
			alert(StringUtil.format(error, '!date || !months', 'addMonths'));
			return;
		}
		
		date = build(date);
		date.setMonth(date.getMonth() + months);
		
		return date;
	};

	/**
	 * 將當前時間增加為"指定天"數
	 * 
	 * date 日期 {TimeStamp or date string}
	 */
	var addDays = function (date, days) {
		if (!date || !days) {
			alert(StringUtil.format(error, '!date || !days', 'addDays'));
			return;
		}
		
		date = build(date);
		date.setDate(date.getDate() + days);
		
		return date;
	};
	
	/**
	 * 將當前時間增加為"指定分鐘"數
	 * 
	 * time 日期 {TimeStamp or date string}
	 */
	var addMinutes = function (time, minutes) {
		if (!time || !minutes) {
			alert(StringUtil.format(error, '!time || !minutes', 'addMinutes'));
			return;
		}
		
		var dateTime = build(time);
		dateTime.setMinutes(dateTime.getMinutes() + minutes);
		
		return toDateTime(dateTime.getTime());
	}
	
	/**
	 * 將當前時間增加為"指定小時"數
	 * 
	 * time 日期 {TimeStamp or date string}
	 */
	var addHours = function (time, hours) {
		if (!time || !hours) {
			alert(StringUtil.format(error, '!time || !hours', 'addHours'));
			return;
		}
		
		var dateTime = build(time);
		dateTime.setHours(dateTime.getHours() + hours);
		
		return toDateTime(dateTime.getTime());
	}
	
	/**
	 * 將當前時間增加為"指定秒鐘"數
	 * 
	 * time 日期 {TimeStamp or date string}
	 */
	var addSeconds = function (time, seconds) {
		if (!time || !seconds) {
			alert(StringUtil.format(error, '!time || !seconds', 'addSeconds'));
			return;
		}
		
		var dateTime = build(time);
		dateTime.setSeconds(dateTime.getSeconds() + seconds);
		
		return toDateTime(dateTime.getTime());
	}
	
	/**
	 * 格式化TimeStamp為日期
	 * 
	 * time 日期 {TimeStamp or date string}
	 */
	var toDate = function (time) {
		if (!time || parseInt(time) < 0) {
			// Must be empty because dataTable will broken when not.
			return '';
		}
		
		var dateTime = build(time);
		var year = dateTime.getFullYear();
		var month = dateTime.getMonth() + 1;
		var date = dateTime.getDate();

		month = month.toString().length < 2 ? '0' + month : month; 
		date = date.toString().length < 2 ? '0' + date : date; 
		
		return year + "/" + month + "/" + date;
	}
	
	/**
	 * 格式化TimeStamp為日期+時間
	 * 
	 * time 日期 {TimeStamp or date string}
	 */
	var toDateTime = function (time) {
		if (!time || parseInt(time) < 0) {
			// Must be empty because dataTable will broken when not.
			return '';
		}
		
		var dateTime = build(time);
		var year = dateTime.getFullYear();
		var month = dateTime.getMonth() + 1;
		var date = dateTime.getDate();
		var hour = dateTime.getHours();
		var minute = dateTime.getMinutes();
		var second = dateTime.getSeconds();

		month = month.toString().length < 2 ? '0' + month : month; 
		date = date.toString().length < 2 ? '0' + date : date; 
		hour = hour.toString().length < 2 ? '0' + hour : hour;
		minute = minute.toString().length < 2 ? '0' + minute : minute;
		second = second.toString().length < 2 ? '0' + second : second;
		
		return year + "/" + month + "/" + date + " " + hour + ":" + minute + ":" + second;
	}
	
	/**
	 * 將時間字串轉型為TimeStamp
	 */
	var fromDate = function(dateStr) {
		if(dateStr != null && dateStr != "" && dateStr != undefined) {
			return new Date(dateStr).getTime();
		} else {
			return null;
		}
	}
	
	/**
	 * 初始化Date picker
	 */
	var datePicker = function (options) {
		var $target = $('input.initDatePicker');

		var o = {
		    showButtonPanel: true,
            changeYear: true,
            changeMonth: true,
            minDate: '',
			dateFormat : DATE_FORMAT, 
			showOn: 'button',
            buttonText: '<i class="iconx-calendar">曆</i>',
		    beforeShow: function (input) {
		    	addClearButton(input);
		    	addTodayButtonListener(input);
		    },
		    onChangeMonthYear: function (year, month, instance) {
		    	addClearButton(instance.input);
		    	addTodayButtonListener(instance.input);
		    }
		};

		o = $.extend(o, options);
		$target.datepicker('destroy');
		$target.datepicker(o).next(".ui-datepicker-trigger").addClass("custom-picker");
	}

	/**
	 * 初始化Date picker 以 ID 綁定
	 */
	var datePickerById = function (id, options) {
		var o = {
		    showButtonPanel: true,
            changeYear: true,
            changeMonth: true,
            minDate: '',
			dateFormat : DATE_FORMAT, 
			showOn: 'button',
            buttonText: '<i class="iconx-calendar">曆</i>',
		    beforeShow: function (input) {
		    	addClearButton(input);
		    	addTodayButtonListener(input);
		    },
		    onChangeMonthYear: function (year, month, instance) {
		    	addClearButton(instance.input);
		    	addTodayButtonListener(instance.input);
		    }
		};

		o = $.extend(o, options);
		$(id).datepicker('destroy');
		$(id).datepicker(o).next(".ui-datepicker-trigger").addClass("custom-picker");
	}	
	
	/**
	 * 初始化Date Time picker
	 */
	var dateTimePicker = function (options) {
		var $target = $('input.initDateTimePicker');

		var o = {
		    showButtonPanel: true,
            changeYear: true,
            changeMonth: true,
            minDate: '',
			dateFormat : DATE_FORMAT, 
			timeFormat : 'HH:mm:ss', 
			showOn: 'button',
            buttonText: '<i class="iconx-calendar">曆</i>',
		    beforeShow: function (input) {
		    	addClearButton(input);
		    	addTodayButtonListener(input);
		    },
		    onChangeMonthYear: function (year, month, instance) {
		    	addClearButton(instance.input);
		    	addTodayButtonListener(instance.input);
		    }
		};

		o = $.extend(o, options);
		$target.datetimepicker('destroy');
		$target.datetimepicker(o).next(".ui-datepicker-trigger").addClass("custom-picker");;
	}

	var dateTimePickerById = function (id, options) {
		var o = {
		    showButtonPanel: true,
            changeYear: true,
            changeMonth: true,
            minDate: '',
			dateFormat : DATE_FORMAT, 
			timeFormat : 'HH:mm:ss', 
			showOn: 'button',
            buttonText: '<i class="iconx-calendar">曆</i>',
		    beforeShow: function (input) {
		    	addClearButton(input);
		    	addTodayButtonListener(input);
		    },
		    onChangeMonthYear: function (year, month, instance) {
		    	addClearButton(instance.input);
		    	addTodayButtonListener(instance.input);
		    }
		};

		o = $.extend(o, options);
		$(id).datetimepicker('destroy');
		$(id).datetimepicker(o).next(".ui-datepicker-trigger").addClass("custom-picker");;
	}
	
	/**
	 * 初始化Time picker
	 */
	var timePicker = function (options) {
		var $target = $('input.initTimePicker');

		var o = {
		    showButtonPanel: true,
			timeFormat : 'HH:mm:ss', 
			showOn: 'button',
            buttonText: '<i class="iconx-calendar">曆</i>',
		    beforeShow: function (input) {
		    	addClearButton(input);
		    	addTodayButtonListener(input);
		    }
		};

		o = $.extend(o, options);
		$target.timepicker('destroy');
		$target.timepicker(o).next(".ui-datepicker-trigger").addClass("custom-picker");;
	}
	
	function build (time) {
		var dateTime;
		
		if ($.isNumeric(time)) {
			dateTime = new Date();
			dateTime.setTime(time);
		} else {
			dateTime = new Date(time);
		}
		
		return dateTime;
	}

	function addTodayButtonListener (input) {
    	setTimeout(function() {
            var buttonPanel = $(input)
                .datepicker("widget")
                .find(".ui-datepicker-buttonpane");
            
            buttonPanel.find('.ui-datepicker-current').click(function () {
            	var dateTime = new Date();
        		var hour = dateTime.getHours();
        		var minute = dateTime.getMinutes();
        		var second = dateTime.getSeconds();

        		hour = hour.toString().length < 2 ? '0' + hour : hour;
        		minute = minute.toString().length < 2 ? '0' + minute : minute;
        		second = second.toString().length < 2 ? '0' + second : second;
        		
            	$(input).datepicker('setDate', dateTime);
            	
            	var $parent = $(input).parent();
            	$parent.find('input#hours').val(hour);
            	$parent.find('input#minutes').val(minute);
            	$parent.find('input#seconds').val(second);
            });
    	}, 1);
	}
	
	function addClearButton (input) {
		setTimeout(function () {
            var buttonPanel = $(input)
                .datepicker("widget")
                .find(".ui-datepicker-buttonpane");

            $("<button>", {
                text: "清除",
                click: function() {
                	$(input).val('');
                	
                	var $parent = $(input).parent();
                	$parent.find('input#hours').val('');
                	$parent.find('input#minutes').val('');
                	$parent.find('input#seconds').val('');
                }
            })
            .appendTo(buttonPanel)
            .addClass("ui-datepicker-clear ui-state-default ui-priority-primary ui-corner-all");
        }, 1);
	}
	
	return {
		datePicker : datePicker,
		datePickerById : datePickerById,
		dateTimePickerById : dateTimePickerById,
		dateTimePicker : dateTimePicker,
		timePicker : timePicker,
		toDate : toDate,
		toDateTime : toDateTime,
		fromDate : fromDate,
		addMinutes : addMinutes,
		addHours : addHours,
		addSeconds : addSeconds,
		addMonths : addMonths,
		addDays : addDays,
		getDates : getDates,
		getMonths : getMonths,
		addYears : addYears,
		formatDate : formatDate
	}
}();
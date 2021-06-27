//# sourceURL=ObjectUtil.js

/**
 * 統一取得物件工具
 * 
 * @author andrewLee, adam.yeh, emily.lai
 */
var ObjectUtil = function () {
	
	/**
	 * 移除物件內為空的欄位
	 */
	var clear = function  (obj) {
		$.each(keys(obj), function (i, attr) {
			if (!obj[attr] && !($.type(obj[attr]) === 'boolean')) {
				delete obj[attr];
			}
		});
	}
	
	/**
	 * 移除陣列內指定元素<br>
	 * 不支援物件
	 */
	var arrayRemove = function (arr, value) {
		return $.grep(arr, function(element, i) {
		    return stringify(element) != stringify(value);
		});
	}
	
	/**
	 * 將前端的日期字串與布林轉成後端儲存用的資料
	 * 
	 * @param dateArgs : 指定哪些欄位內的值需要由日期轉為time stamp
	 * @param ignores : 直接在jsp上新增隱藏的input設定type=checkbox, 這樣就不會自動轉成Y/N
	 * @author AdamYeh
	 */
	var dataToBackend = function (obj, dateArgs) {
		var ignores = [];
		var booleans = $('form#headForm input[type=checkbox]');
		
		$.each(booleans, function () {
			ignores.push($(this).attr('name'));
		});
		
		if (obj.hasOwnProperty('knowledge2') && obj.knowledge2) {
			obj.knowledge2 = obj.knowledge2.toString();// Array to string with comma.
		}
		
		booleanToYN(obj, ignores);
		dateToStamp(obj, dateArgs);
		
		return obj;
	}

	/**
	 * 將輸入物件內有欄位為布林的值轉Y/N字串
	 * @author AdamYeh
	 */
	var booleanToYN = function(obj, ignores) {
		$.each(keys(obj), function (i, key) {
			if (ignores && $.inArray(key, ignores) >= 0) {
				return true;// continue
			}
			
			if (typeof obj[key] === 'boolean') {
				obj[key] = obj[key] ? 'Y' : 'N';
			}
		});
	}

	/**
	 * 將輸入物件內有欄位為Y/N字串的值轉為布林
	 * @author AdamYeh
	 */
	var ynToBoolean = function(obj, ignores) {
		$.each(keys(obj), function (i, key) {
			if (ignores && $.inArray(key, ignores) >= 0) {
				return true;// continue
			}
			
			if (obj[key] == 'Y' || obj[key] == 'N') {
				obj[key] = obj[key] == 'Y';
			}
		});
	}
	
	/**
	 * 將輸入物件內有指定的欄位內的日期轉為後端要用的time stamp
	 * 
	 * @param obj : jQuery 物件
	 * @author AdamYeh
	 */
	var dateToStamp = function (obj, args) {
		if (!args) {
			return;
		}
		
		$.each(args, function (i) {
			obj[this] = DateUtil.fromDate(obj[this]);
		});
	}
	
	/**
	 * 將輸入物件內有指定的欄位內的time stamp轉為前端要用的日期字串
	 * 
	 * @param obj : jQuery 物件
	 * @author AdamYeh
	 */
	var stampToDate = function (obj, args) {
		$.each(args, function (i) {
			obj[this] = DateUtil.toDate(obj[this]);
		});
	}
	
	/**
	 * 傳入formInfo,自動帶入表單內的值,若為下拉選單,則會觸發其onChange()事件
	 * 
	 * 請注意,個別情況下,直接使用本function不一定可以將所有值都正確的帶入欄位中,請額外再進行檢查並且手動塞值
	 * 
	 * ****注意事項****
	 * 1.塞值的時候,HTML tag id的命名必須與物件中的key一致
	 * 2.若為多個Select之間有相依性關係的話,需確認key的順序無誤,否則會出現主Select還沒被賦予值的情況下,子Select先被塞值,因下拉選單未產生的關係,導致該欄位為空白
	 * *************
	 */
	var autoSetColumnValue = function(info) {
		$.each(info,function(key,value) {
			let target = $("#" + key);//直接透過Key來作為Id,找到該HTML TAG
			
			if(target.attr("type") == "checkbox") {
				if(value == true) {//先判斷value是否為true,是的話直接塞值無需在做判斷
					target.prop("checked",value);
				} else {
					let checked = (value == "Y");
					target.prop("checked",checked);
				}
			
			} else if(target.attr("type") == "radio") {
				if(value == true) {//先判斷value是否為true,是的話直接塞值無需在做判斷
					target.prop(":checked",value);
				} else {
					let checked = (value == "Y");
					target.prop(":checked",checked);
				}
				
			} else if(target.is("select")) {//若為select 則自動帶入onChange事件
				if(value) {
					target.val(value).change();
				}
			} else {
				target.val(value);
			}
		});
	}
	
	/**
	 * 用form欄位ID去物件找值
	 * 
	 * @param data
	 * @param htmlFormId 指定要寫入值的表單ID
	 * @param ignoreColumns 指定要忽略的欄位ID
	 */
	var autoSetFormValue = function (data, htmlFormId, ignoreColumns) {
	    if (!data || !htmlFormId) {
	        return;
	    }
	    
	    var inputs = $('#' + htmlFormId).find(':input');

	    $.each(inputs, function (idx, obj) {
	        let target = $(obj);
	        
            if (isIgnores(target, ignoreColumns)) {
                return true;// continue
            }
	        
            let value = data[target.attr("id")];
        	
	        if (!value) {
	        	value = data[target.attr("name")];
	        } 
	        
	        if (!value && !(typeof(value) == "boolean")) {
	        	return true;// continue
	        }
	        
	        if (isDatePicker(target, value)) {
	            target.val(DateUtil.toDateTime(value));
	        } else if (isTrueFalse(target)) {
                if (typeof(value) == "boolean") { 
                    target.prop("checked", value);
                } else if (typeof(value) === 'string') {
                    let checked = (value == "Y");
                    target.prop("checked", checked);
                }
            } else if (isMultiSelect(target, value)) {
            	target.val(value).change();
            	target.multiselect('reload' );
            } else if (isSelect(target, value)) {
                target.val(value).change();
            } else {
                target.val(value.toString());
            }
	    });
	}
	
	/**
	 * For IE11 or latest version. 
	 * Because it not support Object.keys() from jQuery native API.
	 */
	var keys = function (obj) {
		var keys = [];

	    for (var i in obj) {
	      if (obj.hasOwnProperty(i)) {
	        keys.push(i);
	      }
	    }

	    return keys;
	}
	
	var parse = function (str) {
		if (!str) {
			return '';
		}

		// 跳脫pageing的SQL的斷行符號
		str = str.replace(/\n/g, "\\n");
		
		return JSON.parse(str);
	}
	
	var stringify = function (obj) {
		if (!obj) {
			return '';
		}
		
		return JSON.stringify(obj);
	}
	
	function isDatePicker (target, value) {
		return (target.hasClass("initDateTimePicker") && value);
	}
	
	function isSelect (target, value) {
		return (target.is("select") && value);
	}
	
	function isMultiSelect (target, value) {
		return (target.is("select") && value && target.prop('multiple'));
	}
	
	function isTrueFalse (target) {
		return (target.attr("type") == "checkbox" || target.attr("type") == "radio");
	}
    
    function isIgnores (target, ignoreColumns) {
    	return (ignoreColumns && $.inArray(target.attr("id"), ignoreColumns) >= 0);
    }
	
	return {
		keys : keys,
		dateToStamp : dateToStamp,
		stampToDate : stampToDate,
		booleanToYN : booleanToYN,
		ynToBoolean : ynToBoolean,
		dataToBackend : dataToBackend,
		autoSetColumnValue : autoSetColumnValue,
		autoSetFormValue : autoSetFormValue,
		parse : parse,
		stringify : stringify,
		arrayRemove : arrayRemove,
		clear : clear
	}
}();
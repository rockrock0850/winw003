//# sourceURL=SessionUtil.js

/**
 * Session處理工具
 * 
 * @author adam.yeh
 */
var SessionUtil = function () {

	var error = 'SessionUtil has an error parameters 「{0}」 in the {1}().';
	
	/**
	 * 取得查詢紀錄
	 */
	var fetch = function (recordId, callback) {
		if (!recordId || !callback) {
			alert(StringUtil.format(error, 'recordId or callback', 'fetch'));
			return;
		}
		
		var record = SessionUtil.get(recordId);
		
		if (record) {
			record = ObjectUtil.parse(record);
			callback(record);
		}
	}
	
	/**
	 * 儲存查詢紀錄
	 */
	var record = function (recordId, conditions) {
		if (!recordId) {
			alert(StringUtil.format(error, 'recordId', 'record'));
			return;
		}
		
		set(recordId, conditions);
	}
	
	var set = function (key, v) {
		if (!key) {
			alert(StringUtil.format(error, 'key', 'set'));
			return;
		}
		
		if (!$.isNumeric(v)) {
			v = ObjectUtil.stringify(v);
		}
		
		localStorage.setItem(key, v);
	}
	
	var get = function (key) {
		if (!key) {
			alert(StringUtil.format(error, 'key', 'get'));
			return;
		}
		
		return localStorage.getItem(key);
	}
	
	var remove = function (key) {
		if (!key) {
			alert(StringUtil.format(error, 'key', 'remove'));
			return;
		}
		
		localStorage.removeItem(key);
	}
	
	/**
	 * except : array of except item keys
	 */
	var clear = function (except) {
		var def = ['breadcrumb'];
		
		if (isArray(except)) {
			def = $.merge(def, except);
		}
		
		$.each(ObjectUtil.keys(localStorage), function (i, k) {
			if ($.inArray(k, def) != -1) {
				return true;// continue
			}
			
			remove(k);
		});
	}
	
	function isArray (arr) {
		return (arr instanceof Array) && (arr.length > 1);
	}
	
	return {
		set : set,
		get : get,
		clear : clear,
		remove : remove,
		fetch : fetch,
		record : record
	}
}();
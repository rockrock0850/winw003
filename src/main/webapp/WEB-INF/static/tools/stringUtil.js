//# sourceURL=StringUtil.js

/**
 * 字串處理 工具類別
 * 
 * @author adam.yeh
 */
var StringUtil = function () {
	
	var error = 'StringUtil has an error parameters 「{0}」 in the {1}().';
	
	/**
	 * 從GroupName取得登入者的職位
	 */
	var spLoginTitle = function (userInfo) {
		if (!userInfo) {
			alert(StringUtil.format(error, 'userInfo', 'spLoginTitle'));
			return;
		}
		
		var groups = userInfo.groupName.split('_');
		
		return groups[1] ? groups[1] : groups[0]; 
	}
	
	var isEmail = function (email) {
		if (!email) {
			alert(StringUtil.format(error, 'email', 'isEmail'));
			return;
		}

		var reg = /^\b[A-Z0-9._-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b$/i;
		
		if (!reg.test(email)) {
			alert('請輸入正確格式的電子郵件。');
			return false;
		}
		
		return true;
	}
	
	/**
	 * 檢核一般電話號碼格式是否正確
	 */
	var isTelephone = function (phone) {
		if (!phone) {
			alert(StringUtil.format(error, 'phone', 'isTelephone'));
			return;
		}

		var length = phone.length;
		var reg = /([0-9]{2})-([0-9]{7})/;
		
		if (!(length == 10 && reg.test(phone))) {
			alert('請輸入正確格式的電話號碼。');
			return false;
		}
		
		return true;
	}
	
	/**
	 * 檢核手機號碼格式是否正確
	 */
	var isPhoneNumber = function (phone) {
		if (!phone) {
			alert(StringUtil.format(error, 'phone', 'isPhoneNumber'));
			return;
		}

		var length = phone.length;
		var reg = /([0-9]{4})-([0-9]{6})/;
		
		if (!(length == 11 && reg.test(phone))) {
			alert('請輸入正確格式的手機號碼。');
			return false;
		}
		
		return true;
	}
	
	/**
	 * 客製化Java內的format方法
	 */
	var format = function () {
		if (arguments.length == 0) {
	        return null;
		}
		
	    var str = arguments[0];
	    
	    for (var i = 1; i < arguments.length; i++) {
	        var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
	        str = str.replace(re, arguments[i]);
	    }
	    
	    return str;
	}
	
	return {
		format: format,
		isEmail : isEmail,
		isTelephone : isTelephone,
		spLoginTitle : spLoginTitle,
		isPhoneNumber : isPhoneNumber
	}
	
}();
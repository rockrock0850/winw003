//# sourceURL=FileUtil.js

/**
 * 檔案處理工具
 * 
 * @author adam.yeh
 */
var FileUtil = function () {
	
	var file;
	
	/**
	 * 實作全客製化檔案下載
	 */
	var download = function (action, data) {
		DialogUtil.showLoading();
		
		var xhr = new XMLHttpRequest();
		xhr.open('POST', contextPath + action, true);
		xhr.responseType = 'arraybuffer';
		xhr.onload = function() {
			if (this.status === 200) {
				var filename = "";
				var disposition = xhr.getResponseHeader('Content-Disposition');
				
				if (disposition && disposition.indexOf('attachment') !== -1) {
					var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
					var matches = filenameRegex.exec(disposition);
					if (matches != null && matches[1]) {
						filename = matches[1].replace(/['"]/g, '');
					}
				}
				
				var type = xhr.getResponseHeader('Content-Type');
				var blob = typeof File === 'function' ? 
						new File ([ this.response ], filename, {type : type}) : 
						new Blob([ this.response ], {type : type});
						
				if (typeof window.navigator.msSaveBlob !== 'undefined') {
					// IE workaround for "HTML7007: One or more blob URLs were revoked by closing the blob for which they were created. 
					// These URLs will no longer resolve as the data backing the URL has been freed."
					window.navigator.msSaveBlob(blob, filename);
				} else {
					var URL = window.URL || window.webkitURL;
					var downloadUrl = URL.createObjectURL(blob);
					
					if (filename) {
						// use HTML5 a[download] attribute to specify filename
						var a = document.createElement("a");
						
						// safari doesn't support this yet
						if (typeof a.download === 'undefined') {
							window.location = downloadUrl;
						} else {
							a.href = downloadUrl;
							a.download = filename;
							document.body.appendChild(a);
							a.click();
						}
					} else {
						window.location = downloadUrl;
					}
					
					setTimeout(function() {
						URL.revokeObjectURL(downloadUrl);
					}, 100); // cleanup
				}
			}

			DialogUtil.hideLoading();
		};
		xhr.setRequestHeader('Content-type', 'application/json;charset=utf-8;');
		xhr.send(ObjectUtil.stringify(data));
	}
	
	/**
	 * 清空已讀取的檔案
	 */
	var reset = function () {
		file = {};
	}
	
	/**
	 * 將網頁檔案轉換成FormData型態檔案
	 * 
	 * k formData的key
	 * v 檔案
	 */
	var readAsFile = function (k, v) {
		file = new FormData();
		file.append(k, v);
	}
	
	/**
	 * 取得FormData型態的檔案資料
	 */
	var getFormFile = function () {
		return file;
	}
	
	/**
	 * 上傳
	 * 
	 * formFile JS FormData物件
	 */
	var upload = function (uri, formFile, callback) {
		file = {};
		var options = {
			data : formFile,
			cache : false,
			contentType : false,
			processData : false
		};
		SendUtil.post(uri, '', callback, options, true);
	}
	
	return {
		reset : reset,
		download : download,
		getFormFile : getFormFile,
		upload : upload,
		readAsFile : readAsFile
	}
}();
//# sourceURL=PurifyUtil.js

/**
 * 資安處理工具
 * 
 * @author adam.yeh
 */
var PurifyUtil = function () {
	
	/**
	 * 檢核Cross-Side Scripting: DOM
	 */
	var dom = function (evil) {
		if (isExcludeType(evil)) {
			return evil;
		}
		
//		return DOMPurify.sanitize(evil);
		return evil.replace(/<[^>]+>/g, '');
	}
	
	/**
	 * 檢核物件內容是否有Cross-Side Scripting: DOM
	 * TODO 有時間要改成遞回實現深度走訪巢狀物件
	 */
	var obj = function (evil) {
		$.each(ObjectUtil.keys(evil), function (i, key) {
			if (isObj(evil[key])) {
				return true;// continue
			}
			
			evil[key] = dom(evil[key]);
		});
	}
	
	/**
	 * 檢核URL是否掛問號+參數
	 */
	var uri = function (evil) {
		if (!validateURL(evil)) {
			alert('This is none RESTful query !');
			evil = null;
		}
		
		return decodeURI(encodeURI(evil));
	}
	
	function isObj (data) {
		return (data instanceof Array) || $.isPlainObject(data);
	}
	
	function validateURL (evil) {
        return !parseURL(evil).query;
    }
	
	function isExcludeType (evil) {
		return !evil || 
			($.type(evil) === "boolean") || 
			($.type(evil) === 'function' || 
			($.type(evil) === 'number'));
	}
	
    // var myURL = parseURL('http://abc.com:8080/dir/index.html?id=255&m=hello#top');
    // myURL.file;     // = 'index.html'
    // myURL.hash;     // = 'top'
    // myURL.host;     // = 'abc.com'
    // myURL.query;    // = '?id=255&m=hello'
    // myURL.params;   // = Object = { id: 255, m: hello }
    // myURL.path;     // = '/dir/index.html'
    // myURL.segments; // = Array = ['dir', 'index.html']
    // myURL.port;     // = '8080'
    // myURL.protocol; // = 'http'
    // myURL.source;   // = 'http://abc.com:8080/dir/index.html?id=255&m=hello#top'
	function parseURL(url) {
        var a = document.createElement('a');
        a.href = decodeURI(encodeURI(url));
        return {
            source: url,
            protocol: a.protocol.replace(':', ''),
            hostname: a.hostname,
            host: a.host,
            port: a.port,
            query: a.search,
            params: (function () {
                var ret = {},
                    seg = a.search.replace(/^\?/, '').split('&'),
                    len = seg.length, i = 0, s;
                for (; i < len; i++) {
                    if (!seg[i]) { continue; }
                    s = seg[i].split('=');
                    ret[s[0]] = s[1];
                }
                return ret;
            })(),
            file: (a.pathname.match(/\/([^\/?#]+)$/i) || [, ''])[1],
            hash: a.hash.replace('#', ''),
            path: a.pathname.replace(/^([^\/])/, '/$1'),
            relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [, ''])[1],
            segments: a.pathname.replace(/^\//, '').split('/')
        };
    }
	
	return {
		dom : dom,
		obj : obj,
		uri : uri
	};
}();
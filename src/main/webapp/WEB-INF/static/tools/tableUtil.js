//# sourceURL=TableUtil.js

/**
 * 資料表格 工具類別
 * 
 * @author adam.yeh
 */
var TableUtil = function () {
	
	var isOperation = false;// 是否為一般增刪改查操作
	var error = 'TableUtil has an error parameters 「{0}」 in the {1}().';
	
	/**
	 * 實現伺服器端分頁功能
	 * @param options.orderByDef 定義預設排序欄位[Index, 升/降冪]
	 * @param options.orderBy 定義可被排序的欄位(順序需要跟columnDefs一樣)
	 */
	var paging = function (table, options) {
		if (!options || 
				!options.orderBy || 
				!options.request || 
				!options.queryPath) {
			alert(StringUtil.format(error, 'options', 'paging'));
			return;
		}
		
		var o = {
			serverSide: true,
		    processing: true,
			ajax: function (data, callback, settings) {
			    var start = data.start;						// 起始資料行號
			    var page = (data.start) / data.length + 1;	// 當前頁碼
			    var pagesize = data.length;					// 每頁資料筆數
			    var request = options.request;				// 請求參數
			    
			    request.page = page;
			    request.isPaging = true;
			    request.pagesize = pagesize;
			    request.orderBy = getDynamicOrder(options, data);
			    request.sorting = getDynamicSorting(options, data);
			    
			    if (options.isGET) {
				 	SendUtil.get(options.queryPath, request, function (response) {
				 		drawPaging(response, data, callback);
					}, null, true);
			    } else {
				 	SendUtil.post(options.queryPath, request, function (response) {
				 		drawPaging(response, data, callback);
					}, null, true);
			    }
			}
		};
		
		return init(table, $.extend(o, options));
	}

	/**
	 * 初始化資料表格
	 * 
	 * @param options.drew(dataTable) 定義畫面更新之後要做的事情
	 * @param options.checkbox 定義畫面更新之後是否初始化checkbox的狀態
	 * @param options.count 設定true之後會在第一欄資料列內產生流水號。預設false。
	 * @param options.init 設定表格初始化之後要做甚麼事(一次性)。
	 */
	var init = function (table, options) {
		if (!table) {
			alert(StringUtil.format(error, 'table', 'init'));
			return;
		}
		
		var dataTable;
		var o = {
			dom: '<"dt-center"h>lfrtip',
			paging: true,
			pagingType: 'full_numbers',
			lengthChange: false,
			pageLength: 10,
			searching: false,
			ordering: true,
			order: [],
			info: true,
			autoWidth: false,
			scrollX: true,
            initComplete: function (settings, json) {
            	dataTable = settings.oInstance.api();
            	disableRedundantHeader();
            	invokeCustomOptions(dataTable, options);
            },
            drawCallback: function (settings, json) {
            	dataTable = settings.oInstance.api();
            	invokeCustomOptions(dataTable, options);
            	disableRedundantHeader();
            	disableRedundantVerticalScroll();
            }
		};
		
		var dataTable = $(table).DataTable($.extend(o, options));
		HtmlUtil.saveOriWidth($('.dataTable').attr('style'));
		
		return dataTable;
	}

	/**
	 * 刪除已創建的DataTable
	 */
	var deleteTable = function (table) {
		if (!table) {
			alert(StringUtil.format(error, 'table', 'deleteTable'));
			return;
		}

		if ($.fn.dataTable.isDataTable(table)) {
			$(table).DataTable().destroy();
			$(table).find('thead').html('');
			$(table).find('tbody').html('');	
		}
	}	
	
	/**
	 * 新增空的資料在最後一行
	 * 
	 * @param data 物件或陣列, 若是陣列型的dataTable可以不給值
	 */
	var addRow = function (table, data) {
		if (!table) {
			alert(StringUtil.format(error, 'table', 'addRow'));
			return;
		}
		
		var last = table.row(':last').data();
		
		if (last instanceof Array) {
			if (!data) {
				data = last;
			} else if (data.length < last.length) {
				alert(StringUtil.format(error, 'data', 'addRow'));
				return;
			}
		} else {
			data = $.extend(emptyTableObject(last), data);
		}
		
		isOperation = true;
		table.row.add(data).draw().page('last').draw('page');
	}
	
	/**
	 * 編輯所選行內的資料
	 * @param data 物件或陣列, 若是陣列型的dataTable可以不給值
	 * @param row.number 實體資料的index(可以用meta.row找出來)
	 */
	var editRow = function (table, tr, row) {
		if (!table) {
			alert(StringUtil.format(error, 'table', 'editRow'));
			return;
		}

		let all = getRows(segmentTable, {page:'all'});

		isOperation = true;
		all[row.number] = row;
		reDraw(table, all);
		page(table, row.page);
	}
	
	/**
	 * 向上尋找與傳入元素最靠近的tr並刪除行資料
	 * 
	 * @param element 行內任一DOM元素
	 */
	var deleteRow = function (table, element) {
		if (!table || !element) {
			alert(StringUtil.format(error, 'table or element', 'deleteRow'));
			return;
		}

		isOperation = true;
		var $tr = $(element).closest('tr');
		table.row($tr).remove().draw(false);
	}

	/**
	 * 向上尋找與傳入元素最靠近的tr並取得行資料
	 * 
	 * @param element	行內任一DOM元件
	 * @param options	
	 * 		行內客製化的輸入元件, 以JS物件形式傳入。 
	 * 		{欄位名稱 : DOM元件}
	 * 		ex. 
	 * 			options = {summary : 'textarea'}
	 * 			DOM元件範例 : <textarea name='summary' />
	 */
	var getRow = function (table, element, options) {
		if (!table || !element) {
			alert(StringUtil.format(error, 'table or element', 'getRow'));
			return;
		}
		
		var tr = $(element).closest('tr');
		var data = table.row(tr).data();
		
		if (options) {
			renderCustomInputsToRowData(data, tr, options);
		}

		return data;
	}
	
	/**
	 * 自動尋找與傳入元素最接近的td並取得欄資料
	 * 
	 * @param element 	行內任一DOM元素
	 * @param index	第幾欄
	 */
	var getCell = function (table, element, index) {
		if (!table || !element) {
			alert(StringUtil.format(error, 'table or element', 'getCell'));
			return;
		}
		
		var tr = $(element).closest('tr');
		var td =  $(tr).find('td');
		
		return table.cell(td[index]).data();
	}
	
	/**
	 * 取得多行資料
	 * 
	 * @param option.page {current or all} (default current)
	 * ex. {page:'all'}
	 */
	var getRows = function (table, options) {
		if (!table) {
			alert(StringUtil.format(error, 'table', 'getRows'));
			return;
		}
		
		var o = {
			page:'current'
		}
		
		var jsList = [];
		var tbList = table.rows($.extend(o, options)).data();
		
		$.each(tbList, function () {
			jsList.push(this);
		});
		
		return jsList;
	}

	/**
	 * 重畫資料表
	 * 
	 * @param data 新的資料
	 */
	var reDraw = function (table, data, isPaging) {
		if (!table || !data) {
			alert(StringUtil.format(error, 'table or data', 'reDraw'));
			return;
		}

		if (isPaging) {
			deleteTable(table);
			paging(table, data);
		} else {
			table.clear();
			table.rows.add(data);
			table.draw();
		}
		HtmlUtil.saveOriWidth($('.dataTable').attr('style'));
	}
	
	/**
	 * 跳頁
	 * 
	 * @param page : first, next, previous, last, Integer( 頁碼 )
	 */
	var page = function (table, page) {
		if (!table) {
			alert(StringUtil.format(error, 'table', 'page'));
			return;
		}
		
		if ($.isNumeric(page)) {
			page = parseInt(page);
		} else if (page == '' || page == 'undefined') {
			page = 0;
		}
		
		table.page(page).draw('page');
	}
	
	/**
	 * 將物件List轉成陣列List
	 */
	var fromObjList = function (list) {
		if (!list || list.length <= 0) {
			alert(StringUtil.format(error, 'list', 'fromObjList'));
			return [];
		}
		
		var array = [];
		
		$.each(list, function (i, obj) {
			var sub = [];
			
			$.each(ObjectUtil.keys(obj), function (i, key) {
				sub.push(obj[key]);
			});
			
			array.push(sub);	
		});
		
		return array;
	}
	
	/**
	 * 全選
	 * @param checkallBox 	全選按鈕
	 * @param checkboxs 	預備全選的按鈕
	 */
	var checkall = function (checkallBox, checkboxs) {
		$.each($(checkboxs), function () {
			$(this).prop('checked', $(checkallBox).is(':checked'));
		});
	}
	
	/**
	 * 監聽全選按鈕是否依然開啟/關閉
	 * @param checkallBox 	全選按鈕
	 * @param checkboxs 	預備全選的按鈕
	 */
	var checkallChangeEvent = function (checkallBox, checkboxs) {
		var isChecked;
		if ($(checkboxs).length == 0) {
		    $(checkallBox).prop('checked', false);
		    return;
		}
		
		$.each($(checkboxs), function (i) {
			isChecked = $(this).is(':checked');
			
			if(!isChecked){
				$(checkallBox).prop('checked', isChecked);
				return false;
			}
			
			if (i == $(checkboxs).length - 1) {
				$(checkallBox).prop('checked', isChecked);
			}
		});
	}
	
	var isDataTable = function (jsExpression) {
		return $.fn.dataTable.isDataTable(jsExpression);
	}
	
	var getInfo = function (table) {
		return table.page.info();
	}
	
	function disableRedundantHeader () {
        $('.dataTables_scrollBody thead tr').css({'visibility':'collapse'});
        $('.dataTables_scrollBody tfoot tr').css({'visibility':'collapse'});
	}
		
	function drawPaging (response, data, callback) {
		if ($.isArray(response)) {
			var hasData = response.length > 0;
			
			data.data = response;
			data.recordsTotal = 
				hasData ? response[0].recordsTotal : 0;
			data.recordsFiltered = 
				hasData ? response[0].recordsFiltered : 0;
				
			if ($.isArray(data.data) && data.data.length > 0) {
				$.each(data.data, function (i, item) {
					PurifyUtil.obj(item);
				});
			}
			
			callback(data);
		}
	}
	
	function disableRedundantVerticalScroll () {
        $('.dataTables_scrollBody').css({'overflow-y':'hidden'});
	}
	
	function invokeCustomOptions (dataTable, options) {
		if (!isOperation) {	    	
	    	if ('count' in options && options.count) {
				dataTable.column(0).nodes().each(function(cell, i) {
					cell.innerHTML = i += 1;
				});
	    	}
	    	
	    	if ('checkbox' in options && options.checkbox) {
	    		$('#' + dataTable.table().node().id).find(
	    				'input[type="checkbox"]').prop('checked', !options.checkbox);
	    	}

	    	if ('init' in options && options.init) {
	    		options.init(dataTable);
	    	}
	    	
	    	if ('drew' in options) {
	    		options.drew(dataTable);
	    	}
		}
		
		isOperation = false;
	}
	
	function emptyTableObject (obj) {
		var copy = $.extend(true, {}, obj);// 深度複製 ( 防止異動原資料 )
		
		if (copy) {
			$.each(ObjectUtil.keys(copy), function (i, key) {
				copy[key] = null;
			});
		}
		
		return copy;
	}
	
	// 依據傳入的元件類型將資料轉存入table row data
	function renderCustomInputsToRowData (data, tr, options) {
		var target;
		var input = '{0}[name="{1}"]';
		$.each(ObjectUtil.keys(options), function (i, key) {
			target = $(tr).find(StringUtil.format(input, options[key], key));
			
			if (isTrueFalse($(target))) {
				target = $(target).is(':checked');
			} else {
				target = $(target).val();
			}
			
			data[key] = target;
		});
	}
	
	function isTrueFalse (target) {
		return (target.attr("type") == "checkbox" || target.attr("type") == "radio");
	}
    
    function getDynamicSorting (options, data) {
    	let sort = '';
    	let isTabOrder = data.hasOwnProperty('order') && data.order.length > 0;

	    if (isTabOrder) {
	    	sort = data.order[0].dir.toUpperCase();
	    } else if (options.hasOwnProperty('orderByDef')) {
	    	sort = options.orderByDef[1];
	    }
	    
	    return sort;
    }
    
    function getDynamicOrder (options, data) {
    	let orders = [];

	    if (isOrderable(options, data)) {
	    	let col = data.order[0];
	    	orders.push(options.orderBy[col.column]);
	    } else if (options.hasOwnProperty('orderByDef')) {
	    	orders.push(options.orderBy[options.orderByDef[0]]);
	    } else {
	    	orders.push(options.orderBy[0]);
	    }
	    
	    return orders;
    }
	
    function isOrderable (options, data) {
    	let isTabOrder = data.hasOwnProperty('order') && data.order.length > 0;
    	let isOptOrder = options.hasOwnProperty('orderBy') && options.orderBy.length > 0;
    	
    	return isOptOrder && isTabOrder;
    }
	
	return {
		init : init,
		paging : paging,
		getInfo : getInfo,
		isDataTable : isDataTable,
		deleteTable : deleteTable,
		getCell : getCell,
		addRow : addRow,
		editRow : editRow,
		getRow : getRow,
		getRows : getRows,
		deleteRow : deleteRow,
		reDraw : reDraw,
		page : page,
		checkall : checkall,
		fromObjList : fromObjList,
		checkallChangeEvent : checkallChangeEvent
	}
	
}();
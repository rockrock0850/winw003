/**
 * Copyright (c) 2010 Maxim Vasiliev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author Maxim Vasiliev, adam.yeh
 * Date: 10.05.2019
 */
(function() {
	/**
	 * Returns form values represented as Javascript object
	 * "name" attribute defines structure of resulting object
	 *
	 * @param rootNode {Element|String} root form element (or it's id)
	 * @param delimiter {String} structure parts delimiter defaults to '.'
	 */
	window.form2object = function(rootNode, delimiter) {
		rootNode = typeof rootNode == 'string' ? document.getElementById(rootNode) : rootNode;
		delimiter = delimiter || '.';
		var formValues = getFormValues(rootNode);
		var result = {};
		var arrays = {};

		for (var i = 0; i < formValues.length; i++) {
			var value = formValues[i].value;
			var name = formValues[i].name;
			var nameParts = name.split(delimiter);
			var currResult = result;

			for (var j = 0; j < nameParts.length; j++) {
				var arrName;
				var namePart = nameParts[j];

				if (namePart.indexOf('[]') > -1 && j == nameParts.length - 1) {
					arrName = namePart.substr(0, namePart.indexOf('['));

					if (!currResult[arrName]) currResult[arrName] = [];
					currResult[arrName].push(value);
				} else {
					if (namePart.indexOf('[') > -1) {
						arrName = namePart.substr(0, namePart.indexOf('['));
						var arrIdx = namePart.replace(/^[a-z]+\[|\]$/gi, '');

						/*
						 * Because arrIdx in field name can be not zero-based and step can be
						 * other than 1, we can't use them in target array directly.
						 * Instead we're making a hash where key is arrIdx and value is a reference to
						 * added array element
						 */

						if (!arrays[arrName]) arrays[arrName] = {};
						if (!currResult[arrName]) currResult[arrName] = [];

						if (j == nameParts.length - 1) {
							currResult[arrName].push(value);
						}
						else {
							if (!arrays[arrName][arrIdx]) {
								currResult[arrName].push({});
								arrays[arrName][arrIdx] = currResult[arrName][currResult[arrName].length - 1];
							}
						}

						currResult = arrays[arrName][arrIdx];
					} else {
						/* Not the last part of name - means object */
						if (j < nameParts.length - 1) {
							if (!currResult[namePart]) currResult[namePart] = {};
							currResult = currResult[namePart];
						} else {
							currResult[namePart] = value;
						}
					}
				}
			}
		}

		return result;
	};

	function getFormValues(rootNode) {
		var result = [];
		var subresult, fieldValue;
		var currentNode = rootNode.firstChild;

		while (currentNode) {
			if (currentNode.nodeName.match(/INPUT|SELECT|TEXTAREA/i)) {
				result.push({ name: currentNode.name, value: getFieldValue(currentNode)});
			} else {
				subresult = getFormValues(currentNode);
				result = result.concat(subresult);
			}

			currentNode = currentNode.nextSibling;
		}

		return result;
	}

	function getFieldValue(fieldNode) {
		if (fieldNode.nodeName == 'INPUT') {
			if (fieldNode.type.toLowerCase() == 'radio' || fieldNode.type.toLowerCase() == 'checkbox') {
				return fieldNode.checked;
			} else {
				if (!fieldNode.type.toLowerCase().match(/button|reset|submit|image/i)) {
					return fieldNode.value;
				}
			}
		} else {
			if (fieldNode.nodeName == 'TEXTAREA') {
				return fieldNode.value;
			} else {
				if (fieldNode.nodeName == 'SELECT') {
					return getSelectedOptionValue(fieldNode);
				}
			}
		}

		return '';
	}

	function getSelectedOptionValue(selectNode) {
		var multiple = selectNode.multiple;
		if (!multiple) return selectNode.value;

		var result = [];
		for (var options = selectNode.getElementsByTagName("option"), i = 0, l = options.length; i < l; i++) {
			if (options[i].selected) result.push(options[i].value);
		}

		return result;
	}

	/**
	 * @deprecated Use form2object() instead
	 * @param rootNode
	 * @param delimiter
	 */
	window.form2json = window.form2object;

})();
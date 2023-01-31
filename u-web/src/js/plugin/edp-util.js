export const util = {
	/**
	 * 获取转换比例
	 */
	pxToRpx: () => {
		if (util.platform() != "pc") {
			return 750 / uni.getSystemInfoSync().windowWidth;
		} else {
			return 1;
		}
	},
	/**
	 * 获取周相关日期
	 * type  s 开始， e结束
	 * dates -1 上周，0本周，1下周
	 */
	getMonday: (type, dates) => {
		var now = new Date();
		var nowTime = now.getTime();
		var day = now.getDay();
		var longTime = 24 * 60 * 60 * 1000;
		var n = longTime * 7 * (dates || 0);
		if (type == "s") {
			var dd = nowTime - (day - 1) * longTime + n;
		};
		if (type == "e") {
			var dd = nowTime + (7 - day) * longTime + n;
		};
		dd = new Date(dd);
		var y = dd.getFullYear();
		var m = dd.getMonth() + 1;
		var d = dd.getDate();
		m = m < 10 ? "0" + m : m;
		d = d < 10 ? "0" + d : d;
		var day = y + "-" + m + "-" + d;
		return day;
	},
	/**
	 * 判断平台
	 */
	platform: () => {
		try {
			if (!Object.is(document, undefined)) {
				return "pc";
			} else if (!Object.is(my, undefined)) {
				return "mp-ap";
			}
		} catch (e) {
			try {
				if (!Object.is(wx, undefined)) {
					return "mp-wx";
				}
			} catch (e) {

			}
		}
	},
	/**
	 * 
	 */
	isUni: () => {
		try {
			//判断平台
			uni.getSystemInfoSync()
			return true
		} catch (e) {
			return false;
		}
	},
	/**
	 * 判断是否字符串并转换
	 */
	isJSON: (str) => {
		if (typeof str == 'string') {
			try {
				return JSON.parse(str);
			} catch (e) {
				return false;
			}
		}
	},
	/**
	 * 将对象转成url参数
	 * @data 对象
	 * @isPrefix 是否拼接问好
	 * @arrayFormat 数组类型返回值 indices==结果: ids[0]=1&ids[1]=2&ids[2]=3
	 * 						 	 brackets==结果: ids[]=1&ids[]=2&ids[]=3
	 * 							 repeat==结果:ids=1&ids=2&ids=3
	 * 							 comma==结果:ids=1,2,3
	 */
	urlArgs: (data = {}, isPrefix = true, arrayFormat = 'comma') => {
		let prefix = isPrefix ? '?' : ''
		let _result = []
		if (Object.is(['indices', 'brackets', 'repeat', 'comma'].indexOf(arrayFormat), -1)) arrayFormat = 'brackets';
		for (let key in data) {
			let value = data[key]
			// 去掉为空的参数
			if (['', undefined, null].indexOf(value) >= 0) {
				continue;
			}
			// 如果值为数组，另行处理
			if (value.constructor === Array) {
				// e.g. {ids: [1, 2, 3]}
				switch (arrayFormat) {
					case 'indices':
						// 结果: ids[0]=1&ids[1]=2&ids[2]=3
						for (let i = 0; i < value.length; i++) {
							_result.push(key + '[' + i + ']=' + encodeURIComponent(value[i]))
						}
						break;
					case 'brackets':
						// 结果:
						value.forEach(_value => {
							_result.push(key + '[]=' + encodeURIComponent(_value))
						})
						break;
					case 'repeat':
						// 结果: ids=1&ids=2&ids=3
						value.forEach(_value => {
							_result.push(key + '=' + encodeURIComponent(_value))
						})
						break;
					case 'comma':
						// 结果: ids=1,2,3
						let commaStr = "";
						value.forEach(_value => {
							commaStr += (commaStr ? "," : "") + encodeURIComponent(_value);
						})
						_result.push(key + '=' + commaStr)
						break;
					default:
						value.forEach(_value => {
							_result.push(key + '[]=' + encodeURIComponent(_value))
						})
				}
			} else {
				_result.push(key + '=' + encodeURIComponent(value))
			}
		}
		return _result.length ? prefix + _result.join('&') : ''
	},
	/**
	 *  下划线转换驼峰
	 * @param name
	 * @returns {*}
	 */
	toHump: name => {
		// eslint-disable-next-line no-useless-escape
		return name.replace(/_(\w)/g, function (all, letter) {
			return letter.toUpperCase();
		});
	},
	/**
	 * 驼峰转换下划线
	 * @param name
	 * @returns {string}
	 */
	toLine: name => {
		return name.replace(/([A-Z])/g, "_$1").toLowerCase();
	},
	/**
	 * 序列化表单
	 * @param data
	 * @returns {string}
	 */
	serialize: data => {
		let list = []
		Object.keys(data).forEach(ele => {
			list.push(`${ele}=${data[ele]}`)
		})
		return list.join('&')
	},
	/**
	 * 深拷贝
	 * @param data
	 * @returns {{}|*}
	 */
	deepCopy: data => {
		var type = util.getObjType(data)
		var obj
		if (type === 'array') {
			obj = []
		} else if (type === 'object') {
			obj = {}
		} else {
			// 不再具有下一层次
			return data
		}
		if (type === 'array') {
			for (var i = 0, len = data.length; i < len; i++) {
				obj.push(util.deepCopy(data[i]))
			}
		} else if (type === 'object') {
			for (var key in data) {
				obj[key] = util.deepCopy(data[key])
			}
		}
		return obj
	},
	/**
	 * 获取对象类型
	 * @param obj
	 * @returns {string|*}
	 */
	getObjType: obj => {
		var toString = Object.prototype.toString
		var map = {
			'[object Boolean]': 'boolean',
			'[object Number]': 'number',
			'[object String]': 'string',
			'[object Function]': 'function',
			'[object Array]': 'array',
			'[object Date]': 'date',
			'[object RegExp]': 'regExp',
			'[object Undefined]': 'undefined',
			'[object Null]': 'null',
			'[object Object]': 'object'
		}
		if (obj instanceof Element) {
			return 'element'
		}
		return map[toString.call(obj)]
	},
	/**
	 * list 集合转化为tree结构
	 * @param list
	 * @param parentMenuId
	 * @returns {*}
	 */
	listToTree: (list, parentMenuId) => {
		let menuObj = {}
		list.forEach(item => {
			item.children = []
			menuObj[item.id] = item
		})
		return list.filter(item => {
			if (item.pid !== parentMenuId) {
				menuObj[item.pid].children.push(item)
				return false
			}
			return true
		})
	},

	/**
	 * 获取文件名后缀
	 * @param file
	 * @returns {*|string|string}
	 */
	getType: (file) => {
		return file.substring(file.lastIndexOf("."), file.length);
	},
	/**
	 * 生成随机数
	 */
	MathCode: (count) => {
		let num = "";
		for (let i = 0; i < count; i++) {
			num += Math.floor(Math.random() * 10);
		}
		return num;
	},
	/**
	 * 删除数组对象
	 * @param array
	 * @param item
	 * @param field
	 * @returns {*}
	 * @constructor
	 */
	ArrayDel: (array, item, field) => {
		if (!Object.is(field, undefined) && !Object.is(field, '')) {
			if (item instanceof Array) {
				for (let i = array.length - 1; i >= 0; i--) {
					for (let j = 0; j < item.length; j++) {
						if (Object.is(array[i][field], item[j])) {
							array.splice(i, 1);
						}
					}
				}
			} else {
				for (let i = array.length - 1; i >= 0; i--) {
					if (Object.is(array[i][field], item)) {
						array.splice(i, 1);
					}
				}
			}
		} else {
			if (item instanceof Array) {
				for (let i = array.length - 1; i >= 0; i--) {
					for (let j = 0; j < item.length; j++) {
						if (Object.is(array[i], vals[j])) {
							array.splice(i, 1);
						}
					}
				}
			} else {
				for (let i = array.length - 1; i >= 0; i--) {
					if (Object.is(array[i], item)) {
						array.splice(i, 1);
					}
				}
			}
		}

		return array;
	},
	dateFormat: (value, fmt) => {
		let date = new Date(value)
		var o = {
			"Y": date.getFullYear(),
			"M+": date.getMonth() + 1,                 //月份
			"d+": date.getDate(),                    //日
			"h+": date.getHours(),                   //小时
			"m+": date.getMinutes(),                 //分
			"s+": date.getSeconds(),                 //秒
			"q+": Math.floor((date.getMonth() + 3) / 3), //季度
			"S": date.getMilliseconds()             //毫秒
		};
		if (/(y+)/.test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
		}
		for (var k in o) {
			if (new RegExp("(" + k + ")").test(fmt)) {
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
			}
		}
		return fmt;
	},
	getBase64Image: (img) => {
		var image = new Image();
		image.src = img;
		image.onload = function () {
			var canvas = document.createElement("canvas");
			canvas.width = image.width;
			canvas.height = image.height;
			var ctx = canvas.getContext("2d");
			ctx.drawImage(image, 0, 0, image.width, image.height);
			var ext = image.src.substring(image.src.lastIndexOf(".") + 1).toLowerCase();
			return canvas.toDataURL("image/" + ext);
		}
	},

}
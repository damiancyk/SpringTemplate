var Utils = {
	isString : function(str) {
		if (typeof str === "string" && str.length > 0) {
			return true;
		} else {
			return false;
		}
	},
	round2 : function(number) {
		if (typeof number === "undefined") {
			return '';
		}
		number = parseFloat(number);
		number = number.toFixed(2);
		number = number.replace('.', ',');

		return number;
	},
	float02 : function(number) {
		if (typeof number === "undefined") {
			return '';
		}
		number = parseFloat(number);
		number = number.toFixed(2);
		number = number.replace('.', ',');

		return number;
	},
	float03 : function(number) {
		if (typeof number === "undefined") {
			return '';
		}
		number = parseFloat(number);
		number = number.toFixed(3);
		number = number.replace('.', ',');

		return number;
	},
	getInputNumber : function(input) {
		input = $(input);
		if (input.length == 0) {
			return 0;
		} else {
			var val = input.val();
			if (val.length == 0) {
				return 0;
			} else {
				//v=v.replace(/ /g,"");
				val = val.replace(/,/g, '.');
				val = parseFloat(val);
				return val;
			}
		}

	},
	getFileName : function(text) {
		var index = text.lastIndexOf('.');
		return text.slice(0, index);
	},
	substring : function(str, start, end) {
		if (typeof str === 'undefined') {
			return '';
		} else {
			return str.substring(start, end);
		}

	}

};
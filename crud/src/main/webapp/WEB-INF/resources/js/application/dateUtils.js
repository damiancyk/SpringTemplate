DateUtils = {
	dateToStringyyyMMdd : function(date) {
		if (typeof date === "undefined") {
			return "";
		}

		var year = date.getFullYear();
		if (typeof year === "undefined" || isNaN(year)) {
			console.error("Conversion error - param is not a date");
			return "";
		}
		var month = date.getMonth() + 1;
		if (month < 10) {
			month = "0" + month;
		}

		var day = date.getDate();
		if (day < 10) {
			day = "0" + day;
		}

		var str = year + '-' + month + '-' + day;

		return str;
	},
	dateToStringyyyMM : function(date) {
		if (typeof date === "undefined") {
			return "";
		}

		var year = date.getFullYear();
		if (typeof year === "undefined" || isNaN(year)) {
			console.error("Conversion error - param is not a date");
			return "";
		}
		var month = date.getMonth() + 1;
		if (month < 10) {
			month = "0" + month;
		}

		var str = year + '-' + month;

		return str;
	},
	dateToStringHHmm : function(date) {
		if (typeof date === "undefined") {
			return "";
		}

		var str = DateUtils.doubleNumber(date.getHours()) + ":"
				+ DateUtils.doubleNumber(date.getMinutes()) + ":"
				+ DateUtils.doubleNumber(date.getSeconds())

		return str;
	},
	dateToStringHHmm : function(date) {
		if (typeof date === "undefined") {
			return "";
		}

		var str = DateUtils.doubleNumber(date.getHours()) + ":"
				+ DateUtils.doubleNumber(date.getMinutes()) + ":"
				+ DateUtils.doubleNumber(date.getSeconds()) + "."
				+ DateUtils.tripleNumber(date.getMilliseconds())

		return str;
	},
	doubleNumber : function(number) {
		if (number < 10) {
			number = "0" + number;
		}
		return number;
	},
	tripleNumber : function(number) {
		if (number < 10) {
			number = "00" + number;
		} else if (number < 100) {
			number = "0" + number;
		}

		return number;
	}

};

function formatDateYYYYmmdd(date) {
	if (typeof date === "undefined") {
		return "";
	}

	var year = date.getFullYear();
	if (typeof year === "undefined" || isNaN(year)) {
		console.error("Conversion error - param is not a date");
		return "";
	}
	var month = date.getMonth() + 1;
	if (month < 10) {
		month = "0" + month;
	}

	var day = date.getDate();
	if (day < 10) {
		day = "0" + day;
	}

	var str = year + '-' + month + '-' + day;

	return str;
}

function getDateYYYYmmdd(str) {
	var date = new Date();

	date = new Date(str);

	return date;
}
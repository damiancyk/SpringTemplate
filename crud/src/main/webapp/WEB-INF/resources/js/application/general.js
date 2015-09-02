var Utils={
	isString:function(str){
		if(typeof str==="string" && str.length>0){
			return true;
		}else{
			return false;
		}
	},
	round2 : function(number){
		if(typeof number==="undefined"){
			return '';
		}
		number=parseFloat(number);
		number=number.toFixed(2);
		number=number.replace('.', ',');
		
		return number;
	},   
	prepareParamUUID:function(str){
		if(typeof str ==="undefined"){
			return "";
		}
		str="'"+str+"'";
		return str;
	},
	addParam:function(obj,param){
		if(typeof param==="undefined"){
			return obj;
		}
		if(typeof obj==="undefined"){
			obj={};
		}
		
		$.extend(obj,param);
		return obj;
	}
};

$.fn.outerHTML = function(s) {
	return s ? this.before(s).remove() : jQuery("<p>").append(
			this.eq(0).clone()).html();
};
String.prototype.currency = function() {
	return (this.float().round2().toFixed(2) + "").replace('.', ',');
};
Number.prototype.currency = function() {
	return (this.round2().toFixed(2) + "").replace('.', ',');
};
Number.prototype.round2 = function() {
	return Math.round((this * 100)) / 100;
};
function isString(str){
	if(typeof str==="undefined"||str.length==0){
		return false;
	}else{
		return true;
	}
}

function toJson(json) {
	if (typeof json === "undefined" || json.length == 0) {
		return '';
	}

	var json = $.parseJSON(json);
	return json;
}

function showJson(json) {
	return JSON.stringify(json);
}

function log(txt) {
	 window.console.log(txt);
}

function logTime(txt) {

	var d = new Date();
	var time=DateUtils.dateToStringHHmm(d);
	if (window.console) 
		 window.console.log(time+' '+txt);
}

function objectToString(obj) {
	return JSON.stringify(obj);
}

function getStringBetweenChars(str, char1, char2){
	var start_pos = str.indexOf(char1) + 1;
	var end_pos = str.indexOf(char2,start_pos);
	var strBetween = str.substring(start_pos,end_pos)
	
	return strBetween;
}

function isIframe(){
	if (window != window.top) {
		return true;
	}else{
		return false;
	}
}

function stopPropagation(ev) {
	ev = ev || event;
	ev.stopPropagation ? ev.stopPropagation() : ev.cancelBubble = true;
}

function getFloat(val) {

	val = val.replace(",", ".");

	if (typeof val === "undefined" || val == null || val == "undefined") {
		val = 0.0;
		// console.error("getFloat() undefined or invalid value")
	} else {
		// val = val.float();
		val = parseFloat(val);
	}

	if (isNaN(val)) {
		val = 0.0;
	}

	return val;
}

Number.prototype.round0 = function() {
	return Math.round(this);
};
Number.prototype.round1 = function() {
	return Math.round((this * 10)) / 10;
}; 
Number.prototype.round2 = function() {
	return Math.round((this * 100)) / 100;
};
function round2(val){
	if(typeof val === "number"){
		return val.round2();
	}else{
		return val;
	}
}
String.prototype.float = function() {

	var val = parseFloat(this.replace(',', '.').replace(/ /g, ''));
	if (isNaN(val)) {
		return 0.0;
	}

	return val;
};


$(function() {
	initDatepicker($('input.datepicker'));
	initDatetimepicker($('input.datetimepicker'));
});

function initDatepicker(inputs) {
	$(inputs).each(function() {
		var input = $(this);
		input.datepicker({
			numberOfMonths : 3,
			format : "yyyy-mm-dd",
			clearBtn : true,
			language : getLocale(),
			autoclose : true,
			todayHighlight : true,
			changeDate : function() {
			}
		});
	});
}

function initDatetimepicker(inputs) {
	$(inputs).each(function() {
		var input = $(this);
		input.datetimepicker({
			format : "YYYY-MM-DD HH:mm",
			locale: getLocale()
		});
	});
}
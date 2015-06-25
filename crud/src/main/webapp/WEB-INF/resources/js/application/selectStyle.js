$(document).ready(function() {
	$('#idSelectChangeStyle').change(function() {
		var select = $(this);
		var selected = select.find(':selected').first();
		var url = selected.val();
		url = 'css/devexpress/theme/' + url;

		$('#idStylesheetTheme[rel=stylesheet]').attr('href', url);
		$.cookie("savedCssStyle", url);
	});

	setSavedStyle();
	setSelectedStyle();
});
function setSelectedStyle() {
	var select = $('#idSelectChangeStyle');
	var link = $('#idStylesheetTheme');
	var href = link.attr('href');
	var styleArgs = href.split('/');
	var style = styleArgs[styleArgs.length - 1];

	var optionToSelect = select.find('option[value="' + style + '"]');
	optionToSelect.prop('selected', true);
}

function setSavedStyle() {
	var select = $('#idSelectChangeStyle');
	var savedCssStyle = $.cookie("savedCssStyle");
	if (typeof savedCssStyle !== "undefined") {
		var defaultStyle = "css/devexpress/theme/dx.light-own.css";
		if (defaultStyle != savedCssStyle) {
			$('#idStylesheetTheme[rel=stylesheet]').attr('href', savedCssStyle);
		}
	}
}
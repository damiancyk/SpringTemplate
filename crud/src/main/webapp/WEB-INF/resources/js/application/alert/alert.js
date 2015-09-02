$(function() {
	errorPopup($("span.error"));
	alertGlobalMessages();
});

function alertModal(txt, type) {
	var notify = $
			.notify(
					{
						// options
						title : '',
						message : txt,
						url : '',
						target : '_blank'
					},
					{
						type : type,
						allow_dismiss : true,
						newest_on_top : false,
						offset : 20,
						spacing : 10,
						z_index : 1031,
						delay : 5000,
						timer : 1000,
						animate : {
							enter : 'animated fadeInDown',
							exit : 'animated fadeOutUp'
						},
						placement : {
							from : "top",
							align : "center"
						},
						template : '<div data-notify="container" class="col-xs-11 col-md-4 alert alert-{0}" role="alert">'
								+ '<button type="button" aria-hidden="true" class="close" data-notify="dismiss"><i class="close-icon-alert"></i></button>'
								+ '<span data-notify="title">{1}</span> '
								+ '<span data-notify="message"><span class="icon-box-alert"><i class="icon-alert"></i></span><p>{2}</p></span>'
								+ '<div class="progress" data-notify="progressbar">'
								+ '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>'
								+ '</div>' + '</div>'
					});

}

function confirmModal(callback, title, content) {
	var window = $('<div class="modal fade confirmation">');
	var html = '';

	var contentModal = alertLocale.alertConfirmation;
	if (typeof content !== "undefined") {
		contentModal = content;
	}

	html += '<div class="modal-dialog">';
	html += '<div class="modal-content">';
	// html += '<div class="modal-header">';
	// html += '<h4 class="modal-title" id="exampleModalLabel">Header</h4>';
	// html += '</div>';
	html += '<div class="modal-body">';
	html += contentModal;
	html += '</div>';

	html += '<div class="modal-footer">';
	html += '<button class="btn btn-success btnOk">'+alertLocale.btnYes+'</button>';
	html += '<button class="btn btn-danger btnCancel" data-dismiss="modal">'+alertLocale.btnNo+'</button>';
	html += '</div>';
	html += '</div>';
	html += '</div>';

	window.html(html);

	var btnOk = window.find('button.btnOk');
	var btnCancel = window.find('button.btnCancel');

	btnOk.click(function() {
		if (typeof callback !== "undefined") {
			callback();
			btnCancel.click();
		}
	});
	var titleModal = alertLocale.confirmTitle;
	if (typeof title !== "undefined") {
		titleModal = title;
	}

	$('body').append(window);

	window.modal();
}

function alertError(txt) {
	alertModal(txt, 'danger');
}
function alertInfo(txt) {
	alertModal(txt, 'info');
}
function alertSuccess(txt) {
	alertModal(txt, 'success');
}
function alertWarning(txt) {
	alertModal(txt, 'warning');
}

function alertGlobalMessages() {
	var errorMessage = $('#idInputErrorMessage').val();
	if (typeof errorMessage !== 'undefined' && errorMessage.length > 0) {
		alertError(errorMessage);
	}

	var successMessage = $('#idInputSuccessMessage').val();
	if (typeof successMessage !== "undefined" && successMessage.length > 0) {
		alertSuccess(successMessage);
	}

	var warningMessage = $('#idInputWarningMessage').val();
	if (typeof warningMessage !== "undefined" && warningMessage.length > 0) {
		alertWarning(warningMessage);
	}

	var infoMessage = $('#idInputInfoMessage').val();
	if (typeof infoMessage !== "undefined" && infoMessage.length > 0) {
		alertInfo(infoMessage);
	}
}

function errorPopup(elements) {
	var trigger = "click";

	elements
			.each(function() {
				var hideSpan = false;
				var span = $(this);
				var parent = span.parent();
				// log('parent '+parent.length)
				// if (parent.hasClass('clear_input_div')) {
				// parent = parent.parent();
				// }
				var msg = span.text();

				// okreslenie elementu dla popup
				var elements = parent.find(".errorField:not(span)");

				var select2 = parent.find("div.select2-container");

				// log("seelct2: "+select2.length)
				// log("msg: "+msg)
				// log("class: "+parent.attr("class"))
				// log("znalezionych errorElementow: "+elements.length)

				parent.find("input").each(function() {
					var input = $(this);

					if (input.data("select2") !== undefined) {
						elements = select2;
						// log("znaleziono selecta 2 z klasami:
						// "+input.attr("class"))
						input.on("select2-highlight", function() {
							// hidePopovers();
							select2.trigger("click");
						});
					}
				});

				var blockPosition = parent.find("div.blockPosition");
				if (blockPosition.length > 0) {
					elements = blockPosition;
				}

				// trigger="select2-focus";
				// element=select2;
				/*
				 * var message = popover.data("message"); if (typeof message ===
				 * "undefined") { message = "Popraw zaznaczone pole"; }
				 */

				if (elements.length > 0) {
					hideSpan = true;
				}

				if (elements.hasClass("check")) {
					var parentBlock = elements.parent();
					parentBlock.addClass("errorFieldCheck");
				}

				elements
						.popover({
							// className : "errorField",
							placement : "top",
							timeout : "0",
							trigger : trigger,
							"max-width" : "100px",
							html : true,
							content : msg,
							template : '<div class="popover top" data-toggle="tooltip" data-placement="top"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
						});

				elements.find(".label-warning").closest(".popover-content")
						.addClass("errorField");

				if (hideSpan == true) {
					span.hide();
				}

			});

	var firstError = $(".errorField:not(.check)").first();
	// firstError.scrollIntoView();
	firstError.trigger(trigger);

	errorPopupHideListener();
}

function errorPopupHideListener() {
	var popupElements = $(".errorField");
	$(document).click(
			function(e) {
				popupElements.each(function() {
					if (!$(this).is(e.target)
							&& $(this).has(e.target).length === 0
							&& $('.popover').has(e.target).length === 0) {
						$(this).popover('hide');
						return;
					}
				});
			});
}

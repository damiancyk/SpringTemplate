var HEADER_BEAN_REFRESH_MILISECONDS = 120000;
var HEADER_BEAN_REFRESH_MILISECONDS_DELAY = 1000;

$(function() {
	$('.oneMessageClass').click(
			function() {
				idMessage = $(this).attr('id');
				window.location = "messagesView-unread.html?idNotification="
						+ idMessage;
			});

	// ile ms po zaladowaniu strony odpalac sprawdzanie cookie
	var DELAY = 5000;
	//setTimeout(refreshTimerCookie, DELAY);
});

function ajaxCountReceivedMessages(label) {
	$.ajax({
		url : "countMessagesReceivedAjax.html?" + Math.random(),
		success : function(data) {
			if (data == 0) {
				//label.hide();
				label.show();
				label.text(0);
			} else {
				label.show();
				label.text(data);
			}
		}
	});
}

function ajaxCountTemplateMessages(label) {
	$.ajax({
		url : "countMessagesTemplateAjax.html?" + Math.random(),
		success : function(data) {
			if (data == 0) {
			//	label.hide();
			} else {

				label.show();
				label.text(data);
			}
		}
	});
}

function refreshTimerCookie() {
	// co ile milisekund pobierac wiadomosci
	var replaceCookieMs = 20000;
	// co ile milisekund sprawdzac cookie
	var refreshTimerMs = 10000;

	var dateBefore = $.cookie("notificationTime");
	if (typeof dateBefore === "undefined") {
		dateBefore = 0;
	}

	var dateNow = new Date().getTime();
	var diff = dateNow - dateBefore;

	if (diff > replaceCookieMs) {
		refreshNotifications();
		ajaxCountReceivedMessages($(".labelReceived"));
		$.cookie("notificationTime", dateNow);
	}

	setTimeout(refreshTimerCookie, refreshTimerMs);
}

function refreshNotifications() {
	$
			.ajax({
				type : "GET",
				contentType : "text/html;charset=UTF-8",
				url : "getMessageHeaderBean.html?" + Math.random(),
				async : false,
				success : function(data) {
					var n = data.split("NEW_MESSAGE");

					var countMsg = parseInt($('#messageCounter').html());
					var totalCount = countMsg + n.length - 1;
					// $('#messageCounter').html("" + totalCount);

					if (n.length > 0) {
						for (i = 1; i < n.length; i++) {
							var msg = n[i];
							var id = msg.split("BEGIN_ID")[1].split("END_ID")[0];
							var subject = msg.split("BEGIN_SUBJECT")[1]
									.split("END_SUBJECT")[0];
							var senderName = msg.split("BEGIN_SENDERNAME")[1]
									.split("END_SENDERNAME")[0];
							var sentDate = msg.split("BEGIN_SENTDATE")[1]
									.split("END_SENTDATE")[0];

							$(".top-right").attr("id", id);

							var messageFormatted = "<div class='alert-title'><fmt:message key='header.received.new.message' /></div>"
									+ "<div class='alert-time-message'><fmt:message key='header.hour' />:"
									+ sentDate
									+ "</div>"
									+ "<div class='alert-sender'><strong><fmt:message key='header.sender' />:</strong> "
									+ senderName
									+ "</div>"
									+ "<div class='aler-subjeckt'><strong><fmt:message key='header.topic' />:</strong> "
									+ subject + "</div>";

							$(".top-right").notify({
								message : {
									html : '' + messageFormatted
								},
								type : 'blackgloss'
							}).show();

							addNextElementHeader(id, subject, senderName,
									"img/message/avatar.png", sentDate);
							ajaxCountReceivedMessages($(".labelReceived"));
						}

					}
				}
			});
	// setTimeout(refreshNotifications, HEADER_BEAN_REFRESH_MILISECONDS);
}


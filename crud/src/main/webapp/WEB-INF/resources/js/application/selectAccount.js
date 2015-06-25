$(function() {
	$('#idBlockSwitchUser').click(function() {
		openSelectAccount();
	});

});

function openSelectAccount() {
	var window = $('<div class="modal fade confirmation">');
	var html = '';

	html += '<div class="modal-dialog">';
	html += '<div class="modal-content">';
	html += '<div class="modal-header">';
	html += '<h4 class="modal-title" id="exampleModalLabel">Zmiana konta</h4>';
	html += '</div>';
	html += '<div class="modal-body">';
	html += '<input id="idSelectAccountAdmin" type="hidden">';
	html += '</div>';

	html += '<div class="modal-footer">';
	// html += '<button class="btn btn-success btnOk">Tak</button>';
	html += '<button class="btn btn-danger" data-dismiss="modal">Anuluj</button>';
	html += '</div>';
	html += '</div>';
	html += '</div>';

	window.html(html);
	window.append(selectAccount)
	window.modal();
	var selectAccount = window.find('input#idSelectAccountAdmin');
	selectAccount.select2Account();

	selectAccount.change(function(e) {
		var added = e.added;
		var email = added.email;
		sumbmitFormSwitchUser(email);
	});

	return;
}

function sumbmitFormSwitchUser(email) {
	var form = $('#switchUserForm');
	var input = $('#idInputUsername');
	input.val(email);
	form.submit();
}

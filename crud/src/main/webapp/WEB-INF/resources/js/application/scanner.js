function alertWarningScan(msg, barcode) {
	var inputBarcode = $('#idInputBarcode');
	var labelError = $('#idLabelError');
	var labelSuccess = $('#idLabelSuccess');
	var labelScanMessage = $('#idLabelScanMessage');
	var scanMessageBlock = labelScanMessage.closest('.errorInfoScaner');
	var scanerInputBox = $('.ScanerInputBox');

	inputBarcode.val(barcode);
	labelError.hide();
	labelSuccess.show();
	labelScanMessage.html(msg);
	scanMessageBlock.removeClass('okSIB');
	scanMessageBlock.removeClass('errorSIB');
	scanMessageBlock.addClass('warningSIB');
	scanerInputBox.removeClass('errorSIB');
	scanerInputBox.removeClass('okSIB');
	scanerInputBox.addClass('warningSIB');
	hideParcelInfo();

	//logTime('SCANNER: ' + msg);
	// alertWarning(msg);
}

function alertErrorScan(msg, barcode) {
	var inputBarcode = $('#idInputBarcode');
	var labelError = $('#idLabelError');
	var labelSuccess = $('#idLabelSuccess');
	var labelScanMessage = $('#idLabelScanMessage');
	var scanMessageBlock = labelScanMessage.closest('.errorInfoScaner');
	var scanerInputBox = $('.ScanerInputBox');

	inputBarcode.val(barcode);
	labelSuccess.hide();
	labelError.show();
	labelScanMessage.html(msg);
	scanMessageBlock.removeClass('okSIB');
	scanMessageBlock.removeClass('warningSIB');
	scanMessageBlock.addClass('errorSIB');
	scanerInputBox.removeClass('okSIB');
	scanerInputBox.removeClass('warningSIB');
	scanerInputBox.addClass('errorSIB');

	hideParcelInfo();

	//logTime('SCANNER: ' + msg);
	// alertError(msg);
}

function alertSuccessScan(msg, barcode) {
	var inputBarcode = $('#idInputBarcode');
	var labelError = $('#idLabelError');
	var labelSuccess = $('#idLabelSuccess');
	var labelScanMessage = $('#idLabelScanMessage');
	var scanMessageBlock = labelScanMessage.closest('.errorInfoScaner');
	var scanerInputBox = $('.ScanerInputBox');

	inputBarcode.val(barcode);
	labelError.hide();
	labelSuccess.show();
	labelScanMessage.html(msg);
	scanMessageBlock.removeClass('warningSIB');
	scanMessageBlock.removeClass('errorSIB');
	scanMessageBlock.addClass('okSIB');
	scanerInputBox.removeClass('warningSIB');
	scanerInputBox.removeClass('errorSIB');
	scanerInputBox.addClass('okSIB');

	//logTime('SCANNER: ' + msg);
	// alertSuccess(msg);
}

function saveTmp() {
	alertSuccess('Zapisano tymczasowe dane');
}
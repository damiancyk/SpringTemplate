function setFieldsByInvoiceType(invoiceType) {
	var allExpenses = $('.allExpenses');
	var selectExpenseType = $('select.selectExpenseType');
	var expenseOptions = $('.expenseOptions');
	var selectSwapNettoBrutto = $('.selectSwapNettoBrutto');
	var selectCurrency = $("#idSelectCurrency");
	var selectPaymentType = $("#idSelectPaymentType");
	var selectNettoBrutto = $(".selectSwapNettoBrutto");
	var columnDiscount = $('th.rabat, td.rabat');
	var columnPkwiu = $('th.pkwiu, td.pkwiu');
	var columnVat = $('th.columnVat, td.columnVat');
	var columnTotalNetto = $('th.columnTotalNetto, td.columnTotalNetto');
	var columnTotalBrutto = $('th.columnTotalBrutto, td.columnTotalBrutto');
	var btnAddExpense = $('.btnAddExpense ');
	var btnNewRate = $('.btnNewRate');
	var columnRemove = $('th.columnBtnRemove, td.columnBtnRemove');
	var inputExpenseDescription = $('textarea.inputExpenseDescription');
	var blockSummationRates = $('.blockSummationRates');

	if (invoiceType == "PF" || invoiceType == "PAR") {
		selectCurrency.select2("readonly", true);
	}
	if (invoiceType == "PF") {
		selectNettoBrutto.select2("readonly", true);
		selectPaymentType.select2("readonly", true);
		columnDiscount.hide();
	}

	if (invoiceType == "FS_NO_VAT" || invoiceType == "FZ_NO_VAT") {
		columnPkwiu.hide();
		columnVat.hide();
		columnTotalBrutto.hide();
		columnDiscount.hide();
		btnNewRate.remove();
		selectSwapNettoBrutto.select2("readonly", true);
	}
	
	if (invoiceType == "DW") {
		inputExpenseDescription.attr("rows", 10);
		btnAddExpense.hide();
		blockSummationRates.hide();
		btnNewRate.hide();
		expenseOptions.hide();
		columnVat.remove();
		columnTotalNetto.hide();
		selectSwapNettoBrutto.select2("readonly", true);
		columnRemove.hide();
	}

}

function addProductToTr(tr, data, incoming) {
	var productName = $(data).attr("productName");
	var priceNetto = toDouble($(data).attr("priceNetto"));
	var vatBuy = $(data).attr("vatBuy");
	var vatSell = $(data).attr("vatSell");
	var idVatBuy = $(data).attr("idVatBuy");
	var idVatSell = $(data).attr("idVatSell");
	var numberPkwiu = $(data).attr("numberPkwiu");

	/*
	 * if (isIncoming()) { vat = vatSell; } else { vat = vatBuy; } if (typeof
	 * vat !== undefined) { vat = 23; }
	 */
	tr.find(".inputName").val(productName);

	var selectVat = tr.find(".selectVatRate");

	var idVat;
	if (incoming == true) {
		idVat = idVatSell;
	} else {
		idVat = idVatBuy;
	}

	if (typeof idVat === "undefined") {
		idVat = selectVat.find("option").first().val();
	}

	if (typeof idVat !== "undefined") {
		selectVat.select2("val", idVat);
	}

	var rate = getCurrencyRate();
	tr.find(".inputCurrency").val(priceNetto / rate);

	tr.find(".inputNetto").val(priceNetto);
	tr.find("input.inputPkwiu").val(numberPkwiu);

	// var priceBrutto = priceNetto * vat;
	countPricesNew(tr);
}

function fillTableSummation(tables) {
	$(tables).each(function() {
		var table = $(this);
		var footer = table.find('tr.footer');
		var header = table.find('tr.header');
		var columns = header.find('th').length;
		var footerCells = footer.find('td').length;

		for ( var i = footerCells; i < columns; i++) {
			var td = $('<td>');
			var th = header.find('th').eq(i);

			var headerClass = th.attr('class');
			td.addClass(headerClass);
			footer.append(td);
		}
	});
}

function generateTableSummation(tables) {
	$(tables).each(
			function() {
				var table = $(this);
				var footer = table.find('tr.footer');
				var header = table.find('tr.header');
				var productRows = table.find('.productRow');

				var generalSum = 0;
				var totalNettoSum = 0;
				var totalBruttoSum = 0;

				var generalIndex = header.find('.columnGeneral:visible')
						.first().index();
				var totalNettoIndex = header.find('.columnTotalNetto:visible')
						.first().index();
				var totalBruttoIndex = header
						.find('.columnTotalBrutto:visible').first().index();

				productRows.each(function() {
					var productRow = $(this);

					var inputGeneral = productRow.find('input.inputGeneral');
					if (inputGeneral.length > 0) {
						var generalVal = inputGeneral.val();
						if (generalVal.length > 0) {
							generalSum += parseFloat(generalVal);
						}
					}

					var inputTotalNetto = productRow
							.find('input.inputTotalNetto');
					if (inputTotalNetto.length > 0) {
						var totalNetto = inputTotalNetto.val();
						if (totalNetto.length > 0) {
							totalNettoSum += parseFloat(totalNetto);
						}
					}

					var inputTotalBrutto = productRow
							.find('input.inputTotalBrutto');
					if (inputTotalBrutto.length > 0) {
						var totalBrutto = inputTotalBrutto.val();
						if (totalBrutto.length > 0) {
							totalBruttoSum += parseFloat(totalBrutto);
						}
					}

				});

				if (generalIndex >= 0) {
					var tdGeneral = footer.find('td').eq(generalIndex);
					tdGeneral.html('<span class="pull-right">'
							+ generalSum.currency() + '</span>');
				}

				if (totalNettoIndex >= 0) {
					var tdTotalNetto = footer.find('td').eq(totalNettoIndex);
					tdTotalNetto.html('<span class="pull-right">'
							+ totalNettoSum.currency() + '</span>');
				}

				if (totalBruttoIndex >= 0) {
					var tdTotalBrutto = footer.find('td').eq(totalBruttoIndex);
					tdTotalBrutto.html('<span class="pull-right">'
							+ totalBruttoSum.currency() + '</span>');
				}

			});
}

function initRowValues(rows) {
	$(rows).each(function() {
		var row = $(this);

		row.find(".inputCounter").val("1");
		row.find(".inputDiscount").val("0");
		row.find(".inputUnitType").val("szt");
		row.find(".inputCurrency").val("0,00");
		row.find(".inputGeneral").val("0");
		row.find(".inputNetto").val("0");
		row.find(".inputBrutto").val("0");
		row.find(".inputTotalNetto").val("0");
		row.find(".inputTotalBrutto").val("0");
		row.find(".inputTotalNettoCurrency").val("0,00");
		row.find(".inputTotalBruttoCurrency").val("0,00");
	});
}

function countPricesCurrency(lastRate, rate) {

}

function getCurrencyRate() {
	var inputRate = $("#idInputRate");
	if (inputRate.length <= 0) {
		return 1;
	}
	var rate = $("#idInputRate").val().float();
	var counter = $("#idInputCurrencyCounter").val();

	if (rate <= 0) {
		rate = 1;
	}

	if (typeof counter === "undefined" || isNaN(counter) || counter == "") {
		counter = 1;
	}

	rate /= counter;

	return rate;
}

function getTablePositions() {
	var table = $("#idTableProducts");

	return table.find("tr").not(":first").not(":last");
}

function setNativePrices() {
	getTablePositions().each(function() {
		var tr = $(this);
		var inputGeneral = tr.find(".inputGeneral");
		var inputCurrency = tr.find(".inputCurrency");

		var general = inputGeneral.val();
		var rate = getCurrencyRate();

		inputCurrency.val((general / rate).currency());
	});
}

function countPricesNew(trs, saveNativePrice) {
	var rate = getCurrencyRate();

	trs
			.each(function() {
				var tr = $(this);

				// inputy w wierszu
				var table = tr.closest("table");
				var selectSwapNettoBrutto = table
						.find(".selectSwapNettoBrutto");
				var inputCounter = tr.find(".inputCounter");
				if (inputCounter.length <= 0) {
					return;
				}
				var selectVatRate = tr.find(".selectVatRate");
				var inputDiscount = tr.find(".inputDiscount");
				var inputGeneral = tr.find(".inputGeneral");
				var inputCurrency = tr.find(".inputCurrency");
				var inputNetto = tr.find(".inputNetto");
				var inputBrutto = tr.find(".inputBrutto");
				var inputTotalNetto = tr.find(".inputTotalNetto");
				var inputTotalBrutto = tr.find(".inputTotalBrutto");
				var inputTotalNettoCurrency = tr
						.find(".inputTotalNettoCurrency");
				var inputTotalBruttoCurrency = tr
						.find(".inputTotalBruttoCurrency");

				inputTotalNettoCurrency.val("0,00")

				var vat = 0.0;
				if (selectVatRate.length > 0) {
					var selectedOption = selectVatRate.find(":selected")
							.first();
					if (selectedOption.length > 0) {
						vat = selectedOption.data("val");
						vat = parseFloat(vat);
						if (typeof vat === "undefined" || isNaN(vat)) {
							vat = 0.0;
						}
					}
				}

				var discount = 0.0;
				if (inputDiscount.length > 0 && inputDiscount.val().length > 0) {
					discount = inputDiscount.val().float();
					if (isNaN(discount)) {
						discount = 0;
					}
					if (discount > 100) {
						discount = 100;
					}
				}
				inputDiscount.val(discount);

				var counter = inputCounter.val();
				if (counter.length <= 0 || isNaN(counter)) {
					counter = 1;
					inputCounter.val(counter);
				}

				var nettoBrutto = selectSwapNettoBrutto.find(":selected")
						.first().val();

				var currency = 0.0;
				if (inputCurrency.val().length > 0) {
					currency = inputCurrency.val().float();
					if (isNaN(currency)) {
						currency = 0.0;
					}
				}

				var general = (currency * rate);

				if (saveNativePrice == true) {

					// inputGeneral.val(inputCurrency.val());
				} else {
					inputGeneral.val(general);
				}

				var netto = 0;
				var brutto = 0;
				var totalNetto = 0;
				var totalBrutto = 0;

				// TODO przed mnozeniem * counter zaokraglac
				if (nettoBrutto == "netto") {
					netto = general * (100 - discount) / 100;
					totalNetto = (general * counter) * (100 - discount) / 100;
					brutto = (100 + vat) / 100 * netto;
					totalBrutto = (100 + vat) / 100 * (netto * counter);
				} else if (nettoBrutto == "brutto") {
					brutto = general * (100 - discount) / 100;
					totalBrutto = (general * counter) * (100 - discount) / 100

					netto = brutto / ((100 + vat) / 100);
					totalNetto = (brutto * counter) / ((100 + vat) / 100);
				} else {
					netto = general;
				}

				netto = round2(netto);
				brutto = round2(brutto);
				totalNetto = round2(totalNetto);
				totalBrutto = round2(totalBrutto);

				// general/=rate;
				inputCurrency.val(currency.currency());
				inputNetto.val(netto);
				inputBrutto.val(brutto);

				inputTotalNetto.val(totalNetto);
				inputTotalNettoCurrency.val((totalNetto / rate).currency());

				inputTotalBrutto.val(totalBrutto);
				inputTotalBruttoCurrency.val((totalBrutto / rate).currency());

			});

	setSummationPrices();
	setSummationRates();
}

function setSummationRates() {
	function Rate(vat, netto, vatValue, brutto) {
		this.vat = vat;
		this.netto = netto;
		this.vatValue = vatValue;
		this.brutto = brutto;
	}

	var nettos = [];
	var bruttos = [];

	// sumowanie stawek
	var tables = $(".tableProducts");
	var rows = tables.find('.productRow');

	rows.each(function() {
		var row = $(this);
		var counter = 0;
		var nettoTotal = 0;
		var bruttoTotal = 0;
		var nettoTotalCurrency = 0;
		var bruttoTotalCurrency = 0;

		var selectVatRate = row.find('select.selectVatRate');
		var selectedOption = selectVatRate.find(':selected').first();
		var vat = selectedOption.data("val");

		if (typeof vat !== "undefined") {
			vat = parseFloat(vat);
			var netto = parseFloat(row.find('input.inputTotalNetto').val());
			var oldNetto = nettos[vat];
			if (typeof oldNetto === "undefined") {
				oldNetto = 0;
			}
			var newNetto = netto + oldNetto;
			nettos[vat] = newNetto;

			var brutto = parseFloat(row.find('input.inputTotalBrutto').val());
			var oldBrutto = bruttos[vat];
			if (typeof oldBrutto === "undefined") {
				oldBrutto = 0;
			}
			var newBrutto = brutto + oldBrutto;
			bruttos[vat] = newBrutto;
		}

		row.find(".inputCounter").each(function() {
			counter += getFloat($(this).val());
		});
	});

	var rates = new Array();
	for ( var i = 0; i < nettos.length; i++) {
		var netto = nettos[i];
		if (typeof netto !== "undefined") {
			var brutto = bruttos[i];
			var vat = i;
			var vatValue = brutto - netto;
			var rate = new Rate(vat, netto, vatValue, brutto);
			rates.push(rate);
		}
	}

	// dodanie sum do tabeli
	var expenses = $('.allExpenses');
	var table = expenses.find('table.tableSummationRates');
	var tbody = table.find('tbody');
	var tfoot = table.find('tfoot');
	tbody.html('');
	var labelVatValue = tfoot.find('.labelVatValue');
	var labelNetto = tfoot.find('.labelNetto');
	var labelBrutto = tfoot.find('.labelBrutto');

	var vatValueSum = 0;
	var nettoSum = 0;
	var bruttoSum = 0;

	for ( var i = 0; i < rates.length; i++) {
		var rate = rates[i];
		var vat = rate.vat;
		var netto = rate.netto;
		var vatValue = rate.vatValue;
		var brutto = rate.brutto;

		var tr = $('<tr>');
		tr.append('<td>' + vat + '</td>');
		tr.append('<td>' + netto.currency() + '</td>');
		tr.append('<td>' + vatValue.currency() + '</td>');
		tr.append('<td>' + brutto.currency() + '</td>');

		tbody.append(tr);

		vatValueSum += vatValue;
		nettoSum += netto;
		bruttoSum += brutto;
	}

	labelVatValue.html(vatValueSum.currency());
	labelNetto.html(nettoSum.currency());
	labelBrutto.html(bruttoSum.currency());
}

function setSummationPrices() {
	var tables = $(".tableProducts");

	// generateTableSummation(tables);

	// return;
	tables.each(function() {
		var counter = 0;
		var nettoTotal = 0;
		var bruttoTotal = 0;
		var nettoTotalCurrency = 0;
		var bruttoTotalCurrency = 0;

		var table = $(this);

		table.find(".inputCounter").each(function() {
			counter += getFloat($(this).val());
		});

		table.find(".inputTotalNetto").each(function() {
			nettoTotal += getFloat($(this).val());
		});
		table.find(".inputTotalBrutto").each(function() {
			bruttoTotal += getFloat($(this).val());
		});
		table.find(".inputTotalNettoCurrency").each(function() {
			nettoTotalCurrency += getFloat($(this).val());
		});
		table.find(".inputTotalBruttoCurrency").each(function() {
			bruttoTotalCurrency += getFloat($(this).val());
		});

		table.find(".inputNettoTotal").val(nettoTotal.toFixed(2));
		table.find(".inputBruttoTotal").val(bruttoTotal.toFixed(2));

		// $("#idInputNettoTotalCurrency").val(nettoTotalCurrency.toFixed(2));
		// $("#idInputBruttoTotalCurrency").val(bruttoTotalCurrency.toFixed(2));

		table.find(".labelTotalNetto").text(nettoTotalCurrency.currency());
		table.find(".labelTotalBrutto").text(bruttoTotalCurrency.currency());

		table.find(".labelCounter").text(counter.toFixed(0));
	});

}

function countAllPrices(saveNativePrice) {
	var table = $(".tableProducts");

	table.find("tr").not(":first").not(":last").each(function() {
		countPricesNew($(this), saveNativePrice);
	});
}

function addListenerCountPrices(element) {
	var tr = element;

	element.find(".inputCounter, .inputCurrency, .inputDiscount").on("blur",
			function() {
				countPricesNew(tr);
				setSummationPrices();
			});

	element.find(".selectVatRate").change(function() {
		countPricesNew(tr);
		setSummationPrices();
	});
}

function submitWithCheck() {
	if (isAnyZeroPrice()) {
		confirmModal("Zerowe wartosci netto", function() {
			$("form").submit();
		}, "Czy na pewno chcesz dodac dokument z zerowymi cenami?");
	}
}

function isAnyZeroPrice() {
	var table = $("#idTableProducts");
	var anyZero = false;

	table.find(".inputGeneral").each(function() {
		var inputGeneral = $(this);
		var val = inputGeneral.val();

		if (val.length <= 0) {
			anyZero = true;
		}

		val = val.float();

		if (inputGeneral.val <= 0 || isNaN(val)) {
			anyZero = true;
		}
	});

	return anyZero;
}

function generateLp(tables) {
	var tables = $(tables);

	tables.each(function() {
		var table = $(this);
		table.find("tr.productRow").each(
				function(i) {

					var tr = $(this);
					var lpCell = tr.find("td.lpColumn");

					lpCell.addClass("lpCell")
					lpCell.html("<input class='lp' type='hidden' value='"
							+ (i + 1) + "'></input>" + (i + 1) + "");
				});
	});

}

function initializeVatRateSelect(selectElement, vatList) {
	var vatList = $.parseJSON(vatList);

	for ( var i = 0; i < vatList.length; i++) {
		var vat = vatList[i];

		var option = $("<option></option>", {
			"data-val" : vat.value,
			"value" : "" + vat.idVat,
			"html" : "" + vat.symbol,
		});
		selectElement.append(option);
	}

	// selectElement.find("option[val!='']").first().attr("selected",
	// "selected");

	initSelect2(selectElement);
}

function initializeVatRateSelectOld(selectElement, vatRateStr) {
	var vatRateArray = vatRateStr.split(";");

	for ( var i = 0; i < vatRateArray.length; i++) {
		selectElement.append($("<option></option>").attr("value",
				parseFloat(vatRateArray[i])).text(vatRateArray[i] + " %"));
	}

	selectElement.select2();
	selectElement.select2("val", "23");
}

function productViewAjax(idProduct) {
	var idProductPriceGroup = getIdProductPriceGroup();
	var productPriceGroupParam = "";

	if (typeof idProductPriceGroup !== "undefined"
			&& idProductPriceGroup.length > 0) {
		if (idProductPriceGroup.length > 0) {
			productPriceGroupParam = "?idProductPriceGroup="
					+ idProductPriceGroup;
		}
	}

	var url = "productViewAjax-" + idProduct + ".html" + productPriceGroupParam;

	return $.ajax(url, {
		dataType : "json"
	});
}

function updateSelectProducts() {
	initializeSelect2Product($(".selectProduct"));
	$(".selectProduct").change();
}

function getIdPartner() {
	return $("#idSelectPartner").val();
}

function getIdProductPriceGroup() {
	return $("#idSelectProductPriceGroup").val();
}

function swapNettoBruttoEqualSign(selectElement, documentType) {
	if (selectElement.length <= 0) {
		return;
	}

	var table = selectElement.closest(".tableProducts");
	var val = selectElement.find(":selected").val();
	var labelNetto = table.find(".labelSignEqualsNetto");
	var labelBrutto = table.find(".labelSignEqualsBrutto");

	if (val == "netto") {
		labelNetto.hide();
		labelBrutto.show();
	} else {
		labelNetto.show();
		labelBrutto.hide();
	}

}

function removePositions() {
	var positions = $(".productRow");
	positions.each(function() {
		var pos = $(this);
		// pos.
	});
	// positions.remove();

}

function isAnyPosition() {
	var any = false;
	var positions = $(".productRow");

	positions.each(function() {
		var pos = $(this);
		var name = pos.find("input.inputName");
		var val = name.val();
		if (val.length > 0) {
			any = true;
			return true;
		}
	});

	return any;
}

function disableInputsForOrder(invoiceType) {
	if(invoiceType=="FK_POSITIONS"){
		return;
	}
	
	var inputs = $(".inputCounter, .inputUnitType, .inputPkwiu, .inputCurrency, .inputGeneral, .inputDiscount, .inputName");
	inputs.attr("readonly", "true");

	var selects = $(".selectSwapNettoBrutto, .selectVatRate, .selectProduct, #idSelectPartner, #idSelectProductPriceGroup, #idSelectCurrency");
	selects.attr("readonly", "true");

	var buttons = $("#idBtnPartnerAdd, #idBtnPartnerView, #idBtnNewRow");
	buttons.hide();
}

function selectCurrency() {
	function format(state) {
		var option = $(state.element);
		return state.text + "<label class='pull-right'>"
				+ option.data("symbol") + "</label>";
	}
	$("#idSelectCurrency").select2({
		// minimumResultsForSearch : -1,
		formatResult : format,
		formatSelection : format,
		escapeMarkup : function(m) {
			return m;
		}
	}).change(
			function(e) {
				var select = $(this);
				var option = $(e.added);
				var symbol = select.find(":selected").first().data("symbol");
				$(".labelCurrency").text(symbol);
/*
				setCurrencyInfo(symbol, "#idInfoSelect", "#idSelectCurrency")
						.done(function() {

							setNativePrices();
							countAllPrices(true);
						});
						*/
			});

	var symbol = $("#idSelectCurrency").find(":selected").first()
			.data("symbol");
	//setCurrencyInfo(symbol, "#idInfoSelect", "#idSelectCurrency");
}

function onChangeContrahentInvoice(e) {
	var select = $("#idSelectPartner");
	var idPartner = select.val();
	if (typeof idPartner !== "undefined" && idPartner.length > 0) {
		setProductPriceGroupAjax(idPartner);
	}
}

function setProductPriceGroupAjax(idPartner) {
	$.ajax({
		url : "productPriceGroupByIdPartner-" + idPartner + ".html",
		type : "GET",
		dataType : "json",
		success : function(data) {
			var selectProductPriceGroup = $("#idSelectProductPriceGroup");
			var idProductPriceGroup = data.idProductPriceGroup;
			var idProductPriceGroupBefore = selectProductPriceGroup.val();

			if (idProductPriceGroup != idProductPriceGroupBefore) {
				selectProductPriceGroup.val(idProductPriceGroup);
				selectProductPriceGroup.change();
			}
		}
	})
}

jQuery.moveColumn = function(table, from, to) {
	var rows = jQuery('tr', table);
	var cols;
	rows.each(function() {
		cols = jQuery(this).children('th, td');
		cols.eq(from).detach().insertBefore(cols.eq(to));
	});
};

function resetPrices(select) {
	var select = $(select);
	var row = select.closest("tr");

	row.find(".inputCounter").val("1");
	row.find(".inputDiscount").val("0");
	row.find(".inputUnitType").val("szt");
	row.find(".inputCurrency").val("0,00");
	row.find(".inputGeneral").val("0");
	row.find(".inputNetto").val("0");
	row.find(".inputBrutto").val("0");
	row.find(".inputTotalNetto").val("0");
	row.find(".inputTotalBrutto").val("0");
	row.find(".inputTotalNettoCurrency").val("0,00");
	row.find(".inputTotalBruttoCurrency").val("0,00");

	row.find("input.selectProduct").select2("val", "");
	row.find("input.inputName").val("");

	countPricesNew(row);
}

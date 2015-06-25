function showSearch(e, element) {
	var blockPosition = $(element).closest(".blockPosition");

	if (typeof e !== "undefined") {
		stopPropagation(e);
	}

	blockPosition.find(".blockInfo").hide();
	blockPosition.find(".blockSearch").show();
	blockPosition.find(".blockSearch").find("input.select").select2("open");
}

function showSearchWithoutOpen(element) {
	var blockPosition = $(element).closest(".blockPosition");

	if (typeof e !== "undefined") {
		stopPropagation(e);
	}

	blockPosition.find(".blockInfo").hide();
	blockPosition.find(".blockSearch").show();
}

function showInfo(element) {
	var element = $(element);
	var blockPosition = element.closest(".blockPosition");

	var valInput = blockPosition.find(".blockInput").find("input").val();

	if (valInput == "") {
		blockPosition.find(".blockInput").hide();
		blockPosition.find(".blockSearch").hide();
		blockPosition.find(".blockInfo").show();
	}
}

function clearSearch(element) {
	var blockPosition = $(element).closest(".blockPosition");

	blockPosition.find(".blockInput").hide();
	blockPosition.find(".blockSearch").hide();
	blockPosition.find(".blockInfo").show();

	blockPosition.find(".blockSearch").find("input").val("");
	blockPosition.find(".blockInput").find("input").val("");
}

function closeSearch(element) {
	var blockPosition = $(element).closest(".blockPosition");
	var idSelected = $(element).val();

	if (idSelected.length <= 0) {
		blockPosition.find(".blockInput").hide();
		blockPosition.find(".blockSearch").hide();
		blockPosition.find(".blockInfo").show();
	}
}

function showInput(element) {
	var blockPosition = $(element).closest(".blockPosition");

	blockPosition.find(".blockInfo").hide();
	blockPosition.find(".blockInput").show();
	blockPosition.find(".blockInput").find("input").focus();
}

function initializePositionBlocks(elements) {
	$.each(elements, function() {
		var blockPosition = $(this);
		var blockSearch = blockPosition.find(".blockSearch");
		var blockInput = blockPosition.find(".blockInput");
		var blockInfo = blockPosition.find(".blockInfo");

		var name = blockInput.find("input").val();
		var idProduct = blockPosition.find("input.select").val();

		var selects = blockSearch.find('input.select');
		onSelectCloseListener(selects);

		var inputs = blockInput.find('input.inputName');
		initializeClearSearch(inputs);
		addErrorClass(inputs);

		if (idProduct != "") {
			blockSearch.show();
			blockInfo.hide();
			blockInput.hide();
		} else if (name == "") {
			blockSearch.hide();
			blockInfo.show();
			blockInput.hide();
		} else {
			blockSearch.hide();
			blockInfo.hide();
			blockInput.show();
		}

		blockInfo.each(function() {
			var block = $(this);
			block.on('click', function() {
				showInput(this);
			});

		});

		// warunek gdy wybrano cos w select2

	});
}

function showErrorInputName(tableProduct) {
	var blockPosition = tableProduct.find(".productRow").each(function() {
		var productRow = $(this);
		var inputName = productRow.find(".inputName");
		var blockPosition = productRow.find(".blockPosition");
		var errorClass = "errorField";

		if (inputName.hasClass(errorClass)) {
			inputName.removeClass(errorClass);
			blockPosition.addClass(errorClass);
		}
	});
}

function addProductFromSeach(data, btn, event) {
	var idProducts = data.idProducts;
	if (typeof idProducts === "undefined") {
		console.error("Blad - nie otrzymano listy idProducts");
		return;
	}

	var idTr = btn.data("tr");
	console.log("idTr " + idTr);
	var row;
	if (typeof idTr !== "undefined") {
		row = $(idTr)
	} else {
		row = btn.closest("tr");
	}

	var rows = row.closest("table").find("tr.productRow");

	initProductRow(data, row, idProducts[0]);

	var i = 1;
	rows.each(function() {
		var row = $(this);
		var selectProduct = row.find("input.selectProduct");
		var inputName = row.find("input.inputName");
		var name = "";
		if (typeof inputName !== "undefined") {
			name = inputName.val();
		}

		if (typeof selectProduct !== "undefined"
				&& selectProduct.val().length <= 0 && name.length <= 0) {
			initProductRow(data, row, idProducts[i]);
			i++;
		}
	});

	for ( var j = i; j < idProducts.length; j++) {
		var idProduct = idProducts[j];
		addPosition(idProduct);
	}
}

function initProductRow(data, row, idProduct) {
	var selectProduct = row.find("input.selectProduct");
	selectProduct.select2("val", idProduct);
	selectProduct.trigger("change");

	var blockPosition = row.find(".blockPosition");
	blockPosition.find(".blockInput").hide();
	blockPosition.find(".blockInfo").hide();
	blockPosition.find(".blockSearch").show();
}

function onSelectCloseListener(selects) {
	$(selects).on("select2-removed", function() {
		clearSearch(this);
	}).on("select2-close", function() {
		closeSearch(this);
	});
}

function initializeClearSearch(inputs) {
	inputs.clearSearch();
}

function addErrorClass(inputs) {
	inputs.each(function() {
		var input = $(this);
		var block = input.closest('.blockPosition');
		if (input.hasClass('errorField')) {
			block.find('.blockInfo').addClass('errorField')
			block.addClass('errorField');
		}
	})
}

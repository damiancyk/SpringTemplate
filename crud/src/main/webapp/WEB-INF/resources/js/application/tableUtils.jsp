<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>

<script>
	$(function() {
		setTableDataListener($('table.data'));
	});

	function setTableDataListener(tables) {
		tables.each(function() {
			var table = $(this);
			var positions = table.find('tbody tr');

			positions.click(function(e) {
				var position = $(this);
				position.toggleClass('checked');

				var check = position.find('input.check');
				var newState = !check.is(':checked');
				check.prop('checked', newState);
				setRowState($(this), newState)
			});

		});
	}

	function getTargetTable(options) {
		var table;
		if (typeof options !== "undefined") {

			var target = options.target;

			if (typeof target !== "undefined") {
				table = $(target);
			}
		}

		if (typeof table === "undefined") {
			table = $('table');
		}

		return table;
	}

	//TODO przyciski do poszczegolnych tabel
	//wyrzucic
	function getTable() {
		var tables = $("table");
		var table = $('div.dx-datagrid-rowsview table.dx-datagrid-table')
				.first();
		if (table.length == 0) {
			table = $('table').first();
		}

		return table;
	}

	function url(url, options) {
		var table = getTargetTable(options);
		
		url = replaceDisplaytagParams(url, options, table);
		if (typeof options !== "undefined") {
			if (options.popup == true) {
				invokeDialog(url, undefined);
			} else {
				window.location.href = url;
			}
		} else {
			window.location.href = url;
		}
	}

	function extractUrlFromFunc(url) {
		if (url.indexOf('url(') >= 0) {
			url = getStringBetweenChars(url, "'", "'");
		}
		return url;
	}

	function urlConfirm(url, options, text) {
		var table = getTargetTable(options);
		url = replaceDisplaytagParams(url, options, table);
		var callback = function() {
			window.location.href = url;
		};
		confirmModal(callback);
	}

	function replaceDisplaytagParams(url, options, table) {
		if (typeof url === "undefined" || url == "") {
			console.error("Blad - okresl url");
			return;
		}
		if (options === "undefined") {
			options = {};
		}

		var indexId = url.indexOf("||ID||");
		var indexIds = url.indexOf("||IDS||");

		if (indexId >= 0 && indexIds >= 0) {
			console
					.error("Blad - nie mozna wybrac naraz parametrow ID oraz IDS");
			return;
		}

		var numberOfChecked;
		if (indexId >= 0) {
			numberOfChecked = getNumberOfChecked(table, {
				oneRow : true
			});
			var id = getFirstCheckedId(table);

			url = url.replace("||ID||", id);
		} else if (indexIds >= 0) {
			numberOfChecked = getNumberOfChecked(table, options);
			var idsArray = returnCheckedIdsArray(table);
			url = url.replace("||IDS||", idsArray);
		}

		return url;
	}

	// zwraca ilosc zaznaczonych checkboxow w tabeli
	function getNumberOfChecked(table, params) {
		if (typeof params === "undefined") {
			params = {};
		}

		var positions;
		if ($(table).hasClass("dx-datagrid-table")) {
			positions = table.find(".dx-select-checkbox.dx-checkbox-checked").length;
		} else {
			positions = table.find("input.check:checked").length;
		}

		if (typeof params.oneRow !== "undefined") {
			if (positions > 1) {
				alertInfo('<fmt:message key="js.alert.select.only.one.row" />');
				throw ("Wybrano wiecej niz jedna dozwolona pozycje");
			}
		}

		if (positions == 0) {
			if (typeof params.noErr !== "undefined" && params.noErr == true) {
				// throw ("Nie wybrano zadnej pozycji");
			} else {
				alertInfo('<fmt:message key="js.alert.no.selected.column" />');
				throw ("Nie wybrano zadnej pozycji");
			}

		} else {
			return positions;
		}
	}

	function getFirstCheckedId(table) {
		var idPosition = getFirstChecked(table).data("id-position");
		return idPosition;
	}

	// zwraca pierwszego zaznaczonego checkboxa
	function getFirstChecked(table) {
		var check;

		if (table.hasClass("dx-datagrid-table")) {
			var checkDiv = table.find(".dx-checkbox-checked").first();
			check = checkDiv.closest("tr").find("input.check");
		} else {
			check = table.find("input.check:checked").not('.select_all').first();
		}

		return check;
	}

	function returnCheckedIds() {
		return arrayToString(returnCheckedIdsArray());
	}

	function returnCheckedIdsArray(table) {
		var params = new Array();

		if (table.hasClass("dx-datagrid-table")) {
			table.find(".dx-checkbox-checked").each(function() {
				var row = $(this).closest("tr");
				var checkOld = row.find("input[type=checkbox]");
				var param = checkOld.data("id-position");
				if (typeof param !== "undefined") {
					params.push(param);
				}
			});
		} else {
			table.find("input.check:checked").each(function() {
				var param = $(this).data("id-position");
				if (typeof param !== "undefined") {
					params.push(param);
				}
			});
		}

		return params;
	}

	function arrayToString(array) {
		var str = "";

		for ( var i = 0; i < array.length; i++) {
			str += array[i] + ";";
		}

		return str;
	}

	function changePriceListener(netto, brutto, select) {
		$(netto).on('blur', function() {
			if (netto.val().length == 0) {
				netto.val('');
				brutto.val('');
			} else {
				var vatPercent = getVatPercentFromSelect(select);
				var nettoVal = Utils.getInputNumber(netto);
				var bruttoVal = nettoVal * (100 + vatPercent) / 100;

				netto.val(nettoVal.round2());
				brutto.val(bruttoVal.round2());
			}
		});
		$(brutto).on('blur', function() {
			if (brutto.val().length == 0) {
				netto.val('');
				brutto.val('');
			} else {
				var vatPercent = getVatPercentFromSelect(select);
				var bruttoVal = Utils.getInputNumber(brutto);
				var nettoVal = bruttoVal / ((100 + vatPercent) / 100);

				netto.val(nettoVal.round2());
				brutto.val(bruttoVal.round2());
			}
		});
		$(select).on('change', function() {
			if (netto.val().length == 0) {
				netto.val('0');
				brutto.val('0');
			} else {
				var vatPercent = getVatPercentFromSelect(select);
				var nettoVal = Utils.getInputNumber(netto);
				var bruttoVal = nettoVal * (100 + vatPercent) / 100;

				netto.val(nettoVal.round2());
				brutto.val(bruttoVal.round2());
			}
		});

	}

	function getVatPercentFromSelect(select) {
		if (select.length == 0) {
			return 0;
		}

		var data = select.select2('data');
		if (data == null) {
			return 0;
		}
		var percent = data.value;

		return percent;
	}
</script>

(function() {
	var app = angular.module('table', [ 'ngTable', 'ngTableExport',
			'ngResource', 'ngCookies' ]);

	app.controller('TableCtrl', function($scope, $filter, $http, ngTableParams,
			$sce, $element, $resource, $timeout, $q, $location, $interval) {
		var table = $element.find('table');
		var Api = getApi(table, $resource);
		setTableCustomElements(table);
		var urlParams = getUrlParams($location);

		$scope.tableParams = new ngTableParams(angular.extend({
			page : 1,
			count : 10,
			sorting : getSorting(table)
		}, $location.search()), {
			total : 0,
			getData : function($defer, params) {

				setUrlParams($location, params, table);

				if (isTableData(table)) {
					getData($defer, table, params, $filter);
				} else if (isTableAjax(table)) {
					getDataAjax($scope, $defer, Api, $resource, params,
							$timeout, table);
				}
			}
		});

		$scope.renderHtml = function(html_code) {
			if (typeof html_code === "undefined") {
				return "";
			}
			return $sce.trustAsHtml('' + html_code);
		};

		$scope.isTableData = function(table) {
			var data = table.data('json');

			if (typeof data !== "undefined") {
				return true;
			} else {
				return false;
			}
		}

		$scope.selectValues = function(options) {
			var def = $q.defer(), arr = [], selectValues = [];
			var temp = new Array(options);

			$.each(options, function(key, val) {
				selectValues.push({
					'id' : key,
					'title' : val
				});
			});

			def.resolve(selectValues);
			return def;
		}

		setRefreshData(table, $interval, $scope);

		// $timeout(test(), 1000);

		var datepickers = table.find('input.datepicker');
		//initDatepicker(datepickers);
	});

	app.directive('tableRow', function() {
		return {
			link : function(scope, elm) {
				var elm = $(elm);
				var check = elm.find('input.check');
				var links = elm.find('a');

				elm.click(function() {
					var check = $(this).find('input.check');
					var newState = !check.is(':checked');
					check.prop('checked', newState);
					setRowState($(this), newState)
				})
			}
		}
	});

	app.directive('datepicker', function() {
		return {
			link : function(scope, elm) {
				var elm = $(elm);
				var inputDatepicker = elm.find('input.datepicker');
				//initDatepicker(elm);
			}
		}
	});

	app.directive('selectpicker', function() {
		return {
			link : function(scope, elm) {
				var elm = $(elm);
				// initSelect2(elm);
			}
		}
	});

	app.filter('float2', function() {
		return function(number) {
			if (typeof number === "undefined") {
				return '0.00';
			}
			number = parseFloat(number);
			number = number.toFixed(2);

			return number;
		};
	});

	function getUrlParams($location) {
		return $location.search();
	}

	function setUrlParams($location, params, table) {
		var id = 'idTableInvoice';
		$location.search(params.url());
	}

	function setRefreshData(table, $interval, $scope) {
		var refreshMs = table.data('refresh');

		if (typeof refreshMs !== 'undefined') {
			$interval(function() {
				$scope.tableParams.reload();
			}, refreshMs)
		}
	}

	function isTableData(table) {
		var data = table.data('json');

		if (typeof data !== "undefined") {
			return true;
		} else {
			return false;
		}
	}

	function isTableAjax(table) {
		var url = table.data('url');

		if (typeof url !== "undefined") {
			return true;
		} else {
			return false;
		}
	}

	function getData($defer, table, params, $filter) {
		var data = table.data('json');

		var orderedData = params.sorting() ? $filter('orderBy')(data,
				params.orderBy()) : data;

		orderedData = params.filter() ? $filter('filter')(orderedData,
				params.filter()) : data;

		orderedData = orderedData.slice((params.page() - 1) * params.count(),
				params.page() * params.count());

		setTableAfterNewData(table, orderedData.length);

		params.total(data.length);
		$defer.resolve(orderedData);
	}

	function getDataAjax($scope, $defer, Api, $resource, params, $timeout,
			table) {
		Api.get(params.url(), function(data) {
			$timeout(function() {
				params.total(data.counter);

				setTotals($scope, table, data.rows);
				setAverages($scope, table, data.rows);
				setTableAfterNewData(table, data.rows.length);

				$defer.resolve(data.rows);
			}, 0);
		});
	}

	function getSorting(table) {
		var tableSorting = table.data('sorting');
		return tableSorting;
	}

	function getApi(table, $resource) {
		if (isTableAjax(table)) {
			return $resource(table.data('url'));
		}
	}

	function setTableCustomElements(table) {
		table
				.append('<tr class="rowNoData" style="display:none;"><td colspan="100%"><div class="emptyTable"><div class="imgEmptyTable"><i class="fa fa-reply-all fa-rotate-180"></i></div><div class="textEmptyTable"><h1>Brak pozycji w tabeli</h1><h2>Dodaj nową pozycję do tabeli, klikając przycisk <strong>dodaj</strong>.</h2></div></div></td></tr>');
	}

	function setTableAfterNewData(table, rows) {
		var checkboxAll = angular.element(table.find("input.select_all"));
		checkboxAll.prop('checked', false);
		var footer = table.find('tfoot');

		if (rows == 0) {
			table.find('.rowNoData').show();
			footer.hide();
		} else {
			table.find('.rowNoData').hide();
			footer.show();
		}
	}

	function setTotals($scope, table, rows) {
		var totals = table.data('total');
		if (typeof totals === "undefined") {
			return;
		}
		$.each(totals, function(index, varName) {
			var sum = 0;
			$.each(rows, function(index, row) {
				sum += row[varName];
			})
			$scope[varName] = Utils.float02(sum);
		});
	}

	function setAverages($scope, table, rows) {
		var totals = table.data('average');
		if (typeof totals === "undefined") {
			return;
		}
		$.each(totals, function(index, varName) {
			var sum = 0;
			var count = 0;
			$.each(rows, function(index, row) {
				sum += row[varName];
				count++;
			})
			var average = sum / count;

			$scope[varName] = '~ ' + Utils.float02(average);
		});
	}

})();

function reloadNgTable() {
	var tableControllers = $('div[ng-controller="TableCtrl"]');
	tableControllers.each(function() {
		var tableController = $(this);

		angular.element(tableController).scope().tableParams.reload();
	});
}
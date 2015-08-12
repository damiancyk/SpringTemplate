(function() {
	var app = angular
			.module('table',
					[ 'ngCookies', 'ngTable', 'ngTableExport', 'ngResource' ])
			.config(
					[
							'$httpProvider',
							function($httpProvider) {
								// Initialize get if not there
								if (!$httpProvider.defaults.headers.get) {
									$httpProvider.defaults.headers.get = {};
								}

								// Enables Request.IsAjaxRequest() in ASP.NET
								// MVC
								$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

								// Disable IE ajax request caching
								$httpProvider.defaults.headers.get['If-Modified-Since'] = '0';
							} ]);

	app.controller('TableCtrl', function($scope, $filter, $http, ngTableParams,
			$sce, $element, $resource, $timeout, $q, $location, $interval,
			$cookies) {
		var table = $element.find('table');
		var Api = getApi(table, $resource);
		setTableCustomElements(table);

		$scope.tableParams = new ngTableParams(angular.extend({
			page : 1,
			count : getCountPerPage($cookies, table),
			sorting : getSorting(table)
		}, $location.search()), {
			total : 0,
			getData : function($defer, params) {

				setUrlParams($location, params, table, $cookies);

				if (isTableData(table)) {
					getData($defer, table, params, $filter, $scope);
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

		$scope.getLp = function(index) {
			return $scope.tableParams.count() * ($scope.tableParams.page() - 1)
					+ index + 1;
		}

		setRefreshData(table, $interval, $scope);

		// $timeout(test(), 1000);

		var datepickers = table.find('input.datepicker');
		initDatepicker(datepickers);
	});

	app.directive('tableRow', function() {
		return {
			link : function(scope, elm) {
				var elm = $(elm);
				var checks = elm.find('input.check');
				var links = elm.find('a');

				checks.each(function() {
					var check = $(this);
					setRowState($(this), false)
				})

				elm.click(function() {
					var check = $(this).find('input.check');
					if (check.is(':visible')) {
						var newState = !check.is(':checked');
						check.prop('checked', newState);
						setRowState($(this), newState)
					}
				})
			}
		}
	});

	app.directive('datepicker', function() {
		return {
			link : function(scope, elm) {
				var elm = $(elm);
				var inputDatepicker = elm.find('input.datepicker');
				initDatepicker(elm);
			}
		}
	});

	app.directive('selectpicker', function() {
		return {
			link : function(scope, elm) {
				var elm = $(elm);
				initSelect2(elm);
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

	app.filter('float0', function() {
		return function(number) {
			if (typeof number === "undefined") {
				return '0';
			}
			number = parseFloat(number);
			number = number.toFixed(0);

			return number;
		};
	});

	function getCountPerPage($cookies, table) {
		var key = 'ngTableCount';

		// var count = $cookies[key];
		var count = table.data('count');
		// if (typeof count === 'undefined') {
		if (typeof count === 'undefined') {
			// log('count '+count)
			count = 10;
		}
		// }
		// $cookies[key] = count;

		return count;
	}

	function getUrlParams($location, table, $cookies) {
		var key = 'idTableInvoiceUrl';
		var url = $cookies[key];
		if (url === 'undefined') {
			url = $location.search();
		}

		return url;
	}

	function setUrlParams($location, params, table, $cookies) {
		if (table.data('urlParams') == false) {
			return;
		}
		var key = 'idTableInvoiceUrl';

		var url = params.url();
		$cookies[key] = url;

		$location.search(url);
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

	function getData($defer, table, params, $filter, $scope) {
		var data = table.data('json');

		var orderedData = params.sorting() ? $filter('orderBy')(data,
				params.orderBy()) : data;

		orderedData = params.filter() ? $filter('filter')(orderedData,
				params.filter()) : data;

		orderedData = orderedData.slice((params.page() - 1) * params.count(),
				params.page() * params.count());

		setTotals($scope, table, data);
		setAverages($scope, table, data);
		setTableAfterNewData(table, orderedData.length);

		params.total(data.length);
		$defer.resolve(orderedData);
	}

	function getDataAjax($scope, $defer, Api, $resource, params, $timeout,
			table) {
		var errorMsg = 'Wystąpił błąd podczas pobierania danych';

		Api.get(params.url(), function(data) {
			$timeout(function() {
				if (typeof data.rows === 'undefined'
						|| typeof data.counter === 'undefined') {
					alertError(errorMsg)
					console.error('Bad structure of received data')
					return;
				}

				params.total(data.counter);
				setTotals($scope, table, data.rows);
				setAverages($scope, table, data.rows);
				setTableAfterNewData(table, data.rows.length);

				$defer.resolve(data.rows);
			}, 0);
		}, function(response) {
			alertError(errorMsg)
			console.error('Server error ' + response.status)
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
				.append('<tr class="rowNoData" style="display:none;"><td colspan="100%"><div class="emptyTable"><div class="imgEmptyTable"><i class="fa fa-reply-all fa-rotate-180"></i></div><div class="textEmptyTable"><h1>Brak pozycji w tabeli</h1><h1>No data found</h1><h2>Dodaj nową pozycję do tabeli, klikając przycisk <strong>dodaj</strong>.</h2><h2>Add new position by click <strong>Add</strong>.</h2></div></div></td></tr>');
	}

	function setTableAfterNewData(table, rows) {
		var checkboxAll = angular.element(table.find("input.select_all"));
		checkboxAll.prop('checked', false);
		var footer = table.find('tfoot');
		var rowNoData = table.find('.rowNoData');
		var checkAll = table.find('input.select_all');
		var checkAllLabel = checkAll.next();

		if (rows == 0) {
			rowNoData.show();
			footer.hide();
			checkAll.hide();
			checkAllLabel.addClass('disable-checkbox');
			checkAllLabel.removeClass('check-label');
			// checkAllLabel.hide();
		} else {
			rowNoData.hide();
			footer.show();
			checkAll.show();
			// checkAllLabel.show();
			checkAllLabel.removeClass('disable-checkbox');
			checkAllLabel.addClass('check-label');
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
				var rowValue = row[varName];
				if (!isNaN(rowValue)) {
					sum += rowValue;
				}
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
				var rowValue = row[varName];
				if (!isNaN(rowValue)) {
					sum += rowValue;
				}

				count++;
			})
			var average = sum / count;

			$scope[varName] = '~ ' + Utils.float02(average);
		});
	}

})();

function reloadNgTable() {
	var tableControllers = $('div[ng-controller=TableCtrl]');

	tableControllers.each(function() {
		var divController = $(this);
		var tableController = angular.element(divController);
		var scope = tableController.scope();
		var tableParams = scope.tableParams;
		tableParams.reload();
	});
}

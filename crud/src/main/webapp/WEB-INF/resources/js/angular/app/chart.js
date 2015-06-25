(function() {
	var app = angular.module('chart', [ 'chart.js' ]);

	app.controller("LineCtrl", function($scope, $resource, $element) {
		var chart = $element.find('canvas.chart');
		var url = chart.data('url');
		var Api = $resource(url);
		$scope.series = chart.data('series');
		$scope.params = {
			startDate : '2014-01-01',
			endDate : '2015-12-31',
			timeGroupingType : 'MONTH'
		};

		$scope.$watch('params', function() {
			getData($scope, Api, $scope.params);
			// log('params '+JSON.stringify($scope.params))
		}, true);
		getData($scope, Api);

		/*
		 * $scope.labels = [ "January", "February", "March", "April", "May",
		 * "June", "July" ]; $scope.series = [ 'Series A', 'Series B' ];
		 * $scope.data = [ [ 65, 59, 80, 81, 56, 55, 40 ], [ 28, 48, 40, 19, 86,
		 * 27, 90 ] ];
		 */

		$scope.onClick = function(points, evt) {
			console.log(points, evt);
		};

		$scope.getDataVar = function() {
			var idChart = chart.attr('id');
			var dataVar = idChart + 'Data';
			return dataVar;
		}
	});

	function parseData($scope, data) {
		var points = data.points;
		var labels = [];
		var data = [];
		var series1 = [];
		var dataVar = $scope.getDataVar();

		$.each(points, function(key, point) {
			var orderName = point.orderName;
			var orderNumber = point.orderNumber;
			var count = point.count;
			if (typeof count === "undefined") {
				count = 0;
			}

			labels.push(orderName);
			series1.push(count);
		});
		data.push(series1);
		$scope.labels = labels;
		$scope.data = data;

	}

	function getData($scope, Api, params) {
		Api.get(params, function(data) {
			parseData($scope, data);
		});
	}

})();
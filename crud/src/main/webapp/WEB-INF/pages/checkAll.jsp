<script type="text/ng-template" id="ng-table/headers/checkbox.html">
	<input type="checkbox" class="check select_all" ng-model="checkboxes.checked" name="filter-checkbox" value="" />
	<label class="check-label" onclick="setCheckboxes(this, event)"></label>
</script>

<script type="text/ng-template" id="ng-table/filters/date.html">
 	<input type="text" ng-model="params.filter()[name]" value="" class="datepicker form-control" datepicker/> 
</script>

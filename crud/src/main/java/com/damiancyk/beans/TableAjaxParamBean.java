package com.damiancyk.beans;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

public class TableAjaxParamBean {

	private String phrase;
	private Integer count;
	private Integer page;
	// np filter[invoiceDate]=2015-05-15
	private HashMap<String, String> filter;
	private HashMap<String, String> sorting;
	// operacja filtrowania
	// np op[invoiceDate]=biggerThan
	private HashMap<String, String> op;

	public void setPagination(Query query) {
		if (getCount() != null && getCount() > 0) {
			query.setMaxResults(getCount());
		}
		if (getPage() != null && getCount() != null) {
			query.setFirstResult(getCount() * (getPage() - 1));
		}
	}

	public void setHql(StringBuilder hql, HashMap<String, String> aliases) {
		setHqlFilter(hql, aliases);
		setHqlSort(hql, aliases);

		System.out.println("HQL " + hql);
	}

	public void setHqlCount(StringBuilder hql, HashMap<String, String> aliases) {
		setHqlFilter(hql, aliases);

		System.out.println("HQL COUNT " + hql);
	}

	private void setHqlSort(StringBuilder hql, HashMap<String, String> aliases) {
		StringBuilder hqlSort = new StringBuilder();

		if (sorting != null) {
			boolean first = true;
			for (Entry<String, String> sort : sorting.entrySet()) {
				String selector = sort.getKey();
				boolean desc = "desc".equals(sort.getValue());

				String alias = aliases.get(selector);

				if (alias != null) {
					if (first) {
						hqlSort.append(" order by");
					} else {
						hqlSort.append(",");
					}

					hqlSort.append(" ");
					hqlSort.append(alias);
					if (desc) {
						hqlSort.append(" desc");
					}

					first = false;
				}
			}
		}

		hql.append(hqlSort);
	}

	private HashSet<String> setHqlFilter(StringBuilder hql,
			HashMap<String, String> aliases) {
		StringBuilder hqlFilter = new StringBuilder();

		if (filter != null) {
			for (Entry<String, String> f : filter.entrySet()) {
				String filterField = f.getKey();
				String filterValue = f.getValue();
				String filterOperation = "contains";

				if (aliases.get(filterField) != null) {
					hqlFilter.append(" and");
					addFilterOperation(filterField, filterOperation,
							filterValue, hqlFilter, aliases);
				}
			}
		}

		// if (hqlFilter.length() > 0) {
		// hqlFilter.insert(0, " and");
		// }
		hql.append(hqlFilter);

		return null;
	}

	private Entry<String/* pole */, String/* operacja */> addFilterOperation(
			String filterField, String filterOperation, String filterValue,
			StringBuilder hqlFilter, HashMap<String, String> aliases) {

		StringBuilder hqlOperation = new StringBuilder();

		String field = aliases.get(filterField);

		String operationBegin = null;

		String operationEnd = null;

		if ("contains".equals(filterOperation)) {
			operationBegin = "LIKE '%";
			operationEnd = "%'";
		} else if ("notcontains".equals(filterOperation)) {
			operationBegin = "NOT LIKE '%";
			operationEnd = "%'";
		} else if ("startswith".equals(filterOperation)) {
			operationBegin = "LIKE '";
			operationEnd = "%'";
		} else if ("endswith".equals(filterOperation)) {
			operationBegin = "LIKE '%";
			operationEnd = "'";
		} else if ("=".equals(filterOperation)) {
			operationBegin = "='";
			operationEnd = "'";
		} else if (">".equals(filterOperation)) {
			operationBegin = "> '";
			operationEnd = "'";
		} else if ("<".equals(filterOperation)) {
			operationBegin = "< '";
			operationEnd = "'";
		} else if (">=".equals(filterOperation)) {
			operationBegin = ">=";
		} else if ("<=".equals(filterOperation)) {
			operationBegin = "<= '";
			operationEnd = "'";
		} else if ("<>".equals(filterOperation)) {
			operationBegin = "!= '";
			operationEnd = "'";
		}

		if (operationEnd == null) {
			operationEnd = "";
		}

		hqlOperation.append(" " + field + " " + operationBegin + filterValue
				+ operationEnd);

		hqlFilter.append(hqlOperation);

		Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<String, String>(
				filterField, filterOperation);

		return entry;
	}

	private HashMap<String, String> getAliasesFromQuery(
			HashMap<String, String> queryParams, HashMap<String, String> aliases) {
		HashMap<String, String> aliasesInQuery = new HashMap<String, String>();
		if (queryParams != null && aliases != null) {
			for (Entry<String, String> queryParam : queryParams.entrySet()) {
				String aliasValue = aliases.get(queryParam.getKey());
				if (aliasValue != null) {
					aliasesInQuery.put(queryParam.getKey(), aliasValue);
				}
			}

		}

		return aliasesInQuery;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public HashMap<String, String> getFilter() {
		return filter;
	}

	public void setFilter(HashMap<String, String> filter) {
		this.filter = filter;
	}

	public HashMap<String, String> getSorting() {
		return sorting;
	}

	public void setSorting(HashMap<String, String> sorting) {
		this.sorting = sorting;
	}

	public HashMap<String, String> getOp() {
		return op;
	}

	public void setOp(HashMap<String, String> op) {
		this.op = op;
	}

}

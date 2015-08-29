package com.damiancyk.beans;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

public class TableAjaxParamBean {

	public static final String NVARCHAR = "{nvarchar}";
	private Integer count;
	private Integer page;
	/*
	 * np filter[invoiceDate]=2015-05-15
	 */
	private HashMap<String, Object> filter;
	private HashMap<String, String> sorting;

	// TODO operacja filtrowania
	/*
	 * np op[invoiceDate]=biggerThan
	 */
	// private HashMap<String, String> op;

	public void setPagination(Query query) {
		if (getCount() != null && getCount() > 0) {
			query.setMaxResults(getCount());
		}
		if (getPage() != null && getCount() != null) {
			query.setFirstResult(getCount() * (getPage() - 1));
		}
	}

	public void setQuery(StringBuilder hql, HashMap<String, String> aliases, HashMap<String, Object> queryParams) {
		setHqlFilter(hql, aliases, queryParams);
		setHqlSort(hql, aliases);

		System.out.println("QUERY:: " + hql);
	}

	public void setQueryCount(StringBuilder hql, HashMap<String, String> aliases, HashMap<String, Object> queryParams) {
		setHqlFilter(hql, aliases, queryParams);

		System.out.println("QUERY COUNT:: " + hql);
	}

	public Query getQueryHql(StringBuilder hql, HashMap<String, String> aliases, EntityManager em) {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();

		setQuery(hql, aliases, queryParams);
		Query query = em.createQuery(hql.toString());
		setPagination(query);
		setQueryParams(queryParams, query);

		return query;
	}

	public Query getQuerySql(StringBuilder sql, HashMap<String, String> aliases, EntityManager em) {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		setQuery(sql, aliases, queryParams);
		Query query = em.createNativeQuery(sql.toString());
		setPagination(query);
		setQueryParams(queryParams, query);

		return query;
	}

	private void setQueryParams(HashMap<String, Object> queryParams, Query query) {
		if (queryParams.size() > 0) {
			for (Entry<String, Object> queryParam : queryParams.entrySet()) {
				String paramName = queryParam.getKey();
				Object paramValue = queryParam.getValue();

				query.setParameter(paramName, paramValue);
			}

		}
	}

	public Query getQueryHqlCount(StringBuilder hql, HashMap<String, String> aliases, EntityManager em) {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		setQueryCount(hql, aliases, queryParams);
		Query query = em.createQuery(hql.toString());
		setQueryParams(queryParams, query);

		return query;
	}

	public Query getQuerySqlCount(StringBuilder sql, HashMap<String, String> aliases, EntityManager em) {
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		setQueryCount(sql, aliases, queryParams);
		Query query = em.createNativeQuery(sql.toString());
		setQueryParams(queryParams, query);

		return query;
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

					if (alias.contains(NVARCHAR)) {
						alias = replaceVarCharMark(alias);
					}

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

	private HashSet<String> setHqlFilter(StringBuilder hql, HashMap<String, String> aliases,
			HashMap<String, Object> queryParams) {
		StringBuilder hqlFilter = new StringBuilder();

		if (filter != null) {
			boolean firstFilter = true;
			boolean containsWhere = hql.toString().contains(" where");

			for (Entry<String, Object> f : filter.entrySet()) {
				String filterField = f.getKey();
				Object filterValue = f.getValue();
				String filterOperation = "contains";

				if (aliases.get(filterField) != null) {
					if (firstFilter && !containsWhere) {
						hqlFilter.append(" where ");
					} else {
						hqlFilter.append(" and ");
					}
					addFilterOperations(filterField, filterOperation, filterValue, hqlFilter, aliases, queryParams);
					firstFilter = false;
				}

			}
		}

		hql.append(hqlFilter);

		return null;
	}

	// pole <-> aliasy po przecinkach
	private void addFilterOperations(String filterField, String filterOperation, Object filterValue,
			StringBuilder hqlFilter, HashMap<String, String> aliases, HashMap<String, Object> queryParams) {

		String fields = aliases.get(filterField);
		String[] fieldsTab = fields.split(",");

		int length = fieldsTab.length;
		if (length == 1) {
			String field = fieldsTab[0];
			addFilterOperation(filterField, filterOperation, filterValue, hqlFilter, field, queryParams);
		} else if (length > 1) {
			hqlFilter.append("(");
			for (int i = 0; i < length; i++) {
				if (i > 0) {
					hqlFilter.append(" or ");
				}
				String field = fieldsTab[i];
				addFilterOperation(filterField, filterOperation, filterValue, hqlFilter, field, queryParams);
			}
			hqlFilter.append(")");
		}

	}

	// pole <-> alias
	private void addFilterOperation(String filterField, String filterOperation, Object filterValue,
			StringBuilder hqlFilter, String field, HashMap<String, Object> queryParams) {

		StringBuilder hqlOperation = new StringBuilder();

		String operationBegin = null;

		String operationEnd = null;

		if ("contains".equals(filterOperation)) {
			operationBegin = "LIKE ";
			operationEnd = "";
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

		// " CONVERT(VARCHAR, "

		if (field.contains(NVARCHAR)) {
			field = replaceVarCharMark(field);

			String paramName = field.replaceAll("\\.", "_");
			StringBuilder filterValueQueryParams = new StringBuilder();
			filterValueQueryParams.append("%");
			filterValueQueryParams.append(filterValue);
			filterValueQueryParams.append("%");
			queryParams.put(paramName, filterValueQueryParams.toString());
			hqlOperation.append(field + " " + operationBegin + ":" + paramName + operationEnd);
			// hqlOperation.append(field + " like CONVERT(NVARCHAR(4000),'%" +
			// filterValue + "%')");
		} else {
			hqlOperation.append(field + " " + operationBegin + "'%" + filterValue + "%'" + operationEnd);
		}

		hqlFilter.append(hqlOperation);
	}

	private String replaceVarCharMark(String str) {
		String replaced = str.replaceAll("\\{nvarchar\\}", "");
		return replaced;
	}

	public static void main(String[] args) {
		String str = "{nvarchar}";
		System.out.println(str.contains("{nvarchar}"));
	}

	// TODO enumy
	@Deprecated
	private Object getFilterValueByType(Object o) {
		String paramStr = "" + o;

		try {
			Date obj = DateUtils.stringToDate(paramStr);
			if (obj != null) {
				return obj;
			}
		} catch (Exception e) {
		}

		try {
			Long obj = new Long(paramStr);
			if (obj != null) {
				return obj;
			}
		} catch (Exception e) {
		}

		try {
			Double obj = Utils.getDouble(paramStr);
			if (obj != null) {
				return obj;
			}
		} catch (Exception e) {
		}

		try {
			PaymentTypeEnum obj = PaymentTypeEnum.getEnum(paramStr);
			if (obj != null) {
				return obj;
			}
		} catch (Exception e) {
		}

		try {
			InvoiceTypeEnum obj = InvoiceTypeEnum.getEnum(paramStr);
			if (obj != null) {
				return obj;
			}
		} catch (Exception e) {
		}

		try {
			ParcelTrackingHistoryStatusEnum obj = ParcelTrackingHistoryStatusEnum.getEnum(paramStr);
			if (obj != null) {
				return obj;
			}
		} catch (Exception e) {
		}

		return "%" + o + "%";
	}

	private Integer getCount() {
		return count;
	}

	private Integer getPage() {
		return page;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setFilter(HashMap<String, Object> filter) {
		this.filter = filter;
	}

	public void setSorting(HashMap<String, String> sorting) {
		this.sorting = sorting;
	}

	public HashMap<String, Object> getFilter() {
		return filter;
	}

	public HashMap<String, String> getSorting() {
		return sorting;
	}

}
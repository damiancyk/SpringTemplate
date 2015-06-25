package com.damiancyk.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.damiancyk.beans.ItemBean;
import com.damiancyk.beans.TableAjaxParamBean;
import com.damiancyk.entity.Item;
import com.damiancyk.enums.StatusEnum;
import com.damiancyk.forms.ItemForm;

@Component
public class ItemPersistence {

	@PersistenceContext
	EntityManager em;

	private HashMap<String, String> getAliases() {
		HashMap<String, String> aliases = new HashMap<String, String>();

		aliases.put("name", "i.name");
		aliases.put("createDate", "i.createDate");

		return aliases;
	}

	public ArrayList<ItemBean> findViewByIds(TableAjaxParamBean params,
			Long idCompany) throws Exception {
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select new com.damiancyk.beans.ItemBean(i.idItem,i.name,i.createDate)");
			hql.append(" from Item i where i.status=:status");

			HashMap<String, String> aliases = getAliases();

			params.setHql(hql, aliases);
			Query query = em.createQuery(hql.toString());
			params.setHqlCount(hql, aliases);

			query.setParameter("status", StatusEnum.ACTIVE);

			ArrayList<ItemBean> resultList = (ArrayList<ItemBean>) query
					.getResultList();

			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("findViewByIds.error");
		}
	}

	@Transactional
	public Long save(ItemForm form) throws Exception {
		try {
			Item entity = copyValues(form, null);

			entity.setStatus(StatusEnum.ACTIVE);
			entity.setCreateDate(new Date());
			em.persist(entity);

			return entity.getIdItem();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("save.error");
		}
	}

	@Transactional
	private Item copyValues(ItemForm form, Item entity) throws Exception {
		try {
			if (entity == null) {
				entity = new Item();
			}

			entity.setName(form.getName());

			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("copyValues.error");
		}
	}

}

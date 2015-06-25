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
	public void edit(ItemForm form, Long idItem) throws Exception {
		try {
			Item entity = em.find(Item.class, idItem);
			entity = copyValues(form, entity);

			em.merge(entity);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("edit.error");
		}
	}

	@Transactional
	public void delete(Long idItem) throws Exception {
		try {
			Item entity = em.find(Item.class, idItem);
			entity.setStatus(StatusEnum.DELETED);

			em.merge(entity);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("delete.error");
		}
	}

	public ItemBean findById(Long idItem) throws Exception {
		try {
			Item entity = em.find(Item.class, idItem);

			return new ItemBean(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("findById.error");
		}
	}

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
			params.setPagination(query);

			query.setParameter("status", StatusEnum.ACTIVE);

			ArrayList<ItemBean> resultList = (ArrayList<ItemBean>) query
					.getResultList();

			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("findViewByIds.error");
		}
	}

	public Long countViewByIds(TableAjaxParamBean params, Long idCompany)
			throws Exception {
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select count(*)");
			hql.append(" from Item i where i.status=:status");

			HashMap<String, String> aliases = getAliases();

			params.setHqlCount(hql, aliases);
			Query query = em.createQuery(hql.toString());

			query.setParameter("status", StatusEnum.ACTIVE);

			Long resultList = (Long) query.getSingleResult();

			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("findViewByIds.error");
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

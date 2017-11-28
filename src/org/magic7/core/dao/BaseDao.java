package org.magic7.core.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;

public class BaseDao {
	private static final BaseDao instance = new BaseDao();
	public static BaseDao getInstance() {
		return instance;
	}
	public boolean save(Object obj) {
		boolean result = false;
		Transaction tx = null;
		try {
			Session session = DaoAssistant.currentSession();
			if(DaoAssistant.isAuto())
				tx = session.beginTransaction();
			session.saveOrUpdate(obj);
			if(DaoAssistant.isAuto())
				tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
			result = false;
			e.printStackTrace();
		} finally {
			DaoAssistant.closeSession();
		}
		return result;
	}
	public boolean update(Object obj) {
		boolean result = false;
		Transaction tx = null;
		try {
			Session session = DaoAssistant.currentSession();
			//if(HibernateUtil.isAuto())
				tx = session.beginTransaction();
			//session.flush();
			session.merge(obj);
			//if(HibernateUtil.isAuto()) {
				tx.commit();
			//}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
			result = false;
			if(!DaoAssistant.isAuto())
				throw new RuntimeException(e);
			e.printStackTrace();
		} finally {
			DaoAssistant.closeSession();
		}
		return result;
	}
	public Integer listCount(String hql){
		try {
			Session session = DaoAssistant.currentSession();
			return ((Number) session.createQuery(hql).uniqueResult()).intValue();
		} catch (HibernateException e) {
			e.printStackTrace();
			DaoAssistant.closeSession();
			throw new RuntimeException(e.getCause());
		} finally {
			DaoAssistant.closeSession();
		}
	}
	@SuppressWarnings("rawtypes")
	public List list(String hql,Integer start,Integer count) {
		try {
			Session session = DaoAssistant.currentSession();
			Query  query = session.createQuery(hql);
			query.setFirstResult(start).setMaxResults(count);
			return query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
			DaoAssistant.closeSession();
			throw new RuntimeException(e.getCause());
		} finally {
			DaoAssistant.closeSession();
		}
	}
	@SuppressWarnings("rawtypes")
	public Integer listCount(String hql,Map<String,Object> values) {
		try {
			Session session = DaoAssistant.currentSession();
			Query query = session.createQuery(hql);

			for (Iterator<String> iter = values.keySet().iterator(); iter.hasNext();) {
				String key = iter.next();
				Object value = values.get(key);
				if (value instanceof Collection) {
					query.setParameterList(key, (Collection) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(key, (Object[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			return ((Number) query.uniqueResult()).intValue();
		} catch (HibernateException e) {
			e.printStackTrace();
			DaoAssistant.closeSession();
			throw new RuntimeException(e.getCause());
		} finally {
			DaoAssistant.closeSession();
		}
	}
	@SuppressWarnings("rawtypes")
	public List list(String hql,Map<String,Object> values,Integer start,Integer count) {
		try {
			Session session = DaoAssistant.currentSession();
			Query query = session.createQuery(hql);

			for (Iterator iter = values.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				Object value = values.get(key);

				if (value instanceof Collection) {
					query.setParameterList(key, (Collection) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(key, (Object[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			return query.setFirstResult(start).setMaxResults(count).list();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getCause());
		} finally {
			DaoAssistant.closeSession();
		}
	}
	@SuppressWarnings("rawtypes")
	public List listWithSql(String sql,Map<String,Object> values,String collectionName,String className,Integer start,Integer count) {
		try {
			Session session = DaoAssistant.currentSession();
			SQLQuery query = session.createSQLQuery(sql);

			for (Iterator iter = values.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				Object value = values.get(key);

				if (value instanceof Collection) {
					query.setParameterList(key, (Collection) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(key, (Object[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			return query.addEntity(collectionName,className).setFirstResult(start).setMaxResults(count).list();
		} catch (HibernateException e) {
			e.printStackTrace();
			//HibernateUtil.closeSession();
			throw new RuntimeException(e.getCause());
		} finally {
			DaoAssistant.closeSession();
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String,Object>> listMap(String hql,Map<String,Object> values,Integer start,Integer count) {
		try {
			Session session = DaoAssistant.currentSession();
			Query query = session.createQuery(hql);
			for (Iterator iter = values.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				Object value = values.get(key);

				if (value instanceof Collection) {
					query.setParameterList(key, (Collection) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(key, (Object[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			return query.setFirstResult(start).setMaxResults(count).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getCause());
		} finally {
			DaoAssistant.closeSession();
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String,Object>> listMapWithSQL(String hql,Map<String,Object> values,Integer start,Integer count) {
		try {
			Session session = DaoAssistant.currentSession();
			Query query = session.createSQLQuery(hql);
			for (Iterator iter = values.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				Object value = values.get(key);
				if (value instanceof Collection) {
					query.setParameterList(key, (Collection) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(key, (Object[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			return query.setFirstResult(start).setMaxResults(count).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getCause());
		} finally {
			DaoAssistant.closeSession();
		}
	}
	@SuppressWarnings("rawtypes")
	public Integer listCountWithSQL(String hql,Map<String,Object> values) {
		try {
			Session session = DaoAssistant.currentSession();
			Query query = session.createSQLQuery(hql);

			for (Iterator<String> iter = values.keySet().iterator(); iter.hasNext();) {
				String key = iter.next();
				Object value = values.get(key);
				if (value instanceof Collection) {
					query.setParameterList(key, (Collection) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(key, (Object[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			return ((Number) query.uniqueResult()).intValue();
		} catch (HibernateException e) {
			e.printStackTrace();
			DaoAssistant.closeSession();
			throw new RuntimeException(e.getCause());
		} finally {
			DaoAssistant.closeSession();
		}
	}
	public boolean saveWithOutTransactionControll(Object obj) {
		boolean result = false;
		try {
			Session session = DaoAssistant.currentSession();
			session.saveOrUpdate(obj);
			result = true;
		} catch (HibernateException e) {
			e.printStackTrace();
			DaoAssistant.closeSession();
			throw new RuntimeException(e.getCause());
		}
		return result;
	}
	protected Object getObject(Class<?> clazz,Long id) {
		Object result = null;
		try {
			Session session = DaoAssistant.currentSession();
			result = session.get(clazz,id);

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} finally {
			DaoAssistant.closeSession();
		}
		return result;
	}
	public Object getObject(Class<?> clazz,String id) {
		Object result = null;
		try {
			Session session = DaoAssistant.currentSession();
			result = session.get(clazz,id);

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} finally {
			DaoAssistant.closeSession();
		}
		return result;
	}
	@SuppressWarnings("rawtypes")
	public Object getObject(String hql,Map<String,Object> values) {
		try {
			Session session = DaoAssistant.currentSession();
			Query query = session.createQuery(hql);
			
			if(values!=null)
				for (Iterator<String> iter = values.keySet().iterator(); iter.hasNext();) {
					String key = iter.next();
					Object value = values.get(key);
					if (value instanceof Collection) {
						query.setParameterList(key, (Collection) value);
					} else if (value instanceof Object[]) {
						query.setParameterList(key, (Object[]) value);
					} else {
						query.setParameter(key, value);
					}
				}
			return query.uniqueResult();
		} catch (HibernateException e) {
			e.printStackTrace();
			DaoAssistant.closeSession();
			throw new RuntimeException(e.getCause());
		} finally {
			DaoAssistant.closeSession();
		}
	}
	@SuppressWarnings({ "rawtypes"})
	protected Object getObjectWithSQL(String hql,String className,Map<String,Object> values) {
		try {
			Session session = DaoAssistant.currentSession();
			SQLQuery query = session.createSQLQuery(hql);
			
			if(values!=null)
				for (Iterator<String> iter = values.keySet().iterator(); iter.hasNext();) {
					String key = iter.next();
					Object value = values.get(key);
					if (value instanceof Collection) {
						query.setParameterList(key, (Collection) value);
					} else if (value instanceof Object[]) {
						query.setParameterList(key, (Object[]) value);
					} else {
						query.setParameter(key, value);
					}
				}
			return query.addEntity(null,className).uniqueResult();
		} catch (HibernateException e) {
			e.printStackTrace();
			DaoAssistant.closeSession();
			throw new RuntimeException(e.getCause());
		} finally {
			DaoAssistant.closeSession();
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Map<String,Object> getMapWithSQL(String hql,Map<String,Object> values) {
		try {
			Session session = DaoAssistant.currentSession();
			Query query = session.createSQLQuery(hql);
			
			if(values!=null)
				for (Iterator<String> iter = values.keySet().iterator(); iter.hasNext();) {
					String key = iter.next();
					Object value = values.get(key);
					if (value instanceof Collection) {
						query.setParameterList(key, (Collection) value);
					} else if (value instanceof Object[]) {
						query.setParameterList(key, (Object[]) value);
					} else {
						query.setParameter(key, value);
					}
				}
			return (Map<String, Object>) query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
		} catch (HibernateException e) {
			e.printStackTrace();
			DaoAssistant.closeSession();
			throw new RuntimeException(e.getCause());
		} finally {
			DaoAssistant.closeSession();
		}
	}
	public boolean delete(Object obj) {
		boolean result = false;
		Transaction tx = null;
		try {
			Session session = DaoAssistant.currentSession();
			if(DaoAssistant.isAuto())
				tx = session.beginTransaction();
			session.delete(obj);
			if(DaoAssistant.isAuto())
				tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
			result = false;
		} finally {
			DaoAssistant.closeSession();
		}
		return result;
	}
	
	public Boolean deleteById(String hql,Object id) {
		if(id==null||"".equals(id))
			throw new RuntimeException("id should not be null");
		Session session = DaoAssistant.currentSession();
		Transaction tx = null;
		try {
			if(DaoAssistant.isAuto())
				tx = session.beginTransaction();
			session.createQuery(hql).setString(0, id.toString()).executeUpdate();
			if(DaoAssistant.isAuto())
				tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			DaoAssistant.closeSession();
		}
		return false;
	}
	@SuppressWarnings("rawtypes")
	public Boolean delete(String hql,Map<String,Object> values) {
		Session session = DaoAssistant.currentSession();
		Transaction tx = null;
		try {
			if(DaoAssistant.isAuto()){
				tx = session.beginTransaction();
			}
			Query delete = session.createQuery(hql);
			if(values!=null){
				for (Iterator<String> iter = values.keySet().iterator(); iter.hasNext();) {
					String key = iter.next();
					Object value = values.get(key);
					if (value instanceof Collection) {
						delete.setParameterList(key, (Collection) value);
					} else if (value instanceof Object[]) {
						delete.setParameterList(key, (Object[]) value);
					} else {
						delete.setParameter(key, value);
					}
				}
			}
			delete.executeUpdate();
			if(DaoAssistant.isAuto()){
				tx.commit();
			}
			return true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			DaoAssistant.closeSession();
		}
		return false;
	}
	public static Integer batchNum = 100000;
	@SuppressWarnings({ "unchecked" })
	public List<Map<String,Object>> listMapWithSql(String hql,Integer start,Integer count) {
		try {
			Session session = DaoAssistant.currentSession();
			Query query = session.createSQLQuery(hql);
			return query.setFirstResult(start).setMaxResults(count).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getCause());
		} finally {
			DaoAssistant.closeSession();
		}
	}
}

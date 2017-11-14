package org.magic7.core.dao;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.magic7.utils.SpringContextUtil;
import org.magic7.utils.SpringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

public class DaoAssistant {

	private static SessionFactoryImpl sessionFactory;

	private static Configuration config = null;

	/*public static void init() {
		try {
			if (sessionFactory == null) {
				//hibernate4初始化代码
				//Configuration cfg = new Configuration().configure();  
				//ServiceRegistry sr = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry(); 
				//hibernate5初始化代码
				//ServiceRegistry sr = cfg.getStandardServiceRegistryBuilder().build();
				//sessionFactory = cfg.buildSessionFactory(sr);
				ServiceRegistry sr = cfg.getStandardServiceRegistryBuilder().build();
				sessionFactory = SpringUtil.getBean("sessionFactory");
				LocalSessionFactoryBean localBean = SpringUtil.getBean("&sessionFactory");
				config = localBean.getConfiguration();
				//sessionFactory = config.configure().buildSessionFactory();\
			} 
 
		} catch (Throwable e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}*/
	
	public static void init() {
		try {
			if (sessionFactory == null) {
				ApplicationContext ac = SpringContextUtil.getApplicationContext();
				if(ac!=null) {
					sessionFactory = (SessionFactoryImpl) ac.getBean("sessionFactory");
					LocalSessionFactoryBean localBean = (LocalSessionFactoryBean) ac.getBean("&sessionFactory");
					config = localBean.getConfiguration();
				} else {
					sessionFactory = SpringUtil.getBean("sessionFactory");
					LocalSessionFactoryBean localBean = SpringUtil.getBean("&sessionFactory");
					config = localBean.getConfiguration();
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}

	private static final ThreadLocal<Object> session = new ThreadLocal<Object>();
	private static final ThreadLocal<Boolean> isAuto = new ThreadLocal<Boolean>();
	private static final ThreadLocal<Transaction> tx = new ThreadLocal<Transaction>();
	
	public static Session currentSession(Boolean auto) {
		return  currentSession(null,auto);
	}
	public static Session currentSession(EmptyInterceptor interceptor) {
		return  currentSession(interceptor,false);
	}
	public static Session currentSession(EmptyInterceptor interceptor,Boolean auto) {
		if (sessionFactory == null) {
			synchronized (DaoAssistant.class) {
				if (sessionFactory == null)
					DaoAssistant.init();
			}
		}

		Session s = (Session) session.get();
		Boolean autoShade = isAuto.get();
		if(autoShade==null) {
			if(auto==null)
				auto = true;
			isAuto.set(auto);
		}

		if (s == null) {
			if(interceptor!=null)
				s = sessionFactory.withOptions().interceptor(interceptor).openSession();
			else
				s = sessionFactory.openSession();
			
			s.setFlushMode(FlushMode.AUTO);
			session.set(s);
		} else if (!s.isOpen()) {
			s = sessionFactory.openSession();
			session.set(s);
			s.setFlushMode(FlushMode.AUTO);
		}
		return s;
	}
	public static Session currentSession() {
		return currentSession(true);
	}
	public static Boolean isAuto() {
		return isAuto.get();
	}
	public static void closeSession() {
		if(DaoAssistant.isAuto()) {
			Session s = (Session) session.get();
			if (s != null)
				s.close();
			session.set(null);
			isAuto.set(null);
			System.out.println("----------------------------------------------------closeSession"); 
		}
	}
	public static void closeSessionByService() {
		Session s = (Session) session.get();
		if (s != null)
			s.close();
		session.set(null);
		isAuto.set(null);
		System.out.println("----------------------------------------------------closeSessionByService"); 
	}

	/**
	 * Default constructor.
	 */
	private DaoAssistant() {
	}
	protected static Configuration getConfig() {
		return config;
	}
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public static Boolean beginTransaction() {
		Transaction transaction = tx.get();
		if(transaction==null||!transaction.isActive()) 
			tx.set(currentSession().beginTransaction());
		return true;
	}
	public static Boolean commitTransaction() {
		Transaction transaction = tx.get();
		if(transaction==null)
			throw new RuntimeException("transaction is null");
		if(!transaction.isActive())
			throw new RuntimeException("transaction is not Active");
		try {
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
			throw new RuntimeException("commit transaction failed");
		} finally {
			tx.remove();
		}
		return true;
	}
	public static Boolean rollBackTransaction() {
		Transaction transaction = tx.get();
		if(transaction==null)
			throw new RuntimeException("transaction is null");
		if(!transaction.isActive())
			throw new RuntimeException("transaction is not Active");
		try {
			transaction.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.remove();
		}
		return true;
	}
	public static String getTableName(Class<?> clazz) {
		if(clazz==null)
			return null;
        PersistentClass pc = config.getClassMapping(clazz.getSimpleName());
        if(pc==null)
        	return null;
        if(pc.getTable()==null)
        	return null;
        return pc.getTable().getName();  
    }
	public static String getTableName(String className) {
		if(StringUtils.isEmpty(className))
			return null;
        PersistentClass pc = config.getClassMapping(className);
        if(pc==null)
        	return null;
        if(pc.getTable()==null)
        	return null;
        return pc.getTable().getName();  
    }
	public static <T> String getPropertyName(Class<T> clazz, String propertyName) {  
        String fieldName = "";  
        PersistentClass persistentClass = config.getClassMapping(clazz.getSimpleName());  
        Property property = persistentClass.getProperty(propertyName);  
        Iterator<?> iterator = property.getColumnIterator();  
        if (iterator.hasNext()) {  
            Column column = (Column) iterator.next();  
            fieldName += column.getName();  
        }
        return fieldName;
    }  
	public static String getPropertyName(String className, String propertyName) {  
        String fieldName = "";  
        PersistentClass pc = config.getClassMapping(className);  
        Property property = pc.getProperty(propertyName);
        Iterator<?> iterator = property.getColumnIterator();  
        if (iterator.hasNext()) {  
            Column column = (Column) iterator.next();  
            fieldName += column.getName();  
        }
        return fieldName;
    }
	
}

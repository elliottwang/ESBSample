package eab.entity;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import eab.dao.EABSessionFactory;

@SuppressWarnings("unchecked")
public class EABServiceDao {
	public void saveOrUpdateService(EABService service) {
		Session s = null;
		Transaction tx = null;
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			s.saveOrUpdate(service);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
	}

	public EABService loadService(String serviceName) {
		EABService service = null;
		Session s = null;
		Transaction tx = null;
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			service = (EABService) s.get(EABService.class, serviceName);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}

		return service;
	}

	public void deleteService(String serviceName) {
		EABService service = loadService(serviceName);
		if (service == null)
			return;
		
		deleteService(service);
	}
	
	public void deleteService(EABService service) {
		Session s = null;
		Transaction tx = null;

		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			s.delete(service);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
	}
	
	public int getServiceCount(String type) {
		Session s = null;
		Transaction tx = null;
		Iterator it = null;

		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();			
			Query query = null;
			String queryString = "select count(*) from EABService as a";
			if(type != null && type.length() > 0){				
				WSDLProtocolDao dao = new WSDLProtocolDao();
				queryString += " where a.protocol=:p1";
				query = s.createQuery(queryString);				
				query.setEntity("p1", dao.loadProtocol(type));
			} else {
				query = s.createQuery(queryString);
			}
			it = query.list().iterator();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
		
		if(it.hasNext()){
			return  Integer.parseInt(it.next().toString());
		}
		
		return 0;
	}
	
	public EABService[] getServices(int pages, int maxPerPage, String type){		
		Session s = null;
		Transaction tx = null;
		List list = null;		

		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();			
			Query query = null;
			String queryString = "from EABService as a";
			if(type != null && type.length() > 0){				
				WSDLProtocolDao dao = new WSDLProtocolDao();
				queryString += " where a.protocol=:p1";
				query = s.createQuery(queryString);				
				query.setEntity("p1", dao.loadProtocol(type));
			} else {
				query = s.createQuery(queryString);
			}
			query.setFirstResult((pages - 1) * maxPerPage);
			query.setMaxResults(maxPerPage);
			list = query.list();			
			
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
		
		if(list.isEmpty())
			return null;
		
		EABService[] services = new EABService[list.size()];
		Iterator it = list.iterator();
		int i = 0;
		while (it.hasNext()) {
			services[i++] = (EABService)it.next();
		}
		
		return services;
	}
}

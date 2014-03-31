package eab.entity;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import eab.dao.EABSessionFactory;

@SuppressWarnings("unchecked")
public class WSDLProtocolDao {
	public static WSDLProtocolDao dao;
	
	static{
		dao = new WSDLProtocolDao();
	}
	
	public void saveOrUpdateProtocol(WSDLProtocol protocol){
		Session s = null;
		Transaction tx = null;
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			s.saveOrUpdate(protocol);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
	}
	
	public WSDLProtocol loadProtocol(String type){
		WSDLProtocol protocol = null;
		Session s = null;
		Transaction tx = null;
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			protocol = (WSDLProtocol)s.get(WSDLProtocol.class, type);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
		
		return protocol;
	}
	
	public void deleteProtocol(String type){
		WSDLProtocol protocol = loadProtocol(type);
		if(protocol == null)
			return;
		
		deleteProtocol(protocol);
	}	
	
	public void deleteProtocol(WSDLProtocol protocol){
		Session s = null;
		Transaction tx = null;
		
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			s.delete(protocol);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
	}
	
	public WSDLProtocol[] listAllProtocols(){		
		Session s = null;
		Transaction tx = null;
		List list = null;
		
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			Criteria criteria = s.createCriteria(WSDLProtocol.class);
			list = criteria.list();			
			tx.commit();
		} catch (Exception e) {
			if(tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
		
		if(list.isEmpty())
			return null;
		
		WSDLProtocol[] protocols = new WSDLProtocol[list.size()];
		Iterator it = list.iterator();
		int i = 0;
		while (it.hasNext()) {
			protocols[i++] = (WSDLProtocol)it.next();
		}
		
		return protocols;
	}
}

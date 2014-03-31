package eab.entity;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import eab.dao.EABSessionFactory;

@SuppressWarnings("unchecked")
public class EABFlowDao {
	public void saveOrUpdateFlow(EABFlow flow){
		Session s = null;
		Transaction tx = null;
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			s.saveOrUpdate(flow);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
	}
	
	public EABFlow loadFlow(String flowName){
		EABFlow flow = null;
		Session s = null;
		Transaction tx = null;
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			flow = (EABFlow)s.get(EABFlow.class, flowName);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
		
		return flow;
	}
	
	public void deleteFlow(String flowName){
		EABFlow flow = loadFlow(flowName);
		if(flow == null)
			return;

		deleteFlow(flow);
	}
	
	public void deleteFlow(EABFlow flow){
		Session s = null;
		Transaction tx = null;
		
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			s.delete(flow);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
	}
		
	public int getFlowCount() {
		Session s = null;
		Transaction tx = null;
		Iterator it = null;

		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			Query query = s.createQuery("select count(*) from EABFlow as a");
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
	
	public EABFlow[] getFlows(int pages, int maxPerPage){		
		Session s = null;
		Transaction tx = null;
		List list = null;		

		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();			
			Query query = s.createQuery("from EABFlow as a");
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
		
		EABFlow[] flows = new EABFlow[list.size()];
		Iterator it = list.iterator();
		int i = 0;
		while (it.hasNext()) {
			flows[i++] = (EABFlow)it.next();
		}
		
		return flows;
	}
}

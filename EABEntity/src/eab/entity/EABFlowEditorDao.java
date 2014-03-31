package eab.entity;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import eab.dao.EABSessionFactory;

@SuppressWarnings("unchecked")
public class EABFlowEditorDao {
	public void saveOrUpdateTempFlow(EABFlowEditor tempFlow){
		Session s = null;
		Transaction tx = null;
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			s.saveOrUpdate(tempFlow);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
	}
	
	public EABFlowEditor loadFlow(String flowName){
		EABFlowEditor tempFlow = null;
		Session s = null;
		Transaction tx = null;
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			tempFlow = (EABFlowEditor)s.get(EABFlowEditor.class, flowName);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
		
		return tempFlow;
	}
	
	public void deleteFlow(String flowName){
		EABFlowEditor tempFlow = loadFlow(flowName);
		if(tempFlow == null)
			return;

		deleteFlow(tempFlow);
	}
	
	public void deleteFlow(EABFlowEditor tempFlow){
		Session s = null;
		Transaction tx = null;
		
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			s.delete(tempFlow);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}
	}
		
	public int getTempFlowCount() {
		Session s = null;
		Transaction tx = null;
		Iterator it = null;

		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			Query query = s.createQuery("select count(*) from EABFlowEditor as a where a.isDeployed !=:p1");
			query.setInteger("p1", 1);
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
	
	public EABFlowEditor[] getTempFlows(int pages, int maxPerPage){		
		Session s = null;
		Transaction tx = null;
		List list = null;		

		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();			
			Query query = s.createQuery("from EABFlowEditor as a where a.isDeployed !=:p1");
			query.setInteger("p1", 1);
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
		
		EABFlowEditor[] tempFlows = new EABFlowEditor[list.size()];
		Iterator it = list.iterator();
		int i = 0;
		while (it.hasNext()) {
			tempFlows[i++] = (EABFlowEditor)it.next();
		}
		
		return tempFlows;
	}
}

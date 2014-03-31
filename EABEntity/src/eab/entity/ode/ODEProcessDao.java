package eab.entity.ode;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import eab.dao.EABSessionFactory;
import eab.entity.EABFlow;
import eab.parser.WSDLProtocolParser;

@SuppressWarnings("unchecked")
public class ODEProcessDao {

	public ODEProcessDao(){		
	}
	
	public ODEProcess loadProcess(long id){
		ODEProcess process = null;
		Session s = null;
		Transaction tx = null;
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			process = (ODEProcess) s.get(ODEProcess.class, id);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}

		return process;
	}	
	
	public ODEProcess loadProcess(String type){
		ODEProcess process = null;
		Session s = null;
		Transaction tx = null;
		Iterator it = null;
		
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			Query query = s.createQuery("from ODEProcess as a where a.processType=:p1 and " +
					"a.version=(select max(b.version) from ODEProcess as b where b.processType=:p1)");
			query.setString("p1", type);
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
			return (ODEProcess)it.next();
		}

		return process;
	}	
	
	public int getProcessCount(){
		Session s = null;
		Transaction tx = null;
		Iterator it = null;

		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();			
			Query query = s.createQuery("select count(distinct a.processType) from ODEProcess as a");
			it = query.list().iterator();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			s.close();
		}
		
		if(it.hasNext()){
			return  Integer.parseInt(it.next().toString());
		}
		
		return 0;
	}
	
	public ODEProcess[] getProcesses(int pages, int maxPerPage){		
		Session s = null;
		Transaction tx = null;
		List list = null;		

		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();			
			Query query = s.createQuery("from ODEProcess as a where a.version=" +
					"(select max(b.version) from ODEProcess as b where b.processType=a.processType)");
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
		
		ODEProcess[] processes = new ODEProcess[list.size()];
		Iterator it = list.iterator();
		int i = 0;
		while (it.hasNext()) {
			processes[i++] = (ODEProcess)it.next();
		}
		
		return processes;
	}
	
	public ODEProcess bindProcess(EABFlow flow){
		if(flow == null){
			return null;
		}	
		
		WSDLProtocolParser parser = new WSDLProtocolParser();
		parser.parse(flow.getInvokeService().getProtocol().getWsdlFilePath());
		
		return loadProcess("{" + parser.getServiceNamespace() + "}" + flow.getUniqueFlowName());
	}
}

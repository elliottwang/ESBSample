package eab.entity.ode;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import eab.dao.EABSessionFactory;

@SuppressWarnings("unchecked")
public class ODEInstanceDao {
	
	public ODEInstanceDao(){		
	}

	public ODEInstance loadInstance(long id){
		ODEInstance instance = null;
		Session s = null;
		Transaction tx = null;
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			instance = (ODEInstance) s.get(ODEInstance.class, id);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}		

		return instance;
	}	
	
	public int getInstanceCount(ODEProcess process, 
			short state, 
			Date createTimeMin, 
			Date creatTimeMax, 
			Date lastActiveTimeMin,
			Date lastActiveTimeMax) {
		
		Session s = null;
		Transaction tx = null;
		Iterator it = null;

		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();			

			Query query = getQueryString(s, 
					"select count(*) from ODEInstance as a", 
					process, 
					state, 
					createTimeMin, 
					creatTimeMax, 
					lastActiveTimeMin, 
					lastActiveTimeMax);
			
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
	
	public ODEInstance[] getInstances(int pages, 
			int maxPerPage, 
			ODEProcess process, 
			short state, 
			Date createTimeMin, 
			Date creatTimeMax, 
			Date lastActiveTimeMin,
			Date lastActiveTimeMax){		
		
		Session s = null;
		Transaction tx = null;
		List list = null;		

		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();						

			Query query = getQueryString(s, 
					"from ODEInstance as a", 
					process, 
					state, 
					createTimeMin, 
					creatTimeMax, 
					lastActiveTimeMin, 
					lastActiveTimeMax);

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
		
		ODEInstance[] instances = new ODEInstance[list.size()];
		Iterator it = list.iterator();
		int i = 0;
		while (it.hasNext()) {
			instances[i++] = (ODEInstance)it.next();
		}
		
		return instances;
	}
	
	public Query getQueryString(Session s,
			String normal,
			ODEProcess process, 
			short state, 
			Date createTimeMin, 
			Date creatTimeMax, 
			Date lastActiveTimeMin,
			Date lastActiveTimeMax){
		
		String queryString = normal;
		
		ArrayList<String> queryInfo = new ArrayList<String>();
		ArrayList<Object> queryObject = new ArrayList<Object>();
		
		if(process != null){
			queryInfo.add("a.process=:p" + String.valueOf(queryInfo.size() + 1));
			queryObject.add(process);
		}
		
		if(state >= 0){
			queryInfo.add("a.state=:p" + String.valueOf(queryInfo.size() + 1));
			queryObject.add(state);
		}
		
		if(createTimeMin != null){
			queryInfo.add("a.creatTime>=:p" + String.valueOf(queryInfo.size() + 1));
			queryObject.add(createTimeMin);
		}
		
		if(creatTimeMax != null){
			queryInfo.add("a.creatTime<=:p" + String.valueOf(queryInfo.size() + 1));
			queryObject.add(creatTimeMax);
		}
		
		if(lastActiveTimeMin != null){
			queryInfo.add("a.lastActiveTime>=:p" + String.valueOf(queryInfo.size() + 1));
			queryObject.add(lastActiveTimeMin);
		}
		
		if(lastActiveTimeMax != null){
			queryInfo.add("a.lastActiveTime<=:p" + String.valueOf(queryInfo.size() + 1));
			queryObject.add(lastActiveTimeMax);		
		}		
		
		if(queryInfo.size() > 0){
			queryString += " where ";
			for(int i = 0; i < queryInfo.size() - 1; i++){
				queryString += queryInfo.get(i) + " and ";
			}
			queryString += queryInfo.get(queryInfo.size() - 1);
		}
		
		Query query = s.createQuery(queryString);
		
		for(int i = 0; i < queryObject.size(); i++){
			if(queryObject.get(i) instanceof Short){
				query.setShort("p" + String.valueOf(i + 1), (Short)queryObject.get(i));
			}
			else if(queryObject.get(i) instanceof Date){
				query.setDate("p" + String.valueOf(i + 1), (Date)queryObject.get(i));
			}
			else{
				query.setEntity("p" + String.valueOf(i + 1), queryObject.get(i));
			}
		}
		
		return query;
	}
}

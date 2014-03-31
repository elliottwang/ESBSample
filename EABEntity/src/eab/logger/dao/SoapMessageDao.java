package eab.logger.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import eab.dao.EABSessionFactory;

@SuppressWarnings("unchecked")
public class SoapMessageDao {
	public static SoapMessageDao dao;
	
	static{
		dao = new SoapMessageDao();
	}
	
	public void saveMsg(SoapMessageLog msg){
		Session s = null;
		Transaction tx = null;
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			s.save(msg);
			tx.commit();
		} finally {
			if (s != null)
				s.close();
		}
	}
	
	
	public int getLogCount(String serviceType, Date startTime, Date endTime) {		
		Session s = null;
		Transaction tx = null;
		Iterator it = null;

		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();			

			Query query = getQueryString(s, "select count(*) from SoapMessageLog as a", serviceType, startTime, endTime);
			
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
	
	public SoapMessageLog[] getLogs(int pages, int maxPerPage, String serviceType, Date startTime, Date endTime){		
		
		Session s = null;
		Transaction tx = null;
		List list = null;		

		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();						

			Query query = getQueryString(s, "from SoapMessageLog as a", serviceType, startTime, endTime);

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
		
		SoapMessageLog[] logs = new SoapMessageLog[list.size()];
		Iterator it = list.iterator();
		int i = 0;
		while (it.hasNext()) {
			logs[i++] = (SoapMessageLog)it.next();
		}
		
		return logs;
	}
	
	public Query getQueryString(Session s, String normal, String serviceType, Date startTime, Date endTime){
		
		String queryString = normal;
		
		ArrayList<String> queryInfo = new ArrayList<String>();
		ArrayList<Object> queryObject = new ArrayList<Object>();
		
		if(serviceType != null && serviceType.length() > 0){
			queryInfo.add("a.type=:p" + String.valueOf(queryInfo.size() + 1));
			queryObject.add(serviceType);
		}
		
		if(startTime != null){
			queryInfo.add("a.invokeTime>=:p" + String.valueOf(queryInfo.size() + 1));
			queryObject.add(startTime);
		}
		
		if(endTime != null){
			queryInfo.add("a.invokeTime<=:p" + String.valueOf(queryInfo.size() + 1));
			queryObject.add(endTime);		
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
			if(queryObject.get(i) instanceof String){
				query.setString("p" + String.valueOf(i + 1), (String)queryObject.get(i));
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

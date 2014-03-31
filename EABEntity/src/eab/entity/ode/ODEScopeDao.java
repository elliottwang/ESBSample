package eab.entity.ode;

import org.hibernate.Session;
import org.hibernate.Transaction;

import eab.dao.EABSessionFactory;

public class ODEScopeDao {
	
	public ODEScopeDao(){		
	}

	public ODEScope loadScope(long id){
		ODEScope scope = null;
		Session s = null;
		Transaction tx = null;
		try {
			s = EABSessionFactory.getSession();
			tx = s.beginTransaction();
			scope = (ODEScope) s.get(ODEScope.class, id);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			if (s != null)
				s.close();
		}

		return scope;
	}	
}

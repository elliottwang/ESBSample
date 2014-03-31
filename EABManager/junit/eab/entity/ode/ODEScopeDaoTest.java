package eab.entity.ode;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ODEScopeDaoTest {

	private ODEScopeDao dao = new ODEScopeDao();
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadInstance() {
		ODEScope scope = dao.loadScope(313);
		assertEquals(5, scope.getSubScopes().size());
	}

}

package moontrack.analytics;

import java.util.Collection;
import java.util.HashSet;

import org.hibernate.Session;
import org.hibernate.Transaction;

import core.cluster.core.Cluster;
import core.hibernate.DbInfo;
import core.cluster.core.manager.HibernateService;
import core.cluster.services.config.ServiceEntry;
import core.hibernate.Hibernate;
import moontrack.config.MoontrackCache;
import moontrack.config.MoontrackEntityMapping;

public class MoontrackService extends HibernateService {
	
	private boolean isReadOnly = false;

	public MoontrackService() {
		super("analytics", new MoontrackEntityMapping(), new MoontrackCache("analytics"));
	}

	@Override
	public void innerInit(ServiceEntry entry) {
		super.innerInit(entry);
		
		Session session = Hibernate.getFactory(serviceName).getCurrentSession();
		Transaction transaction = session.beginTransaction();
		
		AppInstallsManager.initAppInstalls(session);
		
		transaction.commit();
	}

	@Override
	public DbInfo getDefaultDbInfo() {
		DbInfo dbInfo = new DbInfo(serviceName, serviceName);
		dbInfo.setReadOnly(isReadOnly);
		return dbInfo;
	}

	@Override
	public Collection<Class<? extends Cluster>> getClustersInUse() {
		return new HashSet<>();
	}

	@Override
	public String[] getGroupTypes() {
		return new String[]{"analytics"};
	}

	public void setReadOnly(boolean value) {
		isReadOnly = value;
		
	}
}

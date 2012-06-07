package dk.nsi.minlog.server.dao.ebean;

import javax.inject.Inject;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;

public abstract class SupportDao<T> {
    @Inject
    EbeanServer ebeanServer;

    protected final Class<T> klass;

    protected SupportDao(Class<T> klass) {
        this.klass = klass;
    }

    protected Query<T> query() {
        return ebeanServer.find(klass);
    }
}

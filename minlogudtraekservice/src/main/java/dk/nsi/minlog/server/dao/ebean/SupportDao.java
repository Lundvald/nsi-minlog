package dk.nsi.minlog.server.dao.ebean;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.ExpressionFactory;
import com.avaje.ebean.Query;

import javax.inject.Inject;
import java.util.List;

public abstract class SupportDao<T> {
    @Inject
    EbeanServer ebeanServer;

    protected final Class<T> klass;

    protected SupportDao(Class<T> klass) {
        this.klass = klass;
    }

    public T get(long id) {
        return ebeanServer.find(klass, id);
    }

    public List<T> list() {
        return ebeanServer.findList(query(), null);
    }

    public void save(T entity) {
        ebeanServer.save(entity);
    }

    public T findByKode(String kode) {
        return query().where().eq("kode", kode).findUnique();
    }

    protected Query<T> query() {
        return ebeanServer.find(klass);
    }

    protected ExpressionFactory expr() {
        return ebeanServer.getExpressionFactory();
    }
}

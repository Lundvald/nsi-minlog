package dk.nsi.minlog.server.dao;

import java.util.Date;
import java.util.List;

import dk.nsi.minlog.domain.Registrering;

public interface RegistreringDao {
	List<Registrering> findLogByCPR(String cpr);
	List<Registrering> findLogByCPRAndRange(String cpr, Date from, Date to);
}
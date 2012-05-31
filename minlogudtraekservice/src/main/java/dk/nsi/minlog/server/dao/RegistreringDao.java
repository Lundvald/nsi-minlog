package dk.nsi.minlog.server.dao;

import java.util.List;

import org.joda.time.DateTime;

import dk.nsi.minlog.domain.Registrering;

public interface RegistreringDao {
	List<Registrering> findRegistreringByCPRAndDates(String cpr, DateTime from, DateTime to);
	void removeRegistreringBefore(DateTime date);
}
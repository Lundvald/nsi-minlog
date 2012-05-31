package dk.nsi.minlog.web;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dk.nsi.minlog.server.dao.RegistreringDao;

@Repository
public class MinLogCleanupJob {	
	private static Logger logger = Logger.getLogger(MinLogCleanupJob.class);

	@Inject
	private RegistreringDao registreringDao;
	
	private boolean running;
		
	@Scheduled(cron = "${minlogCleanup.cron}")
	@Transactional
	public void cleanup(){
		if(!running){
			running = true;
			try{
				DateTime date = DateTime.now().minusYears(2);	
				logger.info("Running cleanup job for entries before " + date);
				registreringDao.removeRegistreringBefore(date);
			} catch(Exception e){
				logger.warn("Failed to execute cleanup job", e);
			}
			running = false;
		}
	}
	
	public boolean isRunning(){
		return running;
	}
}
use minlog;
ALTER TABLE `logentry` DISABLE KEYS;
SET FOREIGN_KEY_CHECKS = 0;
SET UNIQUE_CHECKS = 0;
SET sql_log_bin = 0; 

load data infile '/Users/kpi/Documents/java/nsi-minlog/performence/data/logentry-small.csv' into table logentry fields terminated by ',' enclosed by '"' lines terminated by '\n' (regKode, cprNrBorger, bruger, ansvarlig, orgUsingId, systemName, handling, sessionId, tidspunkt);

SET sql_log_bin = 0;
SET UNIQUE_CHECKS = 1;
SET FOREIGN_KEY_CHECKS = 1;
ALTER TABLE `logentry` ENABLE KEYS;
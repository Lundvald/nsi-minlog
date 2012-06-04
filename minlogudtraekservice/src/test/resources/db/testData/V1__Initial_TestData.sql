USE minlog;
INSERT INTO `Registrering` (`cpr`, `tidspunkt`, `bruger`, `paavegneaf`, `organisation`, `system`, `handling`, `session`) VALUES ('0510171632', NOW(), 'abc', 'def', 'trifork', 'someSystem', 'someHandling', 'session1');

INSERT INTO `whitelist` (`name`, `legal_cvr`) VALUES ('test', '1');
INSERT INTO `whitelist` (`name`, `legal_cvr`) VALUES ('test', '2');
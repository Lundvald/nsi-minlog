USE minlog;
INSERT INTO `LogEntry` (`regKode`, `cprNrBorger`, `bruger`, `ansvarlig`, `orgUsingID`, `systemName`, `handling`, `sessionId`, `tidspunkt`) VALUES ('1234', '0510171632', 'abc', 'def', 'trifork', 'someSystem', 'someHandling', 'session1', NOW());

INSERT INTO `whitelist` (`name`, `legal_cvr`) VALUES ('test', '1');
INSERT INTO `whitelist` (`name`, `legal_cvr`) VALUES ('test', '2');
USE minlog;
INSERT INTO `LogEntry` (`regKode`, `cprNrBorger`, `bruger`, `ansvarlig`, `orgUsingID`, `systemName`, `handling`, `sessionId`, `tidspunkt`) VALUES ('1234', '1111111999', 'bruger1', 'ansvarlig1', 'organisation1', 'system1', 'handling1', 'session1', '2012-01-02 08:00:00');
INSERT INTO `LogEntry` (`regKode`, `cprNrBorger`, `bruger`, `ansvarlig`, `orgUsingID`, `systemName`, `handling`, `sessionId`, `tidspunkt`) VALUES ('1234', '1111111999', 'bruger2', 'ansvarlig2', 'organisation2', 'system2', 'handling2', 'session2', '2012-02-01 09:00:00');
INSERT INTO `LogEntry` (`regKode`, `cprNrBorger`, `bruger`, `ansvarlig`, `orgUsingID`, `systemName`, `handling`, `sessionId`, `tidspunkt`) VALUES ('1234', '1111111999', 'bruger3', 'ansvarlig3', 'organisation3', 'system3', 'handling3', 'session3', '2011-01-02 10:00:00');
INSERT INTO `LogEntry` (`regKode`, `cprNrBorger`, `bruger`, `ansvarlig`, `orgUsingID`, `systemName`, `handling`, `sessionId`, `tidspunkt`) VALUES ('1234', '1111111888', 'bruger4', 'ansvarlig4', 'organisation4', 'system4', 'handling4', 'session4', '2010-01-02 11:00:00');

INSERT INTO `whitelist` (`name`, `legal_cvr`) VALUES ('test', '1');
INSERT INTO `whitelist` (`name`, `legal_cvr`) VALUES ('test', '2');
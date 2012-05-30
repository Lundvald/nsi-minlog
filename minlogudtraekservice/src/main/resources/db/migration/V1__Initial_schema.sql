USE minlog;

CREATE TABLE MinLog (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `cpr` varchar(10) not null,
  `tidspunkt` datetime not null,
  `bruger` varchar(20),
  `paavegneaf` varchar(20),
  `organisation` varchar(25),
  `system` varchar(25),
  `handling` varchar(75),
  `session` varchar(20),

  PRIMARY KEY (`id`)
);

CREATE INDEX log_cpr_and_timestamp_index ON MinLog (`cpr`, `tidspunkt`) USING BTREE;
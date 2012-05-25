USE minlog;

CREATE TABLE Log (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `cpr` int not null,
  `timestamp` bigint not null,
  `bruger` varchar(20),
  `paavegneaf` varchar(20),
  `organisation` varchar(25),
  `system` varchar(25),
  `handling` varchar(75),
  `session` varchar(20),

  PRIMARY KEY (`id`)
);

CREATE INDEX log_cpr_and_timestamp_index ON Log (`cpr`, `timestamp`) USING BTREE;

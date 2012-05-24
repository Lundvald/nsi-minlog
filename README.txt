Minlog
===

TBD

Running with MySQL
===

```
CREATE USER 'bemyndigelse'@'localhost' IDENTIFIED BY '';
CREATE DATABASE bemyndigelse;
GRANT ALL PRIVILEGES ON bemyndigelse.* TO 'bemyndigelse'@'localhost';

CREATE DATABASE bemyndigelsetest;
GRANT ALL PRIVILEGES ON bemyndigelsetest.* TO 'bemyndigelse'@'localhost';
```
Minlog
===

TBD

Running with MySQL
===

```
CREATE USER 'minlog'@'localhost' IDENTIFIED BY '';
CREATE DATABASE minlog;
GRANT ALL PRIVILEGES ON minlog.* TO 'minlog'@'localhost';

CREATE DATABASE minlogtest;
GRANT ALL PRIVILEGES ON minlogtest.* TO 'minlog'@'localhost';
```
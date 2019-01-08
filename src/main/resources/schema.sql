SET AUTOCOMMIT = 0;
START TRANSACTION;
CREATE TABLE IF NOT EXISTS TBL_SAF
(
SAF_ID INT(11) AUTO_INCREMENT PRIMARY KEY,
FROM_SYSTEM VARCHAR(100),
TO_SYSTEM VARCHAR(100),
REQUEST TEXT,
RESPONSE TEXT,
FAILURES TEXT,
SERVICE_NAME VARCHAR(100),
RETRY_COUNT INT(11),
STATUS VARCHAR(100),
INSERTED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMIT;
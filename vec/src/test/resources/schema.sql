CREATE TABLE IF NOT EXISTS m_user (
   SERI_NO INT,
    USER_ID VARCHAR,
    USER_PASS VARCHAR
);
DELETE FROM m_user;

 INSERT INTO m_user
 SELECT * FROM CSVREAD('classpath:csv/M_USER.csv',null,'charset=UTF-8');
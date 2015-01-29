drop database if exists lipukadroid;
create database lipukadroid;

use lipukadroid;

drop table if exists DROIDREQUESTS;
create table DROIDREQUESTS (
REQUEST_ID int not null auto_increment,
MSISDN varchar(15) not null,
SERVICE_ID int not null,
PAYLOAD text not null,
primary key(REQUEST_ID))
;
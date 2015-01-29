drop table if exists SERVICE_XML_FILES;
create table SERVICE_XML_FILES(
ID int not null auto_increment,
NAME varchar(30) not null,
VERSION int not null,
primary key(ID))
;

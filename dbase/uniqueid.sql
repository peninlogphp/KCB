use lipukadroid;

drop table if exists uniqueid;
create table uniqueid (
REQUEST_ID int not null auto_increment,
primary key(REQUEST_ID))
;
create table MEMBER_TABLE(
	id bigint generated by default as identity,
	name varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	primary key(id)
);

create table BOARD_TABLE(
	id bigint generated by default as identity,
	title varchar(31) NOT NULL,
	contents varchar(999999) NOT NULL,
	member_id bigint,
	primary key(id),
	foreign key(member_id) references MEMBER_TABLE(id) on delete cascade
)
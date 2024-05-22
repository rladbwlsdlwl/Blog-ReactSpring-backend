create table USER_TABLE(
	id bigint generated by default as identity,
	username varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	primary key(id)
);

create table BOARD_TABLE(
	id bigint generated by default as identity,
	title varchar(31) NOT NULL,
	contents varchar(999999) NOT NULL,
	user_id bigint,
	primary key(id),
	foreign key(user_id) references USER_TABLE(id) on delete cascade
)
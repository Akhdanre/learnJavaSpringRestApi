create table contact(
 id varchar(100) not null, 
 username varchar(100) not null, 
 first_name varchar(100) not null, 
 last_name varchar(100) not null,
 phone varchar(13), 
 email varchar(100),
 primary key (id),
 foreign key fk_user_contact (username) references users(username)
)
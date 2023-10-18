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


create table addresses(
    id varchar(100) not null,
    contact_id varchar(100) not null, 
    street varchar(200),
    city varchar(100),
    province varchar(100),
    country varchar(100),
    postal_code varchar(10),
    primary key (id)
)
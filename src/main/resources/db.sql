create table customer(
	id bigserial not null primary key,
	name varchar(100) not null,
	idade int not null
);

create table posts(
	id bigserial not null primary key,
	post_text varchar(150) not null,
	datatime timestamp not null,
	customer_id bigint not null references public.customer(id)
);

create table followers(
    id bigserial not null primary key,
    user_id bigint not null references customer(id),
    follower_id bigserial not null references customer(id)
);
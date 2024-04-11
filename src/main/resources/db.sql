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
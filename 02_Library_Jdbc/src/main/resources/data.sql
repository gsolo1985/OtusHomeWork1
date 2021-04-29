insert into author (name) values ('Fedor Dostaevskiy');
insert into author (name) values ('Lev Tolstoy');
insert into author (name) values ('Anton Chekhov');

insert into genre (name) values ('Drama');
insert into genre (name) values ('Play');
insert into genre (name) values ('Novella');

insert into book (title, authorid, genreid)
select 'War and Peace' as title,
       (select id from author where name = 'Lev Tolstoy') as authorid,
       (select id from genre where name = 'Drama') as genreid;

insert into book (title, authorid, genreid)
select 'Crime and Punishment' as title,
       (select id from author where name = 'Fedor Dostaevskiy') as authorid,
       (select id from genre where name = 'Drama') as genreid;

insert into book (title, authorid, genreid)
select 'Uncle Vanya' as title,
       (select id from author where name = 'Anton Chekhov') as authorid,
       (select id from genre where name = 'Play') as genreid;

insert into book (title, authorid, genreid)
select 'Youth' as title,
       (select id from author where name = 'Lev Tolstoy') as authorid,
       (select id from genre where name = 'Novella') as genreid;

delete from user_roles;
delete from messages;
delete from users;
delete from roles;

insert into users(id, username, password) values (1, 'Sava', '$2a$10$tM7OTQCgmcgWZO.tlazbmugeg6Jl3m6IsFB5rSpl6PmwnPLbyQ2T6');
insert into roles(id, name) values (1, 'user');
insert into user_roles(user_id, role_id) values (1, 1);
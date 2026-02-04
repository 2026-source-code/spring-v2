insert into user_tb(username, password, email, created_at) values('ssar', '1234', 'ssar@nate.com', now());
insert into user_tb(username, password, email, created_at) values('cos', '1234', 'cos@nate.com', now());

insert into board_tb (user_id, title, content, created_at) values (1,'title1', 'content1', now()); 
insert into board_tb (user_id, title, content, created_at) values (1,'title2', 'content2', now()); 
insert into board_tb (user_id, title, content, created_at) values (1,'title3', 'content3', now()); 
insert into board_tb (user_id, title, content, created_at) values (2,'title4', 'content4', now()); 
insert into board_tb (user_id, title, content, created_at) values (2,'title5', 'content5', now()); 
insert into board_tb (user_id, title, content, created_at) values (2,'title6', 'content6', now());
 
insert into reply_tb (user_id, board_id, comment, created_at) values(1, 6, 'comment1', now());
insert into reply_tb (user_id, board_id, comment, created_at) values(1, 6, 'comment2', now());
insert into reply_tb (user_id, board_id, comment, created_at) values(2, 6, 'comment3', now());
insert into reply_tb (user_id, board_id, comment, created_at) values(1, 5, 'comment4', now());
insert into reply_tb (user_id, board_id, comment, created_at) values(2, 5, 'comment5', now());



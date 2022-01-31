--1. book board 생성
create table board(
    no number,
    title varchar2(500) not null,
    content varchar2(4000),
    hit number,
    reg_date date not null,
    user_no number not null,
    primary key(no),
    constraint board_fk foreign key(user_no) 
    references users(no) 
);

--2.board의 sequence 생성
create sequence seq_board_no 
increment by 1 
start with 1  
nocache;

--3. insert문
insert into board
values(seq_board_no.nextval,'배를 밀며','사랑은 참 부드럽게도 떠나지 뵈지도 않는 길을 부드럽게도',
0,sysdate,1);

insert into board
values(seq_board_no.nextval,'팬텀블루미스트','사랑은 계속될 거야, 언제까지나!',
0,sysdate,4);


--4. table join
select  u.no,
        u.id,
        u.password,
        u.name,
        u.gender,
        bd.no,
        bd.title,
        bd.content,
        bd.hit,
        to_char(reg_date, 'yyyy-mm-dd hh:mi:ss') regDate
from board bd, users u
where bd.user_no = u.no
order by regDate desc;

--5. select
select  no,
        title,
        content,
        hit,
        to_char(reg_date, 'yyyy-mm-dd hh:mi:ss') reg_date,
        user_no
from board;

select  bd.no,
        bd.title,
        bd.content,
        bd.hit,
        to_char(reg_date, 'yyyy-mm-dd hh:mi:ss') regDate,
        u.name,
        u.no uno
from board bd, users u
where bd.user_no = u.no
and bd.no = 2;

--6. 조회수 카운트
update  board 
set     hit = hit + 1
where no = 1;

--7. commit
commit;

--8. 테이블, 시퀀스 삭제
drop table board;
drop sequence seq_board_no;





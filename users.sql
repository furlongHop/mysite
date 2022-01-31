--1. table users 생성
create table users(
    no number,
    id varchar2(20) unique not null,
    password varchar2(20) not null,
    name varchar2(20),
    gender varchar(10),
    primary key(no)
);

--2.board의 sequence 생성
create sequence seq_user_no 
increment by 1 
start with 1  
nocache;

--3. insert문
insert into users
values(seq_user_no.nextval,'choihan','1108','최한','male');

insert into users
values(seq_user_no.nextval,'hong','1104','홍','male');

--4. select문
select  no,
        id,
        password,
        name,
        gender
from users;

--5. commit
commit;

--6. rollback
rollback;

--7. 테이블, 시퀀스 삭제
drop table users;
drop sequence seq_user_no;

--8. 행 삭제
delete from users
where no = 4;

update  users
set id = 'happy_merry'
where no = 4;
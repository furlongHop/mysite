--1. guestbook table 생성
create table guestbook(
    no number,
    name varchar2(80),
    password varchar2(20),
    content varchar2(2000),
    reg_date date,
    primary key(no)
);

--2. 시퀀스 생성
create sequence seq_guestbook_no
increment by 1 
start with 1  
nocache;

--3. insert문
insert into guestbook
values(seq_guestbook_no.nextval,'최한','1108','어디든 가겠습니다.',sysdate);

insert into guestbook
values(seq_guestbook_no.nextval,'케일','1108','우리집으로 가자.',sysdate);

--4. select문
select * from guestbook;


select  no,
        name,
        password,
        content,
        to_char(reg_date, 'yyyy-mm-dd hh:mi:ss') reg_date
from guestbook
order by reg_date desc;

--5. commit
commit;

--6. rollback
rollback;

--7. 테이블, 시퀀스 삭제
drop table guestbook;
drop sequence seq_guestbook_no;

--8. 행 삭제
delete from guestbook
where no = 14;
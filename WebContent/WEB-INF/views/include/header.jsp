<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>   
 <%@ page import="com.javaex.vo.UserVo" %>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

		<div id="header" class="clearfix">
		<h1>
			<a href="/mysite/main">MySite</a>
		</h1>

		<c:choose>
			<c:when test="${empty authUser}">
			<!-- 로그인 실패, 로그인 전 -->
			<ul>
				<li><a href="/mysite/user?action=loginForm" class="btn_s">로그인</a></li>
				<li><a href="/mysite/user?action=joinForm" class="btn_s">회원가입</a></li>
			</ul>
			</c:when>
			
			<c:when test="${authUser != null}">
			<!-- 로그인 성공 -->
			<ul>
				<!--↓ UserVo authUser = (UserVo)session.getAttribute("authUser"); 사용시 -->
				<!--authUser.getName()> -->
				<li>${sessionScope.authUser.name} 님 안녕하세요^^</li><!-- or {authUser.name} -->
				<li><a href="/mysite/user?action=logout" class="btn_s">로그아웃</a></li>
				<li><a href="/mysite/user?action=modifyForm" class="btn_s">회원정보수정</a></li>
			</ul>
			</c:when>
		</c:choose>

		</div>
		<!-- //header -->

		<div id="nav">
			<ul class="clearfix">
				<li><a href="">입사지원서</a></li>
				<li><a href="/mysite/board?action=list">게시판</a></li>
				<li><a href="">갤러리</a></li>
				<li><a href="/mysite/guest?action=addList">방명록</a></li>
			</ul>
		</div>
		<!-- //nav -->

</body>
</html>
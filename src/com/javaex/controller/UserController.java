package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;


@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("user");
		
		String act = request.getParameter("action");
		
		if("joinForm".equals(act)) {
			System.out.println("joinForm");

			//포워드(WebContent 안쪽에서부터 시작)
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");
			
		}else if("join".equals(act)) {
			System.out.println("join");
			
			//request로 파라이터값 받기
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			//파라미터를 모아 vo로 묶기
			UserVo userVo = new UserVo(id,password,name,gender);//해당 생성자 여부 늘 확인하기
		
			//vo를 insert(회원가입)
			UserDao userDao = new UserDao();
			userDao.insert(userVo);
			
			System.out.println(userVo);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinOk.jsp");
		}else if("loginForm".equals(act)) {
			System.out.println("loginForm");
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");
		}else if("login".equals(act)) {
			System.out.println("login");
			
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			
			UserDao userDao = new UserDao();
			UserVo authVo = userDao.getUSer(id,password);//인증된 사용자
			//System.out.println(authVo);
			
			if(authVo == null) {//로그인 실패
				System.out.println("로그인 실패");
				
				WebUtil.redirect(request, response, "/mysite/user?action=loginForm&result=fail");//"url"
			}else {//로그인 성공
				System.out.println("로그인 성공");
				
				//session에 있는 id는 브라우저 단위(같은 기기라도 크롬 접속과 익스 접속 때의 세션 아이디가 다르다)
				//request header에 서버가 뽑아준 sessionId를 어트리뷰트(저장공간)에 담아 주고받는다.(첫 접속시 id 부재>서버가 부여)
				//sessionId는 서버가 처음 부여해줄 때 (어트리뷰트에 담긴 채) session 공간(객체)을 만들어 그곳에 넣어준다.
				HttpSession session = request.getSession();//requset header에 았는 session을 읽어와 HttpSession session에 담는다.
				session.setAttribute("authUser", authVo);//authVo의 주소를 session에 았는 어트리뷰트에 담아준다.
				
				WebUtil.redirect(request, response, "/mysite/main");
			}
		
		}else if("logout".equals(act)) {
			System.out.println("logout");
			
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");//authUser라는 이름의 객체 주소를 세션에서 제거
			session.invalidate();//해당 세션 객체 삭제
			
			WebUtil.redirect(request, response, "/mysite/main");
		}else if("modify".equals(act)) {
			System.out.println("modify");
			
			//수정폼에 적은 파라미터 값을 불러온다  
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			//불러온 값을 userVo에 담고 update 메소드로 회원정보를 수정한다
			UserVo userVo = new UserVo(id, name, password, gender);
			UserDao userDao = new UserDao();
			userDao.update(userVo);
			
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");//기존 객체 주소 삭제
			session.setAttribute("authUser", userVo);//회원 정보를 수정한 새로운 객체 주소 입력
			
			WebUtil.redirect(request, response, "/mysite/main");
			
		}else if("modifyForm".equals(act)) {
			System.out.println("modifyForm");
			
			//session 객체 정보를 request로 받아 HttpSession 형태로 저장
			HttpSession session = request.getSession();
			//세션 어트리뷰트에 저장된 객체 주소(현재 로그인이 성공된 회원정보)를 authUser에 담는다
			UserVo authUser = (UserVo)session.getAttribute("authUSer");
			
			UserDao userDao = new UserDao();
			//id와 pw로 회원의 no, name 정보를 가져오는 getUser 메소드 호출, 불러온 회원 정보를 userVo에 담는다
			UserVo userVo = userDao.getUSer(authUser.getId(), authUser.getPassword());
			
			//가져온 회원정보(userVo의 주소)를 authUser라는 이름으로 어트리뷰트에 저장한다>수정폼 value값 제공
			request.setAttribute("authUser", userVo);
			WebUtil.forward(request, response, "/WEB-INF/views/user/modifyForm.jsp");
		}
	
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}

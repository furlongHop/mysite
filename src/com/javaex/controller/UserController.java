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
		
		request.setCharacterEncoding("UTF-8");//post 방식으로 데이터를 가져올 경우 한글이 깨지는 현상 방지
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
			System.out.println(authVo);
		
		}else if("logout".equals(act)) {
			System.out.println("logout");
			
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");//authUser라는 이름의 객체 주소를 세션에서 제거
			session.invalidate();//해당 세션 객체 삭제
			
			WebUtil.redirect(request, response, "/mysite/main");
		}else if("modify".equals(act)) {
			System.out.println("modify");
			
			//수정폼에 적은 파라미터 값을 불러온다  
			int no= Integer.parseInt(request.getParameter("no"));
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			//db 정보 수정-불러온 값을 userVo에 담고 update 메소드로 회원정보를 수정한다
			UserVo userVo = new UserVo(no, password, name, gender);
			UserDao userDao = new UserDao();
			userDao.update(userVo);
			
			//세션 정보 수정
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");//기존 객체 주소 삭제
			session.setAttribute("authUser", userVo);//회원 정보를 수정한 새로운 객체 주소 입력
			
			WebUtil.redirect(request, response, "/mysite/main");
			
		}else if("modifyForm".equals(act)) {
			System.out.println("modifyForm");
			
			WebUtil.forward(request, response, "/WEB-INF/views/user/modifyForm.jsp");
		}
	
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}

package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestbookDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestbookVo;


@WebServlet("/guest")
public class GuestbookController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.println("guest");
		
		String act = request.getParameter("action");
		
		if("add".equals(act)) {
			System.out.println("add");
			
			String name = request.getParameter("name");
			String password = request.getParameter("pass");
			String content = request.getParameter("content");
			
			GuestbookVo guestbookVo = new GuestbookVo(name,password,content);
			GuestbookDao gbDao = new GuestbookDao();
			gbDao.guestInsert(guestbookVo);
			
			//WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");
			WebUtil.redirect(request, response, "/mysite/guest?action=addList");
			
		}else if("addList".equals(act)){
			System.out.println("addList");
			

			GuestbookDao gbDao = new GuestbookDao();
			List<GuestbookVo> guestbookbList = gbDao.getList();
			
			//guestbookList를 setAttribute 메소드로 gbList라는 이름으로 request에 저장한다. 
			request.setAttribute("guestList", guestbookbList);//jstl 문법에서 사용<c:forEach>
			
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");
			
			
		}else if("delete".equals(act)){
			System.out.println("delete");
			
			int no = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("pass");

			GuestbookVo vo = new GuestbookVo();
			vo.setNo(no);
			vo.setPassword(password);

			GuestbookDao dao = new GuestbookDao();
			dao.guestDelete(no);
			
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");
			
		}else if("deleteForm".equals(act)) {
			System.out.println("deleteForm");
			
			
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}

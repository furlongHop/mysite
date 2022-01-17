package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;


@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.println("board");
		
		String act = request.getParameter("action");
		
		if("list".equals(act)) {
			System.out.println("list");
			
			BoardDao boardDao = new BoardDao();
		 	List<BoardVo> bList = boardDao.getList();
			
			request.setAttribute("boardList", bList);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
		}else if("read".equals(act)) {
			System.out.println("read");
			int no= Integer.parseInt(request.getParameter("no"));
			
			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = boardDao.getBoard(no);
			
			request.setAttribute("board", boardVo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
			
		}else if("write".equals(act)) {
			System.out.println("write");
			
		}else if("writeForm".equals(act)) {
			System.out.println("writeForm");
			
			WebUtil.forward(request, response,  "/WEB-INF/views/board/writeForm.jsp");
			
		}else if("modify".equals(act)) {
			System.out.println("modify");
			
		}else if("modifyForm".equals(act)) {
			System.out.println("modifyForm");
			
		}else if("delete".equals(act)) {
			System.out.println("delete");
			
			WebUtil.redirect(request, response, "mysite/board");
		}
		
		
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}

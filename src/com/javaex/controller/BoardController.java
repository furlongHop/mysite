package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("board");

		String act = request.getParameter("action");

		if ("list".equals(act)) {
			System.out.println("list");

			BoardDao boardDao = new BoardDao();
			List<BoardVo> bList = boardDao.getList();

			request.setAttribute("boardList", bList);

			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");

		} else if ("read".equals(act)) {
			System.out.println("read");
			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = boardDao.getBoard(no);
			
			//조회수 증가
			boardDao.read(no);

			request.setAttribute("getBoard", boardVo);

			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");

		} else if ("write".equals(act)) {
			System.out.println("write");
			
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int uno = Integer.parseInt(request.getParameter("userNo"));
			
			BoardDao boardDao = new BoardDao();
			boardDao.write(title, content, uno); 
			
			WebUtil.redirect(request, response, "/mysite/board?action=list");

		} else if ("writeForm".equals(act)) {
			System.out.println("writeForm");

			HttpSession session = request.getSession();//생성된 세션 객체 데이터 가져오기
			UserVo authUser = (UserVo)session.getAttribute("authUser");//로그인 유저 정보
			
			if(authUser !=null) {
				System.out.println("로그인 했을 때");
				
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
			}else {
				System.out.println("로그인 안 했을 때");
				
				WebUtil.redirect(request, response, "/mysite/board?action=list");
			}
			
			

		} else if ("modify".equals(act)) {
			System.out.println("modify");
			
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int bno = Integer.parseInt(request.getParameter("no"));
			
			BoardVo boardVo = new BoardVo();
			boardVo.setTitle(title);
			boardVo.setContent(content);
			boardVo.setNo(bno);
			
			BoardDao boardDao = new BoardDao();
			boardDao.modify(boardVo);

			WebUtil.redirect(request, response, "/mysite/board?action=list");

		} else if ("modifyForm".equals(act)) {
			System.out.println("modifyForm");
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = boardDao.getBoard(no);
			
			request.setAttribute("getBoard", boardVo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");

		} else if ("delete".equals(act)) {
			System.out.println("delete");

			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao boardDao = new BoardDao();
			boardDao.delete(no);

			WebUtil.redirect(request, response, "/mysite/board?action=list");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}

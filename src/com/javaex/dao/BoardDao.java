package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {

	// 필드
	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

	// 메소드 g/s

	// 메소드 일반
	// 메소드 연결하기
	public void getConnection() {

		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 닫기
	public void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 게시판 리스트 불러오기
	public List<BoardVo> getList() {

		List<BoardVo> boardList = new ArrayList<BoardVo>();

		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			// 문자열
			String query = "";
			query += " select   bd.no ";
			query += "    	   ,bd.title ";
			query += "  	   ,bd.content	";
			query += "    	   ,bd.hit ";
			query += " 		   ,to_char(reg_date, 'yyyy-mm-dd hh:mi:ss') regDate ";
			query += "    	   ,u.name ";
			query += "         ,u.no uno ";
			query += " from board bd, users u ";
			query += " where bd.user_no = u.no ";
			query += " order by regDate desc ";// String query문 안에는 끝날 때 ; 표기하지 말 것. 주의!

			// 문자열>쿼리
			pstmt = conn.prepareStatement(query);

			// 바인딩 x

			// 실행
			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int bno = rs.getInt("no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("regDate");
				String name = rs.getString("name");
				int uno = rs.getInt("uno");

				BoardVo boardVo = new BoardVo(bno, title, content, hit, regDate, name, uno);
				boardList.add(boardVo);

			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return boardList;
	}

	// 조회수 증가
	public int read(int bno) {

		int count = 0;
		getConnection();

		try {
			String query = "";
			query += " update   board ";
			query += " set      hit= hit+1 ";
			query += " where    no= ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, bno);

			count = pstmt.executeUpdate();

			System.out.println(bno + "번 게시글 조회수가 " + count + "증가했습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}

	// 특정 게시글 선택
	public BoardVo getBoard(int bno) {

		BoardVo boardVo = null;
		getConnection();

		try {
			String query = "";
			query += " select   bd.no ";
			query += "    	   ,bd.title ";
			query += "  	   ,bd.content	";
			query += "    	   ,bd.hit ";
			query += " 		   ,to_char(reg_date, 'yyyy-mm-dd hh:mi:ss') regDate ";
			query += "    	   ,u.name ";
			query += "         ,u.no uno ";
			query += " from board bd, users u ";
			query += " where bd.user_no = u.no ";
			query += " and		bd.no= ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, bno);

			System.out.println(bno);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("regDate");
				String name = rs.getString("name");
				int uno = rs.getInt("uno");

				boardVo = new BoardVo(no, title, content, hit, regDate, name, uno);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		close();

		return boardVo;
	}

	// 게시글 삭제
	public int delete(int bno) {

		int count = 0;
		getConnection();

		try {
			String query = "";
			query += " delete from board ";
			query += " where	   no= ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, bno);

			count = pstmt.executeUpdate();

			System.out.println(count + "건 삭제되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}

	// 게시글 작성
	public int write(String title, String content, int userNo) {

		int count = 0;
		getConnection();

		try {
			String query = "";
			query += " insert into board ";
			query += " values(seq_board_no.nextval, ?, ?, 0, sysdate, ?) ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, title); 
			pstmt.setString(2, content); 
			pstmt.setInt(3, userNo);

			count = pstmt.executeUpdate();

			System.out.println(count + "건 등록되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}

}

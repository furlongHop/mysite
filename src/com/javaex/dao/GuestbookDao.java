package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestbookVo;

public class GuestbookDao {

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
	
	
	// guestbookList 가져오기
		public List<GuestbookVo> getList() {

			List<GuestbookVo> guestbookList = new ArrayList<GuestbookVo>();

			getConnection();

			try {
				// 3. SQL문 준비 / 바인딩 / 실행
				// 문자열
				String query = "";
				query += " select  no ";
				query += "    	   ,name ";
				query += " 		   ,password ";
				query += "  	   ,content	";
				query += " 		   ,to_char(reg_date, 'yyyy-mm-dd hh:mi:ss') reg_date ";
				query += " from guestbook ";
				query += " order by reg_date desc ";// String query문 안에는 끝날 때 ; 표기하지 말 것. 주의!

				// 쿼리
				pstmt = conn.prepareStatement(query);

				// 바인딩 x

				// 실행
				rs = pstmt.executeQuery();

				// 4.결과처리
				while (rs.next()) {
					int no = rs.getInt("no");
					String name = rs.getString("name");
					String password = rs.getString("password");
					String content = rs.getString("content");
					String regDate = rs.getString("reg_date");

					GuestbookVo guestbookVo = new GuestbookVo(no, name, password, content, regDate);
					guestbookList.add(guestbookVo);
				}

			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

			close();
			return guestbookList;
		}// getGuestbookList
	
	//게시글 추가
	public int guestInsert(GuestbookVo guestbookVo) {

		int count = 0;
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			// 문자열 만들기
			String query = "";
			query += " insert into guestbook ";
			query += " values(seq_guestbook_no.nextval,?,?, ?,sysdate) ";

			// 쿼리로 만들기
			pstmt = conn.prepareStatement(query);

			// ? 안에 값 받아 넣기
			pstmt.setString(1, guestbookVo.getName());
			pstmt.setString(2, guestbookVo.getPassword());
			pstmt.setString(3, guestbookVo.getContent());

			// 쿼리문 실행
			count = pstmt.executeUpdate();

			// 4.결과처리

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}

	//게시글 삭제
	//비밀번호 일치 여부를 delete.jsp에서 확인하는 삭제 메소드
		public int guestDelete(int no) {
			int count = 0;
			getConnection();

			try {
				// 3. SQL문 준비 / 바인딩 / 실행
				// 문자열 만들기
				String query = "";
				query += " delete from guestbook ";
				query += " where no = ? ";

				// 쿼리로 만들기
				pstmt = conn.prepareStatement(query);

				// ? 안에 값 받아 넣기
				pstmt.setInt(1, no);

				// 쿼리문 실행
				count = pstmt.executeUpdate();

				// 4.결과처리

			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
			close();
			return count;
		}

}

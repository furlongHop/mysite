package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {

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

	// 회원정보 1명 가져오기(로그인용)
	public UserVo getUser(String uid, String pw) {
		
		UserVo userVo = null;
		
		getConnection();
		
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			// 문자열
			String query = "";
			query += " select   no, "; 
			query += "          id, ";
			query += "          password, ";
			query += "          name, ";
			query += "          gender ";
			query += " from 	users ";
			query += " where 	id = ? ";
			query += " and 		password = ? ";
			
			//쿼리문
			
			pstmt = conn.prepareStatement(query);
			
			//바인딩
			pstmt.setString(1, uid);
			pstmt.setString(2, pw);
			
			//실행-db에서 데이터를 가져오는 과정이므로 excuteQuery
			rs = pstmt.executeQuery();
			
			//4. 결과 처리
			while(rs.next()) {
				
				int no = rs.getInt("no");
				String id= rs.getString("id");
				String password= rs.getString("password");
				String name= rs.getString("name");
				String gender= rs.getString("gender");

				
				//개수와 종류에 맞는 생성자를 만들거나 setter로 값을 넣어준다.
				userVo = new UserVo(no, id, password, name, gender);
				//userVo.setNo(no);
				//userVo.setName(name);
			}
			
			
		}catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		
		return userVo;
	}

	// 저장 메소드(회원가입)
	public int insert(UserVo userVo) {
		int count = 0;
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			// 문자열
			String query = "";
			query += " insert into users ";
			query += " values(seq_users_no.nextval,?,?,?,?) ";

			// 쿼리문
			pstmt = conn.prepareStatement(query);

			// 바인딩
			pstmt.setString(1, userVo.getId());
			pstmt.setString(2, userVo.getPassword());
			pstmt.setString(3, userVo.getName());
			pstmt.setString(4, userVo.getGender());

			// 실행-db에서 데이터를 가져오는 게 아니라 데이터를 새로 입력하는 과정이므로 excuteUpdate
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건이 처리되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}

	public int update(UserVo userVo) {
		
		int count = 0;
		getConnection();
		
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			// 문자열
			String query = "";
			query += " update users ";
			query += " set    password = ?, ";
			query += " 		  name = ?, ";
			query += " 		  gender = ? ";
			query += " where  no = ? ";
			//query += " and password = ? ";

			// 쿼리문
			pstmt = conn.prepareStatement(query);

			// 바인딩
			pstmt.setString(1, userVo.getPassword());
			pstmt.setString(2, userVo.getName());
			pstmt.setString(3, userVo.getGender());
			pstmt.setInt(4, userVo.getNo());

			// 실행
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건이 처리되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}
}

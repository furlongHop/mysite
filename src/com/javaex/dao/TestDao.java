package com.javaex.dao;

import com.javaex.vo.UserVo;

public class TestDao {

	public static void main(String[] args) {
		
		UserVo userVo = new UserVo("clae","1108","케일","male");
		
		UserDao userDao = new UserDao();
		userDao.insert(userVo);
		
	}

}

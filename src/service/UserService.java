package service;

import dao.UserDao;
import domain.User;

public class UserService {
		public boolean regist(User user){
			UserDao dao=new UserDao();
			int row=dao.regist(user);
			return row>0?true:false;
		}

		public void active(String activeCode) {
			UserDao dao=new UserDao();
			dao.active(activeCode);
		}

		public boolean checkUsername(String username) {
			UserDao dao=new UserDao();
			Long num=dao.checkUsername(username);
			return num>0?true:false;
		}

	

		public User login(String username, String password) {
			UserDao dao=new UserDao();
			return dao.login(username,password);
		}

}

package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.User;
import service.UserService;
import utils.MD5Utils;


@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			HttpSession session=request.getSession();
			UserService service=new UserService();
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			//对密码进行md5加密
			//password=MD5Utils.md5(password);
			User user= service.login(username,password);
			if(user!=null){
				String autoLogin = request.getParameter("autoLogin");
				if("true".equals(autoLogin)){
					
					//创建存储用户名的cookie
					Cookie cookie_username=new Cookie("cookie_username",user.getUsername());
					Cookie cookie_password=new Cookie("cookie_password",user.getPassword());
					response.addCookie(cookie_username);
					response.addCookie(cookie_password);
				}
				//将user对象存到session中
				session.setAttribute("user", user);
				//重定向到首页
				response.sendRedirect(request.getContextPath()+"/index.jsp");
			}else{
				request.setAttribute("loginError", "用户名或密码错误");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}
			
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}

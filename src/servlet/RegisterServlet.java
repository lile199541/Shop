package servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import domain.User;
import service.UserService;
import utils.CommonUtils;
import utils.MailUtils;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			request.setCharacterEncoding("utf-8");
			
			//获得表单数据
			Map<String,String[]>properties=request.getParameterMap();
			User user=new User();
			try {
				//映射封装
				ConvertUtils.register(new Converter(){
					public Object convert(Class clazz,Object value){
						SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
						Date parse=null;
						try {
							parse=format.parse(value.toString());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						return parse;
					}
				},Date.class);
				BeanUtils.populate(user, properties);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			//User中没有的自己封装
			user.setUid(CommonUtils.getUUID());
			user.setTelephone(null);
			//private int state;//是否激活
			user.setState(0);
			//private String code;//激活码
			String activeCode=CommonUtils.getUUID();
			user.setCode(activeCode);
			//将user传递给service层
			UserService service=new UserService();
			boolean isRegisterSuccess=service.regist(user);
			//是否注册成功
			if(isRegisterSuccess){
				//发送激活邮件
				String emailMsg="恭喜您注册成功，请点击下面的链接进行激活账户<a href='http://localhost:8888/Shop/active?activeCode="+activeCode+"'>http://localhost:8888/Shop/active?activeCode="+activeCode+"</a>";
				
				try {
					MailUtils.sendMail(user.getEmail(),emailMsg);
				} catch (AddressException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				response.sendRedirect("RegisterSuccess.jsp");
			}else{
				response.sendRedirect("RegisterFailed.jsp");
			}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}

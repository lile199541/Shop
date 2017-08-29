package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import domain.Category;
import domain.Order;
import service.AdminService;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String methodName=request.getParameter("method");
		if("findAllCategory".equals(methodName)){
			 findAllCategory(request,response);
		}else if("findAllOrders".equals(methodName)){
			findAllOrders(request,response);
		}else if("findOrderInfoByOid".equals(methodName)){
			findOrderInfoByOid(request,response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
protected void findAllCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	AdminService service=new AdminService();
	List<Category>list=service.findAllCategory();
	Gson gson=new Gson();
	String json=gson.toJson(list);
	response.setContentType("text/html;charset=utf-8");
	response.getWriter().write(json);
	}
	//查询所有订单

protected void findAllOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	AdminService service=new AdminService();
	List<Order>orderList=service.findAllOrders();
	request.setAttribute("orderList", orderList);
	request.getRequestDispatcher("/admin/order/list.jsp").forward(request, response);
	}
//查询订单信息

	protected void findOrderInfoByOid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String oid=request.getParameter("oid");
			AdminService service=new AdminService();
			List<Map<String,Object>>mapList=service.findOrderInfoByOid(oid);
			Gson gson=new Gson();
			String json=gson.toJson(mapList);
			System.out.println(json);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(json);
		}
}

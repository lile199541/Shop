package servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;

import domain.Cart;
import domain.CartItem;
import domain.Category;
import domain.Order;
import domain.OrderItem;
import domain.PageBean;
import domain.Product;
import domain.User;
import service.ProductService;
import utils.CommonUtils;
import utils.PaymentUtil;
@WebServlet("/product")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String methodName=request.getParameter("method");
			if("categoryList".equals(methodName)){
				categoryList(request,response);
			}else if("index".equals(methodName)){
				index(request,response);
			}else if("productInfo".equals(methodName)){
				productInfo(request,response);
			}else if("productListByCid".equals(methodName)){
				productListByCid(request,response);
			}else if("addProductToCart".equals(methodName)){
				addProductToCart(request,response);
			}else if("delProFromCart".equals(methodName)){
				delProFromCart(request,response);
			}else if("clearCart".equals(methodName)){
				clearCart(request,response);
			}else if("submitOrder".equals(methodName)){
				submitOrder(request,response);
			}else if("confirmOrder".equals(methodName)){
				confirmOrder(request,response);
			}else if("myOrders".equals(methodName)){
				myOrders(request,response);
			}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	protected void categoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	    ProductService service=new ProductService();
		List<Category>categoryList=service.findAll();
		Gson gson=new Gson();
		String json=gson.toJson(categoryList);
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(json);
}
	protected void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service=new ProductService();
		List<Product>hotProductList=service.findHotProductList();
		List<Product>newProductList=service.findNewProductList();
		//List<Category>categoryList=service.findAll();
		request.setAttribute("hotList",hotProductList);
		request.setAttribute("newList",newProductList);
		//request.setAttribute("categoryList", categoryList);
		request.getRequestDispatcher("/index.jsp").forward(request, response);
		
		
}
	protected void productInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid=request.getParameter("cid");
		String pid=request.getParameter("pid");
		String currentPage=request.getParameter("currentPage");
		ProductService service=new ProductService();
		Product product=service.getProduct(pid);
		request.setAttribute("product", product);
		request.setAttribute("cid", cid);
		request.setAttribute("currentPage", currentPage);
		Cookie[]cookies=request.getCookies();
		String pids=pid;
		if(cookies!=null){
			for(Cookie cookie:cookies){
				if("pids".equals(cookie.getName())){
					pids=cookie.getValue();
					String[]split=pids.split("-");
					List<String>aslist=Arrays.asList(split);
					LinkedList<String>list=new LinkedList<String>(aslist);
					if(list.contains(pid)){
						list.remove(pid);
					}
					list.addFirst(pid);
					StringBuffer sb=new StringBuffer();
					for(int i=0;i<list.size()&&i<7;i++){
						sb.append(list.get(i));
						sb.append("-");
					}
					pids=sb.substring(0,sb.length()-1);
				}
			}
		}
		Cookie cookie_pids=new Cookie("pids",pids);
		response.addCookie(cookie_pids);
		//转发之前获得cookie
		
		request.getRequestDispatcher("product_info.jsp").forward(request, response);
		
	}
	protected void productListByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid=request.getParameter("cid");
		String currentPageStr=request.getParameter("currentPage");
		if(currentPageStr==null)currentPageStr="1";
		int currentPage=Integer.parseInt(currentPageStr);
		int currentCount=12;
		ProductService service=new ProductService();
		PageBean pagebean=service.getPageBean(cid,currentPage,currentCount);
		request.setAttribute("pagebean", pagebean);
		request.setAttribute("cid", cid);
		//获得客户端名为pids的cookie
		Cookie[]cookies=request.getCookies();
		List<Product>historyList=new ArrayList<Product>();
		if(cookies!=null){
			for(Cookie cookie:cookies){
				if("pids".equals(cookie.getName())){
					String pids=cookie.getValue();
					String[]cookie_pids=pids.split("-");
					for(String pid:cookie_pids){
						Product pro=service.getProduct(pid);
						historyList.add(pro);
					}
				}
			}
		}
		request.setAttribute("historyList", historyList);
		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
		
	}
	//将商品添加到购物车
	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void addProductToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service=new ProductService();
		HttpSession session=request.getSession();
		
		String pid=request.getParameter("pid");
		int buyNum=Integer.parseInt(request.getParameter("buyNum"));
		Product product=service.getProduct(pid);
		double totalPrice=product.getShop_price()*buyNum;
		//封装cartitem

		CartItem cartitem=new CartItem();
		//获得session中的购物车
		Cart cart=(Cart) session.getAttribute("cart");
		if(cart==null){
			cart =new Cart();
		}
		Map<String,CartItem>items=cart.getCartitems();
		double newPrice=0.0;
		if(items.containsKey(pid)){
				int oldNum=items.get(pid).getBuyNum();
				int newNum=oldNum+buyNum;
				items.get(pid).setBuyNum(newNum);
				newPrice=buyNum*product.getShop_price();
				items.get(pid).setTotalPrice(newNum*product.getShop_price());
		}else{
			cartitem.setProduct(product);
			cartitem.setBuyNum(buyNum);
			cartitem.setTotalPrice(totalPrice);
			items.put(product.getPid(),cartitem);
			newPrice=buyNum*product.getShop_price();
		}
		double bonus=cart.getBonus()+newPrice;
		cart.setBonus(bonus);
	
		session.setAttribute("cart", cart);
		//request.getRequestDispatcher("/cart.jsp").forward(request, response);
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	
	}
	//删除商品
	protected void delProFromCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String pid=request.getParameter("pid");
			HttpSession session=request.getSession();
			Cart cart=(Cart) session.getAttribute("cart");
			if(cart!=null){
				Map<String,CartItem>cartitem=cart.getCartitems();
				if(cartitem.containsKey(pid)){
					cart.setBonus(cart.getBonus()-cartitem.get(pid).getTotalPrice());
					cartitem.remove(pid);
					cart.setCartitems(cartitem);
				}
			}
			session.setAttribute("cart", cart);
			response.sendRedirect(request.getContextPath()+"/cart.jsp");
			
}
	//清空购物车
	protected void clearCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		session.removeAttribute("cart");
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
		
}
	//提交订单
	protected void submitOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		
		//判断用户是否已经登录,未登录下面代码都不写
		User user=(User) session.getAttribute("user");
		if(user==null){
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			return;
		}
		//目的:封装好一个Order对象,传递给service层
		Order order=new Order();
//		private String oid;//订单号
		order.setOid(CommonUtils.getUUID());
//		private Date ordertime;//下单时间
		order.setOrdertime(new Date());
//		private double total;//订单总金额
		Cart cart=(Cart) session.getAttribute("cart");
		if(cart!=null){
			order.setTotal(cart.getBonus());
		}
		
//		private int state;//订单支付状态
		order.setState(0);
//		private String address;
		order.setAddress(null);
//		private String name;
		order.setName(null);
//		private String telephone;
		order.setTelephone(null);
//		private User user;//订单属于哪个用户
		order.setUser(user);
//		//该订单中有多少订单项
		Map<String,CartItem>cartitems=cart.getCartitems();
		for(Map.Entry<String, CartItem> entry:cartitems.entrySet()){
			CartItem cartitem=entry.getValue();
			OrderItem orderitem=new OrderItem();
			orderitem.setItemid(CommonUtils.getUUID());
			orderitem.setCount(cartitem.getBuyNum());
			orderitem.setSubtotal(cartitem.getTotalPrice());
			orderitem.setProduct(cartitem.getProduct());
			orderitem.setOrder(order);
			order.getOrderItems().add(orderitem);
		}
		ProductService service=new ProductService();
		service.submitOrder(order);
		
		//1.将order存到session域中
		session.setAttribute("order", order);
		
		//2.页面跳转
		response.sendRedirect(request.getContextPath()+"/order_info.jsp");
}
	//确认订单
	
	protected void confirmOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			//1.更新收货人信息
			Map<String,String[]>properties=request.getParameterMap();
			Order order=new Order();
			try {
				BeanUtils.populate(order, properties);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			ProductService service =new ProductService();
			service.updateOrder(order);
			//2.在线支付
			
			// 获得 支付必须基本数据
			String orderid = request.getParameter("oid");
			String money = "0.01";//支付金额
			// 银行
			String pd_FrpId = request.getParameter("pd_FrpId");//银行

			// 发给支付公司需要哪些数据
			String p0_Cmd = "Buy";
			String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
			String p2_Order = orderid;
			String p3_Amt = money;
			String p4_Cur = "CNY";
			String p5_Pid = "";
			String p6_Pcat = "";
			String p7_Pdesc = "";
			// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
			// 第三方支付可以访问网址
			String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
			String p9_SAF = "";
			String pa_MP = "";
			String pr_NeedResponse = "1";
			// 加密hmac 需要密钥
			String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
					"keyValue");
			String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
					p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
					pd_FrpId, pr_NeedResponse, keyValue);
			
			
			String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId="+pd_FrpId+
							"&p0_Cmd="+p0_Cmd+
							"&p1_MerId="+p1_MerId+
							"&p2_Order="+p2_Order+
							"&p3_Amt="+p3_Amt+
							"&p4_Cur="+p4_Cur+
							"&p5_Pid="+p5_Pid+
							"&p6_Pcat="+p6_Pcat+
							"&p7_Pdesc="+p7_Pdesc+
							"&p8_Url="+p8_Url+
							"&p9_SAF="+p9_SAF+
							"&pa_MP="+pa_MP+
							"&pr_NeedResponse="+pr_NeedResponse+
							"&hmac="+hmac;

			//重定向到第三方支付平台
			response.sendRedirect(url);
	}
	//获得我的订单
	protected void myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		
		//判断用户是否已经登录,未登录下面代码都不写
		User user=(User) session.getAttribute("user");
		if(user==null){
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			return;
		}
		ProductService service=new ProductService();
		List<Order>orderList=service.findAllOrder(user.getUid());
		if(orderList!=null){
			for(Order order:orderList){
				List<Map<String,Object>>mapList=service.findOrderItemByOid(order.getOid());
				List<OrderItem>orderitems=new ArrayList<OrderItem>();
				for(Map<String,Object>map:mapList){
					OrderItem orderitem=new OrderItem();
					Product product=new Product();
					try {
						BeanUtils.populate(orderitem, map);
						BeanUtils.populate(product,map);
					
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					orderitem.setProduct(product);
					orderitems.add(orderitem);
				}
				order.setOrderItems(orderitems);
				
			}
		}
		//封装完毕
		request.setAttribute("orderList", orderList);
		request.getRequestDispatcher("/order_list.jsp").forward(request, response);
		
}
}

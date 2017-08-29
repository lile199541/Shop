package domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
		private String oid;//订单号
		private Date ordertime;//下单时间
		private double total;//订单总金额
		private int state;//订单支付状态
		private String address;
		private String name;
		private String telephone;
		private User user;//订单属于哪个用户
		//该订单中有多少订单项
		List<OrderItem>orderItems=new ArrayList<OrderItem>();
		public String getOid() {
			return oid;
		}
		public void setOid(String oid) {
			this.oid = oid;
		}
		public Date getOrdertime() {
			return ordertime;
		}
		public void setOrdertime(Date ordertime) {
			this.ordertime = ordertime;
		}
		public double getTotal() {
			return total;
		}
		public void setTotal(double total) {
			this.total = total;
		}
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTelephone() {
			return telephone;
		}
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public List<OrderItem> getOrderItems() {
			return orderItems;
		}
		public void setOrderItems(List<OrderItem> orderItems) {
			this.orderItems = orderItems;
		}
		
}

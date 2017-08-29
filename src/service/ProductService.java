package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.ProductDao;
import domain.Category;
import domain.Order;
import domain.PageBean;
import domain.Product;
//import sun.nio.cs.ext.MacHebrew;
import utils.DataSourceUtils;

public class ProductService {

	public List<Product> findHotProductList() {
		ProductDao dao=new ProductDao();
		
		return dao.findHotProductList();
	}

	public List<Product> findNewProductList() {
	ProductDao dao=new ProductDao();
		
		return dao.findNewProductList();
	}

	public List<Category> findAll() {
		ProductDao dao=new ProductDao();
		return dao.findAll();
	}

	public PageBean getPageBean(String cid,int currentPage,int currentCount) {
		//封装PageBean
		PageBean<Product> pagebean=new PageBean<Product>();
		ProductDao dao=new ProductDao();
		pagebean.setCurrentPage(currentPage);
		pagebean.setCurrentCount(currentCount);
		int totalCount=0;
		try {
			totalCount=dao.getCount(cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pagebean.setTotalCount(totalCount);
		int totalpage=(int) Math.ceil(1.0*totalCount/currentCount);
		pagebean.setTotalPage(totalpage);
		//当前页显示数据
		int index=(currentPage-1)*currentCount;
		List<Product>list=dao.findProductByPage(cid,index,currentCount);
		pagebean.setList(list);
		return pagebean;
	}

	public Product getProduct(String pid) {
		ProductDao dao=new ProductDao();
		Product product=dao.getProduct(pid);
		return product;
	}


	public void submitOrder(Order order) {
			ProductDao dao=new ProductDao();
			try {
				//开启事物
				DataSourceUtils.startTransaction();
				//提交数据到order表
				dao.addOrders(order);
				//提交数据到orderitem表
				dao.addOrderItem(order);
			} catch (SQLException e) {
				try {
					DataSourceUtils.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}finally{
				try {
					DataSourceUtils.commitAndRelease();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	}

	public void updateOrder(Order order) {
		ProductDao dao=new ProductDao();
		dao.updateOrder(order);
	}

	public void updateOrderState(String r6_Order) {
		ProductDao dao=new ProductDao();
		dao.updateOrderState(r6_Order);
	}

	public List<Order> findAllOrder(String uid) {
		ProductDao dao=new ProductDao();
		 List<Order>orderList=dao.findAllOrder(uid);
		return orderList;
	}

	public List<Map<String, Object>> findOrderItemByOid(String oid) {
		 List<Map<String, Object>>mapList=new ArrayList<Map<String,Object>>();
		ProductDao dao=new ProductDao();
		mapList=dao.findOrderItemByOid(oid);
		return mapList;
	}



	

	
		
}

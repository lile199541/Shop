package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import dao.AdminDao;
import domain.Category;
import domain.Order;
import domain.Product;

public class AdminService {

	public List<Category> findAllCategory() {
		AdminDao dao=new AdminDao();
		
			return dao.findAllCategory();
		
		
	}

	public void addProduct(Product product) {
		AdminDao dao=new AdminDao();
		dao.addProduct(product);
		
	}

	public List<Order> findAllOrders() {
		AdminDao dao=new AdminDao();
		return dao.findAllOrders();
	}

	public List<Map<String, Object>> findOrderInfoByOid(String oid) {
		AdminDao dao=new AdminDao();
		List<Map<String,Object>>mapList=dao.findOrderInfoByOid(oid);
		return mapList;
	}

}

package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import domain.Category;
import domain.Order;
import domain.Product;
import utils.DataSourceUtils;

public class AdminDao {
	public List<Category> findAllCategory() {
		
			QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
			String sql="select * from category";
			try {
				return runner.query(sql, new BeanListHandler<Category>(Category.class));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	public void addProduct(Product product) {
			QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
			String sql="insert into product values(?,?,?,?,?,?,?,?,?,?)";
			try {
				runner.update(sql, product.getPid(),product.getPname(),product.getMarket_price(),product.getShop_price(),product.getPimage(),product.getPdate(),product.getIs_hot(),product.getPdesc(),product.getPflag(),product.getCategory().getCid());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	public List<Order> findAllOrders() {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from orders";
		try {
			return runner.query(sql, new BeanListHandler<Order>(Order.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Map<String, Object>> findOrderInfoByOid(String oid) {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select p.pimage,p.pname,p.shop_price,i.count,i.subtotal from product p,orderitem i where p.pid=i.pid and i.oid=?";
		try {
			return runner.query(sql, new MapListHandler(), oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	}
		


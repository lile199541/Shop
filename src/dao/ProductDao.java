package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;



import domain.Category;
import domain.Order;
import domain.OrderItem;
import domain.Product;
import utils.DataSourceUtils;

public class ProductDao {

	public List<Product> findHotProductList() {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from product where is_hot=? limit?,?";
		try {
			return	runner.query(sql,new BeanListHandler<Product>(Product.class),1,0,9 );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public List<Product> findNewProductList() {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from product order by pdate desc limit ?,?";
		try {
			return runner.query(sql, new BeanListHandler<Product>(Product.class), 0,9);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Category> findAll() {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from category";
		try {
			return runner.query(sql, new BeanListHandler<Category>(Category.class));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getCount(String cid) throws SQLException {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select count(*) from product where cid=?";
		
		Long query=(Long) runner.query(sql, new ScalarHandler(),cid);
		return query.intValue();
		
	}

	public List<Product> findProductByPage(String cid, int index, int currentCount) {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from product where cid=? limit ?,?";
		List<Product>list=null;
		try {
			list=runner.query(sql, new BeanListHandler<Product>(Product.class), cid,index,currentCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Product getProduct(String pid) {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from product where pid=?";
		try {
			return runner.query(sql, new BeanHandler<Product>(Product.class), pid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	//想orders表假数据
	public void addOrders(Order order) {
			QueryRunner runner =new QueryRunner();
			try {
				Connection conn=DataSourceUtils.getConnection();
				String sql="insert into orders values(?,?,?,?,?,?,?,?)";
				runner.update(conn,sql,order.getOid(),order.getOrdertime(),order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid());
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
	}

	public void addOrderItem(Order order) {
		QueryRunner runner =new QueryRunner();
		try {
			Connection conn=DataSourceUtils.getConnection();
			String sql="insert into orderitem values(?,?,?,?,?)";
			for(OrderItem orderitem:order.getOrderItems()){
				runner.update(conn, sql,orderitem.getItemid(),orderitem.getCount(),orderitem.getSubtotal(),orderitem.getProduct().getPid(),orderitem.getOrder().getOid());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateOrder(Order order) {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="update orders set address=?,name=?,telephone=? where oid=?";
		try {
			runner.update(sql, order.getAddress(),order.getName(),order.getTelephone(),order.getOid());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateOrderState(String r6_Order) {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="update orders set state=? where oid=?";
		try {
			runner.update(sql, 1,r6_Order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Order> findAllOrder(String uid) {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from orders where uid=?";
		List<Order>orderList=null;
		try {
			orderList=runner.query(sql,new BeanListHandler<Order>(Order.class),uid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderList;
	}

	public List<Map<String, Object>> findOrderItemByOid(String oid) {
		QueryRunner runner=new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select i.count,i.subtotal,p.pimage,p.pname,p.shop_price from orderitem i,product p where i.pid=p.pid and i.oid=?";
		List<Map<String, Object>>mapList=null;
		try {
			mapList=runner.query(sql, new MapListHandler(), oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapList;
	}



}

package domain;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	//购物车存储的n个购物箱
		private Map<String,CartItem>cartitems=new HashMap<String,CartItem>();
		private double bonus;
		public Map<String, CartItem> getCartitems() {
			return cartitems;
		}
		public void setCartitems(Map<String, CartItem> cartitems) {
			this.cartitems = cartitems;
		}
		public double getBonus() {
			return bonus;
		}
		public void setBonus(double bonus) {
			this.bonus = bonus;
		}
		
}

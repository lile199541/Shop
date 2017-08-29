package domain;

import java.util.List;

public class PageBean<T> {
		private int currentPage;//当前页
		private int currentCount;//当前页数目
		private int totalPage;//总页数
		private int totalCount;//总数目
		private List<T>list;
		public int getCurrentPage() {
			return currentPage;
		}
		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
		}
		public int getCurrentCount() {
			return currentCount;
		}
		public void setCurrentCount(int currentCount) {
			this.currentCount = currentCount;
		}
		public int getTotalPage() {
			return totalPage;
		}
		public void setTotalPage(int totalPage) {
			this.totalPage = totalPage;
		}
		public int getTotalCount() {
			return totalCount;
		}
		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}
		public List<T> getList() {
			return list;
		}
		public void setList(List<T> list) {
			this.list = list;
		}
		
}

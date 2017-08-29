package servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import domain.Category;
import domain.Product;
import service.AdminService;

import utils.CommonUtils;


@WebServlet("/adminAddProduct")
public class AdminAddProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("----");
		try {
			Map<String,Object>map=new HashMap<String,Object>();
			Product product=new Product();
			DiskFileItemFactory factory=new DiskFileItemFactory();
			ServletFileUpload upload=new ServletFileUpload(factory);
			boolean isMultipartContent=upload.isMultipartContent(request);
			if(isMultipartContent){
					List<FileItem>list=upload.parseRequest(request);
					for(FileItem item:list){
						boolean flag=item.isFormField();
						if(flag){
							String name=item.getFieldName();
							String value=item.getString("utf-8");
							System.out.println(name+"--"+value);
							map.put(name, value);
						}else{
							String filename=item.getName();
							filename=filename.substring(filename.lastIndexOf("\\")+1);
							InputStream in=item.getInputStream();
							String path=this.getServletContext().getRealPath("upload");
							//绝对地址
							OutputStream out=new FileOutputStream(path+"/"+filename);
							IOUtils.copy(in, out);
							in.close();
							out.close();
							item.delete();
							//pimage
							map.put("pimage","upload/"+filename);
						}
					}
				} 
			
			BeanUtils.populate(product, map);
			//pid
			product.setPid(CommonUtils.getUUID());
			//pdate
			product.setPdate(new Date());		
			//pflag
			product.setPflag("0");
			Category category=new Category();
			category.setCid(map.get("cid").toString());
			product.setCategory(category);
			AdminService service=new AdminService();
			service.addProduct(product);
	}
		catch (FileUploadException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}

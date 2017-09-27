import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;

@MultipartConfig
public class FileServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public FileServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");  
        request.setCharacterEncoding("utf-8");  
		PrintWriter out = response.getWriter();
		System.out.println("request.getContentType() is  "+ request.getContentType());
		
        String paramJson = request.getParameter("ParamJson");     
        if (paramJson != null)
        {
        User user = new User();
        user = JSON.parseObject(paramJson,User.class);
        System.out.println("Username = "+user.getUsername());
        System.out.println("Password = "+user.getPassword());
        }
		
        String savePath = request.getServletContext().getRealPath("/WEB-INF/"); 
        //Collection<Part> parts = request.getParts();  
        System.out.println("local savePath is "+savePath);
        Part file = request.getPart("file");
        String header = file.getHeader("content-disposition"); 
        String fileName = getFileName(header);
        System.out.println("fileName is "+fileName);
        
        file.write(savePath + File.separator + fileName);   
        System.out.println("over!!!");
        
        
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
    public String getFileName(String header) {  
        /** 
         * String[] tempArr1 = 
         * header.split(";");代码执行完之后，在不同的浏览器下，tempArr1数组里面的内容稍有区别 
         * 火狐或者google浏览器下： 
         * tempArr1={form-data,name="file",filename="snmp4j--api.zip"} 
         * IE浏览器下：tempArr1={form-data,name="file",filename="E:\snmp4j--api.zip"} 
         */  
        String[] tempArr1 = header.split(";");  
        /** 
         * 火狐或者google浏览器下：tempArr2={filename,"snmp4j--api.zip"} 
         * IE浏览器下：tempArr2={filename,"E:\snmp4j--api.zip"} 
         */  
        String[] tempArr2 = tempArr1[2].split("=");  
        // 获取文件名，兼容各种浏览器的写法  
        String fileName = tempArr2[1].substring(  
                tempArr2[1].lastIndexOf("\\") + 1).replaceAll("\"", "");  
        return fileName;  
    } 

}

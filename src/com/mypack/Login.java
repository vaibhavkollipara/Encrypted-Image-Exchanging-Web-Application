package com.mypack;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Connection con = null;
	private Statement st = null;
	private ResultSet rs = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String uname = request.getParameter("uname");
		String pass = request.getParameter("pass");
		PrintWriter pw = response.getWriter();
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", "img",
					"img");
			con.setAutoCommit(false);
			
			st = con.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			
			rs = st
					.executeQuery("select U_id from users where u_name = '"+ uname +"' and "
							+ "pass = '"+ pass+"'");
			
			if (rs.next()){
				Cookie cook1 = new Cookie("id", String.valueOf(rs.getInt("U_id")));
				Cookie cook2 = new Cookie("uname", uname);
				cook1.setMaxAge(24*60*60);
				cook2.setMaxAge(24*60*60);
				response.addCookie(cook1);
				response.addCookie(cook2);
			}

			con.commit();
			con.close();
			rs.close();
			st.close();
			response.sendRedirect("home.jsp");
			pw.close();
		} catch (Exception ex) {
			pw.println(ex.getMessage());
			System.out.println(ex.getMessage());
		} finally{
			try{
			rs.close();
			st.close();
			con.close();
			}catch(Exception e){
				pw.print(e.getMessage());
			}
			pw.close();
		}
	}

}

package com.mypack;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.merhell.ImgEnc;

/**
 * Servlet implementation class Register
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Connection con = null;
	private Statement st = null;
	private ResultSet rs = null;
	private PreparedStatement ps,ps2,ps1 = null;
    private PrintWriter pw = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
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
		pw = response.getWriter();
		
		System.out.print("Uname :"+ uname +"\nPass :"+ pass);
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", "img",
					"img");
			con.setAutoCommit(false);
			ps = con
					.prepareStatement("insert into Users values(U_id.nextval,?,?)");

			ps.setString(1, uname);
			ps.setString(2, pass);
			
			ps.executeUpdate();
			con.commit();
			//-------------------------//
			st = con.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = st
					.executeQuery("select max(U_id) as max from Users");
			int i=0;
			while (rs.next()) {
			 i= rs.getInt("max");									
			}
			System.out.println("\n i  : "+ i);
			
			ImgEnc ie = new ImgEnc();
			
			int []prkey = ie.privatekey();
			
			ps2 = con.prepareStatement("insert into privatekey values(?,?,?,?,?,?,?,?,?,?,?)");
			ps2.setInt(1, i);
			ps2.setInt(2, prkey[0]);
			ps2.setInt(3, prkey[1]);
			ps2.setInt(4, prkey[2]);
			ps2.setInt(5, prkey[3]);
			ps2.setInt(6, prkey[4]);
			ps2.setInt(7, prkey[5]);
			ps2.setInt(8, prkey[6]);
			ps2.setInt(9, prkey[7]);
			ps2.setInt(10, prkey[8]);
			ps2.setInt(11, prkey[9]);
			ps2.executeUpdate();
			con.commit();
			//-------------------------------------------//
			int []pukey = ie.publickey(prkey,prkey[8],prkey[9]);
			
			ps1 = con.prepareStatement("insert into publickey values(?,?,?,?,?,?,?,?,?)");
			ps1.setInt(1, i);
			ps1.setInt(2, pukey[0]);
			ps1.setInt(3, pukey[1]);
			ps1.setInt(4, pukey[2]);
			ps1.setInt(5, pukey[3]);
			ps1.setInt(6, pukey[4]);
			ps1.setInt(7, pukey[5]);
			ps1.setInt(8, pukey[6]);
			ps1.setInt(9, pukey[7]);
			ps1.executeUpdate();
			con.commit();
			//--------------------//
			
			pw.println("<html><body>");
			pw.print("<h2>Registration Successful</h2>\n");
			pw.print("Click <a href=\"login.jsp\"> here </a> to login");
			pw.println("</body></html>");
		} catch (Exception ex) {
			pw.println(ex.getMessage());
			System.out.println("\n"+ex.getMessage());
		} finally {
			try{
				ps.close();
				rs.close();
				st.close();
				con.close();
				ps1.close();
				ps2.close();
				}catch(Exception e){
					pw.print(e.getMessage());
				}
			pw.close();

		}
		
	}

}

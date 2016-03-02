package com.mypack;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.merhell.ImgEnc;

/**
 * Servlet implementation class Decrypt
 */
@WebServlet("/decrypt")
public class Decrypt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private Statement st = null;
	private ResultSet rs = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Decrypt() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String filename = request.getParameter("fname");
		ImgEnc ie = new ImgEnc();
		int id = 0;
		System.out.println("Running");
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals("id")) {
				id = Integer.parseInt(cookies[i].getValue());
			}
		}
		int privatekey[]= new int[10];
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", "img", "img");
				st = con.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				rs = st.executeQuery("select S1,S2,S3,S4,S5,S6,S7,S8,Q,R from privatekey where U_id= " +id);
				System.out.println("id: "+id);
				if(rs.next()) {
					privatekey[0]= rs.getInt("S1");
					privatekey[1]= rs.getInt("S2");
					privatekey[2]= rs.getInt("S3");
					privatekey[3]= rs.getInt("S4");
					privatekey[4]= rs.getInt("S5");
					privatekey[5]= rs.getInt("S6");
					privatekey[6]= rs.getInt("S7");
					privatekey[7]= rs.getInt("S8");
					privatekey[8]= rs.getInt("Q");
					privatekey[9]= rs.getInt("R");
				}
				System.out.println("q : "+privatekey[8]+"\nr : "+privatekey[9]);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
			rs.close();
			st.close();
			con.close();
			}catch(Exception e){
				System.out.print(e.getMessage());
			}
		}
		response.setContentType("image/png");
		BufferedImage dec = ie.decryption(privatekey,filename);
		OutputStream out = response.getOutputStream();
		ImageIO.write(dec, "png", out);
		out.close();
		
		
	}

}

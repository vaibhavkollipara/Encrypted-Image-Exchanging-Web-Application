package com.mypack;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.merhell.ImgEnc;

/**
 * Servlet implementation class SendImg
 */
@WebServlet("/sendimg")
public class SendImg extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int filename = 1;
	
	private Connection con = null;
	private Statement st = null;
	private ResultSet rs = null;
	private PreparedStatement ps = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SendImg() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = response.getWriter();

		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload sfu = new ServletFileUpload(factory);

			if (!ServletFileUpload.isMultipartContent(request)) {
				System.out.println("sorry. No image uploaded");
				return;
			}
			// parse request
			List items = sfu.parseRequest(request);
			FileItem send = (FileItem) items.get(0);
			String sender = send.getString();

			FileItem receive = (FileItem) items.get(1);
			String receiver = receive.getString();

			System.out.println("sender : " + sender + " \nreceiver :"
					+ receiver);

			// get uploaded file
			FileItem fileitem = (FileItem) items.get(2);

			// String path = request.getServletContext().getRealPath("");
			InputStream is = fileitem.getInputStream();

			ImgEnc ie = new ImgEnc();
			BufferedImage src = ImageIO.read(is);
			int beta[] = new int[8];
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", "img", "img");
			st = con.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = st
					.executeQuery("select B1,B2,B3,B4,B5,B6,B7,B8 from publickey where U_id="
							+ receiver);

			while (rs.next()) {
				beta[0] = rs.getInt("B1");
				beta[1] = rs.getInt("B2");
				beta[2] = rs.getInt("B3");
				beta[3] = rs.getInt("B4");
				beta[4] = rs.getInt("B5");
				beta[5] = rs.getInt("B6");
				beta[6] = rs.getInt("B7");
				beta[7] = rs.getInt("B8");
			}

			BufferedImage dst[] = ie.encryption(src, beta);

			File outputimg = new File(
					"E:\\Java EE Eclipse\\MyProjects\\FinalProject\\encrypted_images\\code_"
							+ filename + ".png");
			ImageIO.write(dst[0], "png", outputimg);
			File outputcode = new File(
					"E:\\Java EE Eclipse\\MyProjects\\FinalProject\\encrypted_images\\image_"
							+ filename + ".png");
			ImageIO.write(dst[1], "png", outputcode);
			
			ps = con.prepareStatement("insert into message(sender_id,receiver_id,img_name) values(?,?,?)");
			ps.setInt(1, Integer.parseInt(sender));
			ps.setInt(2, Integer.parseInt(receiver));
			ps.setString(3, String.valueOf(filename));
			ps.executeUpdate();
			
			filename++;			
			
			con.commit();
			
			pw.println("<html><body>");
			pw.print("<h2>Image Successfully Encrypted and sent</h2><br>");
			pw.print("Click <a href=\"send.jsp\"> here </a> to Send another Image<br><br>");
			pw.print("Click <a href=\"home.jsp\"> here </a> to navigate to home");
			pw.println("</body></html>");
			pw.close();
		} catch (Exception e) {
			pw.print(e.getMessage() + "  Error Dude !!");
			System.out.print("\n" + e.getMessage() + "  Error Dude !!");

		}finally{
			try{
			ps.close();
			rs.close();
			st.close();
			con.close();
			pw.close();
			}catch(Exception e){
				pw.print(e.getMessage());
				pw.close();
			}
		}
	}
}

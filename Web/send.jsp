<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Merkle Hellman Knapsack Cryptosystem</title>
<link rel="stylesheet" type="text/css" href="mystyle.css" />
</head>
<body>
	<%
		String uname = "";
		Cookie[] cookies = request.getCookies();

		if (cookies.length == 0) {
			response.sendRedirect("login.jsp");
		}
	%>
	<div id="header">
		<h1>Image Encryption Using Merkle-Hellman Knapsack Cryptosystem</h1>
	</div>

	<!-- ______________________________________________________________________ -->

	<div id="main">
		<center>
			<%
				String name = "";
				int id = 0;
				for (int i = 0; i < cookies.length; i++) {
					if (cookies[i].getName().equals("uname")) {
						name = cookies[i].getValue();
					}
					if (cookies[i].getName().equals("id")) {
						id = Integer.parseInt(cookies[i].getValue());
					}
				}
			%>
			<center>
     <h2>Welcome <%=name%></h2>  
     <form action="logout" method="post">
     <input type="submit" value="  Logout  " >
     </form>
   </center>
   <br><br>
   <div class="details">
			<form method="post" action="sendimg" enctype="multipart/form-data">
			<input type="hidden" name="send" value="<%=id%>"><br>
			<table>
			<tr>
			  <td>Select Recipient</td>
			  <td><select name="receive" required>
					<option value="">** Select One **</option>
					<%
						Class.forName("oracle.jdbc.driver.OracleDriver");
						Connection con = DriverManager.getConnection(
								"jdbc:oracle:thin:@localhost:1521:xe", "img", "img");
						try {
							Statement st = con.createStatement(
									ResultSet.TYPE_SCROLL_INSENSITIVE,
									ResultSet.CONCUR_UPDATABLE);
							ResultSet rs = st
									.executeQuery("select U_id,U_name from Users");
							while (rs.next()) {
					%>
					<option value='<%=rs.getInt("U_id")%>'><%=rs.getString("U_name")%></option>
					<%
						}
							st.close();
							rs.close();
							con.close();
						} catch (Exception e) {
                          System.out.println(e.getMessage());
						}
					%>
				</select> </td>
			</tr>
		    <tr>
				<td>Select Image to Encrypt and Send </td>
				<td> <input type="file" name="file"
					size="60" /></td>
			</tr>	
			<tr>
			<td><input class="mybutton" type="submit" value="Send" /></td>
			<td><input class="mybutton" type="reset" value="Reset" /></td>
			</tr>
			  </table>
			</form>
			</div><br>
		</center>
		</div>

	<!-- ______________________________________________________________________ -->


	<div id="footer">
		<h3>&copy; Copyrights Reserved<br>
		* Vinod * Vaibhav * Karthik * Rajesh * </h3>
	</div>

</body>
</html>
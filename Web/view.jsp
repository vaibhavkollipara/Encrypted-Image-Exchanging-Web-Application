<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Merkle Hellman Knapsack Cryptosystem</title>
<link rel="stylesheet" type="text/css" href="mystyle.css"/>
</head>
<body>	
	<div id="header">
		<h1>Image Encryption Using Merkle-Hellman Knapsack Cryptosystem</h1>
    </div>
		
<!-- ______________________________________________________________________ -->
<%

String uname="";
int id=0;
Cookie[] cookies = request.getCookies();
boolean foundCookie = false;

for(int i = 0; i < cookies.length; i++) { 
    Cookie cookie1 = cookies[i];
    if (cookie1.getName().equals("uname")) {
        uname = cookie1.getValue();
        foundCookie = true;
    }
    if (cookie1.getName().equals("id")) {
        id = Integer.parseInt(cookie1.getValue());
        foundCookie = true;
    }
}  

if (!foundCookie) {
    response.sendRedirect("login.jsp");
}
  
%>

   <div id="main">
   <center>
     <h2>Welcome <%=uname%></h2>  
     <form action="logout" method="post">
     <input type="submit" value="  Logout  " >
     </form>
   </center>
   <br><br>
   <center>
     <table class="tab">
     <tr>
      <th>S.No</th>
      <th>Sender</th>
      <th>Date</th>
      <th> </th>
     </tr>
     <%try{
   Class.forName("oracle.jdbc.driver.OracleDriver");
	Connection con = DriverManager.getConnection(
			"jdbc:oracle:thin:@localhost:1521:xe", "img", "img");
	Statement st = con.createStatement(
			ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_UPDATABLE);
	ResultSet rs = st.executeQuery("select U_name,Dt,Img_name from Users,Message where Receiver_id = "+ id +
			"and Sender_id=U_id order by Img_name DESC");
	int i=1;
	while(rs.next()){
   %>
     <tr>
     <td><%=i++ %></td>
      <td> <%=rs.getString("U_name") %></td>
      <td><%=rs.getDate("Dt") %> </td>      
      <td> 
        <form action="viewimg.jsp" method="get">
          <input type="hidden" name="fname" value='<%=rs.getString("Img_name")%>'>
          <input type="submit" value="View" >
        </form>
      </td>
     </tr>
     
     <%
	}
	
	st.close();
	rs.close();
	con.close();
     }catch(Exception e){
    	 System.out.print(e.getMessage());
     }
     %>
     </table>
     </center>
   
   </div>
		
<!-- ______________________________________________________________________ -->

<br>
<div id="footer">
  <h3>&copy; Copyrights Reserved<br>
		* Vinod * Vaibhav * Karthik * Rajesh * </h3>
</div>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
Cookie[] cookies = request.getCookies();
boolean foundCookie = false;

for(int i = 0; i < cookies.length; i++) { 
    Cookie cookie1 = cookies[i];
    if (cookie1.getName().equals("uname")) {
        uname = cookie1.getValue();
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
   <div class="box">
       <h2>
          <a href="send.jsp">Send Images</a>
       </h2>
    </div>
    <div class="box">
       <h2>
          <a href="view.jsp">View Images</a>
       </h2>
    </div>
   </div>
		
<!-- ______________________________________________________________________ -->


<div id="footer">
  <h3>&copy; Copyrights Reserved<br>
		* Vinod * Vaibhav * Karthik * Rajesh * </h3>
</div>

</body>
</html>
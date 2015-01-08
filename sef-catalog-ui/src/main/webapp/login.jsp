<!DOCTYPE html>
<html lang="en"> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
  <title>Login Form</title>
  <script type="text/javascript">
  $(document).ready(function(){
	  if(${loginMessage == null}){
		  $('.invalid-usr-pwd').html('');
	  }
  })
  </script>
  
  <link rel="stylesheet" href="css/style.css">
  <!--- Javascript libraries (jQuery and Selectivizr) used for the custom checkbox --->
</head>
	<body>
	
	 
	<div id="container">
		<form method="POST" action="signin">
			<div class="login">LOGIN</div>
			<div style="float:left;margin-top:10px;">
				<div class="username-text">Username:</div>
				<div class="password-text">Password:</div>
				<div class="username-field">
					<input type="text" name="name" id="name"  placeholder="Username" />
				</div>
				<div class="password-field">
					<input type="password" name="password" id="password"  placeholder="Password" />
				</div>
			</div>
			<c:if test="${loginMessage != null}">
			<div class="invalid-usr-pwd">${loginMessage}</div>
			</c:if>
			
			<input type="submit" name="submit" value="GO"/>
		</form>
	</div>


   
</body>
 
</html>

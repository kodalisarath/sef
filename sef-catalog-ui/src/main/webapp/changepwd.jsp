<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en"> <!--<![endif]-->

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
  <script type="text/javascript" src="js/login.js"></script>
  <title>Login Form</title>
  
 <link rel="stylesheet" href="css/style.css">
 <script type="text/javascript">
 $(document).ready(function(){
	 if(${username == null}){
		 window.location.href = 'login.jsp'; 
	 }
<c:if test="${ChangepwdError != null}">
$('.invalid-usr-pwd').show();
</c:if>
	  
	  
  })
 
 </script>
  <!--- Javascript libraries (jQuery and Selectivizr) used for the custom checkbox --->

</head>

	<body>
	<div id="container">
		<form action="changePassword" class="testform" method="POST">
			<div class="login">Change Password</div>
			<a href="login.jsp" style="color:#fff; float:right; margin-right:115px; margin-top:-12px; font-weight:bold; fon-size:14px; text-decoration:none;">Go to Login >></a>
			<div style="float:left;margin-top:35px;">
			<input type="hidden" name="name" id="name"  value="${username}"  />
				<div class="oldpassword-text">Old Password:</div>
				<div class="oldpassword-field">
					<input type="password" name="oldPassword" id="password"  placeholder="Old Password" />
				</div>
				<br>
				<div class="newpassword-text">New Password:</div>
				<div class="newpassword-field">
					<input type="password" name="newPassword" id="password"  placeholder="New Password" />
				</div>
				<br>
				<div class="conformpassword-text">Conform Password:</div>
				<div class="conformpassword-field">
					<input type="password" name="confirmPassword" id="password"  placeholder= "New Password" />
				</div>
			</div>
			<c:if test="${ChangepwdError != null}">
			<div class="invalid-usr-pwd">${ChangepwdError}</div></c:if>
			<input type="submit" name="submit" id="changego" value="Go"/>
		</form>
	</div>

</body>
</html>
  
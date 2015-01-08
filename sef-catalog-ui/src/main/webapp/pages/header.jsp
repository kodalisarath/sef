
<style>
	#shareit-box {
	position:absolute;
	display:none;
	z-index:99999;
	right:5px;
	}

	#shareit-header {
		width:138px;
	}
	#shareit-body {
		width:138px; height:100px;
		background:url(images/tooltip.png) no-repeat;
		cursor:pointer;
		text-align:left;
		margin-top:25px;
		cursor:pointer;
	}
	#shareit-body a {
		text-decoration:none;
		margin-top:5px;
		float:left;
		width:138px;
		font-size:11px;
		margin-left:10px;
	}
	#shareit-body a.first {
		margin-top:15px;
	}
	#shareit-body a:hover {color:#047fff;}

	a input[id="logout"]{
		font-weight: bold;
		color: #222;
		margin-left:-5px;
		transition: color .15s ease-in 0;
		-moz-transition-property: color;
		-moz-transition-timing-function: ease-in;
		-moz-transition-duration: .15s;
		-moz-transition-delay: 0;
		-webkit-transition-property: color;
		-webkit-transition-timing-function: ease-in;
		-webkit-transition-duration: .15s;
		-webkit-transition-delay: 0;
		text-shadow: 0 1px 0 rgba(255, 255, 255, 0.5);
		float: left;
		text-decoration:none;
		font-size:12px;
		line-height:24px;
		border:none !important;
		background:none !important;
		border-radius:none !important;
		cursor:pointer;
	}
	input[id="logout"]:hover {color:#047fff;border:none !important;
		background:none !important;
		border-radius:none !important;}
</style>
<script>


$(document).ready(function() {

	//grab all the anchor tag with rel set to shareit
	$('a[rel=shareit], #shareit-box').mouseenter(function() {		
		
		//get the height, top and calculate the left value for the sharebox
		var height = $(this).height();
		var top = $(this).offset().top;
		
		//display the box
		$('#shareit-box').show();
		
		
	});

	//onmouse out hide the shareit box
	$('#shareit-box').mouseleave(function () {
		$('#shareit-field').val('');
		$(this).hide();
	});
	
	
});


</script>
	<div class="main-container">
            <div id="header" class="true clearfix">
	<div class="logo-container clearfix">
		<a class="site-logo" href="#"> </a>
		
		<img border="0" src="images/ericsson.png"  style="float: left; margin:15px 0 0 15px;">
		<div class="optimer-logo-bg"></div>
				
        <ul class="header-right logged_out">
			
			<%-- <li><a href="index.jsp">
					<img border="0" title="Home" src="images/home-icon.png" >
                     <fmt:message key="HOME" bundle="${lang}"/> 
				</a>
			</li> --%>
			
			<li><a href="#" rel="shareit">
					<img border="0" title="Home" id="loggedUser" src="images/admin.png" style="margin-top:3px;">
                     Hello <span style="font-style:italic;color:#047fff;">${username}</span>
				</a>
				<div id="shareit-box">
					<div id="shareit-header"></div>
					<div id="shareit-body">
						<a href="changepwd.jsp" class="first"><img border="0" title="Change Password" src="images/pwd.png"  style="margin-top:3px;width:16px;">Change Password</a><br />
				        <form action="logout" method="POST">
						<a><img border="0" title="Logout" src="images/logout-icon.png" style="margin-top:3px;width:16px;"><input type="submit" name="logout" id="logout" value="Logout"/></a>
					</form>
				    </div>
				</div>
			</li>
			
			
			
			<%-- <li><a href="changepwd.jsp">
				<img border="0" title="Change Password" src="images/pwd.png"  style="margin-top:3px;">
				<fmt:message key="CHANGEPASSSWORD" bundle="${lang}"/> 
				</a>
			</li>
			
			<li><a href="login.jsp">
					<img border="0" title="Logout" src="images/logout-icon.png" style="margin-top:3px;">
                    <fmt:message key="LOGOUT" bundle="${lang}"/> 
					</a>
			</li> --%>
			
		</ul>
		
		
	</div>
	<input type="text" hidden="true" name="name" value='<%=request.getParameter("username") %>' />
</div>

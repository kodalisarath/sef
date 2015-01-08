var LoginHandler={
		
		
		validate:function(){
			var userName=$('#username').val();
			var password=$('#password').val();
			if(userName === 'admin' && password === 'admin'){
				//window.location("index.jsp"); 
				 window.location.href='index.jsp';
			}else{
				if(userName != '' && password != ''){
					$.ajax({
		                url: '/sef-catalog-ui/validateUser?userName=' +userName+'&password='+password,
		                type: "GET",
		                contentType: 'application/json',
		                success: function (data) {
		                    if (data.status == 'success') {
		                    	 window.location.href='index.jsp';
		                       
		                    } else {
		                    	$('.invalid-usr-pwd').show();
		                    	//$('.invalid-usr-pwd').html(data.message); 
		                    	$('#username').empty();
		                    	$('#password').empty();
		                    }
		                }
		            });

				}else{
					$('.invalid-usr-pwd').show();
                	$('.invalid-usr-pwd').html("Please enter username and password"); 
					return;
					
				}
			}
			
		}
		
		
}
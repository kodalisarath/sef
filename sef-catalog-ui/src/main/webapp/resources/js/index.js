var MenuHandler = {
    //Global variable holds treeview 
		
    treeview: null,
    treeview1: null,
    menuEvents: function () {
        //Event called when a menu item is clicked
    	
        $('#cssmenu ul li ul li').click(function () {
            var thisId = $(this).attr('id');
            $("#cssmenu ul li ul li").each(function (index) {
                if ($(this).attr('id') == thisId) {
                    $('#' + thisId).addClass('active');
                } else {
                    $(this).removeClass('active');
                    $('#ui-btn').show();
                    $('.ui-btn1').hide();
                    $('.ui-btn12').hide();
                    $('.ui-btn123').hide();

                }
            });
            if (thisId == 'offers-menu') {
                OfferHandler.loadOffers();
            } else if (thisId == 'resource-menu') {
            	ResourceHandler.loadResources();
            } else if (thisId == 'business-menu') {
                BusinessHandler.loadBusiness();
            }else if (thisId == 'resource-group-menu') {
            	ResourceManagerHandler.loadResourceGroups();
            }else if (thisId == 'offer-group-menu') {
            	OfferGroupHandler.loadOfferCatalogGroup();;
            }else if (thisId == 'owner-group-menu') {
            	OwnerGroupHandler.loadOwnerGroups();
            }else if (thisId == 'user-menu') {
            	UserHandler.loadUsers();
            }else if (thisId == 'group-menu') {
            	GroupHandler.loadGroups();
            }else if (thisId == 'priviliges-menu') {
            	PrivilegesHandler.loadPrivileges();
            }else if (thisId == 'notification-menu') {
            	NotificationHandler.loadNotifications();
            }
            
            $('#error_message').hide();
            
        });

        $.fn.serializeObject = function () {
            var o = {};
            var a = this.serializeArray();
            $.each(a, function () {
                if (o[this.name] !== undefined) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
            return o;
        };

    },
};

function resourcevalidate() {
	 
	var resorceGroupName = $('#create-resource-group input[name="resource_group_name"]').val();
	var alpha = /^[@#$%&*\s-, ]+$/;  
	 if(resorceGroupName == "")
	  {
         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter ResourceGroup Name');
    	 $('#create-resource-group input[name="resource_group_name"]').focus() ;
	   return false;
	  }
	 else if(resorceGroupName.match(alpha))
	 {
		 $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter Proper ResourceGroup Name');
	     $('#create-resource input[name="offer_group_name"]').focus() ;
	     return true;
	 }	
	 else if($('#create-resource-group #nonassigned-fields-list option').length == 0)
	  {
		 var resourceGroupName = $('#create-resource-group input[name="resource_group_name"]').val();
         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Add atleast One Resource to :' + resourceGroupName);
	   return false;
	  }else if($('#resource-group-menu').hasClass('active')){
		  // check uniqueness of resource group on click of save button
			  var resourceGroupName = $('#create-resource-group input[name="resource_group_name"]').val();
              $.ajax({
 	             url: '/sef-catalog-ui/isResourceGroupExists?resourceGroupName=' + resourceGroupName,
 	             type: "GET",
 	             contentType: 'application/json',
 	             success: function (data) {
 	            	 if(data.status == 'success'){
	                    $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
	                    // save resource group to ccm file
	                    var resourceGroupName = $('#create-resource-group input[name="resource_group_name"]').val();
	                    var url = '/sef-catalog-ui/setResourceGroup?resourceGroupName=' + resourceGroupName;
	                    $('#create-resource-group #nonassigned-fields-list option').each(function(){
	                    	url = url + '&resourceName=' + $(this).val();
	                    });
	                     $.ajax({
	                            url: url,
	                            type: "GET",
	                            contentType: 'application/json',
	                            success: function (data) {
	                               if(data.status == 'success'){
	                             $('#content-resource-group').hide();
	                             $('#save-btn').hide();
	                             $('#save-btn-res').hide();
	                             $('#cancel-btn').hide();
	                             $('#cancel-btn-ser').hide();
	                             ResourceManagerHandler.loadResourceGroups();
	                             return  $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
	                             }else{
	                                 return  $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
	                               }
	                            }
	                        });
 	            	 }else{
	                    $('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
 	            	 }
 	             }
 	         });
	  }
	}

	function offervalidate(){
		var offerGroupName = $('#create-resource input[name="offer_group_name"]').val();
	//alert(resorceGroupName);
	var alpha = /^[@#$%&*\s-, ]+$/;  
	 if(offerGroupName == "")
	 {
         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter OfferGroup Name');
	     $('#create-resource input[name="offer_group_name"]').focus() ;
	  return false;
	  }
	 else if(offerGroupName.match(alpha))
	 {
		 $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter Proper OfferGroup Name');
	     $('#create-resource input[name="offer_group_name"]').focus() ;
	     return true;
	 }	 
		 
	 else if($('#create-resource #nonassigned-fields-list option').length == 0)
	  {
		 var offerGroupName = $('#create-resource input[name="offer_group_name"]').val();
         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Assign atleast One Offer to :' + offerGroupName);
	   return true;
	  }else if($('#offer-group-menu').hasClass('active')){
		// check uniqueness of offer group name in offerGroupStore.ccm file
        	var offerGroupName = $('#create-resource input[name="offer_group_name"]').val();
            $.ajax({
	             url: '/sef-catalog-ui/isOfferGroupExists?offerGroupName=' + offerGroupName,
	             type: "GET",
	             contentType: 'application/json',
	             success: function (data) {
	            	 if(data.status == 'success'){
                       $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
                       
                       var offerGroupName = $('#create-resource input[name="offer_group_name"]').val();
                       var url = '/sef-catalog-ui/setOfferGroup?offerGroupName=' + offerGroupName;
                       $('#create-resource #nonassigned-fields-list option').each(function(){
                        url = url + '&offerName=' + $(this).val();
                       });
                        $.ajax({
                               url:url ,
                               type: "POST",
                               contentType: 'application/json',
                               success: function (data) {
                               	 if(data.status == 'success'){
                                        $('#content-catalog').hide();
                                  		 $('#save-btn').hide();
                                  		 $('#save-btn-res').hide();
                                  		 $('#cancel-btn').hide();
                                  		 $('#cancel-btn-ser').hide();
                                       	 OfferGroupHandler.loadOfferCatalogGroup();
                                        return  $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
                                       }else{
                                    	   return  $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
                                       }
                               }
                           });  
	            	 }else{
                   	   // $('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
	            	 }
	             }
	         });
	  }
	}
	
	

//owner validate
	
	
	
	function ownervalidate(){
		json={};
		 var dropdown = document.getElementById("owner");
		 if($('#content-group input[name="ownerName"]').val() == '')
		 {
	         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter Owner Name');
		     $('#create-resource input[name="ownerName"]').focus() ;
		  return false;
		  }
		 else if(dropdown.selectedIndex==0){
	         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Select Owner Type');
			 return false;
			/* 
			 if($( "#owner option:selected" ).text()== "OpcoGroup")
				 {
				 if($('#content-group input[name="opconame"]').val() == ''){
					 
					 $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter Opco Name');
				 }
				 
				 
				 
				 }
			 if($( "#owner option:selected" ).text()== "Opco")
				 {
				if($('#content-group input[name="partnerName5"]').val() == '')
				 
				 $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter partner Name');
				    
				 }*/
		 }
		 /*else if($('#content-group input[name="opconame"]').val() == ''|| $('#content-group input[name="tenantName"]').val() == '')
		 {
	         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter opconame');
		     $('#create-resource input[name="opconame"]').focus() ;
		  return false;
		  }
		 else if($('#content-group input[name="partnername"]').val() == '')
		 {
		         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter partnername');
			     $('#create-resource input[name="partnername"]').focus() ;
			  return false;
		  }*/
		 else if($('#owner-group-menu').hasClass('active')){
			  var ownerName = $('#content-group input[name="ownerName"]').val();
			  json.name=ownerName;
			  var ownerType = $( "#owner option:selected" ).text();
			  json.ownerType=ownerType;
			  var opcoName = $('#content-group input[name="opconame"]').val();
			  json.opcoName=opcoName;
			  var marketName = $( "#market option:selected" ).text();
			  json.marketName=marketName;
			  var tenantName = $('#content-group input[name="tenantName"]').val();
			  json.tenantName=tenantName;
			  var partnerName = $('#content-group input[name="partnerName"]').val();
			  json.partnerName=partnerName;
			  var partnerName1 = $('#content-group input[name="partnerName1"]').val();
			  json.partnerName1=partnerName1;
			  var tenantName1 = $('#content-group input[name="tenantName1"]').val();
			  json.tenantName1=tenantName1;
			  var partnerName2 = $('#content-group input[name="partnerName1"]').val();
			  json.partnerName2=partnerName2;
			  var partnerName3 = $('#content-group input[name="partnerName3"]').val();
			  json.partnerName3=partnerName3;
			  var partnerName4 = $('#content-group input[name="partnerName4"]').val();
			  json.partnerName4=partnerName4;
			  var tenantName2 = $('#content-group input[name="tenantName2"]').val();
			  json.tenantName2=tenantName2;
			  var tenantName3 = $('#content-group input[name="tenantName3"]').val();
			  json.tenantName3=tenantName3;
			  var partnerName5 = $('#content-group input[name="partnerName5"]').val();
			  json.partnerName5=partnerName5;
			  var partnerName6 = $('#content-group input[name="partnerName6"]').val();
			  json.partnerName6=partnerName6;
			  var partnerName7 = $('#content-group input[name="partnerName7"]').val();
			  json.partnerName7=partnerName7;
			  var partnerName8 = $('#content-group input[name="partnerName8"]').val();
			  json.partnerName8=partnerName8;
			  $.ajax({
                  url: '/sef-catalog-ui/createOwner',
                  type: "POST",
                  contentType: 'application/json',
                  data: JSON.stringify(json),
                  success: function (data) {
                      if(data.status == 'success'){
                      	//alert(data.message);
                      	OwnerGroupHandler.loadOwnerGroups();
                      	$('#error_message').show();$('#myform_succloc').css('color', 'green').text("Owner Created Successfully");
                      	$("#content-group").hide();
                      }else{
                      	//alert(data.message);
                      }
                  }
              });
			  return  $('#error_message').show();$('#myform_succloc').css('color', 'green').text('Owner Group Created Successfully');
		  }
	    // return  $('#error_message').show();$('#myform_succloc').css('color', 'green').text('Owner Group Created Successfully');
		  
		}
		
	// create notification validation
	
	function notificationvalidate() {
		var messageRowCount = $('table tbody#msgList tr').length;
		
		var notificationAction =  $( "#notifcation-action option:selected" ).text();
		 if($('#notification-add input[name="eventId"]').val() == '')
		  {
	         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter Event Id');
	    	 $('#notification-add input[name="eventId"]').focus() ;
		   return false;
		  }else if($('#notification-add input[name="description"]').val() == ''){
		         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter event description');
		    	 $('#notification-add input[name="description"]').focus() ;
		    	 return false;
		  }
		  else if($('#notification-add input[name="sender_address"]').val() == ''){
		         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter Sender Address');
		    	 $('#notification-add input[name="sender_address"]').focus() ;
		    	 return false;
		  }
		  else if($('#notification-add input[name="charge"]').prop('checked') == true && $('#notification-add input[name="chargeAmount"]').val() == ''){
		         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter Charge Amount');
		    	 $('#notification-add input[name="chargeAmount"]').focus() ;
		    	 return false;
		  }
		 else if(messageRowCount <= 0 && notificationAction == 'ASIS'){
			         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Atleast one message added for :'+ notificationAction);
			    	 $('#notification-add input[name="sender_address"]').focus() ;
			    	 return false; 
		  }else if(messageRowCount <= 0 && notificationAction == 'MASSAGE'){
		         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Atleast one message added for :'+ notificationAction);
		    	 $('#notification-add input[name="sender_address"]').focus() ;
		    	 return false; 
	     }
		  else if($('#notification-menu').hasClass('active')){
			// check uniqueness of notification event id in external-notification.ccem file
                var eventId = $('#notification-add input[name="eventId"]').val();
                $.ajax({
   	             url: '/sef-catalog-ui/isNotificationExists?eventId=' + eventId,
   	             type: "GET",
   	             contentType: 'application/json',
   	             success: function (data) {
   	            	 if(data.status == 'success'){
	                  $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
	      	          var notificationEventId = $('#notification-add input[name="eventId"]').val();
	    	          var notificationDescription = $('#notification-add input[name="description"]').val();
	    	          var notificationSenderAddress = $('#notification-add input[name="sender_address"]').val();
	    	          var chargeAmount = $('#notification-add input[name="chargeAmount"]').val();
	    	          var chargeAmountCurrency = $( "#currency option:selected" ).text();
	    	          var notificationAction =  $( "#notifcation-action option:selected" ).text();
	    	          var wsClientId =  $( "#wsClientId option:selected" ).text();
	    	          var url = '/sef-catalog-ui/createNotification?notificationEventId='+notificationEventId+"&notificationDescription="+notificationDescription+"&notificationSenderAddress="+notificationSenderAddress+"&chargeAmount="+chargeAmount+"&chargeAmountCurrency="+chargeAmountCurrency+"&notificationAction="+notificationAction+"&wsClientId="+wsClientId;
	    	          var rowCount = $('table tbody#msgList').length;
	    	          //var messageArr = [];
	    	          var messageIdCount = 1;
	    	          var messagesString = "";
	    	          $("table tbody#msgList").find("tr").each(function(index) {
	    	      	      if ($('table tbody#msgList tr').length > 0){
	    	      	    	var messageId = messageIdCount;
	    	      	    	var message = $(this).find("td").first().text();
	    	      	        var language = $(this).find("td").first().next().text();
	    	      	        //messageArr.push(messageId+","+message+","+language);
	    	      	        messagesString += messageId+"::"+message+"::"+language+"@";
 	        		        messageIdCount++;
	    	      	      }
	    	      	    });
	    	           url = url + "&messages="+ messagesString;
	    	           $.ajax({
	    	                  url: url,
	    	                  type: "GET",
	    	                  //contentType: 'application/json',
	    	                  success: function (data) {
	    	                   if(data.status == 'success'){
	    	                   $('#notification_create_content').hide();
	    	                   $('#save-btn').hide();
	    	                   $('#save-btn-res').hide();
	    	                   $('#cancel-btn').hide();
	    	                   $('#cancel-btn-ser').hide();
	    	                   NotificationHandler.loadNotifications();
	    	                   $('#notification_create_content').hide();
	    	                   $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
	    	                     }else{
	    	                    	 NotificationHandler.loadNotifications();
	    	                    	 $('#notification_create_content').hide();
	    	                    	 $('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
	    	                     }
	    	                  }
	    	              });   
   	            	 }else{
	                    $('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
   	            	 }
   	             }
   	         });
		  }
		}
	
	// validation for notification update

	function notificationUpdateValidate() {
		var messageRowCount = $('table tbody#msgList tr').length;
		var notificationAction =  $( "#notifcation-action option:selected" ).text();
		 if($('#notification-add input[name="eventId"]').val() == '')
		  {
	         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter Event Id');
	    	 $('#notification-add input[name="eventId"]').focus() ;
		   return false;
		  }else if($('#notification-add input[name="description"]').val() == ''){
		         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter event description');
		    	 $('#notification-add input[name="description"]').focus() ;
		    	 return false;
		  }
		  else if($('#notification-add input[name="sender_address"]').val() == ''){
		         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter Sender Address');
		    	 $('#notification-add input[name="sender_address"]').focus() ;
		    	 return false;
		  }
		  else if($('#notification-add input[name="charge"]').prop('checked') == true && $('#notification-add input[name="chargeAmount"]').val() == ''){
		         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Please Enter Charge Amount');
		    	 $('#notification-add input[name="chargeAmount"]').focus() ;
		    	 return false;
		  }
		 
		 else if(messageRowCount <= 0 && notificationAction == 'ASIS'){
			         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Atleast one message added for :'+ notificationAction);
			    	 $('#notification-add input[name="sender_address"]').focus() ;
			    	 return false; 
		  }else if(messageRowCount <= 0 && notificationAction == 'MASSAGE'){
		         $('#error_message').show();$('#myform_succloc').css('color', 'red').text('Atleast one message added for :'+ notificationAction);
		    	 $('#notification-add input[name="sender_address"]').focus() ;
		    	 return false; 
	     }
		  else if($('#notification-menu').hasClass('active')){
			// check uniqueness of notification event id in external-notification.ccem file
               var eventId = $('#notification-add input[name="eventId"]').val();
                /*$.ajax({
   	             url: '/sef-catalog-ui/isNotificationExists?eventId=' + eventId,
   	             type: "GET",
   	             contentType: 'application/json',
   	             success: function (data) {
   	            	 if(data.status == 'success'){
	                    $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);*/
	      	              var notificationEventId = $('#notification-add input[name="eventId"]').val();
	        	          var notificationDescription = $('#notification-add input[name="description"]').val();
	        	          var notificationSenderAddress = $('#notification-add input[name="sender_address"]').val();
	        	          var chargeAmount = $('#notification-add input[name="chargeAmount"]').val();
	        	          var chargeAmountCurrency = $( "#currency option:selected" ).text();
	        	          var notificationAction =  $( "#notifcation-action option:selected" ).text();
	        	          var wsClientId =  $( "#wsClientId option:selected" ).text();
	        	          var url = '/sef-catalog-ui/updateNotification?notificationEventId='+notificationEventId+"&notificationDescription="+notificationDescription+"&notificationSenderAddress="+notificationSenderAddress+"&chargeAmount="+chargeAmount+"&chargeAmountCurrency="+chargeAmountCurrency+"&notificationAction="+notificationAction+"&wsClientId="+wsClientId;
	        	          var rowCount = $('table tbody#msgList').length;
	        	          var messageIdCount = 1;
	        	          var messagesString = "";
	        	          $("table tbody#msgList").find("tr").each(function(index) {
	        	      	      if ($('table tbody#msgList tr').length > 0){
	        	      	    	var messageId = messageIdCount;
	        	      	    	var message = $(this).find("td").first().text();
	        	      	        var language = $(this).find("td").first().next().text();
	        	      	        messagesString += messageId+"::"+message+"::"+language+"@";
	             		        messageIdCount++;
	        	      	      }
	        	      	    });
	        	           url = url + "&messages="+ messagesString;
	        	           $.ajax({
	        	                  url: url,
	        	                  type: "GET",
	        	                  success: function (data) {
	        	                   if(data.status == 'success'){
	        	                   $('#notification_create_content').hide();
	        	                   $('#save-btn').hide();
	        	                   $('#save-btn-res').hide();
	        	                   $('#cancel-btn').hide();
	        	                   $('#cancel-btn-ser').hide();
	        	                   $('#update-btn').hide();
	        	                   NotificationHandler.loadNotifications();
	        	                   $('#notification_create_content').hide();
	        	                   $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
	        	                     }else{
	        	                    	 NotificationHandler.loadNotifications();
	        	                    	 $('#notification_create_content').hide();
	        	                    	 $('#update-btn').hide();
	        	                    	 $('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
	        	                     }
	        	                  }
	        	              });
   	            	 /*}else{
	                    $('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
   	            	 }
   	             }
   	         });*/
		  }
		}


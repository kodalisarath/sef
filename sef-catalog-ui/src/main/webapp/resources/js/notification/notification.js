var NotificationHandler = {
		loadNotifications: function(){
			$('#rule-btn').hide();   
			$('#ws-search').hide();
			$("#resourcegroup-search").hide();	
			$("#offergroup-search").hide();		
			$("#offer-search").hide();
			$("#content-service").hide();			
			$("#resource-search").hide();
			$("#privileges-search").hide();
			$("#rule_search").hide();
			$('#middle_rule').hide();
			$("#rule_search").hide();
			$("#charge_amount").hide();
			$("#select-policy-rule").hide();
			$("#select-owner-group").hide();
			$("#select-offer-group").hide();
			$("#select-product-owner").hide();
			$('#select-product-resource').hide();
			$('#privileges-content').hide();
			$("#ui-btn124").hide();
			$('#addn-btn').show();
			$('#middle8').hide();
			$('#middle7').show();
			$('#notification_search').show();
			$('#middle').hide();
		    $('#middle2').hide();
		    $('#middle3').hide();
		    $('#middle5').hide();
			/*$(".delete-link").hide();
			$(".edit-link").hide();*/
			$("#ui-btn").hide();
			$("#ui-btn2").hide();
			$('#ui-btn1').hide();
			$('#ui-btn-ocg').hide();
			$('#ui-btn-og').hide();
			$('#ui-btn12').hide();
			$("#ui-btn123").hide();
			$('.ui-btn-user').hide();
			$('#content-right').hide();
			$('#content-right1').hide();
			$('#privileges-assigned-fields-list').hide();
  	  		$('#privileges-nonassigned-fields-list').hide();
  	  		$('#notication-add').hide();
  	  		$('#notification-edit').hide();
  	  		$('#editn-btn').hide();
  	  		$('.merge-btns').hide();
  	  	    $('#content-user-mgt').hide();
		   
		    $("#save-btn-notif").hide();
		    $("#cancel-btn-notif").hide();
		    $("#content-resource-group").hide();
		    $("#offer-catalog-group-service").hide();
		    $("#content-group-mgt").hide();
		    
		    
		    //Bug fix: 0000931: Notification management:=Existing Notification Page is not getting refreshed , if we Click on NM again
    		
		    $("#notification-add").hide();
    		$("#save-btn-notif").hide();
    		$("#cancel-btn-notif").hide();
    	    $("#update-btn-notif").hide();
    	    
    	    // clear all the form fields including all the rows from table
    	    $('INPUT:text, INPUT:password, INPUT:file, SELECT, TEXTAREA, TABLE', '#notification-add').val('');
    	    $('table tbody#msgList tr').remove(); 
    	    $('#notification-add').find('input:radio, input:checkbox')
            .removeAttr('checked').removeAttr('selected');
            $("#charge_amount").hide();
            $('#client').hide();
		    
		    
            var grid = $("#notificationEventGrid").data("kendoGrid");
		    if(grid != undefined){
			    grid.destroy();
		    }
		    
		    // display notification events in kendo-ui data tables grid
		    $.ajax({
	            url: '/sef-catalog-ui/getAllNotifications',
	            type: "GET",
	            async:false,
	            contentType: 'application/json',
	            success: function (data) {
	                    dataSource = new kendo.data.DataSource({
	                    	data : data,
	                        batch: true,
	                        pageSize: 15,
	                                           
	                    });

	                $("#notificationEventGrid").kendoGrid({
	                    dataSource: dataSource,
	                    navigatable: true,
	                    pageable: true,
	                     filterable: {
	                        extra: false,
	                        operators: {
	                            string: {
	                                startswith: "Starts with",
	                                eq: "Is equal to",
	                                neq: "Is not equal to"
	                            }
	                        }
	                    },
	                    height: 500,
	                    columns: [ { title: "Event Id", field: "eventId", width: "100px" }, 
	                               { title: "Description", field: "description"},
	                               { title: "Workflow Id", field: "wsClientId" },
	                               { title: "Notification Action", field: "notificationAction" },
	                               { title: "Charge Amount", field: "chargeAmount" },
	                               { title: "Sender Addr", field: "senderAddr" }]
	                });
	            }
	        });
		    
		    $.ajax({
	            url: '/sef-catalog-ui/getNotificationWorkflows',
	            type: "GET",
	            async:false,
	            contentType: 'application/json',
	            success: function (data) {
	            	$('#wsClientId').html('');
	            	var length = data.length;
	            	if(length > 0){
	            		$('#wsClientId').append('<option value="select">Select</option>');
	            		for(var i = 0; i < length; i++){
	            			//$('#wsClientId').append('<option value="' + data[i] + '">' + data[i].substring(0, 1).toUpperCase() + data[i].substring(1) + '</option>');
	            			$('#wsClientId').append('<option value="' + data[i] + '">' + data[i] + '</option>');
	            		}
	            	}
	            }
	        });

  /*  var notificationLength = "";   
   	$.ajax({
            url: '/sef-catalog-ui/getAllNotifications',
            type: "GET",
            async:false,
            contentType: 'application/json',
            success: function (data) {
            	alert(data.length);
            	notificationLength = data.length;
            	//alert(data.length);
            	if(data.status == 'success'){
            	if(data != undefined){
            		for (i = 0; i < data.length; i++) { 
            			   var newTableRow = $("<tr id="+ i +" class=context-menu-one" + i + ">");
                 		   newTableRow.append('<td style="word-wrap: break-word; width: 84px;height: auto;">' + data[i].eventId + '</td>');
                 		   if(data[i].description == null){
                 			   newTableRow.append('<td>' + "" + '</td>');
                 		   }else{
                 			   newTableRow.append('<td style="word-wrap: break-word; width: 52px;height: auto;">' + data[i].description + '</td>');
                 		   }
                 		   if(data[i].wsClientId == null){
                 			   newTableRow.append('<td>' + "" + '</td>');
                 		   }else{
                 			   newTableRow.append('<td style="word-wrap: break-word; width: 40px;height: auto;">' + data[i].wsClientId + '</td>');
                 		   }
              		       newTableRow.append('<td style="word-wrap: break-word; width: 40px;height: auto;">' + data[i].notificationAction + '</td>');
              		       newTableRow.append('<td style="word-wrap: break-word; width: 15px;height: auto;">' + data[i].chargeAmount + '</td>');
                 		   newTableRow.append('<td style="word-wrap: break-word; width: 30px;height: auto;">' + data[i].senderAddr + '</td>');
                 		   newTableRow.append('</tr>');
                 		   newTableRow.appendTo("#notifications");
                 	}
            	}else{
            		
            	}
              $('.overlay-bg').hide();
            }
        });*/
		    
	        $('#save-btn-notif').unbind('click').click(function () {
	        	if ($('#notification-menu').hasClass('active')) {
		            	if(!notificationvalidate($('#notification_create_content'))) {
		                    return;
		                }
	            }
	        });
	        
	        // updating notification event
	        $(document).off('click', "#update-btn-notif").on("click", "#update-btn-notif", function (e) {
                // then display existing event data in notification form
	        	if ($('#notification-menu').hasClass('active')) {
	            	if(!notificationUpdateValidate($('#notification_create_content'))) {
	                    return;
	                }
                }
	        });
	        
	     // When Add Notification button  click
	        $(document).ready(function () {
	            $('#addn-btn').unbind('click').click(function () {
	            	$("#notification-add").show();
	            	$('#notification_create_content').show();
	                $("#content-service").hide();
	                $("#content-right").hide();
	                $("#cancel-btn-notif").show();
	                $("#save-btn-notif").show();
	                $("#update-btn-notif").hide();
	                
	         
	                
	                $('INPUT:text, INPUT:password, INPUT:file, SELECT, TEXTAREA, TABLE', '#notification-add').val('');
	        	    $('table tbody#msgList tr').remove(); 
	                $('#notification-add').find('input:radio, input:checkbox')
                    .removeAttr('checked').removeAttr('selected');
	                $('#client').hide();
	                
	             // check uniqueness of notification event id in external-notification.ccem file
	                 $("#eventId").blur(function() {
	                   if($('#notification-add input[name="eventId"]').val() !== ''){
	                   var eventId = $('#eventId').val();
	                   $.ajax({
	      	             url: '/sef-catalog-ui/isNotificationExists?eventId=' + eventId,
	      	             type: "GET",
	      	             contentType: 'application/json',
	      	             success: function (data) {
	      	            	 if(data.status == 'success'){
	  	                        $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
	      	            	 }else{
	  	                    	$('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
	      	            	 }
	      	             }
	      	         });
	               }
	                 });
	            });
	            
	            $('.close-btn').unbind('click').click(function(){
	    		      $('.overlay-bg').hide(); // hide the overlay
	    		      $('#notification-add').show();
	    		  });
	            
	        });
	        
	        $('#cancel-btn-notif').unbind('click').click(function () {
	        	if ($('#notification-menu').hasClass('active')) {
	        		//NotificationHandler.loadNotifications();
	        		$("#notification-add").hide();
	        		$("#save-btn-notif").hide();
	        		$("#cancel-btn-notif").hide();
	        	    $("#update-btn-notif").hide();
	        	   // $("#msgList").hide();
	        	    
	        	    // clear all the form fields including all the rows from table
	        	    $('INPUT:text, INPUT:password, INPUT:file, SELECT, TEXTAREA, TABLE', '#notification-add').val('');
	        	    $('table tbody#msgList tr').remove(); 
	        	    $('#notification-add').find('input:radio, input:checkbox')
                    .removeAttr('checked').removeAttr('selected');
                    $("#charge_amount").hide();
                    $('#client').hide();
	        	}
	       });
	        
	        
	        // added function for right click on event notification in middle pane
           /*	        $(function(){
	        	for(i = 0; i < notificationLength; i++){
		        $.contextMenu({
		        	selector: '.context-menu-one' + i,  
		        	callback: function(key, options) {
		                if(key == 'edit'){
		                	var eventId = $(options.selector + ' td').first().text();
		                    var event = {};
		                    $.ajax({
			       	             url: '/sef-catalog-ui/getNotificationEventDetails?eventId=' + eventId,
			       	             type: "GET",
			       	             contentType: 'application/json',
			       	             success: function (data) {
			       	            	alert(data.eventId);
			       	            	$('#notification-add input[name="eventId"]').val(data.eventId);
			       	            	$('#notification-add input[name="description"]').val(data.description);
			       	            	$('#notification-add input[name="sender_address"]').val(data.senderAddr);
			       	            	$('#notification_create_content').show();
			       	            	$('#notification-add').show();
				                    $('#add-notification').hide();
				                    $("#save-btn").hide();
				                    $("#cancel-btn").show();
				                    $("#delete-btn").hide();
				                    $("#update-btn").show();
		                        }
		                    });
		                }else if(key == 'delete'){
		                	// dialog box for notification manager delete confirmation
		                	var result = window.confirm('Are you sure want to delete event ?');
		    	            if (result == false) {
		    	                e.preventDefault();
		    	            }else{
		                	var eventId = $(options.selector + ' td').first().text();
		                    $.ajax({
		       	             url: '/sef-catalog-ui/deleteNotification?eventId=' + eventId,
		       	             type: "GET",
		       	             contentType: 'application/json',
		       	             success: function (data) {
		       	            	 if(data.status == 'success'){
		       	            		NotificationHandler.loadNotifications();
		       	            		$('#right1').html('<div id="right1_errorloc" style="color:#ff0000;float:left; width:100%; display:none;">gdfgdfgdf</div>');
			  	                    $('#right1_errorloc').show().text(data.message);
		       	            	 }else{
		       	            		NotificationHandler.loadNotifications();
		       	            		$('#right1').html('<div id="right1_errorloc" style="color:#ff0000;float:left; width:100%; display:none;"></div>');
	    		                    $('#right1_errorloc').show().text(data.message);
		       	            	 }
		       	             }
		                    });
		    	           }
		                }else if(key == 'details'){
		                	var eventId = $(options.selector + ' td').first().text();
		                	 $.ajax({
			       	             url: '/sef-catalog-ui/getNotificationEventDetails?eventId=' + eventId,
			       	             type: "GET",
			       	             contentType: 'application/json',
			       	             success: function (data) {
			       	            	 alert("eventId:" + data.eventId);
			       	            	 alert("Description:" + data.description);
			       	            	 alert("Sender Address :" + data.senderAddr);
			       	            	 alert("Charge Amount :" + data.chargeAmount);
			       	            	 alert("Notification Action :" + data.notificationAction);
			       	            	 var messageDetail = data.notificationMessages;
			       	            	 alert("Message Size:" + messageDetail.length);
			       	             }
			                    });
		                }
		            },

		            items: {
		                "edit": {name: "Edit", icon: "edit"},
		                "delete": {name: "Delete", icon: "delete"},
		                "details": {name: "Details", icon: "details"},
		           }
		        });
		        
		        $('.context-menu-one').on('click', function(e){
		            console.log('clicked', this);
		        })
	        }// for loop
		 });*/ 
	        
	        
// added function for right click on event notification in middle pane
	        
	        
	       $(document).off('click', '.k-state-focused').on('click', '.k-state-focused', function(e){
	    	   e.preventDefault();
	    	   rightClick();
		    });
	       
	       function rightClick(){
	    	    $('#notificationEventGrid tr ').each(function(){
	    	    	
       		$.contextMenu({
		            selector: '[data-uid="' + $(this).attr('data-uid') + '"]', 
		            callback: function(key, options) {
		                if(key == 'edit'){
		                	// clear all the fields of previous form
		                	$('INPUT:text, INPUT:password, INPUT:file, SELECT, TEXTAREA, TABLE', '#notification-add').val('');
		            	    $('table tbody#msgList tr').remove(); 
		            	    $('#notification-add').find('input:radio, input:checkbox')
		                    .removeAttr('checked').removeAttr('selected');
		                    $("#charge_amount").hide();
		                    $('#client').hide();
		                	//alert($(options.selector + ' td').first().text());
		                    
		                   
		                    
		                	var eventId = $(options.selector + ' td').first().text();
		                    var event = {};
		                    $.ajax({
			       	             url: '/sef-catalog-ui/getNotificationEventDetails?eventId=' + eventId,
			       	             type: "GET",
			       	             contentType: 'application/json',
			       	             success: function (data) {
			       	            	 var notificationWorkflow = data.wsClientId;
			       	            	
			       	            	$('#notification-add input[name="eventId"]').val(data.eventId);
			       	            	$('#notification-add input[name="description"]').val(data.description);
			       	            	$('#notification-add input[name="sender_address"]').val(data.senderAddr);
			       	            	if(data.chargeAmount > 0){
			       	            		$('#charge_amount_currency').prop('checked', true);
			       	            		$('#charge_amount').toggle();
			       	            		$('#notification-add input[name="chargeAmount"]').val(data.chargeAmount);
			       	            		$("#currency option[value=" + data.currencyCode +"]").attr("selected","selected") ;
			       	            	}else{
			       	            		$('#charge_amount_currency').prop('checked', false);
			       	            	}
			       	            	$("#notifcation-action option[value=" + data.notificationAction +"]").attr("selected","selected") ;
			       	            	// if notification action is workflow, then display workflowId with respective value
			       	            	//alert(data.wsClientId);
			       	            	
			       	            	// READ ALL WORKFLOWS FROM BEANS.XML, and displaying existing workflow as selected in dropdown
			       	            	
			       	            	if(data.notificationAction == 'WORKFLOW'){
			       	            		$("#client").show();
			       	            		//$("#wsClientId option[value=" + data.wsClientId.substring(0, 1).toUpperCase() + data.wsClientId.substring(1) +"]").attr("selected","selected") ;
			       	            		//load all workflows in dropdown accept above workflows from beans.xml
			       	            		
			       	            		$.ajax({
			       	     	            url: '/sef-catalog-ui/getNotificationWorkflows',
			       	     	            type: "GET",
			       	     	            async:false,
			       	     	            contentType: 'application/json',
			       	     	            success: function (data) {
			       	     	            	$('#wsClientId').html('');
			       	     	            	var length = data.length;
			       	     	            	if(length > 0){
			       	     	            		//$('#wsClientId').append('<option value="select">Select</option>');
			       	     	            		for(var i = 0; i < length; i++){
			       	     	            			if(notificationWorkflow == data[i]){
			       	     	            			// don't maintain duplicate worflows in drop-down
			       	     	            			//$("#wsClientId option[value=" + data.wsClientId +"]").attr("selected","selected") ;
			       	     	            			$('#wsClientId').append('<option value="' + data[i] + '">' + data[i] + '</option>');
			       	     	            		    $("#wsClientId option[value=" + data[i] +"]").attr("selected","selected") ;
			       	     	            			}else{
			       	     	            			//$('#wsClientId').append('<option value="' + data[i] + '">' + data[i].substring(0, 1).toUpperCase() + data[i].substring(1) + '</option>');
			       	     	            		    $('#wsClientId').append('<option value="' + data[i] + '">' + data[i] +'</option>');
			       	     	            			}
			       	     	            		}
			       	     	            	}
			       	     	            }
			       	     	        });
			       	            	
			       	            	}
			       	            	
			       	            	
			       	            	 var messageDetail = data.notificationMessages;
			       	            	 for(i = 0; i< messageDetail.length; i++){
			       	            		var newTableRow = $("<tr>");
					       	     		newTableRow.append('<td class="row1">' + messageDetail[i].message + '</td>');
					       	     		newTableRow.append('<td class="row2">' + messageDetail[i].language + '</td>');
					       	     	newTableRow.append('<td class="row3"><img src="images/edit.png" class="show-popup-edit" title="Edit" /><img src="images/delete-product.png" class="deletemessage" title="Delete" /></td>');
					       	     		newTableRow.append('</tr>');
					       	     	    newTableRow.appendTo("table tbody#msgList");
			       	            	 }
			       	           // check uniqueness of notification event id in external-notification.ccem file
			    	                 $("#eventId").blur(function() {
			    	                   if($('#notification-add input[name="eventId"]').val() !== ''){
			    	                   var eventId = $('#eventId').val();
			    	                   $.ajax({
			    	      	             url: '/sef-catalog-ui/isNotificationExists?eventId=' + eventId,
			    	      	             type: "GET",
			    	      	             contentType: 'application/json',
			    	      	             success: function (data) {
			    	      	            	 if(data.status == 'success'){
			    	  	                        $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
			    	      	            	 }else{
			    	  	                    	$('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
			    	      	            	 }
			    	      	             }
			    	      	         });
			    	               }
			    	                 });
			    	                 
			    	               
			    	                 
			       	            	$('#notification_create_content').show();
			       	            	$('#notification-add').show();
			       	            	$('#notification-details').hide();
				                    $('#add-notification').hide();
				                    $("#save-btn-notif").hide();
				                    $("#cancel-btn-notif").show();
				                    $("#delete-btn-notif").hide();
				                    $("#update-btn-notif").show();
		                        }
		                    });
		                	
		                    $('.close-btn').unbind('click').click(function(){
				    		      $('.overlay-bg').hide(); // hide the overlay
				    		      $('#notification-add').show();
				    		  });
		                    
		                    $('#notification-add [name="eventId"]').prop('readonly', true);
		                }
		                
		                
		                else if(key == 'delete'){
		                	var docHeight = $(document).height(); //grab the height of the page
			    	        var scrollTop = $(window).scrollTop(); //grab the px value from the top of the page to where you're scrolling
		                	if($('#hasNotificationDelete').val() == 'false'){
		            			
		                		$('.overlay-bg-restriction').show().css({'height' : docHeight}); //display your popup and set height to the page height
				    	        $('.overlay-content-delete-restriction').css({'top': scrollTop+20+'px'}); //set the content 20px from the window top
				    	        $('.close-btn-restriction').unbind('click').click(function(){
					    		      $('.overlay-bg-restriction').hide(); // hide the overlay
					    		      $('#notification-add').show();
				    	        });
				    	        $('.ok-restriction').unbind('click').click(function(){
				    	        	$('.overlay-bg-restriction').hide();
				    	        });
				    	        
		            		}
		                	else{
				    	        $('.overlay-bg-delete').show().css({'height' : docHeight}); //display your popup and set height to the page height
				    	        $('.overlay-content-delete').css({'top': scrollTop+20+'px'}); //set the content 20px from the window top
				    	        var eventId = $(options.selector + ' td').first().text();
				    	        $('.ok-btn').unbind('click').click(function(){
				    	        	$.ajax({
					       	             url: '/sef-catalog-ui/deleteNotification?eventId=' + eventId,
					       	             type: "GET",
					       	             contentType: 'application/json',
					       	             success: function (data) {
					       	            	 if(data.status == 'success'){
					       	            		NotificationHandler.loadNotifications();
					       	            		$('.overlay-bg-delete').hide();
					       	            		$('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
					       	            	 }else{
					       	            		NotificationHandler.loadNotifications();
					       	            		$('.overlay-bg-delete').hide();
					       	            		$('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
					       	            	 }
					       	             }
					                    });
				    			});
				    			$('.close-btn').unbind('click').click(function(){
				    		      $('.overlay-bg').hide(); // hide the overlay
				    		      $('#notification-add').show();
				    		  });
		                		
		                	}
		                	
		                	
			    		 
			    		    // hides the popup if user clicks anywhere outside the container
			    		    $('.close-btn').unbind('click').click(function(){
			    		        $('.overlay-bg-delete').hide();
			    		    })
			    		    // hides the popup if user clicks anywhere outside the container
			    		    $('.close-btn-restriction').unbind('click').click(function(){
			    		        $('.overlay-bg-restriction').hide();
			    		    })
			    		    // prevents the overlay from closing if user clicks inside the popup overlay
			    		    $('.overlay-content-delete').unbind('click').click(function(){
			    		        return false;
			    		    });
			    		 // prevents the overlay from closing if user clicks inside the popup overlay
			    		    $('.overlay-content-delete-restriction').unbind('click').click(function(){
			    		        return false;
			    		    });
		                }else if(key == 'details'){
		                	//alert($(options.selector + ' td').first().text());
		                	$('table tbody#msgList1 tr').remove(); 
		                	$('#event_Id').text('');
		                	$('#event_description').text('');
		                	$('#charge_amount_currency_details').text("" + " " + "");
		                	$('#senderAddress').text('');
	       	            	 $('#notificationAction').text('');
		                	var eventId = $(options.selector + ' td').first().text();
		                	 $.ajax({
			       	             url: '/sef-catalog-ui/getNotificationEventDetails?eventId=' + eventId,
			       	             type: "GET",
			       	             contentType: 'application/json',
			       	             success: function (data) {
			       	            	 // display notification details in right pane in notification manager
			       	            	 $('#event_Id').text(data.eventId);
			       	            	 if(data.description!=null){
			       	            		 $('#event_description').text(data.description);
			       	            	 }else{
			       	            		$('#event_description').text("");
			       	            	 }
			       	            	 if(data.chargeAmount > 0){
			       	            		$('#charge_amount_currency_details').text(data.chargeAmount + " " + data.currencyCode);
			       	            	 }
			       	            	 $('#senderAddress').text(data.senderAddr);
			       	            	 $('#notificationAction').text(data.notificationAction);
			       	            	 var messageDetail = data.notificationMessages;
			       	            	 for(i = 0; i< messageDetail.length; i++){
			       	            		var newTableRow = $("<tr>");
					       	     		newTableRow.append('<td class="row1">' + messageDetail[i].message + '</td>');
					       	     		newTableRow.append('<td class="row2">' + messageDetail[i].language + '</td>');
					       	     	
					       	     		newTableRow.append('</tr>');
					       	     		newTableRow.appendTo("table tbody#msgList1");
			       	            	 }
			       	            	 $('#notification-details').show();
			       	            	 $('#add-notification').hide();
			       	            	 //$('#details_notification').show();
			       	            	$('#notification_create_content').hide();
			       	            	$('#notification-add').hide();
			       	            	$('#update-btn').hide();
			       	            	$('#cancel-btn').hide();
			       	             }
			                    });
		                	 
		                	 
		                }
		                
		            	$('.close-btn-details').unbind('click').click(function(){
			    		      $('#notification-details').hide(); // hide the overlay
			    		      $('#notification-add').hide();
			    		      $('#details_notification').hide();
			    		  });
			    		 
		                
		               
		            },
		            
		            items: {
		                "edit": {name: "Edit", icon: "edit"},
		                "delete": {name: "Delete", icon: "delete"},
		                "details": {name: "Details", icon: "details"},
		           }
		            
		        });
       		
       		
       	});
	        
	       
	       }
	       rightClick();
	       
	      /*  $('.close-btn').click(function () {
	        	if ($('#notification-menu').hasClass('active')) {
	        		$("#notification-add").hide();
	        		$('#notification-details').hide();
	        		$('#add-notification').show();
	        	}
	       });
	*/

	},

};
  	  		
			
	
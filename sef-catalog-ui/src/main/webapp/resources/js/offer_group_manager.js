//offer group
var OfferGroupHandler = {
		loadOfferCatalogGroup: function () {
				
			$.ajax({
		 		url: '/sef-catalog-ui/getAllOffers?pageNumber=1&pageSize=30',
		 		type: "GET",
				//async:false,
		  		contentType: 'application/json',
		  		success: function (data) {
		  			json = JSON.parse(data);
		  			
		  			$('#assigned1 #assigned-fields-list').html('');
		  			
					var offers = json.offers;
					var offersLength = offers.length;
					for (var k = 0; k < offersLength; k++) {
		  				$('#assigned1 #assigned-fields-list').append("<option value='" + offers[k][1].name+ "'>" + offers[k][1].name +"</option>");
					}
	 			}
			});
			$("#content-middle .search").val('');
			$('#ws-search').hide();
			$("#resourcegroup-search").hide();
			$("#offergroup-search").show();	
			$("#offer-search").hide();
			$("#resource-search").hide();
			$('#rule-search').hide();
			$("#privileges-search").hide();
			$('#middle_rule').hide();
			$("#rule_search").hide();
			$("#select-policy-rule").hide();
			$("#select-owner-group").hide();
			$("#select-offer-group").hide();
			$('#select-product-resource').hide();
			$('#middle').height('515');
			$('#notification-details').hide();
			$('#middle7').hide();
			$('#rule-btn').hide();
			$('#middle5').hide();
			$('#middle3').hide();
	    	$('#middle8').hide();
			$('#notification-add').hide();
		     $('#notification-edit').hide();
		     $('#notification_create_content').hide();
		     $('#editn-btn').hide();
		     $('#addn-btn').hide();
		     $('#notification_search').hide();
		     $('#content-user-mgt').hide();
		     $('#content-group-mgt').hide();
		     $('#privileges-content').hide();
		     $('#ui-btn124').hide();
		     $('#ui-btn123').hide();
				$('#middle').show();
		    	$('#middle2').hide();
				$('#content-group').hide();
				$('#ui-btn-ocg').show();
				$('#content-group-mgt').hide();
				$("#ui-btn2").hide();
			       $("#ui-btn-og").hide();
			       $("#cancel-btn-ser").hide();
			          $('#ui-btn').hide();
			          $('#ui-btn12').hide();
			          $('#ui-btn1').hide();
			          $('#ui-btn2').hide();
			          $(".delete-link").hide();
			          $(".edit-link").hide();
			          $('#content-right').hide();
			          $('#offer-catalog-group-service').hide();
			          $('#add-offer-catalog-group').show();
			          $('#content-resource-group').hide();
				      $('#add-resource-group').hide();
				        $('#create-owner-group').hide();
				        $('#add-owner-group').hide();
				        
				        
				       
		
				var deleteOfferCatalogGroupManager = "";
				        
			$(document).off("click", ".delete-link").on("click", ".delete-link", function (e) {
	           // e.preventDefault();
	            if($('#offer-group-menu').hasClass('active')){
	            	deleteOfferCatalogGroupManager = $(this).attr("text");
	            	//alert("delete offer group");
	            	var offerGroupName = $(this).attr("text");
	            	$('#group-desc2').val(offerGroupName);
	            	
	            	e.preventDefault(); // disable normal link function so that it doesn't refresh the page
	    	        var docHeight = $(document).height(); //grab the height of the page
	    	        var scrollTop = $(window).scrollTop(); //grab the px value from the top of the page to where you're scrolling
	    	        $('.overlay-bg-delete').show().css({'height' : docHeight}); //display your popup and set height to the page height
	    	        $('.overlay-content-delete').css({'top': scrollTop+20+'px'}); //set the content 20px from the window top
	             }  
			});
			$('.ok-btn').unbind('click').click(function(){
				$.ajax({
                    url: '/sef-catalog-ui/deleteOfferGroup?offerGroupName=' + deleteOfferCatalogGroupManager,
                    type: "GET",
                    async: false,
                    success: function (data) {
                        if(data.status == 'success'){
                        	$('.overlay-bg-delete').hide();
  	                        $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
                        	OfferGroupHandler.loadOfferCatalogGroup();
                        }else{
		                    $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
                        }
                    }
                });
	         
			});
			$('.close-btn').unbind('click').click(function(){
		        $('.overlay-bg-delete').hide(); // hide the overlay
		    });
		 
		    // hides the popup if user clicks anywhere outside the container
		    $('.overlay-bg-delete').unbind('click').click(function(){
		        $('.overlay-bg-delete').hide();
		    })
		    // prevents the overlay from closing if user clicks inside the popup overlay
		    $('.overlay-content-delete').unbind('click').click(function(){
		        return false;
		    });
		    
			$(document).off("click", ".edit-link").on("click", ".edit-link", function (e) {
	            e.preventDefault();
	            $('#create-resource input').prop('readonly',true);
	            $('#assigned-prod-group-btn').show();
	            $('#assigned-resource-grp-btn').show();
	            $('#non-assigned2 #nonassigned-fields-list').empty();
	            $('#non-assigned1 #nonassigned-fields-list').empty();
	      	  $('#assigned1 #assigned-fields-list option').show();
	      	  if ($('#offer-group-menu').hasClass('active')) {
	           	var offerGroupName = $(this).attr("text");
	           	var offers = {};
	               $.ajax({
	                   url: '/sef-catalog-ui/getOfferGroup?offerGroupName=' + $(this).attr("text"),
	                   type: "GET",
	                   async: false,
	                   success: function (data) {
	                   	offers = JSON.parse(data.responseString);
	                   }
	               });
	               $('#group-desc2').val(offerGroupName);
	               $(this).closest('li').find('ul .edit-link').each( function () {
	                   var newOption = '<option value="'+$(this).attr("text")+'" class="optionChecked">'+$(this).attr("text")+'</option>';
	                   $('#assigned1 #assigned-fields-list').find('[value="' + $(this).attr("text") + '"]').hide();
	                   $('#non-assigned1 #nonassigned-fields-list').append(newOption);
	               });
	               $("#save-btn-ocg").hide();
	               $("#cancel-btn-ocg").show();
	               $("#update-btn-ocg").hide();
	               $("#prod-grp-displays").hide();
	               $('#add-offer-catalog-group').hide();
	               $('#offer-catalog-group-service').show();
	               $('#assigned-prod-group-btn').addClass("offer_group_edit");
	               $('#assign-deassign-offer').addClass("offer_assign_deassign");
	           }
	               
			});
			 $('#cancel-btn-ocg').unbind('click').click(function () {
				 if ($('#offer-group-menu').hasClass('active')) {
		        		OfferGroupHandler.loadOfferCatalogGroup();
		        	    $("#group-desc2").val('');
		        		$('#non-assigned1 #nonassigned-fields-list').empty();
		        	}
		
			 });
			 $('#save-btn-ocg').unbind('click').click(function () {
				 if ($('#offer-group-menu').hasClass('active')) {
		              if(!offervalidate($('#create-resource'))) {
		                 return;
		             }
		         	
		         }
				 
			 });
	    
	      $('#img-right1').unbind('click').click(function(){
	  	    if(! $('#save-btn-ocg').is(':visible')){
	      			 
	      			 var selected=$( "#assigned1 #assigned-fields-list option:selected" );
	      		     //selected.remove();
	      		     var selectedOffer = selected.val();
	      		     //selected.remove();
	      		     if(selectedOffer === null || selectedOffer === undefined || selectedOffer === ''){
		      			 
		      			   return;
	   			         }
	      		   
	      		     $('#assigned1 #assigned-fields-list option:selected').each(function (e) {
	   	  	    	 $('#non-assigned1 #nonassigned-fields-list').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');

	                 });
	      		  
	                 $('#assigned1 #assigned-fields-list option:selected').remove();
	      		        // while editing offer group, user may assign new offer to offer group
	      		        var offerGroupName = $('#create-resource input[name="offer_group_name"]').val();
	  			        //alert(offerGroupName);
	  			        //alert(selectedOffer);
	  			        var setOfferPriority = 0;
	  			        var offerPriority;
	  			        $('#create-resource #nonassigned-fields-list option').each(function(i,e){
	  			        	setOfferPriority ++;
	  	                	if($(this).val() !==  selectedOffer)
	  	                		return true;
	  	                		else
	  	                			offerPriority = setOfferPriority;
	  			        		return false;
	  	                });
	  			        //alert(offerPriority);
	  			        $.ajax({
	  	                    url: '/sef-catalog-ui/defineOfferGroupAndPriority?offerGroupName=' + offerGroupName +'&offerName=' + selectedOffer +'&offerPriority='+ offerPriority,
	  	                    type: "GET",
	  	                    async: false,
	  	                    success: function (data) {
	  	                    	if(data.status == 'success'){
	  	                       // alert(data.message);
	  	                        $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
	  	                        OfferGroupHandler.loadOfferCatalogGroup();
	  	                        $('#offer-catalog-group-service').show();
	  	                    }else{
	  	                    	//alert(data.message);
	  	                    	$('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
	  	                    }
	  	                }
	  			    });
	  	    }else{
	  	     //alert("save");
	  	    	
	  	     var selected=$( "#assigned1 #assigned-fields-list option:selected" );
	  	     var optionValue = selected.val();
	  	     //selected.remove();
	  	     if(optionValue === null || optionValue === undefined || optionValue === ''){
	  	    	return;
		     }
	  	     /*var newOption = $('<option id="'+optionValue+'" class="optionChecked" />');
	  	        $('#non-assigned1 #nonassigned-fields-list').append(newOption);
	  	        newOption.val(optionValue); 
	  	        newOption.html(optionValue);*/
	  	      $('#assigned1 #assigned-fields-list option:selected').each(function (e) {
	  	    	$('#non-assigned1 #nonassigned-fields-list').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');

              });
              $('#assigned1 #assigned-fields-list option:selected').remove();
	  	        
	  	        var rightButtonCount = $('#displayCount').text();
	  		    if(rightButtonCount > 1){
	  		    	$('#assigned-prod-group-btn').show();
	  		    }
	  	     }
	  	    });
	      
	  	    $('#img-left1').unbind('click').click(function(){
	  	    	
	  	    /*if($('#assign-deassign-offer').hasClass('offer_assign_deassign')){*/
	  	    	if(! $('#save-btn-ocg').is(':visible')){
	  	    	
	  	    	if($( "#non-assigned1 #nonassigned-fields-list option").length <= 1 || $( "#non-assigned1 #nonassigned-fields-list option").length == $( "#non-assigned1 #nonassigned-fields-list option:selected").length){
					$('#error_message').show();$('#myform_succloc').css('color', 'red').text('Atleast a resource should be assigned.');
					return false;
				}
	  	    	
	     	 var selected=$( "#non-assigned1 #nonassigned-fields-list option:selected" );
	     	 selected.remove();
	   	     var selectedOffer = selected.val();
	   	     if(selectedOffer === null || selectedOffer === undefined || selectedOffer === ''){
   			 
			   return;
		      }
	   	     var newOption = $('<option id="'+selectedOffer+'" class="optionChecked" />');
	   	        $('#assigned1 #assigned-fields-list').append(newOption);
	   	        newOption.val(selectedOffer); 
	   	        newOption.html(selectedOffer);
	   	        // while editing offer group user may unset offer from offer group
	   	       var offerGroupName = $('#create-resource input[name="offer_group_name"]').val();
	  	        //alert(offerGroupName);
	  	       // alert(selectedOffer);
	  	        $.ajax({
	  	            url: '/sef-catalog-ui/unsetOfferGroupAndPriority?offerGroupName=' + offerGroupName +'&offerName=' + selectedOffer ,
	  	            type: "GET",
	  	            contentType: 'application/json',
	  	            success: function (data) {
	  	                if(data.status == 'success'){
	  	                	//alert(data.message);
  	                        $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
	  	                	OfferGroupHandler.loadOfferCatalogGroup();
	                          $('#offer-catalog-group-service').show();
	  	                }else{
	  	                	//alert(data.message);
  	                    	$('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
	  	                }
	  	            }
	  	        });
	  	        
	   	        
	  	    }else{
	  	     //alert("save");
	  	     var selected=$( "#non-assigned1 #nonassigned-fields-list option:selected" );
	  	    // selected.remove();
	  	     var selectedOffer = selected.val();
	  	     if(selectedOffer === null || selectedOffer === undefined || selectedOffer === ''){
			    return;
	         }
	  	     /*var newOption = $('<option id="'+selectedOffer+'" class="optionChecked" />');
	  	        $('#assigned1 #assigned-fields-list').append(newOption);
	  	        newOption.val(selectedOffer); 
	  	        newOption.html(selectedOffer);*/
	  	        
	  	      $('#non-assigned1 #nonassigned-fields-list option:selected').each(function (e) {
	  	    	$('#assigned1 #assigned-fields-list').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');

	              });
	  	    $('#non-assigned1 #nonassigned-fields-list option:selected').remove();
	  	      
	  	     }
	  	    });

	          $.ajax({
		             url: '/sef-catalog-ui/getAllOffersGroups',
		             type: "GET",
		             contentType: 'application/json',
		             success: function (data) {
		            	 if(data.status == 'success'){
		            	/* $('#right1').html('<div id="right1_errorloc" style="color:#ff0000;float:left; width:100%; display:none;"></div>');
	                     $('#right1_errorloc').show().text(data.message);*/
		                 json = JSON.parse(data.responseString);
		                 offerGroupData = [];
		                 for (var key in json) {
		                     var offerGroup = {};
		                     offerGroup.text = key;
		                     offerGroup.expanded = false;
		                     offerGroup.items = [];
		                     offerGroup.spriteCssClass = "rootfolder";

		                     var offerGroupObject = json[key];
		                     var sortable = [];
		                     for (key1 in offerGroupObject) {
		                      sortable.push([key1, offerGroupObject[key1]])
		                     }
		                     sortable.sort(function(a, b) {return a[1] - b[1]})
		                     for(i = 0; i < sortable.length; i++){
		                      
		                      var offers = {};
		                      offers.text = sortable[i][0];
		                      offers.expanded = false;
		                      offers.spriteCssClass = "folder";
		                      offerGroup.items.push(offers);
		                     }
		                     
		                     offerGroupData.push(offerGroup);
		                 }

		                 if ($('#middle')
		                     .hasClass('k-treeview')) {
		                     treeview = $("#middle").data("kendoTreeView");
		                     treeview.setDataSource(new kendo.data.HierarchicalDataSource({
		                         data: offerGroupData
		                     }));

		                 } else {
		                     $treeview = $("#middle").kendoTreeView({
		                         template: kendo.template($("#middle-template").html()),
		                         dataSource: offerGroupData,
		                     }).data("kendoTreeView");
		                 }
		                 if($('#hasOfferGroupDelete').val() === 'false'){
		 					$('.delete-link').hide();
		 				}
		                 if($('#hasOfferGroupUpdate').val() === 'false'){
			 					$('.edit-link').hide();
			 				}
		                 $('li ul li .delete-link').hide(); 
		                 $('li ul li .edit-link').hide();
		                 $("#middle div span.k-checkbox").hide();
		             }else{
		            	 /*$('#right1').html('<div id="right1_errorloc" style="color:#ff0000;float:left; width:100%; display:none;">gdfgdfgdf</div>');
	                     $('#right1_errorloc').show().text(data.message);*/
		             }
	          }
		         });
	          
	  		$("#middle").mCustomScrollbar("destroy");

			/* all available option parameters with their default values */
			$("#middle").mCustomScrollbar({
				setWidth: false,
				setHeight: false,
				setTop: 0,
				setLeft: 0,
				axis: "y",
				scrollbarPosition: "inside",
				scrollInertia: 950,
				autoDraggerLength: true,
				autoHideScrollbar: false,
				autoExpandScrollbar: false,
				alwaysShowScrollbar: 0,
				snapAmount: null,
				snapOffset: 0,
				mouseWheel: {
					enable: true,
					scrollAmount: "auto",
					axis: "y",
					preventDefault: false,
					deltaFactor: "auto",
					normalizeDelta: false,
					invert: false
				},
				scrollButtons: {
					enable: false,
					scrollType: "stepless",
					scrollAmount: "auto"
				},
				keyboard: {
					enable: true,
					scrollType: "stepless",
					scrollAmount: "auto"
				},
				contentTouchScroll: 25,
				advanced: {
					autoExpandHorizontalScroll: false,
					autoScrollOnFocus: "input,textarea,select,button,datalist,keygen,a[tabindex],area,object,[contenteditable='true']",
					updateOnContentResize: true,
					updateOnSelectorLength: false
				},
				theme: "light",
				callbacks: {
					onScrollStart: false,
					onScroll: false,
					onTotalScroll: false,
					onTotalScrollBack: false,
					whileScrolling: false,
					onTotalScrollOffset: 0,
					onTotalScrollBackOffset: 0,
					alwaysTriggerOffsets: true
				},
				live: false
			});

	          
	       // When add resource group click
	          $(document).ready(function () {
	              $('#ui-btn-ocg').unbind('click').click(function () {
	            	  document.getElementById("group-desc1").value= "";
	            	  $('#create-resource input').prop('readonly',false);
		              $('#non-assigned1 #nonassigned-fields-list').empty();
	                  $('#offer-catalog-group-service').show();
	                  $("#group-desc2").val('');
	                  $("#content-right").hide();
	                  $("#save-btn-ocg").show();
	                  $("#cancel-btn-ocg").show();
	                  $("#cancel-btn-ser").hide();
	                  $("#save-btn-res").hide();
	                  $("#update-btn-ocg").hide(); 
	                  $('#prod-grp-displays').hide();
	                  $('#assigned-prod-group-btn').hide();
	                  
	                  // check uniqueness of offer group name in offerGroupStore.ccm file
		                 $("#group-desc2").blur(function() { 
		                   var offerGroupName = $('#group-desc2').val();
		                   $.ajax({
		      	             url: '/sef-catalog-ui/isOfferGroupExists?offerGroupName=' + offerGroupName,
		      	             type: "GET",
		      	             contentType: 'application/json',
		      	             success: function (data) {
		      	            	 if(data.status == 'success'){
		  	                       // $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
		      	            	 }else{
		  	                    	$('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
		      	            	 }
		      	             }
		      	         });
		                 });

	              });
	          });
	          document.getElementById("group-desc1").value= "";
	     }
	};


var UpdateOfferGroupHandler = {
		updateOfferGroup: function () {
			$('.pro-navigate-up').unbind('click').click(function(){
		        var $op = $('#non-assigned1 #nonassigned-fields-list option:selected');
		        if($('#assigned-prod-group-btn').hasClass('offer_group_edit')){
		        var offerGroupName = $('#create-resource input[name="offer_group_name"]').val();
		        var selectedOffer = $("#non-assigned1 #nonassigned-fields-list option:selected").text();
		        var setOfferPriority = 0;
		        var offerPriority;
		        $('#create-resource #nonassigned-fields-list option').each(function(i,e){
		        	setOfferPriority ++;
                	if($(this).val() !==  selectedOffer)
                		return true;
                		else
                			offerPriority = setOfferPriority;
		        		return false;
                });
		        $.ajax({
                    url: '/sef-catalog-ui/defineOfferGroupAndPriority?offerGroupName=' + offerGroupName +'&offerName=' + selectedOffer +'&offerPriority='+ offerPriority,
                    type: "GET",
                    async: false,
                    success: function (data) {
                    	if(data.status == 'success'){
                    		$op.first().prev().before($op);
  	                        $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
                            OfferGroupHandler.loadOfferCatalogGroup();
                            $('#offer-catalog-group-service').show();
                    }else{
	                    $('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
	                    OfferGroupHandler.loadOfferCatalogGroup();
	                    $('#offer-catalog-group-service').show();
                    }
                }
		    });
		   }else{
			   $op.first().prev().before($op);
		   }
		});
			 $('.pro-navigate-down').unbind('click').click(function(){
			        var $op = $('#non-assigned1 #nonassigned-fields-list option:selected');
			        if($('#assigned-prod-group-btn').hasClass('offer_group_edit')){
				        var offerGroupName = $('#create-resource input[name="offer_group_name"]').val();
				        var selectedOffer = $("#non-assigned1 #nonassigned-fields-list option:selected").text();
				        var setOfferPriority = 0;
				        var offerPriority;
				        $('#create-resource #nonassigned-fields-list option').each(function(i,e){
				        	setOfferPriority ++;
		                	if($(this).val() !==  selectedOffer)
		                		return true;
		                		else
		                			offerPriority = setOfferPriority;
				        		return false;
		                });
				        $.ajax({
		                    url: '/sef-catalog-ui/defineOfferGroupAndPriority?offerGroupName=' + offerGroupName +'&offerName=' + selectedOffer +'&offerPriority='+ offerPriority,
		                    type: "GET",
		                    async: false,
		                    success: function (data) {
		                    	if(data.status == 'success'){
		                    		$op.last().next().after($op);
		  	                        $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
		                            OfferGroupHandler.loadOfferCatalogGroup();
		                            $('#offer-catalog-group-service').show();
		                    }else{
			                    $('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
			                    $('#offer-catalog-group-service').show();
		                    }
		                }
				    });
				   }else{
					   $op.last().next().after($op);
				   }
		   });
			 $('.pro-navigate-first').unbind('click').click(function(){
		    	  var $op1 = $('#non-assigned1 #nonassigned-fields-list option:selected');
		          if($('#assigned-prod-group-btn').hasClass('offer_group_edit')){
				        var offerGroupName = $('#create-resource input[name="offer_group_name"]').val();
				        var selectedOffer = $("#non-assigned1 #nonassigned-fields-list option:selected").text();
				        var setOfferPriority = 0;
				        var offerPriority;
				        $('#create-resource #nonassigned-fields-list option').each(function(i,e){
				        	setOfferPriority ++;
		                	if($(this).val() !==  selectedOffer)
		                		return true;
		                		else
		                			offerPriority = setOfferPriority;
				        		return false;
		                });
				        $.ajax({
		                    url: '/sef-catalog-ui/defineOfferGroupAndPriority?offerGroupName=' + offerGroupName +'&offerName=' + selectedOffer +'&offerPriority='+ offerPriority,
		                    type: "GET",
		                    async: false,
		                    success: function (data) {
		                    	if(data.status == 'success'){
		                    		 $($op1).parent().prepend($op1);
		  	                         $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
		                             OfferGroupHandler.loadOfferCatalogGroup();
		                             $('#offer-catalog-group-service').show();
		                    }else{
			                    $('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
			                    $('#offer-catalog-group-service').show();
		                    }
		                }
				    });
				   }else{
					   $($op1).parent().prepend($op1);
				   }
		    });

			 $('.pro-navigate-last').unbind('click').click(function(){
		    	  var $op1 = $('#non-assigned1 #nonassigned-fields-list option:selected');
		          if($('#assigned-prod-group-btn').hasClass('offer_group_edit')){
				        var offerGroupName = $('#create-resource input[name="offer_group_name"]').val();
				        var selectedOffer = $("#non-assigned1 #nonassigned-fields-list option:selected").text();
				        var setOfferPriority = 0;
				        var offerPriority;
				        $('#create-resource #nonassigned-fields-list option').each(function(i,e){
				        	setOfferPriority ++;
		                	if($(this).val() !==  selectedOffer)
		                		return true;
		                		else
		                			offerPriority = setOfferPriority;
				        		return false;
		                });
				        $.ajax({
		                    url: '/sef-catalog-ui/defineOfferGroupAndPriority?offerGroupName=' + offerGroupName +'&offerName=' + selectedOffer +'&offerPriority='+ offerPriority,
		                    type: "GET",
		                    async: false,
		                    success: function (data) {
		                    	if(data.status == 'success'){
		                    		 $($op1).parent().append($op1);
		  	                        $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
		                            OfferGroupHandler.loadOfferCatalogGroup();
		                            $('#offer-catalog-group-service').show();
		                    }else{
			                    $('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
			                    $('#offer-catalog-group-service').show();
		                    }
		                }
				    });
				   }else{
					   $($op1).parent().append($op1);
				   }
		    });
		}
};
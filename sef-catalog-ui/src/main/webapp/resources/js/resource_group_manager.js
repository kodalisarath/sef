var ResourceManagerHandler = {
	    loadResourceGroups: function () {
	    	
	    	$.ajax({
	    		url: '/sef-catalog-ui/getAllResources?pageNumber=1&pageSize=30',
	    	    type: "GET",
	    	    contentType: 'application/json',
	    	    success: function (data) {
	    	        json = JSON.parse(data);
	    	        $('#assigned #assigned-fields-list').html('');
	    	        var myStringArray =  json.resources;
	    	        var arrayLength = myStringArray.length;
	    	        for (var i = 0; i < arrayLength; i++) {
	    	       	 $('#assigned2 #assigned-fields-list').append("<option value='" + myStringArray[i].name + "'>" + myStringArray[i].name +"</option>");
	    	        }
	    	    }
	    	});
	    	$("#content-middle .search").val('');
	    	$('#ws-search').hide();
			$("#resourcegroup-search").show();	
			$("#offergroup-search").hide();		
			$("#offer-search").hide();
			$("#resource-search").hide();
	    	$('#middle_rule').hide();
	    	$("#privileges-search").hide();
	    	$("#rule_search").hide();
	    	$("#select-policy-rule").hide();
	    	$('#rule-btn').hide();
	    	$("#select-owner-group").hide();
	    	$("#select-offer-group").hide();
	    	$("#select-product-owner").hide();
	    	$('#select-product-resource').hide();
			$('#middle').height('515');
			$('#notification-details').hide();
			$('#middle8').hide();
	    	$('#middle7').hide();
	    	$('#middle5').hide();
	    	$('#middle3').hide();
	    	$('#notification-add').hide();
	        $('#notification-edit').hide();
	        $('#editn-btn').hide();
	        $('#addn-btn').hide();
	        $('#notification_search').hide();
	        $('#content-user-mgt').hide();
	        $('#notification_create_content').hide();
	        $('#content-group-mgt').hide();
	        $('#privileges-content').hide();
	        $('#ui-btn124').hide();
            $('#content-user-mgt').hide();
	    	$('#ui-btn123').hide();
	    	$('#middle').show();
	    	$('#middle2').hide();
	    	 $('#ui-btn-ocg').hide();
	    	 $('#ui-btn-og').hide();
	    	 $('#content-group-mgt').hide();
	    	 $("#ui-btn2").show();
	    	 $("#ui-btn").hide();
	    	 $('#content-group').hide();
	    	 $('#content-resource-group').hide();
	         $('#add-resource-group').show();
	         $('#offer-catalog-group-service').hide();
	         $('#add-offer-catalog-group').hide();
	         $('#create-owner-group').hide();
	         $('#add-owner-group').hide();
	         $('#content-right').hide();
	         $('.ui-btn').hide();
	         $('#ui-btn12').hide();
	         $('#ui-btn1').hide();
	         $("#merge-btn").show();
	         $(".delete-link").hide();
	         $(".edit-link").hide();
	          $('#content-right1').hide();
	         $('#content-right2').hide();
	         $('#content-service').hide();
	         $('#content-profiles').hide();
	         $('#content-policy').hide();
	         $('#content-rule').hide();
	        
	         
	         var deleteResourceGroupManager = "";
	         
	    	$(document).off("click", ".delete-link").on("click", ".delete-link", function (e) {
	           
	           if($('#resource-group-menu').hasClass('active')){
	        	   deleteResourceGroupManager = $(this).attr("text");
	            	//alert("delete resource group");
	            	 //e.preventDefault();
	            	var resourceGroupName = $(this).attr("text");
	                $('#group-desc1').val(resourceGroupName);
	               
	                e.preventDefault(); // disable normal link function so that it doesn't refresh the page
	    	        var docHeight = $(document).height(); //grab the height of the page
	    	        var scrollTop = $(window).scrollTop(); //grab the px value from the top of the page to where you're scrolling
	    	        $('.overlay-bg-delete').show().css({'height' : docHeight}); //display your popup and set height to the page height
	    	        $('.overlay-content-delete').css({'top': scrollTop+20+'px'}); //set the content 20px from the window top
	             }  
	            });
	    	$('.ok-btn').unbind('click').click(function(){
		        $.ajax({
                    url: '/sef-catalog-ui/deleteResourceGroup?resourceGroupName=' + deleteResourceGroupManager,
                    type: "GET",
                    async: false,
                    success: function (data) {
                        if(data.status == 'success'){
                        	 $('.overlay-bg-delete').hide();
                        	//alert(data.message);
  	                        $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
                        	ResourceManagerHandler.loadResourceGroups();
                        }else{
                        	//alert(data.message);
		                    $('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
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
	            $('#create-resource-group input').prop('readonly',true);
	            $('#assigned-prod-group-btn').show();
	            $('#assigned-resource-grp-btn').show();
	            $('#non-assigned2 #nonassigned-fields-list').empty();
	            $('#non-assigned1 #nonassigned-fields-list').empty();
	      	    $('#assigned2 #assigned-fields-list option').show();
	      	  if ($('#resource-group-menu').hasClass('active')) {
	                var resourceGroupName = $(this).attr("text");
	                $('#group-desc1').val(resourceGroupName);
	                $(this).closest('li').find('ul .edit-link').each( function () {
	                var newOption = '<option value="'+$(this).attr("text")+'" class="optionChecked">'+$(this).attr("text")+'</option>';
	                $('#assigned2 #assigned-fields-list').find('[value="' + $(this).attr("text") + '"]').hide();
	                $('#non-assigned2 #nonassigned-fields-list').append(newOption);
	            });
	            
	                
	            $("#save-btn-rg").hide();
	            $("#cancel-btn-rg").show();
	            $("#update-btn-rg").hide();
	            $('#add-resource-group').hide();
	            $('#content-resource-group').show();
	            $('#resource-grp-displays').hide();
	            $('#assigned-resource-grp-btn').addClass("resource_edit");
	            $('#assign-deassign-resource').addClass("resource_assign_deassign");
	        }
	      	    
	    	});
	    	$('#cancel-btn-rg').unbind('click').click(function () {
	    		if ($('#resource-group-menu').hasClass('active')) {
	    			$('#content-resource-group').hide();
	    			 $("#save-btn-rg").hide();
	 	            $("#cancel-btn-rg").hide();
	 	           $("#ui-btn2").show();
	        		ResourceManagerHandler.loadResourceGroups();
	        		$("#group-desc1").val('');
	        		$('#non-assigned2 #nonassigned-fields-list').empty();
	        	}
	    	});
	    	$('#save-btn-rg').unbind('click').click(function () {
	    		if ($('#resource-group-menu').hasClass('active')) {
	            	if(!resourcevalidate($('#content-resource-group'))) {
	                    return;
	                }
	            }
	       		
	    	});
	    	
	    	// while editing resource group user can assigned new resource from unassigned resources so it should be updated in resource group
	    	 $('#img-right12').unbind('click').click(function () {
	    		 	   /* if($('#assign-deassign-resource').hasClass('resource_assign_deassign')){*/
	    			if(! $('#save-btn-rg').is(':visible')){
	    			    var selected=$( "#assigned2 #assigned-fields-list option:selected");
	    			    var selectedResource = selected.val();
	    			    //selected.remove();
	    			    if(selectedResource === null || selectedResource === undefined || selectedResource === ''){
	    			    	return;
	    			    }
	    			   // selected.remove();
	    			    $('#assigned2 #assigned-fields-list option:selected').each(function (e) {
					    	$('#non-assigned2 #nonassigned-fields-list').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');

				              });
					    $('#assigned2 #assigned-fields-list option:selected').remove();
					    // make ajax call to add this assigned resource in resource group
					    var resourceGroupName = $('#create-resource-group input[name="resource_group_name"]').val();
				        var setResourcePriority = 0;
				        var resourcePriority;
				        $('#create-resource-group #nonassigned-fields-list option').each(function(i,e){
				        	setResourcePriority ++;
		                	if($(this).val() !==  selectedResource)
		                		return true;
		                		else
		                			resourcePriority = setResourcePriority;
				        		return false;
		                });
				        
				        $.ajax({
		                    url: '/sef-catalog-ui/defineResourceGroupAndPriority?resourceGroupName=' + resourceGroupName +'&resourceName=' + selectedResource +'&resourcePriority='+ resourcePriority,
		                    type: "GET",
		                    async: false,
		                    success: function (data) {
		                    	if(data.status == 'success'){
		                        //alert(data.message);
		  	                        $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
		                    	  ResourceManagerHandler.loadResourceGroups();
		                    	$('#content-resource-group').show();
		                    }else{
		                    	//alert(data.message);
	  	                    	$('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
		                    }
		                }
				        
				    });
				       
	    		 }else{
	    			
	    		    var selected=$( "#assigned2 #assigned-fields-list option:selected" );
					var optionValue = selected.val();
					if(optionValue === null || optionValue === undefined || optionValue === ''){
						return;
	    			    }
					 
					    $('#assigned2 #assigned-fields-list option:selected').each(function (e) {
					    	$('#non-assigned2 #nonassigned-fields-list').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');

				              });
					    $('#assigned2 #assigned-fields-list option:selected').remove();
				    
				    var rightButtonCount = $('#displayCount').text();
				    if(rightButtonCount > 1){
				    	$('#assigned-resource-grp-btn').show();
				    }
	    		 }
				});
	    	// while editing resource group, user can unset (unassigned) resource from resource group 
				$('#img-left12').unbind('click').click(function () {
					/*if($('#assign-deassign-resource').hasClass('resource_assign_deassign')){*/
					if(! $('#save-btn-rg').is(':visible')){
						if($( "#non-assigned2 #nonassigned-fields-list option").length <= 1 || $( "#non-assigned2 #nonassigned-fields-list option").length == $( "#non-assigned2 #nonassigned-fields-list option:selected").length){
							$('#error_message').show();$('#myform_succloc').css('color', 'red').text('Atleast a resource should be assigned.');
							return false;
						}
						var selected=$( "#non-assigned2 #nonassigned-fields-list option:selected" );
						selected.remove();
						var selectedResource = selected.val();
						if(selectedResource === null || selectedResource === undefined || selectedResource === ''){
	    			    	return false;
	    			    }
						var newOption = $('<option id="'+selectedResource+'" class="optionChecked" />');
					    $('#assigned2 #assigned-fields-list').append(newOption);
					    newOption.val(selectedResource); 
					    newOption.html(selectedResource);
					    // make ajax call to remove selected resource from resource group
					    var resourceGroupName = $('#create-resource-group input[name="resource_group_name"]').val();
				        /*alert(resourceGroupName);
				        alert(selectedResource);*/
				        $.ajax({
				            url: '/sef-catalog-ui/unsetResourceGroupAndPriority?resourceGroupName=' + resourceGroupName +'&resourceName=' + selectedResource ,
				            type: "GET",
				            contentType: 'application/json',
				            success: function (data) {
				                if(data.status == 'success'){
		  	                        $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
				                	ResourceManagerHandler.loadResourceGroups();
				                	$('#content-resource-group').show();
				                }else{
		  	                    	$('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
				                }
				            }
				        });
					}else{
					var selected=$( "#non-assigned2 #nonassigned-fields-list option:selected" );
					//selected.remove();
					var optionValue = selected.val();
					if(optionValue === null || optionValue === undefined || optionValue === ''){
    			    	return;
    			    }
					/*var newOption = $('<option id="'+optionValue+'" class="optionChecked" />');
				    $('#assigned2 #assigned-fields-list').append(newOption);
				    newOption.val(optionValue); 
				    newOption.html(optionValue);*/
				    
				    $('#non-assigned2 #nonassigned-fields-list option:selected').each(function (e) {
				    	$('#assigned2 #assigned-fields-list').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');

			              });
				    $('#non-assigned2 #nonassigned-fields-list option:selected').remove();
					}
				});
    	
	        $.ajax({
	             url: '/sef-catalog-ui/getAllResourceGroups',
	             type: "GET",
	             contentType: 'application/json',
	             success: function (data) {
	            	 if(data.status == 'success'){
	                 json = JSON.parse(data.responseString);
	                 resourceGroupData = [];
	                 for (var key in json) {
	                     var resourceGroup = {};
	                     resourceGroup.text = key;
	                     resourceGroup.expanded = false;
	                     resourceGroup.items = [];
	                     resourceGroup.spriteCssClass = "resourcegroup";

	                     var resourceGroupObject = json[key];
	                     var sortable = [];
	                     for (key1 in resourceGroupObject) {
	                      sortable.push([key1, resourceGroupObject[key1]])
	                     }
	                     sortable.sort(function(a, b) {return a[1] - b[1]})
	                     for(i = 0; i < sortable.length; i++){
	                      
	                      var resources = {};
	                         resources.text = sortable[i][0];
	                         resources.expanded = false;
	                         resources.spriteCssClass = "rgchild";
	                         resourceGroup.items.push(resources);
	                     }
	                     
	                     resourceGroupData.push(resourceGroup);
	                 }

	                 if ($('#middle')
	                     .hasClass('k-treeview')) {
	                	 

	                     treeview = $("#middle").data("kendoTreeView");

	                     treeview.setDataSource(new kendo.data.HierarchicalDataSource({
	                         data: resourceGroupData
	                     }));


	                 } else {
	                     $treeview = $("#middle").kendoTreeView({
	                         template: kendo.template($("#middle-template").html()),
	                         dataSource: resourceGroupData,
	                     }).data("kendoTreeView");
	                 }
	                 if($('#hasResourceGroupDelete').val() === 'false'){
		 					$('.delete-link').hide();
		 				}
	                 
	                 if($('#hasResourceGroupUpdate').val() === 'false'){
		 					$('.edit-link').hide();
		 				}
	                 $('li ul li .delete-link').hide(); 
	                 $('li ul li .edit-link').hide();
	                 $("#middle div span.k-checkbox").hide();
	            	}else{
	            	
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
	             $('#ui-btn2').unbind('click').click(function () {
	            	 document.getElementById("group-desc1").value= "";
	            	 $('#create-resource-group input').prop('readonly',false);
	            	 $('#non-assigned2 #nonassigned-fields-list').empty();
	                 $('#content-resource-group').show();
	                 $('#create-resource-group').show();
	                 $("#content-right").hide();
	                 $("#save-btn-rg").show();
	                 $(".merge-btns").show();
	                 $("#cancel-btn-rg").show();
	                 $("#delete-btn-rg").hide();
	                 $("#update-btn-rg").hide(); 
	                 $('.merge-btns').show();
	                 $('#assigned-resource-grp-btn').hide();
	                 $('#resource-grp-displays').hide();
	              // check uniqueness of resource group name in resourceGroupStore.ccm file
	                 $("#group-desc1").blur(function() { 
	                   var resourceGroupName = $('#group-desc1').val();
	                   $.ajax({
	      	             url: '/sef-catalog-ui/isResourceGroupExists?resourceGroupName=' + resourceGroupName,
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
	    }
};

var UpdateResourceGroupHandler = {
		updateResourceGroup: function () {
			$('.res-navigate-up').unbind('click').click(function(){
		        var $op = $('#non-assigned2 #nonassigned-fields-list option:selected');
		        if($('#assigned-resource-grp-btn').hasClass('resource_edit')){
		        var resourceGroupName = $('#create-resource-group input[name="resource_group_name"]').val();
		        var selectedResource = $("#non-assigned2 #nonassigned-fields-list option:selected").text();
		        var setResourcePriority = 0;
		        var resourcePriority;
		        $('#create-resource-group #nonassigned-fields-list option').each(function(i,e){
		        	setResourcePriority ++;
                	if($(this).val() !==  selectedResource)
                		return true;
                		else
                			resourcePriority = setResourcePriority;
		        		return false;
                });
		        $.ajax({
                    url: '/sef-catalog-ui/defineResourceGroupAndPriority?resourceGroupName=' + resourceGroupName +'&resourceName=' + selectedResource +'&resourcePriority='+ resourcePriority,
                    type: "GET",
                    async: false,
                    success: function (data) {
                    	if(data.status == 'success'){
                    		 $op.first().prev().before($op);
  	                         $('#error_message').show();
  	                         $('#myform_succloc').css('color', 'green').text(data.message);
                    	     ResourceManagerHandler.loadResourceGroups();
                    	     $('#content-resource-group').show();
                    }else{
	                    $('#error_message').show();
	                    $('#myform_succloc').css('color', 'red').text(data.message);
	                    $('#content-resource-group').show();
                    }
                }
		    });
		   }else{
			   $op.first().prev().before($op);
		   }
		});
		    $('.res-navigate-down').unbind('click').click(function(){
		        var $op = $('#non-assigned2 #nonassigned-fields-list option:selected');
		        if($('#assigned-resource-grp-btn').hasClass('resource_edit')){
		        var resourceGroupName = $('#create-resource-group input[name="resource_group_name"]').val();
		        var selectedResource = $("#non-assigned2 #nonassigned-fields-list option:selected").text();
		        var setResourcePriority = 0;
		        var resourcePriority;
		        $('#create-resource-group #nonassigned-fields-list option').each(function(i,e){
		        	setResourcePriority ++;
                	if($(this).val() !==  selectedResource)
                		return true;
                		else
                			resourcePriority = setResourcePriority;
		        		return false;
                });
		        $.ajax({
                    url: '/sef-catalog-ui/defineResourceGroupAndPriority?resourceGroupName=' + resourceGroupName +'&resourceName=' + selectedResource +'&resourcePriority='+ resourcePriority,
                    type: "GET",
                    async: false,
                    success: function (data) {
                    	if(data.status == 'success'){
                    		$op.last().next().after($op);
                    		ResourceManagerHandler.loadResourceGroups();
  	                        $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
                    	  	$('#content-resource-group').show();
                    }else{
	                    $('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
	                    $('#content-resource-group').show();
                    }
                }
		    });
		        }else{
		        	 $op.last().next().after($op);
		        }
		   });
		    $('.res-navigate-first').unbind('click').click(function(){
		    	  var $op1 = $('#non-assigned2 #nonassigned-fields-list option:selected');
		    	  if($('#assigned-resource-grp-btn').hasClass('resource_edit')){
		    	  var resourceGroupName = $('#create-resource-group input[name="resource_group_name"]').val();
			        var selectedResource = $("#non-assigned2 #nonassigned-fields-list option:selected").text();
			        var setResourcePriority = 0;
			        var resourcePriority;
			        $('#create-resource-group #nonassigned-fields-list option').each(function(i,e){
			        	setResourcePriority ++;
	                	if($(this).val() !==  selectedResource)
	                		return true;
	                		else
	                			resourcePriority = setResourcePriority;
			        		return false;
	                });
			        $.ajax({
	                    url: '/sef-catalog-ui/defineResourceGroupAndPriority?resourceGroupName=' + resourceGroupName +'&resourceName=' + selectedResource +'&resourcePriority='+ resourcePriority,
	                    type: "GET",
	                    async: false,
	                    success: function (data) {
	                    	if(data.status == 'success'){
	                    		$($op1).parent().prepend($op1);
	  	                        $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
	                    	    ResourceManagerHandler.loadResourceGroups();
	                    	    $('#content-resource-group').show();
	                    }else{
		                    $('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
		                    $('#content-resource-group').show();
	                    }
	                }
			    });
		    	  }else{
		    		  $($op1).parent().prepend($op1);
		    	  }
		    });

		    $('.res-navigate-last').unbind('click').click(function(){
		    	  var $op1 = $('#non-assigned2 #nonassigned-fields-list option:selected');
		    	  if($('#assigned-resource-grp-btn').hasClass('resource_edit')){
		    	    var resourceGroupName = $('#create-resource-group input[name="resource_group_name"]').val();
			        var selectedResource = $("#non-assigned2 #nonassigned-fields-list option:selected").text();
			        var setResourcePriority = 0;
			        var resourcePriority;
			        $('#create-resource-group #nonassigned-fields-list option').each(function(i,e){
			        	setResourcePriority ++;
	                	if($(this).val() !==  selectedResource)
	                		return true;
	                		else
	                			resourcePriority = setResourcePriority;
			        		return false;
	                });
			        $.ajax({
	                    url: '/sef-catalog-ui/defineResourceGroupAndPriority?resourceGroupName=' + resourceGroupName +'&resourceName=' + selectedResource +'&resourcePriority='+ resourcePriority,
	                    type: "GET",
	                    async: false,
	                    success: function (data) {
	                    	if(data.status == 'success'){
	                    		$($op1).parent().append($op1);
	  	                        $('#error_message').show();
	  	                        $('#myform_succloc').css('color', 'green').text(data.message);	
	  	                        ResourceManagerHandler.loadResourceGroups();
	  	                        $('#content-resource-group').show();
	                    }else 
	                    	$('#error_message').show();
                    	    $('#myform_succloc').css('color', 'red').text(data.message);
                    	    $('#content-resource-group').show();
	                    {
	                    }
	                }
			    });
		    	  }else{
		    		  $($op1).parent().append($op1);
		    	  }
		    });
		}
};
var PrivilegesHandler = {
		
		loadPrivileges : function(){
			$("#content-middle .search").val('');
			$('#rule-btn').hide();  
			$('#ws-search').hide();
			$("#privileges-search").show();
			$('#middle_rule').hide();
			$('#resource-search').hide();
			$('#offer-search').hide();
			$("#resourcegroup-search").hide();		
			$("#offergroup-search").hide();
			$("#rule_search").hide();
			$("#select-policy-rule").hide();
			$("#select-owner-group").hide();
			$("#select-offer-group").hide();
			$("#select-product-owner").hide();
			$('#select-product-resource').hide();
			$('#offer-catalog-group-service').hide();
			$('#content-resource-group').hide();
			$('#content-group').hide();
			$('#content-rule').hide();
			$('#notification-details').hide();
			$('#notification_create_content').hide();
			$('#middle').height('515');
			$('#content-service').hide();
			$('#privileges-content').hide();
			$("#ui-btn124").hide();
			$("#ui-btn").hide();
			$("#ui-btn2").hide();
			$("#middle").show();
			$("#middle2").hide();
			$("#middle3").hide();
			$("#middle5").hide();
			$("#middle7").hide();
			$("#middle8").hide();
			$(".delete-link").hide();
			$(".edit-link").hide();
			$("#ui-btn").hide();
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
  	  	$('#notification-add').hide();
  	     $('#notification-edit').hide();
  	   $('#notification_search').hide();
  	  
  	     $('#editn-btn').hide();
  	     $('#addn-btn').hide();
  	   $('#save-btn-priv').hide();
  	 $('#cancel-btn-priv').hide();
  	   $('#content-group-mgt').hide();
  	 $('#content-user-mgt').hide();
  	   
  	   
  	   $('#cancel-btn-priv').unbind('click').click(function() {
       	if ($('#priviliges-menu').hasClass('active')) {
       		   $('#ui-btn124').hide();
               $("#cancel-btn").hide();
               $("#update-btn-priv").hide();
               $("#content-right").hide();
               $('#privileges-content').hide();
               $("#ui-btn123").hide();
       	}
       });
  	 /*$('#save-btn-priv').unbind('click').click(function () {	
			if ($('#priviliges-menu').hasClass('active')) {
			 var privilegeName = $('#privileges-content input[name="name"]').val();
			 var url = '/sef-catalog-ui/createPrivilege?privilegeName=' + privilegeName;
			 
			 $.ajax({
                 url: url,
                 type: "GET",
                 contentType: 'application/json',
                 success: function (data) {
                     if (data.status == 'success') {
                     	$('#error_message').show();$('#myform_succloc').css('color', 'green').text('Privilige Created Successfully');
                     	PrivilegesHandler.loadPrivileges();
                     } else
                     	{
                     	$('#error_message').show();
                     	$('#myform_succloc').css('color', 'red').text(data.message);
                     	}
                     	
                 }

             });
		}
   });*/
  	$(document).off("click", ".edit-link").on("click", ".edit-link", function (e) {
        e.preventDefault();
        $('#non-assigned5 #assigned-fields-priviliges').empty();
        if ($('#priviliges-menu').hasClass('active')) {
        	$('#privilige-name').attr("disabled", true) 
        	var priviligeName = $(this).attr("text");
        	//alert(priviligeName);
        	var priviliges = {};
        	$.ajax({
                url: '/sef-catalog-ui/readPrivilege?privilegeName=' + $(this).attr("text"),
                type: "GET",
                async: false,
                success: function (data) {
                  //alert(data.responseString);
                  priviliges = JSON.parse(data.responseString);
           	      var list=priviliges.impliedPermissions;
           	      //var list1=priviliges.referenceInIdentity;
           	   $('#assigned5 #nonassigned-fields-priviliges').find('[value="' + priviligeName + '"]').remove();

           	   for(var i=0;i<list.length;i++){
            		  var newOption = $('<option id="'+list[i].name+'" class="optionChecked" />');
           		    $('#non-assigned5 #assigned-fields-priviliges').append(newOption);
           		    $('#assigned5 #nonassigned-fields-priviliges').find('[value="' + list[i].name + '"]').remove();

           		    newOption.val(list[i].name); 
           		    newOption.html(list[i].name);
            	      }
           	   
                  $('#privilige-name').val(priviligeName);
                  $('#referenceIn-Identity').val(priviliges.referenceInIdentity);
              	  $('#privileges-content').show();
              	  $('#cancel-btn-priv').show();
              	  $('#update-btn-priv').show();
              	  $('#ui-btn124').hide();
                  $('#non-assigned5 #assigned-fields-user').empty();
                  
           	      
           	      
                }
            });
        	        	
        }
  	});
  	 $(document).off("click", "#update-btn-priv").on("click", "#update-btn-priv", function (e) {
         e.preventDefault();
         json={};
         if ($('#priviliges-menu').hasClass('active')) {
        	 var privilegeName = $('#privileges-content input[name="name"]').val();
        	 json.name=privilegeName;
        	 var identity = $('#privileges-content input[name="identityname"]').val();
        	 json.identity=identity;
        	 //alert(identity);
			// var url = '/sef-catalog-ui/updatePrivilege?privilegeName=' + privilegeName+'&identity=' + identity;
			 
			 var permisions=null;
			 json.impliedPrivileges=[];
	          $('#create-privilege #assigned-fields-priviliges option').each(function(){
	        	  json.impliedPrivileges.push($(this).val());
	        	  /*if(permisions == null){
	        		  permisions=$(this).val();
	        	  }else{
	        		  permisions=permisions+","+$(this).val();
	        	  }*/
	          });
	          
        	//alert(permisions);
        	 $.ajax({
                 url: '/sef-catalog-ui/updatePrivilege',
                 type: "POST",
                 contentType: 'application/json',
                 data: JSON.stringify(json),
                 success: function (data) {
                     if (data.status == 'success') {
                         $('#error_message').show();$('#myform_succloc').css('color', 'green').text('Privilige Updated Successfully');
                         $("#update-btn-priv").hide();
                         PrivilegesHandler.loadPrivileges();
                     } else {
                     	$('#error_message').show();$('#myform_succloc').css('color', 'red').text(data.message);
                     }
                 }

             });

        	 
         }
  	 });
  	 var deletePrivilige = "";
     
 	$(document).off("click", ".delete-link").on("click", ".delete-link", function (e) {
        
        if($('#priviliges-menu').hasClass('active')){
        	//alert("hai");
        	deletePrivilige = $(this).attr("text");
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
 		$('.overlay-bg-delete').hide();
	        $.ajax({
             url: '/sef-catalog-ui/deletePrivilege?privilegeName=' + deletePrivilige,
             type: "GET",
             async: false,
             success: function (data) {
                 if(data.status == 'success'){
                 	   $('.overlay-bg-delete').hide();
                 	   $('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
                       PrivilegesHandler.loadPrivileges();
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
  	
       /*$(document).ready(function () {
           $('#ui-btn124').unbind('click').click(function () {
        	   $('#privileges-content').show();
               $("#save-btn").show();
               $("#cancel-btn").show();
               $("#delete-btn").hide();
               $("#update-btn").hide();
           });
       });*/
  	  	$('.merge-btns').hide();

  	  $.ajax({
          url: '/sef-catalog-ui/getAllPrivileges',
          type: "GET",
          contentType: 'application/json',
          success: function (data) {
         	 json = JSON.parse(data.responseString);
              
              $('#assigned #assigned-fields-list').html('');
              var myStringArray =  json.privileges;
              
              for(var key in myStringArray){
	                	 $('#assigned5 #nonassigned-fields-priviliges').append("<option value='" + key + "'>" + key +"</option>");
	                 
              }
              
          }
      });
  	 $('#img-right-privilige').unbind('click').click(function () {
  		 //alert("hai");
  		 
  		var selected=$( "#assigned5 #nonassigned-fields-priviliges option:selected" );
		var optionValue = selected.val();
		/*if(optionValue === null || optionValue === undefined || optionValue === ''){
			return;*/
		
		    $('#assigned5 #nonassigned-fields-priviliges option:selected').each(function (e) {
		    	$('#non-assigned5 #assigned-fields-priviliges').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');

	              });
		    $('#assigned5 #nonassigned-fields-priviliges option:selected').remove();
	    
	    
	 //}
  		 
  	 });
  	 $('#img-left-privilige').unbind('click').click(function () {
  		 //alert("hai");
  		 
  		var selected=$( "#non-assigned5 #assigned-fields-priviliges option:selected" );
		var optionValue = selected.val();
		/*if(optionValue === null || optionValue === undefined || optionValue === ''){
			return;*/
		
		    $('#non-assigned5 #assigned-fields-priviliges option:selected').each(function (e) {
		    	$('#assigned5 #nonassigned-fields-priviliges').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');

	              });
		    $('#non-assigned5 #assigned-fields-priviliges option:selected').remove();
	    
	    
	 //}
  		 
  	 });
			
  	  $.ajax({
          url: '/sef-catalog-ui/readallPrivileges',
          type: "GET",
          async: false,
          contentType: 'application/json',
          success: function (data) {
        	  //alert(data.responseString);
              //var arry = [];
        	  json = JSON.parse(data.responseString);
              privilegeData = [];
	                for (var key in json.privileges) {
	                    var privilege = {};
	                  
	                    privilege.text =key;
	                    privilege.expanded = false;
	                    
	                    privilege.spriteCssClass = "rootfolder";
	                   
	                    privilegeData.push(privilege);
	                }

             if ($('#middle').hasClass('k-treeview')) {

                  treeview = $("#middle").data("kendoTreeView");

                  treeview.setDataSource(new kendo.data.HierarchicalDataSource({
                      data: privilegeData
                  }));


              } else {
                  treeview = $("#middle").kendoTreeView({
                      //template: "#= item.text # (#=  #)",
                      template: kendo.template($("#middle-template").html()),
                      checkboxTemplate: kendo.template($("#treeview-checkbox-template").html()),
                      dataSource: privilegeData,
                      // select: onSelect,
                  }).data("kendoTreeView");
              }

          }
      });
  	$("#middle div span.k-checkbox").hide();
  	
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

},
		
};
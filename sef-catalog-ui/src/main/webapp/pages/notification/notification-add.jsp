 <script>
$(document).ready(function(){
	$(".save-btn").click(function(){
		var newTableRow = $("<tr class='tr'>");
		newTableRow.append('<td class="row1">' + $("#mes").val() + '</td>');
		newTableRow.append('<td class="row2">' + $("#sel").val() + '</td>');
		newTableRow.append('<td class="row3"><img src="images/edit.png" class="show-popup-edit" title="Edit" /><img src="images/delete-product.png" class="deletemessage" title="Delete" /></td>');
		newTableRow.append('</tr>');
		newTableRow.appendTo(".tbody_notification table tbody#msgList ");
		$('.overlay-bg').hide();
	});
	
	$(".update-btn").on('click',function(e){
		
		var newTableRow = $("<tr class='tr'>");
		newTableRow.append('<td class="row1">' + $("#mes").val() + '</td>');
		newTableRow.append('<td class="row2">' + $("#sel").val() + '</td>');
		newTableRow.append('<td class="row3"><img src="images/edit.png" class="show-popup-edit" title="Edit" /><img src="images/delete-product.png" class="deletemessage" title="Delete" /></td>');
		newTableRow.append('</tr>');
		if(index_message == 0){
			$("#msgList tr").eq(0).after(newTableRow);
			$("#msgList tr").eq(0).remove();
		}else{
			$("#msgList tr").eq(index_message).remove();
			$("#msgList tr").eq(index_message - 1).after(newTableRow);
		}
		$('.overlay-bg').hide();
	});
	
	$("#message_search").keyup(function() {
	    var value = this.value;
	    $("table tbody#msgList").find("tr").each(function(index) {
	        if (index === -1) return;
	        var id = $(this).find("td").first().text();
	        $(this).toggle(id.indexOf(value) !== -1);
	    });
	});
	$('.cancel-btn').click(function(){
	    $('.overlay-bg').hide(); // hide the overlay
	});
	

	$(document).on('click', '.deletemessage', function () {
        $(this).closest('tr').find('td').fadeOut(1000,

        function () {
            
            $(this).parents('tr:first').remove();
        });

        return false;
    });
	
	   

	});
</script>

<script src="js/notification/notification.js"></script>
<script type="text/javascript" src="js/custom.js"></script>
<style>
.table_wrapper_notification {
    background: tomato;
    border: 1px double #049fff;
    float: left;
 width:99%;
}
.tbody_notification {
    height: 100px;
    overflow-y: auto;
    width: 100%;
    background: #eaeaea;
    text-align: center;
}
table {
    border-collapse: collapse;
    width: 100%;
}
 td {
    border-right: 1px solid #fff;
    border-bottom: 1px solid #fff;
    padding: 1px 5px;
}
.td3 {
    border-right-width: 0;
}
.header_notification {
    width: 100%;
    float: left;
    background: DodgerBlue;
    border-bottom: 1px solid #fff;
    color: #fff;
    text-align: center;
}
.header_notification div {
    padding: 1px 5px;
    float: left;
    border-right: 1px solid #fff;
}

/*  .head3 span {
    padding-left: 5px;
}*/
 .row1 {
    width: 325px;
}
.row2 {
    width: 90px;
}

.header_notification .head1 {
    width: 325px;
}
.header_notification .head2 {
    width: 90px;
}
.header_notification .head3 {
   border-right:none;
   text-align:center;
   float:none;
}

.clear_both {
    clear: both;
    height: 0.75em;
}
</style>
<script>

var index_message = "";

    // show popup when you click on the edit in grid
    $(document).on("click",".show-popup-edit" ,function(event){
        event.preventDefault(); // disable normal link function so that it doesn't refresh the page
        var docHeight = $(document).height(); //grab the height of the page
        var scrollTop = $(window).scrollTop(); //grab the px value from the top of the page to where you're scrolling
        $('.overlay-bg').show().css({'height' : docHeight}); //display your popup and set height to the page height
        $('.overlay-content').css({'top': scrollTop+20+'px'}); //set the content 20px from the window top
     	$(".update-btn").show();
        $(".save-btn").hide();
        index_message = $( "#msgList tr" ).index( $(this).closest("tr") );
        $("#mes").val($(this).closest("tr").find('td:first').text());
	    $("#sel").val($(this).closest("tr").find('td').eq(1).text());
    });
 
    // hide popup when user clicks on close button
    $('.close-btn').click(function(){
        $('.overlay-bg-edit').hide(); // hide the overlay
    });
 
    // hides the popup if user clicks anywhere outside the container
    $('.overlay-bg-edit').click(function(){
        $('.overlay-bg-edit').hide();
    })
    // prevents the overlay from closing if user clicks inside the popup overlay
    $('.overlay-content').click(function(){
        return false;
    });
 

</script>
<div id="notification_create_content">
    <form id="notification-add">
        <div class="form-row">
            <div class="label">Event Id</div><em>*</em>
        </div>
        <div class="input-field">
            <input class="input-field-style mandatory constrained" name="eventId" id="eventId" placeholder="Enter Event Id" onkeypress="return isNumberKey(event)" maxlength="9" style="width:160px;">
        </div>

        <div class="form-row">
            <div class="label">Description</div><em>*</em>
        </div>
        <div class="input-field">
            <input class="input-field-style mandatory constrained" name="description" id="description" placeholder="Enter Decription" style="width:160px;">
        </div>
        
        <div class="form-row">
	        <div class="label">Sender Address</div><em>*</em>
	    </div>
	    <div class="input-field">
	        <input class="input-field-style mandatory constrained" name="sender_address" id="sender_address" placeholder="Enter Sender Address" style="width:160px;">
	    </div>

        <div class="form-row" style="float:left;">
                <div class="label">Charge Amount</div><em>&nbsp;&nbsp;</em>
                <input type="checkbox" name="charge" id="charge_amount_currency" value="enablecharge" />Enable Charge
                <div id="charge_amount" style="float:left; width:100%;display:none; margin-left:180px;">
	                <div class="input-field" style="float:left;">
		    	        <input class="input-field-style mandatory constrained" name="chargeAmount" id="chargeAmount" placeholder="Enter Charge" onkeypress="return isNumberKey(event)" maxlength="9" style="width:160px;">
		    	    </div>
	                <select name="currency" id="currency" style="background:#fff !important; width:160px;border-radius:10px !important; height:22px !important; border:1px solid #808080 !important;">
	                	<option>PHP</option>
	                    <option>CENTS</option>
	                </select>
                </div>
        </div>
        
         <div class="form-row" style="float:left;">
             <div class="label">Notification Action</div><em>&nbsp;&nbsp;</em>
             <select name="notifcation-action" id="notifcation-action" style="background:#fff !important;width:160px; border-radius:10px !important; height:22px !important; border:1px solid #808080 !important;">
                     <option value="ASIS">ASIS</option>
                     <option value="MASSAGE">MASSAGE</option>
                     <option value="WORKFLOW">WORKFLOW</option>
             </select>
         </div>
         <div id="workflowDiv" style="float:left; width:100%;">
	         <div id="client">
	       		 <div class="label">Workflow Id</div><em>&nbsp;&nbsp;</em>
		         <select id="wsClientId" style="background:#fff !important; border-radius:10px !important;width:160px; height:22px !important; border:1px solid #808080 !important;">
		         <!-- <option value="select">Select</option> -->
		                 <!-- <option value="activateSubscriberWorkflow">ActivateSubscriberWorkflow</option>
		                 <option value="alkansyaWorkflow">AlkansyaWorkflow</option>
		                 <option value="callingCircleWorkflow">CallingCirclWorkflow</option> -->
		         </select>
	         </div>
      	</div> 
      	
      	<div style="margin-top:20px; float:left; width:100%; margin-bottom:15px;">
			<h3 style="float:left;">Messages &nbsp;
			<input type="text" id="message_search"  style="width:150px;" placeholder="Search"/></h3>
			<button id="add" class="show-popup" value="Add">Add</button>
		</div>
		
		<div id ="message-box" class="overlay-bg" >
		   <div class="overlay-content" title="New Message">
		    <div><span style="float:left;">Message:</span>&nbsp;&nbsp;<textarea id="mes" rows="5" cols="20" placeholder="Enter your message here..."></textarea></div><br>
		    <div>Language: &nbsp;<select id="sel" style="background:#fff !important; border-radius:10px !important;width:160px; height:22px !important; border:1px solid #808080 !important;">
		                   <option >en</option>
		                   <option >bahasa</option>
		                   <option >fil</option>
		           </select>
		           </div>
		           <button class="save-btn">Save</button>
		           <button class="update-btn">update</button>
		           <button class="close-btn">Close</button>
		           
		        
		           
		          
		   </div>
		</div>
		  
		<div style="float:left;width:100%;">
	        <div class="table_wrapper_notification">
	            <div class="header_notification">
	                <div class="head1">Message</div>
	                <div class="head2">Language</div>
	                <div class="head3">Action</div>
	            </div>
	            <div class="tbody_notification">
	                <table>
	                    <tbody id="msgList">
	                   </tbody>
	                </table>
	            </div>
	        </div>
	    </div>
	    
	    <div class="overlay-bg-edit" >
		   <div class="overlay-content-edit">
		    <div><span style="float:left;">Message:</span>&nbsp;&nbsp;<textarea id="mes" rows="5" cols="20" placeholder="Enter your message here..."></textarea></div><br>
		    <div>Language: &nbsp;<select id="sel" style="background:#fff !important; border-radius:10px !important;width:160px; height:22px !important; border:1px solid #808080 !important;">
		                   <option >en</option>
		                   <option >bahasa</option>
		                   <option >fil</option>
		           </select>
		           </div>
		           <button class="update-btn">update</button>
		           <button class="close-btn">Close</button>
		          
		   </div>
		</div>
	    
	</form>
	<div  style="float:right;margin-top:15px;">
		<button value="Add Offer" id="save-btn-notif" class="btn">Save</button>
		<button value="Add Offer" id="update-btn-notif" class="btn">Update</button>
		<button value="Add Offer" id="cancel-btn-notif" class="btn">Cancel</button>
	</div>
</div>


   	
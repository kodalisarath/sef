 <script src="js/notification/notification.js"></script>
<style>
.header_notification1 .head1{width:347px !important;}
.header_notification1 .head2{border-right:none !important; text-align:center !important;}

.header_notification1 {
    width: 100%;
    float: left;
    background: DodgerBlue;
    border-bottom: 1px solid #fff;
    color: #fff;
    text-align: center;
}
.header_notification1 div {
    padding: 1px 5px;
    float: left;
    border-right: 1px solid #fff;
}
</style>
<div id="notification-details" style="margin:15px; padding:10px;height:100%;float:left; border:5px solid #4f81be;border-radius:10px;background:#fff; ">
       <h3 style="margin-bottom:15px;text-align:center;" >Event Notification Details</h3><br>
        <div class="form-row">
            <div class="label">Event Id:</div>
        </div>
        <div class="input-field">
            <span id="event_Id"></span>
        </div>
		<div style="float:left; width:100%;">
	        <div class="form-row">
	            <div class="label">Description:</div>
	        </div>
	        <div class="input-field">
	           <span id="event_description"></span>
	        </div>
        </div>
        <div style="float:left; width:100%;">
	        <div class="form-row">
		        <div class="label">Sender Address:</div>
		    </div>
		    <div class="input-field">
		        <span id="senderAddress"></span>
		    </div>
 		</div>
 		 <div style="float:left; width:100%;">
	        <div class="form-row" style="float:left;">
	                <div class="label">Charge Amount:</div>
	                <div class="input-field" style="float:left;">
		    	        <span id="charge_amount_currency_details"></span>
		    	    </div>
	        </div>
         </div> 
         <div style="float:left; width:100%;">
	         <div class="form-row" style="float:left;">
	             <div class="label">Notification Action:</div>
	             <span id="notificationAction"></span>
	         </div>
         </div>
         
      	<div style="margin-top:10px; float:left; width:100%; margin-bottom:5px;">
			<h3 style="float:left;">Messages </h3>
		</div>
		<div style="float:left;width:100%;">
	        <div class="table_wrapper_notification">
				<div class="header_notification1">
			                <div class="head1">Message</div>
			                <div class="head2">Language</div>
			    </div>
				
		      	 <div class="tbody_notification">
			                <table>
			                    <tbody id="msgList1">
			                    	
			                   </tbody>
			                </table>
			    </div>
			 </div>
		</div>
       <button id="close" value="close" class="close-btn-details">Close</button>
</div>
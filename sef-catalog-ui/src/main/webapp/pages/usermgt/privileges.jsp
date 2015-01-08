
<script src="js/usermgt/privilegeHandler.js"></script>
<script>
$(document).ready(function(){
    // show popup when you click on the link
    $('.show-popup1').click(function(event){
        event.preventDefault(); // disable normal link function so that it doesn't refresh the page
        var docHeight = $(document).height(); //grab the height of the page
        var scrollTop = $(window).scrollTop(); //grab the px value from the top of the page to where you're scrolling
        $('.overlay-bg1').show().css({'height' : docHeight}); //display your popup and set height to the page height
        $('.overlay-content1').css({'top': scrollTop+20+'px'}); //set the content 20px from the window top
        
        // clear message pop-up in add notification msg in notification manager
        $('.overlay-bg1').find('input:text, input\\:number, input:password, input:file, select, textarea').val('');
        $('.overlay-bg1').find('input:radio, input:checkbox')
             .removeAttr('checked').removeAttr('selected');
    });
 
    // hide popup when user clicks on close button
    $('.close-btn').click(function(){
        $('.overlay-bg1').hide(); // hide the overlay
    });
 
    // hides the popup if user clicks anywhere outside the container
    $('.overlay-bg').click(function(){
        $('.overlay-bg1').hide();
    })
    // prevents the overlay from closing if user clicks inside the popup overlay
    $('.overlay-content1').click(function(){
        return false;
    });
 
});
</script>

<div id="add-offer" style="float: left; width: 100%;">
	<button value="Add Offer" id="ui-btn124">Create Privilege</button>
</div>

<div id="privileges-content">
	<form id="create-privilege">

		<div class="form-row">
			<div class="label" style="float: left; width: 160px;">Privilege Name</div>
			<em>*</em>
		</div>
		<div class="input-field">
			<input class="input-field-style mandatory constrained offerName" name="name" id="privilige-name">
		</div>
		
		<!-- <div class="form-row">
			<div class="label" style="float: left; width: 160px;">ReferenceInIdentity</div>
			<em>*</em>
		</div>
		<div class="input-field">
			<input class="input-field-style mandatory constrained offerName" name="identityname" id="referenceIn-Identity">
		</div> -->
		<!-- <div class="form-row" style="float:left; width:100%; margin-top:10px;">
			<div class="history8 show-popup1">Add Implied Permissions</div>
		</div> -->
		<!-- <div id ="message-box" class="overlay-bg1" >
			<div class="overlay-content1" title="New Message">
				<div>Select Your Domain: &nbsp;
				<select style="background:#fff !important; border-radius:10px !important;width:180px; height:22px !important; border:1px solid #808080 !important;">
		                 <option>DOMAIN_ALL</option>
		                 <option>DOMAIN_SUBSCRIBER_ALL</option>
		                 <option>DOMAIN_SUBSCRIPTION_ALL</option>
		                 <option>DOMAIN_PRODUCT_ALL</option>
		                 <option>DOMAIN_SERVICE_ALL</option>
		                 <option>DOMAIN_PARTNER_ALL</option>
		                 <option>DOMAIN_USER_ALL</option>
		                 <option>DOMAIN_SUBSCRIBER_META</option>
		                 <option>DOMAIN_SUBSCRIBER_WORKFLOW</option>
		                 <option>DOMAIN_SUBSCRIBER_LIFE_CYCLE</option>
		                 <option>DOMAIN_SUBSCRIBER_MVNO_ALL</option>
		                 <option>DOMAIN_SUBSCRIBER_MVNO_OWN</option>
		                 <option>DOMAIN_SUBSCRIBER_MARKET_ALL</option>
		                 <option>DOMAIN_SUBSCRIBER_MARKET_OWN</option>
		         </select>
		         </div>
		         <br>
				<div style="float:left;">Access permissions: &nbsp;</div>
				<div>
				<select multiple style="background:#fff !important; border-radius:10px !important;width:200px; height:60px !important; overflow-y:auto; border:1px solid #808080 !important;">
		                 <option>ACCESS_ALL</option>
		                 <option>ACCESS_READ_ONLY</option>
		                 <option>ACCESS_CREATE_NEW</option>
		                 <option>ACCESS_UPDATE</option>
		                 <option>ACCESS_DELETE</option>
		         </select>
		         </div>
		         <button class="save-btn">Save</button>
		         <button class="close-btn-privileges">Close</button>
		        
			</div>
		</div>
		
		<table class="permissions" style="width:100%;float:left;" id="tab">
		    <thead>
				<tr>
					<th>Domain Name</th>
					<th>Access Permisions</th>
				</tr>
			</thead>
		</table>
		<div style="height:155px; float:left;width:100%;background:#fff; overflow-y:scroll; ">
				<table id="" class="permissions" style="width:100%;" >
					<tbody id="">
					<tr>
						<td>DOMAIN_SUBSCRIPTION_ALL</td>
						<td>ACCESS_READ_ONLY, ACCESS_UPDATE, ACCESS_READ_ONLY</td>
					</tr>
					<tr>
						<td>DOMAIN_SUBSCRIBER_LIFE_CYCLE</td>
						<td>ACCESS_ALL</td>
					</tr>
					<tr>
						<td>DOMAIN_SUBSCRIBER_MARKET_OWN</td>
						<td>ACCESS_READ_ONLY, ACCESS_UPDATE</td>
					</tr>
				</tbody>
	       </table>
      	</div> -->
		 <div style="width: 40%; float: left; margin-top: 10px;">
			<div class="label" style="width: 100%; height: 30px;"
				id="groups-assigned-label">
				Non-Assigned Permissions <em>*</em>
			</div>

			<div id="assigned5">
				<select id="nonassigned-fields-priviliges"
					class="total-kpi-fields select-kpi-fields-list" multiple="multiple"
							style="height:auto; min-height:220px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:left;width: 230px; background:#fff;" 
				name="cdrHeaderField">

				</select>
			</div>
		</div>

		<div id="assign-deassign-resource" class="navigate">
			<div style="margin-left:40px !important;">
				<center>
					<img border="0" src="images/right.png" id="img-right-privilige"
						title="Move to Right">
				</center>
	
				<center>
					<img border="0" src="images/left.png" id="img-left-privilige"
						title="Move to Left">
				</center>
			</div>
		</div>

		<div class="label"
			style="width: 42%; height: 30px; float: right; margin-top: 10px;"
			id="groups-nonassigned-label">
			Assigned Permissions <em>*</em>
		</div>

		<div id ="non-assigned5">
			<select id="assigned-fields-priviliges"
				class="total-kpi-fields select-kpi-fields-list" constraints="{"
				fieldLabel":"Cdr Header
				Field","mustSelect":"true"}" multiple="multiple"
						style="height:auto; min-height:220px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:right;width: 230px; background:#fff;" 
				name="cdrHeaderField">
			</select>

		</div> 
		
	</form>
		<div  style="float:right; margin-top:15px;">
			<button value="Add Offer" id="update-btn-priv" class="btn">Update</button>
			<button value="Add Offer" id="cancel-btn-priv" class="btn">Cancel</button>
		</div>
</div>
 <script src="js/notification/notification.js"></script>
 
<!--  <div id="add-notification" style="float:left; width:100%;">
    <button value="Edit Notification" id="editn-btn">Edit Notification</button>
</div> -->

<div id="notification-edit">
    <form id="edit-notification">
       <div class="form-row">
            <div class="label">Event Id</div><em>*</em>
        </div>
        <div class="input-field">
            <input class="input-field-style mandatory constrained" id="eventId" name="eventId" id="group-desc">
        </div>

        <div class="form-row">
            <div class="label">Description</div><em>&nbsp;&nbsp;</em>
        </div>
        <div class="input-field">
            <input class="input-field-style mandatory constrained" name="description" id="group-name">
        </div>
        
        <div class="form-row">
	        <div class="label">Sender Address</div><em>*</em>
	    </div>
	    <div class="input-field">
	        <input class="input-field-style mandatory constrained" name="description" id="group-name">
	    </div>

        <div class="form-row" style="float:left;">
                <div class="label">Charge Amount</div><em>&nbsp;&nbsp;</em>
                <div class="input-field" style="float:left;">
	    	        <input class="input-field-style mandatory constrained" name="description" id="group-name">
	    	    </div>
                <select name="currency" style="background:#fff !important; width:160px;border-radius:10px !important; height:22px !important; border:1px solid #808080 !important;">
                	<option>CENTS</option>
                    <option>PHP</option>
                </select>
        </div>
        
         <div class="form-row" style="float:left;">
             <div class="label">Notification Action</div><em>&nbsp;&nbsp;</em>
             <select name="notifcation-action" style="background:#fff !important;width:160px; border-radius:10px !important; height:22px !important; border:1px solid #808080 !important;">
                     <option value="select">AS-IS</option>
                     <option value="">MESSAGE</option>
                     <option value="workflow">WORKFLOW</option>
             </select>
         </div>
         <div style="float:left; width:100%;">
	         <div id="client">
	       		 <div class="label">WS Client Id</div><em>&nbsp;&nbsp;</em>
		         <select style="background:#fff !important; border-radius:10px !important;width:160px; height:22px !important; border:1px solid #808080 !important;">
		                 <option value="select">ActivateSubscriberWorkflow</option>
		                 <option value="">AlkansyaWorkflow</option>
		                 <option value="workflow">CallingCirclWorkflow</option>
		         </select>
	         </div>
      	</div> 
      	
      	<div style="margin-top:20px; float:left; width:100%; margin-bottom:15px;">
			<h3 style="float:left;">Messages &nbsp;
			<input type="text"  style="width:150px;"/></h3>
			<button id="add" class="show-popup" value="Add">Add</button>
		</div>
		
		<div class="overlay-bg" >
			<div class="overlay-content" title="New Message">
				<div><span style="float:left;">Message:</span>&nbsp;&nbsp;<textarea rows="5" cols="20"></textarea></div><br>
				<div>Languages: &nbsp;<select style="background:#fff !important; border-radius:10px !important;width:160px; height:22px !important; border:1px solid #808080 !important;">
		                 <option value="select">en</option>
		                 <option value="">bahasa</option>
		                 <option value="workflow">fil</option>
		         </select>
		         </div>
		         <button class="save-btn">Save</button>
		         <button class="close-btn">Close</button>
		        
			</div>
		</div>
		
		<table class="reference" style="width:100%; ">
			<tbody>
				<tr>
					<th>Message</th>
					<th>Language</th>
				</tr>
				<tr>
					<td>Notification add on 10th June 2014.</td>
					<td>en</td>
				</tr>
				<tr>
					<td>Notification add on 30th July 2014.</td>
					<td>bahasa</td>
				</tr>
			</tbody>
       </table>
      	
    </form>
   
</div>
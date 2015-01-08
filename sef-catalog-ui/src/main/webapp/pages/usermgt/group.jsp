<script src="js/usermgt/groupHandler.js"></script>
<script>
$(document).ready(function(){
	document.getElementById("group-name9").value= "";
	document.getElementById("group-desc9").value= "";
});
</script>
<script>
$(document).ready(function() {
	
	
    $('#offer_selectall').click(function(event) {  //on click 
    	
        if(this.checked) { // check select status
            $('.checkbox1').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"               
            });
        }else{
            $('.checkbox1').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"                       
            });         
        }
        
    });
    $(".checkbox1").click(function(){
   		
        if($('.checkbox1:checked').length == $('.checkbox1').length) {
          $('#offer_selectall').prop('checked', true);
          
        }
        else {
          $('#offer_selectall').prop('checked', false);
          if($('.checkbox1:checked').length > 0){
			$('.checkbox1.view').prop('checked',true);
          }
          
        }
        
    });
    $('input:checkbox').removeAttr('checked');
    
    $('#offergroup_selectall').click(function(event) {  //on click 
        if(this.checked) { // check select status
            $('.checkbox2').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"               
            });
        }else{
            $('.checkbox2').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"                       
            });         
        }
    });
    $(".checkbox2").click(function(){
    	if($('.checkbox2:checked').length == $('.checkbox2').length) 
   			{
   			$("#offergroup_selectall").prop('checked', true);
   			}
   		else{
   			$("#offergroup_selectall").prop('checked', false);
   			if($('.checkbox2:checked').length > 0){
   				$('.checkbox2.view').prop('checked',true);
   	          }
   			}
   	});
    
    
    
    $('#resource_selectall').click(function(event) {  //on click 
        if(this.checked) { // check select status
            $('.checkbox3').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"               
            });
        }else{
            $('.checkbox3').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"                       
            });         
        }
    });
    $(".checkbox3").click(function(){
    	if($('.checkbox3:checked').length == $('.checkbox3').length) 
   			{
   			$("#resource_selectall").prop('checked', true);
   			}
   		else{
   			$("#resource_selectall").prop('checked', false);
   			
   			if($('.checkbox3:checked').length > 0){
   				$('.checkbox3.view').prop('checked',true);
   	          }
   			}
   	});
    
    
    
    $('#resourcegroup_selectall').click(function(event) {  //on click 
        if(this.checked) { // check select status
            $('.checkbox4').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"               
            });
        }else{
            $('.checkbox4').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"                       
            });         
        }
    });
    $(".checkbox4").click(function(){
    	if($('.checkbox4:checked').length == $('.checkbox4').length) 
   			{
   			$("#resourcegroup_selectall").prop('checked', true);
   			}
   		else{
   			$("#resourcegroup_selectall").prop('checked', false);
   			if($('.checkbox4:checked').length > 0){
   				$('.checkbox4.view').prop('checked',true);
   	          }
   			}
   	});
    
    
    $('#rule_selectall').click(function(event) {  //on click 
        if(this.checked) { // check select status
            $('.checkbox5').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"               
            });
        }else{
            $('.checkbox5').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"                       
            });         
        }
    });
    $(".checkbox5").click(function(){
    	if($('.checkbox5:checked').length == $('.checkbox5').length) 
   			{
   			$("#rule_selectall").prop('checked', true);
   			}
   		else{
   			$("#rule_selectall").prop('checked', false);
   			if($('.checkbox5:checked').length > 0){
   				$('.checkbox5.view').prop('checked',true);
   	          }
   			}
   	});
    
    
    $('#owner_selectall').click(function(event) {  //on click 
        if(this.checked) { // check select status
            $('.checkbox6').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"               
            });
        }else{
            $('.checkbox6').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"                       
            });         
        }
    });
    $(".checkbox6").click(function(){
    	if($('.checkbox6:checked').length == $('.checkbox6').length) 
   			{
   			$("#owner_selectall").prop('checked', true);
   			}
   		else{
   			$("#owner_selectall").prop('checked', false);
   			if($('.checkbox6:checked').length > 0){
   				$('.checkbox6.view').prop('checked',true);
   	          }
   			}
   	});
    
    
    
    $('#user_selectall').click(function(event) {  //on click 
        if(this.checked) { // check select status
            $('.checkbox7').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"               
            });
        }else{
            $('.checkbox7').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"                       
            });         
        }
    });
    $(".checkbox7").click(function(){
    	if($('.checkbox7:checked').length == $('.checkbox7').length) 
   			{
   			$("#user_selectall").prop('checked', true);
   			}
   		else{
   			$("#user_selectall").prop('checked', false);
   			if($('.checkbox7:checked').length > 0){
   				$('.checkbox7.view').prop('checked',true);
   	          }
   			}
   	});
    
    
    $('#group_selectall').click(function(event) {  //on click 
        if(this.checked) { // check select status
            $('.checkbox8').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"               
            });
        }else{
            $('.checkbox8').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"                       
            });         
        }
    });
    $(".checkbox8").click(function(){
    	if($('.checkbox8:checked').length == $('.checkbox8').length) 
   			{
   			$("#group_selectall").prop('checked', true);
   			}
   		else{
   			$("#group_selectall").prop('checked', false);
   			if($('.checkbox8:checked').length > 0){
   				$('.checkbox8.view').prop('checked',true);
   	          }
   			}
   	});
    
    
    $('#notfication_selectall').click(function(event) {  //on click 
        if(this.checked) { // check select status
            $('.checkbox9').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"               
            });
        }else{
            $('.checkbox9').each(function() { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"                       
            });         
        }
    });
    $(".checkbox9").click(function(){
    	if($('.checkbox9:checked').length == $('.checkbox9').length) 
   			{
   			$("#notfication_selectall").prop('checked', true);
   			}
   		else{
   			$("#notfication_selectall").prop('checked', false);
   			if($('.checkbox9:checked').length > 0){
   				$('.checkbox9.view').prop('checked',true);
   	          }
   			}
   	});
    
    
});

</script>
<script src="js/jquery.accordion.source.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		// <![CDATA[
			
		$(document).ready(function () {
			$('.accordion').accordion();
		});
				
		// ]]>
	</script>
<style>
	
	.accordion { list-style-type: none; padding: 0; margin: 10px 0 30px;  border-top: none; border-left: none; }
	.accordion ul { padding: 0; margin: 0; float: left; display: block; width: 100%; }
	.accordion li {cursor: pointer;outline:none;background: #548bc9; list-style-type: none; padding: 0; margin: 0; float: left; display: block; width: 100%;}
	.accordion li.active>a { background: url('images/minus.gif') no-repeat center right; }
	.accordion li div { padding: 20px; background: #548bc9; display: block; clear: both; float: left; width: 92.5%;}
	.accordion a { text-decoration: none; border-bottom: 1px solid #3b72b1; font: bold 1.1em/2em Arial, sans-serif; color: #fff; padding: 0 10px; display: block; cursor: pointer; background: url('images/plus.gif') no-repeat center right;}
	
	/* Level 2 */
	.accordion li ul li { background: #7FD2FF; font-size: 0.9em;padding-left:25px; padding-top:5px; float:left; width:auto; }
	
	ul.chk-container{background:#fff !important;}
	ul.chk-container li{background:#fff !important;outline:none;}
	</style>
<div id="content-group-mgt">
    <form id="create-group">
        
        <div class="group" >
            	
            <div class="form-row">
						<div class="label" >Group Name</div><em>*</em>
						</div>
            <div class="input-field">
							<input class="input-field-style mandatory constrained" name="userName" id="group-name9">
						</div>
			<div class="form-row">
						<div class="label" >Description</div><em>&nbsp;&nbsp;</em>
						</div>
            <div class="input-field">
							<input class="input-field-style mandatory constrained" name="description"  id="group-desc9">
						</div>
			</div>
        	
        	<h3>Roles:</h3>
        	<ul class="accordion">
				<li>
					<a href="#">Offers</a>
					<ul class="chk-container" style="background: #fff;">
						<li><input type="checkbox" class="chk" id="offer_selectall" name="offers" value="access_offer_all"/>SELECT ALL</li>
						<li><input class="checkbox1 chk" type="checkbox" name="offers" value="access_offer_create_new"> ADD</li>
						<li><input class="checkbox1  chk" type="checkbox" name="offers" value="access_offer_update">EDIT</li>
						<!--<li><input class="checkbox1 chk" type="checkbox" name="offers" value="access_offer_delete">DELETE</li>-->
						<li><input class="checkbox1 chk view" type="checkbox" checked="checked" name="offers" value="access_offer_read_only"> VIEW</li>
					</ul>
				</li>
				<li>
				
					<a href="#">Offer Group</a>
					<ul class="chk-container" style="background: #fff;">
						<li><input type="checkbox" class="chk" id="offergroup_selectall" name="offerGroup" value="access_offer_group_all"/> SELECT ALL</li>
						<li><input class="checkbox2 chk" type="checkbox" name="offerGroup" value="access_offer_group_create_new"> ADD</li>
						<li><input class="checkbox2 chk" type="checkbox" name="offerGroup" value="access_offer_group_update">EDIT</li>
						<li><input class="checkbox2 chk" type="checkbox" name="offerGroup" value="access_offer_group_delete">DELETE</li>
						<li><input class="checkbox2 chk view" type="checkbox" checked="checked" name="offerGroup" value="access_offer_group_read_only">VIEW</li>
					</ul>
				</li>
				<li>

					<a href="#">Resource</a>
					<ul class="chk-container" style="background: #fff;">
						<li><input type="checkbox" class="chk" id="resource_selectall" name="resources" value="access_resource_all"/> SELECT ALL</li>
						<li><input class="checkbox3 chk" type="checkbox" name="resources" value="access_resource_create_new"> ADD</li>
						<li><input class="checkbox3 chk" type="checkbox" name="resources" value="access_resource_update">EDIT</li>
						<li><input class="checkbox3 chk" type="checkbox" name="resources" value="access_resource_delete">DELETE</li>
						<li><input class="checkbox3 chk view" type="checkbox" checked="checked" name="resources" value="access_resource_read_only"> VIEW</li>
					</ul>
				</li>
				<li>

					<a href="#">Resource Group</a>
					<ul class="chk-container" style="background: #fff;">
						<li><input type="checkbox" class="chk" id="resourcegroup_selectall" name="resourceGroup" value="access_resource_group_all"/> SELECT ALL</li>
						<li><input class="checkbox4 chk" type="checkbox" name="resourceGroup" value="access_resource_group_create_new"> ADD</li>
						<li><input class="checkbox4 chk" type="checkbox" name="resourceGroup" value="access_resource_group_update">EDIT</li>
						<li><input class="checkbox4 chk" type="checkbox" name="resourceGroup" value="access_resource_group_delete">DELETE</li>
						<li><input class="checkbox4 chk view" type="checkbox" checked="checked" name="resourceGroup" value="access_resource_group_read_only"> VIEW</li>
					</ul>
				</li>
				<li>

					<a href="#">Rules</a>
					<ul class="chk-container" style="background: #fff;">
						<li><input type="checkbox" class="chk" id="rule_selectall" name="rules" value="access_rules_all"/> SELECT ALL</li>
						<li><input class="checkbox5 chk" type="checkbox" name="rules" value="access_rules_create_new"> ADD</li>
						<li><input class="checkbox5 chk" type="checkbox" name="rules" value="access_rules_update">EDIT</li>
						<li><input class="checkbox5 chk" type="checkbox" name="rules" value="access_rules_delete">DELETE</li>
						<li><input class="checkbox5 chk view" type="checkbox" checked="checked" name="rules" value="access_rules_read_only"> VIEW</li>
					</ul>
				</li>
				<li>

					<a href="#">Owner</a>
					<ul class="chk-container" style="background: #fff;">
						<li><input type="checkbox"  class="chk" id="owner_selectall" name="owner" value="access_owner_group_all"/> SELECT ALL</li>
						<li><input class="checkbox6 chk" type="checkbox" name="owner" value="access_owner_group_create_new"> ADD</li>
						<li><input class="checkbox6 chk" type="checkbox" name="owner" value="access_owner_group_update">EDIT</li>
						<li><input class="checkbox6 chk" type="checkbox" name="owner" value="access_owner_group_delete">DELETE</li>
						<li><input class="checkbox6 chk view" type="checkbox" checked="checked" name="owner" value="access_owner_group_read_only"> VIEW</li>
					</ul>
				</li>
				<li>

					<a href="#">Users</a>
					<ul class="chk-container" style="background: #fff;">
						
						<li><input type="checkbox" class="chk" id="user_selectall" name="users" value="access_user_all"/> SELECT ALL</li>
						<li><input class="checkbox7 chk" type="checkbox" name="users" value="access_user_create_new"> ADD</li>
						<li><input class="checkbox7 chk" type="checkbox" name="users" value="access_user_update">EDIT</li>
						<li><input class="checkbox7 chk" type="checkbox" name="users" value="access_user_delete">DELETE</li>
						<li><input class="checkbox7 chk view" type="checkbox" checked="checked" name="users" value="access_user_read_only">VIEW</li>
					</ul>
				</li>
				<li>

					<a href="#">Groups</a>
					<ul class="chk-container" style="background: #fff;">
						<li><input type="checkbox" class="chk" id="group_selectall" name="groups" value="access_group_all"/> SELECT ALL</li>
						<li><input class="checkbox8 chk" type="checkbox" name="groups" value="access_group_create_new"> ADD</li>
						<li><input class="checkbox8 chk" type="checkbox" name="groups" value="access_group_update">EDIT</li>
						<li><input class="checkbox8 chk" type="checkbox" name="groups" value="access_group_delete">DELETE</li>
						<li><input class="checkbox8 chk view" type="checkbox" checked="checked" name="groups" value="access_group_read_only"> VIEW</li>
					</ul>
				</li>
				
				<li>

					<a href="#">Notification </a>
					<ul class="chk-container" style="background: #fff;">
						<li><input type="checkbox" class="chk" id="notfication_selectall" name="notification" value="access_notification_all"/>SELECT ALL</li>
						<li><input class="checkbox9 chk" type="checkbox" name="notification" value="access_notification_create_new"> ADD</li>
						<li><input class="checkbox9 chk" type="checkbox" name="notification" value="access_notification_update">EDIT</li>
						<li><input class="checkbox9 chk" type="checkbox" name="notification" value="access_notification_delete">DELETE</li>
						<li><input class="checkbox9 chk view" type="checkbox" checked="checked" name="notification" value="access_notification_read_only"> VIEW</li>
					</ul>
				</li>
				
				
			</ul>
         <!--  <div style="float:left;width:100%;"> -->
        
        	  <!-- <div style="width:40%; float:left; margin-top:10px;">
                    <div class="label" style="width: 100%; height: 30px;" id="groups-assigned-label">
                        Non-Assigned Priviliges
                        <em>*</em>
                    </div>
                                    
                    <div id="assigned3">
                        <select id="assigned-fields-list" class="total-kpi-fields select-kpi-fields-list" multiple="multiple"
                        style="height:auto; min-height:250px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:left;width: 230px; background:#fff;" 
				name="cdrHeaderField">

                            
                        </select>
                    </div>
                </div> 
              
                <div id="assign-deassign-resource" class="navigate">
                	<div style="margin-left:40px !important;">
                      <center>
                        <img border="0" src="images/right.png" id="img-right15"  title="Move to Right">
                      </center>
                    
                       <center>
                        <img border="0" src="images/left.png" id="img-left15"  title="Move to Left">
                       </center>
                    </div>
                </div>
                <div class="label" style="width: 42%; height: 30px;float:right;margin-top:10px; "id="groups-nonassigned-label">
                    Assigned Priviliges
                    <em>*</em>
                </div>
                              
                <div id="non-assigned3">
                    <select id="nonassigned-fields-list" class="total-kpi-fields select-kpi-fields-list" constraints="{"fieldLabel":"Cdr Header Field","mustSelect":"true"}" multiple="multiple"
                    style="height:auto; min-height:250px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:right;width: 230px; background:#fff;" 
				name="cdrHeaderField">

                       
                    </select>
                   
                </div>         -->
    
    </form>
    <div  style="float:right; margin-top:15px;">
		<button value="Add Offer" id="save-btn-group" class="btn">Save</button>
		<button value="Add Offer" id="update-btn-group" class="btn">Update</button>
		<button value="Add Offer" id="cancel-btn-group" class="btn">Cancel</button>
	</div>
</div>
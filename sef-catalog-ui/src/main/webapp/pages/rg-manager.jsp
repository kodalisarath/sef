<script src="js/resource_group_manager.js"></script>
<script>
$(document).ready(function(){
	UpdateResourceGroupHandler.updateResourceGroup();
	//ResourceManagerHandler.loadResourceGroups();
	var count = 0;
	var button = document.getElementById("img-right12");
	var display = document.getElementById("displayCount");
	button.onclick = function(){
	  count++;
	  display.innerHTML = count;
	}
	document.getElementById("group-desc1").value= "";
});



</script>

<div id="add-resource-group" style="float:left; width:100%;">
    <button value="Add Resource Group" id="ui-btn2">Add Resource Group</button>
</div>

<div id="content-resource-group">
    <form id="create-resource-group">
        <div class="form-row">
            <div class="label">Resource Group Name</div><em>*</em>
        </div>
        <div class="input-field">
            <input class="input-field-style mandatory constrained" name="resource_group_name" id="group-desc1">
        </div>
        
        <!-- display of buttons in right assigned box when box has minimum two assigned resources -->
        <p id="resource-grp-displays"><span id="displayCount">0</span></p>
         
         <div style="float:left;width:100%;">
        
        	 	<div style="width:40%; float:left; margin-top:10px;">
                    <div class="label" style="width: 100%; height: 30px;" id="groups-assigned-label">
                        Non-Assigned Resources
                        <em>*</em>
                    </div>
                                    
                    <div id="assigned2">
                        <select id="assigned-fields-list" class="total-kpi-fields select-kpi-fields-list" multiple="multiple"   style="font-size:10px; width: 220px;border:1px solid #ccc; background:#fff;height:auto; min-height:250px;font-family:Tahoma, Geneva, sans-serif;" name="cdrHeaderField">
                            
                        </select>
                    </div>
                </div>
              
                <div id="assign-deassign-resource" class="navigate" style="float:left; width:18%;margin-top:25%; margin-left:-50px;">
                   
                      <center>
                        <img border="0" src="images/right.png" id="img-right12"  title="Move to Right">
                      </center>
                    
                       <center>
                        <img border="0" src="images/left.png" id="img-left12"  title="Move to Left">
                       </center>
                </div>
               
                <div class="label" style="width: 50%; height: 30px;float:right;margin-top:10px; "id="groups-nonassigned-label">
                    Assigned Resources
                    <em>*</em>
                </div>
                              
                <div id="non-assigned2">
                    <select id="nonassigned-fields-list" class="total-kpi-fields select-kpi-fields-list" constraints="{"fieldLabel":"Cdr Header Field","mustSelect":"true"}" multiple="multiple"  style="height:auto; min-height:250px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:left;width: 220px; background:#fff;" name="cdrHeaderField">
                       
                    </select>
                    <div class="error-container"></div>
                </div>
                <div id="assigned-resource-grp-btn"  class="arrows" >
	                <img id="first-btn" border="0" src="images/first.png"  class="res-navigate-first" title="First">
	                <div>
		                <img id="up-btn" border="0" src="images/up.png"  class="res-navigate-up" title="Up"><br>
		                <img id="down-btn" border="0" src="images/down.png"  class="res-navigate-down" title="Down">
		            </div>
	                <img id="last-btn" border="0" src="images/last.png"  class="res-navigate-last" title="Last">
           		 </div>
            
        </div>
    </form>
    <div  style="float:right;margin-top:15px;">
		<button value="Add Offer" id="save-btn-rg" class="btn">Save</button>
		<button value="Add Offer" id="update-btn-rg" class="btn">Update</button>
		<button value="Add Offer" id="cancel-btn-rg" class="btn">Cancel</button>
	</div>
</div>


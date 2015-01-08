<script src="js/offer_group_manager.js"></script>
<script>
$(document).ready(function(){
	UpdateOfferGroupHandler.updateOfferGroup();
	var count = 0;
    var button = document.getElementById("img-right1");
    var display = document.getElementById("displayCount");
    button.onclick = function(){
      count++;
      display.innerHTML = count;
    }
    document.getElementById("group-desc2").value= "";
});
</script>
<div id="add-offer-catalog-group" style="float:left; width:100%;">
    <button value="Add Offer Catalog Group" id="ui-btn-ocg">Add Offer Catalog Group</button>
</div>

<div id="offer-catalog-group-service">
    <form id="create-resource">
        <div class="form-row">
            <div class="label">Offer Catalog Group Name</div><em>*</em>
        </div>
        <div class="input-field">
            <input class="input-field-style mandatory constrained" name="offer_group_name" id="group-desc2">
        </div>
        
        <p id="prod-grp-displays"> <span id="displayCount">0</span></p>
        
        <div style="float:left;width:100%; margin-top:15px;">
        
        	 	<div style="width:40%; float:left;">
                    <div class="label" style="width: 100%; height: 30px;" id="groups-assigned-label">
                        Non-Assigned Offers
                        <em>*</em>
                    </div>
                                    
                    <div id="assigned1">
                        <select id="assigned-fields-list" class="total-kpi-fields select-kpi-fields-list" multiple="multiple"   style="font-size:10px; width: 220px;border:1px solid #ccc; background:#fff;height:auto; min-height:250px;font-family:Tahoma, Geneva, sans-serif;" name="cdrHeaderField">
                            
                        </select>
                    </div>
                </div>
              
                <div  id ="assign-deassign-offer"  class="navigate" style="float:left; width:18%;margin-top:25%; margin-left:-50px;">
                   
                      <center>
                        <img border="0" src="images/right.png" id="img-right1"   title="Move to Right">
                      </center>
                    
                       <center>
                        <img border="0" src="images/left.png" id="img-left1" title="Move to Left">
                       </center>
                </div>
               
                <div class="label" style="width: 50%; height: 30px;float:right; "id="groups-nonassigned-label">
                    Assigned Offers
                    <em>*</em>
                </div>
                              
                <div id="non-assigned1">
                    <select id="nonassigned-fields-list" class="total-kpi-fields select-kpi-fields-list" constraints="{"fieldLabel":"Cdr Header Field","mustSelect":"true"}" multiple="multiple"  style="height:auto; min-height:250px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:left;width:220px; background:#fff;" name="cdrHeaderField">
                       
                    </select>
                    <div class="error-container"></div>
                </div>
                <div id="assigned-prod-group-btn" class="arrows" >
	                <img id="first-btn" border="0" src="images/first.png"  class="pro-navigate-first" title="First" >
	                <div >
		                <img id="up-btn" border="0" src="images/up.png"  class="pro-navigate-up" title="Up" ><br>
		                <img id="down-btn" border="0" src="images/down.png"  class="pro-navigate-down" title="Down" >
		            </div>
	                <img id="last-btn" border="0" src="images/last.png"  class="pro-navigate-last" title="Last" >
           		 </div>
            
        </div>
    </form>
    <div  style="float:right;margin-top:15px;">
		<button value="Add Offer" id="save-btn-ocg" class="btn">Save</button>
		<button value="Add Offer" id="update-btn-ocg" class="btn">Update</button>
		<button value="Add Offer" id="cancel-btn-ocg" class="btn">Cancel</button>
	</div>
</div>

<script type="text/javascript">
      var count = 0;
      var button = document.getElementById("img-right1");
      var display = document.getElementById("displayCount");
      button.onclick = function(){
        count++;
        display.innerHTML = count;
      }
    </script>
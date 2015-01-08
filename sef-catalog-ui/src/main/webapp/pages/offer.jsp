<script src="js/offer_catalog.js"></script>

<script>
    // when save button on offer
    $(document).ready(function () {
        $("#dd6").trigger('reset');


        var today = new Date().toISOString().split('T')[0];
        document.getElementsByName("renewalDate")[0].setAttribute('min', today);
        document.getElementsByName("trialDate")[0].setAttribute('min', today);
        document.getElementsByName("minimumCommitmentDate")[0].setAttribute('min', today);
        document.getElementsByName("autoTerminationDate")[0].setAttribute('min', today);
        document.getElementsByName("validityDate")[0].setAttribute('min', today);
    });
</script>
<style>
    .table_wrapper {
        background: tomato;
        border: 1px double #049fff;
        float: left;
    	width:100%;
    }
    .tbody {
        height: 80px;
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
		border-right: 1px solid #fff !important;
		}
    .td5 {
        border-right-width: 0;
    }
    .header {
        width: 100%;
        float: left;
        background: DodgerBlue;
        border-bottom: 1px solid #fff;
        color: #fff;
        text-align: center;
    }
    .header div {
        padding: 1px 5px;
        float: left;
        border-right: 1px solid #fff;
    }
    .header .head5 {
        float: none;
        border-right-width: 0;
    }
  /*  .head3 span {
        padding-left: 5px;
    }*/
    .td1 {
        width: 80px;
    }
    .td2 {
        width: 100px;
    }
    .td3 {
        width: 100px;
    }
    .td4 {
        width: 100px;
    }
   
    .header .head1 {
        width: 80px;
    }
    .header .head2 {
        width: 100px;
    }
    .header .head3 {
        width: 100px;
    }
    .header .head4 {
        width: 100px;
    }
    
    .clear_both {
        clear: both;
        height: 0.75em;
    }
    
     #lifecycle {
        display: none;
    }
    #simple {
        display: none;
    }
</style>
<div id="add-offer" style="float:left; width:100%;">
<button value="Add Offer" id="ui-btn">Create Offer</button>
</div>
<div id="message-box-offer" class="overlay-bg-delete-offer">
	<div class="overlay-content-delete-offer">
		<div id="dailog-message-div-offer"><span style="float:left;"></span>
		</div>
		<button class="ok-btn-offer">Ok</button>
	</div>
</div>
<div id="content-right">

    <form id="create-offer">

        <div class="form-row">
            <div class="label" style="float:left; width:160px;">Offer Name</div><em>*</em>
        </div>
        <div class="input-field">
            <input class="input-field-style mandatory constrained offerName" maxlength="20" name="name" id="group-name">
        </div>

        <div class="form-row">
            <div class="label">Description</div><em>&nbsp;&nbsp;</em>
        </div>
        <div class="input-field">
            <input class="input-field-style mandatory constrained description" name="description" id="group-name">
        </div>

        <div style="float:left; width:100%; margin-top:10px;">
            <div class="form-row">
                <div class="label">Offer State</div><em>&nbsp;&nbsp;</em>
            </div>
            <div class="input-field">
                <input class="input-field-style mandatory constrained description" disabled value="In_Creation" name="offerState" id="group-name">
            </div>
        </div>

        <div class="price" style="margin-top:10px; width:100%;float:left;">
            <div class="history2">External Handler</div>
            <div id="panel2" style="margin-top:10px; width:95%;float:left;">
                <div class="form-row">
                    <div class="label">External Handler Name</div><em>&nbsp;&nbsp;</em>
                </div>
                <div class="input-field">
                    <input class="input-field-style mandatory constrained description" name="handler" id="group-name" style="float:left;">
                    <input type="button" value="Add" id="add-handler" />
                </div>

                <div class="form-row">
                    <div class="label" style="width: 42%; height: 30px; float: left;" id="groups-nonassigned-label">
                        External Handler<em>*</em>
                    </div>

                    <div style="float:left;width:100%;">
                        <div id="assigned-fields-handler" class="total-kpi-fields select-kpi-fields-list" constraints="{" fieldLabel ":"Cdr Header Field ","mustSelect ":"true "}" multiple="multiple" style="height:100px;overflow-y:scroll; min-height:150px;;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:left;;width: 190px; background:#fff;" name="cdrHeaderField">
                            <ul>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="form-row" style="margin-top:10px;">
            <div class="label">IsReccurent</div><em>&nbsp;&nbsp;</em>
            <div id="dd">

                <input type="radio" name="recurrent" value="true" />True
                <input type="radio" name="recurrent" value="false" checked/>False
            </div>
        </div>
        <div class="red">

            <div class="form-row">
                <div class="label" id="validity">Validity</div>
				<div class="label" id="setreneval" style="float:left;">Set Renewal Period</div><em>&nbsp;&nbsp;</em>
                <span id="rp-infinite">
                 	<input type="radio" name="renewalPeriod" value="infinite" />Infinite
                 </span>
                <input type="radio" name="renewalPeriod" value="hours" />Hours
                <input type="text" name="renewalHours" max="24" onkeypress="return isNumberKey(event)" maxlength="9" id="set_hours" style="width:16px;height:15px;display:none;">
                <input type="radio" name="renewalPeriod" value="days" />Days
                <input type="text" name="renewalDays" min="1" id="set_days" onkeypress="return isNumberKey(event)" maxlength="9" style="width:16px;height:15px;display:none;">
                <input type="radio" name="renewalPeriod" value="date" style="margin-left:180px;" />Date
                <input type="text" attr-date="datepicker" name="renewalDate" min="" id="set_date" style="width: 130px;display:none;"  placeholder="yyyy-mm-dd">
            </div>
        </div>


        <div class="form-row" style="margin-top:10px; float:left; width:100%;">
            <div class="label">IsCommercial</div><em>&nbsp;&nbsp;</em>
            <div id="commercial">
                <input type="radio" name="commercial" value="true" checked/>True
                <input type="radio" name="commercial" value="false" />False
            </div>
        </div>
        <div class="blue" style=" width:100%;float:left;">
        	<div class="price" style="margin-top:10px; width:100%;float:left;">
                <!--  price div -->
                <div class="history">Price</div>
            </div>
            <!--  price div -->
            <div id="panel">
                <!--  panel div -->

                <div class="form-row">
                    <div class="label" style="float:left; width:160px;">Cost</div><em>&nbsp;&nbsp;</em>
                </div>
                <div class="input-field">
                    <input onkeypress="return isNumberKey(event)" class="input-field-style mandatory constrained offerName" maxlength="9" type="text" name="cost" id="group-name" style="width: 145px;">
                    <select name="currency" style="width:75px !important;">
                        <option value="PHP">PHP</option>
                    </select>
                </div>

                <div style="float:left;width:100%; margin-top:10px;">
                    <div class="label">Taxes</div>
                    <div id="add2" style="cursor:pointer;">+</div>
                    <div id="taxtype" style=" margin-top:15px;display:none;">
                        <div class="form-row">
                            <div class="label">Tax Type</div><em>&nbsp;&nbsp;</em>
                        </div>

                        <div class="form-row">
                            <select name="tax" style="width:150px !important; float:left !important; margin-right:5px;">
                                <option value="Absolute">Absolute</option>
                                <option value="Free">Free</option>
                                <option value="Percentage">Percentage</option>
                            </select>
                        </div>
                        <div class="form-row">
                            <div class="label">Tax Value</div><em>&nbsp;&nbsp;</em>
                        </div>
                        <div class="form-row">
                            <input onkeypress="return isNumberKey(event)" type="text" maxlength="9" min="1" name="taxValue" id="taxnumber" style="float:left; display:block;width:25px;height:25px; margin-right:7px;">
                            <input type="text" min="1" name="taxCurrency" style="float:left; margin-right:5px;display:block;width:30px;height:25px; margin-right:15px;" readonly>
                            <input value="Add" type="button" id="add-tax" style="display: block;" />
                        </div>

                        <div class="form-row" style="float:left;width:100%;">
                            <div class="label" style="width: 42%; height: 30px; float: left;" id="groups-nonassigned-label">
                                Added Tax <em>*</em>
                            </div>

                            <div style="float:left;width:100%;">
                                <div id="assigned-fields-tax" class="total-kpi-fields select-kpi-fields-list" constraints="{" fieldLabel ":"Cdr Header Field ","mustSelect ":"true "}" multiple="multiple" style="height:100px;overflow-y:scroll; min-height:150px;;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:left;;width: 190px; background:#fff;" name="cdrHeaderField">
                                    <ul>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div style="float:left;width:100%; margin-top:10px;">
                    <div class="label">Pricing Policies</div>
                    <div id="add1" style="cursor:pointer;">+</div>
                    <div id="addpolicy" style=" margin-top:15px;display:none">
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">Policy Name</div><em>*</em>
                        </div>
                        <div class="input-field">
                            <input class="input-field-style mandatory constrained offerName" name="pricePolicy" id="group-name">
                        </div>
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">Policy Type</div><em>&nbsp;&nbsp;</em>
                        </div>
                        <div class="input-field">
                            <select name="policyType" style="width:160px !important; float:left !important; margin-bottom:5px;">
                                <option value="SmartSimplePricingPolicy">Smart Simple Pricing Policy</option>
                                <option value="SmartLIfeCyclePricingPolicy">Smart LifeCycle Pricing Policy</option>
                            </select>
                        </div>
                        <div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
		                    <div id="lifecycle" style="margin-bottom:5px; width: 94%;float: left;"> 
		                        <div class="form-row">
		                            <div class="label" style="float:left; width:160px;">Purchase Cost</div><em>*</em>
		                        </div>
		                        <div class="input-field">
		                            <input class="input-field-style mandatory constrained " onkeypress="return isNumberKey(event)" id="number" maxlength="9" min="1" name="purchaseCost">
		                        </div>
		                        <div class="form-row">
		                            <div class="label" style="float:left; width:160px;">Renewal Cost</div><em>*</em>
		                        </div>
		                        <div class="input-field">
		                            <input class="input-field-style mandatory constrained " onkeypress="return isNumberKey(event)" id="number" min="1" maxlength="9" name="renewalCost">
		                        </div>
		                    </div>
		                </div>
                        <div class="form-row">
                            <div class="label" style="float:left; width:160px;">Policy Rule</div><em>*</em>
                        </div>
                        <div class="input-field">
                            <input class="input-field-style mandatory constrained offerName" name="rule" id="group-name" readonly>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--  panel div end -->


        <div class="price" style="margin-top:10px; width:100%;float:left;">
            <div class="history1">Owner</div>
            <div id="panel1" style="margin-top:10px; width:95%;float:left;">
                <div class="form-row">
                    <div class="label">Owner Name</div><em>&nbsp;&nbsp;</em>
                </div>
                <div class="input-field">
                    <input class="input-field-style mandatory constrained description" name="ownerName" id="group-name" readonly>
                </div>
                <div class="form-row">
                    <div class="label">Owner Type</div><em>&nbsp;&nbsp;</em>
                </div>
                <div class="input-field">
                    <input class="input-field-style mandatory constrained description" name="ownerType" id="group-name" readonly>
                </div>
            </div>
        </div>

        <div class="price" style="margin-top:10px; width:100%;float:left;">
            <div class="history6">Offer Group Behaviour</div>
            <div id="panel6" style="margin-top:10px; width:95%;float:left;display:none;">
                <div class="form-row">
                    <div class="label">Hoarding Behaviour</div><em>&nbsp;&nbsp;</em>
                    <input type="radio" name="accumulationPolicy" value="NO_LIMIT" checked />No Limit
                    <br>
                    <div style="float:left; margin-left:180px;">
                        <input type="radio" name="accumulationPolicy" value="THRESHOLD_REJECT" />Allow Max
                        <input onkeypress="return isNumberKey(event)" type="text" maxlength="9" min="1" id="" style="width:16px;height:15px;">&nbsp; Instance & Reject
                        <br>
                        <input type="radio" name="accumulationPolicy" value="THRESHOLD_SCHEDULE" />Allow Max
                        <input onkeypress="return isNumberKey(event)" type="text" maxlength="9" min="1" id="" style="width:16px;height:15px;">&nbsp; Instance & Schedule of earliest expiry
                        <br>
                        <input type="radio" name="accumulationPolicy" value="NOT_ALLOWED" />Not Allowed
                    </div>
                </div>

                <div class="form-row">
                    <div class="label">Upgrade & Downgrade Behaviour</div><em>&nbsp;&nbsp;</em>
                    <input type="radio" name="switchPolicy" value="NO_LIMIT" checked />No Limit
                    <span id="offer-group-hs" style="display:none">
                    	<input type="text" style="width:110px;height:15px;" name="offerGroup" id="group-name" readonly>&nbsp; Offer Group
                    </span>
                    <br>
                    <div style="float:left; margin-left:180px; margin-top:-12px;">
                        <input type="radio" name="switchPolicy" value="THRESHOLD_REJECT" />Allow Max
                        <input onkeypress="return isNumberKey(event)" type="text" maxlength="9" min="1" id="" style="width:16px;height:15px;">&nbsp; Instance & Reject
                        <br>
                        <input type="radio" name="switchPolicy" value="THRESHOLD_SCHEDULE" />Allow Max
                        <input onkeypress="return isNumberKey(event)" type="text" maxlength="9" min="1" id="" style="width:16px;height:15px;">&nbsp; Instance & Schedule of earliest expiry
                        <br>
                        <input type="radio" name="switchPolicy" value="NOT_ALLOWED" />Not Allowed
                    </div>
                </div>

                <div class="form-row">
                    <div class="label">Automatic Termination</div><em>&nbsp;&nbsp;</em>
                    <input type="radio" name="autoTermination" value="NO_TERMINATION" checked />No Termination
                    <input type="radio" name="autoTermination" value="AFTER_X_RENEWALS" />After 'n' renewals
                    <input onkeypress="return isNumberKey(event)" type="text" name="autoTerminationRenewals" min="1" maxlength="9" id="day_after_term" style="width:16px;height:15px;display:none;">
                    <br>
                    <div style="float:left; margin-left:180px;">
                        <input type="radio" name="autoTermination" value="AFTER_X_DAYS" />After 'n' days
                        <input onkeypress="return isNumberKey(event)" type="text" name="autoTerminationDays" min="1" maxlength="9" id="day_term" style="width:16px;height:15px;display:none;">
                        <input type="radio" name="autoTermination" value="HARD_STOP" id="hards" />Hard Stop
                        <input type="text" attr-date="datepicker" name="autoTerminationDate" min="" id="hs" style="width: 130px;display:none;" placeholder="yyyy-mm-dd">
                    </div>
                </div>

                <div class="form-row">
                    <div class="label">Minimum Commitment</div><em>&nbsp;&nbsp;</em>

                    <input type="radio" name="minimumCommitment" value="NO_COMMITMENT" checked />No Commitment
                    <input type="radio" name="minimumCommitment" value="UNTIL_X_RENEWALS" />Until 'X' Renewals
                    <input onkeypress="return isNumberKey(event)" type="text" maxlength="9" min="1" name="minimumCommitmentRenewals" id="day_after_comm" style="width:16px;height:15px;display:none;">

                    <br>
                    <div style="float:left; margin-left:180px;">
                        <input type="radio" name="minimumCommitment" value="UNTIL_X_DAYS" />Until 'X' Days
                        <input onkeypress="return isNumberKey(event)" type="text" name="minimumCommitmentDays" id="day_comm" maxlength="9" style="width:16px;height:15px;display:none;">
                        <input type="radio" name="minimumCommitment" value="HARD_LIMIT" />Hard Limit
                        <input type="text" attr-date="datepicker" placeholder="yyyy-mm-dd" name="minimumCommitmentDate" min="" id="hl" style="width: 130px;display:none;">
                    </div>
                </div>

                <div style="float:left; width:100%; ">
                    <div class="form-row">
                        <div class="label">Trial Period</div><em>&nbsp;&nbsp;</em>
                        <input type="radio" name="trialPeriod" value="hours" />Hours
                        <input type="text" onkeypress="return isNumberKey(event)" name="trialHours" maxlength="9" max="24" id="h" style="width:16px;height:15px;display:none;">
                        <input type="radio" name="trialPeriod" value="days" />Days
                        <input type="text" onkeypress="return isNumberKey(event)" name="trialDays" maxlength="9" min="1" id="hd" style="width:16px;height:15px;display:none;">
                        <br>
                        <input type="radio" name="trialPeriod" value="date" style="margin-left:180px;" />Date
                        <input type="text" placeholder="yyyy-mm-dd" attr-date="datepicker" name="trialDate" min="" id="trial_date" style="width: 130px;display:none;">

                    </div>
                </div>

                <div style="float:left; width:100%; ">
                    <div class="form-row">
                        <div class="label">Immediate Termination</div><em>&nbsp;&nbsp;</em>
                        <input type="checkbox" name="PREPAID" value="true" checked/>Prepaid
                        <input type="checkbox" name="POSTPAID" value="true" />Postpaid
                        <input type="checkbox" name="HYBRID" value="true" />Hybrid
                        <input type="checkbox" name="CONVERGANT" value="true" />Convergant
                    </div>
                </div>

            </div>

            <div class="price" style="margin-top:10px; width:100%;float:left;">
                <div class="history3">Product</div>
                <div id="panel3" style="margin-top:10px; width:95%;float:left;">
                    <div class="form-row">
                        <div class="label">Product Name</div><em>*</em>
                    </div>
                    <div class="input-field">
                        <input class="input-field-style mandatory constrained description" name="productName" id="group-name">
                    </div>
                    <div class="form-row">
                        <div class="label">Owner</div><em>&nbsp;&nbsp;</em>
                    </div>
                    <div class="input-field">
                        <input class="input-field-style mandatory constrained description" name="productOwner" id="group-name" readonly>
                    </div>
                    <div class="form-row">
                        <div class="label">Resource</div><em>*</em>
                    </div>
                    <div class="input-field">
                        <input class="input-field-style mandatory constrained description" name="productResource" id="group-name" readonly>
                    </div>
                    <div class="form-row">
                         <div class="label">Quota</div><em>&nbsp;&nbsp;</em>
                    	 <select name="productQuota" style="width:160px !important; float:left !important; margin-right:5px;">
							 <option value="">Select</option>                                
                             <option value="LIMITED">LIMITED</option>
                             <option value="UNLIMITED">UNLIMITED</option>
                         </select>
                    </div>
                    <div class="red">
	                     <div class="form-row">
			                <div class="label" id="validity">Validity</div><em>&nbsp;&nbsp;</em>
			                <input type="radio" name="productValidity" value="infinite" />Infinite
			                <input type="radio" name="productValidity" value="hours" />Hours
			                <input type="text" name="validityHours" max="24" onkeypress="return isNumberKey(event)" maxlength="9" min="1" id="val_hours" style="width:16px;height:15px;display:none;">
			                <input type="radio" name="productValidity" value="days" />Days
			                <input type="text" name="validityDays" min="1" id="val_days" onkeypress="return isNumberKey(event)" maxlength="9" style="width:16px;height:15px;display:none;">
			                <input type="radio" name="productValidity" value="date" style="margin-left:180px;" />Date
			                <input type="text" placeholder="yyyy-mm-dd" attr-date="datepicker" name="validityDate" min="" id="val_date" style="width: 130px;display:none;">
			            </div>
			        </div>
                    <input type="button" value="Add" id="add-product" />

                    <div style="float:left;width:100%;">
                        <div class="table_wrapper">
                            <div class="header">
                                <div class="head1" style="width:80px;">Product Name</div>
                                <div class="head2" style="width:70px;">Owner</div>
                                <div class="head3" style="width:70px;">Resource</div>
                                <div class="head4" style="width:70px;">Quota</div>
                                <div class="head4" style="width:70px;">Validity</div>
                                <div class="head5">Action</div>
                            </div>
                            <div class="tbody">
                                <table>
                                    <tbody id="productList">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="price" style="margin-top:10px; width:100%;float:left;">
	            <div class="history-whitelist">Whitelisted Users</div>
	            <div id="panel-whitelist" style="margin-top:10px; width:95%;float:left;">
	                <div class="form-row">
	                    <div class="label">User Name</div><em>&nbsp;&nbsp;</em>
	                </div>
	                <div class="input-field">
	                    <input class="input-field-style mandatory constrained description" name="whitelistUser" id="group-name" style="float:left;">
	                    <input type="button" value="Add" id="add-whitelist" />
	                </div>
	
	                <div class="form-row">
	                    <div class="label" style="width: 42%; height: 30px; float: left;" id="groups-nonassigned-label">
	                    Whitelisted Users<em>&nbsp;&nbsp;</em>
	                    </div>
	
	                    <div style="float:left;width:100%;">
	                        <div id="assigned-fields-whitelistUser" class="total-kpi-fields select-kpi-fields-list" constraints="{" fieldLabel ":"Cdr Header Field ","mustSelect ":"true "}" multiple="multiple" style="height:100px;overflow-y:scroll; min-height:150px;;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:left;;width: 190px; background:#fff;" name="cdrHeaderField">
	                            <ul>
	                            </ul>
	                        </div>
	                    </div>
	                </div>
	            </div>
	        </div>
	        
	        <div style="float:left;width:100%;margin-top:15px;">
		        <div class="form-row">
		            <div class="label">Exit Offer Id</div><em>&nbsp;&nbsp;</em>
		        </div>
		        <div class="input-field">
		            <input class="input-field-style mandatory constrained description" name="exitOfferId" id="group-name" style="float:left;">
		        </div>
		    </div>
		    
		    <div style="float:left;width:100%;margin-top:10px;cursor:pointer;">
		    
		        <div class="form-row">
		            <div class="label" title="Available in full blown ECI"> <img src="images/expiry.2.png" id="prereneval" style="float:left; margin-right:3px;">Pre-Renewal</div><em></em>
		        </div>
		    </div>
		    
		    <div style="float:left;width:100%;margin-top:10px;cursor:pointer;">
		     
		        <div class="form-row">
		            <div class="label" title="Available in full blown ECI"><img src="images/expiry.2.png" id="prereneval" style="float:left; margin-right:3px;">Pre-Expiry</div><em></em>
		        </div>
		    </div>
		    
	        
        
        </div>
        <div  style="float:right;margin-top:15px;">
			<button type="button" value="Add Offer" id="save-btn" class="btn">Save</button>
			<button type="button" value="Add Offer" id="update-btn" class="btn">Update</button>
			<button type="button" value="Add Offer" id="cancel-btn" class="btn">Cancel</button>
		</div>
    </form>
    
</div>

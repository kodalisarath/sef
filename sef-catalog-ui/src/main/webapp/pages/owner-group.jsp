<script src="js/owner_group_manager.js"></script>
<script>
    $(document).ready(function () {
        jQuery('select[name="ownerType"]').change(function () {

            if (jQuery(this).val() === "opco-group") {
                jQuery('#opcogroup').show();
            } else {
                jQuery('#opcogroup').hide();
            }
        });

        jQuery('select[name="ownerType"]').change(function () {

            if (jQuery(this).val() === "opco") {
                jQuery('#opco').show();
            } else {
                jQuery('#opco').hide();
            }
        });
        
        //$('#content-group input[name="ownerName"]').val().empty();
    });
</script>
<div id="add-offer" style="float:left; width:100%;">
<button value="Add Owner" id="ui-btn-og">Add Owner</button>
</div>
<div id="content-group">
    <form id="create-resource">
        <div style="float:left; width:100%;">
            <div class="form-row">
                <div class="label">Owner Name</div><em>*</em>
            </div>
            <div class="input-field">
                <input class="input-field-style mandatory constrained" name="ownerName" id="ownerName">
            </div>
        </div>

        <div id="ownerdiv" style="float:left; width:100%;">
            <div class="form-row">
                <div class="label">Owner Type</div><em>*</em>
                <div style="float:left;">
                    <select name="ownerType" id="owner" style="background:#fff !important;width:160px; border-radius:10px !important; height:22px !important; border:1px solid #808080 !important;">
                        <option value="select" selected>Select</option>
                        <option value="opco-group">Opco Group</option>
                        <option value="opco">Opco</option>

                    </select>
                </div>
            </div>
        </div>


       
            <ul id="cbp-ntaccordion" class="cbp-ntaccordion">

                <li id="opcogroup" style="float:left; width:100%;">
                    <h3 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />OpcoGroup</a></h3>
                    <div class="cbp-ntcontent" style="margin-left:15px;width:100%; float:left;">
                        <ul class="cbp-ntsubaccordion">
                            <li>
                                <h4 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />Opco</a></h4>
                                <div class="cbp-ntcontent" style="margin-left:25px;">

                                    <div class="form-row">
                                        <div class="label" style="width:120px !important;">Opco Name</div><em>*</em>
                                    </div>
                                    <div class="input-field">
                                        <input class="input-field-style mandatory constrained"name="opconame" id="opconame">
                                    </div>

                                    <ul class="cbp-ntsubaccordion">
                                        <li>
                                            <h5 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />Market</a></h5>
                                            <div class="cbp-ntcontent" style="margin-left:30px;">
                                                <div class="form-row">
                                                    <div class="label">Market Name</div><em>*</em>
                                                </div>
                                                <div class="input-field">
	                                                <select id="market" name="market">
		                                                <option value="">Select</option>
		                                                <option value="">Mobile</option>
		                                                <option value="">TV</option>
		                                                <option value="">INTERNET</option>
		                                                <option value="">LANDLINE</option>
		                                            </select>
	                                            </div>
                                                
                                                <ul class="cbp-ntsubaccordion">
                                                    <li>
                                                        <h5 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />TenantMVNO</a></h5>
                                                        <div class="cbp-ntcontent" style="margin-left:30px;float:left;">
                                                            <div class="form-row">
                                                                <div class="label">Tenant Name</div><em>*</em>
                                                            </div>
                                                            <div class="input-field">
                                                                <input class="input-field-style mandatory constrained" name="tenantName" id="tenantName">
                                                            </div>
                                                            <ul class="cbp-ntsubaccordion">
                                                                <li>
                                                                    <h5 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />Partner</a></h5>
                                                                    <div class="cbp-ntcontent" style="margin-left:30px;">
                                                                        <div class="form-row" style="float:left;">
                                                                            <div class="label">Partner Name</div><em>*</em>
                                                                        </div>
                                                                        <div class="input-field">
                                                                            <input class="input-field-style mandatory constrained" name="partnerName" id="partnerName">
                                                                        </div>
                                                                    </div>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                    </li>
                                                    <li>
                                                        <h5 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />Partner</a></h5>
                                                        <div class="cbp-ntcontent" style="margin-left:30px;">
                                                            <div class="form-row" style="float:left;">
                                                                <div class="label">Partner Name</div><em>*</em>
                                                            </div>
                                                            <div class="input-field">
                                                                <input class="input-field-style mandatory constrained" name="partnerName1" id="partnerName1">
                                                            </div>
                                                        </div>
                                                    </li>

                                            </div>

                                        </li>

                                        <li>
                                            <h5 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />TenantMVNO</a></h5>
                                            <div class="cbp-ntcontent" style="margin-left:30px;">
                                                <div class="form-row" style="float:left;">
                                                    <div class="label">Tenant Name</div><em>*</em>
                                                </div>
                                                <div class="input-field">
                                                    <input class="input-field-style mandatory constrained" name="tenantName1" id="tenantName1">
                                                </div>
                                                <ul class="cbp-ntsubaccordion">
                                                    <li>
                                                        <h5 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />Partner</a></h5>
                                                        <div class="cbp-ntcontent" style="margin-left:30px;">
		                                                    <div class="form-row " style="float:left; ">
		                                                        <div class="label ">Partner Name</div><em>*</em>
		                                                    </div>
		                                                    <div class="input-field ">
		                                                        <input class="input-field-style mandatory constrained " name="partnerName2" id="partnerName2">
		                                                    </div>
		                                                </div>
		                                            </li>
		                                        </ul>
                                            </div>
                                        </li>
                                        <li>
                                            <h5 class="cbp-nttrigger "><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />Partner</a></h5>
                                            <div class="cbp-ntcontent " style="margin-left:30px;">
                                                <div class="form-row " style="float:left; ">
                                                    <div class="label ">Partner Name</div><em>*</em>
                                                </div>
                                                <div class="input-field ">
                                                    <input class="input-field-style mandatory constrained " name="partnerName3" id="partnerName3">
                                                </div>
                                            </div>
                                        </li>
                                        </ul>
                                </div>
                            </li>
                            <li>
                                <h4 class="cbp-nttrigger "><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />Partner</a></h4>
                                <div class="cbp-ntcontent " style="margin-left:30px;">
                                    <div class="form-row " style="float:left; ">
                                        <div class="label ">Partner Name</div><em>*</em>
                                    </div>
                                    <div class="input-field ">
                                        <input class="input-field-style mandatory constrained " name="partnerName4" id="partnerName4">
                                    </div>
                                </div>
                            </li>

                            </ul>
                    </div>
                </li>
               
            <li id="opco">
                <h3 class="cbp-nttrigger "><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />Opco</a></h3>
                <div class="cbp-ntcontent" style="margin-left:15px;" >
                    <ul class="cbp-ntsubaccordion ">
                        <li>
                        <h4 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />Market</a></h4>
                        <div class="cbp-ntcontent" style="margin-left:30px;">
                            <div class="form-row">
                                <div class="label">Market Name</div><em>*</em>
                            </div>
                            <div class="input-field">
	                            <select id="market" name="market">
	                                <option value="">Select</option>
	                                <option value="">Mobile</option>
	                                <option value="">TV</option>
	                                <option value="">INTERNET</option>
	                                <option value="">LANDLINE</option>
	                            </select>
	                        </div>
                            <ul class="cbp-ntsubaccordion">
                                <li>
                                    <h5 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />TenantMVNO</a></h5>
                                    <div class="cbp-ntcontent" style="margin-left:30px;">
                                        <div class="form-row">
                                            <div class="label">Tenant Name</div><em>*</em>
                                        </div>
                                        <div class="input-field">
                                            <input class="input-field-style mandatory constrained" name="tenantName2" id="tenantName2">
                                        </div>
                                        <ul class="cbp-ntsubaccordion">
                                            <li>
                                                <h5 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />Partner</a></h5>
                                                <div class="cbp-ntcontent" style="margin-left:30px;">
                                                    <div class="form-row" style="float:left;">
                                                        <div class="label">Partner Name</div><em>*</em>
                                                    </div>
                                                    <div class="input-field">
                                                        <input class="input-field-style mandatory constrained" name="partnerName5" id="partnerName5">
                                                    </div>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </li>
                                <li>
                                    <h5 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />Partner</a></h5>
                                    <div class="cbp-ntcontent">
                                        <div class="form-row" style="float:left;">
                                            <div class="label">Partner Name</div><em>*</em>
                                        </div>
                                        <div class="input-field">
                                            <input class="input-field-style mandatory constrained" name="partnerName6" id="partnerName6">
                                        </div>
                                    </div>
                                </li>

                        </div>

                    </li>

                    <li>
                        <h4 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />TenantMVNO</a></h4>
                        <div class="cbp-ntcontent" style="margin-left:30px;">
                            <div class="form-row" style="float:left;">
                                <div class="label">Tenant Name</div><em>*</em>
                            </div>
                            <div class="input-field">
                                <input class="input-field-style mandatory constrained" name="tenantName3" id="tenantName3">
                            </div>
                            <ul class="cbp-ntsubaccordion">
                                <li>
                                    <h5 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />Partner</a></h5>
                                    <div class="cbp-ntcontent" style="margin-left:30px;">
                                        <div class="form-row " style="float:left; ">
                                            <div class="label ">Partner Name</div><em>*</em>
                                        </div>
                                        <div class="input-field ">
                                            <input class="input-field-style mandatory constrained " name="partnerName7" id="partnerName7">
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </li>
                    <li>
                        <h4 class="cbp-nttrigger"><a href="#"><img src="images/collapse.png" style="vertical-align:middle;" />Partner</a></h4>
                        <div class="cbp-ntcontent" style="margin-left:30px;">
                            <div class="form-row" style="float:left; ">
                                <div class="label">Partner Name</div><em>*</em>
                            </div>
                            <div class="input-field ">
                                <input class="input-field-style mandatory constrained " name="partnerName8" id="partnerName8">
                            </div>
                        </div>
                    </li>
                    </ul>
            </div>
        </li>
       

            </ul>
    </form>
        <div  style="float:right; margin-top:15px;">
		<button value="Add Offer" id="save-btn-owner" class="btn">Save</button>
		<button value="Add Offer" id="update-btn-owner" class="btn">Update</button>
		<button value="Add Offer" id="cancel-btn-owner" class="btn">Cancel</button>
	</div>
    </div>

   
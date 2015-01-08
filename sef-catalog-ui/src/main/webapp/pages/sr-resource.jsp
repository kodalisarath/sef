<script src="js/service_registry.js"></script>
<script>
	$(document).ready(function() {
		$('input.resourceName').bind('keypress', function(event) {
			var regex = new RegExp("^[a-zA-Z0-9\b]+$");
			var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
			if (!regex.test(key)) {
				event.preventDefault();
				return false;
			}
		});
	});
</script>
<div id="add-offer" style="float:left; width:100%;">
	<button value="Add Resource" id="ui-btn1">Create Resource</button>
</div>
<div id="content-service">
	<form id="create-resource">
		<div class="form-row">
			<div class="label">Resource Name</div><em>*</em>
		</div>
		<div class="input-field">
			<input class="input-field-style mandatory constrained resourceName" type="text" name="name" maxlength="20" id="group-desc">
		</div>

		<div class="form-row">
			<div class="label">Description</div><em>&nbsp;&nbsp;</em>
		</div>
		<div class="input-field">
			<input class="input-field-style mandatory constrained" name="description" id="group-name">
		</div>

		<div style="width:100%; float:left;">
			<div class="form-row" style="margin-top:10px;">
				<div class="label">IsAbstract</div><em>&nbsp;&nbsp;</em>
				<div id="dd1">
					<input type="radio" id="id_radio1" name="abstract" value="true" />True
					<input type="radio" id="id_radio2" name="abstract" value="false" />False
				</div>
			</div>
		</div>
		<div class="cost">
			<div class="form-row">
				<div class="label">Cost</div><em>&nbsp;&nbsp;</em>
			</div>
			<div class="input-field">
				<input onkeypress="return isNumberKey(event)" maxlength="9" min="1" class="input-field-style mandatory constrained" type="text" name="cost" id="group-name">
			</div>
		</div>

		<div style="float:left; width:100%;">
			<div class="form-row">
				<div class="label">IsDiscoverable</div><em>&nbsp;&nbsp;</em>
				<input type="radio" name="discoverable" value="true" />True
				<input type="radio" name="discoverable" value="false" checked/>False
			</div>
		</div>

		<div class="form-row">
			<div class="label">IsExtrnally Consumed</div><em>&nbsp;&nbsp;</em>
			<input type="radio" name="externallyConsumed" value="true" />True
			<input type="radio" name="externallyConsumed" value="false" />False
		</div>
		<div style="float:left;width:100%;">
			<div class="form-row">
				<div class="label">IsConsumable</div><em>&nbsp;&nbsp;</em>
				<div id="dd2">
					<input type="radio" id="id_radio3" name="consumable" value="true" />True
					<input type="radio" id="id_radio4" name="consumable" value="false" />False
				</div>
			</div>

			<div class="unit" style="margin-top:15px;">

				<div style=" float:left; width:100%;">

					<div class="form-row">
						<div class="label">Consumption Unit Name</div><em>*</em>
					</div>
					<div class="input-field">
						<select class="input-field-style mandatory constrained" name="consumptionUnitName" id="group-name">
							<option value="PHP">PHP</option>
						</select>
					</div>
					<div style="float:left;width:100%; padding-top:5px;">
						<div class="form-row">
							<div class="label">Min Quota</div><em>&nbsp;&nbsp;</em>
						</div>
						<div class="input-field">
							<input onkeypress="return isNegativeNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained" type="text" name="enforcedMinQuota" id="group-name">
						</div>
					</div>
					<div class="form-row">
						<div class="label">Max Quota</div><em>&nbsp;&nbsp;</em>
					</div>
					<div class="input-field">
						<input onkeypress="return isNegativeNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained" type="text" name="enforcedMaxQuota" id="group-name">
					</div>
				</div>

			</div>
		</div>

		<div style="float:left; width:100%;">
			<div class="form-row">
				<div class="label">Owner Type</div><em>&nbsp;&nbsp;</em>
				<div id="dd5">
					<select name="ownerType">
						<option value="select">Select</option>
						<option value="opcogroup">Opco Grop</option>
						<option value="opco">Opco</option>
						<option value="market">Market</option>
						<option value="tenant">Tenant MVO</option>
						<option value="partner">Partner</option>
						<option value="enduser">End User</option>
					</select>
				</div>
			</div>
		</div>

		<div style="float:left; width:100%;">
			<div class="form-row">
				<div class="label">Fulfillment Profiles</div><em>&nbsp;&nbsp;</em>
				<select multiple="multiple" name="profileType" id="profileList" style="height:auto; min-height:150px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:left;width: 230px; background:#fff;">
				</select>
			</div>
		</div>
		<div style="float:left; width:100%;">
			<div class="form-row">
				<div class="label">Enhanced Fulfillment Profiles</div><em>&nbsp;&nbsp;</em>
				<select name="workflowType" multiple="multiple" id="workflowList" style="height:auto; min-height:150px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:left;width: 230px; background:#fff;">
				</select>
			</div>
		</div>

		<div class="price" style="margin-top: 0px !important; width: 100%; float: left; ">
			<div id="OFFERPROFILE" style="margin-bottom:5px; width: 94%;float: left;display: none;">
			</div>
		</div>

		<div style="float:left; width:100%; margin-top:10px;">
			<!-- <div class="form-row">
                <div class="label">Resource Group</div><em>&nbsp;&nbsp;</em>
            </div>
            <div class="input-field">
                <input class="input-field-style mandatory constrained" name="resourceGroup" id="group-name" />
            </div> -->
			<!-- 				<div class="form-row" > -->
			<!-- 				<form id="dd3"> -->
			<!-- 			    	<input type="radio"  name="Dependency" value="Dependency" />Dependency Check<br> -->
			<!-- 					<input type="radio"  name="Dependency" value="Cascade" />Cascade Remove -->
			<!-- 				</form> -->
			<!-- 				</div> -->
		</div>
	</form>
	<div style="margin-top: 48px;">
		<div class="price" style="margin-top:10px; width:100%;float:left;display:none;">
			<div class="pamInfo">Pam Imformation</div>
			<div id="pamInfo" style="margin-top:10px; width:95%;float:left;">
				<div class="form-row">
					<div class="label">pamServiceID</div><em>*</em>
				</div>
				<div class="input-field">
					<input onkeypress="return isNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained description" name="pamServiceID" id="group-name">
				</div>
				<div class="form-row">
					<div class="label">pamClassID</div><em>&nbsp;&nbsp;</em>
				</div>
				<div class="input-field">
					<input onkeypress="return isNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained description" name="pamClassID" id="group-name">
				</div>
				<div class="form-row">
					<div class="label">scheduleID</div><em>&nbsp;&nbsp;</em>
				</div>
				<div class="input-field">
					<input onkeypress="return isNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained description" name="scheduleID" id="group-name">
				</div>
				<div class="form-row">
					<div class="label">pamIndicator</div><em>&nbsp;&nbsp;</em>
				</div>
				<div class="input-field">
					<input onkeypress="return isNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained description" name="pamIndicator" id="group-name">
				</div>
				<input type="button" value="Add" id="add-pamInfo" />

				<div style="float:left;width:100%;">
					<div class="table_wrapper">
						<div class="header">
							<div class="head1">pamServiceID</div>
							<div class="head2">pamClassID</div>
							<div class="head3">scheduleID</div>
							<div class="head4">pamIndicator</div>
							<div class="head5">Action</div>
						</div>
						<div class="tbody">
							<table>
								<tbody id="pamInfoList">
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="price" style="margin-top:10px; width:100%;float:left;display:none;">
			<div class="serviceOffer">Service Offering</div>
			<div id="serviceOffer" style="margin-top:10px; width:95%;float:left;">
				<div class="form-row">
					<div class="label">serviceOfferingId</div><em>*</em>
				</div>
				<div class="input-field">
					<input onkeypress="return isNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained description" name="serviceOfferingId" id="group-name">
				</div>
				<div class="form-row">
					<div class="label">serviceOfferingActiveFlag</div><em>&nbsp;&nbsp;</em>
				</div>
				<div class="input-field">
					<input type="radio" class="input-field-style mandatory constrained description" name="serviceOfferingActiveFlag" id="group-name" value="true">True
					<input type="radio" class="input-field-style mandatory constrained description" name="serviceOfferingActiveFlag" id="group-name" value="false">False
				</div>
				<input type="button" value="Add" id="add-serviceOffer" />

				<div style="float:left;width:100%;">
					<div class="table_wrapper">
						<div class="header">
							<div class="head1" style="width:200px;">serviceOfferingId</div>
							<div class="head2" style="width:200px;">serviceOfferingActiveFlag</div>
							<div class="head5">Action</div>
						</div>
						<div class="tbody">
							<table>
								<tbody id="serviceOfferList">
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>



		<div class="price" style="margin-top:10px; width:100%;float:left;display:none;">
			<div class="daReversals">Dedicated Account Reversal</div>
			<div id="daReversals" style="margin-top:10px; width:95%;float:left;">
				<div class="form-row">
					<div class="label">DedicatedAccount InformationID</div><em>*</em>
				</div>
				<div class="input-field">
					<input onkeypress="return isNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained description" name="dedicatedAccountInformationID" id="group-name">
				</div>
				<div style="float:left;width:100%;">
					<div class="form-row">
						<div class="label">HoursToReverse</div><em>&nbsp;&nbsp;</em>
					</div>
					<div class="input-field">
						<input onkeypress="return isNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained description" name="hoursToReverse" id="group-name">
					</div>
				</div>
				<div class="form-row">
					<div class="label">AmountToReverse</div><em>&nbsp;&nbsp;</em>
				</div>
				<div class="input-field">
					<input onkeypress="return isNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained description" name="amountToReverse" id="group-name">
				</div>

				<input type="button" value="Add" id="add-daReversals" />

				<div style="float:left;width:100%;">
					<div class="table_wrapper">
						<div class="header">
							<div class="head1" style="width:190px;">DedicatedAccount InformationID</div>
							<div class="head2" style="width:100px;">HoursToReverse</div>
							<div class="head3" style="width:100px;">AmountToReverse</div>
							<div class="head5">Action</div>
						</div>
						<div class="tbody">
							<table>
								<tbody id="daReversalsList">
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="price" style="margin-top:10px; width:100%;float:left;display:none;">
			<div class="toReversals">Timer Offer Reversal</div>
			<div id="toReversals" style="margin-top:10px; width:95%;float:left;">
				<div class="form-row">
					<div class="label">OfferID</div><em>*</em>
				</div>
				<div class="input-field">
					<input onkeypress="return isNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained description" name="offerID" id="group-name">
				</div>
				<div class="form-row">
					<div class="label">DedicatedAccount InformationID</div><em>*</em>
				</div>
				<div class="input-field">
					<input onkeypress="return isNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained description" name="dedicatedAccountInformationID" id="group-name">
				</div>
				<div style="float:left;width:100%;">
					<div class="form-row">
						<div class="label">HoursToReverse</div><em>&nbsp;&nbsp;</em>
					</div>
					<div class="input-field">
						<input onkeypress="return isNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained description" name="hoursToReverse" id="group-name">
					</div>
				</div>
				<input type="button" value="Add" id="add-toReversals" />

				<div style="float:left;width:100%;">
					<div class="table_wrapper">
						<div class="header">
							<div class="head1" style="width:100px;">OfferID</div>
							<div class="head2" style="width:190px;">DedicatedAccount InformationID</div>
							<div class="head3" style="width:100px;">HoursToReverse</div>
							<div class="head5">Action</div>
						</div>
						<div class="tbody">
							<table>
								<tbody id="toReversalsList">
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>


		<div class="price" style="margin-top:10px; width:100%;float:left;display:none;">
			<div class="offerSelection">Offer Selection</div>
			<div id="offerSelection" style="margin-top:10px; width:95%;float:left;">
				<div class="form-row">
					<div class="label">offerIDFirst</div><em>*</em>
				</div>
				<div class="input-field">
					<input onkeypress="return isNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained description" name="offerIDFirst" id="group-name">
				</div>
				<div class="form-row">
					<div class="label">offerIDLast</div><em>*</em>
				</div>
				<div class="input-field">
					<input onkeypress="return isNumberKey(event)" maxlength="9" class="input-field-style mandatory constrained description" name="offerIDLast" id="group-name">
				</div>
				<input type="button" value="Add" id="add-offerSelection" />

				<div style="float:left;width:100%;">
					<div class="table_wrapper">
						<div class="header">
							<div class="head1" style="width:190px;">offerIDFirst</div>
							<div class="head2" style="width:190px;">offerIDLast</div>
							<div class="head5">Action</div>
						</div>
						<div class="tbody">
							<table>
								<tbody id="offerSelectionList">
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div style="float:right; margin-top:15px;">
			<button value="Add Offer" id="save-btn-res" class="btn" type="button">Save</button>
			<button value="Add Offer" id="update-btn-res" class="btn" type="button">Update</button>
			<button value="Add Offer" id="cancel-btn-res" class="btn" type="button">Cancel</button>
		</div>
	</div>

</div>

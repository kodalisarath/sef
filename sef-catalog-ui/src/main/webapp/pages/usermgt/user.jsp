<style>
#keyvalue1 {
	display: none;
}

#keyvalue2 {
	display: none;
}

</style>
<script src="js/usermgt/userHandler.js"></script>
<script>
	$(document).ready(function() {
		$('.actor').click(function() {
			$('#keyvalue1').slideToggle('slow');
			$('#keyvalue2').slideUp('slow');

		});
		$('.user').click(function() {
			$('#keyvalue2').slideToggle('slow');
			$("#keyvalue1").slideUp("slow");
		});
		$(document).on("click", ".addActorMeta", function() {
			$("#appendactor").append($("#actoradd").html()); 
			});
		$(document).on("click", ".addUserMeta", function() {
			$("#appenduser").append($("#useradd").html());
		});
		$(document).on("click", ".removeActorMeta", function() {
			$("#appendactor").children().last().remove();
			$("#appendactor").children().last().remove();
		
			    });
	
		$(document).on("click", ".removeUserMeta", function() {
			$("#appenduser").children().last().remove();
			$("#appenduser").children().last().remove();
		});
		});
</script>


<div id="content-user-mgt">
	<form id="create-user">

		<div class="form-row">
			<div class="label" style="float: left; width: 160px;">Name</div>
			<em>*</em>
		</div>
		<div class="input-field">
			<input class="input-field-style mandatory constrained offerName"
				name="name" id="group-name">
		</div>
		<div class="form-row">
			<div class="label" style="float: left; width: 160px;">User Name</div>
			<em>*</em>
		</div>
		<div class="input-field">
			<input class="input-field-style mandatory constrained offerName"
				name="userName" id="group-name">
		</div>
		
		<div class="form-row">
			<div class="label" style="float: left; width: 160px;">Credential Type</div>
			<em>*</em>
		</div>
		<div class="input-field">
			<select class="input-field-style mandatory constrained offerName"
				name="credentialType" id="group-name">
				<option value="PASSWORD">Password</option>
				<option value="UNAUTHENTICATED">Unauthenticated</option>
				<option value="CERTIFICATE">Certificate</option>
				<option value="PIN_OTP">Pin OTP</option>
			</select>
		</div>
		<div style="float:left; width:100%; margin-top:10px;">
			<div class="form-row">
				<div class="label" style="float: left; width: 160px;">Password</div>
				<em>*</em>
			</div>
			<div class="input-field">
				<input class="input-field-style mandatory constrained offerName"
					name="password" id="group-name">
			</div>
		</div>
		<div class="price" style="margin-top: 10px; width: 100%; float: left;">
			<div class="actor">Actor Meta</div>
			<div id="keyvalue1"
				style="margin-top: 10px; width: 95%; float: left;">
				<div id="actoradd">
					<div style="float: left; margin-right: 15px;">
						<div class="form-row">
							<div class="label" style="float: left; width: auto;">Key
								&nbsp;&nbsp;</div>
							<em>*</em> <input
								class="input-field-style mandatory constrained offerName"
								name="actorKey" id="group-name" style="width:135px;">
						</div>
					</div>
					<div style="float: left;">
						<div class="form-row">
							<div class="label" style="float: left; width: auto;">Value
								&nbsp; &nbsp;</div>
							<em>*</em> <input
								class="input-field-style mandatory constrained offerName"
								name="actorValue" id="group-name" style="width:135px;">
						</div>
					</div>
					
				</div>
				<div class="removeActorMeta"
						style="cursor: pointer; float: right; margin-top: -5px;">
						<img src="images/remove.png" title="Remove" />
					</div>
				<div class="addActorMeta"
					style="cursor: pointer; float: right; margin-top: -5px;">
					<img src="images/add.png" title="Add" />
				</div>
				
				
				

				<div id="appendactor"></div>
			</div>
		</div>
		<div class="price" style="margin-top: 10px; width: 100%; float: left;">
			<div class="user">User Meta</div>
			<div id="keyvalue2"
				style="margin-top: 10px; width: 95%; float: left;">
				<div id="useradd">
					<div style="float: left; margin-right: 15px;">
						<div class="form-row">
							<div class="label" style="float: left; width: auto;">Key
								&nbsp;&nbsp;</div>
							<em>*</em> <input
								class="input-field-style mandatory constrained offerName"
								name="userKey" id="group-name" style="width:135px;">
						</div>
					</div>
					<div style="float: left;">
						<div class="form-row">
							<div class="label" style="float: left; width: auto;">Value
								&nbsp; &nbsp;</div>
							<em>*</em> <input
								class="input-field-style mandatory constrained offerName"
								name="userValue" id="group-name" style="width:135px;">
						</div>
					</div>
				</div>
				<div class="removeUserMeta"
					style="cursor: pointer; float: right; margin-top: -5px;">
					<img src="images/remove.png"  title="Remove" />
				</div>
				<div class="addUserMeta"
					style="cursor: pointer; float: right; margin-top: -5px;">
					<img src="images/add.png" title="Add" />
				</div>
				
				
				<div id="appenduser"></div>
			</div>
		</div>

		<div style="width: 40%; float: left; margin-top: 10px;">
			<div class="label" style="width: 100%; height: 30px;"
				id="groups-assigned-label">
				Non-Assigned Groups <em>*</em>
			</div>

			<div>
				<select id="nonassigned-fields-user"
					class="total-kpi-fields select-kpi-fields-list" multiple="multiple"
					style="height:auto; min-height:220px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:left;width: 230px; background:#fff;" 
				name="cdrHeaderField">


				</select>
			</div>
		</div>

		<div id="assign-deassign-resource" class="navigate">
			<div style="margin-left:40px !important;">
				<center>
					<img border="0" src="images/right.png" id="img-right-actor"
						title="Move to Right">
				</center>
	
				<center>
					<img border="0" src="images/left.png" id="img-left-actor"
						title="Move to Left">
				</center>
			</div>
		</div>

		<div class="label"
			style="width: 42%; height: 30px; float: right; margin-top: 10px;"
			id="groups-nonassigned-label">
			Assigned Groups <em>*</em>
		</div>

		<div>
			<select id="assigned-fields-user"
				class="total-kpi-fields select-kpi-fields-list" constraints="{"
				fieldLabel":"Cdr Header
				Field","mustSelect":"true"}" multiple="multiple"
				style="height:auto; min-height:220px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:right;width: 230px; background:#fff;" 
				name="cdrHeaderField">

			</select>

		</div>

		<div style="width: 40%; float: left; margin-top: 10px;">
			<div class="label" style="width: 100%; height: 30px;"
				id="groups-assigned-label">
				Non-Assigned Privileges <em>*</em>
			</div>

			<div>
				<select id="nonassigned-fields-user-privileges"
					class="total-kpi-fields select-kpi-fields-list" multiple="multiple"
					style="height:auto; min-height:220px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:left;width: 230px; background:#fff;" 
				name="cdrHeaderField">

				</select>
			</div>
		</div>

		<div id="assign-deassign-resource" class="navigate">
			<div style="margin-left:40px !important;">
				<center>
					<img border="0" src="images/right.png" id="img-right-actor1"
						title="Move to Right">
				</center>
	
				<center>
					<img border="0" src="images/left.png" id="img-left-actor1"
						title="Move to Left">
				</center>
			</div>
		</div>
		<div class="label"
			style="width: 42%; height: 30px; float: right; margin-top: 10px;"
			id="groups-nonassigned-label">
			Assigned Privileges <em>*</em>
		</div>

		<div>
			<select id="assigned-fields-user-privileges"
				class="total-kpi-fields select-kpi-fields-list" constraints="{"
				fieldLabel":"Cdr Header
				Field","mustSelect":"true"}" multiple="multiple"
				style="height:auto; min-height:220px;border:1px solid #ccc; font-size:10px; font-family:Tahoma, Geneva, sans-serif; float:right;width: 230px; background:#fff;" 
				name="cdrHeaderField">
			</select>
		</div>

	</form>
	<div  style="float:left; margin-top:15px;margin-left:360px;">
		<button value="Add Offer" id="save-btn-user" class="btn">Save</button>
		<button value="Add Offer" id="update-btn-user" class="btn">Update</button>
		<button value="Add Offer" id="cancel-btn-user" class="btn">Cancel</button>
	</div>
</div>

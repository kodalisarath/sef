var OwnerGroupHandler = {
	loadOwnerGroups: function() {
		$("#content-middle .search").val('');
		$('#ws-search').hide();
		$("#resourcegroup-search").hide();
		$("#offergroup-search").hide();
		$("#offer-search").hide();
		$("#resource-search").hide();
		$('#middle_rule').hide();
		$("#rule_search").hide();
		$("#select-policy-rule").hide();
		$("#select-owner-group").hide();
		$("#select-offer-group").hide();
		$("#select-product-owner").hide();
		$('#select-product-resource').hide();
		$('#notification_search').hide();
		$('#notification-details').hide();
		$('#notification_create_content').hide();
		$('#privileges-content').hide();
		$('#middle').show();
		$('#middle3').hide();
		$('#middle2').show();
		$('#middle5').hide();
		$('#middle').height('515');
		$('#middle7').hide();
		$('#notification-search').hide();
		$('#content-group-mgt').hide();
		$('#rule-btn').hide();
		$('#update-btn-owner').hide();
		$('#ui-btn').hide();
		$('#ui-btn12').hide();
		$('#ui-btn1').hide();
		$('#ui-btn2').hide();
		$('#ui-btn-ocg').hide();
		$('#ui-btn-og').show();
		$("#cancel-btn").hide();
		$(".delete-link").hide();
		$(".edit-link").hide();

		$('#content-right').hide();
		$('#content-resource-group').hide();
		$('#add-resource-group').hide();
		$('#add-notification').hide();
		$('#notification-add').hide();
		$('#ui-btn123').hide();
		$('#content-right').hide();
		$('.ui-btn').hide();
		$('#ui-btn124').hide();
		$('#ui-btn1').hide();
		$('#ui-btn-ocg').hide();

		$('#content-right1').hide();
		$('#content-right2').hide();
		$('#content-service').hide();
		$('#content-profiles').hide();
		$('#content-policy').hide();
		$('#content-rule').hide();
		$('#offer-catalog-group-service').hide();
		$('#add-notification').hide();
		$('#notification-add').hide();
		$('#content-user-mgt').hide();
		$('#content-group-mgt').hide();


		$('#cancel-btn-owner').click(function() {
			if ($('#owner-group-menu').hasClass('active')) {
				OwnerGroupHandler.loadOwnerGroups();
				$("#content-group").hide();
			}
		});
		$('#save-btn-owner').click(function() {
			if ($('#owner-group-menu').hasClass('active')) {

				if (!ownervalidate($('#content-resource-group'))) {
					return;
				}
			}

		});
		var editOwnerGroupManager = " ";
		$(document).on("click", ".edit-link", function(e) {
			e.preventDefault();
			if ($('#owner-group-menu').hasClass('active')) {
				var ownerNameArry = $(this).attr('text').split(' ');

				if (ownerNameArry.length > 1) {
					editOwnerGroupManager = ownerNameArry[1];
				} else {
					editOwnerGroupManager = ownerNameArry[0];
				}

				//alert(editOwnerGroupManager);
				var owner = {};
				$.ajax({
					url: '/sef-catalog-ui/readOwner?ownerName=' + editOwnerGroupManager,
					type: "GET",
					async: false,
					success: function(data) {
						if (data.status == 'success') {
							owner = JSON.parse(data.responseString);
							$('#ownerName').val(editOwnerGroupManager);
							$('#ownerName').attr("disabled", true) 
			           	    $( "#owner option:selected" ).text(owner.type);
							if (owner.type == "OpcoGroup") {
								
								var allMembers = owner.allMembers;
								var allMembersLength = owner.allMembers.length;
								for (var j = 0; j < allMembersLength; j++) {
									if(allMembers[j].type == "Opco"){
										$('#content-group input[name="opconame"]').val(allMembers[j].name);
									}else if(allMembers[j].type == "Partner"){
										$('#content-group input[name="partnerName4"]').val(allMembers[j].name);
									}
									var allMembers1 = allMembers[j].allMembers;
									var allMembersLength1 = allMembers[j].allMembers.length;
									for (var i = 0; i < allMembersLength1; i++) {
										if(allMembers1[i].type == "Market"){
											$( "#market option:selected" ).text(allMembers1[i].name);
										}else if(allMembers1[i].type == "TenantMVNO"){
											$('#content-group input[name="tenantName1"]').val(allMembers1[i].name);
										}else if(allMembers1[i].type == "Partner"){
											$('#ownerdiv name[name="partnerName3"]').val(allMembers1[i].name);
										}
										var allMembers2 = allMembers1[i].allMembers;
										var allMembersLength2 = allMembers1[i].allMembers.length;
										for (var k = 0; k < allMembersLength2; k++) {
											if(allMembers1[k].type == "Partner"){
												$('#ownerdiv name[name="partnerName1"]').val(allMembers[k].name);
											}else if(allMembers1[k].type == "TenantMVNO"){
												$('#ownerdiv name[name="tenantName"]').val(allMembers[k].name);
											}else if(allMembers1[k].type == "Partner"){
												$('#ownerdiv name[name="partnerName2"]').val(allMembers[k].name);
											}

											var allMembers3 = allMembers2[k].allMembers;
											var allMembersLength3 = allMembers2[k].allMembers.length;
											for (var h = 0; h < allMembersLength3; h++) {
												if(allMembers1[h].type == "Partner"){
													$('#ownerdiv name[name="partnerName"]').val(allMembers[h].name);
												}
											}


										}

									}
		
								}
								
							} else if(owner.type == "Opco"){
								
								
							}
						} else {
							//alert(data.message);
						}
					}
				});
				
				$('#opcogroup').hide();
				$('#opco').hide();
				$("#content-group").show();
				$("#update-btn-owner").show();
				$("#cancel-btn-owner").show();
				$("#save-btn-owner").hide();
				$("#sog").show();
				$("#ui-btn-og").hide();

				$("#partner-name").hide();
				$("#opco-name").hide();
				// });
			}
		});

		var deleteOwnerGroupManager = "";

		$(document).on("click", ".delete-link", function(e) {
			e.preventDefault();
			if ($('#owner-group-menu').hasClass('active')) {
				var ownerNameArry = $(this).attr('text').split(' ');

				if (ownerNameArry.length > 1) {
					deleteOwnerGroupManager = ownerNameArry[1];
				} else {
					deleteOwnerGroupManager = ownerNameArry[0];
				}

				//alert(deleteOwnerGroupManager);
				//e.preventDefault(); // disable normal link function so that it doesn't refresh the page
				var docHeight = $(document).height(); //grab the height of the page
				var scrollTop = $(window).scrollTop(); //grab the px value from the top of the page to where you're scrolling
				$('.overlay-bg-delete').show().css({
					'height': docHeight
				}); //display your popup and set height to the page height
				$('.overlay-content-delete').css({
					'top': scrollTop + 20 + 'px'
				}); //set the content 20px from the window top
			}
		});
		$('.ok-btn').unbind('click').click(function() {
			//var deleteOwnerGroupManager = $(this).attr("text");
			//alert(deleteOwnerGroupManager);
			$.ajax({
				url: '/sef-catalog-ui/deleteOwner?ownerName=' + deleteOwnerGroupManager,
				type: "GET",
				async: false,
				success: function(data) {
					$('.overlay-bg-delete').hide();
					if (data.status == 'success') {
						//alert(data.message);
						$('#error_message').show();
						$('#myform_succloc').css('color', 'green').text(data.message);
						OwnerGroupHandler.loadOwnerGroups();
					} else {
						//alert(data.message);
						$('#error_message').show();
						$('#myform_succloc').css('color', 'red').text(data.message);
						OwnerGroupHandler.loadOwnerGroups();
					}
				}
			});
		});
		$('.close-btn').unbind('click').click(function() {
			$('.overlay-bg-delete').hide(); // hide the overlay
		});

		// hides the popup if user clicks anywhere outside the container
		$('.overlay-bg-delete').unbind('click').click(function() {
				$('.overlay-bg-delete').hide();
			})
			// prevents the overlay from closing if user clicks inside the popup overlay
		$('.overlay-content-delete').unbind('click').click(function() {
			return false;
		});


		$('#update-btn-owner').unbind('click').click(function(e) {
			e.preventDefault();

			if ($('#owner-group-menu').hasClass('active')) {
				/*var ownerName = $('#content-group input[name="ownerName"]').val();
		   			  var ownerType = $( "#owner option:selected" ).text();
		   			  var opcoName = $('#content-group input[name="opconame"]').val();
		   			  var partnerName = $('#content-group input[name="partnername"]').val();
		   	          var url='/sef-catalog-ui/updateOwner?ownerName=' + ownerName +'&ownerType=' + ownerType
		   	               +'&opcoName=' + opcoName+'&partnerName=' + partnerName ;*/
				var ownerName = $('#content-group input[name="ownerName"]').val();
				json.name = ownerName;
				var ownerType = $("#owner option:selected").text();
				json.ownerType = ownerType;
				var opcoName = $('#content-group input[name="opconame"]').val();
				json.opcoName = opcoName;
				var marketName = $("#market option:selected").text();
				json.marketName = marketName;
				var tenantName = $('#content-group input[name="tenantName"]').val();
				json.tenantName = tenantName;
				var partnerName = $('#content-group input[name="partnerName"]').val();
				json.partnerName = partnerName;
				var partnerName1 = $('#content-group input[name="partnerName1"]').val();
				json.partnerName1 = partnerName1;
				var tenantName1 = $('#content-group input[name="tenantName1"]').val();
				json.tenantName1 = tenantName1;
				var partnerName2 = $('#content-group input[name="partnerName1"]').val();
				json.partnerName2 = partnerName2;
				var partnerName3 = $('#content-group input[name="partnerName3"]').val();
				json.partnerName3 = partnerName3;
				var partnerName4 = $('#content-group input[name="partnerName4"]').val();
				json.partnerName4 = partnerName4;
				var tenantName2 = $('#content-group input[name="tenantName2"]').val();
				json.tenantName2 = tenantName2;
				var tenantName3 = $('#content-group input[name="tenantName3"]').val();
				json.tenantName3 = tenantName3;
				var partnerName5 = $('#content-group input[name="partnerName5"]').val();
				json.partnerName5 = partnerName5;
				var partnerName6 = $('#content-group input[name="partnerName6"]').val();
				json.partnerName6 = partnerName6;
				var partnerName7 = $('#content-group input[name="partnerName7"]').val();
				json.partnerName7 = partnerName7;
				var partnerName8 = $('#content-group input[name="partnerName8"]').val();
				json.partnerName8 = partnerName8;

				$.ajax({
					url: '/sef-catalog-ui/updateOwner',
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(json),
					success: function(data) {
						if (data.status == 'success') {
							//alert(data.message);
							$('#error_message').show();$('#myform_succloc').css('color', 'green').text(data.message);
							OwnerGroupHandler.loadOwnerGroups();
						} else {
							//alert(data.message);
							//OwnerGroupHandler.loadOwnerGroups();
						}
					}
				});
				$("#content-group").hide();
				$("#sog").hide();
				$("#update-btn-owner").hide();
				$("#cancel-btn-owner").hide();
				$("#partner-name").hide();
				$("#opco-name").hide();
			}
		});


		$.ajax({
			url: '/sef-catalog-ui/getAllOwners',
			type: "GET",
			async: false,
			contentType: 'application/json',
			success: function(data) {
				// alert(data.responseString);
				json = JSON.parse(data.responseString);
				ownerGroupData1 = [];
				var owners = json.owners;

				var ownersLength = json.owners.length;
				for (var key = 0; key < ownersLength; key++) {
					var owner = {};

					owner.text = "<" + owners[key].type + ">" + " " + owners[key].name;
					owner.expanded = false;
					owner.items = [];
					owner.spriteCssClass = "rootfolder";

					var allMembers = owners[key].allMembers;
					var allMembersLength = owners[key].allMembers.length;
					for (var j = 0; j < allMembersLength; j++) {
						var members = {};
						members.expanded = false;
						members.name = allMembers[j].name;

						members.spriteCssClass = "roorfolder";
						members.text = "<" + allMembers[j].type + ">" + " " + allMembers[j].name;
						members.items = [];
						var allMembers1 = allMembers[j].allMembers;
						var allMembersLength1 = allMembers[j].allMembers.length;
						for (var i = 0; i < allMembersLength1; i++) {
							var secondMembers = {};
							secondMembers.expanded = false;
							secondMembers.name = allMembers1[i].name;

							secondMembers.spriteCssClass = "roorfolder";
							secondMembers.text = "<" + allMembers1[i].type + ">" + " " + allMembers1[i].name;
							secondMembers.items = [];

							var allMembers2 = allMembers1[i].allMembers;
							var allMembersLength2 = allMembers1[i].allMembers.length;
							for (var k = 0; k < allMembersLength2; k++) {
								var thirdMembers = {};
								thirdMembers.expanded = false;
								thirdMembers.name = allMembers2[k].name;

								thirdMembers.spriteCssClass = "roorfolder";
								thirdMembers.text = "<" + allMembers2[k].type + ">" + " " + allMembers2[k].name;
								thirdMembers.items = [];

								var allMembers3 = allMembers2[k].allMembers;
								var allMembersLength3 = allMembers2[k].allMembers.length;
								for (var h = 0; h < allMembersLength3; h++) {
									var fourMembers = {};
									fourMembers.expanded = false;
									fourMembers.name = allMembers3[h].name;

									fourMembers.spriteCssClass = "roorfolder";
									fourMembers.text = "<" + allMembers3[h].type + ">" + " " + allMembers3[h].name;
									fourMembers.items = [];
									thirdMembers.items.push(fourMembers);
								}


								secondMembers.items.push(thirdMembers);
							}

							members.items.push(secondMembers);
						}


						owner.items.push(members);

					}

					ownerGroupData1.push(owner);
				}

				if ($('#middle')
					.hasClass('k-treeview')) {
					treeview = $("#middle").data("kendoTreeView");
					treeview.setDataSource(new kendo.data.HierarchicalDataSource({
						data: ownerGroupData1
					}));

				} else {
					$treeview = $("#middle").kendoTreeView({
						template: kendo.template($("#middle-template").html()),
						dataSource: ownerGroupData1,
					}).data("kendoTreeView");
				}
				if($('#hasOwnerGroupDelete').val() === 'false'){
					$('.delete-link').hide();
				}
				$("#middle div span.k-checkbox").hide();
				$('li ul li .delete-link').show();
				$('li ul li .edit-link').show();
			}
		});

		$("#middle").mCustomScrollbar("destroy");

		/* all available option parameters with their default values */
		$("#middle").mCustomScrollbar({
			setWidth: false,
			setHeight: false,
			setTop: 0,
			setLeft: 0,
			axis: "y",
			scrollbarPosition: "inside",
			scrollInertia: 950,
			autoDraggerLength: true,
			autoHideScrollbar: false,
			autoExpandScrollbar: false,
			alwaysShowScrollbar: 0,
			snapAmount: null,
			snapOffset: 0,
			mouseWheel: {
				enable: true,
				scrollAmount: "auto",
				axis: "y",
				preventDefault: false,
				deltaFactor: "auto",
				normalizeDelta: false,
				invert: false
			},
			scrollButtons: {
				enable: false,
				scrollType: "stepless",
				scrollAmount: "auto"
			},
			keyboard: {
				enable: true,
				scrollType: "stepless",
				scrollAmount: "auto"
			},
			contentTouchScroll: 25,
			advanced: {
				autoExpandHorizontalScroll: false,
				autoScrollOnFocus: "input,textarea,select,button,datalist,keygen,a[tabindex],area,object,[contenteditable='true']",
				updateOnContentResize: true,
				updateOnSelectorLength: false
			},
			theme: "light",
			callbacks: {
				onScrollStart: false,
				onScroll: false,
				onTotalScroll: false,
				onTotalScrollBack: false,
				whileScrolling: false,
				onTotalScrollOffset: 0,
				onTotalScrollBackOffset: 0,
				alwaysTriggerOffsets: true
			},
			live: false
		});
		
		
		// click on create owner button click  
		$(document).ready(function() {
			$('#ui-btn-og').click(function() {
				//document.getElementById("owner").value= "";
				/*document.getElementById("ownerName").value= "";
	                 document.getElementById("opconame").value= "";
	                 document.getElementById("tenantName").value= "";
	                 document.getElementById("partnerName").value= "";
	                 document.getElementById("partnerName1").value= "";
	                 document.getElementById("tenantName1").value= "";
	                 document.getElementById("partnerName1").value= "";
	                 document.getElementById("partnerName3").value= "";
	                 document.getElementById("partnerName4").value= "";
	                 document.getElementById("partnerName2").value= "";*/
				$('#content-group').find('input:text, input:password, input:file, select, textarea').val('');
				$("#content-service").hide();
				$("#content-group").show();
				$("#content-right").hide();
				$('#opcogroup').hide();
				$('#opco').hide();
				//$("#opco-name1").hide();
				$("#sog").hide();
				$("#ui-btn-og").hide();
				/*$("#partner-name").show();
				$("#opco-name").show();*/
				$("#save-btn-owner").show();
				$("#cancel-btn-owner").show();
				$("#delete-btn-owner").hide();
				$("#update-btn-owner").hide();
				$('.merge-btns').show();

				// check uniqueness of owner name in ownerStore.ccm file
				$("#ownerName").blur(function() {
					var ownerName = $('#ownerName').val();
					$.ajax({
						url: '/sef-catalog-ui/isOwnerExists?ownerName=' + ownerName,
						type: "GET",
						contentType: 'application/json',
						success: function(data) {
							if (data.status == 'success') {
								/*$('#right1').html('<div id="right1_succloc" style="color:#368606;float:left; width:100%; display:none;"></div>');
	  	                        $('#right1_succloc').show().text(data.message);*/
							} else {
								$('#right1').html('<div id="right1_errorloc" style="color:#ff0000;float:left; width:100%; display:none;"></div>');
								$('#right1_errorloc').show().text(data.message);
							}
						}
					});
				});
			});
		});
	}
};
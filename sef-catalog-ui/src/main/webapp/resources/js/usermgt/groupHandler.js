var GroupHandler = {

	loadGroups: function() {
		$("#content-group-search").val('');
		$('#rule-btn').hide();
		$('#ws-search').hide();
		$('#middle_rule').hide();
		$("#resourcegroup-search").hide();
		$("#offergroup-search").hide();
		$("#offer-search").hide();
		$("#resource-search").hide();
		$("#privileges-search").hide();
		$("#rule_search").hide();
		$('#notification-details').hide();
		$('#middle').height('515');
		$("#select-policy-rule").hide();
		$("#select-owner-group").hide();
		$("#select-offer-group").hide();
		$("#select-product-owner").hide();
		$('#select-product-resource').hide();
		$('#notification_create_content').hide();
		$('#ui-btn12').show();
		$('#middle8').hide();
		$('#middle7').hide();
		$('#middle5').show();
		$('#middle').show();
		$('#middle2').hide();
		$('#middle3').hide();
		$('#content-user-mgt').hide();
		$('#ui-btn').hide();
		$('#notification-add').hide();
		$('#notification-edit').hide();
		$('#editn-btn').hide();
		$('#addn-btn').hide();
		$('#ui-btn124').hide();
		$('#ui-btn123').hide();
		$('#privileges-content').hide();
		$('#notification_search').hide();
		//$('#middle').html('');

		$('#save-btn-group').unbind('click').click(function() {
			if ($('#group-menu').hasClass('active')) {
				var json = {};
				var name = $('#create-group input[name="userName"]').val();
				var description = $('#create-group input[name="description"]').val();
				var url = '/sef-catalog-ui/createGroup?name=' + name + '&description=' + description;
				var privileges = null;
				json.privileges = [];
				json.name = name;
				json.description = description;
				json.offers = [];
				json.resources = [];
				json.offerGroup = [];
				json.resourceGroup = [];
				json.rules = [];
				json.owner = [];
				json.users = [];
				json.groups = [];
				json.notification = [];
				
				function getValueUsingClass() {
					/* declare an checkbox array */
					var chkArray = [];
					/* look for all checkboes that have a class 'chk' attached to it and check if it was checked */
					$(".chk:checked").each(function() {
						if ($(this).attr('name') === 'offers') {
							json.offers.push($(this).val());
						} else if ($(this).attr('name') === 'resources') {
							json.resources.push($(this).val());
						} else if ($(this).attr('name') === 'offerGroup') {
							json.offerGroup.push($(this).val());
						} else if ($(this).attr('name') === 'resourceGroup') {
							json.resourceGroup.push($(this).val());
						} else if ($(this).attr('name') === 'rules') {
							json.rules.push($(this).val());
						} else if ($(this).attr('name') === 'owner') {
							json.owner.push($(this).val());
						} else if ($(this).attr('name') === 'users') {
							json.users.push($(this).val());
						} else if ($(this).attr('name') === 'groups') {
							json.groups.push($(this).val());
						} else if ($(this).attr('name') === 'notification') {
							json.notification.push($(this).val());
						}
						/*chkArray.push($(this).val());*/
					});

				}
				
				getValueUsingClass();

				
				$.ajax({
					url: '/sef-catalog-ui/createGroup',
					type: "POST",
					data: JSON.stringify(json),
					contentType: 'application/json',
					success: function(data) {
						if (data.status == 'success') {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'green').text('Group Created Successfully');
							GroupHandler.loadGroups();
						} else {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'red').text(data.message);
						}

					}

				});
			}
		});



		$(document).ready(function() {
			page = 1;
			$.ajax({
				url: '/sef-catalog-ui/getAllGroups?searchQuery=' + document.getElementById('content-group-search').value + '&pageNumber=' + page + '&pageSize=30',
				type: "GET",
				async: false,
				contentType: 'application/json',
				success: function(data) {
					json = JSON.parse(data.responseString);

					groupData = [];

					var myStringArray = json.groups;
					var arrayLength = myStringArray.length;
					for (var i = 0; i < arrayLength; i++) {

						var group = {};
						group.text = myStringArray[i].name;
						group.expanded = false;
						group.items = [];
						group.spriteCssClass = "pdf";
						groupData.push(group);
					}


					if ($('#middle')
						.hasClass('k-treeview')) {
						treeview = $("#middle").data("kendoTreeView");

						treeview.setDataSource(new kendo.data.HierarchicalDataSource({
							data: groupData,
						}));


					} else {
						treeview = $("#middle").kendoTreeView({
							//template: "#= item.text # (#=  #)",
							template: kendo.template($("#middle-template").html()),
							dataSource: groupData,
							//expand: onExpand,
							//select: onSelect,
						}).data("kendoTreeView");
					}

				}

			});
		});
		$("#middle div span.k-checkbox").hide();
		if($('#hasGroupDelete').val() === 'false'){
			$('.delete-link').hide();
		}
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

		/*Global variable for check that current right
		  left move of privilege id for edit or update.*/
		var action = "";


		$(document).off("click", ".edit-link").on("click", ".edit-link", function(e) {
			e.preventDefault();
			$("#save-btn-group").hide();
			$('#group-name9').attr("disabled", true)
			action = "update";

			if ($('#group-menu').hasClass('active')) {
				var groupName = $(this).attr("text");
				var string = encodeURIComponent(groupName);
				var groups = {};
				$.ajax({
					url: '/sef-catalog-ui/readGroup?groupName='+string,
					type: "GET",
					async: false,
					success: function(data) {
						groups = JSON.parse(data.responseString);
						var list = groups.privileges;
						$('#group-name9').val(groups.name);
						$('#group-desc9').val(groups.description);
						for (var i = 0; i < list.length; i++) {
							$('#create-group input[value="' + list[i].name.toLowerCase() + '"]').prop('checked', true);
						}
						//alert(groups.privileges.get(0));
					}
				});

				$(this).closest('li').find('ul .edit-link').each(function() {
					var newOption = '<option value="' + $(this).attr("text") + '" class="optionChecked">' + $(this).attr("text") + '</option>';
				});
				$("#ui-btn12").show();
				$("#cancel-btn-group").show();
				$("#update-btn-group").show();
				$("#prod-grp-displays").hide();
				$('#add-offer-catalog-group').hide();
				$("#content-group-mgt").show();
				$('#assigned-prod-group-btn').addClass("offer_group_edit");
				$('#assign-deassign-offer').addClass("offer_assign_deassign");
			}

		});

		var deleteGroup = "";

		$(document).off("click", ".delete-link").on("click", ".delete-link", function(e) {

			if ($('#group-menu').hasClass('active')) {
				deleteGroup = $(this).attr("text");
				//e.preventDefault(); 
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
			//deleteGroup = $(this).attr("text");
			if (deleteGroup != undefined) {
				$.ajax({
					url: '/sef-catalog-ui/deleteGroup?groupName=' + deleteGroup,
					type: "GET",
					contentType: 'application/json',
					success: function(data) {
						$('.overlay-bg-delete').hide();
						if (data.status == 'success') {
							json = JSON.parse(data.responseString);
							$('#error_message').show();
							$('#myform_succloc').css('color', 'green').text("Group deleted successfully");
							$('#content-group-search').keyup();

						} else {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'red').text(data.message);
						}
					}
				});
			}
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


		$('#middle').show();
		$('#middle2').hide();
		$('#ui-btn-og').hide();
		$('#ui-btn').hide();
		$('#ui-btn12').show();
		$('#ui-btn1').hide();
		$("#cancel-btn-group").hide();
		$("#save-btn-group").hide();
		$("#update-btn-group").hide();
		$('#content-right').hide();
		$('#content-right1').hide();
		$('#content-right2').hide();
		$('#content-service').show(); //show//
		$('#content-profiles').hide();
		$('#content-policy').hide();
		$('#content-rule').hide();
		$('#content-group').hide();
		$('.ui-btn-user').hide();
		$("#content-group-mgt").hide();
		$('#content-resource-group').hide();
		$('#add-resource-group').hide();
		$('#offer-catalog-group-service').hide();
		$('#add-offer-catalog-group').hide();
		$('#ui-btn123').hide();

		$('#create-owner-group').hide();
		$('#add-owner-group').hide();
		$('#content-right').hide();
		$('.ui-btn').hide();
		$('#ui-btn1').hide();
		$('#content-right1').hide();
		$('#content-right2').hide();
		$('#content-service').hide();
		$('#content-profiles').hide();
		$('#content-policy').hide();


		$(document).off("keyup", "#content-group-search").on("keyup", "#content-group-search", function(e) {
			page = 1;
			$.ajax({
				url: '/sef-catalog-ui/getAllGroups?searchQuery=' + document.getElementById('content-group-search').value + '&pageNumber=' + page + '&pageSize=30',
				type: "GET",
				async: false,
				contentType: 'application/json',
				success: function(data) {
					json = JSON.parse(data.responseString);

					groupData = [];

					var myStringArray = json.groups;
					var arrayLength = myStringArray.length;
					for (var i = 0; i < arrayLength; i++) {

						var group = {};
						group.text = myStringArray[i].name;
						group.expanded = false;
						group.items = [];
						group.spriteCssClass = "pdf";

						//                    var profileArray = myStringArray[i].fulfillmentProfiles;
						//                    if (profileArray && profileArray != null && profileArray != undefined) {
						//
						//                        var profileLength = profileArray.length;
						//
						//                        for (var j = 0; j < profileLength; j++) {
						//                            var profile = {};
						//                            profile.text = profileArray[j];
						//                            profile.expanded = false;
						//                            profile.spriteCssClass = "html";
						//                            resource.items.push(profile);
						//                        }
						//                    }
						groupData.push(group);
					}


					if ($('#middle')
						.hasClass('k-treeview')) {
						treeview = $("#middle").data("kendoTreeView");

						treeview.setDataSource(new kendo.data.HierarchicalDataSource({
							data: groupData,
						}));


					} else {
						treeview = $("#middle").kendoTreeView({
							//template: "#= item.text # (#=  #)",
							template: kendo.template($("#middle-template").html()),
							dataSource: groupData,
							//expand: onExpand,
							//select: onSelect,
						}).data("kendoTreeView");
					}
					$("#middle div span.k-checkbox").hide();
					

				}

			});
		});


		$(document).ready(function() {
			$('#ui-btn12').unbind('click').click(function() {
				$('input[type=checkbox]').prop('checked', false);
				document.getElementById("group-name9").value = "";
				document.getElementById("group-desc9").value = "";
				action = "update";
				$("#content-group-mgt").show(); //show//
				$("#content-right").hide();
				$('#group-name9').attr("disabled", false)
				$("#cancel-btn-group").show();
				$("#save-btn-group").show();
				// $('#content-group-mgt').html('');
				$("#update-btn-group").hide();
				$('#group-name9').empty();
				$('#group-desc9').empty();
			});
		});


		//on click on cancel -btn


		$('#cancel-btn-group').unbind('click').click(function() {
			if ($('#group-menu').hasClass('active')) {
				//GroupHandler.loadGroups();
				
				$('#ui-btn123').hide();
				$("#content-user-mgt").hide();
				$("#content-group-mgt").hide();
				$("#content-service").hide();
				$('#content-group-mgt').clearForm();
				$('#group-name9').html('');
				$('#group-desc9').html('');
			}
		});
		$('#update-btn-group').unbind('click').click(function() {

			if ($('#group-menu').hasClass('active')) {
				var json = {};
				var name = $('#create-group input[name="userName"]').val();
				var description = $('#create-group input[name="description"]').val();
				var url = '/sef-catalog-ui/createGroup?name=' + name + '&description=' + description;
				var privileges = null;
				json.privileges = [];
				json.name = name;
				json.description = description;
				json.offers = [];
				json.resources = [];
				json.offerGroup = [];
				json.resourceGroup = [];
				json.rules = [];
				json.owner = [];
				json.users = [];
				json.groups = [];
				json.notification = [];
				
				function getValueUsingClass() {
						/* declare an checkbox array */
						var chkArray = [];
						/* look for all checkboes that have a class 'chk' attached to it and check if it was checked */
						$(".chk:checked").each(function() {
							if ($(this).attr('name') === 'offers') {
								json.offers.push($(this).val());
							} else if ($(this).attr('name') === 'resources') {
								json.resources.push($(this).val());
							} else if ($(this).attr('name') === 'offerGroup') {
								json.offerGroup.push($(this).val());
							} else if ($(this).attr('name') === 'resourceGroup') {
								json.resourceGroup.push($(this).val());
							} else if ($(this).attr('name') === 'rules') {
								json.rules.push($(this).val());
							} else if ($(this).attr('name') === 'owner') {
								json.owner.push($(this).val());
							} else if ($(this).attr('name') === 'users') {
								json.users.push($(this).val());
							} else if ($(this).attr('name') === 'groups') {
								json.groups.push($(this).val());
							} else if ($(this).attr('name') === 'notification') {
								json.notification.push($(this).val());
							}
							/*chkArray.push($(this).val());*/
						});

					}
				
				getValueUsingClass();
				$.ajax({
					url: '/sef-catalog-ui/updateGroup',
					type: "POST",
					data: JSON.stringify(json),
					contentType: 'application/json',
					success: function(data) {
						if (data.status == 'success') {
							$("#content-group-mgt").hide();
							$('#error_message').show();
							$('#myform_succloc').css('color', 'green').text('Group Updated Successfully');
						} else {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'red').text(data.message);
						}

					}

				});
			}



		});

	}

};
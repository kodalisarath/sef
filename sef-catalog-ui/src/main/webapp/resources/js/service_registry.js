var ResourceHandler = {
	loadResources: function() {
		$('#resource-search').show();
		$('#offer-search').hide();
		$("#resourcegroup-search").hide();
		$("#offergroup-search").hide();
		$("#rule_search").hide();
		$("#privileges-search").hide();
		$('#middle5').hide();
		$('#middle3').hide();
		$('#ws-search').hide();
		$('#middle_rule').hide();
		$("#select-policy-rule").hide();
		$("#select-owner-group").hide();
		$("#select-offer-group").hide();
		$("#select-product-owner").hide();
		$('#select-product-resource').hide();
		$('#middle').height('515');
		$('#notification-details').hide();
		$('#middle8').hide();
		$('#middle7').hide();
		$('#notification-add').hide();
		$('#notification-edit').hide();
		$('#editn-btn').hide();
		$('#addn-btn').hide();
		$('#rule-btn').hide();
		$('#notification_search').hide();
		$('#notification_create_content').hide();
		$('#content-user-mgt').hide();
		$('#content-group-mgt').hide();
		$('#privileges-content').hide();
		$('#ui-btn124').hide();
		$('#ui-btn123').hide();

		$('#cancel-btn-res').unbind('click').click(function() {
			if ($('#resource-menu').hasClass('active')) {
				$('#ui-btn1').show();
				$('#ui-btn12').hide();
				$("#content-service").hide();
			}
		});

		$(".pamInfo").unbind('click').click(function() {
			$('#pamInfo').find('input:text, input:password, input:file, select, textarea').val('');
			$('#pamInfo').find('input:radio, input:checkbox')
				.removeAttr('checked').removeAttr('selected');
			$("#pamInfo").slideToggle("slow");
		});

		$('#add-pamInfo').unbind('click').click(function() {

			if ($("#pamInfo input[name='pamServiceID']").val() === "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('pamServiceID cannot be empty.');
				return false;
			}
			//
			//			var isExist = false;
			//			$('#productList tr').each(function() {
			//				if ($(this).find('td:first').text() === $("#pamInfo input[name='productName']").val()) {
			//					$('#error_message').show();
			//					$('#myform_succloc').css('color', 'red').text('This Product already exist, Use different name.');
			//					isExist = true;
			//					return false;
			//				}
			//			});
			//			if (isExist) {
			//				return false;
			//			}
			$('#pamInfoList').append('<tr><td class="td1">' + $("#pamInfo input[name='pamServiceID']").val() + '</td><td class="td2">' + $("#pamInfo input[name='pamClassID']").val() + '</td><td class="td3">' + $("#pamInfo input[name='scheduleID']").val() + '</td><td class="td4">' + $("#pamInfo input[name='pamIndicator']").val() + '</td><td class="td5"><img src="images/delete-product.png" class="deleteResourceExtraInfo" title="Delete Product" /></td></tr>');
			$('#pamInfo').find('input:text').val('');
		});


		$(document).off('click', '.deleteResourceExtraInfo').on('click', '.deleteResourceExtraInfo', function() {
			$(this).closest('tr').remove();
		});


		$(".serviceOffer").unbind('click').click(function() {
			$('#serviceOffer').find('input:text, input:password, input:file, select, textarea').val('');
			$('#serviceOffer').find('input:radio, input:checkbox')
				.removeAttr('checked').removeAttr('selected');
			$("#serviceOffer").slideToggle("slow");
		});

		$('#add-serviceOffer').unbind('click').click(function() {

			if ($("#serviceOffer input[name='serviceOfferingId']").val() === "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('serviceOfferingId cannot be empty.');
				return false;
			}
			//
			//			var isExist = false;
			//			$('#productList tr').each(function() {
			//				if ($(this).find('td:first').text() === $("#serviceOffer input[name='productName']").val()) {
			//					$('#error_message').show();
			//					$('#myform_succloc').css('color', 'red').text('This Product already exist, Use different name.');
			//					isExist = true;
			//					return false;
			//				}
			//			});
			//			if (isExist) {
			//				return false;
			//			}
			$('#serviceOfferList').append('<tr><td class="td1" style="width:200px;">' + $("#serviceOffer input[name='serviceOfferingId']").val() + '</td><td class="td2" style="width:200px;">' + (($("#serviceOffer input[name='serviceOfferingActiveFlag']:checked").val() == undefined) ? "" : $("#serviceOffer input[name='serviceOfferingActiveFlag']:checked").val()) + '</td><td class="td5"><img src="images/delete-product.png" class="deleteResourceExtraInfo" title="Delete Product" /></td></tr>');
			$('#serviceOffer').find('input:text').val('');
		});


		$(".daReversals").unbind('click').click(function() {
			$('#daReversals').find('input:text, input:password, input:file, select, textarea').val('');
			$('#daReversals').find('input:radio, input:checkbox')
				.removeAttr('checked').removeAttr('selected');
			$("#daReversals").slideToggle("slow");
		});

		$('#add-daReversals').unbind('click').click(function() {

			if ($("#daReversals input[name='dedicatedAccountInformationID']").val() === "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('dedicatedAccountInformationID cannot be empty.');
				return false;
			}
			//
			//			var isExist = false;
			//			$('#productList tr').each(function() {
			//				if ($(this).find('td:first').text() === $("#daReversals input[name='productName']").val()) {
			//					$('#error_message').show();
			//					$('#myform_succloc').css('color', 'red').text('This Product already exist, Use different name.');
			//					isExist = true;
			//					return false;
			//				}
			//			});
			//			if (isExist) {
			//				return false;
			//			}
			$('#daReversalsList').append('<tr><td class="td1" style="width:190px;">' + $("#daReversals input[name='dedicatedAccountInformationID']").val() + '</td><td class="td2" style="width:100px;">' + $("#daReversals input[name='hoursToReverse']").val() + '</td><td class="td3" style="width:100px;">' + $("#daReversals input[name='amountToReverse']").val() + '</td><td class="td5"><img src="images/delete-product.png" class="deleteResourceExtraInfo" title="Delete Product" /></td></tr>');
			$('#daReversals').find('input:text').val('');
		});

		$(".toReversals").unbind('click').click(function() {
			$('#toReversals').find('input:text, input:password, input:file, select, textarea').val('');
			$('#toReversals').find('input:radio, input:checkbox')
				.removeAttr('checked').removeAttr('selected');
			$("#toReversals").slideToggle("slow");
		});

		$('#add-toReversals').unbind('click').click(function() {

			if ($("#toReversals input[name='offerID']").val() === "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('offerID cannot be empty.');
				return false;
			}

			if ($("#toReversals input[name='dedicatedAccountInformationID']").val() === "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('dedicatedAccountInformationID cannot be empty.');
				return false;
			}
			//
			//			var isExist = false;
			//			$('#productList tr').each(function() {
			//				if ($(this).find('td:first').text() === $("#toReversals input[name='productName']").val()) {
			//					$('#error_message').show();
			//					$('#myform_succloc').css('color', 'red').text('This Product already exist, Use different name.');
			//					isExist = true;
			//					return false;
			//				}
			//			});
			//			if (isExist) {
			//				return false;
			//			}
			$('#toReversalsList').append('<tr><td class="td1" style="width:100px;">' + $("#toReversals input[name='offerID']").val() + '</td><td class="td2" style="width:190px;">' + $("#toReversals input[name='dedicatedAccountInformationID']").val() + '</td><td class="td3" style="width:100px;">' + $("#toReversals input[name='hoursToReverse']").val() + '</td><td class="td5"><img src="images/delete-product.png" class="deleteResourceExtraInfo" title="Delete Product" /></td></tr>');
			$('#toReversals').find('input:text').val('');
		});


		$(".offerSelection").unbind('click').click(function() {
			$('#offerSelection').find('input:text, input:password, input:file, select, textarea').val('');
			$('#offerSelection').find('input:radio, input:checkbox')
				.removeAttr('checked').removeAttr('selected');
			$("#offerSelection").slideToggle("slow");
		});

		$('#add-offerSelection').unbind('click').click(function() {

			if ($("#offerSelection input[name='offerIDFirst']").val() === "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('offerIDFirst cannot be empty.');
				return false;
			}

			if ($("#offerSelection input[name='offerIDLast']").val() === "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('offerIDLast cannot be empty.');
				return false;
			}
			//
			//			var isExist = false;
			//			$('#productList tr').each(function() {
			//				if ($(this).find('td:first').text() === $("#offerSelection input[name='productName']").val()) {
			//					$('#error_message').show();
			//					$('#myform_succloc').css('color', 'red').text('This Product already exist, Use different name.');
			//					isExist = true;
			//					return false;
			//				}
			//			});
			//			if (isExist) {
			//				return false;
			//			}
			$('#offerSelectionList').append('<tr><td class="td1" style="width:190px;">' + $("#offerSelection input[name='offerIDFirst']").val() + '</td><td class="td2" style="width:190px;">' + $("#offerSelection input[name='offerIDLast']").val() + '</td><td class="td5"><img src="images/delete-product.png" class="deleteResourceExtraInfo" title="Delete Product" /></td></tr>');
			$('#offerSelection').find('input:text').val('');
		});

		var deleteResorce = "";

		$(document).off("click", ".delete-link").on("click", ".delete-link", function(e) {
			// e.preventDefault();
			if ($('#resource-menu').hasClass('active')) {
				deleteResorce = $(this).attr("text");

				e.preventDefault(); // disable normal link function so that it doesn't refresh the page
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

			$.ajax({
				url: '/sef-catalog-ui/deleteResource?resourceName=' + deleteResorce,
				type: "GET",
				async: false,
				success: function(data) {

					if (data.status == 'success') {
						$('.overlay-bg-delete').hide();
						$('#error_message').show();
						$('#myform_succloc').css('color', 'green').text('Resource Deleted Successfully');
						ResourceHandler.loadResources();
					} else {
						$('#error_message').show();
						$('#myform_succloc').css('color', 'red').text(data.message);
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
		$(document).off("click", ".edit-link").on("click", ".edit-link", function(e) {
			e.preventDefault();

			$('.pamInfo').closest('.price').hide();
			$('.serviceOffer').closest('.price').hide();
			$('.daReversals').closest('.price').hide();
			$('.toReversals').closest('.price').hide();
			$('.offerSelection').closest('.price').hide();
			$('#pamInfo').hide();
			$('#serviceOffer').hide();
			$('#daReversals').hide();
			$('#toReversals').hide();
			$('#offerSelection').hide();
			$('#pamInfoList').html('');
			$('#serviceOfferList').html('');
			$('#daReversalsList').html('');
			$('#toReversalsList').html('');
			$('#offerSelectionList').html('');

			if ($('#resource-menu').hasClass('active')) {
				var resource = {};
				var profile = {};
				var profileClass = null;

				$.ajax({
					url: '/sef-catalog-ui/readResource?resourceName=' + $(this).attr("text"),
					type: "GET",
					async: false,
					success: function(data) {
						json = JSON.parse(data);
						resource = json.resource;
						profile = json.profile;
						profileClass = json.profileClass;
					}

				});

				$.each(resource, function(name, val) {
					var $el = $('#create-resource [name="' + name + '"]'),
						type = $el.attr('type');

					switch (type) {
						case 'checkbox':
							$el.attr('checked', 'checked');
							break;
						case 'radio':
							$el.filter('[value="' + val + '"]').prop('checked', true)
							break;
						default:
							$el.val(val);
					}
				});

				if (resource.consumable == true) {
					$('.unit').show();
				} else {
					$('.unit').hide();
				}

				if (resource.abstract == true) {
					$('.cost').hide();
				} else {
					$('.cost').show();
				}

				if (resource.cost != null && resource.cost != undefined && resource.cost.amount != null && resource.cost.amount != undefined) {
					$('#create-resource [name="cost"]').val(resource.cost.amount);
				}

				$('#workflowList option').prop('selected', false);
				$('#profileList option').prop('selected', false);
				$('#OFFERPROFILE').html('').show();

				if (profileClass != null && profileClass != undefined) {
					if (profileClass.indexOf('smart') > -1) {
						$('#workflowList option[value="' + profileClass.substring(profileClass.lastIndexOf(".") + 1) + '"]').prop('selected', true);
					} else {
						$('#profileList option[value="' + profileClass.substring(profileClass.lastIndexOf(".") + 1) + '"]').prop('selected', true);
					}

					var obj = profileData[profileClass];

					$('#OFFERPROFILE').append('<div class="form-row">' +
						'<div class="label" style="float: left; width: 160px;">Profile Name &nbsp;&nbsp;</div>' +
						'<em>*</em>' +
						'<input class="input-field-style mandatory constrained" name="profileName" placeholder="Enter Profile Name" id="group-name" style="width:148px;" value="' + profile.name + '"></div>');

					for (key in obj) {
						var newKey = "";
						if (key.lastIndexOf("is", 0) === 0) {
							newKey = key.substring(2, 3).toLowerCase() + key.substring(3);
						} else {
							newKey = key;
						}

						if (obj[key].toLowerCase().indexOf("int") > -1 || obj[key].toLowerCase().indexOf("long") > -1) {
							if (profile[newKey] == null || profile[newKey] == undefined) {
								profile[newKey] = "";
							}

							$('#OFFERPROFILE').append('<div class="form-row">' +
								'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
								'<em>&nbsp;&nbsp;</em>' +
								'<input onkeypress="return isNumberKey(event)" maxlength="9" placeholder="Enter ' + newKey + ' in digits" class="input-field-style mandatory constrained" name="' + newKey + '" id="group-name" style="width:148px;" value="' + profile[newKey] + '"></div>');
						} else if (obj[key].toLowerCase().indexOf("boolean") > -1) {
							$('#OFFERPROFILE').append('<div class="form-row">' +
								'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
								'<em>&nbsp;&nbsp;</em>' +
								'<input type="radio" class="input-field-style mandatory constrained" name="' + newKey + '" id="group-name" value="true">True<input type="radio" class="input-field-style mandatory constrained" name="' + newKey + '" id="group-name" value="false">False</div>');

							if (profile[newKey] != null && profile[newKey] != undefined) {
								$('#create-resource [name="' + newKey + '"][value="' + profile[newKey] + '"]').prop('checked', true);
							}

						} else if (obj[key].toLowerCase().indexOf("date") > -1) {

							if (profile[newKey] == null || profile[newKey] == undefined) {
								profile[newKey] = "";
							} else {
								var d = new Date(profile[newKey]); // The 0 there is the key, which sets the date to the epoch
								d.setDate(d.getDate() + 1);
								profile[newKey] = d.toISOString().split('T')[0];
							}

							$('#OFFERPROFILE').append('<div class="form-row">' +
								'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
								'<em>&nbsp;&nbsp;</em>' +
								'<input type="text" attr-date="datepicker" placeholder="yyyy-mm-dd" class="input-field-style mandatory constrained" name="' + newKey + '" style="width:130px;" value="' + profile[newKey] + '"></div>');
						} else if (obj[key].toLowerCase().indexOf("string") > -1) {
							if (profile[newKey] == null || profile[newKey] == undefined) {
								profile[newKey] = "";
							}
							$('#OFFERPROFILE').append('<div class="form-row">' +
								'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
								'<em>&nbsp;&nbsp;</em>' +
								'<input class="input-field-style mandatory constrained" placeholder="Enter ' + newKey + '" name="' + newKey + '" id="group-name" style="width:148px;" value="' + profile[newKey] + '"></div>');
						} else if (obj[key].toLowerCase().indexOf("currencycode") > -1) {
							$('#OFFERPROFILE').append('<div class="form-row">' +
								'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
								'<em>&nbsp;&nbsp;</em>' +
								'<select class="input-field-style mandatory constrained" name="' + newKey + '" id="group-name" style="width:148px;"><option value="PHP">PHP</option><option value="CENTS">CENTS</option></select></div>');
							$('#OFFERPROFILE select[name="' + newKey + '"]').val(profile[newKey]);
						} else if (obj[key].toLowerCase().indexOf("paminformation") > -1) {
							$('.pamInfo').closest('.price').show();
							if (profile[newKey] != null && profile[newKey] != undefined) {
								var pamInformationList = profile[newKey];
								if (!(profile[newKey] instanceof Array)) {
									pamInformationList = profile[newKey].pamInfolist;
								}
								var arrayLength = pamInformationList.length;
								for (var i = 0; i < arrayLength; i++) {
									$('#pamInfoList').append('<tr><td class="td1">' + ((pamInformationList[i].pamServiceID == null) ? "" : pamInformationList[i].pamServiceID) + '</td><td class="td2">' + ((pamInformationList[i].pamClassID == null) ? "" : pamInformationList[i].pamClassID) + '</td><td class="td3">' + ((pamInformationList[i].scheduleID == null) ? "" : pamInformationList[i].scheduleID) + '</td><td class="td4">' + ((pamInformationList[i].pamIndicator == null) ? "" : pamInformationList[i].pamIndicator) + '</td><td class="td5"><img src="images/delete-product.png" class="deleteResourceExtraInfo" title="Delete Product" /></td></tr>');
								}
							}

						} else if (obj[key].toLowerCase().indexOf("serviceoffering") > -1) {
							$('.serviceOffer').closest('.price').show();
							if (profile[newKey] != null && profile[newKey] != undefined) {
								var serviceOfferList = profile[newKey];
								var arrayLength = serviceOfferList.length;
								for (var i = 0; i < arrayLength; i++) {
									$('#serviceOfferList').append('<tr><td class="td1" style="width:200px;">' + ((serviceOfferList[i].serviceOfferingId == null) ? "" : serviceOfferList[i].serviceOfferingId) + '</td><td class="td2" style="width:200px;">' + serviceOfferList[i].serviceOfferingActiveFlag + '</td><td class="td5"><img src="images/delete-product.png" class="deleteResourceExtraInfo" title="Delete Product" /></td></tr>');
								}
							}
						} else if (obj[key].toLowerCase().indexOf("dedicatedaccountreversal") > -1) {
							$('.daReversals').closest('.price').show();
							if (profile[newKey] != null && profile[newKey] != undefined) {
								var daReversalList = profile[newKey];
								var arrayLength = daReversalList.length;
								for (var i = 0; i < arrayLength; i++) {
									$('#daReversalsList').append('<tr><td class="td1" style="width:190px;">' + ((daReversalList[i].dedicatedAccountInformationID == null) ? "" : daReversalList[i].dedicatedAccountInformationID) + '</td><td class="td2" style="width:100px;">' + ((daReversalList[i].hoursToReverse == null) ? "" : daReversalList[i].hoursToReverse) + '</td><td class="td3" style="width:100px;">' + ((daReversalList[i].amountToReverse == null) ? "" : daReversalList[i].amountToReverse) + '</td><td class="td5"><img src="images/delete-product.png" class="deleteResourceExtraInfo" title="Delete Product" /></td></tr>');
								}
							}
						} else if (obj[key].toLowerCase().indexOf("timerofferreversal") > -1) {
							$('.toReversals').closest('.price').show();
							if (profile[newKey] != null && profile[newKey] != undefined) {
								var toReversalList = profile[newKey];
								var arrayLength = toReversalList.length;
								for (var i = 0; i < arrayLength; i++) {
									$('#toReversalsList').append('<tr><td class="td1" style="width:100px;">' + ((toReversalList[i].offerID == null) ? "" : toReversalList[i].offerID) + '</td><td class="td2" style="width:190px;">' + ((toReversalList[i].dedicatedAccountInformationID == null) ? "" : toReversalList[i].dedicatedAccountInformationID) + '</td><td class="td3" style="width:100px;">' + ((toReversalList[i].hoursToReverse == null) ? "" : toReversalList[i].hoursToReverse) + '</td><td class="td5"><img src="images/delete-product.png" class="deleteResourceExtraInfo" title="Delete Product" /></td></tr>');
								}
							}
						} else if (obj[key].toLowerCase().indexOf("offerselection") > -1) {
							$('.offerSelection').closest('.price').show();
							if (profile[newKey] != null && profile[newKey] != undefined) {
								var offerSelectionList = profile[newKey];
								var arrayLength = offerSelectionList.length;
								for (var i = 0; i < arrayLength; i++) {
									$('#offerSelectionList').append('<tr><td class="td1" style="width:190px;">' + ((offerSelectionList[i].offerIDFirst == null) ? "" : offerSelectionList[i].offerIDFirst) + '</td><td class="td2" style="width:190px;">' + ((offerSelectionList[i].offerIDLast == null) ? "" : offerSelectionList[i].offerIDLast) + '</td><td class="td5"><img src="images/delete-product.png" class="deleteResourceExtraInfo" title="Delete Product" /></td></tr>');
								}
							}
						}
					}
					addDatepicker();
				}

				$('#create-resource [name="name"]').prop('readonly', true);

				$('#ui-btn123').hide();
				$('#content-right').hide();
				$('.offer-history').hide();
				$('#content-right :input').attr('disabled', false);
				$('#content-right1').hide();
				$('#content-right2').hide();
				$('#content-service').show(); //show//
				$('#content-profiles').hide();
				$('#content-policy').hide();
				$('#content-rule').hide();
				$('#ui-btn').hide();
				$('#ui-btn12').hide();
				$('#ui-btn1').hide();
				$("#save-btn-res").hide();
				$("#cancel-btn-res").show();
				$("#update-btn-res").show();


			}

		});

		$('#content-right').hide();
		$('.ui-btn').hide();
		$('#ui-btn12').hide();
		$('#ui-btn1').show();
		$("#delete-btn").hide();
		$("#cancel-btn").hide();
		$("#save-btn").hide();
		$("#update-btn").hide();
		$('#content-right1').hide();
		$('#content-right2').hide();
		$('#content-profiles').hide();
		$('#content-policy').hide();
		$('#middle').show();
		$('#content-group-mgt').hide();
		$('#middle2').hide();
		$('#ui-btn-og').hide();
		$('#ui-btn').hide();
		$('#ui-btn1').show();
		$('#ui-btn12').hide();
		$("#cancel-btn").hide();
		$("#delete-btn").hide();
		$("#save-btn").hide();
		$("#update-btn").hide();
		$('#content-right').hide();
		$('#content-right1').hide();
		$('#content-right2').hide();
		$('#content-service').hide(); //show//
		$('#content-profiles').hide();
		$('#content-policy').hide();
		$('#content-rule').hide();
		$('#content-group').hide();

		$('#content-resource-group').hide();
		$('#add-resource-group').hide();
		$('#offer-catalog-group-service').hide();
		$('#add-offer-catalog-group').hide();


		$('#create-owner-group').hide();
		$('#add-owner-group').hide();


		resourceData = [];

		var pageNumber = 1;

		var search = "";

		$("#content-middle .search").val('');

		$("#resource-search .search").unbind("keyup").keyup(function() {
			pageNumber = 1;
			resourceData = [];
			search = this.value;
			getAllResources();
		});

		function getAllResources() {
			$.ajax({
				url: '/sef-catalog-ui/getAllResources?pageNumber=' + pageNumber + '&pageSize=30&search=' + search,
				type: "GET",
				async: false,
				contentType: 'application/json',
				success: function(data) {

					json = JSON.parse(data);

					var myStringArray = json.resources;
					var arrayLength = myStringArray.length;
					for (var i = 0; i < arrayLength; i++) {

						var resource = {};
						resource.text = myStringArray[i].name;
						resource.expanded = false;
						resource.items = [];
						resource.spriteCssClass = "pdf";

						var profileArray = myStringArray[i].fulfillmentProfiles;
						if (profileArray && profileArray != null && profileArray != undefined) {

							var profileLength = profileArray.length;

							for (var j = 0; j < profileLength; j++) {
								var profile = {};
								profile.text = profileArray[j];
								profile.expanded = false;
								profile.spriteCssClass = "html";
								resource.items.push(profile);
							}
						}
						resourceData.push(resource);
					}


					if ($('#middle')
						.hasClass('k-treeview')) {
						treeview = $("#middle").data("kendoTreeView");

						treeview.setDataSource(new kendo.data.HierarchicalDataSource({
							data: resourceData,
						}));


					} else {
						treeview = $("#middle").kendoTreeView({
							//template: "#= item.text # (#=  #)",
							template: kendo.template($("#middle-template").html()),
							dataSource: resourceData,
							//select: onSelect,
						}).data("kendoTreeView");
					}
					if($('#hasResourceDelete').val() === 'false'){
						$('.delete-link').hide();
					}

					pageNumber++;
                    
					$("#middle div span.k-checkbox").hide();
					$('#middle ul li ul li .edit-link').hide();
					$('#middle ul li ul li .delete-link').hide();
				}

			});
		}
	
		var profileData = {};

		var basePkg = "";

		$.ajax({
			url: '/sef-catalog-ui/getAllProfileTypes',
			type: "GET",
			async: false,
			contentType: 'application/json',
			success: function(data) {
				$('#profileList').html('');
				$('#workflowList').html('');
				profileData = data;
				for (key in data) {
					var className = key.split('profiles.')[1];

					if (basePkg == "") {
						basePkg = key.split('profiles.')[0] + "profiles";
					}

					if (className.indexOf("smart") > -1) {
						className = className.replace("smart.", "");
						$('#workflowList').append('<option value="' + className + '">' + className + '</option>');
					} else if (className.indexOf(".") > -1) {
						continue;
					} else {
						$('#profileList').append('<option value="' + className + '">' + className + '</option>');
					}
				}
			}
		});

		$(document).off('change', '#workflowList').on('change', '#workflowList', function(e) {
			e.preventDefault();
			$('.pamInfo').closest('.price').hide();
			$('.serviceOffer').closest('.price').hide();
			$('.daReversals').closest('.price').hide();
			$('.toReversals').closest('.price').hide();
			$('.offerSelection').closest('.price').hide();
			$('#pamInfo').hide();
			$('#serviceOffer').hide();
			$('#daReversals').hide();
			$('#toReversals').hide();
			$('#offerSelection').hide();
			$('#pamInfoList').html('');
			$('#serviceOfferList').html('');
			$('#daReversalsList').html('');
			$('#toReversalsList').html('');
			$('#offerSelectionList').html('');
			var value = $(this).val()[0];
			$('#workflowList option').prop('selected', false);
			$('#profileList option').prop('selected', false);
			$(this).val(value);

			var obj = profileData[basePkg + '.smart.' + value];

			$('#OFFERPROFILE').html('').hide();

			$('#OFFERPROFILE').append('<div class="form-row">' +
				'<div class="label" style="float: left; width: 160px;">Profile Name &nbsp;&nbsp;</div>' +
				'<em>*</em>' +
				'<input class="input-field-style mandatory constrained" placeholder="Enter Profile Name" name="profileName" id="group-name" style="width:148px;"></div>');

			for (key in obj) {
				var newKey = "";
				if (key.lastIndexOf("is", 0) === 0) {
					newKey = key.substring(2, 3).toLowerCase() + key.substring(3);
				} else {
					newKey = key;
				}
				if (obj[key].toLowerCase().indexOf("int") > -1 || obj[key].toLowerCase().indexOf("long") > -1) {
					$('#OFFERPROFILE').append('<div class="form-row">' +
						'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
						'<em>&nbsp;&nbsp;</em>' +
						'<input onkeypress="return isNumberKey(event)" maxlength="9" placeholder="Enter ' + newKey + ' in digits" class="input-field-style mandatory constrained" name="' + newKey + '" id="group-name" style="width:148px;"></div>');
				} else if (obj[key].toLowerCase().indexOf("boolean") > -1) {
					$('#OFFERPROFILE').append('<div class="form-row">' +
						'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
						'<em>&nbsp;&nbsp;</em>' +
						'<input type="radio" class="input-field-style mandatory constrained" name="' + newKey + '" id="group-name" value="true">True<input type="radio" class="input-field-style mandatory constrained" name="' + newKey + '" id="group-name" value="false">False</div>');
				} else if (obj[key].toLowerCase().indexOf("date") > -1) {
					$('#OFFERPROFILE').append('<div class="form-row">' +
						'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
						'<em>&nbsp;&nbsp;</em>' +
						'<input type="text" attr-date="datepicker" placeholder="yyyy-mm-dd" class="input-field-style mandatory constrained" name="' + newKey + '" style="width:130px;"></div>');
				} else if (obj[key].toLowerCase().indexOf("string") > -1) {
					$('#OFFERPROFILE').append('<div class="form-row">' +
						'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
						'<em>&nbsp;&nbsp;</em>' +
						'<input class="input-field-style mandatory constrained" name="' + newKey + '" placeholder="Enter ' + newKey + '" id="group-name" style="width:148px;"></div>');
				} else if (obj[key].toLowerCase().indexOf("currencycode") > -1) {
					$('#OFFERPROFILE').append('<div class="form-row">' +
						'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
						'<em>&nbsp;&nbsp;</em>' +
						'<select class="input-field-style mandatory constrained" name="' + newKey + '" id="group-name" style="width:148px;"><option value="PHP">PHP</option><option value="CENTS">CENTS</option></select></div>');
				} else if (obj[key].toLowerCase().indexOf("paminformation") > -1) {
					$('.pamInfo').closest('.price').show();
				} else if (obj[key].toLowerCase().indexOf("serviceoffering") > -1) {
					$('.serviceOffer').closest('.price').show();
				} else if (obj[key].toLowerCase().indexOf("dedicatedaccountreversal") > -1) {
					$('.daReversals').closest('.price').show();
				} else if (obj[key].toLowerCase().indexOf("timerofferreversal") > -1) {
					$('.toReversals').closest('.price').show();
				} else if (obj[key].toLowerCase().indexOf("offerselection") > -1) {
					$('.offerSelection').closest('.price').show();
				}
			}
			addDatepicker();
			$('#OFFERPROFILE').slideDown('slow');
		});

		$(document).off('change', '#profileList').on('change', '#profileList', function(e) {
			e.preventDefault();
			$('.pamInfo').closest('.price').hide();
			$('.serviceOffer').closest('.price').hide();
			$('.daReversals').closest('.price').hide();
			$('.toReversals').closest('.price').hide();
			$('.offerSelection').closest('.price').hide();
			$('#pamInfo').hide();
			$('#serviceOffer').hide();
			$('#daReversals').hide();
			$('#toReversals').hide();
			$('#offerSelection').hide();
			$('#pamInfoList').html('');
			$('#serviceOfferList').html('');
			$('#daReversalsList').html('');
			$('#toReversalsList').html('');
			$('#offerSelectionList').html('');
			var value = $(this).val()[0];
			$('#workflowList option').prop('selected', false);
			$('#profileList option').prop('selected', false);
			$(this).val(value);

			var obj = profileData[basePkg + '.' + value];

			$('#OFFERPROFILE').html('').hide();

			$('#OFFERPROFILE').append('<div class="form-row">' +
				'<div class="label" style="float: left; width: 160px;">Profile Name &nbsp;&nbsp;</div>' +
				'<em>*</em>' +
				'<input class="input-field-style mandatory constrained" name="profileName" placeholder="Enter Profile Name" id="group-name" style="width:148px;"></div>');

			for (key in obj) {
				var newKey = "";
				if (key.lastIndexOf("is", 0) === 0) {
					newKey = key.substring(2, 3).toLowerCase() + key.substring(3);
				} else {
					newKey = key;
				}
				if (obj[key].toLowerCase().indexOf("int") > -1 || obj[key].toLowerCase().indexOf("long") > -1) {
					$('#OFFERPROFILE').append('<div class="form-row">' +
						'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
						'<em>&nbsp;&nbsp;</em>' +
						'<input onkeypress="return isNumberKey(event)" maxlength="9" placeholder="Enter ' + newKey + ' in digits" class="input-field-style mandatory constrained" name="' + newKey + '" id="group-name" style="width:148px;"></div>');
				} else if (obj[key].toLowerCase().indexOf("boolean") > -1) {
					$('#OFFERPROFILE').append('<div class="form-row">' +
						'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
						'<em>&nbsp;&nbsp;</em>' +
						'<input type="radio" class="input-field-style mandatory constrained" name="' + newKey + '" id="group-name" value="true">True<input type="radio" class="input-field-style mandatory constrained" name="' + newKey + '" id="group-name" value="false">False</div>');
				} else if (obj[key].toLowerCase().indexOf("date") > -1) {
					$('#OFFERPROFILE').append('<div class="form-row">' +
						'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
						'<em>&nbsp;&nbsp;</em>' +
						'<input type="text" attr-date="datepicker" placeholder="yyyy-mm-dd" class="input-field-style mandatory constrained" name="' + newKey + '" style="width:130px;"></div>');
				} else if (obj[key].toLowerCase().indexOf("string") > -1) {
					$('#OFFERPROFILE').append('<div class="form-row">' +
						'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
						'<em>&nbsp;&nbsp;</em>' +
						'<input class="input-field-style mandatory constrained"  placeholder="Enter ' + newKey + '" name="' + newKey + '" id="group-name" style="width:148px;"></div>');
				} else if (obj[key].toLowerCase().indexOf("currencycode") > -1) {
					$('#OFFERPROFILE').append('<div class="form-row">' +
						'<div class="label" style="float: left; width: 160px;">' + key + ' &nbsp;&nbsp;</div>' +
						'<em>&nbsp;&nbsp;</em>' +
						'<select class="input-field-style mandatory constrained" name="' + newKey + '" id="group-name" style="width:148px;"><option value="PHP">PHP</option><option value="CENTS">CENTS</option></select></div>');
				} else if (obj[key].toLowerCase().indexOf("paminformation") > -1) {
					$('.pamInfo').closest('.price').show();
				} else if (obj[key].toLowerCase().indexOf("serviceoffering") > -1) {
					$('.serviceOffer').closest('.price').show();
				} else if (obj[key].toLowerCase().indexOf("dedicatedaccountreversal") > -1) {
					$('.daReversals').closest('.price').show();
				} else if (obj[key].toLowerCase().indexOf("timerofferreversal") > -1) {
					$('.toReversals').closest('.price').show();
				} else if (obj[key].toLowerCase().indexOf("offerselection") > -1) {
					$('.offerSelection').closest('.price').show();
				}
			}
			addDatepicker();
			$('#OFFERPROFILE').slideDown('slow');
		});

		getAllResources();

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
				onTotalScroll: function() {
					$("#overlay1").show(100, function(){
						getAllResources();
					});
				},
				onTotalScrollBack: false,
				whileScrolling: false,
				onTotalScrollOffset: 150,
				onTotalScrollBackOffset: 0,
				alwaysTriggerOffsets: true
			},
			live: false
		});


		$('#save-btn-res').unbind('click').click(function() {

			if ($('#resource-menu').hasClass('active')) {
				var resource = $('#create-resource').serializeObject();
				if (resource.profileType != "" && resource.profileType != null) {
					resource.profileType = basePkg + "." + resource.profileType;
				}
				if (resource.workflowType != "" && resource.workflowType != null) {
					resource.profileType = basePkg + ".smart." + resource.workflowType;
				}

				if ($('.pamInfo').closest('.price').is(':visible')) {
					resource.pamInformation = [];
					$('#pamInfoList tr').each(function(i, e) {
						pamInformation = {};
						$(e).find('td').each(function(index, a) {
							pamInformation[$('#pamInfo input').eq(index).attr('name')] = $(this).text();
						});
						resource.pamInformation.push(pamInformation);
					});
				}

				if ($('.serviceOffer').closest('.price').is(':visible')) {
					resource.serviceOfferings = [];
					$('#serviceOfferList tr').each(function(i, e) {
						serviceOffering = {};
						$(e).find('td').each(function(index, a) {
							if (index == 0) {
								serviceOffering.serviceOfferingId = $(this).text();
							} else if (index == 1) {
								serviceOffering.serviceOfferingActiveFlag = $(this).text();
							}
						});
						resource.serviceOfferings.push(serviceOffering);
					});
				}

				if ($('.daReversals').closest('.price').is(':visible')) {
					resource.daReversals = [];
					$('#daReversalsList tr').each(function(i, e) {
						daReversal = {};
						$(e).find('td').each(function(index, a) {
							daReversal[$('#daReversals input').eq(index).attr('name')] = $(this).text();
						});
						resource.daReversals.push(daReversal);
					});
				}

				if ($('.toReversals').closest('.price').is(':visible')) {
					resource.toReversals = [];
					$('#toReversalsList tr').each(function(i, e) {
						toReversal = {};
						$(e).find('td').each(function(index, a) {
							toReversal[$('#toReversals input').eq(index).attr('name')] = $(this).text();
						});
						resource.toReversals.push(toReversal);
					});
				}

				if ($('.offerSelection').closest('.price').is(':visible')) {
					resource.offerSelection = [];
					$('#offerSelectionList tr').each(function(i, e) {
						offerSelection = {};
						$(e).find('td').each(function(index, a) {
							offerSelection[$('#offerSelection input').eq(index).attr('name')] = $(this).text();
						});
						resource.offerSelection.push(offerSelection);
					});
				}

				$.ajax({
					url: '/sef-catalog-ui/createResource',
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(resource),
					success: function(data) {
						if (data.status == 'success') {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'green').text('Resource Created Successfully');
							ResourceHandler.loadResources();
						} else {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'red').text(data.message);
						}
					}

				});
			}

		});
		$(document).off("click", "#update-btn-res").on("click", "#update-btn-res", function(e) {
			e.preventDefault();
			if ($('#resource-menu').hasClass('active')) {
				var resource = $('#create-resource').serializeObject();
				if (resource.profileType != "" && resource.profileType != null) {
					resource.profileType = basePkg + "." + resource.profileType;
				}
				if (resource.workflowType != "" && resource.workflowType != null) {
					resource.profileType = basePkg + ".smart." + resource.workflowType;
				}
				if ($('.pamInfo').closest('.price').is(':visible')) {
					resource.pamInformation = [];
					$('#pamInfoList tr').each(function(i, e) {
						pamInformation = {};
						$(e).find('td').each(function(index, a) {
							pamInformation[$('#pamInfo input').eq(index).attr('name')] = $(this).text();
						});
						resource.pamInformation.push(pamInformation);
					});
				}

				if ($('.serviceOffer').closest('.price').is(':visible')) {
					resource.serviceOfferings = [];
					$('#serviceOfferList tr').each(function(i, e) {
						serviceOffering = {};
						$(e).find('td').each(function(index, a) {
							if (index == 0) {
								serviceOffering.serviceOfferingId = $(this).text();
							} else if (index == 1) {
								serviceOffering.serviceOfferingActiveFlag = $(this).text();
							}
						});
						resource.serviceOfferings.push(serviceOffering);
					});
				}

				if ($('.daReversals').closest('.price').is(':visible')) {
					resource.daReversals = [];
					$('#daReversalsList tr').each(function(i, e) {
						daReversal = {};
						$(e).find('td').each(function(index, a) {
							daReversal[$('#daReversals input').eq(index).attr('name')] = $(this).text();
						});
						resource.daReversals.push(daReversal);
					});
				}

				if ($('.toReversals').closest('.price').is(':visible')) {
					resource.toReversals = [];
					$('#toReversalsList tr').each(function(i, e) {
						toReversal = {};
						$(e).find('td').each(function(index, a) {
							toReversal[$('#toReversals input').eq(index).attr('name')] = $(this).text();
						});
						resource.toReversals.push(toReversal);
					});
				}

				if ($('.offerSelection').closest('.price').is(':visible')) {
					resource.offerSelection = [];
					$('#offerSelectionList tr').each(function(i, e) {
						offerSelection = {};
						$(e).find('td').each(function(index, a) {
							offerSelection[$('#offerSelection input').eq(index).attr('name')] = $(this).text();
						});
						resource.offerSelection.push(offerSelection);
					});
				}
				$.ajax({
					url: '/sef-catalog-ui/updateResource',
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(resource),
					success: function(data) {
						if (data.status == 'success') {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'green').text('Resource Updated Successfully');
							$("#update-btn-res").hide();
						} else {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'red').text(data.message);
						}
					}

				});

			}

		});

		// When create resource click
		$(document).ready(function() {
			$('#ui-btn1').unbind('click').click(function() {
				$('.pamInfo').closest('.price').hide();
				$('.serviceOffer').closest('.price').hide();
				$('.daReversals').closest('.price').hide();
				$('.toReversals').closest('.price').hide();
				$('.offerSelection').closest('.price').hide();
				$('#pamInfo').hide();
				$('#serviceOffer').hide();
				$('#daReversals').hide();
				$('#toReversals').hide();
				$('#offerSelection').hide();
				$('#pamInfoList').html('');
				$('#serviceOfferList').html('');
				$('#daReversalsList').html('');
				$('#toReversalsList').html('');
				$('#offerSelectionList').html('');
				$("#content-service").show(); //show//
				$("#content-right").hide();
				$("#cancel-btn-res").show();
				$("#delete-btn-res").hide();
				$('#OFFERPROFILE').hide();
				$('.unit').hide();
				$('.cost').show();
				$('#create-resource [name="name"]').prop('readonly', false);
				$("#ui-btn1").hide();
				$("#save-btn-res").show();
				$("#update-btn-res").hide();
				$('#create-resource').find('input:text, input:password, input:file, select, textarea').val('');
				$('#create-resource').find('input:radio, input:checkbox')
					.removeAttr('checked').removeAttr('selected');
				$('#create-resource').find('input[name="abstract"][value="false"]').prop("checked", true);
				$('#create-resource').find('input[name="discoverable"][value="false"]').prop("checked", true);
				$('#create-resource').find('input[name="consumable"][value="false"]').prop("checked", true);
				$('#create-resource').find('input[name="externallyConsumed"][value="false"]').prop("checked", true);
				$('#create-resource').find('input[name="enforcedMinQuota"]').val(-1);
				$('#create-resource').find('input[name="enforcedMaxQuota"]').val(-1);
			});
		});
	}

};
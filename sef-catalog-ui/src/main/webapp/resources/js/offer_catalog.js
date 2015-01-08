var OfferHandler = {
	loadOffers: function() {
		$("#content-middle .search").val('');
		$('#middle_rule').hide();
		$('#resource-search').hide();
		$('#offer-search').show();
		$("#resourcegroup-search").hide();
		$("#offergroup-search").hide();
		$("#privileges-search").hide();
		$("#rule_search").hide();
		$('#rule-btn').hide();
		$('#middle').height('515');
		$('#notification-details').hide();
		$('#content-user-mgt').hide();
		$('#privileges-content').hide();
		$("#content-user-mgt").hide();
		$('#notification_search').hide();
		$('#content-manager').hide(); //i add//
		$('#content-catalog').hide();
		$('#content-group').hide();
		$('#content-resource-group').hide();
		$('#add-resource-group').hide();
		$('#offer-catalog-group-service').hide();
		$('#add-offer-catalog-group').hide();
		$('#create-owner-group').hide();
		$('#add-owner-group').hide();
		$('#content-group-mgt').hide();
		$('#ui-btn2').hide();
		$('#ui-btn12').hide();
		$('#ui-btn124').hide();
		$('#ui-btn-ocg').hide();
		$('#ui-btn-og').hide();
		$('#content-group').hide();
		$('#ui-btn123').hide();
		$('#notification-add').hide();
		$('#notification-edit').hide();
		$('#notification_create_content').hide();
		$('#editn-btn').hide();
		$('#addn-btn').hide();
		$('#middle2').hide();
		$('#middle3').hide();
		$('#middle5').hide();
		$('#middle').show();
		$('#middle7').hide();
		$('#middle8').hide();
		$('#content-right').hide();
		$('#ui-btn').show();
		$('#ui-btn1').hide();
		$('#content-right1').hide();
		$('#content-right2').hide();
		$('#content-service').hide();
		$('#content-profiles').hide();
		$('#content-policy').hide();
		$('#content-rule').hide();
		$("#select-policy-rule").hide();
		$("#select-owner-group").hide();
		$("#select-offer-group").hide();
		$("#select-product-owner").hide();
		$('#select-product-resource').hide();
		$('#ws-search').hide();

		$('#cancel-btn').unbind('click').click(function() {
			if ($('#offers-menu').hasClass('active')) {
				$('#ui-btn').show();
				$("#content-right").hide();
				$("#select-policy-rule").hide();
				$("#select-owner-group").hide();
				$("#select-offer-group").hide();
				$("#select-product-owner").hide();
				$('#select-product-resource').hide();
				$('#ws-search').hide();
				$("#setreneval").hide();
				$("#validity").show();
				$("#rp-infinite").show();
			}
		});

		jQuery('select[name="policyType"]').unbind('change').change(function() {
			if (jQuery(this).val() === "SmartLIfeCyclePricingPolicy") {
				jQuery('#lifecycle').slideDown('slow');
			} else {
				jQuery('#lifecycle').slideUp('slow');
			}
		});

		$('.ok-btn-offer').unbind('click').click(function() {
			$('.overlay-bg-delete-offer').hide(); // hide the overlay
		});

		// hides the popup if user clicks anywhere outside the container
		$('.overlay-bg-delete-offer').unbind('click').click(function() {
				$('.overlay-bg-delete-offer').hide();
			})
			// prevents the overlay from closing if user clicks inside the popup overlay
		$('.overlay-content-delete-offer').unbind('click').click(function() {
			return false;
		});

		var products = [];

		var pageNumber = 1;

		var offerData = [];

		var search = "";

		function getAllOffers() {
			$.ajax({
				url: '/sef-catalog-ui/getAllOffers?pageNumber=' + pageNumber + '&pageSize=30&search=' + search,
				type: "GET",
				async: false,
				contentType: 'application/json',
				success: function(data) {

					json = JSON.parse(data);

					var offers = json.offers;
					var offersLength = offers.length;

					for (var k = 0; k < offersLength; k++) {
						var offer = {};
						offer.text = offers[k][1].name;
						offer.state = offers[k][1].offerState;
						offer.expanded = false;
						offer.items = [];
						offer.spriteCssClass = "rootfolder";

						var productArray = offers[k][1].products;
						var arrayLength = productArray.length;
						for (var i = 0; i < arrayLength; i++) {

							var product = {};
							product.text = productArray[i].name;
							product.expanded = false;
							product.items = [];
							product.spriteCssClass = "folder";
							var resource = {};

							if (productArray[i].resource != null) {
								resource.text = productArray[i].resource.name;

								resource.expanded = false;
								resource.spriteCssClass = "pdf";

								product.items.push(resource);
							}

							offer.items.push(product);

						}
						offerData.push(offer);
					}

					if ($('#middle').hasClass('k-treeview')) {

						treeview = $("#middle").data("kendoTreeView");

						treeview.setDataSource(new kendo.data.HierarchicalDataSource({
							data: offerData
						}));


					} else {
						treeview = $("#middle").kendoTreeView({
							//template: "#= item.text # (#=  #)",
							template: kendo.template($("#middle-template").html()),
							checkboxTemplate: kendo.template($("#treeview-checkbox-template").html()),
							dataSource: offerData,
							// select: onSelect,
						}).data("kendoTreeView");
					}

					pageNumber++;
					$('.delete-link').hide();
					$('#middle ul li ul li .edit-link').hide();
					$('#middle ul li ul li input[type="checkbox"]').hide();

					bindCheckbox();

				}
			});
		}

		getAllOffers();

		$("#offer-search .search").unbind("keyup").keyup(function() {
			pageNumber = 1;
			offerData = [];
			search = this.value;
			getAllOffers();
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
				onTotalScroll: function() {
					$("#overlay1").show(100, function() {
						getAllOffers();
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


		$(document).off("click", ".edit-link").on("click", ".edit-link", function(e) {
			e.preventDefault();
			if ($('#offers-menu').hasClass('active')) {

				$('#content-right').find('input:text, input:password, input:file, select, textarea').val('');
				$('#content-right').find('input:radio, input:checkbox')
					.removeAttr('checked').removeAttr('selected');
				products = [];
				$('#productList').html('');
				$('#assigned-fields-handler ul').html('');
				$('#assigned-fields-whitelistUser ul').html('');
				$('#content-right').find('.blue').hide();
				$('#content-right').find('input[name="renewalHours"]').hide();
				$('#content-right').find('input[name="renewalDate"]').hide();
				$('#content-right').find('#assigned-fields-tax ul').html('');
				$('#content-right').find('input[name="taxValue"]').show();
				$('#content-right').find('#offer-group-hs').hide();
				$('#content-right').find('input[name="autoTerminationRenewals"]').hide();
				$('#content-right').find('input[name="autoTerminationDays"]').hide();
				$('#content-right').find('input[name="autoTerminationDate"]').hide();
				$('#content-right').find('input[name="minimumCommitmentRenewals"]').hide();
				$('#content-right').find('input[name="minimumCommitmentDays"]').hide();
				$('#content-right').find('input[name="trialHours"]').hide();
				$('#content-right').find('input[name="trialDays"]').hide();
				$('#content-right').find('input[name="trialDate"]').hide();

				var offer = {};

				$.ajax({
					url: '/sef-catalog-ui/readOffer?offerId=' + $(this).attr("text"),
					type: "GET",
					async: false,
					success: function(data) {
						offer = JSON.parse(data);
					}

				});

				$('#create-offer [name="name"]').val(offer.name);
				$('#create-offer [name="exitOfferId"]').val(offer.exit);
				$('#create-offer [name="description"]').val(offer.description);
				$('#create-offer [name="offerGroup"]').val(offer.offerGroup);
				$('#create-offer [name="offerState"]').val(offer.offerState);
				$('#create-offer [name="recurrent"]').filter('[value="' + offer.recurrent + '"]').prop('checked', true);

				if (!offer.recurrent) {
					$('#rp-infinite').show();
					$('#validity').show();
					$('#setreneval').hide();
				} else {
					$('#rp-infinite').hide();
					$('#validity').hide();
					$('#setreneval').show();
				}

				if (offer.renewalPeriod != null && offer.renewalPeriod != undefined && offer.renewalPeriod.type != null && offer.renewalPeriod.type != undefined) {
					$('#create-offer [name="renewalPeriod"]').filter('[value="' + offer.renewalPeriod.type.toLowerCase() + '"]').prop('checked', true);
					var timeSinceEpoch = new Date().getTime();
					var hours = (offer.renewalPeriod.expiryTimeInMillis - timeSinceEpoch) / 1000 / 60 / 60;

					if (offer.renewalPeriod.type == "DAYS") {
						$('#create-offer [name="renewalDays"]').val((hours / 24).toString().split('.')[0]);
						$('#create-offer [name="renewalDays"]').show();
					} else if (offer.renewalPeriod.type == "HOURS") {
						$('#create-offer [name="renewalHours"]').val((hours).toString().split('.')[0]);
						$('#create-offer [name="renewalHours"]').show();
					}
				}

				if (offer.trialPeriod != null && offer.trialPeriod != undefined && offer.trialPeriod.type != null && offer.trialPeriod.type != undefined) {
					$('#create-offer [name="trialPeriod"]').filter('[value="' + offer.trialPeriod.type.toLowerCase() + '"]').prop('checked', true);
					var timeSinceEpoch = new Date().getTime();
					var hours = (offer.trialPeriod.expiryTimeInMillis - timeSinceEpoch) / 1000 / 60 / 60;

					if (offer.trialPeriod.type == "DAYS") {
						$('#create-offer [name="trialDays"]').val((hours / 24).toString().split('.')[0]);
						$('#create-offer [name="trialDays"]').show();
					} else if (offer.trialPeriod.type == "HOURS") {
						$('#create-offer [name="trialHours"]').val((hours).toString().split('.')[0]);
						$('#create-offer [name="trialHours"]').show();
					}
				}

				$('#create-offer [name="commercial"]').filter('[value="' + offer.commercial + '"]').prop('checked', true);

				if (offer.commercial) {
					$('.blue').show();
				} else {
					$('.blue').hide();
				}

				if (offer.price != null && offer.price != undefined) {

					if (offer.price.taxes != null && offer.price.taxes != undefined) {
						for (var i = 0; i < offer.price.taxes.length; i++) {
							if (offer.price.taxes[i].taxation == 'TAX_ABSOLUTE') {
								$('#assigned-fields-tax ul').append('<li>Absolute  ' + offer.price.taxes[i].taxAbsolute + '(' + offer.price.iso4217CurrencyCode + ')<a class="search-choice-close"></a></li>');
							} else if (offer.price.taxes[i].taxation == 'NO_TAX') {
								$('#assigned-fields-tax ul').append('<li>Free<a class="search-choice-close"></a></li>');
							} else if (offer.price.taxes[i].taxation == 'TAX_PERCENTAGE') {
								$('#assigned-fields-tax ul').append('<li>Percentage  ' + offer.price.taxes[i].taxPercentile + '<a class="search-choice-close"></a></li>');
							}
						}
					}

					if (offer.price.ratingRules != null && offer.price.ratingRules != undefined && offer.price.ratingRules.length > 0) {
						$("#create-offer input[name='pricePolicy']").val(offer.price.ratingRules[0].name);
						if (offer.price.ratingRules[0].rule != null && offer.price.ratingRules[0].rule != undefined) {
							$("#create-offer input[name='rule']").val(offer.price.ratingRules[0].rule.name);
						}
					}

					if (offer.price.cost != null && offer.price.cost != undefined) {
						if (offer.price.cost.amount != null && offer.price.cost.amount != undefined) {
							$('#create-offer [name="cost"]').val(offer.price.cost.amount);
							$('#create-offer [name="policyCost"]').val(offer.price.cost.amount);
						}
						if (offer.price.cost.iso4217CurrencyCode != null && offer.price.cost.iso4217CurrencyCode != undefined) {
							$('#create-offer [name="currency"]').val(offer.price.cost.iso4217CurrencyCode);
						}
					}
				}

				if (offer.owner != null && offer.owner != undefined) {
					if (offer.owner.name != null && offer.owner.name != undefined) {
						$('#create-offer [name="ownerName"]').val(offer.owner.name);
						$('#create-offer [name="ownerType"]').val(offer.owner.type);
					}

					if (offer.owner.type != null && offer.owner.type != undefined) {
						$('#create-offer [value="' + offer.owner.type + '"]').prop('checked', true);
					}
				}

				if (offer.autoTermination != null && offer.autoTermination != undefined && offer.autoTermination.type != null && offer.autoTermination.type != undefined) {
					$('#create-offer [value="' + offer.autoTermination.type + '"]').prop('checked', true);
					if (offer.autoTermination.type.indexOf('DAYS') > -1) {
						$('#create-offer [name="autoTerminationDays"]').val(offer.autoTermination.days).show();;
					} else if (offer.autoTermination.type.indexOf('RENEWALS') > -1) {
						$('#create-offer [name="autoTerminationRenewals"]').val(offer.autoTermination.renewals).show();
					} else if (offer.autoTermination.type.indexOf('HARD') > -1) {
						var d = new Date(offer.autoTermination.date); // The 0 there is the key, which sets the date to the epoch
						var today = d.toISOString().split('T')[0];
						$('#create-offer [name="autoTerminationDate"]').val(today).show();
					}
				}

				if (offer.minimumCommitment != null && offer.minimumCommitment != undefined && offer.minimumCommitment.type != null && offer.minimumCommitment.type != undefined) {
					$('#create-offer [value="' + offer.minimumCommitment.type + '"]').prop('checked', true);
					if (offer.minimumCommitment.type.indexOf('DAYS') > -1) {
						$('#create-offer [name="minimumCommitmentDays"]').val(offer.minimumCommitment.days).show();;
					} else if (offer.minimumCommitment.type.indexOf('RENEWALS') > -1) {
						$('#create-offer [name="minimumCommitmentRenewals"]').val(offer.minimumCommitment.renewals).show();
					} else if (offer.minimumCommitment.type.indexOf('HARD') > -1) {
						var d = new Date(offer.minimumCommitment.date); // The 0 there is the key, which sets the date to the epoch
						var today = d.toISOString().split('T')[0];
						$('#create-offer [name="minimumCommitmentDate"]').val(today).show();
					}
				}

				if (offer.immediateTermination != null && offer.immediateTermination != undefined && offer.immediateTermination.isAllowed != null && offer.immediateTermination.isAllowed != undefined) {
					for (key in offer.immediateTermination.isAllowed) {
						if (offer.immediateTermination.isAllowed[key])
							$('#create-offer [name="' + key + '"]').prop('checked', true);
					}
				}

				if (offer.externalHandles != null && offer.externalHandles != undefined) {
					var handlesLength = offer.externalHandles.length;
					for (var i = 0; i < handlesLength; i++) {
						$('#assigned-fields-handler ul').append('<li>' + offer.externalHandles[i] + '<a class="search-choice-close"></a></li>');
					}
				}

				if (offer.whiteListedUsers != null && offer.whiteListedUsers != undefined) {
					var whiteListedUsersLength = offer.whiteListedUsers.length;
					for (var i = 0; i < whiteListedUsersLength; i++) {
						$('#assigned-fields-whitelistUser ul').append('<li>' + offer.whiteListedUsers[i] + '<a class="search-choice-close"></a></li>');
					}
				}

				if (offer.products != null && offer.products != undefined) {
					var productsLength = offer.products.length;
					for (var i = 0; i < productsLength; i++) {
						product = {};
						product.name = offer.products[i].name;

						product.owner = '';

						if (offer.products[i].owner != null && offer.products[i].owner != undefined) {
							product.owner = offer.products[i].owner.name;
						}

						if (offer.products[i].resource != null && offer.products[i].resource != undefined) {
							product.resource = offer.products[i].resource.name;
						} else {
							product.resource = "";
						}

						product.quota = "";

						if (offer.products[i].quota != null && offer.products[i].quota != undefined) {
							product.quota = offer.products[i].quota.type;
						}

						product.productValidity = "";

						var validityStr = "";

						if (offer.products[i].validity != null && offer.products[i].validity != undefined && offer.products[i].validity.type != null && offer.products[i].validity.type != undefined) {
							if (offer.products[i].validity.type == "DAYS") {
								var timeSinceEpoch = new Date().getTime();
								var hours = (offer.products[i].validity.expiryTimeInMillis - timeSinceEpoch) / 1000 / 60 / 60;
								product.productValidity = 'days';
								product.validityDays = (hours / 24).toString().split('.')[0];
								validityStr = 'Days(' + product.validityDays + ')';
							} else if (offer.products[i].validity.type == "HOURS") {
								var timeSinceEpoch = new Date().getTime();
								var hours = (offer.products[i].validity.expiryTimeInMillis - timeSinceEpoch) / 1000 / 60 / 60;
								product.productValidity = 'hours';
								product.validityHours = hours.toString().split('.')[0];
								validityStr = 'Hours(' + product.validityHours + ')';
							} else if (offer.products[i].validity.type == 'INFINITE') {
								product.productValidity = 'infinite';
								validityStr = 'Infinite';
							} else if (offer.products[i].validity.type == 'DATE') {
								var date = new Date(offer.products[i].validity.expiryTimeInMillis).toISOString().split('T')[0];
								product.productValidity = 'date';
								product.validityDate = date;
								validityStr = 'Date(' + date + ')';
							}
						}

						products.push(product);
						//$('#productList').append('<tr><td class="td1">' + product.name + '</td><td class="td2">' + product.owner + '</td><td class="td3">' + product.rule + '</td><td class="td4">' + product.resource + '</td><td class="td5"><img src="images/edit.png" class="deleteProduct" title="Edit Product" /><img src="images/delete-product.png" class="deleteProduct" title="Delete Product" /></td></tr>');
						$('#productList').append('<tr><td class="td1" style="width:80px;">' + product.name + '</td><td class="td2" style="width:70px;">' + product.owner + '</td><td class="td3" style="width:70px;">' + product.resource + '</td><td class="td4" style="width:70px;">' + product.quota + '</td><td class="td4" style="width:70px;">' +
							validityStr + '</td><td class="td5"><img src="images/delete-product.png" class="deleteProduct" title="Delete Product" /></td></tr>');
					}
				}

				$('#create-offer [name="name"]').prop('readonly', true);

				$('#content-right').show();
				$('.offer-history').hide();
				$('#content-right1').hide();
				$('#content-right2').hide();
				$('#content-service').hide();
				$('#content-profiles').hide();
				$('#content-policy').hide();
				$('#content-rule').hide();
				$('#ui-btn').hide();
				$('#ui-btn1').hide();
				$("#save-btn").hide();
				$("#cancel-btn").show();
				$("#delete-btn").hide();
				$("#update-btn").show();
				$('#content-manager').hide();
			}
			// edit functionality for offer group
		});

		//New implementation
		var existingState = null;
		var offerName = null;

		function bindCheckbox() {
			//	        This method is called when a checkbox is selected or deselected
			$("#middle").data("kendoTreeView").dataSource.bind("change", function(e) {
				e.preventDefault();

				if (!e.items[0].checked) {
					return;
				}
				offerName = e.items[0].text;
				existingState = e.items[0].state;

				$("#middle input[name*='checkedFiles']").prop('checked', false);

				$("#middle #middle_tv_active input").prop('checked', true);



				var kendoWindow = $("#dailog-area").kendoWindow({
					title: "Select an option",
					resizable: false,
					modal: true,
					viewable: false,
					position: {
						top: 100,
						left: 200
					},
					width: 250,
					height: 225,

					content: {
						template: $("#record-jsp").html()
					}
				}).data("kendoWindow");
				kendoWindow.open().center();

				//event call on radio buttons click
				$("#dailog-radio input[name='type']").click(function() {
					//selected radio button is promote
					if ($('input:radio[name=type]:checked').val() == "promote") {
						$('.promote-pop').slideDown('fast');
						$('.promote-pop1').hide();

						if (existingState == "IN_CREATION") {
							$('#dailog-area .promote-pop').html(
								'<input type="radio" name="promote-to" value="Testing">Testing<br>'
							);
						} else if (existingState == "TESTING") {
							$('#dailog-area .promote-pop').html(
								'<input type="radio" name="promote-to" value="Publish">Publish<br>' +
								'<input type="radio" name="promote-to" value="Disabled">Disabled<br>' +
								'<input type="radio" name="promote-to" value="Retired">Retired<br>'
							);
						} else if (existingState == "PUBLISHED") {
							$('#dailog-area .promote-pop').html(
								'<input type="radio" name="promote-to" value="Disabled">Disabled<br>' +
								'<input type="radio" name="promote-to" value="Retired">Retired<br>'
							);
						} else if (existingState == "DISABLED") {
							$('#dailog-area .promote-pop').html(
								'<input type="radio" name="promote-to" value="Publish">Publish<br>' +
								'<input type="radio" name="promote-to" value="Retired">Retired<br>'
							);
						} else if (existingState == "RETIRED") {
							$('#dailog-area .promote-pop').html('');
						}


						$(document).off('click', '#dailog-area .promote-pop input[name="promote-to"]').on('click', '#dailog-area .promote-pop input[name="promote-to"]', function() {
							$('.promote-pop1').show();
						});

					} else if ($('input:radio[name=type]:checked').val() == "edit") {
						$('.promote-pop').hide();
						$('input[name="promote-to"]').prop('checked', false);

						$("#dailog-area").data("kendoWindow").close();

						$('#content-right').find('input:text, input:password, input:file, select, textarea').val('');
						$('#content-right').find('input:radio, input:checkbox')
							.removeAttr('checked').removeAttr('selected');
						products = [];
						$('#productList').html('');
						$('#assigned-fields-handler ul').html('');
						$('#assigned-fields-whitelistUser ul').html('');
						$('#content-right').find('.blue').hide();
						$('#content-right').find('input[name="renewalHours"]').hide();
						$('#content-right').find('input[name="renewalDate"]').hide();
						$('#content-right').find('#assigned-fields-tax ul').html('');
						$('#content-right').find('input[name="taxValue"]').show();
						$('#content-right').find('#offer-group-hs').hide();
						$('#content-right').find('input[name="autoTerminationRenewals"]').hide();
						$('#content-right').find('input[name="autoTerminationDays"]').hide();
						$('#content-right').find('input[name="autoTerminationDate"]').hide();
						$('#content-right').find('input[name="minimumCommitmentRenewals"]').hide();
						$('#content-right').find('input[name="minimumCommitmentDays"]').hide();
						$('#content-right').find('input[name="trialHours"]').hide();
						$('#content-right').find('input[name="trialDays"]').hide();
						$('#content-right').find('input[name="trialDate"]').hide();


						var offer = {};

						$.ajax({
							url: '/sef-catalog-ui/readOffer?offerId=' + offerName,
							type: "GET",
							async: false,
							success: function(data) {
								offer = JSON.parse(data);
							}

						});

						$('#create-offer [name="name"]').val(offer.name);
						$('#create-offer [name="exitOfferId"]').val(offer.exit);
						$('#create-offer [name="description"]').val(offer.description);
						$('#create-offer [name="offerGroup"]').val(offer.offerGroup);
						$('#create-offer [name="offerState"]').val(offer.offerState);
						$('#create-offer [name="recurrent"]').filter('[value="' + offer.recurrent + '"]').prop('checked', true);

						if (!offer.recurrent) {
							$('#rp-infinite').hide();
							$('#validity').show();
							$('#setreneval').hide();
						} else {
							$('rp-infinite').hide();
							$('#validity').hide();
							$('#setreneval').show();
						}

						if (offer.renewalPeriod != null && offer.renewalPeriod != undefined && offer.renewalPeriod.type != null && offer.renewalPeriod.type != undefined) {
							$('#create-offer [name="renewalPeriod"]').filter('[value="' + offer.renewalPeriod.type.toLowerCase() + '"]').prop('checked', true);
							var timeSinceEpoch = new Date().getTime();
							var hours = (offer.renewalPeriod.expiryTimeInMillis - timeSinceEpoch) / 1000 / 60 / 60;

							if (offer.renewalPeriod.type == "DAYS") {
								$('#create-offer [name="renewalDays"]').val((hours / 24).toString().split('.')[0]);
								$('#create-offer [name="renewalDays"]').show();
							} else if (offer.renewalPeriod.type == "HOURS") {
								$('#create-offer [name="renewalHours"]').val((hours).toString().split('.')[0]);
								$('#create-offer [name="renewalHours"]').show();
							}
						}

						if (offer.trialPeriod != null && offer.trialPeriod != undefined && offer.trialPeriod.type != null && offer.trialPeriod.type != undefined) {
							$('#create-offer [name="trialPeriod"]').filter('[value="' + offer.trialPeriod.type.toLowerCase() + '"]').prop('checked', true);
							var timeSinceEpoch = new Date().getTime();
							var hours = (offer.trialPeriod.expiryTimeInMillis - timeSinceEpoch) / 1000 / 60 / 60;

							if (offer.trialPeriod.type == "DAYS") {
								$('#create-offer [name="trialDays"]').val((hours / 24).toString().split('.')[0]);
								$('#create-offer [name="trialDays"]').show();
							} else if (offer.trialPeriod.type == "HOURS") {
								$('#create-offer [name="trialHours"]').val((hours).toString().split('.')[0]);
								$('#create-offer [name="trialHours"]').show();
							}
						}

						$('#create-offer [name="commercial"]').filter('[value="' + offer.commercial + '"]').prop('checked', true);

						if (offer.commercial) {
							$('.blue').show();
						} else {
							$('.blue').hide();
						}

						if (offer.price != null && offer.price != undefined) {

							if (offer.price.taxes != null && offer.price.taxes != undefined) {
								for (var i = 0; i < offer.price.taxes.length; i++) {
									if (offer.price.taxes[i].taxation == 'TAX_ABSOLUTE') {
										$('#assigned-fields-tax ul').append('<li>Absolute  ' + offer.price.taxes[i].taxAbsolute + '(' + offer.price.iso4217CurrencyCode + ')<a class="search-choice-close"></a></li>');
									} else if (offer.price.taxes[i].taxation == 'NO_TAX') {
										$('#assigned-fields-tax ul').append('<li>Free<a class="search-choice-close"></a></li>');
									} else if (offer.price.taxes[i].taxation == 'TAX_PERCENTAGE') {
										$('#assigned-fields-tax ul').append('<li>Percentage  ' + offer.price.taxes[i].taxPercentile + '<a class="search-choice-close"></a></li>');
									}
								}
							}

							if (offer.price.ratingRules != null && offer.price.ratingRules != undefined && offer.price.ratingRules.length > 0) {
								$("#create-offer input[name='pricePolicy']").val(offer.price.ratingRules[0].name);
								if (offer.price.ratingRules[0].rule != null && offer.price.ratingRules[0].rule != undefined) {
									$("#create-offer input[name='rule']").val(offer.price.ratingRules[0].rule.name);
								}
							}

							if (offer.price.cost != null && offer.price.cost != undefined) {
								if (offer.price.cost.amount != null && offer.price.cost.amount != undefined) {
									$('#create-offer [name="cost"]').val(offer.price.cost.amount);
									$('#create-offer [name="policyCost"]').val(offer.price.cost.amount);
								}
								if (offer.price.cost.iso4217CurrencyCode != null && offer.price.cost.iso4217CurrencyCode != undefined) {
									$('#create-offer [name="currency"]').val(offer.price.cost.iso4217CurrencyCode);
								}
							}
						}

						if (offer.owner != null && offer.owner != undefined) {
							if (offer.owner.name != null && offer.owner.name != undefined) {
								$('#create-offer [name="ownerName"]').val(offer.owner.name);
								$('#create-offer [name="ownerType"]').val(offer.owner.type);
							}

							if (offer.owner.type != null && offer.owner.type != undefined) {
								$('#create-offer [value="' + offer.owner.type + '"]').prop('checked', true);
							}
						}

						if (offer.autoTermination != null && offer.autoTermination != undefined && offer.autoTermination.type != null && offer.autoTermination.type != undefined) {
							$('#create-offer [value="' + offer.autoTermination.type + '"]').prop('checked', true);
							if (offer.autoTermination.type.indexOf('DAYS') > -1) {
								$('#create-offer [name="autoTerminationDays"]').val(offer.autoTermination.days).show();;
							} else if (offer.autoTermination.type.indexOf('RENEWALS') > -1) {
								$('#create-offer [name="autoTerminationRenewals"]').val(offer.autoTermination.renewals).show();
							} else if (offer.autoTermination.type.indexOf('HARD') > -1) {
								var d = new Date(offer.autoTermination.date); // The 0 there is the key, which sets the date to the epoch
								var today = d.toISOString().split('T')[0];
								$('#create-offer [name="autoTerminationDate"]').val(today).show();
							}
						}

						if (offer.minimumCommitment != null && offer.minimumCommitment != undefined && offer.minimumCommitment.type != null && offer.minimumCommitment.type != undefined) {
							$('#create-offer [value="' + offer.minimumCommitment.type + '"]').prop('checked', true);
							if (offer.minimumCommitment.type.indexOf('DAYS') > -1) {
								$('#create-offer [name="minimumCommitmentDays"]').val(offer.minimumCommitment.days).show();;
							} else if (offer.minimumCommitment.type.indexOf('RENEWALS') > -1) {
								$('#create-offer [name="minimumCommitmentRenewals"]').val(offer.minimumCommitment.renewals).show();
							} else if (offer.minimumCommitment.type.indexOf('HARD') > -1) {
								var d = new Date(offer.minimumCommitment.date); // The 0 there is the key, which sets the date to the epoch
								var today = d.toISOString().split('T')[0];
								$('#create-offer [name="minimumCommitmentDate"]').val(today).show();
							}
						}

						if (offer.immediateTermination != null && offer.immediateTermination != undefined && offer.immediateTermination.isAllowed != null && offer.immediateTermination.isAllowed != undefined) {
							for (key in offer.immediateTermination.isAllowed) {
								if (offer.immediateTermination.isAllowed[key])
									$('#create-offer [name="' + key + '"]').prop('checked', true);
							}
						}

						if (offer.externalHandles != null && offer.externalHandles != undefined) {
							var handlesLength = offer.externalHandles.length;
							for (var i = 0; i < handlesLength; i++) {
								$('#assigned-fields-handler ul').append('<li>' + offer.externalHandles[i] + '<a class="search-choice-close"></a></li>');
							}
						}

						if (offer.whiteListedUsers != null && offer.whiteListedUsers != undefined) {
							var whiteListedUsersLength = offer.whiteListedUsers.length;
							for (var i = 0; i < whiteListedUsersLength; i++) {
								$('#assigned-fields-whitelistUser ul').append('<li>' + offer.whiteListedUsers[i] + '<a class="search-choice-close"></a></li>');
							}
						}

						if (offer.products != null && offer.products != undefined) {
							var productsLength = offer.products.length;
							for (var i = 0; i < productsLength; i++) {
								product = {};
								product.name = offer.products[i].name;

								product.owner = '';

								if (offer.products[i].owner != null && offer.products[i].owner != undefined) {
									product.owner = offer.products[i].owner.name;
								}

								if (offer.products[i].resource != null && offer.products[i].resource != undefined) {
									product.resource = offer.products[i].resource.name;
								} else {
									product.resource = "";
								}
								product.quota = "";

								if (offer.products[i].quota != null && offer.products[i].quota != undefined) {
									product.quota = offer.products[i].quota.type;
								}

								product.productValidity = "";

								var validityStr = "";

								if (offer.products[i].validity != null && offer.products[i].validity != undefined && offer.products[i].validity.type != null && offer.products[i].validity.type != undefined) {
									if (offer.products[i].validity.type == "DAYS") {
										var timeSinceEpoch = new Date().getTime();
										var hours = (offer.products[i].validity.expiryTimeInMillis - timeSinceEpoch) / 1000 / 60 / 60;
										product.productValidity = 'days';
										product.validityHours = (hours / 24).toString().split('.')[0];
										validityStr = 'Days(' + product.validityDays + ')';
									} else if (offer.products[i].validity.type == "HOURS") {
										var timeSinceEpoch = new Date().getTime();
										var hours = (offer.products[i].validity.expiryTimeInMillis - timeSinceEpoch) / 1000 / 60 / 60;
										product.productValidity = 'hours';
										product.validityHours = hours.toString().split('.')[0];
										validityStr = 'Hours(' + product.validityHours + ')';
									} else if (offer.products[i].validity.type == 'INFINITE') {
										product.productValidity = 'infinite';
										validityStr = 'Infinite';
									} else if (offer.products[i].validity.type == 'DATE') {
										var date = new Date(offer.products[i].validity.expiryTimeInMillis).toISOString().split('T')[0];
										product.productValidity = 'date';
										product.validityDate = date;
										validityStr = 'Date(' + date + ')';
									}
								}

								products.push(product);
								//$('#productList').append('<tr><td class="td1">' + product.name + '</td><td class="td2">' + product.owner + '</td><td class="td3">' + product.rule + '</td><td class="td4">' + product.resource + '</td><td class="td5"><img src="images/edit.png" class="deleteProduct" title="Edit Product" /><img src="images/delete-product.png" class="deleteProduct" title="Delete Product" /></td></tr>');
								$('#productList').append('<tr><td class="td1" style="width:80px;">' + product.name + '</td><td class="td2" style="width:70px;">' + product.owner + '</td><td class="td3" style="width:70px;">' + product.resource + '</td><td class="td4" style="width:70px;">' + product.quota + '</td><td class="td4" style="width:70px;">' +
									validityStr + '</td><td class="td5"><img src="images/delete-product.png" class="deleteProduct" title="Delete Product" /></td></tr>');
							}
						}

						$('#create-offer [name="name"]').prop('readonly', true);
						$('#content-right').show();
						$('.offer-history').hide();
						$('#content-right1').hide();
						$('#content-right2').hide();
						$('#content-service').hide();
						$('#content-profiles').hide();
						$('#content-policy').hide();
						$('#content-rule').hide();
						$('#ui-btn').hide();
						$('#ui-btn1').hide();
						$("#save-btn").hide();
						$("#cancel-btn").show();
						$("#delete-btn").hide();
						$("#update-btn").show();
					}
				});

				//Promote button is clicked
				$(".ui-btn-pop").unbind('click').click(function(e) {
					var checked = $('.promote-pop').find('input:radio:checked');
					if (checked.val() != null) {
						$("#dailog-area").data("kendoWindow").close();
						var url = '/sef-catalog-ui/promote?offerName=' + offerName + '&offerState=' + checked.val();
						$.ajax({
							url: url,
							type: "GET",
							contentType: 'application/json',
							success: function(data) {
								if (data.status == 'success') {
									OfferHandler.loadOffers();
									$('#error_message').show();
									$('#myform_succloc').css('color', 'green').text('Offer Promoted');
									//$("#update-btn").hide();
								} else {
									$('#error_message').show();
									$('#myform_succloc').css('color', 'red').text(data.message);
								}
							}

						});
					}
				});
			});


			$('#middle ul.k-treeview-lines li ul li div span.k-in').unbind('click').click(function(e) {
				e.preventDefault(); // disable normal link function so that it doesn't refresh the page
				var productName = $(this).text().trim();
				var $span = $(this).closest('li.k-item').parent().closest('li.k-item').find('span.k-in').first();
				$.ajax({
					url: '/sef-catalog-ui/readOffer?offerId=' + $span.text().trim(),
					type: "GET",
					async: false,
					success: function(data) {
						offer = JSON.parse(data);
						$('#dailog-message-div-offer span').html('');
						if (offer.products != null && offer.products != undefined) {
							var productsLength = offer.products.length;
							for (var i = 0; i < productsLength; i++) {
								if (productName === offer.products[i].name) {

									$('#dailog-message-div-offer span').append('<b>Product Name:</b> ' + offer.products[i].name + '<br>');
									if (offer.products[i].owner != null && offer.products[i].owner != undefined) {
										$('#dailog-message-div-offer span').append('<b>Owner Name:</b> ' + offer.products[i].owner.name + '<br>');
									}

									if (offer.products[i].resource != null && offer.products[i].resource != undefined) {
										$('#dailog-message-div-offer span').append('<b>Resource Name:</b> ' + offer.products[i].resource.name + '<br>');
									}

									if (offer.products[i].quota != null && offer.products[i].quota != undefined) {
										$('#dailog-message-div-offer span').append('<b>Quota: </b>' + offer.products[i].quota.type + '<br>');
									}

									var validityStr = "";

									if (offer.products[i].validity != null && offer.products[i].validity != undefined && offer.products[i].validity.type != null && offer.products[i].validity.type != undefined) {
										if (offer.products[i].validity.type == "DAYS") {
											var timeSinceEpoch = new Date().getTime();
											var hours = (offer.products[i].validity.expiryTimeInMillis - timeSinceEpoch) / 1000 / 60 / 60;
											validityStr = 'Days(' + product.validityDays + ')';
										} else if (offer.products[i].validity.type == "HOURS") {
											var timeSinceEpoch = new Date().getTime();
											var hours = (offer.products[i].validity.expiryTimeInMillis - timeSinceEpoch) / 1000 / 60 / 60;
											validityStr = 'Hours(' + product.validityHours + ')';
										} else if (offer.products[i].validity.type == 'INFINITE') {
											validityStr = 'Infinite';
										} else if (offer.products[i].validity.type == 'DATE') {
											var date = new Date(offer.products[i].validity.expiryTimeInMillis).toISOString().split('T')[0];
											validityStr = 'Date(' + date + ')';
										}
									}
									$('#dailog-message-div-offer span').append('<b>Validity:</b> ' + validityStr + '<br>');
									break;
								}
							}
						}
						var docHeight = $(document).height(); //grab the height of the page
						var scrollTop = $(window).scrollTop(); //grab the px value from the top of the page to where you're scrolling
						$('.overlay-bg-delete-offer').show().css({
							'height': docHeight
						}); //display your popup and set height to the page height
						$('.overlay-content-delete-offer').css({
							'top': scrollTop + 20 + 'px'
						}); //set the content 20px from the window top
					}
				});
			});

			$('#middle ul.k-treeview-lines li ul li ul li div span.k-in').unbind('click').click(function(e) {
				e.preventDefault(); // disable normal link function so that it doesn't refresh the page
				var resourceName = $(this).text().trim();
				$.ajax({
					url: '/sef-catalog-ui/readResource?resourceName=' + resourceName,
					type: "GET",
					async: false,
					success: function(data) {
						json = JSON.parse(data);
						resource = json.resource;
						profile = json.profile;
						profileClass = json.profileClass;


						$('#dailog-message-div-offer span').html('');

						$('#dailog-message-div-offer span').append('<b>Resource Name:</b> ' + resource.name + '<br>');
						$('#dailog-message-div-offer span').append('<b>Description:</b> ' + resource.description + '<br>');
						$('#dailog-message-div-offer span').append('<b>IsAbstract:</b> ' + resource.abstract + '<br>');
						if (!resource.abstract && resource.cost != null && resource.cost != undefined && resource.cost.amount != null && resource.cost.amount != undefined) {
							$('#dailog-message-div-offer span').append('<b>Cost: ' + resource.cost.amount + '<br>');
						}
						$('#dailog-message-div-offer span').append('<b>IsDiscoverable:</b> ' + resource.discoverable + '<br>');
						$('#dailog-message-div-offer span').append('<b>IsExtrnally Consumed:</b> ' + resource.externallyConsumed + '<br>');
						$('#dailog-message-div-offer span').append('<b>IsConsumable:</b> ' + resource.consumable + '<br>');
						if (resource.consumable) {
							$('#dailog-message-div-offer span').append('<b>Consumption Unit Name:</b> ' + resource.consumptionUnitName + '<br>');
							$('#dailog-message-div-offer span').append('<b>Min Quota:</b> ' + resource.enforcedMinQuota + '<br>');
							$('#dailog-message-div-offer span').append('<b>Max Quota:</b> ' + resource.enforcedMaxQuota + '<br>');
						}

						var docHeight = $(document).height(); //grab the height of the page
						var scrollTop = $(window).scrollTop(); //grab the px value from the top of the page to where you're scrolling
						$('.overlay-bg-delete-offer').show().css({
							'height': docHeight
						}); //display your popup and set height to the page height
						$('.overlay-content-delete-offer').css({
							'top': scrollTop + 20 + 'px'
						}); //set the content 20px from the window top
					}

				});

			});
		}

		//Hides third pane on load by default

		//End of all eventsproductOwner

		$('#panel3 input[name="productOwner"]').unbind('focusin').focusin(function() {
			$("#select-policy-rule").hide();
			$("#select-owner-group").hide();
			$("#select-offer-group").hide();
			$("#select-product-owner").show();
			$('#select-product-resource').hide();
			$('#ws-search').hide();
		});

		$('#panel3 input[name="productRule"]').unbind('focusin').focusin(function() {
			$("#select-policy-rule").hide();
			$("#select-owner-group").hide();
			$("#select-offer-group").hide();
			$("#select-product-owner").hide();
			$('#select-product-resource').hide();
			$('#ws-search').hide();
		});

		$('#panel input[name="rule"]').unbind('focusin').focusin(function() {
			$("#select-policy-rule").show();
			$("#select-owner-group").hide();
			$("#select-offer-group").hide();
			$("#select-product-owner").hide();
			$('#select-product-resource').hide();
			$('#ws-search').hide();
		});

		$('#panel3 input[name="productResource"]').unbind('focusin').focusin(function() {
			$('#ws-search').show();
			$("#ws-search .search").val('');
			$("#ws-search .search").keyup();
			$("#select-policy-rule").hide();
			$("#select-owner-group").hide();
			$("#select-offer-group").hide();
			$("#select-product-owner").hide();
			$('#select-product-resource').show();
		});

		$('#add-whitelist').unbind('click').click(function() {

			if ($("#panel-whitelist input[name='whitelistUser']").val() === "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('This User cannot be empty.');
				return false;
			}

			var isExist = false;
			$('#assigned-fields-whitelistUser ul li').each(function() {
				if ($(this).text() === $("#panel-whitelist input[name='whitelistUser']").val()) {
					$('#error_message').show();
					$('#myform_succloc').css('color', 'red').text('This User already exist, Use different name.');
					isExist = true;
					return false;
				}
			});
			if (isExist) {
				return false;
			}
			$('#assigned-fields-whitelistUser ul').append('<li>' + $("#panel-whitelist input[name='whitelistUser']").val() + '<a class="search-choice-close"></a></li>');
			$("#panel-whitelist input[name='whitelistUser']").val("");
		});

		$('#add-handler').unbind('click').click(function() {

			if ($("#panel2 input[name='handler']").val() === "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('This External Handler cannot be empty.');
				return false;
			}

			var isExist = false;
			$('#assigned-fields-handler ul li').each(function() {
				if ($(this).text() === $("#panel2 input[name='handler']").val()) {
					$('#error_message').show();
					$('#myform_succloc').css('color', 'red').text('This External Handler already exist, Use different name.');
					isExist = true;
					return false;
				}
			});
			if (isExist) {
				return false;
			}
			$('#assigned-fields-handler ul').append('<li>' + $("#panel2 input[name='handler']").val() + '<a class="search-choice-close"></a></li>');
			$("#panel2 input[name='handler']").val("");
		});

		$('#add-tax').unbind('click').click(function() {
			if ($("#taxtype select[name='tax']").val() === "Percentage") {
				$('#assigned-fields-tax ul').append('<li>' + $("#taxtype select[name='tax']").val() + '  ' + $("#taxtype input[name='taxValue']").val() + '<a class="search-choice-close"></a></li>');
			} else if ($("#taxtype select[name='tax']").val() === "Absolute") {
				$('#assigned-fields-tax ul').append('<li>' + $("#taxtype select[name='tax']").val() + '  ' + $("#taxtype input[name='taxValue']").val() + '(' + $("#panel select[name='currency']").val() + ')<a class="search-choice-close"></a></li>');
			} else {
				$('#assigned-fields-tax ul').append('<li>' + $("#taxtype select[name='tax']").val() + '<a class="search-choice-close"></a></li>');
			}
			$("#taxtype input[name='taxValue']").val('');
		});

		$(document).off('click', '.search-choice-close').on('click', '.search-choice-close', function() {
			$(this).closest('li').remove();
		});


		$('#add-product').unbind('click').click(function() {

			if ($("#panel3 input[name='productName']").val() === "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('Product name cannot be empty.');
				return false;
			}

			if ($("#panel3 input[name='productResource']").val() === "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('Resource name cannot be empty.');
				return false;
			}

			var isExist = false;
			$('#productList tr').each(function() {
				if ($(this).find('td:first').text() === $("#panel3 input[name='productName']").val()) {
					$('#error_message').show();
					$('#myform_succloc').css('color', 'red').text('This Product already exist, Use different name.');
					isExist = true;
					return false;
				}
			});
			if (isExist) {
				return false;
			}
			product = {};

			var validityStr = "";

			if ($("#panel3 input[name='productValidity']:checked").val() == "infinite") {
				product.productValidity = 'infinite';
				validityStr = 'Infinite';
			} else if ($("#panel3 input[name='productValidity']:checked").val() == "hours") {
				product.productValidity = 'hours';
				if ($("#panel3 input[name='validityHours']").val() == "" || $("#panel3 input[name='validityHours']").val() > 24) {
					$('#error_message').show();
					$('#myform_succloc').css('color', 'red').text('Enter Validity Hours in between 0 and 24');
					return false;
				}
				product.validityHours = $("#panel3 input[name='validityHours']").val();
				validityStr = 'Hours(' + product.validityHours + ')';
			} else if ($("#panel3 input[name='productValidity']:checked").val() == "days") {
				product.productValidity = 'days';
				if ($("#panel3 input[name='validityDays']").val() == "") {
					$('#error_message').show();
					$('#myform_succloc').css('color', 'red').text('Enter Validity Days.');
					return false;
				}
				product.validityDays = $("#panel3 input[name='validityDays']").val();
				validityStr = 'Days(' + product.validityDays + ')';
			} else if ($("#panel3 input[name='productValidity']:checked").val() == "date") {
				product.productValidity = 'date';
				if ($("#panel3 input[name='validityDate']").val() == "") {
					$('#error_message').show();
					$('#myform_succloc').css('color', 'red').text('Enter Validity Date.');
					return false;
				}
				product.validityDate = $("#panel3 input[name='validityDate']").val();
				validityStr = 'Date(' + product.validityDate + ')';
			}

			product.name = $("#panel3 input[name='productName']").val();
			product.owner = $("#panel3 input[name='productOwner']").val();
			product.resource = $("#panel3 input[name='productResource']").val();
			product.quota = $("#panel3 select[name='productQuota']").val();
			products.push(product);



			$('#productList').append('<tr><td class="td1" style="width:80px;">' + $("#panel3 input[name='productName']").val() + '</td><td class="td2" style="width:70px;">' + $("#panel3 input[name='productOwner']").val() + '</td><td class="td3" style="width:70px;">' + $("#panel3 input[name='productResource']").val() + '</td><td class="td4" style="width:70px;">' + $("#panel3 select[name='productQuota']").val() + '</td><td class="td4" style="width:70px;">' +
				validityStr + '</td><td class="td5"><img src="images/delete-product.png" class="deleteProduct" title="Delete Product" /></td></tr>');
			$('#panel3').find('input:text').val('');
		});

		$(document).off('click', '.deleteProduct').on('click', '.deleteProduct', function() {
			var thi = this;
			$.each(products, function(index, value) {
				if (value != null && value != undefined && value.name === $(thi).closest('tr').find('td:first').text()) {
					products.splice(index, 1);
				}
			});
			$(this).closest('tr').remove();
		});

		// When add offer click
		$(document).ready(function() {
			$('#ui-btn').unbind('click').click(function() {
				products = [];
				$('#productList').html('');
				$('#assigned-fields-handler ul').html('');
				$('#assigned-fields-whitelistUser ul').html('');
				$("#content-right").show();
				$("#save-btn").show();
				$("#cancel-btn").show();
				$('#ui-btn').hide();
				$('.offer-history').hide();
				$("#delete-btn").hide();
				$("#update-btn").hide();
				$('#create-offer [name="name"]').prop('readonly', false);
				$('#content-right').find('input:text, input:password, input:file, select, textarea').val('');
				$('#content-right').find('input:radio, input:checkbox')
					.removeAttr('checked').removeAttr('selected');
				$('#content-right').find('input[name="offerState"]').val('IN_CREATION');
				$('#content-right').find('input[value="NO_TERMINATION"]').prop("checked", true);
				$('#content-right').find('input[name="renewalPeriod"][value="infinite"]').prop("checked", true);
				$('#content-right').find('input[value="NO_COMMITMENT"]').prop("checked", true);
				$('#content-right').find('input[name="commercial"][value="true"]').prop('checked', true);
				$('#content-right').find('input[name="recurrent"][value="false"]').prop('checked', true);
				$('#validity').show();
				$('#setreneval').hide();
				$('.blue').show();
				$('#content-right').find('input[name="renewalDays"]').hide();
				$('#content-right').find('input[name="renewalHours"]').hide();
				$('#content-right').find('input[name="renewalDate"]').hide();
				$('#content-right').find('#assigned-fields-tax ul').html('');
				$('#content-right').find('input[name="taxValue"]').show();
				$('#content-right').find('#offer-group-hs').hide();
				$('#content-right').find('input[name="autoTerminationRenewals"]').hide();
				$('#content-right').find('input[name="autoTerminationDays"]').hide();
				$('#content-right').find('input[name="autoTerminationDate"]').hide();
				$('#content-right').find('input[name="minimumCommitmentRenewals"]').hide();
				$('#content-right').find('input[name="minimumCommitmentDays"]').hide();
				$('#content-right').find('input[name="trialHours"]').hide();
				$('#content-right').find('input[name="trialDays"]').hide();
				$('#content-right').find('input[name="trialDate"]').hide();
				$("#setreneval").hide();
				$('#txt').hide();
				$('#txt1').hide();
				//$('#content-service').hide();//i add//

			});
		});

		$('#save-btn').unbind('click').click(function() {
			if ($('#offers-menu').hasClass('active')) {

				var offerName = $('#create-offer input[name="name"]').val();

				if (offerName != null && offerName != undefined && offerName != "") {
					$.ajax({
						url: '/sef-catalog-ui/isOfferExists?offerId=' + offerName,
						type: "GET",
						async: false,
						contentType: 'application/json',
						success: function(data) {
							if (data.status == 'success') {
								$('#error_message').show();
								$('#myform_succloc').css('color', 'red').text("Offer exists");
								return;
							}
						}
					});

				} else {
					$('#error_message').show();
					$('#myform_succloc').css('color', 'red').text("Offer name cannot be empty.");
					return;
				}

				var offer = $('#create-offer').serializeObject();

				var taxes = [];

				$('#assigned-fields-tax ul li').each(function() {
					var tax = {};
					var array = $(this).text().split('  ');
					tax.taxType = array[0];
					if (array.length > 1) {
						tax.value = array[1].split('(')[0];
					}
					taxes.push(tax);
				});

				offer.taxes = taxes;

				externalHandlers = [];
				$('#assigned-fields-handler ul li').each(function() {
					externalHandlers.push($(this).text());
				});
				offer.externalHandlers = externalHandlers;

				whitelistUsers = [];
				$('#assigned-fields-whitelistUser ul li').each(function() {
					whitelistUsers.push($(this).text());
				});
				offer.whitelistUsers = whitelistUsers;

				offer.products = products;

				$.ajax({
					url: '/sef-catalog-ui/createOffer',
					type: "POST",
					async: false,
					contentType: 'application/json',
					data: JSON.stringify(offer),
					success: function(data) {
						if (data.status == 'success') {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'green').text("Offer Created Successfully");

							OfferHandler.loadOffers();

						} else {
							$('#myform_succloc').css('color', 'red').text(data.message);
							$('#error_message').show();
						}
					}

				});

				$("#select-policy-rule").hide();
				$("#select-owner-group").hide();
				$("#select-offer-group").hide();
				$("#select-product-owner").hide();
				$('#select-product-resource').hide();
				$('#ws-search').hide();
			}
		});

		$('#update-btn').unbind('click').click(function(e) {
			e.preventDefault();

			if ($('#offers-menu').hasClass('active')) {

				var offer = $('#create-offer').serializeObject();

				var taxes = [];

				$('#assigned-fields-tax ul li').each(function() {
					var tax = {};
					var array = $(this).text().split('  ');
					tax.taxType = array[0];
					if (array.length > 1) {
						tax.value = array[1].split('(')[0];
					}
					taxes.push(tax);
				});

				offer.taxes = taxes;

				externalHandlers = [];
				$('#assigned-fields-handler ul li').each(function() {
					externalHandlers.push($(this).text());
				});
				offer.externalHandlers = externalHandlers;

				whitelistUsers = [];
				$('#assigned-fields-whitelistUser ul li').each(function() {
					whitelistUsers.push($(this).text());
				});
				offer.whitelistUsers = whitelistUsers;

				offer.products = products;

				$.ajax({
					url: '/sef-catalog-ui/updateOffer',
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(offer),
					success: function(data) {
						if (data.status == 'success') {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'green').text('Offer Updated Successfully');
						} else {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'red').text(data.message);
						}
					}

				});
				$("#select-policy-rule").hide();
				$("#select-owner-group").hide();
				$("#select-offer-group").hide();
				$("#select-product-owner").hide();
				$('#select-product-resource').hide();
				$('#ws-search').hide();
			}

		});

		$("#create-offer input[name='name']").unbind('focusout').focusout(function(e) {
			var offerName = $('#create-offer input[name="name"]').val();

			if (offerName != null && offerName != undefined && offerName != "") {
				$.ajax({
					url: '/sef-catalog-ui/isOfferExists?offerId=' + offerName,
					type: "GET",
					async: false,
					contentType: 'application/json',
					success: function(data) {
						if (data.status == 'success')
							$('#error_message').show();
						$('#myform_succloc').css('color', 'red').text("Offer exists");
						return;
					}

				});
			} else {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text("Offer name cannot be empty");
				return;
			}

		});


		$("#right1").mCustomScrollbar("destroy");

		/* all available option parameters with their default values */
		$("#right1").mCustomScrollbar({
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
					workspaceToggle();
				},
				onTotalScrollBack: false,
				whileScrolling: false,
				onTotalScrollOffset: 50,
				onTotalScrollBackOffset: 0,
				alwaysTriggerOffsets: true
			},
			live: false
		});


		function workspaceToggle() {
			if ($('#select-offer-group').is(':visible')) {
				getAllOffersGroups();
			}
			if ($('#select-policy-rule').is(':visible')) {
				getAllRules();
			}
			if ($('#select-owner-group').is(':visible')) {
				getAllOwners();
			}
			if ($('#select-product-owner').is(':visible')) {
				getAllProductOwners();
			}
			if ($('#select-product-resource').is(':visible')) {
				getAllResources();
			}
		}

		function getAllOffersGroups() {
			$.ajax({
				url: '/sef-catalog-ui/getAllOffersGroups',
				type: "GET",
				contentType: 'application/json',
				success: function(data) {
					if (data.status == 'success') {
						/* $('#right1').html('<div id="right1_errorloc" style="color:#ff0000;float:left; width:100%; display:none;"></div>');
						    $('#right1_errorloc').show().text(data.message);*/
						json = JSON.parse(data.responseString);
						offerGroupData = [];
						for (var key in json) {
							var offerGroup = {};
							offerGroup.text = key;
							offerGroup.expanded = false;
							offerGroup.items = [];
							offerGroup.spriteCssClass = "rootfolder";

							offerGroupData.push(offerGroup);
						}

						if ($('#select-offer-group')
							.hasClass('k-treeview')) {
							treeview = $("#select-offer-group").data("kendoTreeView");
							treeview.setDataSource(new kendo.data.HierarchicalDataSource({
								data: offerGroupData
							}));

						} else {
							$("#select-offer-group").kendoTreeView({
								template: kendo.template($("#middle-template").html()),
								checkboxTemplate: kendo.template($("#treeview-checkbox-template").html()),
								dataSource: offerGroupData,
							}).data("kendoTreeView");
						}
						if($('#hasOfferUpdate').val() === 'false'){
							$("#middle div span.k-checkbox").hide();
						}
						$('#select-offer-group li .delete-link').hide();
						$('#select-offer-group li .edit-link').hide();

						$("#select-offer-group").data("kendoTreeView").dataSource.bind("change", function(e) {
							e.preventDefault();

							if (!e.items[0].checked) {
								$("#create-offer input[name='offerGroup']").val('');
								return;
							}

							$("#create-offer input[name='offerGroup']").val(e.items[0].text);

							$("#select-offer-group input[name*='checkedFiles']").prop('checked', false);

							$("#select-offer-group #select-offer-group_tv_active input").prop('checked', true);


						});
					}
				}
			});
		}


		function getAllOwners() {
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
						owner.type = owners[key].type;
						owner.name = owners[key].name;
						owner.expanded = false;
						owner.items = [];
						owner.spriteCssClass = "rootfolder";


						ownerGroupData1.push(owner);
					}
					if ($('#select-owner-group')
						.hasClass('k-treeview')) {
						treeview = $("#select-owner-group").data("kendoTreeView");
						treeview.setDataSource(new kendo.data.HierarchicalDataSource({
							data: ownerGroupData1,
						}));


					} else {
						$treeview = $("#select-owner-group").kendoTreeView({
							template: kendo.template($("#middle-template").html()),
							dataSource: ownerGroupData1,
							checkboxTemplate: kendo.template($("#treeview-checkbox-template").html()),
						}).data("kendoTreeView");

					}
					$('#select-owner-group li .delete-link').hide();
					$('#select-owner-group li .edit-link').hide();


					$("#select-owner-group").data("kendoTreeView").dataSource.bind("change", function(e) {
						e.preventDefault();

						if (!e.items[0].checked) {
							$("#create-offer input[name='ownerName']").val('');
							$("#create-offer input[name='ownerType']").val('');
							return;
						}

						$("#create-offer input[name='ownerName']").val(e.items[0].name);

						$("#create-offer input[name='ownerType']").val(e.items[0].type);

						$("#select-owner-group input[name*='checkedFiles']").prop('checked', false);

						$("#select-owner-group #select-owner-group_tv_active input").prop('checked', true);


					});
				}
			});
		}

		function getAllRules() {
			$.ajax({
				url: '/sef-catalog-ui/readAllRules',
				type: "GET",
				success: function(data) {
					if (data.status == 'success') {
						json = JSON.parse(data.responseString);
						var ruleData = [];

						for (var key in json) {
							var rule = {};

							rule.text = key;
							rule.expanded = false;
							rule.spriteCssClass = "rootfolder";
							ruleData.push(rule);
						}

						if ($('#select-policy-rule')
							.hasClass('k-treeview')) {
							treeview = $("#select-policy-rule").data("kendoTreeView");
							treeview.setDataSource(new kendo.data.HierarchicalDataSource({
								data: ruleData,
							}));


						} else {
							$treeview = $("#select-policy-rule").kendoTreeView({
								template: kendo.template($("#middle-template").html()),
								dataSource: ruleData,
								checkboxTemplate: kendo.template($("#treeview-checkbox-template").html()),
							}).data("kendoTreeView");

						}
						$('#select-policy-rule li .delete-link').hide();
						$('#select-policy-rule li .edit-link').hide();


						$("#select-policy-rule").data("kendoTreeView").dataSource.bind("change", function(e) {
							e.preventDefault();

							if (!e.items[0].checked) {
								$("#panel input[name='rule']").val('');
								return;
							}

							$("#panel input[name='rule']").val(e.items[0].text);

							$("#select-policy-rule input[name*='checkedFiles']").prop('checked', false);

							$("#select-policy-rule #select-policy-rule_tv_active input").prop('checked', true);

						});
					}
				}

			});
		}

		resourceData = [];

		var searchResource = "";

		$("#ws-search .search").unbind("keyup").keyup(function() {
			pageNumber1 = 1;
			resourceData = [];
			searchResource = this.value;
			getAllResources();
		});

		var pageNumber1 = 1;

		function getAllResources() {
			$.ajax({
				url: '/sef-catalog-ui/getAllResources?pageNumber=' + pageNumber1 + '&pageSize=30&search=' + searchResource,
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
						resourceData.push(resource);
					}


					if ($('#select-product-resource')
						.hasClass('k-treeview')) {
						treeview = $("#select-product-resource").data("kendoTreeView");
						treeview.setDataSource(new kendo.data.HierarchicalDataSource({
							data: resourceData,
						}));


					} else {
						$treeview = $("#select-product-resource").kendoTreeView({
							template: kendo.template($("#middle-template").html()),
							dataSource: resourceData,
							checkboxTemplate: kendo.template($("#treeview-checkbox-template").html()),
						}).data("kendoTreeView");

					}
					$('#select-product-resource li .delete-link').hide();
					$('#select-product-resource li .edit-link').hide();
					pageNumber1++;

					$("#select-product-resource").data("kendoTreeView").dataSource.bind("change", function(e) {
						e.preventDefault();

						if (!e.items[0].checked) {
							$("#panel3 input[name='productResource']").val('');
							return;
						}

						$("#panel3 input[name='productResource']").val(e.items[0].text);

						$("#select-product-resource input[name*='checkedFiles']").prop('checked', false);

						$("#select-product-resource #select-product-resource_tv_active input").prop('checked', true);

					});

				}

			});

		}

		$(document).off("click", "input[name*='checkedFiles']").on("click", "input[name*='checkedFiles']", function(e) {
			if ($(this).prop('checked') == true) {
				$("input[name*='checkedFiles']:checked").prop('checked', false);
				$(this).prop('checked', true);
			} else {
				$("input[name*='checkedFiles']").prop('checked', false);
			}
		});

		function getAllProductOwners() {
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
						owner.name = owners[key].name;
						owner.expanded = false;
						owner.items = [];
						owner.spriteCssClass = "rootfolder";


						ownerGroupData1.push(owner);
					}
					if ($('#select-product-owner')
						.hasClass('k-treeview')) {
						treeview = $("#select-product-owner").data("kendoTreeView");
						treeview.setDataSource(new kendo.data.HierarchicalDataSource({
							data: ownerGroupData1,
						}));


					} else {
						$treeview = $("#select-product-owner").kendoTreeView({
							template: kendo.template($("#middle-template").html()),
							dataSource: ownerGroupData1,
							checkboxTemplate: kendo.template($("#treeview-checkbox-template").html()),
						}).data("kendoTreeView");

					}
					$('#select-product-owner li .delete-link').hide();
					$('#select-product-owner li .edit-link').hide();


					$("#select-product-owner").data("kendoTreeView").dataSource.bind("change", function(e) {
						e.preventDefault();

						if (!e.items[0].checked) {
							$("#panel3 input[name='productOwner']").val('');
							return;
						}

						$("#panel3 input[name='productOwner']").val(e.items[0].name);

						$("#select-product-owner input[name*='checkedFiles']").prop('checked', false);

						$("#select-product-owner #select-product-owner_tv_active input").prop('checked', true);
					});
				}
			});
		}

		getAllOffersGroups();
		getAllOwners();
		getAllRules();
		getAllProductOwners();
		getAllResources();

	},
};
// End of OfferHandler
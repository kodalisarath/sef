var BusinessHandler = {
	loadBusiness: function() {
		$('#rule-btn').show();
		$('#resource-search').hide();
		$('#ws-search').hide();
		$('#offer-search').hide();
		$("#resourcegroup-search").hide();
		$("#offergroup-search").hide();
		$("#privileges-search").hide();
		//$('#middle').height('600');
		$('#middle_rule').show();
		$("#content-rule").hide();
		$("#rule_search").show();
		$("#select-policy-rule").hide();
		$("#select-owner-group").hide();
		$("#select-offer-group").hide();
		$("#select-product-owner").hide();
		$('#select-product-resource').hide();
		$('#notification-details').hide();
		$('#privileges-content').hide();
		$('#content-group-mgt').hide();
		$('#content-user-mgt').hide();
		$('#middle').hide();
		$('#notification_search').hide();
		$('#middle2').hide();
		$('#middle3').hide();
		$('#middle5').hide();
		$('#middle7').hide();
		$('#middle8').hide();
		$('#ui-btn').hide();
		$('#ui-btn12').hide();
		$('#ui-btn-og').hide();
		$('#ui-btn1').hide();
		$(".delete-link").hide();
		$(".edit-link").hide();
		$('#content-right').hide();
		// $('#content-service').hide();//i add//
		$('#content-resource-group').hide();
		$('#add-resource-group').hide();
		$('#offer-catalog-group-service').hide();
		$('#add-offer-catalog-group').hide();
		$('#content-group').hide();
		$('#notification-add').hide();
		$('#notification-edit').hide();
		$('#notification_create_content').hide();
		$('#editn-btn').hide();
		$('#addn-btn').hide();
		$('#create-owner-group').hide();
		$('#add-owner-group').hide();
		$("#content-rule").hide();
		$('#content-right').hide();
		$('.ui-btn').hide();
		$('#ui-btn12').hide();
		$('#ui-btn1').hide();
		$('#content-right1').hide();
		$('#content-right2').hide();
		$('#content-service').hide();
		$('#content-profiles').hide();
		$('#content-policy').hide();
		$("#content-middle .search").val('');

		jQuery('select[name="condition"]').unbind('change').change(function() {

			if (jQuery(this).val() === "ENUMERATED") {
				jQuery('#enum-brm').slideDown('slow');
			} else {
				jQuery('#enum-brm').slideUp('fast');
			}
			if (jQuery(this).val() === "EXCLUSION_LIST") {
				jQuery('#exclusion-brm').slideDown('slow');
			} else {
				jQuery('#exclusion-brm').slideUp('fast');
			}
			if (jQuery(this).val() === "RANGE" || jQuery(this).val() === "NOT_IN_RANGE") {
				jQuery('#range-brm').slideDown('slow');
			} else {
				jQuery('#range-brm').slideUp('fast');
			}
			if (jQuery(this).val() === "STARTS_WITH") {
				jQuery('#start-brm').slideDown('slow');
			} else {
				jQuery('#start-brm').slideUp('fast');
			}
			if (jQuery(this).val() === "DOESNT_START_WITH") {
				jQuery('#doesntstart-brm').slideDown('slow');
			} else {
				jQuery('#doesntstart-brm').slideUp('fast');
			}
			if (jQuery(this).val() === "ENDS_WITH") {
				jQuery('#end-brm').slideDown('slow');
			} else {
				jQuery('#end-brm').slideUp('fast');
			}
			if (jQuery(this).val() === "DOESNT_END_WITH") {
				jQuery('#doesntend-brm').slideDown('slow');
			} else {
				jQuery('#doesntend-brm').slideUp('fast');
			}
			if (jQuery(this).val() === "CONTAINS") {
				jQuery('#contain-brm').slideDown('slow');
			} else {
				jQuery('#contain-brm').slideUp('fast');
			}
			if (jQuery(this).val() === "DOESNT_CONTAIN") {
				jQuery('#doesntcontain-brm').slideDown('slow');
			} else {
				jQuery('#doesntcontain-brm').slideUp('fast');
			}
			if (jQuery(this).val() === "MATCHES") {
				jQuery('#match-brm').slideDown('slow');
			} else {
				jQuery('#match-brm').slideUp('fast');
			}
			if (jQuery(this).val() === "NOT_MATCHES") {
				jQuery('#notmatch-brm').slideDown('slow');
			} else {
				jQuery('#notmatch-brm').slideUp('fast');
			}
			if (jQuery(this).val() === "EQUALS" || jQuery(this).val() === "NOT_EQUALS") {
				jQuery('#equal-brm').slideDown('slow');
			} else {
				jQuery('#equal-brm').slideUp('fast');
			}
			if (jQuery(this).val() === "GREATER_THAN" || jQuery(this).val() === "GREATER_THAN_OR_EQUALS" || jQuery(this).val() === "LESSER_THAN" || jQuery(this).val() === "LESSER_THAN_OR_EQUALS") {
				jQuery('#greaterthan-brm').slideDown('slow');
			} else {
				jQuery('#greaterthan-brm').slideUp('fast');
			}
		});
		jQuery('input[name="conditionValue"]').unbind('change').change(function() {
			if (jQuery(this).val() === "number") {
				jQuery('#number').show();
			} else {
				jQuery('#number').hide();
			}

			if (jQuery(this).val() === "date") {
				jQuery('#date').show();
			} else {
				jQuery('#date').hide();
			}
		});

		var nonAssnRuleUnitArray = [];

		$('#add-brm').unbind('click').click(function() {
			if ($('#panel4 input[name="unitName"]').val() == "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('This Rule Unit name cannot be empty.');
				return false;
			}
			if ($('#panel4 input[name="condition"]').val() == "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('This Condition cannot be empty.');
				return false;
			}
			if ($('#panel4 input[name="ruleVariable"]').val() == "") {
				$('#error_message').show();
				$('#myform_succloc').css('color', 'red').text('This Rule Variable cannot be empty.');
				return false;
			}
			var ruleUnit = {};
			$('#panel4').find('input:text, select, input[type="radio"]:checked, input[type="checkbox"]:checked, input[type="date"]').each(function() {
				if (ruleUnit[$(this).attr('name')] != null && ruleUnit[$(this).attr('name')] != undefined) {

					if (ruleUnit[$(this).attr('name')] instanceof Array) {
						ruleUnit[$(this).attr('name')].push($(this).val());
					} else {
						var temp = ruleUnit[$(this).attr('name')];
						ruleUnit[$(this).attr('name')] = [];
						ruleUnit[$(this).attr('name')].push(temp);
						ruleUnit[$(this).attr('name')].push($(this).val());
					}
				} else {
					ruleUnit[$(this).attr('name')] = $(this).val();
				}
			});
			var isExist = false;

			$.each(nonAssnRuleUnitArray, function(index, value) {
				if (value.unitName === ruleUnit.unitName) {
					$('#error_message').show();
					$('#myform_succloc').css('color', 'red').text('This Rule Unit already exist, Use different name.');
					isExist = true;
					return false;
				}
			});

			if (isExist) {
				return false;
			}

			nonAssnRuleUnitArray.push(ruleUnit);
			$('#nonassigned-fields-unit').append('<option value="' + ruleUnit.unitName + '">' + ruleUnit.unitName + '</option>');
			$('#panel4').find('input:text, input[type="date"], input:password, input:file, select, textarea').val('');
			$('#panel4').find('input:radio, input:checkbox')
				.removeAttr('checked').removeAttr('selected');
		});


		allRules = {};

		$.ajax({
			url: '/sef-catalog-ui/readAllRules',
			async: false,
			type: "GET",
			success: function(data) {
				if (data.status == 'success') {
					json = JSON.parse(data.responseString);
					allRules = json;
					var rule_upper = $('#middle_rule .rule-upper:first').show().clone();
					var rulesetHtml = $('#middle_rule .ruleset .rule-main:first').clone();
					$('#middle_rule').html('');
					for (var key in json) {

						$('#nonassigned-fields-rule').append('<option value="' + key + '">' + key + '</option>');

						var rule_upper = $(rule_upper).clone();
						$(rule_upper)
						$(rule_upper).find('h4').text(json[key].name);
						$(rule_upper).find('.rule-gate').text(json[key].logicGate);

						$(rule_upper).find('.ruleset').html('');
						var rulsetArry = json[key].ruleset;
						var rulsetLength = rulsetArry.length;
						for (var i = 0; i < rulsetLength; i++) {
							var rulesetHtml = $(rulesetHtml).clone();

							var ruleUnit = "";

							if (rulsetArry[i].ruleset != undefined && rulsetArry[i].ruleset != null) {
								ruleUnit = "Rule: " + rulsetArry[i].name;
								$(rulesetHtml).find('div').text(ruleUnit);

							} else {
								ruleUnit = rulsetArry[i].ruleName + "  |  " + rulsetArry[i].baseObjectName +
									"  |  " + rulsetArry[i].evaluate.type + " THIS '";

								var values = rulsetArry[i].evaluate.values;

								for (var j = 0; j < values.length; j++) {
									ruleUnit = ruleUnit + values[j];
									if (values.length > j + 1)
										ruleUnit = ruleUnit + "', '";
									else
										ruleUnit = ruleUnit + "'";
								}

								$(rulesetHtml).find('div').text(ruleUnit + "  |  " + rulsetArry[i].ruleVariable);
							}


							$(rule_upper).find('.ruleset').append(rulesetHtml);
						}
						$('#middle_rule').append(rule_upper);
					}
				}
			}
		});

		$("#middle_rule").mCustomScrollbar("destroy");

		/* all available option parameters with their default values */
		$("#middle_rule").mCustomScrollbar({
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

		var deleteRule = "";

		$(document).off('click', '#middle_rule .delete-rule').on('click', '#middle_rule .delete-rule', function() {
			deleteRule = $(this).closest('.rule-upper').find('h4').text();
			var docHeight = $(document).height(); //grab the height of the page
			var scrollTop = $(window).scrollTop(); //grab the px value from the top of the page to where you're scrolling
			$('.overlay-bg-delete').show().css({
				'height': docHeight
			}); //display your popup and set height to the page height
			$('.overlay-content-delete').css({
				'top': scrollTop + 20 + 'px'
			}); //set the content 20px from the window top
		});

		$('.ok-btn').unbind('click').click(function() {
			$.ajax({
				url: '/sef-catalog-ui/deleteRule?ruleName=' + deleteRule,
				type: "GET",
				async: false,
				success: function(data) {
					$('.overlay-bg-delete').hide();
					if (data.status == 'success') {
						$('#error_message').show();
						$('#myform_succloc').css('color', 'green').text('Rule deleted Successfully');
						BusinessHandler.loadBusiness();
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

		$(document).off('click', '#middle_rule .edit-rule').on('click', '#middle_rule .edit-rule', function() {
			$("#appendenumerated-list").html('');
			$("#appendenumerated").html('');
			$('#create-rule #panel4 .price div:first').hide();
			var json = {};

			readRule = $(this).closest('.rule-upper').find('h4').text();

			$.ajax({
				url: '/sef-catalog-ui/readRule?ruleName=' + readRule,
				type: "GET",
				async: false,
				success: function(data) {
					if (data.status == 'success') {
						json = JSON.parse(data.responseString);
					} else {
						return false;
					}
				}
			});
			$("#save-btn-brm").hide();
			$("#update-btn-brm").show();
			$("#cancel-btn-brm").show();
			$("#content-rule").show();
			$('#create-rule').find('input:text, input[type="date"], input:password, input:file, select, textarea').val('');
			$('#create-rule').find('input:radio, input:checkbox')
				.removeAttr('checked').removeAttr('selected');

			$('#nonassigned-fields-unit').html('');
			$('#nonassigned-fields-rule').html('');
			$('#assigned-fields-unit').html('');

			$('#assigned-fields-rule').html('');

			for (var key in allRules) {
				if (readRule === key)
					continue;
				$('#nonassigned-fields-rule').append('<option value="' + key + '">' + key + '</option>');
			}

			$('#create-rule input[name="name"]').val(json.name);
			$('#create-rule select[name="gate"]').val(json.logicGate);

			var ruleset = json.ruleset;
			var rulesetLength = ruleset.length;

			nonAssnRuleUnitArray = [];

			for (var j = 0; j < rulesetLength; j++) {

				if (ruleset[j].ruleset != undefined && ruleset[j].ruleset != null) {
					$('#assigned-fields-rule').append('<option value="' + ruleset[j].name + '">' + ruleset[j].name + '</option>');
					$('#nonassigned-fields-rule option[value="' + ruleset[j].name + '"]').remove();
					continue;
				}

				ruleUnit = {};
				ruleUnit.unitName = ruleset[j].ruleName;
				ruleUnit.baseObject = ruleset[j].baseObjectName;
				ruleUnit.condition = ruleset[j].evaluate.type;

				if (ruleUnit.condition === "ENUMERATED") {
					ruleUnit.enumeratedValue = ruleset[j].evaluate.values;
				} else if (ruleUnit.condition === "EXCLUSION_LIST") {
					ruleUnit.exclusionList = ruleset[j].evaluate.values;
				} else if (ruleUnit.condition === "RANGE") {
					if (ruleset[j].evaluate.values.length >= 2) {
						ruleUnit.upperBound = ruleset[j].evaluate.values[1];
						ruleUnit.lowerBound = ruleset[j].evaluate.values[0];
					}
				} else if (ruleUnit.condition === "NOT_IN_RANGE") {
					if (ruleset[j].evaluate.values.length >= 2) {
						ruleUnit.upperBound = ruleset[j].evaluate.values[1];
						ruleUnit.lowerBound = ruleset[j].evaluate.values[0];
					}
				} else if (ruleUnit.condition === "STARTS_WITH") {
					if (ruleset[j].evaluate.values.length >= 1) {
						ruleUnit.startWithPattern = ruleset[j].evaluate.values[0];
					}
				} else if (ruleUnit.condition === "DOESNT_START_WITH") {
					if (ruleset[j].evaluate.values.length >= 1) {
						ruleUnit.notStartWithPattern = ruleset[j].evaluate.values[0];
					}
				} else if (ruleUnit.condition === "ENDS_WITH") {
					if (ruleset[j].evaluate.values.length >= 1) {
						ruleUnit.endWithPattern = ruleset[j].evaluate.values[0];
					}
				} else if (ruleUnit.condition === "DOESNT_END_WITH") {
					if (ruleset[j].evaluate.values.length >= 1) {
						ruleUnit.notEndWithPattern = ruleset[j].evaluate.values[0];
					}
				} else if (ruleUnit.condition === "CONTAINS") {
					if (ruleset[j].evaluate.values.length >= 1) {
						ruleUnit.containWithPattern = ruleset[j].evaluate.values[0];
					}
				} else if (ruleUnit.condition === "DOESNT_CONTAIN") {
					if (ruleset[j].evaluate.values.length >= 1) {
						ruleUnit.notContainWithPattern = ruleset[j].evaluate.values[0];
					}
				} else if (ruleUnit.condition === "MATCHES") {
					if (ruleset[j].evaluate.values.length >= 1) {
						ruleUnit.matchesPattern = ruleset[j].evaluate.values[0];
					}
				} else if (ruleUnit.condition === "NOT_MATCHES") {
					if (ruleset[j].evaluate.values.length >= 1) {
						ruleUnit.notMatchesPattern = ruleset[j].evaluate.values[0];
					}
				} else if (ruleUnit.condition === "EQUALS") {
					if (ruleset[j].evaluate.values.length >= 1) {
						ruleUnit.ruleCondition = ruleset[j].evaluate.values[0];
					}
				} else if (ruleUnit.condition === "NOT_EQUALS") {
					if (ruleset[j].evaluate.values.length >= 1) {
						ruleUnit.ruleCondition = ruleset[j].evaluate.values[0];
					}
				} else if (ruleUnit.condition === "GREATER_THAN") {
					if (ruleset[j].evaluate.values.length >= 1) {
						if (ruleset[j].evaluate.values[0].indexOf('00:00:00') > -1) {
							ruleUnit.conditionValue = 'date';
							var dateStr = ruleset[j].evaluate.values[0];
							dateStr = dateStr.replace(dateStr.split(' ')[4], '(' + dateStr.split(' ')[4] + ')');
							var date = new Date(dateStr);
							ruleUnit.conditionValueDate = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
						} else {
							ruleUnit.conditionValue = 'number';
							ruleUnit.conditionValueNumber = ruleset[j].evaluate.values[0];
						}
					}
				} else if (ruleUnit.condition === "GREATER_THAN_OR_EQUALS") {
					if (ruleset[j].evaluate.values.length >= 1) {
						if (ruleset[j].evaluate.values[0].indexOf('00:00:00') > -1) {
							ruleUnit.conditionValue = 'date';
							var dateStr = ruleset[j].evaluate.values[0];
							dateStr = dateStr.replace(dateStr.split(' ')[4], '(' + dateStr.split(' ')[4] + ')');
							var date = new Date(dateStr);
							ruleUnit.conditionValueDate = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
						} else {
							ruleUnit.conditionValue = 'number';
							ruleUnit.conditionValueNumber = ruleset[j].evaluate.values[0];
						}
					}
				} else if (ruleUnit.condition === "LESSER_THAN") {
					if (ruleset[j].evaluate.values.length >= 1) {
						if (ruleset[j].evaluate.values[0].indexOf('00:00:00') > -1) {
							ruleUnit.conditionValue = 'date';
							var dateStr = ruleset[j].evaluate.values[0];
							dateStr = dateStr.replace(dateStr.split(' ')[4], '(' + dateStr.split(' ')[4] + ')');
							var date = new Date(dateStr);
							ruleUnit.conditionValueDate = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
						} else {
							ruleUnit.conditionValue = 'number';
							ruleUnit.conditionValueNumber = ruleset[j].evaluate.values[0];
						}
					}
				} else if (ruleUnit.condition === "LESSER_THAN_OR_EQUALS") {
					if (ruleset[j].evaluate.values.length >= 1) {
						if (ruleset[j].evaluate.values[0].indexOf('00:00:00') > -1) {
							ruleUnit.conditionValue = 'date';
							var dateStr = ruleset[j].evaluate.values[0];
							dateStr = dateStr.replace(dateStr.split(' ')[4], '(' + dateStr.split(' ')[4] + ')');
							var date = new Date(dateStr);
							ruleUnit.conditionValueDate = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
						} else {
							ruleUnit.conditionValue = 'number';
							ruleUnit.conditionValueNumber = ruleset[j].evaluate.values[0];
						}
					}
				}

				ruleUnit.ruleVariable = ruleset[j].ruleVariable;
				ruleUnit.schemaName = ruleset[j].schemaName;

				nonAssnRuleUnitArray.push(ruleUnit);
				$('#assigned-fields-unit').append('<option value="' + ruleUnit.unitName + '">' + ruleUnit.unitName + '</option>');
			}
		});


		$("#img-right-unit").unbind('click').click(function() {
			if ($('#nonassigned-fields-unit option:selected').val() === undefined) {
				return;
			}
			$('#nonassigned-fields-unit option:selected').each(function(e) {
				$('#assigned-fields-unit').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');

			});
			$('#nonassigned-fields-unit option:selected').remove();
		});

		$("#img-left-unit").unbind('click').click(function() {
			if ($('#assigned-fields-unit option:selected').val() === undefined) {
				return;
			}
			$('#assigned-fields-unit option:selected').each(function(e) {
				$('#nonassigned-fields-unit').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');

			});
			$('#assigned-fields-unit option:selected').remove();
		});


		$("#img-right-rule").unbind('click').click(function() {
			if ($('#nonassigned-fields-rule option:selected').val() === undefined) {
				return;
			}
			$('#nonassigned-fields-rule option:selected').each(function(e) {
				$('#assigned-fields-rule').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');

			});
			$('#nonassigned-fields-rule option:selected').remove();
		});

		$("#img-left-rule").unbind('click').click(function() {
			if ($('#assigned-fields-rule option:selected').val() === undefined) {
				return;
			}
			$('#assigned-fields-rule option:selected').each(function(e) {
				$('#nonassigned-fields-rule').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');

			});
			$('#assigned-fields-rule option:selected').remove();
		});

		$('#save-btn-brm').unbind('click').click(function() {
			if ($('#business-menu').hasClass('active')) {

				if ($('#create-rule select[name="gate"]').val() == 'select') {
					$('#error_message').show();
					$('#myform_succloc').css('color', 'red').text('Please select Logic Gate.');
					return;
				}

				rule = {};

				assnRuleUnitArray = [];

				$('#assigned-fields-unit option').each(function() {
					for (var i = 0; i < nonAssnRuleUnitArray.length; i++) {
						if (nonAssnRuleUnitArray[i].unitName == $(this).val()) {
							assnRuleUnitArray.push(nonAssnRuleUnitArray[i]);
							break;
						}
					}
				});

				rule.name = $('#create-rule input[name="name"]').val();
				rule.gate = $('#create-rule select[name="gate"]').val();
				rule.ruleUnits = assnRuleUnitArray;

				rule.rules = [];

				$('#assigned-fields-rule option').each(function() {
					rule.rules.push($(this).val());
				});

				$.ajax({
					url: '/sef-catalog-ui/createRule',
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(rule),
					success: function(data) {
						if (data.status == 'success') {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'green').text('Rule Created Successfully');
							BusinessHandler.loadBusiness();
						} else {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'red').text(data.message);
						}
					}

				});
			}
		});


		$('#update-btn-brm').unbind('click').click(function() {
			if ($('#business-menu').hasClass('active')) {

				if ($('#create-rule select[name="gate"]').val() == 'select') {
					$('#error_message').show();
					$('#myform_succloc').css('color', 'red').text('Please select Logic Gate.');
					return;
				}

				rule = {};

				assnRuleUnitArray = [];

				$('#assigned-fields-unit option').each(function() {
					for (var i = 0; i < nonAssnRuleUnitArray.length; i++) {
						if (nonAssnRuleUnitArray[i].unitName == $(this).val()) {
							assnRuleUnitArray.push(nonAssnRuleUnitArray[i]);
							break;
						}
					}
				});


				rule.name = $('#create-rule input[name="name"]').val();
				rule.gate = $('#create-rule select[name="gate"]').val();
				rule.ruleUnits = assnRuleUnitArray;

				rule.rules = [];

				$('#assigned-fields-rule option').each(function() {
					rule.rules.push($(this).val());
				});

				$.ajax({
					url: '/sef-catalog-ui/updateRule',
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(rule),
					success: function(data) {
						if (data.status == 'success') {
							BusinessHandler.loadBusiness();
							$('#error_message').show();
							$('#myform_succloc').css('color', 'green').text('Rule updated Successfully');
						} else {
							$('#error_message').show();
							$('#myform_succloc').css('color', 'red').text(data.message);
						}
					}

				});
			}

		});


		$("#rule-btn").unbind('click').click(function() {
			$("#appendenumerated-list").html('');
			$("#appendenumerated").html('');
			$('#create-rule #panel4 .price div:first').hide();
			nonAssnRuleUnitArray = [];
			$('#create-rule').find('input:text, input[type="date"], input:password, input:file, select, textarea').val('');
			$('#create-rule').find('input:radio, input:checkbox')
				.removeAttr('checked').removeAttr('selected');

			$('#nonassigned-fields-unit').html('');
			$('#nonassigned-fields-rule').html('');
			$('#assigned-fields-unit').html('');

			$('#assigned-fields-rule').html('');

			for (var key in allRules) {
				$('#nonassigned-fields-rule').append('<option value="' + key + '">' + key + '</option>');
			}
			$('#rule-btn').hide();
			$("#content-rule").show();
			$("#save-btn-brm").show();
			$("#cancel-btn-brm").show();
			$("#update-btn-brm").hide();

		});

		$('#cancel-btn-brm').unbind('click').click(function() {
			if ($('#business-menu').hasClass('active')) {
				$("#content-rule").hide();
				$('#save-btn-brm').hide();
				$('#update-btn-brm').hide();
				$('#cancel-btn-brm').hide();
				$('#rule-btn').show();

			}
		});
	}
};
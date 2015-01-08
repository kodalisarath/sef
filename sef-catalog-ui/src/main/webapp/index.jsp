<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<%@ page pageEncoding="UTF-8" %>
		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
			<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

				<html xmlns="http://www.w3.org/1999/xhtml">

				<head>
					<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
					<title>Product Catalog</title>
					<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
					<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.9.2.css" />
					<script src="js/menu_jquery.js"></script>
					<link rel="stylesheet" type="text/css" href="css/menu.css" />


					<link rel="stylesheet" type="text/css" href="css/usermgnt.css" />

					<link rel="stylesheet" href="css/jqtree.css" />

					<link href="kindo/css/examples-offline.css" rel="stylesheet" />
					<link href="kindo/css/kendo.common.min.css" rel="stylesheet" />
					<link href="kindo/css/kendo.rtl.min.css" rel="stylesheet" />
					<link href="kindo/css/kendo.default.min.css" rel="stylesheet" />
					<link href="kindo/css/kendo.silver.min.css" rel="stylesheet" />

					<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
					<script src="js/index.js"></script>
					<script type="text/javascript" src="js/tree.jquery.js"></script>

					<script src="kindo/js/jquery.min.js"></script>
					<script type="text/javascript" src="js/jquery-ui-1.9.2.js"></script>
					<script src="kindo/js/kendo.web.min.js"></script>
					<script src="kindo/js/console.js"></script>


					<link href='css/overlaypopup.css' rel='stylesheet' type='text/css' />

					<link rel="stylesheet" type="text/css" href="css/component.css" />
					<script src="js/modernizr.custom.js"></script> <!--  Owner group manager plugin -->

					<script>
						$(document).ready(function() {
							$("#content-middle .search").keyup(function() {
								if ($('#business-menu').hasClass('active')) {
									var value = this.value;
									$("#middle_rule").find('.rule-upper').each(function(index) {
										if (index === -1) return;
										var id = $(this).find("h4").text();
										$(this).toggle(id.indexOf(value) !== -1);
									});
								} else {
									var value = this.value;
									$("#middle ul.k-treeview-lines").children().each(function(index) {
										if (index === -1) return;
										var id = $(this).find(".k-in:first").text();
										$(this).toggle(id.indexOf(value) !== -1);
									});
								}
							});

							$(document).ajaxStart(function() {
								$("#overlay1").show();
							});

							$(document).ajaxStop(function() {
								$("#overlay1").hide();
							});

							$(".history").click(function() {
								$("#panel").slideToggle("slow");
								$("#panel1").slideUp("slow");
								$("#addpolicy").slideUp("slow");
								$("#taxtype").slideUp("slow");
								$("#panel6").slideUp("slow");
								$("#select-owner-group").hide();
								$("#select-offer-group").hide();
								$("#select-product-owner").hide();
								$('#select-product-resource').hide();
								$('#ws-search').hide();
							});

							$(".history1").click(function() {
								$("#panel1").slideToggle("slow");
								$("#panel").slideUp("slow");
								$("#addpolicy").slideUp("slow");
								$("#taxtype").slideUp("slow");
								$("#panel6").slideUp("slow");

								$("#select-policy-rule").hide();
								$("#select-owner-group").show();
								$("#select-offer-group").hide();
								$("#select-product-owner").hide();
								$('#select-product-resource').hide();
								$('#ws-search').hide();
							});
							$(".history6").click(function() {
								$("#panel6").slideToggle("slow");
								$("#panel1").slideUp("slow");
								$("#panel").slideUp("slow");
								$("#addpolicy").slideUp("slow");

								$("#taxtype").slideUp("slow");
								$("#select-policy-rule").hide();
								$("#select-owner-group").hide();
								$("#select-offer-group").hide();
								$("#select-product-owner").hide();
								$('#select-product-resource').hide();
								$('#ws-search').hide();
							});
							$(".history2").click(function() {
								$("#panel2").slideToggle("slow");
							});
							$(".history3").click(function() {
								if (!$('#panel3').is(':hidden')) {
									$('#select-product-owner').hide();
									$('#select-product-resource').hide();
									$('#ws-search').hide();
								}
								$("#panel3").slideToggle("slow");
							});
							$(".history4").click(function() {
								$("#panel4").slideToggle("slow");
							});
							$(".history-whitelist").click(function() {
								$("#panel-whitelist").slideToggle("slow");
							});
							$("#add1").click(function() {
								$("#addpolicy").slideToggle("slow");
							});

							$("#add2").click(function() {
								$("#taxtype").slideToggle("slow");
								$("#taxtype input[name='taxCurrency']").val($('#create-offer [name="currency"]').val());
							});
							
							addDatepicker();
						});

						$(document).on("cut copy paste", "input[onkeypress*='return isN']",function(e) {
					          e.preventDefault();
					     });
						
						function addDatepicker() {
						    $("input[attr-date='datepicker']").each(function () {
						    	$(this).datepicker({
						    		dateFormat: 'yy-mm-dd',
						    		minDate: 0,
						      changeMonth: true,//this option for allowing user to select month
						      changeYear: true //this option for allowing user to select from year range
						    });
						    $(this).unbind('keydown').keydown(function(e){
						    	e.preventDefault();
						    });
						    	$.extend($.datepicker,{_checkOffset:function(inst,offset,isFixed){return offset}});
						    });
						  }
						
						function isNumberKey(evt) {
							var charCode = (evt.which) ? evt.which : event.keyCode
							if (charCode > 31 && (charCode < 48 || charCode > 57))
								return false;
							return true;
						}
						
						function isNegativeNumberKey(evt) {
							var charCode = (evt.which) ? evt.which : event.keyCode;
							if(evt.target.value.length < 1){
								if ((charCode > 47 && charCode < 58) || charCode == 45)
									return true;
							}else{
								if ((charCode > 47 && charCode < 58))
									return true;
							}
							
							return false;
						}
					</script>
					<script id="middle-template-new" type="text/kendo-ui-template">
						#: item.text #
						<a owner="#: item.name #" class="delete-link" text="#: item.text #"></a>
						<a owner="#: item.name #" class="edit-link" text="#: item.text #"></a>
					</script>

					<script id="middle-template" type="text/kendo-ui-template">
						#: item.text #
						<a class="delete-link" text="#: item.text #"></a>
						<a class="edit-link" text="#: item.text #"></a>
					</script>
					<script id="treeview-checkbox-template" type="text/kendo-ui-template">
						<input type="checkbox" name="checkedFiles[#= item.text #]" value="true" />
					</script>

					<script type="text/javascript">
						$(document).ready(function() {
							
							MenuHandler.menuEvents();
							
							//
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
		
		if(${sessionContains == null}){
			window.location.href = 'login.jsp'; 
		}
		
	    <c:if test="${offerReadOnlyContains}">
		   $('#offers-menu').addClass('active');
		   OfferHandler.loadOffers();
		</c:if> 
		<c:set var="username" scope="session" value="${username}">
		</c:set>
							$('#content-profiles').hide();
							<c:if test="${username != 'admin'}">
							
							<c:if test="${offerContains != true}">
						       <c:if test="${offerWriteContains  != true}">
						           $('#ui-btn').remove();
						           $('.edit-link').remove();
					          </c:if>
					          <c:if test="${offerUpdateContains  != true}">
					               $('#update-btn').remove();
					          </c:if>
					          
						</c:if>
						
					     <c:if test="${resourceContains != true}">
					      <c:if test="${resourceWriteContains  != true}">
					        $('#ui-btn1').remove();
					        $('.edit-link').remove();
					      </c:if>
					      <c:if test="${resourceUpdateContains  != true}">
					           $('#update-btn-res').remove();
					      </c:if>
					</c:if>

					<c:if test="${ruleConatains != true}">
					     <c:if test="${ruleWriteContains  != true}">
					        $('#rule-btn').remove();
					        $('.edit-link').remove();
					    </c:if>
					    <c:if test="${ruleUpdateContains  != true}">
					        $('#update-btn-brm').remove();
					    </c:if>
					</c:if>

					<c:if test="${resourceGroupConatains != true}">
					    <c:if test="${resourceGroupWriteContains  != true}">
					       $('#ui-btn2').remove();
					       $('.edit-link').remove();
					    </c:if>
					    
					    <c:if test="${resourceGroupUpdateContains  != true}">
					       $('.resource_edit').remove();
					    </c:if>
					</c:if>

					<c:if test="${offerGroupConatains != true}">
					   <c:if test="${offerGroupWriteContains  != true}">
					      $('#ui-btn-ocg').remove();
					      $('.edit-link').remove();
					  </c:if>
					  <c:if test="${offerGroupUpdateContains  != true}">
					      $('.offer_group_edit').remove();
					  </c:if>
					</c:if>


					<c:if test="${ownerGroupConatains != true}">
					    <c:if test="${ownerGroupWriteContains  != true}">
					      $('#ui-btn-og').remove();
					      $('.edit-link').remove();
					   </c:if>
					   <c:if test="${ownerGroupUpdateContains  != true}">
					     $('#update-btn-owner').remove();
					   </c:if>
					</c:if>

					<c:if test="${userConatains != true}">
					   <c:if test="${userWriteContains  != true}">
					    $('#ui-btn123').remove();
					    $('.edit-link').remove();
					   </c:if>
					   <c:if test="${userUpdateContains  != true}">
					    $('#update-btn-user').remove();
					   </c:if>
					</c:if>

					<c:if test="${groupConatains != true}">
					    <c:if test="${groupWriteContains  != true}">
					     $('#ui-btn12').remove();
					     $('.edit-link').remove();
					    </c:if>
					   <c:if test="${groupUpdateContains  != true}">
					     $('#update-btn-group').remove();
					   </c:if>
					</c:if>


					<c:if test="${notificationConatains != true}">
					   <c:if test="${notificationWriteContains  != true}">
					     $('#addn-btn').remove();
					     $('.edit-link').remove();
					   </c:if>
					   <c:if test="${notificationUpdateContains  != true}">
					     $('#update-btn-notif').remove();
					   </c:if>
					</c:if>
						</c:if>	
	
	
							/* IsRecurrent */
							jQuery('input[name="recurrent"]').change(function() {
								if (jQuery(this).val() === "true") {
									jQuery('#rp-infinite').hide();
									jQuery('input[name="renewalPeriod"][value="hours"]').prop('checked', true);
									jQuery('#set_hours').show().val('');
									jQuery('#set_days').hide();
									jQuery('#set_date').hide();
									jQuery('#setreneval').show();
									jQuery('#validity').hide();
								} else {
									jQuery('#rp-infinite').show();
									jQuery('input[name="renewalPeriod"][value="infinite"]').prop('checked', true);
									jQuery('#set_hours').hide();
									jQuery('#set_days').hide();
									jQuery('#set_date').hide();
									jQuery('#setreneval').hide();
									jQuery('#validity').show();
								}

							});
							/* IsRecurrent */


							/* is commercial*/

							jQuery('input[name="commercial"]').change(function() {
								if (jQuery(this).val() === "true") {
									jQuery('.blue').show();
								} else {
									jQuery('.blue').hide();
								}

							});

							$("#taxtype select[name='tax']").change(function() {
								if ($(this).val() === "Absolute" || $(this).val() === "Percentage") {
									if ($(this).val() === "Absolute") {
										$("#taxtype input[name='taxCurrency']").show();
										$("#taxtype input[name='taxCurrency']").val($('#create-offer [name="currency"]').val());
									} else {
										$("#taxtype input[name='taxCurrency']").hide();
									}
									$("#taxtype input[name='taxValue']").show();
								} else {
									$("#taxtype input[name='taxValue']").hide();
									$("#taxtype input[name='taxCurrency']").hide();
								}
							});

							$("#panel6 input[name='switchPolicy']").click(function() {
								if ($(this).val() === "NO_LIMIT") {
									$("#select-offer-group").show();
									$("#offer-group-hs").show();
								} else {
									$("#select-offer-group").hide();
									$("#offer-group-hs").hide();
								}
							});

							//autoTermination
							jQuery('input[name="autoTermination"]').change(function() {
								if (jQuery(this).val() === "HARD_STOP") {
									jQuery('#hs').show();
								} else {
									jQuery('#hs').hide();
								}
							});

							// minimumCommitment
							jQuery('input[name="minimumCommitment"]').change(function() {
								if (jQuery(this).val() === "HARD_LIMIT") {
									jQuery('#hl').show();
								} else {
									jQuery('#hl').hide();
								}
							});

							// trai period date
							jQuery('input[name="trialPeriod"]').change(function() {
								if (jQuery(this).val() === "date") {
									jQuery('#trial_date').show();
								} else {
									jQuery('#trial_date').hide();
								}
							});

							// set renewal period
							jQuery('input[name="renewalPeriod"]').change(function() {
								if (jQuery(this).val() === "hours") {
									jQuery('#set_hours').show().val('');
								} else {
									jQuery('#set_hours').hide();
								}
								if (jQuery(this).val() === "days") {
									jQuery('#set_days').show().val('');
								} else {
									jQuery('#set_days').hide();
								}
								if (jQuery(this).val() === "date") {
									jQuery('#set_date').show().val('');
								} else {
									jQuery('#set_date').hide();
								}
							});
							
							jQuery('input[name="productValidity"]').change(function() {
								if (jQuery(this).val() === "hours") {
									jQuery('#val_hours').show();
								} else {
									jQuery('#val_hours').hide();
								}
								if (jQuery(this).val() === "days") {
									jQuery('#val_days').show();
								} else {
									jQuery('#val_days').hide();
								}
								if (jQuery(this).val() === "date") {
									jQuery('#val_date').show();
								} else {
									jQuery('#val_date').hide();
								}
							});

							//time period

							jQuery('input[name="trialPeriod"]').change(function() {
								if (jQuery(this).val() === "hours") {
									jQuery('#h').show();
								} else {
									jQuery('#h').hide();
								}
							});

							jQuery('input[name="trialPeriod"]').change(function() {
								if (jQuery(this).val() === "days") {
									jQuery('#hd').show();
								} else {
									jQuery('#hd').hide();
								}
							});

							// set renewal period
							jQuery('input[name="charge"]').change(function() {
								if (jQuery(this).val() === "enablecharge") {
									jQuery('#charge_amount').toggle();
								}
							});



							jQuery('input[name="autoTermination"]').change(function() {
								if (jQuery(this).val() === "AFTER_X_DAYS") {
									jQuery('#day_term').show();
								} else {
									jQuery('#day_term').hide();
								}
							});

							jQuery('input[name="minimumCommitment"]').change(function() {
								if (jQuery(this).val() === "UNTIL_X_DAYS") {
									jQuery('#day_comm').show();
								} else {
									jQuery('#day_comm').hide();
								}
							});


							jQuery('input[name="popup"]').change(function() {
								if (jQuery(this).val() === "promote") {
									jQuery('.promote-pop').show();
								} else
								if (jQuery(this).val() === "edit") {
									jQuery('.promote-pop').hide();
								}
							});

							jQuery('input[name="autoTermination"]').change(function() {
								if (jQuery(this).val() === "AFTER_X_RENEWALS") {
									jQuery('#day_after_term').show();
								} else {
									jQuery('#day_after_term').hide();
								}
							});

							jQuery('input[name="minimumCommitment"]').change(function() {
								if (jQuery(this).val() === "UNTIL_X_RENEWALS") {
									jQuery('#day_after_comm').show();
								} else {
									jQuery('#day_after_comm').hide();
								}
							});


							jQuery('select[name="notifcation-action"]').change(function() {

								if (jQuery(this).val() === "WORKFLOW") {
									jQuery('#client').slideDown('slow');
								} else {
									jQuery('#client').slideUp('fast');
								}
							});


							//isabstract, iasconsumable radio buttons in SR Resources
							$(document).ready(function() {
								$('#id_radio1').click(function() {
									$('.child').show();
									$('.cost').hide();
								});
								$('#id_radio2').click(function() {
									$('.child').hide();
									$('.cost').show();
								});
								$('#id_radio3').click(function() {
									$('.unit').show();

								});
								$('#id_radio4').click(function() {
									$('.unit').hide();

								});

							});


							// error msg hide
							$(document).ready(function() {
								$('#error').click(function() {
									$('#error_message').hide();
								});
							});

							//Reset all radio and dropdowns
							$("#dd").trigger('reset');
							$("#dd1").trigger('reset');
							$("#dd2").trigger('reset');
							$("#dd3").trigger('reset');
							$("#dd4").trigger('reset');
							$("#dd5").trigger('reset');
							$("#Unit").trigger('reset');
							$("#panel").trigger('reset');
							$("#hl").trigger('reset');
							$("#hs").trigger('reset');
							$("#h").trigger('reset');
							$("#hd").trigger('reset');
							$("#charge_amount_currency").trigger('reset');
							$("#charge_amount").trigger('reset');

						});
					</script>

					<style>
						select {
							width: 160px;
							padding: 2px;
							border-radius: 10px;
							border: 1px solid #808080;
							background: #fff;
						}
						select#daydropdown {
							width: 50px;
						}
						select#monthdropdown {
							width: 50px;
						}
						select#yeardropdown {
							width: 50px;
						}
						#dd {
							width: 162px;
							float: right;
						}
						#dd input {
							width: auto;
						}
						#dd1 {
							width: 162px;
							float: right;
						}
						#dd1 input {
							width: auto;
						}
						#dd2 {
							width: 162px;
							float: right;
						}
						#dd2 input {
							width: auto;
						}
						#dd3 {
							width: 162px;
							float: right;
							margin-top: 10px;
						}
						#dd3 input {
							width: auto;
						}
						#dd4 {
							width: 162px;
							float: right;
						}
						#dd4 input {
							width: auto;
						}
						#dd5 {
							width: 162px;
							float: right;
						}
						#dd5 input {
							width: auto;
						}
						#dd6 {
							width: 162px;
							float: right;
						}
						#dd6 input {
							width: auto;
						}
						.red {
							margin-top: 10px;
							border-radius: 10px;
							width: 450px;
							min-height: auto;
							height: auto;
							float: left;
						}
						.data {
							display: none;
							margin-top: 30px;
							border-radius: 10px;
							width: 460px;
							min-height: 30px;
							height: auto;
						}
						.msg {
							display: none;
							margin-top: 30px;
							border-radius: 10px;
							width: 460px;
							min-height: 30px;
							height: auto;
						}
						.voice {
							display: none;
							margin-top: 30px;
							border-radius: 10px;
							width: 460px;
							min-height: 30px;
							height: auto;
						}
						.app {
							display: none;
							margin-top: 30px;
							border-radius: 10px;
							width: 460px;
							min-height: 30px;
							height: auto;
						}
						.child {
							display: none;
							margin-top: 30px;
							border-radius: 10px;
							width: 450px;
							min-height: 50px;
							height: auto;
						}
						.cost {
							margin-top: 30px;
							border-radius: 10px;
							width: 450px;
							/*min-height:100px;*/
							height: auto;
						}
						.unit {
							display: none;
							margin-top: 30px;
							border-radius: 10px;
							width: 450px;
							min-height: 110px;
							height: auto;
						}
						.switch {
							display: none;
							margin-top: 30px;
							border-radius: 10px;
							width: 450px;
							min-height: 110px;
							height: auto;
						}
						#client {
							display: none;
							margin-top: 5px;
						}
						#middle .k-sprite {
							background-image: url(images/coloricons-sprite.png);
						}
						#middle2 .k-sprite {
							background-image: url(images/coloricons-sprite.png);
						}
						.rootfolder {
							background-position: 0 0;
						}
						.folder {
							background-position: 0 -16px;
						}
						.pdf {
							background-position: 0 -32px;
						}
						.html {
							background-position: 0 -48px;
						}
						.image {
							background-position: 0 -64px;
						}
						.policy {
							background-position: 0 -80px;
						}
						.rule {
							background-position: 0 -97px;
						}
						.resourcegroup {
							background-position: 0 -114px;
						}
						.rgchild {
							background-position: 0 -130px;
						}
						.opcogroup {
							background-position: 0 -146px;
						}
						.opco {
							background-position: 0 -146px;
						}
						.market {
							background-position: 0 -216px;
						}
						.tenant {
							background-position: 0 -199px;
						}
						.partner {
							background-position: 0 -163px;
						}
						.enduser {
							background-position: 0 -182px;
						}
						.k-window,
						.k-state-selected,
						.k-state-focused {
							background: #4f81be !important;
							color: #fff !important;
							width: 96%;
							margin-bottom: 1px !important;
						}
						.delete-link {
							width: 12px;
							height: 12px;
							overflow: hidden;
							display: inline-block;
							font-size: 0;
							line-height: 0;
							background: transparent url(images/delete1.gif) no-repeat 50% 50%;
							vertical-align: top;
							margin: 2px 0 0 46px;
							-webkit-border-radius: 5px;
							-mox-border-radius: 5px;
							border-radius: 5px;
							cursor: pointer;
						}
						.edit-link {
							width: 12px;
							height: 12px;
							overflow: hidden;
							display: inline-block;
							background: transparent url(images/edit.png) no-repeat 50% 50%;
							font-size: 0;
							line-height: 0;
							vertical-align: top;
							margin: 2px 0 0 3px;
							-webkit-border-radius: 5px;
							-mox-border-radius: 5px;
							border-radius: 5px;
							cursor: pointer;
						}
						#dailog-radio,
						.promote-pop {
							font-family: Tahoma;
							font-size: 12px;
						}
						div.k-window {
							box-shadow: 2px 2px 1px #bb0cff6;
							padding: 5px;
							padding-top: 34px !important;
							-webkit-border-radius: 10px;
							-moz-border-radius: 10px;
							border-radius: 10px;
						}
						.k-window-titlebar {
							width: 96.2%;
						}
						#dailog-area {
							padding: 15px;
							border: 1px solid #eaeaea;
							background: #eaeaea;
							display: none;
						}
						#dailog-radio {
							padding: 15px;
						}
						#dailog-radio input {
							vertical-align: middle;
							color: #366aa6;
						}
						.promote-pop input {
							vertical-align: middle;
							color: #023d85;
						}
					</style>


					
					<script id="record-jsp" type="text/x-kendo-template">
						<div id="dailog-radio">
							<input type="radio" name="type" value="promote" />Promote Offer &nbsp;&nbsp;&nbsp;
							<input type="radio" name="type" value="edit" />Edit Offer
						</div>
						<div class="promote-pop" style="display:none; margin-left:15px;">
							<input type="radio" name="promote-to" value="Testing" />Testing
							<br>
							<input type="radio" name="promote-to" value="Publish" />Publish
							<br>
							<input type="radio" name="promote-to" value="Disabled" />Disabled
							<br>
							<input type="radio" name="promote-to" value="Retired" />Retired
							<br>
						</div>
						<div class="promote-pop1" style="display:none; margin-left:15px;">
							<button value="Promote" class="ui-btn-pop">Promote</button>
						</div>
					</script>

				</head>

				<body>
				
			
					<fmt:setBundle basename="myapp" var="lang" />
					<fmt:setLocale value="en" />

					<%@ include file="pages/header.jsp" %>

						<%@ include file="pages/menu.jsp" %>

							<div class="user-content-right">
								<div id="dailog-area"></div>
								<div id="content-middle">
									<div id="middle3" style="display:none;">
										<div>
											<h4 style="float:left; text-weight:normal !important;">Search your User: &nbsp;</h4>
											<input id="content-actor-search" type="text" />
											<button value="Add Offer" id="ui-btn123">Create User</button>
										</div>
									</div>

									<div id="middle5" style="display:none;">
										<div>
											<h4 style="float:left; text-weight:normal !important;">Search your Group: &nbsp;</h4>
											<input id="content-group-search" type="text" />
											<button value="Add Group" id="ui-btn12">Create Group</button>
										</div>
									</div>
									<div id="privileges-search">
										<div>
											<h4 style="float:left; text-weight:normal !important;">Search your Privilege: &nbsp;</h4>
											<input class="search" type="text" />
										</div>
									</div>
									<div id="middle2">
										<div>
											<h4 style="float:left; text-weight:normal !important;">Search your Owner: &nbsp;</h4>
											<input class="search" type="text" />
										</div>
									</div>
									<c:if test="${offerReadOnlyContains}">
                                 	<div id="offer-search">
										<div>
											<h4 style="float:left; text-weight:normal !important;">Search your Offer: &nbsp;</h4>
											<input class="search" type="text" />
										</div>
									</div>
                                   </c:if>

									<div id="resource-search">
										<div>
											<h4 style="float:left; text-weight:normal !important;">Search your Resource: &nbsp;</h4>
											<input class="search" type="text" />
										</div>
									</div>
									<div id="resourcegroup-search">
										<div>
											<h4 style="float:left; text-weight:normal !important;">Search your Resource Group: &nbsp;</h4>
											<input class="search" type="text" />
										</div>
									</div>
									<div id="offergroup-search">
										<div>
											<h4 style="float:left; text-weight:normal !important;">Search your Offer Group: &nbsp;</h4>
											<input class="search" type="text" />
										</div>
									</div>
									<div id="middle">
										<div id="groups-tree"></div>
									</div>

									<div id="notification_search">
										<h4 style="float:left; text-weight:normal !important;">External Notification</h4>
										<button value="Add Notification" id="addn-btn">Add Notification</button>
									</div>
									<div id="middle7">
										<%@ include file="pages/notification/notification.jsp" %>
									</div>
									<div id="rule_search">
										<h4 style="float:left; text-weight:normal !important;">Search your Rule: &nbsp;</h4>
										<input class="search" type="text" />

									</div>
									<div id="middle_rule">
										<div class="rule-upper" style="margin-bottom:10px;display:none;">
											<h4>Rule1</h4>
											<div style="float:right; margin-right:25px;">
											 <c:if test="${ruleDeleteContains}">
											 <img class="delete-rule" src="images/delete1.gif" width="14" />
											 </c:if>
												<c:if test="${ruleUpdateContains}">
												<img class="edit-rule" src="images/edit.png" width="14" />
												</c:if>
											</div>
											<div style="margin-top:5px;">
												<img src="images/filter.png" style="vertical-align:middle; float:left; margin-right:5px;" />
												<span class="rule-gate" style="color:blue;text-decoration:underline; margin-right:3px;">All</span>of
											</div>
											<div class="ruleset" style="border-left:2px dotted #0096ff;height:auto; margin-left:15px; margin-top:10px;">
												<div class="rule-main">
													<img src="images/filter.png" style="vertical-align:bottom;margin-right:5px;float:left; margin-left:5px;" />
													<div>RuleUnit1.1 &nbsp;&nbsp;|&nbsp;&nbsp; Base Object1.1&nbsp;&nbsp;|&nbsp;&nbsp;Condition1.1&nbsp;&nbsp;|&nbsp;&nbsp;Rule Variable1.1</div>
												</div>
											</div>
										</div>
									</div>
									
								</div>
								
								<!-- These are the DOM values populates as to use it in the javascript files. -->
                   <div>
								<input id="hasOfferDelete" type="hidden" value="${offerDeleteContains}"/>
								<input id="hasResourceDelete" type="hidden" value="${resourceDeleteContains}"/>
								<input id="hasUserDelete" type="hidden" value="${userDeleteContains}"/>
								<input id="hasGroupDelete" type="hidden" value="${groupDeleteContains}"/>
								<input id="hasNotificationDelete" type="hidden" value="${notificationDeleteContains}"/>
								<input id="hasOfferGroupDelete" type="hidden" value="${offerGroupDeleteContains}"/>
								<input id="hasOwnerGroupDelete" type="hidden" value="${ownerGroupDeleteContains}"/>
								<input id="hasResourceGroupDelete" type="hidden" value="${resourceGroupDeleteContains}"/>
								<input id="hasRuleDelete" type="hidden" value="${ruleDeleteContains}"/>
								
								<input id="hasOfferUpdate" type="hidden" value="${offerUpdateContains}"/>
								<input id="hasResourceGroupUpdate" type="hidden" value="${resourceGroupUpdateContains}"/>
								<input id="hasOfferGroupUpdate" type="hidden" value="${offerGroupUpdateContains}"/>
								</div>
								<!-- END -->
								<div id="right" class="content">
								
                                   <c:if test="${offerReadOnlyContains}">
                                   <%@ include file="pages/offer.jsp" %>
                                   </c:if>
									

											<%@ include file="pages/sr-resource.jsp" %>

												<%@ include file="pages/sr-profiles.jsp" %>

													<%@ include file="pages/policy.jsp" %>

														<%@ include file="pages/brm.jsp" %>

															<%@ include file="pages/rg-manager.jsp" %>

																<%@ include file="pages/oc-group.jsp" %>

																	<%@ include file="pages/owner-group.jsp" %>

																		<%@ include file="pages/usermgt/group.jsp" %>

																			<%@ include file="pages/usermgt/user.jsp" %>

																				<%@ include file="pages/usermgt/privileges.jsp" %>

																					<%@ include file="pages/notification/notification-add.jsp" %>

																						<%@ include file="pages/notification/notification-edit.jsp" %>

																							<%@ include file="pages/notification/notification-details.jsp" %>



																								<!-- <div  style="float:right;">
	        		<button value="Add Offer" id="save-btn">Save</button>
	        		<button value="Add Offer" id="update-btn">Update</button>
	        		<button value="Add Offer" id="cancel-btn">Cancel</button>
	       		</div> -->

								</div>
								<div id="overlay1">
									<img src="images/loading.gif" class="loading_circle" alt="loading" />
								</div>
								
								<div id="message-box" class="overlay-bg-delete">
									<div class="overlay-content-delete">
									
										<div id="dailog-message-div"><span style="float:left;">Are you sure want to delete?</span>
										</div>
										<button class="ok-btn">Ok</button>
										<button class="close-btn">Cancel</button>
									</div>
								</div>
								
								<div id="message-box-restriction" class="overlay-bg-restriction">
									<div class="overlay-content-delete-restriction">
									
										<div id="dailog-message-div-restriction"><span style="float:left;">You do not have privilege to delete</span>
										</div>
										<button class="ok-restriction">Ok</button>
										
									</div>
								</div>
								

								<div id="right1" class="content">
									<div id="error_message" style="display:none;">
										<img id="error" src="images/close.png" style="float:right;right:0px;position:absolute; " />
										<div id='myform_succloc' style="color:#368606;float:left; width:95%;padding:10px;margin-bottom:10px; border:1px solid #c5c5c5;background-color:#fff; border-radius:10px;">
											ffgdfgdfgdf
										</div>
									</div>
									<div id="ws-search" style="display:none;">
										<h4 style="float:left; text-weight:normal !important;">Search your Resource: &nbsp;</h4>
										<input class="search" type="text" />
									</div>
									<div id='select-offer-group' style="float:left; width:100%; display:none;">sddfgdsgds</div>
									<div id='select-policy-rule' style="float:left; width:100%; display:none;">sddfgdsgds</div>
									<div id='select-owner-group' style="float:left; width:100%; display:none;">sddfgdsgds</div>
									<div id='select-product-owner' style="float:left; width:100%; display:none;">sddfgdsgds</div>
									<div id='select-product-resource' style="float:left; width:100%; display:none;">sddfgdsgds</div>
								</div>

							</div>

							<!-- custom scrollbar stylesheet -->
							<link rel="stylesheet" href="css/jquery.mCustomScrollbar.css">

							<!-- custom scrollbar plugin -->
							<script src="js/jquery.mCustomScrollbar.concat.min.js"></script>

							<script>
								(function($) {
									$(window).load(function() {

										/* all available option parameters with their default values */
										$(".content").mCustomScrollbar({
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

									});
								})(jQuery);
							</script>
							<script src="js/jquery.cbpNTAccordion.min.js"></script>
							<script>
								$(function() {
									/*
			- how to call the plugin:
			$( selector ).cbpNTAccordion( [options] );
			- destroy:
			$( selector ).cbpNTAccordion( 'destroy' );
			*/

									$('#cbp-ntaccordion').cbpNTAccordion();

								});
							</script>
				</body>

				</html>
var UserHandler = {

    loadUsers: function () {
    	$("#content-actor-search").val('');
        $('#middle_rule').hide();
        $('#ws-search').hide();
        $("#resourcegroup-search").hide();	
		$("#offergroup-search").hide();		
		$("#offer-search").hide();
		$("#resource-search").hide();
		$("#privileges-search").hide();
		$("#rule_search").hide();
        $("#select-policy-rule").hide();
        $("#select-owner-group").hide();
        $("#select-offer-group").hide();
        $("#select-product-owner").hide();
        $('#select-product-resource').hide();
        $('#notification-details').hide();
        $('#notification_create_content').hide();
        $('#middle').height('515');
        $('#middle8').hide();
        $('#middle7').hide();
        $('#middle5').hide();
        $('#middle').show();
        $('#middle2').hide();
        $('#middle3').show();
        $('#ui-btn-og').hide();
        $('#ui-btn').hide();
        $('#ui-btn12').hide();
        $('#ui-btn1').hide();
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
        $('.ui-btn-user').hide();
        $('#content-group-mgt').hide();
        $('#content-resource-group').hide();
        $('#add-resource-group').hide();
        $('#offer-catalog-group-service').hide();
        $('#add-offer-catalog-group').hide();
        $('#ui-btn123').show();
        $('#notification-add').hide();
        $('#notification-edit').hide();
        $('#editn-btn').hide();
        $('#addn-btn').hide();
        $('#rule-btn').hide();        
        $('#content-user-mgt').hide();
        $('#ui-btn124').hide();
        $('#privileges-content').hide();
        $('#notification_search').hide();


        var page = 1;
        var actorData = [];

        $('#middle').unbind('scroll').scroll(function () {
            if ($('#middle').scrollTop() == $('#middle .k-treeview-lines').height() - $('#middle').height()) {
                $.ajax({
                    url: '/sef-catalog-ui/getAllActors?searchQuery=' + document.getElementById('content-actor-search').value + '&pageNumber=' + page + '&pageSize=30',
                    type: "GET",
                    async: false,
                    contentType: 'application/json',
                    success: function (data) {
                        json = JSON.parse(data.responseString);
                        page = page + 1;

                        var myStringArray = json.actors;
                        var arrayLength = myStringArray.length;

                        if (arrayLength == 0) {
                            return;
                        }

                        for (var i = 0; i < arrayLength; i++) {

                            var actor = {};
                            actor.text = myStringArray[i].name;
                            actor.expanded = false;
                            actor.items = [];
                            actor.spriteCssClass = "pdf";

                            var memberships = myStringArray[i].memberships;
                            if (memberships != null && memberships != undefined) {

                                for (key in memberships) {
                                    var membership = {};
                                    membership.text = memberships[key].name;
                                    membership.expanded = false;
                                    membership.spriteCssClass = "html";
                                    actor.items.push(membership);
                                }
                            }

                            actorData.push(actor);
                        }


                        if ($('#middle')
                            .hasClass('k-treeview')) {
                            treeview = $("#middle").data("kendoTreeView");

                            treeview.setDataSource(new kendo.data.HierarchicalDataSource({
                                data: actorData,
                            }));


                        } else {
                            treeview = $("#middle").kendoTreeView({
                                //template: "#= item.text # (#=  #)",
                                template: kendo.template($("#middle-template").html()),
                                dataSource: actorData,
                                //select: onSelect,
                            }).data("kendoTreeView");
                        }
                        if($('#hasUserDelete').val() == 'false'){
                			$('.delete-link').hide();
                		}
                        $("#middle div span.k-checkbox").hide();
                        
                        bindEvents();
                    }

                });
            }
        });
       

        $('#content-actor-search').unbind('keyup').keyup(function (e) {
            page = 1;
            $.ajax({
                url: '/sef-catalog-ui/getAllActors?searchQuery=' + document.getElementById('content-actor-search').value + '&pageNumber=' + page + '&pageSize=30',
                type: "GET",
                async: false,
                contentType: 'application/json',
                success: function (data) {
                    json = JSON.parse(data.responseString);
                    actorData = [];

                    page = page + 1;

                    var myStringArray = json.actors;
                    var arrayLength = myStringArray.length;
                    for (var i = 0; i < arrayLength; i++) {

                        var actor = {};
                        actor.text = myStringArray[i].name;
                        actor.expanded = false;
                        actor.items = [];
                        actor.spriteCssClass = "pdf";

                        var memberships = myStringArray[i].memberships;
                        if (memberships != null && memberships != undefined) {

                            for (key in memberships) {
                                var membership = {};
                                membership.text = memberships[key].name;
                                membership.expanded = false;
                                membership.spriteCssClass = "html";
                                actor.items.push(membership);
                            }
                        }
                        actorData.push(actor);
                    }


                    if ($('#middle')
                        .hasClass('k-treeview')) {
                        treeview = $("#middle").data("kendoTreeView");

                        treeview.setDataSource(new kendo.data.HierarchicalDataSource({
                            data: actorData,
                        }));


                    } else {
                        treeview = $("#middle").kendoTreeView({
                            //template: "#= item.text # (#=  #)",
                            template: kendo.template($("#middle-template").html()),
                            dataSource: actorData,
                            //select: onSelect,
                        }).data("kendoTreeView");
                    }
                    if($('#hasUserDelete').val() == 'false'){
            			$('.delete-link').hide();
            		}
                    $("#middle div span.k-checkbox").hide();
                    bindEvents();
                }

            });
        });
        
        $(document).ready(function () {
            $.ajax({
                url: '/sef-catalog-ui/getAllActors?searchQuery=' + document.getElementById('content-actor-search').value + '&pageNumber=' + page + '&pageSize=30',
                type: "GET",
                async: false,
                contentType: 'application/json',
                success: function (data) {
                    json = JSON.parse(data.responseString);
                    actorData = [];

                    page = page + 1;

                    var myStringArray = json.actors;
                    var arrayLength = myStringArray.length;
                    for (var i = 0; i < arrayLength; i++) {

                        var actor = {};
                        actor.text = myStringArray[i].name;
                        actor.expanded = false;
                        actor.items = [];
                        actor.spriteCssClass = "pdf";

                        var memberships = myStringArray[i].memberships;
                        if (memberships != null && memberships != undefined) {

                            for (key in memberships) {
                                var membership = {};
                                membership.text = memberships[key].name;
                                membership.expanded = false;
                                membership.spriteCssClass = "html";
                                actor.items.push(membership);
                            }
                        }
                        actorData.push(actor);
                    }


                    if ($('#middle')
                        .hasClass('k-treeview')) {
                        treeview = $("#middle").data("kendoTreeView");

                        treeview.setDataSource(new kendo.data.HierarchicalDataSource({
                            data: actorData,
                        }));


                    } else {
                        treeview = $("#middle").kendoTreeView({
                            //template: "#= item.text # (#=  #)",
                            template: kendo.template($("#middle-template").html()),
                            dataSource: actorData,
                            //select: onSelect,
                        }).data("kendoTreeView");
                    }
                    if($('#hasUserDelete').val() == 'false'){
            			$('.delete-link').hide();
            		}
                    $("#middle div span.k-checkbox").hide();
                    bindEvents();
                }
            });
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
        
        

        $("#ui-btn123").unbind('click').click(function () {
            $('#create-user input[name="name"]').prop('disabled', false);
            $("#content-user-mgt").show();
            $("#ui-btn123").show();
            $("#cancel-btn-user").show();
            $("#save-btn-user").show();
            $("#update-btn-user").hide();
            $('#assigned-fields-user-privileges').html('');
            $('#assigned-fields-user').html('');
            $('#create-user').find('input:text').val('');
            $.ajax({
                url: '/sef-catalog-ui/getAllGroups?searchQuery=' + '&pageNumber=1&pageSize=30',
                type: "GET",
                async: false,
                contentType: 'application/json',
                success: function (data) {
                    if (data.status == 'success') {
                        json = JSON.parse(data.responseString);
                        $('#nonassigned-fields-user').html('');
                        var myStringArray = json.groups;
                        var arrayLength = myStringArray.length;
                        for (var i = 0; i < arrayLength; i++) {
                            $('#nonassigned-fields-user').append('<option value="' + myStringArray[i].name + '">' + myStringArray[i].name + '</option>');
                        }
                    }
                }

            });

            $.ajax({
                url: '/sef-catalog-ui/getAllPrivileges',
                type: "GET",
                contentType: 'application/json',
                success: function (data) {
                    json = JSON.parse(data.responseString);

                    $('#nonassigned-fields-user-privileges').html('');
                    var myStringArray = json.privileges;

                    for (var key in myStringArray) {
                        $('#nonassigned-fields-user-privileges').append("<option value='" + key + "'>" + key + "</option>");
                    }

                }
            });
        });

        var readActor = {};

        $('#update-btn-user').unbind('click').click(function () {

            if ($('#user-menu').hasClass('active')) {
                json = {};
                json.name = $('#create-user input[name="name"]').val();
                json.userName = $('#create-user input[name="userName"]').val();
                json.password = $('#create-user input[name="password"]').val();
                json.credentialType = $('#create-user select[name="credentialType"]').val();

                json.actorMeta = {};

                var key = "";
                $('#keyvalue1 input').each(function (index) {
                    if (index % 2 == 0) {
                        key = $(this).val();
                    } else {
                        if (key == "" || key == undefined)
                            return true;

                        json.actorMeta[key] = key = $(this).val();
                    }
                });

                json.userMeta = {};

                $('#keyvalue2 input').each(function (index) {
                    if (index % 2 == 0) {
                        key = $(this).val();
                    } else {
                        if (key == "" || key == undefined)
                            return true;

                        json.userMeta[key] = key = $(this).val();
                    }
                });

                json.groups = [];

                $('#assigned-fields-user option').each(function () {
                    json.groups.push($(this).val());
                });

                json.privileges = [];

                $('#assigned-fields-user-privileges option').each(function () {
                    json.privileges.push($(this).val());
                });

                $.ajax({
                    url: '/sef-catalog-ui/updateActor',
                    type: "POST",
                    contentType: 'application/json',
                    data: JSON.stringify(json),
                    success: function (data) {
                        if (data.status == 'success') {
                            $('#error_message').show();
                            $('#myform_succloc').css('color', 'green').text('Actor Updated Successfully');
                        } else {
                            $('#error_message').show();
                            $('#myform_succloc').css('color', 'red').text(data.message);
                        }
                    }

                });
            }

        });



        $('#save-btn-user').unbind('click').click(function () {

            if ($('#user-menu').hasClass('active')) {
                json = {};
                json.name = $('#create-user input[name="name"]').val();
                json.userName = $('#create-user input[name="userName"]').val();
                json.password = $('#create-user input[name="password"]').val();
                json.credentialType = $('#create-user select[name="credentialType"]').val();

                json.actorMeta = {};

                var key = "";
                $('#keyvalue1 input').each(function (index) {
                    if (index % 2 == 0) {
                        key = $(this).val();
                    } else {
                        if (key == "" || key == undefined)
                            return true;

                        json.actorMeta[key] = key = $(this).val();
                    }
                });

                json.userMeta = {};

                $('#keyvalue2 input').each(function (index) {
                    if (index % 2 == 0) {
                        key = $(this).val();
                    } else {
                        if (key == "" || key == undefined)
                            return true;

                        json.userMeta[key] = key = $(this).val();
                    }
                });

                json.groups = [];

                $('#assigned-fields-user option').each(function () {
                    json.groups.push($(this).val());
                });

                json.privileges = [];

                $('#assigned-fields-user-privileges option').each(function () {
                    json.privileges.push($(this).val());
                });

                $.ajax({
                    url: '/sef-catalog-ui/createActor',
                    type: "POST",
                    contentType: 'application/json',
                    data: JSON.stringify(json),
                    success: function (data) {
                        if (data.status == 'success') {
                            $('#error_message').show();
                            $('#myform_succloc').css('color', 'green').text('Actor Created Successfully');
                            UserHandler.loadUsers();
                        } else {
                            $('#error_message').show();
                            $('#myform_succloc').css('color', 'red').text(data.message);
                        }
                    }

                });
            }

        });

        function bindEvents() {
        	if($('#hasUserDelete').val() == 'false'){
    			$('.delete-link').hide();
    		}
            $('.edit-link').unbind('click').click(function (e) {
                if ($('#user-menu').hasClass('active')) {
                    $('#create-user input[name="name"]').prop('disabled', true);
                    $("#save-btn-user").hide();
                    $("#update-btn-user").show();
                    $("#content-user-mgt").show();
                    $("#cancel-btn-user").show();
                    $('#assigned-fields-user-privileges').html('');
                    $('#assigned-fields-user').html('');
                    $('#create-user').find('input:text').val('');

                    $.ajax({
                        url: '/sef-catalog-ui/readActor?name=' + $(this).attr('text'),
                        type: "GET",
                        contentType: 'application/json',
                        success: function (data) {
                            if (data.status == 'success') {
                                $.ajax({
                                    url: '/sef-catalog-ui/getAllGroups?searchQuery=' + '&pageNumber=1&pageSize=30',
                                    type: "GET",
                                    async: false,
                                    contentType: 'application/json',
                                    success: function (data) {
                                        if (data.status == 'success') {
                                            json = JSON.parse(data.responseString);
                                            $('#nonassigned-fields-user').html('');
                                            var myStringArray = json.groups;
                                            var arrayLength = myStringArray.length;
                                            for (var i = 0; i < arrayLength; i++) {
                                                $('#nonassigned-fields-user').append('<option value="' + myStringArray[i].name + '">' + myStringArray[i].name + '</option>');
                                            }
                                        }
                                    }

                                });

                                $.ajax({
                                    url: '/sef-catalog-ui/getAllPrivileges',
                                    type: "GET",
                                    contentType: 'application/json',
                                    success: function (data) {
                                        json = JSON.parse(data.responseString);

                                        $('#nonassigned-fields-user-privileges').html('');
                                        var myStringArray = json.privileges;

                                        for (var key in myStringArray) {
                                            $('#nonassigned-fields-user-privileges').append("<option value='" + key + "'>" + key + "</option>");
                                        }

                                    }
                                });

                                json = JSON.parse(data.responseString);

                                $('#create-user input[name="name"]').val(json.name);

                                memberships = json.memberships;
                                for (key in memberships) {
                                    $('#assigned-fields-user').append('<option value="' + memberships[key].name + '">' + memberships[key].name + '</option>');
                                    $('#nonassigned-fields-user').find('[value="' + memberships[key].name + '"]').remove();
                                }

                                $("#appenduser").html('');

                                for (key in json.identities) {
                                    $('#create-user input[name="userName"]').val(json.identities[key].name);
                                    $('#create-user input[name="password"]').val(json.identities[key].password);

                                    var myStringArray = json.identities[key].privileges;
                                    if (myStringArray != null && myStringArray != undefined) {
                                        var arrayLength = myStringArray.length;
                                        for (var i = 0; i < arrayLength; i++) {
                                            $('#assigned-fields-user-privileges').append('<option value="' + myStringArray[i].name + '">' + myStringArray[i].name + '</option>');
                                            $('#nonassigned-fields-user-privileges').find('[value="' + myStringArray[i].name + '"]').remove();
                                        }
                                    }

                                    var myStringArray = json.identities[key].userMeta;
                                    var arrayLength = myStringArray.length;

                                    for (var i = 0; i < arrayLength; i++) {
                                        if (i != 0) {
                                            $("#appenduser").append($("#useradd").html());
                                        }
                                        $('#keyvalue2 input').get(i * 2).value = myStringArray[i].key;
                                        $('#keyvalue2 input').get(i * 2 + 1).value = myStringArray[i].value;
                                    }

                                    break;
                                }

                                var i = 0;

                                $("#appendactor").html('');

                                for (key in json.metas) {
                                    if (i != 0) {
                                        $("#appendactor").append($("#actoradd").html());
                                    }
                                    $('#keyvalue1 input').get(i * 2).value = key;
                                    $('#keyvalue1 input').get(i * 2 + 1).value = json.metas[key];
                                    i++;
                                }

                            } else {
                                $('#error_message').show();
                                $('#myform_succloc').css('color', 'red').text(data.message);
                            }
                        }

                    });
                }
            });


            $(".delete-link").unbind('click').click(function (e) {
                e.preventDefault();

                if ($('#user-menu').hasClass('active')) {
                    deleteuser = $(this).attr("text");
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
        }


        var deleteuser = "";


        $('.ok-btn').unbind('click').click(function () {
            $.ajax({
                url: '/sef-catalog-ui/deleteActor?name=' + deleteuser,
                type: "GET",
                contentType: 'application/json',
                success: function (data) {
                    $('.overlay-bg-delete').hide();
                    if (data.status == 'success') {
                        json = JSON.parse(data.responseString);
                        $('#content-actor-search').keyup();
                        $('#error_message').show();
                        $('#myform_succloc').css('color', 'green').text("Actor deleted successfully");
                    } else {
                        $('#error_message').show();
                        $('#myform_succloc').css('color', 'red').text(data.message);
                    }
                }
            });
        });
        $('.close-btn').unbind('click').click(function () {
            $('.overlay-bg-delete').hide(); // hide the overlay
        });

        // hides the popup if user clicks anywhere outside the container
        $('.overlay-bg-delete').unbind('click').click(function () {
                $('.overlay-bg-delete').hide();
            })
            // prevents the overlay from closing if user clicks inside the popup overlay
        $('.overlay-content-delete').unbind('click').click(function () {
            return false;
        });


        $("#cancel-btn-user").unbind('click').click(function () {
            $("#content-user-mgt").hide();
            $("#ui-btn123").show();
            $("#cancel-btn-user").hide();
            $("#save-btn-user").hide();
            $("#update-btn-user").hide();
        });




        $("#img-right-actor").unbind('click').click(function () {
            if ($('#nonassigned-fields-user option:selected').val() === undefined) {
                return;
            }
            $('#nonassigned-fields-user option:selected').each(function (e) {
                $('#assigned-fields-user').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');
            });
            $('#nonassigned-fields-user option:selected').remove();
        });

        $("#img-left-actor").unbind('click').click(function () {
            if ($('#assigned-fields-user option:selected').val() === undefined) {
                return;
            }
            $('#nonassigned-fields-user').append('<option value="' + $('#assigned-fields-user option:selected').val() + '">' + $('#assigned-fields-user option:selected').val() + '</option>');
            $('#assigned-fields-user option:selected').remove();
        });


        $("#img-right-actor1").unbind('click').click(function () {
            if ($('#nonassigned-fields-user-privileges option:selected').val() === undefined) {
                return;
            }
            $('#nonassigned-fields-user-privileges option:selected').each(function (e) {
                $('#assigned-fields-user-privileges').append('<option value="' + $(this).val() + '">' + $(this).val() + '</option>');

            });
            $('#nonassigned-fields-user-privileges option:selected').remove();
        });

        $("#img-left-actor1").unbind('click').click(function () {
            if ($('#assigned-fields-user-privileges option:selected').val() === undefined) {
                return;
            }
            $('#nonassigned-fields-user-privileges').append('<option value="' + $('#assigned-fields-user-privileges option:selected').val() + '">' + $('#assigned-fields-user-privileges option:selected').val() + '</option>');
            $('#assigned-fields-user-privileges option:selected').remove();
        });

    },

};
// End of UserHandler
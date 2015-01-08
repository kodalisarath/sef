<link href="css/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
<script src="js/jquery.contextMenu.js" type="text/javascript"></script>
<script src="js/notification/notification.js"></script>
<script type="text/javascript" class="showcase">
$("#notification_search").find("input[type=text]  ").each(function(){
    $(this).val('');            
}); 
/* $(".esearch").keyup(function() {
    var value = this.value;
    $("table#notifications").find("tr").each(function(index) {
        if (index === -1) return;
        var id = $(this).find("td").first().text();
        $(this).toggle(id.indexOf(value) !== -1);
    });
}); */
</script>
 
	      	
	      	<!-- <div id="policySplitter">
                <div id="policyTree" style="overflow: hidden;">
                      <div id="treeView"></div>
            </div>
               <div id="policyDetails" style="overflow: hidden;">
                       <div id="notificationEventGrid"></div>
               </div>
           </div>  -->
           
           <div id="policyDetails" style="overflow: hidden;">
                       <div id="notificationEventGrid"></div>
           </div>
	      	
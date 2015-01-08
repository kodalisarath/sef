<div class="user-content">
               <div class="user-content-left">
             
               		<div id='cssmenu'>
						<ul>
						<c:if test="${offerReadOnlyContains ||resourceReadOnlyContains ||ruleReadOnlyContains || resourceGroupReadOnlyContains||offerGroupReadOnlyContains|| ownerGroupReadOnlyContains  }">			   
						   <li class='has-sub active' >
						   			<a><img src="images/catalog.png" />
						   			 
                            		<fmt:message key="PRODUCTCATALOG" bundle="${lang}"/></a>
						      <ul>
						      <c:if test="${offerReadOnlyContains}">
						      <li id="offers-menu"  >
							         <a> 
							         <img src="images/offers.png" />
							         <fmt:message key="OFFERCATALOG" bundle="${lang}"/></a>
							         <span class="selected"></span>
						         </li>
						      </c:if>
						          <c:if test="${resourceReadOnlyContains}">
						           <li id="resource-menu">
						         	<a> <img src="images/registry.png"  /> 
                            		<fmt:message key="SERVICEREGISTRY" bundle="${lang}"/></a>
                            		<span class="selected"></span>
						         </li>
						          </c:if>
						        <c:if test="${ruleReadOnlyContains}">
						        <li id="business-menu">
						         	<a><img src="images/business.png"  />
                           			<fmt:message key="RULES" bundle="${lang}"/></a>
                           			<span class="selected"></span>
						         </li>
						        </c:if>
						         <c:if test="${resourceGroupReadOnlyContains}">
						          <li id="resource-group-menu">
						         	<a><img src="images/resource-mgr.png"  />
                            		<fmt:message key="RESOURCEGROUPMANAGER" bundle="${lang}"/></a>
                            		<span class="selected"></span>
						         </li>
						         </c:if>
						        
						         <c:if test="${offerGroupReadOnlyContains}">
						          <li id="offer-group-menu">
						         	<a><img src="images/catalog-group.png"  />
                           			<fmt:message key="OFFERCATALOGGROUPMANAGER" bundle="${lang}"/></a>
                           			<span class="selected"></span>
                            	 </li>
						         </c:if>
						         <c:if test="${ownerGroupReadOnlyContains}">
						          <li id="owner-group-menu">
						         	<a><img src="images/owner-group.png"  />
                           			<fmt:message key="OWNERGROUPMANAGER" bundle="${lang}"/></a>
                           			<span class="selected"></span>
                            	 </li>
						         </c:if>
						        
						         
						      </ul>
						   </li>	
						   </c:if>						
							<c:if test="${userReadOnlyContains ||groupReadOnlyContains}">
						   <li class='has-sub'>
						   		<a><img src="images/user.png" />
                            		<fmt:message key="USERMANAGEMENT" bundle="${lang}"/></a>
						      <ul>
						      <c:if test="${userReadOnlyContains}">
						       <li id="user-menu">
						         	<a><img src="images/users.png" />
                            		<fmt:message key="USERS" bundle="${lang}"/></a>
                            		<span class="selected"></span>
                           		</li>
						      </c:if>
						          <c:if test="${groupReadOnlyContains}">
						           <li id="group-menu">
						         	<a><img src="images/groups.png"  /> 
                            		<fmt:message key="GROUPS" bundle="${lang}"/></a>
                            		<span class="selected"></span>
                            	</li>
						          </c:if>
						          <!--This is not required as we are not gonna include Privileges as if now -->
						         <%-- <li class='last' id="priviliges-menu">
						         	<a><img src="images/privileges.png"  />
                            		<fmt:message key="PRIVILIGES" bundle="${lang}"/></a>
                            		<span class="selected"></span>
                            	</li> --%>
						      </ul>
						   </li>
						   </c:if>
						    <c:if test="${notificationReadOnlyContains}">
						   <li class='has-sub' >
						   		<a><img src="images/notification.png" />
	                       		<fmt:message key="NOTIFICATIONMANAGER" bundle="${lang}"/></a>
						   
						      <ul>
						     
						      <li id="notification-menu" class='last'>
							         <a>
							         <img src="images/notification_create.png" />
							         <fmt:message key="CREATENOTIFICATION" bundle="${lang}"/></a>
							         <span class="selected"></span>
						         </li>
						      
						         
						     </ul>
						  </li>
						</c:if>
						</ul>
					</div>
               </div>
</div>

package com.ericsson.raso.sef.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ericsson.raso.sef.auth.User;
import com.ericsson.raso.sef.auth.permissions.Privilege;
import com.ericsson.raso.sef.auth.service.IUserStore;
import com.ericsson.raso.sef.auth.service.UserStoreService;
import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.core.FrameworkException;


@Controller
public class AuthenticationController {

	static final org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(AuthenticationController.class.getName());

	private IUserStore userStoreService = null;
	

	public AuthenticationController() {
		this.userStoreService = new UserStoreService();
	}

	/**
	 * 
	 * @param user
	 * @param session
	 * @return view
	 */
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public String validateUser(@ModelAttribute("user") User user,HttpSession session) {
		if (user.getName() == null || user.getName().isEmpty()
				|| user.getPassword() == null || user.getPassword().isEmpty()) {
			session.setAttribute("loginMessage", "Username and Password cannot be empty");
			return "redirect:login";
		}
		if (user.getName().equals("admin")
				&& user.getPassword().equals("admin")) {
			session.setAttribute("username","admin");
			prepareAdminUserRoles(session);
			return "redirect:index";
		}

		try {
			validateAndPrepareRoles(user.getName(), user.getPassword(),session);
			return "redirect:index";

		} catch (CatalogException e) {
			logger.error("Error validating the user" + e);
			session.setAttribute("loginMessage","Invalid username/password");
			return "redirect:login";
		}
	}
	
	//Private Method to load default roles for a Main Admin user
	/**
	 * 
	 * @param session
	 * @return boolean 
	 * 
	 * Private method to prepare admin roles(Main user),admin login name can be changed in the future
	 */
	 private boolean prepareAdminUserRoles(HttpSession session) {
		 
		 
		    boolean offerConatains=true;
			boolean offerWriteContains=true;
			boolean offerReadOnlyContains=true;
			boolean offerUpdateContains=true;
			boolean offerDeleteContains=true;
			
			boolean resourceConatains=true;
			boolean resourceWriteContains=true;
			boolean resourceReadOnlyContains=true;
			boolean resourceUpdateContains=true;
			boolean resourceDeleteContains=true;
			
			boolean ownerGroupConatains=true;
			boolean ownerGroupWriteContains=true;
			boolean ownerGroupReadOnlyContains=true;
			boolean ownerGroupUpdateContains=true;
			boolean ownerGroupDeleteContains=true;
			
			boolean offerGroupConatains=true;
			boolean offerGroupWriteContains=true;
			boolean offerGroupReadOnlyContains=true;
			boolean offerGroupUpdateContains=true;
			boolean offerGroupDeleteContains=true;
			
			boolean resourceGroupConatains=true;
			boolean resourceGroupWriteContains=true;
			boolean resourceGroupReadOnlyContains=true;
			boolean resourceGroupUpdateContains=true;
			boolean resourceGroupDeleteContains=true;
			
			boolean groupConatains=true;
			boolean groupWriteContains=true;
			boolean groupReadOnlyContains=true;
			boolean groupUpdateContains=true;
			boolean groupDeleteContains=true;
			
			boolean userConatains=true;
			boolean userWriteContains=true;
			boolean userReadOnlyContains=true;
			boolean userUpdateContains=true;
			boolean userDeleteContains=true;
			
			boolean notificationConatains=true;
			boolean notificationWriteContains=true;
			boolean notificationReadOnlyContains=true;
			boolean notificationUpdateContains=true;
			boolean notificationDeleteContains=true;
			
			boolean ruleConatains=true;
			boolean ruleWriteContains=true;
			boolean ruleReadOnlyContains=true;
			boolean ruleUpdateContains=true;
			boolean ruleDeleteContains=true;
			
			
			
			session.setAttribute("username","admin");
			
			session.setAttribute("offerConatains",offerConatains);
			session.setAttribute("offerWriteContains",offerWriteContains);
			session.setAttribute("offerReadOnlyContains",offerReadOnlyContains);
			session.setAttribute("offerUpdateContains",offerUpdateContains);
			session.setAttribute("offerDeleteContains",offerDeleteContains);
			
			
			session.setAttribute("resourceConatains",resourceConatains);
			session.setAttribute("resourceWriteContains",resourceWriteContains);
			session.setAttribute("resourceDeleteContains",resourceDeleteContains);
			session.setAttribute("resourceUpdateContains",resourceUpdateContains);
			session.setAttribute("resourceReadOnlyContains",resourceReadOnlyContains);
			
			
			
			session.setAttribute("ruleConatains",ruleConatains);
			session.setAttribute("ruleReadOnlyContains",ruleReadOnlyContains);
			session.setAttribute("ruleUpdateContains",ruleUpdateContains);
			session.setAttribute("ruleWriteContains",ruleWriteContains);
			session.setAttribute("ruleDeleteContains",ruleDeleteContains);
			
			
			
			session.setAttribute("notificationConatains",notificationConatains);
			session.setAttribute("notificationReadOnlyContains",notificationReadOnlyContains);
			session.setAttribute("notificationWriteContains",notificationWriteContains);
			session.setAttribute("notificationUpdateContains",notificationUpdateContains);
			session.setAttribute("notificationDeleteContains",notificationDeleteContains);
			
			
			
			session.setAttribute("ownerGroupConatains",ownerGroupConatains);
			session.setAttribute("ownerGroupDeleteContains",ownerGroupDeleteContains);
			session.setAttribute("ownerGroupReadOnlyContains",ownerGroupReadOnlyContains);
			session.setAttribute("ownerGroupWriteContains",ownerGroupWriteContains);
			session.setAttribute("ownerGroupUpdateContains",ownerGroupUpdateContains);
			
			
			
			session.setAttribute("offerGroupConatains",offerGroupConatains);
			session.setAttribute("offerGroupDeleteContains",offerGroupDeleteContains);
			session.setAttribute("offerGroupReadOnlyContains",offerGroupReadOnlyContains);
			session.setAttribute("offerGroupWriteContains",offerGroupWriteContains);
			session.setAttribute("offerGroupUpdateContains",offerGroupUpdateContains);
			
			
			session.setAttribute("resourceGroupConatains",resourceGroupConatains);
			session.setAttribute("resourceGroupDeleteContains",resourceGroupDeleteContains);
			session.setAttribute("resourceGroupReadOnlyContains",resourceGroupReadOnlyContains);
			session.setAttribute("resourceGroupWriteContains",resourceGroupWriteContains);
			session.setAttribute("resourceGroupUpdateContains",resourceGroupUpdateContains);
			
			
			
			session.setAttribute("userConatains",userConatains);
			session.setAttribute("userDeleteContains",userDeleteContains);
			session.setAttribute("userReadOnlyContains",userReadOnlyContains);
			session.setAttribute("userWriteContains",userWriteContains);
			session.setAttribute("userUpdateContains",userUpdateContains);
			
			
			
			session.setAttribute("groupConatains",groupConatains);
			session.setAttribute("groupDeleteContains",groupDeleteContains);
			session.setAttribute("groupReadOnlyContains",groupReadOnlyContains);
			session.setAttribute("groupWriteContains",groupWriteContains);
			session.setAttribute("groupUpdateContains",groupUpdateContains);
			
			
			session.setAttribute("sessionContains",true);
			return true;
		 
		
	}

	 /**
	  * 
	  * @param request
	  * @return view
	  * 
	  * Returns back the appropriate jsp 
	  */
	@RequestMapping(value="index", method=RequestMethod.GET)
	    public String checkFlashAttributes(HttpServletRequest request) {
		if(request.getSession() != null){
			 return "index.jsp";
		}
		return "redirect:login.jsp";
	    }
	
	/**
	  * 
	  * @param request
	  * @return view
	  * 
	  * invalidate the session and takes the user to login page
	  */
	@RequestMapping(value="logout", method=RequestMethod.POST)
	    public String logoutSession(HttpServletRequest request) {
		if(request.getSession() != null){
			HttpSession userSession = request.getSession();
			  if(userSession != null){
				  userSession.invalidate();
			  }
			 return "login.jsp";
		}
		return "login.jsp";
	    }
	 
	/**
	 * 
	 * @param request
	 * @return view
	 * Returns back the appropriate jsp
	 */
	 @RequestMapping(value="login", method=RequestMethod.GET)
	    public String showLogin(HttpServletRequest request) {
		 if(request.getSession() != null){
			 return "login.jsp";
		}
		 return "redirect:login.jsp";
	    }


	 /**
	  * 
	  * @param name
	  * @param oldPassword
	  * @param newPassword
	  * @param confirmPassword
	  * @param redirectAttributes
	  * @return redirect view
	  */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String changeUserPassword(
			@RequestParam(required = true) String name,
			@RequestParam(required = true) String oldPassword,
			@RequestParam(required = true) String newPassword,
			@RequestParam(required = true) String confirmPassword,HttpSession session) {
		if (name == null||name.isEmpty() || oldPassword == null|| oldPassword.isEmpty() || newPassword == null|| newPassword.isEmpty() ||confirmPassword == null||confirmPassword.isEmpty() ) {
			session.setAttribute("ChangepwdError","Passwords cannot be empty");
			return "redirect:changepwd";
		}
		if(! newPassword.equalsIgnoreCase(confirmPassword))
		{
			session.setAttribute("ChangepwdError","New password and confirm password are not same");
			return "redirect:changepwd";
		}
		if (oldPassword.equalsIgnoreCase(newPassword)) {
			session.setAttribute("ChangepwdError","New password and old password should not be same");
			return "redirect:changepwd";
		}
		try {
			this.userStoreService.changeUserPassword(name,
					oldPassword, newPassword);
			session.setAttribute("loginMessage","Password change successfull");
			return "redirect:login";
		}catch (FrameworkException e) {
			logger.error("Error changing the password" + e);
			if(e.getMessage().equalsIgnoreCase("Old password entered is not correct") ||e.getMessage().equalsIgnoreCase("New Password cannot be same as old password"))
				session.setAttribute("ChangepwdError",e.getMessage());
			else
				session.setAttribute("ChangepwdError","Error changing the password");
			return "redirect:changepwd";
		}
		
		/*try {
			validateAndPrepareRoles(name, newPassword,redirectAttributes);
			return "redirect:index";
		} catch (CatalogException e) {
			logger.error("Error validating the user" + e);
			redirectAttributes.addFlashAttribute("ErrorMessage","Error validating the user.");
			return "redirect:login";
		}*/
			
				
		} 

	/**
	 * 
	 * @param request
	 * @return view
	 */
	 @RequestMapping(value="changepwd", method=RequestMethod.GET)
	    public String showChangePwd(HttpServletRequest request) {
		 if(request.getSession() != null){
			 return "changepwd.jsp";
		}
		 return "redirect:changepwd.jsp";
	    }
	 
	
	 /**
	  * 
	  * @param userName
	  * @param password
	  * @param session
	  * @return boolean
	  * @throws CatalogException
	  * 
	  * Private method to validate the logged in user and prepare roles for him
	  */
	private boolean validateAndPrepareRoles(String userName,String password, HttpSession session) throws CatalogException{
		try {
			this.userStoreService.validateUser(userName,
					password);
			User userObj = this.userStoreService.retrieveUserRoles(userName);
			
			boolean offerConatains=false;
			boolean offerWriteContains=false;
			boolean offerReadOnlyContains=false;
			boolean offerUpdateContains=false;
			boolean offerDeleteContains=false;
			
			boolean resourceConatains=false;
			boolean resourceWriteContains=false;
			boolean resourceReadOnlyContains=false;
			boolean resourceUpdateContains=false;
			boolean resourceDeleteContains=false;
			
			boolean ownerGroupConatains=false;
			boolean ownerGroupWriteContains=false;
			boolean ownerGroupReadOnlyContains=false;
			boolean ownerGroupUpdateContains=false;
			boolean ownerGroupDeleteContains=false;
			
			boolean offerGroupConatains=false;
			boolean offerGroupWriteContains=false;
			boolean offerGroupReadOnlyContains=false;
			boolean offerGroupUpdateContains=false;
			boolean offerGroupDeleteContains=false;
			
			boolean resourceGroupConatains=false;
			boolean resourceGroupWriteContains=false;
			boolean resourceGroupReadOnlyContains=false;
			boolean resourceGroupUpdateContains=false;
			boolean resourceGroupDeleteContains=false;
			
			boolean groupConatains=false;
			boolean groupWriteContains=false;
			boolean groupReadOnlyContains=false;
			boolean groupUpdateContains=false;
			boolean groupDeleteContains=false;
			
			boolean userConatains=false;
			boolean userWriteContains=false;
			boolean userReadOnlyContains=false;
			boolean userUpdateContains=false;
			boolean userDeleteContains=false;
			
			boolean notificationConatains=false;
			boolean notificationWriteContains=false;
			boolean notificationReadOnlyContains=false;
			boolean notificationUpdateContains=false;
			boolean notificationDeleteContains=false;
			
			boolean ruleConatains=false;
			boolean ruleWriteContains=false;
			boolean ruleReadOnlyContains=false;
			boolean ruleUpdateContains=false;
			boolean ruleDeleteContains=false;
			
			
			
			for (Privilege privilege : userObj.getPrivileges()) {
				switch (privilege.getName().name()) {
				case "ACCESS_OFFER_ALL":offerConatains=true;
				                        break;
				case "ACCESS_OFFER_READ_ONLY":offerReadOnlyContains=true;
				                              break;
				case "ACCESS_OFFER_CREATE_NEW":offerWriteContains=true;
				                               break;
				case "ACCESS_OFFER_UPDATE":offerUpdateContains=true;
				                           break;
				case "ACCESS_OFFER_DELETE":{
					offerDeleteContains=true;
					break;
					
					
				}
				case "ACCESS_RESOURCE_ALL":resourceConatains=true;
				                           break;
				case "ACCESS_RESOURCE_READ_ONLY":resourceReadOnlyContains=true;
				                                 break;
				case "ACCESS_RESOURCE_CREATE_NEW":resourceWriteContains=true;
				                                  break;
				case "ACCESS_RESOURCE_UPDATE":resourceUpdateContains=true;
				                              break;
				case "ACCESS_RESOURCE_DELETE":{
					resourceDeleteContains=true;
					break;
				}
				case "ACCESS_USER_ALL":userConatains=true;
				                       break;
				case "ACCESS_USER_READ_ONLY":userReadOnlyContains=true;
				                              break;
				case "ACCESS_USER_CREATE_NEW":userWriteContains=true;
				                              break;
				case "ACCESS_USER_UPDATE":userUpdateContains=true;
				                          break;
				case "ACCESS_USER_DELETE":{
					userDeleteContains=true;
					break;
				}
				case "ACCESS_GROUP_ALL":groupConatains=true;
				                        break;
				case "ACCESS_GROUP_READ_ONLY":groupReadOnlyContains=true;
				                              break;
				case "ACCESS_GROUP_CREATE_NEW":groupWriteContains=true;
				                               break;
				case "ACCESS_GROUP_UPDATE":groupUpdateContains=true;
				                           break;
				case "ACCESS_GROUP_DELETE":{
					groupDeleteContains=true;
					break;
				}
				case "ACCESS_NOTIFICATION_ALL":notificationConatains=true;
				                               break;
				case "ACCESS_NOTIFICATION_READ_ONLY":notificationReadOnlyContains=true;
				                                     break;
				case "ACCESS_NOTIFICATION_CREATE_NEW":notificationWriteContains=true;
				                                      break;
				case "ACCESS_NOTIFICATION_UPDATE":notificationUpdateContains=true;
				                                  break;
				case "ACCESS_NOTIFICATION_DELETE":{
					notificationDeleteContains=true;
					break;
				}
				case "ACCESS_RULES_ALL":ruleConatains=true;
				                        break;
				case "ACCESS_RULES_READ_ONLY":ruleReadOnlyContains=true;
				                              break;
				case "ACCESS_RULES_CREATE_NEW":ruleWriteContains=true;
				                               break;
				case "ACCESS_RULES_UPDATE":ruleUpdateContains=true;
				                           break;
				case "ACCESS_RULES_DELETE":{
					ruleDeleteContains=true;
					break;
				}
				case "ACCESS_OFFER_GROUP_ALL":offerGroupConatains=true;
				                              break;
				case "ACCESS_OFFER_GROUP_READ_ONLY":offerGroupReadOnlyContains=true;
				                                    break;
				case "ACCESS_OFFER_GROUP_CREATE_NEW":offerGroupWriteContains=true;
				                                     break;
				case "ACCESS_OFFER_GROUP_UPDATE":offerGroupUpdateContains=true;
				                                 break;
				case "ACCESS_OFFER_GROUP_DELETE":{
					offerGroupDeleteContains=true;
					break;
				}
				case "ACCESS_RESOURCE_GROUP_ALL":resourceGroupConatains=true;
				                                 break;
				case "ACCESS_RESOURCE_GROUP_READ_ONLY":resourceGroupReadOnlyContains=true;
				                                       break;
				case "ACCESS_RESOURCE_GROUP_CREATE_NEW":resourceGroupWriteContains=true;
				                                        break;
				case "ACCESS_RESOURCE_GROUP_UPDATE":resourceGroupUpdateContains=true;
				                                    break;
				case "ACCESS_RESOURCE_GROUP_DELETE":{
					resourceGroupDeleteContains=true;
					break;
				}
				case "ACCESS_OWNER_GROUP_ALL":ownerGroupConatains=true;
				                              break;
				case "ACCESS_OWNER_GROUP_READ_ONLY":ownerGroupReadOnlyContains=true;
				                                    break;
				case "ACCESS_OWNER_GROUP_CREATE_NEW":ownerGroupWriteContains=true;
				                                     break;
				case "ACCESS_OWNER_GROUP_UPDATE":ownerGroupUpdateContains=true;
				                                 break;
				case "ACCESS_OWNER_GROUP_DELETE":{
					ownerGroupDeleteContains=true;
					break;
				}
				default:
					break;
				}

			}
			session.setAttribute("username",userObj.getName());
			
			session.setAttribute("offerConatains",offerConatains);
			session.setAttribute("offerWriteContains",offerWriteContains);
			session.setAttribute("offerReadOnlyContains",offerReadOnlyContains);
			session.setAttribute("offerUpdateContains",offerUpdateContains);
			session.setAttribute("offerDeleteContains",offerDeleteContains);
			
			
			session.setAttribute("resourceConatains",resourceConatains);
			session.setAttribute("resourceWriteContains",resourceWriteContains);
			session.setAttribute("resourceDeleteContains",resourceDeleteContains);
			session.setAttribute("resourceUpdateContains",resourceUpdateContains);
			session.setAttribute("resourceReadOnlyContains",resourceReadOnlyContains);
			
			
			
			session.setAttribute("ruleConatains",ruleConatains);
			session.setAttribute("ruleReadOnlyContains",ruleReadOnlyContains);
			session.setAttribute("ruleUpdateContains",ruleUpdateContains);
			session.setAttribute("ruleWriteContains",ruleWriteContains);
			session.setAttribute("ruleDeleteContains",ruleDeleteContains);
			
			
			
			session.setAttribute("notificationConatains",notificationConatains);
			session.setAttribute("notificationReadOnlyContains",notificationReadOnlyContains);
			session.setAttribute("notificationWriteContains",notificationWriteContains);
			session.setAttribute("notificationUpdateContains",notificationUpdateContains);
			session.setAttribute("notificationDeleteContains",notificationDeleteContains);
			
			
			
			session.setAttribute("ownerGroupConatains",ownerGroupConatains);
			session.setAttribute("ownerGroupDeleteContains",ownerGroupDeleteContains);
			session.setAttribute("ownerGroupReadOnlyContains",ownerGroupReadOnlyContains);
			session.setAttribute("ownerGroupWriteContains",ownerGroupWriteContains);
			session.setAttribute("ownerGroupUpdateContains",ownerGroupUpdateContains);
			
			
			
			session.setAttribute("offerGroupConatains",offerGroupConatains);
			session.setAttribute("offerGroupDeleteContains",offerGroupDeleteContains);
			session.setAttribute("offerGroupReadOnlyContains",offerGroupReadOnlyContains);
			session.setAttribute("offerGroupWriteContains",offerGroupWriteContains);
			session.setAttribute("offerGroupUpdateContains",offerGroupUpdateContains);
			
			
			session.setAttribute("resourceGroupConatains",resourceGroupConatains);
			session.setAttribute("resourceGroupDeleteContains",resourceGroupDeleteContains);
			session.setAttribute("resourceGroupReadOnlyContains",resourceGroupReadOnlyContains);
			session.setAttribute("resourceGroupWriteContains",resourceGroupWriteContains);
			session.setAttribute("resourceGroupUpdateContains",resourceGroupUpdateContains);
			
		
			
			session.setAttribute("userConatains",userConatains);
			session.setAttribute("userDeleteContains",userDeleteContains);
			session.setAttribute("userReadOnlyContains",userReadOnlyContains);
			session.setAttribute("userWriteContains",userWriteContains);
			session.setAttribute("userUpdateContains",userUpdateContains);
			
		
			
			session.setAttribute("groupConatains",groupConatains);
			session.setAttribute("groupDeleteContains",groupDeleteContains);
			session.setAttribute("groupReadOnlyContains",groupReadOnlyContains);
			session.setAttribute("groupWriteContains",groupWriteContains);
			session.setAttribute("groupUpdateContains",groupUpdateContains);
			
			session.setAttribute("sessionContains",true);
			return true;
			
		} catch (FrameworkException e) {
			throw new CatalogException(e.getMessage());
		}
		
	}
	

}

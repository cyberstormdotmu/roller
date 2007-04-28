/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */

package org.apache.roller.ui.core.util.struts2;

import com.opensymphony.xwork2.ActionSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.roller.config.RollerConfig;
import org.apache.roller.config.RollerRuntimeConfig;
import org.apache.roller.pojos.UserData;
import org.apache.roller.pojos.WebsiteData;
import org.apache.roller.ui.core.util.UIUtils;
import org.apache.roller.ui.core.util.menu.Menu;
import org.apache.roller.ui.core.util.menu.MenuHelper;


/**
 * Extends the Struts2 ActionSupport class to add in support for handling an
 * error and status success.  Other actions extending this one only need to
 * calle setError() and setSuccess() accordingly.
 * 
 * NOTE: as a small convenience, all errors and messages are assumed to be keys
 * which point to a success in a resource bundle, so we automatically call
 * getText(key) on the param passed into setError() and setSuccess().
 */
public abstract class UIAction extends ActionSupport 
        implements UIActionPreparable, UISecurityEnforced {
    
    // the authenticated user accessing this action, or null if client is not logged in
    private UserData authenticatedUser = null;
    
    // the weblog this action is intended to work on, or null if no weblog specified
    private WebsiteData actionWeblog = null;
    
    // action name (used by tabbed menu utility)
    protected String actionName = null;
    
    // the name of the menu this action wants to show, or null for no menu
    protected String desiredMenu = null;
    
    // page title
    protected String pageTitle = null;
    
    
    public void myPrepare() {
        // no-op
    }
    
    
    // default action permissions, user is required
    public boolean isUserRequired() {
        return true;
    }
    
    // default action permissions, weblog is required
    public boolean isWeblogRequired() {
        return true;
    }
    
    // default action permissions, no role required
    public String requiredUserRole() {
        return null;
    }
    
    // default action permissions, no weblog permissions required
    public short requiredWeblogPermissions() {
        return -1;
    }
    
    
    public String getSiteURL() {
        return RollerRuntimeConfig.getRelativeContextURL();
    }
    
    public String getAbsoluteSiteURL() {
        return RollerRuntimeConfig.getAbsoluteContextURL();
    }
    
    public String getProp(String key) {
        // first try static config
        String value = RollerConfig.getProperty(key);
        if(value == null) {
            value = RollerRuntimeConfig.getProperty(key);
        }
        
        return (value == null) ? key : value;
    }
    
    public boolean getBooleanProp(String key) {
        // first try static config
        String value = RollerConfig.getProperty(key);
        if(value == null) {
            value = RollerRuntimeConfig.getProperty(key);
        }
        
        return (value == null) ? false : (new Boolean(value)).booleanValue();
    }
    
    public int getIntProp(String key) {
        // first try static config
        String value = RollerConfig.getProperty(key);
        if(value == null) {
            value = RollerRuntimeConfig.getProperty(key);
        }
        
        return (value == null) ? 0 : (new Integer(value)).intValue();
    }
    
    
    public void addError(String errorKey) {
        addActionError(getText(errorKey));
    }
    
    public void addError(String errorKey, String param) {
        addActionError(getText(errorKey, errorKey, param));
    }
    
    /**
     * This simply returns the result of hasActionErrors() but we need it
     * because without it you can't easily check if there were errors since
     * you can't call a hasXXX() method via OGNL.
     */
    public boolean errorsExist() {
        return hasActionErrors();
    }
    
    
    public void addMessage(String msgKey) {
        addActionMessage(getText(msgKey));
    }
    
    public void addMessage(String msgKey, String param) {
        addActionMessage(getText(msgKey, msgKey, param));
    }
    
    /**
     * This simply returns the result of hasActionMessages() but we need it
     * because without it you can't easily check if there were messages since
     * you can't call a hasXXX() method via OGNL.
     */
    public boolean messagesExist() {
        return hasActionMessages();
    }
    

    public UserData getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(UserData authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public WebsiteData getActionWeblog() {
        return actionWeblog;
    }

    public void setActionWeblog(WebsiteData workingWeblog) {
        this.actionWeblog = workingWeblog;
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }
    
    
    public String getActionName() {
        return this.actionName;
    }
    
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getDesiredMenu() {
        return desiredMenu;
    }

    public void setDesiredMenu(String desiredMenu) {
        this.desiredMenu = desiredMenu;
    }
    
    public Menu getMenu() {
        return MenuHelper.getMenu(getDesiredMenu(), getActionName(), getAuthenticatedUser(), getActionWeblog());
    }
    
    
    public String getShortDateFormat() {
        DateFormat sdf = DateFormat.getDateInstance(
                DateFormat.SHORT, getLocale());
        if (sdf instanceof SimpleDateFormat) {
            return ((SimpleDateFormat)sdf).toPattern();
        }
        return "yyyy/MM/dd";
    }
    
    public String getMediumDateFormat() {
        DateFormat sdf = DateFormat.getDateInstance(
                DateFormat.MEDIUM, getLocale());
        if (sdf instanceof SimpleDateFormat) {
            return ((SimpleDateFormat)sdf).toPattern();
        }
        return "MMM dd, yyyy";
    }
    
    public List getLocalesList() {
        return UIUtils.getLocales();
    }
    
    public List getTimeZonesList() {
        return UIUtils.getTimeZones();
    }
    
}

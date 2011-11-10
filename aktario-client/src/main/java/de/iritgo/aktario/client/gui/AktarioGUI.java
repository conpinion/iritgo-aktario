/**
 * This file is part of the Iritgo/Aktario Framework.
 *
 * Copyright (C) 2005-2011 Iritgo Technologies.
 * Copyright (C) 2003-2005 BueroByte GbR.
 *
 * Iritgo licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.iritgo.aktario.client.gui;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.gui.IDesktopManager;
import de.iritgo.aktario.core.gui.IOverlayIcon;
import de.iritgo.aktario.framework.base.InitIritgoException;
import de.iritgo.aktario.framework.client.gui.ClientGUI;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JPopupMenu;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;


/**
 * AktarioStandaloneGUI.
 *
 * @version $Id: AktarioGUI.java,v 1.26 2006/09/25 10:34:31 grappendorf Exp $
 */
public class AktarioGUI extends BaseObject implements ClientGUI
{
	/**
	 * Reload the menu bar.
	 * This method is called after a change to the language to reload
	 * the menu labels.
	 */
	public void reloadMenuBar()
	{
	}

	/**
	 * Reload the tool bar.
	 * This method is called after a change to the language to reload
	 * the menu labels.
	 */
	public void reloadToolBar()
	{
	}

	/**
	 * Get the name of the current color scheme.
	 *
	 * @retrurn The color scheme name.
	 */
	public String getColorScheme()
	{
		return null;
	}

	/**
	 * Change the color scheme.
	 *
	 * @param colorScheme The new color scheme.
	 */
	public void setColorScheme(String colorScheme)
	{
	}

	/**
	 * Retrieve the desktop manager.
	 *
	 * @return The desktop manager.
	 */
	public IDesktopManager getDesktopManager()
	{
		return null;
	}

	/**
	 * Show/hide the administration menu.
	 *
	 * @param visible If true the admin menu is visible.
	 */
	public void setAdminMenuVisible(boolean visible)
	{
	}

	/**
	 * Enable/disable the application menu.
	 *
	 * @param enabled If true the application menu is enabled.
	 */
	public void setApplicationMenuEnabled(boolean enabled)
	{
	}

	/**
	 * Retrieve the desktop pane.
	 *
	 * @return The desktop pane.
	 */
	public JDesktopPane getDesktopPane()
	{
		return null;
	}

	/**
	 * Called when the server connection was lost.
	 * This method shuts down the client and redisplays the login dialog.
	 */
	public void lostNetworkConnection()
	{
	}

	/**
	 * Stop the client gui.
	 */
	public void stopGUI()
	{
	}

	/**
	 * Start the client gui.
	 */
	public void startGUI()
	{
	}

	/**
	 * Start the client application.
	 */
	public void startApplication()
	{
	}

	/**
	 * Stop the client application.
	 */
	public void stopApplication()
	{
	}

	/**
	 * Initialize the main gui.
	 */
	public void init() throws InitIritgoException
	{
	}

	/**
	 * Set the user status text.
	 *
	 * @param userName The user name text to set.
	 */
	public void setStatusUser(String userName)
	{
	}

	/**
	 * Show the client gui.
	 */
	public void show()
	{
	}

	/**
	 * Get the login dialog background image.
	 *
	 * @return The background image.
	 */
	public ImageIcon getLoginBackground()
	{
		return null;
	}

	/**
	 * Get the about dialog background image.
	 *
	 * @return The background image.
	 */
	public ImageIcon getAboutBackground()
	{
		return null;
	}

	/**
	 * Get the participant indicator icon.
	 *
	 * @return The participant indicator icon.
	 */
	public IOverlayIcon getParticipantIndicator()
	{
		return null;
	}

	/**
	 * Get the system tray menu.
	 *
	 * @return The system tray popup menu.
	 */
	public JPopupMenu getSystemTrayMenu()
	{
		return null;
	}

	/**
	 * Get the position of the systray icon.
	 *
	 * @return The systray icon position.
	 */
	public Point getSystemTrayIconPosition()
	{
		return null;
	}

	/**
	 * Get the size of the systray icon.
	 *
	 * @return The systray icon size.
	 */
	public Dimension getSystemTrayIconSize()
	{
		return null;
	}

	/**
	 * Get the application title.
	 *
	 * @return The application title.
	 */
	public String getAppTitle()
	{
		return "Iritgo";
	}

	/**
	 * Set an application icon.
	 *
	 * @param key The icon key.
	 * @param icon The icon to set.
	 */
	public void setIcon(String key, ImageIcon icon)
	{
	}

	/**
	 * Get an application icon.
	 *
	 * @param key The icon key.
	 * @return The icon.
	 */
	public ImageIcon getIcon(String key)
	{
		return null;
	}

	/**
	 * Make the client gui the foreground window.
	 */
	public void bringToFront()
	{
	}

	/**
	 * Get the main application window.
	 *
	 * @return The application window.
	 */
	public Window getMainWindow()
	{
		return null;
	}
}

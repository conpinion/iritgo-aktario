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

package de.iritgo.aktario.framework.client.gui;


import de.iritgo.aktario.core.gui.IDesktopManager;
import de.iritgo.aktario.framework.base.InitIritgoException;
import javax.swing.JDesktopPane;
import java.awt.Window;


/**
 * This interface must be implemented by a client class, that actually creates
 * and manages the user interface of the client.
 */
public interface ClientGUI
{
	/**
	 * Get the desktop manager.
	 *
	 * @return The desktop manager. */
	public IDesktopManager getDesktopManager ();

	/**
	 * Get the desktop pane.
	 *
	 * @return The jdesktopPane. */
	public JDesktopPane getDesktopPane ();

	/**
	 * Initialize the client gui.
	 */
	public void init () throws InitIritgoException;

	/**
	 * Start the client application.
	 */
	public void startApplication ();

	/**
	 * Stop the client application.
	 */
	public void stopApplication ();

	/**
	 * Start the client gui.
	 */
	public void startGUI ();

	/**
	 * Stop the client gui.
	 */
	public void stopGUI ();

	/**
	 * Called when the network connection was lost.
	 */
	public void lostNetworkConnection ();

	/**
	 * Make the client gui the foreground window.
	 */
	public void bringToFront ();

	/**
	 * Get the main application window.
	 *
	 * @return The application window.
	 */
	public Window getMainWindow ();
}

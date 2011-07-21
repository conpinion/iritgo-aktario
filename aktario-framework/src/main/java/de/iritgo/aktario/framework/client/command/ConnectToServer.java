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

package de.iritgo.aktario.framework.client.command;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.config.Configuration;
import de.iritgo.aktario.core.config.SocketConfig;
import de.iritgo.aktario.core.gui.IDisplay;
import de.iritgo.aktario.core.network.ConnectObserver;
import de.iritgo.aktario.core.network.NetworkService;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.client.Client;
import java.util.Properties;


/**
 *
 */
public class ConnectToServer extends Command
{
	private String observerGuiPaneId;

	/**
	 * Create a mew ConnectToServer command.
	 *
	 * @param guiPaneId The id of the gui pane used as a connection
	 *   progress observer.
	 */
	public ConnectToServer (String guiPaneId)
	{
		this.observerGuiPaneId = guiPaneId;
	}

	/**
	 * Create a mew ConnectToServer command.
	 */
	public ConnectToServer ()
	{
	}

	/**
	 * Set the command Properties
	 *
	 * @param properties The properties.
	 */
	public void setProperties (Properties properties)
	{
	}

	/**
	 * ConnectToServer
	 */
	public void perform ()
	{
		AppContext appContext = AppContext.instance ();

		ConnectObserver observer = null;

		if (observerGuiPaneId != null)
		{
			IDisplay display = Client.instance ().getClientGUI ().getDesktopManager ().waitForDisplay (
							observerGuiPaneId);

			observer = (ConnectObserver) display.getGUIPane ();
		}

		NetworkService networkService = Client.instance ().getNetworkService ();

		Configuration config = IritgoEngine.instance ().getConfiguration ();
		SocketConfig socketConfig = config.getNetwork ().getSocket ();

		int port = socketConfig.getPort ();

		double channelNumber = networkService.connect (appContext.getServerIP (), port, observer);

		if (channelNumber < 0)
		{
			appContext.setConnectionState (false);

			return;
		}

		appContext.setConnectionState (true);
		appContext.setChannelNumber (channelNumber);

		return;
	}
}

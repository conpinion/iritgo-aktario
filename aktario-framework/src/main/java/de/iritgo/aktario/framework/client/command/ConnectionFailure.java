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


import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.gui.IDesktopManager;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.client.Client;
import java.util.Properties;


/**
 *
 */
public class ConnectionFailure extends Command
{
	private String textId;

	private AppContext appContext;

	/**
	 * Standard constructor
	 */
	public ConnectionFailure()
	{
		super("connectionfailure");
		appContext = AppContext.instance();
		textId = "common.connectionfailure";
	}

	/**
	 * Set the command Properties
	 *
	 * @param properties The properties.
	 */
	public void setProperties(Properties properties)
	{
	}

	/**
	 * Display the IWindow-Pane.
	 */
	public void perform()
	{
		IDesktopManager iDesktopManager = Client.instance().getClientGUI().getDesktopManager();

		iDesktopManager.closeAllDisplays();

		ShowDialog showDialog = new ShowDialog("common.connectionfailure");

		IritgoEngine.instance().getAsyncCommandProcessor().perform(showDialog);
	}

	public boolean canPerform()
	{
		return ! appContext.isConnectedWithServer();
	}
}

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
import de.iritgo.aktario.core.gui.IDisplay;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.user.action.RegisterNewUserServerAction;
import java.util.Properties;


/**
 *
 */
public class RegisterNewUser extends Command
{
	private String nickname;

	private String email;

	private AppContext appContext;

	/**
	 * Standard constructor
	 */
	public RegisterNewUser(String nickname, String email)
	{
		appContext = AppContext.instance();
		this.nickname = nickname;
		this.email = email;
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
		IDisplay display = Client.instance().getClientGUI().getDesktopManager().getDisplay("main.connect");

		if (display != null)
		{
			display.close();
			Client.instance().getClientGUI().getDesktopManager().removeDisplay(display);
		}

		double channelNumber = appContext.getChannelNumber();
		ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);

		clientTransceiver.addReceiver(channelNumber);

		RegisterNewUserServerAction registerNewUserServerAction = new RegisterNewUserServerAction(nickname, email);

		registerNewUserServerAction.setTransceiver(clientTransceiver);

		ActionTools.sendToServer(registerNewUserServerAction);
	}

	public boolean canPerform()
	{
		return appContext.isConnectedWithServer();
	}
}

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
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.action.UserLoginServerAction;
import de.iritgo.simplelife.string.StringTools;
import java.util.Properties;


/**
 *
 */
public class UserLogin extends Command
{
	private String username;

	private String password;

	private AppContext appContext;

	/**
	 * Standard constructor
	 * Get the user information from the application context.
	 */
	public UserLogin()
	{
		appContext = AppContext.instance();

		User user = appContext.getUser();

		if (user == null)
		{
			return;
		}

		this.username = user.getName();
		this.password = user.getPassword();
	}

	/**
	 * Standard constructor
	 */
	public UserLogin(String username, String password)
	{
		appContext = AppContext.instance();
		this.username = username;
		this.password = password;
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
	 * Create a Action to login.
	 */
	public void perform()
	{
		double channelNumber = appContext.getChannelNumber();
		ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);

		clientTransceiver.addReceiver(channelNumber);
		AppContext.instance().setUserPassword(password);
		AppContext.instance().setUserName(username);

		UserLoginServerAction userLoginServerAction = new UserLoginServerAction(username, password, System
						.getProperty("iritgo.app.version.long"));

		userLoginServerAction.setTransceiver(clientTransceiver);

		ActionTools.sendToServer(userLoginServerAction);
	}

	public boolean canPerform()
	{
		return appContext.isConnectedWithServer();
	}
}

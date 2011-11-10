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

package de.iritgo.aktario.framework.user.action;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.io.IOException;


/**
 *
 */
public class UserLoginAction extends FrameworkAction
{
	private User user;

	private String appId;

	/**
	 * Standard constructor
	 */
	public UserLoginAction()
	{
		super(- 1);
		user = new User();
	}

	/**
	 * Standard constructor
	 */
	public UserLoginAction(User user)
	{
		super(user.getUniqueId());
		this.user = user;
	}

	/**
	 * Get the id of the iritgo object.
	 */
	public String getTypeId()
	{
		return "action.userlogin";
	}

	/**
	 * Get the UserName.
	 */
	public String getUserName()
	{
		return user.getName();
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public String getAppId()
	{
		return appId;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		user.readObject(stream);
		appId = stream.readUTF();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		user.writeObject(stream);
		stream.writeUTF(appId);
	}

	/**
	 * Perform the action.
	 */
	public void perform()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;
		UserRegistry userRegistry = Client.instance().getUserRegistry();

		user.setNetworkChannel(clientTransceiver.getSender());
		user.setOnline(true);

		userRegistry.addUser(user);

		AppContext appContext = AppContext.instance();

		appContext.setUser(user);
		appContext.setAppId(appId);

		FrameworkProxy userProxy = new FrameworkProxy(user);

		Engine.instance().getProxyRegistry().addProxy(userProxy, user.getTypeId());
		Engine.instance().getBaseRegistry().add(user);

		Engine.instance().getFlowControl().ruleSuccess("UserLogin");

		Engine.instance().getEventRegistry().fire("User", new UserEvent(user, UserEvent.USER_LOGGED_IN));
	}
}

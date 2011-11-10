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
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.user.User;
import java.io.IOException;


/**
 *
 */
public class RegisterNewUserAction extends FrameworkAction
{
	private String userName;

	private String email;

	private String password;

	/**
	 * Standard constructor
	 */
	public RegisterNewUserAction()
	{
	}

	/**
	 * Standard constructor
	 */
	public RegisterNewUserAction(String userName, String email, long userUniqueId, String password)
	{
		super(userUniqueId);

		this.userName = userName;
		this.email = email;
		this.password = password;
	}

	/**
	 * Get the id of the iritgo object.
	 */
	public String getTypeId()
	{
		return "action.registernewuser";
	}

	/**
	 * Get the UserName.
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		userName = stream.readUTF();
		email = stream.readUTF();
		password = stream.readUTF();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeUTF(userName);
		stream.writeUTF(email);
		stream.writeUTF(password);
	}

	/**
	 * Perform the action.
	 */
	public void perform()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		User user = new User(userName, email, 0, password, clientTransceiver.getSender());

		AppContext appContext = AppContext.instance();

		appContext.setUser(user);

		Engine.instance().getFlowControl().ruleSuccess("userregisted");
	}
}

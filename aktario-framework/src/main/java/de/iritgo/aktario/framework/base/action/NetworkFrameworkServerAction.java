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

package de.iritgo.aktario.framework.base.action;


import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.user.User;
import java.io.IOException;


/**
 *
 */
public class NetworkFrameworkServerAction extends FrameworkServerAction
{
	/**
	 * Standard constructor
	 */
	public NetworkFrameworkServerAction()
	{
		super();
	}

	/**
	 * Standard constructor
	 *
	 * @param userUniqueId
	 */
	public NetworkFrameworkServerAction(long userUniqueId)
	{
		super(userUniqueId);
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		FrameworkAction action = getAction(clientTransceiver);

		if (action == null)
		{
			return;
		}

		action.setTransceiver(clientTransceiver);

		// The direct answer of a action have the same uniqueId, its import for the blocked networkprocessors, its a fact...
		action.setUniqueId(getUniqueId());
		ActionTools.sendToClient(action);
	}

	public User getUser()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		return (User) clientTransceiver.getConnectedChannel().getCustomerContextObject();
	}

	public FrameworkAction getAction(@SuppressWarnings("unused") ClientTransceiver clientTransceiver)
	{
		return null;
	}
}

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
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.action.EditIObjectServerAction;


/**
 *
 */
public class EditIObject extends Command
{
	private IObject prototype;

	/**
	 * Standard constructor
	 *
	 */
	public EditIObject ()
	{
		super ("EditIObject");
	}

	/**
	 * Standard constructor
	 *
	 * @param prototype The prototype that have changed
	 */
	public EditIObject (IObject prototype)
	{
		this ();
		this.prototype = prototype;
	}

	/**
	 * EditIObject
	 */
	public void perform ()
	{
		double channelNumber = AppContext.instance ().getChannelNumber ();
		ClientTransceiver clientTransceiver = new ClientTransceiver (channelNumber);

		clientTransceiver.addReceiver (channelNumber);

		IObjectProxy iObjectProxy = Engine.instance ().getProxyRegistry ().getProxy (prototype.getUniqueId (),
						prototype.getTypeId ());

		iObjectProxy.setUpToDate (false);

		EditIObjectServerAction editPrototypeServerAction = new EditIObjectServerAction (prototype);

		editPrototypeServerAction.setTransceiver (clientTransceiver);

		ActionTools.sendToServer (editPrototypeServerAction);
	}
}

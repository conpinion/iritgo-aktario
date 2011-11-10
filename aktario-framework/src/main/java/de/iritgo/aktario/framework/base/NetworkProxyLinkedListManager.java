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

package de.iritgo.aktario.framework.base;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectListEvent;
import de.iritgo.aktario.core.iobject.IObjectListListener;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.action.ProxyLinkedListAddServerAction;
import de.iritgo.aktario.framework.base.action.ProxyLinkedListRemoveServerAction;
import de.iritgo.aktario.framework.user.User;


/**
 *
 */
public class NetworkProxyLinkedListManager extends BaseObject implements IObjectListListener
{
	/**
	 * Constructor
	 */
	public NetworkProxyLinkedListManager()
	{
		super("networkproxylinkedlistmanager");
		Engine.instance().getEventRegistry().addListener("proxylinkedlistupdate", this);
	}

	/**
	 * The IObjectProxyListener, called if the basicobject is a fresh object or the update process is working.
	 *
	 * @param event The EventOject.
	 */
	public void iObjectListEvent(IObjectListEvent event)
	{
		// TODO: Function to remove a object from list
		IObject newProt = event.getObject();
		double channelNumber = AppContext.instance().getChannelNumber();
		ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);

		if (event.getType() == IObjectListEvent.REMOVE)
		{
			ProxyLinkedListRemoveServerAction action = new ProxyLinkedListRemoveServerAction(event.getOwnerObject()
							.getUniqueId(), event.getOwnerObject().getTypeId(), event.getListAttribute(), newProt);

			clientTransceiver.addReceiver(clientTransceiver.getSender());

			action.setTransceiver(clientTransceiver);

			ActionTools.sendToServer(action);

			return;
		}

		if (newProt.getUniqueId() > 0)
		{
			//Ist kein lokales erzeugtes neues object
			return;
		}

		long newUniqueId = newProt.getUniqueId();

		((User) AppContext.instance().getUser()).putNewObjectsMapping(new Long(newUniqueId), new Long(newUniqueId));

		// Ya, its a brand new object, and now we transfer it to the server
		clientTransceiver.addReceiver(channelNumber);

		ProxyLinkedListAddServerAction action = new ProxyLinkedListAddServerAction(
						event.getOwnerObject().getUniqueId(), event.getOwnerObject().getTypeId(), event
										.getListAttribute(), newProt);

		action.setTransceiver(clientTransceiver);

		ActionTools.sendToServer(action);
	}
}

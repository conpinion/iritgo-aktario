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
import de.iritgo.aktario.core.iobject.IObjectProxyEvent;
import de.iritgo.aktario.core.iobject.IObjectProxyListener;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.action.ProxyServerAction;


/**
 *
 */
public class NetworkProxyManager extends BaseObject implements IObjectProxyListener
{
	/**
	 * Constructor
	 */
	public NetworkProxyManager ()
	{
		super ("networkproxymanager");
		Engine.instance ().getEventRegistry ().addListener ("proxyupdate", this);
	}

	/**
	 * The IObjectProxyListener, called if the basicobject is a fresh object or the update process is working.
	 *
	 * @param event The EventOject.
	 */
	public void proxyEvent (IObjectProxyEvent event)
	{
		double channelNumber = AppContext.instance ().getChannelNumber ();
		ClientTransceiver clientTransceiver = new ClientTransceiver (channelNumber);

		clientTransceiver.addReceiver (channelNumber);

		ProxyServerAction action = new ProxyServerAction (event.getUniqueId (), event.getIObjectTypeId ());

		action.setTransceiver (clientTransceiver);

		ActionTools.sendToServer (action);
	}
}

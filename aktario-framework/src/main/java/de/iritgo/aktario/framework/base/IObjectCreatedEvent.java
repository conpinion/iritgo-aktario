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


import de.iritgo.aktario.core.event.Event;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.network.ClientTransceiver;


/**
 *
 */
public class IObjectCreatedEvent implements Event
{
	private IObject createdObject;

	private IObject ownerObject;

	private ClientTransceiver clientTransceiver;

	private String listAttribute;

	/**
	 * Standard constructor
	 */
	public IObjectCreatedEvent (IObject createdObject, IObject ownerObject, String listAttribute,
					ClientTransceiver clientTransceiver)
	{
		this.createdObject = createdObject;
		this.ownerObject = ownerObject;
		this.clientTransceiver = clientTransceiver;
		this.listAttribute = listAttribute;
	}

	public IObject getCreatedObject ()
	{
		return createdObject;
	}

	public IObject getOwnerObject ()
	{
		return ownerObject;
	}

	public String getListAttribute ()
	{
		return listAttribute;
	}

	public ClientTransceiver getClientTransceiver ()
	{
		return clientTransceiver;
	}
}

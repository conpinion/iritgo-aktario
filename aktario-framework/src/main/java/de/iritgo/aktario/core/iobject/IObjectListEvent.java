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

package de.iritgo.aktario.core.iobject;


import de.iritgo.aktario.core.event.Event;
import de.iritgo.aktario.core.network.ClientTransceiver;


/**
 *
 */
public class IObjectListEvent implements Event
{
	public static int ADD = 0;

	public static int REMOVE = 1;

	private int type;

	private IObject iObject;

	private IObject ownerIObject;

	private String listAttribute;

	private ClientTransceiver clientTransceiver;

	/**
	 * Standard constructor
	 *
	 * @param iObject The new prototypeable to add to the system
	 * @param ownerIObject The ownerIObject of the proxylinkedlist
	 * @param listAttribute The name of the list attribute.
	 * @param type The type of this event (Add, Remove)
	 */
	public IObjectListEvent(IObject iObject, IObject ownerIObject, String listAttribute, int type)
	{
		this(iObject, ownerIObject, listAttribute, null, type);
	}

	/**
	 * Standard constructor
	 */
	public IObjectListEvent(IObject iObject, IObject ownerIObject, String id, ClientTransceiver clientTransceiver,
					int type)
	{
		this.iObject = iObject;
		this.ownerIObject = ownerIObject;
		this.listAttribute = id;
		this.type = type;
		this.clientTransceiver = clientTransceiver;
	}

	public IObject getObject()
	{
		return iObject;
	}

	public IObject getOwnerObject()
	{
		return ownerIObject;
	}

	public int getType()
	{
		return type;
	}

	public String getListAttribute()
	{
		return listAttribute;
	}

	public ClientTransceiver getClientTransceiver()
	{
		return clientTransceiver;
	}
}

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

package de.iritgo.aktario.framework.dataobject;


import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import de.iritgo.aktario.framework.base.action.EditIObjectAction;
import de.iritgo.aktario.framework.base.action.ProxyLinkedListRemoveAction;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.util.Iterator;


/**
 * A abstract query for one data objects
 */
public class AbstractQuery extends DataObject implements Query
{
	private DataObject dataObject;

	public AbstractQuery (String queryName)
	{
		super (queryName);
		addAttribute ("dataObjectTypeId", "none");
		addAttribute ("userUniqueId", new Long (0));
		addAttribute ("searchCondition", "");
		addAttribute ("results", new IObjectList ("results", new FrameworkProxy (new DataObject ("dummy")), this));
	}

	public void setDataObject (DataObject dataObject)
	{
		this.dataObject = dataObject;
		setAttribute ("dataObjectTypeId", dataObject.getTypeId ());
	}

	public void setDataObjectTypeId (String dataObjectTypeId)
	{
		setAttribute ("dataObjectTypeId", dataObjectTypeId);
	}

	public String getDataObjectTypeId ()
	{
		return getStringAttribute ("dataObjectTypeId");
	}

	public void setSearchCondition (String searchCondition)
	{
		setAttribute ("searchCondition", searchCondition);
	}

	public String getSearchCondition ()
	{
		return getStringAttribute ("searchCondition");
	}

	public void setUserUniqueId (long uniqueId)
	{
		setAttribute ("userUniqueId", uniqueId);
	}

	public long getUserUniqueId ()
	{
		return getLongAttribute ("userUniqueId");
	}

	public Iterator getResults ()
	{
		return getIObjectListAttribute ("results").iterator ();
	}

	public IObjectList getIObjectListResults ()
	{
		return getIObjectListAttribute ("results");
	}

	public boolean workingWithThisDataObjectTypeId (String dataObjectTypeId)
	{
		return getDataObjectTypeId ().equals (dataObjectTypeId);
	}

	public void doQuery ()
	{
	}

	public void doCreatedDataObjectQuery (DataObject dataObject)
	{
	}

	public void doDeletedDataObjectQuery (DataObject dataObject)
	{
	}

	public void refresh ()
	{
	}

	public void createdUpdateOwner ()
	{
		UserRegistry userRegistry = Server.instance ().getUserRegistry ();
		User user = userRegistry.getUser (getUserUniqueId ());

		ClientTransceiver clientTransceiver = new ClientTransceiver (user.getNetworkChannel ());

		clientTransceiver.addReceiver (user.getNetworkChannel ());

		EditIObjectAction editPrototypeAction = new EditIObjectAction (EditIObjectAction.OK, this);

		editPrototypeAction.setTransceiver (clientTransceiver);
		editPrototypeAction.setUniqueId (getUniqueId ());
		ActionTools.sendToClient (editPrototypeAction);
	}

	public void removedUpdateOwner (IObject iObject)
	{
		UserRegistry userRegistry = Server.instance ().getUserRegistry ();
		User user = userRegistry.getUser (getUserUniqueId ());

		ClientTransceiver clientTransceiver = new ClientTransceiver (user.getNetworkChannel ());

		clientTransceiver.addReceiver (user.getNetworkChannel ());

		ProxyLinkedListRemoveAction action = new ProxyLinkedListRemoveAction (iObject.getUniqueId (), iObject
						.getTypeId (), getUniqueId (), getTypeId (), "results");

		action.setTransceiver (clientTransceiver);
		action.setUniqueId (getUniqueId ());
		ActionTools.sendToClient (action);
	}
}

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


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.user.User;
import java.util.Iterator;


/**
 * A simple query for one data objects
 */
public class SimpleQuery extends AbstractQuery
{
	public SimpleQuery ()
	{
		super ("SimpleQuery");

		User user = AppContext.instance ().getUser ();

		if (user != null)
		{
			setUserUniqueId (user.getUniqueId ());
		}
	}

	public SimpleQuery (String dataObjectTypeId)
	{
		this ();
		setDataObjectTypeId (dataObjectTypeId);
	}

	public void refresh ()
	{
		IObjectList results = (IObjectList) getIObjectListResults ();

		results.clearIObjectList ();
		doQuery ();
	}

	public void doQuery ()
	{
		String dataObjectTypeId = getDataObjectTypeId ();
		IObjectList results = (IObjectList) getIObjectListResults ();

		for (Iterator i = Engine.instance ().getBaseRegistry ().iterator (dataObjectTypeId); i.hasNext ();)
		{
			results.add ((IObject) i.next ());
		}
	}

	public void doCreatedDataObjectQuery (DataObject dataObject)
	{
		IObjectList results = (IObjectList) getIObjectListResults ();

		if (! results.contains (dataObject))
		{
			results.add ((IObject) dataObject);
			createdUpdateOwner ();
		}
	}

	public void doDeletedDataObjectQuery (DataObject dataObject)
	{
		IObjectList results = (IObjectList) getIObjectListResults ();

		if (results.contains (dataObject))
		{
			results.removeIObject ((IObject) dataObject);
			removedUpdateOwner (dataObject);
		}
	}

	public boolean workingWithThisDataObjectTypeId (String dataObjectTypeId)
	{
		return getDataObjectTypeId ().equals (dataObjectTypeId);
	}
}

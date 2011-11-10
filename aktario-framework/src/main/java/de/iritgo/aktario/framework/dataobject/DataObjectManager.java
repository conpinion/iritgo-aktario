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
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectListEvent;
import de.iritgo.aktario.core.iobject.IObjectListListener;
import de.iritgo.aktario.core.iobject.IObjectRequestEvent;
import de.iritgo.aktario.core.iobject.IObjectRequestListener;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.IObjectCreatedEvent;
import de.iritgo.aktario.framework.base.IObjectCreatedListener;
import de.iritgo.aktario.framework.base.IObjectDeletedEvent;
import de.iritgo.aktario.framework.base.IObjectDeletedListener;
import de.iritgo.aktario.framework.base.IObjectModifiedEvent;
import de.iritgo.aktario.framework.base.IObjectModifiedListener;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserListener;
import java.util.Iterator;


/**
 *
 */
public class DataObjectManager extends BaseObject implements Manager, UserListener, IObjectListListener,
				IObjectCreatedListener, IObjectModifiedListener, IObjectDeletedListener, IObjectRequestListener
{
	private DynDataObjectRegistry dynDataObjectRegistry;

	private QueryRegistry queryRegistry;

	public DataObjectManager()
	{
		super("DataObjectManager");
		dynDataObjectRegistry = new DynDataObjectRegistry();
		queryRegistry = new QueryRegistry();
		init();
	}

	public void init()
	{
		Engine.instance().getEventRegistry().addListener("User", this);
		Engine.instance().getEventRegistry().addListener("objectcreated", this);
		Engine.instance().getEventRegistry().addListener("objectmodified", this);
		Engine.instance().getEventRegistry().addListener("objectrequested", this);
		Engine.instance().getEventRegistry().addListener("objectremoved", this);
	}

	public void unload()
	{
		Engine.instance().getEventRegistry().removeListener("User", this);
		Engine.instance().getEventRegistry().removeListener("objectcreated", this);
		Engine.instance().getEventRegistry().removeListener("objectmodified", this);
		Engine.instance().getEventRegistry().removeListener("objectrequested", this);
		Engine.instance().getEventRegistry().removeListener("objectremoved", this);
	}

	public DynDataObjectRegistry getDynDataObjectRegistry()
	{
		return dynDataObjectRegistry;
	}

	/**
	 * Get the query registry
	 *
	 * @return queryregistry
	 */
	public QueryRegistry getQueryRegistry()
	{
		return queryRegistry;
	}

	/**
	 * This method is called when user has logged in.
	 *
	 * @param event The user event.
	 */
	public void userEvent(UserEvent event)
	{
		if ((event != null) && (event.isLoggedIn()))
		{
			User user = event.getUser();
		}

		if ((event != null) && (event.isLoggedOut()))
		{
			User user = event.getUser();

			for (Iterator i = queryRegistry.queryIterator(); i.hasNext();)
			{
				Query query = (Query) i.next();

				if (query.getUserUniqueId() == user.getUniqueId())
				{
					queryRegistry.removeQuery(query);
					Engine.instance().getBaseRegistry().remove((BaseObject) query);

					Log.logInfo("system", "DataObjectManager:UserLoggedOut", "Query from user removed:" + user);
				}
			}
		}
	}

	/**
	 * Called when a data object was added to or removed from an
	 * object list.
	 *
	 * @param event The object list event.
	 */
	public void iObjectListEvent(IObjectListEvent event)
	{
		if (event.getType() == IObjectListEvent.ADD)
		{
			checkCreatedDataObjectQueries((DataObject) event.getObject());
		}

		if (event.getType() == IObjectListEvent.REMOVE)
		{
			checkDeletedDataObjectQueries((DataObject) event.getObject());
		}
	}

	/**
	 * Called when an iritgo object was created.
	 *
	 * @param event The creation event.
	 */
	public void iObjectCreatedEvent(IObjectCreatedEvent event)
	{
		checkCreatedDataObjectQueries((DataObject) event.getCreatedObject());
	}

	/**
	 * Called when an iritgo object was modified.
	 *
	 * @param event The modification event.
	 */
	public void iObjectModifiedEvent(IObjectModifiedEvent event)
	{
		IObject iObject = (IObject) event.getModifiedObject();

		if (iObject instanceof AbstractQuery)
		{
			((AbstractQuery) iObject).refresh();
		}
	}

	/**
	 * Called when an iritgo object was deleted.
	 *
	 * @param event The delete event.
	 */
	public void iObjectDeletedEvent(IObjectDeletedEvent event)
	{
		checkDeletedDataObjectQueries((DataObject) event.getDeletedObject());
	}

	/**
	 * Called when an iritgo object is requested.
	 *
	 * @param event The reuqest event.
	 */
	public void iObjectRequestEvent(IObjectRequestEvent event)
	{
	}

	private void checkCreatedDataObjectQueries(DataObject dataObject)
	{
		for (Iterator i = queryRegistry.queryIterator(dataObject.getTypeId()); i.hasNext();)
		{
			Query query = (Query) i.next();

			query.doCreatedDataObjectQuery(dataObject);
		}
	}

	private void checkDeletedDataObjectQueries(DataObject dataObject)
	{
		for (Iterator i = queryRegistry.queryIterator(dataObject.getTypeId()); i.hasNext();)
		{
			Query query = (Query) i.next();

			query.doDeletedDataObjectQuery(dataObject);
		}
	}
}

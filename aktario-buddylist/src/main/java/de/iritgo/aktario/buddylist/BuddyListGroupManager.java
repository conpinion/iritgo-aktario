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

package de.iritgo.aktario.buddylist;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.core.user.AktarioUser;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import de.iritgo.aktario.framework.base.IObjectCreatedEvent;
import de.iritgo.aktario.framework.base.IObjectDeletedEvent;
import de.iritgo.aktario.framework.base.IObjectModifiedEvent;
import de.iritgo.aktario.framework.base.action.EditIObjectAction;
import de.iritgo.aktario.framework.dataobject.DynDataObject;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserListener;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.util.Iterator;


/**
 * BuddyListGroupManager
 *
 * @version $Id: BuddyListGroupManager.java,v 1.5 2006/09/25 10:34:32 grappendorf Exp $
 */
public class BuddyListGroupManager extends BaseObject implements Manager, UserListener
{
	/**
	 * Create a new client manager.
	 */
	public BuddyListGroupManager()
	{
		super("BuddyListGroupManager");
	}

	/**
	 * Initialize the client manager.
	 */
	public void init()
	{
	}

	/**
	 * Called when an iritgo object was created.
	 *
	 * @param event The creation event.
	 */
	public void iObjectCreatedEvent(IObjectCreatedEvent event)
	{
		if (event.getCreatedObject() instanceof AktarioUser)
		{
			try
			{
				DataObject participantGroup = (DataObject) Engine.instance().getIObjectFactory().newInstance(
								"ParticipantGroup");

				participantGroup.setUniqueId(Engine.instance().getPersistentIDGenerator().createId());

				IObjectProxy proxy = (IObjectProxy) new FrameworkProxy();

				proxy.setSampleRealObject((IObject) participantGroup);
				Engine.instance().getBaseRegistry().add((BaseObject) participantGroup);
				Engine.instance().getProxyRegistry().addProxy(proxy, participantGroup.getTypeId());

				Engine.instance().getEventRegistry().fire("objectcreated",
								new IObjectCreatedEvent(participantGroup, null, null, null));

				ActionTools.sendServerBroadcast(new EditIObjectAction(EditIObjectAction.OK, participantGroup));
			}
			catch (Exception x)
			{
				Log.logFatal("system", "ParicipantGroupManager:pluginEvent(Create participantGroups)",
								"Can not create participant group error.");
				x.printStackTrace();
			}
		}
	}

	/**
	 * Called when an iritgo object was modified.
	 *
	 * @param event The modification event.
	 */
	public void iObjectModifiedEvent(IObjectModifiedEvent event)
	{
		if (event.getModifiedObject() instanceof AktarioUser)
		{
		}
	}

	/**
	 * Called when an iritgo object was deleted.
	 *
	 * @param event The delete event.
	 */
	public void iObjectDeletedEvent(IObjectDeletedEvent event)
	{
		if (event.getDeletedObject() instanceof AktarioUser)
		{
			try
			{
				UserRegistry userRegistry = Server.instance().getUserRegistry();

				User user = userRegistry.getUser(((AktarioUser) event.getDeletedObject()).getUserId());

				DynDataObject participantGroup = null;

				for (Iterator i = Engine.instance().getBaseRegistry().iterator("ParticipantGroup"); i.hasNext();)
				{
					participantGroup = (DynDataObject) i.next();

					if (participantGroup.getStringAttribute("iritgoUserName").equals(user.getName()))
					{
						break;
					}
				}

				Engine.instance().getEventRegistry().fire("objectremoved",
								new IObjectDeletedEvent(participantGroup, null, null, null));
				Engine.instance().getBaseRegistry().remove(participantGroup);
			}
			catch (Exception x)
			{
				Log.logFatal("system", "ParicipantGroupManager:pluginEvent(delete participant group)",
								"Can not delete participant group error.");
			}
		}
	}

	public void userEvent(UserEvent event)
	{
		User user = event.getUser();

		if (user == null)
		{
			return;
		}

		if (event.isLoggedIn())
		{
		}

		if (event.isLoggedOut())
		{
		}
	}

	/**
	 * Free all client manager resources.
	 */
	public void unload()
	{
	}
}

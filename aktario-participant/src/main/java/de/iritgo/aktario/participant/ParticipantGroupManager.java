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

package de.iritgo.aktario.participant;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.core.user.AktarioUser;
import de.iritgo.aktario.core.user.AktarioUserManager;
import de.iritgo.aktario.core.user.AktarioUserRegistry;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import de.iritgo.aktario.framework.base.IObjectCreatedEvent;
import de.iritgo.aktario.framework.base.IObjectDeletedEvent;
import de.iritgo.aktario.framework.base.IObjectModifiedEvent;
import de.iritgo.aktario.framework.base.action.EditIObjectAction;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.dataobject.DynDataObject;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserListener;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.util.Iterator;
import java.util.Properties;


/**
 * ParticipantGroupServerManager
 *
 * @version $Id: ParticipantGroupManager.java,v 1.4 2006/09/25 10:34:31 grappendorf Exp $
 */
public class ParticipantGroupManager extends BaseObject implements Manager, UserListener
{
	/**
	 * Create a new client manager.
	 */
	public ParticipantGroupManager()
	{
		super("ParticipantGroupManager");
	}

	/**
	 * Initialize the client manager.
	 */
	public void init()
	{
		Engine.instance().getEventRegistry().addListener("Plugin", this);
		Engine.instance().getEventRegistry().addListener("User", this);
		Engine.instance().getEventRegistry().addListener("objectcreated", this);
		Engine.instance().getEventRegistry().addListener("objectmodified", this);
		Engine.instance().getEventRegistry().addListener("objectremoved", this);

		Properties props = new Properties();

		props.put("type", "AktarioUserRegistry");
		props.put("id", new Long(11000));
		CommandTools.performSimple("persist.LoadObject", props);

		AktarioUserRegistry userRegistry = (AktarioUserRegistry) Engine.instance().getBaseRegistry().get(11000,
						"AktarioUserRegistry");

		Engine.instance().getEventRegistry().addListener("User", this);
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
				UserRegistry userRegistry = Server.instance().getUserRegistry();

				AktarioUserManager aktarioUserManager = (AktarioUserManager) Engine.instance().getManagerRegistry()
								.getManager("AktarioUserManager");

				User user = userRegistry.getUser(((AktarioUser) event.getCreatedObject()).getUserId());

				DataObject participantGroup = (DataObject) Engine.instance().getIObjectFactory().newInstance(
								"ParticipantGroup");

				participantGroup.setUniqueId(Engine.instance().getPersistentIDGenerator().createId());

				// 				participantGroup.setAttribute ("iritgoUserName", user.getName ());

				// 				if (aktarioUserManager.getUserByName (user.getName ()) != null)
				// 				{
				// 					participantState.setAttribute (
				// 						"aktarioUserName",
				// 						aktarioUserManager.getUserByName (user.getName ()).getFullName ());
				// 				}

				// 				participantState.setAttribute ("iritgoUserUniqueId", user.getUniqueId ());
				// 				participantState.setAttribute ("onlineUser", false);
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
			// 			try
			// 			{
			// 				UserRegistry userRegistry = Server.instance ().getUserRegistry ();

			// 				AktarioUserManager aktarioUserManager =
			// 					(AktarioUserManager) Engine.instance ().getManagerRegistry ().getManager (
			// 						"AktarioUserManager");

			// 				User user =
			// 					userRegistry.getUser (((AktarioUser) event.getModifiedObject ()).getUserId ());

			// 				DynDataObject participantState = null;

			// 				for (
			// 					Iterator i =
			// 						Engine.instance ().getBaseRegistry ().iterator ("ParticipantState");
			// 					i.hasNext ();)
			// 				{
			// 					participantState = (DynDataObject) i.next ();

			// 					if (
			// 						participantState.getStringAttribute ("iritgoUserName").equals (
			// 							user.getName ()))
			// 					{
			// 						break;
			// 					}
			// 				}

			// 				participantState.setAttribute ("iritgoUserName", user.getName ());

			// 				if (aktarioUserManager.getUserByName (user.getName ()) != null)
			// 				{
			// 					participantState.setAttribute (
			// 						"aktarioUserName",
			// 						aktarioUserManager.getUserByName (user.getName ()).getFullName ());
			// 				}

			// 				IObjectProxy proxy = (IObjectProxy) new FrameworkProxy();

			// 				proxy.setSampleRealObject ((IObject) participantState);
			// 				Engine.instance ().getBaseRegistry ().add ((BaseObject) participantState);
			// 				Engine.instance ().getProxyRegistry ().addProxy (
			// 					proxy, participantState.getTypeId ());

			// 				ActionTools.sendServerBroadcast (
			// 					new EditIObjectAction(EditIObjectAction.OK, participantState));
			// 			}
			// 			catch (Exception x)
			// 			{
			// 				Log.logFatal (
			// 					"system", "ParicipantServerManager:pluginEvent(Modify participants)",
			// 					"Can not modify participant error.");
			// 			}
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

				AktarioUserManager aktarioUserManager = (AktarioUserManager) Engine.instance().getManagerRegistry()
								.getManager("AktarioUserManager");

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

		long userUniqueId = user.getUniqueId();

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

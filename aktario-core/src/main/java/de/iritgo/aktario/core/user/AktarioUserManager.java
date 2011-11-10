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

package de.iritgo.aktario.core.user;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.IObjectCreatedEvent;
import de.iritgo.aktario.framework.base.IObjectDeletedEvent;
import de.iritgo.aktario.framework.base.IObjectModifiedEvent;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserListener;
import java.util.Properties;


/**
 * @version $Id: AktarioUserManager.java,v 1.22 2006/09/25 10:34:30 grappendorf Exp $
 */
public class AktarioUserManager extends BaseObject implements Manager, UserListener
{
	/** The user registry. */
	private AktarioUserRegistry userRegistry;

	/**
	 * Create a new AktarioUserManager.
	 */
	public AktarioUserManager()
	{
		super("AktarioUserManager");
	}

	/**
	 * Initialize the manager.
	 */
	public void init()
	{
		Properties props = new Properties();

		props.put("type", "AktarioUserRegistry");
		props.put("id", new Long(11000));
		CommandTools.performSimple("persist.LoadObject", props);

		userRegistry = (AktarioUserRegistry) Engine.instance().getBaseRegistry().get(11000, "AktarioUserRegistry");

		Engine.instance().getEventRegistry().addListener("User", this);
	}

	/**
	 * Unload the manager from the system.
	 */
	public void unload()
	{
	}

	/**
	 * This method is called when user has logged in.
	 *
	 * @param event The user event.
	 */
	public void userEvent(UserEvent event)
	{
	}

	/**
	 * Get the user registry.
	 *
	 * @return The user registry.
	 */
	public AktarioUserRegistry getUserRegistry()
	{
		return userRegistry;
	}

	public void addUser(AktarioUser aktarioUser)
	{
		long userUniqueId = Engine.instance().getPersistentIDGenerator().createId();

		aktarioUser.setUniqueId(userUniqueId);
		Engine.instance().getBaseRegistry().add(aktarioUser);
		userRegistry.addUser(aktarioUser);

		User user = new User();

		user.setName(aktarioUser.getName());
		user.setEmail(aktarioUser.getEmail());
		user.setPassword(aktarioUser.getPassword());
		user.setUniqueId(userUniqueId);

		AktarioUserPreferences aktarioUserPreferences = new AktarioUserPreferences();

		aktarioUserPreferences.setUniqueId(userUniqueId);
		//		aktarioUserPreferences.setColorScheme ("com.jgoodies.looks.plastic.theme.KDE");
		Engine.instance().getBaseRegistry().add(aktarioUserPreferences);

		AktarioUserProfile aktarioUserProfile = new AktarioUserProfile();

		aktarioUserProfile.setUniqueId(userUniqueId);
		aktarioUserProfile.addPreferences(aktarioUserPreferences);
		userRegistry.addProfile(aktarioUserProfile);

		//TODO: old delete this
		// 		user.putNamedIritgoObject ("AktarioUser", aktarioUser);
		// 		user.putNamedIritgoObject ("AktarioUserPreferences", aktarioUserPreferences);
		// 		user.putNamedIritgoObject ("AktarioUserProfile", aktarioUserProfile);
		Server.instance().getUserRegistry().addUser(user);

		Properties props = new Properties();

		props.put("id", new Long(user.getUniqueId()));
		CommandTools.performSimple("persist.StoreUser", props);

		Engine.instance().getEventRegistry().fire("objectcreated",
						new IObjectCreatedEvent(aktarioUser, userRegistry, "users", null));
		Engine.instance().getEventRegistry().fire("objectcreated",
						new IObjectCreatedEvent(aktarioUserProfile, userRegistry, "profiles", null));
		Engine.instance().getEventRegistry().fire("objectcreated",
						new IObjectCreatedEvent(aktarioUserPreferences, aktarioUserProfile, "preferences", null));
	}

	public void delUser(AktarioUser aktarioUser)
	{
		User user = Server.instance().getUserRegistry().getUser(aktarioUser.getUserId());

		if (user.isOnline())
		{
			ActionTools.sendToClient(user, new AktarioUserDeletedResponse());
		}

		Properties props = new Properties();

		props.put("id", new Long(user.getUniqueId()));
		CommandTools.performSimple("persist.DeleteUser", props);

		Engine.instance().getEventRegistry().fire("objectremoved",
						new IObjectDeletedEvent(aktarioUser, userRegistry, "users", null));

		userRegistry.getIObjectListAttribute("users").removeIObject(aktarioUser);
		Engine.instance().getBaseRegistry().remove(aktarioUser);
		Server.instance().getUserRegistry().removeUser(user);
	}

	public void modifyUser(AktarioUser aktarioUser)
	{
		User user = Server.instance().getUserRegistry().getUser(aktarioUser.getUserId());

		user.setName(aktarioUser.getName());
		user.setEmail(aktarioUser.getEmail());
		user.setPassword(aktarioUser.getPassword());

		Properties props = new Properties();

		props.put("id", new Long(user.getUniqueId()));
		CommandTools.performSimple("persist.StoreUser", props);
		Engine.instance().getEventRegistry().fire("objectmodified", new IObjectModifiedEvent(aktarioUser));
	}

	public void modifyUser(AktarioUser aktarioUser, String userName)
	{
		User user = Server.instance().getUserRegistry().getUser(userName);

		if (user == null)
		{
			return;
		}

		Server.instance().getUserRegistry().removeUser(user);
		user.setName(aktarioUser.getName());
		user.setEmail(aktarioUser.getEmail());
		user.setPassword(aktarioUser.getPassword());
		Server.instance().getUserRegistry().addUser(user);

		Properties props = new Properties();

		props.put("id", new Long(user.getUniqueId()));
		CommandTools.performSimple("persist.StoreUser", props);
		Engine.instance().getEventRegistry().fire("objectmodified", new IObjectModifiedEvent(user));
		Engine.instance().getEventRegistry().fire("objectmodified", new IObjectModifiedEvent(aktarioUser));
	}

	/**
	 * Get the user by name.
	 *
	 * @return AktarioUser.
	 */
	public AktarioUser getUserByName(String name)
	{
		return userRegistry.getUserByName(name);
	}
}

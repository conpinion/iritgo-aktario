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

package de.iritgo.aktario.client;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.IObjectProxyEvent;
import de.iritgo.aktario.core.iobject.IObjectProxyListener;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.core.plugin.PluginEventListener;
import de.iritgo.aktario.core.plugin.PluginStateEvent;
import de.iritgo.aktario.core.user.AktarioUser;
import de.iritgo.aktario.core.user.AktarioUserPreferences;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.dataobject.DataObjectTools;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserListener;
import java.util.Properties;


/**
 * AktarioClientManager.
 *
 * @version $Id: AktarioClientManager.java,v 1.21 2006/09/25 10:34:32 grappendorf Exp $
 */
public class AktarioClientManager extends BaseObject implements Manager, UserListener, PluginEventListener
{
	/** The current user. */
	AktarioUser aktarioUser;

	/** True if we are in the startup phase. */
	protected boolean startup;

	/**
	 * Create a new client manager.
	 */
	public AktarioClientManager ()
	{
		super ("aktarioclient");
		startup = true;
	}

	/**
	 * Initialize the client manager.
	 */
	public void init ()
	{
		Engine.instance ().getEventRegistry ().addListener ("User", this);
		Client.instance ().createDefaultNetworkProcessingSystem ();
		Engine.instance ().getEventRegistry ().addListener ("Plugin", this);
	}

	/**
	 * Free all client manager resources.
	 */
	public void unload ()
	{
	}

	/**
	 * Get the current user.
	 *
	 * @return The current user.
	 */
	public AktarioUser getUser ()
	{
		return aktarioUser;
	}

	/**
	 * This will called if the user is loggedin
	 *
	 * This method triggers the loading of the current aktario user.
	 *
	 * @param userEvent The userEvent.
	 */
	public void userEvent (UserEvent event)
	{
		if ((event != null) && (event.isLoggedIn ()))
		{
			aktarioUser = (AktarioUser) DataObjectTools.registerDataObject ("AktarioUser", event.getUser ()
							.getUniqueId (), new IObjectProxyListener ()
			{
				public void proxyEvent (IObjectProxyEvent event)
				{
					if (! event.isWaitingForNewObject ())
					{
						AktarioUser aktarioUser = (AktarioUser) event.getObject ();

						Properties props = new Properties ();

						props = new Properties ();
						props.put ("enabled", new Boolean (aktarioUser.getRole () == AktarioUser.ROLE_ADMIN));
						CommandTools.performAsync ("EnableAdminFunctions", props);
					}
				}
			});

			@SuppressWarnings("unused")
			AktarioUserPreferences preferences = (AktarioUserPreferences) DataObjectTools.registerDataObject (
							"AktarioUserPreferences", event.getUser ().getUniqueId (), new IObjectProxyListener ()
							{
								public void proxyEvent (IObjectProxyEvent event)
								{
									if (! event.isWaitingForNewObject ())
									{
										AktarioUserPreferences preferences = (AktarioUserPreferences) event
														.getObject ();
										Properties props = new Properties ();

										props.put ("preferences", preferences);
										CommandTools.performAsync ("ApplyPreferences", props);

										if (startup)
										{
											startup (aktarioUser);
											startup = false;
										}
									}
								}
							});
		}

		if ((event != null) && (! event.isLoggedIn ()))
		{
			startup = true;
		}
	}

	/**
	 * Called after we have received the user object to fully initialize the
	 * client gui.
	 *
	 * @param user The user who has logged in.
	 */
	private void startup (AktarioUser user)
	{
		Properties props = new Properties ();

		props.put ("user", user);

		if (CommandTools.commandExists ("Startup"))
		{
			CommandTools.performAsync ("Startup", props);
		}
		else
		{
			CommandTools.performAsync ("DefaultStartup", props);
		}
	}

	public void pluginEvent (PluginStateEvent event)
	{
		if (event.allPluginsInitialized ())
		{
			Properties props = new Properties ();

			props.setProperty ("command", "test.aktera-client.loginCommand");
			props.setProperty ("name", "login");

			CommandTools.performSimple ("aktario-xmlrpc.AddXmlRpcCommand", props);

			props.setProperty ("command", "test.aktera-client.logoffCommand");
			props.setProperty ("name", "logoff");

			CommandTools.performSimple ("aktario-xmlrpc.AddXmlRpcCommand", props);
		}
	}
}

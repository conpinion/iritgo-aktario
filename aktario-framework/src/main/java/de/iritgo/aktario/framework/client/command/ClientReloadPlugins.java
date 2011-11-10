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

package de.iritgo.aktario.framework.client.command;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.gui.IDesktopManager;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.plugin.PluginManager;
import de.iritgo.aktario.framework.base.InitIritgoException;
import de.iritgo.aktario.framework.base.shutdown.ShutdownManager;
import de.iritgo.aktario.framework.client.Client;


/**
 *
 */
public class ClientReloadPlugins extends Command implements Runnable
{
	public ClientReloadPlugins()
	{
	}

	/**
	 */
	public void perform()
	{
		new Thread(this).start();
	}

	/**
	 * ConnectToServer
	 */
	public void run()
	{
		IDesktopManager desktopManager = Client.instance().getClientGUI().getDesktopManager();

		desktopManager.saveVisibleDisplays();
		desktopManager.closeAllDisplays();

		ShutdownManager shutdownManager = (ShutdownManager) Engine.instance().getManagerRegistry().getManager(
						"shutdown");

		shutdownManager.shutdown();

		try
		{
			Client.instance().stopGUI();
		}
		catch (InitIritgoException x)
		{
			Log.logFatal("ClientReloadPlugin", "preform/run", "Can not close the GUI!");

			// TODO: Save hold...
		}

		reloadPlugins();

		try
		{
			Client.instance().initGUI();
		}
		catch (InitIritgoException x)
		{
			Log.logFatal("ClientReloadPlugin", "preform/run", "Can not init and start the GUI!");

			// TODO: Save hold...
		}

		desktopManager.showSavedDisplays();
	}

	private void reloadPlugins()
	{
		Engine engine = Engine.instance();
		PluginManager pluginManager = engine.getPluginManager();

		pluginManager.unloadPlugins();

		Engine.instance().getBaseRegistry().clear();
		Engine.instance().getProxyRegistry().clear();

		pluginManager.loadPlugins();

		pluginManager.initPlugins(null);
	}
}

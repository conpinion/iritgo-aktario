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

package de.iritgo.aktario.framework.server.command;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.core.plugin.PluginManager;
import de.iritgo.aktario.framework.base.shutdown.ShutdownManager;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


/**
 *
 */
public class ReloadPlugins extends Command implements Runnable
{
	// Helper Class
	public class DisplayItem
	{
		public String displayId;

		public String guiPaneId;

		public IObjectProxy businessProxy;

		public DisplayItem (String displayId, String guiPaneId, IObjectProxy businessProxy)
		{
			this.displayId = displayId;
			this.guiPaneId = guiPaneId;
			this.businessProxy = businessProxy;
		}
	}

	private List visibleWindows;

	public ReloadPlugins ()
	{
		visibleWindows = new LinkedList ();
	}

	/**
	 * Set the command Properties
	 *
	 * @param properties The properties.
	 */
	public void setProperties (Properties properties)
	{
	}

	/**
	 * A dirty hack, but commands a in sync.!
	 */
	public void perform ()
	{
		new Thread (this).start ();
	}

	/**
	 * reloadplugins
	 */
	public void run ()
	{
		reloadPlugins ();
	}

	private void reloadPlugins ()
	{
		Engine engine = Engine.instance ();

		ShutdownManager shutdownManager = (ShutdownManager) Engine.instance ().getManagerRegistry ().getManager (
						"shutdown");

		shutdownManager.shutdown ();

		PluginManager pluginManager = engine.getPluginManager ();

		pluginManager.unloadPlugins ();

		Engine.instance ().getBaseRegistry ().clear ();
		Engine.instance ().getProxyRegistry ().clear ();
		Engine.instance ().getEventRegistry ().clear ();
		Engine.instance ().getProxyEventRegistry ().clear ();

		pluginManager.loadPlugins ();

		pluginManager.initPlugins (null);
	}
}
